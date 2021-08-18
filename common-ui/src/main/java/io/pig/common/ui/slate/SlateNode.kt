package io.pig.common.ui.slate

/**
 * @author yinhang
 * @since 2021/8/18
 */
class SlateNode(
    val type: String? = null,
    val text: String? = null,
    val id: Int? = null,
    val url: String? = null,
    val children: List<SlateNode>? = null
) {

    override fun toString(): String {
        if (type.isNullOrEmpty()) {
            return text ?: ""
        }
        val sb = StringBuilder()
        children?.forEach {
            sb.append(it.toString())
        }
        val child = sb.toString()
        return when (type) {
            "quote" -> "<blockquote><p>${child}</p></blockquote>"
            "paragraph" -> "<p>${child}</p>"
            "link" -> "<a href=\"${url}\">${children}</a>"
            "emotion" -> "<span><img src=\"https://avatar.lkong.com/bq/em${id}.gif\"></span>"
            else -> child
        }
    }
}