package ru.mirea.trainscheduler.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.mirea.trainscheduler.R
import ru.mirea.trainscheduler.ServiceLocator
import ru.mirea.trainscheduler.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ServiceLocator.init(this)
    }
}