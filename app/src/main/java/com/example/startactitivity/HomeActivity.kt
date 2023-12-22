package com.example.startactitivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat

class HomeActivity : AppCompatActivity() {
    private val tvId: TextView by lazy {
        findViewById(R.id.tv_id)
    }
    private val btnFinish: Button by lazy {
        findViewById(R.id.btn_finish)
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
        if ( intent.hasExtra("id")) {
            tvId.text = "아이디 : ${intent.getStringExtra("id")}"
        }

        btnFinish.setOnClickListener {
            finish()
        }

        ivLogo.setImageDrawable(ResourcesCompat.getDrawable(resources, selectedImage, null))



    }
}