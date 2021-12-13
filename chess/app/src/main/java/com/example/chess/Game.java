package com.example.chess;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Game extends AppCompatActivity {

    public HashMap<Integer, int[]> findSquares = new HashMap<Integer, int[]>();
    public HashMap<ArrayList<Integer>, Integer> findIDFromMatrix = new HashMap<ArrayList<Integer>, Integer>();
    private ArrayList<Integer> firstClick = new ArrayList<Integer>();
    private ArrayList<Move> gameMoves = new ArrayList<Move>();
    private HashMap<String, Integer> pieces = new HashMap<String, Integer>();
    private boolean gameOn = true;
    private boolean whiteTurn = true;
    private boolean printBoard = true;
    private boolean enPassantPossible = false;
    boolean resign=false;
    boolean draw=false;
    Player white = new Player("white");
    Player black = new Player("black");
    Board board = new Board(white, black);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        board.createBoard();
        board.populateBoard();

        populateBoard(pieces);
        createbuttons();

        Button AIMoveButton = (Button) findViewById(R.id.AIMove);
        AIMoveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AIMove();
            }
        });

        Button undoButton = (Button) findViewById(R.id.undo);
        undoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                undoMove();
            }
        });

        Button resignButton = (Button) findViewById(R.id.resign);
        resignButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(whiteTurn){
                    Toast.makeText(Game.this, "White resigns, black wins!", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(Game.this, "Black resigns, white wins!", Toast.LENGTH_LONG).show();
                }
                resign = true;
                offerSaveGameDialog(Game.this);
            }
        });

        Button drawButton = (Button) findViewById(R.id.draw);
        drawButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(Game.this, "The game ends in a draw.", Toast.LENGTH_LONG).show();
                draw=true;
                offerSaveGameDialog(Game.this);
            }
        });


    }

    public void move(int row, int col, int id){
        int frow = firstClick.get(0);
        int fcol = firstClick.get(1);
        
        board.move()
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
    
    public void createbuttons(){
        ImageButton a1 = (ImageButton) findViewById(R.id.A1);
        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(a1.getId())[0], findSquares.get(a1.getId())[1], a1.getId());
            }
        });

        ImageButton a2 = (ImageButton) findViewById(R.id.A2);
        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(a2.getId())[0], findSquares.get(a2.getId())[1], a2.getId());

            }
        });

        ImageButton a3 = (ImageButton) findViewById(R.id.A3);
        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(a3.getId())[0], findSquares.get(a3.getId())[1], a3.getId());
            }
        });
        ImageButton a4 = (ImageButton) findViewById(R.id.A4);
        a4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(a4.getId())[0], findSquares.get(a4.getId())[1], a4.getId());
            }
        });
        ImageButton a5 = (ImageButton) findViewById(R.id.A5);
        a5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(a5.getId())[0], findSquares.get(a5.getId())[1], a5.getId());
            }
        });
        ImageButton a6 = (ImageButton) findViewById(R.id.A6);
        a6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(a6.getId())[0], findSquares.get(a6.getId())[1], a6.getId());
            }
        });
        ImageButton a7 = (ImageButton) findViewById(R.id.A7);
        a7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(a7.getId())[0], findSquares.get(a7.getId())[1], a7.getId());
            }
        });
        ImageButton a8 = (ImageButton) findViewById(R.id.A8);
        a8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(a8.getId())[0], findSquares.get(a8.getId())[1], a8.getId());
            }
        });

        //Column B ImageButtons
        ImageButton b1 = (ImageButton) findViewById(R.id.B1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(b1.getId())[0], findSquares.get(b1.getId())[1], b1.getId());
            }
        });

        ImageButton b2 = (ImageButton) findViewById(R.id.B2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(b2.getId())[0], findSquares.get(b2.getId())[1], b2.getId());
            }
        });

        ImageButton b3 = (ImageButton) findViewById(R.id.B3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(b3.getId())[0], findSquares.get(b3.getId())[1], b3.getId());
            }
        });
        ImageButton b4 = (ImageButton) findViewById(R.id.B4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(b4.getId())[0], findSquares.get(b4.getId())[1], b4.getId());
            }
        });
        ImageButton b5 = (ImageButton) findViewById(R.id.B5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(b5.getId())[0], findSquares.get(b5.getId())[1], b5.getId());
            }
        });
        ImageButton b6 = (ImageButton) findViewById(R.id.B6);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(b6.getId())[0], findSquares.get(b6.getId())[1], b6.getId());
            }
        });
        ImageButton b7 = (ImageButton) findViewById(R.id.B7);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(b7.getId())[0], findSquares.get(b7.getId())[1], b7.getId());
            }
        });
        ImageButton b8 = (ImageButton) findViewById(R.id.B8);
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(b8.getId())[0], findSquares.get(b8.getId())[1], b8.getId());
            }
        });

        //Column C ImageButtons
        ImageButton c1 = (ImageButton) findViewById(R.id.C1);
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(c1.getId())[0], findSquares.get(c1.getId())[1], c1.getId());
            }
        });

        ImageButton c2 = (ImageButton) findViewById(R.id.C2);
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(c2.getId())[0], findSquares.get(c2.getId())[1], c2.getId());
            }
        });

        ImageButton c3 = (ImageButton) findViewById(R.id.C3);
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(c3.getId())[0], findSquares.get(c3.getId())[1], c3.getId());
            }
        });
        ImageButton c4 = (ImageButton) findViewById(R.id.C4);
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(c4.getId())[0], findSquares.get(c4.getId())[1], c4.getId());
            }
        });
        ImageButton c5 = (ImageButton) findViewById(R.id.C5);
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(c5.getId())[0], findSquares.get(c5.getId())[1], c5.getId());
            }
        });
        ImageButton c6 = (ImageButton) findViewById(R.id.C6);
        c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(c6.getId())[0], findSquares.get(c6.getId())[1], c6.getId());
            }
        });
        ImageButton c7 = (ImageButton) findViewById(R.id.C7);
        c7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(c7.getId())[0], findSquares.get(c7.getId())[1], c7.getId());
            }
        });
        ImageButton c8 = (ImageButton) findViewById(R.id.C8);
        c8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(c8.getId())[0], findSquares.get(c8.getId())[1], c8.getId());
            }
        });

        //Column D ImageButtons
        ImageButton d1 = (ImageButton) findViewById(R.id.D1);
        d1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(d1.getId())[0], findSquares.get(d1.getId())[1], d1.getId());
            }
        });

        ImageButton d2 = (ImageButton) findViewById(R.id.D2);
        d2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(d2.getId())[0], findSquares.get(d2.getId())[1], d2.getId());
            }
        });

        ImageButton d3 = (ImageButton) findViewById(R.id.D3);
        d3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(d3.getId())[0], findSquares.get(d3.getId())[1], d3.getId());
            }
        });
        ImageButton d4 = (ImageButton) findViewById(R.id.D4);
        d4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(d4.getId())[0], findSquares.get(d4.getId())[1], d4.getId());
            }
        });
        ImageButton d5 = (ImageButton) findViewById(R.id.D5);
        d5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(d5.getId())[0], findSquares.get(d5.getId())[1], d5.getId());
            }
        });
        ImageButton d6 = (ImageButton) findViewById(R.id.D6);
        d6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(d6.getId())[0], findSquares.get(d6.getId())[1], d6.getId());
            }
        });
        ImageButton d7 = (ImageButton) findViewById(R.id.D7);
        d7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(d7.getId())[0], findSquares.get(d7.getId())[1], d7.getId());
            }
        });
        ImageButton d8 = (ImageButton) findViewById(R.id.D8);
        d8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(d8.getId())[0], findSquares.get(d8.getId())[1], d8.getId());
            }
        });

        //Column E

        ImageButton e1 = (ImageButton) findViewById(R.id.E1);
        e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(e1.getId())[0], findSquares.get(e1.getId())[1], e1.getId());
            }
        });

        ImageButton e2 = (ImageButton) findViewById(R.id.E2);
        e2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(e2.getId())[0], findSquares.get(e2.getId())[1], e2.getId());
            }
        });

        ImageButton e3 = (ImageButton) findViewById(R.id.E3);
        e3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(e3.getId())[0], findSquares.get(e3.getId())[1], e3.getId());
            }
        });
        ImageButton e4 = (ImageButton) findViewById(R.id.E4);
        e4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(e4.getId())[0], findSquares.get(e4.getId())[1], e4.getId());
            }
        });
        ImageButton e5 = (ImageButton) findViewById(R.id.E5);
        e5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(e5.getId())[0], findSquares.get(e5.getId())[1], e5.getId());
            }
        });
        ImageButton e6 = (ImageButton) findViewById(R.id.E6);
        e6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(e6.getId())[0], findSquares.get(e6.getId())[1], e6.getId());
            }
        });
        ImageButton e7 = (ImageButton) findViewById(R.id.E7);
        e7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(e7.getId())[0], findSquares.get(e7.getId())[1], e7.getId());
            }
        });
        ImageButton e8 = (ImageButton) findViewById(R.id.E8);
        e8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(e8.getId())[0], findSquares.get(e8.getId())[1], e8.getId());
            }
        });
        ImageButton f1 = (ImageButton) findViewById(R.id.F1);
        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(f1.getId())[0], findSquares.get(f1.getId())[1], f1.getId());
            }
        });

        ImageButton f2 = (ImageButton) findViewById(R.id.F2);
        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(f2.getId())[0], findSquares.get(f2.getId())[1], f2.getId());
            }
        });

        ImageButton f3 = (ImageButton) findViewById(R.id.F3);
        f3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(f3.getId())[0], findSquares.get(f3.getId())[1], f3.getId());
            }
        });
        ImageButton f4 = (ImageButton) findViewById(R.id.F4);
        f4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(f4.getId())[0], findSquares.get(f4.getId())[1], f4.getId());
            }
        });
        ImageButton f5 = (ImageButton) findViewById(R.id.F5);
        f5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(f5.getId())[0], findSquares.get(f5.getId())[1], f5.getId());
            }
        });
        ImageButton f6 = (ImageButton) findViewById(R.id.F6);
        f6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(f6.getId())[0], findSquares.get(f6.getId())[1], f6.getId());
            }
        });
        ImageButton f7 = (ImageButton) findViewById(R.id.F7);
        f7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(f7.getId())[0], findSquares.get(f7.getId())[1], f7.getId());
            }
        });
        ImageButton f8 = (ImageButton) findViewById(R.id.F8);
        f8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(f8.getId())[0], findSquares.get(f8.getId())[1], f8.getId());
            }
        });

        ImageButton g1 = (ImageButton) findViewById(R.id.G1);
        g1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(g1.getId())[0], findSquares.get(g1.getId())[1], g1.getId());
            }
        });

        ImageButton g2 = (ImageButton) findViewById(R.id.G2);
        g2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(g2.getId())[0], findSquares.get(g2.getId())[1], g2.getId());
            }
        });

        ImageButton g3 = (ImageButton) findViewById(R.id.G3);
        g3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(g3.getId())[0], findSquares.get(g3.getId())[1], g3.getId());
            }
        });
        ImageButton g4 = (ImageButton) findViewById(R.id.G4);
        g4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(g4.getId())[0], findSquares.get(g4.getId())[1], g4.getId());
            }
        });
        ImageButton g5 = (ImageButton) findViewById(R.id.G5);
        g5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(g5.getId())[0], findSquares.get(g5.getId())[1], g5.getId());
            }
        });
        ImageButton g6 = (ImageButton) findViewById(R.id.G6);
        g6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(g6.getId())[0], findSquares.get(g6.getId())[1], g6.getId());
            }
        });
        ImageButton g7 = (ImageButton) findViewById(R.id.G7);
        g7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(g7.getId())[0], findSquares.get(g7.getId())[1], g7.getId());
            }
        });
        ImageButton g8 = (ImageButton) findViewById(R.id.G8);
        g8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(g8.getId())[0], findSquares.get(g8.getId())[1], g8.getId());
            }
        });

        ImageButton h1 = (ImageButton) findViewById(R.id.H1);
        h1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(h1.getId())[0], findSquares.get(h1.getId())[1], h1.getId());
            }
        });

        ImageButton h2 = (ImageButton) findViewById(R.id.H2);
        h2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(h2.getId())[0], findSquares.get(h2.getId())[1], h2.getId());
            }
        });

        ImageButton h3 = (ImageButton) findViewById(R.id.H3);
        h3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(h3.getId())[0], findSquares.get(h3.getId())[1], h3.getId());
            }
        });
        ImageButton h4 = (ImageButton) findViewById(R.id.H4);
        h4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(h4.getId())[0], findSquares.get(h4.getId())[1], h4.getId());
            }
        });
        ImageButton h5 = (ImageButton) findViewById(R.id.H5);
        h5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(h5.getId())[0], findSquares.get(h5.getId())[1], h5.getId());
            }
        });
        ImageButton h6 = (ImageButton) findViewById(R.id.H6);
        h6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(h6.getId())[0], findSquares.get(h6.getId())[1], h6.getId());
            }
        });
        ImageButton h7 = (ImageButton) findViewById(R.id.H7);
        h7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(h7.getId())[0], findSquares.get(h7.getId())[1], h7.getId());
            }
        });
        ImageButton h8 = (ImageButton) findViewById(R.id.H8);
        h8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(findSquares.get(h8.getId())[0], findSquares.get(h8.getId())[1], h8.getId());
            }
        });
    }
}