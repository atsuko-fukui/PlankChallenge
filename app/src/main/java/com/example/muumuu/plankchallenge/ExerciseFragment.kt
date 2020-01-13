package com.example.muumuu.plankchallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.preference.PreferenceManager
import com.example.muumuu.plankchallenge.viewmodel.ExerciseViewModel

class ExerciseFragment : Fragment() {

    companion object {
        const val EXERCISE_DURATION_IN_SECOND = 30L
        const val KEY_TIMER_DURATION = "key_timer_duration"
        const val TAG_EDIT_TIMER_DIALOG = "tag_edit_timer_dialog"
    }

    private val timer: TextView
        get() = view!!.findViewById(R.id.timer)

    private val start: Button
        get() = view!!.findViewById(R.id.start)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exercise, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val timerDuration = getTimerDuration()
        val viewModel =
            ViewModelProviders.of(this)[ExerciseViewModel::class.java]
        val context = context ?: return
        viewModel.initTimer(context)
        viewModel.timer.observe(this) { value ->
            timer.text = value.toString()
        }
        start.setOnClickListener {
            viewModel.startTimer(timerDuration, context)
        }
        timer.setOnClickListener {
            showTimerEditDialog(timerDuration)
        }
    }

    private fun getTimerDuration() =
        PreferenceManager.getDefaultSharedPreferences(context)
            .getLong(KEY_TIMER_DURATION, EXERCISE_DURATION_IN_SECOND)

    private fun showTimerEditDialog(duration: Long) {
        activity?.supportFragmentManager?.let {
            EditTimerDialogFragment
                .createInstance(duration)
                .apply {
                    setListener { duration ->
                        timer.text = duration.toString()
                    }
                }
                .show(it, TAG_EDIT_TIMER_DIALOG)
        }
    }
}