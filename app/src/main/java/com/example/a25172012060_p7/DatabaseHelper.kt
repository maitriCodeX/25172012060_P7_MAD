package com.example.a25172012060_p7

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "PersonDatabase.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "persons"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_LATITUDE = "latitude"
        const val COLUMN_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " TEXT PRIMARY KEY, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_PHONE + " TEXT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_ADDRESS + " TEXT, "
                + COLUMN_LATITUDE + " REAL, "
                + COLUMN_LONGITUDE + " REAL)")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertPerson(person: Person): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, person.id)
            put(COLUMN_NAME, person.name)
            put(COLUMN_PHONE, person.phoneNo)
            put(COLUMN_EMAIL, person.emailId)
            put(COLUMN_ADDRESS, person.address)
            put(COLUMN_LATITUDE, person.latitude)
            put(COLUMN_LONGITUDE, person.longitute)
        }
        val result = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE)
        db.close()
        return result
    }

    fun updatePerson(person: Person): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, person.name)
            put(COLUMN_PHONE, person.phoneNo)
            put(COLUMN_EMAIL, person.emailId)
            put(COLUMN_ADDRESS, person.address)
            put(COLUMN_LATITUDE, person.latitude)
            put(COLUMN_LONGITUDE, person.longitute)
        }
        val result = db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(person.id))
        db.close()
        return result
    }

    fun udatePerson(person: Person): Int {
        return updatePerson(person)
    }

    fun getPerson(id: String): Person? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id),
            null,
            null,
            null
        )
        var person: Person? = null
        if (cursor.moveToFirst()) {
            person = Person(
                id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                emailId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                phoneNo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                longitute = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE))
            )
        }
        cursor.close()
        db.close()
        return person
    }

    fun getAllPersons(): ArrayList<Person> {
        val personList = ArrayList<Person>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val person = Person(
                    id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    emailId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    phoneNo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                    address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                    latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                    longitute = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE))
                )
                personList.add(person)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return personList
    }

    fun deletePerson(id: String): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id))
        db.close()
        return result
    }

    fun deleteAllPersons(): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, null, null)
        db.close()
        return result
    }
}
