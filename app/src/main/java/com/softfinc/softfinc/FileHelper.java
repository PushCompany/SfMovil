package com.softfinc.softfinc;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class FileHelper {

    public  Context  mcontext;

    static String fileName=""; ;//"ejemplo.txt";
    static String path ;

       final static String TAG = FileHelper.class.getName();


    public FileHelper (Context context,String nombrearchivo) throws PackageManager.NameNotFoundException {
        mcontext = context;
        path =  Utiles.getDataDir(mcontext)+"/conf/";
       fileName =nombrearchivo;
    }

    public static String ReadFile(){
        String line = null;

        try {
            FileInputStream fileInputStream = new FileInputStream (new File(path + fileName));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ( (line = bufferedReader.readLine()) != null )
            {
               // stringBuilder.append(line + System.getProperty("line.separator"));
               stringBuilder.append(line);
            }
            fileInputStream.close();
            line = stringBuilder.toString();

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        }
        catch(IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return line;
    }



    public static boolean Leer( String data){

        try {
            new File(path  ).mkdir();
            File file = new File(path+ fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(file,true);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
            //fileOutputStream.write((data + System.getProperty("line.separator")).getBytes());

            return true;
        }  catch(FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        }  catch(IOException ex) {
            Log.d(TAG, ex.getMessage());
        }


        return  false;


    }


    public static boolean Escribir( String data){
        try {
            new File(path  ).mkdir();
            File file = new File(path+ fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(file,true);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
            //fileOutputStream.write((data + System.getProperty("line.separator")).getBytes());

            return true;
        }  catch(FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        }  catch(IOException ex) {
            Log.d(TAG, ex.getMessage());
        }


        return  false;


    }

    public static boolean Eliminar(){

        boolean deleted;
        new File(path  ).mkdir();
        File file = new File(path+ fileName);
        if (file.exists()) {
           deleted = file.delete();

        }else{

            deleted=false;
        }


        return deleted;

    }

    public static String Borrarlinea(String lineToRemove){

        try {

            File inFile = new File(path+ fileName);
            if (!inFile.isFile()) {
                Log.e("error","no hay file");
                return "";
            }

            //Construct the new file that will later be renamed to the original filename.
            File tempFile = new File(path + ".tmp");

            BufferedReader br = new BufferedReader(new FileReader(inFile));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String line = null;


            //Read from the original file and write to the new
            //unless content matches data to be removed.
            while ((line = br.readLine()) != null) {
              // Log.e("linea",line);
               // if (!line.trim().equals(lineToRemove)) {
                  pw.println("");
                  pw.flush();
               // }

            }

            pw.close();
            br.close();

            //Delete the original file
            if (!inFile.delete()) {
                System.out.println("Could not delete file");
                return "";
            }

            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(inFile)){
                System.out.println("Could not rename file");
            }

        } catch (FileNotFoundException ex) {

            ex.printStackTrace();

        } catch (IOException ex) {

            ex.printStackTrace();

        }


        return "";
    }

}