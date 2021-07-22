package io.pig.lkong.model

import android.os.Parcel
import android.os.Parcelable
import io.pig.lkong.http.data.resp.data.TimelineItemData
import io.pig.lkong.ui.adapter.base.BaseCollectionItem
import java.util.*

/**
 * @author yinhang
 * @since 2021/7/22
 */
class TimelineModel : BaseCollectionItem {

    val isQuote: Boolean
    val userId: Long
    val userName: String
    val dateline: Date
    val message: String
    val isThread: Boolean
    val tid: Long
    val subject: String
    val threadAuthor: String
    val threadAuthorId: Long
    val threadReplyCount: Int
    val id: String
    private val sortKey: Long
    val sortKeyDate: Date
    val replyQuote: ReplyQuote?

    private constructor(parcel: Parcel) {
        isQuote = parcel.readByte().toInt() != 0
        userId = parcel.readLong()
        userName = parcel.readString() ?: ""
        val tmpDateline: Long = parcel.readLong()
        dateline = if (tmpDateline == -1L) null else Date(tmpDateline)
        message = parcel.readString() ?: ""
        isThread = parcel.readByte().toInt() != 0
        tid = parcel.readLong()
        subject = parcel.readString() ?: ""
        threadAuthor = parcel.readString() ?: ""
        threadAuthorId = parcel.readLong()
        threadReplyCount = parcel.readInt()
        id = parcel.readString() ?: ""
        sortKey = parcel.readLong()
        val tmpSortKeyDate: Long = parcel.readLong()
        sortKeyDate = if (tmpSortKeyDate == -1L) {
            Date()
        } else {
            Date(tmpSortKeyDate)
        }
        replyQuote = parcel.readParcelable(ReplyQuote::class.java.classLoader)
    }

    constructor(timeline: TimelineItemData) {
        this.tid = timeline.thread.tid
    }


    override fun getSortKey(): Long {
        return sortKey
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte(if (isQuote) 1.toByte() else 0.toByte())
        dest.writeLong(userId)
        dest.writeString(userName)
        dest.writeLong(dateline.time)
        dest.writeString(message)
        dest.writeByte(if (isThread) 1.toByte() else 0.toByte())
        dest.writeLong(tid)
        dest.writeString(subject)
        dest.writeString(threadAuthor)
        dest.writeLong(threadAuthorId)
        dest.writeInt(threadReplyCount)
        dest.writeString(id)
        dest.writeLong(sortKey)
        dest.writeLong(sortKeyDate.time)
        dest.writeParcelable(replyQuote, 0)
    }

    class ReplyQuote : Parcelable {
        val posterName: String
        val message: String
        val posterMessage: String
        val posterDatelineString: String

        private constructor(parcel: Parcel) {
            posterName = parcel.readString() ?: ""
            posterMessage = parcel.readString() ?: ""
            posterDatelineString = parcel.readString() ?: ""
            message = parcel.readString() ?: ""
        }

        constructor(
            posterName: String,
            message: String,
            posterMessage: String,
            posterDatelineString: String
        ) {
            this.posterName = posterName
            this.message = message
            this.posterMessage = posterMessage
            this.posterDatelineString = posterDatelineString
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(posterName)
            dest.writeString(posterMessage)
            dest.writeString(posterDatelineString)
            dest.writeString(message)
        }

        companion object CREATOR : Parcelable.Creator<ReplyQuote> {
            override fun createFromParcel(parcel: Parcel): ReplyQuote {
                return ReplyQuote(parcel)
            }

            override fun newArray(size: Int): Array<ReplyQuote?> {
                return arrayOfNulls(size)
            }
        }
    }

    companion object CREATOR : Parcelable.Creator<TimelineModel> {
        override fun createFromParcel(parcel: Parcel): TimelineModel {
            return TimelineModel(parcel)
        }

        override fun newArray(size: Int): Array<TimelineModel?> {
            return arrayOfNulls(size)
        }
    }
}