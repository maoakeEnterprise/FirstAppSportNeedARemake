package com.example.maoak.app_sport;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Modif_profil extends AppCompatActivity {
    EditText login;
    EditText nom;
    EditText prenom;
    EditText taille;
    EditText poids;
    EditText mail;
    EditText mdp;
    EditText mdp_confirm;
    Button valider;
    FloatingActionButton camera;
    FloatingActionButton save;
    ImageView photo;
    DBAdapter db;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modif_profil);
        login = (EditText)findViewById(R.id.editText2);
        nom = (EditText)findViewById(R.id.editText4);
        prenom = (EditText)findViewById(R.id.editText7);
        taille = (EditText)findViewById(R.id.editText8);
        poids = (EditText)findViewById(R.id.editText9);
        mail = (EditText)findViewById(R.id.editText12);
        mdp = (EditText)findViewById(R.id.editText10);
        mdp_confirm = (EditText)findViewById(R.id.editText11);
        camera = findViewById(R.id.btncamera);
        save = findViewById(R.id.btnsave);
        photo = findViewById(R.id.imageView2);
        valider = (Button)findViewById(R.id.button);

        remplir_champ();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"profil","description");
            }
        });

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        bitmap = (Bitmap)data.getExtras().get("data");
        photo.setImageBitmap(bitmap);
    }
    public void onclick_update(View v){


        int id = Integer.parseInt(recup_id());
        if(verif()){
            db = new DBAdapter(this);
            db.open();
            db.update_donnee_profil(id, login.getText().toString(),
                    mdp.getText().toString(),
                    nom.getText().toString(),
                    prenom.getText().toString(),
                    taille.getText().toString(),
                    poids.getText().toString(),
                    mail.getText().toString());
            db.close();
            Intent intent = new Intent(this,test_fragment.class);;
            intent.putExtra("id",recup_id());
            startActivity(intent);
            finish();

        }

    }
    public boolean verif(){
        boolean etat = false;
        if(!(mdp.getText().toString().isEmpty()) && !(mdp_confirm.getText().toString().isEmpty()) && !(login.getText().toString().isEmpty()))
            if(mdp.getText().toString().equals(mdp_confirm.getText().toString()) && verif_new_login(login.getText().toString()))
                etat =true;

        return  etat;
    }

    public void remplir_champ(){
        db = new DBAdapter(this);
        db.open();
        int id = Integer.parseInt(recup_id());
        Cursor c = db.get_donne_profil(id);
        login.setText(c.getString(1));
        mdp.setText(c.getString(2));
        nom.setText(c.getString(3));
        prenom.setText(c.getString(4));
        taille.setText(c.getString(5));
        poids.setText(c.getString(6));
        mail.setText(c.getString(7));
        db.close();

    }
    private String recup_id(){
        Intent intent = getIntent();
        String id;
        if(intent.hasExtra("id"))
            id = intent.getStringExtra("id");
        else
            id = "";
        return id;
    }
    private void print_information(String text){
        Intent data = new Intent();
        data.setData(Uri.parse(text));
        Toast.makeText(this, data.getData().toString(),Toast.LENGTH_SHORT).show();
    }
    private boolean verif_new_login(String pseudo){
        boolean etat=true;
        int id = Integer.parseInt(recup_id());
        db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAll_donnee_profil();
        if(c.moveToFirst()){
            do{
                if(pseudo.equals(c.getString(1)) && id != c.getInt(0)){
                    etat = false;
                }
            }while(c.moveToNext());
        }
        db.close();
        return etat;
    }
    private void debug(Cursor c){
        if(c.moveToFirst()){
            do{


                print_information(
                        "LOGIN: " + c.getString(1)+
                                " MDP: "+c.getString(2)
                );


            }while(c.moveToNext());
        }
    }
}
