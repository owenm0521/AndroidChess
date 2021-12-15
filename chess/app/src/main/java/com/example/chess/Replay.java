package com.example.chess;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

    boolean enPassantPossible = false;
    // holds details of User's first click: first element is destRow num, second element is col num,
    // this element is ID of the square that was clicked
    private ArrayList<Integer> firstClick = new ArrayList<Integer>();
    // stores all the moves in this game
    private ArrayList<Move> moves = new ArrayList<Move>();
    // maps the toString representation of a Piece to its drawable
    private HashMap<String, Integer> pieces = new HashMap<String, Integer>();
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

    public void move(Move move){
        int startRow = move.originalSquareRow;
        int startCol = move.originalSquareCol;
        int endCol = move.newLocCol;
        int endRow = move.newLocRow;
        boolean whiteTurn = move.isTurn();
        boolean enPassantPossible = move.enPassant;
        boolean enPassantMove = move.enPassant;
        int id = move.newID;
        int firstClickID = move.originalID;


            int sourceRow = startRow;
            int sourceCol = startCol;



            int destCol = endCol;
            int destRow = endRow;

            ArrayList<Piece> capturedPiece = new ArrayList<Piece>();

            // stores Pieces's current firstMove value
            boolean firstMove = false;
            if(board.board[sourceRow][sourceCol]!=null){
                firstMove = board.board[sourceRow][sourceCol].getmoved();
            }
            Log.e("firstclick", sourceRow + " " + sourceCol + " " + firstClick.get(2));
            Log.e("secondclick", destRow + " " + destCol + " " + id);
            Character playerTurn = whiteTurn ? 'w' : 'b';

            if(board.move(playerTurn, sourceRow, sourceCol, destRow, destCol, capturedPiece)) {
                Log.e("games", "legal");
                whiteTurn = !whiteTurn;

                if((sourceRow == destRow+2 || sourceRow == destRow-2) && sourceCol==destCol && board.board[destRow][destCol] instanceof Pawn) {
                    enPassantPossible = true;
                }
                else {
                    enPassantPossible = false;
                }

                // opens dialog for user to handle promotion when applicable (and handles the rest of promotion there)
                if(board.board[destRow][destCol] instanceof Pawn && (destRow==0 || destRow==7)){
                    if(capturedPiece.size() < 1){
                        capturedPiece.add(new FreeSpace(destRow, destCol));
                    }
                    promotion(Replay.this, sourceRow, sourceCol, destRow, destCol, !whiteTurn, capturedPiece, id, firstMove);
                    return;
                }


                Piece pieceCaptured = new FreeSpace(destRow, destCol);
                if(capturedPiece.size()>0) pieceCaptured = capturedPiece.get(0);

                boolean castlingMove = false;
                if(Math.abs(sourceCol-destCol)==2 && board.board[destRow][destCol] instanceof King){
                    castlingMove = true;
                }

                // records whether a piece's first move was changed
                boolean firstMoveChanged = firstMove != board.board[destRow][destCol].getmoved();

                //add this move to the list of moves
                Log.e("move",destRow+" "+destCol);
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
                        !whiteTurn,  true,
                        null, false, false));

                //handle the visuals of the pawn piece that was captured
                //handle en passants along destRow 6 (on the chessboard)
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
                // handle en passants along destRow 3 (on the chessboard)
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

                Toast.makeText(Replay.this, "Illegal Move", Toast.LENGTH_LONG).show();
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
        int temp = destRow;
        destRow = destCol;
        destCol = temp;
        destRow = 7-destRow;
        Log.e("updateView",destRow+" "+destCol);
        secondPiece.setImageResource(pieces.get(board.board[destRow][destCol].getName()));

        Character playerTurn = whiteTurn ? 'w' : 'b';
        if (board.checkmate(playerTurn)) {
            if (!whiteTurn) {
                Toast.makeText(Replay.this,"CheckMate, White Wins", Toast.LENGTH_LONG).show();
            } else {
                //System.out.println("Black wins");
                Toast.makeText(Replay.this,"CheckMate, Black Wins", Toast.LENGTH_LONG).show();
            }
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
}