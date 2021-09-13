package com.example.maoak.app_sport;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;

import java.io.FileOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class SecondActivity extends AppCompatActivity {

    Button create_confirme;
    EditText login;
    EditText password;
    EditText confirmpassword;
    String[] donnee;
    DBAdapter db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        DBAdapter db = new DBAdapter(this);
        create_confirme = (Button)findViewById(R.id.creation_confirmer);
        login = (EditText)findViewById(R.id.editText3);
        password = (EditText)findViewById(R.id.editText5);
        confirmpassword = (EditText)findViewById(R.id.editText6);
        //initialisation de la base de donnee
        db.open();
        //debug(db.get_donne_profil(1));
        db.close();
    }

    public void onclick_logtt(View v){
        Button button = (Button)v;
        db= new DBAdapter(this);
        boolean etat=false;
        boolean verif_pseudo=false;
        boolean verif_code=false;

        if(button.equals(create_confirme)){
            etat = verif_champ();
            //delete_account();
            /*db = new DBAdapter(this);
            db.open();
            db.delete_drop();
            db.close();*/
        }
        if(etat) {
            db.open();
            Cursor c = db.getAll_donnee_profil();
            verif_pseudo = verif_new_login(login.getText().toString(),c);
            db.close();
        }

        if(verif_pseudo)
            verif_code = verif_code(password.getText().toString(),confirmpassword.getText().toString());

        if(verif_code) {

            db.open();
            long id=db.insert_donne_profil(login.getText().toString(),password.getText().toString(), " ", " ", " ", " ", " ");
            db.close();
            db.open();
            Cursor c = db.getAll_donnee_profil();
            debug(c);
            db.close();
            finish();

        }
    }
    private boolean verif_champ(){
        boolean  etat = false;
        if(     !(login.getText().toString().equals("")) &&
                !(password.getText().toString().equals("")) &&
                !(confirmpassword.getText().toString().equals("")))
            etat = true;
        else {
            print_information("Un/Des champ(s) n' / ne est/sont pas rempli");
        }
        return etat;
    }
    private boolean verif_new_login(String pseudo,Cursor c){
        boolean etat=true;
        if(c.moveToFirst()){
            do{
                if(pseudo.equals(c.getString(1))){
                    etat=false;
                    print_information("LOGIN DEJA PRIS");
                }
            }while(c.moveToNext());
        }
        return etat;
    }
    private boolean verif_code(String code1, String code2){
        boolean etat=false;
        if(code1.equals(code2))
            etat=true;
        else
            print_information("Code incorrect");
        return  etat;
    }
    private void print_information(String text){
        Intent data = new Intent();
        data.setData(Uri.parse(text));
        Toast.makeText(this, data.getData().toString(),
                Toast.LENGTH_SHORT).show();
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
