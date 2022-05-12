package ru.mirea.trainscheduler.issue

class UnknownCurrencyException(val unknownCode: String) :
    TrainSchedulerException("Unknown currency code: $unknownCode")