package com.example.a7minutesworkout

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding
import com.example.a7minutesworkout.databinding.ActivityMainBinding

class ExerciseActivity : AppCompatActivity() {

    private var binding: ActivityExerciseBinding? = null

    private var restProgress = 0
    private var exerciseProgress = 0
    private var restTimer: CountDownTimer? = null
    private var exerciseTimer: CountDownTimer? = null

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

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

        setUpRestView()
    }

    private fun setUpRestView() {
        //change the view for the exercise
        binding?.frameLayoutProgressbarRest?.visibility = View.VISIBLE
        binding?.textviewTitle?.visibility = View.VISIBLE
        binding?.frameLayoutProgressbarExercise?.visibility = View.INVISIBLE
        binding?.textViewExerciseName?.visibility = View.INVISIBLE
        binding?.exerciseImage?.visibility = View.INVISIBLE
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
        binding?.frameLayoutProgressbarExercise?.visibility = View.VISIBLE
        binding?.textViewExerciseName?.visibility = View.VISIBLE
        binding?.exerciseImage?.visibility = View.VISIBLE

        //check if there is existing timer
        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        //set the name and image for the current exercise
        binding?.exerciseImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.textViewExerciseName?.text = exerciseList!![currentExercisePosition].getName()
        setExerciseProgressBar()
    }

    private fun setRestProgressBar() {
        binding?.progressbar?.progress = restProgress
        restTimer = object : CountDownTimer(10000, 1000) {
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
        exerciseTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressbarExercise?.progress = 30 - exerciseProgress
                binding?.textviewTimerExercise?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                if(currentExercisePosition < exerciseList?.size!!-1){
                    setUpRestView()
                }else{
                    Toast.makeText(this@ExerciseActivity,"Congrats you finished the workout!", Toast.LENGTH_LONG).show()
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
        binding = null
    }
}