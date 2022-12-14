package com.qxdzbc.pcr

import com.qxdzbc.pcr.database.model.DbEntry
import com.qxdzbc.pcr.database.model.DbEntryWithTags
import com.qxdzbc.pcr.database.model.DbTag
import com.qxdzbc.pcr.database.model.DbTagAssignment
import com.qxdzbc.pcr.state.app.MockFirebaseUserWrapper
import com.qxdzbc.pcr.state.model.WriteState
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

class TestSample {
    val entries = (1..10).map {
        DbEntry(
            id = it.toString(),
            money = Random.nextInt(100..200).toDouble(),
            detail = "entry $it",
            dateTime = Date().time,
            state = WriteState.WritePending.name,
        )
    }
    val tags = (1..10).map {
        DbTag(id = it.toString(), name = "Tag $it")
    }
    val tagAsignments = (1..10).map {
        DbTagAssignment(
            entryId = it.toString(),
            tagId = maxOf(it.toLong() / 2, 1L).toString()
        )
    }
    val entriesWithTag:List<DbEntryWithTags> = run{
        val m=tagAsignments.groupBy { it.entryId }.map { (entryId,tagList)->
            DbEntryWithTags(
                entry = entries.first { it.id == entryId },
                tags = tagList.map { ta->
                    tags.first{tag->tag.id == ta.tagId}
                }
            )
        }
        m
    }
    val fakeUser=MockFirebaseUserWrapper()
    val comp: TestComponent = DaggerTestComponent.create()

    init{
        val userMs = comp.userMs()
        userMs.value = fakeUser
    }
}
