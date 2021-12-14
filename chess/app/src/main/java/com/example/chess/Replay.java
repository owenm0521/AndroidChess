package com.example.chess;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Replay extends AppCompatActivity {

    private Button nextMove;
    Player black = new Player("black");
    Player white = new Player("white");
    Board board = new Board(black, white);
    int moveCount = 0;
    public static ArrayList<SavedGame> userSavedGames = new ArrayList<SavedGame>();
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

    public HashMap<Integer, int[]> findSquares = new HashMap<Integer, int[]>();
    // holds details of User's first click: first element is row num, second element is col num,
    // this element is ID of the square that was clicked
    private ArrayList<Integer> firstClick = new ArrayList<Integer>();
    // stores all the moves in this game
    private ArrayList<Move> gameMoves = new ArrayList<Move>();
    // maps the toString representation of a Piece to its drawable
    private HashMap<String, Integer> pieceToDrawable = new HashMap<String, Integer>();
    private boolean gameOn = true;
    private boolean whiteTurn = true;
    private boolean printBoard = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);
        SavedGame savedGame = (SavedGame) getIntent().getSerializableExtra("clickedSavedGame");
        ArrayList<Move> moves = savedGame.getMoves();
        nextMove = (Button) findViewById(R.id.nextMove);



        nextMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moveCount < moves.size()) {

                    move(moves.get(moveCount));
                    moveCount++;
                }
            }
        });
    }



    public HashMap<String, Integer> populateBoard(HashMap<String, Integer> pieces){
        pieces.put("wp",R.drawable.wp);
        pieces.put("wB",R.drawable.wb);
        pieces.put("wQ",R.drawable.wq);
        pieces.put("wR",R.drawable.wr);
        pieces.put("wN",R.drawable.wn);
        pieces.put("wK",R.drawable.wk);
        pieces.put("bp",R.drawable.bp);
        pieces.put("bB",R.drawable.bb);
        pieces.put("bQ",R.drawable.bq);
        pieces.put("bR",R.drawable.br);
        pieces.put("bK",R.drawable.bk);
        pieces.put("bN",R.drawable.bn);
        return pieces;
    }

}