package io.pig.ui.reveal

enum class SearchViewState(private val state: Int) {
    NORMAL(0),
    EDITING(1),
    SEARCH(2);

    fun toInt(): Int {
        return state
    }

    companion object {
        fun fromInt(state: Int): SearchViewState {
            for (enumState in values()) {
                if (enumState.state == state) {
                    return enumState
                }
            }
            throw IllegalArgumentException()
        }
    }
}