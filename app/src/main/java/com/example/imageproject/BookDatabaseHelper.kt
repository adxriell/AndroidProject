package com.example.imageproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BookDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 2 // Incremented version
        private const val TABLE_NAME = "allbooks"
        private const val COLUMN_ID = "id"
        private const val COLUMN_Nama = "nama"
        private const val COLUMN_Namapang = "namapang"
        private const val COLUMN_Email = "email"
        private const val COLUMN_Alamat = "alamat"
        private const val COLUMN_TglLahir = "tglLahir"
        private const val COLUMN_Telepon = "telp"
        private const val COLUMN_Photo = "photo"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME(" +
                "$COLUMN_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_Nama TEXT, " +
                "$COLUMN_Namapang TEXT, " +
                "$COLUMN_Email TEXT, " +
                "$COLUMN_Alamat TEXT, " +
                "$COLUMN_TglLahir DATE, " +
                "$COLUMN_Telepon TEXT, " +
                "$COLUMN_Photo BLOB)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_Photo BLOB")
        }
    }


    fun insertNote(book: Book): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_Nama, book.nama)
            put(COLUMN_Namapang, book.namapang)
            put(COLUMN_Email, book.email)
            put(COLUMN_Alamat, book.alamat)
            put(COLUMN_TglLahir, book.tglLahir)
            put(COLUMN_Telepon, book.telp)
            put(COLUMN_Photo, book.photo)
        }

        // Insert the book into the database
        val result = db.insert(TABLE_NAME, null, values)
        db.close()

        // Return true if the insertion was successful (result is not -1)
        return result != -1L
    }


    fun getAllNotes(): List<Book> {
        val notesList = mutableListOf<Book>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME" // Note the space after "SELECT"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val nama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Nama))
            val namapang = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Namapang))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Email))
            val alamat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Alamat))
            val tglLahir = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TglLahir))
            val telp = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Telepon))
            val photo = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_Photo)) // Get photo blob

            val book = Book(id, nama, namapang, email, alamat, tglLahir, telp, photo)
            notesList.add(book)
        }
        cursor.close()
        db.close()
        return notesList
    }

    fun updateBook(book: Book){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_Nama, book.nama)
            put(COLUMN_Namapang, book.namapang)
            put(COLUMN_Email, book.email)
            put(COLUMN_Alamat, book.alamat)
            put(COLUMN_TglLahir, book.tglLahir)
            put(COLUMN_Telepon, book.telp)
            put(COLUMN_Photo, book.photo)
        }
        val whereClause = "$COLUMN_ID =?"
        val whereArgs = arrayOf(book.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun getBookByID(bookId: Int): Book{
        val db = readableDatabase
        val query = "SELECT *FROM $TABLE_NAME WHERE $COLUMN_ID = $bookId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val nama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Nama))
        val namapang = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Namapang))
        val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Email))
        val alamat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Alamat))
        val tglLahir = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TglLahir))
        val telp = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Telepon))
        val photo = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_Photo))

        cursor.close()
        db.close()
        return Book(id, nama, namapang, email, alamat, tglLahir, telp, photo)
    }

    fun deleteBook(bookId : Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(bookId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }




}