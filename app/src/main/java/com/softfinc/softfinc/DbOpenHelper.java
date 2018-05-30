package com.softfinc.softfinc;



import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteException;
        import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;

import static android.content.Context.MODE_PRIVATE;

public class DbOpenHelper extends SQLiteOpenHelper {
    private Context        mContext;
    private String         mDbPath;
    private String         mDbName;
    private int            mDbVersion;

    public  SQLiteDatabase db;

    public DbOpenHelper(Context context, String dbName, int version) {
      //  super(context,  DATABASE_NAME, null, DATABASE_VERSION);

        super(context, dbName, null, version);
        mContext   = context;
        mDbPath    = context.getApplicationInfo().dataDir + "/";
        mDbName    = dbName;
        mDbVersion = version;
    }

    public boolean exists() {
        SQLiteDatabase db = null;
        try {
       db = SQLiteDatabase.openOrCreateDatabase(mDbPath + mDbName, null);
        }
        catch (SQLiteException e) {
            //database does not exist yet.
        }
        if (db != null) {
            db.close();
            return true;
        } else {
            return false;
        }
    }



    public void openDatabase(int flag) throws SQLiteException, IOException {
        if (!exists()) {
            if (flag == SQLiteDatabase.OPEN_READONLY) {
                this.getReadableDatabase();
            } else if (flag == SQLiteDatabase.OPEN_READWRITE) {
                this.getWritableDatabase();
            }
            InputStream  iStream = null;
            OutputStream oStream = null;
            try {
                iStream = mContext.getAssets().open(mDbName);
                oStream = new FileOutputStream(mDbPath + mDbName);
                byte[]       buffer  = new byte[1024];
                int          length;

                while ((length = iStream.read(buffer)) > 0) {
                    oStream.write(buffer, 0, length);
                }
            } catch (IOException e) {
                throw e;
            } finally {
                if (iStream != null) {
                    iStream.close();
                }

                if (oStream != null) {
                    oStream.flush();
                    oStream.close();
                }
            }
        }

        try {
            if (flag == SQLiteDatabase.OPEN_READWRITE) {
                db = SQLiteDatabase.openDatabase(mDbPath + mDbName, null,
                        SQLiteDatabase.OPEN_READWRITE);
            } else if (flag == SQLiteDatabase.OPEN_READWRITE) {
                db = SQLiteDatabase.openDatabase(mDbPath + mDbName, null,
                        SQLiteDatabase.OPEN_READWRITE);
            }
        } catch (SQLiteException e) {
            throw e;
        }
    }


    @Override
    public synchronized void close() {
        if (db != null) {
            db.close();
        }
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
       // db.execSQL(Note.CREATE_TABLE);
        Log.e("Creo Db ", "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
      //  db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);
        // Create tables again
        onCreate(db);
        Log.e("actualizo db ", "onUpgrade");
    }
}