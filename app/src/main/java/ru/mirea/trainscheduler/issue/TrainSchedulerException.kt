package ru.mirea.trainscheduler.issue

import java.lang.Exception

abstract class TrainSchedulerException(message: String): Exception(message)