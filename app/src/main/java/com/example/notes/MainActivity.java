package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    static ArrayList<String> notes;
    static ArrayAdapter<String> arrayAdapter;
    static ListView listView;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        int pos=notes.size();
        notes.add("");
        Intent intent=new Intent(getApplicationContext(),NoteDescription.class);
        intent.putExtra("text",notes.get(pos));
        intent.putExtra("pos",pos);
        startActivity(intent);
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listView);

        sharedPreferences=this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        try {
            notes=(ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("notes",null));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(notes==null){
            notes=new ArrayList<>();
            notes.add("Example note....");

        }
        arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1,notes);
        listView.setAdapter(arrayAdapter);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),NoteDescription.class);
                intent.putExtra("text",notes.get(position));
                intent.putExtra("pos",position);
                startActivity(intent);
            }

        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure you want to delete ?")
                        .setMessage("the note will be deleted and unsaved changes lost.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                Toast.makeText(getApplicationContext(),"Note deleted",Toast.LENGTH_SHORT).show();
                                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                                try {
                                    sharedPreferences.edit().putString("notes",ObjectSerializer.serialize(notes)).apply();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                arrayAdapter.notifyDataSetChanged();
                                listView.setAdapter(arrayAdapter);
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();

                return false;
            }
        });


    }

}