package com.aufmass.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.aufmass.app.data.local.entity.ObjektfragebogenEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ObjektfragebogenDao_Impl implements ObjektfragebogenDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ObjektfragebogenEntity> __insertionAdapterOfObjektfragebogenEntity;

  private final EntityDeletionOrUpdateAdapter<ObjektfragebogenEntity> __deletionAdapterOfObjektfragebogenEntity;

  private final EntityDeletionOrUpdateAdapter<ObjektfragebogenEntity> __updateAdapterOfObjektfragebogenEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByAufmassId;

  public ObjektfragebogenDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfObjektfragebogenEntity = new EntityInsertionAdapter<ObjektfragebogenEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `objektfragebogen` (`id`,`aufmassId`,`materialkammer`,`waschmaschine`,`schmutzfangzone`,`wasser`,`strom`,`muelltrennung`,`muellentsorgung`,`aufzug`,`reinigungszustand`,`wechselgruende`,`schluesselobjekt`,`alarmanlage`,`besonderheiten`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ObjektfragebogenEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getAufmassId());
        statement.bindString(3, entity.getMaterialkammer());
        final int _tmp = entity.getWaschmaschine() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.getSchmutzfangzone() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        final int _tmp_2 = entity.getWasser() ? 1 : 0;
        statement.bindLong(6, _tmp_2);
        final int _tmp_3 = entity.getStrom() ? 1 : 0;
        statement.bindLong(7, _tmp_3);
        final int _tmp_4 = entity.getMuelltrennung() ? 1 : 0;
        statement.bindLong(8, _tmp_4);
        statement.bindString(9, entity.getMuellentsorgung());
        final int _tmp_5 = entity.getAufzug() ? 1 : 0;
        statement.bindLong(10, _tmp_5);
        statement.bindString(11, entity.getReinigungszustand());
        statement.bindString(12, entity.getWechselgruende());
        final int _tmp_6 = entity.getSchluesselobjekt() ? 1 : 0;
        statement.bindLong(13, _tmp_6);
        final int _tmp_7 = entity.getAlarmanlage() ? 1 : 0;
        statement.bindLong(14, _tmp_7);
        statement.bindString(15, entity.getBesonderheiten());
      }
    };
    this.__deletionAdapterOfObjektfragebogenEntity = new EntityDeletionOrUpdateAdapter<ObjektfragebogenEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `objektfragebogen` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ObjektfragebogenEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfObjektfragebogenEntity = new EntityDeletionOrUpdateAdapter<ObjektfragebogenEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `objektfragebogen` SET `id` = ?,`aufmassId` = ?,`materialkammer` = ?,`waschmaschine` = ?,`schmutzfangzone` = ?,`wasser` = ?,`strom` = ?,`muelltrennung` = ?,`muellentsorgung` = ?,`aufzug` = ?,`reinigungszustand` = ?,`wechselgruende` = ?,`schluesselobjekt` = ?,`alarmanlage` = ?,`besonderheiten` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ObjektfragebogenEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getAufmassId());
        statement.bindString(3, entity.getMaterialkammer());
        final int _tmp = entity.getWaschmaschine() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.getSchmutzfangzone() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        final int _tmp_2 = entity.getWasser() ? 1 : 0;
        statement.bindLong(6, _tmp_2);
        final int _tmp_3 = entity.getStrom() ? 1 : 0;
        statement.bindLong(7, _tmp_3);
        final int _tmp_4 = entity.getMuelltrennung() ? 1 : 0;
        statement.bindLong(8, _tmp_4);
        statement.bindString(9, entity.getMuellentsorgung());
        final int _tmp_5 = entity.getAufzug() ? 1 : 0;
        statement.bindLong(10, _tmp_5);
        statement.bindString(11, entity.getReinigungszustand());
        statement.bindString(12, entity.getWechselgruende());
        final int _tmp_6 = entity.getSchluesselobjekt() ? 1 : 0;
        statement.bindLong(13, _tmp_6);
        final int _tmp_7 = entity.getAlarmanlage() ? 1 : 0;
        statement.bindLong(14, _tmp_7);
        statement.bindString(15, entity.getBesonderheiten());
        statement.bindLong(16, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteByAufmassId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM objektfragebogen WHERE aufmassId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final ObjektfragebogenEntity objektfragebogen,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfObjektfragebogenEntity.insert(objektfragebogen);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final ObjektfragebogenEntity objektfragebogen,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfObjektfragebogenEntity.handle(objektfragebogen);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final ObjektfragebogenEntity objektfragebogen,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfObjektfragebogenEntity.handle(objektfragebogen);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByAufmassId(final long aufmassId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByAufmassId.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, aufmassId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteByAufmassId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<ObjektfragebogenEntity> getByAufmassId(final long aufmassId) {
    final String _sql = "SELECT * FROM objektfragebogen WHERE aufmassId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, aufmassId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"objektfragebogen"}, new Callable<ObjektfragebogenEntity>() {
      @Override
      @Nullable
      public ObjektfragebogenEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAufmassId = CursorUtil.getColumnIndexOrThrow(_cursor, "aufmassId");
          final int _cursorIndexOfMaterialkammer = CursorUtil.getColumnIndexOrThrow(_cursor, "materialkammer");
          final int _cursorIndexOfWaschmaschine = CursorUtil.getColumnIndexOrThrow(_cursor, "waschmaschine");
          final int _cursorIndexOfSchmutzfangzone = CursorUtil.getColumnIndexOrThrow(_cursor, "schmutzfangzone");
          final int _cursorIndexOfWasser = CursorUtil.getColumnIndexOrThrow(_cursor, "wasser");
          final int _cursorIndexOfStrom = CursorUtil.getColumnIndexOrThrow(_cursor, "strom");
          final int _cursorIndexOfMuelltrennung = CursorUtil.getColumnIndexOrThrow(_cursor, "muelltrennung");
          final int _cursorIndexOfMuellentsorgung = CursorUtil.getColumnIndexOrThrow(_cursor, "muellentsorgung");
          final int _cursorIndexOfAufzug = CursorUtil.getColumnIndexOrThrow(_cursor, "aufzug");
          final int _cursorIndexOfReinigungszustand = CursorUtil.getColumnIndexOrThrow(_cursor, "reinigungszustand");
          final int _cursorIndexOfWechselgruende = CursorUtil.getColumnIndexOrThrow(_cursor, "wechselgruende");
          final int _cursorIndexOfSchluesselobjekt = CursorUtil.getColumnIndexOrThrow(_cursor, "schluesselobjekt");
          final int _cursorIndexOfAlarmanlage = CursorUtil.getColumnIndexOrThrow(_cursor, "alarmanlage");
          final int _cursorIndexOfBesonderheiten = CursorUtil.getColumnIndexOrThrow(_cursor, "besonderheiten");
          final ObjektfragebogenEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpAufmassId;
            _tmpAufmassId = _cursor.getLong(_cursorIndexOfAufmassId);
            final String _tmpMaterialkammer;
            _tmpMaterialkammer = _cursor.getString(_cursorIndexOfMaterialkammer);
            final boolean _tmpWaschmaschine;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWaschmaschine);
            _tmpWaschmaschine = _tmp != 0;
            final boolean _tmpSchmutzfangzone;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSchmutzfangzone);
            _tmpSchmutzfangzone = _tmp_1 != 0;
            final boolean _tmpWasser;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfWasser);
            _tmpWasser = _tmp_2 != 0;
            final boolean _tmpStrom;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfStrom);
            _tmpStrom = _tmp_3 != 0;
            final boolean _tmpMuelltrennung;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfMuelltrennung);
            _tmpMuelltrennung = _tmp_4 != 0;
            final String _tmpMuellentsorgung;
            _tmpMuellentsorgung = _cursor.getString(_cursorIndexOfMuellentsorgung);
            final boolean _tmpAufzug;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfAufzug);
            _tmpAufzug = _tmp_5 != 0;
            final String _tmpReinigungszustand;
            _tmpReinigungszustand = _cursor.getString(_cursorIndexOfReinigungszustand);
            final String _tmpWechselgruende;
            _tmpWechselgruende = _cursor.getString(_cursorIndexOfWechselgruende);
            final boolean _tmpSchluesselobjekt;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfSchluesselobjekt);
            _tmpSchluesselobjekt = _tmp_6 != 0;
            final boolean _tmpAlarmanlage;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfAlarmanlage);
            _tmpAlarmanlage = _tmp_7 != 0;
            final String _tmpBesonderheiten;
            _tmpBesonderheiten = _cursor.getString(_cursorIndexOfBesonderheiten);
            _result = new ObjektfragebogenEntity(_tmpId,_tmpAufmassId,_tmpMaterialkammer,_tmpWaschmaschine,_tmpSchmutzfangzone,_tmpWasser,_tmpStrom,_tmpMuelltrennung,_tmpMuellentsorgung,_tmpAufzug,_tmpReinigungszustand,_tmpWechselgruende,_tmpSchluesselobjekt,_tmpAlarmanlage,_tmpBesonderheiten);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getById(final long id,
      final Continuation<? super ObjektfragebogenEntity> $completion) {
    final String _sql = "SELECT * FROM objektfragebogen WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ObjektfragebogenEntity>() {
      @Override
      @Nullable
      public ObjektfragebogenEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAufmassId = CursorUtil.getColumnIndexOrThrow(_cursor, "aufmassId");
          final int _cursorIndexOfMaterialkammer = CursorUtil.getColumnIndexOrThrow(_cursor, "materialkammer");
          final int _cursorIndexOfWaschmaschine = CursorUtil.getColumnIndexOrThrow(_cursor, "waschmaschine");
          final int _cursorIndexOfSchmutzfangzone = CursorUtil.getColumnIndexOrThrow(_cursor, "schmutzfangzone");
          final int _cursorIndexOfWasser = CursorUtil.getColumnIndexOrThrow(_cursor, "wasser");
          final int _cursorIndexOfStrom = CursorUtil.getColumnIndexOrThrow(_cursor, "strom");
          final int _cursorIndexOfMuelltrennung = CursorUtil.getColumnIndexOrThrow(_cursor, "muelltrennung");
          final int _cursorIndexOfMuellentsorgung = CursorUtil.getColumnIndexOrThrow(_cursor, "muellentsorgung");
          final int _cursorIndexOfAufzug = CursorUtil.getColumnIndexOrThrow(_cursor, "aufzug");
          final int _cursorIndexOfReinigungszustand = CursorUtil.getColumnIndexOrThrow(_cursor, "reinigungszustand");
          final int _cursorIndexOfWechselgruende = CursorUtil.getColumnIndexOrThrow(_cursor, "wechselgruende");
          final int _cursorIndexOfSchluesselobjekt = CursorUtil.getColumnIndexOrThrow(_cursor, "schluesselobjekt");
          final int _cursorIndexOfAlarmanlage = CursorUtil.getColumnIndexOrThrow(_cursor, "alarmanlage");
          final int _cursorIndexOfBesonderheiten = CursorUtil.getColumnIndexOrThrow(_cursor, "besonderheiten");
          final ObjektfragebogenEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpAufmassId;
            _tmpAufmassId = _cursor.getLong(_cursorIndexOfAufmassId);
            final String _tmpMaterialkammer;
            _tmpMaterialkammer = _cursor.getString(_cursorIndexOfMaterialkammer);
            final boolean _tmpWaschmaschine;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWaschmaschine);
            _tmpWaschmaschine = _tmp != 0;
            final boolean _tmpSchmutzfangzone;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSchmutzfangzone);
            _tmpSchmutzfangzone = _tmp_1 != 0;
            final boolean _tmpWasser;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfWasser);
            _tmpWasser = _tmp_2 != 0;
            final boolean _tmpStrom;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfStrom);
            _tmpStrom = _tmp_3 != 0;
            final boolean _tmpMuelltrennung;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfMuelltrennung);
            _tmpMuelltrennung = _tmp_4 != 0;
            final String _tmpMuellentsorgung;
            _tmpMuellentsorgung = _cursor.getString(_cursorIndexOfMuellentsorgung);
            final boolean _tmpAufzug;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfAufzug);
            _tmpAufzug = _tmp_5 != 0;
            final String _tmpReinigungszustand;
            _tmpReinigungszustand = _cursor.getString(_cursorIndexOfReinigungszustand);
            final String _tmpWechselgruende;
            _tmpWechselgruende = _cursor.getString(_cursorIndexOfWechselgruende);
            final boolean _tmpSchluesselobjekt;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfSchluesselobjekt);
            _tmpSchluesselobjekt = _tmp_6 != 0;
            final boolean _tmpAlarmanlage;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfAlarmanlage);
            _tmpAlarmanlage = _tmp_7 != 0;
            final String _tmpBesonderheiten;
            _tmpBesonderheiten = _cursor.getString(_cursorIndexOfBesonderheiten);
            _result = new ObjektfragebogenEntity(_tmpId,_tmpAufmassId,_tmpMaterialkammer,_tmpWaschmaschine,_tmpSchmutzfangzone,_tmpWasser,_tmpStrom,_tmpMuelltrennung,_tmpMuellentsorgung,_tmpAufzug,_tmpReinigungszustand,_tmpWechselgruende,_tmpSchluesselobjekt,_tmpAlarmanlage,_tmpBesonderheiten);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
