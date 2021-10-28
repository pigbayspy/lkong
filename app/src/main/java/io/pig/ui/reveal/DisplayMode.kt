package io.pig.ui.reveal

enum class DisplayMode(val value: Int) {
    MENUITEM(0),
    TOOLBAR(1);

    companion object {
        fun fromInt(mode: Int): DisplayMode {
            for (enumMode in values()) {
                if (enumMode.value == mode) {
                    return enumMode
                }
            }
            throw IllegalArgumentException()
        }
    }
}