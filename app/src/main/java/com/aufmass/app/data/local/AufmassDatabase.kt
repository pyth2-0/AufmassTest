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
    version = 6,
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
            val raumarten = listOf(
                RaumartEntity(bezeichnung = "Büro", schnittvorgabe = 200.0),
                RaumartEntity(bezeichnung = "Besprechungsraum", schnittvorgabe = 210.0),
                RaumartEntity(bezeichnung = "WC", schnittvorgabe = 60.0),
                RaumartEntity(bezeichnung = "Waschraum", schnittvorgabe = 90.0),
                RaumartEntity(bezeichnung = "Duschraum", schnittvorgabe = 90.0),
                RaumartEntity(bezeichnung = "Teeküchen", schnittvorgabe = 120.0),
                RaumartEntity(bezeichnung = "Aufenthaltsräume", schnittvorgabe = 120.0),
                RaumartEntity(bezeichnung = "Umkleiden", schnittvorgabe = 160.0),
                RaumartEntity(bezeichnung = "Aufzüge", schnittvorgabe = 120.0),
                RaumartEntity(bezeichnung = "Flur", schnittvorgabe = 210.0),
                RaumartEntity(bezeichnung = "Eingang", schnittvorgabe = 130.0),
                RaumartEntity(bezeichnung = "Windfang", schnittvorgabe = 130.0),
                RaumartEntity(bezeichnung = "Foyers", schnittvorgabe = 130.0),
                RaumartEntity(bezeichnung = "TH", schnittvorgabe = 130.0),
                RaumartEntity(bezeichnung = "Nebenraum", schnittvorgabe = 200.0),
                RaumartEntity(bezeichnung = "Behandlungsraum", schnittvorgabe = 120.0),
                RaumartEntity(bezeichnung = "Lager", schnittvorgabe = 200.0),
                RaumartEntity(bezeichnung = "Klassenraum", schnittvorgabe = 250.0),
                RaumartEntity(bezeichnung = "Sportraum", schnittvorgabe = 250.0),
                RaumartEntity(bezeichnung = "Gruppenraum", schnittvorgabe = 120.0)
            )
            raumarten.forEach { database.raumartDao().insert(it) }

            val bodenbelage = listOf(
                BodenbelagEntity(bezeichnung = "Betonwerkstein", abkuerzung = "BTW"),
                BodenbelagEntity(bezeichnung = "Designboden mit Struktur", abkuerzung = "DBS"),
                BodenbelagEntity(bezeichnung = "Designboden ohne Struktur", abkuerzung = "DBOS"),
                BodenbelagEntity(bezeichnung = "Elastomerbelag", abkuerzung = "Noppe"),
                BodenbelagEntity(bezeichnung = "Fliese", abkuerzung = "FL"),
                BodenbelagEntity(bezeichnung = "Holzdiele", abkuerzung = "Diele"),
                BodenbelagEntity(bezeichnung = "Kugelg./FL", abkuerzung = "KG/FL"),
                BodenbelagEntity(bezeichnung = "Linoleum", abkuerzung = "Lino"),
                BodenbelagEntity(bezeichnung = "Nadelflies", abkuerzung = "NF"),
                BodenbelagEntity(bezeichnung = "Parkett", abkuerzung = "Park."),
                BodenbelagEntity(bezeichnung = "PVC", abkuerzung = "PVC"),
                BodenbelagEntity(bezeichnung = "sonstiges", abkuerzung = "sonst"),
                BodenbelagEntity(bezeichnung = "Estrich", abkuerzung = "Estrich"),
                BodenbelagEntity(bezeichnung = "Beton", abkuerzung = "Beton"),
                BodenbelagEntity(bezeichnung = "Stein", abkuerzung = "Stein"),
                BodenbelagEntity(bezeichnung = "Teppich", abkuerzung = "Tepp")
            )
            bodenbelage.forEach { database.bodenbelagDao().insert(it) }

            val rhysmen = listOf(
                RhythmusEntity(klartext = "7/W", exportwert = 30.31, lvWert = 7.0),
                RhythmusEntity(klartext = "6/W", exportwert = 25.98, lvWert = 6.0),
                RhythmusEntity(klartext = "5/W", exportwert = 21.65, lvWert = 5.0),
                RhythmusEntity(klartext = "4/W", exportwert = 17.32, lvWert = 4.0),
                RhythmusEntity(klartext = "3/W", exportwert = 12.99, lvWert = 3.0),
                RhythmusEntity(klartext = "2/3/W", exportwert = 10.83, lvWert = 2.5),
                RhythmusEntity(klartext = "2/W", exportwert = 8.66, lvWert = 2.0),
                RhythmusEntity(klartext = "1/W", exportwert = 4.33, lvWert = 1.0),
                RhythmusEntity(klartext = "2/M", exportwert = 2.0, lvWert = 2.0),
                RhythmusEntity(klartext = "1/M", exportwert = 1.0, lvWert = 1.0)
            )
            rhysmen.forEach { database.rhythmusDao().insert(it) }

            val glasarten = listOf(
                GlasartEntity(bezeichnung = "Dreh-Kipp"),
                GlasartEntity(bezeichnung = "Fest"),
                GlasartEntity(bezeichnung = "Schaufenster"),
                GlasartEntity(bezeichnung = "Schiebefenster"),
                GlasartEntity(bezeichnung = "Tür")
            )
            glasarten.forEach { database.glasartDao().insert(it) }

            // LV Standard-Aufgaben (basierend auf Excel kalkulation_vorlage.xlsx)
            val lvEinstellungen = listOf(
                // Bodenreinigungsarbeiten
                LvEinstellungEntity(raumart = "Büro", spalte = "A", aufgabe = "Kehren", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Büro", spalte = "B", aufgabe = "2-stufig wischen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Büro", spalte = "C", aufgabe = "Saugen, komplett", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Büro", spalte = "D", aufgabe = "kehren/Saugen, auf Sicht", rhythmusPlatzhalter = "{Rhythmus}"),
                
                LvEinstellungEntity(raumart = "WC", spalte = "A", aufgabe = "Kehren", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "WC", spalte = "B", aufgabe = "2-stufig wischen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "WC", spalte = "C", aufgabe = "Sanitärobjekte, einschl. Armaturen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "WC", spalte = "D", aufgabe = "WC - Bürstenhalter", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "WC", spalte = "E", aufgabe = "Papierspender auffüllen", rhythmusPlatzhalter = "{Rhythmus}"),
                
                LvEinstellungEntity(raumart = "Flur", spalte = "A", aufgabe = "Kehren", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Flur", spalte = "B", aufgabe = "2-stufig wischen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Flur", spalte = "C", aufgabe = "Schmutzfangmatten saugen", rhythmusPlatzhalter = "{Rhythmus}"),
                
                LvEinstellungEntity(raumart = "Küche", spalte = "A", aufgabe = "Kehren", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Küche", spalte = "B", aufgabe = "2-stufig wischen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Küche", spalte = "C", aufgabe = "Arbeitsflächen reinigen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Küche", spalte = "D", aufgabe = "Spüle reinigen", rhythmusPlatzhalter = "{Rhythmus}"),
                
                // Pauschal für alle Räume - Ausstattung
                LvEinstellungEntity(raumart = "*", spalte = "E", aufgabe = "Abfallbehälter leeren", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "*", spalte = "F", aufgabe = "Staub wischen/Entstauben", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "*", spalte = "G", aufgabe = "Griffspuren entfernen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "*", spalte = "H", aufgabe = "Lichtschalter/Steckdosen reinigen", rhythmusPlatzhalter = "{Rhythmus}")
            )
            lvEinstellungen.forEach { database.lvEinstellungDao().insert(it) }
        }
    }
}
