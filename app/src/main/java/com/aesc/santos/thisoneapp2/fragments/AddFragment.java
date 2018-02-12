package com.aesc.santos.thisoneapp2.fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.aesc.santos.thisoneapp2.Entidades.DatosEnvio;
import com.aesc.santos.thisoneapp2.Entidades.VolleySingleton;
import com.aesc.santos.thisoneapp2.R;
import com.aesc.santos.thisoneapp2.SQLiteHelper.RecursosSQLite;
import com.aesc.santos.thisoneapp2.SQLiteHelper.SQLiteHelper;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //MIS VARIABLES CREADAS PARA EL PROYECTO
    ArrayList<DatosEnvio> arrayList = new ArrayList<>();

    private static final String CARPETA_PRINCIPAL ="misImagenesApp/"; //DIRECTORIO PRINCIPAL
    private static final String CARPETA_IMAGEN ="imagenes"; //CARPETA DONDE SE GUARDARAN LAS IMAGENES
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN; //RUTA COMPLETA DEL DIRECTORIO DONDE SE ALOJARA LA IMAGEN
    private String path; //ALMACENA LA RUTA DEL IMAGEN
    File fileImagen;
    Bitmap bitmap;

    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;

    EditText nombre, profeccion;
    Button bFoto;
    ImageView imgFoto;
    ProgressDialog progreso;
    BroadcastReceiver broadcastReceiver;
    StringRequest stringRequest;

    private OnFragmentInteractionListener mListener;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //CONSTRUCTOR SQLite
        readFromLocalStorage();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                readFromLocalStorage();
            }
        };

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_add, container, false);

        //CONSTRUCTOR SQLite
        //readFromLocalStorage();
        Toolbar toolbar = vista.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Add");
        setHasOptionsMenu(true);

        nombre= vista.findViewById(R.id.campoNombreURL);
        profeccion = vista.findViewById(R.id.campoProfesion);
        bFoto= vista.findViewById(R.id.btnFoto);
        imgFoto = vista.findViewById(R.id.imgFoto);

        bFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogOpciones();
            }
        });

        return vista;
    }

    private void readFromLocalStorage(){
        arrayList.clear();

        SQLiteHelper helper = new SQLiteHelper(getContext());
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();

        Cursor cursor = helper.readFromLocal(sqLiteDatabase);

        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(RecursosSQLite.NOMBRE));
            String profesion = cursor.getString(cursor.getColumnIndex(RecursosSQLite.PROFESION));
            String ruta = cursor.getString(cursor.getColumnIndex(RecursosSQLite.RUTA_IMAGEN));
            int sync_status = cursor.getInt(cursor.getColumnIndex(RecursosSQLite.SYNC_STATUS));

            arrayList.add(new DatosEnvio(name,profesion,ruta,sync_status));
        }

        cursor.close();
        helper.close();
    }

    private void saveToAppServer(String name, String profesion, String ruta){

        if (chechkNetworkConection()){
            cargarWebService();
            Toast.makeText(getContext(), "Hay buen internet!!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "Me di cuenta que hay internet", Toast.LENGTH_SHORT).show();
            saveToLocalStorage(name,profesion,ruta,RecursosSQLite.SYNC_STATUS_FAILED);
        }

    }

    //VERIFICADOR DE LA RED
    public boolean chechkNetworkConection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }

    //SAVE TO LOCAL STORAGE
    private void saveToLocalStorage(String name, String profesion, String ruta, int sync){
        SQLiteHelper dbhelper = new SQLiteHelper(getContext());
        SQLiteDatabase database = dbhelper.getWritableDatabase();

        dbhelper.saveToLocal(name,profesion,ruta,sync,database);
        readFromLocalStorage();
        dbhelper.close();

        Log.i("Nombre", name.toString());
        Log.i("Profecion", profesion.toString());
        Log.i("Ruta", ruta.toString());
        Toast.makeText(getContext(), "Guarda los datos en SQLite", Toast.LENGTH_SHORT).show();
    }

    //ENCARGADO DE MOSTRAR LA TOOLBAR CON LAS OPCINES PROGRAMADAS
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //OPCIONES A MOSTAR
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String sNombre = nombre.getText().toString();
        String sProfesion = profeccion.getText().toString();
        String sImagen = convertirImgString(bitmap);
        if (id==R.id.inicioItem){
            saveToAppServer(sNombre,sProfesion,sImagen);
        }
        return super.onOptionsItemSelected(item);
    }

    //MOSTRAR DIALOGO
    private void mostrarDialogOpciones() {
        final CharSequence[] opciones={"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    abriCamara();
                }else{
                    if (opciones[i].equals("Elegir de Galeria")){
                        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione"),COD_SELECCIONA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        builder.show();
    }

    //INTENT DE LA CAMRA
    private void abriCamara() {
        File miFile = new File(Environment.getExternalStorageDirectory(),DIRECTORIO_IMAGEN);
        Boolean isCreada = miFile.exists();

        if (isCreada == false){
            isCreada = miFile.mkdirs(); //ESTO CREA EL DIRECTORIO POR SI ANTERIORMENTE NO SE CREO
        }

        if (isCreada == true){
            Long consecutivo = System.currentTimeMillis()/1000; //CAPTURA LA FECHA U HORA DEL DISPOSITIVO
            String nombre = consecutivo.toString()+".jpg";
            path = Environment.getExternalStorageDirectory()+File.separator+DIRECTORIO_IMAGEN + File.separator+nombre; //INDICA LA RUTA DE ALMACENAMIENTO

            fileImagen= new File(path);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(fileImagen));

            startActivityForResult(intent,COD_FOTO);
        }
    }

    //SELECCIONADOR DE LAS OPCIONES DEL DIALOG
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case COD_SELECCIONA:
                Uri miPath=data.getData();
                imgFoto.setImageURI(miPath);

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),miPath);
                    imgFoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case COD_FOTO:
                MediaScannerConnection.scanFile(getContext(), new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String s, Uri uri) {
                        Log.i("Path",""+path);
                    }
                });

                bitmap = BitmapFactory.decodeFile(path);
                imgFoto.setImageBitmap(bitmap);
                break;
        }

        bitmap = redireccionarImagen(bitmap,600,800);
    }

    //NUEVA REDIRECCON DE LA IMAGEN
    private Bitmap redireccionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();

        if (ancho > anchoNuevo || alto > altoNuevo){
            float escalaAncho= anchoNuevo/ancho;
            float escalaAlto = altoNuevo/alto;

            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);
        }else{
            return bitmap;
        }

    }

    // MI WEBSEVICE
    private void cargarWebService() {

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando...");
        progreso.show();

        String ip = getString(R.string.ip);
        String carpetaD = getString(R.string.carpetaDestino);

        String url = ip + carpetaD + "/wsJSONRegistroMovil.php?";

        stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            //ESTO RECIBE LA RESPUESTA DEL WEBSERVICE CUANDO TODO ESTA CORRECTO
            @Override
            public void onResponse(String response) {
                progreso.hide();

                if (response.trim().equalsIgnoreCase("registra")){
                    //documento.setText("");
                    nombre.setText("");
                    profeccion.setText("");
                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "Sea Registrado con exito!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "No se pudo registrar", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            //Y ESTA PARTE SE EJECUTA CUANDO DA ALGUN ERROR
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se ah podido conectar", Toast.LENGTH_SHORT).show();
                progreso.hide();
            }
        }){
            //ESTO NOS VA A PERMITIR ENVIAR LOS DATOS MEDIANTE POST A NUESTRO WEBSERVICE
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String sDocumento = "";
                String sNombre = nombre.getText().toString();
                String sProfesion = profeccion.getText().toString();
                String sImagen = convertirImgString(bitmap);

                Map<String,String> parametros = new HashMap<>();
                parametros.put("documento",sDocumento);
                parametros.put("nombre",sNombre);
                parametros.put("profesion",sProfesion);
                parametros.put("imagen",sImagen);

                return parametros;

            }
        };
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(stringRequest);
    }

    //ENCARGADO DE COMVERTIR LA IMAGEN
    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
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

    @Override
    public void onStart() {
        super.onStart();
        getContext().registerReceiver(broadcastReceiver, new IntentFilter(RecursosSQLite.IU_UPDATE_BROADCAST));
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(broadcastReceiver);
    }

}
