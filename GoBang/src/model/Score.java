package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Score {
	
	FIVE, 
	LIVE_FOUR, DEAD_FOUR, 
	LIVE_THREE, DEAD_THREE, 
	LIVE_TWO, DEAD_TWO, 
	ONE, ZERO;
	
	static public int WIN_SCORE = 80000;
	static public int LOSE_SCORE = -80000;
	
	static final private Map<Score, Integer> SCORE_MAP = new HashMap<Score, Integer>() {{
		put(FIVE, 100000);
		put(LIVE_FOUR, 10000);
		put(DEAD_FOUR, 1000);
		put(LIVE_THREE, 1000);
		put(DEAD_THREE, 100);
		put(LIVE_TWO, 30);
		put(DEAD_TWO, 10);
		put(ONE, 1);
		put(ZERO, 0);
	}};
	
	static private BoardDataScoresMap  BOARD_DATA_SCORES_MAP = BoardDataScoresMap.getInstance();
	
	public int getScore() {
		return SCORE_MAP.get(this);
	}
	
	static private Score getSocre(int count, boolean isDead) {
		switch (count) {
		case 1:
			return ZERO;
		case 2:
			if(!isDead) {
				return LIVE_TWO;
			}
			return DEAD_TWO;
		case 3:
			if(!isDead) {
				return LIVE_THREE;
			}
			return DEAD_THREE;
		case 4:
			if(!isDead) {
				return LIVE_FOUR;
			}
			return Score.DEAD_FOUR;
		case 5:
			return FIVE;
		default:
			return ZERO;
		}
	}
	
	static public int getScore(ArrayList<PieceColor> line) {
		int score = 0;
		boolean isDead = false;
		int count = 0;
		boolean hasBlank = false;
		for(int i=0; i<line.size(); i++) {
			if (line.get(i) == null) {
				if(i>0&&line.get(i-1) == null) {
					score += Score.getSocre(count, isDead).getScore();
					count = 0;
					hasBlank = false;
					isDead = false;
				} else if(i>0&&line.get(i-1).equals(PieceColor.White)) {
					isDead = false;
				} else if(i>0&&line.get(i-1).equals(PieceColor.Black)) {
					if (hasBlank) {
						score += Score.getSocre(count, isDead).getScore();
						count = 0;
						hasBlank = false;
						isDead = false;
					} else {
						hasBlank = true;
					}
				}
			} else if (line.get(i).equals(PieceColor.Black)) {
				count += 1;
			} else {
				if (!isDead) {
					isDead = true;
					score += Score.getSocre(count, isDead).getScore();
					count = 0;
					hasBlank = false;
				} else {
					if (count > 4 || (count >= 4 && hasBlank)) {
						score += Score.getSocre(count, isDead).getScore();
					}
					count = 0;
					hasBlank = false;
				}
			}
		}
		score += Score.getSocre(count, isDead).getScore();
		
		isDead = false;
		count = 0;
		for(int i=0; i<line.size(); i++) {
			if (line.get(i) == null) {
				if(i>0&&line.get(i-1) == null) {
					score -= Score.getSocre(count, isDead).getScore();
					count = 0;
					hasBlank = false;
					isDead = false;
				} else if(i>0&&line.get(i-1).equals(PieceColor.Black)) {
					isDead = false;
				} else if(i>0&&line.get(i-1).equals(PieceColor.White)) {
					if (hasBlank) {
						score -= Score.getSocre(count, isDead).getScore();
						count = 0;
						hasBlank = false;
						isDead = false;
					} else {
						hasBlank = true;
					}
				}
			} else if (line.get(i).equals(PieceColor.White)) {
				count += 1;
			} else {
				if (!isDead) {
					isDead = true;
					score -= Score.getSocre(count, isDead).getScore();
					count = 0;
					hasBlank = false;
				} else {
					if (count > 4 || (count >= 4 && hasBlank)) {
						score -= Score.getSocre(count, isDead).getScore();
					}
					count = 0;
					hasBlank = false;
				}
			}
		}
		score -= Score.getSocre(count, isDead).getScore();
		return score;
	}
	
	static public int getScore(BoardData boardData) {
		int key = boardData.hashCode();
		if (BOARD_DATA_SCORES_MAP.containsKey(key)) {
			return BOARD_DATA_SCORES_MAP.get(key);
		}
		PieceColor[][] data = boardData.getData();
		int score = 0;
		// col
		for(int c=0; c<15; c++) {
			score += Score.getScore(new ArrayList<PieceColor>(Arrays.asList(data[c])));
		}
		ArrayList<PieceColor> aList = new ArrayList<>();
		// row
		for(int r=0; r<15; r++) {
			for(int c=0; c<15; c++) {
				aList.add(data[c][r]);
			}
			score += Score.getScore(aList);
			aList.clear();
		}
		// left - top
		for(int c=0; c<11; c++) {
			for(int i=0; i+c<15; i++) {
				aList.add(data[c+i][i]);
			}
			score += Score.getScore(aList);
			aList.clear();
		}
		for(int r=1; r<11; r++) {
			for(int i=0; i+r<15; i++) {
				aList.add(data[i][r+i]);
			}
			score += Score.getScore(aList);
			aList.clear();
		}
		// left - bottom
		for(int c=0; c<11; c++) {
			for(int i=0; i+c<15; i++) {
				aList.add(data[c+i][14-i]);
			}
			score += Score.getScore(aList);
			aList.clear();
		}
		for(int r=13; r>=4; r--) {
			for(int i=0; r-i>=0; i++) {
				aList.add(data[i][r-i]);
			}
			score += Score.getScore(aList);
			aList.clear();
		}
		BOARD_DATA_SCORES_MAP.put(key, score);
		return score;
	}
	
	
}
