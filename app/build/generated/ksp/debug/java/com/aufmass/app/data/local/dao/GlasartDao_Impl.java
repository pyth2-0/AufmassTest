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
import com.aufmass.app.data.local.entity.GlasartEntity;
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
public final class GlasartDao_Impl implements GlasartDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GlasartEntity> __insertionAdapterOfGlasartEntity;

  private final EntityDeletionOrUpdateAdapter<GlasartEntity> __deletionAdapterOfGlasartEntity;

  private final EntityDeletionOrUpdateAdapter<GlasartEntity> __updateAdapterOfGlasartEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public GlasartDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGlasartEntity = new EntityInsertionAdapter<GlasartEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `einstellung_glasart` (`id`,`bezeichnung`) VALUES (nullif(?, 0),?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GlasartEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getBezeichnung());
      }
    };
    this.__deletionAdapterOfGlasartEntity = new EntityDeletionOrUpdateAdapter<GlasartEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `einstellung_glasart` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GlasartEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfGlasartEntity = new EntityDeletionOrUpdateAdapter<GlasartEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `einstellung_glasart` SET `id` = ?,`bezeichnung` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GlasartEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getBezeichnung());
        statement.bindLong(3, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM einstellung_glasart";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final GlasartEntity glasart, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfGlasartEntity.insertAndReturnId(glasart);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final GlasartEntity glasart, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfGlasartEntity.handle(glasart);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final GlasartEntity glasart, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfGlasartEntity.handle(glasart);
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
  public Flow<List<GlasartEntity>> getAllGlasarten() {
    final String _sql = "SELECT * FROM einstellung_glasart ORDER BY bezeichnung";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"einstellung_glasart"}, new Callable<List<GlasartEntity>>() {
      @Override
      @NonNull
      public List<GlasartEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBezeichnung = CursorUtil.getColumnIndexOrThrow(_cursor, "bezeichnung");
          final List<GlasartEntity> _result = new ArrayList<GlasartEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GlasartEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpBezeichnung;
            _tmpBezeichnung = _cursor.getString(_cursorIndexOfBezeichnung);
            _item = new GlasartEntity(_tmpId,_tmpBezeichnung);
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
