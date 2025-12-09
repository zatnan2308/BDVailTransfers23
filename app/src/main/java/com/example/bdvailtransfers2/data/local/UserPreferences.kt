package com.example.bdvailtransfers2.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Настройки пользователя, которые храним локально через DataStore:
 * - имя
 * - телефон
 * - код языка (на будущее)
 */

private const val USER_PREFERENCES_NAME = "user_preferences"

// Делегат DataStore, привязанный к Context
val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

// Удобная модель для использования в UI
data class UserPreferences(
    val name: String = "",
    val phone: String = "",
    val languageCode: String = "en"
)

class UserPreferencesManager(private val context: Context) {

    companion object {
        private val KEY_NAME = stringPreferencesKey("name")
        private val KEY_PHONE = stringPreferencesKey("phone")
        private val KEY_LANGUAGE = stringPreferencesKey("language")
    }

    /**
     * Flow с актуальными настройками пользователя.
     * В UI его можно собирать через collectAsState().
     */
    val userPreferencesFlow: Flow<UserPreferences> =
        context.userPreferencesDataStore.data.map { prefs ->
            UserPreferences(
                name = prefs[KEY_NAME] ?: "",
                phone = prefs[KEY_PHONE] ?: "",
                languageCode = prefs[KEY_LANGUAGE] ?: "en"
            )
        }

    /**
     * Обновить имя и телефон (вызываем после успешной брони).
     */
    suspend fun updateNameAndPhone(name: String, phone: String) {
        context.userPreferencesDataStore.edit { prefs ->
            prefs[KEY_NAME] = name
            prefs[KEY_PHONE] = phone
        }
    }

    /**
     * Обновить язык интерфейса (на будущее).
     */
    suspend fun updateLanguage(languageCode: String) {
        context.userPreferencesDataStore.edit { prefs ->
            prefs[KEY_LANGUAGE] = languageCode
        }
    }
}
