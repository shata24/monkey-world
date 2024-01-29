package com.example.monkey_classification.model

import android.os.Parcel
import android.os.Parcelable

data class User(
    val uid: String = "",
    var name: String = "",
    val userType: String = "",
    val userName: String = "",
    var email: String = "",
    val image: String = "",
    var mobileNumber: String = "",
    val fcmToken: String = "",
    var age: String = "",
    var address: String = "",
    val gender: String = "",
    val doctorCategory: String = "",
    val doctorDesignation: String = "",
    val doctorPrice: Int = 0,
    val doctorRating: Float = 0.0f
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(name)
        parcel.writeString(userType)
        parcel.writeString(email)
        parcel.writeString(image)
        parcel.writeString(mobileNumber)
        parcel.writeString(fcmToken)
        parcel.writeString(age)
        parcel.writeString(address)
        parcel.writeString(gender)
        parcel.writeString(doctorCategory)
        parcel.writeString(doctorDesignation)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
