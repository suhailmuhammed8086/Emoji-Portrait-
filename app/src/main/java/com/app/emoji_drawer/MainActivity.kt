package com.app.emoji_drawer

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.emoji_drawer.components.JoyStickView
import com.app.emoji_drawer.databinding.ActivityMainBinding
import com.app.emoji_drawer.ui.drawing.DrawingActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        DrawingActivity.start(this)
        binding.joyStick.setListener(object : JoyStickView.Listener {
            override fun onChange(dx: Float, dy: Float) {
                "onChange() called with: dx = $dx, dy = $dy".log()
                binding.movableView.translationX += dx
                binding.movableView.translationY += dy
            }
        })
    }
}