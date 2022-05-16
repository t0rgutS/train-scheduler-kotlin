package ru.mirea.trainscheduler.issue

class RemoteRepositoryException(
    private val operation: String,
    private val errorCode: Int,
    private val errorMessage: String?,
) : TrainSchedulerException() {
}