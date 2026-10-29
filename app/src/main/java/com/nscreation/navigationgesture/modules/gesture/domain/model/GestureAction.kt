package com.nscreation.navigationgesture.modules.gesture.domain.model

enum class GestureAction(val value: String, val displayName: String) {
    BACK("back", "Back"),
    HOME("home", "Home"),
    RECENTS("recents", "Recent Apps"),
    NOTIFICATIONS("notifications", "Notifications"),
    QUICK_SETTINGS("quick_settings", "Quick Settings"),
    SPLIT_SCREEN("split_screen", "Split Screen"),
    NONE("none", "None");

    companion object {
        fun fromValue(value: String?): GestureAction {
            return entries.find { it.value == value } ?: BACK
        }
    }
}
