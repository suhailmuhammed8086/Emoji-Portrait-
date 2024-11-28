package com.app.emoji_drawer.ui.emoji

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Consumer
import androidx.emoji2.emojipicker.EmojiPickerView
import androidx.emoji2.emojipicker.EmojiViewItem
import androidx.fragment.app.FragmentManager
import com.app.emoji_drawer.R
import com.app.emoji_drawer.databinding.FragmentEmojiPickerBinding
import com.app.emoji_drawer.log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.math.log


class EmojiPickerFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentEmojiPickerBinding
    private var emojiPickListener: Listener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        log("onCreate")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Listener) {
            emojiPickListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEmojiPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emojiPickerView.setOnEmojiPickedListener{ emojiPickListener?.onEmojiPicked(it) }
    }

    interface Listener {
        fun onEmojiPicked(emojiViewItem: EmojiViewItem)
    }

    companion object {
        private const val ALERT_TAG = "EMOJI_PICKER"

        @JvmStatic
        fun show(manager: FragmentManager) {
            (manager.findFragmentByTag(ALERT_TAG) as? BottomSheetDialogFragment?)?.dismiss()
            EmojiPickerFragment().show(manager, ALERT_TAG)
            log("showCalled")
        }
    }
}