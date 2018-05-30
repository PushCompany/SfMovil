package com.softfinc.softfinc;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Utiles extends Application {


     public static  DateFormat Formtfecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
     public static  DateFormat Formthora = new SimpleDateFormat("HH:mm:ss aa");
     public static  DateFormat Formtfechas = new SimpleDateFormat("dd/MM/yyyy");

    //convertir Date en int y Viseversa

    public static int DateToDays (Date date){

        int dias = (int) (date.getTime()/1000);

        return dias;
       //return (int) (date.getTime()/MAGIC);
    }
    public static Date DaysToDate(int days) {

        Date fecha =(new Date(((long)days)*1000L)) ;

        return fecha;

       // return new Date((long) days*MAGIC);
    }
    //######

    //#### crear id unico en app
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    public synchronized static String Uidapp(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }
    //#########


    @SuppressLint("MissingPermission")
    public static String ObtenerImei(Context context) {

        String deviceUniqueIdentifier = null;
        try{

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != tm) {
                deviceUniqueIdentifier = tm.getDeviceId();
            }
            if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
                deviceUniqueIdentifier = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }

        catch (Exception e)
        {
            deviceUniqueIdentifier="00";
        }

        finally{
            //  System.out.println("Instrucciones a ejecutar finalmente tanto si se producen errores como si no.");
            return  deviceUniqueIdentifier;
        }




    }


    public static String getDataDir(final Context context) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.dataDir;
    }

    //crear archivo txt en la carpeta conf
    public static void Creartxt(Context context, String sFileName, String sBody) throws PackageManager.NameNotFoundException {

        try {
            File root = new File(Utiles.getDataDir(context), "conf");
            if (!root.exists()) {
                root.mkdirs();
            }

            File gpxfile = new File(root, sFileName);
            if (!gpxfile.exists()) {
               FileWriter writer = new FileWriter(gpxfile);
                writer.append(sBody);
                writer.flush();
               writer.close();
            }else{


            }



        } catch (IOException e) {
            e.printStackTrace();
        }


    }



 //desencriptar en Java
    static String Decrypt(String text, String key) throws Exception{

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] keyBytes= new byte[16];
            byte[] b= key.getBytes("UTF-8");
            int len= b.length;
            if (len > keyBytes.length) len = keyBytes.length;
            System.arraycopy(b, 0, keyBytes, 0, len);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
            cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);

        Base64 decode =null;
        byte [] results = cipher.doFinal(decode.decode(text, Base64.DEFAULT));
        return new String(results,"UTF-8");

    }

    //Encriptar en java
    public static String Encrypt(String text, String key)
            throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes= new byte[16];
        byte[] b= key.getBytes("UTF-8");
        int len= b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);


        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
        Base64 decode =null;
        byte[] encodeValue = Base64.encode(results, Base64.DEFAULT);

          return new String(encodeValue);
    }



    //Convertir string a date
public static Date StringtoDate(String fecha) throws ParseException {

    Date date = Utiles.Formtfecha.parse(fecha);


    return date ;
}



//funcion que permitira actualizar si hay actualizacion
public static boolean actualizar(boolean valor){


        return valor;
}



}
