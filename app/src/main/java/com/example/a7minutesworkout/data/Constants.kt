package com.example.a7minutesworkout.data

import com.example.a7minutesworkout.R

object Constants {

    fun defaultExerciseList(): ArrayList<ExerciseModel> {
        val exerciseModel = ArrayList<ExerciseModel>()

        val jumpingJacks = ExerciseModel(
            1,
            "Jumping Jacks",
            R.drawable.jumping_jacks,
            false,
            false
        )
        exerciseModel.add(jumpingJacks)

        val jumpingSquats = ExerciseModel(
            2,
            "Jumping Squats",
            R.drawable.jump_squats,
            false,
            false
        )
        exerciseModel.add(jumpingSquats)

        val squatThrusts = ExerciseModel(
            3,
            "Squat Thrusts",
            R.drawable.squat_thrust,
            false,
            false
        )
        exerciseModel.add(squatThrusts)

        val switchKicks = ExerciseModel(
            4,
            "Switch Kicks",
            R.drawable.switch_kicks,
            false,
            false
        )
        exerciseModel.add(switchKicks)

        val beltKicks = ExerciseModel(
            5,
            "Belt Kicks",
            R.drawable.belt_kicks,
            false,
            false
        )
        exerciseModel.add(beltKicks)

        val buttKicks = ExerciseModel(
            6,
            "Butt Kicks",
            R.drawable.butt_kicks,
            false,
            false
        )
        exerciseModel.add(buttKicks)

        val highKnees = ExerciseModel(
            7,
            "High Knees",
            R.drawable.high_knees,
            false,
            false
        )
        exerciseModel.add(highKnees)

        val donkeyKicks = ExerciseModel(
            8,
            "Donkey Kicks",
            R.drawable.donkey_kick,
            false,
            false
        )
        exerciseModel.add(donkeyKicks)

        val birdDog = ExerciseModel(
            9,
            "Bird Dog",
            R.drawable.bird_dog,
            false,
            false
        )
        exerciseModel.add(birdDog)

        val crunches = ExerciseModel(
            10,
            "Crunches",
            R.drawable.crunches,
            false,
            false
        )
        exerciseModel.add(crunches)

        val boxJumps = ExerciseModel(
            11,
            "Box Jumps",
            R.drawable.box_jumps,
            false,
            false
        )
        exerciseModel.add(boxJumps)

        val tricepsDips = ExerciseModel(
            12,
            "Triceps Dips",
            R.drawable.triceps_dips,
            false,
            false
        )
        exerciseModel.add(tricepsDips)
        return exerciseModel
    }
}