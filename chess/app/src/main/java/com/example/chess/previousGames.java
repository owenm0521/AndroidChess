package com.example.chess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class previousGames extends AppCompatActivity {

    private ArrayList<SavedGame> games;
    private Button sortByName;
    private Button sortByDate;
    private ListView gamesList;
    private ArrayList<String> names;
    private ArrayList<Date> dates;
    private ArrayAdapter<String> arrayAdapt;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_game);
        sortByName = (Button)findViewById(R.id.sortByName);
        sortByDate = (Button)findViewById(R.id.sortByDate);
        gamesList = (ListView)findViewById(R.id.gamesList);

        names = new ArrayList<String>();
        dates = new ArrayList<Date>();
        arrayList = new ArrayList<String>();
        arrayAdapt = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);

        games = readGames();
        for(SavedGame s: games){
            names.add(s.getName());
            dates.add(s.getDate());
            arrayList.add(s.toString());
        }

        sortByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort(names, arrayList, games, dates);
                gamesList.setAdapter(arrayAdapt);
                arrayAdapt.notifyDataSetChanged();
                gamesList.refreshDrawableState();
            }
        });

        sortByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortDate(names,arrayList,games,dates);
                gamesList.setAdapter(arrayAdapt);
                arrayAdapt.notifyDataSetChanged();
                gamesList.refreshDrawableState();
            }
        });

        gamesList.setAdapter(arrayAdapt);
        arrayAdapt.notifyDataSetChanged();


        gamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(previousGames.this, Replay.class);
                intent.putExtra("clickedSavedGame", games.get(position));
                startActivity(intent);
            }
        });
    }


    public ArrayList<SavedGame> readGames(){
        try {
            File file = new File(previousGames.this.getFilesDir(), "games");
            File saveFile = new File(file, "savedGame");
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(saveFile));

            ArrayList<SavedGame> listGames = (ArrayList<SavedGame>) input.readObject();
            return listGames;
        }
        catch(Exception e) {
            return new ArrayList<SavedGame>();
        }
    }

    public void sort(ArrayList<String> names, ArrayList<String> arrayAdapt, ArrayList<SavedGame> gamesList, ArrayList<Date> dates){
        for ( int j=0; j < names.size()-1; j++ )
        {
            int min = j;
            for ( int k=j+1; k < names.size(); k++ )
                if ( names.get(k).toLowerCase().compareTo( names.get(min).toLowerCase()) < 0 ) min = k;

            // Swap the reference at j with the reference at min
            Collections.swap(names, j, min);
            Collections.swap(gamesList, j, min);
            Collections.swap(arrayAdapt, j, min);
            Collections.swap(dates, j, min);

        }
    }

    public void sortDate(ArrayList<String> names, ArrayList<String> arrayAdapt, ArrayList<SavedGame> gamesList, ArrayList<Date> dates )
    {

        for ( int j=0; j < dates.size()-1; j++ )
        {
            int min = j;
            for ( int k=j+1; k < dates.size(); k++ )
                if ( dates.get(k).compareTo( dates.get(min)) < 0 ) min = k;

            // Swap the reference at j with the reference at min
            Collections.swap(names, j, min);
            Collections.swap(gamesList, j, min);
            Collections.swap(arrayAdapt, j, min);
            Collections.swap(dates, j, min);

        }}
}