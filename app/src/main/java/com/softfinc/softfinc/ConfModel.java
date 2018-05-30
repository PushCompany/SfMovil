package com.softfinc.softfinc;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.Display;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfModel {

    // variable to hold context
    private Context context;


    public static final String TABLE_NAME = "confi";

    public static final String COLUMN_COLUMN_ID = "id";
    public static final String COLUMN_ID_EMPRESA = "idempresa";
    public static final String COLUMN_N_EMPRESA = "nempresa";
    public static final String COLUMN_SlO_EMPRESA = "sloempresa";
    public static final String COLUMN_ID_USUARIO = "idusuario";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_USUARIO = "usuario";
    public static final String COLUMN_CLAVE = "clave";
    public static final String COLUMN_COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_F_ULT_UPDATE = "fecha";
    public static final String COLUMN_TPRINTER = "tprinter";

    private int id;
    private String idempresa;
    private String nempresa;
    private String sloempresa;
    private String idusuario;
    private int status;
    private String usuario ;
    private String clave;
    private String timestamp;
    private int fUltUpdate;
    private int tprinter;

    //Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_ID_EMPRESA + " TEXT,"
                    + COLUMN_N_EMPRESA + " TEXT,"
                    + COLUMN_SlO_EMPRESA + " TEXT,"
                    + COLUMN_ID_USUARIO  + " TEXT,"
                    +  COLUMN_STATUS + " INTEGET,"
                    + COLUMN_USUARIO  + " TEXT,"
                    + COLUMN_CLAVE + " TEXT,"
                    + COLUMN_COLUMN_TIMESTAMP + " DATE DEFAULT (datetime('now','localtime')),"
                    + COLUMN_F_ULT_UPDATE + " INTEGER, "
                    + COLUMN_TPRINTER + " INTEGER "
                    +  ")";


//crear instancia de Clase
    public ConfModel() {
    }


    public ConfModel(int id, String idempresa, String nempresa, String sloempresa,
                     String idusuario, int status, String usuario, String clave,
                     String timestamp, int fUltUpdate, int tprinter) {
        this.id = id;
        this.idempresa = idempresa;
        this.nempresa = nempresa;
        this.sloempresa = sloempresa;
        this.idusuario = idusuario;
        this.status = status;
        this.usuario = usuario;
        this.clave = clave;
        this.timestamp = timestamp;
        this.fUltUpdate = fUltUpdate;
        this.tprinter=tprinter;

    }

    public int getId() {
        return id;
    }

    public String getIdempresa() {
        return idempresa;
    }

    public String getNempresa() {
        return nempresa;
    }

    public String getSloempresa() {
        return sloempresa;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public int getStatus() {
        return status;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getClave() {
        return clave;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getfUltUpdate() {
        return fUltUpdate;
    }

    public int getTprinter() {
        return tprinter;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdempresa(String idempresa) {
        this.idempresa = idempresa;
    }

    public void setNempresa(String nempresa) {
        this.nempresa = nempresa;
    }

    public void setSloempresa(String sloempresa) {
        this.sloempresa = sloempresa;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setfUltUpdate(int fUltUpdate) {
        this.fUltUpdate = fUltUpdate;
    }


    public void setTprinter(int tprinter) {
        this.tprinter = tprinter;
    }



    //Iniciar un registro en configuracion
    public static String Initconf(Context context) {


        final Date date = new Date();
        DBHelper dbOpenHelper = new DBHelper(context); //DbOpenHelper(context,"DbSoftfinc.db",1);
        try {
            dbOpenHelper.openDatabase( SQLiteDatabase.OPEN_READWRITE| SQLiteDatabase.OPEN_READWRITE);
        } catch (IOException e) {
            // TODO: Handle
        } catch (SQLiteException e) {
            // TODO: Handle
        }

        ContentValues values = new ContentValues();

        values.put(ConfModel.COLUMN_N_EMPRESA, "Empresa");
        values.put(ConfModel.COLUMN_ID_EMPRESA, "1233333");
        values.put(ConfModel.COLUMN_F_ULT_UPDATE, Utiles.DateToDays(date));

        dbOpenHelper.db.insert(ConfModel.TABLE_NAME,null,values);

        dbOpenHelper.close();


        return "";
    }

    public static boolean Actualizar2(Context context,List<ConfModel> Modelo){

      //  final Date date = new Date();

        DBHelper dbOpenHelper = new DBHelper(context); //DbOpenHelper(context,"DbSoftfinc.db",1);

        try {
            dbOpenHelper.openDatabase( SQLiteDatabase.OPEN_READWRITE| SQLiteDatabase.OPEN_READWRITE);
        } catch (IOException e) {
            // TODO: Handle
        } catch (SQLiteException e) {
            // TODO: Handle
        }




        ContentValues values = new ContentValues();
        values.put(ConfModel.COLUMN_N_EMPRESA, "Empresa");
        values.put(ConfModel.COLUMN_ID_EMPRESA, "55555");
       // values.put(ConfModel.COLUMN_F_ULT_UPDATE, Utiles.DateToDays(date));

        String whereClause = ConfModel.COLUMN_COLUMN_ID +  "=? AND " + ConfModel.COLUMN_ID_EMPRESA + "=?";

        String whereArgs[] = {"1","44444"};
        dbOpenHelper.db.update(ConfModel.TABLE_NAME,  values, whereClause, whereArgs);
        dbOpenHelper.close();

return true;

    }



    public static String insertConfi(Context context,String note,int hora) {


        final Date date = new Date();

        DBHelper dbOpenHelper = new DBHelper(context); //DbOpenHelper(context,"DbSoftfinc.db",1);

        try {
            dbOpenHelper.openDatabase( SQLiteDatabase.OPEN_READWRITE| SQLiteDatabase.OPEN_READWRITE);
        } catch (IOException e) {
            // TODO: Handle
        } catch (SQLiteException e) {
            // TODO: Handle
        }

        //Log.e("hora",  String.valueOf(Utiles.DateToDays(date))) ;
        //String s = Utiles.Formtfecha.format(date);
        //int output = Integer.valueOf(s);

        //int i = (int) (new Date().getTime()/1000);
        // Log.e("ss",f );
        // Date s = Utiles.DaysToDate(Utiles.DateToDays(date));
        // Log.e("fecha",  Utiles.Formtfecha.format(s)) ;
        //asi ingreso datos a tablas
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(ConfModel.COLUMN_N_EMPRESA, "Empresa");
        values.put(ConfModel.COLUMN_ID_EMPRESA, "1233333");
        values.put(ConfModel.COLUMN_F_ULT_UPDATE, Utiles.DateToDays(date));

        dbOpenHelper.db.insert(ConfModel.TABLE_NAME,null,values);
/*
         //asi hago un query y recorro los datos
        String mQuery = "SELECT * FROM notes";
        Cursor mCur = dbOpenHelper.db.rawQuery(mQuery, new String[]{});
        mCur.moveToFirst();
        while ( !mCur.isAfterLast()) {
            String id= mCur.getString(mCur.getColumnIndex(Note.COLUMN_ID));
            String nota= mCur.getString(mCur.getColumnIndex(Note.COLUMN_NOTE));
            mCur.moveToNext();
            Log.e("Datos: ",id +" --> " + nota);

        }
*/
        dbOpenHelper.close();


        return "";
    }

    public static String Actualizar(Context context, List<ConfModel> Modelo) {
        final Date date = new Date();

        DBHelper dbOpenHelper = new DBHelper(context); //DbOpenHelper(context,"DbSoftfinc.db",1);

        try {
            dbOpenHelper.openDatabase( SQLiteDatabase.OPEN_READWRITE| SQLiteDatabase.OPEN_READWRITE);
        } catch (IOException e) {
            // TODO: Handle
        } catch (SQLiteException e) {
            // TODO: Handle
        }

        String whereClause = ConfModel.COLUMN_COLUMN_ID +  "=?";
        String whereArgs[] = {"1"};

        for (ConfModel item: Modelo) {

            ContentValues values = new ContentValues();
            values.put(COLUMN_ID_EMPRESA, item.getIdempresa());
            values.put(COLUMN_N_EMPRESA,item.getNempresa());
            values.put(COLUMN_SlO_EMPRESA,item.getSloempresa());
            values.put(COLUMN_ID_USUARIO,item.getIdusuario());
            values.put(COLUMN_STATUS, item.getStatus());
            values.put(COLUMN_USUARIO,item.getUsuario());
            values.put(COLUMN_CLAVE,item.getClave());
            values.put(COLUMN_F_ULT_UPDATE,item.getfUltUpdate());
            values.put(COLUMN_TPRINTER,item.getTprinter());

            dbOpenHelper.db.update(ConfModel.TABLE_NAME,  values, whereClause, whereArgs);
        }



        // String whereClause = ConfModel.COLUMN_COLUMN_ID +  "=? AND " + ConfModel.COLUMN_ID_EMPRESA + "=?";
        //
        //  String whereArgs[] = {"1","4444"};

        dbOpenHelper.close();

        return "";
    }

    public static  List<ConfModel> Consultaarray(Context context, String consulta) {


            List<ConfModel> Arrayclase = new ArrayList<>();

            DBHelper dbOpenHelper = new DBHelper(context); //DbOpenHelper(context,"DbSoftfinc.db",1);

            try {
                dbOpenHelper.openDatabase( SQLiteDatabase.OPEN_READWRITE| SQLiteDatabase.OPEN_READWRITE);
            } catch (IOException e) {
                // TODO: Handle
            } catch (SQLiteException e) {
                // TODO: Handle
            }
            //asi hago un query y recorro los datos
            String mQuery = "SELECT * FROM " + ConfModel.TABLE_NAME +" where " + " Id= " + consulta  ;
            Cursor mCur = dbOpenHelper.db.rawQuery(mQuery, new String[]{});
            mCur.moveToFirst();

            while ( !mCur.isAfterLast()) {

                ConfModel clase = new ConfModel();
                int  id = mCur.getInt(mCur.getColumnIndex(ConfModel.COLUMN_COLUMN_ID));
                String idempresa = mCur.getString(mCur.getColumnIndex(ConfModel.COLUMN_ID_EMPRESA));
                String  nempresa = mCur.getString(mCur.getColumnIndex(ConfModel.COLUMN_N_EMPRESA));
                String  slogan = mCur.getString(mCur.getColumnIndex(ConfModel.COLUMN_SlO_EMPRESA ));
                String  idusuario = mCur.getString(mCur.getColumnIndex(ConfModel.COLUMN_ID_USUARIO));
                int  status = mCur.getInt(mCur.getColumnIndex(ConfModel.COLUMN_STATUS));
                String  usuario = mCur.getString(mCur.getColumnIndex(ConfModel.COLUMN_USUARIO));
                String  clave = mCur.getString(mCur.getColumnIndex(ConfModel.COLUMN_CLAVE));
                String  tmspam = mCur.getString(mCur.getColumnIndex(ConfModel.COLUMN_COLUMN_TIMESTAMP));
                int fupdate = mCur.getInt(mCur.getColumnIndex(ConfModel.COLUMN_F_ULT_UPDATE));
                int tprinter= mCur.getInt(mCur.getColumnIndex(ConfModel.COLUMN_TPRINTER));

                clase.setId(id);
                clase.setIdempresa(idempresa);
                clase.setNempresa(nempresa);
                clase.setSloempresa(slogan);
                clase.setIdusuario(idusuario);
                clase.setStatus(status);
                clase.setUsuario(usuario);
                clase.setClave(clave);
                clase.setTimestamp(tmspam);
                clase.setfUltUpdate(fupdate);
                clase.setTprinter(tprinter);


                mCur.moveToNext();
                //  Log.e("Datos: ",id +" --> " + nota);
                Arrayclase.add(clase);
            }

            dbOpenHelper.close();

            return Arrayclase;
        }


    public static  List<ConfModel> Obtenerconf(Context context){
        List<ConfModel> Arrayclase = new ArrayList<>();

        DBHelper dbOpenHelper = new DBHelper(context); //DbOpenHelper(context,"DbSoftfinc.db",1);

        try {
            dbOpenHelper.openDatabase( SQLiteDatabase.OPEN_READWRITE| SQLiteDatabase.OPEN_READWRITE);
        } catch (IOException e) {
            // TODO: Handle
        } catch (SQLiteException e) {
            // TODO: Handle
        }
        //asi hago un query y recorro los datos
        String mQuery = "SELECT * FROM " + ConfModel.TABLE_NAME ;
        Cursor mCur = dbOpenHelper.db.rawQuery(mQuery, new String[]{});
        mCur.moveToFirst();

        while ( !mCur.isAfterLast()) {
            ConfModel clase = new ConfModel();

            int  id = mCur.getInt(mCur.getColumnIndex(ConfModel.COLUMN_COLUMN_ID));
            String idempresa = mCur.getString(mCur.getColumnIndex(ConfModel.COLUMN_ID_EMPRESA));
            String  nempresa = mCur.getString(mCur.getColumnIndex(ConfModel.COLUMN_N_EMPRESA));
            String  slogan = mCur.getString(mCur.getColumnIndex(ConfModel.COLUMN_SlO_EMPRESA ));
            String  idusuario = mCur.getString(mCur.getColumnIndex(ConfModel.COLUMN_ID_USUARIO));
            int  status = mCur.getInt(mCur.getColumnIndex(ConfModel.COLUMN_STATUS));
            String  usuario = mCur.getString(mCur.getColumnIndex(ConfModel.COLUMN_USUARIO));
            String  clave = mCur.getString(mCur.getColumnIndex(ConfModel.COLUMN_CLAVE));
            String  tmspam = mCur.getString(mCur.getColumnIndex(ConfModel.COLUMN_COLUMN_TIMESTAMP));
            int fupdate = mCur.getInt(mCur.getColumnIndex(ConfModel.COLUMN_F_ULT_UPDATE));
            int ftprinter = mCur.getInt(mCur.getColumnIndex(ConfModel.COLUMN_TPRINTER));


            clase.setId(id);
            clase.setIdempresa(idempresa);
            clase.setNempresa(nempresa);
            clase.setSloempresa(slogan);
            clase.setIdusuario(idusuario);
            clase.setStatus(status);
            clase.setUsuario(usuario);
            clase.setClave(clave);
            clase.setTimestamp(tmspam);
            clase.setfUltUpdate(fupdate);
            clase.setTprinter(ftprinter);

            mCur.moveToNext();
          //  Log.e("Datos: ",id +" --> " + nota);
            Arrayclase.add(clase);
        }

        dbOpenHelper.close();


        return Arrayclase;


    }






}
