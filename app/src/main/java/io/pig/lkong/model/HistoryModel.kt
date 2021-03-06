package io.pig.lkong.model

import android.os.Parcel
import android.os.Parcelable
import io.pig.lkong.ui.adapter.base.BaseCollectionItem
import java.util.*

class HistoryModel : BaseCollectionItem {

    val userId: Long
    val threadId: Long
    val threadTitle: String
    val forumId: Long
    val postId: String
    val forumTitle: String
    val authorId: Long
    val authorName: String
    val lastReadTime: Date

    constructor(
        userId: Long, threadId: Long, threadTitle: String, forumId: Long,
        postId: String, forumTitle: String, authorId: Long, authorName: String, lastReadTime: Date
    ) {
        this.userId = userId
        this.threadId = threadId
        this.threadTitle = threadTitle
        this.forumId = forumId
        this.postId = postId
        this.forumTitle = forumTitle
        this.authorId = authorId
        this.authorName = authorName
        this.lastReadTime = lastReadTime
    }

    private constructor(parcel: Parcel) {
        userId = parcel.readLong()
        threadId = parcel.readLong()
        threadTitle = parcel.readString() ?: ""
        forumId = parcel.readLong()
        postId = parcel.readString() ?: ""
        forumTitle = parcel.readString() ?: ""
        authorId = parcel.readLong()
        authorName = parcel.readString() ?: ""
        val tmpLastReadTime: Long = parcel.readLong()
        lastReadTime = if (tmpLastReadTime == -1L) Date() else Date(tmpLastReadTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(userId)
        dest.writeLong(threadId)
        dest.writeString(threadTitle)
        dest.writeValue(forumId)
        dest.writeValue(postId)
        dest.writeString(forumTitle)
        dest.writeLong(authorId)
        dest.writeString(authorName)
        dest.writeLong(lastReadTime.time)
    }

    companion object CREATOR : Parcelable.Creator<HistoryModel> {
        override fun createFromParcel(parcel: Parcel): HistoryModel {
            return HistoryModel(parcel)
        }

        override fun newArray(size: Int): Array<HistoryModel?> {
            return arrayOfNulls(size)
        }
    }
}