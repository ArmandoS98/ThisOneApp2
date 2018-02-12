package com.aesc.santos.thisoneapp2.Entidades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by Android on 28/11/2017.
 */

public class Usuario {
    //A ESTO SE LE LLAMA POJO

    private Integer documento;
    private String nombre;
    private String profeccion;
    private String rutaImagen;
    private int sync_status;


    public Usuario() {
    }

    public Usuario(Integer documento, String nombre, String profeccion, String rutaImagen, int sync_status) {
        this.documento = documento;
        this.nombre = nombre;
        this.profeccion = profeccion;
        this.rutaImagen = rutaImagen;
        this.sync_status = sync_status;
    }

    /*public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;

        try {
            byte[] byteCode = Base64.decode(dato,Base64.DEFAULT);
            //this.imagen= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);

            int alto = 100; //ESTO ES EL ALTO EN PIXELES
            int ancho = 150;


            Bitmap foto= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
            //this.imagen = Bitmap.createScaledBitmap(foto,alto,ancho,true);
        }catch (Exception e){
            e.printStackTrace();
        }

    }*/


    public int getSync_status() {
        return sync_status;
    }

    public void setSync_status(int sync_status) {
        this.sync_status = sync_status;
    }

    public Integer getDocumento() {
        return documento;
    }

    public void setDocumento(Integer documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProfeccion() {
        return profeccion;
    }

    public void setProfeccion(String profeccion) {
        this.profeccion = profeccion;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
}
