package com.example.startactitivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.startactitivity.UserDatabase.getUser
import com.example.startactitivity.databinding.ActivityHomeBinding
import com.example.startactitivity.signup.SignUpActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

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
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initView()
    }

    private fun initView() {
        val userId = intent.getStringExtra("id")!!
        val userInfo = getUser(userId)
        binding.tvId.text = "아이디 : ${intent.getStringExtra("id")}"
        if (userInfo != null) {
            binding.tvName.text = "이름 : ${userInfo.name}"
            binding.tvId.text = "아이디 : ${userInfo.id}"
            // TODO : 회원 확인 기능 추가하고 난 다음에 회원 정보에서 나이랑 mbti 가져오기
//            tvAge.text = "나이 : ${userInfo.age}"
//            tvMbti.text = "MBTI " ${userInfo.mbti}
        }



        binding.btnFinish.setOnClickListener()
        {
            finish()
        }

        binding.ivLogo.setImageDrawable(ResourcesCompat.getDrawable(resources, selectedImage, null))

        binding.btnEdit.setOnClickListener()
        {
            intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra("edit", 0)

            startActivity(intent)
        }


    }
}