package com.example.a7minutesworkout.activities

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a7minutesworkout.R
import com.example.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNITS_VIEW"
        private const val US_UNIT_VIEW = "US_UNIT_VIEW"
    }

    private var binding: ActivityBmiBinding? = null
    private var currentVisibleView: String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //Set the action bar
        setSupportActionBar(binding?.toolbarBmi)
        //Activate back button
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }
        binding?.toolbarBmi?.setNavigationOnClickListener {
            onBackPressed()
        }

        makeVisibleMetricUnitsView()
        binding?.bmiRDG?.setOnCheckedChangeListener { _, checkedId: Int ->
            if (checkedId == R.id.RDBMetricUnits) {
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleUsUnitsView()
            }
        }

        binding?.bmiBTNCalculate?.setOnClickListener {
            calculateUnits()
        }
    }

    private fun calculateUnits() {
        if (currentVisibleView == METRIC_UNITS_VIEW) {
            if (validateMetricUnits()) {
                val height: Float = binding?.bmiEDTHeight?.text.toString().toFloat() / 100
                val weight: Float = binding?.bmiEDTWeight?.text.toString().toFloat()
                val bmi = weight / (height * height)
                displayBMIResults(bmi)
            } else {
                Toast.makeText(this@BMIActivity, "Enter values to calculate", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            if (validateUsUnits()) {
                val feet: Float = binding?.bmiEDTFeet?.text.toString().toFloat()
                val inch: Float = binding?.bmiEDTInch?.text.toString().toFloat()
                val weight: Float = binding?.bmiEDTUsWeight?.text.toString().toFloat()
                val height = inch + feet * 12
                val bmi = 703 * (weight / (height * height))
                displayBMIResults(bmi)
            } else {
                Toast.makeText(this@BMIActivity, "Enter values to calculate", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun makeVisibleMetricUnitsView() {
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.bmiMetricUnits?.visibility = View.VISIBLE
        binding?.bmiUsUnits?.visibility = View.INVISIBLE
        binding?.bmiLLResult?.visibility = View.INVISIBLE

        binding?.bmiEDTHeight?.text!!.clear()
        binding?.bmiEDTWeight?.text!!.clear()
    }

    private fun makeVisibleUsUnitsView() {
        currentVisibleView = US_UNIT_VIEW
        binding?.bmiMetricUnits?.visibility = View.INVISIBLE
        binding?.bmiUsUnits?.visibility = View.VISIBLE
        binding?.bmiLLResult?.visibility = View.INVISIBLE

        binding?.bmiEDTInch?.text!!.clear()
        binding?.bmiEDTFeet?.text!!.clear()
        binding?.bmiEDTUsWeight?.text!!.clear()
    }

    private fun validateMetricUnits(): Boolean {
        var isValid = true
        if (binding?.bmiEDTWeight?.text.toString().isEmpty()) {
            isValid = false
        } else if (binding?.bmiEDTHeight?.text.toString().isEmpty()) {
            isValid = false
        }
        return isValid
    }

    private fun validateUsUnits(): Boolean {
        var isValid = true
        when {
            binding?.bmiEDTUsWeight?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.bmiEDTFeet?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.bmiEDTInch?.text.toString().isEmpty() -> {
                isValid = false
            }
        }
        return isValid
    }

    private fun displayBMIResults(bmi: Float) {
        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight!"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "Severely underweight!"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of yourself! Workout more!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmiLabel = "Moderately obese"
            bmiDescription = "Oops! You really need to take care of yourself! Workout more!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {
            bmiLabel = "Severely obese"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Very severely obese"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }
        binding?.bmiLLResult?.visibility = View.VISIBLE
        val bmiVal = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()
        binding?.bmiTXTYourBmi?.text = bmiVal
        binding?.bmiTXTStatus?.text = bmiLabel
        binding?.bmiTXTAdvice?.text = bmiDescription
    }
}