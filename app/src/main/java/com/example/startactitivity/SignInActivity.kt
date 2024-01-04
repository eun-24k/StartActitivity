package com.example.startactitivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.startactitivity.UserDatabase.getUser
import com.example.startactitivity.databinding.ActivitySigninBinding
import com.example.startactitivity.signup.SignUpActivity
import com.example.startactitivity.signup.SignUpErrorMessage

class SignInActivity : AppCompatActivity(), EditTextValidation {

    private lateinit var binding: ActivitySigninBinding

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>


    // Array
    private val editTextArray: Array<EditText> by lazy {
        arrayOf(binding.etId, binding.etPw)
    }
    private val errorMessageArray: Array<TextView> by lazy {
        arrayOf(binding.emId, binding.emPw)
    }
    private val EmptyEditTextErrorMessage: Array<String> by lazy {
        arrayOf(
            getString(R.string.empty_id_message),
            getString(R.string.empty_password_message),
        )
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initView()

    }

    private fun initView() {
        activityResultLauncher()
        btnSignUp()
        btnLogIn()


    }

    private fun activityResultLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val userId = it.data?.getStringExtra("id") ?: ""
                    val userPw = it.data?.getStringExtra("pw") ?: ""
                    binding.etId.setText(userId)
                    binding.etPw.setText(userPw)
                }
            }
    }

    private fun btnLogIn() {
        binding.btnLogin.setOnClickListener {
            val userInfo = getUser(binding.etId.text.toString())
            when {
                binding.etId.text.toString().trim().isEmpty() -> {
                    Toast.makeText(this, SignUpErrorMessage.EMPTY_ID.message, Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                binding.etPw.text.toString().trim().isEmpty() -> {
                    Toast.makeText(
                        this,
                        SignUpErrorMessage.EMPTY_PASSWORD.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                (userInfo == null) -> {
                    Toast.makeText(
                        this,
                        SignUpErrorMessage.INVALID_ID.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                (userInfo.password != binding.etPw.text.toString()) -> {
                    Toast.makeText(
                        this,
                        SignUpErrorMessage.PASSWORD_MISMATCH.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }

            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("id", binding.etId.text.toString())

            startActivity(intent)


        }

    }

    private fun btnSignUp() {
        binding.btnSignup.setOnClickListener {

            val intent = Intent(this, SignUpActivity::class.java)
            activityResultLauncher.launch(intent)
        }

    }


}


