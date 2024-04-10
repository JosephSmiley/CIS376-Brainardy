package com.example.brainardy

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

class CreateCategoryActivity : ComponentActivity() {

    private lateinit var dbHelper: MyDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_category)

        dbHelper = MyDBHelper(this)

        val editTextCategory: EditText = findViewById(R.id.editTextCategory)
        val editTextQuestions: Array<EditText> = arrayOf(
            findViewById(R.id.editTextQuestion1),
            findViewById(R.id.editTextQuestion2),
            findViewById(R.id.editTextQuestion3),
            findViewById(R.id.editTextQuestion4),
            findViewById(R.id.editTextQuestion5)
        )
        val editTextAnswers: Array<EditText> = arrayOf(
            findViewById(R.id.editTextAnswer1),
            findViewById(R.id.editTextAnswer2),
            findViewById(R.id.editTextAnswer3),
            findViewById(R.id.editTextAnswer4),
            findViewById(R.id.editTextAnswer5)
        )

        val btnBack = findViewById<Button>(R.id.buttonBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val buttonSubmit: Button = findViewById(R.id.buttonSubmit)
        buttonSubmit.setOnClickListener {
            val category = editTextCategory.text.toString().trim()
            val questions = mutableListOf<String>()
            val answers = mutableListOf<String>()

            // Retrieve questions and answers
            for (i in editTextQuestions.indices) {
                val question = editTextQuestions[i].text.toString().trim()
                val answer = editTextAnswers[i].text.toString().trim()
                if (question.isNotEmpty() && answer.isNotEmpty()) {
                    questions.add(question)
                    answers.add(answer)
                } else {
                    Toast.makeText(this, "Question and answer cannot be empty", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
            }
            // Insert into database
            if (category.isNotEmpty() && questions.isNotEmpty() && answers.isNotEmpty()) {
                addCategoryWithQuestions(category, questions, answers)
            } else {
                Toast.makeText(this, "Category, questions, and answers cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun addCategoryWithQuestions(category: String, questions: List<String>, answers: List<String>) {
        val db = dbHelper.writableDatabase
        val categoryId = getNextCategoryId(db)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val ownerEmail = sharedPreferences.getString("userEmail", "")

        // Insert category
        val categoryValues = ContentValues().apply {
            put(Category.COLUMN_CATEGORY_NAME, category)
            put(Category.COLUMN_CATEGORY_ID, categoryId)
            put(Category.COLUMN_OWNER_EMAIL, ownerEmail)
        }
        val categoryInserted = db.insert(Category.TABLE_NAME, null, categoryValues)

        // Insert questions
        for (i in questions.indices) {
            val questionValues = ContentValues().apply {
                put(Question.COLUMN_CATEGORY_ID, categoryId)
                put(Question.COLUMN_QUESTION, questions[i])
                put(Question.COLUMN_ANSWER, answers[i])
            }
            db.insert(Question.TABLE_NAME, null, questionValues)
        }

        if (categoryInserted != -1L) {
            Toast.makeText(this, "Category and questions added successfully", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Failed to add category and questions", Toast.LENGTH_SHORT).show()
        }

        db.close()
    }

    private fun getNextCategoryId(db: SQLiteDatabase): Int {
        val cursor: Cursor = db.rawQuery("SELECT MAX(${Category.COLUMN_CATEGORY_ID}) FROM ${Category.TABLE_NAME}", null)
        var categoryId = 0
        if (cursor.moveToFirst()) {
            categoryId = cursor.getInt(0)
        }
        cursor.close()
        return categoryId + 1
    }
}
