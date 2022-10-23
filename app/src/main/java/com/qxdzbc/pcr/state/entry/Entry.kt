package com.qxdzbc.pcr.state.entry

import com.qxdzbc.pcr.database.model.DbEntry
import com.qxdzbc.pcr.database.model.DbTagAssignment
import java.util.*

interface Entry {
    val id:String
    val money: Double
    val detail:String?
    val dateTime: Date
    val tags:List<Tag>

    fun toDbEntry():DbEntry
    fun toDbTagAssignments(): List<DbTagAssignment>
}
