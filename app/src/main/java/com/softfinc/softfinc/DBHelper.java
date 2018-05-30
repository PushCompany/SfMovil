package com.softfinc.softfinc;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 19;
    // Database Name
    private static final String DATABASE_NAME = "DbSoftfinc.db";

    private Context        mContext;
    private String         mDbPath;
    public  SQLiteDatabase db;

      public DBHelper(Context context) {
        super(context,  DATABASE_NAME, null, DATABASE_VERSION);
          mDbPath    = context.getApplicationInfo().dataDir + "/databases/";//ruta donde se crean las Db por defecto
          mContext   = context;
    }


    public boolean exists() {
        SQLiteDatabase db = null;
        try {
       db = SQLiteDatabase.openOrCreateDatabase(mDbPath + DATABASE_NAME, null);
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
                iStream = mContext.getAssets().open(DATABASE_NAME);
                oStream = new FileOutputStream(mDbPath + DATABASE_NAME);
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
                db = SQLiteDatabase.openDatabase(mDbPath + DATABASE_NAME, null,
                        SQLiteDatabase.OPEN_READWRITE);
            } else if (flag == SQLiteDatabase.OPEN_READWRITE) {
                db = SQLiteDatabase.openDatabase(mDbPath + DATABASE_NAME, null,
                        SQLiteDatabase.OPEN_READWRITE);
            }
        } catch (SQLiteException e) {
            throw e;
        }
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(ConfModel.CREATE_TABLE);//Tb configuracion
        Log.e("Creo Db help1 ", "onCreate");
        // create notes table
        db.execSQL(Note.CREATE_TABLE);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ConfModel.TABLE_NAME);//Tb configuracion
        // Create tables again
        onCreate(db);
        Log.e("actualizo db help1 ", "onUpgrade");
    }


    public String CrearDb() {

        List<ConfModel> lstconfig = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ConfModel.TABLE_NAME + " ORDER BY " +
                ConfModel.COLUMN_COLUMN_ID + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ConfModel configuracion = new ConfModel();
               configuracion.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                lstconfig.add(configuracion);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

       // Log.e("pase aqui ",lstconfig.toString());
        // return notes list
        return "";
    }






}