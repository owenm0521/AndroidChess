/**
 * @Author Ali Khan and Owen Morris
 */

package com.example.chess;

import java.io.Serializable;

/**
 * King class for the king piece of the board
 */
public class Pawn implements Piece, Serializable {
	
	
	/**
	 * @field name the name of the piece specified by color and type
	 * @field type the type of piece
	 * @field moved a boolean to tell whether a piece is moved
	 * 
	 * @field num_moves the number of moves a pawn makes
	 * @field moved2 true if a pawn moves twice in first move
	 */
	String name; 
	String type = "Pawn"; 
	String color;
	
	int num_moves = 0; 
	boolean moved2 = false;
	
	/**
	 * returns moved boolean
	 * @return moved 
	 */
	public boolean getmoved() {
		return moved2;
	}
	/**
	 * Constructs the pawn piece
	 * @param color the color of the pawn
	 */
	public Pawn (String color) {
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
	 * test method that returns color of pawn
	 * @return color of pawn
	 */
	public char getColor() {
		return this.name.charAt(0);
	}
	
	/**
	 * returns whether a pawn can be enpassant'ed
	 * return true of false based on en passant rules
	 */
	public boolean getEnPassant() {
		if (num_moves == 1 && moved2) {
			return true;
		}
		return false;
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
	@Override
	public boolean check_move(int c_row, int c_col, int n_row, int n_col) {
		if(getColor() == 'w') {
			if (n_row >= c_row) {
				return false;
			}
			if (Math.abs(n_col - c_col) == 1 && c_row - n_row == 1) {
				num_moves++;
				return true;
			}
			else {
				if (n_row == (c_row - 2)) {
					if(num_moves == 0) {
						num_moves++;
						moved2 = true;
						return true;
					}
					else {
						return false;
					}
				}
				if (n_row == (c_row - 1) && c_col == n_col) {
						num_moves++;
						return true;
					}
				}
			return false;
	}
		else {
			if (n_row <= c_row) {
				return false;
			}
			if (Math.abs(n_col - c_col) == 1 && n_row - c_row == 1) {
				return true;
			}
			if (n_col < c_col || n_col > c_col) {
				return false;
			}
			else {
			if (n_row == (c_row + 2)) {
				if(num_moves == 0) {
					num_moves++;
					moved2 = true;
					return true;
				}
				else {
					return false;
				}
			}
			if (n_row == (c_row + 1) && c_col == n_col) {
					num_moves++;
					return true;
				}
			}
			return false;
		}
		}

	@Override
	public void setmoved(int n) {
		num_moves = n;
	}

	public String toString(){
		return this.name;
	}
}
