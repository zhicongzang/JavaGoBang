package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BoardDataHashValuesManager {
	
	static private final String BLACK_HASHVALUES_FILE = "res/blackHashValues.data";
	static private final String WHITE_HASHVALUES_FILE = "res/whiteHashValues.data";
	
	static private final BoardDataHashValuesManager instance = new BoardDataHashValuesManager();
	
	static private final String BLACK = "0";
	static private final String WHITE = "1";
	
	private Map<PieceColor, Long[][]> boardDataHashValues = new HashMap<>();
	
	private BoardDataHashValuesManager() {
		init(BLACK_HASHVALUES_FILE, PieceColor.Black);
		init(WHITE_HASHVALUES_FILE, PieceColor.White);
	}
	
	private void init(String fileName, PieceColor pieceColor) {
		Long[][] hashValues = new Long[15][15];
		File file = new File(fileName);
		FileInputStream inputStream = null;
		DataInputStream dataInputStream = null;
		FileOutputStream outputStream = null;
		DataOutputStream dataOutputStream = null;
		try {
			inputStream = new FileInputStream(file);
			dataInputStream = new DataInputStream(inputStream);
			for(int c=0;c<15;c++) {
				for(int r=0; r<15; r++) {
					hashValues[c][r] = dataInputStream.readLong();
				}
			}
		} catch (Exception e) {
			try {
				file.createNewFile();
				outputStream = new FileOutputStream(file);
				dataOutputStream = new DataOutputStream(outputStream);
				Random random = new Random();
				for(int c=0;c<15;c++) {
					for(int r=0; r<15; r++) {
						Long value = random.nextLong();
						hashValues[c][r] = value;
						dataOutputStream.writeLong(value);
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			if (dataInputStream != null) {
				try {
					dataInputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (dataOutputStream != null) {
				try {
					dataOutputStream.flush();
					dataOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		boardDataHashValues.put(pieceColor, hashValues);
	}
	
	public static BoardDataHashValuesManager getInstance() {
		return instance;
	}
	
	public Long getHashValue(int col, int row, PieceColor pieceColor) {
		return boardDataHashValues.get(pieceColor)[col][row];
	}
	
	public Long getPreHashValue(BoardData boardData, int col, int row) {
		return (boardData.getHashValue() ^ BoardDataHashValuesManager.getInstance().getHashValue(col, row, boardData.getData()[col][row]));
	}
	
	/*
	 *  key: col,row,color
	 */
	public Long getHashValue(String key) {
		String[] info = key.split(",");
		switch (info[2]) {
		case BLACK:
			return boardDataHashValues.get(PieceColor.Black)[Integer.parseInt(info[0])][Integer.parseInt(info[1])];
		case WHITE:
			return boardDataHashValues.get(PieceColor.White)[Integer.parseInt(info[0])][Integer.parseInt(info[1])];
		default:
			return null;
		}
	}
}
