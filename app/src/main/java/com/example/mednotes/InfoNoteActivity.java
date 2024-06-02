package com.example.mednotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mednotes.databinding.ActivityInfoNoteBinding;

public class InfoNoteActivity extends AppCompatActivity {


    private ActivityInfoNoteBinding binding;

    NotesDataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent =  getIntent();

        String name =  intent.getStringExtra("Name");
        String desc =  intent.getStringExtra("Desc");
        String finishing =  intent.getStringExtra("Finishing");
        String example =  intent.getStringExtra("Example");
        String date =  intent.getStringExtra("Date");
        int id =  intent.getIntExtra("Id",0);

        binding = ActivityInfoNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.editTextNameInfo.setText(name);
        binding.editTextDescInfo.setText(desc);
        binding.editTextFinishingInfo.setText(finishing);
        binding.editTextExampleInfo.setText(example);

        db = new NotesDataBaseHelper(this);


        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.editTextNameInfo.getText().toString();
                String desc = binding.editTextDescInfo.getText().toString();
                String finishing = binding.editTextFinishingInfo.getText().toString();
                String example = binding.editTextExampleInfo.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("my_id", Context.MODE_PRIVATE);
                int user_id = sharedPreferences.getInt("id", 0);

                item Item = new item(id,name,user_id,desc, date,example,finishing);

                db.updateNote(Item,id);
                finish();
                Toast.makeText(InfoNoteActivity.this,"Отредактировано",Toast.LENGTH_SHORT).show();
            }
        });

    }
}