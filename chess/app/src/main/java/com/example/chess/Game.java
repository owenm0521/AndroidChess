package com.example.chess;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Game extends AppCompatActivity {

    public HashMap<Integer, int[]> findSquares = new HashMap<Integer, int[]>();
    public HashMap<ArrayList<Integer>, Integer> findIDFromMatrix = new HashMap<ArrayList<Integer>, Integer>();
    private ArrayList<Integer> firstClick = new ArrayList<Integer>();
    private ArrayList<Move> moves = new ArrayList<Move>();
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
                    Toast.makeText(Game.this, "White resigns, black wins!",Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(Game.this, "Black resigns, white wins!", Toast.LENGTH_LONG).show();
                }
                resign = true;
                gameOver(Game.this);
            }
        });

        Button drawButton = (Button) findViewById(R.id.draw);
        drawButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(Game.this, "The game ends in a draw.", Toast.LENGTH_LONG).show();
                draw=true;
                gameOver(Game.this);
            }
        });


    }

    public void move(int row, int col, int id){
        if(firstClick.size()>0){

            int sourceRow = firstClick.get(0);
            int sourceCol = firstClick.get(1);



            int destCol = col;
            int destRow = row;

            ArrayList<Piece> capturedPiece = new ArrayList<Piece>();

            // stores Pieces's current firstMove value
            boolean firstMove = false;
            if(board.board[sourceRow][sourceCol]!=null){
                firstMove = board.board[sourceRow][sourceCol].firstMove;
            }

            Character playerTurn = whiteTurn ? 'w' : 'b';

            if(board.move(playerTurn, sourceRow, sourceCol, destRow, destCol, capturedPiece)) {
                if(whiteTurn)
                    whiteTurn=false;
                else
                    whiteTurn = true;
                if((sourceRow == destRow+2 || sourceRow == destRow-2) && sourceCol==destCol && board.board[destRow][destCol] instanceof Pawn) {
                    enPassantPossible = true;
                }
                else {
                    enPassantPossible = false;
                }

                // opens dialog for user to handle promotion when applicable (and handles the rest of promotion there)
                if(board.board[destRow][destCol] instanceof Pawn && (destRow==0 || destRow==7)){
                    promotionDialog(Game.this, sourceRow, sourceCol, destRow, destCol, !whiteTurn, capturedPiece, id, firstMove);
                    return;
                }


                Piece pieceCaptured = null;
                if(capturedPiece.size()>0) pieceCaptured = capturedPiece.get(0);

                boolean castlingMove = false;
                if(Math.abs(sourceCol-destCol)==2 && board.board[destRow][destCol] instanceof King){
                    castlingMove = true;
                }

                // records whether a piece's first move was changed
                boolean firstMoveChanged = firstMove != board.board[destRow][destCol].firstMove;

                //add this move to the list of moves
                moves.add(new Move(sourceRow, sourceCol, firstClick.get(2), destRow, destCol, id,
                        !whiteTurn, enPassantPossible, false, castlingMove,
                        pieceCaptured, false, firstMoveChanged));

                updateUserView(destRow, destCol, id);
            }
            else if(enPassantPossible && board.enPassantValid(sourceRow, sourceCol, destRow, destCol, whiteTurn)) {
                if(whiteTurn)
                    whiteTurn=false;
                else {
                    whiteTurn = true;
                }
                enPassantPossible=false;

                updateUserView(destRow, destCol, id);

                // add this move to the list of moves
                moves.add(new Move(sourceRow, sourceCol, firstClick.get(2), destRow, destCol, id,
                        !whiteTurn, enPassantPossible, true, false,
                        null, false, false));

                //handle the visuals of the pawn piece that was captured
                //handle en passants along row 6 (on the chessboard)
                if(destRow==2 && destCol==0){
                    ImageButton capturedPawn = (ImageButton) findViewById(R.id.A5);
                    capturedPawn.setImageResource(0);
                } else if(destRow==2 && destCol==1){
                    ImageButton capturedPawn = (ImageButton) findViewById(R.id.B5);
                    capturedPawn.setImageResource(0);
                } else if(destRow==2 && destCol==2){
                    ImageButton capturedPawn = (ImageButton) findViewById(R.id.C5);
                    capturedPawn.setImageResource(0);
                } else if(destRow==2 && destCol==3){
                    ImageButton capturedPawn = (ImageButton) findViewById(R.id.D5);
                    capturedPawn.setImageResource(0);
                } else if(destRow==2 && destCol==4){
                    ImageButton capturedPawn = (ImageButton) findViewById(R.id.E5);
                    capturedPawn.setImageResource(0);
                } else if(destRow==2 && destCol==5){
                    ImageButton capturedPawn = (ImageButton) findViewById(R.id.F5);
                    capturedPawn.setImageResource(0);
                } else if(destRow==2 && destCol==6){
                    ImageButton capturedPawn = (ImageButton) findViewById(R.id.G5);
                    capturedPawn.setImageResource(0);
                } else if(destRow==2 && destCol==7){
                    ImageButton capturedPawn = (ImageButton) findViewById(R.id.H5);
                    capturedPawn.setImageResource(0);
                }
                // handle en passants along row 3 (on the chessboard)
                if(destRow==5 && destCol==0){
                    ImageButton capturedPawn = (ImageButton) findViewById(R.id.A4);
                    capturedPawn.setImageResource(0);
                } else if(destRow==5 && destCol==1){
                    ImageButton capturedPawn = (ImageButton) findViewById(R.id.B4);
                    capturedPawn.setImageResource(0);
                } else if(destRow==5 && destCol==2){
                    ImageButton capturedPawn = (ImageButton) findViewById(R.id.C4);
                    capturedPawn.setImageResource(0);
                } else if(destRow==5 && destCol==3){
                    ImageButton capturedPawn = (ImageButton) findViewById(R.id.D4);
                    capturedPawn.setImageResource(0);
                } else if(destRow==5 && destCol==4){
                    ImageButton capturedPawn = (ImageButton) findViewById(R.id.E4);
                    capturedPawn.setImageResource(0);
                } else if(destRow==5 && destCol==5){
                    ImageButton capturedPawn = (ImageButton) findViewById(R.id.F4);
                    capturedPawn.setImageResource(0);
                } else if(destRow==5 && destCol==6){
                    ImageButton capturedPawn = (ImageButton) findViewById(R.id.G4);
                    capturedPawn.setImageResource(0);
                } else if(destRow==5 && destCol==7){
                    ImageButton capturedPawn = (ImageButton) findViewById(R.id.H4);
                    capturedPawn.setImageResource(0);
                }

            }
            else {

                Toast.makeText(Game.this, "Illegal Move", Toast.LENGTH_LONG).show();
            }
            firstClick.clear();
        }
        else{
            firstClick.add(row);
            firstClick.add(col);
            firstClick.add(id);

        }
    }


    public void undoMove(){
        if(moves.size() < 1){
            Toast.makeText(Game.this,"nothing to undo, please make a move!",Toast.LENGTH_LONG).show();
        }

        Move moveToUndo = moves.remove(moves.size()-1);
        Piece originalPiece = board.board[moveToUndo.newLocRow][moveToUndo.newLocCol];
        if(originalPiece.getType() == "Pawn" && (moveToUndo.newLocCol == 7 || moveToUndo.newLocCol == 0)){
            String color = originalPiece.getName();
            originalPiece = new Pawn(color);
            originalPiece.setmoved(6);
        }
        if(originalPiece.getType() == "King" && Math.abs(moveToUndo.originalSquareRow - moveToUndo.newLocRow) == 2){
            if(moveToUndo.originalSquareRow == 0){

            }
            else{

            }
            return;
        }
        board.board[moveToUndo.originalSquareRow][moveToUndo.originalSquareCol] = originalPiece;
        board.board[moveToUndo.newLocRow][moveToUndo.newLocCol] = moveToUndo.capturedPiece;
        whiteTurn = moveToUndo.isTurn();
        if(moveToUndo.firstMove){
            board.board[moveToUndo.originalSquareRow][moveToUndo.originalSquareCol].setmoved(0);
        }
        ImageButton ogSquare = (ImageButton)findViewById(moveToUndo.originalID);
        ogSquare.setImageResource(0);
        if(board.board[moveToUndo.originalSquareRow][moveToUndo.originalSquareCol].getType() == "Free Space"){
            ogSquare.setImageResource(pieces.get(board.board[moveToUndo.originalSquareRow][moveToUndo.originalSquareCol].toString()));
        }

        // initially makes end Position blank, then fills in a piece there if one exists
        ImageButton newSquare = (ImageButton)findViewById(moveToUndo.newID);
        newSquare.setImageResource(0);
        if(board.board[moveToUndo.newLocRow][moveToUndo.newLocCol].getType() == "Free Space"){
            newSquare.setImageResource(pieces.get(board.board[moveToUndo.newLocRow][moveToUndo.newLocCol].toString()));
        }
    }

    public void AIMove(){
        
    }

    public void gameOver(Context context){
        EditText gameName = new EditText(context);
        AlertDialog alert = new AlertDialog.Builder(context).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = String.valueOf(gameName.getText());
                Date date = Calendar.getInstance().getTime();
                SavedGame newGame = new SavedGame(moves, date, name, draw, resign);
                saveGame(newGame, getApplicationContext());

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }).create();
        alert.setTitle("Game Over");
        alert.setMessage("Game has finished, would you like to save?");
        alert.setView(gameName);

        WindowManager.LayoutParams sizeCheck = new WindowManager.LayoutParams();
        sizeCheck.copyFrom(alert.getWindow().getAttributes());
        sizeCheck.width = WindowManager.LayoutParams.MATCH_PARENT;
        sizeCheck.height = WindowManager.LayoutParams.MATCH_PARENT;
        alert.show();

        alert.getWindow().setAttributes(sizeCheck);
    }

    private void saveGame(SavedGame tempGame, Context context){
        File file = new File(Game.this.getFilesDir(),"games");
        ArrayList<SavedGame> all_games = new ArrayList<SavedGame>();
        if(!file.exists()){
            file.mkdir();
        }
        else{
            all_games = readGames();

        }
        all_games.add(tempGame);
        try {
            File saveFile = new File(file,"savedGame");
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(saveFile));
            output.writeObject(all_games);

            Toast.makeText(Game.this, "Game saved!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<SavedGame> readGames(){
        try{
            File file = new File(Game.this.getFilesDir(),"games");
            File saveFile = new File(file,"savedGame");
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(saveFile));

            ArrayList<SavedGame> listGames = (ArrayList<SavedGame>) input.readObject();
            return listGames;
        }
        catch(Exception e){
            return new ArrayList<SavedGame>();
        }
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
                move(0, 0, a1.getId());
            }
        });

        ImageButton a2 = (ImageButton) findViewById(R.id.A2);
        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(0, 1, a2.getId());

            }
        });

        ImageButton a3 = (ImageButton) findViewById(R.id.A3);
        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(0, 2, a3.getId());
            }
        });
        ImageButton a4 = (ImageButton) findViewById(R.id.A4);
        a4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(0, 3, a4.getId());
            }
        });
        ImageButton a5 = (ImageButton) findViewById(R.id.A5);
        a5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(0, 4, a5.getId());
            }
        });
        ImageButton a6 = (ImageButton) findViewById(R.id.A6);
        a6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(0, 5, a6.getId());
            }
        });
        ImageButton a7 = (ImageButton) findViewById(R.id.A7);
        a7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(0, 6, a7.getId());
            }
        });
        ImageButton a8 = (ImageButton) findViewById(R.id.A8);
        a8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(0, 7, a8.getId());
            }
        });

        //Column B ImageButtons
        ImageButton b1 = (ImageButton) findViewById(R.id.B1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(1, 0, b1.getId());
            }
        });

        ImageButton b2 = (ImageButton) findViewById(R.id.B2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(1, 1, b2.getId());
            }
        });

        ImageButton b3 = (ImageButton) findViewById(R.id.B3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(1, 2, b3.getId());
            }
        });
        ImageButton b4 = (ImageButton) findViewById(R.id.B4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(1, 3, b4.getId());
            }
        });
        ImageButton b5 = (ImageButton) findViewById(R.id.B5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(1, 4, b5.getId());
            }
        });
        ImageButton b6 = (ImageButton) findViewById(R.id.B6);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(1, 5, b6.getId());
            }
        });
        ImageButton b7 = (ImageButton) findViewById(R.id.B7);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(1, 6, b7.getId());
            }
        });
        ImageButton b8 = (ImageButton) findViewById(R.id.B8);
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(1, 7, b4.getId());
            }
        });

        //Column C ImageButtons
        ImageButton c1 = (ImageButton) findViewById(R.id.C1);
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(2, 0, c1.getId());
            }
        });

        ImageButton c2 = (ImageButton) findViewById(R.id.C2);
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(2, 1, c2.getId());
            }
        });

        ImageButton c3 = (ImageButton) findViewById(R.id.C3);
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(2, 2, c3.getId());
            }
        });
        ImageButton c4 = (ImageButton) findViewById(R.id.C4);
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(2, 3, c4.getId());
            }
        });
        ImageButton c5 = (ImageButton) findViewById(R.id.C5);
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(2, 4, c5.getId());
            }
        });
        ImageButton c6 = (ImageButton) findViewById(R.id.C6);
        c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(2, 5, c6.getId());
            }
        });
        ImageButton c7 = (ImageButton) findViewById(R.id.C7);
        c7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(2, 6, c7.getId());
            }
        });
        ImageButton c8 = (ImageButton) findViewById(R.id.C8);
        c8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(2, 7, c8.getId());
            }
        });

        //Column D ImageButtons
        ImageButton d1 = (ImageButton) findViewById(R.id.D1);
        d1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(3, 0, d1.getId());
            }
        });

        ImageButton d2 = (ImageButton) findViewById(R.id.D2);
        d2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(3, 1, d2.getId());
            }
        });

        ImageButton d3 = (ImageButton) findViewById(R.id.D3);
        d3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(3, 2, d3.getId());
            }
        });
        ImageButton d4 = (ImageButton) findViewById(R.id.D4);
        d4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(3, 3, d4.getId());
            }
        });
        ImageButton d5 = (ImageButton) findViewById(R.id.D5);
        d5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(3, 4, d5.getId());
            }
        });
        ImageButton d6 = (ImageButton) findViewById(R.id.D6);
        d6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(3, 5, d6.getId());
            }
        });
        ImageButton d7 = (ImageButton) findViewById(R.id.D7);
        d7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(3, 6, d7.getId());
            }
        });
        ImageButton d8 = (ImageButton) findViewById(R.id.D8);
        d8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(3, 7, d8.getId());
            }
        });

        //Column E

        ImageButton e1 = (ImageButton) findViewById(R.id.E1);
        e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(4, 0, e1.getId());
            }
        });

        ImageButton e2 = (ImageButton) findViewById(R.id.E2);
        e2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(4, 1, e2.getId());
            }
        });

        ImageButton e3 = (ImageButton) findViewById(R.id.E3);
        e3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(4, 2, e3.getId());
            }
        });
        ImageButton e4 = (ImageButton) findViewById(R.id.E4);
        e4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(4, 3, e4.getId());
            }
        });
        ImageButton e5 = (ImageButton) findViewById(R.id.E5);
        e5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(4, 4, e5.getId());
            }
        });
        ImageButton e6 = (ImageButton) findViewById(R.id.E6);
        e6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(4, 5, e6.getId());
            }
        });
        ImageButton e7 = (ImageButton) findViewById(R.id.E7);
        e7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(4, 6, e7.getId());
            }
        });
        ImageButton e8 = (ImageButton) findViewById(R.id.E8);
        e8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(4, 7, e8.getId());
            }
        });
        ImageButton f1 = (ImageButton) findViewById(R.id.F1);
        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(5, 0, f1.getId());
            }
        });

        ImageButton f2 = (ImageButton) findViewById(R.id.F2);
        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(5, 1, f2.getId());
            }
        });

        ImageButton f3 = (ImageButton) findViewById(R.id.F3);
        f3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(5, 2, f3.getId());
            }
        });
        ImageButton f4 = (ImageButton) findViewById(R.id.F4);
        f4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(5, 3, f4.getId());
            }
        });
        ImageButton f5 = (ImageButton) findViewById(R.id.F5);
        f5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(5, 4, f5.getId());
            }
        });
        ImageButton f6 = (ImageButton) findViewById(R.id.F6);
        f6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(5, 5, f6.getId());
            }
        });
        ImageButton f7 = (ImageButton) findViewById(R.id.F7);
        f7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(5, 6, f7.getId());
            }
        });
        ImageButton f8 = (ImageButton) findViewById(R.id.F8);
        f8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(5, 7, f8.getId());
            }
        });

        ImageButton g1 = (ImageButton) findViewById(R.id.G1);
        g1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(6, 0, g1.getId());
            }
        });

        ImageButton g2 = (ImageButton) findViewById(R.id.G2);
        g2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(6, 1, g2.getId());
            }
        });

        ImageButton g3 = (ImageButton) findViewById(R.id.G3);
        g3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(6, 2, g3.getId());
            }
        });
        ImageButton g4 = (ImageButton) findViewById(R.id.G4);
        g4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(6, 3, g4.getId());
            }
        });
        ImageButton g5 = (ImageButton) findViewById(R.id.G5);
        g5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(6, 4, g5.getId());
            }
        });
        ImageButton g6 = (ImageButton) findViewById(R.id.G6);
        g6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(6, 5, g6.getId());
            }
        });
        ImageButton g7 = (ImageButton) findViewById(R.id.G7);
        g7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(6, 6, g7.getId());
            }
        });
        ImageButton g8 = (ImageButton) findViewById(R.id.G8);
        g8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(6, 7, g8.getId());
            }
        });

        ImageButton h1 = (ImageButton) findViewById(R.id.H1);
        h1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(7, 0, h1.getId());
            }
        });

        ImageButton h2 = (ImageButton) findViewById(R.id.H2);
        h2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(7, 1, h2.getId());
            }
        });

        ImageButton h3 = (ImageButton) findViewById(R.id.H3);
        h3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(7, 2, h3.getId());
            }
        });
        ImageButton h4 = (ImageButton) findViewById(R.id.H4);
        h4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(7, 3, h4.getId());
            }
        });
        ImageButton h5 = (ImageButton) findViewById(R.id.H5);
        h5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(7, 4, h5.getId());
            }
        });
        ImageButton h6 = (ImageButton) findViewById(R.id.H6);
        h6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(7, 5, h6.getId());
            }
        });
        ImageButton h7 = (ImageButton) findViewById(R.id.H7);
        h7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(7, 6, h7.getId());
            }
        });
        ImageButton h8 = (ImageButton) findViewById(R.id.H8);
        h8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(7, 7, h8.getId());
            }
        });
    }
}