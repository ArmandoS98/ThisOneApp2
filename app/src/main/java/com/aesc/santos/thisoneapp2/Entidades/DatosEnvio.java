package com.aesc.santos.thisoneapp2.Entidades;

/**
 * Created by Android on 28/11/2017.
 */

public class DatosEnvio {
    //A ESTO SE LE LLAMA POJO


    private String nombre;
    private String profeccion;
    private String rutaImagen;
    private int sync_status;


    public DatosEnvio() {
    }

    public DatosEnvio(String nombre, String profeccion, String rutaImagen, int sync_status) {
        this.nombre = nombre;
        this.profeccion = profeccion;
        this.rutaImagen = rutaImagen;
        this.sync_status = sync_status;
    }

    public int getSync_status() {
        return sync_status;
    }

    public void setSync_status(int sync_status) {
        this.sync_status = sync_status;
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
