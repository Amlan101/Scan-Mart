package com.example.scanmart.domains

import android.os.Parcel
import android.os.Parcelable

data class ItemDomain(
    var Title: String = "",
    var ImagePath: String = "",
    var Description: String = "",
    var Price: Double = 0.0,
    var Id: Int = 0,
    var Stars: Double = 0.0,
    var CategoryId: Int = 0,
    var LocationId: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Title)
        parcel.writeString(ImagePath)
        parcel.writeString(Description)
        parcel.writeDouble(Price)
        parcel.writeInt(Id)
        parcel.writeDouble(Stars)
        parcel.writeInt(CategoryId)
        parcel.writeInt(LocationId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemDomain> {
        override fun createFromParcel(parcel: Parcel): ItemDomain {
            return ItemDomain(parcel)
        }

        override fun newArray(size: Int): Array<ItemDomain?> {
            return arrayOfNulls(size)
        }
    }
}
