package com.example.maoak.app_sport;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Exercices_read extends AppCompatActivity {
    TextView nom_exo;
    TextView descriptif;
    TextView principale;
    TextView secondaire;
    TextView categories;
    TextView type_exo;
    DBAdapter db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercice_read);
        nom_exo = (TextView)findViewById(R.id.textView25);
        descriptif = (TextView)findViewById(R.id.textView27);
        principale = (TextView)findViewById(R.id.textView29);
        secondaire = (TextView)findViewById(R.id.textView40);
        categories = (TextView)findViewById(R.id.textView42);
        type_exo = (TextView)findViewById(R.id.textView49);
        remplir_champ();
    }
    public void onclick_exoread(View v){
        Intent intent = new Intent(this,Exercice_add.class);
        intent.putExtra("exo_id",recup_id_exo());
        intent.putExtra("id",recup_id());
        startActivity(intent);
        finish();

    }
    public void remplir_champ(){

        String categorie;
        String type;

        int id= Integer.parseInt(recup_id_exo());
        db = new DBAdapter(this);
        db.open();
        Cursor c = db.get_donne_exo(id);
        nom_exo.setText(c.getString(1));
        descriptif.setText(c.getString(2));
        categorie = recup_categorie(c.getInt(3));
        type = recup_type(c.getInt(4));
        principale.setText(recup_muscle_p(c.getInt(0)));
        secondaire.setText(recup_muscle_s(c.getInt(0)));

        categories.setText(categorie);
        type_exo.setText(type);
        db.close();

    }
    private String recup_id_exo(){
        Intent intent = getIntent();
        String id;
        if(intent.hasExtra("exo_id"))
            id = intent.getStringExtra("exo_id");
        else
            id = "";
        return id;
    }
    private String recup_type(int id_type){
        String test="Aucun";
        db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAll_donnee_type();
        if(c.moveToFirst())
            do {
                if(c.getInt(0) == id_type)
                    test = c.getString(1);
            }while(c.moveToNext());
        db.close();
        return test;
    }
    private String recup_categorie(int id_categorie){
        String test="Aucun";
        db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAll_donnee_categorie();
        if(c.moveToFirst())
            do {
                if(c.getInt(0) == id_categorie)
                    test = c.getString(1);
            }while(c.moveToNext());
        db.close();
        return test;
    }
    private String recup_muscle_p(int id_exo){
        String stock="";
        db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAll_donne_muscle_principaux_secondaire();
        if(c.moveToFirst())
            do {
                if(c.getInt(0) == id_exo && c.getInt(2) == 0)
                    stock = stock + " " + recup_muscle_texte(c.getInt(1));
            }while(c.moveToNext());
        db.close();
        return stock;
    }
    private String recup_muscle_s(int id_exo){
        String stock="";
        db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAll_donne_muscle_principaux_secondaire();
        if(c.moveToFirst())
            do {
                if(c.getInt(0) == id_exo && c.getInt(2) == 1)
                    stock = stock + "  " + recup_muscle_texte(c.getInt(1));
            }while(c.moveToNext());
        db.close();
        return stock;
    }
    private String recup_muscle_texte(int id){
        String stock="";
        db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAll_donne_muscle();
        if(c.moveToFirst())
            do {
                if(id == c.getInt(0))
                    stock = c.getString(1);
            }while (c.moveToNext());
        db.close();
        return stock;
    }
    private String recup_id(){
        Intent intent = this.getIntent();
        String id;
        if(intent.hasExtra("id"))
            id = intent.getStringExtra("id");
        else
            id = "";
        return id;
    }
}
