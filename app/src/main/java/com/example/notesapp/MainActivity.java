package com.example.notesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    static ArrayAdapter arrayAdapter;
    static LinkedList<String> notes=new LinkedList<>();
    ListView listView;


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.newnote){
            Intent intent=new Intent(getApplicationContext(), NoteTaker.class);
            startActivity(intent);
            return true;
        }else return false;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView) findViewById(R.id.newNote);

        //get all notes from shared preferences and add to notes array
        SharedPreferences sharedPreferences=this.getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);
        notes.clear();
        try {
            notes=(LinkedList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("notes",ObjectSerializer.serialize(new LinkedList<String>())));
            Log.i("LOADED ALL FROM MEMORY", ", COMPLETED");

        }catch (IOException e){
            e.printStackTrace();
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        //if(arrayAdapter==null) Log.i("ADAPTER IS NULL","NULL :(")
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 //open note at position 1
                 Intent intent = new Intent(getApplicationContext(), NoteTaker.class);
                 intent.putExtra("toOpen", notes.get(position));
                 startActivity(intent);
                }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    createBoxAndDelete(position);
                    return true;
                }
            });


    }
    private void createBoxAndDelete(final int pos){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Deleting note")
                .setMessage("Are you sure you want to delete this note")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notes.remove(pos);
                        arrayAdapter.notifyDataSetChanged();
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);
                        try {
                            sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(MainActivity.notes)).apply();
                            Log.i("ITEM DELETED AND MEMORY UPDATED", ", COMPLETED");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                }
        }).show();


    }
}