package com.example.chess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class SavedGame implements Serializable {
    ArrayList<Move> moves;
    Date date;
    String name;
    boolean draw;
    boolean resign;

    public SavedGame(ArrayList<Move> moves, Date date, String name, boolean draw, boolean resign){
        this.moves = moves;
        this.date = date;
        this.name = name;
        this.draw = draw;
        this.resign = resign;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public boolean isDraw() {
        return draw;
    }

    public boolean isResign() {
        return resign;
    }

    public String toString(){
        return this.name + " " + this.date;
    }
}
