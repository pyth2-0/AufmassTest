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
import com.aufmass.app.data.local.entity.RaumartEntity;
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
public final class RaumartDao_Impl implements RaumartDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RaumartEntity> __insertionAdapterOfRaumartEntity;

  private final EntityDeletionOrUpdateAdapter<RaumartEntity> __deletionAdapterOfRaumartEntity;

  private final EntityDeletionOrUpdateAdapter<RaumartEntity> __updateAdapterOfRaumartEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public RaumartDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRaumartEntity = new EntityInsertionAdapter<RaumartEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `einstellung_raumart` (`id`,`bezeichnung`,`schnittvorgabe`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RaumartEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getBezeichnung());
        statement.bindDouble(3, entity.getSchnittvorgabe());
      }
    };
    this.__deletionAdapterOfRaumartEntity = new EntityDeletionOrUpdateAdapter<RaumartEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `einstellung_raumart` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RaumartEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfRaumartEntity = new EntityDeletionOrUpdateAdapter<RaumartEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `einstellung_raumart` SET `id` = ?,`bezeichnung` = ?,`schnittvorgabe` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RaumartEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getBezeichnung());
        statement.bindDouble(3, entity.getSchnittvorgabe());
        statement.bindLong(4, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM einstellung_raumart";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final RaumartEntity raumart, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRaumartEntity.insertAndReturnId(raumart);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final RaumartEntity raumart, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRaumartEntity.handle(raumart);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final RaumartEntity raumart, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRaumartEntity.handle(raumart);
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
  public Flow<List<RaumartEntity>> getAllRaumarten() {
    final String _sql = "SELECT * FROM einstellung_raumart ORDER BY bezeichnung";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"einstellung_raumart"}, new Callable<List<RaumartEntity>>() {
      @Override
      @NonNull
      public List<RaumartEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBezeichnung = CursorUtil.getColumnIndexOrThrow(_cursor, "bezeichnung");
          final int _cursorIndexOfSchnittvorgabe = CursorUtil.getColumnIndexOrThrow(_cursor, "schnittvorgabe");
          final List<RaumartEntity> _result = new ArrayList<RaumartEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RaumartEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpBezeichnung;
            _tmpBezeichnung = _cursor.getString(_cursorIndexOfBezeichnung);
            final double _tmpSchnittvorgabe;
            _tmpSchnittvorgabe = _cursor.getDouble(_cursorIndexOfSchnittvorgabe);
            _item = new RaumartEntity(_tmpId,_tmpBezeichnung,_tmpSchnittvorgabe);
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
