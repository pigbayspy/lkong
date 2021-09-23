package io.pig.lkong.model

import android.os.Parcel
import android.os.Parcelable
import io.pig.lkong.http.data.resp.data.PrivateMsgListRespData
import io.pig.lkong.ui.adapter.base.BaseCollectionItem

class PmUserModel : BaseCollectionItem {

    val authorId: Long
    val authorName: String
    val authorAvatar: String?
    val content: String
    val lastTime: Long
    val newCount: Int

    constructor(item: PrivateMsgListRespData.PrivateMsgItem) {
        this.authorId = item.user.uid
        this.authorName = item.user.name
        this.authorAvatar = item.user.avatar
        this.content = item.content
        this.lastTime = item.lastTime
        this.newCount = item.newCount
    }

    private constructor(parcel: Parcel) {
        this.authorId = parcel.readLong()
        this.authorName = parcel.readString() ?: ""
        this.authorAvatar = parcel.readString()
        this.content = parcel.readString() ?: ""
        this.lastTime = parcel.readLong()
        this.newCount = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(this.authorId)
        parcel.writeString(this.authorName)
        parcel.writeString(this.authorAvatar)
        parcel.writeString(this.content)
        parcel.writeLong(this.lastTime)
        parcel.writeInt(this.newCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PmUserModel> {
        override fun createFromParcel(parcel: Parcel): PmUserModel {
            return PmUserModel(parcel)
        }

        override fun newArray(size: Int): Array<PmUserModel?> {
            return arrayOfNulls(size)
        }
    }
}