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
import com.example.startactitivity.signup.SignUpActivity
import com.example.startactitivity.signup.SignUpErrorMessage

class SignInActivity : AppCompatActivity(), EditTextValidation {
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    // edit Text
    private val etId: EditText by lazy {
        findViewById(R.id.et_id)
    }
    private val etPw: EditText by lazy {
        findViewById(R.id.et_password)
    }

    // button
    private val btnLogin: Button by lazy {
        findViewById(R.id.btn_login)
    }
    private val btnSignup: Button by lazy {
        findViewById(R.id.btn_signup)
    }

    // em : Error Message
    private val emId: TextView by lazy {
        findViewById(R.id.type_id)
    }
    private val emPw: TextView by lazy {
        findViewById(R.id.type_pw)
    }

    // Array
    private val editTextArray: Array<EditText> by lazy {
        arrayOf(etId, etPw)
    }
    private val errorMessageArray: Array<TextView> by lazy {
        arrayOf(emId, emPw)
    }
    private val EmptyEditTextErrorMessage: Array<String> by lazy {
        arrayOf(
            getString(R.string.empty_id_message),
            getString(R.string.empty_password_message),
        )
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

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
                    etId.setText(userId)
                    etPw.setText(userPw)
                }
            }
    }

    private fun btnLogIn() {
        btnLogin.setOnClickListener {
            val userInfo = getUser(etId.text.toString())
            when {
                etId.text.toString().trim().isEmpty() -> {
                    Toast.makeText(this, SignUpErrorMessage.EMPTY_ID.message, Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                etPw.text.toString().trim().isEmpty() -> {
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
                (userInfo.password != etPw.text.toString()) -> {
                    Toast.makeText(
                        this,
                        SignUpErrorMessage.PASSWORD_MISMATCH.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }

            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("id", etId.text.toString())

            startActivity(intent)


        }

    }

    private fun btnSignUp() {
        btnSignup.setOnClickListener {

            val intent = Intent(this, SignUpActivity::class.java)
            activityResultLauncher.launch(intent)
        }

    }


}


