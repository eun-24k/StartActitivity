package com.example.startactitivity.signup

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.startactitivity.signup.SignUpValidExtension.includeAlphabetAndNumber
import com.example.startactitivity.signup.SignUpValidExtension.includeKorean
import com.example.startactitivity.signup.SignUpValidExtension.includeSpecialCharacters

class SignUpViewModel : ViewModel(){

    private val _nameErrorMessage: MutableLiveData<SignUpErrorMessage> = MutableLiveData()
    private val _idErrorMessage: MutableLiveData<SignUpErrorMessage> = MutableLiveData()
    private val _passwordErrorMessage: MutableLiveData<SignUpErrorMessage> = MutableLiveData()
    private val _checkName: MutableLiveData<String?> = MutableLiveData()
    private val _checkId: MutableLiveData<String?> = MutableLiveData()
    private val _checkPassword: MutableLiveData<String?> = MutableLiveData()
    private val _enableConfirmButton: MutableLiveData<Boolean> = MutableLiveData()
    val nameErrorMessage : LiveData<SignUpErrorMessage> get() = _nameErrorMessage
    val idErrorMessage : LiveData<SignUpErrorMessage> get() = _idErrorMessage
    val passwordErrorMessage : LiveData<SignUpErrorMessage> get() = _passwordErrorMessage
    val checkName: LiveData<String?> get() = _checkName
    val checkId: LiveData<String?> get() = _checkId
    val checkPassword: LiveData<String?> get() = _checkPassword
    val enableConfrimButton : LiveData<Boolean> get() = _enableConfirmButton
    fun setMessageValidName(text: String) {
        _nameErrorMessage.value = when {
            text.isBlank() -> SignUpErrorMessage.EMPTY_NAME
            text.includeKorean() -> SignUpErrorMessage.NULL
            else -> SignUpErrorMessage.INVALID_NAME
        }
    }

    fun setMessageValidId(text: String) {
        _idErrorMessage.value = when {
            text.isBlank() -> SignUpErrorMessage.EMPTY_ID
            text.includeAlphabetAndNumber() -> SignUpErrorMessage.NULL
            else -> SignUpErrorMessage.INVALID_ID
        }
    }
    fun setMessageValidPassword(text: String) {
        _passwordErrorMessage.value = when {
            text.isBlank() -> SignUpErrorMessage.EMPTY_PASSWORD
            text.includeSpecialCharacters() -> SignUpErrorMessage.NULL
            else -> SignUpErrorMessage.INVALID_PASSWORD
        }
    }

    fun enableConfirmButton() {

    }

    fun setNullIfValid(type: String, text: String) {
        when (type) {
            "name" -> {
                _checkName.value =when {
                    text.isEmpty() -> null
                    else -> text
                }
            }
            "id" -> {
                _checkId.value = when {
                    text.isEmpty() -> null
                    else -> text
                }
            }
            "password" -> {
                _checkPassword.value = when {
                    text.isEmpty() -> null
                    else -> text
                }
            }

        }
    }

    fun enableConfirmButton(name: String?, id : String?, password: String?) {
        _enableConfirmButton.value = when {
            name == null && id == null && password == null -> true
            else -> false
        }
    }
}