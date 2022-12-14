package com.qxdzbc.pcr.state.container

import com.github.michaelbull.result.Ok
import com.qxdzbc.pcr.common.Rs
import com.qxdzbc.pcr.err.ErrorReport
import com.qxdzbc.pcr.state.model.Tag

data class MockTagContainer(val m: Map<String, Tag> = emptyMap()): AbsTagContainer(m){

    override fun loadFromDbAndOverwrite(): TagContainer {
        return this
    }

    override suspend fun susLoadFromDbAndOverWrite(): TagContainer {
        return this
    }

    override fun writeToDb(): Rs<Unit, ErrorReport> {
        return Ok(Unit)
    }

    override suspend fun susWriteToDb(): Rs<Unit, ErrorReport> {
        return Ok(Unit)
    }

    override suspend fun loadFromFirestoreAndOverwrite(userId: String): Rs<TagContainer, ErrorReport> {
        return Ok(this)
    }

    override suspend fun writeToFirestore(userId: String): Rs<Unit, ErrorReport> {
        return Ok(Unit)
    }

    override suspend fun initLoad(userId: String?): TagContainer {
        return this
    }

    override suspend fun addTagAndWriteToDb(tag: Tag): TagContainer {
        return this.copy(m=m+(tag.tagId to tag))
    }

    override suspend fun uploadThePendings(): TagContainer {
        return this
    }

    override suspend fun deleteAndWriteToDb(tag: Tag): TagContainer {
        return this
    }

    override suspend fun deleteThePendings(): TagContainer {
        return this
    }

    override fun bluntRemoveTag(tag: Tag): TagContainer {
        return this
    }

    override suspend fun replaceAndWriteToDbRs(tag: Tag): Rs<TagContainer, ErrorReport> {
        return Ok(this)
    }
}
