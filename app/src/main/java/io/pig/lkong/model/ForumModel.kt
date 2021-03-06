package io.pig.lkong.model

import android.os.Parcel
import android.os.Parcelable
import io.pig.lkong.http.data.resp.data.ForumRespData
import io.pig.lkong.ui.adapter.base.BaseCollectionItem

/**
 * @author yinhang
 * @since 2021/7/4
 */
class ForumModel : BaseCollectionItem {
    val fid: Long
    val name: String
    val avatar: String
    val num: Long

    private constructor(parcel: Parcel) {
        fid = parcel.readLong()
        name = parcel.readString() ?: ""
        avatar = parcel.readString() ?: ""
        num = parcel.readLong()
    }

    constructor(data: ForumRespData) {
        this.fid = data.fid
        this.name = data.name
        this.avatar = data.avatar
        this.num = data.todayPostNum
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(fid)
        dest.writeString(name)
        dest.writeString(avatar)
        dest.writeLong(num)
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