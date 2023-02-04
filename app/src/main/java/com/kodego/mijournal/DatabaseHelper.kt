package com.kodego.mijournal

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper (context: Context): SQLiteOpenHelper(context, "diary.db", null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable: String = "CREATE TABLE STORY_TABLE (id integer primary key autoincrement, title varchar(30), date varchar(20),story varchar(150))"
        val scheduleTable: String = "CREATE TABLE SCHEDULE_TABLE (id integer primary key autoincrement, event varchar(30), schedule varchar(20))"
        val notesTable: String = "CREATE TABLE NOTES_TABLE (id integer primary key autoincrement, title varchar(30), item varchar(150))"
        db?.execSQL(notesTable)
        db?.execSQL(scheduleTable)
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
    fun updateNoteOne(notesModel: NotesModel, id:Int){
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("title", notesModel.title)
        cv.put("item", notesModel.item)
        db.update("NOTES_TABLE", cv,"id = ?", arrayOf(id.toString()))
    }
    fun updateEventOne (scheduleModel: ScheduleModel, id: Int){
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("event", scheduleModel.event)
        cv.put("schedule", scheduleModel.schedule)
        db.update("SCHEDULE_TABLE", cv, "id = ?", arrayOf(id.toString()))
    }

    fun addOne (storyModel: StoryModel){
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("title", storyModel.title)
        cv.put("date",storyModel.date)
        cv.put("story", storyModel.story)
        db.insert("STORY_TABLE",null, cv)
    }
    fun addNOtesOne (notesModel: NotesModel){
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("title", notesModel.title)
        cv.put("item", notesModel.item)
        db.insert("NOTES_TABLE",null, cv)
    }
    fun addEventOne (scheduleModel: ScheduleModel){
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("event", scheduleModel.event)
        cv.put("schedule", scheduleModel.schedule)
        db.insert("SCHEDULE_TABLE", null, cv)
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
    fun getAllDataNotes(): MutableList<NotesModel>{
        val returnList = ArrayList<NotesModel>()
        val queryString = "SELECT * FROM NOTES_TABLE"
        val db = this.readableDatabase

        val cursor: Cursor = db.rawQuery(queryString, null)

        if (cursor.moveToFirst()){
            do {
                val id = cursor.getInt(0)
                val title = cursor.getString(1)
                val item = cursor.getString(2)

                val newNotesModel : NotesModel = NotesModel(id, title, item)
                returnList.add(newNotesModel)
            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return returnList
    }
    fun getAllDataSchedule(): MutableList<ScheduleModel>{
        val returnList = ArrayList<ScheduleModel>()
        val queriString = "SELECT * FROM SCHEDULE_TABLE"
        val db = this.readableDatabase
        val cursor : Cursor = db.rawQuery(queriString, null)
        if (cursor.moveToFirst()){
            do {
                val id = cursor.getInt(0)
                val event = cursor.getString(1)
                val schedule = cursor.getString(2)
                val newScheduleModel : ScheduleModel = ScheduleModel(id,event,schedule)
                returnList.add(newScheduleModel)
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
    fun deleteNotesOne(notesModel: NotesModel){
        val db = this.writableDatabase
        val queryString = "DELETE FROM NOTES_TABLE WHERE id = ${notesModel.id}"
        val cursor = db.rawQuery(queryString,null)
        cursor.moveToFirst()
    }
    fun deleteEventOne(scheduleModel: ScheduleModel){
        val db = this.writableDatabase
        val queryString = "DELETE FROM SCHEDULE_TABLE WHERE id = ${scheduleModel.id}"
        val cursor = db.rawQuery(queryString, null)
        cursor.moveToFirst()
    }

}