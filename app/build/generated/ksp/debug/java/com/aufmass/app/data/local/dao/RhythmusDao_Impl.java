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
import com.aufmass.app.data.local.entity.RhythmusEntity;
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
public final class RhythmusDao_Impl implements RhythmusDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RhythmusEntity> __insertionAdapterOfRhythmusEntity;

  private final EntityDeletionOrUpdateAdapter<RhythmusEntity> __deletionAdapterOfRhythmusEntity;

  private final EntityDeletionOrUpdateAdapter<RhythmusEntity> __updateAdapterOfRhythmusEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public RhythmusDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRhythmusEntity = new EntityInsertionAdapter<RhythmusEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `einstellung_rhythmus` (`id`,`klartext`,`exportwert`,`lvWert`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RhythmusEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getKlartext());
        statement.bindDouble(3, entity.getExportwert());
        statement.bindDouble(4, entity.getLvWert());
      }
    };
    this.__deletionAdapterOfRhythmusEntity = new EntityDeletionOrUpdateAdapter<RhythmusEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `einstellung_rhythmus` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RhythmusEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfRhythmusEntity = new EntityDeletionOrUpdateAdapter<RhythmusEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `einstellung_rhythmus` SET `id` = ?,`klartext` = ?,`exportwert` = ?,`lvWert` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RhythmusEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getKlartext());
        statement.bindDouble(3, entity.getExportwert());
        statement.bindDouble(4, entity.getLvWert());
        statement.bindLong(5, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM einstellung_rhythmus";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final RhythmusEntity rhythmus,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRhythmusEntity.insertAndReturnId(rhythmus);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final RhythmusEntity rhythmus,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRhythmusEntity.handle(rhythmus);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final RhythmusEntity rhythmus,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRhythmusEntity.handle(rhythmus);
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
  public Flow<List<RhythmusEntity>> getAllRhythmen() {
    final String _sql = "SELECT * FROM einstellung_rhythmus ORDER BY exportwert";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"einstellung_rhythmus"}, new Callable<List<RhythmusEntity>>() {
      @Override
      @NonNull
      public List<RhythmusEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKlartext = CursorUtil.getColumnIndexOrThrow(_cursor, "klartext");
          final int _cursorIndexOfExportwert = CursorUtil.getColumnIndexOrThrow(_cursor, "exportwert");
          final int _cursorIndexOfLvWert = CursorUtil.getColumnIndexOrThrow(_cursor, "lvWert");
          final List<RhythmusEntity> _result = new ArrayList<RhythmusEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RhythmusEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpKlartext;
            _tmpKlartext = _cursor.getString(_cursorIndexOfKlartext);
            final double _tmpExportwert;
            _tmpExportwert = _cursor.getDouble(_cursorIndexOfExportwert);
            final double _tmpLvWert;
            _tmpLvWert = _cursor.getDouble(_cursorIndexOfLvWert);
            _item = new RhythmusEntity(_tmpId,_tmpKlartext,_tmpExportwert,_tmpLvWert);
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
