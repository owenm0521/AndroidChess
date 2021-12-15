/**
 * @Author Ali Khan and Owen Morris
 */
package com.example.chess;

/**
 * Rook class for the rook piece
 */
public class Rook implements Piece {
	/**
	 * name the name of the piece specified by color and type
	 * type the type of piece
	 * moved becomes true once piece move
	 */
	String name; 
	String type = "Rook";
	boolean moved = false;
	
	/**
	 * Constructs the rook piece
	 * 
	 * @param color what color the piece is
	 */
	public Rook (String color) {
		this.name = "" + color.charAt(0) + type.charAt(0);
	}
	
	/**
	 * returns the name of the piece
	 * @return name of piece
	 */
	public String getName() {
		return this.name; 
	}
	/**
	 * returns the type of piece
	 * @return type of piece
	 */
	public String getType() {
		return this.type; 
	}
	/**
	 * returns whether a move is legal based on piece type
	 * 
	 * @param c_row current row of piece
	 * @param c_col current column of piece
	 * @param n_col new column of piece
	 * @param n_row new row of piece
	 * @return true if move is legal and false otherwise
	 */
	public boolean check_move(int c_row, int c_col, int n_row, int n_col) {
		if(n_col == c_col || n_row == c_row) {
			moved = true;
			return true; 
		}
		return false;
	}

    @Override
    public void setmoved(int n) {
        moved = false;
    }

    /**
	 * returns moved boolean
	 * @return moved 
	 */
	public boolean getmoved() {
		return moved;
	}

	/**
	 * test method that returns zero
	 * @return 0
	 */
	@Override
	public char getColor() {
		// TODO Auto-generated method stub
		return 0;
	}
}
