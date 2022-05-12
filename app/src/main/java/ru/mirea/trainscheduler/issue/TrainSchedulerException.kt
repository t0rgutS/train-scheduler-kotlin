package ru.mirea.trainscheduler.issue

import java.lang.Exception

abstract class TrainSchedulerException(private val errorMessage: String) : Exception(errorMessage)