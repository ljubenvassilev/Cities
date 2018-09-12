package com.ljubenvassilev.cities.cities.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = CityEntity.TABLE_NAME)
data class CityEntity(
        @PrimaryKey
        @SerializedName("id")
        val uuid: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("slug")
        val slug: String
) {
    companion object {
        const val TABLE_NAME = "city"
    }
}