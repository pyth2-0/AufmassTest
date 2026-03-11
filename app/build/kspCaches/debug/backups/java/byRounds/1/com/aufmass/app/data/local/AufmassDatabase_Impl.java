package com.aufmass.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.aufmass.app.data.local.dao.AufmassDao;
import com.aufmass.app.data.local.dao.AufmassDao_Impl;
import com.aufmass.app.data.local.dao.BodenSeDao;
import com.aufmass.app.data.local.dao.BodenSeDao_Impl;
import com.aufmass.app.data.local.dao.BodenbelagDao;
import com.aufmass.app.data.local.dao.BodenbelagDao_Impl;
import com.aufmass.app.data.local.dao.GlasDao;
import com.aufmass.app.data.local.dao.GlasDao_Impl;
import com.aufmass.app.data.local.dao.GlasartDao;
import com.aufmass.app.data.local.dao.GlasartDao_Impl;
import com.aufmass.app.data.local.dao.LvEinstellungDao;
import com.aufmass.app.data.local.dao.LvEinstellungDao_Impl;
import com.aufmass.app.data.local.dao.ObjektfragebogenDao;
import com.aufmass.app.data.local.dao.ObjektfragebogenDao_Impl;
import com.aufmass.app.data.local.dao.RaumDao;
import com.aufmass.app.data.local.dao.RaumDao_Impl;
import com.aufmass.app.data.local.dao.RaumartDao;
import com.aufmass.app.data.local.dao.RaumartDao_Impl;
import com.aufmass.app.data.local.dao.RhythmusDao;
import com.aufmass.app.data.local.dao.RhythmusDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AufmassDatabase_Impl extends AufmassDatabase {
  private volatile AufmassDao _aufmassDao;

  private volatile RaumDao _raumDao;

  private volatile GlasDao _glasDao;

  private volatile BodenSeDao _bodenSeDao;

  private volatile RaumartDao _raumartDao;

  private volatile RhythmusDao _rhythmusDao;

  private volatile BodenbelagDao _bodenbelagDao;

  private volatile GlasartDao _glasartDao;

  private volatile LvEinstellungDao _lvEinstellungDao;

  private volatile ObjektfragebogenDao _objektfragebogenDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(6) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `aufmass` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `titel` TEXT NOT NULL, `firma` TEXT NOT NULL, `anschrift` TEXT NOT NULL, `objektanschrift` TEXT NOT NULL, `standardrhythmus` TEXT NOT NULL, `wochentage` TEXT NOT NULL, `reinigungszeiten` TEXT NOT NULL, `reinigungstage` TEXT NOT NULL, `erstelltAm` INTEGER NOT NULL, `notizen` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `raum` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `aufmassId` INTEGER NOT NULL, `name` TEXT NOT NULL, `raumart` TEXT NOT NULL, `bodenbelag` TEXT NOT NULL, `anzahl` INTEGER NOT NULL, `laenge` REAL NOT NULL, `breite` REAL NOT NULL, `schnittvorgabe` REAL NOT NULL, `rhythmus` TEXT NOT NULL, `notizen` TEXT NOT NULL, `fotoPaths` TEXT NOT NULL, `zusatzlicheMasse` TEXT NOT NULL, FOREIGN KEY(`aufmassId`) REFERENCES `aufmass`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_raum_aufmassId` ON `raum` (`aufmassId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `glas` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `aufmassId` INTEGER NOT NULL, `bezeichnung` TEXT NOT NULL, `glasart` TEXT NOT NULL, `anzahl` INTEGER NOT NULL, `breite` REAL NOT NULL, `hoehe` REAL NOT NULL, `notizen` TEXT NOT NULL, `fotoPath` TEXT NOT NULL, FOREIGN KEY(`aufmassId`) REFERENCES `aufmass`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_glas_aufmassId` ON `glas` (`aufmassId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `boden_se` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `aufmassId` INTEGER NOT NULL, `bezeichnung` TEXT NOT NULL, `bodenart` TEXT NOT NULL, `anzahl` INTEGER NOT NULL, `laenge` REAL NOT NULL, `breite` REAL NOT NULL, `notizen` TEXT NOT NULL, `fotoPath` TEXT NOT NULL, FOREIGN KEY(`aufmassId`) REFERENCES `aufmass`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_boden_se_aufmassId` ON `boden_se` (`aufmassId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `einstellung_raumart` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `bezeichnung` TEXT NOT NULL, `schnittvorgabe` REAL NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `einstellung_rhythmus` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `klartext` TEXT NOT NULL, `exportwert` REAL NOT NULL, `lvWert` REAL NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `einstellung_bodenbelag` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `bezeichnung` TEXT NOT NULL, `abkuerzung` TEXT NOT NULL, `quadratmeterSchnitt` REAL NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `einstellung_glasart` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `bezeichnung` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `einstellung_lv` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `raumart` TEXT NOT NULL, `spalte` TEXT NOT NULL, `aufgabe` TEXT NOT NULL, `rhythmusPlatzhalter` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `objektfragebogen` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `aufmassId` INTEGER NOT NULL, `materialkammer` TEXT NOT NULL, `waschmaschine` INTEGER NOT NULL, `schmutzfangzone` INTEGER NOT NULL, `wasser` INTEGER NOT NULL, `strom` INTEGER NOT NULL, `muelltrennung` INTEGER NOT NULL, `muellentsorgung` TEXT NOT NULL, `aufzug` INTEGER NOT NULL, `reinigungszustand` TEXT NOT NULL, `wechselgruende` TEXT NOT NULL, `schluesselobjekt` INTEGER NOT NULL, `alarmanlage` INTEGER NOT NULL, `besonderheiten` TEXT NOT NULL, FOREIGN KEY(`aufmassId`) REFERENCES `aufmass`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_objektfragebogen_aufmassId` ON `objektfragebogen` (`aufmassId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'f5749d872793c84a69111fdce550cc2e')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `aufmass`");
        db.execSQL("DROP TABLE IF EXISTS `raum`");
        db.execSQL("DROP TABLE IF EXISTS `glas`");
        db.execSQL("DROP TABLE IF EXISTS `boden_se`");
        db.execSQL("DROP TABLE IF EXISTS `einstellung_raumart`");
        db.execSQL("DROP TABLE IF EXISTS `einstellung_rhythmus`");
        db.execSQL("DROP TABLE IF EXISTS `einstellung_bodenbelag`");
        db.execSQL("DROP TABLE IF EXISTS `einstellung_glasart`");
        db.execSQL("DROP TABLE IF EXISTS `einstellung_lv`");
        db.execSQL("DROP TABLE IF EXISTS `objektfragebogen`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsAufmass = new HashMap<String, TableInfo.Column>(11);
        _columnsAufmass.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAufmass.put("titel", new TableInfo.Column("titel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAufmass.put("firma", new TableInfo.Column("firma", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAufmass.put("anschrift", new TableInfo.Column("anschrift", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAufmass.put("objektanschrift", new TableInfo.Column("objektanschrift", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAufmass.put("standardrhythmus", new TableInfo.Column("standardrhythmus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAufmass.put("wochentage", new TableInfo.Column("wochentage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAufmass.put("reinigungszeiten", new TableInfo.Column("reinigungszeiten", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAufmass.put("reinigungstage", new TableInfo.Column("reinigungstage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAufmass.put("erstelltAm", new TableInfo.Column("erstelltAm", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAufmass.put("notizen", new TableInfo.Column("notizen", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAufmass = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAufmass = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAufmass = new TableInfo("aufmass", _columnsAufmass, _foreignKeysAufmass, _indicesAufmass);
        final TableInfo _existingAufmass = TableInfo.read(db, "aufmass");
        if (!_infoAufmass.equals(_existingAufmass)) {
          return new RoomOpenHelper.ValidationResult(false, "aufmass(com.aufmass.app.data.local.entity.AufmassEntity).\n"
                  + " Expected:\n" + _infoAufmass + "\n"
                  + " Found:\n" + _existingAufmass);
        }
        final HashMap<String, TableInfo.Column> _columnsRaum = new HashMap<String, TableInfo.Column>(13);
        _columnsRaum.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRaum.put("aufmassId", new TableInfo.Column("aufmassId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRaum.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRaum.put("raumart", new TableInfo.Column("raumart", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRaum.put("bodenbelag", new TableInfo.Column("bodenbelag", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRaum.put("anzahl", new TableInfo.Column("anzahl", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRaum.put("laenge", new TableInfo.Column("laenge", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRaum.put("breite", new TableInfo.Column("breite", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRaum.put("schnittvorgabe", new TableInfo.Column("schnittvorgabe", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRaum.put("rhythmus", new TableInfo.Column("rhythmus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRaum.put("notizen", new TableInfo.Column("notizen", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRaum.put("fotoPaths", new TableInfo.Column("fotoPaths", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRaum.put("zusatzlicheMasse", new TableInfo.Column("zusatzlicheMasse", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRaum = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysRaum.add(new TableInfo.ForeignKey("aufmass", "CASCADE", "NO ACTION", Arrays.asList("aufmassId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesRaum = new HashSet<TableInfo.Index>(1);
        _indicesRaum.add(new TableInfo.Index("index_raum_aufmassId", false, Arrays.asList("aufmassId"), Arrays.asList("ASC")));
        final TableInfo _infoRaum = new TableInfo("raum", _columnsRaum, _foreignKeysRaum, _indicesRaum);
        final TableInfo _existingRaum = TableInfo.read(db, "raum");
        if (!_infoRaum.equals(_existingRaum)) {
          return new RoomOpenHelper.ValidationResult(false, "raum(com.aufmass.app.data.local.entity.RaumEntity).\n"
                  + " Expected:\n" + _infoRaum + "\n"
                  + " Found:\n" + _existingRaum);
        }
        final HashMap<String, TableInfo.Column> _columnsGlas = new HashMap<String, TableInfo.Column>(9);
        _columnsGlas.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGlas.put("aufmassId", new TableInfo.Column("aufmassId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGlas.put("bezeichnung", new TableInfo.Column("bezeichnung", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGlas.put("glasart", new TableInfo.Column("glasart", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGlas.put("anzahl", new TableInfo.Column("anzahl", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGlas.put("breite", new TableInfo.Column("breite", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGlas.put("hoehe", new TableInfo.Column("hoehe", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGlas.put("notizen", new TableInfo.Column("notizen", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGlas.put("fotoPath", new TableInfo.Column("fotoPath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGlas = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysGlas.add(new TableInfo.ForeignKey("aufmass", "CASCADE", "NO ACTION", Arrays.asList("aufmassId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesGlas = new HashSet<TableInfo.Index>(1);
        _indicesGlas.add(new TableInfo.Index("index_glas_aufmassId", false, Arrays.asList("aufmassId"), Arrays.asList("ASC")));
        final TableInfo _infoGlas = new TableInfo("glas", _columnsGlas, _foreignKeysGlas, _indicesGlas);
        final TableInfo _existingGlas = TableInfo.read(db, "glas");
        if (!_infoGlas.equals(_existingGlas)) {
          return new RoomOpenHelper.ValidationResult(false, "glas(com.aufmass.app.data.local.entity.GlasEntity).\n"
                  + " Expected:\n" + _infoGlas + "\n"
                  + " Found:\n" + _existingGlas);
        }
        final HashMap<String, TableInfo.Column> _columnsBodenSe = new HashMap<String, TableInfo.Column>(9);
        _columnsBodenSe.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodenSe.put("aufmassId", new TableInfo.Column("aufmassId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodenSe.put("bezeichnung", new TableInfo.Column("bezeichnung", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodenSe.put("bodenart", new TableInfo.Column("bodenart", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodenSe.put("anzahl", new TableInfo.Column("anzahl", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodenSe.put("laenge", new TableInfo.Column("laenge", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodenSe.put("breite", new TableInfo.Column("breite", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodenSe.put("notizen", new TableInfo.Column("notizen", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodenSe.put("fotoPath", new TableInfo.Column("fotoPath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBodenSe = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBodenSe.add(new TableInfo.ForeignKey("aufmass", "CASCADE", "NO ACTION", Arrays.asList("aufmassId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesBodenSe = new HashSet<TableInfo.Index>(1);
        _indicesBodenSe.add(new TableInfo.Index("index_boden_se_aufmassId", false, Arrays.asList("aufmassId"), Arrays.asList("ASC")));
        final TableInfo _infoBodenSe = new TableInfo("boden_se", _columnsBodenSe, _foreignKeysBodenSe, _indicesBodenSe);
        final TableInfo _existingBodenSe = TableInfo.read(db, "boden_se");
        if (!_infoBodenSe.equals(_existingBodenSe)) {
          return new RoomOpenHelper.ValidationResult(false, "boden_se(com.aufmass.app.data.local.entity.BodenSeEntity).\n"
                  + " Expected:\n" + _infoBodenSe + "\n"
                  + " Found:\n" + _existingBodenSe);
        }
        final HashMap<String, TableInfo.Column> _columnsEinstellungRaumart = new HashMap<String, TableInfo.Column>(3);
        _columnsEinstellungRaumart.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEinstellungRaumart.put("bezeichnung", new TableInfo.Column("bezeichnung", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEinstellungRaumart.put("schnittvorgabe", new TableInfo.Column("schnittvorgabe", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEinstellungRaumart = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEinstellungRaumart = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEinstellungRaumart = new TableInfo("einstellung_raumart", _columnsEinstellungRaumart, _foreignKeysEinstellungRaumart, _indicesEinstellungRaumart);
        final TableInfo _existingEinstellungRaumart = TableInfo.read(db, "einstellung_raumart");
        if (!_infoEinstellungRaumart.equals(_existingEinstellungRaumart)) {
          return new RoomOpenHelper.ValidationResult(false, "einstellung_raumart(com.aufmass.app.data.local.entity.RaumartEntity).\n"
                  + " Expected:\n" + _infoEinstellungRaumart + "\n"
                  + " Found:\n" + _existingEinstellungRaumart);
        }
        final HashMap<String, TableInfo.Column> _columnsEinstellungRhythmus = new HashMap<String, TableInfo.Column>(4);
        _columnsEinstellungRhythmus.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEinstellungRhythmus.put("klartext", new TableInfo.Column("klartext", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEinstellungRhythmus.put("exportwert", new TableInfo.Column("exportwert", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEinstellungRhythmus.put("lvWert", new TableInfo.Column("lvWert", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEinstellungRhythmus = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEinstellungRhythmus = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEinstellungRhythmus = new TableInfo("einstellung_rhythmus", _columnsEinstellungRhythmus, _foreignKeysEinstellungRhythmus, _indicesEinstellungRhythmus);
        final TableInfo _existingEinstellungRhythmus = TableInfo.read(db, "einstellung_rhythmus");
        if (!_infoEinstellungRhythmus.equals(_existingEinstellungRhythmus)) {
          return new RoomOpenHelper.ValidationResult(false, "einstellung_rhythmus(com.aufmass.app.data.local.entity.RhythmusEntity).\n"
                  + " Expected:\n" + _infoEinstellungRhythmus + "\n"
                  + " Found:\n" + _existingEinstellungRhythmus);
        }
        final HashMap<String, TableInfo.Column> _columnsEinstellungBodenbelag = new HashMap<String, TableInfo.Column>(4);
        _columnsEinstellungBodenbelag.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEinstellungBodenbelag.put("bezeichnung", new TableInfo.Column("bezeichnung", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEinstellungBodenbelag.put("abkuerzung", new TableInfo.Column("abkuerzung", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEinstellungBodenbelag.put("quadratmeterSchnitt", new TableInfo.Column("quadratmeterSchnitt", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEinstellungBodenbelag = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEinstellungBodenbelag = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEinstellungBodenbelag = new TableInfo("einstellung_bodenbelag", _columnsEinstellungBodenbelag, _foreignKeysEinstellungBodenbelag, _indicesEinstellungBodenbelag);
        final TableInfo _existingEinstellungBodenbelag = TableInfo.read(db, "einstellung_bodenbelag");
        if (!_infoEinstellungBodenbelag.equals(_existingEinstellungBodenbelag)) {
          return new RoomOpenHelper.ValidationResult(false, "einstellung_bodenbelag(com.aufmass.app.data.local.entity.BodenbelagEntity).\n"
                  + " Expected:\n" + _infoEinstellungBodenbelag + "\n"
                  + " Found:\n" + _existingEinstellungBodenbelag);
        }
        final HashMap<String, TableInfo.Column> _columnsEinstellungGlasart = new HashMap<String, TableInfo.Column>(2);
        _columnsEinstellungGlasart.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEinstellungGlasart.put("bezeichnung", new TableInfo.Column("bezeichnung", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEinstellungGlasart = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEinstellungGlasart = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEinstellungGlasart = new TableInfo("einstellung_glasart", _columnsEinstellungGlasart, _foreignKeysEinstellungGlasart, _indicesEinstellungGlasart);
        final TableInfo _existingEinstellungGlasart = TableInfo.read(db, "einstellung_glasart");
        if (!_infoEinstellungGlasart.equals(_existingEinstellungGlasart)) {
          return new RoomOpenHelper.ValidationResult(false, "einstellung_glasart(com.aufmass.app.data.local.entity.GlasartEntity).\n"
                  + " Expected:\n" + _infoEinstellungGlasart + "\n"
                  + " Found:\n" + _existingEinstellungGlasart);
        }
        final HashMap<String, TableInfo.Column> _columnsEinstellungLv = new HashMap<String, TableInfo.Column>(5);
        _columnsEinstellungLv.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEinstellungLv.put("raumart", new TableInfo.Column("raumart", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEinstellungLv.put("spalte", new TableInfo.Column("spalte", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEinstellungLv.put("aufgabe", new TableInfo.Column("aufgabe", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEinstellungLv.put("rhythmusPlatzhalter", new TableInfo.Column("rhythmusPlatzhalter", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEinstellungLv = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEinstellungLv = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEinstellungLv = new TableInfo("einstellung_lv", _columnsEinstellungLv, _foreignKeysEinstellungLv, _indicesEinstellungLv);
        final TableInfo _existingEinstellungLv = TableInfo.read(db, "einstellung_lv");
        if (!_infoEinstellungLv.equals(_existingEinstellungLv)) {
          return new RoomOpenHelper.ValidationResult(false, "einstellung_lv(com.aufmass.app.data.local.entity.LvEinstellungEntity).\n"
                  + " Expected:\n" + _infoEinstellungLv + "\n"
                  + " Found:\n" + _existingEinstellungLv);
        }
        final HashMap<String, TableInfo.Column> _columnsObjektfragebogen = new HashMap<String, TableInfo.Column>(15);
        _columnsObjektfragebogen.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsObjektfragebogen.put("aufmassId", new TableInfo.Column("aufmassId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsObjektfragebogen.put("materialkammer", new TableInfo.Column("materialkammer", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsObjektfragebogen.put("waschmaschine", new TableInfo.Column("waschmaschine", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsObjektfragebogen.put("schmutzfangzone", new TableInfo.Column("schmutzfangzone", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsObjektfragebogen.put("wasser", new TableInfo.Column("wasser", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsObjektfragebogen.put("strom", new TableInfo.Column("strom", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsObjektfragebogen.put("muelltrennung", new TableInfo.Column("muelltrennung", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsObjektfragebogen.put("muellentsorgung", new TableInfo.Column("muellentsorgung", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsObjektfragebogen.put("aufzug", new TableInfo.Column("aufzug", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsObjektfragebogen.put("reinigungszustand", new TableInfo.Column("reinigungszustand", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsObjektfragebogen.put("wechselgruende", new TableInfo.Column("wechselgruende", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsObjektfragebogen.put("schluesselobjekt", new TableInfo.Column("schluesselobjekt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsObjektfragebogen.put("alarmanlage", new TableInfo.Column("alarmanlage", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsObjektfragebogen.put("besonderheiten", new TableInfo.Column("besonderheiten", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysObjektfragebogen = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysObjektfragebogen.add(new TableInfo.ForeignKey("aufmass", "CASCADE", "NO ACTION", Arrays.asList("aufmassId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesObjektfragebogen = new HashSet<TableInfo.Index>(1);
        _indicesObjektfragebogen.add(new TableInfo.Index("index_objektfragebogen_aufmassId", false, Arrays.asList("aufmassId"), Arrays.asList("ASC")));
        final TableInfo _infoObjektfragebogen = new TableInfo("objektfragebogen", _columnsObjektfragebogen, _foreignKeysObjektfragebogen, _indicesObjektfragebogen);
        final TableInfo _existingObjektfragebogen = TableInfo.read(db, "objektfragebogen");
        if (!_infoObjektfragebogen.equals(_existingObjektfragebogen)) {
          return new RoomOpenHelper.ValidationResult(false, "objektfragebogen(com.aufmass.app.data.local.entity.ObjektfragebogenEntity).\n"
                  + " Expected:\n" + _infoObjektfragebogen + "\n"
                  + " Found:\n" + _existingObjektfragebogen);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "f5749d872793c84a69111fdce550cc2e", "178265c3c82014873c731f39677dadcb");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "aufmass","raum","glas","boden_se","einstellung_raumart","einstellung_rhythmus","einstellung_bodenbelag","einstellung_glasart","einstellung_lv","objektfragebogen");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `aufmass`");
      _db.execSQL("DELETE FROM `raum`");
      _db.execSQL("DELETE FROM `glas`");
      _db.execSQL("DELETE FROM `boden_se`");
      _db.execSQL("DELETE FROM `einstellung_raumart`");
      _db.execSQL("DELETE FROM `einstellung_rhythmus`");
      _db.execSQL("DELETE FROM `einstellung_bodenbelag`");
      _db.execSQL("DELETE FROM `einstellung_glasart`");
      _db.execSQL("DELETE FROM `einstellung_lv`");
      _db.execSQL("DELETE FROM `objektfragebogen`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AufmassDao.class, AufmassDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RaumDao.class, RaumDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(GlasDao.class, GlasDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BodenSeDao.class, BodenSeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RaumartDao.class, RaumartDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RhythmusDao.class, RhythmusDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BodenbelagDao.class, BodenbelagDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(GlasartDao.class, GlasartDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(LvEinstellungDao.class, LvEinstellungDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ObjektfragebogenDao.class, ObjektfragebogenDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public AufmassDao aufmassDao() {
    if (_aufmassDao != null) {
      return _aufmassDao;
    } else {
      synchronized(this) {
        if(_aufmassDao == null) {
          _aufmassDao = new AufmassDao_Impl(this);
        }
        return _aufmassDao;
      }
    }
  }

  @Override
  public RaumDao raumDao() {
    if (_raumDao != null) {
      return _raumDao;
    } else {
      synchronized(this) {
        if(_raumDao == null) {
          _raumDao = new RaumDao_Impl(this);
        }
        return _raumDao;
      }
    }
  }

  @Override
  public GlasDao glasDao() {
    if (_glasDao != null) {
      return _glasDao;
    } else {
      synchronized(this) {
        if(_glasDao == null) {
          _glasDao = new GlasDao_Impl(this);
        }
        return _glasDao;
      }
    }
  }

  @Override
  public BodenSeDao bodenSeDao() {
    if (_bodenSeDao != null) {
      return _bodenSeDao;
    } else {
      synchronized(this) {
        if(_bodenSeDao == null) {
          _bodenSeDao = new BodenSeDao_Impl(this);
        }
        return _bodenSeDao;
      }
    }
  }

  @Override
  public RaumartDao raumartDao() {
    if (_raumartDao != null) {
      return _raumartDao;
    } else {
      synchronized(this) {
        if(_raumartDao == null) {
          _raumartDao = new RaumartDao_Impl(this);
        }
        return _raumartDao;
      }
    }
  }

  @Override
  public RhythmusDao rhythmusDao() {
    if (_rhythmusDao != null) {
      return _rhythmusDao;
    } else {
      synchronized(this) {
        if(_rhythmusDao == null) {
          _rhythmusDao = new RhythmusDao_Impl(this);
        }
        return _rhythmusDao;
      }
    }
  }

  @Override
  public BodenbelagDao bodenbelagDao() {
    if (_bodenbelagDao != null) {
      return _bodenbelagDao;
    } else {
      synchronized(this) {
        if(_bodenbelagDao == null) {
          _bodenbelagDao = new BodenbelagDao_Impl(this);
        }
        return _bodenbelagDao;
      }
    }
  }

  @Override
  public GlasartDao glasartDao() {
    if (_glasartDao != null) {
      return _glasartDao;
    } else {
      synchronized(this) {
        if(_glasartDao == null) {
          _glasartDao = new GlasartDao_Impl(this);
        }
        return _glasartDao;
      }
    }
  }

  @Override
  public LvEinstellungDao lvEinstellungDao() {
    if (_lvEinstellungDao != null) {
      return _lvEinstellungDao;
    } else {
      synchronized(this) {
        if(_lvEinstellungDao == null) {
          _lvEinstellungDao = new LvEinstellungDao_Impl(this);
        }
        return _lvEinstellungDao;
      }
    }
  }

  @Override
  public ObjektfragebogenDao objektfragebogenDao() {
    if (_objektfragebogenDao != null) {
      return _objektfragebogenDao;
    } else {
      synchronized(this) {
        if(_objektfragebogenDao == null) {
          _objektfragebogenDao = new ObjektfragebogenDao_Impl(this);
        }
        return _objektfragebogenDao;
      }
    }
  }
}
