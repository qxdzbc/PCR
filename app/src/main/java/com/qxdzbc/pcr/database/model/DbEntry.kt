package com.qxdzbc.pcr.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = DbEntry.tableName)
data class DbEntry(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = idCol)
    val id:String,
    @ColumnInfo(name = moneyCol)
    val money: Double = 0.0,
    @ColumnInfo(name = detailCol)
    val detail:String? = null,
    @ColumnInfo(name = dateTimeCol)
    val dateTime:Long = 0,
    @ColumnInfo(name = isUploadedCol)
    val isUploaded:Int=0,
) {
    companion object{
        const val idCol = "id"
        const val moneyCol ="money"
        const val detailCol ="detail"
        const val dateTimeCol = "dateTime"
        const val tableName="Entry"
        const val isUploadedCol = "isUploaded"

        fun random():DbEntry{
            val id = UUID.randomUUID().toString()
            return DbEntry(
                id = id,
                money = (1 .. 100).random().toDouble(),
                detail =  "detail : ${id}",
                dateTime = (1 .. 100).random().toLong(),
                isUploaded = 0,
            )
        }
    }

    fun setUploaded(i:Boolean):DbEntry{
        return this.copy(isUploaded = if(i) 1 else 0)
    }
}
