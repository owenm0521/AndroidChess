package com.example.chess;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
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
import java.util.Iterator;
import java.util.Map;

public class Game extends AppCompatActivity {

    public HashMap<Integer, int[]> findSquares = new HashMap<Integer, int[]>();
    public HashMap<int[], Integer> findID = new HashMap<int[], Integer>();
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
                firstMove = board.board[sourceRow][sourceCol].getmoved();
            }
            Log.e("games", row + " " + col + " " + id);
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
                    promotion(Game.this, sourceRow, sourceCol, destRow, destCol, !whiteTurn, capturedPiece, id, firstMove);
                    return;
                }


                Piece pieceCaptured = null;
                if(capturedPiece.size()>0) pieceCaptured = capturedPiece.get(0);

                boolean castlingMove = false;
                if(Math.abs(sourceCol-destCol)==2 && board.board[destRow][destCol] instanceof King){
                    castlingMove = true;
                }

                // records whether a piece's first move was changed
                boolean firstMoveChanged = firstMove != board.board[destRow][destCol].getmoved();

                //add this move to the list of moves
                moves.add(new Move(sourceRow, sourceCol, firstClick.get(2), destRow, destCol, id,
                        !whiteTurn, false,
                        pieceCaptured, false, firstMoveChanged));

                updateUserView(destRow, destCol, id);
            }
            else if(enPassantPossible && board.enPassantMove(playerTurn, sourceRow, sourceCol, destRow, destCol, capturedPiece)) {
                if(whiteTurn)
                    whiteTurn=false;
                else {
                    whiteTurn = true;
                }
                enPassantPossible=false;

                updateUserView(destRow, destCol, id);

                // add this move to the list of moves
                moves.add(new Move(sourceRow, sourceCol, firstClick.get(2), destRow, destCol, id,
                        !whiteTurn,  false,
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

    public void updateUserView(int destRow, int destCol, int id){

        ImageButton firstPiece = (ImageButton)findViewById(firstClick.get(2));
        firstPiece.setImageResource(0);


        // handle castling first
        if(board.board[destRow][destCol] instanceof King && Math.abs(firstClick.get(1)-destCol)==2){
            ImageButton secondPiece = (ImageButton)findViewById(id);
            // castling black E8 --> C8
            if(destCol==2 && destRow==0){
                secondPiece.setImageResource(R.drawable.bk);
                ImageButton movingRook = (ImageButton)findViewById(R.id.A8);
                movingRook.setImageResource(0);
                ImageButton movingRookDestination = (ImageButton)findViewById(R.id.D8);
                movingRookDestination.setImageResource(R.drawable.br);
            }
            // castling black E8 --> G8
            if(destCol==6 && destRow==0){
                secondPiece.setImageResource(R.drawable.bk);
                ImageButton movingRook = (ImageButton)findViewById(R.id.H8);
                movingRook.setImageResource(0);
                ImageButton movingRookDestination = (ImageButton)findViewById(R.id.F8);
                movingRookDestination.setImageResource(R.drawable.br);
            }
            // castling white E1 --> G1
            if(destCol==6 && destRow==7){
                secondPiece.setImageResource(R.drawable.wk);
                ImageButton movingRook = (ImageButton)findViewById(R.id.H1);
                movingRook.setImageResource(0);
                ImageButton movingRookDestination = (ImageButton)findViewById(R.id.F1);
                movingRookDestination.setImageResource(R.drawable.wr);
            }
            // castling white E1 --> C1
            if(destCol==2 && destRow==7){
                secondPiece.setImageResource(R.drawable.wk);
                ImageButton movingRook = (ImageButton)findViewById(R.id.A1);
                movingRook.setImageResource(0);
                ImageButton movingRookDestination = (ImageButton)findViewById(R.id.D1);
                movingRookDestination.setImageResource(R.drawable.wr);
            }
            return;
        }

        // updates virtual board's appearance using pieceToKey hashmap
        ImageButton secondPiece = (ImageButton)findViewById(id);
        secondPiece.setImageResource(pieces.get(board.board[destRow][destCol].toString()));

        Character playerTurn = whiteTurn ? 'w' : 'b';
        if (board.checkmate(playerTurn)) {
            if (!whiteTurn) {
                Toast.makeText(Game.this,"CheckMate, White Wins", Toast.LENGTH_LONG).show();
            } else {
                //System.out.println("Black wins");
                Toast.makeText(Game.this,"CheckMate, Black Wins", Toast.LENGTH_LONG).show();
            }
            gameOver(Game.this);
        }
    }

    public void promotion(Context c, int sourceRow, int sourceCol, int destRow, int destCol, boolean whiteTurn,
                                 ArrayList<Piece> capturedPiece, int id, boolean firstMove){
        android.app.AlertDialog.Builder buildPromotionList = new android.app.AlertDialog.Builder(c);
        buildPromotionList.setTitle("Promote pawn to one of the following pieces:");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(c, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Queen");
        arrayAdapter.add("Rook");
        arrayAdapter.add("Bishop");
        arrayAdapter.add("Knight");
        String color = whiteTurn ? "white" : "black";
        buildPromotionList.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);

                if(strName.equals("Queen")){
                    board.board[destRow][destCol] = new Queen(color);
                } else if(strName.equals("Rook")){
                    board.board[destRow][destCol] = new Rook(color);
                } else if(strName.equals("Bishop")){
                    board.board[destRow][destCol] = new Bishop(color);
                } else if(strName.equals("Knight")){
                    board.board[destRow][destCol] = new Knight(color);
                }

                /*
                board.inCheck = board.checkCheck((whiteTurn));
                board.inCheckMate = board.checkCheckMate(whiteTurn);
                */

                Piece pieceCaptured = null;
                if(capturedPiece.size()>0) pieceCaptured = capturedPiece.get(0);

                boolean castlingMove = false;

                // records whether a piece's first move was changed
                boolean firstMoveChanged = firstMove != board.board[destRow][destCol].getmoved();

                //add this move to the list of moves (this currently assumes no pawn promotion)
                moves.add(new Move(sourceRow, sourceCol, firstClick.get(2), destRow, destCol, id,
                        whiteTurn, false,
                        pieceCaptured, true, firstMoveChanged, board.board[destRow][destCol]));

                updateUserView(destRow, destCol, id);

                firstClick.clear();
            }
        });
        Dialog d = buildPromotionList.setView(new View(c)).create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        d.show();
        d.getWindow().setAttributes(lp);
        //Log.d("savedGames: ", SavedGames.userSavedGames.toString());
    }


    public void undoMove(){
        if(moves.size() < 1){
            Toast.makeText(Game.this,"nothing to undo, please make a move!",Toast.LENGTH_LONG).show();
        }

        Move moveToUndo = moves.remove(moves.size()-1);
        Piece originalPiece = board.board[moveToUndo.newLocRow][moveToUndo.newLocCol];
        String temp_color = originalPiece.getName();
        if(originalPiece.getType() == "Pawn" && (moveToUndo.newLocCol == 7 || moveToUndo.newLocCol == 0)){
            originalPiece = new Pawn(temp_color);
            originalPiece.setmoved(6);
        }
        if(originalPiece.getType() == "King" && Math.abs(moveToUndo.originalSquareRow - moveToUndo.newLocRow) == 2){
            ImageButton kingsCurrent = null;
            ImageButton rooksCurrent = null;
            ImageButton originalKing = null;
            ImageButton originalRook = null;

            if(moveToUndo.originalSquareRow == 0){
                if(moveToUndo.newLocCol==6){

                    board.board[0][6] = null;
                    kingsCurrent = (ImageButton) findViewById(R.id.G8);
                    board.board[0][5] = null;
                    rooksCurrent = (ImageButton) findViewById(R.id.F8);
                    board.board[0][7] = new Rook(temp_color);
                    originalRook = (ImageButton) findViewById(R.id.H8);
                }
                //castling left
                if(moveToUndo.newLocCol==2){
                    board.board[0][2] = null;
                    kingsCurrent = (ImageButton) findViewById(R.id.C8);
                    board.board[0][3] = null;
                    rooksCurrent = (ImageButton) findViewById(R.id.D8);
                    board.board[0][0] = new Rook(temp_color);
                    originalRook = (ImageButton) findViewById(R.id.A8);
                }
                board.board[0][4] = new King(temp_color);
                originalKing = (ImageButton) findViewById(R.id.E8);
            }
            else{
                if(moveToUndo.newLocCol==6){
                    board.board[7][6] = null;
                    kingsCurrent = (ImageButton) findViewById(R.id.G1);
                    board.board[7][5] = null;
                    rooksCurrent = (ImageButton) findViewById(R.id.F1);
                    board.board[7][7] = new Rook(temp_color);
                    originalRook = (ImageButton) findViewById(R.id.H1);
                }
                //castling left
                if(moveToUndo.newLocCol==2){
                    board.board[7][2] = null;
                    kingsCurrent = (ImageButton) findViewById(R.id.C1);
                    board.board[7][3] = null;
                    rooksCurrent = (ImageButton) findViewById(R.id.D1);
                    board.board[7][0] = new Rook(temp_color);
                    originalRook = (ImageButton) findViewById(R.id.A1);
                }
                board.board[7][4] = new King(temp_color);
                originalKing = (ImageButton) findViewById(R.id.E1);
            }
            kingsCurrent.setImageResource(0);
            rooksCurrent.setImageResource(0);
            originalRook.setImageResource(pieces.get((new Rook(temp_color).toString())));
            originalKing.setImageResource(pieces.get((new King(temp_color).toString())));
            return;
        }

        if(moveToUndo.enPassant == true){
            Piece movedPawn = board.board[moveToUndo.newLocRow][moveToUndo.newLocCol];
            board.board[moveToUndo.originalSquareRow][moveToUndo.originalSquareCol] = movedPawn;
            board.board[moveToUndo.newLocRow][moveToUndo.newLocCol] = null;
            String color = movedPawn.getColor() == 'w' ? "black" : "white";
            Pawn capturedPawn = new Pawn(color);
            capturedPawn.num_moves = 0;
            board.board[moveToUndo.originalSquareRow][moveToUndo.newLocCol] = capturedPawn;
            enPassantPossible = false;
            // sets enPassantPossible to the previous value, if that value exists
            
            whiteTurn = moveToUndo.isTurn();

            ImageButton startPosition = (ImageButton)findViewById(moveToUndo.originalID);
            startPosition.setImageResource(0);
            if(board.board[moveToUndo.originalSquareRow][moveToUndo.originalSquareCol]!=null){
                startPosition.setImageResource(pieces.get(board.board[moveToUndo.originalSquareRow][moveToUndo.originalSquareCol].toString()));
            }
            ImageButton endPosition = (ImageButton)findViewById(moveToUndo.newID);
            endPosition.setImageResource(0);
            if(board.board[moveToUndo.newLocRow][moveToUndo.newLocCol]!=null){
                endPosition.setImageResource(pieces.get(board.board[moveToUndo.newLocRow][moveToUndo.newLocCol].toString()));
            }

            if(moveToUndo.originalSquareRow==3){
                ImageButton capturedPawnPosition = null;
                if(moveToUndo.newLocCol==0){
                    capturedPawnPosition = (ImageButton) findViewById(R.id.A5);
                } else if(moveToUndo.newLocCol==1){
                    capturedPawnPosition = (ImageButton) findViewById(R.id.B5);
                } else if(moveToUndo.newLocCol==2){
                    capturedPawnPosition = (ImageButton) findViewById(R.id.C5);
                } else if(moveToUndo.newLocCol==3){
                    capturedPawnPosition = (ImageButton) findViewById(R.id.D5);
                } else if(moveToUndo.newLocCol==4){
                    capturedPawnPosition = (ImageButton) findViewById(R.id.E5);
                } else if(moveToUndo.newLocCol==5){
                    capturedPawnPosition = (ImageButton) findViewById(R.id.F5);
                } else if(moveToUndo.newLocCol==6){
                    capturedPawnPosition = (ImageButton) findViewById(R.id.G5);
                } else if(moveToUndo.newLocCol==7){
                    capturedPawnPosition = (ImageButton) findViewById(R.id.H5);
                }
                capturedPawnPosition.setImageResource(R.drawable.bp);
            } else if(moveToUndo.originalSquareRow==4){
                ImageButton capturedPawnPosition = null;
                if(moveToUndo.newLocCol==0){
                    capturedPawnPosition = (ImageButton) findViewById(R.id.A4);
                } else if(moveToUndo.newLocCol==1){
                    capturedPawnPosition = (ImageButton) findViewById(R.id.B4);
                } else if(moveToUndo.newLocCol==2){
                    capturedPawnPosition = (ImageButton) findViewById(R.id.C4);
                } else if(moveToUndo.newLocCol==3){
                    capturedPawnPosition = (ImageButton) findViewById(R.id.D4);
                } else if(moveToUndo.newLocCol==4){
                    capturedPawnPosition = (ImageButton) findViewById(R.id.E4);
                } else if(moveToUndo.newLocCol==5){
                    capturedPawnPosition = (ImageButton) findViewById(R.id.F4);
                } else if(moveToUndo.newLocCol==6){
                    capturedPawnPosition = (ImageButton) findViewById(R.id.G4);
                } else if(moveToUndo.newLocCol==7){
                    capturedPawnPosition = (ImageButton) findViewById(R.id.H4);
                }
                capturedPawnPosition.setImageResource(R.drawable.wp);
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
        for(int i = 0; i < board.board.length; i++){
            for(int j = 0; j < board.board[i].length;j++){
                Piece pp = board.board[i][j];
                if(pp.getType() != "Free Space"){
                    Character c = pp.getColor();
                    if((whiteTurn == false && c == 'b') || (whiteTurn == true && c == 'w')){
                        for(int a = 0; a<board.board.length; a++) {
                            for (int b = 0; b < board.board[a].length; b++) {
                                ArrayList<Piece> capturedPiece = new ArrayList<Piece>();
                                ArrayList<Integer> startMatrixPos = new ArrayList<Integer>();
                                startMatrixPos.add(i);
                                startMatrixPos.add(j);
                                ArrayList<Integer> endMatrixPos = new ArrayList<Integer>();
                                endMatrixPos.add(a);
                                endMatrixPos.add(b);
                                int startID = findID.get(startMatrixPos);
                                int endID = findID.get(endMatrixPos);
                                firstClick.add(i); firstClick.add(j); firstClick.add(startID);



                                if(board.move(c, i, j, a, b, capturedPiece)) {
                                    if(whiteTurn)
                                        whiteTurn=false;
                                    else
                                        whiteTurn = true;
                                    if((i == a+2 || i == a-2) && j==b && board.board[a][b] instanceof Pawn) {
                                        enPassantPossible = true;
                                    }
                                    else {
                                        enPassantPossible = false;
                                    }

                                    // CHANGE PROMOTION FOR AI
                                    if(board.board[a][b] instanceof Pawn && (a==0 || a==7)){

                                        board.board[a][b] = new Queen(c.toString());

                                        Piece pieceCaptured = null;
                                        if(capturedPiece.size()>0) pieceCaptured = capturedPiece.get(0);

                                        boolean castlingMove = false;


                                        //add this move to the list of moves
                                        moves.add(new Move(i, j, startID, a, b, endID,
                                                !whiteTurn, false,
                                                pieceCaptured, true, false, board.board[a][b]));

                                        updateUserView(a, b, endID);

                                        firstClick.clear();
                                        return;
                                    }


                                    Piece pieceCaptured = null;
                                    if(capturedPiece.size()>0) pieceCaptured = capturedPiece.get(0);

                                    boolean castlingMove = false;
                                    if(Math.abs(j-b)==2 && board.board[a][b] instanceof King){
                                        castlingMove = true;
                                    }


                                    //add this move to the list of moves
                                    moves.add(new Move(i, j, startID, a, b, endID,
                                            !whiteTurn, false,
                                            pieceCaptured, false, false));

                                    updateUserView(a, b, endID);
                                    firstClick.clear();
                                    return;
                                } else if(enPassantPossible && board.enPassantValid(board.board, c, i, j, a, b)) {
                                    if(whiteTurn)
                                        whiteTurn=false;
                                    else {
                                        whiteTurn = true;
                                    }
                                    enPassantPossible=false;

                                    updateUserView(a, b, endID);

                                    // add this move to the list of moves
                                    moves.add(new Move(i, j, startID, a, b, endID,
                                            !whiteTurn, true,
                                            null, false, false));

                                    //handle the visuals of the pawn piece that was captured
                                    //handle en passants along row 6 (on the chessboard)
                                    if(a==2 && b==0){
                                        ImageButton capturedPawn = (ImageButton) findViewById(R.id.A5);
                                        capturedPawn.setImageResource(0);
                                    } else if(a==2 && b==1){
                                        ImageButton capturedPawn = (ImageButton) findViewById(R.id.B5);
                                        capturedPawn.setImageResource(0);
                                    } else if(a==2 && b==2){
                                        ImageButton capturedPawn = (ImageButton) findViewById(R.id.C5);
                                        capturedPawn.setImageResource(0);
                                    } else if(a==2 && b==3){
                                        ImageButton capturedPawn = (ImageButton) findViewById(R.id.D5);
                                        capturedPawn.setImageResource(0);
                                    } else if(a==2 && b==4){
                                        ImageButton capturedPawn = (ImageButton) findViewById(R.id.E5);
                                        capturedPawn.setImageResource(0);
                                    } else if(a==2 && b==5){
                                        ImageButton capturedPawn = (ImageButton) findViewById(R.id.F5);
                                        capturedPawn.setImageResource(0);
                                    } else if(a==2 && b==6){
                                        ImageButton capturedPawn = (ImageButton) findViewById(R.id.G5);
                                        capturedPawn.setImageResource(0);
                                    } else if(a==2 && b==7){
                                        ImageButton capturedPawn = (ImageButton) findViewById(R.id.H5);
                                        capturedPawn.setImageResource(0);
                                    }
                                    // handle en passants along row 3 (on the chessboard)
                                    if(a==5 && b==0){
                                        ImageButton capturedPawn = (ImageButton) findViewById(R.id.A4);
                                        capturedPawn.setImageResource(0);
                                    } else if(a==5 && b==1){
                                        ImageButton capturedPawn = (ImageButton) findViewById(R.id.B4);
                                        capturedPawn.setImageResource(0);
                                    } else if(a==5 && b==2){
                                        ImageButton capturedPawn = (ImageButton) findViewById(R.id.C4);
                                        capturedPawn.setImageResource(0);
                                    } else if(a==5 && b==3){
                                        ImageButton capturedPawn = (ImageButton) findViewById(R.id.D4);
                                        capturedPawn.setImageResource(0);
                                    } else if(a==5 && b==4){
                                        ImageButton capturedPawn = (ImageButton) findViewById(R.id.E4);
                                        capturedPawn.setImageResource(0);
                                    } else if(a==5 && b==5){
                                        ImageButton capturedPawn = (ImageButton) findViewById(R.id.F4);
                                        capturedPawn.setImageResource(0);
                                    } else if(a==5 && b==6){
                                        ImageButton capturedPawn = (ImageButton) findViewById(R.id.G4);
                                        capturedPawn.setImageResource(0);
                                    } else if(a==5 && b==7){
                                        ImageButton capturedPawn = (ImageButton) findViewById(R.id.H4);
                                        capturedPawn.setImageResource(0);
                                    }
                                    firstClick.clear();
                                    return;
                                }

                                firstClick.clear();
                            }
                        }
                    }

                }
            }
        }
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

        // first row inputted to HashMap
        findID.put(new int[]{0,0}, R.id.A8);
        findID.put(new int[]{0,1}, R.id.B8);
        findID.put(new int[]{0,2}, R.id.C8);
        findID.put(new int[]{0,3}, R.id.D8);
        findID.put(new int[]{0,4}, R.id.E8);
        findID.put(new int[]{0,5}, R.id.F8);
        findID.put(new int[]{0,6}, R.id.G8);
        findID.put(new int[]{0,7}, R.id.H8);

        // second row inputted to HashMap
        findID.put(new int[]{1,0}, R.id.A7);
        findID.put(new int[]{1,1}, R.id.B7);
        findID.put(new int[]{1,2}, R.id.C7);
        findID.put(new int[]{1,3}, R.id.D7);
        findID.put(new int[]{1,4}, R.id.E7);
        findID.put(new int[]{1,5}, R.id.F7);
        findID.put(new int[]{1,6}, R.id.G7);
        findID.put(new int[]{1,7}, R.id.H7);

        // third row inputted to HashMap
        findID.put(new int[]{2,0}, R.id.A6);
        findID.put(new int[]{2,1}, R.id.B6);
        findID.put(new int[]{2,2}, R.id.C6);
        findID.put(new int[]{2,3}, R.id.D6);
        findID.put(new int[]{2,4}, R.id.E6);
        findID.put(new int[]{2,5}, R.id.F6);
        findID.put(new int[]{2,6}, R.id.G6);
        findID.put(new int[]{2,7}, R.id.H6);

        // fourth row inputted to HashMap
        findID.put(new int[]{3,0}, R.id.A5);
        findID.put(new int[]{3,1}, R.id.B5);
        findID.put(new int[]{3,2}, R.id.C5);
        findID.put(new int[]{3,3}, R.id.D5);
        findID.put(new int[]{3,4}, R.id.E5);
        findID.put(new int[]{3,5}, R.id.F5);
        findID.put(new int[]{3,6}, R.id.G5);
        findID.put(new int[]{3,7}, R.id.H5);

        // fifth row inputted to HashMap
        findID.put(new int[]{4,0}, R.id.A4);
        findID.put(new int[]{4,1}, R.id.B4);
        findID.put(new int[]{4,2}, R.id.C4);
        findID.put(new int[]{4,3}, R.id.D4);
        findID.put(new int[]{4,4}, R.id.E4);
        findID.put(new int[]{4,5}, R.id.F4);
        findID.put(new int[]{4,6}, R.id.G4);
        findID.put(new int[]{4,7}, R.id.H4);

        // sixth row inputted to HashMap
        findID.put(new int[]{5,0}, R.id.A3);
        findID.put(new int[]{5,1}, R.id.B3);
        findID.put(new int[]{5,2}, R.id.C3);
        findID.put(new int[]{5,3}, R.id.D3);
        findID.put(new int[]{5,4}, R.id.E3);
        findID.put(new int[]{5,5}, R.id.F3);
        findID.put(new int[]{5,6}, R.id.G3);
        findID.put(new int[]{5,7}, R.id.H3);

        // seventh row inputted to HashMap
        findID.put(new int[]{6,0}, R.id.A2);
        findID.put(new int[]{6,1}, R.id.B2);
        findID.put(new int[]{6,2}, R.id.C2);
        findID.put(new int[]{6,3}, R.id.D2);
        findID.put(new int[]{6,4}, R.id.E2);
        findID.put(new int[]{6,5}, R.id.F2);
        findID.put(new int[]{6,6}, R.id.G2);
        findID.put(new int[]{6,7}, R.id.H2);

        // eighth row inputted to HashMap
        findID.put(new int[]{7,0}, R.id.A1);
        findID.put(new int[]{7,1}, R.id.B1);
        findID.put(new int[]{7,2}, R.id.C1);
        findID.put(new int[]{7,3}, R.id.D1);
        findID.put(new int[]{7,4}, R.id.E1);
        findID.put(new int[]{7,5}, R.id.F1);
        findID.put(new int[]{7,6}, R.id.G1);
        findID.put(new int[]{7,7}, R.id.H1);

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