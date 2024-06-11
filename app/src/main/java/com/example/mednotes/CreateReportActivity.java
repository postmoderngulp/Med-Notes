package com.example.mednotes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mednotes.databinding.ActivityCreateReportBinding;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateReportActivity extends AppCompatActivity {

    ActivityCreateReportBinding binding;

    NotesDataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateReportBinding.inflate(getLayoutInflater());
        db = new NotesDataBaseHelper(this);
        setContentView(binding.getRoot());
        SharedPreferences sharedPreferences = getSharedPreferences("my_id", Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", 0);
        List<item> listItems = db.getAllNotes(id);

        ArrayList<item> items = new ArrayList<item>();

        for(int i = 0;i < listItems.size();i++){
            if(listItems.get(i).user_id == id){
                items.add(listItems.get(i));
            }
        }

        Collections.reverse(items);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.ReportDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDateTime nowTime = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    nowTime = LocalDateTime.now();
                }

                List<item> filterItems = new ArrayList<item>();
                for(int i = 0;i < items.size();i++){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if(items.get(i).date.substring(0,9).equals(nowTime.toString().substring(0,9))){
                            filterItems.add(items.get(i));
                        }
                    }
                }
                if(filterItems.isEmpty()){
                    Toast.makeText(CreateReportActivity.this,"Нет задач за этот период",Toast.LENGTH_SHORT).show();
                    return;
                }
                createPdf(filterItems, "неделю" + " ("  + filterItems.get(0).date.substring(0,10) + ")");
            }
        });

        binding.ReportWeek.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                List<item> filterItems = new ArrayList<item>();
                LocalDateTime endTime = LocalDateTime.now();
                LocalDateTime startTime = LocalDateTime.now().minusDays(7);

                for(int i = 0;i < items.size();i++){
                    LocalDateTime date = LocalDateTime.parse(items.get(i).getDate());
                    if(date.isBefore(endTime) && date.isAfter(startTime)){
                        filterItems.add(items.get(i));
                    }
                }
                if(filterItems.isEmpty()){
                    Toast.makeText(CreateReportActivity.this,"Нет задач за этот период",Toast.LENGTH_SHORT).show();
                    return;
                }
                createPdf(filterItems,"день" + " ("  + startTime.toString().substring(0,10) + " - " +  endTime.toString().substring(0,10)  + ")");
            }
        });

        binding.ReportMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDateTime createdTime = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createdTime = LocalDateTime.now();
                }

                List<item> filterItems = new ArrayList<item>();
                for(int i = 0;i < items.size();i++){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if(items.get(i).date.substring(0,7).equals(createdTime.toString().substring(0,7))){
                            filterItems.add(items.get(i));
                        }
                    }
                }
                if(filterItems.isEmpty()){
                    Toast.makeText(CreateReportActivity.this,"Нет задач за этот период",Toast.LENGTH_SHORT).show();
                    return;
                }
                createPdf(filterItems,"месяц" + " ("  + filterItems.get(0).date.substring(0,7) + ")");
            }
        });


    }



    private  void createPdf(List<item> items,String date)  {
        try{
            File[] dir = ContextCompat.getExternalFilesDirs(
                    this, null);
            String path = dir[0] + "/PDF_Practice";
            File file = new File(path);
            String fontPath = "res/font/arial.ttf";
            PdfFont customFont = PdfFontFactory.createFont(fontPath);


            if(!file.exists()){
                file.mkdirs();
            }
            File pdf_file = new File(file.getAbsolutePath()
                    + "/MYPDF" +
                    getCurrentTime() + "_" + getTodayDate() +
                    ".pdf");
            if(!pdf_file.exists()){
                pdf_file.createNewFile();
            }
            PdfWriter writer = new PdfWriter(pdf_file.
                    getAbsoluteFile());
            PdfDocument pdfDocument = new
                    PdfDocument(writer);
            pdfDocument.addNewPage();
            Document document = new Document(pdfDocument);
            document.setHorizontalAlignment(HorizontalAlignment.CENTER);
            Paragraph paragraph = new Paragraph();
            paragraph.setMarginBottom(20);
            paragraph.add("Отчет за " + date).setHorizontalAlignment(HorizontalAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER).setFont(customFont).setFontSize(36);
            Table table = new Table(new float[]{1, 1, 1}).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER);

            table.addHeaderCell("Тема").setTextAlignment(TextAlignment.CENTER).setFontSize(28).setFont(customFont);
            table.addHeaderCell("Задачи").setTextAlignment(TextAlignment.CENTER).setFontSize(28).setFont(customFont);
            table.addHeaderCell("Сделано").setTextAlignment(TextAlignment.CENTER).setFontSize(28).setFont(customFont);

            for(int i = 0; i < items.size();i++) {

                table.addCell(items.get(i).name).setTextAlignment(TextAlignment.CENTER).setFontSize(28).setFont(customFont);
                table.addCell(items.get(i).example).setTextAlignment(TextAlignment.CENTER).setFontSize(28).setFont(customFont);
                table.addCell(items.get(i).finishing).setTextAlignment(TextAlignment.CENTER).setFontSize(28).setFont(customFont);
            }
            document.add(paragraph);
            document.add(table);
            document.close();
            Toast.makeText(this,"Отчет сформирован",
                    Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager =
                    getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.FrameShare,
                    new ShareFragment(pdf_file.getAbsolutePath()));
            fragmentTransaction.commit();
        }
        catch (Exception e) {
            Log.i("client", e.getMessage().toString());
        }
    }


    private  String getCurrentTime(){
        return  new SimpleDateFormat("hh:mm a" ,Locale.getDefault()).format(new Date());
    }

    private  String getTodayDate(){
        return  new SimpleDateFormat("dd-MM-yyyy" ,Locale.getDefault()).format(new Date());
    }
}