package com.example.startactitivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.startactitivity.UserDatabase.getUser
import com.example.startactitivity.signup.SignUpActivity

class HomeActivity : AppCompatActivity() {
    private val tvId: TextView by lazy {
        findViewById(R.id.tv_id)
    }
    private val tvName: TextView by lazy {
        findViewById(R.id.tv_name)
    }
    private val tvAge: TextView by lazy {
        findViewById(R.id.tv_age)
    }
    private val tvMbti: TextView by lazy {
        findViewById(R.id.tv_mbti)
    }
    private val btnFinish: Button by lazy {
        findViewById(R.id.btn_finish)
    }
    private val btnEdit: Button by lazy {
        findViewById(R.id.btn_edit_user_info)
    }
    private val ivLogo: ImageView by lazy {
        findViewById(R.id.iv_me)
    }
    private val selectedImage = when ((1..6).random()) {
        1 -> R.drawable.me
        2 -> R.drawable.piano
        3 -> R.drawable.profile_picture
        4 -> R.drawable.volunteer
        5 -> R.drawable.badminton
        else -> R.drawable.me
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initView()
    }

    private fun initView() {
        val userId = intent.getStringExtra("id")!!
        val userInfo = getUser(userId)
        tvId.text = "아이디 : ${intent.getStringExtra("id")}"
        if (userInfo != null) {
            tvName.text = "이름 : ${userInfo.name}"
            tvId.text = "아이디 : ${userInfo.id}"
            // TODO : 회원 확인 기능 추가하고 난 다음에 회원 정보에서 나이랑 mbti 가져오기
//            tvAge.text = "나이 : ${userInfo.age}"
//            tvMbti.text = "MBTI " ${userInfo.mbti}
        }



        btnFinish.setOnClickListener()
        {
            finish()
        }

        ivLogo.setImageDrawable(ResourcesCompat.getDrawable(resources, selectedImage, null))

        btnEdit.setOnClickListener()
        {
            intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra("edit", 0)

            startActivity(intent)
        }


    }
}