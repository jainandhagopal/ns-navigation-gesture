package com.nscreation.navigationgesture.modules.gesture.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.nscreation.navigationgesture.modules.gesture.domain.model.GestureAction
import com.nscreation.navigationgesture.ui.theme.ThemeMode

object GesturePreferences {
    private const val PREF_NAME = "ns_navigation_gesture_prefs"
    private const val KEY_LEFT_ACTION = "left_action"
    private const val KEY_RIGHT_ACTION = "right_action"
    private const val KEY_BOTTOM_ACTION = "bottom_action"
    private const val KEY_BOTTOM_HOLD_ACTION = "bottom_hold_action"
    private const val KEY_HAPTIC_ENABLED = "haptic_enabled"
    private const val KEY_LEFT_BAR_VISIBLE = "left_bar_visible"
    private const val KEY_RIGHT_BAR_VISIBLE = "right_bar_visible"
    private const val KEY_BOTTOM_BAR_VISIBLE = "bottom_bar_visible"
    private const val KEY_THEME_MODE = "theme_mode"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getThemeMode(context: Context): ThemeMode {
        val value = getPrefs(context).getString(KEY_THEME_MODE, ThemeMode.SYSTEM.name)
        return try {
            ThemeMode.valueOf(value ?: ThemeMode.SYSTEM.name)
        } catch (_: Exception) {
            ThemeMode.SYSTEM
        }
    }

    fun setThemeMode(context: Context, mode: ThemeMode) {
        getPrefs(context).edit { putString(KEY_THEME_MODE, mode.name) }
    }

    fun getLeftAction(context: Context): GestureAction {
        val value = getPrefs(context).getString(KEY_LEFT_ACTION, GestureAction.BACK.value)
        return GestureAction.fromValue(value)
    }

    fun setLeftAction(context: Context, action: GestureAction) {
        getPrefs(context).edit { putString(KEY_LEFT_ACTION, action.value) }
    }

    fun getRightAction(context: Context): GestureAction {
        val value = getPrefs(context).getString(KEY_RIGHT_ACTION, GestureAction.BACK.value)
        return GestureAction.fromValue(value)
    }

    fun setRightAction(context: Context, action: GestureAction) {
        getPrefs(context).edit { putString(KEY_RIGHT_ACTION, action.value) }
    }

    fun getBottomAction(context: Context): GestureAction {
        val value = getPrefs(context).getString(KEY_BOTTOM_ACTION, GestureAction.HOME.value)
        return GestureAction.fromValue(value)
    }

    fun setBottomAction(context: Context, action: GestureAction) {
        getPrefs(context).edit { putString(KEY_BOTTOM_ACTION, action.value) }
    }

    fun getBottomHoldAction(context: Context): GestureAction {
        val value = getPrefs(context).getString(KEY_BOTTOM_HOLD_ACTION, GestureAction.RECENTS.value)
        return GestureAction.fromValue(value)
    }

    fun setBottomHoldAction(context: Context, action: GestureAction) {
        getPrefs(context).edit { putString(KEY_BOTTOM_HOLD_ACTION, action.value) }
    }

    fun isHapticEnabled(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_HAPTIC_ENABLED, true)
    }

    fun setHapticEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit { putBoolean(KEY_HAPTIC_ENABLED, enabled) }
    }

    fun isLeftBarVisible(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_LEFT_BAR_VISIBLE, true)
    }

    fun setLeftBarVisible(context: Context, visible: Boolean) {
        getPrefs(context).edit { putBoolean(KEY_LEFT_BAR_VISIBLE, visible) }
    }

    fun isRightBarVisible(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_RIGHT_BAR_VISIBLE, true)
    }

    fun setRightBarVisible(context: Context, visible: Boolean) {
        getPrefs(context).edit { putBoolean(KEY_RIGHT_BAR_VISIBLE, visible) }
    }

    fun isBottomBarVisible(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_BOTTOM_BAR_VISIBLE, true)
    }

    fun setBottomBarVisible(context: Context, visible: Boolean) {
        getPrefs(context).edit { putBoolean(KEY_BOTTOM_BAR_VISIBLE, visible) }
    }
}
