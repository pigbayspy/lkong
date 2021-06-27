package io.pig.lkong.model

import android.os.Parcel
import android.os.Parcelable
import io.pig.lkong.account.util.AccountUtil
import io.pig.lkong.http.data.LkongForumItemResp
import io.pig.lkong.ui.adapter.base.BaseCollectionItem
import io.pig.lkong.util.DateUtil
import io.pig.lkong.util.HtmlUtil
import java.util.*

/**
 * 帖子模型
 *
 * @author yinhang
 * @since 2021/6/8
 */
class ThreadModel : BaseCollectionItem {
    private val sortKey: Long
    private val sortKeyTime: Date?

    val userName: String
    val userIcon: String
    val userId: Long

    val dateline: Date?
    val subject: String
    val digest: Boolean
    val closed: Int
    val replyCount: Int
    val id: String
    val fid: Long

    constructor(itemResp: LkongForumItemResp) {
        this.sortKey = itemResp.sortkey
        this.sortKeyTime = Date(this.sortKey * 1000L)
        // 用户信息
        this.userName = itemResp.username
        this.userIcon = AccountUtil.generateAvatarUrl(itemResp.uid)
        this.userId = itemResp.uid
        this.closed = itemResp.closed
        this.dateline = DateUtil.parse(itemResp.dateline)
        this.digest = itemResp.digest > 0
        this.fid = itemResp.fid
        this.id = itemResp.id
        this.replyCount = itemResp.replynum
        this.subject = HtmlUtil.htmlToPlain(itemResp.subject)
    }

    override fun getSortKey(): Long {
        return this.sortKey
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(sortKey)
        dest.writeLong(sortKeyTime?.time ?: -1)
        dest.writeLong(dateline?.time ?: -1)
        dest.writeString(subject)
        dest.writeString(userName)
        dest.writeByte(if (digest) 1.toByte() else 0.toByte())
        dest.writeInt(closed)
        dest.writeLong(this.userId)
        dest.writeInt(replyCount)
        dest.writeString(id)
        dest.writeLong(fid)
        dest.writeString(userIcon)
    }

    private constructor(parcel: Parcel) {
        this.sortKey = parcel.readLong()
        val tmpSortKeyTime: Long = parcel.readLong()
        this.sortKeyTime = if (tmpSortKeyTime == -1L) null else Date(tmpSortKeyTime)
        val tmpDateline: Long = parcel.readLong()
        this.dateline = if (tmpDateline == -1L) null else Date(tmpDateline)
        this.subject = parcel.readString()!!
        this.userName = parcel.readString()!!
        this.digest = parcel.readByte().toInt() != 0
        this.closed = parcel.readInt()
        this.userId = parcel.readLong()
        this.replyCount = parcel.readInt()
        this.id = parcel.readString()!!
        this.fid = parcel.readLong()
        this.userIcon = parcel.readString()!!
    }

    fun idNum(): Long {
        return this.id.substring(7).toLong()
    }

    companion object CREATOR : Parcelable.Creator<ThreadModel> {
        override fun createFromParcel(parcel: Parcel): ThreadModel {
            return ThreadModel(parcel)
        }

        override fun newArray(size: Int): Array<ThreadModel?> {
            return arrayOfNulls(size)
        }
    }
}