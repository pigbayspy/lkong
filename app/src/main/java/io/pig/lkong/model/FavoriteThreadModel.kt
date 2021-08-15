package io.pig.lkong.model

import android.os.Parcel
import android.os.Parcelable
import io.pig.lkong.http.data.resp.data.FavoriteRespData
import io.pig.lkong.ui.adapter.base.BaseCollectionItem

/**
 * 帖子模型
 *
 * @author yinhang
 * @since 2021/6/8
 */
class FavoriteThreadModel : BaseCollectionItem {
    val authorName: String
    val authorId: Long
    val authorAvatar: String
    val dateline: Long
    val title: String
    val replyCount: Int
    val tid: Long
    val fid: Long

    constructor(item: FavoriteRespData) {
        // 用户信息
        this.dateline = item.dateline
        this.authorName = item.author.name
        this.authorId = item.author.uid
        this.authorAvatar = item.author.avatar
        this.fid = item.thread.fid
        this.tid = item.thread.tid
        this.replyCount = item.thread.replies
        this.title = item.thread.title
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(dateline)
        dest.writeString(title)
        dest.writeString(authorName)
        dest.writeLong(authorId)
        dest.writeString(authorAvatar)
        dest.writeInt(replyCount)
        dest.writeLong(tid)
        dest.writeLong(fid)
    }

    private constructor(parcel: Parcel) {
        this.dateline = parcel.readLong()
        this.title = parcel.readString() ?: ""
        this.authorName = parcel.readString() ?: ""
        this.authorId = parcel.readLong()
        this.authorAvatar = parcel.readString() ?: ""
        this.replyCount = parcel.readInt()
        this.tid = parcel.readLong()
        this.fid = parcel.readLong()
    }

    companion object CREATOR : Parcelable.Creator<FavoriteThreadModel> {
        override fun createFromParcel(parcel: Parcel): FavoriteThreadModel {
            return FavoriteThreadModel(parcel)
        }

        override fun newArray(size: Int): Array<FavoriteThreadModel?> {
            return arrayOfNulls(size)
        }
    }
}