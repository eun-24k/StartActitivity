package com.example.startactitivity.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.startactitivity.HomeActivity
import com.example.startactitivity.SignInActivity
import com.example.startactitivity.User
import com.example.startactitivity.UserDatabase
import com.example.startactitivity.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private lateinit var binding: ActivitySignupBinding

    private var accessPath: Int = 0

    private var checkName : String? = ""
    private var checkId : String? = ""
    private var checkPassword : String? = ""

    val editTextArray by lazy {
        arrayOf(binding.etName, binding.etId, binding.etPw)
    }

    private val viewModel by lazy {
        ViewModelProvider(this@SignUpActivity)[SignUpViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initView()
        initViewModel()
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

    private fun initViewModel() = with(viewModel) {
        nameErrorMessage.observe(this@SignUpActivity) {
//            getNullIfValid(binding.etName, getString(it.message))
            getNullIfValid("name", getString(it.message))
//            binding.etName.error = when {
//                getString(it.message).isEmpty() -> null
//                else -> getString(it.message)
//            }
        }
        idErrorMessage.observe(this@SignUpActivity) {
//            getNullIfValid(binding.etId, getString(it.message))
            getNullIfValid("id",getString(it.message))
//            binding.etId.error = when {
//                getString(it.message).isEmpty() -> null
//                else -> getString(it.message)
//            }
        }
        passwordErrorMessage.observe(this@SignUpActivity) {
//            getNullIfValid(binding.etPw, getString(it.message))
            getNullIfValid("password", getString(it.message))
//            binding.etPw.error = when {
//                getString(it.message).isEmpty() -> null
//                else -> getString(it.message)
//            }
        }

        checkName.observe(this@SignUpActivity) {
            binding.etName.error = it
            this@SignUpActivity.checkName = it

        }
        checkId.observe(this@SignUpActivity) {
            binding.etId.error = it
            this@SignUpActivity.checkId = it
        }
        checkPassword.observe(this@SignUpActivity) {
            binding.etPw.error = it
            this@SignUpActivity.checkPassword = it
        }
        enableConfrimButton.observe(this@SignUpActivity) {
            binding.btnSignup.isEnabled = it
        }
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
            binding.etName -> getMessageValidName()
            binding.etId -> getMessageValidId()
            binding.etPw -> getMessageValidPassword()

            else -> Unit
        }
    }

    private fun getMessageValidName() {
        viewModel.setMessageValidName(binding.etName.text.toString())
//        val text = binding.etName.text.toString()
//        val errorCode = when {
//            text.isBlank() -> SignUpErrorMessage.EMPTY_NAME
//            text.includeKorean() -> null
//            else -> SignUpErrorMessage.INVIALID_NAME
//        }
//        return errorCode?.let {
//            getString(it.message)
//        }
    }

    private fun getMessageValidId(){
        viewModel.setMessageValidId(binding.etId.text.toString())

//        val text = binding.etId.text.toString()
//        val errorCode = when {
//            text.isBlank() -> SignUpErrorMessage.EMPTY_ID
//            text.includeAlphabetAndNumber() -> null
//            else -> SignUpErrorMessage.INVALID_ID
//        }
//        return errorCode?.let {
//            getString(it.message)
//        }
    }

    private fun getMessageValidPassword(){
        viewModel.setMessageValidPassword(binding.etPw.text.toString())

//        val text = binding.etPw.text.toString()
//        val errorCode = when {
//            text.isBlank() -> SignUpErrorMessage.EMPTY_PASSWORD
//            text.includeSpecialCharacters() -> null
//            else -> SignUpErrorMessage.INVALID_PASSWORD
//        }
//        return errorCode?.let {
//            getString(it.message)
//        }
    }

    private fun getNullIfValid(type: String, message: String) {
        viewModel.setNullIfValid(type, message)
    }

//    private fun setConfirmButtonEnable() {
//        binding.btnSignup.isEnabled = getMessageValidName() == null
//                && getMessageValidId() == null
//                && getMessageValidPassword() == null
//    }

    private fun setConfirmButtonEnable() {
        viewModel.enableConfirmButton(checkName, checkId, checkPassword)
    }

}

