package com.example.maoak.app_sport;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button login;
    Button create;
    EditText login_pseudo;
    EditText login_passwordd;
    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (Button)findViewById(R.id.log);
        create = (Button)findViewById(R.id.create);
        login_pseudo = (EditText)findViewById(R.id.login_pseudo);
        login_passwordd = (EditText)findViewById(R.id.login_password);
        File f = new File(getFilesDir(), "connexion.txt");
        if(f.exists()){
            ArrayList<ArrayList<String>> donnee = recup_info_init(f);
            Intent intent = new Intent(this,test_fragment.class);
            intent.putExtra("id",donnee.get(0).get(0));
            startActivity(intent);

        }
    }

    public void onclick_create(View v){
        Button button = (Button)v;
        boolean etat=false;
        if(button.equals(login)){
            asset_init();
            if(login_pseudo.getText().toString().equals("") && login_passwordd.getText().toString().equals("")){
                print_information("Champs pseudo et password vide.");
            }
            else if(login_pseudo.getText().toString().equals("")){
                print_information("Champs pseudo vide.");
            }
            else if(login_passwordd.getText().toString().equals("") ){
                print_information("Champs password vide.");
            }
            else{
                etat = login(login_pseudo.getText().toString(),login_passwordd.getText().toString());
                if(etat){
                    String pseudo = login_pseudo.getText().toString();

                    String[] donnnee = new String[]{
                            ""+recup_compte(pseudo)
                    };
                    add_donne_in_file(donnnee,"connexion.txt");
                    Intent intent = new Intent(this,test_fragment.class);
                    intent.putExtra("id",recup_compte(pseudo)+"");
                    startActivity(intent);
                    login_pseudo.setText("");
                    login_passwordd.setText("");
                }
                else
                    print_information("ERROR CONNEXION");
            }
        }
        else if(button.equals(create))
            startActivity(new Intent("com.example.maoak.app_sport.SecondActivity"));

    }
    private boolean login(String pseudo, String mdp){
        boolean etat = false;
        db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAll_donnee_profil();
        if(c.moveToFirst()){
            do{
                if(pseudo.equals(c.getString(1)) && mdp.equals(c.getString(2))){
                    etat = true;
                }
            }while(c.moveToNext());
        }
        db.close();
        return etat;
    }
    private int recup_compte(String pseudo){
        int x = 0;
        db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAll_donnee_profil();
        if(c.moveToFirst()){
            do{
                if(pseudo.equals(c.getString(1))){
                    x = c.getInt(0);
                }
            }while(c.moveToNext());
        }
        db.close();
        return x;
    }
    private ArrayList recup_info_init(File f){
        ArrayList<ArrayList<String>> donne=new ArrayList<>();
        ArrayList<String> stock;
        String[] stock_donne;
        try {
            FileInputStream pr = new FileInputStream(f);
            InputStreamReader inputStreamReader = new InputStreamReader(pr);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lines;
            while ((lines=bufferedReader.readLine())!=null) {
                stock_donne = lines.split("::");
                stock = new ArrayList<>();
                for(int i=0;i<stock_donne.length;i++)
                    stock.add(stock_donne[i]);

                donne.add(stock);
            }
            pr.close();
        }
        catch (IOException e){
            print_information(e.getMessage());
        }
        return donne;
    }
    private File init_file_text(String name_file){
        File path = getFilesDir();
        File f = new File(path,name_file);
        if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }
    private void add_donne_in_file(String[] donnee, String filetext){
        try {
            File file = init_file_text(filetext);
            ArrayList<ArrayList<String>> donnee_file = recup_info_init(file);
            FileOutputStream pw = new FileOutputStream(file,true);
            if(donnee_file.size()>0)
                pw.write("\n".getBytes());
            for (int i=0;i<donnee.length;i++){
                pw.write(donnee[i].getBytes());
                pw.write("::".getBytes());
            }
            pw.close();
        }catch (IOException e) {
            print_information(e.getMessage());
        }
    }
    private void asset_init(){

        int compteur=0;
        ArrayList<String[]> donne=new ArrayList<>();
        String[] stock_donne;

        db = new DBAdapter(this);
        try {
            InputStream is1 = getAssets().open("base_categorie.txt");
            InputStream is2 = getAssets().open("base_type.txt");
            InputStream is3 = getAssets().open("base_muscle.txt");
            InputStream is4 = getAssets().open("base_exercice.txt");
            InputStream is5 = getAssets().open("base_muscle_PS.txt");
            InputStreamReader categorie = new InputStreamReader(is1);
            InputStreamReader type = new InputStreamReader(is2);
            InputStreamReader muscle = new InputStreamReader(is3);
            InputStreamReader exercice = new InputStreamReader(is4);
            InputStreamReader exo_muscle = new InputStreamReader(is5);
            String lines;

            //LA CATEGORIE
            BufferedReader bufferedReader = new BufferedReader(categorie);
            while ((lines=bufferedReader.readLine())!=null){
                stock_donne = lines.split("::");
                donne.add(stock_donne);
            }
            is1.close();

            db.open();


            Cursor c = db.getAll_donnee_categorie();
            if(!c.moveToFirst()){
                for(int i=0;i<donne.size();i++){
                    db.insert_donne_categorie(donne.get(i));
                }
            }

            //LE TYPE
            donne = new ArrayList<>();
            bufferedReader = new BufferedReader(type);
            while ((lines=bufferedReader.readLine())!=null){
                stock_donne = lines.split("::");
                donne.add(stock_donne);
            }
            is2.close();
            c = db.getAll_donnee_type();
            if(!c.moveToFirst()){
                for(int i=0;i<donne.size();i++){
                    db.insert_donne_type(donne.get(i));
                }
            }


            //MUSCLE
            donne = new ArrayList<>();
            bufferedReader = new BufferedReader(muscle);
            while ((lines=bufferedReader.readLine())!=null){
                stock_donne = lines.split("::");
                donne.add(stock_donne);
            }
            is3.close();
            c = db.getAll_donne_muscle();
            if(!c.moveToFirst()){
                for(int i=0;i<donne.size();i++){
                    db.insert_donne_muscle(donne.get(i));
                }
            }

            //EXERCICE
            donne = new ArrayList<>();
            bufferedReader = new BufferedReader(exercice);
            while ((lines=bufferedReader.readLine())!=null){
                stock_donne = lines.split("::");
                donne.add(stock_donne);
            }
            is4.close();
            if(!c.moveToFirst()){
                for(int i=0;i<donne.size();i++){
                    db.insert_donne_exo(donne.get(i));
                }
            }
            //EXERCICE MUSCLE
            donne = new ArrayList<>();
            bufferedReader = new BufferedReader(exo_muscle);
            while ((lines=bufferedReader.readLine())!=null){
                stock_donne = lines.split("::");
                donne.add(stock_donne);
            }
            is5.close();
            if(!c.moveToFirst()){
                for(int i=0;i<donne.size();i++){
                    db.insert_donne_principale_secondaire(donne.get(i));
                }
            }

            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void print_information(String text){
        Intent data = new Intent();
        data.setData(Uri.parse(text));
        Toast.makeText(this, data.getData().toString(),
                Toast.LENGTH_SHORT).show();
    }
}
