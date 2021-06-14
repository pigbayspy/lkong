package io.pig.lkong.util.transform

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * @author yinhang
 * @since 2021/6/14
 */
class CircleTransform: BitmapTransformation() {

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
       return TransformUtil.circleCrop(pool, toTransform)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        TODO("Not yet implemented")
    }

}