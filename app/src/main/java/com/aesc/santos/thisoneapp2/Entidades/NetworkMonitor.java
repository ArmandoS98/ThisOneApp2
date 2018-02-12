package com.aesc.santos.thisoneapp2.Entidades;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.aesc.santos.thisoneapp2.SQLiteHelper.RecursosSQLite;
import com.aesc.santos.thisoneapp2.SQLiteHelper.SQLiteHelper;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Android on 12/13/2017.
 */

public class NetworkMonitor extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        if (chechkNetworkConection(context)){
            final SQLiteHelper dbHelper = new SQLiteHelper(context);
            final SQLiteDatabase database = dbHelper.getWritableDatabase();

            Cursor cursor  = dbHelper.readFromLocal(database);

            while (cursor.moveToNext()){
                int sync_status = cursor.getInt(cursor.getColumnIndex(RecursosSQLite.SYNC_STATUS));
                if (sync_status == RecursosSQLite.SYNC_STATUS_FAILED){
                    final String name = cursor.getString(cursor.getColumnIndex(RecursosSQLite.NOMBRE));
                    final String info = cursor.getString(cursor.getColumnIndex(RecursosSQLite.PROFESION));
                    final String ruta = cursor.getString(cursor.getColumnIndex(RecursosSQLite.RUTA_IMAGEN));

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, RecursosSQLite.SERVER_URL, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equalsIgnoreCase("registra")){
                            dbHelper.updateLocal(name,info,ruta ,RecursosSQLite.SYNC_STATUS_OK,database);
                            context.sendBroadcast(new Intent(RecursosSQLite.IU_UPDATE_BROADCAST));
                            Toast.makeText(context, "Sea Registrado con exito!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "No se pudo registrar", Toast.LENGTH_SHORT).show();
                        }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Parece que ubo un error " + error, Toast.LENGTH_SHORT).show();
                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("name",name);
                            params.put("info",info);
                            params.put("ruta",ruta);
                            return params;
                        }
                    };
                    VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
                }
            }
            dbHelper.close();
        }
    }

    public boolean chechkNetworkConection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }
}
