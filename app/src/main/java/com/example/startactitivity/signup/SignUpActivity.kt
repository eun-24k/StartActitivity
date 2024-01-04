package com.example.startactitivity.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.example.startactitivity.HomeActivity
import com.example.startactitivity.R
import com.example.startactitivity.SignInActivity
import com.example.startactitivity.User
import com.example.startactitivity.UserDatabase
import com.example.startactitivity.databinding.ActivitySignupBinding
import com.example.startactitivity.signup.SignUpValidExtension.includeAlphabetAndNumber
import com.example.startactitivity.signup.SignUpValidExtension.includeKorean
import com.example.startactitivity.signup.SignUpValidExtension.includeSpecialCharacters

class SignUpActivity : AppCompatActivity() {

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private lateinit var binding: ActivitySignupBinding

    private var accessPath: Int = 0

    val editTextArray by lazy {
        arrayOf(binding.etName, binding.etId, binding.etPw)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initView()
    }

    private fun initView() {
        if ( intent.hasExtra("edit")) {
            binding.btnSignup.text = "회원 수정"
            accessPath = 1
        }
        setTextChangedListener()
        setOnFocusChangedListener()
        onClickBtnSignUp()
    }


    private fun onClickBtnSignUp() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    intent.putExtra("id", binding.etId.text.toString())
                    intent.putExtra("password", binding.etPw.text.toString())
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }


        binding.btnSignup.setOnClickListener {


            when (accessPath) {
                0 -> {

                    UserDatabase.addUser(
                        User(binding.etName.text.toString(), binding.etId.text.toString(), binding.etPw.text.toString())
                    )

                    val intent = Intent(this, SignInActivity::class.java).apply {
                        putExtra("id", binding.etId.text.toString())
                        putExtra("password", binding.etPw.text.toString())
                    }
                    setResult(RESULT_OK, intent)
                    resultLauncher.launch(intent)
                }
                1 -> {

                    UserDatabase.editUserData(
                        User(binding.etName.text.toString(), binding.etId.text.toString(), binding.etPw.text.toString())
                    )

                    val intent = Intent(this, HomeActivity::class.java).apply {
                        putExtra("id", binding.etId.text.toString())
                        putExtra("password", binding.etPw.text.toString())
                    }
                    setResult(RESULT_OK, intent)
                    resultLauncher.launch(intent)
                }
            }


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
            binding.etName -> error = getMessageValidName()
            binding.etId -> error = getMessageValidId()
            binding.etPw -> error = getMessageValidPassword()

            else -> Unit
        }
    }

    private fun getMessageValidName(): String? {
        val text = binding.etName.text.toString()
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
        val text = binding.etId.text.toString()
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
        val text = binding.etPw.text.toString()
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
        binding.btnSignup.isEnabled = getMessageValidName() == null
                && getMessageValidId() == null
                && getMessageValidPassword() == null
    }

}

