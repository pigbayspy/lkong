package io.pig.lkong.model

import android.os.Parcel
import android.os.Parcelable
import io.pig.lkong.http.data.resp.data.PostRespPostData
import io.pig.lkong.ui.adapter.base.BaseCollectionItem

/**
 * @author yinhang
 * @since 2021/6/28
 */
class PostModel : BaseCollectionItem {

    val dateline: Long
    val message: String
    val authorName: String
    val authorId: Long
    val authorAvatar: String
    val pid: String
    val tid: Long
    val status: String
    val ordinal: Int

    constructor(item: PostRespPostData) {
        this.dateline = item.dateline
        this.message = item.content
        this.authorName = item.user.name
        this.authorAvatar = item.user.avatar
        this.authorId = item.user.uid
        this.pid = item.pid
        this.tid = item.tid
        this.status = item.status
        this.ordinal = item.lou
    }

    private constructor(parcel: Parcel) {
        dateline = parcel.readLong()
        message = parcel.readString() ?: ""
        authorName = parcel.readString() ?: ""
        authorId = parcel.readLong()
        authorAvatar = parcel.readString() ?: ""
        pid = parcel.readString() ?: ""
        tid = parcel.readLong()
        status = parcel.readString() ?: ""
        ordinal = parcel.readInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(dateline)
        dest.writeString(message)
        dest.writeString(authorName)
        dest.writeLong(authorId)
        dest.writeString(authorAvatar)
        dest.writeString(pid)
        dest.writeLong(tid)
        dest.writeString(status)
        dest.writeInt(ordinal)
    }

    companion object CREATOR : Parcelable.Creator<PostModel> {
        override fun createFromParcel(parcel: Parcel): PostModel {
            return PostModel(parcel)
        }

        override fun newArray(size: Int): Array<PostModel?> {
            return arrayOfNulls(size)
        }
    }

}