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

/**
 * package com.example.cashbook
 *
 * import android.content.Context
 * import android.database.Cursor
 * import android.database.sqlite.SQLiteDatabase
 * import android.database.sqlite.SQLiteOpenHelper
 *
 * class Databasehelper(context: Context):SQLiteOpenHelper(context,"cashbook",null,1) {
 *     override fun onCreate(db: SQLiteDatabase?) {
 *
 *         var query="create table cashbook (id integer PRIMARY KEY AUTOINCREMENT, Amount integer ,notes Text ,dateYear Text, Time Text, type TEXT)"
 *         db!!.execSQL(query)
 *
 *     }
 *
 *     override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
 *
 *      fun insertData(Amount: String, notes: String, dateYear: String, Time: String, type: String) {
 *
 *          var insertQuary ="insert into cashbook (Amount,notes,dateYear,Time,type) values('$Amount','$notes','$dateYear','$Time','$type')"
 *          writableDatabase.execSQL(insertQuary)
 *      }
 *
 *     fun Ca_Out_Data(Amount: String, notes: String, dateYear: String, Time: String, type: String) {
 *         var insertQuary ="insert into cashbook (Amount,notes,dateYear,Time,type) values('$Amount','$notes','$dateYear','$Time','$type')"
 *         writableDatabase.execSQL(insertQuary)
 *     }
 *
 *     fun cashInData():ArrayList<UserData> {
 *
 *
 *         var SelectQuery ="select * from cashbook"
 *         var cursor : Cursor=writableDatabase.rawQuery(SelectQuery,null)
 *         var List = ArrayList<UserData>()
 *
 *         while (cursor.moveToNext()){
 *
 *             var idCol =cursor.getColumnIndex("id")
 *             var cashCol =cursor.getColumnIndex("Amount")
 *             var notCol =cursor.getColumnIndex("notes")
 *             var datYerCol =cursor.getColumnIndex("dateYear")
 *             var timeCol =cursor.getColumnIndex("Time")
 *             var typeCol =cursor.getColumnIndex("type")
 *
 *             var id:Int= cursor.getInt(idCol)
 *             var Amount = cursor.getInt(cashCol)
 *             var notes = cursor.getString(notCol)
 *             var dateYear = cursor.getString(datYerCol)
 *             var Time = cursor.getString(timeCol)
 *             var type = cursor.getString(typeCol)
 *
 *             var datta :UserData = UserData(id,Amount,notes,dateYear,Time,type)
 *             List.add(datta)
 *
 *         }
 *
 *         cursor.close()
 *
 *         return List
 *
 *     }
 *     fun UpdateData(Amount: String, notes: String, dateYear: String, Time: String, type: String, id: Int) {
 *
 *         var Query: String= "Update cashbook set tital='$Amount',notes ='$notes',dateYear='$dateYear',Time ='$Time',type='$type' where id =$id "
 *         writableDatabase.execSQL(Query)
 *
 *     }
 *
 *     fun DeletData(id: Int) {
 *         var deleteQuery = "delete from cashbook where id = $id"
 *         writableDatabase.execSQL(deleteQuery)
 *     }
 *
 * }
 */
