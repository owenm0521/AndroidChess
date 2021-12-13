package com.example.chess;

public class Move {
    int originalSquareRow;
    int originalSquareCol;
    int firstClickID;
    int newLocRow;
    int newLocCol;
    int secondClickID;
    boolean turn;

    public Move(int originalSquareRow, int originalSquareCol, int firstClickID, int newLocRow,
                int newLocCol, int secondClickID, boolean whiteTurn, boolean enPassantPossible,
                boolean enPassantMove, boolean castlingMove, Piece capturedPiece, boolean pawnPromotion,
                boolean firstMoveChanged) {
        this.originalSquareRow = originalSquareRow;
        this.originalSquareCol = originalSquareCol;
        this.firstClickID = firstClickID;
        this.newLocRow = newLocRow;
        this.newLocCol = newLocCol;
        this.secondClickID = secondClickID;
        this.turn = turn;
    }

    public int getOriginalSquareRow() {
        return originalSquareRow;
    }

    public int getOriginalSquareCol() {
        return originalSquareCol;
    }

    public int getFirstClickID() {
        return firstClickID;
    }

    public int getNewLocRow() {
        return newLocRow;
    }

    public int getNewLocCol() {
        return newLocCol;
    }

    public int getSecondClickID() {
        return secondClickID;
    }

    public boolean isTurn() {
        return turn;
    }
}
