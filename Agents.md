# Android App ObAuf - Entwicklungsrichtlinien

## WICHTIG: Versionsnummer IMMER erhöhen
Nach JEDEM Build und bevor du dem User die APK übergibst:
1. Öffne build.gradle.kts und erhöhe versionName nach dem Schema: **Features.Versionsnummer.Unterversionen**
   - Erste Zahl: Anzahl Features (wird nur auf Befehl erhöht)
   - Zweite Zahl: Versionsnummer (wird bei jedem Feature um 1 erhöht)
   - Dritte Zahl: Unterversionen (alle Builds bis Feature complete)
2. Öffne HomeScreen.kt und aktualisiere die Versionsnummer im Titel
3. Baue die APK NACH dem Versionsnummer-Update
4. Aktualisiere versionCode um 1

## Dezimaltrennzeichen
- IMMER Komma als Dezimaltrennzeichen verwenden (deutsches Format)
- NumberFormatter nutzen: `formatDecimal()` gibt Komma zurück
- Bei Eingaben Komma automatisch in Punkt umwandeln für interne Verarbeitung

## S-Pen Unterstützung
- Für S-Pen Handwriting: Native Android `EditText` via `AndroidView` in Compose einbetten
- NICHT `OutlinedTextField` von Compose verwenden (unterstützt kein S-Pen Handwriting)
- Shared Component: `ui/components/StylusEditText.kt` wiederverwenden

## Code-Qualität
- Ungenutzte Imports entfernen
- Ungenutzte Variablen entfernen (z.B. focusManager wenn nicht mehr benötigt)
- Build erfolgreich ohne Warnings anstreben
- Prüfe nach jedem fertigstellen deine arbeit auf Fehler und korrigiere sie, verlasse dich dabei nicht nur auf das ergebnis des compilers
