package com.example.muumuu.plankchallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.edit
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import com.example.muumuu.plankchallenge.ExerciseFragment.Companion.KEY_TIMER_DURATION

class EditTimerDialogFragment : DialogFragment() {

    companion object {
        private const val KEY_DURATION = "key_duration"
        fun createInstance(duration: Long) = EditTimerDialogFragment().apply {
            arguments = Bundle().apply {
                putLong(KEY_DURATION, duration)
            }
        }
    }

    private val editText: EditText
        get() = view!!.findViewById(R.id.edit_text)

    private val doneButton: TextView
        get() = view!!.findViewById(R.id.done)

    private var listener: ((Long) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_edit_timer_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getLong(KEY_DURATION)?.let {
            editText.setText(it.toString())
        }

        doneButton.setOnClickListener {
            saveTimerDuration()
            this.dismiss()
        }
    }

    private fun saveTimerDuration() {
        val context = context ?: return
        val duration = editText.text.toString().toLongOrNull() ?: return
        PreferenceManager.getDefaultSharedPreferences(context).edit {
            putLong(KEY_TIMER_DURATION, duration)
        }
        listener?.invoke(duration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener = null
    }

    fun setListener(listener: (Long) -> Unit) {
        this.listener = listener
    }
}