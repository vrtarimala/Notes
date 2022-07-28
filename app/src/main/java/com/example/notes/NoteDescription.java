package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

public class NoteDescription extends AppCompatActivity {
    TextView textView;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_description);
        Intent intent=getIntent();
        String text=intent.getStringExtra("text");
        i=intent.getIntExtra("pos",0);
        textView=findViewById(R.id.textWriting);
        if(text!=null) {
            textView.setText(text);
        }


    }

    @Override
    public void onBackPressed() {
        MainActivity.notes.set(i,textView.getText().toString());
        SharedPreferences sharedPreferences=this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        try {
            sharedPreferences.edit().putString("notes",ObjectSerializer.serialize(MainActivity.notes)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }


        MainActivity.arrayAdapter.notifyDataSetChanged();
        MainActivity.listView.setAdapter(MainActivity.arrayAdapter);
        super.onBackPressed();
    }
}