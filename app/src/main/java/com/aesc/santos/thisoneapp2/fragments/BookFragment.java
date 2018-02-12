package com.aesc.santos.thisoneapp2.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aesc.santos.thisoneapp2.Entidades.Usuario;
import com.aesc.santos.thisoneapp2.Entidades.VolleySingleton;
import com.aesc.santos.thisoneapp2.R;
import com.aesc.santos.thisoneapp2.Utilidades;
import com.aesc.santos.thisoneapp2.adapters.UsuariosImagenURLAdapters;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // TODO Armando: Those are my variebles
    RecyclerView recyclerView;
    ArrayList<Usuario> listaUsuario;

    ProgressDialog proceso;
    JsonObjectRequest jsonObjectRequest;

    private OnFragmentInteractionListener mListener;

    public BookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookFragment newInstance(String param1, String param2) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Book");
        setHasOptionsMenu(true);

        construirRecycler(view);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.idList){
            Utilidades.visualizacion = Utilidades.LIST;
        }

        if (id == R.id.idGrid){
            Toast.makeText(getContext(), "Grid Mode", Toast.LENGTH_SHORT).show();
            //Utilidades.visualizacion = Utilidades.GRID;
        }
        construirRecycler(getView());
        return super.onOptionsItemSelected(item);
    }

    private void construirRecycler(View view) {
        listaUsuario = new ArrayList<>();
        recyclerView = view.findViewById(R.id.idRecyclerImagen);

        if (Utilidades.visualizacion == Utilidades.LIST){
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        }else{
            recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(),3));
        }

        recyclerView.setHasFixedSize(true);
        cargarWebService();
    }

    private void cargarWebService() {
        proceso= new ProgressDialog(getContext());
        proceso.setMessage("Consultando Imagenes...");
        proceso.show();

        String ip = getString(R.string.ip);
        String carpetaD = getString(R.string.carpetaDestino);
        String url = ip + carpetaD + "/wsJSONConsultarListaImagenesUrl.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        Usuario usuario = null;

        JSONArray json = response.optJSONArray("usuario");

        try {
            for (int i = 0; i< json.length();i++ ){
                usuario = new Usuario();
                JSONObject jsonObject = null;
                jsonObject=json.getJSONObject(i);
                usuario.setDocumento(jsonObject.getInt("documento"));
                usuario.setNombre(jsonObject.getString("nombre"));
                usuario.setProfeccion(jsonObject.getString("profesion"));
                usuario.setRutaImagen(jsonObject.getString("ruta_imagen"));

                listaUsuario.add(usuario);
            }
            proceso.hide();

            UsuariosImagenURLAdapters adapters = new UsuariosImagenURLAdapters(listaUsuario,getContext());
            recyclerView.setAdapter(adapters);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se pudo obtener la conexion!!" + response, Toast.LENGTH_SHORT).show();
            proceso.hide();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear(); //Empty the old menu
        inflater.inflate(R.menu.menu_book, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Al parecer la DB esta Vacia -> "+error.toString(), Toast.LENGTH_SHORT).show();
        proceso.hide();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
