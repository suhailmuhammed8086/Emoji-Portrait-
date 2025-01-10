package com.app.emoji_drawer.ui.drawing

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.util.Consumer
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.emoji2.emojipicker.EmojiViewItem
import com.app.emoji_drawer.R
import com.app.emoji_drawer.components.JoyStickView
import com.app.emoji_drawer.components.handlers.OverlayOptionHandler
import com.app.emoji_drawer.databinding.ActivityDrawingBinding
import com.app.emoji_drawer.log
import com.app.emoji_drawer.model.DrawingObject
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
    private lateinit var overlayOption: OverlayOptionHandler

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
        binding.bmEditor.loadBitmap(BitmapFactory.decodeResource(resources, R.drawable.img_1))
    }

    private fun initView() {
        binding.menuLayout.setOnClickListener(this)
        binding.joyStickView.setListener(object : JoyStickView.Listener {
            override fun onChange(dx: Float, dy: Float) {
                binding.drawingCanvas.updateDrawObjectPosition(dx, dy)
            }
        })

        binding.angleSlider.addOnChangeListener { slider, value, fromUser ->
            val angle = (360/100) * value
            angle.log("angle")
            binding.drawingCanvas.updateRotation(angle)
        }
        binding.btPlusSize.setOnPressed {
            binding.drawingCanvas.increaseSize()
        }
        binding.btMinusSize.setOnPressed {
            binding.drawingCanvas.decreaseSize()
        }

        overlayOption = OverlayOptionHandler(binding.overlayOptions)
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
        val image = BitmapConverter.generateBitmap(emojiViewItem.emoji)
        binding.drawingCanvas.addDrawObject(
            DrawingObject(
                type = DrawingObject.DrawingObjectType.Emoji(image)
            )
        )

    }
}