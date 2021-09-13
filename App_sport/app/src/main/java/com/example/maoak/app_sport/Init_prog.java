package com.example.maoak.app_sport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class Init_prog extends AppCompatActivity{
    EditText nom_prog;
    DBAdapter db = new DBAdapter(this);
    Intent intent;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_prog);
        nom_prog = (EditText) findViewById(R.id.editText);
    }

    public void create_prog_init(View v){
        String[] info = new String[]{""+nom_prog.getText()};
        db = new DBAdapter(this);
        db.open();
        db.insert_donne_programme_init(info);
        db.close();
        intent = new Intent(v.getContext(),test_fragment.class);
        intent.putExtra("id",recup_id());
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
}
