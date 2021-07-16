package io.pig.lkong.model

import io.pig.lkong.http.data.resp.UserProfileResp
import io.pig.lkong.util.LkongUtil

/**
 * @author yinhang
 * @since 2021/7/16
 */
class UserModel(userProfileResp: UserProfileResp) {

    val uid: Long
    val name: String
    val avatar: String?
    val verify: String?
    val status: String?
    val followings: Long
    val followers: Long
    val posts: Long
    val threads: Long
    val money: Long
    val diamond: Long
    val vertyInfo: String?
    val level: String
    val isPunch: Boolean
    val punchAllDay: Int
    val punchDay: Int
    val punchHighestDay: Int

    init {
        this.uid = userProfileResp.user.uid
        this.name = userProfileResp.user.name
        this.avatar = userProfileResp.user.avatar ?: LkongUtil.generateAvatarUrl(uid)
        this.verify = userProfileResp.user.verify
        this.status = userProfileResp.user.status
        // other
        this.followings = userProfileResp.userCount.followings
        this.followers = userProfileResp.userCount.followers
        this.posts = userProfileResp.userCount.posts
        this.threads = userProfileResp.userCount.threads
        this.money = userProfileResp.userCount.money
        this.diamond = userProfileResp.userCount.longjing
        this.vertyInfo = userProfileResp.userCount.vertyInfo
        this.level = userProfileResp.userCount.level
        this.isPunch = userProfileResp.userPunch.isPunch
        this.punchAllDay = userProfileResp.userPunch.punchallday
        this.punchDay = userProfileResp.userPunch.punchday
        this.punchHighestDay = userProfileResp.userPunch.punchhighestday
    }
}