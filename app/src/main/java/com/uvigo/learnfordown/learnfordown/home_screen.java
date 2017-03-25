package com.uvigo.learnfordown.learnfordown;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.database.DatabaseUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.util.ArrayList;

public class home_screen extends AppCompatActivity {

    private GoogleApiClient client;
    private   GestionNiveles gn;
    TextView titulo;
    boolean registrado=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //Eliminar la barra con el titulo de la aplicacion
        setContentView(R.layout.activity_home_screen);
        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/Berlin Sans FB Demi Bold.ttf");
        titulo = (TextView) findViewById(R.id.textView);
        titulo.setTypeface(face);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        //Prueba bd
/*
        Context context =this.getApplicationContext();
        context.deleteDatabase("learn.sqlite");
        DataBaseManager db = new DataBaseManager(context);

        InsertData iD = new InsertData(context);
        iD.insertar();
        //db.insertar_user("pepe",5);
*/
        Context context =this.getApplicationContext();
        File dbFile = context.getDatabasePath("learn.sqlite");
        registrado = dbFile.exists();
        if(registrado){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setView(R.layout.dialog_recuperar_nivel);
            }
            else {
                builder.setMessage("¿Desea recuperar el último nivel?  ")
                         .setTitle("Recuperacion nivel");
            }
            builder.setPositiveButton("Vale", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    DataBaseManager db = new DataBaseManager(getApplicationContext());
                    lanzaIntent(db.getPrimerNivel());
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

            AlertDialog dialog = builder.create();
           dialog.show();
        }


    }

    /**
     * Called when the user clicks the Send button
     */
    public void sendMessage(View view) {
        // Do something in response to button
        if(registrado) {
            Intent intent = new Intent(home_screen.this, menu_screen.class);
            startActivity(intent);
        }
        else{
            lanzaAlerta();
        }
    }



    public void sendMessage2(View view) {
        // Do something in response to button
        if(registrado) {
            Intent intent = new Intent(home_screen.this, menu_write_screen.class);
            startActivity(intent);
        }

        else{
            lanzaAlerta();
        }
    }
    public void ajustes (View view){
        Intent intent1 = new Intent(home_screen.this, login_screen.class);
        startActivity(intent1);
    }

    public void salir (View view) {
        // Do something in response to button
        finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void lanzaAlerta() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Registre un usuario en el boton de ajustes");
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("home_screen Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();

    }
    public void lanzaIntent(Nivel nivel){

            if(nivel.tipoNivel.equals("leerletras")) {
                if (nivel.dificultad == 1) {
                    Intent intent = new Intent(this, lettergame1lvl_screen.class);
                    startActivity(intent);
                }
                if (nivel.dificultad == 2) {
                    Intent intent = new Intent(this, lettergame2lvl_screen.class);
                    startActivity(intent);
                }
                if (nivel.dificultad == 3) {
                    Intent intent = new Intent(this, lettergame3lvl_screen.class);
                    startActivity(intent);
                }
                if (nivel.dificultad == 4) {
                    Intent intent = new Intent(this, lettergame4lvl_screen.class);
                    startActivity(intent);
                }
            }
            if(nivel.tipoNivel.equals("silabasdirectas")||nivel.tipoNivel.equals("silabasinversas")||nivel.tipoNivel.equals("silabastrabadas")){
                if(nivel.dificultad==1) {
                    Intent intent = new Intent(this, silabasgame1lvl_screen.class);
                    intent.putExtra("tipoSilaba", nivel.tipoNivel);
                    startActivity(intent);
                }
                if(nivel.dificultad==2) {
                    Intent intent = new Intent(this, silabasgame2lvl_screen.class);
                    intent.putExtra("tipoSilaba", nivel.tipoNivel);
                    startActivity(intent);
                }
                if(nivel.dificultad==3) {
                    Intent intent = new Intent(this, silabasgame3lvl_screen.class);
                    intent.putExtra("tipoSilaba", nivel.tipoNivel);
                    startActivity(intent);
                }
                if(nivel.dificultad==4) {
                    Intent intent = new Intent(this, silabasgame4lvl_screen.class);
                    intent.putExtra("tipoSilaba", nivel.tipoNivel);
                    startActivity(intent);
                }

              }
         if(nivel.tipoNivel.contains("palabras")){
            Intent intent = new Intent(this,palabrasgame1_2lvl_screen.class);
            intent.putExtra("tipoSilaba", nivel.tipoNivel);
            intent.putExtra("nivel", nivel.dificultad);
            startActivity(intent);
        }
        if(nivel.tipoNivel.contains("frase")){
            Intent intent = new Intent(this,frasegame1lvl_screen.class);
            intent.putExtra("tipoSilaba", nivel.tipoNivel);
            intent.putExtra("nivel", nivel.dificultad);
            startActivity(intent);
        }
        if(nivel.tipoNivel.equals("escribirletras")){
            Intent intent = new Intent(this,writegame_level1_screen.class);
            startActivity(intent);
        }
        if(nivel.tipoNivel.equals("escribirconsombreado")){
            Intent intent = new Intent(this,writegame_level2_screen.class);
            startActivity(intent);

        }
        if(nivel.tipoNivel.equals("escribirsinsombreado")){
            Intent intent = new Intent(this,writegame_level3_screen.class);
            startActivity(intent);

        }
        if(nivel.tipoNivel.equals("escribirtecladopalabra")){
            Intent intent = new Intent(this,writegame_level4_screen.class);
            startActivity(intent);

        }
    }

}