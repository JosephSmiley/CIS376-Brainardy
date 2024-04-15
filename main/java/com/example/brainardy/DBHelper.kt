package com.example.brainardy

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
class MyDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "mydatabase.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Account.CREATE_TABLE_QUERY)
        db.execSQL(Category.CREATE_TABLE_QUERY)
        db.execSQL(Question.CREATE_TABLE_QUERY)

        insertSampleData(db)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(Account.DELETE_TABLE_QUERY)
        db.execSQL(Category.DELETE_TABLE_QUERY)
        db.execSQL(Question.DELETE_TABLE_QUERY)
        onCreate(db)
    }

    private fun insertSampleData(db: SQLiteDatabase) {
        val adminValues = ContentValues().apply {
            put(Account.COLUMN_EMAIL, "admin@example.com")
            put(Account.COLUMN_PASSWORD, "admin123")
        }
        db.insert(Account.TABLE_NAME, null, adminValues)

        val categories = arrayOf(
            "Science", "History", "Geography", "Literature", "Movies"
        )

        val questions = arrayOf(
            arrayOf(
                "What is the boiling point of water?", "100°C",
                "What is the freezing point of water?", "0°C",
                "What is the chemical symbol for water?", "H2O",
                "What is the largest planet in our solar system?", "Jupiter",
                "What is the speed of light?", "299,792,458 meters per second"
            ),
            arrayOf(
                "Who was the first president of the United States?", "George Washington",
                "When did World War I end?", "November 11, 1918",
                "Who wrote 'The Communist Manifesto'?", "Karl Marx",
                "Who painted the Mona Lisa?", "Leonardo da Vinci",
                "When was the Declaration of Independence signed?", "July 4, 1776"
            ),
            arrayOf(
                "What is the capital of France?", "Paris",
                "What is the largest ocean on Earth?", "Pacific Ocean",
                "Which country is known as the Land of the Rising Sun?", "Japan",
                "What is the longest river in the world?", "Nile River",
                "What is the tallest mountain in the world?", "Mount Everest"
            ),
            arrayOf(
                "Who wrote 'To Kill a Mockingbird'?", "Harper Lee",
                "Who wrote '1984'?", "George Orwell",
                "Who is the author of 'Pride and Prejudice'?", "Jane Austen",
                "Who wrote 'The Great Gatsby'?", "F. Scott Fitzgerald",
                "Who wrote 'Romeo and Juliet'?", "William Shakespeare"
            ),
            arrayOf(
                "Who directed the movie 'The Shawshank Redemption'?", "Frank Darabont",
                "Who directed the movie 'The Godfather'?", "Francis Ford Coppola",
                "Who directed the movie 'Titanic'?", "James Cameron",
                "Who directed the movie 'Jurassic Park'?", "Steven Spielberg",
                "Who directed the movie 'Inception'?", "Christopher Nolan"
            )
        )

        for (i in categories.indices) {
            val categoryId = i + 1
            val categoryValues = ContentValues().apply {
                put(Category.COLUMN_CATEGORY_NAME, categories[i])
                put(Category.COLUMN_CATEGORY_ID, categoryId)
                put(Category.COLUMN_OWNER_EMAIL, "admin@example.com")
            }
            db.insert(Category.TABLE_NAME, null, categoryValues)

            for (j in questions[i].indices step 2) {
                val questionValues = ContentValues().apply {
                    put(Question.COLUMN_CATEGORY_ID, categoryId)
                    put(Question.COLUMN_QUESTION, questions[i][j])
                    put(Question.COLUMN_ANSWER, questions[i][j + 1])
                }
                db.insert(Question.TABLE_NAME, null, questionValues)
            }
        }
    }

    fun deleteCategoryAndQuestions(categoryId: Int) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            db.delete(Question.TABLE_NAME, "${Question.COLUMN_CATEGORY_ID} = ?", arrayOf(categoryId.toString()))
            db.delete(Category.TABLE_NAME, "${Category.COLUMN_CATEGORY_ID} = ?", arrayOf(categoryId.toString()))
            db.setTransactionSuccessful()
        } catch (e: Exception) {

        } finally {
            db.endTransaction()
        }
    }
}

object Account {
    const val TABLE_NAME = "Account"
    const val COLUMN_EMAIL = "email"
    const val COLUMN_PASSWORD = "password"

    const val CREATE_TABLE_QUERY =
        "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_EMAIL VARCHAR PRIMARY KEY," +
                "$COLUMN_PASSWORD VARCHAR)"

    const val DELETE_TABLE_QUERY =
        "DROP TABLE IF EXISTS $TABLE_NAME"
}

object Category {
    const val TABLE_NAME = "Category"
    const val COLUMN_CATEGORY_NAME = "categoryName"
    const val COLUMN_CATEGORY_ID = "categoryID"
    const val COLUMN_OWNER_EMAIL = "ownerEmail"

    const val CREATE_TABLE_QUERY =
        "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_CATEGORY_NAME VARCHAR," +
                "$COLUMN_CATEGORY_ID INTEGER PRIMARY KEY," +
                "$COLUMN_OWNER_EMAIL VARCHAR," +
                "FOREIGN KEY ($COLUMN_OWNER_EMAIL) REFERENCES ${Account.TABLE_NAME}(${Account.COLUMN_EMAIL}))"

    const val DELETE_TABLE_QUERY =
        "DROP TABLE IF EXISTS $TABLE_NAME"
}

object Question {
    const val TABLE_NAME = "Question"
    const val COLUMN_CATEGORY_ID = "categoryID"
    const val COLUMN_QUESTION = "question"
    const val COLUMN_ANSWER = "answer"
    const val COLUMN_QUESTION_ID = "questionID"

    const val CREATE_TABLE_QUERY =
        "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_CATEGORY_ID INTEGER," +
                "$COLUMN_QUESTION VARCHAR," +
                "$COLUMN_ANSWER VARCHAR," +
                "$COLUMN_QUESTION_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FOREIGN KEY ($COLUMN_CATEGORY_ID) REFERENCES ${Category.TABLE_NAME}(${Category.COLUMN_CATEGORY_ID}))"

    const val DELETE_TABLE_QUERY =
        "DROP TABLE IF EXISTS $TABLE_NAME"
}