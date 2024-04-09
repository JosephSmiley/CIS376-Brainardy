package com.example.brainardy

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.ComponentActivity
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.brainardy.ui.theme.BrainardyTheme

class LoginActivity : ComponentActivity() {

    private lateinit var dbHelper: MyDBHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = MyDBHelper(this)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val buttonLogin: Button = findViewById(R.id.buttonLogin)
        buttonLogin.setOnClickListener {
            loginUser()
        }

        val buttonSignUp: Button = findViewById(R.id.buttonSignUp)
        buttonSignUp.setOnClickListener {
            showSignUpPopup()
        }
    }

    private fun loginUser() {
        val editTextEmail: EditText = findViewById(R.id.editTextEmail)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)

        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        if (validateCredentials(email, password)) {
            if (checkUserInDatabase(email, password)) {
                // User exists in the database, navigate to the main activity
                sharedPreferences.edit().putString("userEmail", email).apply()
                val emailStr = sharedPreferences.getString("userEmail", "")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "You are now logged in to email: $emailStr", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please enter valid email and password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateCredentials(email: String, password: String): Boolean {
        // Add your own validation logic here if needed
        return email.isNotEmpty() && password.isNotEmpty()
    }

    private fun checkUserInDatabase(email: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        val columns = arrayOf(Account.COLUMN_EMAIL, Account.COLUMN_PASSWORD)
        val selection = "${Account.COLUMN_EMAIL} = ? AND ${Account.COLUMN_PASSWORD} = ?"
        val selectionArgs = arrayOf(email, password)
        val cursor = db.query(Account.TABLE_NAME, columns, selection, selectionArgs, null, null, null)
        val userExists = cursor.count > 0
        cursor.close()
        db.close()
        return userExists
    }

    private fun showSignUpPopup() {
        val inflater: LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.popup_sign_up, null)

        val editTextEmail: EditText = view.findViewById(R.id.editTextEmail)
        val editTextPassword: EditText = view.findViewById(R.id.editTextPassword)
        val editTextReEnterPassword: EditText = view.findViewById(R.id.editTextReEnterPassword)
        val buttonConfirmSignUp: Button = view.findViewById(R.id.buttonConfirmSignUp)

        // Create the popup window
        val popupWindow = PopupWindow(
            view,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Dismiss the popup window when the background is clicked
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        buttonConfirmSignUp.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val reEnterPassword = editTextReEnterPassword.text.toString()

            if (password == reEnterPassword) {
                // Add account to the database
                if (addAccountToDatabase(email, password)) {
                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                    popupWindow.dismiss()
                } else {
                    Toast.makeText(this, "Failed to create account", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }

        // Show the popup window at the center of the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    private fun addAccountToDatabase(email: String, password: String): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(Account.COLUMN_EMAIL, email)
            put(Account.COLUMN_PASSWORD, password)
        }
        val newRowId = db.insert(Account.TABLE_NAME, null, values)
        db.close()
        return newRowId != -1L
    }
}
