package com.qxdzbc.pcr.err

import com.qxdzbc.pcr.database.DbErrors

object OtherErrors {

    const val prefix="OtherErrors"
    private val l = listOf(UnableToLogin)

    object UnableToLogin{
        fun report(detail:String?=null):ErrorReport{
            return ErrorReport(
                code= "$prefix ${l.indexOf(this)}",
                detail = detail ?: "unable to login"
            )
        }
    }
}
