//package vcmsa.projects.activitytwo
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import vcmsa.projects.activitytwo.databinding.ActivityMainBinding
//
//class MainActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        enableEdgeToEdge()
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//
//        binding.button.setOnClickListener {
//            val username = binding.editTextText2.text.toString().trim()
//            val password = binding.editTextText3.text.toString().trim()
//
//            if (username.isEmpty()) {
//                binding.editTextText2.error = "Username is required"
//                return@setOnClickListener
//            }
//
//            if (password.isEmpty()) {
//                binding.editTextText3.error = "Password is required"
//                return@setOnClickListener
//            }
//
//            if (username == "Kyle" && password == "Password") {
//                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
//
//                val intent = Intent(this, EventCreation::class.java)
//                startActivity(intent)
//                finish()
//            } else {
//                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        binding.button4.setOnClickListener {
//            val intent = Intent(this, RegisterPage::class.java)
//            startActivity(intent)
//        }
//    }
//}

package vcmsa.projects.activitytwo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import vcmsa.projects.activitytwo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.button.setOnClickListener {
            val username = binding.editTextText2.text.toString().trim()
            val password = binding.editTextText3.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                if (username.isEmpty()) binding.editTextText2.error = "Username required"
                if (password.isEmpty()) binding.editTextText3.error = "Password required"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val database = AppDatabase.getDatabase(applicationContext)
                    val user = database.userDao().getUserByUsername(username)

                    runOnUiThread {
                        if (user != null && user.password == password) {
                            Toast.makeText(
                                this@MainActivity,
                                "Login successful!",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@MainActivity, EventCreation::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Invalid credentials",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "Login failed: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.button4.setOnClickListener {
            startActivity(Intent(this, RegisterPage::class.java))
        }
    }
}