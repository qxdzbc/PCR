package com.qxdzbc.pcr.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.qxdzbc.pcr.firestore.EntryDoc
import com.qxdzbc.pcr.state.model.WriteState
import com.qxdzbc.pcr.util.DateUtils
import java.util.*

@Entity(tableName = DbEntry.tableName)
data class DbEntry(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = idCol,defaultValue = "")
    val id:String,
    @ColumnInfo(name = moneyCol,defaultValue = "0")
    val money: Double = 0.0,
    @ColumnInfo(name = detailCol)
    val detail:String? = null,
    @ColumnInfo(name = dateTimeCol,defaultValue = "0")
    val dateTime:Long = 0,
    @ColumnInfo(name = isUploadedCol, defaultValue = "0")
    val isUploaded:Int=0,
    @ColumnInfo(name = isCostCol, defaultValue = "0")
    val isCost:Int =0,
    @ColumnInfo(name = stateCol, defaultValue = "")
    val state:String = WriteState.WritePending.name
) {
    companion object{
        const val stateCol="state"
        const val isCostCol = "isCost"
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
                dateTime = DateUtils.randomDate().time,
                isUploaded = 0,
                state=WriteState.WritePending.name
            )
        }
        fun fromEntryDoc(ed:EntryDoc):DbEntry{
            return DbEntry(
                id =ed.id,
                money=ed.money,
                detail=ed.detail,
                dateTime = ed.date,
                isUploaded = 1,
                state =WriteState.OK.name
            )
        }
    }

    fun setUploaded(i:Boolean):DbEntry{
        return this.copy(isUploaded = if(i) 1 else 0)
    }
}
