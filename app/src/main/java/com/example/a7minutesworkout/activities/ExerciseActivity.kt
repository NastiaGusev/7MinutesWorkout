package com.example.a7minutesworkout.activities

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.data.Constants
import com.example.a7minutesworkout.data.ExerciseModel
import com.example.a7minutesworkout.adapters.ExerciseStatusAdapter
import com.example.a7minutesworkout.R
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding
import com.example.a7minutesworkout.databinding.DialogCustomBackBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var restTimerDuration: Long = 1
    private var exerciseTimerDuration: Long = 3

    private var binding: ActivityExerciseBinding? = null

    private var restProgress = 0
    private var exerciseProgress = 0
    private var restTimer: CountDownTimer? = null
    private var exerciseTimer: CountDownTimer? = null

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //Set the action bar
        setSupportActionBar(binding?.toolBarExercise)
        //Activate back button
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolBarExercise?.setNavigationOnClickListener {
            customDialogBackButton()
        }

        //Create the list of the exercises
        exerciseList = Constants.defaultExerciseList()
        //Set up the RecyclerView
        setUpExerciseRecyclerView()

        tts = TextToSpeech(this, this)
        initMediaPlayer()
        setUpRestView()
    }

    private fun customDialogBackButton() {
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)

        //Cancel option to dismiss dialog from outside of the dialog
        customDialog.setCanceledOnTouchOutside(false)

        dialogBinding.dialogYes.setOnClickListener {
            //Close the current activity
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }

        dialogBinding.dialogNo.setOnClickListener {
            //Stay in the current activity
            customDialog.dismiss()
        }
        customDialog.show()

    }

    private fun setUpExerciseRecyclerView() {
        binding?.recyclerViewExerciseStatus?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.recyclerViewExerciseStatus?.adapter = exerciseAdapter
    }

    private fun initMediaPlayer() {
        try {
            val soundURI =
                Uri.parse("android.resource://com.example.a7minutesworkout/" + R.raw.press_start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The language specified is not supported!")
            }
        } else {
            Log.e("TTS", "Initialization failed")
        }
    }

    private fun speakOut(textToSpeak: String) {
        tts!!.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun setUpRestView() {
        try {
            player?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //change the view for the exercise
        binding?.frameLayoutProgressbarRest?.visibility = View.VISIBLE
        binding?.textviewTitle?.visibility = View.VISIBLE
        binding?.frameLayoutProgressbarExercise?.visibility = View.INVISIBLE
        binding?.textViewExerciseName?.visibility = View.INVISIBLE
        binding?.exerciseImage?.visibility = View.INVISIBLE

        //upcoming exercise set up
        if (currentExercisePosition < exerciseList?.size!! - 1) {
            binding?.textViewUpcoming?.visibility = View.VISIBLE
            binding?.textViewUpcomingName?.visibility = View.VISIBLE
            binding?.textViewUpcomingName?.text =
                exerciseList!![currentExercisePosition + 1].getName()
        }

        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }
        setRestProgressBar()
    }

    private fun setUpExerciseView() {
        //change the view for the exercise
        binding?.frameLayoutProgressbarRest?.visibility = View.INVISIBLE
        binding?.textviewTitle?.visibility = View.INVISIBLE
        binding?.textViewUpcoming?.visibility = View.INVISIBLE
        binding?.textViewUpcomingName?.visibility = View.INVISIBLE

        binding?.frameLayoutProgressbarExercise?.visibility = View.VISIBLE
        binding?.textViewExerciseName?.visibility = View.VISIBLE
        binding?.exerciseImage?.visibility = View.VISIBLE

        //check if there is existing timer
        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        //set the name and image for the current exercise
        binding?.exerciseImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.textViewExerciseName?.text = exerciseList!![currentExercisePosition].getName()
        setExerciseProgressBar()
    }

    private fun setRestProgressBar() {
        binding?.progressbar?.progress = restProgress
        restTimer = object : CountDownTimer(restTimerDuration * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressbar?.progress = 10 - restProgress
                binding?.textviewTimer?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                //Go to the next exercise
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setUpExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar() {
        binding?.progressbarExercise?.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(exerciseTimerDuration * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressbarExercise?.progress = 30 - exerciseProgress
                binding?.textviewTimerExercise?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                if (currentExercisePosition < exerciseList?.size!! - 1) {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setUpRestView()
                } else {
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    override fun onBackPressed() {
        customDialogBackButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }
        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }
        if (player != null) {
            player!!.stop()
        }
        binding = null
    }
}