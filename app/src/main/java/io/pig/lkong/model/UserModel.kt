package io.pig.lkong.model

import io.pig.lkong.http.data.resp.UserProfileResp

/**
 * @author yinhang
 * @since 2021/7/16
 */
class UserModel(userProfileResp: UserProfileResp) {

    val uid: Long
    val name: String
    val avatar: String
    val verify: String?
    val status: String?
    val followers: Long
    val fans: Long
    val posts: Long
    val threads: Long
    val money: Long
    val diamond: Long
    val verityInfo: String?
    val level: String
    val isPunch: Boolean
    val punchAllDay: Int
    val punchDay: Int
    val punchHighestDay: Int
    val dateline: Long

    init {
        this.uid = userProfileResp.user.uid
        this.name = userProfileResp.user.name
        this.avatar = userProfileResp.user.avatar
        this.verify = userProfileResp.user.verify
        this.status = userProfileResp.user.status
        // other
        this.followers = userProfileResp.userCount.followings
        this.fans = userProfileResp.userCount.followers
        this.posts = userProfileResp.userCount.posts
        this.threads = userProfileResp.userCount.threads
        this.money = userProfileResp.userCount.money
        this.diamond = userProfileResp.userCount.longjing
        this.verityInfo = userProfileResp.userCount.vertyInfo
        this.level = userProfileResp.userCount.level
        this.isPunch = userProfileResp.userPunch.isPunch
        this.punchAllDay = userProfileResp.userPunch.punchallday
        this.punchDay = userProfileResp.userPunch.punchday
        this.punchHighestDay = userProfileResp.userPunch.punchhighestday
        this.dateline = userProfileResp.user.dateline
    }
}