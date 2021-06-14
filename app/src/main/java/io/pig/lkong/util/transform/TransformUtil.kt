package io.pig.lkong.util.transform

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool

/**
 * @author yinhang
 * @since 2021/6/14
 */
object TransformUtil {

    fun circleCrop(pool: BitmapPool, source: Bitmap): Bitmap {
        val size = minOf(source.height, source.width)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        // TODO this could be acquired from the pool too
        val squared = Bitmap.createBitmap(source, x, y, size, size)
        var result: Bitmap? = pool[size, size, Bitmap.Config.ARGB_8888]
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(result!!)
        val paint = Paint()
        paint.shader = BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)
        return result
    }
}