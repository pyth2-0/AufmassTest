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
import com.aufmass.app.data.local.entity.RaumEntity;
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
public final class RaumDao_Impl implements RaumDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RaumEntity> __insertionAdapterOfRaumEntity;

  private final EntityDeletionOrUpdateAdapter<RaumEntity> __deletionAdapterOfRaumEntity;

  private final EntityDeletionOrUpdateAdapter<RaumEntity> __updateAdapterOfRaumEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public RaumDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRaumEntity = new EntityInsertionAdapter<RaumEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `raum` (`id`,`aufmassId`,`name`,`raumart`,`bodenbelag`,`anzahl`,`laenge`,`breite`,`schnittvorgabe`,`rhythmus`,`notizen`,`fotoPaths`,`zusatzlicheMasse`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RaumEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getAufmassId());
        statement.bindString(3, entity.getName());
        statement.bindString(4, entity.getRaumart());
        statement.bindString(5, entity.getBodenbelag());
        statement.bindLong(6, entity.getAnzahl());
        statement.bindDouble(7, entity.getLaenge());
        statement.bindDouble(8, entity.getBreite());
        statement.bindDouble(9, entity.getSchnittvorgabe());
        statement.bindString(10, entity.getRhythmus());
        statement.bindString(11, entity.getNotizen());
        statement.bindString(12, entity.getFotoPaths());
        statement.bindString(13, entity.getZusatzlicheMasse());
      }
    };
    this.__deletionAdapterOfRaumEntity = new EntityDeletionOrUpdateAdapter<RaumEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `raum` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RaumEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfRaumEntity = new EntityDeletionOrUpdateAdapter<RaumEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `raum` SET `id` = ?,`aufmassId` = ?,`name` = ?,`raumart` = ?,`bodenbelag` = ?,`anzahl` = ?,`laenge` = ?,`breite` = ?,`schnittvorgabe` = ?,`rhythmus` = ?,`notizen` = ?,`fotoPaths` = ?,`zusatzlicheMasse` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RaumEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getAufmassId());
        statement.bindString(3, entity.getName());
        statement.bindString(4, entity.getRaumart());
        statement.bindString(5, entity.getBodenbelag());
        statement.bindLong(6, entity.getAnzahl());
        statement.bindDouble(7, entity.getLaenge());
        statement.bindDouble(8, entity.getBreite());
        statement.bindDouble(9, entity.getSchnittvorgabe());
        statement.bindString(10, entity.getRhythmus());
        statement.bindString(11, entity.getNotizen());
        statement.bindString(12, entity.getFotoPaths());
        statement.bindString(13, entity.getZusatzlicheMasse());
        statement.bindLong(14, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM raum WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final RaumEntity raum, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRaumEntity.insertAndReturnId(raum);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final RaumEntity raum, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRaumEntity.handle(raum);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final RaumEntity raum, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRaumEntity.handle(raum);
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
  public Flow<List<RaumEntity>> getRaeumeByAufmassId(final long aufmassId) {
    final String _sql = "SELECT * FROM raum WHERE aufmassId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, aufmassId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"raum"}, new Callable<List<RaumEntity>>() {
      @Override
      @NonNull
      public List<RaumEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAufmassId = CursorUtil.getColumnIndexOrThrow(_cursor, "aufmassId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfRaumart = CursorUtil.getColumnIndexOrThrow(_cursor, "raumart");
          final int _cursorIndexOfBodenbelag = CursorUtil.getColumnIndexOrThrow(_cursor, "bodenbelag");
          final int _cursorIndexOfAnzahl = CursorUtil.getColumnIndexOrThrow(_cursor, "anzahl");
          final int _cursorIndexOfLaenge = CursorUtil.getColumnIndexOrThrow(_cursor, "laenge");
          final int _cursorIndexOfBreite = CursorUtil.getColumnIndexOrThrow(_cursor, "breite");
          final int _cursorIndexOfSchnittvorgabe = CursorUtil.getColumnIndexOrThrow(_cursor, "schnittvorgabe");
          final int _cursorIndexOfRhythmus = CursorUtil.getColumnIndexOrThrow(_cursor, "rhythmus");
          final int _cursorIndexOfNotizen = CursorUtil.getColumnIndexOrThrow(_cursor, "notizen");
          final int _cursorIndexOfFotoPaths = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoPaths");
          final int _cursorIndexOfZusatzlicheMasse = CursorUtil.getColumnIndexOrThrow(_cursor, "zusatzlicheMasse");
          final List<RaumEntity> _result = new ArrayList<RaumEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RaumEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpAufmassId;
            _tmpAufmassId = _cursor.getLong(_cursorIndexOfAufmassId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpRaumart;
            _tmpRaumart = _cursor.getString(_cursorIndexOfRaumart);
            final String _tmpBodenbelag;
            _tmpBodenbelag = _cursor.getString(_cursorIndexOfBodenbelag);
            final int _tmpAnzahl;
            _tmpAnzahl = _cursor.getInt(_cursorIndexOfAnzahl);
            final double _tmpLaenge;
            _tmpLaenge = _cursor.getDouble(_cursorIndexOfLaenge);
            final double _tmpBreite;
            _tmpBreite = _cursor.getDouble(_cursorIndexOfBreite);
            final double _tmpSchnittvorgabe;
            _tmpSchnittvorgabe = _cursor.getDouble(_cursorIndexOfSchnittvorgabe);
            final String _tmpRhythmus;
            _tmpRhythmus = _cursor.getString(_cursorIndexOfRhythmus);
            final String _tmpNotizen;
            _tmpNotizen = _cursor.getString(_cursorIndexOfNotizen);
            final String _tmpFotoPaths;
            _tmpFotoPaths = _cursor.getString(_cursorIndexOfFotoPaths);
            final String _tmpZusatzlicheMasse;
            _tmpZusatzlicheMasse = _cursor.getString(_cursorIndexOfZusatzlicheMasse);
            _item = new RaumEntity(_tmpId,_tmpAufmassId,_tmpName,_tmpRaumart,_tmpBodenbelag,_tmpAnzahl,_tmpLaenge,_tmpBreite,_tmpSchnittvorgabe,_tmpRhythmus,_tmpNotizen,_tmpFotoPaths,_tmpZusatzlicheMasse);
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
  public Object getRaumById(final long id, final Continuation<? super RaumEntity> $completion) {
    final String _sql = "SELECT * FROM raum WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<RaumEntity>() {
      @Override
      @Nullable
      public RaumEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAufmassId = CursorUtil.getColumnIndexOrThrow(_cursor, "aufmassId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfRaumart = CursorUtil.getColumnIndexOrThrow(_cursor, "raumart");
          final int _cursorIndexOfBodenbelag = CursorUtil.getColumnIndexOrThrow(_cursor, "bodenbelag");
          final int _cursorIndexOfAnzahl = CursorUtil.getColumnIndexOrThrow(_cursor, "anzahl");
          final int _cursorIndexOfLaenge = CursorUtil.getColumnIndexOrThrow(_cursor, "laenge");
          final int _cursorIndexOfBreite = CursorUtil.getColumnIndexOrThrow(_cursor, "breite");
          final int _cursorIndexOfSchnittvorgabe = CursorUtil.getColumnIndexOrThrow(_cursor, "schnittvorgabe");
          final int _cursorIndexOfRhythmus = CursorUtil.getColumnIndexOrThrow(_cursor, "rhythmus");
          final int _cursorIndexOfNotizen = CursorUtil.getColumnIndexOrThrow(_cursor, "notizen");
          final int _cursorIndexOfFotoPaths = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoPaths");
          final int _cursorIndexOfZusatzlicheMasse = CursorUtil.getColumnIndexOrThrow(_cursor, "zusatzlicheMasse");
          final RaumEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpAufmassId;
            _tmpAufmassId = _cursor.getLong(_cursorIndexOfAufmassId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpRaumart;
            _tmpRaumart = _cursor.getString(_cursorIndexOfRaumart);
            final String _tmpBodenbelag;
            _tmpBodenbelag = _cursor.getString(_cursorIndexOfBodenbelag);
            final int _tmpAnzahl;
            _tmpAnzahl = _cursor.getInt(_cursorIndexOfAnzahl);
            final double _tmpLaenge;
            _tmpLaenge = _cursor.getDouble(_cursorIndexOfLaenge);
            final double _tmpBreite;
            _tmpBreite = _cursor.getDouble(_cursorIndexOfBreite);
            final double _tmpSchnittvorgabe;
            _tmpSchnittvorgabe = _cursor.getDouble(_cursorIndexOfSchnittvorgabe);
            final String _tmpRhythmus;
            _tmpRhythmus = _cursor.getString(_cursorIndexOfRhythmus);
            final String _tmpNotizen;
            _tmpNotizen = _cursor.getString(_cursorIndexOfNotizen);
            final String _tmpFotoPaths;
            _tmpFotoPaths = _cursor.getString(_cursorIndexOfFotoPaths);
            final String _tmpZusatzlicheMasse;
            _tmpZusatzlicheMasse = _cursor.getString(_cursorIndexOfZusatzlicheMasse);
            _result = new RaumEntity(_tmpId,_tmpAufmassId,_tmpName,_tmpRaumart,_tmpBodenbelag,_tmpAnzahl,_tmpLaenge,_tmpBreite,_tmpSchnittvorgabe,_tmpRhythmus,_tmpNotizen,_tmpFotoPaths,_tmpZusatzlicheMasse);
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
