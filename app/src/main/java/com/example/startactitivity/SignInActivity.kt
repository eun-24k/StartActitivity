package com.example.startactitivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ReportFragment.Companion.reportFragment

class SignInActivity : AppCompatActivity() {
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private val et_id: EditText by lazy {
        findViewById(R.id.et_id)
    }
    private val et_pw: EditText by lazy {
        findViewById(R.id.et_password)
    }
    private val btn_login: Button by lazy {
        findViewById(R.id.btn_login)
    }
    private val btn_signUp: Button by lazy {
        findViewById(R.id.btn_signup)
    }
//    private val
//    private val editTextArray : Array<EditText> by lazy {
//        arrayOf(et_id, et_pw)
//    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        initView()

    }

    private fun initView() {
        resultLaunchActivity()
        setButton()


    }

    fun resultLaunchActivity() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val user_id = it.data?.getStringExtra("id") ?: ""
                    val user_pw = it.data?.getStringExtra("pw") ?: ""
                    et_id.setText(user_id)
                    et_pw.setText(user_pw)
                }
            }
    }



    fun setButton() {
        btn_login.setOnClickListener {
            if (et_id.text.toString().trim().isEmpty() || et_pw.text.toString().trim().isEmpty()) {
                Toast.makeText(this, getString(R.string.id_error_message), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("id", et_id.text.toString())
            startActivity(intent)
        }
        btn_signUp.setOnClickListener {

            val intent = Intent(this, SignUpActivity::class.java)
            activityResultLauncher.launch(intent)
        }
    }
}