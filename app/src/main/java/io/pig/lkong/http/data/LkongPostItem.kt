package io.pig.lkong.http.data

/**
 * @author yinhang
 * @since 2021/6/27
 */
data class LkongPostItem(
    val fid: Long,
    val sortkey: Long,
    val dateline: String,
    val message: String,
    val author: String,
    val authorid: Long,
    val favorite: Boolean,
    val isme: Int,
    val notgroup: Int,
    val pid: String,
    val first: Int,
    val status: Int,
    val id: String,
    val tsadmin: Boolean,
    val isadmin: Int,
    val lou: Int,
    val tid: Long,
    val ratelog: List<LkongPostRateItem>,
    val alluser: LkongPostUser
)