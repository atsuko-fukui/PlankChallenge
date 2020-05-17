package com.example.muumuu.plankchallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import com.example.muumuu.plankchallenge.ExerciseFragment.Companion.KEY_TIMER_DURATION
import com.example.muumuu.plankchallenge.databinding.FragmentEditTimerDialogBinding

class EditTimerDialogFragment : DialogFragment() {

    companion object {
        private const val KEY_DURATION = "key_duration"
        fun createInstance(duration: Long) = EditTimerDialogFragment().apply {
            arguments = Bundle().apply {
                putLong(KEY_DURATION, duration)
            }
        }
    }

    private var _binding: FragmentEditTimerDialogBinding? = null
    private val binding
        get() = _binding!!

    private var listener: ((Long) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditTimerDialogBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getLong(KEY_DURATION)?.let {
            binding.editText.setText(it.toString())
        }

        binding.done.setOnClickListener {
            saveTimerDuration()
            this.dismiss()
        }
    }

    private fun saveTimerDuration() {
        val context = context ?: return
        val duration = binding.editText.text.toString().toLongOrNull() ?: return
        PreferenceManager.getDefaultSharedPreferences(context).edit {
            putLong(KEY_TIMER_DURATION, duration)
        }
        listener?.invoke(duration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener = null
        _binding = null
    }

    fun setListener(listener: (Long) -> Unit) {
        this.listener = listener
    }
}