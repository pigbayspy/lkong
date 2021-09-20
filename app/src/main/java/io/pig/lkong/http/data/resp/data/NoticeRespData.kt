package io.pig.lkong.http.data.resp.data

import android.os.Parcel
import android.os.Parcelable

class NoticeRespData(
    val noticeCount: Int,
    val fansCount: Int,
    val rateCount: Int,
    val atmeCount: Int,
    val pmCount: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(noticeCount)
        parcel.writeInt(fansCount)
        parcel.writeInt(rateCount)
        parcel.writeInt(atmeCount)
        parcel.writeInt(pmCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NoticeRespData> {
        override fun createFromParcel(parcel: Parcel): NoticeRespData {
            return NoticeRespData(parcel)
        }

        override fun newArray(size: Int): Array<NoticeRespData?> {
            return arrayOfNulls(size)
        }
    }

    fun hasNotice(): Boolean {
        return noticeCount > 0 || fansCount > 0 || rateCount > 0 || atmeCount > 0 || pmCount > 0
    }

    fun getAllNoticeCount():Int {
        return noticeCount + fansCount + rateCount + atmeCount + pmCount
    }
}