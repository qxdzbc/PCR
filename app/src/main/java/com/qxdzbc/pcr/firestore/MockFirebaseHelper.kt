package com.qxdzbc.pcr.firestore

import com.github.michaelbull.result.Ok
import com.qxdzbc.pcr.common.Rs
import com.qxdzbc.pcr.err.ErrorReport
import com.qxdzbc.pcr.state.model.Entry
import com.qxdzbc.pcr.state.model.Tag

class MockFirebaseHelper constructor(
    val entries: List<Entry> = emptyList(),
    val tags:List<Tag> = emptyList()
) : FirebaseHelper {
    override suspend fun writeTag(userId: String, tag: TagDoc): Rs<Unit, ErrorReport> {
        return Ok(Unit)
    }

    override suspend fun writeTag(userId: String, tag: Tag): Rs<Unit, ErrorReport> {
        return Ok(Unit)
    }

    override suspend fun writeMultiTags(userId: String, tags: List<Tag>): Rs<Unit, ErrorReport> {
        return Ok(Unit)
    }

    override suspend fun removeTag(userId: String, tag: TagDoc): Rs<Unit, ErrorReport> {
        return Ok(Unit)
    }

    override suspend fun removeTag(userId: String, tag: Tag): Rs<Unit, ErrorReport> {
        return Ok(Unit)
    }

    override suspend fun removeTag(userId: String, tagId: String): Rs<Unit, ErrorReport> {
        return Ok(Unit)
    }

    override suspend fun readAllTags(userId: String): Rs<List<TagDoc>, ErrorReport> {
        return Ok(emptyList())
    }

    override suspend fun readAllTagsToModel(userId: String): Rs<List<Tag>, ErrorReport> {
        return Ok(tags)
    }

    override suspend fun readMultiTagsById(
        userId: String,
        tagIds: Collection<String>
    ): Rs<List<TagDoc>, ErrorReport> {
        return Ok(emptyList())
    }

    override suspend fun writeEntry(userId: String, entryDoc: EntryDoc): Rs<Unit, ErrorReport> {
        return Ok(Unit)
    }

    override suspend fun writeEntry(userId: String, entryDoc: Entry): Rs<Unit, ErrorReport> {
        return Ok(Unit)
    }

    override suspend fun writeMultiEntries(
        userId: String,
        entries: List<Entry>
    ): Rs<Unit, ErrorReport> {
        return Ok(Unit)
    }

    override suspend fun removeEntry(userId: String, entryDoc: EntryDoc): Rs<Unit, ErrorReport> {
        return Ok(Unit)
    }

    override suspend fun removeEntry(userId: String, entry: Entry): Rs<Unit, ErrorReport> {
        return Ok(Unit)
    }

    override suspend fun removeEntry(userId: String, entryId: String): Rs<Unit, ErrorReport> {
        return Ok(Unit)
    }

    override suspend fun readAllEntries(userId: String): Rs<List<EntryDoc>, ErrorReport> {
        return Ok(emptyList())
    }

    override suspend fun readAllEntriesToModel(userId: String): Rs<List<Entry>, ErrorReport> {
        return Ok(entries)
    }
}