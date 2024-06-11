package com.example.mednotes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mednotes.databinding.ActivityAddNewNoteBinding;

import java.time.LocalDateTime;

public class AddNewNoteActivity extends AppCompatActivity {

    ActivityAddNewNoteBinding binding;
    NotesDataBaseHelper db;
    String name;
    String desc;
    String example;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        db = new NotesDataBaseHelper(this);

    }

    public  void onClickSave(View v){
         desc = binding.editTextDesc
                 .getText().toString();
         name = binding.editTextNickname
                 .getText().toString();
         example = binding.editTextExample
                 .getText().toString();
         if(desc.isEmpty() || name.isEmpty()
                 || example.isEmpty()){
             Toast.makeText(AddNewNoteActivity.this,
                     "Все поля должны быть заполнены",
                     Toast.LENGTH_SHORT).show();
             return;
         }
        LocalDateTime createdTime = null;
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            createdTime = LocalDateTime.now();
        }
        item Item = null;
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            SharedPreferences sharedPreferences =
                    getSharedPreferences(
                    "my_id", Context.MODE_PRIVATE);
            int user_id = sharedPreferences.getInt(
                    "id", 0);
            Item = new item(0,name,user_id,desc,
                    createdTime.toString()
                    ,example,"");
        }
        db.insertNote(Item);
        finish();
        Toast.makeText(AddNewNoteActivity.this,
                "Создано",Toast.LENGTH_SHORT).show();
    }
}