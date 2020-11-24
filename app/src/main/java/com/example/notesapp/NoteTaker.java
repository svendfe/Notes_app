package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;

public class NoteTaker extends AppCompatActivity {
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_taker);
        editText=(EditText) findViewById(R.id.editText);

        ///what to do a already existing note is selected
        Intent intent = getIntent();
        if(intent.hasExtra("toOpen")){
            String newNote=intent.getStringExtra("toOpen");
            editText.setText(newNote);
            MainActivity.notes.remove(newNote);
        }
        else
            editText.setHint("Start note...");
    }

    @Override
    protected void onStop(){
        MainActivity.notes.add(editText.getText().toString());
        MainActivity.arrayAdapter.notifyDataSetChanged();
        SharedPreferences sharedPreferences=this.getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);
        try {
            sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(MainActivity.notes)).apply();
            Log.i("SAVED ALL IN MEMORY", ", COMPLETED");

        }catch (IOException e){
            e.printStackTrace();
        }
        editText.setText("");
        super.onStop();
    }
}
