package io.pig.lkong.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * @author yinhang
 * @since 2021/6/28
 */
class PostAuthorModel : Parcelable {

    private val adminId: String
    private val customStatus: String
    private val gender: Int
    private val regDate: Date
    private val uid: Long
    private val userName: String
    private val verify: Boolean
    private val verifyMessage: String
    private val color: String
    private val stars: String
    private val rankTitle: String

    constructor(
        adminId: String,
        customStatus: String,
        gender: Int,
        regDate: Date,
        uid: Long,
        userName: String,
        verify: Boolean,
        verifyMessage: String,
        color: String,
        stars: String,
        rankTitle: String
    ) {
        this.adminId = adminId
        this.customStatus = customStatus
        this.gender = gender
        this.regDate = regDate
        this.uid = uid
        this.userName = userName
        this.verify = verify
        this.verifyMessage = verifyMessage
        this.color = color
        this.stars = stars
        this.rankTitle = rankTitle
    }

    private constructor(parcel: Parcel) {
        adminId = parcel.readString()!!
        customStatus = parcel.readString()!!
        gender = parcel.readInt()
        val tmpRegDate: Long = parcel.readLong()
        regDate = if (tmpRegDate == -1L) Date() else Date(tmpRegDate)
        uid = parcel.readLong()
        userName = parcel.readString()!!
        verify = parcel.readByte().toInt() != 0
        verifyMessage = parcel.readString()!!
        color = parcel.readString()!!
        stars = parcel.readString()!!
        rankTitle = parcel.readString()!!
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(adminId)
        dest.writeString(customStatus)
        dest.writeInt(gender)
        dest.writeLong(regDate.time)
        dest.writeLong(uid)
        dest.writeString(userName)
        dest.writeByte(if (verify) 1.toByte() else 0.toByte())
        dest.writeString(verifyMessage)
        dest.writeString(color)
        dest.writeString(stars)
        dest.writeString(rankTitle)
    }

    companion object CREATOR : Parcelable.Creator<PostAuthorModel> {
        override fun createFromParcel(parcel: Parcel): PostAuthorModel {
            return PostAuthorModel(parcel)
        }

        override fun newArray(size: Int): Array<PostAuthorModel?> {
            return arrayOfNulls(size)
        }
    }
}