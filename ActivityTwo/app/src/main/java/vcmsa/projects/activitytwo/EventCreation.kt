package vcmsa.projects.activitytwo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.widget.Toast
import vcmsa.projects.activitytwo.databinding.ActivityEventCreationBinding

class EventCreation : AppCompatActivity() {
    private lateinit var binding: ActivityEventCreationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        setupSaveButton()
        handleWindowInsets()
    }

    private fun setupSaveButton() {
        binding.button2.setOnClickListener {
            saveEventToDatabase()
        }
    }

    private fun saveEventToDatabase() {
        val event = EventsEntity(
            title = binding.editTextText4.text.toString(),
            description = binding.editTextText5.text.toString(),
            date = binding.editTextText6.text.toString(),
            time = binding.editTextText7.text.toString(),
            location = binding.editTextText8.text.toString()
        )

        if (validateInput(event)) {
            lifecycleScope.launch {
                try {
                    AppDatabase.getDatabase(applicationContext)
                        .eventDao()
                        .insertEvent(event)

                    runOnUiThread {
                        Toast.makeText(
                            this@EventCreation,
                            "Event saved!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@EventCreation,
                        "Error saving event: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun validateInput(event: EventsEntity): Boolean {
        return if (event.title.isBlank() || event.description.isBlank() ||
            event.date.isBlank() || event.time.isBlank() || event.location.isBlank()
        ) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    private fun handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}