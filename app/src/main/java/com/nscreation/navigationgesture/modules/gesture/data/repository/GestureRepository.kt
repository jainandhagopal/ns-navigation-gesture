package com.nscreation.navigationgesture.modules.gesture.data.repository

import android.content.Context
import com.nscreation.navigationgesture.modules.gesture.data.preferences.GesturePreferences
import com.nscreation.navigationgesture.modules.gesture.domain.model.GestureAction
import com.nscreation.navigationgesture.ui.theme.ThemeMode

class GestureRepository(private val context: Context) {

    fun getLeftAction(): GestureAction = GesturePreferences.getLeftAction(context)
    fun setLeftAction(action: GestureAction) = GesturePreferences.setLeftAction(context, action)

    fun getRightAction(): GestureAction = GesturePreferences.getRightAction(context)
    fun setRightAction(action: GestureAction) = GesturePreferences.setRightAction(context, action)

    fun getBottomAction(): GestureAction = GesturePreferences.getBottomAction(context)
    fun setBottomAction(action: GestureAction) = GesturePreferences.setBottomAction(context, action)

    fun getBottomHoldAction(): GestureAction = GesturePreferences.getBottomHoldAction(context)
    fun setBottomHoldAction(action: GestureAction) = GesturePreferences.setBottomHoldAction(context, action)

    fun isHapticEnabled(): Boolean = GesturePreferences.isHapticEnabled(context)
    fun setHapticEnabled(enabled: Boolean) = GesturePreferences.setHapticEnabled(context, enabled)

    fun isLeftBarVisible(): Boolean = GesturePreferences.isLeftBarVisible(context)
    fun setLeftBarVisible(visible: Boolean) = GesturePreferences.setLeftBarVisible(context, visible)

    fun isRightBarVisible(): Boolean = GesturePreferences.isRightBarVisible(context)
    fun setRightBarVisible(visible: Boolean) = GesturePreferences.setRightBarVisible(context, visible)

    fun isBottomBarVisible(): Boolean = GesturePreferences.isBottomBarVisible(context)
    fun setBottomBarVisible(visible: Boolean) = GesturePreferences.setBottomBarVisible(context, visible)

    fun getThemeMode(): ThemeMode = GesturePreferences.getThemeMode(context)
    fun setThemeMode(mode: ThemeMode) = GesturePreferences.setThemeMode(context, mode)
}
