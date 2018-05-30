package com.softfinc.softfinc;

import org.json.JSONObject;

public class Globales {


    //##########globles del la app
    public static int Autorizacion =0; ;//Recojo si esta autorizado el movil
    public static double VersionCode =1.4 ;//marco la version del codigo Actual
    public static String IMEI =""; ;//Recojo el imesi del movil
    public static String Uidapp =""; ;//Recojo el serial de app udi
    public static int Tprinter =0;
    public static String Keyinterno = "205eedd0-40e2-460e-8856-9802f53f24d7";
    public static int Actualizarapp =0;


    //########Globales de usuario
    public static String Usuario ="";
    public static String Idusuario ="";
    public static String Clave ="";

    //*********Globales de empresa
    public static String Idempresa ="";
    public static String Nempresa ="";


    // ########globales del coneccion
    public static String Respuesta =""; ;//Recojo la respuesta de hhtprequest
    public static String Resultconeccion =""; ;//Recojo la respuesta de HttpRequest
    public static String Urlserver ="";//"http://10.0.0.4:57130/api/"; ;//Recojo la url del Servidor
    public static String Ippublica =""; ;//Recojo el serial de app udi
    public static JSONObject jsonParam = new JSONObject();//pasar parametros a post


    // #verificar si tengo el persmiso para esto




}
