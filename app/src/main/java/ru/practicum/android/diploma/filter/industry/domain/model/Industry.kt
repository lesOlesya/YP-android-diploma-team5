package ru.practicum.android.diploma.filter.industry.domain.model

import android.os.Parcel
import android.os.Parcelable

data class Industry(
    val industryId: String, // ID отрасли для фильтра поиска
    val industryName: String, // Название отрасли
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(industryId)
        parcel.writeString(industryName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Industry> {
        override fun createFromParcel(parcel: Parcel): Industry {
            return Industry(parcel)
        }

        override fun newArray(size: Int): Array<Industry?> {
            return arrayOfNulls(size)
        }
    }

}
