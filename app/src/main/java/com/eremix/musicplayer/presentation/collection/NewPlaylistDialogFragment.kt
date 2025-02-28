package com.eremix.musicplayer.presentation.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.eremix.musicplayer.databinding.DialogFragmentNewPlaylistBinding

class NewPlaylistDialogFragment: DialogFragment() {

    private var _binding: DialogFragmentNewPlaylistBinding? = null
    private val binding: DialogFragmentNewPlaylistBinding
        get() = _binding ?: throw RuntimeException("DialogFragmentNewPlaylistBinding = null")

    private var listener: MyInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //buttons
        val saveButton = binding.newPlaylistSaveButton
        saveButton.setOnClickListener {
            val playlistName = binding.newPlaylistEt.text.toString()
            listener?.newPlaylistSaveData(playlistName)
            this.dismiss()
        }
        val cancelButton = binding.newPlaylistCancelButton
        cancelButton.setOnClickListener {
            this.dismiss()
        }

        //editText
        binding.newPlaylistEt.addTextChangedListener {
            val contentIsNotBlank = it.toString().isNotBlank()
            saveButton.isEnabled = contentIsNotBlank
        }

    }

    companion object {
        fun getInstance(listener: MyInterface): NewPlaylistDialogFragment {
            return NewPlaylistDialogFragment().apply {
                this.listener = listener
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface MyInterface {
        fun newPlaylistSaveData(playlistName: String)
    }

}