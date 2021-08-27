package io.pig.lkong.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import io.pig.lkong.R

/**
 * @author yinhang
 * @since 2021/6/13
 */
object ImageLoaderUtil {

    const val IMAGE_LOAD_ALWAYS = 0
    const val IMAGE_LOAD_NEVER = 1
    const val IMAGE_LOAD_ONLY_WIFI = 2

    fun loadLkongAvatar(
        context: Context, target: ImageView, uid: Long, avatar: String?, avatarSize: Int
    ) {
        if (avatar.isNullOrEmpty()) {
            Glide.with(context)
                .load(R.drawable.ic_placeholder_avatar)
                .circleCrop()
                .into(target)
            return
        }
        val avatarUrl = LkongUtil.generateAvatarUrl(uid, avatar)
        val glideHeader = LazyHeaders.Builder()
            .addHeader("Referer", "https://www.lkong.com/").build()
        val avatarGlideUrl = GlideUrl(avatarUrl, glideHeader)
        Glide.with(context)
            .load(avatarGlideUrl)
            .error(R.drawable.ic_placeholder_avatar)
            .placeholder(R.drawable.ic_placeholder_avatar)
            .override(avatarSize, avatarSize)
            .circleCrop()
            .into(target)
    }

    fun loadLkongAvatar(
        context: Context, target: ImageView, uid: Long, avatar: String
    ) {
        val avatarUrl = LkongUtil.generateAvatarUrl(uid, avatar)
        val glideHeader = LazyHeaders.Builder()
            .addHeader("Referer", "https://www.lkong.com/").build()
        val avatarGlideUrl = GlideUrl(avatarUrl, glideHeader)
        Glide.with(context)
            .load(avatarGlideUrl)
            .error(R.drawable.ic_placeholder_avatar)
            .placeholder(R.drawable.ic_placeholder_avatar)
            .circleCrop()
            .into(target)
    }

    fun loadForumAvatar(context: Context, target: ImageView, fid: Long, avatar: String) {
        val avatarUrl = LkongUtil.generateForumAvatarUrl(fid, avatar)
        val glideHeader = LazyHeaders.Builder()
            .addHeader("Referer", "https://www.lkong.com/").build()
        val avatarGlideUrl = GlideUrl(avatarUrl, glideHeader)
        Glide.with(context)
            .load(avatarGlideUrl)
            .placeholder(R.drawable.ic_forum_loading)
            .error(R.drawable.ic_forum_error)
            .into(target)
    }

    fun loadForumIcon(context: Context, target: ImageView, iconUrl: String) {
        val glideHeader = LazyHeaders.Builder()
            .addHeader("Referer", "https://www.lkong.com/").build()
        val avatarGlideUrl = GlideUrl(iconUrl, glideHeader)
        Glide.with(context)
            .load(avatarGlideUrl)
            .placeholder(R.drawable.ic_forum_loading)
            .error(R.drawable.ic_forum_error)
            .into(target)
    }

    fun shouldDownloadImage(policy: Int): Boolean {
        val wifiAvailability: Boolean = NetworkUtil.isWifiConnect()
        return when (policy) {
            IMAGE_LOAD_ONLY_WIFI -> wifiAvailability
            IMAGE_LOAD_NEVER -> false
            IMAGE_LOAD_ALWAYS -> true
            else -> true
        }
    }
}