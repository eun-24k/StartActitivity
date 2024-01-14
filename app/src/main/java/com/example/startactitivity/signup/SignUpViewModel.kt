package com.example.startactitivity.signup

import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.startactitivity.signup.SignUpValidExtension.includeAlphabetAndNumber
import com.example.startactitivity.signup.SignUpValidExtension.includeKorean
import com.example.startactitivity.signup.SignUpValidExtension.includeSpecialCharacters

class SignUpViewModel : ViewModel() {

    private val _nameErrorMessage: MutableLiveData<SignUpErrorMessage> = MutableLiveData()
    private val _idErrorMessage: MutableLiveData<SignUpErrorMessage> = MutableLiveData()
    private val _passwordErrorMessage: MutableLiveData<SignUpErrorMessage> = MutableLiveData()
    private val _checkName: MutableLiveData<String?> = MutableLiveData()
    private val _checkId: MutableLiveData<String?> = MutableLiveData()
    private val _checkPassword: MutableLiveData<String?> = MutableLiveData()
    private val _enableConfirmButton: MutableLiveData<Boolean> = MutableLiveData()
    private val _editTextOnFocus: MutableLiveData<String?> = MutableLiveData()
    private val _editTextTextChange: MutableLiveData<String?> = MutableLiveData()
    val nameErrorMessage: LiveData<SignUpErrorMessage> get() = _nameErrorMessage
    val idErrorMessage: LiveData<SignUpErrorMessage> get() = _idErrorMessage
    val passwordErrorMessage: LiveData<SignUpErrorMessage> get() = _passwordErrorMessage
    val checkName: LiveData<String?> get() = _checkName
    val checkId: LiveData<String?> get() = _checkId
    val checkPassword: LiveData<String?> get() = _checkPassword
    val enableConfrimButton: LiveData<Boolean> get() = _enableConfirmButton
    val editTextOnFocus: LiveData<String?> get() = _editTextOnFocus
    val editTextTextChange: LiveData<String?> get() = _editTextTextChange
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
                _checkName.value = editTextValidation(text)
            }

            "id" -> {
                _checkId.value = editTextValidation(text)
                }

            "password" -> {
                _checkPassword.value = editTextValidation(text)
            }

        }
    }

    private fun editTextValidation(text:String):String? {
        val validate = when {
            text.isEmpty() -> null
            else -> text
        }
        return validate
    }

    fun enableConfirmButton(name: String?, id: String?, password: String?) {
        _enableConfirmButton.value = when {
            name == null && id == null && password == null -> true
            else -> false
        }
    }

    fun setOnFocusChangedListener(editTextArray: Array<EditText>) {
        val type = arrayOf("name", "id", "password")
        for (i in type.indices) {
            editTextArray[i].setOnFocusChangeListener { _, hasFocus ->
                _editTextOnFocus.value = when (hasFocus) {
                    false -> type[i]
                    else -> null
                }
            }
        }

    }

    fun setTextChangedListener(editTextArray: Array<EditText>) {
        val type = arrayOf("name", "id", "password")
        for (i in type.indices) {
            editTextArray[i].addTextChangedListener {
                _editTextTextChange.value = type[i]
            }
        }
    }

    private fun setErrorMessage(type: String?,editTextArray: Array<EditText>) {
        when (type) {
            "name" -> setMessageValidName(editTextArray[0].text.toString())
            "id" -> setMessageValidId(editTextArray[1].text.toString())
            "password" -> setMessageValidPassword(editTextArray[2].text.toString())
        }
    }
}
