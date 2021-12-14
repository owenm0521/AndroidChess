/**
 * @author Ali Khan and Owen Morris
 */
package com.example.chess;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 *board handles all logic between pieces and game logic
 *
 */
public class Board {
	/**
	 * uses players black and white for each side
	 * board is a 2d array where the pieces can move 
	 */
	Player white; 
	Player black; 
	Piece[][] board; 
	/**
	 * Constructs the board for the game to begin
	 * 
	 * @param white white player
	 * @param black black player
	 */
	public Board(Player white, Player black) {
		this.white = white; this.black = black; 
		this.board = this.createBoard(); 
		
	}
	
	/*
	 * Point allows for easy conversion for array indices to chess moves
	 */
	public class Point {
		
		int row; 
		int col; 
		/**
		 * row represents first array (y)
		 * col represents index within array (x)
		 */
		
		/**
		 * creates the graph equivalent point
		 * @param gridPoint the board location as string
		 */
		public Point (String gridPoint) {
			this.row = 8 - Character.getNumericValue(gridPoint.charAt(1)); 
			char c = gridPoint.charAt(0); 
			switch(c) {
				case 'a': this.col = 0; break; 
				case 'b': this.col = 1; break; 
				case 'c': this.col = 2; break; 
				case 'd': this.col = 3; break; 
				case 'e': this.col = 4; break; 
				case 'f': this.col = 5; break; 
				case 'g': this.col = 6; break; 
				case 'h': this.col = 7; break; 
				default: this.col = -1; break; 
			}
		}
		
		/**
		 * 
		 * @param row row index (y)
		 * @param col columns index (x)
		 */
		public Point(int row, int col) {
			this.row = row; 
			this.col = col; 
		}
		
	}
	
	/**
	 * creates the board by adding freespace to entire array
	 * @return 2d piece array
	 */
	public Piece[][] createBoard() {
		Piece[][] board = new Piece[8][8]; 
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				board[i][j] = new FreeSpace(i, j); 
			}
		}
		return board; 
	}
	
	/**
	 * prints out board with whatever is on it and one empty line above
	 */
	public void printBoard() {
		System.out.println(); 
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				System.out.print(board[i][j].getName()); 
			}
			System.out.println(" " + (8-i)); 
		}
		System.out.print(" a  b  c  d  e  f  g  h \n\n");
	}
	
	/**
	 * adds each players initial peices
	 */
	public void populateBoard() {
		int bi = 1; int bj = 0;  
		for(Piece piece : black.pieces) {
			if(bj > 7) {
				bj = 0; 
				bi = 0; 
			}
			this.board[bi][bj] = piece; 
			bj++; 
		}
		
		int wi = 6; int wj = 0; 
		for(Piece piece : white.pieces) {
			if(wj > 7) {
				wj = 0; 
				wi = 7; 
			}
			this.board[wi][wj] = piece; 
			wj++; 
		}
	}
	
	/**
	 * checks the legality of a move within the context of the board, available spaces, and other pieces
	 * 
	 * @param board 2d piece array
	 * @param playerTurn either w or b representing white's turn or black's turn respectively
	 * @param source original location of piece
	 * @param dest potential new location of piece
	 * @return true if move is legal or false if not
	 */
	public boolean checkBoard(Piece[][] board, Character playerTurn, Point source, Point dest) {
		Piece sourcePiece = board[source.row][source.col];
		Piece destPiece = board[dest.row][dest.col];
		
		if(destPiece.getName().charAt(0) == playerTurn) {
			return false; 
		}
		
		int s_row = source.row; 
		int s_col = source.col; 
		int d_row = dest.row;
		int d_col = dest.col; 
		
		boolean moveRight = false; 
		boolean moveLeft = false; 
		boolean moveUp = false;
		boolean moveDown = false; 
		if(dest.col > source.col) {
			moveRight = true; 
		}else {
			moveLeft = true; 
		}
		if(dest.row > source.row) {
			moveDown = true; 
		}else {
			moveUp = true; 
		}
		
		boolean vertical = false; 
		boolean horizontal = false; 
		boolean diagonal = false; 
		if(dest.row == source.row) {
			horizontal = true; 
		}else if(dest.col == source.col) {
			vertical = true; 
		}else {
			diagonal = true; 
		}
		
		
		if(sourcePiece.getType().equals("King")) {
			if(s_row == 0 && d_row == 0) {
				if(Math.abs(s_col-d_col) == 2) {
					if(d_col == 2 ) {
						if(board[s_row][0].getType().equals("Rook") && board[s_row][0].getmoved()==false) {
							boolean all_empty = true;
							for(int i  = 1; i < 4;i++) {
								if(board[s_row][i].getType().equals("Free Space")){
									continue;
								}
								else {
									all_empty = false;
									break;
								}
							}
							if(all_empty) {
								board[s_row][d_col+1] = board[s_row][0];
								board[s_row][0] = new FreeSpace(s_row, 0);
								return true;
							}
							else {
								return false;
							}
						}
						else {
							return false;
						}
					}
					else if(d_col == 6) {
						if(board[s_row][7].getType().equals("Rook") && board[s_row][7].getmoved()==false) {
							boolean all_empty = true;
							for(int i  = 5; i < 7;i++) {
								if(board[s_row][i].getType().equals("Free Space")){
									continue;
								}
								else {
									all_empty = false;
									break;
								}
							}
							if(all_empty) {
								board[s_row][d_col-1] = board[s_row][0];
								board[s_row][7] = new FreeSpace(s_row, 7);
								return true;
							}
							else {
								return false;
							}
						}
						else {
							return false;
						}
					}
				}
			}
		}
		
		if(sourcePiece.getType().equals("Pawn")) {
			if(board[dest.row][dest.col].getType().equals("Free Space")) {
				if(diagonal) {
					// If diagonal movement check if the piece on its right or left are pawns
					if(board[source.row][dest.col].getType().equals("Pawn") && board[source.row][dest.col].getColor() != board[source.row][source.col].getColor()) {
						//check if en passant is legal
						if(((Pawn) board[source.row][dest.col]).getEnPassant()) {
							board[source.row][dest.col] = new FreeSpace(source.row, dest.col);
							if (playerTurn == 'b') {
								white.pieces.remove(destPiece); 
							}else {
								black.pieces.remove(destPiece); 
							}
						}
					}
					else {
						return false; 
					}
				}
			}
			else {
				if(diagonal && Math.abs(source.row - dest.row) == 1) {
					if (playerTurn == 'b') {
						white.pieces.remove(destPiece); 
					}else {
						black.pieces.remove(destPiece); 
					}
				}
				else {
					return false;
				}
			}
			/*
			if(Math.abs(source.row - dest.row) == 1) {
				if(!board[dest.row][dest.col].getType().equals("King")) {
					updatePawn(playerTurn, source, dest, promotion);
				}
			}
			*/
			return true; 
		}
		
		if(diagonal) {
			if(moveRight && moveUp) {
				d_row++; 
				d_col--; 
			}else if(moveRight && moveDown) {
				d_row--; 
				d_col--; 
			}else if(moveLeft && moveUp) {
				d_row++; 
				d_col++; 
			}else if(moveLeft && moveDown) {
				d_row--; 
				d_col++; 
			}else {
				return false; 
			}
			while(s_row != d_row && s_col != d_col) {
				if(moveRight && moveUp) {
					s_row--; 
					s_col++; 
				}else if(moveRight && moveDown) {
					s_row++; 
					s_col++; 
				}else if(moveLeft && moveUp) {
					s_row--; 
					s_col--; 
				}else if(moveLeft && moveDown) {
					s_row++; 
					s_col--; 
				}else {
					return false; 
				}
				if(!board[s_row][s_col].getType().equals("Free Space")) {
					return false; 
				}
			}  
		}else if(horizontal) {
			if(moveRight) {
				d_col--; 
			}else {
				d_col++; 
			}
			while(s_col != d_col) {
				if(moveRight) {
					s_col++; 
				}else {
					s_col--; 
				}
				if(!board[s_row][s_col].getType().equals("Free Space")) {
					return false; 
				}
			}
		}else if(vertical) {
			if(moveUp) {
				d_row++; 
			}else {
				d_row--; 
			}
			while(s_row != d_row) {
				if(moveUp) {
					s_row--; 
				}else {
					s_row++; 
				}
				if(!board[s_row][s_col].getType().equals("Free Space")) {
					return false; 
				}
			}
		}else {
			return false; 
		}
		
		if(!board[dest.row][dest.col].getType().equals("Free Space")) {
			if (playerTurn == 'b') {
				white.pieces.remove(destPiece); 
			}else {
				black.pieces.remove(destPiece); 
			}
		}
		return true; 
	}
	
	/**
	 * checks is given point is within the bounds of board array
	 * @param p point that has coordinates (x,y) to be checked
	 * @return true if within bounds of board otherwise false
	 */
	public boolean onBoard(Point p) {
		if(p.row < 0 || p.row > 7 || p.col < 0 || p.col > 7) {
			return false; 
		}
		return true; 
	}
	
	/**
	 * Once pawn reaches end of the board this method promotes the pawn to a higher piece class
	 * Defaults to Queen if no promotion string is provided
	 * 
	 * @param playerTurn 'w' or 'b' signifying player turn as white or black respectively
	 * @param source original location of pawn
	 * @param dest future location of pawn
	 * @param promotion the promotion string should be 'N' 'R' 'B' or 'Q' depending on promotion type
	 */
	/*
	public void updatePawn(Character playerTurn, Point source, Point dest, String promotion) {
		String newPieceType = promotion;
		if( (playerTurn == 'b' && dest.row == 7) || (playerTurn == 'w' && dest.row == 0) ) {
		}
		else {return;}
		Piece piece = null; 
		String color = playerTurn.toString(); 
		switch(newPieceType) {
			case "Q": piece = new Queen(color); break; 
			case "N": piece = new Knight(color); break; 
			case "R": piece = new Rook(color); break; 
			case "B": piece = new Bishop(color); break; 
			// case "pawn": piece = new Pawn(color); break; 
			default: piece = new Queen(color); break; 
		}
		if(playerTurn == 'b') {
			black.pieces.remove(board[source.row][source.col]); 
			black.pieces.add(piece); 
		}else {
			white.pieces.remove(board[source.row][source.col]); 
			white.pieces.add(piece); 
		}
		board[source.row][source.col] = piece; 
		return; 
	}
	*/
	
	/**
	 * when checknum is 0, checks to see if player's move will put themself in check before allowing move to be completed; 
	 * when cheknum is 1, checks to see if player's move, once completed, will put opponent in check 
	 * 
	 * @param board 2d piece array representing the board
	 * @param playerTurn 'w' or 'b' representing white or blacks turn respectively
	 * @param checkNum 0 = check if player's move puts themself in check (temp), 1 = check if player's move puts opponent in check (board)
	 * @return when checknum is 0 returns true if player's own king is in check, when checknum is 1 returns true if opponent's king is in check
	 */
	public boolean check(Piece[][] board, Character playerTurn, int checkNum) {
		Character player_to_check; 
		Character opponent; 
		if(checkNum == 0) {
			player_to_check = playerTurn; 
			opponent = playerTurn == 'w' ? 'b' : 'w'; 
		}else {
			player_to_check = playerTurn == 'w' ? 'b' : 'w'; 
			opponent = playerTurn; 
		}
		int king_row = 0; 
		int king_col = 0; 
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				if(board[i][j].getType().equals("King") && board[i][j].getName().charAt(0) == player_to_check) {
					king_row = i; 
					king_col = j; 
				}
			}
		}
		Point dest = new Point(king_row, king_col); 
		Piece curr_piece = null; 
		boolean check = false; 
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				curr_piece = board[i][j]; 
				if(board[i][j].getName().charAt(0) != player_to_check && !board[i][j].getType().equals("Free Space")) {
					Point source = new Point(i, j);
					check = (curr_piece.check_move(source.row, source.col, king_row, king_col) && checkBoard(board, opponent, source, dest)); // playerTurn = !playerTurn
					if (check) break;
				}
			}
			if (check) break;
		}
		return check; 
	}
	
	/**runs after turn to see if opponent is now in checkmate
	 * 
	 * @param playerTurn 'w' or 'b' representing white or black's turn respectively
	 * @return true if you are in checkmate and false if not
	 */
	public boolean checkmate(Character playerTurn) {
		boolean checkmate = true; 
		Piece king = null; 
		Point kingPos = new Point(0,0); 
		ArrayList<Point> kingMoves = new ArrayList<Point>(); 
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++){
				if(board[i][j].getType().equals("King") && board[i][j].getName().charAt(0) == playerTurn) {
					king = board[i][j]; 
					kingPos.row = i; 
					kingPos.col = j; 
				}
			}
		}
		
		ArrayList<Point> possibleMoves = new ArrayList<Point>(); 
		possibleMoves.add(new Point(kingPos.row, kingPos.col + 1)); 
		possibleMoves.add(new Point(kingPos.row, kingPos.col - 1));
		possibleMoves.add(new Point(kingPos.row + 1, kingPos.col));
		possibleMoves.add(new Point(kingPos.row - 1, kingPos.col));
		possibleMoves.add(new Point(kingPos.row + 1, kingPos.col + 1));
		possibleMoves.add(new Point(kingPos.row + 1, kingPos.col - 1));
		possibleMoves.add(new Point(kingPos.row - 1, kingPos.col + 1));
		possibleMoves.add(new Point(kingPos.row - 1, kingPos.col - 1));
		for(Point move : possibleMoves) {
			if (onBoard(move) && 
					( board[move.row][move.col].getType().equals("Free Space") || board[move.row][move.col].getName().charAt(0) != playerTurn ) ){
				kingMoves.add(move); 
			}
		}
		
		int checkNum = 0; 
		for (Point move : kingMoves) {
			Piece[][] tempBoard = createBoard(); 
			for(int i = 0; i < tempBoard.length; i++) {
				for(int j = 0; j < tempBoard[i].length; j++) {
					 tempBoard[i][j] = board[i][j]; 
				}
			}
			tempBoard = updateBoard(tempBoard, kingPos, move);
			if(!check(tempBoard, playerTurn, checkNum)) {
				checkmate = false; 
				return checkmate; 
			}
		}
		 
		Piece checkPiece = null; 
		Point checkPiecePos = new Point(0, 0);
		Character opponent = playerTurn == 'w' ? 'b' : 'w'; 
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++){
				if(board[i][j].getName().charAt(0) != playerTurn && !board[i][j].getType().equals("Free Space")) {
					Piece p = board[i][j]; 
					boolean pieceLegal = p.check_move(i, j, kingPos.row, kingPos.col);
					boolean boardLegal = checkBoard(board, opponent, new Point(i, j), kingPos);
					if(pieceLegal && boardLegal) {
						checkPiece = p;
						checkPiecePos.row = i; 
						checkPiecePos.col = j; 
					}
				}
			}
		}
		
		if(checkPiece == null) {
			checkmate = false; 
			return checkmate; 
		}
		
		boolean moveRight = false; 
		boolean moveLeft = false; 
		boolean moveUp = false;
		boolean moveDown = false; 
		if(kingPos.col > checkPiecePos.col) {
			moveRight = true; 
		}else {
			moveLeft = true; 
		}
		if(kingPos.row > checkPiecePos.row) {
			moveDown = true; 
		}else {
			moveUp = true; 
		}
		
		boolean vertical = false; 
		boolean horizontal = false; 
		boolean diagonal = false; 
		boolean knight = false; 
		if(kingPos.row == checkPiecePos.row) {
			horizontal = true; 
		}else if(kingPos.col == checkPiecePos.col) {
			vertical = true; 
		}else {
			diagonal = true; 
		}
		if (!vertical && !horizontal && !diagonal) {
			knight = true; 
		}
		
		ArrayList<Point> movesToBlock = new ArrayList<Point>(); 
		int sourceRow = checkPiecePos.row; 
		int sourceCol = checkPiecePos.col;
		int destRow = kingPos.row; 
		int destCol = kingPos.col; 
		
		movesToBlock.add(new Point(sourceRow, sourceCol)); 
		if(diagonal) {
			if(moveRight && moveUp) {
				destRow++; 
				destCol--; 
			}else if(moveRight && moveDown) {
				destRow--; 
				destCol--; 
			}else if(moveLeft && moveUp) {
				destRow++; 
				destCol++; 
			}else if(moveLeft && moveDown) {
				destRow--; 
				destCol++; 
			}else {
			}
			while(sourceRow != destRow && sourceCol != destCol) {
				if(moveRight && moveUp) {
					sourceRow--; 
					sourceCol++; 
				}else if(moveRight && moveDown) {
					sourceRow++; 
					sourceCol++; 
				}else if(moveLeft && moveUp) {
					sourceRow--; 
					sourceCol--; 
				}else if(moveLeft && moveDown) {
					sourceRow++; 
					sourceCol--; 
				}else {
				}
				movesToBlock.add(new Point(sourceRow, sourceCol)); 	
			}  
		}else if(horizontal) {
			if(moveRight) {
				destCol--; 
			}else {
				destCol++; 
			}
			while(sourceCol != destCol) {
				if(moveRight) {
					sourceCol++; 
				}else {
					sourceCol--; 
				}
				movesToBlock.add(new Point(sourceRow, sourceCol)); 	
			}
		}else if(vertical) {
			if(moveUp) {
				destRow++; 
			}else {
				destRow--; 
			}
			while(sourceRow != destRow) {
				if(moveUp) {
					sourceRow--; 
				}else {
					sourceRow++; 
				}
				movesToBlock.add(new Point(sourceRow, sourceCol)); 	
			}
		}else {
		}
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++){
				if(board[i][j].getName().charAt(0) == playerTurn && !board[i][j].getType().equals("King")) {
					Piece p = board[i][j]; 
					for(Point move : movesToBlock) {
						boolean pieceLegal = p.check_move(i, j, move.row, move.col);
						boolean boardLegal = checkBoard(board, playerTurn, new Point(i, j), move);
						if(pieceLegal && boardLegal) {
							checkmate = false; 
							return checkmate;  
						}
					}
				}
			}
		}
		
		return checkmate; 
	}
	

	public boolean move(Character playerTurn, int sourceRow, int sourceCol, int destRow, int destCol, ArrayList<Piece> capturedPiece) {
		//String to point
		Point source = new Point(sourceRow, sourceCol);
		Point dest = new Point(destRow, destCol);
		
		//check piece is on board
		if(source.row < 0 || source.row > 8 || source.col == -1) {
			System.out.println("Illegal move, try again");
			return false; 
		}
		
		//get piece 
		Piece piece = board[source.row][source.col]; 
		
		//check that source piece corresponds with player piece 
		if(piece.getName().charAt(0) != playerTurn) {
			System.out.println("Illegal move, try again"); 
			return false; 
		}
		
		//check dest is on board
		if(dest.row < 0 || dest.row > 8 || dest.col == -1) {
			System.out.println("Illegal move, try again");
			return false; 
		}
		
		
		boolean pieceLegal = piece.check_move(source.row, source.col, dest.row, dest.col); // -> conditional, check legality of move by piece type
		boolean boardLegal = checkBoard(board, playerTurn, source, dest); // -> conditional, check legality of move by availability of board spaces
		int checkNum = 0; // 0 = check if player's move puts themself in check (temp), 1 = check if player's move puts opponent in check (board)
			//if legal - pass move to update board 
		if(pieceLegal && boardLegal) {
			
			//create copy of board 
			Piece[][] tempBoard = createBoard(); 
			for(int i = 0; i < tempBoard.length; i++) {
				for(int j = 0; j < tempBoard[i].length; j++) {
					 tempBoard[i][j] = board[i][j]; 
				}
			}
			tempBoard = updateBoard(tempBoard, source, dest);
			
			if(!check(tempBoard, playerTurn, checkNum)) {
				// board[source.row][source.col].setmoved();
				if(!board[dest.row][dest.col].getType().equals("Free Space")) {
					capturedPiece.add(board[dest.row][dest.col]);
				}
				board = updateBoard(board, source, dest); 
				checkNum++; 
			}
			else {
				System.out.println("Illegal move, try again");
				return false;
			}
		}
			//if illegal - exception/warning message 
		else {
			System.out.println("Illegal move, try again");
			return false;
		}
		
		//print updated board
		printBoard();
		
		// check if opponent is in check 
		if(check(board, playerTurn, checkNum) && !checkmate(playerTurn == 'w' ? 'b' : 'w')){
			System.out.println("Check"); 
		}
		
		return true;
	}
	
	/**
	 * updates given board with new move
	 * @param board 2d piece array
	 * @param source original location of piece
	 * @param dest new location to move piece
	 * @return updated board(2d piece array)
	 */
	public Piece[][] updateBoard(Piece[][] board, Point source, Point dest) {
		board[dest.row][dest.col] = board[source.row][source.col]; 
		board[source.row][source.col] = new FreeSpace(source.row, source.col); 
		return board; 
	}
	
	

}
