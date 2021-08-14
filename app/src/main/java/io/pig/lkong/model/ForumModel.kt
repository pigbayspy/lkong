package io.pig.lkong.model

import android.os.Parcel
import android.os.Parcelable
import io.pig.lkong.http.data.resp.data.ForumRespData
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
    val num: Long

    private constructor(parcel: Parcel) {
        fid = parcel.readLong()
        name = parcel.readString() ?: ""
        icon = parcel.readString() ?: ""
        num = parcel.readLong()
    }

    constructor(data: ForumRespData) {
        this.fid = data.fid
        this.name = data.name
        this.icon = LkongUtil.generateForumAvatarUrl(data.fid, data.avatar)
        this.num = data.todayPostNum
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(fid)
        dest.writeString(name)
        dest.writeString(icon)
        dest.writeLong(num)
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