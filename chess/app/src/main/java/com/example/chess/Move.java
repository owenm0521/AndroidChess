package com.example.chess;

public class Move {
    int originalSquareRow;
    int originalSquareCol;
    int originalID;
    int newLocRow;
    int newLocCol;
    int newID;
    boolean turn;

    public Move(int originalSquareRow, int originalSquareCol, int originalID, int newLocRow,
                int newLocCol, int newID, boolean whiteTurn, boolean enPassantPossible,
                boolean enPassantMove, boolean castlingMove, Piece capturedPiece, boolean pawnPromotion,
                boolean firstMoveChanged) {
        this.originalSquareRow = originalSquareRow;
        this.originalSquareCol = originalSquareCol;
        this.originalID = originalID;
        this.newLocRow = newLocRow;
        this.newLocCol = newLocCol;
        this.newID = newID;
        this.turn = turn;
    }

    public int getOriginalSquareRow() {
        return originalSquareRow;
    }

    public int getOriginalSquareCol() {
        return originalSquareCol;
    }

    public int getoriginalID() {
        return originalID;
    }

    public int getNewLocRow() {
        return newLocRow;
    }

    public int getNewLocCol() {
        return newLocCol;
    }

    public int getnewID() {
        return newID;
    }

    public boolean isTurn() {
        return turn;
    }
}
