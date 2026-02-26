package com.example.expenses.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Expense::class, Category::class], version = 5, exportSchema = false) // Zwiększona wersja do 5
abstract class AppDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao

    companion object {

        // KROK 1: Definicja migracji z wersji 4 do 5
        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // KROK 2: Dodanie nowej kolumny 'date' do tabeli 'expenses'
                // Używamy polecenia SQL ALTER TABLE.
                // Dajemy wartość domyślną 0, aby stare rekordy nie powodowały błędu.
                db.execSQL("ALTER TABLE expenses ADD COLUMN date INTEGER NOT NULL DEFAULT 0")
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "expense_database"
                )
                // KROK 3: Usunięcie starej metody i dodanie nowej migracji
                // .fallbackToDestructiveMigration() // USUNIĘTE!
                .addMigrations(MIGRATION_4_5) // DODANE!
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}