package com.example.maoak.app_sport;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Mon_profil extends Fragment {
    TextView numero_utilisateur;
    TextView login;
    TextView nom;
    TextView prenom;
    TextView taille;
    TextView poids;
    TextView mail;
    TextView mdp;
    TextView id_profil;
    DBAdapter db;
    Button modif;

    String[] info;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //RECUPERATION DE LA VIEW
        View v = inflater.inflate(R.layout.mon_profil, container, false);

        numero_utilisateur = (TextView) v.findViewById(R.id.textView30);
        login = (TextView) v.findViewById(R.id.textView31);
        nom = (TextView) v.findViewById(R.id.textView32);
        prenom = (TextView) v.findViewById(R.id.textView33);
        taille = (TextView) v.findViewById(R.id.textView34);
        poids = (TextView) v.findViewById(R.id.textView35);
        mail = (TextView) v.findViewById(R.id.textView37);
        mdp = (TextView) v.findViewById(R.id.textView38);
        id_profil = (TextView) v.findViewById(R.id.textView30);
        modif = (Button) v.findViewById(R.id.button9);
        modif.setOnClickListener(boutonModif);

        remplir_champ();


        return v;
    }

    private View.OnClickListener boutonModif = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            File path = getActivity().getFilesDir();
            String id = recup_id();
            Intent intent = new Intent(getActivity(),Modif_profil.class);
            Intent intent1 = new Intent(getActivity(),test_fragment.class);
            intent1.putExtra("id",id);
            intent.putExtra("id",id);
            startActivity(intent);
        }
    };


    public void remplir_champ(){
        db = new DBAdapter(getActivity());
        db.open();
        String mdpp ="";
        int id = Integer.parseInt(recup_id());
        Cursor c = db.get_donne_profil(id);
        id_profil.setText(recup_id());
        login.setText(c.getString(1));
        for(int i=0;i<c.getString(2).length();i++)
            mdpp+="*";
        mdp.setText(mdpp);
        nom.setText(c.getString(3));
        prenom.setText(c.getString(4));
        taille.setText(c.getString(5) +" m");
        poids.setText(c.getString(6) +" kg");
        mail.setText(c.getString(7));
       db.close();

    }
    private String recup_id(){
        Intent intent = getActivity().getIntent();
        String id;
        if(intent.hasExtra("id"))
            id = intent.getStringExtra("id");
        else
            id = "";
        return id;
    }

}
