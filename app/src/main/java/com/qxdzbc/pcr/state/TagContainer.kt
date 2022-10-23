package com.qxdzbc.pcr.state

import com.qxdzbc.pcr.common.Rs
import com.qxdzbc.pcr.database.model.DbTag
import com.qxdzbc.pcr.err.ErrorReport
import com.qxdzbc.pcr.state.entry.Tag

interface TagContainer: Map<String, Tag> {
    val allTags:List<Tag>

    fun loadFromDbAndOverwrite():TagContainer
    suspend fun susLoadFromDbAndOverWrite():TagContainer

    fun writeToDb():Rs<Unit, ErrorReport>
    suspend fun susWriteToDb():Rs<Unit, ErrorReport>
}
