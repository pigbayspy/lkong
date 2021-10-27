package io.pig.ui.reveal

interface SearchListener {

    /**
     * Called when a suggestion is pressed is pressed
     */
    fun onSuggestion(searchItem: SearchItem): Boolean

    /**
     * Called when the clear button is pressed
     */
    fun onSearchCleared()

    /**
     * Called when the PersistentSearchView's EditText text changes
     */
    fun onSearchTermChanged(term: String)

    /**
     * Called when search happens
     *
     * @param query search string
     */
    fun onSearch(query: String)

    /**
     * Called when search state change to SEARCH and EditText, Suggestions visible
     */
    fun onSearchEditOpened()

    /**
     * Called when search state change from SEARCH and EditText, Suggestions gone
     */
    fun onSearchEditClosed()

    /**
     * Called when edit text get focus and backpressed
     */
    fun onSearchEditBackPressed(): Boolean

    /**
     * Called when search back to start state.
     */
    fun onSearchExit()
}