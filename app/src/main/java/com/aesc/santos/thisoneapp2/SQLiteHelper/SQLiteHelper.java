package com.aesc.santos.thisoneapp2.SQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Android on 12/12/2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String CREATE_TABLE = "CREATE TABLE " + RecursosSQLite.TABLE_NAME + " (" +
                                                                RecursosSQLite.DOCUMENTO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                                RecursosSQLite.NOMBRE + " VARCHAR(45), " +
                                                                RecursosSQLite.PROFESION + " VARCHAR(45), " +
                                                                RecursosSQLite.RUTA_IMAGEN + " VARCHAR(45), " +
                                                                RecursosSQLite.SYNC_STATUS + " INTEGER);";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS" + RecursosSQLite.TABLE_NAME;

    public SQLiteHelper(Context context) {
        super(context, RecursosSQLite.DATABASE_NAME, null, DATABASE_VERSION);
    }

    //ESTE METODO SE EJECUTA CUANDO SE INSTALA POR PRIMERA VWEZ LA APP
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    //ESTE METODO SE EJECUTA CUANDO REALIZAMOS ALGUNA MODIFICACION A LA DB
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    //METODO QUE GUARDAR LOS DATOS DE MANERA LOCAL
    public static void saveToLocal(String nombre, String profesion, String ruta_imagen, int sync_status, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecursosSQLite.NOMBRE, nombre);
        contentValues.put(RecursosSQLite.PROFESION, profesion);
        contentValues.put(RecursosSQLite.RUTA_IMAGEN, ruta_imagen);
        contentValues.put(RecursosSQLite.SYNC_STATUS, sync_status);
        db.insert(RecursosSQLite.TABLE_NAME, null,contentValues);
    }

    //PREPARAMOS NUESTRA INFORMACION
    public Cursor readFromLocal (SQLiteDatabase db){
        String[] projection = {RecursosSQLite.NOMBRE,RecursosSQLite.PROFESION,RecursosSQLite.RUTA_IMAGEN,RecursosSQLite.SYNC_STATUS};

        return (db.query(RecursosSQLite.TABLE_NAME, projection,null,null,null,null,null));
    }

    //ALISTANDO DATOS PARA SINCRONIZAR
    public void updateLocal(String nombre, String profesion, String ruta_imagen, int sync_status, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecursosSQLite.SYNC_STATUS, sync_status);
        String selection = (RecursosSQLite.NOMBRE + " LIKE ?");
        selection += (RecursosSQLite.PROFESION + " LIKE ?");
        selection += (RecursosSQLite.RUTA_IMAGEN + " LIKE ?");
        //String id = String.valueOf(documento);

        String [] selection_args = {nombre,profesion,ruta_imagen};
        db.update(RecursosSQLite.TABLE_NAME,contentValues,selection,selection_args);
    }
}
