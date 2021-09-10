package io.pig.lkong.http.data.resp

import io.pig.lkong.http.data.resp.data.CollectionData

data class CollectionResp(
    val userFavoriteGroupList: List<CollectionData>
)