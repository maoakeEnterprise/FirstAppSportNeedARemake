package com.example.maoak.app_sport;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Programme_exo extends AppCompatActivity {
    TextView nom;
    ListView list_exercice;
    ArrayList<ArrayList<String>> list_exo2 = new ArrayList<>();
    DBAdapter db;
    Intent intent;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.programme_exo);
        nom = findViewById(R.id.textView);
        list_exercice = findViewById(R.id.listview);
        db = new DBAdapter(this);
        db.open();
        Cursor c =db.getAll_donne_programme_init();
        if(c.moveToFirst())
            do {
                if(recup_pos().equals(c.getString(0)))
                    nom.setText(c.getString(1));

            }while (c.moveToNext());
        db.close();

        //LIST VIEW CUSTOM
        //==================================================================================================

        list_exo2 = recup_exo2();
        CustomAdapter arraycustom = new CustomAdapter();

        //==================================================================================================

        list_exercice.setAdapter(arraycustom);

        list_exercice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(view.getContext(),Serie_exo.class);
                int pos = position;
                /*System.out.println("size : "+list_exo2.get(pos).size());
                System.out.println("nom exo : "+list_exo2.get(pos).get(0));
                System.out.println("temps recuperation : "+list_exo2.get(pos).get(1));
                System.out.println("id exo : "+list_exo2.get(pos).get(2));*/
                intent.putExtra("id",recup_id());
                intent.putExtra("position",recup_pos());
                intent.putExtra("id_exo",""+list_exo2.get(pos).get(2));
                intent.putExtra("nom_exo",""+list_exo2.get(pos).get(0));
                startActivity(intent);
                finish();
            }
        });
    }

    public ArrayList<ArrayList<String>> recup_exo2(){
        ArrayList<ArrayList<String>> test=new ArrayList<>();
        ArrayList<String> stock;
        ArrayList<ArrayList<String>> exo_programme=new ArrayList<>();
        int pos = Integer.parseInt(recup_pos());

        db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAll_donne_programme();
        Cursor c2 = db.getAll_donnee_exo();
        if(c.moveToFirst())
            do {
                if(c.getInt(0) == pos){
                    stock = new ArrayList<>();
                    stock.add(c.getString(1));
                    stock.add(c.getString(2));
                    exo_programme.add(stock);
                }

            }while (c.moveToNext());
        for(int i=0;i<exo_programme.size();i++){
            if(c2.moveToFirst())
                do {

                        if(exo_programme.get(i).get(0).equals(c2.getString(0))){
                            stock = new ArrayList<>();
                            stock.add(c2.getString(1));
                            stock.add(exo_programme.get(i).get(1));
                            stock.add(c2.getString(0));
                            test.add(stock);
                        }

                }while (c2.moveToNext());
        }
        db.close();
        return test;
    }

    public void add_exo(View v){
        intent = new Intent(v.getContext(),Muscle_exo.class);
        intent.putExtra("id",recup_id());
        intent.putExtra("position",recup_pos());
        startActivity(intent);
        finish();
    }
    public String recup_pos(){
        String chiffre ="";
        Intent intent = getIntent();
        if(intent.hasExtra("position")){
            chiffre = intent.getStringExtra("position");
        }
        return chiffre;
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

    public class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list_exo2.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.customlayout3,null);
            TextView nom_exo = (TextView)convertView.findViewById(R.id.nom_prog);
            TextView temps_recuperation = (TextView)convertView.findViewById(R.id.textView51);

            nom_exo.setText(list_exo2.get(i).get(0));
            temps_recuperation.setText(list_exo2.get(i).get(1)+" s");


            return convertView;
        }
    }
}
