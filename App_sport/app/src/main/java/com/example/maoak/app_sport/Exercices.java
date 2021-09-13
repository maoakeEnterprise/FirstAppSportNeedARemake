package com.example.maoak.app_sport;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Exercices extends Fragment {
    ListView list_exercice;
    ArrayList<ArrayList<String>> list_exo2 = new ArrayList<>();
    int pos;
    Intent intent;
    DBAdapter db;
    Button add_exo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.exercices, container, false);
        db = new DBAdapter(v.getContext());
        intent = new Intent(v.getContext(),Exercices_read.class);
        list_exercice = (ListView) v.findViewById(R.id.listview);
        add_exo = (Button)v.findViewById(R.id.button2);

        //LIST VIEW CUSTOM
        //==================================================================================================

        list_exo2 = recup_exo2();
        CustomAdapter arraycustom = new CustomAdapter();

        //==================================================================================================

        list_exercice.setAdapter(arraycustom);

        list_exercice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                System.out.println("======================");
                pos++;
                System.out.println(pos);
                intent.putExtra("exo_id",""+(pos));
                intent.putExtra("id",recup_id());
                startActivity(intent);
                getActivity().finish();
            }
        });


        add_exo.setOnClickListener(bouton_add_exo);


        return v;
    }

    private View.OnClickListener bouton_add_exo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String[] donnee = new String[]{
                    "EXERCICE ",
                    " ",
                    "0",
                    "0"
            };
            db = new DBAdapter(getActivity());
            db.open();
            db.insert_donne_exo(donnee);
            db.close();
            list_exo2 = recup_exo2();
            CustomAdapter arraycustom = new CustomAdapter();
            list_exercice.setAdapter(arraycustom);
            pos = list_exo2.size();
            System.out.println("======================");
            System.out.println(pos);
            intent.putExtra("exo_id",""+(pos));
            intent.putExtra("id",recup_id());
            startActivity(intent);
            getActivity().finish();
        }
    };
    private ArrayList recup_exo2(){
        ArrayList<ArrayList<String>> test = new ArrayList<>();
        ArrayList<String> stock;
        db = new DBAdapter(getActivity());
        db.open();
        Cursor c = db.getAll_donnee_exo();

        if(c.moveToFirst()){
            do{
                stock = new ArrayList<>();
                stock.add(c.getString(1));
                stock.add(recup_categorie(c.getInt(3)));
                stock.add(recup_type(c.getInt(4)));

                test.add(stock);


            }while(c.moveToNext());
        }
        db.close();
        return test;
    }
    private String recup_type(int id_type){
        String test="Aucun type";
        db = new DBAdapter(getActivity());
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
        String test="Aucune Categorie";
        db = new DBAdapter(getActivity());
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
    private String recup_id(){
        Intent intent = getActivity().getIntent();
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
            convertView = getLayoutInflater().inflate(R.layout.customlayout,null);
            TextView nom_exo = (TextView)convertView.findViewById(R.id.nom_prog);
            TextView categ = (TextView)convertView.findViewById(R.id.textView51);
            TextView type = (TextView)convertView.findViewById(R.id.textView52);

            nom_exo.setText(list_exo2.get(i).get(0));
            categ.setText(list_exo2.get(i).get(1));
            type.setText(list_exo2.get(i).get(2));


            return convertView;
        }
    }
}

