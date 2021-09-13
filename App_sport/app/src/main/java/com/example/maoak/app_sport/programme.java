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

public class programme extends Fragment {
    Button add_exo;
    ListView list_exercice;
    ArrayList<ArrayList<String>> list_exo2 = new ArrayList<>();
    int pos;
    Intent intent;
    DBAdapter db;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.programme, container, false);
        add_exo = (Button)v.findViewById(R.id.button7);
        list_exercice = (ListView) v.findViewById(R.id.listview);
        intent = new Intent(v.getContext(),Programme_exo.class);

        //LIST VIEW CUSTOM
        //==================================================================================================

        list_exo2 = recup_exo2();
        programme.CustomAdapter arraycustom = new programme.CustomAdapter();

        //==================================================================================================

        list_exercice.setAdapter(arraycustom);

        list_exercice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                pos++;
                intent.putExtra("id",recup_id());
                intent.putExtra("position",""+pos);
                startActivity(intent);
            }
        });
        add_exo.setOnClickListener(bouton_add_exo);
        return v;
    }
    private View.OnClickListener bouton_add_exo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent=new Intent(v.getContext(),Init_prog.class);
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
        Cursor c = db.getAll_donne_programme_init();

        if(c.moveToFirst()){
            do{
                stock = new ArrayList<>();
                stock.add(c.getString(0));
                stock.add(c.getString(1));
                test.add(stock);
            }while(c.moveToNext());
        }
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
            convertView = getLayoutInflater().inflate(R.layout.customlayout2,null);
            TextView nom_exo = (TextView)convertView.findViewById(R.id.nom_prog);

            nom_exo.setText(list_exo2.get(i).get(1));


            return convertView;
        }
    }
}
