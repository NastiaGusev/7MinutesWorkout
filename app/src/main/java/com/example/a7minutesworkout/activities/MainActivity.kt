package com.example.a7minutesworkout.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a7minutesworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.mainFrameLayoutStart?.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
        }

        binding?.mainFrameLayoutBMI?.setOnClickListener {
            val intent = Intent(this, BMIActivity::class.java)
            startActivity(intent)
        }

        binding?.mainFrameLayoutHistory?.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}