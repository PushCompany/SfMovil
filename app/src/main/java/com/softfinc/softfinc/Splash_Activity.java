package com.softfinc.softfinc;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.softfinc.softfinc.Globales.jsonParam;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class Splash_Activity extends AppCompatActivity {
    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;


    static  ProgressBar Progressbar;
    static TextView ShowText;
    static  TextView Succesosplash;
    static int progressBarValue = 0;
    TextView version;

    static Context mContext=null;


    TextView Version;

    public static Activity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //onCreate
        activity = this;
        //####poner el layaunt full screen
        //No title bar is set for the activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Full screen is set for the Window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //#####
        setContentView(R.layout.activity_splash);

        Progressbar = (ProgressBar)findViewById(R.id.progressBar);
        ShowText = (TextView)findViewById(R.id.textView1);
        Succesosplash=(TextView)findViewById(R.id.txtsucceso1);

        //recojo el id del txtversion
        version = (TextView)findViewById(R.id.txtversion);
        version.setText(String.valueOf("Vers:" +" "+ Globales.VersionCode));


        //   String id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        //verificar si hay salir en intent
        if (getIntent().getBooleanExtra("EXIT", false)) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            //startActivity(launch);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            //abrir el form del login
            startActivity(intent);
            overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);
            finish();


        } else{




            // ConfModel.Update(getApplication(),"23");
            // ConfModel.insertConfi(getApplication(),"",12);


         List<ConfModel> recojo = new ArrayList<>();
         recojo= ConfModel.Obtenerconf(Splash_Activity.this);
           for (ConfModel item: recojo) {

                Log.e("idempresa",item.getIdempresa() );
                Log.e("nempresa",item.getNempresa());
                Log.e("sloempresa",item.getSloempresa());
                Log.e("idusuario",item.getIdusuario() );
                Log.e("status", String.valueOf(item.getStatus())  );
                Log.e("usuario",item.getUsuario() );
                Log.e("clave",item.getClave() );
                Log.e("timestam",item.getTimestamp() );
                Log.e("fUltUpdate",String.valueOf(item.getfUltUpdate()) );
                Log.e("t_printer",String.valueOf(item.getTprinter()) );
            }

            Iniciar();

/*
            for(int i=0; i < recojo.size(); i++){

               String s =   Utiles.Formthora.format(Utiles.DaysToDate(recojo.get(i).getfUltUpdate()));

                Log.e("Lista:", );
            }
            */
            // Log.e("hora a int", String.valueOf(DateToDays(date)));
            // Log.e("int a hora ", String.valueOf(DaysToDate(DateToDays(date))));



        }//termino de ver si tengo intent exit


    }





    private  void Iniciar(){


        /*

        //Broadcast receiver for our Web Request
        IntentFilter filter = new IntentFilter(MyWebReceiver.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyWebReceiver();
        registerReceiver(receiver, filter);
        //Broadcast receiver for the download manager
        filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);


        Urlapp="http://10.0.0.100:57130/api/descargas/Download/2311";

        //oh yeah we do need an upgrade, let the user know send an alert message
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage("verdion")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    //if the user agrees to upgrade
                    public void onClick(DialogInterface dialog, int id) {
                        //start downloading the file using the download manager
                        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                        Uri Download_Uri = Uri.parse(Urlapp);
                        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
                        request.setAllowedOverRoaming(false);
                        request.setTitle("Descargando App..");
                        request.setDestinationInExternalFilesDir(MainActivity.this, Environment.DIRECTORY_DOWNLOADS,"SoftFinc.apk");
                        downloadReference = downloadManager.enqueue(request);
                    }
                })
                .setNegativeButton("Después", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        //show the alert message
        builder.create().show();

*/

        // Log.e("antes ",this.toString());

        // Toast.makeText(this, "antes", Toast.LENGTH_SHORT).show();


        //#####recojer la ip publicas
         // new Ubicacion_Http(Splash_Activity.this, "")
           //   .execute("0","", "");
        //#########


        //Preparo verificar el movil Y CARGAR BD LOCAL
        try {
           JSONObject jsonParam = new JSONObject();
            jsonParam.put("Idempresa", "envio Dato");
            jsonParam.put("imei",Globales.IMEI);
            jsonParam.put("Uidapp",Globales.Uidapp);
            jsonParam.put("Ultip",Globales.Ippublica);
            jsonParam.put("Version",Globales.VersionCode);

            new Httppost(Splash_Activity.this,"Verificarmovil")
                    .execute("0","cuentas","Verificarmovil",jsonParam.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //######


   }


    //cargar el progrees bar i ejecutar procesos luego de cargar la configuracion
    private static void Cargarprocesos(final Context context){
        mContext=context;

        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public  void run() {
                //  Looper.prepare();

                progressBarValue=0;
                // TODO Auto-generated method stub
                while(progressBarValue<100)
                {
                    progressBarValue++;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            Progressbar.setProgress(progressBarValue);
                            ShowText.setText(progressBarValue +"/"+Progressbar.getMax());

                            switch (progressBarValue){
                                case 1:
                                    Succesosplash.setText("-- "+ Globales.Urlserver +" --");
                                    break;
                                case 40:
                                    Succesosplash.setText("Probando Coneccion..remota");
                                    break;
                                case 50:
                                    Succesosplash.setText(" Creando Archivos Temporales..");
                                    break;
                                case 55:
                                    Succesosplash.setText("Cargando Ip..");
                                    break;
                                case 70:
                                    //Succesosplash.setText("-- Sincronizando Servidor2 --");
                                    break;
                                case 80:
                                    // Succesosplash.setText("-- Sincronizando Servidor3333 --");
                                    break;
                                case 97:
                                    Succesosplash.setText("-- Abriendo Terminal --");
                                    break;
                                case 100:
                                    // Log.e("ss",String.valueOf( Utiles.Autorizacion) );
                                    //si esta autorizado
                                    if(Globales.Autorizacion==1){
                                        //verifico si hay version nueva

                                          //si la app esta de actulizacion
                                        if(Globales.Actualizarapp==1){
                                            //si esta bloqueado
                                            Intent launch = new Intent(mContext,Update_Activity.class);
                                            mContext.startActivity(launch);
                                            activity.finish();
                                            Splash_Activity.activity.finish();

                                        }else{
                                            Intent launch = new Intent(mContext,Login_Activity.class);
                                            mContext.startActivity(launch);
                                            activity.finish();
                                            Splash_Activity.activity.finish();
                                        }






                                    }
                                    else{
                                        //si esta bloqueado
                                        Intent launch = new Intent(mContext,Bloqueo_Activity.class);
                                        mContext.startActivity(launch);
                                        Splash_Activity.activity.finish();
                                    }
                                    break;
                            }


                        }
                    });try {
                    sleep(40);
                    //mas tiempo mas lento
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
                }
            }
        }).start();

    }


    //#####abrir primer form
    public static void  RevisarAutoizacion(Context context, String result1){

        Cargarprocesos(context);


    }
    //#######




    @Override
    protected void onStart() {
        super.onStart();
        //    Toast.makeText(this, "OnStart", Toast.LENGTH_SHORT).show();
        // La actividad está a punto de hacerse visible.


    }
    @Override
    protected void onResume() {
        super.onResume();
        //  Toast.makeText(this, "OnResume", Toast.LENGTH_SHORT).show();
        // La actividad se ha vuelto visible (ahora se "reanuda").
    }

    @Override
    protected void onStop() {
        super.onStop();
        //  Toast.makeText(this, "OnStop", Toast.LENGTH_SHORT).show();
        // La actividad ya no es visible (ahora está "detenida")
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Toast.makeText(this, "OnDestroy", Toast.LENGTH_SHORT).show();
        // La actividad está a punto de ser destruida.
    }

    @Override
    protected void onPause() {
        super.onPause();
        //   Toast.makeText(this, "OnPause", Toast.LENGTH_SHORT).show();
        // Enfocarse en otra actividad  (esta actividad está a punto de ser "detenida").
    }


    @Override
    public void onBackPressed(){
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
           // super.onBackPressed();

            return;
        }else {
            Toast.makeText(this, " No Permitido En la Carga Espereee..", Toast.LENGTH_SHORT).show();
        }
        tiempoPrimerClick = System.currentTimeMillis();


    }

}
