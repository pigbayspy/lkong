package io.pig.lkong.http.data.resp

import io.pig.lkong.http.data.resp.data.UserCountRespData
import io.pig.lkong.http.data.resp.data.UserPunchRespData
import io.pig.lkong.http.data.resp.data.UserRespData

/**
 * @author yinhang
 * @since 2021/7/16
 */
class UserProfileResp(
    val user: UserRespData,
    val userCount: UserCountRespData,
    val userPunch: UserPunchRespData
)