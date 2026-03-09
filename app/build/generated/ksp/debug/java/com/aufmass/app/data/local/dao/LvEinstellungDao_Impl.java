package com.aufmass.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.aufmass.app.data.local.entity.LvEinstellungEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class LvEinstellungDao_Impl implements LvEinstellungDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LvEinstellungEntity> __insertionAdapterOfLvEinstellungEntity;

  private final EntityDeletionOrUpdateAdapter<LvEinstellungEntity> __deletionAdapterOfLvEinstellungEntity;

  private final EntityDeletionOrUpdateAdapter<LvEinstellungEntity> __updateAdapterOfLvEinstellungEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public LvEinstellungDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLvEinstellungEntity = new EntityInsertionAdapter<LvEinstellungEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `einstellung_lv` (`id`,`raumart`,`spalte`,`aufgabe`,`rhythmusPlatzhalter`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LvEinstellungEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getRaumart());
        statement.bindString(3, entity.getSpalte());
        statement.bindString(4, entity.getAufgabe());
        statement.bindString(5, entity.getRhythmusPlatzhalter());
      }
    };
    this.__deletionAdapterOfLvEinstellungEntity = new EntityDeletionOrUpdateAdapter<LvEinstellungEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `einstellung_lv` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LvEinstellungEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfLvEinstellungEntity = new EntityDeletionOrUpdateAdapter<LvEinstellungEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `einstellung_lv` SET `id` = ?,`raumart` = ?,`spalte` = ?,`aufgabe` = ?,`rhythmusPlatzhalter` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LvEinstellungEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getRaumart());
        statement.bindString(3, entity.getSpalte());
        statement.bindString(4, entity.getAufgabe());
        statement.bindString(5, entity.getRhythmusPlatzhalter());
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM einstellung_lv";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final LvEinstellungEntity lv, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfLvEinstellungEntity.insertAndReturnId(lv);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final LvEinstellungEntity lv, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfLvEinstellungEntity.handle(lv);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final LvEinstellungEntity lv, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfLvEinstellungEntity.handle(lv);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
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
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<LvEinstellungEntity>> getAllLvEinstellungen() {
    final String _sql = "SELECT * FROM einstellung_lv ORDER BY raumart, spalte";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"einstellung_lv"}, new Callable<List<LvEinstellungEntity>>() {
      @Override
      @NonNull
      public List<LvEinstellungEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRaumart = CursorUtil.getColumnIndexOrThrow(_cursor, "raumart");
          final int _cursorIndexOfSpalte = CursorUtil.getColumnIndexOrThrow(_cursor, "spalte");
          final int _cursorIndexOfAufgabe = CursorUtil.getColumnIndexOrThrow(_cursor, "aufgabe");
          final int _cursorIndexOfRhythmusPlatzhalter = CursorUtil.getColumnIndexOrThrow(_cursor, "rhythmusPlatzhalter");
          final List<LvEinstellungEntity> _result = new ArrayList<LvEinstellungEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LvEinstellungEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpRaumart;
            _tmpRaumart = _cursor.getString(_cursorIndexOfRaumart);
            final String _tmpSpalte;
            _tmpSpalte = _cursor.getString(_cursorIndexOfSpalte);
            final String _tmpAufgabe;
            _tmpAufgabe = _cursor.getString(_cursorIndexOfAufgabe);
            final String _tmpRhythmusPlatzhalter;
            _tmpRhythmusPlatzhalter = _cursor.getString(_cursorIndexOfRhythmusPlatzhalter);
            _item = new LvEinstellungEntity(_tmpId,_tmpRaumart,_tmpSpalte,_tmpAufgabe,_tmpRhythmusPlatzhalter);
            _result.add(_item);
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
  public Flow<List<LvEinstellungEntity>> getLvEinstellungenByRaumart(final String raumart) {
    final String _sql = "SELECT * FROM einstellung_lv WHERE raumart = ? ORDER BY spalte";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, raumart);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"einstellung_lv"}, new Callable<List<LvEinstellungEntity>>() {
      @Override
      @NonNull
      public List<LvEinstellungEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRaumart = CursorUtil.getColumnIndexOrThrow(_cursor, "raumart");
          final int _cursorIndexOfSpalte = CursorUtil.getColumnIndexOrThrow(_cursor, "spalte");
          final int _cursorIndexOfAufgabe = CursorUtil.getColumnIndexOrThrow(_cursor, "aufgabe");
          final int _cursorIndexOfRhythmusPlatzhalter = CursorUtil.getColumnIndexOrThrow(_cursor, "rhythmusPlatzhalter");
          final List<LvEinstellungEntity> _result = new ArrayList<LvEinstellungEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LvEinstellungEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpRaumart;
            _tmpRaumart = _cursor.getString(_cursorIndexOfRaumart);
            final String _tmpSpalte;
            _tmpSpalte = _cursor.getString(_cursorIndexOfSpalte);
            final String _tmpAufgabe;
            _tmpAufgabe = _cursor.getString(_cursorIndexOfAufgabe);
            final String _tmpRhythmusPlatzhalter;
            _tmpRhythmusPlatzhalter = _cursor.getString(_cursorIndexOfRhythmusPlatzhalter);
            _item = new LvEinstellungEntity(_tmpId,_tmpRaumart,_tmpSpalte,_tmpAufgabe,_tmpRhythmusPlatzhalter);
            _result.add(_item);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
