package com.example.startactitivity.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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

    private var checkName: String? = ""
    private var checkId: String? = ""
    private var checkPassword: String? = ""

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
        if (intent.hasExtra("edit")) {
            binding.btnSignup.text = "회원 수정"
            accessPath = 1
        }
        setTextChangedListener()
        setOnFocusChangedListener()
        onClickBtnSignUp()
    }

    private fun initViewModel() = with(viewModel) {
        nameErrorMessage.observe(this@SignUpActivity) {
            getNullIfValid("name", getString(it.message))
        }
        idErrorMessage.observe(this@SignUpActivity) {
            getNullIfValid("id", getString(it.message))
        }
        passwordErrorMessage.observe(this@SignUpActivity) {
            getNullIfValid("password", getString(it.message))
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
        editTextOnFocus.observe(this@SignUpActivity) {
            setErrorMessage(it)
            setConfirmButtonEnable()
        }
        editTextTextChange.observe(this@SignUpActivity) {
            setErrorMessage(it)
            setConfirmButtonEnable()
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
                        User(
                            binding.etName.text.toString(),
                            binding.etId.text.toString(),
                            binding.etPw.text.toString()
                        )
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
                        User(
                            binding.etName.text.toString(),
                            binding.etId.text.toString(),
                            binding.etPw.text.toString()
                        )
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
        viewModel.setTextChangedListener(editTextArray)
    }

    private fun setOnFocusChangedListener() {
        viewModel.setOnFocusChangedListener(editTextArray)

    }

    private fun getMessageValidName() {
        viewModel.setMessageValidName(binding.etName.text.toString())
    }

    private fun getMessageValidId() {
        viewModel.setMessageValidId(binding.etId.text.toString())
    }

    private fun getMessageValidPassword() {
        viewModel.setMessageValidPassword(binding.etPw.text.toString())
    }

    private fun getNullIfValid(type: String, message: String) {
        viewModel.setNullIfValid(type, message)
    }

    private fun setConfirmButtonEnable() {
        viewModel.enableConfirmButton(checkName, checkId, checkPassword)
    }

    private fun setErrorMessage(type: String?) {
        when (type) {
            "name" -> getMessageValidName()
            "id" -> getMessageValidId()
            "password" -> getMessageValidPassword()
        }
    }
}
