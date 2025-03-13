//package vcmsa.projects.activitytwo
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import vcmsa.projects.activitytwo.databinding.ActivityRegisterPageBinding
//
//class RegisterPage : AppCompatActivity() {
//
//    private lateinit var binding: ActivityRegisterPageBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//
//        binding = ActivityRegisterPageBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        enableEdgeToEdge()
//
//        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        binding.button3.setOnClickListener {
//            val username = binding.editTextText9.text.toString().trim()
//            val password = binding.editTextText10.text.toString().trim()
//            val confirmPassword = binding.editTextText11.text.toString().trim()
//
//            if (username.isEmpty()) {
//                binding.editTextText9.error = "Username is required"
//                return@setOnClickListener
//            }
//
//            if (password.isEmpty()) {
//                binding.editTextText10.error = "Password is required"
//                return@setOnClickListener
//            }
//
//            if (confirmPassword.isEmpty()) {
//                binding.editTextText11.error = "Confirm Password is required"
//                return@setOnClickListener
//            }
//
//
//            if (password != confirmPassword) {
//                Toast.makeText(this, "Password fields do not match", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            Toast.makeText(this, "You are registered!", Toast.LENGTH_SHORT).show()
//
//            val intent = Intent(this, EventCreation::class.java)
//            startActivity(intent)
//            finish()
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
import vcmsa.projects.activitytwo.databinding.ActivityRegisterPageBinding

class RegisterPage : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.button3.setOnClickListener {
            val username = binding.editTextText9.text.toString().trim()
            val password = binding.editTextText10.text.toString().trim()
            val confirmPassword = binding.editTextText11.text.toString().trim()

            if (validateInput(username, password, confirmPassword)) {
                lifecycleScope.launch {
                    try {
                        val database = AppDatabase.getDatabase(applicationContext)
                        val existingUser = database.userDao().getUserByUsername(username)

                        if (existingUser != null) {
                            runOnUiThread {
                                binding.editTextText9.error = "Username already exists"
                                Toast.makeText(
                                    this@RegisterPage,
                                    "Username already taken",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            database.userDao().insertUser(
                                UserEntity(
                                    username = username,
                                    password = password
                                )
                            )
                            runOnUiThread {
                                Toast.makeText(
                                    this@RegisterPage,
                                    "Registration successful!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this@RegisterPage, MainActivity::class.java))
                                finish()
                            }
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(
                                this@RegisterPage,
                                "Registration failed: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun validateInput(username: String, password: String, confirmPassword: String): Boolean {
        var isValid = true

        if (username.isEmpty()) {
            binding.editTextText9.error = "Username required"
            isValid = false
        }

        if (password.isEmpty()) {
            binding.editTextText10.error = "Password required"
            isValid = false
        }

        if (confirmPassword.isEmpty()) {
            binding.editTextText11.error = "Confirm password required"
            isValid = false
        }

        if (password != confirmPassword) {
            binding.editTextText11.error = "Passwords don't match"
            isValid = false
        }

        return isValid
    }
}