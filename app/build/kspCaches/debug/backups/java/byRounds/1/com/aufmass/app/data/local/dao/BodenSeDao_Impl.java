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
import com.aufmass.app.data.local.entity.BodenSeEntity;
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
public final class BodenSeDao_Impl implements BodenSeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BodenSeEntity> __insertionAdapterOfBodenSeEntity;

  private final EntityDeletionOrUpdateAdapter<BodenSeEntity> __deletionAdapterOfBodenSeEntity;

  private final EntityDeletionOrUpdateAdapter<BodenSeEntity> __updateAdapterOfBodenSeEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public BodenSeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBodenSeEntity = new EntityInsertionAdapter<BodenSeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `boden_se` (`id`,`aufmassId`,`bezeichnung`,`bodenart`,`anzahl`,`laenge`,`breite`,`notizen`,`fotoPath`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BodenSeEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getAufmassId());
        statement.bindString(3, entity.getBezeichnung());
        statement.bindString(4, entity.getBodenart());
        statement.bindLong(5, entity.getAnzahl());
        statement.bindDouble(6, entity.getLaenge());
        statement.bindDouble(7, entity.getBreite());
        statement.bindString(8, entity.getNotizen());
        statement.bindString(9, entity.getFotoPath());
      }
    };
    this.__deletionAdapterOfBodenSeEntity = new EntityDeletionOrUpdateAdapter<BodenSeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `boden_se` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BodenSeEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfBodenSeEntity = new EntityDeletionOrUpdateAdapter<BodenSeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `boden_se` SET `id` = ?,`aufmassId` = ?,`bezeichnung` = ?,`bodenart` = ?,`anzahl` = ?,`laenge` = ?,`breite` = ?,`notizen` = ?,`fotoPath` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BodenSeEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getAufmassId());
        statement.bindString(3, entity.getBezeichnung());
        statement.bindString(4, entity.getBodenart());
        statement.bindLong(5, entity.getAnzahl());
        statement.bindDouble(6, entity.getLaenge());
        statement.bindDouble(7, entity.getBreite());
        statement.bindString(8, entity.getNotizen());
        statement.bindString(9, entity.getFotoPath());
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM boden_se WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final BodenSeEntity bodenSe, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBodenSeEntity.insertAndReturnId(bodenSe);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final BodenSeEntity bodenSe, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBodenSeEntity.handle(bodenSe);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final BodenSeEntity bodenSe, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBodenSeEntity.handle(bodenSe);
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
  public Flow<List<BodenSeEntity>> getBodenSeByAufmassId(final long aufmassId) {
    final String _sql = "SELECT * FROM boden_se WHERE aufmassId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, aufmassId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"boden_se"}, new Callable<List<BodenSeEntity>>() {
      @Override
      @NonNull
      public List<BodenSeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAufmassId = CursorUtil.getColumnIndexOrThrow(_cursor, "aufmassId");
          final int _cursorIndexOfBezeichnung = CursorUtil.getColumnIndexOrThrow(_cursor, "bezeichnung");
          final int _cursorIndexOfBodenart = CursorUtil.getColumnIndexOrThrow(_cursor, "bodenart");
          final int _cursorIndexOfAnzahl = CursorUtil.getColumnIndexOrThrow(_cursor, "anzahl");
          final int _cursorIndexOfLaenge = CursorUtil.getColumnIndexOrThrow(_cursor, "laenge");
          final int _cursorIndexOfBreite = CursorUtil.getColumnIndexOrThrow(_cursor, "breite");
          final int _cursorIndexOfNotizen = CursorUtil.getColumnIndexOrThrow(_cursor, "notizen");
          final int _cursorIndexOfFotoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoPath");
          final List<BodenSeEntity> _result = new ArrayList<BodenSeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BodenSeEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpAufmassId;
            _tmpAufmassId = _cursor.getLong(_cursorIndexOfAufmassId);
            final String _tmpBezeichnung;
            _tmpBezeichnung = _cursor.getString(_cursorIndexOfBezeichnung);
            final String _tmpBodenart;
            _tmpBodenart = _cursor.getString(_cursorIndexOfBodenart);
            final int _tmpAnzahl;
            _tmpAnzahl = _cursor.getInt(_cursorIndexOfAnzahl);
            final double _tmpLaenge;
            _tmpLaenge = _cursor.getDouble(_cursorIndexOfLaenge);
            final double _tmpBreite;
            _tmpBreite = _cursor.getDouble(_cursorIndexOfBreite);
            final String _tmpNotizen;
            _tmpNotizen = _cursor.getString(_cursorIndexOfNotizen);
            final String _tmpFotoPath;
            _tmpFotoPath = _cursor.getString(_cursorIndexOfFotoPath);
            _item = new BodenSeEntity(_tmpId,_tmpAufmassId,_tmpBezeichnung,_tmpBodenart,_tmpAnzahl,_tmpLaenge,_tmpBreite,_tmpNotizen,_tmpFotoPath);
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
  public Object getBodenSeById(final long id,
      final Continuation<? super BodenSeEntity> $completion) {
    final String _sql = "SELECT * FROM boden_se WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BodenSeEntity>() {
      @Override
      @Nullable
      public BodenSeEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAufmassId = CursorUtil.getColumnIndexOrThrow(_cursor, "aufmassId");
          final int _cursorIndexOfBezeichnung = CursorUtil.getColumnIndexOrThrow(_cursor, "bezeichnung");
          final int _cursorIndexOfBodenart = CursorUtil.getColumnIndexOrThrow(_cursor, "bodenart");
          final int _cursorIndexOfAnzahl = CursorUtil.getColumnIndexOrThrow(_cursor, "anzahl");
          final int _cursorIndexOfLaenge = CursorUtil.getColumnIndexOrThrow(_cursor, "laenge");
          final int _cursorIndexOfBreite = CursorUtil.getColumnIndexOrThrow(_cursor, "breite");
          final int _cursorIndexOfNotizen = CursorUtil.getColumnIndexOrThrow(_cursor, "notizen");
          final int _cursorIndexOfFotoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoPath");
          final BodenSeEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpAufmassId;
            _tmpAufmassId = _cursor.getLong(_cursorIndexOfAufmassId);
            final String _tmpBezeichnung;
            _tmpBezeichnung = _cursor.getString(_cursorIndexOfBezeichnung);
            final String _tmpBodenart;
            _tmpBodenart = _cursor.getString(_cursorIndexOfBodenart);
            final int _tmpAnzahl;
            _tmpAnzahl = _cursor.getInt(_cursorIndexOfAnzahl);
            final double _tmpLaenge;
            _tmpLaenge = _cursor.getDouble(_cursorIndexOfLaenge);
            final double _tmpBreite;
            _tmpBreite = _cursor.getDouble(_cursorIndexOfBreite);
            final String _tmpNotizen;
            _tmpNotizen = _cursor.getString(_cursorIndexOfNotizen);
            final String _tmpFotoPath;
            _tmpFotoPath = _cursor.getString(_cursorIndexOfFotoPath);
            _result = new BodenSeEntity(_tmpId,_tmpAufmassId,_tmpBezeichnung,_tmpBodenart,_tmpAnzahl,_tmpLaenge,_tmpBreite,_tmpNotizen,_tmpFotoPath);
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
