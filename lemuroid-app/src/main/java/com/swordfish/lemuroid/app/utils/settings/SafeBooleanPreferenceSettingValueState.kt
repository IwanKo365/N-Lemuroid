package com.swordfish.lemuroid.app.utils.settings

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.alorma.compose.settings.storage.base.SettingValueState

@Composable
fun rememberSafePreferenceBooleanSettingState(
    key: String,
    defaultValue: Boolean,
    preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(LocalContext.current),
): SafeBooleanPreferenceSettingValueState {
    val state = remember {
        SafeBooleanPreferenceSettingValueState(
            preferences = preferences,
            key = key,
            defaultValue = defaultValue,
        )
    }
    
    DisposableEffect(key, preferences) {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, changedKey ->
            if (key == changedKey) {
                state.updateValue(prefs.safeGetBoolean(changedKey, defaultValue))
            }
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        onDispose {
            preferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }
    
    return state
}

class SafeBooleanPreferenceSettingValueState(
    private val preferences: SharedPreferences,
    val key: String,
    val defaultValue: Boolean = false,
) : SettingValueState<Boolean> {
    private var _value by mutableStateOf(preferences.safeGetBoolean(key, defaultValue))

    override var value: Boolean
        set(value) {
            if (_value != value) {
                _value = value
                preferences.edit { putBoolean(key, value) }
            }
        }
        get() = _value

    fun updateValue(newValue: Boolean) {
        _value = newValue
    }

    override fun reset() {
        value = defaultValue
    }
}
