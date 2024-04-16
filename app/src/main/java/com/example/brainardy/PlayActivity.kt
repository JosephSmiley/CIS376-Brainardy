package com.example.brainardy

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlayActivity : ComponentActivity() {

    private lateinit var categoryAdapter: CategoryAdapter
    private val selectedCategoryIds = HashSet<Int>()

    data class PlayActivityCategory(val id: Int, val name: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val btnBack = findViewById<Button>(R.id.buttonBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val nextButton: Button = findViewById(R.id.nextButton)
        nextButton.setOnClickListener {
            launchNextActivity()
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        categoryAdapter = CategoryAdapter(fetchCategoriesFromDatabase(this), onCategoryClickListener)
        recyclerView.adapter = categoryAdapter
    }

    private fun launchNextActivity() {
        val intent = Intent(this, PlayGameActivity::class.java)
        // Convert HashSet<Integer> into an ArrayList<Integer> to be able to put it as an integer array extra
        val selectedIdsArray = selectedCategoryIds.toIntArray()
        intent.putExtra("SELECTED_CATEGORY_IDS", selectedIdsArray)
        startActivity(intent)
    }

    private val onCategoryClickListener = object : CategoryAdapter.OnCategoryClickListener {
        override fun onCategoryClicked(category: PlayActivityCategory) {
            if (selectedCategoryIds.contains(category.id)) {
                selectedCategoryIds.remove(category.id)
            } else {
                if (selectedCategoryIds.size < 3) {
                    selectedCategoryIds.add(category.id)
                } else {
                    Toast.makeText(this@PlayActivity, "You can only select up to 3 categories", Toast.LENGTH_SHORT).show()
                }
            }
            categoryAdapter.notifyDataSetChanged()
        }
    }
    private fun fetchCategoriesFromDatabase(context: Context): List<PlayActivityCategory> {
        val dbHelper = MyDBHelper(context)
        val categories = mutableListOf<PlayActivityCategory>()

        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            Category.COLUMN_CATEGORY_ID,
            Category.COLUMN_CATEGORY_NAME
        )

        val cursor: Cursor? = db.query(
            Category.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val categoryId = it.getInt(it.getColumnIndexOrThrow(Category.COLUMN_CATEGORY_ID))
                val categoryName = it.getString(it.getColumnIndexOrThrow(Category.COLUMN_CATEGORY_NAME))
                categories.add(PlayActivityCategory(categoryId, categoryName))
                Log.d( "tag", categoryName)
                Log.d("tag2", categoryId.toString())
            }
        }

        return categories
    }
}

class CategoryAdapter(private val categories: List<PlayActivity.PlayActivityCategory>, private val clickListener: OnCategoryClickListener) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private val selectedCategoryIds = HashSet<Int>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryNameTextView: TextView = itemView.findViewById(R.id.categoryNameTextView)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryNameTextView.text = category.name
        holder.checkBox.isChecked = selectedCategoryIds.contains(category.id)

        holder.itemView.setOnClickListener {
            if (selectedCategoryIds.contains(category.id)) {
                selectedCategoryIds.remove(category.id)
                holder.checkBox.isChecked = false
            } else {
                if (selectedCategoryIds.size < 3) {
                    selectedCategoryIds.add(category.id)
                    holder.checkBox.isChecked = true
                } else {
                    Toast.makeText(holder.itemView.context, "You can only select up to 3 categories", Toast.LENGTH_SHORT).show()
                }
            }
            clickListener.onCategoryClicked(category)
        }

        holder.checkBox.setOnClickListener {
            holder.itemView.performClick()
        }
    }

    interface OnCategoryClickListener {
        fun onCategoryClicked(category: PlayActivity.PlayActivityCategory)
    }


    override fun getItemCount(): Int {
        return categories.size
    }

    fun getSelectedCategoryIds(): Set<Int> {
        return selectedCategoryIds
    }
}

