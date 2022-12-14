package com.qxdzbc.pcr.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.github.michaelbull.result.Ok
import com.qxdzbc.pcr.common.ResultUtils.toErr
import com.qxdzbc.pcr.common.Rs
import com.qxdzbc.pcr.database.DbErrors
import com.qxdzbc.pcr.database.model.DbTag
import com.qxdzbc.pcr.database.model.DbTagWithEntries
import com.qxdzbc.pcr.err.ErrorReport
import com.qxdzbc.pcr.state.model.Tag

@Dao
interface TagDao {
    @Query("SELECT * FROM ${DbTag.tableName}")
    @Transaction
    fun getTagWithEntries(): List<DbTagWithEntries>

    @Query("SELECT * FROM ${DbTag.tableName}")
    fun getAll(): List<DbTag>

    @Insert
    fun insertVA(vararg tags: DbTag)

    fun insertRs(vararg tags: DbTag): Rs<Unit, ErrorReport> {
        try {
            insertVA(*tags)
            return Ok(Unit)
        } catch (e: Throwable) {
            return DbErrors.UnableToWriteTagToDb.report().toErr()
        }
    }

    @Insert
    fun insert(tags: List<DbTag>)

    fun insertMultiRs(tags: List<DbTag>): Rs<Unit, ErrorReport> {
        try {
            insert(tags)
            return Ok(Unit)
        } catch (e: Throwable) {
            return DbErrors.UnableToWriteTagToDb.report().toErr()
        }
    }

    @Delete
    fun delete(tag: DbTag)

    fun deleteRs(tag:Tag): Rs<Unit, ErrorReport> {
        try {
            delete(tag.toDbTag())
            return Ok(Unit)
        } catch (e: Throwable) {
            return DbErrors.UnableToDeleteEntryFromDb.report().toErr()
        }
    }

    @Transaction
    fun deleteMultiRs(tags:List<Tag>): Rs<Unit, ErrorReport> {
        try {
            tags.forEach {
                delete(it.toDbTag())
            }
            return Ok(Unit)
        } catch (e: Throwable) {
            return DbErrors.UnableToDeleteEntryFromDb.report().toErr()
        }
    }



    @Update
    fun updateTag(tag: DbTag)

    @Update
    fun updateTags(tag: List<DbTag>)

    /**
     * Process a list of [DbTag]. Insert the new tag, and update the old tag having the same id as the new ones.
     */
    @Transaction
    fun insertOrUpdate(tags: List<DbTag>) {
        if (tags.isNotEmpty()) {
            val oldTagMap = getAll().associateBy { it.tagId }

            val insertTargets = tags.filter { it.tagId !in oldTagMap.keys }

            val updateTargets = run {
                val ids = oldTagMap.map { it.key }.toSet()
                tags.filter {
                    it.tagId in ids
                }
            }
            updateTags(updateTargets.map { it.toDbTag() })
            insert(insertTargets.map { it.toDbTag() })
        }
    }

    fun insertOrUpdateRs(tags: List<DbTag>): Rs<Unit, ErrorReport> {
        try {
            insertOrUpdate(tags)
            return Ok(Unit)
        } catch (e: Throwable) {
            return DbErrors.UnableToWriteTagToDb.report().toErr()
        }
    }
}
