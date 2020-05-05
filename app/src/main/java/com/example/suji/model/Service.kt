package com.example.suji.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.jetbrains.anko.db.*
import androidx.annotation.Nullable

val diaryLiveData = MutableLiveData<List<DiaryData>>()

data class DiaryData(
    @Nullable val id: Int,
    @NonNull val diaryId: String,
    val content: String,
    val writeDate: String,
    val uploaded: Int = 0,
    val modifiedDate: String,
    val isDeleted: Int = 0,
    val background: String = ""
)

object DiaryTable {
    const val table = "t_diary"
    const val id = "_id"
    const val diaryId = "diaryId"
    const val content = "content"
    const val writeDate = "writeDate"
    const val uploaded = "uploaded"
    const val modifiedDate = "modifiedDate"
    const val isDeleted = "isDeleted"
    const val background = "background"
}

inline fun <T> LiveData<T>.bindNonNull(
    lifecycleOwner: LifecycleOwner,
    crossinline block: (T) -> Unit
) = observe(lifecycleOwner, Observer { it?.let(block) })

class DatabaseHelper private constructor(context: Context) :
    ManagedSQLiteOpenHelper(context, "MyDatabase", null, 1) {
    init {
        instance = this
    }

    companion object {
        private var instance: DatabaseHelper? = null

        @Synchronized
        fun getInstance(context: Context) = instance
            ?: DatabaseHelper(context.applicationContext)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            DiaryTable.table, true,
            DiaryTable.id to INTEGER,
            DiaryTable.diaryId to TEXT + PRIMARY_KEY + UNIQUE + NOT_NULL,//2018-06-20
            DiaryTable.content to TEXT,
            DiaryTable.writeDate to TEXT,
            DiaryTable.uploaded to INTEGER + NOT_NULL + DEFAULT("0"),
            DiaryTable.modifiedDate to TEXT,
            DiaryTable.isDeleted to INTEGER + NOT_NULL + DEFAULT("0"),
            DiaryTable.background to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable("t_diary")
        onCreate(db)
    }

    fun postLiveData(date: String) = use {
        select(DiaryTable.table).whereArgs(
            DiaryTable.diaryId + " = {today}", "today" to date
        ).exec {
            diaryLiveData.postValue(parseList(object : MapRowParser<DiaryData> {
                override fun parseRow(columns: Map<String, Any?>): DiaryData {
                    return DiaryData(
                        id = columns[DiaryTable.id] as Int,
                        diaryId = columns[DiaryTable.diaryId] as String,
                        content = columns[DiaryTable.content] as String,
                        writeDate = columns[DiaryTable.writeDate] as String,
                        uploaded = columns[DiaryTable.uploaded] as Int,
                        modifiedDate = columns[DiaryTable.modifiedDate] as String,
                        isDeleted = columns[DiaryTable.isDeleted] as Int,
                        background = columns[DiaryTable.background] as String
                    )
                }
            }))
        }
    }
}

val Context.database: DatabaseHelper get() = DatabaseHelper.getInstance(applicationContext)