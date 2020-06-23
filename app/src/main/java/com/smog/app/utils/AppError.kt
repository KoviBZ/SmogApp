package com.smog.app.utils

sealed class AppError: Exception()

object PermissionError : AppError()

object LocalizationError : AppError()

object FindAllError : AppError()

class SensorDataError(val id: Int) : AppError()