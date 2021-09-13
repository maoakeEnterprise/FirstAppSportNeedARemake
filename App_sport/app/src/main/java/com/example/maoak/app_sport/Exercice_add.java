package com.example.maoak.app_sport;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Exercice_add extends AppCompatActivity {
    EditText nom_exo;
    EditText descriptif;
    TextView principale;
    TextView secondaire;
    RadioButton base;
    RadioButton avance;
    RadioButton finition;

    Spinner s1;
    ArrayAdapter<ArrayList<String>> adapter;
    int index=0;
    DBAdapter db;
    Intent intent;

    String[] type_exo = new String[10];
    ArrayList<String> type_exo2 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercice_add);
        nom_exo = (EditText)findViewById(R.id.editText18);
        descriptif = (EditText)findViewById(R.id.editText17);
        principale = (TextView)findViewById(R.id.editText16);
        secondaire = (TextView)findViewById(R.id.editText15);
        remplir_champ();

        base = findViewById(R.id.radioButton);
        avance = findViewById(R.id.radioButton2);
        finition = findViewById(R.id.radioButton3);

        db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAll_donnee_type();
        if(c.moveToFirst())
            do {
                type_exo2.add(c.getString(1));
            }while(c.moveToNext());
        db.close();
        s1 = (Spinner)findViewById(R.id.spinner);
        adapter = new ArrayAdapter(this,
                R.layout.spinner_item, type_exo2);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        s1.setAdapter(adapter);
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1, int arg2, long arg3)
            {
                index = arg0.getSelectedItemPosition()+1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });
    }
    public void onclick_modifier_exo(View v){
        String[] donnnee = new String[]{
                nom_exo.getText().toString(),
                descriptif.getText().toString(),
                recup_radiobutton_value(),
                ""+index
        };
        int id = Integer.parseInt(recup_id_exo());
        db = new DBAdapter(this);
        db.open();
        db.update_donnee_exo(id,donnnee);
        db.close();
        intent = new Intent(this,test_fragment.class);
        intent.putExtra("id",recup_id());
        startActivity(intent);
        finish();
    }
    public void onclick_modifier_p(View v){
        intent = new Intent(this,Muscle.class);
        intent.putExtra("exo_id",recup_id_exo());
        intent.putExtra("etat_muscle","0");
        intent.putExtra("etat",Integer.toString(0));
        intent.putExtra("id",recup_id());
        startActivity(intent);

        finish();
    }
    public void onclick_modifier_s(View v){
        intent = new Intent(this,Muscle.class);
        intent.putExtra("exo_id",recup_id_exo());
        intent.putExtra("etat_muscle","0");
        intent.putExtra("etat",Integer.toString(1));
        intent.putExtra("id",recup_id());
        startActivity(intent);

        finish();
    }
    public void onclick_supp_p(View v){
        intent = new Intent(this,Muscle.class);
        intent.putExtra("exo_id",recup_id_exo());
        intent.putExtra("etat_muscle","1");
        intent.putExtra("etat",Integer.toString(0));
        intent.putExtra("id",recup_id());
        startActivity(intent);

        finish();
    }
    public void onclick_supp_s(View v){
        intent = new Intent(this,Muscle.class);
        intent.putExtra("exo_id",recup_id_exo());
        intent.putExtra("etat_muscle","1");
        intent.putExtra("etat",Integer.toString(1));
        intent.putExtra("id",recup_id());
        startActivity(intent);

        finish();
    }

    public void remplir_champ(){


        int id= Integer.parseInt(recup_id_exo());
        db = new DBAdapter(this);
        db.open();
        Cursor c = db.get_donne_exo(id);
        nom_exo.setText(c.getString(1));
        descriptif.setText(c.getString(2));
        principale.setText(recup_muscle_p(c.getInt(0)));
        secondaire.setText(recup_muscle_s(c.getInt(0)));
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
    private String recup_radiobutton_value(){
        String value = "";
        if(base.isChecked())
            value = "1";
        if(avance.isChecked())
            value = "2";
        if(finition.isChecked())
            value = "3";

        return  value;
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
