/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserFlags.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   16.04.25, 19:18
 */

package dev.bittim.valolink.user.data.flags

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserFlags(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storeData")
        private val LOCAL = booleanPreferencesKey("isLocal")
    }

    fun getLocal(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[LOCAL] == true
        }
    }

    suspend fun setLocal(isLocal: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LOCAL] = isLocal
        }
    }
}
