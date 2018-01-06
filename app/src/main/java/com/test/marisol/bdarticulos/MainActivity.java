package com.test.marisol.bdarticulos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private EditText et1, et2, et3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Cale

        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);

        try {
            deployDatabase();
        }catch (IOException e){
            Toast.makeText(this,"no se pudo ejecutar importacion de base de datos",Toast.LENGTH_SHORT);
        }
    }

    public void alta(View view) {
        AdminSqliteOpenHelper admin = new AdminSqliteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String cod = et1.getText().toString();
        String descrip = et2.getText().toString();
        String prec = et3.getText().toString();

        ContentValues registro = new ContentValues();
        registro.put("codigo", cod);
        registro.put("descripcion", descrip);
        registro.put("precio", prec);

        bd.insert("articulos", null, registro);
        bd.close();

        et1.setText("");
        et2.setText("");
        et3.setText("");
        //Mensaje que se muestra po pantalla por unos segundos
        Toast.makeText(this, "Se cargaron todos los datos del artículo", Toast.LENGTH_SHORT).show();

    }

    public void consultaPorCodigo(View view) {
        AdminSqliteOpenHelper admin = new AdminSqliteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String cod = et1.getText().toString();
        Cursor fila = bd.rawQuery(
                "select descripcion,precio from articulos where codigo=" + cod, null);
        if (fila.moveToFirst()) {
            et2.setText(fila.getString(0));
            et3.setText(fila.getString(1));
        } else
            Toast.makeText(this, "No existe un artículo con dicho código", Toast.LENGTH_SHORT).show();
        bd.close();
    }

    public void consultaPorDescrip(View view) {

        AdminSqliteOpenHelper admin = new AdminSqliteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String descrip = et2.getText().toString();
        Cursor fila = bd.rawQuery("select codigo,precio from articulos where descripcion='"
                + descrip + "'", null);
        if (fila.moveToFirst()) {
            et1.setText(fila.getString(0));
            et3.setText(fila.getString(1));
        } else
            Toast.makeText(this, "No existe un artículo con dicho código", Toast.LENGTH_SHORT).show();
        bd.close();
    }

    public void bajaPorCodigo(View view) {
        AdminSqliteOpenHelper admin = new AdminSqliteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String cod = et1.getText().toString();
        int cantidad = bd.delete("articulos", "codigo=" + cod, null);
        bd.close();

        et1.setText("");
        et2.setText("");
        et3.setText("");

        if (cantidad == 1) {
            Toast.makeText(this, "Se borró el artículo con dicho código", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "No existe un artículo con dicho código", Toast.LENGTH_SHORT).show();
        }
    }

    public void modificacion(View view){
        AdminSqliteOpenHelper admin = new AdminSqliteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String cod = et1.getText().toString();
        String descrip = et2.getText().toString();
        String prec = et3.getText().toString();
        ContentValues registro = new ContentValues();

        registro.put("codigo", cod);
        registro.put("descripcion", descrip);
        registro.put("precio", prec);

        int cantidad = bd.update("articulos", registro, "codigo=" + cod, null);
        bd.close();

        if (cantidad == 1)
            Toast.makeText(this, "Se modificaron los datos dicho código", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "No existe un artículo con dicho código", Toast.LENGTH_SHORT).show();

    }

    public void deployDatabase() throws IOException {
    //Open your local db as the input stream
        String packageName = getApplicationContext().getPackageName();
        String DB_PATH = "/data/data/" + packageName + "/databases/";
//Create the directory if it does not exist
        File directory = new File(DB_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String DB_NAME = "administracion"; //The name of the source sqlite file

        if(!fileExists(this, DB_NAME)) {

            // Path to the just created empty db
            String outFileName = DB_PATH + DB_NAME;

            InputStream myInput = getAssets().open("administracion");

            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();

        }else
            Toast.makeText(this, "El archivo existe", Toast.LENGTH_LONG).show();
    }
    //comprobar si el archivo en este caso la BD exite
    public boolean fileExists(Context context, String filename) {
        File file = context.getDatabasePath(filename);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }


}
