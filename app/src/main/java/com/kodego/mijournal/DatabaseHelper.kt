package com.kodego.mijournal

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper (context: Context): SQLiteOpenHelper(context, "diary.db", null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable: String = "CREATE TABLE STORY_TABLE (id integer primary key autoincrement, title varchar(30), date varchar(20),story varchar(150))"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
    fun updateOne (storyModel: StoryModel, id:Int){
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("title", storyModel.title)
        cv.put("date", storyModel.date)
        cv.put("story", storyModel.story)
        db.update("STORY_TABLE", cv,"id = ?", arrayOf(id.toString()))
    }

    fun addOne (storyModel: StoryModel){
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("title", storyModel.title)
        cv.put("date",storyModel.date)
        cv.put("story", storyModel.story)

        db.insert("STORY_TABLE",null, cv)
    }

    fun getAllData(): MutableList<StoryModel>{
        val returnList = ArrayList<StoryModel>()
        val queryString = "SELECT * FROM STORY_TABLE"
        val db = this.readableDatabase

        val cursor: Cursor = db.rawQuery(queryString, null)

        if (cursor.moveToFirst()){
            do {
                val id = cursor.getInt(0)
                val title = cursor.getString(1)
                val date = cursor.getString(2)
                val story = cursor.getString(3)

                val newStoryModel : StoryModel = StoryModel(id, title, date, story)
                returnList.add(newStoryModel)
            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return returnList
    }
    fun deleteOne(storyModel: StoryModel){
        val db = this.writableDatabase
        val queryString = "DELETE FROM STORY_TABLE WHERE id = ${storyModel.id}"
        val cursor = db.rawQuery(queryString,null)
        cursor.moveToFirst()
    }

}