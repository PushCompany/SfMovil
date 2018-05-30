package com.softfinc.softfinc;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Login_Activity extends AppCompatActivity {
    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;

    TextView txtnempresa, txtusuario, txtclave;

    Button btnentrar_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //No title bar is set for the activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Full screen is set for the Window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

       setContentView(R.layout.activity_login);

btnentrar_login =(Button)findViewById(R.id.btnentrar_login);

       txtusuario= (TextView)findViewById( R.id.txtusuario_login);
       txtclave= (TextView)findViewById(R.id.txtpasword_login);


         txtusuario.setText(Globales.Usuario);
         //apago el txt usuario
        txtusuario.setEnabled(false);
        txtusuario.setFocusable(false);

        btnentrar_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Entrar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        // txtnempresa= (TextView)findViewById(R.id.txtnempresa);
      //  txtusuario= (TextView)findViewById(R.id.txtusuario);

      //  txtnempresa.setText(Globales.Nempresa);
      //  txtusuario.setText(String.valueOf(Globales.Tprinter));
/*

        final Button button = findViewById(R.id.btn1);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

               // startActivity(new Intent(Login_Activity.this, Principal.class));
              //  overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);

              //  startActivity(new Intent(Login_Activity.this, Principal.class));
              //  overridePendingTransition(R.anim.left_in,R.anim.left_out);

              // startActivity(new Intent(MainActivity.this, segunda.class));
              //  overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                //abrir un intent pasando parametros al sisguiente
           //    Intent i = new Intent(Login_Activity.this    , Principal.class);
            //   i.putExtra("Value1", "valor1 ");
            //   i.putExtra("Value2", "valor2");
            //   startActivity(i);
             //  overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                //abrir el menu pricipal
           // Intent launch = new Intent(Login_Activity.this,Principal.class);
            //startActivity(launch);
        }
        });
        */
    }



    private void Entrar() throws Exception {
        String usuario =txtusuario.getText().toString();
        String clave = txtclave.getText().toString();

        if (usuario.equals("")){



            Toast.makeText(getApplicationContext(),"Ingrese un Usuario..!", Toast.LENGTH_SHORT).show();
        }else{

            if(clave.equals("")){
                Toast.makeText(getApplicationContext(),"Ingrese una clave..!", Toast.LENGTH_SHORT).show();

            }else{
//aqui ejecuto si todo esta bien
                validarusuario(usuario,clave);
            }
        }


    }



    private void validarusuario(String usuario,String clave) throws Exception {

      if(Validauser(usuario,clave)){

         // Log.e("paso","bien");


          //abrir un intent pasando parametros al sisguiente
              Intent i = new Intent(Login_Activity.this    , Principal.class);
           i.putExtra("Value1", "valor1 ");
           i.putExtra("Value2", "valor2");
          startActivity(i);
         overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
finish();


      }else{

         // Log.e("paso","mal");
          Toast.makeText(getApplicationContext(),"Datos Incorrectos..!", Toast.LENGTH_SHORT).show();


      }

    }


    //Validar usuario y clave en Db
    public  boolean Validauser(String pusuario,String pclave) throws Exception {

        //encriptar la clave para compararla con la db encriptada
        pclave=Utiles.Encrypt(pclave,Globales.Keyinterno).trim();
               boolean validar=false;
        DBHelper dbOpenHelper = new DBHelper(Login_Activity.this); //DbOpenHelper(context,"DbSoftfinc.db",1);

        try {
            dbOpenHelper.openDatabase( SQLiteDatabase.OPEN_READWRITE| SQLiteDatabase.OPEN_READWRITE);
        } catch (IOException e) {
            // TODO: Handle
        } catch (SQLiteException e) {
            // TODO: Handle
        }

        Cursor mCur = dbOpenHelper.db.rawQuery("SELECT * FROM "+ ConfModel.TABLE_NAME+" WHERE " +
                "usuario=? and clave = ?", new String[] {pusuario,pclave});

        mCur.moveToFirst();

        while ( !mCur.isAfterLast()) {
          mCur.moveToNext();
          validar=true;
        }
        dbOpenHelper.close();

        return validar;
    }






    @Override
    public void onBackPressed(){
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
           super.onBackPressed();

            Intent intent = new Intent(getApplicationContext(), Splash_Activity.class);
            //startActivity(launch);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            //abrir el form del login
            startActivity(intent);
            overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);
            finish();
          return;
        }else {
              Toast.makeText(this, "Vuelve a presionar para salir", Toast.LENGTH_SHORT).show();
        }
        tiempoPrimerClick = System.currentTimeMillis();


    }
}
