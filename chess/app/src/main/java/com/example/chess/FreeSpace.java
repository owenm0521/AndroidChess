/**
 * @author Ali Khan and Owen Morris
 */
package com.example.chess;

/**
 * Free space class for the spaces on the board where there are no pieces
 */
public class FreeSpace implements Piece{
	/**
	 * name the name of the piece specified by color and type
	 * type the type of piece
	 */
	String name; 
	String type; 
	
	/**
	 * Constructs the free space piece
	 * 
	 * @param color what color the piece is
	 */
	public FreeSpace (int row, int col) {
		this.type = "Free Space"; 
		if((row + col) % 2 == 1){
			//this.type = "black space"; 
			this.name = "## "; 
		}else {
			//this.type = "white space"; 
			this.name = "   "; 
		}
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
		return false;
	}
	/**
	 * returns moved boolean
	 * @return moved 
	 */
	public boolean getmoved() {
		return false;
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
