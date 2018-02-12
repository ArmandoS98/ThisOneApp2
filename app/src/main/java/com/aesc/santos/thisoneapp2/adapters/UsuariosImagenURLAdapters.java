package com.aesc.santos.thisoneapp2.adapters;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aesc.santos.thisoneapp2.Utilidades;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.aesc.santos.thisoneapp2.Entidades.Usuario;
import com.aesc.santos.thisoneapp2.Entidades.VolleySingleton;
import com.aesc.santos.thisoneapp2.R;

import java.util.List;

/**
 * Created by Android on 28/11/2017.
 */

public class UsuariosImagenURLAdapters extends RecyclerView.Adapter<UsuariosImagenURLAdapters.UsuariosHolder> {

    List<Usuario> listaUsuarios;
    Context context;

    public UsuariosImagenURLAdapters(List<Usuario> listaUsuarios, Context context){
        this.listaUsuarios = listaUsuarios;
        this.context = context;
    }

    @Override
    public UsuariosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = 0;

        if (Utilidades.visualizacion==Utilidades.LIST){
            layout = R.layout.usuarios_list_image;
        }else {
            layout = R.layout.item_grid_datos;
        }

        View vista = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new UsuariosHolder(vista);
    }

    @Override
    public void onViewAttachedToWindow(UsuariosHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateCircularReveal(holder.itemView);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateCircularReveal(View view){
        int centerX = 0;
        int centerY = 0;
        int startRadius = 0;
        int endRadius = Math.max(view.getWidth(), view.getHeight());
        Animator animaton = ViewAnimationUtils.createCircularReveal(view,centerX,centerY,startRadius,endRadius);
        view.setVisibility(View.VISIBLE);
        animaton.start();
    }


    @Override
    public void onBindViewHolder(UsuariosHolder holder, int position) {
        //holder.txtDocumento.setText(listaUsuarios.get(position).getDocumento().toString());
        holder.txtNombre.setText(listaUsuarios.get(position).getNombre().toString());

        if (Utilidades.visualizacion==Utilidades.LIST){
            holder.txtProfeccion.setText(listaUsuarios.get(position).getProfeccion().toString());
        }

        if (listaUsuarios.get(position).getRutaImagen()!=null){
            cargarImagenWebService(listaUsuarios.get(position).getRutaImagen(),holder);
        }else{
            holder.imagen.setImageResource(R.drawable.img_base);
        }
    }

    private void cargarImagenWebService(String rutaImagen, final UsuariosHolder holder) {
        String ip = context.getString(R.string.ip);
        String carpetaD = context.getString(R.string.carpetaDestino);

        String urlImagen = ip + carpetaD + "/" +rutaImagen;
        urlImagen = urlImagen.replace(" ","%20");

        ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.imagen.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, "Wait Moment Please", Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(imageRequest);
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public class UsuariosHolder extends RecyclerView.ViewHolder {

        TextView txtNombre, txtProfeccion;
        ImageView imagen;

        public UsuariosHolder(View itemView) {
            super(itemView);

            txtNombre= itemView.findViewById(R.id.idNombre);
            if (Utilidades.visualizacion==Utilidades.LIST){
                txtProfeccion= itemView.findViewById(R.id.idProfesion);
            }
            imagen =itemView.findViewById(R.id.idImagen);
        }
    }
}
