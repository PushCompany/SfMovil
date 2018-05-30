package com.softfinc.softfinc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Httppost extends AsyncTask<String, String, String> {

    ProgressDialog pd;

    Context mcontext;
    String _controlador;
    String _metodo;
    int _thread=0;
    String _Jsonparam;

    String _ejecutar="";

    public Httppost (Context context,String ejecutar){
        mcontext = context;
       _ejecutar=ejecutar;

    }


    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog( mcontext);
       pd.setMessage("Espere Por favor..");
        pd.setCancelable(false);
        pd.show();
    }

    protected String doInBackground( String... params) {

        Globales.Resultconeccion="";

        // Log.e("url",Vr_globales.Url+params[0].toString()+"/"+params[1].toString());
        HttpURLConnection conn =null;
        String responseString="";
        try {

            _thread=Integer.parseInt(params[0].toString());
            _controlador=params[1].toString();
            _metodo=params[2].toString();
            _Jsonparam=params[3].toString();

            Thread.sleep(_thread);
            String token ="";

            responseString ="0";//inicio la repuesta
            URL url = new URL(Globales.Urlserver + "/api/"+_controlador+"/"+_metodo);
            HttpURLConnection.setFollowRedirects(false);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
           conn.setRequestProperty("Accept","application/json");
           // conn.setRequestProperty("Accept","application/json, application/xml, text/plain, text/html, *.*");
            // conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
            // conn.setRequestProperty("Accept","application/json");


            conn.setConnectTimeout(5000); //set timeout to 5 seconds
            conn.setRequestProperty("Authorization", "Bearer " + token);//token autenticado

            conn.setDoOutput(true);
            conn.setDoInput(true);
            //JSONObject jsonParam = new JSONObject();

            // jsonParam.put("id", 2);
            // jsonParam.put("Nombre", "mi pais");
            // jsonParam.put("Key", "este es mi valor");
            // jsonParam.put("edad", "34");

            // Log.e("JSON", jsonParam.toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
             //os.writeBytes(URLEncoder.encode(_Jsonparam.toString(), "UTF-8"));
            os.writeBytes(_Jsonparam);
            os.flush();
            os.close();

            responseString = readStream(conn.getInputStream());



/*
           //reading HTTP response code
            int response = conn.getResponseCode();
            Log.e("response",String.valueOf(conn.getResponseCode()) );

            if (response == HttpURLConnection.HTTP_OK) {

             Log.e("response","ok" );
                   }else{

              responseString="[]";
               Log.e("response",String.valueOf(response));
           }
*/
            // Log.e("STATUS", String.valueOf(conn.getResponseCode()));
            // Log.e("MSG" , conn.getResponseMessage());
            //Log.e("Respuesta", responseString);

                    /*
                JSONArray jsonarray = new JSONArray(responseString);
                for(int i=0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String id       = jsonobject.getString("id");
                    String nombre   = jsonobject.getString("nombre");
                    String edad   = jsonobject.getString("edad");
                    Log.e("-- ", id +" - " + nombre + " " + edad);
                }
                    */


        } catch (IOException e) {
         //   e.printStackTrace();
            Globales.Resultconeccion=e.toString();
        } finally {

            if (conn != null) {
                conn.disconnect();
            }
            return  responseString;
        }





/*
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {

                Thread.sleep(5000);
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
*/

        //    return null;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (pd.isShowing()) {
            pd.dismiss();
        }

         //Log.e("data",result);

                      //si la respuesta fue satisfactoria
                     //Uso esta clase para llamar un evento en una clase luego de la respuesta Correcta del Servidor
                Class c = null;
                try {
                    c = Class.forName(Recojerdata.class.getName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Object o = null;
                try {
                    o = c.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                Class[] paramTypes = new Class[2];
                paramTypes[0]=Context.class;
                paramTypes[1]=String.class;

                String methodName = _ejecutar;
                Method m = null;
                try {
                    m = c.getDeclaredMethod(methodName, paramTypes);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                try {

                    m.invoke(o,mcontext,result );

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }


        if(result.toString()=="0")
        {//si fallo la respuesta

            /*

            Log.e("paso",result);
            //algo fallo
            AlertDialog.Builder altBx = new AlertDialog.Builder( mcontext);
            altBx.setTitle("Error Descargando datos.!!");
            altBx.setMessage("El Servidor No esta en Servicio...");
            altBx.setPositiveButton("Reintentar", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    new  Httppost(mcontext,_ejecutar).execute(_controlador,_metodo);
                }
            });
            altBx.setNeutralButton("Salir", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    //show any message
                }

            });
            altBx.show();
*/

        }else
        {
                //aqui ejecuto si todo pasa bien
        }

    }
}


