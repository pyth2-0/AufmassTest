package com.aufmass.app.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.aufmass.app.data.local.entity.*
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
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
        bodenbelage: List<BodenbelagEntity> = emptyList()
    ): File {
        val templatePath = "kalkulation_vorlage.xlsx"
        val inputStream: InputStream = context.assets.open(templatePath)
        val workbook = XSSFWorkbook(inputStream)
        
        fillStammdatenSheet(workbook, aufmass)
        fillKalkulationUrsheet(workbook, raeume, rhysmen, bodenbelage)
        fillAufmassBodenSheet(workbook, bodenSeList)
        fillLeistungsverzeichnisSheet(workbook, raeume, lvEinstellungen, rhysmen)
        
        val fileName = "Kalkulation_${aufmass.titel.replace(" ", "_")}_${System.currentTimeMillis()}.xlsx"
        val file = File(context.getExternalFilesDir(null), fileName)
        
        FileOutputStream(file).use { outputStream ->
            workbook.write(outputStream)
        }
        workbook.close()
        
        return file
    }

    private fun fillStammdatenSheet(workbook: Workbook, aufmass: AufmassEntity) {
        val sheet = workbook.getSheet("Stammdaten") ?: return
        
        val setCellValue = { row: Int, col: Int, value: String ->
            sheet.getRow(row)?.getCell(col)?.setCellValue(value)
        }
        
        setCellValue(2, 1, aufmass.firma)
        setCellValue(3, 1, aufmass.anschrift)
        setCellValue(4, 1, "")
        setCellValue(5, 1, "")
        setCellValue(6, 1, "")
        setCellValue(7, 1, "")
        setCellValue(8, 1, "")
        setCellValue(9, 1, "")
        setCellValue(10, 1, "")
        setCellValue(12, 1, aufmass.objektanschrift)
        setCellValue(13, 1, "")
        setCellValue(14, 1, aufmass.standardrhythmus)
    }

    private fun fillKalkulationUrsheet(
        workbook: Workbook,
        raeume: List<RaumEntity>,
        rhysmen: List<RhythmusEntity>,
        bodenbelage: List<BodenbelagEntity>
    ) {
        val sheet = workbook.getSheet("Kalk. UR") ?: return
        
        val rhysmenMap = rhysmen.associateBy { it.klartext }
        
        if (raeume.isEmpty()) return
        
        // First, insert new rows for each room (shifts TOTAL and footer rows down)
        val numRooms = raeume.size
        if (numRooms > 0) {
            sheet.shiftRows(26, sheet.lastRowNum, numRooms)
        }
        
        var rowNum = 26
        
        raeume.forEachIndexed { index, raum ->
            val rhythmusValue = rhysmenMap[raum.rhythmus]?.exportwert ?: 1.0
            
            val row = sheet.getRow(rowNum) ?: sheet.createRow(rowNum)
            
            row.getCell(0)?.setCellValue(raum.name)
            row.getCell(1)?.setCellValue(raum.bodenbelag)
            row.getCell(2)?.setCellValue(raum.anzahl.toDouble())
            row.getCell(3)?.setCellValue(raum.laenge)
            row.getCell(4)?.setCellValue(raum.breite)
            row.getCell(5)?.setCellValue(raum.gesamtflaeche)
            row.getCell(7)?.setCellValue(rhythmusValue)
            
            if (row.getCell(8) == null) row.createCell(8)
            row.getCell(8)?.setCellFormula("\$I\$24")
            
            if (row.getCell(9) == null) row.createCell(9)
            val totalFormula = "IFERROR((F$rowNum/G$rowNum*I$rowNum*H$rowNum),\"\")"
            row.getCell(9)?.setCellFormula(totalFormula)
            
            if (row.getCell(10) == null) row.createCell(10)
            val timeFormula = "IFERROR((F$rowNum/G$rowNum),\"\")"
            row.getCell(10)?.setCellFormula(timeFormula)
            
            for (dayCol in 11..16) {
                if (row.getCell(dayCol) == null) row.createCell(dayCol)
                row.getCell(dayCol)?.setCellFormula("\$K$rowNum")
            }
            if (row.getCell(16) == null) row.createCell(16)
            row.getCell(16)?.setCellFormula("\$K$rowNum")
            
            rowNum++
        }
        
        // Update TOTAL row (now shifted down by numRooms)
        val totalRowNum = 28 + numRooms
        val totalRow = sheet.getRow(totalRowNum) ?: sheet.createRow(totalRowNum)
        totalRow.getCell(5)?.setCellFormula("SUM(F26:F${totalRowNum - 1})")
        totalRow.getCell(9)?.setCellFormula("SUM(J26:J${totalRowNum - 1})")
        totalRow.getCell(10)?.setCellFormula("SUM(K26:K${totalRowNum - 1})")
        
        totalRow.getCell(11)?.setCellFormula("SUM(L26:L${totalRowNum - 1})")
        totalRow.getCell(12)?.setCellFormula("SUM(M26:M${totalRowNum - 1})")
        totalRow.getCell(13)?.setCellFormula("SUM(N26:N${totalRowNum - 1})")
        totalRow.getCell(14)?.setCellFormula("SUM(O26:O${totalRowNum - 1})")
        totalRow.getCell(15)?.setCellFormula("SUM(P26:P${totalRowNum - 1})")
        totalRow.getCell(16)?.setCellFormula("SUM(Q26:Q${totalRowNum - 1})")
        
        // Update footer rows (Anfahrt, etc.)
        val anfangFahrtRow = sheet.getRow(30 + numRooms)
        if (anfangFahrtRow != null) {
            anfangFahrtRow.getCell(7)?.setCellFormula("MAX(H26:H${totalRowNum - 1})")
        }
        val zeitlichErweitertRow = sheet.getRow(31 + numRooms)
        if (zeitlichErweitertRow != null) {
            zeitlichErweitertRow.getCell(7)?.setCellFormula("MAX(H26:H${totalRowNum - 1})")
        }
        
        // Update Angebotssumme
        val angebotssummeRow = sheet.getRow(33 + numRooms)
        if (angebotssummeRow != null) {
            angebotssummeRow.getCell(10)?.setCellFormula("SUM(J${totalRowNum},J${30 + numRooms}:J${31 + numRooms})")
            angebotssummeRow.getCell(11)?.setCellFormula("SUM(K${totalRowNum},K${30 + numRooms}:K${31 + numRooms})")
            angebotssummeRow.getCell(12)?.setCellFormula("SUM(L${totalRowNum},L${30 + numRooms}:L${31 + numRooms})")
        }
    }

    private fun fillAufmassBodenSheet(workbook: Workbook, bodenSeList: List<BodenSeEntity>) {
        val sheet = workbook.getSheet("Aufmaß Boden") ?: return
        
        if (bodenSeList.isEmpty()) return
        
        // Insert new rows for each entry
        val numEntries = bodenSeList.size
        sheet.shiftRows(18, sheet.lastRowNum, numEntries)
        
        var rowNum = 18
        
        bodenSeList.forEach { bodenSe ->
            val row = sheet.getRow(rowNum) ?: sheet.createRow(rowNum)
            
            if (row.getCell(0) == null) row.createCell(0)
            row.getCell(0)?.setCellValue((rowNum - 18 + 1).toDouble())
            
            if (row.getCell(1) == null) row.createCell(1)
            row.getCell(1)?.setCellValue(bodenSe.bezeichnung)
            
            if (row.getCell(2) == null) row.createCell(2)
            row.getCell(2)?.setCellValue(bodenSe.bodenart)
            
            if (row.getCell(3) == null) row.createCell(3)
            row.getCell(3)?.setCellValue(bodenSe.anzahl.toDouble())
            
            if (row.getCell(4) == null) row.createCell(4)
            row.getCell(4)?.setCellValue(bodenSe.laenge)
            
            if (row.getCell(5) == null) row.createCell(5)
            row.getCell(5)?.setCellValue(bodenSe.breite)
            
            if (row.getCell(6) == null) row.createCell(6)
            row.getCell(6)?.setCellFormula("E$rowNum*F$rowNum")
            
            if (row.getCell(7) == null) row.createCell(7)
            row.getCell(7)?.setCellFormula("D$rowNum*G$rowNum")
            
            rowNum++
        }
        
        // Update SUM row (now at 20 + numEntries)
        val sumRowNum = 20 + numEntries
        val sumRow = sheet.getRow(sumRowNum) ?: sheet.createRow(sumRowNum)
        sumRow.getCell(7)?.setCellFormula("SUM(H18:H${sumRowNum - 1})")
        sumRow.getCell(9)?.setCellFormula("SUM(J18:J${sumRowNum - 1})")
        sumRow.getCell(11)?.setCellFormula("SUM(L18:L${sumRowNum - 1})")
    }

    private fun fillLeistungsverzeichnisSheet(
        workbook: Workbook,
        raeume: List<RaumEntity>,
        lvEinstellungen: List<LvEinstellungEntity>,
        rhysmen: List<RhythmusEntity>
    ) {
        val sheet = workbook.getSheet("Leistungsverzeichnis") ?: return
        
        val raeumeByRaumart = raeume.groupBy { it.raumart }
        
        val lvByRaumart = lvEinstellungen.groupBy { it.raumart }
        
        val rowStart = 7
        var rowNum = rowStart
        
        raeumeByRaumart.forEach { (raumart, raeumeList) ->
            val totalFlaeche = raeumeList.sumOf { it.gesamtflaeche }
            val lvTasks = lvByRaumart[raumart] ?: emptyList()
            
            val row = sheet.getRow(rowNum) ?: sheet.createRow(rowNum)
            
            if (row.getCell(0) == null) row.createCell(0)
            row.getCell(0)?.setCellValue(raumart)
            
            if (row.getCell(1) == null) row.createCell(1)
            row.getCell(1)?.setCellValue(totalFlaeche)
            
            if (row.getCell(2) == null) row.createCell(2)
            row.getCell(2)?.setCellValue(raeumeList.size.toDouble())
            
            if (row.getCell(3) == null) row.createCell(3)
            val rhythmus = raeumeList.firstOrNull()?.rhythmus ?: ""
            row.getCell(3)?.setCellValue(rhythmus)
            
            lvTasks.forEachIndexed { index, lvTask ->
                val colIndex = 10 + index
                if (colIndex < 55) {
                    if (row.getCell(colIndex) == null) row.createCell(colIndex)
                    row.getCell(colIndex)?.setCellValue("X")
                }
            }
            
            rowNum++
        }
        
        val wildcardLvTasks = lvByRaumart["*"] ?: lvByRaumart[""] ?: emptyList()
        if (wildcardLvTasks.isNotEmpty()) {
            val row = sheet.getRow(rowNum) ?: sheet.createRow(rowNum)
            if (row.getCell(0) == null) row.createCell(0)
            row.getCell(0)?.setCellValue("Allgemein")
            
            val totalFlaeche = raeume.sumOf { it.gesamtflaeche }
            if (row.getCell(1) == null) row.createCell(1)
            row.getCell(1)?.setCellValue(totalFlaeche)
            
            wildcardLvTasks.forEachIndexed { index, lvTask ->
                val colIndex = 10 + index
                if (colIndex < 55) {
                    if (row.getCell(colIndex) == null) row.createCell(colIndex)
                    row.getCell(colIndex)?.setCellValue("X")
                }
            }
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
