package io.pig.lkong.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.DynamicLayout
import android.text.Layout
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.StaticLayout
import android.text.TextPaint
import android.text.style.AbsoluteSizeSpan
import android.text.style.DynamicDrawableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.URLSpan
import android.util.DisplayMetrics
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.PostDisplayModel
import io.pig.lkong.model.PostModel
import io.pig.lkong.ui.adapter.item.PostViewHolder
import io.pig.lkong.ui.adapter.listener.OnPostButtonClickListener
import io.pig.lkong.util.DateUtil
import io.pig.lkong.util.ImageLoaderUtil
import io.pig.lkong.util.SlateUtil
import io.pig.lkong.util.TextSizeUtil
import io.pig.lkong.util.ThemeUtil
import io.pig.lkong.util.UiUtil
import io.pig.ui.html.EmptyImageGetter
import io.pig.ui.html.HtmlTagHandler
import io.pig.ui.html.HtmlUtil
import io.pig.widget.adapter.FixedViewAdapter
import io.pig.widget.html.ClickableImageSpan
import io.pig.widget.html.EmojiSpan
import java.util.*

/**
 * @author yinhang
 * @since 2021/6/28
 */
class PostListAdapter(
    private val context: Context,
    private val userId: Long,
    private val authorId: Long,
    private val display: Display,
    private val listener: OnPostButtonClickListener,
    imageDownloadPolicy: Int,
    themeKey: String,
    postList: List<PostModel>
) : FixedViewAdapter<PostModel>(postList) {

    private val avatarSize = UiUtil.getDefaultAvatarSize(context)
    private val todayPrefix = context.getString(R.string.text_datetime_today)
    private val textColorSecondary = ThemeUtil.textColorSecondary(context, themeKey)
    private val authorTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val contentTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val accentColor = ThemeUtil.accentColor(context, themeKey)
    private val datelineTextSize = UiUtil.getSpDimensionPixelSize(context, R.dimen.text_size_body1)
    private val imageGetter = EmptyImageGetter()
    private val tagHandler = HtmlTagHandler()
    private val loadingDrawable =
        ResourcesCompat.getDrawable(context.resources, R.drawable.placeholder_loading, null)!!
    private val showImageValue = ImageLoaderUtil.shouldDownloadImage(imageDownloadPolicy)

    init {
        val textColorPrimary = ThemeUtil.textColorPrimary(context, themeKey)
        val contentTextSize =
            TextSizeUtil.textSizeForMode(context, themeKey, TextSizeUtil.TEXT_SIZE_BODY)
        val accentColor = ThemeUtil.accentColor(context, themeKey)
        contentTextPaint.textSize = contentTextSize.toFloat()
        contentTextPaint.color = textColorPrimary
        contentTextPaint.linkColor = accentColor
        val authorTextSize = UiUtil.getSpDimensionPixelSize(context, R.dimen.text_size_subhead)
        authorTextPaint.textSize = authorTextSize
        authorTextPaint.color = textColorPrimary
        authorTextPaint.linkColor = accentColor
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)

        return PostViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val viewHolder = holder as PostViewHolder
        val post = getItem(position)
        if (post.rateSum != 0) {
            viewHolder.rateText.apply {
                visibility = View.VISIBLE
                text = context.getString(R.string.format_post_rate_summary, post.rateSum)
                setOnClickListener {
                    listener.onRateTextClick(viewHolder.itemView, post.rates)
                }
            }
        }
        if (post.authorId == userId) {
            viewHolder.editButton.visibility = View.VISIBLE
        }
        ImageLoaderUtil.loadLkongAvatar(
            context,
            viewHolder.avatarImage,
            post.authorId,
            post.authorAvatar,
            avatarSize
        )

        val displayModel = createSpan(post)

        // init post item
        viewHolder.postItem.apply {
            postId = post.pid
            identityTag = post.pid
            showImages = showImageValue
            ordinalText = post.ordinal.toString()
            setPostDisplayCache(displayModel)
        }

        // add listener
        viewHolder.avatarImage.setOnClickListener {
            listener.onProfileImageClick(viewHolder.itemView, post.authorId)
        }
        viewHolder.postItem.apply {
            isLongClickable = true
            setOnLongClickListener {
                listener.onPostTextLongClick(viewHolder.itemView, post)
                return@setOnLongClickListener false
            }
        }
    }

    private fun createSpan(postModel: PostModel): PostDisplayModel {
        val spannedText = HtmlUtil.htmlToSpanned(
            SlateUtil.slateToText(postModel.message ?: ""), imageGetter, tagHandler
        )
        return replaceImageSpan(SpannableString(spannedText), postModel, loadingDrawable)
    }

    private fun replaceImageSpan(
        sequence: CharSequence,
        postModel: PostModel,
        initPlaceHolder: Drawable
    ): PostDisplayModel {
        val spannable: SpannableStringBuilder =
            if (sequence is SpannableStringBuilder) {
                sequence
            } else {
                SpannableStringBuilder(sequence)
            }
        val imageSpans = spannable.getSpans(
            0, sequence.length,
            ImageSpan::class.java
        )
        val urlSpans = spannable.getSpans(0, sequence.length, URLSpan::class.java)
        val importantSpanList = mutableListOf<Any>()
        val imageUrlList = mutableListOf<String>()
        val emoticonSpanList = mutableListOf<Any>()
        importantSpanList.addAll(listOf(*urlSpans))
        for (imageSpan in imageSpans) {
            val spanStart = spannable.getSpanStart(imageSpan)
            val spanEnd = spannable.getSpanEnd(imageSpan)
            val spanFlags = spannable.getSpanFlags(imageSpan)
            imageSpan.source.apply {
                if (!isNullOrEmpty()) {
                    if (!contains("https://avatar.lkong.com/bq")) {
                        spannable.removeSpan(imageSpan)
                        val clickableImageSpan = ClickableImageSpan(
                            context,
                            null,
                            postModel.pid,
                            POST_PICASSO_TAG,
                            this,
                            R.drawable.placeholder_loading,
                            R.drawable.placeholder_error,
                            256,
                            256,
                            DynamicDrawableSpan.ALIGN_BASELINE,
                            initPlaceHolder
                        )
                        spannable.setSpan(
                            clickableImageSpan,
                            spanStart,
                            spanEnd,
                            spanFlags
                        )
                        importantSpanList.add(clickableImageSpan)
                        imageUrlList.add(this)
                    } else if (contains("https://avatar.lkong.com/bq")) {
                        spannable.removeSpan(imageSpan)
                        val emoticonImageSpan = EmojiSpan(
                            context,
                            this,
                            (contentTextPaint.textSize * 2).toInt(),
                            ImageSpan.ALIGN_BASELINE,
                            contentTextPaint.textSize.toInt()
                        )
                        spannable.setSpan(
                            emoticonImageSpan,
                            spanStart,
                            spanEnd,
                            spanFlags
                        )
                        emoticonSpanList.add(emoticonImageSpan)
                    }
                }
            }
        }
        // Generate content StaticLayout
        val dm = DisplayMetrics()
        display.getMetrics(dm)
        val padding = UiUtil.getCardViewPadding(
            context.resources.getDimensionPixelSize(R.dimen.default_card_elevation),
            (2.0 * dm.density).toInt()
        )
        val contentWidth: Int =
            dm.widthPixels - UiUtil.dp2px(context, 16f) * 2 - padding.left - padding.right
        val textLayout = DynamicLayout(
            spannable,
            contentTextPaint,
            contentWidth,
            Layout.Alignment.ALIGN_NORMAL,
            1.3f,
            0.0f,
            false
        )
        // Generate author StaticLayout
        val authorNameSpannable = SpannableStringBuilder()
        authorNameSpannable.append(postModel.authorName)
        if (postModel.authorId == authorId) {
            val threadAuthorIndicator: String = context.getString(R.string.indicator_thread_author)
            authorNameSpannable.append(threadAuthorIndicator)
            authorNameSpannable.setSpan(
                ForegroundColorSpan(accentColor),
                postModel.authorName.length,
                postModel.authorName.length + threadAuthorIndicator.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
        authorNameSpannable.append('\n')
        val datelineString: String = DateUtil.formatDateByToday(postModel.dateline, todayPrefix)
        val start = authorNameSpannable.length
        val end = authorNameSpannable.length + datelineString.length
        authorNameSpannable.append(datelineString)
        authorNameSpannable.setSpan(
            ForegroundColorSpan(textColorSecondary),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        authorNameSpannable.setSpan(
            AbsoluteSizeSpan(datelineTextSize.toInt()),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val authorWidth = dm.widthPixels - UiUtil.dp2px(context, 72f) - padding.left - padding.right
        val authorLayout = StaticLayout(
            authorNameSpannable,
            authorTextPaint,
            authorWidth,
            Layout.Alignment.ALIGN_NORMAL,
            1.0f,
            0.0f,
            false
        )
        return PostDisplayModel(
            authorLayout = authorLayout,
            textLayout = textLayout,
            spannableStringBuilder = spannable,
            urlSpanCount = urlSpans.size,
            importantSpans = importantSpanList,
            emoticonSpans = emoticonSpanList
        )
    }

    companion object {
        const val POST_PICASSO_TAG = "picasso_post_list_adapter"
    }
}