package com.example.startactitivity.signup

import androidx.annotation.StringRes
import com.example.startactitivity.R

enum class SignUpErrorMessage(
    @StringRes val message: Int,
) {
    EMPTY_NAME(R.string.empty_name_message),
    EMPTY_ID(R.string.empty_id_message),
    EMPTY_PASSWORD(R.string.empty_password_message),

    INVIALID_NAME(R.string.name_error_message),
    INVALID_ID(R.string.id_error_message),
    INVALID_PASSWORD(R.string.password_error_message),

    PASSWORD_MISMATCH(R.string.password_check_error_message),
    OVERLAPPING_ID(R.string.overlapping_id),
}