package ru.mirea.trainscheduler.service

import kotlinx.coroutines.flow.Flow
import ru.mirea.trainscheduler.model.Profile

interface ProfileService : Service {
    companion object {
        const val DEFAULT_CURRENCY_CODE = "default_currency"
        const val DEFAULT_CURRENCY_VALUE = "RUB"
        const val THEME_CODE = "theme"
    }

    fun getProfileByCode(code: String): Flow<Profile?>

    fun upsertProfile(profile: Profile)
}