package io.pig.lkong.model

import android.os.Parcel
import android.os.Parcelable
import io.pig.lkong.http.data.resp.data.TimelineItemData
import io.pig.lkong.ui.adapter.base.BaseCollectionItem
import io.pig.lkong.util.LkongUtil
import java.util.*

/**
 * @author yinhang
 * @since 2021/7/22
 */
class TimelineModel : BaseCollectionItem {

    val authorId: Long
    val authorName: String
    val authorAvatar: String?
    val dateline: Date
    val pid: String
    val content: String
    val threadId: Long
    val replyInfo: ReplyInfo?
    val threadInfo: ThreadInfo?
    val quoteInfo: QuoteInfo?

    private constructor(parcel: Parcel) {
        this.authorId = parcel.readLong()
        this.authorName = parcel.readString() ?: ""
        this.authorAvatar = parcel.readString() ?: ""
        val tmpDateline: Long = parcel.readLong()
        this.dateline = if (tmpDateline == -1L) {
            Date()
        } else {
            Date(tmpDateline)
        }
        this.pid = parcel.readString() ?: ""
        this.content = parcel.readString() ?: ""
        this.threadId = parcel.readLong()
        this.replyInfo = parcel.readParcelable(ReplyInfo::class.java.classLoader)
        this.threadInfo = parcel.readParcelable(ThreadInfo::class.java.classLoader)
        this.quoteInfo = parcel.readParcelable(QuoteInfo::class.java.classLoader)
    }

    constructor(timeline: TimelineItemData) {
        this.authorId = timeline.author.uid
        this.authorName = timeline.author.name
        this.authorAvatar = timeline.author.avatar
        this.dateline = Date(timeline.dateline)
        this.pid = timeline.pid
        val contentModels = LkongUtil.parseTimelineContent(timeline.content)
        this.content = findLastParagraph(contentModels)
        this.threadId = timeline.thread.tid
        if (timeline.quote != null) {
            // 提取信息
            val quoteContentModels = LkongUtil.parseTimelineContent(timeline.quote.content)
            val quoteContent = findLastParagraph(quoteContentModels)
            this.quoteInfo = QuoteInfo(
                timeline.quote.author.name,
                timeline.quote.author.uid,
                quoteContent
            )
        } else {
            this.quoteInfo = null
        }
        if (timeline.thread.replies != null) {
            this.threadInfo = ThreadInfo(
                timeline.thread.replies,
                timeline.thread.title,
                timeline.thread.forumName ?: ""
            )
        } else {
            this.threadInfo = null
        }
        if (timeline.thread.author != null) {
            this.replyInfo = ReplyInfo(
                timeline.thread.title,
                timeline.thread.author.name,
                timeline.thread.author.uid
            )
        } else {
            this.replyInfo = null
        }
    }

    private fun findLastParagraph(contents: List<TimelineContentModel>): String {
        for (content in contents.reversed()) {
            if (content.type == "paragraph") {
                for (child in content.children.reversed()) {
                    for ((key, value) in child) {
                        if (key == "text") {
                            return value as String
                        }
                    }
                }
            }
        }
        return ""
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(authorId)
        dest.writeString(authorName)
        dest.writeString(authorAvatar)
        dest.writeLong(dateline.time)
        dest.writeString(pid)
        dest.writeString(content)
        dest.writeLong(threadId)
        dest.writeParcelable(replyInfo, 0)
        dest.writeParcelable(threadInfo, 0)
        dest.writeParcelable(quoteInfo, 0)
    }

    class ThreadInfo : Parcelable {
        val replyCount: Int
        val title: String
        val forumName: String

        constructor(replyCount: Int, title: String, forumName: String) {
            this.replyCount = replyCount
            this.title = title
            this.forumName = forumName
        }

        private constructor(parcel: Parcel) {
            replyCount = parcel.readInt()
            title = parcel.readString() ?: ""
            forumName = parcel.readString() ?: ""
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(replyCount)
            dest.writeString(title)
            dest.writeString(forumName)
        }

        companion object CREATOR : Parcelable.Creator<ThreadInfo> {
            override fun createFromParcel(parcel: Parcel): ThreadInfo {
                return ThreadInfo(parcel)
            }

            override fun newArray(size: Int): Array<ThreadInfo?> {
                return arrayOfNulls(size)
            }
        }
    }

    class ReplyInfo : Parcelable {
        val title: String
        val authorName: String
        val authorId: Long

        constructor(title: String, authorName: String, authorId: Long) {
            this.title = title
            this.authorName = authorName
            this.authorId = authorId
        }

        private constructor(parcel: Parcel) {
            this.title = parcel.readString() ?: ""
            this.authorName = parcel.readString() ?: ""
            this.authorId = parcel.readLong()
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(title)
            dest.writeString(authorName)
            dest.writeLong(authorId)
        }

        companion object CREATOR : Parcelable.Creator<ReplyInfo> {
            override fun createFromParcel(parcel: Parcel): ReplyInfo {
                return ReplyInfo(parcel)
            }

            override fun newArray(size: Int): Array<ReplyInfo?> {
                return arrayOfNulls(size)
            }
        }
    }

    class QuoteInfo : Parcelable {
        val authorName: String
        val authorId: Long
        val content: String

        private constructor(parcel: Parcel) {
            authorName = parcel.readString() ?: ""
            authorId = parcel.readLong()
            content = parcel.readString() ?: ""
        }

        constructor(
            authorName: String,
            authorId: Long,
            content: String,
        ) {
            this.authorName = authorName
            this.authorId = authorId
            this.content = content
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(authorName)
            dest.writeLong(authorId)
            dest.writeString(content)
        }

        companion object CREATOR : Parcelable.Creator<QuoteInfo> {
            override fun createFromParcel(parcel: Parcel): QuoteInfo {
                return QuoteInfo(parcel)
            }

            override fun newArray(size: Int): Array<QuoteInfo?> {
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