package io.pig.common.ui.slate

/**
 * @author yinhang
 * @since 2021/8/18
 */
class SlateNode(
    val type: String? = null,
    val text: String? = "",
    val id: Int? = null,
    val bold: Boolean? = false,
    val url: String? = null,
    val children: List<SlateNode>? = null
) {

    override fun toString(): String {
        if (type.isNullOrBlank()) {
            // 默认类型为 "text"
            if (bold == true) {
                return "<strong>${text}</strong>"
            }
            return text ?: ""
        }
        val child = buildString {
            children?.forEach {
                append(it.toString())
            }
        }
        return when (type) {
            "quote" -> "<blockquote><p>${child}</p></blockquote>"
            "paragraph" -> "<p>${child}</p>"
            "link" -> "<a href=\"${url}\">${children}</a>"
            "emotion" -> "<span><img src=\"https://avatar.lkong.com/bq/em${id}.gif\"></span>"
            else -> child
        }
    }
}