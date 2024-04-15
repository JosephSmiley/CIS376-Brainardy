package com.example.brainardy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.brainardy.ui.theme.BrainardyTheme

class MainActivity : ComponentActivity() {

    private lateinit var dbHelper: MyDBHelper
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val ownerEmail = sharedPreferences.getString("userEmail", "")

        dbHelper = MyDBHelper(this)
        val db = dbHelper.readableDatabase

        val btnPlay = findViewById<Button>(R.id.buttonPlay)
        btnPlay.setOnClickListener {
            if (ownerEmail != "") {
                val intent = Intent(this, PlayActivity::class.java)
                startActivity(intent)
            }
            else
                Toast.makeText(this, "You are not logged in", Toast.LENGTH_SHORT).show()
        }

        val btnEdit = findViewById<Button>(R.id.buttonEditQuestions)
        btnEdit.setOnClickListener {
            if (ownerEmail != "") {
                val intent = Intent(this, EditActivity::class.java)
                startActivity(intent)
            }
            else
                Toast.makeText(this, "Login to edit categories and questions", Toast.LENGTH_SHORT).show()
        }

        val btnLogin = findViewById<Button>(R.id.buttonSignUp)
        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val btnSignOut = findViewById<Button>(R.id.buttonSignOut)
        btnSignOut.setOnClickListener {
            if (ownerEmail != "") {
                sharedPreferences.edit().putString("userEmail", "").apply()
            }
            else
                Toast.makeText(this, "You are not logged in", Toast.LENGTH_SHORT).show()
        }
    }
}