package com.smog.app.utils

sealed class AppError: Exception()

object PermissionError : AppError()

object LocalizationError : AppError()

object FindAllError : AppError()

object SensorIdError : AppError()

class SensorDataError(val wrongId: Int) : AppError()