package ru.mirea.trainscheduler.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import ru.mirea.trainscheduler.model.Profile
import ru.mirea.trainscheduler.repository.ProfileRepository
import ru.mirea.trainscheduler.service.ProfileService.Companion.DEFAULT_CURRENCY_CODE
import ru.mirea.trainscheduler.service.ProfileService.Companion.DEFAULT_CURRENCY_VALUE
import ru.mirea.trainscheduler.service.ProfileService.Companion.THEME_CODE

class ProfileServiceImpl(private val profileRepository: ProfileRepository) : ProfileService {
    private val profiles: HashMap<String, Profile> = HashMap()

    override fun getProfileByCode(code: String): Flow<Profile?> = flow {
        if (profiles.containsKey(code))
            emit(profiles[code])
        else {
            val profile = profileRepository.getProfileByCode(code)
            if (profile != null)
                profiles[code] = profile
            emit(profile)
        }
    }

    override fun upsertProfile(profile: Profile) {
        profiles[profile.code!!] = profile
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