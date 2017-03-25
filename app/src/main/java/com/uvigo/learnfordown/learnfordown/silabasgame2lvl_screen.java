package com.uvigo.learnfordown.learnfordown;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.TreeSet;

public class silabasgame2lvl_screen extends AppCompatActivity {
    private RecyclerView horizontal_recycler_view;
    private ArrayList<String> horizontalList;
    private HorizontalAdapter horizontalAdapter;
    private RecyclerView horizontal_recycler_view2;
    private ArrayList<String> horizontalList2;
    private HorizontalAdapter horizontalAdapter2;
    Button ButtonActual;
    TextView titulo,letracorrecta;
    RatingBar ratingbar1;
    String Correcta,palabracom, tmpDownSlash= " ";
    ImageButton BackArrow,Home;
    final HashMap<Integer, Float> thresholds = new HashMap<>();

    ImageView palabra;
    GestionNiveles  gn;
    String tipoNivel="silabasdirectas";
    ArrayList<FotoPalabra> fp;

    int i=0;
    /*
    int contador;
    int aciertos=0;
    */
    Estrellas es;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silabasgame2lvl_screen);
        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/Berlin Sans FB Demi Bold.ttf");
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            tipoNivel = extras.getString("tipoSilaba");
            System.out.println(tipoNivel);
        }
        horizontal_recycler_view= (RecyclerView) findViewById(R.id.horizontal_recycler_view);
        horizontal_recycler_view2= (RecyclerView) findViewById(R.id.horizontal_recycler_view2);
        titulo = (TextView) findViewById(R.id.textView2);
        Home = (ImageButton) findViewById(R.id.button5);
        palabra= (ImageView)findViewById(R.id.imageView2);
        letracorrecta=(TextView)findViewById(R.id.textView4);
        titulo.setTypeface(face);
        /*
        ratingbar1 = (RatingBar) findViewById(R.id.ratingBar);

        thresholds.clear();
        thresholds.put(1, 1f); // 1 acierto, 1 estrella
        thresholds.put(10, 2f); //10 aciertos, 2 estrellas
        thresholds.put(25, 3f); //25 aciertos, 3 estrellas
        thresholds.put(45, 4f); //45 aciertos, 4 estrellas
        thresholds.put(65, 5f); //65 aciertos, 5 estrellas
        thresholds.put(80, 6f); //80 aciertos, 6 estrellas
        */

        Context context = this.getApplicationContext();
        gn = new GestionNiveles(context);
       es =new Estrellas(this,gn, gn.setNivel(tipoNivel,2));
        fp=gn.getFotos();
        horizontalList=new ArrayList<String>();
        gn.rellenarConletras(fp.get(i).getSilaba().toUpperCase(),horizontalList);
        Collections.shuffle( horizontalList);
        letracorrecta.setText(fp.get(i).getSilaba().toUpperCase());
        palabra.setImageResource(fp.get(i).getFoto());
        Correcta= fp.get(i).getSilaba().toUpperCase();
        tmpDownSlash = "";
        for (int i=0;i<Correcta.length();i++){
            tmpDownSlash += " _";
        }
        palabracom = fp.get(i).getPalabra().toUpperCase();
        palabracom = palabracom.replaceAll(Correcta.toUpperCase().replaceAll("A","Á").replaceAll("E","É").replaceAll("I","Í").replaceAll("O","Ó").replaceAll("U","Ú"), tmpDownSlash);
        palabracom = palabracom.replaceAll(Correcta.toUpperCase(), tmpDownSlash);
        letracorrecta.setText(palabracom);

        horizontalAdapter=new HorizontalAdapter(horizontalList);

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(silabasgame2lvl_screen.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);

        horizontalList2=new ArrayList<String>();
        gn.rellenarConletras(fp.get(i).getSilaba().toUpperCase(),horizontalList2);
        Collections.shuffle( horizontalList2);
        horizontalAdapter2=new HorizontalAdapter(horizontalList2);

        LinearLayoutManager horizontalLayoutManagaer2 = new LinearLayoutManager(silabasgame2lvl_screen.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view2.setLayoutManager(horizontalLayoutManagaer2);





        horizontal_recycler_view.setAdapter(horizontalAdapter);


        horizontal_recycler_view2.setAdapter(horizontalAdapter2);
    }
    public void BackArrow (View v){
        Intent intent1 = new Intent(silabasgame2lvl_screen.this, menu_screen.class);
        startActivity(intent1);
    }
    public void goHome (View v){
        Intent intent1 = new Intent(silabasgame2lvl_screen.this, home_screen.class);
        startActivity(intent1);
    }
/*
    public void pulsar() {
        float rating = 0;
        for (int i : new TreeSet<>(thresholds.keySet())) {
            if (contador < i) {
                break;
            }
            rating = thresholds.get(i);
        }
        if (rating != ratingbar1.getRating()) {
            ratingbar1.setRating(rating);
            Toast toast = Toast.makeText(this, "¡HAS CONSEGUIDO UNA ESTRELLITA!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.RELATIVE_LAYOUT_DIRECTION, -350, -50);
            toast.show();
        }
    }
*/
    public void ButtonCheck (View v){
        Button b = (Button)v;
        ButtonActual =b;
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f,
                -50.0f, 0.0f);
        animation.setDuration(400);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (Correcta.equals(ButtonActual.getText().toString())) {
                    ButtonActual.setBackgroundColor(Color.GREEN);
                    ButtonActual.setEnabled(false);
                    palabracom=fp.get(i).getPalabra().toUpperCase().replaceAll(tmpDownSlash,ButtonActual.getText().toString());
                    letracorrecta.setText(palabracom);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (Correcta.equals(ButtonActual.getText().toString())) {
                        System.out.println("Se ha anotado un acierto");
                        es.acierto();
                        es.pulsar(true);
                        if (!gn.isnivelCompletado()) {
                            i++;
                            cambiarFoto();
                        } else {
                            System.out.print("el nivel esta finalizado");
                            gn.avanzaNivel();
                            if (gn.getDificultad() != 2 || !(gn.getTipo().equals(tipoNivel))) {
                                System.out.println("Se debe abrir otra pantalla porque esta ya no vale");
                                //Código para abrir otra pantalla
                                Intent intent = new Intent(silabasgame2lvl_screen.this, silabasgame3lvl_screen.class);
                                startActivity(intent);
                            } else {
                                fp = gn.getFotos();
                                i = 0;
                                cambiarFoto();
                                System.out.println("Se debe avanzar el nivel");
                            }

                        }

                } else {
                    gn.fallo();
                    //Codigo de Animacion Fallo

                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        b.startAnimation(animation);
    }

    private void cambiarFoto() {
        horizontalList.clear();
        horizontalList = new ArrayList<String>();
        gn.rellenarConletras(fp.get(i).getSilaba().toUpperCase(),horizontalList);
        Collections.shuffle(horizontalList);
        palabra.setImageResource(fp.get(i).getFoto());
        letracorrecta.setText(fp.get(i).getSilaba().toUpperCase());
        Correcta= fp.get(i).getSilaba().toUpperCase();
        horizontalAdapter = new HorizontalAdapter(horizontalList);

        tmpDownSlash = "";
        for (int i=0;i<Correcta.length();i++){
            tmpDownSlash += " _";
        }

        palabracom = fp.get(i).getPalabra().toUpperCase();
        palabracom = palabracom.replaceAll(Correcta.toUpperCase().replaceAll("A","Á").replaceAll("E","É").replaceAll("I","Í").replaceAll("O","Ó").replaceAll("U","Ú"), tmpDownSlash);
        palabracom = palabracom.replaceAll(Correcta.toUpperCase(), tmpDownSlash);
        letracorrecta.setText(palabracom);

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(silabasgame2lvl_screen.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);

        horizontal_recycler_view.setAdapter(horizontalAdapter);
        horizontalList2.clear();
        horizontalList2=new ArrayList<String>();
        gn.rellenarConletras(fp.get(i).getSilaba().toUpperCase(),horizontalList2);
        Collections.shuffle( horizontalList2);
        horizontalAdapter2=new HorizontalAdapter(horizontalList2);

        LinearLayoutManager horizontalLayoutManagaer2 = new LinearLayoutManager(silabasgame2lvl_screen.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view2.setLayoutManager(horizontalLayoutManagaer2);
        horizontal_recycler_view2.setAdapter(horizontalAdapter2);
    }


}

