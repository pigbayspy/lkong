package io.pig.lkong.model

import android.os.Parcel
import android.os.Parcelable
import io.pig.lkong.http.data.resp.data.PmMsgRespData
import io.pig.lkong.ui.adapter.base.BaseCollectionItem

class PrivateMessageModel : BaseCollectionItem {

    val content: String
    val dateline: Long
    val id: String
    val uid: Long

    constructor(item: PmMsgRespData.PmMsgItem) {
        this.content = item.content
        this.dateline = item.dateline
        this.id = item.id
        this.uid = item.uid
    }

    private constructor(parcel: Parcel) {
        this.content = parcel.readString() ?: ""
        this.dateline = parcel.readLong()
        this.id = parcel.readString() ?: ""
        this.uid = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(content)
        parcel.writeLong(dateline)
        parcel.writeString(id)
        parcel.writeLong(uid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PrivateMessageModel> {
        override fun createFromParcel(parcel: Parcel): PrivateMessageModel {
            return PrivateMessageModel(parcel)
        }

        override fun newArray(size: Int): Array<PrivateMessageModel?> {
            return arrayOfNulls(size)
        }
    }
}