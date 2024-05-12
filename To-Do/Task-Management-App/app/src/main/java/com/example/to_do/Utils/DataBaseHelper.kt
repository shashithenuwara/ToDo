package com.example.to_do.Utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.to_do.Model.ToDoModel

class DataBaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object { // creating constant variables
        private const val DATABASE_NAME = "TODO_DATABASE"
        private const val TABLE_NAME = "TODO_TABLE"
        private const val COL_1 = "ID"
        private const val COL_2 = "TASK"
        private const val COL_3 = "STATUS"
    }

    private lateinit var db: SQLiteDatabase

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME " +
                    "($COL_1 INTEGER PRIMARY KEY AUTOINCREMENT, $COL_2 TEXT, $COL_3 INTEGER)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertTask(model: ToDoModel) {
        db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_2, model.task)
            put(COL_3, 0)
        }
        db.insert(TABLE_NAME, null, values)
    }

    fun updateTask(id: Int, task: String) {
        db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_2, task)
        }
        db.update(TABLE_NAME, values, "ID=?", arrayOf(id.toString()))
    }

    fun updateStatus(id: Int, status: Int) {
        db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_3, status)
        }
        db.update(TABLE_NAME, values, "ID=?", arrayOf(id.toString()))
    }

    fun deleteTask(id: Int) {
        db = this.writableDatabase
        db.delete(TABLE_NAME, "ID=?", arrayOf(id.toString()))
    }

    @SuppressLint("Range")
    fun getAllTasks(): List<ToDoModel> {
        db = this.writableDatabase
        var cursor: Cursor? = null
        val modelList = mutableListOf<ToDoModel>() // get as an array list

        db.beginTransaction()
        try {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
            cursor?.let { // cursor can be nullable
                if (it.moveToFirst()) { // it = name of cursor
                    do {
                        val task = ToDoModel().apply {
                            id = it.getInt(it.getColumnIndex(COL_1))
                            this.task = it.getString(it.getColumnIndex(COL_2))
                            status = it.getInt(it.getColumnIndex(COL_3))
                        }
                        modelList.add(task)
                    } while (it.moveToNext())
                }
            }
        } finally {
            db.endTransaction()
            cursor?.close()
        }
        return modelList
    }
}
