package io.pig.lkong.model

import android.os.Parcel
import android.os.Parcelable
import io.pig.lkong.http.data.resp.data.FansRespData
import io.pig.lkong.ui.adapter.base.BaseCollectionItem

class FansModel : BaseCollectionItem {

    val userId: Long
    val avatar: String?
    val name: String
    val isFollow: Boolean

    constructor(item: FansRespData) {
        this.userId = item.uid
        this.avatar = item.avatar
        this.name = item.name
        this.isFollow = item.isFollow
    }

    private constructor(parcel: Parcel) {
        this.userId = parcel.readLong()
        this.avatar = parcel.readString() ?: ""
        this.name = parcel.readString() ?: ""
        this.isFollow = parcel.readByte() == 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(userId)
        parcel.writeString(avatar)
        parcel.writeString(name)
        parcel.writeByte(if (isFollow) 0 else 1)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FansModel> {
        override fun createFromParcel(parcel: Parcel): FansModel {
            return FansModel(parcel)
        }

        override fun newArray(size: Int): Array<FansModel?> {
            return arrayOfNulls(size)
        }
    }
}