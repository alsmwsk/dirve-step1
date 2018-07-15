package joy.common;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.joy.db.DbCommand;
import com.joy.db.DbDatabase;
import com.joy.db.DbInterface;
import com.joy.db.DbRecordset;
import java.util.List;

public class JoyDbAdapter extends JoyDbInterface {
    public static boolean DATABASE_CREATE_TYPE = true;
    private static final String TAG = "JoyDbAdapter";
    private final Context context;
    private boolean dbConnectType = false;
    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private Context dbContext;

        DatabaseHelper(Context context) {
            super(context, JoyNInterface.DATABASE_NAME, null, 1);
            this.dbContext = context;
        }

        public void onCreate(SQLiteDatabase db) {
            List<String> sqlList = JoyDbInterface.dbCreate();
            String sql = "";
            JoyDbAdapter.DATABASE_CREATE_TYPE = false;
            for (int i = 0; i < sqlList.size(); i++) {
                sql = (String) sqlList.get(i);
                db.beginTransaction();
                try {
                    db.execSQL(sql);
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                } finally {
                    db.endTransaction();
                }
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public JoyDbAdapter(Context context) {
        this.context = context;
    }

    public JoyDbAdapter open() throws SQLException {
        this.mDbHelper = new DatabaseHelper(this.context);
        this.mDb = this.mDbHelper.getWritableDatabase();
        this.dbConnectType = true;
        return this;
    }

    public void close() {
        this.mDbHelper.close();
        this.dbConnectType = false;
    }

    public void dbUpgrade(String oldVersion, String newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        DbDatabase dbDatabase = new DbDatabase(JoyNInterface.MAIN_IP, 6408);
        dbDatabase.open();
        DbCommand command = new DbCommand(dbDatabase, "sp_mobile02_getUpdateList_new");
        command.addParameter((int) DbInterface.STRING_TYPE, 1, oldVersion);
        command.addParameter((int) DbInterface.STRING_TYPE, 1, newVersion);
        DbRecordset pRs = new DbRecordset(dbDatabase);
        if (pRs.execute(command) && pRs.getRowCount() > 0) {
            for (int i = 0; i < pRs.getRowCount(); i++) {
                try {
                    String dbSql = pRs.getFieldStringValue("DBUPDATE_SQL");
                    if (!(dbSql.equals("") || queryExecute(dbSql))) {
                        queryExecute("insert into tb_dbupdate_fail (dbupdate_sql, fail_dt) values ('" + dbSql + "', datetime('now'))");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pRs.moveNext();
            }
        }
        dbDatabase.close();
    }

    public boolean queryExecute(String sql) {
        try {
            this.mDb.execSQL(sql);
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Query Execute Error!!!" + e);
            return false;
        }
    }

    public Cursor queryGetCursor(String sql) {
        Cursor cursor = null;
        try {
            cursor = this.mDb.rawQuery(sql, null);
            cursor.moveToFirst();
            return cursor;
        } catch (Exception e) {
            Log.d(TAG, "Select Query Error!!!");
            return cursor;
        }
    }

    public boolean getDbConnectType() {
        return this.dbConnectType;
    }
}
