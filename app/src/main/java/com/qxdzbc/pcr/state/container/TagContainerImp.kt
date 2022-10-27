package com.qxdzbc.pcr.state.container

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.map
import com.qxdzbc.pcr.common.ResultUtils.toErr
import com.qxdzbc.pcr.common.Rs
import com.qxdzbc.pcr.database.DbErrors
import com.qxdzbc.pcr.database.dao.TagDao
import com.qxdzbc.pcr.di.DefaultTagMap
import com.qxdzbc.pcr.err.ErrorReport
import com.qxdzbc.pcr.firestore.FirebaseHelper
import com.qxdzbc.pcr.firestore.MockFirebaseHelper
import com.qxdzbc.pcr.state.model.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class TagContainerImp @Inject constructor(
    @DefaultTagMap
    private val m: Map<String, Tag>,
    private val tagDao: TagDao,
    private val firebaseHelper: FirebaseHelper
) : TagContainer, Map<String, Tag> by m {

    override val allTags: List<Tag>
        get() = m.values.toList()

    override fun loadFromDbAndOverwrite(): TagContainer {
        return this.copy(m = tagDao.getAll().associateBy { it.id })
    }

    override suspend fun susLoadFromDbAndOverWrite(): TagContainer {
        return withContext(Dispatchers.Default) {
            loadFromDbAndOverwrite()
        }
    }

    override fun writeToDb(): Rs<Unit, ErrorReport> {
        try {
            tagDao.insertOrUpdate(this.allTags.map { it.toDbTag() })
            return Ok(Unit)
        } catch (e: Throwable) {
            val msg = "Unable to write tag in TagContainer into db"
            return DbErrors.UnableToWriteTagToDb.report(msg).toErr()
        }
    }

    override suspend fun susWriteToDb(): Rs<Unit, ErrorReport> {
        return withContext(Dispatchers.Default) {
            writeToDb()
        }
    }

    override suspend fun loadFromFirestoreAndOverwrite(userId: String): Rs<TagContainer, ErrorReport> {
        val rt = firebaseHelper.readAllTagsToModel(userId).map {
            this.copy(m=it.associateBy { it.tagId })
        }
        return rt
    }

}
