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
import com.aufmass.app.data.local.entity.GlasEntity;
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
public final class GlasDao_Impl implements GlasDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GlasEntity> __insertionAdapterOfGlasEntity;

  private final EntityDeletionOrUpdateAdapter<GlasEntity> __deletionAdapterOfGlasEntity;

  private final EntityDeletionOrUpdateAdapter<GlasEntity> __updateAdapterOfGlasEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public GlasDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGlasEntity = new EntityInsertionAdapter<GlasEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `glas` (`id`,`aufmassId`,`bezeichnung`,`glasart`,`anzahl`,`breite`,`hoehe`,`notizen`,`fotoPath`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GlasEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getAufmassId());
        statement.bindString(3, entity.getBezeichnung());
        statement.bindString(4, entity.getGlasart());
        statement.bindLong(5, entity.getAnzahl());
        statement.bindDouble(6, entity.getBreite());
        statement.bindDouble(7, entity.getHoehe());
        statement.bindString(8, entity.getNotizen());
        statement.bindString(9, entity.getFotoPath());
      }
    };
    this.__deletionAdapterOfGlasEntity = new EntityDeletionOrUpdateAdapter<GlasEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `glas` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GlasEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfGlasEntity = new EntityDeletionOrUpdateAdapter<GlasEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `glas` SET `id` = ?,`aufmassId` = ?,`bezeichnung` = ?,`glasart` = ?,`anzahl` = ?,`breite` = ?,`hoehe` = ?,`notizen` = ?,`fotoPath` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GlasEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getAufmassId());
        statement.bindString(3, entity.getBezeichnung());
        statement.bindString(4, entity.getGlasart());
        statement.bindLong(5, entity.getAnzahl());
        statement.bindDouble(6, entity.getBreite());
        statement.bindDouble(7, entity.getHoehe());
        statement.bindString(8, entity.getNotizen());
        statement.bindString(9, entity.getFotoPath());
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM glas WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final GlasEntity glas, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfGlasEntity.insertAndReturnId(glas);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final GlasEntity glas, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfGlasEntity.handle(glas);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final GlasEntity glas, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfGlasEntity.handle(glas);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<GlasEntity>> getGlasByAufmassId(final long aufmassId) {
    final String _sql = "SELECT * FROM glas WHERE aufmassId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, aufmassId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"glas"}, new Callable<List<GlasEntity>>() {
      @Override
      @NonNull
      public List<GlasEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAufmassId = CursorUtil.getColumnIndexOrThrow(_cursor, "aufmassId");
          final int _cursorIndexOfBezeichnung = CursorUtil.getColumnIndexOrThrow(_cursor, "bezeichnung");
          final int _cursorIndexOfGlasart = CursorUtil.getColumnIndexOrThrow(_cursor, "glasart");
          final int _cursorIndexOfAnzahl = CursorUtil.getColumnIndexOrThrow(_cursor, "anzahl");
          final int _cursorIndexOfBreite = CursorUtil.getColumnIndexOrThrow(_cursor, "breite");
          final int _cursorIndexOfHoehe = CursorUtil.getColumnIndexOrThrow(_cursor, "hoehe");
          final int _cursorIndexOfNotizen = CursorUtil.getColumnIndexOrThrow(_cursor, "notizen");
          final int _cursorIndexOfFotoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoPath");
          final List<GlasEntity> _result = new ArrayList<GlasEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GlasEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpAufmassId;
            _tmpAufmassId = _cursor.getLong(_cursorIndexOfAufmassId);
            final String _tmpBezeichnung;
            _tmpBezeichnung = _cursor.getString(_cursorIndexOfBezeichnung);
            final String _tmpGlasart;
            _tmpGlasart = _cursor.getString(_cursorIndexOfGlasart);
            final int _tmpAnzahl;
            _tmpAnzahl = _cursor.getInt(_cursorIndexOfAnzahl);
            final double _tmpBreite;
            _tmpBreite = _cursor.getDouble(_cursorIndexOfBreite);
            final double _tmpHoehe;
            _tmpHoehe = _cursor.getDouble(_cursorIndexOfHoehe);
            final String _tmpNotizen;
            _tmpNotizen = _cursor.getString(_cursorIndexOfNotizen);
            final String _tmpFotoPath;
            _tmpFotoPath = _cursor.getString(_cursorIndexOfFotoPath);
            _item = new GlasEntity(_tmpId,_tmpAufmassId,_tmpBezeichnung,_tmpGlasart,_tmpAnzahl,_tmpBreite,_tmpHoehe,_tmpNotizen,_tmpFotoPath);
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
  public Object getGlasById(final long id, final Continuation<? super GlasEntity> $completion) {
    final String _sql = "SELECT * FROM glas WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<GlasEntity>() {
      @Override
      @Nullable
      public GlasEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAufmassId = CursorUtil.getColumnIndexOrThrow(_cursor, "aufmassId");
          final int _cursorIndexOfBezeichnung = CursorUtil.getColumnIndexOrThrow(_cursor, "bezeichnung");
          final int _cursorIndexOfGlasart = CursorUtil.getColumnIndexOrThrow(_cursor, "glasart");
          final int _cursorIndexOfAnzahl = CursorUtil.getColumnIndexOrThrow(_cursor, "anzahl");
          final int _cursorIndexOfBreite = CursorUtil.getColumnIndexOrThrow(_cursor, "breite");
          final int _cursorIndexOfHoehe = CursorUtil.getColumnIndexOrThrow(_cursor, "hoehe");
          final int _cursorIndexOfNotizen = CursorUtil.getColumnIndexOrThrow(_cursor, "notizen");
          final int _cursorIndexOfFotoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoPath");
          final GlasEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpAufmassId;
            _tmpAufmassId = _cursor.getLong(_cursorIndexOfAufmassId);
            final String _tmpBezeichnung;
            _tmpBezeichnung = _cursor.getString(_cursorIndexOfBezeichnung);
            final String _tmpGlasart;
            _tmpGlasart = _cursor.getString(_cursorIndexOfGlasart);
            final int _tmpAnzahl;
            _tmpAnzahl = _cursor.getInt(_cursorIndexOfAnzahl);
            final double _tmpBreite;
            _tmpBreite = _cursor.getDouble(_cursorIndexOfBreite);
            final double _tmpHoehe;
            _tmpHoehe = _cursor.getDouble(_cursorIndexOfHoehe);
            final String _tmpNotizen;
            _tmpNotizen = _cursor.getString(_cursorIndexOfNotizen);
            final String _tmpFotoPath;
            _tmpFotoPath = _cursor.getString(_cursorIndexOfFotoPath);
            _result = new GlasEntity(_tmpId,_tmpAufmassId,_tmpBezeichnung,_tmpGlasart,_tmpAnzahl,_tmpBreite,_tmpHoehe,_tmpNotizen,_tmpFotoPath);
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
