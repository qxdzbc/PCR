package com.qxdzbc.pcr.screen.create_entry

import com.qxdzbc.pcr.action.remove_err.RemoveErrAction

interface CreateEntryScreenAction: RemoveErrAction {
    companion object
    {
        val forReview = CreateEntryScreenActionDoNothing
    }
}
