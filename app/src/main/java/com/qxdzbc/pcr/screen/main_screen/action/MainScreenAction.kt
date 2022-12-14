package com.qxdzbc.pcr.screen.main_screen.action

import com.qxdzbc.pcr.action.create_entry.CreateEntryAction
import com.qxdzbc.pcr.action.filter_entry.FilterEntryOnMainScreenAction
import com.qxdzbc.pcr.action.log_out.LogoutAction
import com.qxdzbc.pcr.action.remove_entry.RemoveEntryAction
import com.qxdzbc.pcr.action.switch_loading_state.SwitchLoadingStateAction
import com.qxdzbc.pcr.action.switch_theme.SwitchThemeAction
import com.qxdzbc.pcr.action.upload_entry.UploadEntryAction

interface MainScreenAction :SwitchThemeAction, FilterEntryOnMainScreenAction,CreateEntryAction, UploadEntryAction,
    RemoveEntryAction, LogoutAction, SwitchLoadingStateAction {
    companion object{
        val forPreview:MainScreenAction = MainScreenActionDoNothing
    }
}
