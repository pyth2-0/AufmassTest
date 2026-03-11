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
import com.aufmass.app.data.local.entity.BodenbelagEntity;
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
public final class BodenbelagDao_Impl implements BodenbelagDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BodenbelagEntity> __insertionAdapterOfBodenbelagEntity;

  private final EntityDeletionOrUpdateAdapter<BodenbelagEntity> __deletionAdapterOfBodenbelagEntity;

  private final EntityDeletionOrUpdateAdapter<BodenbelagEntity> __updateAdapterOfBodenbelagEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public BodenbelagDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBodenbelagEntity = new EntityInsertionAdapter<BodenbelagEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `einstellung_bodenbelag` (`id`,`bezeichnung`,`abkuerzung`,`quadratmeterSchnitt`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BodenbelagEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getBezeichnung());
        statement.bindString(3, entity.getAbkuerzung());
        statement.bindDouble(4, entity.getQuadratmeterSchnitt());
      }
    };
    this.__deletionAdapterOfBodenbelagEntity = new EntityDeletionOrUpdateAdapter<BodenbelagEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `einstellung_bodenbelag` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BodenbelagEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfBodenbelagEntity = new EntityDeletionOrUpdateAdapter<BodenbelagEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `einstellung_bodenbelag` SET `id` = ?,`bezeichnung` = ?,`abkuerzung` = ?,`quadratmeterSchnitt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BodenbelagEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getBezeichnung());
        statement.bindString(3, entity.getAbkuerzung());
        statement.bindDouble(4, entity.getQuadratmeterSchnitt());
        statement.bindLong(5, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM einstellung_bodenbelag";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final BodenbelagEntity bodenbelag,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBodenbelagEntity.insertAndReturnId(bodenbelag);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final BodenbelagEntity bodenbelag,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBodenbelagEntity.handle(bodenbelag);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final BodenbelagEntity bodenbelag,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBodenbelagEntity.handle(bodenbelag);
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
  public Flow<List<BodenbelagEntity>> getAllBodenbelage() {
    final String _sql = "SELECT * FROM einstellung_bodenbelag ORDER BY bezeichnung";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"einstellung_bodenbelag"}, new Callable<List<BodenbelagEntity>>() {
      @Override
      @NonNull
      public List<BodenbelagEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBezeichnung = CursorUtil.getColumnIndexOrThrow(_cursor, "bezeichnung");
          final int _cursorIndexOfAbkuerzung = CursorUtil.getColumnIndexOrThrow(_cursor, "abkuerzung");
          final int _cursorIndexOfQuadratmeterSchnitt = CursorUtil.getColumnIndexOrThrow(_cursor, "quadratmeterSchnitt");
          final List<BodenbelagEntity> _result = new ArrayList<BodenbelagEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BodenbelagEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpBezeichnung;
            _tmpBezeichnung = _cursor.getString(_cursorIndexOfBezeichnung);
            final String _tmpAbkuerzung;
            _tmpAbkuerzung = _cursor.getString(_cursorIndexOfAbkuerzung);
            final double _tmpQuadratmeterSchnitt;
            _tmpQuadratmeterSchnitt = _cursor.getDouble(_cursorIndexOfQuadratmeterSchnitt);
            _item = new BodenbelagEntity(_tmpId,_tmpBezeichnung,_tmpAbkuerzung,_tmpQuadratmeterSchnitt);
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
