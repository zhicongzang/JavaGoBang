package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

public class BoardDataScoresMap {
	static private final String SCORE_DATA_FILE = "res/Score.data";
	
	static private final BoardDataScoresMap instance = new BoardDataScoresMap();
	
	private Map<Integer, Integer> scores = new HashMap<>();
	private Map<Integer, Integer> updates = new HashMap<>();
	
	private BoardDataScoresMap() {
		init();
		System.out.println("******Loading Score Data successful!******");
		System.out.println("******Data Count: " + scores.size() + ".******");
	}
	
	private void init() {
		File file = new File(SCORE_DATA_FILE);
		FileInputStream inputStream = null;
		DataInputStream dataInputStream = null;
		try {
			inputStream = new FileInputStream(file);
			dataInputStream = new DataInputStream(inputStream);
			while(true) {
				scores.put(dataInputStream.readInt(), dataInputStream.readInt());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			try {
				file.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (EOFException e) { } catch (IOException e) { }
		finally {
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
	
	public void update() {
		if (updates.isEmpty()) {
			return;
		}
		File file = new File(SCORE_DATA_FILE);
		FileOutputStream outputStream = null;
		DataOutputStream dataOutputStream = null;
		try {
			outputStream = new FileOutputStream(file,true);
			FileChannel fileChannel = outputStream.getChannel();
			fileChannel.position(fileChannel.size());
			dataOutputStream = new DataOutputStream(outputStream);
			for(Map.Entry<Integer, Integer>entry: updates.entrySet()) {
				dataOutputStream.writeInt(entry.getKey());
				dataOutputStream.writeInt(entry.getValue());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
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
		System.out.println("******Update Score Data: " + updates.size() + "******");
		updates.clear();
		
	}
	
	public void put(int key, int value) {
		scores.put(key, value);
		updates.put(key, value);
	}
	
	public int get(int key) {
		return scores.get(key);
	}
	
	public boolean containsKey(int key) {
		return scores.containsKey(key);
	}
	
	public static BoardDataScoresMap getInstance() {
		return instance;
	}
}
