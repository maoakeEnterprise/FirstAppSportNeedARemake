package com.example.maoak.app_sport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class Muscle_exo extends AppCompatActivity {
    Intent intent;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muscle_exo);
        intent = new Intent(new Intent(this,Muscle_choisis.class));
    }

    public void muscle_Triceps(View v){
        intent.putExtra("id",recup_id());
        intent.putExtra("position",recup_pos());
        intent.putExtra("muscle","Triceps");
        startActivity(intent);
        finish();

    }
    public void muscle_Poitrine(View v){
        intent.putExtra("id",recup_id());
        intent.putExtra("position",recup_pos());
        intent.putExtra("muscle","Poitrine");
        startActivity(intent);
        finish();

    }
    public void muscle_Epaule(View v){
        intent.putExtra("id",recup_id());
        intent.putExtra("position",recup_pos());
        intent.putExtra("muscle","Epaule");
        startActivity(intent);
        finish();

    }
    public void muscle_Biceps(View v){
        intent.putExtra("id",recup_id());
        intent.putExtra("position",recup_pos());
        intent.putExtra("muscle","Biceps");
        startActivity(intent);
        finish();

    }
    public void muscle_Abdos(View v){
        intent.putExtra("id",recup_id());
        intent.putExtra("position",recup_pos());
        intent.putExtra("muscle","Abdos");
        startActivity(intent);
        finish();

    }
    public void muscle_Dos(View v){
        intent.putExtra("id",recup_id());
        intent.putExtra("position",recup_pos());
        intent.putExtra("muscle","Dos");
        startActivity(intent);
        finish();

    }
    public void muscle_Avantbras(View v){
        intent.putExtra("id",recup_id());
        intent.putExtra("position",recup_pos());
        intent.putExtra("muscle","Avant-bras");
        startActivity(intent);
        finish();

    }
    public void muscle_Cuisses(View v){
        intent.putExtra("id",recup_id());
        intent.putExtra("position",recup_pos());
        intent.putExtra("muscle","Cuisses");
        startActivity(intent);
        finish();

    }
    public void muscle_Fessiers(View v){
        intent.putExtra("id",recup_id());
        intent.putExtra("position",recup_pos());
        intent.putExtra("muscle","Fessiers");
        startActivity(intent);
        finish();

    }
    public void muscle_Jambe(View v){
        intent.putExtra("id",recup_id());
        intent.putExtra("position",recup_pos());
        intent.putExtra("muscle","Jambe");
        startActivity(intent);
        finish();

    }
    public void muscle_ALL(View v){
        intent.putExtra("id",recup_id());
        intent.putExtra("position",recup_pos());
        intent.putExtra("muscle","ALL");
        startActivity(intent);
        finish();

    }
    public void muscle_Retour(View v){
        intent = new Intent(v.getContext(),test_fragment.class);
        intent.putExtra("id",recup_id());
        intent.putExtra("position",recup_pos());
        startActivity(intent);
        finish();
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
    public String recup_pos(){
        String chiffre ="";
        Intent intent = getIntent();
        if(intent.hasExtra("position")){
            chiffre = intent.getStringExtra("position");
        }
        return chiffre;
    }

}
