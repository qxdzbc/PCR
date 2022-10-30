package com.qxdzbc.pcr.state.container

import com.github.michaelbull.result.*
import com.qxdzbc.pcr.common.ResultUtils.toErr
import com.qxdzbc.pcr.common.ResultUtils.toOk
import com.qxdzbc.pcr.common.Rs
import com.qxdzbc.pcr.common.St
import com.qxdzbc.pcr.database.DbErrors
import com.qxdzbc.pcr.database.dao.EntryDao
import com.qxdzbc.pcr.database.dao.TagAssignmentDao
import com.qxdzbc.pcr.database.dao.TagDao
import com.qxdzbc.pcr.database.model.DbEntry
import com.qxdzbc.pcr.database.model.DbEntryWithTags
import com.qxdzbc.pcr.di.DefaultEntryMap
import com.qxdzbc.pcr.di.state.UserSt
import com.qxdzbc.pcr.err.ErrorReport
import com.qxdzbc.pcr.firestore.FirestoreHelper
import com.qxdzbc.pcr.state.app.FirebaseUserWrapper
import com.qxdzbc.pcr.state.model.Entry
import com.qxdzbc.pcr.state.model.EntryState
import com.qxdzbc.pcr.state.model.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

data class EntryContainerImp @Inject constructor(
    @DefaultEntryMap
    private val m: Map<String, @JvmSuppressWildcards Entry>,
    private val entryDao: EntryDao,
    private val tagDao: TagDao,
    private val tagAssignmentDao: TagAssignmentDao,
    private val firestoreHelper: FirestoreHelper,
    @UserSt
    private val userSt: St<@JvmSuppressWildcards FirebaseUserWrapper?>
) : AbsEntryContainer(m) {

    companion object {
        fun empty(
            entryDao: EntryDao,
            tagDao: TagDao,
            tagAssignmentDao: TagAssignmentDao,
            firestoreHelper: FirestoreHelper,
            userSt: St<FirebaseUserWrapper?>
        ) =
            EntryContainerImp(
                emptyMap(),
                entryDao,
                tagDao,
                tagAssignmentDao,
                firestoreHelper,
                userSt
            )
    }

    val userId get() = userSt.value?.uid
    override val allEntries: List<Entry> get() = m.values.toList()
    override fun clearAll(): EntryContainer {
        return this.copy(m = emptyMap())
    }

    override fun loadFromDbAndOverwrite(): EntryContainer {
        val nm = entryDao.getEntryWithTag().associateBy { it.id }
        return this.copy(m = nm)
    }

    override suspend fun susLoadFromDbAndOverWrite(): EntryContainer {
        return withContext(Dispatchers.Default) {
            loadFromDbAndOverwrite()
        }
    }

    override fun writeToDb(): Rs<Unit, ErrorReport> {
//        try {
//            entryDao.insertOrUpdate(
//                this.allEntries.map { it.toDbEntry() }
//            )
//            tagDao.insertOrUpdate(
//                this.allEntries
//                    .flatMap { it.tags }
//                    .distinct()
//                    .map { it.toDbTag() }
//            )
//            tagAssignmentDao.insertAndDeleteByEntryId(
//                this.allEntries.flatMap { it.toDbTagAssignments() }
//            )
//            return Unit.toOk()
//        } catch (e: Throwable) {
//            val msg = "Unable to write entries in entry container into the db"
//            return DbErrors.UnableToWriteEntryToDb.report(msg).toErr()
//        }

        val msg = "Unable to write entries in entry container into the db"
        val q = DbErrors.UnableToWriteEntryToDb.report(msg).toErr()

        try {
            entryDao.insertOrUpdate(
                this.allEntries.map { it.toDbEntry() }
            )
            try {
                tagDao.insertOrUpdate(
                    this.allEntries
                        .flatMap { it.tags }
                        .distinct()
                        .map { it.toDbTag() }
                )
                try {
                    tagAssignmentDao.insertAndDeleteByEntryId(
                        this.allEntries.flatMap { it.toDbTagAssignments() }
                    )
                    return Unit.toOk()
                } catch (e3: Throwable) {
                    return q
                }
            } catch (e2: Throwable) {
                return q
            }
        } catch (e: Throwable) {
            return q
        }

    }

    override suspend fun susWriteToDb(): Rs<Unit, ErrorReport> {
        return withContext(Dispatchers.Default) {
            writeToDb()
        }
    }

    override suspend fun loadFromFirestoreAndOverwrite(userId: String): Rs<EntryContainer, ErrorReport> {
        val rs = firestoreHelper.readAllEntriesToModel(userId)
        val rt = rs.map {
            this.copy(m = it.associateBy { it.id })
        }
        return rt
    }

    private fun setAll(entries: List<Entry>): EntryContainerImp {
        return this.copy(m = entries.associateBy { it.id })
    }

    override suspend fun writeAllToFirestore(userId: String): Rs<EntryContainer, ErrorReport> {
        return firestoreHelper.writeMultiEntries(userId, allEntries).map {
            setAll(it)
        }
    }

    override suspend fun writeUnUploadedToFirestore(userId: String): Rs<EntryContainer, ErrorReport> {
        val all = allEntries
        val targetEntries = allEntries.filter { !it.isUploaded }
        val rt = firestoreHelper.writeMultiEntries(userId, targetEntries).map {
            val uploadedEntries = it.associateBy { it.id }
            val allNewEntries = all.map { oldEntry ->
                if (oldEntry.id in uploadedEntries) {
                    uploadedEntries[oldEntry.id]!!
                } else {
                    oldEntry
                }
            }
            setAll(allNewEntries)
        }
        return rt
    }

    override suspend fun initLoad(userId: String?): EntryContainer {
        val tCont = susLoadFromDbAndOverWrite()
        val rt = userId?.let { uid ->
            if (tCont.isEmpty()) {
                tCont.loadFromFirestoreAndOverwrite(uid).component1() ?: tCont
            } else {
                tCont
            }
        } ?: tCont
        return rt
    }

    private fun plainAdd(e: Entry): EntryContainerImp {
        return this.copy(m = m + (e.id to e))
    }

    override fun addEntryAndWriteToDb(newEntry: Entry): Rs<EntryContainer, ErrorReport> {
        val currentCont = this
        val rt = insertIntoDb(newEntry).map {
            currentCont.plainAdd(newEntry)
        }
        return rt
    }

    override fun createEntryAndWriteToDb(
        date: Date,
        money: Double,
        detail: String?,
        tags: List<Tag>,
        isCost: Boolean
    ): Rs<EntryContainer, ErrorReport> {
        val entry = DbEntryWithTags(
            entry = DbEntry(
                id = UUID.randomUUID().toString(),
                money = money,
                detail = detail,
                dateTime = date.time,
                isUploaded = 0,
                isCost = if (isCost) 1 else 0,
                state = EntryState.WritePending.name,
            ),
            tags = tags.map { it.toDbTag() }
        )
        return this.addEntryAndWriteToDb(entry)
    }


    override fun addOrReplaceAndWriteToDb(entry: Entry): Rs<EntryContainerImp, ErrorReport> {
        val mm = m.toMutableMap()
        mm[entry.id] = entry
        val nc = this.copy(m = mm)
        val rt = nc.writeToDb().map { nc }
        return rt
    }


    override suspend fun removeEntry(entry: Entry): EntryContainer {
        val u = userId
        val newE = entry.setState(EntryState.DeletePending)
        val afterMarkingCont = this.addOrReplaceAndWriteToDb(newE).component1() ?: this
        if (u != null) {
            val frs = firestoreHelper.removeEntry(u, newE.id)
            when (frs) {
                is Ok -> {
                    // remove from db
                    val rs = entryDao.deleteRs(newE.toDbEntry())
                    val afterDbCont = rs.mapBoth(
                        success = {
                            // remove from mem
                            afterMarkingCont.copy(m = m - newE.id)
                        },
                        failure = { afterMarkingCont }
                    )
                    return afterDbCont
                }
                is Err -> {
                    return afterMarkingCont
                }
            }
        } else {
            return afterMarkingCont
        }
    }


    private fun insertIntoDb(e: Entry): Rs<Unit, ErrorReport> {
        try {
            entryDao.insert(e.toDbEntry())
            return Ok(Unit)
        } catch (e: Throwable) {
            val msg = "Unable to write ${e} into the db"
            return DbErrors.UnableToWriteEntryToDb.report(msg).toErr()
        }
    }

}
