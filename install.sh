#!/bin/bash
# Installiert die APK und löscht vorher Daten + App

cd /var/home/bazzite/aufmassapp

echo "🔨 Build wird erstellt..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
    
    echo "🗑️  Alte App wird deinstalliert..."
    adb uninstall com.aufmass.app 2>/dev/null
    
    # Für nur Daten löschen (App behalten):
    # adb shell pm clear com.aufmass.app
    
    echo "📦 APK wird installiert..."
    adb install -r "$APK_PATH"
    
    echo "✅ Fertig!"
else
    echo "❌ Build fehlgeschlagen"
    exit 1
fi
