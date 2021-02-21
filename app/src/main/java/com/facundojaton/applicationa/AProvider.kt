package com.facundojaton.applicationa

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

class AProvider : ContentProvider() {

    companion object {
        val PROVIDER_NAME = "com.facundojaton.applicationa.AUTHORITY"
        val URL = "content://$PROVIDER_NAME/ACTABLE"
        val CONTENT_URI = Uri.parse(URL)
        val _ID = "_id"
        val NAME = "NAME"
        val MEANING = "MEANING"
        val TABLE_NAME = "ACTABLE"
    }

    lateinit var db: SQLiteDatabase

    override fun onCreate(): Boolean {
        context?.let {
            val helper = DatabaseHelper(it)
            db = helper.writableDatabase
            return true
        }
        return false
    }

    override fun query(
        uri: Uri,
        cols: Array<out String>?,
        condition: String?,
        conditionValues: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return db.query(TABLE_NAME,cols,condition,conditionValues,null,null,sortOrder)
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.dir/vnd.example.actable"
    }

    override fun insert(uri: Uri, cv: ContentValues?): Uri? {
        db.insert(TABLE_NAME,null,cv)
        context?.contentResolver?.notifyChange(uri, null)
        return uri
    }

    override fun delete(uri: Uri, condition: String?, conditionValues: Array<out String>?): Int {
        val count = db.delete("ACTABLE",condition,conditionValues)
        context?.contentResolver?.notifyChange(uri,null)
        return count
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        condition: String?,
        conditionValues: Array<out String>?
    ): Int {
        val count = db.update("ACTABLE",values,condition,conditionValues)
        context?.contentResolver?.notifyChange(uri,null)
        return count
    }
}