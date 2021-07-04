package io.pig.lkong.model

import android.os.Parcel
import android.os.Parcelable
import io.pig.lkong.http.data.LkongForumInfoResp
import io.pig.lkong.ui.adapter.base.BaseCollectionItem
import io.pig.lkong.util.LkongUtil

/**
 * @author yinhang
 * @since 2021/7/4
 */
class ForumModel : BaseCollectionItem {
    val fid: Long
    val name: String
    val icon: String
    val description: String
    val status: String
    val sortByDateline: Int
    val threads: Int
    val todayPosts: Int
    val fansNum: Int
    val blackboard: String
    val moderators: Array<String>

    private constructor(parcel: Parcel) {
        fid = parcel.readLong()
        name = parcel.readString() ?: ""
        icon = parcel.readString() ?: ""
        description = parcel.readString() ?: ""
        status = parcel.readString() ?: ""
        sortByDateline = parcel.readInt()
        threads = parcel.readInt()
        todayPosts = parcel.readInt()
        fansNum = parcel.readInt()
        blackboard = parcel.readString() ?: ""
        moderators = parcel.createStringArray() ?: emptyArray()
    }

    constructor(data: LkongForumInfoResp) {
        this.fid = data.fid
        this.name = data.name
        this.icon = LkongUtil.fidToForumIconUrl(data.fid)
        this.description = data.description
        this.status = data.status
        this.sortByDateline = data.sortbydateline
        this.threads = data.threads.toInt()
        this.todayPosts = data.todayposts.toInt()
        this.fansNum = data.fansnum
        this.blackboard = data.blackboard
        this.moderators = data.moderators
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(fid)
        dest.writeString(name)
        dest.writeString(icon)
        dest.writeString(description)
        dest.writeString(status)
        dest.writeInt(sortByDateline)
        dest.writeInt(threads)
        dest.writeInt(todayPosts)
        dest.writeInt(fansNum)
        dest.writeString(blackboard)
        dest.writeStringArray(moderators)
    }

    override fun getSortKey(): Long {
        return 0
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ForumModel> {
        override fun createFromParcel(parcel: Parcel): ForumModel {
            return ForumModel(parcel)
        }

        override fun newArray(size: Int): Array<ForumModel?> {
            return arrayOfNulls(size)
        }
    }
}