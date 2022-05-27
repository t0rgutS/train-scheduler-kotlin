package ru.mirea.trainscheduler.issue

class UnknownCurrencyException(unknownCode: String) :
    TrainSchedulerException("Неопознанная валюта: $unknownCode")