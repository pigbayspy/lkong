package io.pig.ui.reveal

enum class DisplayMode(private val mode: Int) {
    MENUITEM(0),
    TOOLBAR(1);

    fun toInt(): Int {
        return mode
    }

    companion object {
        fun fromInt(mode: Int): DisplayMode {
            for (enumMode in values()) {
                if (enumMode.mode == mode) {
                    return enumMode
                }
            }
            throw IllegalArgumentException()
        }
    }
}