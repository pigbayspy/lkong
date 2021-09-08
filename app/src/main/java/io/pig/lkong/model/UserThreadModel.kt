package io.pig.lkong.model

import android.os.Parcel
import android.os.Parcelable
import io.pig.lkong.http.data.resp.data.UserThreadRespData
import io.pig.lkong.ui.adapter.base.BaseCollectionItem

class UserThreadModel : BaseCollectionItem {

    val fid: Long
    val tid: Long
    val status: String
    val lock: Boolean
    val title: String
    val dateline: Long
    val replies: Int
    val digest: Boolean

    constructor(thread: UserThreadRespData) {
        this.fid = thread.fid
        this.tid = thread.tid
        this.status = thread.status
        this.lock = thread.lock
        this.title = thread.title
        this.dateline = thread.dateline
        this.replies = thread.replies
        this.digest = thread.digest != null
    }

    private constructor(parcel: Parcel) {
        this.fid = parcel.readLong()
        this.tid = parcel.readLong()
        this.status = parcel.readString() ?: ""
        this.lock = parcel.readByte() == 0.toByte()
        this.title = parcel.readString() ?: ""
        this.dateline = parcel.readLong()
        this.replies = parcel.readInt()
        this.digest = parcel.readByte() == 0.toByte()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(this.fid)
        parcel.writeLong(this.tid)
        parcel.writeString(this.status)
        parcel.writeByte(if (this.lock) 0 else 1)
        parcel.writeString(this.title)
        parcel.writeLong(this.dateline)
        parcel.writeInt(this.replies)
        parcel.writeByte(if (this.digest) 0 else 1)
    }

    companion object CREATOR : Parcelable.Creator<UserThreadModel> {
        override fun createFromParcel(parcel: Parcel): UserThreadModel {
            return UserThreadModel(parcel)
        }

        override fun newArray(size: Int): Array<UserThreadModel?> {
            return arrayOfNulls(size)
        }
    }
}