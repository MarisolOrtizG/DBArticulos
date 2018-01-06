package com.test.marisol.bdarticulos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jorgeonidas on 13/4/2017.
 */

public class AdminSqliteOpenHelper extends SQLiteOpenHelper {
    public AdminSqliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*Creamos la tabla articulos con los campos codigos(entero y clave primaria,
        descripcion que es un tipo texto y precio es un valor real*/
        db.execSQL("CREATE TABLE articulos (codigo INT PRIMARY KEY, descripcion TEXT, precio REAL)");

    }

    //onUpgrade se utiliza para modificar la estructura de la tabla
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
