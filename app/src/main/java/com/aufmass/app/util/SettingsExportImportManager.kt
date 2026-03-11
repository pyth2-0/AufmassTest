package com.aufmass.app.util

import com.aufmass.app.data.local.entity.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

data class SettingsExport(
    val version: String = "1.0",
    val exportDate: String,
    val raumarten: List<RaumartExport>,
    val bodenbelage: List<BodenbelagExport>,
    val rythmen: List<RhythmusExport>,
    val glasarten: List<GlasartExport>,
    val lvEinstellungen: List<LvEinstellungExport>
)

data class RaumartExport(
    val bezeichnung: String,
    val schnittvorgabe: Double
)

data class BodenbelagExport(
    val bezeichnung: String,
    val abkuerzung: String,
    val quadratmeterSchnitt: Double = 0.0
)

data class RhythmusExport(
    val klartext: String,
    val exportwert: Double,
    val lvWert: Double
)

data class GlasartExport(
    val bezeichnung: String
)

data class LvEinstellungExport(
    val raumart: String,
    val spalte: String,
    val aufgabe: String,
    val rhythmusPlatzhalter: String
)

class SettingsExportImportManager {

    fun exportToJson(
        raumarten: List<RaumartEntity>,
        bodenbelage: List<BodenbelagEntity>,
        rhythmus: List<RhythmusEntity>,
        glasarten: List<GlasartEntity>,
        lvEinstellungen: List<LvEinstellungEntity>
    ): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        
        val json = JSONObject()
        json.put("version", "1.0")
        json.put("exportDate", dateFormat.format(Date()))
        
        val raumartenArray = JSONArray()
        for (raumart in raumarten) {
            val obj = JSONObject()
            obj.put("bezeichnung", raumart.bezeichnung)
            obj.put("schnittvorgabe", raumart.schnittvorgabe)
            raumartenArray.put(obj)
        }
        json.put("raumarten", raumartenArray)
        
        val bodenbelageArray = JSONArray()
        for (boden in bodenbelage) {
            val obj = JSONObject()
            obj.put("bezeichnung", boden.bezeichnung)
            obj.put("abkuerzung", boden.abkuerzung)
            obj.put("quadratmeterSchnitt", boden.quadratmeterSchnitt)
            bodenbelageArray.put(obj)
        }
        json.put("bodenbelage", bodenbelageArray)
        
        val rhythmusArray = JSONArray()
        for (rhythm in rhythmus) {
            val obj = JSONObject()
            obj.put("klartext", rhythm.klartext)
            obj.put("exportwert", rhythm.exportwert)
            obj.put("lvWert", rhythm.lvWert)
            rhythmusArray.put(obj)
        }
        json.put("rhythmen", rhythmusArray)
        
        val glasartenArray = JSONArray()
        for (glas in glasarten) {
            val obj = JSONObject()
            obj.put("bezeichnung", glas.bezeichnung)
            glasartenArray.put(obj)
        }
        json.put("glasarten", glasartenArray)
        
        val lvArray = JSONArray()
        for (lv in lvEinstellungen) {
            val obj = JSONObject()
            obj.put("raumart", lv.raumart)
            obj.put("spalte", lv.spalte)
            obj.put("aufgabe", lv.aufgabe)
            obj.put("rhythmusPlatzhalter", lv.rhythmusPlatzhalter)
            lvArray.put(obj)
        }
        json.put("lvEinstellungen", lvArray)
        
        return json.toString(2)
    }

    fun importFromJson(jsonString: String): SettingsExport? {
        return try {
            val json = JSONObject(jsonString)
            
            val raumarten = mutableListOf<RaumartExport>()
            val raumartenArray = json.optJSONArray("raumarten")
            if (raumartenArray != null) {
                for (i in 0 until raumartenArray.length()) {
                    val obj = raumartenArray.getJSONObject(i)
                    raumarten.add(RaumartExport(
                        bezeichnung = obj.getString("bezeichnung"),
                        schnittvorgabe = obj.getDouble("schnittvorgabe")
                    ))
                }
            }
            
            val bodenbelage = mutableListOf<BodenbelagExport>()
            val bodenbelageArray = json.optJSONArray("bodenbelage")
            if (bodenbelageArray != null) {
                for (i in 0 until bodenbelageArray.length()) {
                    val obj = bodenbelageArray.getJSONObject(i)
                    bodenbelage.add(BodenbelagExport(
                        bezeichnung = obj.getString("bezeichnung"),
                        abkuerzung = obj.optString("abkuerzung", ""),
                        quadratmeterSchnitt = obj.optDouble("quadratmeterSchnitt", 0.0)
                    ))
                }
            }
            
            val rhythmus = mutableListOf<RhythmusExport>()
            val rhythmusArray = json.optJSONArray("rhythmen")
            if (rhythmusArray != null) {
                for (i in 0 until rhythmusArray.length()) {
                    val obj = rhythmusArray.getJSONObject(i)
                    rhythmus.add(RhythmusExport(
                        klartext = obj.getString("klartext"),
                        exportwert = obj.getDouble("exportwert"),
                        lvWert = obj.optDouble("lvWert", 1.0)
                    ))
                }
            }
            
            val glasarten = mutableListOf<GlasartExport>()
            val glasartenArray = json.optJSONArray("glasarten")
            if (glasartenArray != null) {
                for (i in 0 until glasartenArray.length()) {
                    val obj = glasartenArray.getJSONObject(i)
                    glasarten.add(GlasartExport(
                        bezeichnung = obj.getString("bezeichnung")
                    ))
                }
            }
            
            val lvEinstellungen = mutableListOf<LvEinstellungExport>()
            val lvArray = json.optJSONArray("lvEinstellungen")
            if (lvArray != null) {
                for (i in 0 until lvArray.length()) {
                    val obj = lvArray.getJSONObject(i)
                    lvEinstellungen.add(LvEinstellungExport(
                        raumart = obj.getString("raumart"),
                        spalte = obj.getString("spalte"),
                        aufgabe = obj.getString("aufgabe"),
                        rhythmusPlatzhalter = obj.optString("rhythmusPlatzhalter", "{Rhythmus}")
                    ))
                }
            }
            
            SettingsExport(
                version = json.optString("version", "1.0"),
                exportDate = json.optString("exportDate", ""),
                raumarten = raumarten,
                bodenbelage = bodenbelage,
                rythmen = rhythmus,
                glasarten = glasarten,
                lvEinstellungen = lvEinstellungen
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun validateImport(settingsExport: SettingsExport): List<String> {
        val errors = mutableListOf<String>()
        
        if (settingsExport.raumarten.isEmpty()) {
            errors.add("Keine Raumarten vorhanden")
        }
        
        if (settingsExport.bodenbelage.isEmpty()) {
            errors.add("Keine Bodenbeläge vorhanden")
        }
        
        if (settingsExport.rythmen.isEmpty()) {
            errors.add("Keine Rhythmen vorhanden")
        }
        
        return errors
    }
}
