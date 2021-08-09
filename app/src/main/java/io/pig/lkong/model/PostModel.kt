package io.pig.lkong.model

import android.os.Parcel
import android.os.Parcelable
import io.pig.lkong.ui.adapter.base.BaseCollectionItem
import java.util.*

/**
 * @author yinhang
 * @since 2021/6/28
 */
class PostModel : BaseCollectionItem {

    private val fid: Long
    private val sortKey: Long
    private val sortKeyTime: Date
    private val dateline: Date
    private val message: String
    private val authorName: String
    val authorId: Long
    val authorAvatar: String
    private val favorite: Boolean
    private val isMe: Boolean
    private val notGroup: Boolean
    val pid: Long
    private val first: Boolean
    private val status: Int
    private val id: String
    private val tsAdmin: Boolean
    private val isAdmin: Boolean
    val ordinal: Int
    private val tid: Long
    val rateScore: Int
    private val rateLog: List<PostRateModel>
    private val author: PostAuthorModel
    var postDisplayCache: PostDisplayModel? = null

    private constructor(parcel: Parcel) {
        fid = parcel.readLong()
        sortKey = parcel.readLong()
        val tmpSortKeyTime: Long = parcel.readLong()
        sortKeyTime = if (tmpSortKeyTime == -1L) Date() else Date(tmpSortKeyTime)
        val tmpDateline: Long = parcel.readLong()
        dateline = if (tmpDateline == -1L) Date() else Date(tmpDateline)
        message = parcel.readString()!!
        authorName = parcel.readString()!!
        authorId = parcel.readLong()
        authorAvatar = parcel.readString()!!
        favorite = parcel.readByte().toInt() != 0
        isMe = parcel.readByte().toInt() != 0
        notGroup = parcel.readByte().toInt() != 0
        pid = parcel.readLong()
        first = parcel.readByte().toInt() != 0
        status = parcel.readInt()
        id = parcel.readString()!!
        tsAdmin = parcel.readByte().toInt() != 0
        isAdmin = parcel.readByte().toInt() != 0
        ordinal = parcel.readInt()
        tid = parcel.readLong()
        rateScore = parcel.readInt()
        rateLog = parcel.createTypedArrayList(PostRateModel.CREATOR)!!
        author = parcel.readParcelable(PostAuthorModel::class.java.classLoader)!!
    }

    override fun getSortKey(): Long {
        return sortKey
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(fid)
        dest.writeLong(sortKey)
        dest.writeLong(sortKeyTime.time)
        dest.writeLong(dateline.time)
        dest.writeString(message)
        dest.writeString(authorName)
        dest.writeLong(authorId)
        dest.writeString(authorAvatar)
        dest.writeByte(if (favorite) 1.toByte() else 0.toByte())
        dest.writeByte(if (isMe) 1.toByte() else 0.toByte())
        dest.writeByte(if (notGroup) 1.toByte() else 0.toByte())
        dest.writeLong(pid)
        dest.writeByte(if (first) 1.toByte() else 0.toByte())
        dest.writeInt(status)
        dest.writeString(id)
        dest.writeByte(if (tsAdmin) 1.toByte() else 0.toByte())
        dest.writeByte(if (isAdmin) 1.toByte() else 0.toByte())
        dest.writeInt(ordinal)
        dest.writeLong(tid)
        dest.writeInt(rateScore)
        dest.writeTypedList(rateLog)
        dest.writeParcelable(author, 0)
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