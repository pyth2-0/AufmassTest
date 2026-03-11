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
                // Büro
                LvEinstellungEntity(raumart = "Büro", spalte = "d", aufgabe = "Kehren", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Büro", spalte = "e", aufgabe = "2-stufig wischen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Büro", spalte = "f", aufgabe = "Saugen, komplett", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Büro", spalte = "m", aufgabe = "freigeräumte Schreib- und Beistelltische reinigen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Büro", spalte = "n", aufgabe = "Telefone entstauben", rhythmusPlatzhalter = "{Rhythmus}/2"),
                LvEinstellungEntity(raumart = "Büro", spalte = "q", aufgabe = "Fensterbänke, Kabelkanäle entstauben", rhythmusPlatzhalter = "{Rhythmus}/2"),
                LvEinstellungEntity(raumart = "Büro", spalte = "t", aufgabe = "Zimmertüren und Zargen, Griffspuren entfernen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Büro", spalte = "y", aufgabe = "Lichtschalter, Steckdosen, Griffspuren entfernen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Büro", spalte = "z", aufgabe = "Bilderrahmen entstauben", rhythmusPlatzhalter = "{Rhythmus}/2"),
                LvEinstellungEntity(raumart = "Büro", spalte = "aa", aufgabe = "Spinnweben entfernen", rhythmusPlatzhalter = "M"),
                LvEinstellungEntity(raumart = "Büro", spalte = "ac", aufgabe = "Handläufe reinigen", rhythmusPlatzhalter = "{Rhythmus}"),
                
                // WC
                LvEinstellungEntity(raumart = "WC", spalte = "aj", aufgabe = "Sanitärobjekte inkl. Armaturen reinigen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "WC", spalte = "ak", aufgabe = "Spiegel, Ablagen reinigen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "WC", spalte = "al", aufgabe = "Wandfliesen im Spritzbereich reinigen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "WC", spalte = "am", aufgabe = "Wandfliesen + WC-Trennwände Gesamtfläche reinigen", rhythmusPlatzhalter = "M"),
                LvEinstellungEntity(raumart = "WC", spalte = "an", aufgabe = "Wannen + Duschen reinigen", rhythmusPlatzhalter = "M"),
                LvEinstellungEntity(raumart = "WC", spalte = "ao", aufgabe = "WC-Bürstenhalter reinigen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "WC", spalte = "ap", aufgabe = "Seifen- und Papierspender reinigen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "WC", spalte = "aq", aufgabe = "Versorgung / Lieferung", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "WC", spalte = "aa", aufgabe = "Spinnweben entfernen", rhythmusPlatzhalter = "M"),
                
                // Umkleide
                LvEinstellungEntity(raumart = "Umkleide", spalte = "f", aufgabe = "Saugen, komplett", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Umkleide", spalte = "e", aufgabe = "2-stufig wischen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Umkleide", spalte = "s", aufgabe = "Polstermöbel entstauben", rhythmusPlatzhalter = "{Rhythmus}/2"),
                LvEinstellungEntity(raumart = "Umkleide", spalte = "ac", aufgabe = "Handläufe reinigen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Umkleide", spalte = "ad", aufgabe = "Fuß- / Sockelleisten entstauben", rhythmusPlatzhalter = "{Rhythmus}/2"),
                LvEinstellungEntity(raumart = "Umkleide", spalte = "aa", aufgabe = "Spinnweben entfernen", rhythmusPlatzhalter = "M"),
                
                // Konferenzraum
                LvEinstellungEntity(raumart = "Konferenzraum", spalte = "f", aufgabe = "Saugen, komplett", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Konferenzraum", spalte = "e", aufgabe = "2-stufig wischen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Konferenzraum", spalte = "m", aufgabe = "freigeräumte Tische reinigen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Konferenzraum", spalte = "o", aufgabe = "Tisch- und Stuhlbeine reinigen", rhythmusPlatzhalter = "{Rhythmus}/2"),
                LvEinstellungEntity(raumart = "Konferenzraum", spalte = "t", aufgabe = "Zimmertüren und Zargen, Griffspuren entfernen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Konferenzraum", spalte = "y", aufgabe = "Lichtschalter, Steckdosen, Griffspuren entfernen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Konferenzraum", spalte = "z", aufgabe = "Bilderrahmen entstauben", rhythmusPlatzhalter = "{Rhythmus}/2"),
                LvEinstellungEntity(raumart = "Konferenzraum", spalte = "aa", aufgabe = "Spinnweben entfernen", rhythmusPlatzhalter = "M"),
                
                // Lager
                LvEinstellungEntity(raumart = "Lager", spalte = "d", aufgabe = "Kehren", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Lager", spalte = "g", aufgabe = "Kehren/Saugen auf Sicht", rhythmusPlatzhalter = "{Rhythmus}/2"),
                LvEinstellungEntity(raumart = "Lager", spalte = "h", aufgabe = "maschinelle Bodenreinigung", rhythmusPlatzhalter = "M"),
                LvEinstellungEntity(raumart = "Lager", spalte = "aa", aufgabe = "Spinnweben entfernen", rhythmusPlatzhalter = "M"),
                LvEinstellungEntity(raumart = "Lager", spalte = "ad", aufgabe = "Sockelleisten entstauben", rhythmusPlatzhalter = "M"),
                
                // Produktionsraum
                LvEinstellungEntity(raumart = "Produktionsraum", spalte = "d", aufgabe = "Kehren", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Produktionsraum", spalte = "h", aufgabe = "maschinelle Bodenreinigung", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Produktionsraum", spalte = "i", aufgabe = "Schmutzfangmatten saugen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Produktionsraum", spalte = "aa", aufgabe = "Spinnweben entfernen", rhythmusPlatzhalter = "M"),
                LvEinstellungEntity(raumart = "Produktionsraum", spalte = "ac", aufgabe = "Handläufe reinigen", rhythmusPlatzhalter = "{Rhythmus}"),
                LvEinstellungEntity(raumart = "Produktionsraum", spalte = "ad", aufgabe = "Sockelleisten entstauben", rhythmusPlatzhalter = "{Rhythmus}")
            )
            lvEinstellungen.forEach { database.lvEinstellungDao().insert(it) }
        }
    }
}
