/**
 * @author Ali Khan and Owen Morris
 */
package com.example.chess;

import java.util.ArrayList;

/**
 * Player class for each player with a color and piece array
 *
 */
public class Player {
	/**
	 * color of player and pieces
	 * turn of each player
	 * array of pieces each player has
	 */
	String color; 
	boolean turn = false; 
	ArrayList<Piece> pieces = new ArrayList<Piece>(); 
	/**
	 * creates pieces for the player
	 * @param color color of players
	 */
	public Player(String color) {
		this.color = color; 
		for(int i = 0; i < 8; i++) {
			this.pieces.add(new Pawn(this.color)); 
		}
		this.pieces.add(new Rook(this.color)); 
		this.pieces.add(new Knight(this.color)); 
		this.pieces.add(new Bishop(this.color)); 
		this.pieces.add(new Queen(this.color)); 
		this.pieces.add(new King(this.color)); 
		this.pieces.add(new Bishop(this.color)); 
		this.pieces.add(new Knight(this.color)); 
		this.pieces.add(new Rook(this.color)); 
	}
}
