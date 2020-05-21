package com.example.muumuu.plankchallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.preference.PreferenceManager
import com.example.muumuu.plankchallenge.databinding.FragmentExerciseBinding
import com.example.muumuu.plankchallenge.util.EventObserver
import com.example.muumuu.plankchallenge.viewmodel.ExerciseViewModel

class ExerciseFragment : Fragment() {

    companion object {
        const val EXERCISE_DURATION_IN_SECOND = 30L
        const val KEY_TIMER_DURATION = "key_timer_duration"
        const val TAG_EDIT_TIMER_DIALOG = "tag_edit_timer_dialog"
    }

    private var _binding: FragmentExerciseBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val timerDuration = getTimerDuration()
        val viewModel =
            ViewModelProviders.of(this)[ExerciseViewModel::class.java]
        val context = context ?: return
        viewModel.initTimer(context)
        viewModel.timer.observe(this) { value ->
            binding.timer.text = (value / 100).toInt().toString()
            val timerDuration0dot01sec = timerDuration * 100
            binding.motionBase.progress =
                ((timerDuration0dot01sec - value.toDouble()) / timerDuration0dot01sec).toFloat()
        }
        viewModel.onCompleteSavingRecord.observe(this, EventObserver {
            (activity as Host).showRecordScreen()
        })
        binding.start.setOnClickListener {
            viewModel.startTimer(timerDuration, context)
        }
        binding.timer.setOnClickListener {
            showTimerEditDialog(timerDuration)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                        binding.timer.text = duration.toString()
                    }
                }
                .show(it, TAG_EDIT_TIMER_DIALOG)
        }
    }
}