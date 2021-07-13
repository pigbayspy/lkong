package io.pig.lkong.http.data.resp

import io.pig.lkong.http.data.resp.data.HotThreadRespData

data class HotThreadReq(
    val hots: List<HotThreadRespData>
)