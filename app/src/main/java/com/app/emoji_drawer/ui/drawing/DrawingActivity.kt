package com.app.emoji_drawer.ui.drawing

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.util.Consumer
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.emoji2.emojipicker.EmojiViewItem
import com.app.emoji_drawer.R
import com.app.emoji_drawer.databinding.ActivityDrawingBinding
import com.app.emoji_drawer.ui.BaseActivity
import com.app.emoji_drawer.ui.drawing.DrawingViewModel.MenuPositions.*
import com.app.emoji_drawer.ui.emoji.EmojiPickerFragment
import com.app.helpers.BitmapConverter
import java.io.File

class DrawingActivity : BaseActivity(), View.OnClickListener, EmojiPickerFragment.Listener {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, DrawingActivity::class.java)
            context.startActivity(starter)
        }
    }

    private lateinit var binding: ActivityDrawingBinding
    private val viewModel: DrawingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
    }

    private fun initView() {
        binding.menuLayout.setOnClickListener(this)
    }

    override fun observeViewModel() {
        viewModel.menuPosition.observe(this, ::onMenuPositionChanged)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.menuLayout -> {
//                viewModel.changeMenuPosition()
//                avoidMultipleClick {
                    EmojiPickerFragment.show(supportFragmentManager)
//                }
            }
        }
    }

    private fun onMenuPositionChanged(position: DrawingViewModel.MenuPositions) {
        when (position) {
            TOP -> {
                val dy = binding.root.height - binding.menuLayout.height
                binding.menuLayout.animate().translationY(-dy.toFloat())
            }
            BOTTOM -> {
                binding.menuLayout.animate().translationY(0f)
            }
        }
    }

    override fun onEmojiPicked(emojiViewItem: EmojiViewItem) {
        var text = emojiViewItem.emoji
        emojiViewItem.variants.forEach {
            text += it
        }
        binding.tvEmoji.text = text

        val image = BitmapConverter.generateBitmap(emojiViewItem.emoji)
        val file = File.createTempFile("converted-",".jpg")
        image.compress(Bitmap.CompressFormat.JPEG, 100, file.outputStream())


    }
}