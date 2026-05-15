package com.swordfish.lemuroid.app.mobile.feature.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.swordfish.lemuroid.R
import com.swordfish.lemuroid.app.shared.library.PendingOperationsMonitor
import com.swordfish.lemuroid.lib.savesync.SaveSyncManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    private val appContext: Context,
    private val saveSyncManager: SaveSyncManager,
    private val flowSharedPreferences: FlowSharedPreferences,
) : ViewModel() {
    class Factory(
        private val appContext: Context,
        private val saveSyncManager: SaveSyncManager,
        private val flowSharedPreferences: FlowSharedPreferences,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(appContext, saveSyncManager, flowSharedPreferences) as T
        }
    }

    data class UiState(
        val operationInProgress: Boolean = false,
        val saveSyncEnabled: Boolean = false,
        val displaySearch: Boolean = false,
        val searchQuery: String = "",
        val themeMode: String = "system",
    )

    private val currentRouteFlow = MutableStateFlow(MainRoute.HOME)
    private val saveSyncEnabledFlow = MutableStateFlow(false)
    private val operationInProgressFlow = PendingOperationsMonitor(appContext).anyOperationInProgress()
    private val searchQueryFlow = MutableStateFlow("")
    private val themeModeFlow =
        flowSharedPreferences.getString(
            appContext.getString(R.string.pref_key_app_theme),
            "system",
        ).asFlow()

    val state = buildStateFlow()

    private fun buildStateFlow(): StateFlow<UiState> {
        val combinedFlows =
            combine(
                currentRouteFlow,
                saveSyncEnabledFlow,
                operationInProgressFlow,
                searchQueryFlow,
                themeModeFlow,
            ) { currentRoute, saveSyncEnabled, operationInProgress, searchQuery, themeMode ->
                UiState(
                    operationInProgress = operationInProgress,
                    saveSyncEnabled = saveSyncEnabled,
                    displaySearch = currentRoute == MainRoute.SEARCH,
                    searchQuery = searchQuery,
                    themeMode = themeMode,
                )
            }

        return combinedFlows
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = UiState(),
            )
    }

    fun changeRoute(currentRoute: MainRoute) {
        val current = saveSyncManager.isSupported() && saveSyncManager.isConfigured()
        saveSyncEnabledFlow.value = current

        currentRouteFlow.value = currentRoute
    }

    fun changeQueryString(newSearchQuery: String) {
        searchQueryFlow.value = newSearchQuery
    }
}
