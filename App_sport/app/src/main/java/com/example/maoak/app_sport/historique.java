package com.example.maoak.app_sport;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class historique extends Fragment {
    ListView list_exercice;
    FloatingActionButton refresh;
    ArrayList<ArrayList<String>> list_exo2 = new ArrayList<>();
    DBAdapter db;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.historique, container, false);
        refresh = v.findViewById(R.id.refresh);
        list_exercice = (ListView) v.findViewById(R.id.listview);
        //LIST VIEW CUSTOM
        //==================================================================================================

        list_exo2 = recup_exo2();
        CustomAdapter arraycustom = new CustomAdapter();

        //==================================================================================================
        list_exercice.setAdapter(arraycustom);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_exo2 = recup_exo2();
                CustomAdapter arraycustom = new CustomAdapter();
                list_exercice.setAdapter(arraycustom);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf.format(new Date());
                System.out.println("Date : "+date);
            }
        });


        return v;
    }
    private ArrayList<ArrayList<String>> recup_exo2(){
        ArrayList<ArrayList<String>> test=new ArrayList<>();
        ArrayList<String> stock;
        db = new DBAdapter(getContext());
        db.open();
        Cursor c = db.getAll_donne_historique();
        if(c.moveToFirst()){
            do {
                stock = new ArrayList<>();
                stock.add(c.getString(0));
                stock.add(c.getString(1));
                stock.add(c.getString(2));
                stock.add(c.getString(3));
                stock.add(c.getString(4));
                test.add(stock);
            }while (c.moveToNext());
        }
        db.close();
        return test;
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
            convertView = getLayoutInflater().inflate(R.layout.customlayout_historique,null);
            TextView nom_prog = (TextView)convertView.findViewById(R.id.nom_prog);
            TextView nom_exo = convertView.findViewById(R.id.nom_exo);
            TextView date = convertView.findViewById(R.id.date);

            db = new DBAdapter(convertView.getContext());
            db.open();
            Cursor c = db.get_init_prog(Long.parseLong(list_exo2.get(i).get(0)));
            Cursor c1 = db.get_donne_exo(Long.parseLong(list_exo2.get(i).get(1)));
            nom_prog.setText(c.getString(0));
            nom_exo.setText(c1.getString(1));
            date.setText(list_exo2.get(i).get(4));
            db.close();





            return convertView;
        }
    }
}
