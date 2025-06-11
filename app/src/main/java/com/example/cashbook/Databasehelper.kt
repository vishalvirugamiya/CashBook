package com.example.cashbook

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
class Databasehelper(context: Context) : SQLiteOpenHelper(context, "cashbook", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val query = """
            CREATE TABLE cashbook (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                Amount INTEGER,
                notes TEXT,
                dateYear TEXT,
                Time TEXT,
                type TEXT
            )
        """.trimIndent()
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun insertData(amount: String, notes: String, dateYear: String, time: String, type: String) {
        val query = "INSERT INTO cashbook (Amount, notes, dateYear, Time, type) VALUES('$amount', '$notes', '$dateYear', '$time', '$type')"
        writableDatabase.execSQL(query)
    }

    fun updateData(amount: String, notes: String, dateYear: String, time: String, type: String, id: Int) {
        val query = "UPDATE cashbook SET Amount='$amount', notes='$notes', dateYear='$dateYear', Time='$time', type='$type' WHERE id=$id"
        writableDatabase.execSQL(query)
    }

    fun deleteData(id: Int) {
        val deleteQuery = "DELETE FROM cashbook WHERE id=$id"
        writableDatabase.execSQL(deleteQuery)
    }

    fun fetchAllData(): ArrayList<UserData> {
        val query = "SELECT * FROM cashbook"
        val cursor = writableDatabase.rawQuery(query, null)
        val list = ArrayList<UserData>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val amount = cursor.getInt(cursor.getColumnIndexOrThrow("Amount"))
            val notes = cursor.getString(cursor.getColumnIndexOrThrow("notes"))
            val dateYear = cursor.getString(cursor.getColumnIndexOrThrow("dateYear"))
            val time = cursor.getString(cursor.getColumnIndexOrThrow("Time"))
            val type = cursor.getString(cursor.getColumnIndexOrThrow("type"))


            list.add(UserData(id, amount, notes, dateYear, time, type))
        }

        cursor.close()
        return list
    }
}

