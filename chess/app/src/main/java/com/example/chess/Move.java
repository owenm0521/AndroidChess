package com.example.chess;

import java.io.Serializable;

public class Move implements Serializable {
    int originalSquareRow;
    int originalSquareCol;
    int originalID;
    int newLocRow;
    int newLocCol;
    int newID;
    boolean turn;
    Piece capturedPiece;
    boolean firstMove;
    boolean enPassant;
    boolean pawnPromotion;
    Piece promoted;

    public Move(int originalSquareRow, int originalSquareCol, int originalID, int newLocRow,
                int newLocCol, int newID, boolean whiteTurn,
                boolean enPassant, Piece capturedPiece, boolean pawnPromotion,
                boolean firstMove) {
        this.originalSquareRow = originalSquareRow;
        this.originalSquareCol = originalSquareCol;
        this.originalID = originalID;
        this.newLocRow = newLocRow;
        this.newLocCol = newLocCol;
        this.newID = newID;
        this.turn = whiteTurn;
        this.capturedPiece = capturedPiece;
        this.firstMove  = firstMove;
        this.enPassant = enPassant;
        this.pawnPromotion = pawnPromotion;


    }
    public Move(int originalSquareRow, int originalSquareCol, int originalID, int newLocRow,
                int newLocCol, int newID, boolean whiteTurn,
                boolean enPassant, Piece capturedPiece, boolean pawnPromotion,
                boolean firstMove, Piece promotedTo) {
        this.originalSquareRow = originalSquareRow;
        this.originalSquareCol = originalSquareCol;
        this.originalID = originalID;
        this.newLocRow = newLocRow;
        this.newLocCol = newLocCol;
        this.newID = newID;
        this.turn = whiteTurn;
        this.capturedPiece = capturedPiece;
        this.firstMove  = firstMove;
        this.enPassant = enPassant;
        this.pawnPromotion = pawnPromotion;
        this.promoted = promotedTo;

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
