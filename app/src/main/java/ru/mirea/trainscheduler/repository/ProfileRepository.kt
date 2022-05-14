package ru.mirea.trainscheduler.repository

import ru.mirea.trainscheduler.model.Profile

interface ProfileRepository {
    fun getProfileByCode(code: String): Profile?

    fun upsertProfile(profile: Profile)
}