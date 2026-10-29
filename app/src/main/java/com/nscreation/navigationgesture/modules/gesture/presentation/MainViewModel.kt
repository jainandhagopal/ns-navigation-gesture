package com.nscreation.navigationgesture.modules.gesture.presentation

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.nscreation.navigationgesture.modules.gesture.data.repository.GestureRepository
import com.nscreation.navigationgesture.modules.gesture.domain.model.GestureAction
import com.nscreation.navigationgesture.service.GestureAccessibilityService
import com.nscreation.navigationgesture.ui.theme.ThemeMode

class MainViewModel : ViewModel() {

    private lateinit var repository: GestureRepository
    private lateinit var appContext: Context

    var leftAction by mutableStateOf(GestureAction.BACK)
        private set
    var rightAction by mutableStateOf(GestureAction.BACK)
        private set
    var bottomAction by mutableStateOf(GestureAction.HOME)
        private set
    var bottomHoldAction by mutableStateOf(GestureAction.RECENTS)
        private set
    var hapticEnabled by mutableStateOf(true)
        private set
    var leftBarVisible by mutableStateOf(true)
        private set
    var rightBarVisible by mutableStateOf(true)
        private set
    var bottomBarVisible by mutableStateOf(true)
        private set
    var themeMode by mutableStateOf(ThemeMode.SYSTEM)
        private set

    var isServiceEnabled by mutableStateOf(false)
        private set

    fun init(context: Context) {
        if (!::repository.isInitialized) {
            appContext = context.applicationContext
            repository = GestureRepository(appContext)
        }
        loadPreferences()
        checkServiceStatus(context)
    }

    private fun loadPreferences() {
        leftAction = repository.getLeftAction()
        rightAction = repository.getRightAction()
        bottomAction = repository.getBottomAction()
        bottomHoldAction = repository.getBottomHoldAction()
        hapticEnabled = repository.isHapticEnabled()
        leftBarVisible = repository.isLeftBarVisible()
        rightBarVisible = repository.isRightBarVisible()
        bottomBarVisible = repository.isBottomBarVisible()
        themeMode = repository.getThemeMode()
    }

    fun updateThemeMode(mode: ThemeMode) {
        themeMode = mode
        repository.setThemeMode(mode)
    }

    private fun refreshService() {
        if (::appContext.isInitialized) {
            try {
                val intent = Intent(appContext, GestureAccessibilityService::class.java).apply {
                    action = GestureAccessibilityService.ACTION_REFRESH_OVERLAYS
                }
                appContext.startService(intent)
            } catch (_: Exception) {
            }
        }
    }

    fun updateLeftAction(action: GestureAction) {
        leftAction = action
        repository.setLeftAction(action)
        refreshService()
    }

    fun updateRightAction(action: GestureAction) {
        rightAction = action
        repository.setRightAction(action)
        refreshService()
    }

    fun updateBottomAction(action: GestureAction) {
        bottomAction = action
        repository.setBottomAction(action)
        refreshService()
    }

    fun updateBottomHoldAction(action: GestureAction) {
        bottomHoldAction = action
        repository.setBottomHoldAction(action)
        refreshService()
    }

    fun updateHapticEnabled(enabled: Boolean) {
        hapticEnabled = enabled
        repository.setHapticEnabled(enabled)
    }

    fun updateLeftBarVisible(visible: Boolean) {
        leftBarVisible = visible
        repository.setLeftBarVisible(visible)
        refreshService()
    }

    fun updateRightBarVisible(visible: Boolean) {
        rightBarVisible = visible
        repository.setRightBarVisible(visible)
        refreshService()
    }

    fun updateBottomBarVisible(visible: Boolean) {
        bottomBarVisible = visible
        repository.setBottomBarVisible(visible)
        refreshService()
    }

    fun checkServiceStatus(context: Context) {
        val packageName = context.packageName
        val serviceName = "$packageName/${GestureAccessibilityService::class.java.name}"
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        if (!TextUtils.isEmpty(enabledServices)) {
            val splitter = TextUtils.SimpleStringSplitter(':')
            splitter.setString(enabledServices)
            while (splitter.hasNext()) {
                val componentName = splitter.next()
                if (componentName.equals(serviceName, ignoreCase = true)) {
                    isServiceEnabled = true
                    return
                }
            }
        }
        isServiceEnabled = false
    }
}
