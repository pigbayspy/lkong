package io.pig.lkong.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * @author yinhang
 * @since 2021/6/28
 */
class PostRateModel : Parcelable {

    private val dateline: Date
    private val extCredits: Int
    private val pid: Long
    private val reason: String
    private val score: Int
    private val uid: Long
    private val userName: String

    constructor(
        dateline: Date,
        extCredits: Int,
        pid: Long,
        reason: String,
        score: Int,
        uid: Long,
        userName: String
    ) {
        this.dateline = dateline
        this.extCredits = extCredits
        this.pid = pid
        this.reason = reason
        this.score = score
        this.uid = uid
        this.userName = userName
    }

    private constructor(parcel: Parcel) {
        val tmpDateline: Long = parcel.readLong()
        dateline = if (tmpDateline == -1L) Date() else Date(tmpDateline)
        extCredits = parcel.readInt()
        pid = parcel.readLong()
        reason = parcel.readString()!!
        score = parcel.readInt()
        uid = parcel.readLong()
        userName = parcel.readString()!!
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(dateline.time)
        dest.writeInt(extCredits)
        dest.writeLong(pid)
        dest.writeString(reason)
        dest.writeInt(score)
        dest.writeLong(uid)
        dest.writeString(userName)
    }

    companion object CREATOR : Parcelable.Creator<PostRateModel> {
        override fun createFromParcel(parcel: Parcel): PostRateModel {
            return PostRateModel(parcel)
        }

        override fun newArray(size: Int): Array<PostRateModel?> {
            return arrayOfNulls(size)
        }
    }
}