package com.example.maoak.app_sport;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Muscle extends ListActivity {

    ArrayList<String> muscle2 = new ArrayList<>();
    DBAdapter db;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        muscle2 = recup_muscle();
        setContentView(R.layout.muscle);
        ListView lstView = getListView();
        lstView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        lstView.setTextFilterEnabled(true);

        setListAdapter(new ArrayAdapter(this,
                android.R.layout.simple_list_item_checked, muscle2));
    }


    public void onListItemClick(
            ListView parent, View v, int position, long id)
    {
        Toast.makeText(this,
                "You have selected " + recup_id_exo(),
                Toast.LENGTH_SHORT).show();
    }

    public void onClick(View view) {
        ListView lstView = getListView();
        String stock0;
        String stock1;
        String stock2;
        db = new DBAdapter(this);
        for (int i=0; i<lstView.getCount(); i++) {
            if (lstView.isItemChecked(i)) {
                stock0 = recup_id_exo();
                stock1  = recup_muscle_id(""+lstView.getItemAtPosition(i));
                stock2 = recup_etat();
                db.open();
                if(Integer.parseInt(recup_etat_muscle()) == 0)
                    db.insert_donne_principale_secondaire(new String[]{stock0,stock1,stock2});
                if(Integer.parseInt(recup_etat_muscle()) == 1)
                    db.delete_donne_muscle_principaux_secondaire(Integer.parseInt(recup_id_exo()),Integer.parseInt(stock1));
                db.close();
            }
        }

        Intent intent = new Intent(this,Exercice_add.class);
        intent.putExtra("id",recup_id());
        intent.putExtra("exo_id",recup_id_exo());
        startActivity(intent);
        finish();
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
    private String recup_etat(){
        Intent intent = getIntent();
        String id;

        if(intent.hasExtra("etat"))
            id = intent.getStringExtra("etat");
        else
            id = "";
        return id;
    }
    private ArrayList recup_muscle(){
        ArrayList<String> musclee = new ArrayList<>();
        int id=Integer.parseInt(recup_id_exo());
        db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAll_donne_muscle();
        if(c.moveToFirst())
            do {

                if(verif_muscle_list(c.getInt(0)))
                    musclee.add(c.getString(1));
            }while(c.moveToNext());

        db.close();
        return musclee;
    }

    private String recup_muscle_id(String nom){

        String stock="";
        db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAll_donne_muscle();
        if(c.moveToFirst())
            do {
                if(nom.equals(c.getString(1))) {
                    stock = c.getString(0);
                }
            }while (c.moveToNext());
        db.close();
        return stock;
    }
    private boolean verif_muscle_list(int id){
        boolean verif = false;
        if(Integer.parseInt(recup_etat_muscle()) == 0)
            verif = true;
        if(Integer.parseInt(recup_etat_muscle()) == 1)
            verif = false;
        db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAll_donne_muscle_principaux_secondaire();
        if(c.moveToFirst())
            do {
                if(id == c.getInt(1) && Integer.parseInt(recup_id_exo()) == c.getInt(0) && Integer.parseInt(recup_etat_muscle()) == 0)
                    verif = false;
                else if(id == c.getInt(1) && Integer.parseInt(recup_id_exo()) == c.getInt(0) && Integer.parseInt(recup_etat_muscle()) == 1 && Integer.parseInt(recup_etat()) == c.getInt(2))
                    verif = true;
            }while (c.moveToNext());
        db.close();

        return verif;
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
    private String recup_etat_muscle(){
        Intent intent = this.getIntent();
        String id;
        if(intent.hasExtra("etat_muscle"))
            id = intent.getStringExtra("etat_muscle");
        else
            id = "";
        return id;
    }
}
