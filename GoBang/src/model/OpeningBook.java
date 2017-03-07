package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class OpeningBook {
	
	static private final String BOOK_FILE = "res/opening_book";
	
	static private final String BLACK = "0";
	static private final String WHITE = "1";
	
	private Map<Long, List<Piece>> book = new HashMap<>();
	
	private static OpeningBook instance = new OpeningBook();
	
	private BoardDataHashValuesManager boardDataHashValuesManager = BoardDataHashValuesManager.getInstance();
	
	private OpeningBook() {
		List<Piece> first = new ArrayList<>();
		first.add(new Piece(7, 7, PieceColor.Black));
		book.put((long) 0, first);
		File file = new File(BOOK_FILE);
		processBook(file);
	}
	
	private void processBook(File file) {
		FileInputStream inputStream = null;
		DataInputStream dataInputStream = null;
		try {
			inputStream = new FileInputStream(file);
			dataInputStream = new DataInputStream(inputStream);
			while(true) {
				generateData(dataInputStream.readLine(), dataInputStream.readLine());
			}
		} catch (Exception e) { 
		} finally {
			if (dataInputStream != null) {
				try {
					dataInputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void generateData(String key, String value) {
		String[] keyArray = key.split(" ");
		Long hashValue = (long) 0;
		for (String k: keyArray) {
			hashValue = hashValue ^ boardDataHashValuesManager.getHashValue(k);
		}
		String[] valueArray = value.split(" ");
		List<Piece> pieces = new ArrayList<>();
		for (String v: valueArray) {
			pieces.add(generatePieceByStringInfo(v));
		}
		book.put(hashValue, pieces);
	}
	
	private Piece generatePieceByStringInfo(String value) {
		String[] info = value.split(",");
		switch (info[2]) {
		case BLACK:
			return new Piece(Integer.parseInt(info[0]), Integer.parseInt(info[1]), PieceColor.Black);
		case WHITE:
			return new Piece(Integer.parseInt(info[0]), Integer.parseInt(info[1]), PieceColor.White);
		default:
			return null;
		}
	}
	
	static public OpeningBook getInstance() {
		return instance;
	}
	
	public Piece getPiece(long key) {
		if (!book.containsKey(key)) {
			return null;
		}
		List<Piece> pieces = book.get(key);
		Random random = new Random();
		return pieces.get(random.nextInt(pieces.size()));
	}
}
