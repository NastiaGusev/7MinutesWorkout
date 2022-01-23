package com.example.a7minutesworkout.activities

import android.app.Application
import com.example.a7minutesworkout.room.HistoryDatabase

class WorkoutApp:Application() {
    val db by lazy {
        HistoryDatabase.getInstance(this)
    }
}