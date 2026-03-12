package com.aufmass.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aufmass.app.data.local.dao.*
import com.aufmass.app.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        AufmassEntity::class,
        RaumEntity::class,
        GlasEntity::class,
        BodenSeEntity::class,
        RaumartEntity::class,
        RhythmusEntity::class,
        BodenbelagEntity::class,
        GlasartEntity::class,
        LvEinstellungEntity::class,
        ObjektfragebogenEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class AufmassDatabase : RoomDatabase() {
    abstract fun aufmassDao(): AufmassDao
    abstract fun raumDao(): RaumDao
    abstract fun glasDao(): GlasDao
    abstract fun bodenSeDao(): BodenSeDao
    abstract fun raumartDao(): RaumartDao
    abstract fun rhythmusDao(): RhythmusDao
    abstract fun bodenbelagDao(): BodenbelagDao
    abstract fun glasartDao(): GlasartDao
    abstract fun lvEinstellungDao(): LvEinstellungDao
    abstract fun objektfragebogenDao(): ObjektfragebogenDao

    companion object {
        @Volatile
        private var INSTANCE: AufmassDatabase? = null

        fun getDatabase(context: Context): AufmassDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AufmassDatabase::class.java,
                    "aufmass_database"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDefaultData(database)
                }
            }
        }

        suspend fun populateDefaultData(database: AufmassDatabase) {
            // No default data - user will import from JSON
        }
    }
}
