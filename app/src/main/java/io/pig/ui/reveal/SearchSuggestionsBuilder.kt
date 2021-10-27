package io.pig.ui.reveal

interface SearchSuggestionsBuilder {
    fun buildEmptySearchSuggestion(maxCount: Int): Collection<SearchItem>

    fun buildSearchSuggestion(maxCount: Int, query: String): Collection<SearchItem>
}