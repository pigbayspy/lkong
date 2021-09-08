package io.pig.lkong.model.listener

import android.os.Parcel
import android.os.Parcelable
import io.pig.lkong.http.data.resp.data.ForumThreadRespData
import io.pig.lkong.ui.adapter.base.BaseCollectionItem

/**
 * @author yinhang
 * @since 2021/8/15
 */
class ForumThreadModel : BaseCollectionItem {

    val tid: Long
    val fid: Long
    val authorId: Long
    val authorName: String
    val authorAvatar: String
    val title: String
    val digest: Boolean
    val replies: Int
    val dateline: Long

    constructor(item: ForumThreadRespData) {
        this.tid = item.tid
        this.fid = item.fid
        this.authorId = item.author.uid
        this.authorName = item.author.name
        this.authorAvatar = item.author.avatar
        this.title = item.title
        this.digest = item.digest != null
        this.replies = item.replies
        this.dateline = item.dateline
    }

    private constructor(parcel: Parcel) {
        this.tid = parcel.readLong()
        this.fid = parcel.readLong()
        this.authorId = parcel.readLong()
        this.authorName = parcel.readString() ?: ""
        this.authorAvatar = parcel.readString() ?: ""
        this.title = parcel.readString() ?: ""
        this.digest = parcel.readByte() == 0.toByte()
        this.replies = parcel.readInt()
        this.dateline = parcel.readLong()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(tid)
        dest.writeLong(fid)
        dest.writeLong(authorId)
        dest.writeString(authorName)
        dest.writeString(authorAvatar)
        dest.writeString(title)
        dest.writeByte(if (this.digest) 0 else 1)
        dest.writeInt(replies)
        dest.writeLong(dateline)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ForumThreadModel> {
        override fun createFromParcel(parcel: Parcel): ForumThreadModel {
            return ForumThreadModel(parcel)
        }

        override fun newArray(size: Int): Array<ForumThreadModel?> {
            return arrayOfNulls(size)
        }
    }

}