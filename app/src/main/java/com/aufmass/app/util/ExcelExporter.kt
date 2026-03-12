package com.aufmass.app.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.aufmass.app.data.local.entity.*
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFColor
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ExcelExporter(private val context: Context) {

    fun exportAufmass(
        aufmass: AufmassEntity,
        raeume: List<RaumEntity>,
        glasList: List<GlasEntity>,
        bodenSeList: List<BodenSeEntity>,
        lvEinstellungen: List<LvEinstellungEntity> = emptyList(),
        rhysmen: List<RhythmusEntity> = emptyList(),
        bodenbelage: List<BodenbelagEntity> = emptyList(),
        objektfragebogen: ObjektfragebogenEntity? = null,
        raumarten: List<RaumartEntity> = emptyList()
    ): File {
        val templatePath = "kalkulation_vorlage.xlsx"
        val inputStream: InputStream = context.assets.open(templatePath)
        val workbook = XSSFWorkbook(inputStream)
        
        val standardRhythmus = rhysmen.find { it.klartext == aufmass.standardrhythmus }?.exportwert ?: 1.0
        
        fillStammdatenSheet(workbook, aufmass, objektfragebogen)
        fillKalkulationUrsheet(workbook, raeume, rhysmen, bodenbelage, raumarten)
        fillAufmassGlasSheet(workbook, glasList)
        fillAufmassBodenSheet(workbook, bodenSeList)
        fillLeistungsverzeichnisSheet(workbook, raeume, lvEinstellungen, rhysmen, standardRhythmus)
        
        val fileName = "Kalkulation_${aufmass.titel.replace(" ", "_")}_${System.currentTimeMillis()}.xlsx"
        val file = File(context.getExternalFilesDir(null), fileName)
        
        FileOutputStream(file).use { outputStream ->
            workbook.write(outputStream)
        }
        workbook.close()
        
        return file
    }

    private fun fillStammdatenSheet(workbook: Workbook, aufmass: AufmassEntity, objektfragebogen: ObjektfragebogenEntity?) {
        val sheet = workbook.getSheet("Stammdaten") ?: return
        
        // Stammdaten
        setCellValue(sheet, 2, 1, aufmass.firma)
        setCellValue(sheet, 3, 1, "")
        setCellValue(sheet, 4, 1, "")
        setCellValue(sheet, 5, 1, aufmass.anschrift)
        setCellValue(sheet, 6, 1, "")
        setCellValue(sheet, 7, 1, "")
        setCellValue(sheet, 8, 1, "")
        setCellValue(sheet, 9, 1, "")
        setCellValue(sheet, 10, 1, "")
        setCellValue(sheet, 11, 1, "")
        setCellValue(sheet, 12, 1, "")
        setCellValue(sheet, 13, 1, aufmass.objektanschrift)
        setCellValue(sheet, 14, 1, "")
        setCellValue(sheet, 15, 1, aufmass.standardrhythmus)
        
        // Objektfrageblatt Daten ab Zeile 19
        var rowNum = 19
        if (objektfragebogen != null) {
            val objektRows = listOf(
                "Objektfragebogen:" to "",
                "Materialkammer: ${objektfragebogen.materialkammer}" to "",
                "Waschmaschine: ${if (objektfragebogen.waschmaschine) "ja" else "nein"}" to "",
                "Schmutzfangzone: ${if (objektfragebogen.schmutzfangzone) "ja" else "nein"}" to "",
                "Wasser: ${if (objektfragebogen.wasser) "ja" else "nein"}" to "",
                "Strom: ${if (objektfragebogen.strom) "ja" else "nein"}" to "",
                "Mülltrennung: ${if (objektfragebogen.muelltrennung) "ja" else "nein"}" to "",
                "Müllentsorgung: ${objektfragebogen.muellentsorgung}" to "",
                "Aufzug: ${if (objektfragebogen.aufzug) "ja" else "nein"}" to "",
                "Reinigungszustand: ${objektfragebogen.reinigungszustand}" to "",
                "Wechselgründe: ${objektfragebogen.wechselgruende}" to "",
                "Schlüsselobjekt: ${if (objektfragebogen.schluesselobjekt) "ja" else "nein"}" to "",
                "Alarmanlage: ${if (objektfragebogen.alarmanlage) "ja" else "nein"}" to "",
                "Besonderheiten: ${objektfragebogen.besonderheiten}" to ""
            )
            
            objektRows.forEach { (label, value) ->
                setCellValue(sheet, rowNum, 0, label)
                setCellValue(sheet, rowNum, 1, value)
                rowNum++
            }
        }
    }

    private fun setCellValue(sheet: Sheet, row: Int, col: Int, value: String) {
        val r = sheet.getRow(row) ?: sheet.createRow(row)
        val c = r.getCell(col) ?: r.createCell(col)
        c.setCellValue(value)
    }
    
    private fun setCellValue(sheet: Sheet, row: Int, col: Int, value: Double) {
        val r = sheet.getRow(row) ?: sheet.createRow(row)
        val c = r.getCell(col) ?: r.createCell(col)
        c.setCellValue(value)
    }
    
    private fun setCellValue(sheet: Sheet, row: Int, col: Int, value: Int) {
        val r = sheet.getRow(row) ?: sheet.createRow(row)
        val c = r.getCell(col) ?: r.createCell(col)
        c.setCellValue(value.toDouble())
    }

    private fun fillKalkulationUrsheet(
        workbook: Workbook,
        raeume: List<RaumEntity>,
        rhysmen: List<RhythmusEntity>,
        bodenbelage: List<BodenbelagEntity>,
        raumarten: List<RaumartEntity>
    ) {
        val sheet = workbook.getSheet("Kalk. UR") ?: return
        
        val rhysmenMap = rhysmen.associateBy { it.klartext }
        val bodenbelagMap = bodenbelage.associateBy { it.bezeichnung }
        val raumartMap = raumarten.associateBy { it.bezeichnung }
        
        if (raeume.isEmpty()) return
        
        val numRooms = raeume.size
        val startRow = 26
        val templateEndRow = 35
        
        // Insert new rows AFTER the template data row (shift from row 27, not 26)
        if (numRooms > 0) {
            sheet.shiftRows(startRow + 1, templateEndRow, numRooms)
        }
        
        var rowNum = startRow
        
        raeume.forEach { raum ->
            val rhythmusValue = rhysmenMap[raum.rhythmus]?.exportwert ?: 1.0
            val bodenbelagEntity = bodenbelagMap[raum.bodenbelag]
            val belagKuerzel = bodenbelagEntity?.abkuerzung ?: ""
            val bodenSchnitt = bodenbelagEntity?.quadratmeterSchnitt ?: 0.0
            
            // Get Raumart schnittvorgabe
            val raumartEntity = raumartMap[raum.raumart]
            val raumartSchnitt = raumartEntity?.schnittvorgabe ?: 0.0
            
            // Total schnitt = Bodenbelag schnitt + Raumart schnitt
            val totalSchnitt = bodenSchnitt + raumartSchnitt
            
            val row = sheet.getRow(rowNum) ?: sheet.createRow(rowNum)
            
            // A: Raumname
            setCellValue(sheet, rowNum, 0, raum.name)
            // B: Belag-Kürzel
            setCellValue(sheet, rowNum, 1, belagKuerzel)
            // C: Anzahl
            setCellValue(sheet, rowNum, 2, raum.anzahl.toDouble())
            // D: Länge
            setCellValue(sheet, rowNum, 3, raum.laenge)
            // E: Breite
            setCellValue(sheet, rowNum, 4, raum.breite)
            // G: m²-Schnitt (Boden + Raumart)
            setCellValue(sheet, rowNum, 6, totalSchnitt)
            // H: Rhythmus-Wert
            setCellValue(sheet, rowNum, 7, rhythmusValue)
            
            // Set formulas for this row
            setCellFormula(row, 5, "C$rowNum*D$rowNum*E$rowNum")  // m²-Fläche
            setCellFormula(row, 8, "\$I\$23")  // Std.lohn - references SVS in I23
            setCellFormula(row, 9, "IFERROR((F$rowNum/G$rowNum*I$rowNum*H$rowNum),\"\")")  // Total
            setCellFormula(row, 10, "IFERROR((F$rowNum/G$rowNum),\"\")")  // Zeit
            
            // Day columns
            for (dayCol in 11..16) {
                setCellFormula(row, dayCol, "\$K$rowNum")
            }
            
            // Rhythmus-Kontrolle column R
            setCellFormula(row, 17, "IF(H$rowNum<2.17,IF(Q$rowNum=0,\"Fehler\",\"\"),IF(H$rowNum/4.33=COUNT(L$rowNum:P$rowNum),\"\",\"Fehler\"))")
            
            rowNum++
        }
        
        // Update TOTAL row
        val totalRowNum = 28 + numRooms
        val totalRow = sheet.getRow(totalRowNum) ?: sheet.createRow(totalRowNum)
        setCellFormula(totalRow, 5, "SUM(F$startRow:F${totalRowNum - 1})")
        setCellFormula(totalRow, 9, "SUM(J$startRow:J${totalRowNum - 1})")
        setCellFormula(totalRow, 10, "SUM(K$startRow:K${totalRowNum - 1})")
        
        for (col in 11..17) {
            setCellFormula(totalRow, col, "SUM(${getColumnLetter(col)}$startRow:${getColumnLetter(col)}${totalRowNum - 1})")
        }
        
        // Update footer rows
        val anfangFahrtRowNum = 30 + numRooms
        val anfangFahrtRow = sheet.getRow(anfangFahrtRowNum)
        if (anfangFahrtRow != null) {
            setCellFormula(anfangFahrtRow, 7, "MAX(H$startRow:H${totalRowNum - 1})")
        }
        
        val zeitlichErweitertRowNum = 31 + numRooms
        val zeitlichErweitertRow = sheet.getRow(zeitlichErweitertRowNum)
        if (zeitlichErweitertRow != null) {
            setCellFormula(zeitlichErweitertRow, 7, "MAX(H$startRow:H${totalRowNum - 1})")
        }
        
        // Update Angebotssumme
        val angebotssummeRowNum = 33 + numRooms
        val angebotssummeRow = sheet.getRow(angebotssummeRowNum)
        if (angebotssummeRow != null) {
            setCellFormula(angebotssummeRow, 10, "SUM(J$totalRowNum,J${anfangFahrtRowNum}:J${zeitlichErweitertRowNum})")
            setCellFormula(angebotssummeRow, 11, "SUM(K$totalRowNum,K${anfangFahrtRowNum}:K${zeitlichErweitertRowNum})")
            setCellFormula(angebotssummeRow, 12, "SUM(L$totalRowNum,L${anfangFahrtRowNum}:L${zeitlichErweitertRowNum})")
        }
    }

    private fun setCellFormula(row: Row, col: Int, formula: String) {
        val c = row.getCell(col) ?: row.createCell(col)
        c.setCellFormula(formula)
    }

    private fun getColumnLetter(col: Int): String {
        return when (col) {
            0 -> "A"; 1 -> "B"; 2 -> "C"; 3 -> "D"; 4 -> "E"; 5 -> "F"; 6 -> "G"; 7 -> "H"
            8 -> "I"; 9 -> "J"; 10 -> "K"; 11 -> "L"; 12 -> "M"; 13 -> "N"; 14 -> "O"; 15 -> "P"; 16 -> "Q"
            else -> "R"
        }
    }

    private fun fillAufmassGlasSheet(workbook: Workbook, glasList: List<GlasEntity>) {
        val sheet = workbook.getSheet("Aufmaß Glas") ?: return
        
        if (glasList.isEmpty()) return
        
        val numEntries = glasList.size
        val startRow = 18
        val templateEndRow = 22
        
        // Insert new rows
        sheet.shiftRows(startRow, templateEndRow, numEntries)
        
        var rowNum = startRow
        
        glasList.forEach { glas ->
            // A: Bezeichnung
            setCellValue(sheet, rowNum, 0, glas.bezeichnung)
            // B: Glasart
            setCellValue(sheet, rowNum, 1, glas.glasart)
            // C: Anzahl
            setCellValue(sheet, rowNum, 2, glas.anzahl.toDouble())
            // D: Breite
            setCellValue(sheet, rowNum, 3, glas.breite)
            // E: Höhe
            setCellValue(sheet, rowNum, 4, glas.hoehe)
            
            rowNum++
        }
        
        // Update SUM row
        val sumRowNum = 20 + numEntries
        val sumRow = sheet.getRow(sumRowNum) ?: sheet.createRow(sumRowNum)
        setCellFormula(sumRow, 7, "SUM(H$startRow:H${sumRowNum - 1})")
        setCellFormula(sumRow, 9, "SUM(J$startRow:J${sumRowNum - 1})")
        setCellFormula(sumRow, 11, "SUM(L$startRow:L${sumRowNum - 1})")
    }

    private fun fillAufmassBodenSheet(workbook: Workbook, bodenSeList: List<BodenSeEntity>) {
        val sheet = workbook.getSheet("Aufmaß Boden") ?: return
        
        if (bodenSeList.isEmpty()) return
        
        val numEntries = bodenSeList.size
        val startRow = 18
        val templateEndRow = 22
        
        sheet.shiftRows(startRow, templateEndRow, numEntries)
        
        var rowNum = startRow
        
        bodenSeList.forEachIndexed { index, bodenSe ->
            // A: lfd.Nr.
            setCellValue(sheet, rowNum, 0, (index + 1).toDouble())
            // B: Bezeichnung
            setCellValue(sheet, rowNum, 1, bodenSe.bezeichnung)
            // C: Bodenart
            setCellValue(sheet, rowNum, 2, bodenSe.bodenart)
            // D: Anzahl
            setCellValue(sheet, rowNum, 3, bodenSe.anzahl.toDouble())
            // E: Länge
            setCellValue(sheet, rowNum, 4, bodenSe.laenge)
            // F: Breite
            setCellValue(sheet, rowNum, 5, bodenSe.breite)
            
            rowNum++
        }
        
        // Update SUM row
        val sumRowNum = 20 + numEntries
        val sumRow = sheet.getRow(sumRowNum) ?: sheet.createRow(sumRowNum)
        setCellFormula(sumRow, 7, "SUM(H$startRow:H${sumRowNum - 1})")
        setCellFormula(sumRow, 9, "SUM(J$startRow:J${sumRowNum - 1})")
        setCellFormula(sumRow, 11, "SUM(L$startRow:L${sumRowNum - 1})")
    }

    private fun calculateRhythmusValue(platzhalter: String, standardRhythmus: Double): Double {
        return when {
            platzhalter.contains("{Rhythmus}/2") || platzhalter.contains("{Rhythmus}/2") -> {
                val baseValue = standardRhythmus
                if (baseValue == 1.0) 1.0 else kotlin.math.floor(baseValue / 2.0)
            }
            platzhalter.contains("{Rhythmus}") -> {
                standardRhythmus
            }
            platzhalter.isNotBlank() -> {
                try {
                    platzhalter.replace(",", ".").toDoubleOrNull() ?: 0.0
                } catch (e: Exception) {
                    0.0
                }
            }
            else -> 0.0
        }
    }

    private fun columnLetterToIndex(letter: String): Int {
        val col = letter.lowercase().trim()
        return when (col) {
            "a" -> 0; "b" -> 1; "c" -> 2; "d" -> 3; "e" -> 4; "f" -> 5; "g" -> 6; "h" -> 7
            "i" -> 8; "j" -> 9; "k" -> 10; "l" -> 11; "m" -> 12; "n" -> 13; "o" -> 14; "p" -> 15
            "q" -> 16; "r" -> 17; "s" -> 18; "t" -> 19; "u" -> 20; "v" -> 21; "w" -> 22; "x" -> 23
            "y" -> 24; "z" -> 25
            "aa" -> 26; "ab" -> 27; "ac" -> 28; "ad" -> 29; "ae" -> 30; "af" -> 31; "ag" -> 32; "ah" -> 33
            "ai" -> 34; "aj" -> 35; "ak" -> 36; "al" -> 37; "am" -> 38; "an" -> 39; "ao" -> 40; "ap" -> 41
            "aq" -> 42
            else -> -1
        }
    }

    private fun fillLeistungsverzeichnisSheet(
        workbook: Workbook,
        raeume: List<RaumEntity>,
        lvEinstellungen: List<LvEinstellungEntity>,
        rhysmen: List<RhythmusEntity>,
        standardRhythmus: Double
    ) {
        val sheet = workbook.getSheet("Leistungsverzeichnis") ?: return
        
        // Group raeume by raumart
        val raeumeByRaumart = raeume.groupBy { it.raumart }
        
        // Get universal LV tasks
        val universalLvTasks = lvEinstellungen.filter { it.raumart == "*" || it.raumart.isEmpty() }
        
        // Start row for data
        var rowNum = 7
        
        // Create a row for each raumart
        raeumeByRaumart.forEach { (raumart, raeumeList) ->
            val anzahl = raeumeList.size
            
            // Get LV tasks for this raumart, or fall back to universal
            val raumartLvTasks = lvEinstellungen.filter { it.raumart == raumart }
            val tasksToUse = if (raumartLvTasks.isNotEmpty()) raumartLvTasks else universalLvTasks
            val isUniversal = raumartLvTasks.isEmpty()
            
            val row = sheet.getRow(rowNum) ?: sheet.createRow(rowNum)
            
            // Spalten A-C zusammengefügt: "Raumart (Anzahl)"
            setCellValue(sheet, rowNum, 0, "$raumart ($anzahl)")
            if (isUniversal) {
                // Rot färben für universelle LV
                val cell = row.getCell(0) ?: row.createCell(0)
                val style = workbook.createCellStyle()
                val font = workbook.createFont()
                font.color = org.apache.poi.ss.usermodel.IndexedColors.RED.getIndex()
                style.setFont(font)
                cell.cellStyle = style
            }
            
            // Fill LV task columns based on spalte
            tasksToUse.forEach { lvTask ->
                val colIndex = columnLetterToIndex(lvTask.spalte)
                if (colIndex >= 0 && colIndex <= 54) {
                    val value = calculateRhythmusValue(lvTask.rhythmusPlatzhalter, standardRhythmus)
                    setCellValue(sheet, rowNum, colIndex, value)
                }
            }
            
            rowNum++
        }
    }

    fun shareFile(file: File): Intent {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        
        return Intent(Intent.ACTION_SEND).apply {
            type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
}
