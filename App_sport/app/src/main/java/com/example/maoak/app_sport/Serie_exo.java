package com.example.maoak.app_sport;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Serie_exo extends AppCompatActivity {
        FloatingActionButton add_serie;
        FloatingActionButton supp_serie;
        FloatingActionButton save_time;
        TextView temps;
        TextView nom_exo;
        TextView timer_count;
        DBAdapter db;
        int post_item = 0;

    ListView list_exercice;
    ArrayList<ArrayList<String>> list_exo2 = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serie_exo);
        add_serie = findViewById(R.id.button1);
        supp_serie = findViewById(R.id.button2);
        nom_exo = findViewById(R.id.textView);
        nom_exo.setText(recup_nom_exo());
        list_exercice = findViewById(R.id.listview);
        save_time = findViewById(R.id.timer);
        temps = findViewById(R.id.time);
        timer_count = findViewById(R.id.timer_count);
        db = new DBAdapter(this);
        db.open();
        Cursor c = db.get_donne_prog(Long.parseLong(recup_pos()),Long.parseLong(recup_id_exo()));
        temps.setText(""+c.getString(2));

        db.close();
        //ACTION SUR LES LES DEUX BOUTONS
        add_serie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int countdown=1;

                log("ajouter_serie");
                db = new DBAdapter(v.getContext());
                db.open();
                Cursor c  = db.getAll_donne_seri_prog();
                if(c.moveToFirst())
                    do {
                        if(recup_pos().equals(c.getString(1)) && recup_id_exo().equals(c.getString(2)))
                            countdown++;
                    }while (c.moveToNext());
                String[] donnee = new String[]{""+countdown,recup_pos(),recup_id_exo(),"0","0"};
                db.insert_donne_serie(donnee);
                db.close();
                list_exo2 = recup_exo2();
                CustomAdapter arraycustom = new CustomAdapter();
                list_exercice.setAdapter(arraycustom);
            }
        });
        supp_serie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int countdown=1;
                db = new DBAdapter(v.getContext());
                db.open();
                Cursor c  = db.getAll_donne_seri_prog();
                if(c.moveToFirst())
                    do {
                        if(recup_pos().equals(c.getString(1)) && recup_id_exo().equals(c.getString(2)))
                            countdown++;
                    }while (c.moveToNext());
                countdown--;
                db.delete_donne_serie(Long.parseLong(""+countdown),Long.parseLong(recup_pos()),Long.parseLong(recup_id_exo()));
                db.close();
                log("count : " + countdown);
                list_exo2 = recup_exo2();
                CustomAdapter arraycustom = new CustomAdapter();
                list_exercice.setAdapter(arraycustom);
            }
        });
        //UPDATE LE TEMPS
        save_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new DBAdapter(v.getContext());
                db.open();
                String[] donnee = new String[]{""+temps.getText()};
                if(!(temps.getText().toString().equals("")))
                    db.update_donne_prog(donnee,Long.parseLong(recup_pos()),Long.parseLong(recup_id_exo()));
                else
                    print_information("Champ non rempli");
                db.close();
                list_exo2 = recup_exo2();
                CustomAdapter arraycustom = new CustomAdapter();
                list_exercice.setAdapter(arraycustom);
            }
        });

        //LIST VIEW CUSTOM
        //==================================================================================================

        list_exo2 = recup_exo2();
        CustomAdapter arraycustom = new CustomAdapter();

        //==================================================================================================

        list_exercice.setAdapter(arraycustom);

        list_exercice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
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
    private String recup_id_exo(){
        Intent intent = getIntent();
        String id_exo="";
        if(intent.hasExtra("id_exo"))
            id_exo = intent.getStringExtra("id_exo");
        return id_exo;
    }
    private String recup_nom_exo(){
        Intent intent = getIntent();
        String nom_exo="";
        if(intent.hasExtra("nom_exo"))
            nom_exo = intent.getStringExtra("nom_exo");
        return nom_exo;
    }
    private ArrayList<ArrayList<String>> recup_exo2(){
        ArrayList<ArrayList<String>> test = new ArrayList<>();
        ArrayList<String> stock;
        db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAll_donne_seri_prog();
        Cursor c2 = db.get_donne_prog(Long.parseLong(recup_pos()),Long.parseLong(recup_id_exo()));
        if(c.moveToFirst())
            do {
                if(recup_pos().equals(c.getString(1)) && recup_id_exo().equals(c.getString(2))){
                    stock = new ArrayList<>();
                    stock.add(c.getString(0));
                    stock.add(c.getString(3));
                    stock.add(c.getString(4));
                    stock.add(c2.getString(2));
                    test.add(stock);
                }
            }while (c.moveToNext());
        db.close();
        return test;
    }
    private void print_information(String text){
        Intent data = new Intent();
        data.setData(Uri.parse(text));
        Toast.makeText(this, data.getData().toString(),
                Toast.LENGTH_SHORT).show();
    }

    private void log(String text){
        System.out.println(text);
    }

    public class CustomAdapter extends BaseAdapter {
        CountDownTimer mCountDownTimer;
        boolean mTimerRunning;
        long mTimeLeftInMillis;
        long stock=0;
        TextView id_serie;
        String id_prog = recup_pos();
        String id_exo =recup_id_exo();
        FloatingActionButton enregistrer;
        FloatingActionButton play;

        public CustomAdapter() {
            mTimerRunning = false;
            mCountDownTimer = null;
            mTimeLeftInMillis=0;
            id_serie=null;
            enregistrer=null;
            play=null;
        }

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
        public View getView(final int i, View convertView, ViewGroup parent) {
            //post_item = i;
            final int post_list = i;
            post_item =post_list;
            convertView = getLayoutInflater().inflate(R.layout.customlayout_serie,null);
            id_serie = (TextView)convertView.findViewById(R.id.textView);
            final EditText charge = convertView.findViewById(R.id.editText1);
            final EditText rep = convertView.findViewById(R.id.editText2);
            final TextView rmax = convertView.findViewById(R.id.textView1);
            //COMPTEUR
            mTimeLeftInMillis = Long.parseLong(list_exo2.get(i).get(3)+"000");
            stock =  Long.parseLong(list_exo2.get(i).get(3)+"000");


            enregistrer = convertView.findViewById(R.id.floatingbutton);
            play = convertView.findViewById(R.id.play);

            int CalculRmax = (int) (( (0.0333*Integer.parseInt(list_exo2.get(i).get(2))) + 1 ) * Integer.parseInt(list_exo2.get(i).get(1)));

            id_serie.setText(list_exo2.get(i).get(0));
            charge.setText(list_exo2.get(i).get(1));
            rep.setText(list_exo2.get(i).get(2));
            rmax.setText(""+CalculRmax);


            enregistrer.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    /*log("id_serie : "+ id_serie.getText());
                    log("id_prog : " + id_prog);
                    log("id_exo : " + id_exo);
                    log("charge : " + charge.getText());
                    log("repetition : " + rep.getText());*/
                    String[] donnee = new String[]{""+charge.getText(),""+rep.getText()};
                    db = new DBAdapter(v.getContext());
                    db.open();
                    log("id_serie : "+ list_exo2.get(post_list).get(0));
                    log("id_prog : " + id_prog);
                    log("id_exo : " + id_exo);
                    log("charge : " + charge.getText());
                    log("repetition : " + rep.getText());
                    if(!(charge.getText().toString().equals("")) && !(rep.getText().toString().equals(""))) {
                        db.update_donne_serie(donnee, Long.parseLong(list_exo2.get(post_list).get(0)), Long.parseLong(id_prog), Long.parseLong(id_exo));
                        int rrmax = (int) (( (0.0333*Integer.parseInt(""+rep.getText())) + 1 ) * Integer.parseInt(""+charge.getText()));
                        rmax.setText(""+rrmax);

                    }
                    else
                        print_information("Champs non remplis");
                    db.close();



                }
            });
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mTimerRunning){
                        pauseTimer();
                    }else{
                        log("PLAY!!!");
                        startTimer(v);
                    }
                }
            });



            return convertView;
        }
        @SuppressLint("RestrictedApi")
        private void startTimer(final View v){
            mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimeLeftInMillis = millisUntilFinished;
                    updateCountDownText();
                }

                @Override
                public void onFinish() {
                    log("FINISH");
                    mTimeLeftInMillis = stock;
                    mTimerRunning = false;
                    timer_count.setVisibility(View.INVISIBLE);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String date = sdf.format(new Date());
                    db = new DBAdapter(v.getContext());
                    db.open();
                    String[] donnee = new String[]{id_prog,id_exo,list_exo2.get(post_item).get(1),list_exo2.get(post_item).get(2),date};
                    db.insert_donne_historique(donnee);
                    db.close();
                }
            }.start();

            mTimerRunning = true;
            timer_count.setVisibility(View.VISIBLE);
        }

        private void pauseTimer(){

        }
        private void updateCountDownText(){

            int minuts = (int) (mTimeLeftInMillis / 1000) / 60;
            int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
            String timeformat = String.format(Locale.getDefault(),"%02d:%02d",minuts,seconds);
            System.out.println("time: " + timeformat);
            timer_count.setText(timeformat);

        }
    }

}
