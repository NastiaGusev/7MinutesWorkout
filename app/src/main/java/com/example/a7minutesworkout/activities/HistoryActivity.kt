package com.example.a7minutesworkout.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.R
import com.example.a7minutesworkout.adapters.HistoryAdapter
import com.example.a7minutesworkout.databinding.ActivityBmiBinding
import com.example.a7minutesworkout.databinding.ActivityHistoryBinding
import com.example.a7minutesworkout.room.HistoryDao
import com.example.a7minutesworkout.room.HistoryEntity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private var binding: ActivityHistoryBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //Set the action bar
        setSupportActionBar(binding?.toolBarHistory)
        //Activate back button
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "EXERCISE HISTORY"
        }
        binding?.toolBarHistory?.setNavigationOnClickListener {
            onBackPressed()
        }

        val historyDao = (application as WorkoutApp).db.historyDao()
        getAllCompletedDates(historyDao)
    }

    private fun getAllCompletedDates(historyDao: HistoryDao) {
        lifecycleScope.launch {
            historyDao.fetchAllDates().collect { allCompletedDates ->
                if (allCompletedDates.isNotEmpty()) {
                    binding?.tvExercisesCompleted?.visibility = View.VISIBLE
                    binding?.rvItemsList?.visibility = View.VISIBLE
                    binding?.tvNoData?.visibility = View.INVISIBLE

                    binding?.rvItemsList?.layoutManager = LinearLayoutManager(this@HistoryActivity)
                    val dates = ArrayList<String>()
                    for (historyEntity in allCompletedDates) {
                        dates.add(historyEntity.date)
                    }
                    val historyAdapter = HistoryAdapter(
                        dates
                    ) { historyId -> deleteDateAlertDialog(historyId, historyDao) }
                    binding?.rvItemsList?.adapter = historyAdapter
                } else {
                    binding?.tvExercisesCompleted?.visibility = View.GONE
                    binding?.rvItemsList?.visibility = View.GONE
                    binding?.tvNoData?.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun deleteDateAlertDialog(id: String, historyDao: HistoryDao ){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Date")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes"){dialogInterface, _->
            lifecycleScope.launch {
                historyDao.delete(HistoryEntity(id))
                Toast.makeText(applicationContext, "Date deleted successfully!", Toast.LENGTH_LONG).show()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No"){dialogInterface, _->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}