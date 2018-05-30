package com.softfinc.softfinc;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    //Los valores no deben repetirse
    private static final int READ_PHONE_STATE = 100;

    //...para multiples permisos en ejecucion
    private static final int MULTIPLE_PERMISSIONS_REQUEST_CODE = 3;
    private String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //...

  //  static Handler handler = new Handler();


   private TextView txtsucceso;
    private DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



         txtsucceso = (TextView)findViewById(R.id.txtsucceso_maint);

        //recojo el imei en una variable publica
        Globales.IMEI= Utiles.ObtenerImei(MainActivity.this);
        Globales.Uidapp=Utiles.Uidapp(MainActivity.this);
        txtsucceso.setText("...Error IMEI Por Falta de Permisos ...");



        //Multiples permisos en android
        /*
        //Verifica si los permisos establecidos se encuentran concedidos
        if (ActivityCompat.checkSelfPermission(MainActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(MainActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
            //Si alguno de los permisos no esta concedido lo solicita
            ActivityCompat.requestPermissions(MainActivity.this, permissions, MULTIPLE_PERMISSIONS_REQUEST_CODE);
           Log.e("error","Aqui");

        } else {
            //Si todos los permisos estan concedidos prosigue con el flujo normal
             //permissionGranted();
        }
        */

        //Log.e("paso","2");

        Button btnreset = (Button) findViewById(R.id.btnresetmaint);
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


        //   String id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        //verificar si hay salir en intent
        if (getIntent().getBooleanExtra("EXIT", false)) {

            Log.e("salir","ok");
            finish();

        } else
            {//########todo aqui es si no hay variable EXIT PARA SALIR DE LA APP

                //verifico el acceso a l movil para el imei
                Verificaracceso_read_phone_state();

                //PREPARO LA DBLOCAL
                PrepararDb();

             // Revisarconfig();
            }
        }

    //VERIRIFICAR SI TENGO PERMISOS DE AGENDA
    private void Verificaracceso_read_phone_state(){
        //si la API 23 a mas
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Habilitar permisos para la version de API 23 a mas
            int verificarPermisoReadContacts = ContextCompat
                    .checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            //Verificamos si el permiso no existe
            if(verificarPermisoReadContacts != PackageManager.PERMISSION_GRANTED){
                //verifico si el usuario a rechazado el permiso anteriormente
                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)){
                    //Si a rechazado el permiso anteriormente muestro un mensaje
                    Msgokreadphone();
                }else{
                    //De lo contrario carga la ventana para autorizar el permiso
                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
                }
            }else{

                //Si el permiso ya fue concedido abrimos en intent de contactos
                Revisarconfig();
                //Loadprogreess();//ejecuto cargar el status bard
            }

        }else{//Si la API es menor a 23 - abrimos en intent de contactos

            Revisarconfig();
            // Loadprogreess();;//ejecuto cargar el status bard


        }
    }
    private void Msgokreadphone() {
        new AlertDialog.Builder(this)
                .setTitle("Autorización")
                .setMessage("La App Necesita, permiso para acceder al IMEI del movil .")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
                        }

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Mensaje acción cancelada
                       Msgcancelreadphone();
                        //  Loadprogreess();
                    }
                })
                .show();
    }
    public void Msgcancelreadphone(){
        Toast.makeText(getApplicationContext(),
                "Haz rechazado la petición, por favor considere en aceptarla.",
                Toast.LENGTH_SHORT).show();


      finish();
    }
    //##################3


    private void Revisarconfig(){

        //Verifico si el movil tiene un servidor configurado en el host.txt
        //o si tienen algun error de IMEI
        if(Verificar_movil()==true){
            //ejecuto todos los precesos 1 a 1
            Iniciar();
        }
        else{



        }

    }


    //iniciar la funciones
private void Iniciar() {

    //abrir el al login
    Intent intent = new Intent(getApplicationContext(), Splash_Activity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    //intent.putExtra("EXIT", true);
    startActivity(intent);
    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    finish();


    // Toast.makeText(getApplicationContext(),
         //   "aceptado",
          //  Toast.LENGTH_SHORT).show();

}



    private boolean Verificar_movil(){
        boolean result=false;
        //######verificar si tengo el host apuntando a algun lado
        //creo archivo de configuracion de host
        String host ="";
        try {
            Utiles.Creartxt(this, "host.txt", "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        FileHelper file = null;
        try {
            file= new FileHelper(MainActivity.this,"host.txt");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



        //file.Borrarlinea( "");
        //recojo el valor de host

        host=file.ReadFile();
        if(host==""){

                    //si el host esta vacio  y el imei tambien
            if(Globales.IMEI.length()<=4){
                txtsucceso.setText("...Error IMEI Por Falta de Permisos ...");

                return false;
            }else{

                //si es solo el host que esta vacio
                //si el nombre de hosat no existe en el host.txt
                Intent intent = new Intent(getApplicationContext(), RegHost_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               intent.putExtra("EXIT", true);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                txtsucceso.setText("");

                return false;
            }






        }else//si hay un nombre de host registrado en host.txt
        {        //si el imei esta dudoso
            if(Globales.IMEI.length()<=4){
                txtsucceso.setText("...Error IMEI Por Falta de Permisos ...");

                return false;
            }else{
                //recojo la url del servidor del host.txt
                Globales.Urlserver=host;
                txtsucceso.setText("");

                return true;
            }
        }
    }



    private void PrepararDb(){
        //crear una instancia de DB
        db = new DBHelper(MainActivity.this);
        db.CrearDb(); //enviando consulta de prueba para crear db o actualizar

    };


}



