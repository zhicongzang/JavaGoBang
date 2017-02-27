package model;

import java.util.Arrays;

public class BoardData {
	
	static private BoardDataHashValuesManager hashValuesManager = BoardDataHashValuesManager.getInstance();
	
	private PieceColor[][] data;
	private Long hashValue;

	public PieceColor[][] getData() {
		return data;
	}
	
	public Long getHashValue() {
		return hashValue;
	}
	
	public BoardData() {
		data = new PieceColor[15][15];
		hashValue = (long) 0;
	}
	
	public BoardData(BoardData boardData) {
		data = Arrays.stream(boardData.getData()).map(PieceColor[]::clone).toArray(PieceColor[][]::new);
		hashValue = boardData.getHashValue();
	}
	
	public boolean addPiece(int col, int row, PieceColor pieceColor) {
		if (col >=0 && col < 15 && row >=0 && row < 15 && data[col][row] == null) {
			data[col][row] = pieceColor;
			hashValue = hashValue ^ hashValuesManager.getHashValue(col, row, pieceColor);
			return true;
		}
		return false;
	}
	
	public void addToData(int col, int row, PieceColor pieceColor) {
		data[col][row] = pieceColor;
		hashValue = hashValue ^ hashValuesManager.getHashValue(col, row, pieceColor);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BoardData) {
			BoardData boardData = (BoardData) obj;
			return Arrays.equals(boardData.getData(), data);
		}
		return false;
	}
	
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return hashValue.hashCode();
	}
	
}
