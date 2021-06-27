package io.pig.lkong.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import io.pig.lkong.R

/**
 * @author yinhang
 * @since 2021/6/13
 */
object ImageLoaderUtil {

    fun loadAvatar(context: Context, target: ImageView, avatarUrl: String) {
        Glide.with(context)
            .load(toSmallAvatar(avatarUrl))
            .error(R.drawable.ic_placeholder_avatar)
            .placeholder(R.drawable.ic_placeholder_avatar)
            .circleCrop()
            .into(target)
    }

    fun loadAvatar(
        context: Context, target: ImageView, avatarUrl: String, avatarSize: Int
    ) {
        Glide.with(context)
            .load(toSmallAvatar(avatarUrl))
            .error(R.drawable.ic_placeholder_avatar)
            .placeholder(R.drawable.ic_placeholder_avatar)
            .override(avatarSize, avatarSize)
            .circleCrop()
            .into(target)
    }

    private fun toSmallAvatar(url: String): String {
        return if (!NetworkUtil.isWifiConnect())
            url.replace(
                "middle",
                "small"
            ) else url
    }
}