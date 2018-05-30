package com.softfinc.softfinc;

import android.app.ProgressDialog;
import android.app.UiAutomation;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Ubicacion_Http extends AsyncTask<String, String, String> {

    ProgressDialog pd;

    Context mcontext;
    String _controlador;
    String _metodo;
    Integer _thread;

    String _ejecutar="";

    public Ubicacion_Http(Context context, String ejecutar){
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

        Globales.Resultconeccion = "";

        HttpURLConnection conn =null;
        String responseString = "";
        InputStream DataInputStream = null;
        try {


            _thread= Integer.parseInt(params[0].toString());
            _controlador = params[1].toString();
            _metodo = params[2].toString();


            URL url = new URL("http://freegeoip.net/json/");
            conn = (HttpURLConnection)
                    url.openConnection();

            //set timeout for reading InputStream
           // conn.setReadTimeout(5000);
            // set timeout for connection
           // conn.setConnectTimeout(5000);
            //set HTTP method to GET
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000); //set timeout to 5 seconds
            //set it to true as we are connecting for input
            conn.setDoInput(true);

            //reading HTTP response code
            int response = conn.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuffer response2 = new StringBuffer();

            //if response code is 200 / OK then read Inputstream
            if (response == HttpURLConnection.HTTP_OK) {
                DataInputStream = conn.getInputStream();
                while ((inputLine = in.readLine()) != null) { response2.append(inputLine); }
                in.close();

                //recojo la respuesta
                responseString = response2.toString();

                //json de ejemplo
               // String s = "[{\"name\":\"name1\",\"url\":\"url1\"},{\"name\":\"name2\",\"url\":\"url2\"}]";

                responseString = "[" + responseString + "]";
                JSONArray arr = new JSONArray(responseString);
                //loop through each object
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonProductObject = arr.getJSONObject(i);
                    String ip = jsonProductObject.getString("ip");
                    String country_code = jsonProductObject.getString("country_code");
                    String country_name = jsonProductObject.getString("country_name");
                    String region_code = jsonProductObject.getString("region_code");
                    String region_name = jsonProductObject.getString("region_name");
                    String city = jsonProductObject.getString("city");
                    String zip_code = jsonProductObject.getString("zip_code");
                    String time_zone = jsonProductObject.getString("time_zone");
                    String latitude = jsonProductObject.getString("latitude");
                    String longitude = jsonProductObject.getString("longitude");
                    String metro_code = jsonProductObject.getString("metro_code");
                   Log.e("ippublica", ip);

                    //recojo la ip y la guardo en una varible publica
                    Globales.Ippublica = ip;
                }

            }

            Thread.sleep(_thread);

        } catch (IOException e) {
            e.printStackTrace();
            Globales.Resultconeccion = e.toString();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            return responseString;
        }

    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (pd.isShowing()) {
            pd.dismiss();
        }
      //  Log.e("paso",result);

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

