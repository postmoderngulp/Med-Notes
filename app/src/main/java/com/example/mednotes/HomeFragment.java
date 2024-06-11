package com.example.mednotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mednotes.databinding.FragmentHomeBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment implements  RecyclerViewInterface {

    FragmentHomeBinding binding;
    List<item> items = new ArrayList<>();
    List<item> listItemsHelp = new ArrayList<>();

    NotesDataBaseHelper db;

    MyAdapter adapter;

    RecyclerViewInterface recyclerViewInterface;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,
                container,false);
        SharedPreferences sharedPreferences = getContext().
                getSharedPreferences("my_id",
                        Context.MODE_PRIVATE);
        int id = sharedPreferences.
                getInt("id", 0);
        db = new NotesDataBaseHelper(requireContext());
        List<item> listItems = db.getAllNotes(id);
         listItemsHelp = new ArrayList<>();
        item[] itemArray = listItems.
                toArray(new item[listItems.size()]);
        for(int i = 0;i < itemArray.length;i++){
            if(itemArray[i].user_id == id){
                listItemsHelp.
                        add(new item(itemArray[i].id,itemArray[i].name,
                        itemArray[i].user_id,itemArray[i].
                                description,itemArray[i].date,
                        itemArray[i].example,itemArray[i].finishing));
                items.add(itemArray[i]);
            }
        }
        for(int i = 0;i < items.size();i++){
            LocalDateTime date = LocalDateTime.
                    parse(items.get(i).getDate());
            items.get(i).date = DateTimeFormatter.
                    ofPattern("yyyy-MM-dd",
                            Locale.ENGLISH).format(date);
        }
        Collections.reverse(items);
        Collections.reverse(listItemsHelp);
        binding.addReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),
                        CreateReportActivity.class);
                startActivity(intent);
            }
        });
        recyclerViewInterface = new RecyclerViewInterface(){
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(requireContext(),
                        InfoNoteActivity.class);
                intent.putExtra("Name",
                        items.get(position).getName());
                intent.putExtra("Desc",
                        items.get(position).getDescription());
                intent.putExtra("Example",
                        items.get(position).getExample());
                intent.putExtra("Date",
                        listItemsHelp.get(position).getDate());
                intent.putExtra("Id",
                        items.get(position).getId());
                intent.putExtra("Finishing",
                        items.get(position).getFinishing());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                db.deleteNoteById(position);
                items.clear();
                List<item> listItems = db.getAllNotes(id);
                item[] itemArray = listItems.toArray(
                        new item[listItems.size()]);
                for(int i = 0;i < itemArray.length;i++){
                    if(itemArray[i].user_id == id){
                        listItemsHelp.add(new item(
                                itemArray[i].id,
                                itemArray[i].name,
                                itemArray[i].user_id,
                                itemArray[i].description,
                                itemArray[i].date,
                                itemArray[i].example,
                                itemArray[i].finishing));
                        items.add(itemArray[i]);
                    }
                }
                for(int i = 0;i < items.size();i++){
                    LocalDateTime date = LocalDateTime.
                            parse(items.get(i).getDate());
                    items.get(i).date = DateTimeFormatter.
                            ofPattern("yyyy-MM-dd",
                                    Locale.ENGLISH).format(date);
                }
                Collections.reverse(items);
                Collections.reverse(listItemsHelp);
                adapter.refreshData(items);
            }
        };

        adapter = new MyAdapter(requireContext(),
                items,recyclerViewInterface);
        binding.recyclerview.setLayoutManager(
                new LinearLayoutManager(requireContext()));
        binding.recyclerview.setAdapter(adapter);
        binding.addnewnote.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(requireContext(),
                    AddNewNoteActivity.class));
            }
        });
        return binding.getRoot();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onResume(){
        super.onResume();
        SharedPreferences sharedPreferences = getContext().
                getSharedPreferences("my_id",
                        Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", 0);
        List<item> listItems = db.getAllNotes(id);
         listItemsHelp = new ArrayList<>();
        item[] itemArray = listItems.toArray(new item[listItems.size()]);
        items.clear();
        listItemsHelp.clear();
        for(int i = 0;i < itemArray.length;i++){
            if(itemArray[i].user_id == id){
                items.add(itemArray[i]);
                listItemsHelp.add(new item(itemArray[i].id,
                        itemArray[i].name,itemArray[i].user_id,
                        itemArray[i].description,itemArray[i].date,
                        itemArray[i].example,itemArray[i].finishing));
            }
        }
        for(int i = 0;i < items.size();i++){
            LocalDateTime date = LocalDateTime.parse(
                    items.get(i).getDate());
            items.get(i).date = DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd", Locale.ENGLISH).format(date);
        }
        Collections.reverse(items);
        Collections.reverse(listItemsHelp);
        adapter = new MyAdapter(requireContext(),
                items,recyclerViewInterface);
        binding.recyclerview.setLayoutManager(new
                LinearLayoutManager(requireContext()));
        binding.recyclerview.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(requireContext(),InfoNoteActivity.class);

        intent.putExtra("Name",items.get(position).getName());
        intent.putExtra("Desc",items.get(position).getDescription());
        intent.putExtra("Example",items.get(position).getExample());
        intent.putExtra("Date",listItemsHelp.get(position).getDate());
        intent.putExtra("Finishing",items.get(position).getFinishing());


        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {

    }
}