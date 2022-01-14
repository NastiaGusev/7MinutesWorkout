package com.example.a7minutesworkout

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding: ActivityExerciseBinding? = null

    private var restProgress = 0
    private var exerciseProgress = 0
    private var restTimer: CountDownTimer? = null
    private var exerciseTimer: CountDownTimer? = null

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

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
        //Create the list of the exercises
        exerciseList = Constants.defaultExerciseList()

        binding?.toolBarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        tts = TextToSpeech(this, this)
        initPlayer()
        setUpRestView()
    }

    private fun initPlayer(){
        try{
            val soundURI = Uri.parse("android.resource://com.example.a7minutesworkout/" + R.raw.press_start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun setUpRestView() {
        try{
            player?.start()
        }catch (e: Exception){
            e.printStackTrace()
        }

        //change the view for the exercise
        binding?.frameLayoutProgressbarRest?.visibility = View.VISIBLE
        binding?.textviewTitle?.visibility = View.VISIBLE
        binding?.frameLayoutProgressbarExercise?.visibility = View.INVISIBLE
        binding?.textViewExerciseName?.visibility = View.INVISIBLE
        binding?.exerciseImage?.visibility = View.INVISIBLE

        //upcoming exercise set up
        if (currentExercisePosition < exerciseList?.size!! - 1){
            binding?.textViewUpcoming?.visibility = View.VISIBLE
            binding?.textViewUpcomingName?.visibility = View.VISIBLE
            binding?.textViewUpcomingName?.text = exerciseList!![currentExercisePosition + 1].getName()
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
        restTimer = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressbar?.progress = 10 - restProgress
                binding?.textviewTimer?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                //Go to the next exercise
                currentExercisePosition++
                setUpExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar() {
        binding?.progressbarExercise?.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressbarExercise?.progress = 30 - exerciseProgress
                binding?.textviewTimerExercise?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                if (currentExercisePosition < exerciseList?.size!! - 1) {
                    setUpRestView()
                } else {
                    Toast.makeText(
                        this@ExerciseActivity,
                        "Congrats you finished the workout!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }.start()
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
        if(player !=null){
            player!!.stop()
        }
        binding = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "The language specified is not supported!")
            }
        } else {
            Log.e("TTS", "Initialization failed")
        }
    }

    private fun speakOut(textToSpeak: String) {
        tts!!.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}