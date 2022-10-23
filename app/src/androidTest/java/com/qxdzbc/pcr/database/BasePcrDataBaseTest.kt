package com.qxdzbc.pcr.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.qxdzbc.test.TestSample
import com.qxdzbc.pcr.database.dao.EntryDao
import com.qxdzbc.pcr.database.dao.TagAssignmentDao
import com.qxdzbc.pcr.database.dao.TagDao
import com.qxdzbc.pcr.database.model.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

abstract class BasePcrDataBaseTest {

    lateinit var db: AbsPcrDataBase
    lateinit var entryDao: EntryDao
    lateinit var tagDao: TagDao
    lateinit var tagAssDao: TagAssignmentDao
    lateinit var entries: List<DbEntry>
    lateinit var tagAsignments: List<DbTagAssignment>
    lateinit var tags: List<DbTag>
    lateinit var ts: TestSample

    @Before
    fun b() {
        ts = TestSample()
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AbsPcrDataBase::class.java).build()
        entryDao = db.entryDao
        tagDao = db.tagDao
        tagAssDao = db.tagAssignmentDao
        createInitDataObj()
        insertTestData()
    }

    @After
    fun af() {
        db.close()
    }

    fun createInitDataObj() {
        entries = ts.entries
        tags = ts.tags
        tagAsignments = ts.tagAsignments
    }

    fun insertTestData() {
        tagDao.insert(*tags.toTypedArray())
        entryDao.insert(*entries.toTypedArray())
        tagAssDao.insert(*tagAsignments.toTypedArray())
    }
}