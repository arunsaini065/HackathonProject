package com.serhat.autosub

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.serhat.autosub.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.subtitleGeneratorBt.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.subtitleTranslateBt.setOnClickListener {
            startActivity(Intent(this, TranslatorActivity::class.java))
        }

    }

}
