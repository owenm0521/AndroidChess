/**
 * @author Ali Khan and Owen Morris
 */

package com.example.chess;

/**
 * King class for the king piece of the board
 */
public class King implements Piece {
	/** 
	 * name the name of the piece specified by color and type
	 * type the type of piece
	 * moved a boolean to tell whether a piece is moved
	 */
	String name; 
	String type = "King"; 
	boolean moved = false;
	
	/**
	 * Constructs the king piece
	 * 
	 * @param color what color the piece is
	 */
	public King (String color) {
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
		if(Math.abs(n_row - c_row) <= 1 && Math.abs(n_col - c_col) <= 1) {
			moved = true;
			return true; 
		}
		if(!moved && Math.abs(n_row - c_row) == 0 && Math.abs(n_col-c_col) == 2) {
			moved=true;
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
