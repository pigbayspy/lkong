package io.pig.lkong.ui.pm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.pig.lkong.databinding.ActivityPrivateChatBinding

class PmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivateChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivateChatBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
    }
}