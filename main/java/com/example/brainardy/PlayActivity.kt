package com.example.brainardy

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.SimpleCursorAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity


class PlayActivity : ComponentActivity() {

    private lateinit var dbHelper: MyDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        dbHelper = MyDBHelper(this)
        val db = dbHelper.readableDatabase

        val btnBack = findViewById<Button>(R.id.buttonBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
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

        //val categoryList = findViewById<RecyclerView>(R.id.recyclerViewCategory)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("Range")
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedCategoryId = id.toInt()

                //spinner.remove(spinner.getSelectedItem() as String)
                //spinner.notifyDataSetChanged()

                //val categoryCursor = getCategoryName(selectedCategoryId)
                //categoryList.set
            }

            override fun onNothingSelected(adapterView: AdapterView<*>)
            {
                //val categoryCursor = getCategoryName(selectedCategoryId)
            }
        }

        val btnConfirm = findViewById<Button>(R.id.buttonConfirm)
        btnConfirm.setOnClickListener {
            //val selectedCat = getCategoryName(selectedCategoryId).toString();

            // Showing selected spinner item
            Toast.makeText(this@PlayActivity, "Selected: $", Toast.LENGTH_LONG).show()
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

    private fun getCategoryName(categoryId: Int): Cursor {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            Category.COLUMN_CATEGORY_NAME)
        val selection = "${Category.COLUMN_CATEGORY_ID} = ?"
        val selectionArgs = arrayOf(categoryId.toString())
        return db.query(
            Category.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
    }
}
