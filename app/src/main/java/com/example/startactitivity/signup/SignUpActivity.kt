package com.example.startactitivity.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.example.startactitivity.R
import com.example.startactitivity.SignInActivity
import com.example.startactitivity.signup.SignUpValidExtension.includeAlphabetAndNumber
import com.example.startactitivity.signup.SignUpValidExtension.includeKorean
import com.example.startactitivity.signup.SignUpValidExtension.includeSpecialCharacters

class SignUpActivity : AppCompatActivity() {

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>



    private val etName: EditText by lazy {
        findViewById(R.id.et_name)
    }
    private val etId: EditText by lazy {
        findViewById(R.id.et_id)
    }
    private val etPw: EditText by lazy {
        findViewById(R.id.et_pw)
    }
    private val btnSignup: Button by lazy {
        findViewById(R.id.btn_signup)
    }
    private val editTextArray: Array<EditText> by lazy {
        arrayOf(etName, etId, etPw)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        initView()
    }

    private fun initView() {
        if ( intent.hasExtra("edit")) {
            btnSignup.text = "회원 수정"
        }
        setTextChangedListener()
        setOnFocusChangedListener()
        onClickBtnSignUp()
    }


    private fun onClickBtnSignUp() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    intent.putExtra("id", etId.text.toString())
                    intent.putExtra("password", etPw.text.toString())
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }


        btnSignup.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java).apply {
                putExtra("id", etId.text.toString())
                putExtra("password", etPw.text.toString())
            }
            setResult(RESULT_OK, intent)
            resultLauncher.launch(intent)
        }
    }

    private fun setTextChangedListener() {
        editTextArray.forEach { editText ->
            editText.addTextChangedListener {
                editText.setErrorMessage()
                setConfirmButtonEnable()
            }
        }
    }

    private fun setOnFocusChangedListener() {
        editTextArray.forEach { editText ->
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    editText.setErrorMessage()
                    setConfirmButtonEnable()
                }
            }
        }
    }

    private fun EditText.setErrorMessage() {
        when (this) {
            etName -> error = getMessageValidName()
            etId -> error = getMessageValidId()
            etPw -> error = getMessageValidPassword()

            else -> Unit
        }
    }

    private fun getMessageValidName(): String? {
        val text = etName.text.toString()
        val errorCode = when {
            text.isBlank() -> SignUpErrorMessage.EMPTY_NAME
            text.includeKorean() -> null
            else -> SignUpErrorMessage.INVIALID_NAME
        }
        return errorCode?.let {
            getString(it.message)
        }
    }

    fun getMessageValidId(): String? {
        val text = etId.text.toString()
        val errorCode = when {
            text.isBlank() -> SignUpErrorMessage.EMPTY_ID
            text.includeAlphabetAndNumber() -> null
            else -> SignUpErrorMessage.INVALID_ID
        }
        return errorCode?.let {
            getString(it.message)
        }
    }

    fun getMessageValidPassword(): String? {
        val text = etPw.text.toString()
        val errorCode = when {
            text.isBlank() -> SignUpErrorMessage.EMPTY_PASSWORD
            text.includeSpecialCharacters() -> null
            else -> SignUpErrorMessage.INVALID_PASSWORD
        }
        return errorCode?.let {
            getString(it.message)
        }
    }

    fun setConfirmButtonEnable() {
        btnSignup.isEnabled = getMessageValidName() == null
                && getMessageValidId() == null
                && getMessageValidPassword() == null
    }

}

