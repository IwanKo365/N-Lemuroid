package com.swordfish.lemuroid.app.mobile.feature.settings.general

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.swordfish.lemuroid.R
import com.swordfish.lemuroid.app.shared.library.PendingOperationsMonitor
import com.swordfish.lemuroid.app.shared.settings.SavesInteractor
import com.swordfish.lemuroid.app.shared.settings.SettingsInteractor
import com.swordfish.lemuroid.lib.savesync.SaveSyncManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    context: Context,
    private val settingsInteractor: SettingsInteractor,
    private val savesInteractor: SavesInteractor,
    saveSyncManager: SaveSyncManager,
    sharedPreferences: FlowSharedPreferences,
) : ViewModel() {
    class Factory(
        private val context: Context,
        private val settingsInteractor: SettingsInteractor,
        private val savesInteractor: SavesInteractor,
        private val saveSyncManager: SaveSyncManager,
        private val sharedPreferences: FlowSharedPreferences,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(
                context,
                settingsInteractor,
                savesInteractor,
                saveSyncManager,
                sharedPreferences,
            ) as T
        }
    }

    data class State(
        val currentDirectory: String = "",
        val isSaveSyncSupported: Boolean = false,
    )

    val indexingInProgress = PendingOperationsMonitor(context).anyLibraryOperationInProgress()

    val directoryScanInProgress = PendingOperationsMonitor(context).isDirectoryScanInProgress()

    private val _events = MutableSharedFlow<Int>()
    val events = _events.asSharedFlow()

    val uiState =
        sharedPreferences.getString(context.getString(com.swordfish.lemuroid.lib.R.string.pref_key_extenral_folder))
            .asFlow()
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, "")
            .map { State(it, saveSyncManager.isSupported()) }

    fun changeLocalStorageFolder() {
        settingsInteractor.changeLocalStorageFolder()
    }

    fun exportSaves(uri: Uri) {
        viewModelScope.launch {
            runCatching { savesInteractor.exportSaves(uri) }
                .onSuccess { _events.emit(R.string.settings_saves_export_success) }
                .onFailure { _events.emit(R.string.settings_saves_export_failure) }
        }
    }

    fun importSaves(uri: Uri) {
        viewModelScope.launch {
            runCatching { savesInteractor.importSaves(uri) }
                .onSuccess { _events.emit(R.string.settings_saves_import_success) }
                .onFailure { _events.emit(R.string.settings_saves_import_failure) }
        }
    }
}
