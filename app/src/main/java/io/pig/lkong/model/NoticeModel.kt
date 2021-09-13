package io.pig.lkong.model

import android.os.Parcel
import android.os.Parcelable
import io.pig.lkong.http.data.resp.data.SystemNoticeRespData
import io.pig.lkong.ui.adapter.base.BaseCollectionItem

class NoticeModel : BaseCollectionItem {

    val id: String
    val uid: Long
    val authorId: Long
    val authorName: String
    val content: String
    val dateline: Long
    val action: String

    constructor(item: SystemNoticeRespData.SystemNoticeItem) {
        id = item.id
        uid = item.uid
        authorId = item.authorid
        authorName = item.author
        content = item.content
        dateline = item.dateline
        action = item.action
    }

    private constructor(parcel: Parcel) {
        id = parcel.readString() ?: ""
        uid = parcel.readLong()
        authorId = parcel.readLong()
        authorName = parcel.readString() ?: ""
        content = parcel.readString() ?: ""
        dateline = parcel.readLong()
        action = parcel.readString() ?: ""
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(id)
        parcel.writeLong(uid)
        parcel.writeLong(authorId)
        parcel.writeString(authorName)
        parcel.writeString(content)
        parcel.writeLong(dateline)
        parcel.writeString(action)
    }

    companion object CREATOR : Parcelable.Creator<NoticeModel> {
        override fun createFromParcel(parcel: Parcel): NoticeModel {
            return NoticeModel(parcel)
        }

        override fun newArray(size: Int): Array<NoticeModel?> {
            return arrayOfNulls(size)
        }
    }
}