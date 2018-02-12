package com.aesc.santos.thisoneapp2.SQLiteHelper;

import com.aesc.santos.thisoneapp2.R;

/**
 * Created by Android on 12/12/2017.
 */

public class RecursosSQLite {

    public static final int SYNC_STATUS_OK = 0;
    public static final int SYNC_STATUS_FAILED = 1;

    public static final String DATABASE_NAME = "info";
    public static final String TABLE_NAME = "datos";
    public static final String DOCUMENTO = "documento";
    public static final String NOMBRE = "nombre";
    public static final String PROFESION = "profesion";
    public static final String RUTA_IMAGEN = "ruta_imagen";
    public static final String SYNC_STATUS = "syncstatus";

    //NUEVA CONSTANTE
    public static final String SERVER_URL = "http://adrax.hol.es/wsJSONRegistroMovil.php?";
    public static final String IU_UPDATE_BROADCAST = "com.aesc.santos.thisoneapp2.uiupdatebroadcast";
}
