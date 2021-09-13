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

public class Muscle_choisis extends AppCompatActivity {
    ListView list_exercice;
    ArrayList<ArrayList<String>> list_exo2 = new ArrayList<>();
    int pos;
    Intent intent;
    DBAdapter db = new DBAdapter(this);
    String info;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muscle_choisis);
        list_exercice = (ListView)findViewById(R.id.listview);
        recup_info();

        //LIST VIEW CUSTOM
        //==================================================================================================

        list_exo2 = recup_exo2();
        CustomAdapter arraycustom = new CustomAdapter();
        //recup_exo2();
        //==================================================================================================

        list_exercice.setAdapter(arraycustom);

        list_exercice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                intent = new Intent(view.getContext(),Programme_exo.class);
                intent.putExtra("id",recup_id());
                intent.putExtra("position",recup_pos());
                db = new DBAdapter(view.getContext());
                db.open();
                String[] donnee = new String[]{ recup_pos() , list_exo2.get(pos).get(0) , "60"};
               //System.out.println(list_exo2.get(pos).get(0));
                db.insert_donne_programme(donnee);
                db.close();
                startActivity(intent);
                finish();
            }
        });
    }
    public ArrayList recup_exo2(){
        ArrayList<ArrayList<String>> muscle = recup_muscle();
        ArrayList<ArrayList<Integer>> muscle_exo = recup_muscle_exo();
        db = new DBAdapter(this);
        ArrayList<ArrayList<String>> test = new ArrayList<>();
        ArrayList<String> stock;
        int id_muscle=-1;
        db.open();
        Cursor c = db.getAll_donnee_exo();
        for(int i=0;i<muscle.size();i++){
            System.out.println(muscle.get(i).get(1));
            if(info.equals(muscle.get(i).get(1))){
                //System.out.println("info : "+info);
                id_muscle = Integer.parseInt(muscle.get(i).get(0));
            }
        }

        if(c.moveToFirst()){
            do{
                stock = new ArrayList<>();
                for(int i=0;i<muscle_exo.size();i++){
                    //System.out.println("id_exo : "+c.getInt(0));
                    //System.out.println("id_exo_tab : "+ muscle_exo.get(i).get(0));
                    //System.out.println("id_muscle : "+id_muscle);
                    //System.out.println("id_muscle_tab : "+muscle_exo.get(i).get(1));
                    if(c.getInt(0) == muscle_exo.get(i).get(0) && id_muscle == muscle_exo.get(i).get(1)) {
                        stock.add(c.getString(0));
                        stock.add(c.getString(1));
                    }
                    else if(info.equals("ALL")){
                        stock.add(c.getString(0));
                        stock.add(c.getString(1));
                    }
                }
                if(stock.size()>0)
                 test.add(stock);


            }while(c.moveToNext());
        }
        db.close();
        return test;
    }
    public ArrayList recup_muscle_exo(){
        db = new DBAdapter(this);
        ArrayList<ArrayList<Integer>> test = new ArrayList<>();
        ArrayList<Integer> stock;
        db.open();

        Cursor c = db.getAll_donne_muscle_principaux_secondaire();
        if(c.moveToFirst()){
            do{
                if(c.getInt(2) == 0){
                    stock = new ArrayList<>();
                    stock.add(c.getInt(0));
                    stock.add(c.getInt(1));
                    test.add(stock);
                }
            }while(c.moveToNext());
        }
        db.close();

        return test;
    }
    public ArrayList recup_muscle(){
        db = new DBAdapter(this);
        ArrayList<ArrayList<String>> test = new ArrayList<>();
        ArrayList<String> stock;
        db.open();
        Cursor c = db.getAll_donne_muscle();
        if(c.moveToFirst()){
            do {
                stock = new ArrayList<>();
                stock.add(c.getString(0));
                stock.add(c.getString(1));
                test.add(stock);
            }while (c.moveToNext());
        }
        db.close();
        return test;
    }

    public void recup_info(){
        Intent intent = getIntent();
        if(intent.hasExtra("muscle"))
            info = intent.getStringExtra("muscle");
        else
            info="";
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
            convertView = getLayoutInflater().inflate(R.layout.customlayout2,null);
            TextView nom_exo = (TextView)convertView.findViewById(R.id.nom_prog);

            nom_exo.setText(list_exo2.get(i).get(1));


            return convertView;
        }

    }


}
