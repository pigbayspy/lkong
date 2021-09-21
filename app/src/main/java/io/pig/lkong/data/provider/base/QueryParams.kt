package io.pig.lkong.data.provider.base

data class QueryParams(
    var table: String = "",
    var tablesWithJoins: String = "",
    var idColumn: String = "",
    var selection: String = "",
    var orderBy: String = ""
)