package com.example.startactitivity.signup

import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.startactitivity.signup.SignUpValidExtension.includeAlphabetAndNumber
import com.example.startactitivity.signup.SignUpValidExtension.includeKorean
import com.example.startactitivity.signup.SignUpValidExtension.includeSpecialCharacters

/** [2] 뷰모델 클래스는 뷰모델을 상속받는다 (당연히...**/
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
    /** [11] 유효성 검사에 필요한 문자열들이 모두 SignUpErrorMessage 클래스에 들어있다.
     *       여기서 문자열을 바로 가져오면 뷰모델에 뷰를 가져오는 것이므로 MVVM을 따르지 않는 것이다.
     *       따라서 변수의 타입을 SignUpErrorMessage로 해주었다.
     *       SignUpErrorMessage.NULL이 입력값이 유효한 경우이고 SignUpErrorMessage.NULL 은 비어있는 문자열이다.**/
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

    /** [14] 타입에 따라 각각 checkName, checkId, checkPassword에 들어가는 값을 지정해준다.
     *       유효하면(isEmpty()인경우) null값을 부여해주고 아니면 원래 있었던 text를 전달한다.
     *       이 유효성 검사 작업이 모든 EditText에 동일하게 적용되기 때문에 뷰모델 안에 editTextValidation 함수를 만들었다.**/
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
    /** [15] 앞서 말했듯이 문자열이 비어있으면 null을 부여 아닌 경우에는 원래 있었던 문자열을 리턴한다.**/
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
    /** [17] 에딧텍스트가 활성화 되어있을 경우엔ㄴ setTextChangedListener이 잘 작동할 것이기 때문에 아무런 작업을 해줄 필요가 없다.
     *       근데 에딧텍스트가 비활성화 되었을 경우에 에딧텍스트에 들어 있는 값이 유효한지 마지막으로 한번 확인해 주어야한다.
     *       따라서 hasFocus가 false일 때 어느 editText인지 구분하기 위해 전과 마찬가지로 type을 지정해준다.
     *       만약 값이 유효하다면 이때도 null값을 가진다.**/
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

    /** [7] editTextArray에 들어있는 editText의 순서가 이름/아이디/비밀번호이기 때문에
     *      동일한 인덱스를 사용하여 코드의 가독성을 높이기 위해 type이라는 배열에
     *      "name, "id", "password"의 상수들을 저장해 주었다.
     *      이 type이라는 배열은 앞으로도 같은 형식으로 계속 사용할 것이다.
     *      이제 각각의 에딧텍스트가 변경 될때마다 어떤 에딧텍스트의 값이 변경되었는지 editTextTextChange라는 변수에 저장해 준다.
     *      이 값을 이제 initViewModel() 함수에서 관찰해 줄 것이다.**/
    fun setTextChangedListener(editTextArray: Array<EditText>) {
        val type = arrayOf("name", "id", "password")
        for (i in type.indices) {
            editTextArray[i].addTextChangedListener {
                _editTextTextChange.value = type[i]
            }
        }
    }


}
