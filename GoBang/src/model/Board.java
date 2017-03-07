package model;

import java.util.ArrayList;
import java.util.Observable;

import javax.swing.undo.UndoableEdit;

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
	
	public void undo(ArrayList<Piece> undoPiece) {
		if (undoPiece.size() <= 0) {
			return;
		}
		if (undoPiece.size() < 3) {
			latestPiece = null;
			currentPieceColor = PieceColor.Black;
		} else {
			latestPiece = undoPiece.remove(undoPiece.size() - 1);
			currentPieceColor = latestPiece.getColor().changeColor();
		}
		for (Piece piece: undoPiece) {
			boardData.undoPiece(piece);
		}
		setChanged();
		notifyObservers();
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
	
	public boolean isTie() {
		return boardData.isFilled();
	}
	
	private boolean checkLatestPieceWinState() {
		return Utils.checkIsWin(boardData.getData(), latestPiece.getColor(), latestPiece.getCol(), latestPiece.getRow());
	}
}
