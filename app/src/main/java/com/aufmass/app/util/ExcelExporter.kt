package com.aufmass.app.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.aufmass.app.data.local.entity.*
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

class ExcelExporter(private val context: Context) {

    fun exportAufmass(
        aufmass: AufmassEntity,
        raeume: List<RaumEntity>,
        glasList: List<GlasEntity>,
        bodenSeList: List<BodenSeEntity>
    ): File {
        val workbook = XSSFWorkbook()
        
        createStammdatenSheet(workbook, aufmass)
        createRaumeSheet(workbook, raeume)
        createGlasSheet(workbook, glasList)
        createBodenSeSheet(workbook, bodenSeList)
        
        val fileName = "Aufmass_${aufmass.titel.replace(" ", "_")}_${System.currentTimeMillis()}.xlsx"
        val file = File(context.getExternalFilesDir(null), fileName)
        
        FileOutputStream(file).use { outputStream ->
            workbook.write(outputStream)
        }
        workbook.close()
        
        return file
    }

    private fun createStammdatenSheet(workbook: Workbook, aufmass: AufmassEntity) {
        val sheet = workbook.createSheet("Stammdaten")
        val boldStyle = workbook.createCellStyle().apply {
            val font = workbook.createFont().apply { bold = true }
            setFont(font)
        }
        
        var rowNum = 0
        listOf(
            "Firma / Name" to aufmass.firma,
            "Anschrift" to aufmass.anschrift,
            "Objektanschrift" to aufmass.objektanschrift,
            "Standardrhythmus" to aufmass.standardrhythmus,
            "Notizen" to aufmass.notizen
        ).forEach { (label, value) ->
            val row = sheet.createRow(rowNum++)
            row.createCell(0).apply { 
                setCellValue(label)
                cellStyle = boldStyle
            }
            row.createCell(1).setCellValue(value)
        }
        
        sheet.setColumnWidth(0, 4000)
        sheet.setColumnWidth(1, 8000)
    }

    private fun createRaumeSheet(workbook: Workbook, raeume: List<RaumEntity>) {
        val sheet = workbook.createSheet("Unterhaltsreinigung")
        val headerStyle = workbook.createCellStyle().apply {
            val font = workbook.createFont().apply { bold = true }
            setFont(font)
        }
        
        var rowNum = 0
        val headerRow = sheet.createRow(rowNum++)
        listOf("Raumname", "Raumart", "Bodenbelag", "Anzahl", "Länge", "Breite", "m²", "Rhythmus", "Notizen")
            .forEachIndexed { index, title ->
                headerRow.createCell(index).apply {
                    setCellValue(title)
                    cellStyle = headerStyle
                }
            }
        
        raeume.forEach { raum ->
            val row = sheet.createRow(rowNum++)
            row.createCell(0).setCellValue(raum.name)
            row.createCell(1).setCellValue(raum.raumart)
            row.createCell(2).setCellValue(raum.bodenbelag)
            row.createCell(3).setCellValue(raum.anzahl.toDouble())
            row.createCell(4).setCellValue(raum.laenge)
            row.createCell(5).setCellValue(raum.breite)
            row.createCell(6).setCellValue(raum.flaeche)
            row.createCell(7).setCellValue(raum.rhythmus)
            row.createCell(8).setCellValue(raum.notizen)
        }
        
        sheet.setColumnWidth(0, 4000)
        sheet.setColumnWidth(1, 3000)
        sheet.setColumnWidth(2, 3000)
    }

    private fun createGlasSheet(workbook: Workbook, glasList: List<GlasEntity>) {
        val sheet = workbook.createSheet("Glasaufmaß")
        val headerStyle = workbook.createCellStyle().apply {
            val font = workbook.createFont().apply { bold = true }
            setFont(font)
        }
        
        var rowNum = 0
        val headerRow = sheet.createRow(rowNum++)
        listOf("Bezeichnung", "Glasart", "Anzahl", "Breite", "Höhe", "m²", "Notizen")
            .forEachIndexed { index, title ->
                headerRow.createCell(index).apply {
                    setCellValue(title)
                    cellStyle = headerStyle
                }
            }
        
        glasList.forEach { glas ->
            val row = sheet.createRow(rowNum++)
            row.createCell(0).setCellValue(glas.bezeichnung)
            row.createCell(1).setCellValue(glas.glasart)
            row.createCell(2).setCellValue(glas.anzahl.toDouble())
            row.createCell(3).setCellValue(glas.breite)
            row.createCell(4).setCellValue(glas.hoehe)
            row.createCell(5).setCellValue(glas.flaeche)
            row.createCell(6).setCellValue(glas.notizen)
        }
    }

    private fun createBodenSeSheet(workbook: Workbook, bodenSeList: List<BodenSeEntity>) {
        val sheet = workbook.createSheet("Boden_S_E")
        val headerStyle = workbook.createCellStyle().apply {
            val font = workbook.createFont().apply { bold = true }
            setFont(font)
        }
        
        var rowNum = 0
        val headerRow = sheet.createRow(rowNum++)
        listOf("Bezeichnung", "Bodenart", "Anzahl", "Länge", "Breite", "m²", "Notizen")
            .forEachIndexed { index, title ->
                headerRow.createCell(index).apply {
                    setCellValue(title)
                    cellStyle = headerStyle
                }
            }
        
        bodenSeList.forEach { bodenSe ->
            val row = sheet.createRow(rowNum++)
            row.createCell(0).setCellValue(bodenSe.bezeichnung)
            row.createCell(1).setCellValue(bodenSe.bodenart)
            row.createCell(2).setCellValue(bodenSe.anzahl.toDouble())
            row.createCell(3).setCellValue(bodenSe.laenge)
            row.createCell(4).setCellValue(bodenSe.breite)
            row.createCell(5).setCellValue(bodenSe.flaeche)
            row.createCell(6).setCellValue(bodenSe.notizen)
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
