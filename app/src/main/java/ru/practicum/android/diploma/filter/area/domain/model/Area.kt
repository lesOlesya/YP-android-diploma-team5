package ru.practicum.android.diploma.filter.area.domain.model

import android.os.Parcel
import android.os.Parcelable

data class Area(
    val areaId: String, // ID региона для фильтра поиска
    val parentId: String?, // Если значение == null, то этот регион — страна
    val areaName: String, // Название региона
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(areaId)
        parcel.writeString(parentId)
        parcel.writeString(areaName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Area> {
        override fun createFromParcel(parcel: Parcel): Area {
            return Area(parcel)
        }

        override fun newArray(size: Int): Array<Area?> {
            return arrayOfNulls(size)
        }
    }
}
