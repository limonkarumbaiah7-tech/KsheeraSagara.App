package com.example.ksheerasagara.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ksheerasagara.data.local.dao.DairyDao
import com.example.ksheerasagara.data.local.entities.Cow
import com.example.ksheerasagara.data.local.entities.Expense
import com.example.ksheerasagara.data.local.entities.MilkSlip

@Database(entities = [MilkSlip::class, Expense::class, Cow::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dairyDao(): DairyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ksheera_sagara_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
