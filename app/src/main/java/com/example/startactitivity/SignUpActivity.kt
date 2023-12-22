package com.example.startactitivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SignUpActivity : AppCompatActivity() {

    private val et_name: EditText by lazy {
        findViewById(R.id.et_name)
    }
    private val et_id: EditText by lazy {
        findViewById(R.id.et_id)
    }
    private val et_pw: EditText by lazy {
        findViewById(R.id.et_pw)
    }
    private val btn_signUp: Button by lazy {
        findViewById(R.id.btn_signup)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        initView()
    }

    private fun initView() {
        setButton()
    }

    private fun setButton() {
        btn_signUp.setOnClickListener {

            val intent = Intent(this, SignInActivity::class.java).apply {
                putExtra("id", et_id.text.toString())
                putExtra("pw", et_pw.text.toString())
            }

            setResult(RESULT_OK, intent)

            if (!isFinishing) finish()

        }
    }

}