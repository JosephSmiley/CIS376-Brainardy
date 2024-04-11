package com.example.brainardy

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.SimpleCursorAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity


class EditCategoryActivity : ComponentActivity() {

    private lateinit var dbHelper: MyDBHelper
    var questionID1 = 0
    var questionID2 = 0
    var questionID3 = 0
    var questionID4 = 0
    var questionID5 = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)

        dbHelper = MyDBHelper(this)

        val btnBack = findViewById<Button>(R.id.buttonBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }

        val spinner = findViewById<Spinner>(R.id.spinnerCategory)

        val cursor = getSampleData()

        val fromColumns = arrayOf(Category.COLUMN_CATEGORY_NAME)

        val toViews = intArrayOf(android.R.id.text1)

        val cursorAdapter = SimpleCursorAdapter(
            this,
            android.R.layout.simple_spinner_item,
            cursor,
            fromColumns,
            toViews,
            0
        )

        cursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = cursorAdapter

        var selectedCategoryId = 0

        val editTextCategory = findViewById<EditText>(R.id.editTextCategory)
        val editTextQuestion1 = findViewById<EditText>(R.id.editTextQuestion1)
        val editTextAnswer1 = findViewById<EditText>(R.id.editTextAnswer1)
        val editTextQuestion2 = findViewById<EditText>(R.id.editTextQuestion2)
        val editTextAnswer2 = findViewById<EditText>(R.id.editTextAnswer2)
        val editTextQuestion3 = findViewById<EditText>(R.id.editTextQuestion3)
        val editTextAnswer3 = findViewById<EditText>(R.id.editTextAnswer3)
        val editTextQuestion4 = findViewById<EditText>(R.id.editTextQuestion4)
        val editTextAnswer4 = findViewById<EditText>(R.id.editTextAnswer4)
        val editTextQuestion5 = findViewById<EditText>(R.id.editTextQuestion5)
        val editTextAnswer5 = findViewById<EditText>(R.id.editTextAnswer5)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("Range")
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedCategoryId = id.toInt()

                clearEditTexts(
                    editTextCategory,
                    editTextQuestion1,
                    editTextAnswer1,
                    editTextQuestion2,
                    editTextAnswer2
                )

                val questionsCursor = getQuestionsForCategory(selectedCategoryId)
                if (questionsCursor.moveToFirst()) {
                    var count = 0
                    do {
                        when (count) {
                            0 -> {
                                questionID1 = questionsCursor.getInt(questionsCursor.getColumnIndex(Question.COLUMN_QUESTION_ID))
                                editTextQuestion1.setText(
                                    questionsCursor.getString(
                                        questionsCursor.getColumnIndex(
                                            Question.COLUMN_QUESTION
                                        )
                                    )
                                )
                                editTextAnswer1.setText(
                                    questionsCursor.getString(
                                        questionsCursor.getColumnIndex(
                                            Question.COLUMN_ANSWER
                                        )
                                    )
                                )
                            }

                            1 -> {
                                questionID2 = questionsCursor.getInt(questionsCursor.getColumnIndex(Question.COLUMN_QUESTION_ID))
                                editTextQuestion2.setText(
                                    questionsCursor.getString(
                                        questionsCursor.getColumnIndex(
                                            Question.COLUMN_QUESTION
                                        )
                                    )
                                )
                                editTextAnswer2.setText(
                                    questionsCursor.getString(
                                        questionsCursor.getColumnIndex(
                                            Question.COLUMN_ANSWER
                                        )
                                    )
                                )
                            }

                            2 -> {
                                questionID3 = questionsCursor.getInt(questionsCursor.getColumnIndex(Question.COLUMN_QUESTION_ID))
                                editTextQuestion3.setText(
                                    questionsCursor.getString(
                                        questionsCursor.getColumnIndex(
                                            Question.COLUMN_QUESTION
                                        )
                                    )
                                )
                                editTextAnswer3.setText(
                                    questionsCursor.getString(
                                        questionsCursor.getColumnIndex(
                                            Question.COLUMN_ANSWER
                                        )
                                    )
                                )
                            }

                            3 -> {
                                questionID4 = questionsCursor.getInt(questionsCursor.getColumnIndex(Question.COLUMN_QUESTION_ID))
                                editTextQuestion4.setText(
                                    questionsCursor.getString(
                                        questionsCursor.getColumnIndex(
                                            Question.COLUMN_QUESTION
                                        )
                                    )
                                )
                                editTextAnswer4.setText(
                                    questionsCursor.getString(
                                        questionsCursor.getColumnIndex(
                                            Question.COLUMN_ANSWER
                                        )
                                    )
                                )
                            }

                            4 -> {
                                questionID5 = questionsCursor.getInt(questionsCursor.getColumnIndex(Question.COLUMN_QUESTION_ID))
                                editTextQuestion5.setText(
                                    questionsCursor.getString(
                                        questionsCursor.getColumnIndex(
                                            Question.COLUMN_QUESTION
                                        )
                                    )
                                )
                                editTextAnswer5.setText(
                                    questionsCursor.getString(
                                        questionsCursor.getColumnIndex(
                                            Question.COLUMN_ANSWER
                                        )
                                    )
                                )
                            }
                        }


                        count++
                    } while (questionsCursor.moveToNext() && count < 5)
                }
                questionsCursor.close()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                clearEditTexts(
                    editTextCategory,
                    editTextQuestion1,
                    editTextAnswer1,
                    editTextQuestion2,
                    editTextAnswer2
                )
            }
        }
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)
        buttonSubmit.setOnClickListener {
            updateCategoryQuestions(selectedCategoryId)
        }
    }
    private fun getSampleData(): Cursor {
        val db = dbHelper.readableDatabase
        val projection = arrayOf("${Category.COLUMN_CATEGORY_ID} AS _id", Category.COLUMN_CATEGORY_NAME)
        return db.query(
            Category.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
    }
    private fun clearEditTexts(vararg editTexts: EditText) {
        editTexts.forEach { it.text.clear() }
    }
    private fun getQuestionsForCategory(categoryId: Int): Cursor {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            Question.COLUMN_QUESTION_ID,
            Question.COLUMN_CATEGORY_ID,
            Question.COLUMN_QUESTION,
            Question.COLUMN_ANSWER)
        val selection = "${Question.COLUMN_CATEGORY_ID} = ?"
        val selectionArgs = arrayOf(categoryId.toString())
        return db.query(
            Question.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
    }

    private fun updateCategoryQuestions(categoryId: Int) {
        val categoryNewName = findViewById<EditText>(R.id.editTextCategory).text.toString()

        val question1NewText = findViewById<EditText>(R.id.editTextQuestion1).text.toString()
        val answer1NewText = findViewById<EditText>(R.id.editTextAnswer1).text.toString()
        val question2NewText = findViewById<EditText>(R.id.editTextQuestion2).text.toString()
        val answer2NewText = findViewById<EditText>(R.id.editTextAnswer2).text.toString()
        val question3NewText = findViewById<EditText>(R.id.editTextQuestion3).text.toString()
        val answer3NewText = findViewById<EditText>(R.id.editTextAnswer3).text.toString()
        val question4NewText = findViewById<EditText>(R.id.editTextQuestion4).text.toString()
        val answer4NewText = findViewById<EditText>(R.id.editTextAnswer4).text.toString()
        val question5NewText = findViewById<EditText>(R.id.editTextQuestion5).text.toString()
        val answer5NewText = findViewById<EditText>(R.id.editTextAnswer5).text.toString()

        val db = dbHelper.writableDatabase

        val categoryValues = ContentValues()
        categoryValues.put(Category.COLUMN_CATEGORY_NAME, categoryNewName)
        db.update(Category.TABLE_NAME, categoryValues, "${Category.COLUMN_CATEGORY_ID} = ?", arrayOf(categoryId.toString()))
        val question1Values = ContentValues()
        question1Values.put(Question.COLUMN_QUESTION, question1NewText)
        question1Values.put(Question.COLUMN_ANSWER, answer1NewText)
        db.update(Question.TABLE_NAME, question1Values, "${Question.COLUMN_CATEGORY_ID} = ? AND [questionID] = ?", arrayOf(categoryId.toString(), questionID1.toString()))
        val question2Values = ContentValues()
        question2Values.put(Question.COLUMN_QUESTION, question2NewText)
        question2Values.put(Question.COLUMN_ANSWER, answer2NewText)
        db.update(Question.TABLE_NAME, question2Values, "${Question.COLUMN_CATEGORY_ID} = ? AND [questionID] = ?", arrayOf(categoryId.toString(), questionID2.toString()))
        val question3Values = ContentValues()
        question3Values.put(Question.COLUMN_QUESTION, question3NewText)
        question3Values.put(Question.COLUMN_ANSWER, answer3NewText)
        db.update(Question.TABLE_NAME, question3Values, "${Question.COLUMN_CATEGORY_ID} = ? AND [questionID] = ?", arrayOf(categoryId.toString(), questionID3.toString()))
        val question4Values = ContentValues()
        question4Values.put(Question.COLUMN_QUESTION, question4NewText)
        question4Values.put(Question.COLUMN_ANSWER, answer4NewText)
        db.update(Question.TABLE_NAME, question4Values, "${Question.COLUMN_CATEGORY_ID} = ? AND [questionID] = ?", arrayOf(categoryId.toString(), questionID4.toString()))
        val question5Values = ContentValues()
        question5Values.put(Question.COLUMN_QUESTION, question5NewText)
        question5Values.put(Question.COLUMN_ANSWER, answer5NewText)
        db.update(Question.TABLE_NAME, question5Values, "${Question.COLUMN_CATEGORY_ID} = ? AND [questionID] = ?", arrayOf(categoryId.toString(), questionID5.toString()))

        Toast.makeText(this, "Updated successfully!", Toast.LENGTH_SHORT).show()
    }
}