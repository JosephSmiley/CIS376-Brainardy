package com.example.brainardy

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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

class PlayGameActivity : ComponentActivity() {

    private lateinit var dbHelper: MyDBHelper
    private var totalPoints = 0
    private var questionsAnswered = 0
    private val askedQuestionsMap: MutableMap<Int, MutableSet<Int>> = mutableMapOf()


    interface QuestionAnsweredListener {
        fun onQuestionAnswered(isCorrect: Boolean)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_game)

        val dbHelper = MyDBHelper(this)

        val selectedCategoryIds = intent.getIntArrayExtra("SELECTED_CATEGORY_IDS") ?: intArrayOf()

        val categoryNameTextView1 = findViewById<TextView>(R.id.categoryName1)
        val categoryNameTextView2 = findViewById<TextView>(R.id.categoryName2)
        val categoryNameTextView3 = findViewById<TextView>(R.id.categoryName3)

        categoryNameTextView1.text =
            dbHelper.getCategoryNameById(selectedCategoryIds[0]) ?: "Category 1"
        categoryNameTextView2.text =
            dbHelper.getCategoryNameById(selectedCategoryIds[1]) ?: "Category 2"
        categoryNameTextView3.text =
            dbHelper.getCategoryNameById(selectedCategoryIds[2]) ?: "Category 3"

        val buttonCatOne100 = findViewById<Button>(R.id.buttonCat1_100)
        val buttonCatOne200 = findViewById<Button>(R.id.buttonCat1_200)
        val buttonCatOne300 = findViewById<Button>(R.id.buttonCat1_300)
        val buttonCatOne400 = findViewById<Button>(R.id.buttonCat1_400)
        val buttonCatOne500 = findViewById<Button>(R.id.buttonCat1_500)

        buttonCatOne100.setOnClickListener {
            showQuestionPopup(this, selectedCategoryIds[0], 100, object : QuestionAnsweredListener {
                override fun onQuestionAnswered(isCorrect: Boolean) {
                    val statusText = if (isCorrect) "CORRECT 100" else "WRONG"
                    buttonCatOne100.text = statusText
                    buttonCatOne100.isEnabled = false
                }
            })
        }

        buttonCatOne200.setOnClickListener {
            showQuestionPopup(this, selectedCategoryIds[0], 200, object : QuestionAnsweredListener {
                override fun onQuestionAnswered(isCorrect: Boolean) {
                    val statusText = if (isCorrect) "CORRECT 200" else "WRONG"
                    buttonCatOne200.text = statusText
                    buttonCatOne200.isEnabled = false
                }
            })
        }

        buttonCatOne300.setOnClickListener {
            showQuestionPopup(this, selectedCategoryIds[0], 300, object : QuestionAnsweredListener {
                override fun onQuestionAnswered(isCorrect: Boolean) {
                    val statusText = if (isCorrect) "CORRECT 300" else "WRONG"
                    buttonCatOne300.text = statusText
                    buttonCatOne300.isEnabled = false
                }
            })
        }

        buttonCatOne400.setOnClickListener {
            showQuestionPopup(this, selectedCategoryIds[0], 400, object : QuestionAnsweredListener {
                override fun onQuestionAnswered(isCorrect: Boolean) {
                    val statusText = if (isCorrect) "CORRECT 400" else "WRONG"
                    buttonCatOne400.text = statusText
                    buttonCatOne400.isEnabled = false
                }
            })
        }

        buttonCatOne500.setOnClickListener {
            showQuestionPopup(this, selectedCategoryIds[0], 500, object : QuestionAnsweredListener {
                override fun onQuestionAnswered(isCorrect: Boolean) {
                    val statusText = if (isCorrect) "CORRECT 500" else "WRONG"
                    buttonCatOne500.text = statusText
                    buttonCatOne500.isEnabled = false
                }
            })
        }

        val buttonCatTwo100 = findViewById<Button>(R.id.buttonCat2_100)
        val buttonCatTwo200 = findViewById<Button>(R.id.buttonCat2_200)
        val buttonCatTwo300 = findViewById<Button>(R.id.buttonCat2_300)
        val buttonCatTwo400 = findViewById<Button>(R.id.buttonCat2_400)
        val buttonCatTwo500 = findViewById<Button>(R.id.buttonCat2_500)

        buttonCatTwo100.setOnClickListener {
            showQuestionPopup(this, selectedCategoryIds[1], 100, object : QuestionAnsweredListener {
                override fun onQuestionAnswered(isCorrect: Boolean) {
                    val statusText = if (isCorrect) "CORRECT 100" else "WRONG"
                    buttonCatTwo100.text = statusText
                    buttonCatTwo100.isEnabled = false
                }
            })
        }

        buttonCatTwo200.setOnClickListener {
            showQuestionPopup(this, selectedCategoryIds[1], 200, object : QuestionAnsweredListener {
                override fun onQuestionAnswered(isCorrect: Boolean) {
                    val statusText = if (isCorrect) "CORRECT 200" else "WRONG"
                    buttonCatTwo200.text = statusText
                    buttonCatTwo200.isEnabled = false
                }
            })
        }

        buttonCatTwo300.setOnClickListener {
            showQuestionPopup(this, selectedCategoryIds[1], 300, object : QuestionAnsweredListener {
                override fun onQuestionAnswered(isCorrect: Boolean) {
                    val statusText = if (isCorrect) "CORRECT 300" else "WRONG"
                    buttonCatTwo300.text = statusText
                    buttonCatTwo300.isEnabled = false
                }
            })
        }

        buttonCatTwo400.setOnClickListener {
            showQuestionPopup(this, selectedCategoryIds[1], 400, object : QuestionAnsweredListener {
                override fun onQuestionAnswered(isCorrect: Boolean) {
                    val statusText = if (isCorrect) "CORRECT 400" else "WRONG"
                    buttonCatTwo400.text = statusText
                    buttonCatTwo400.isEnabled = false
                }
            })
        }

        buttonCatTwo500.setOnClickListener {
            showQuestionPopup(this, selectedCategoryIds[1], 500, object : QuestionAnsweredListener {
                override fun onQuestionAnswered(isCorrect: Boolean) {
                    val statusText = if (isCorrect) "CORRECT 500" else "WRONG"
                    buttonCatTwo500.text = statusText
                    buttonCatTwo500.isEnabled = false
                }
            })
        }

        val buttonCatThree100 = findViewById<Button>(R.id.buttonCat3_100)
        val buttonCatThree200 = findViewById<Button>(R.id.buttonCat3_200)
        val buttonCatThree300 = findViewById<Button>(R.id.buttonCat3_300)
        val buttonCatThree400 = findViewById<Button>(R.id.buttonCat3_400)
        val buttonCatThree500 = findViewById<Button>(R.id.buttonCat3_500)

        buttonCatThree100.setOnClickListener {
            showQuestionPopup(this, selectedCategoryIds[2], 100, object : QuestionAnsweredListener {
                override fun onQuestionAnswered(isCorrect: Boolean) {
                    val statusText = if (isCorrect) "CORRECT 100" else "WRONG"
                    buttonCatThree100.text = statusText
                    buttonCatThree100.isEnabled = false
                }
            })
        }

        buttonCatThree200.setOnClickListener {
            showQuestionPopup(this, selectedCategoryIds[2], 200, object : QuestionAnsweredListener {
                override fun onQuestionAnswered(isCorrect: Boolean) {
                    val statusText = if (isCorrect) "CORRECT 200" else "WRONG"
                    buttonCatThree200.text = statusText
                    buttonCatThree200.isEnabled = false
                }
            })
        }

        buttonCatThree300.setOnClickListener {
            showQuestionPopup(this, selectedCategoryIds[2], 300, object : QuestionAnsweredListener {
                override fun onQuestionAnswered(isCorrect: Boolean) {
                    val statusText = if (isCorrect) "CORRECT 300" else "WRONG"
                    buttonCatThree300.text = statusText
                    buttonCatThree300.isEnabled = false
                }
            })
        }

        buttonCatThree400.setOnClickListener {
            showQuestionPopup(this, selectedCategoryIds[2], 400, object : QuestionAnsweredListener {
                override fun onQuestionAnswered(isCorrect: Boolean) {
                    val statusText = if (isCorrect) "CORRECT 400" else "WRONG"
                    buttonCatThree400.text = statusText
                    buttonCatThree400.isEnabled = false
                }
            })
        }

        buttonCatThree500.setOnClickListener {
            showQuestionPopup(this, selectedCategoryIds[2], 500, object : QuestionAnsweredListener {
                override fun onQuestionAnswered(isCorrect: Boolean) {
                    val statusText = if (isCorrect) "CORRECT 500" else "WRONG"
                    buttonCatThree500.text = statusText
                    buttonCatThree500.isEnabled = false
                }
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showQuestionPopup(context: Context, categoryId: Int, points: Int, questionAnsweredListener: QuestionAnsweredListener) {
        val questionPopup = AlertDialog.Builder(context).create()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.question_dialog, null)
        questionPopup.setView(dialogView)

        val questionWithAnswers = MyDBHelper(context).getQuestionWithAnswers(categoryId)
        questionWithAnswers?.let { qwa ->
            val tvQuestion = dialogView.findViewById<TextView>(R.id.tvQuestion)
            tvQuestion.text = qwa.question

            val allAnswers = (qwa.incorrectAnswers + qwa.correctAnswer).shuffled()
            val buttons = listOf(
                dialogView.findViewById<Button>(R.id.btnAnswer1),
                dialogView.findViewById<Button>(R.id.btnAnswer2),
                dialogView.findViewById<Button>(R.id.btnAnswer3),
                dialogView.findViewById<Button>(R.id.btnAnswer4)
            )

            buttons.zip(allAnswers).forEach { (button, answer) ->
                button.text = answer
                button.setOnClickListener {
                    val isCorrect = answer == qwa.correctAnswer
                    if (isCorrect) {
                        totalPoints += points
                    }
                    questionsAnswered++
                    if (questionsAnswered == 15){
                        Toast.makeText(this, "Congratulations! You finished the game with: $totalPoints points", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this, "You currently have: $totalPoints points", Toast.LENGTH_SHORT).show()
                    }
                    questionAnsweredListener.onQuestionAnswered(isCorrect)
                    questionPopup.dismiss()
                }
            }
            questionPopup.show()
        }
    }
}