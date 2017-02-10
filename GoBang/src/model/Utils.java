package model;

public class Utils {
	
	static public boolean checkColumn(PieceColor[][] boardData, PieceColor color, int col, int row) {
		int r = row - 1;
        int count = 1;
        while(r>=0) {
            if (boardData[col][r] != null && boardData[col][r].equals(color)) {
                count += 1;
                r -= 1;
            } else {
                break;
            }
        }
        if (count >= 5) {
            return true;
        }
        r = row + 1;
        while(r<15) {
            if (boardData[col][r] != null && boardData[col][r].equals(color)) {
                count += 1;
                r += 1;
            } else {
                break;
            }
        }
        if (count >= 5) {
            return true;
        }
        return false;
	}
	
	static public boolean checkRow(PieceColor[][] boardData, PieceColor color, int col, int row) {
        int c = col - 1;
        int count = 1;
        while(c>=0) {
            if (boardData[c][row] != null && boardData[c][row].equals(color)) {
                count += 1;
                c -= 1;
            } else {
                break;
            }
        }
        if (count >= 5) {
            return true;
        }
        c = col + 1;
        while(c<15) {
            if (boardData[c][row] != null && boardData[c][row].equals(color)) {
                count += 1;
                c += 1;
            } else {
                break;
            }
        }
        if (count >= 5) {
            return true;
        }
        return false;
    }

	static public boolean checkLeftTop(PieceColor[][] boardData, PieceColor color, int col, int row) {
        int c = col - 1;
        int r = row - 1;
        int count = 1;
        while(c>=0 && r>=0) {
            if (boardData[c][r] != null && boardData[c][r].equals(color)) {
                count += 1;
                c -= 1;
                r -= 1;
            } else {
                break;
            }
        }
        if (count >= 5) {
            return true;
        }
        c = col + 1;
        r = row + 1;
        while(c<15 && r<15) {
            if (boardData[c][r] != null && boardData[c][r].equals(color)) {
                count += 1;
                c += 1;
                r +=1;
            } else {
                break;
            }
        }
        if (count >= 5) {
            return true;
        }
        return false;
    }

    static public boolean checkLeftBottom(PieceColor[][] boardData, PieceColor color, int col, int row) {
        int c = col - 1;
        int r = row + 1;
        int count = 1;
        while(c>=0 && r<15) {
            if (boardData[c][r] != null && boardData[c][r].equals(color)) {
                count += 1;
                c -= 1;
                r += 1;
            } else {
                break;
            }
        }
        if (count >= 5) {
            return true;
        }
        c = col + 1;
        r = row - 1;
        while(c<15 && r>=0) {
            if (boardData[c][r] != null && boardData[c][r].equals(color)) {
                count += 1;
                c += 1;
                r -=1;
            } else {
                break;
            }
        }
        if (count >= 5) {
            return true;
        }
        return false;
    }
    
    static public boolean checkIsWin(PieceColor[][] boardData, PieceColor color, int col, int row) {
    	return Utils.checkColumn(boardData, color, col, row) ||
				Utils.checkRow(boardData, color, col, row) ||
				Utils.checkLeftTop(boardData, color, col, row) ||
				Utils.checkLeftBottom(boardData, color, col, row);
    }
	
}
