package com.example.mednotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mednotes.databinding.ActivityEditProfileBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding binding;

    NotesDataBaseHelper db;

    File image;

    ActivityResultLauncher<Intent> resultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        registerResult();
        db = new NotesDataBaseHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("my_id", Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", 0);
        User user = db.getUserById(id);

        if(user.getImage().length > 0){
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
            binding.editAvatarProfile.setImageBitmap(bitmap);
        }


        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.editTextFullname.setText(user.fullName);
        binding.editTextRucFullname.setText(user.fullName_ruc);
        binding.editTextTerms.setText(user.terms_practice);
        binding.editTextExamplesPractice.setText(user.examples_practice);


        binding.editAvatarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });



        binding.btnSaveEdits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String fullnameUser  = binding.editTextFullname.getText().toString();
               String fullnameRuc  = binding.editTextRucFullname.getText().toString();
               String Terms  = binding.editTextTerms.getText().toString();
               String ExamplePractice  = binding.editTextExamplesPractice.getText().toString();

                byte[] byteImage = new byte[0];
                Drawable drawable = binding.editAvatarProfile.getDrawable();
                if(drawable instanceof BitmapDrawable){
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteImage = stream.toByteArray();
                }

               User newUser = new User(user.email,fullnameUser,fullnameRuc,user.password,user.user_id,ExamplePractice,Terms,byteImage);

                db.updateUser(newUser,id);
                finish();
                Toast.makeText(EditProfileActivity.this,"Изменено",Toast.LENGTH_SHORT).show();
            }
        });


    }


    private  void pickImage() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    private void registerResult(){
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        try {

                            Uri imageUri = o.getData().getData();
                            image = new File(imageUri.toString());


                            binding.editAvatarProfile.setImageURI(imageUri);

                        }catch (Exception e){
                            Toast.makeText(EditProfileActivity.this,"Изображение не выбрано",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}