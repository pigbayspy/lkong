package io.pig.lkong.account

import java.io.Serializable

/**
 * 用户账户数据
 *
 * @author yinhang
 * @since 2021/5/16
 */
class UserAccount(
    val userId: Long,
    val userName: String,
    val userEmail: String,
    val userAvatar: String
) : Serializable {


}