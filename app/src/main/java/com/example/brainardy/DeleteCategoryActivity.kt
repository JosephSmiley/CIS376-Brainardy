package com.example.brainardy

import android.annotation.SuppressLint
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
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.brainardy.ui.theme.BrainardyTheme

class DeleteCategoryActivity : ComponentActivity() {

    private lateinit var dbHelper: MyDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_category)

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

        var selectedCategoryId: Int = 0

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedCategoryId = id.toInt()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        val buttonDelete = findViewById<Button>(R.id.buttonDelete)
        buttonDelete.setOnClickListener {
            if (selectedCategoryId != 0) {
                dbHelper.deleteCategoryAndQuestions(selectedCategoryId)
                Toast.makeText(this, "Category and related questions deleted", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Please select a category to delete", Toast.LENGTH_SHORT).show()
            }
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
}
