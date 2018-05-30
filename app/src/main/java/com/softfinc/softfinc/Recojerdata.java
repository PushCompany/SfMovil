package com.softfinc.softfinc;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Recojerdata{


    //si el movil esta autorizado en el api
        public static void Verificarmovil(Context context,String dt) throws Exception {


           Log.e("Data->",dt);

            //array para recojer los datos del Modelo
            List<ConfModel> ArrayConfimodel = new ArrayList<>();

            int _Status= 0;
            String _cambios="false";
            String _actualizarapp="false";

            //si paso correcto
            if (Globales.Resultconeccion.equals("")){

                JSONObject obj = new JSONObject(dt);
                JSONArray arr = obj.getJSONArray("data");
                //{"items":[{"name":"command","index":"X","optional":"0"},{"name":"command","index":"X","optional":"0"}]}
                // JSONArray jsonarray = new JSONArray(arr);//solo[array]
                for(int i=0; i <  arr.length(); i++) {

                    ConfModel clase = new ConfModel();

                    JSONObject jsonobject =  arr.getJSONObject(i);
                    _Status = jsonobject.getInt("_Status");
                    String _Idempresa = jsonobject.getString("_Idempresa");
                    String _nempresa = jsonobject.getString("_nempresa");
                    String _sloempresa = jsonobject.getString("_sloempresa");
                    String _idusuario = jsonobject.getString("_idusuario");
                    String _clave = jsonobject.getString("_clave");
                    String _nusuario = jsonobject.getString("_nusuario");
                    String _fUltUpdate = jsonobject.getString("_fUltUpdate");
                    _cambios = jsonobject.getString("_cambios");
                     String  _Tprinter = jsonobject.getString("_tprinter");
                     _actualizarapp = jsonobject.getString("_actualizarapp");

                    int fechaint =Utiles.DateToDays(Utiles.StringtoDate(_fUltUpdate));

                    clase.setStatus(_Status);
                    clase.setIdempresa(_Idempresa);
                    clase.setNempresa(_nempresa);
                    clase.setSloempresa(_sloempresa);
                    clase.setIdusuario(_idusuario);
                    clase.setClave(_clave);
                    clase.setUsuario(_nusuario);
                    clase.setfUltUpdate(fechaint);
                    clase.setTprinter(Integer.parseInt(_Tprinter));

                    //Log.e("Datos: ",id +" --> " + nota);
                    ArrayConfimodel.add(clase);


                  //Log.e("Resultado ",result);
                   // Log.e("_Idempresa", _Idempresa);
                  //  Log.e("_nempresa",_nempresa);
                  //  Log.e("_sloempresa",_sloempresa);
                  //  Log.e("_idusuario",_idusuario);
                  //  Log.e("_clave1", _clave);
                  //  Log.e("fecha",_fUltUpdate);
                   // Log.e("_nusuario",_nusuario);
                   // Log.e("_cambios",_cambios);
                }

                //defino si quiero actualizar los datos del movil
                if(_cambios.equals("true")){
                    List<ConfModel> Modelo = new ArrayList<>();
                   Modelo= ConfModel.Consultaarray(context,"1");
                   if(Modelo.isEmpty()){
                       ConfModel.Initconf(context);//si esta vacio creo un campo
                       ConfModel.Actualizar(context,  ArrayConfimodel);//luego actualizo la tabla config
                   }else{
                      ConfModel.Actualizar(context,  ArrayConfimodel);
                    //   Log.e("modelo","lleno");
                   }


                    // Log.e("actualizo","si");
                }else{
                    //Log.e("actualizo","no");
                }


                //verifico si la app esta de actualizar
                if(_actualizarapp.equals("true")){
                    Globales.Actualizarapp=1;
                    Log.e("actualizo ","si");
                }else{
                   Globales.Actualizarapp=0;
                    Log.e("actualizo ","no");
                }





                    //recojo las variables de la BD CONFIGURACION
                CargarvariablesDb(context);

                // if(_Status==1){
               //Utiles.Autorizacion=1;//envio que el movil esta autorizado
               // }else{
             // Utiles.Autorizacion=0;//envio que el movil no esta autorizado
              // }

                new Splash_Activity().RevisarAutoizacion(context,String.valueOf(_Status));

            }else{
                //aqui si no hay coneccion del servidor tengo que recojer la configuracion de la Db
               // Utiles.Autorizacion=0;//envio que el movil esta autorizado
                //recojo las variables de la BD CONFIGURACION
                CargarvariablesDb(context);

                new Splash_Activity().RevisarAutoizacion(context,String.valueOf(_Status));
            }




        }


    //revisar si puede guardar el host
    public static void verificarhost(Context context,String dt) throws JSONException {


       // Log.e("valor : ",valor);
            if (Globales.Resultconeccion==""){
                //Log.e("data : ",dt);
                JSONObject obj = new JSONObject(dt);
                JSONArray arr = obj.getJSONArray("data");
                //{"items":[{"name":"command","index":"X","optional":"0"},{"name":"command","index":"X","optional":"0"}]}

                String result="0";
                // JSONArray jsonarray = new JSONArray(arr);//solo[array]
                for(int i=0; i <  arr.length(); i++) {
                    JSONObject jsonobject =  arr.getJSONObject(i);
                   result= jsonobject.getString("result").trim();
                    // String nombre   = jsonobject.getString("nombre");
                    // String edad   = jsonobject.getString("edad");
                }
                new RegHost_Activity().evento(context,result);
            }else{

            //Envio "0" para saber que paso un error y limpiar lo que guardo el txt

                new RegHost_Activity().evento(context,"0");

            }
    }




    //revisar si puede guardar el host
    public static void VerificarActualizacion(Context context,String dt) throws JSONException {
          //  Log.e("data",dt);
        // Log.e("valor : ",valor);
        if (Globales.Resultconeccion==""){

            new Update_Activity().Datosactualizar(context,dt);
        }else{
            //Envio "0" para saber que paso un error y limpiar lo que guardo el txt
           new Update_Activity().Datosactualizar(context,"");

        }

    }




    //recojer las variables de la bse de datos y guradarla en variables globales
    public static void CargarvariablesDb(Context context){

        List<ConfModel> recojo = new ArrayList<>();
        recojo= ConfModel.Obtenerconf(context);
        for (ConfModel item: recojo) {

            Globales.Autorizacion=item.getStatus();//recojo de db si Esta autorizado
            Globales.Idempresa=item.getIdempresa();
            Globales.Usuario=item.getUsuario();
            Globales.Idusuario= item.getIdusuario();
            Globales.Clave= item.getClave();
            Globales.Nempresa= item.getNempresa();
            Globales.Tprinter= item.getTprinter();

           // Log.e("idempresa",item.getIdempresa() );
         //   Log.e("nempresa",item.getNempresa());
           // Log.e("sloempresa",item.getSloempresa() );
           // Log.e("idusuario",item.getIdusuario() );
           // Log.e("status", String.valueOf(item.getStatus())  );
          //  Log.e("usuario",item.getUsuario() );
          //  Log.e("clave",item.getClave() );
          //  Log.e("timestam",item.getTimestamp() );
          //  Log.e("fUltUpdate",String.valueOf(item.getfUltUpdate()) );
        }



    }

}
