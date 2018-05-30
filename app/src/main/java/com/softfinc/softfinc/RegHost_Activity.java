package com.softfinc.softfinc;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegHost_Activity extends AppCompatActivity {

    //tiempo para salir al pulsar dos veces##
    private static final int INTERVALO = 5000; //2 segundos para salir
    private long tiempoPrimerClick;

    EditText txthost;
    static   TextView txterror;
    Button btnguardar;

    Context currentContext;

    private static final Handler handler1 = new Handler();
    private static FileHelper file = null;


    //#########
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarhost);

        currentContext = this;

        txthost = (EditText) findViewById(R.id.txthost);
        txterror = (TextView) findViewById(R.id.txterror);
        btnguardar= (Button)findViewById(R.id.btngrabar);




        txterror.setVisibility(View.GONE);//esconder un Textview

        try {
            file= new FileHelper(this,"host.txt");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //recojo el valor de host
        String host;
        host=file.ReadFile();
        txthost.setText(host);//envio el host al txt


        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String host;
                FileHelper file = null;
                try {
                    file= new FileHelper(RegHost_Activity.this,"host.txt");
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                txterror.setVisibility(View.GONE);//esconder un Textview txterror

                //eliminar todas las lineas
                file.Borrarlinea("tfff");

                file.Escribir(txthost.getText().toString());

                //recojo el valor de host
                host=file.ReadFile();
              //  Log.e("Url",file.ReadFile());

                //Recojo la url del servidor del host.txt
                Globales.Urlserver=host;

                    new Httppost(RegHost_Activity.this, "verificarhost")
                            .execute("1000","cuentas", "chekhost","");

            }
        });


    }


    public void evento(Context context,String result1){


        //abrir el al login
        //recojo la respuesta del servidor para ver siel host esta bien
        if(result1.equals("1")){
            //reseteo la respuesta

            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //intent.putExtra("EXIT", true);
            try {
                context.startActivity(intent);
                finish();
               // overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);
            } catch (ActivityNotFoundException e) {
                // If there is nothing that can send a text/html MIME type
                e.printStackTrace();
            }


           // Log.e("termino ","bien ");

        }else
        {
            txterror.setText("Coneccion Fallida..!!");
            txterror.setVisibility(View.VISIBLE);//Mostrar el Txterror

            //eliminar todas las lineas
            file.Borrarlinea("tfff");

        }



    }

private void llam(){




}


    @Override
    public void onBackPressed(){
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){

            //abrir el al login
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);
            finish();

             super.onBackPressed();
            return;
        }else {
            Toast.makeText(this, "Vuelve a presionar para salir", Toast.LENGTH_SHORT).show();
        }
        tiempoPrimerClick = System.currentTimeMillis();
    }





}
