package com.softfinc.softfinc;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.softfinc.softfinc.FileHelper.fileName;

public class Update_Activity extends AppCompatActivity {

    ProgressDialog bar;
    private static String TAG = "Update_activity";
    private int AppVersion = 1;
    private static final int BUFFER_SIZE = 4096;

    //Los valores no deben repetirse
    private static final int READ_STORAGE_MEMORIA = 110;


    //datos actualizacion
    public static String Version="";
    public static String Urlapp="";
    public static String Nota="";
    public static String Appidsecret="";

    public static TextView heading;
    public static Activity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_);

        activity = this;

          heading = (TextView) findViewById(R.id.heading);
        Button   update_btn = (Button) findViewById(R.id.btn);

        heading.setText("Cargando ....");



        Verificaracceso_storage();



        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                File path =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS); //getApplicationInfo().dataDir + "/Download/";
                new DownloadNewVersion().execute(path.toString());

            }
        });

    }



    private void Ejecutarhttpost(){


        //Preparo verificar el movil Y CARGAR BD LOCAL
        try {

            //Globales.jsonParam=null;//limpio el jsonParam para que siga sin datos
            // Globales.jsonParam=new JSONObject();
            //   Globales.jsonParam.put("Idempresa",Globales.IMEI);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("Idempresa", Utiles.Encrypt(Globales.Idempresa,Globales.Keyinterno));
            new Httppost(Update_Activity.this,"VerificarActualizacion")
                    .execute("0","cuentas","RevisarActualizar",jsonParam.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //######

    }



   //recojer los datos cuando termina el httppost al servidor
    public void Datosactualizar(Context context, String result1) throws JSONException {


        //si  la  respuesta no esta vacia
                 if(result1!=""){

                   //  Log.e("data : ",result1);
                  //   JSONObject obj = new JSONObject(result1);
                   //  JSONArray arr = obj.getJSONArray("data");
                     //{"items":[{"name":"command","index":"X","optional":"0"},{"name":"command","index":"X","optional":"0"}]}

                    JSONArray jsonarray = new JSONArray("[" + result1 + "]");//solo[array]
                     for(int i=0; i <  jsonarray.length(); i++) {
                         JSONObject jsonobject =  jsonarray.getJSONObject(i);
                         Version= jsonobject.getString("version").trim();
                         Nota= jsonobject.getString("nota").trim();
                         Urlapp= jsonobject.getString("urlapp").trim();
                         Appidsecret= jsonobject.getString("appidupdate").trim();

                        // String nombre   = jsonobject.getString("nombre");
                         // String edad   = jsonobject.getString("edad");
                     }

                     heading.setText("Version Nueva: " + Version);

                   //  Log.e("version",Version);


                 }else
                     {


                         Intent intent = new Intent(context, MainActivity.class);
                         //startActivity(launch);
                         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                         // intent.putExtra("EXIT", true);
                         //abrir el form del login
                        context.startActivity(intent);
                       //  overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);
                         activity.finish();



                 }




    }





    class DownloadNewVersion extends AsyncTask<String,Integer,Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            bar = new ProgressDialog(Update_Activity.this);
            bar.setCancelable(false);

            bar.setMessage("Downloading...");

            bar.setIndeterminate(true);
            bar.setCanceledOnTouchOutside(false);
            bar.show();

        }

        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

            bar.setIndeterminate(false);
            bar.setMax(100);
            bar.setProgress(progress[0]);
            String msg = "";
            if(progress[0]>99){

                msg="Finishing... ";

            }else {

                msg="Downloading... "+progress[0]+"%";
            }
            bar.setMessage(msg);

        }
        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            bar.dismiss();

            if(result){

                Toast.makeText(getApplicationContext(),"Archivo ,Completado. ",
                        Toast.LENGTH_SHORT).show();

            }else{


                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(launch);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               // intent.putExtra("EXIT", true);
                //abrir el form del login
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);
finish();

              //  Toast.makeText(getApplicationContext(),"Error: Try Again",
                   //     Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            Boolean flag = false;


            try {

              //  URL url = new URL("http://10.0.0.100:57130/api/descargas/Download/23");


              //  HttpURLConnection con = (HttpURLConnection) ( new URL("http://10.0.0.100:57130/api/descargas/Download/23")).openConnection();

               // URL url = new URL("http://10.0.0.100:57130/api/descargas/Download/23");

                //URL url = new URL("http://10.0.0.100:57130/api/descargas/app/23");

                URL url = new URL(Urlapp + Appidsecret);

                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                int responseCode = httpConn.getResponseCode();

              //  Log.e("sd",arg0[0]);
                String PATH =  arg0[0];
                // Environment.getExternalStorageDirectory()+"/Download/";
                File file = new File(PATH);
                file.mkdirs();

                File outputFile = new File(file,"SoftFinc.apk");

                if(outputFile.exists()){
                    outputFile.delete();
                }


                // opens input stream from the HTTP connection
                InputStream inputStream = httpConn.getInputStream();
                String saveFilePath = PATH +"SoftFinc.apk";


               // Log.e("ruta",saveFilePath);


                // opens an output stream to save into file
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                int bytesRead = -1;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();



                //  HttpURLConnection c = (HttpURLConnection) url.openConnection();
                //c.setRequestMethod("GET");
               // c.setDoOutput(true);
               // c.connect();


                //reading HTTP response code
              //  int responseCode = httpConn.getResponseCode();
               // BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));

              //  Log.e("ssd",);

/*
                Log.e("sd",arg0[0]);
                String PATH =  arg0[0];
                // Environment.getExternalStorageDirectory()+"/Download/";
                File file = new File(PATH);
                file.mkdirs();

                File outputFile = new File(file,"SoftFinc.apk");

                if(outputFile.exists()){
                    outputFile.delete();
                }

                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = responseCode.getInputStream();

              int total_size = 1431692;//size of apk
                byte[] buffer = new byte[1024];
                int len1 = 0;
                int per = 0;
                int downloaded=0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                    downloaded +=len1;
                    per = (int) (downloaded * 100 / total_size);
                    publishProgress(per);
                }

                fos.close();
                is.close();
*/

             OpenNewVersion(PATH);

                flag = true;
            } catch (Exception e) {
                Log.e(TAG, "Update http: error : " + e.getMessage());
                flag = false;

            }


            return flag;

        }

    }

    void OpenNewVersion(String location) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(location + "SoftFinc.apk")),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);

         //Verificar si el archivo existe
        File file = new File(location);
             File outputFile = new File(file,"");
        if(outputFile.exists()){
           // Log.e("si ","existe");
           // outputFile.delete();
        }

    }

    //VERIRIFICAR SI TENGO PERMISOS DE AGENDA
    private void Verificaracceso_storage(){
        //si la API 23 a mas
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Habilitar permisos para la version de API 23 a mas
            int verificarPermisoReadContacts = ContextCompat
                    .checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //Verificamos si el permiso no existe
            if(verificarPermisoReadContacts != PackageManager.PERMISSION_GRANTED){
                //verifico si el usuario a rechazado el permiso anteriormente
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    //Si a rechazado el permiso anteriormente muestro un mensaje
                    MostrarMensaje2();
                }else{
                    //De lo contrario carga la ventana para autorizar el permiso
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_STORAGE_MEMORIA);
                }
            }else{
                //Si el permiso ya fue concedido abrimos en intent de contactos
                Ejecutarhttpost();
                //Loadprogreess();//ejecuto cargar el status bard
            }

        }else{//Si la API es menor a 23 - abrimos en intent de contactos

            Ejecutarhttpost();
            // Loadprogreess();;//ejecuto cargar el status bard


        }
    }
    private void MostrarMensaje2() {
        new AlertDialog.Builder(this)
                .setTitle("Autorización")
                .setMessage("Permiso a Memoria Interna. .")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_STORAGE_MEMORIA);
                        }

                        // Loadprogreess();//ejecuto cargar el status bard
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        //Mensaje acción cancelada
                        Msgboxcancel2();
                        //  Loadprogreess();
                    }
                })
                .show();
    }
    public void Msgboxcancel2(){
        Toast.makeText(getApplicationContext(),
                "Haz rechazado la petición, por favor considere en aceptarla.",
                Toast.LENGTH_SHORT).show();
    }
    //##################3
}
