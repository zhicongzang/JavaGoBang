package model;

import java.util.Observable;

public class Board extends Observable {
	
	private BoardData boardData = new BoardData();
	private PieceColor currentPieceColor = PieceColor.Black;
	
	private Piece latestPiece;
	
	public Board() {
		reset();
	}
	
	/*
	 * Get current Piece Color
	 */
	public PieceColor getCurrentPieceColor() {
		return currentPieceColor;
	}
	
	public Piece getLatestPiece() {
		return latestPiece;
	}
	
	/*
	 * Get boardData
	 */
	public BoardData getBoardData() {
		return boardData;
	}
	
	/*
	 *  Prepare for next game. Notify observers that board state has been changed.
	 */
	public void reset() {
		latestPiece = null;
		boardData = new BoardData();
		currentPieceColor = PieceColor.Black;
		setChanged();
		notifyObservers();
	}
	
	/*
	 * 	Add piece into board. Return Piece if succeeded else return null. Notify observers that board state has been changed.
	 */
	public Piece addPiece(int col, int row) {
		if(boardData.addPiece(col, row, currentPieceColor)) {
			latestPiece = new Piece(col, row, currentPieceColor);
			currentPieceColor = currentPieceColor.changeColor();
			setChanged();
			notifyObservers();
			return latestPiece;
		}
		return null;
	}
	
	/*
	 * 	If someone wins or board is filled, game over.
	 */
	public boolean isEnd() {
		if (latestPiece == null) {
			return false;
		}
		return checkLatestPieceWinState();
	}
	
	private boolean checkLatestPieceWinState() {
		return Utils.checkIsWin(boardData.getData(), latestPiece.getColor(), latestPiece.getCol(), latestPiece.getRow());
	}
}
