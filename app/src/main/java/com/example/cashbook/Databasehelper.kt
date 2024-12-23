package com.example.cashbook

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Databasehelper(context: Context):SQLiteOpenHelper(context,"cashbook",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {

        var query="create table cashbook (id integer PRIMARY KEY AUTOINCREMENT, Amount Text,notes Text ,dateYear Text, Time Text)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

     fun insertData(Amount: String, notes: String, dateYear: String, Time: String) {
         var insertQuary ="insert into cashbook (Amount,notes,dateYear,Time) values('$Amount','$notes','$dateYear','$Time')"
         writableDatabase.execSQL(insertQuary)
     }

    fun cashInData():ArrayList<UserData> {

        var SelectQuery ="select * from cashbook"
        var cursor : Cursor=writableDatabase.rawQuery(SelectQuery,null)
        var List = ArrayList<UserData>()

        while (cursor.moveToNext()){

            var idCol =cursor.getColumnIndex("id")
            var cashCol =cursor.getColumnIndex("Amount")
            var notCol =cursor.getColumnIndex("notes")
            var datYerCol =cursor.getColumnIndex("dateYear")
            var timeCol =cursor.getColumnIndex("Time")

            var id:Int= cursor.getInt(idCol)
            var Amount = cursor.getString(cashCol)
            var notes = cursor.getString(notCol)
            var dateYear = cursor.getString(datYerCol)
            var Time = cursor.getString(timeCol)

            var datta :UserData = UserData(id,Amount,notes,dateYear,Time)
            List.add(datta)

        }
        cursor.close()

        return List
    }

}