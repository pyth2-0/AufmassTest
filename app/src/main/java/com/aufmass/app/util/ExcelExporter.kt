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
        
        val rowStart = 26
        var rowNum = rowStart
        
        raeume.forEach { raum ->
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
        
        val totalRow = sheet.getRow(28) ?: sheet.createRow(28)
        totalRow.getCell(5)?.setCellFormula("SUM(F$rowStart:F${rowNum - 1})")
        totalRow.getCell(9)?.setCellFormula("SUM(J$rowStart:J${rowNum - 1})")
        totalRow.getCell(10)?.setCellFormula("SUM(K$rowStart:K${rowNum - 1})")
        
        totalRow.getCell(11)?.setCellFormula("SUM(L$rowStart:L${rowNum - 1})")
        totalRow.getCell(12)?.setCellFormula("SUM(M$rowStart:M${rowNum - 1})")
        totalRow.getCell(13)?.setCellFormula("SUM(N$rowStart:N${rowNum - 1})")
        totalRow.getCell(14)?.setCellFormula("SUM(O$rowStart:O${rowNum - 1})")
        totalRow.getCell(15)?.setCellFormula("SUM(P$rowStart:P${rowNum - 1})")
        totalRow.getCell(16)?.setCellFormula("SUM(Q$rowStart:Q${rowNum - 1})")
    }

    private fun fillAufmassBodenSheet(workbook: Workbook, bodenSeList: List<BodenSeEntity>) {
        val sheet = workbook.getSheet("Aufmaß Boden") ?: return
        
        val rowStart = 18
        var rowNum = rowStart
        
        bodenSeList.forEach { bodenSe ->
            val row = sheet.getRow(rowNum) ?: sheet.createRow(rowNum)
            
            if (row.getCell(0) == null) row.createCell(0)
            row.getCell(0)?.setCellValue((rowNum - rowStart + 1).toDouble())
            
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
        
        val sumRow = sheet.getRow(20) ?: sheet.createRow(20)
        sumRow.getCell(7)?.setCellFormula("SUM(H$rowStart:H${rowNum - 1})")
        sumRow.getCell(9)?.setCellFormula("SUM(J$rowStart:J${rowNum - 1})")
        sumRow.getCell(11)?.setCellFormula("SUM(L$rowStart:L${rowNum - 1})")
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
