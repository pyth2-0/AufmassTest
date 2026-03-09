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
import com.aufmass.app.data.local.entity.AufmassEntity;
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
public final class AufmassDao_Impl implements AufmassDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AufmassEntity> __insertionAdapterOfAufmassEntity;

  private final EntityDeletionOrUpdateAdapter<AufmassEntity> __deletionAdapterOfAufmassEntity;

  private final EntityDeletionOrUpdateAdapter<AufmassEntity> __updateAdapterOfAufmassEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public AufmassDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAufmassEntity = new EntityInsertionAdapter<AufmassEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `aufmass` (`id`,`titel`,`firma`,`anschrift`,`objektanschrift`,`standardrhythmus`,`wochentage`,`reinigungszeiten`,`reinigungstage`,`erstelltAm`,`notizen`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AufmassEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitel());
        statement.bindString(3, entity.getFirma());
        statement.bindString(4, entity.getAnschrift());
        statement.bindString(5, entity.getObjektanschrift());
        statement.bindString(6, entity.getStandardrhythmus());
        statement.bindString(7, entity.getWochentage());
        statement.bindString(8, entity.getReinigungszeiten());
        statement.bindString(9, entity.getReinigungstage());
        statement.bindLong(10, entity.getErstelltAm());
        statement.bindString(11, entity.getNotizen());
      }
    };
    this.__deletionAdapterOfAufmassEntity = new EntityDeletionOrUpdateAdapter<AufmassEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `aufmass` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AufmassEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfAufmassEntity = new EntityDeletionOrUpdateAdapter<AufmassEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `aufmass` SET `id` = ?,`titel` = ?,`firma` = ?,`anschrift` = ?,`objektanschrift` = ?,`standardrhythmus` = ?,`wochentage` = ?,`reinigungszeiten` = ?,`reinigungstage` = ?,`erstelltAm` = ?,`notizen` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AufmassEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitel());
        statement.bindString(3, entity.getFirma());
        statement.bindString(4, entity.getAnschrift());
        statement.bindString(5, entity.getObjektanschrift());
        statement.bindString(6, entity.getStandardrhythmus());
        statement.bindString(7, entity.getWochentage());
        statement.bindString(8, entity.getReinigungszeiten());
        statement.bindString(9, entity.getReinigungstage());
        statement.bindLong(10, entity.getErstelltAm());
        statement.bindString(11, entity.getNotizen());
        statement.bindLong(12, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM aufmass WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final AufmassEntity aufmass, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAufmassEntity.insertAndReturnId(aufmass);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final AufmassEntity aufmass, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfAufmassEntity.handle(aufmass);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final AufmassEntity aufmass, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAufmassEntity.handle(aufmass);
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
  public Flow<List<AufmassEntity>> getAllAufmass() {
    final String _sql = "SELECT * FROM aufmass ORDER BY erstelltAm DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"aufmass"}, new Callable<List<AufmassEntity>>() {
      @Override
      @NonNull
      public List<AufmassEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitel = CursorUtil.getColumnIndexOrThrow(_cursor, "titel");
          final int _cursorIndexOfFirma = CursorUtil.getColumnIndexOrThrow(_cursor, "firma");
          final int _cursorIndexOfAnschrift = CursorUtil.getColumnIndexOrThrow(_cursor, "anschrift");
          final int _cursorIndexOfObjektanschrift = CursorUtil.getColumnIndexOrThrow(_cursor, "objektanschrift");
          final int _cursorIndexOfStandardrhythmus = CursorUtil.getColumnIndexOrThrow(_cursor, "standardrhythmus");
          final int _cursorIndexOfWochentage = CursorUtil.getColumnIndexOrThrow(_cursor, "wochentage");
          final int _cursorIndexOfReinigungszeiten = CursorUtil.getColumnIndexOrThrow(_cursor, "reinigungszeiten");
          final int _cursorIndexOfReinigungstage = CursorUtil.getColumnIndexOrThrow(_cursor, "reinigungstage");
          final int _cursorIndexOfErstelltAm = CursorUtil.getColumnIndexOrThrow(_cursor, "erstelltAm");
          final int _cursorIndexOfNotizen = CursorUtil.getColumnIndexOrThrow(_cursor, "notizen");
          final List<AufmassEntity> _result = new ArrayList<AufmassEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AufmassEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitel;
            _tmpTitel = _cursor.getString(_cursorIndexOfTitel);
            final String _tmpFirma;
            _tmpFirma = _cursor.getString(_cursorIndexOfFirma);
            final String _tmpAnschrift;
            _tmpAnschrift = _cursor.getString(_cursorIndexOfAnschrift);
            final String _tmpObjektanschrift;
            _tmpObjektanschrift = _cursor.getString(_cursorIndexOfObjektanschrift);
            final String _tmpStandardrhythmus;
            _tmpStandardrhythmus = _cursor.getString(_cursorIndexOfStandardrhythmus);
            final String _tmpWochentage;
            _tmpWochentage = _cursor.getString(_cursorIndexOfWochentage);
            final String _tmpReinigungszeiten;
            _tmpReinigungszeiten = _cursor.getString(_cursorIndexOfReinigungszeiten);
            final String _tmpReinigungstage;
            _tmpReinigungstage = _cursor.getString(_cursorIndexOfReinigungstage);
            final long _tmpErstelltAm;
            _tmpErstelltAm = _cursor.getLong(_cursorIndexOfErstelltAm);
            final String _tmpNotizen;
            _tmpNotizen = _cursor.getString(_cursorIndexOfNotizen);
            _item = new AufmassEntity(_tmpId,_tmpTitel,_tmpFirma,_tmpAnschrift,_tmpObjektanschrift,_tmpStandardrhythmus,_tmpWochentage,_tmpReinigungszeiten,_tmpReinigungstage,_tmpErstelltAm,_tmpNotizen);
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
  public Object getAufmassById(final long id,
      final Continuation<? super AufmassEntity> $completion) {
    final String _sql = "SELECT * FROM aufmass WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AufmassEntity>() {
      @Override
      @Nullable
      public AufmassEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitel = CursorUtil.getColumnIndexOrThrow(_cursor, "titel");
          final int _cursorIndexOfFirma = CursorUtil.getColumnIndexOrThrow(_cursor, "firma");
          final int _cursorIndexOfAnschrift = CursorUtil.getColumnIndexOrThrow(_cursor, "anschrift");
          final int _cursorIndexOfObjektanschrift = CursorUtil.getColumnIndexOrThrow(_cursor, "objektanschrift");
          final int _cursorIndexOfStandardrhythmus = CursorUtil.getColumnIndexOrThrow(_cursor, "standardrhythmus");
          final int _cursorIndexOfWochentage = CursorUtil.getColumnIndexOrThrow(_cursor, "wochentage");
          final int _cursorIndexOfReinigungszeiten = CursorUtil.getColumnIndexOrThrow(_cursor, "reinigungszeiten");
          final int _cursorIndexOfReinigungstage = CursorUtil.getColumnIndexOrThrow(_cursor, "reinigungstage");
          final int _cursorIndexOfErstelltAm = CursorUtil.getColumnIndexOrThrow(_cursor, "erstelltAm");
          final int _cursorIndexOfNotizen = CursorUtil.getColumnIndexOrThrow(_cursor, "notizen");
          final AufmassEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitel;
            _tmpTitel = _cursor.getString(_cursorIndexOfTitel);
            final String _tmpFirma;
            _tmpFirma = _cursor.getString(_cursorIndexOfFirma);
            final String _tmpAnschrift;
            _tmpAnschrift = _cursor.getString(_cursorIndexOfAnschrift);
            final String _tmpObjektanschrift;
            _tmpObjektanschrift = _cursor.getString(_cursorIndexOfObjektanschrift);
            final String _tmpStandardrhythmus;
            _tmpStandardrhythmus = _cursor.getString(_cursorIndexOfStandardrhythmus);
            final String _tmpWochentage;
            _tmpWochentage = _cursor.getString(_cursorIndexOfWochentage);
            final String _tmpReinigungszeiten;
            _tmpReinigungszeiten = _cursor.getString(_cursorIndexOfReinigungszeiten);
            final String _tmpReinigungstage;
            _tmpReinigungstage = _cursor.getString(_cursorIndexOfReinigungstage);
            final long _tmpErstelltAm;
            _tmpErstelltAm = _cursor.getLong(_cursorIndexOfErstelltAm);
            final String _tmpNotizen;
            _tmpNotizen = _cursor.getString(_cursorIndexOfNotizen);
            _result = new AufmassEntity(_tmpId,_tmpTitel,_tmpFirma,_tmpAnschrift,_tmpObjektanschrift,_tmpStandardrhythmus,_tmpWochentage,_tmpReinigungszeiten,_tmpReinigungstage,_tmpErstelltAm,_tmpNotizen);
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
