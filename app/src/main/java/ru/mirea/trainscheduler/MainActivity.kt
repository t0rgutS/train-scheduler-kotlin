package ru.mirea.trainscheduler

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ru.mirea.trainscheduler.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ServiceLocator.init(this, lifecycleScope)
        if (getSharedPreferences(TrainSchedulerConstants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .getString(TrainSchedulerConstants.DEFAULT_CURRENCY_PREF, null) == null
        )
            getSharedPreferences(TrainSchedulerConstants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit().putString(TrainSchedulerConstants.DEFAULT_CURRENCY_PREF,
                    TrainSchedulerConstants.INITIAL_CURRENCY).apply()
    }
}