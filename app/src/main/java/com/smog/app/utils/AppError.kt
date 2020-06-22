package com.smog.app.utils

sealed class AppError: Exception()

object PermissionError : AppError()

object LocalizationError : AppError()

object SensorIdError : AppError()

object SensorDataError : AppError()