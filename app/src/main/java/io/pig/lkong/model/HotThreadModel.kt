package io.pig.lkong.model

import android.os.Parcel
import android.os.Parcelable
import io.pig.lkong.ui.adapter.base.BaseCollectionItem

/**
 * @author yinhang
 * @since 2021/7/13
 */
class HotThreadModel : BaseCollectionItem {

    val tid: Long

    val title: String

    private constructor(parcel: Parcel) {
        this.tid = parcel.readLong()
        this.title = parcel.readString() ?: ""
    }

    constructor(tid: Long, title: String) {
        this.tid = tid
        this.title = title
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(tid)
        dest.writeString(title)
    }

    companion object CREATOR : Parcelable.Creator<HotThreadModel> {
        override fun createFromParcel(parcel: Parcel): HotThreadModel {
            return HotThreadModel(parcel)
        }

        override fun newArray(size: Int): Array<HotThreadModel?> {
            return arrayOfNulls(size)
        }
    }
}