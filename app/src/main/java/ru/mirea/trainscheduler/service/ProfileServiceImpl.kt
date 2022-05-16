package ru.mirea.trainscheduler.service

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import ru.mirea.trainscheduler.model.Profile
import ru.mirea.trainscheduler.repository.ProfileRepository
import ru.mirea.trainscheduler.service.ProfileService.Companion.DEFAULT_CURRENCY_CODE
import ru.mirea.trainscheduler.service.ProfileService.Companion.DEFAULT_CURRENCY_VALUE
import ru.mirea.trainscheduler.service.ProfileService.Companion.THEME_CODE
import ru.mirea.trainscheduler.viewModel.SettingsViewModel

class ProfileServiceImpl(private val profileRepository: ProfileRepository) : ProfileService {
    companion object {
        const val TAG = "Settings"
    }

    private val profiles: HashMap<String, Profile> = HashMap()

    override fun getProfileByCode(code: String): Flow<Profile?> = flow {
        if (profiles.containsKey(code)) {
            Log.d(TAG, "В кэше найден профиль $code")
            emit(profiles[code])
        } else {
            Log.d(TAG, "Профиль $code в кэше не найден, выполняется поиск по локальной базе данных")
            val profile = profileRepository.getProfileByCode(code)
            if (profile != null) {
                Log.d(TAG, "Профиль $code найден в локальной базе данных")
                profiles[code] = profile
            } else Log.d(TAG, "Профиль $code не найден")
            emit(profile)
        }
    }

    override fun upsertProfile(profile: Profile) {
        profiles[profile.code!!] = profile
        Log.i(TAG, "Запрос на изменение системного профиля ${profile.code} на ${profile.value}")
        profileRepository.upsertProfile(profile)
    }

    override suspend fun init() {
        var defaultCurrencyProfile = getProfileByCode(DEFAULT_CURRENCY_CODE).firstOrNull()
        if (defaultCurrencyProfile == null) {
            defaultCurrencyProfile = Profile()
            defaultCurrencyProfile.code = DEFAULT_CURRENCY_CODE
            defaultCurrencyProfile.value = DEFAULT_CURRENCY_VALUE
            upsertProfile(defaultCurrencyProfile)
        }
        var themeProfile = getProfileByCode(THEME_CODE).firstOrNull()
        if (themeProfile == null) {
            themeProfile = Profile()
            themeProfile.code = THEME_CODE
            themeProfile.value = Profile.ThemeProfileValues.DEFAULT.name
            upsertProfile(themeProfile)
        }
    }
}