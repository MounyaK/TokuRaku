package com.kamyart.tokuraku.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.sql.RowId

@Entity(tableName = "user_table")
class User (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") private var id: Int,
    @ColumnInfo(name = "name") private var name: String,
    @ColumnInfo(name = "email") private var email: String,
    @ColumnInfo(name = "profile_picture") private var profilePicture: String,

    ){
    //getters
    fun getId() = id
    fun getProfilePicture() = profilePicture
    fun getName() = name
    fun getEmail() = email

    //setters
    fun setProfilePicture(profile_picture: String){this.profilePicture = profile_picture}
    fun setName(name: String){this.name = name}
    fun setEmail(email: String){this.email = email}
}