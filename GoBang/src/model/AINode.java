package model;

import java.awt.datatransfer.FlavorTable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.print.attribute.standard.RequestingUserName;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;
import javax.xml.crypto.Data;

public class AINode {
	
	private Integer score = null;
	private BoardData boardData = new BoardData();
	private Position position;
	private Position winPosition;
	private List<Position> highDangerousPositions = new ArrayList<>();
	private List<Position> fourInLinePositions = new ArrayList<>();
	private List<Position> lowDangerousPositions = new ArrayList<>();
	private List<Position> possiblePositions = new ArrayList<>();
	private AINode subNode;
	private boolean isOnlyNode = false;
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return boardData.hashCode() * 7 + 13;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj instanceof AINode) {
			AINode node = (AINode) obj;
			return boardData.equals(node.boardData);
		}
		return false;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Col: " + position.col + " Row: " + position.row + " Estimated Score: " + getSubNodeScore();
	}

	public BoardData getBoardData() {
		return boardData;
	}

	public void setBoardData(BoardData boardData) {
		this.boardData = boardData;
	}
	
	public AINode getSubNode() {
		return subNode;
	}

	public void setSubNode(AINode subNode) {
		this.subNode = subNode;
	}
	
	public boolean isOnlyNode() {
		return isOnlyNode;
	}

	public void setOnlyNode(boolean isOnlyNode) {
		this.isOnlyNode = isOnlyNode;
	}
	
	public Position getWinPosition() {
		return winPosition;
	}
	
	public List<Position> getHighDangerousPositions() {
		return highDangerousPositions;
	}

	public List<Position> getFourInLinePositions() {
		return fourInLinePositions;
	}


	/*
	 * Get color
	 */
	public PieceColor getColor(){
		if (position.col < 0 || position.col > 14 || position.row < 0 || position.row > 14) {
			return PieceColor.Black;
		}
		return boardData.getData()[position.col][position.row];
	}
	
	public boolean isExplorable() {
		if (!Utils.checkIsWin(boardData.getData(), getColor(), position.col, position.row)) {
			return true;
		}
		return false;
	}
	
	/*
	 *  First Node.
	 */
	public AINode(Board board) {
		this.boardData = new BoardData(board.getBoardData());
		this.position = new Position(board.getLatestPiece());
	}
	
	/*
	 * 	Construct node from parent node.
	 */
	public AINode(AINode parentNode, int col, int row) {
		this.boardData = new BoardData(parentNode.getBoardData());
		this.position = new Position(col, row);
		boardData.addToData(col, row, parentNode.getColor().changeColor());
	}
	
	public AINode(AINode parentNode, Position position) {
		this.boardData = new BoardData(parentNode.getBoardData());
		this.position = new Position(position.col, position.row);
		boardData.addToData(position.col, position.row, parentNode.getColor().changeColor());
	}
	
	public void generatePossiblePositions() {
		possiblePositions.clear();
		highDangerousPositions.clear();
		fourInLinePositions.clear();
		lowDangerousPositions.clear();
		winPosition = null;
		// Check win Position
		for(int c=0; c<15; c++) {
			for(int r=0; r< 15; r++) {
				if (positionIsValid(c, r, 1, 1)) {
					Position p = new Position(c, r);
					possiblePositions.add(p);
					if (checkWinPositionAndFourInLine(p)) {
						winPosition = p;
						return;
					}
				}
			}
		}
		// Check dangerous positions
		evaluatePositionsColumn();
		evaluatePositionsRow();
		evaluatePositionsLeftBottom();
		evaluatePositionsLeftTop();
		if (highDangerousPositions.size() > 0) {
			return;
		}
		// Generate neighbor positions
		for(int c=0; c<15; c++) {
			for(int r=0; r< 15; r++) {
				if (positionIsValid(c, r, 2, 2)) {
					Position p = new Position(c, r);
					possiblePositions.add(p);
					checkWinPositionAndFourInLine(p);
				}
			}
		}
	}
	
	private void evaluatePositionsColumn() {
        int r = position.row - 1;
        int count = 1;
        // color
        PieceColor color;
        int emptyCount = 0;
        boolean lastIsEmpty = false;
        boolean isDead = false;
        ArrayList<Position> ps = new ArrayList<>();
        PieceColor[][] data = boardData.getData();
        while(r>=0) {
        	color = data[position.col][r];
            if (color != null && color.equals(getColor())) {
                count += 1;
                r -= 1;
                lastIsEmpty = false;
            } else if (color != null) {
                isDead = true;
                break;
            } else if (lastIsEmpty || emptyCount > 1) {
                break;
            } else {
                lastIsEmpty = true;
                ps.add(new Position(position.col, r));
                r -= 1;
                emptyCount += 1;
            }
        }
        r = position.row + 1;
        lastIsEmpty = false;
        emptyCount = 0;
        while(r<15) {
        	color = data[position.col][r];
            if (color != null && color.equals(getColor())) {
                count += 1;
                r += 1;
                lastIsEmpty = false;
            } else if (color != null) {
                isDead = true;
                break;
            } else if (lastIsEmpty || emptyCount > 1) {
                break;
            } else {
                lastIsEmpty = true;
                ps.add(new Position(position.col, r));
                r += 1;
                emptyCount += 1;
            }
        }
        addToDangerousPositions(ps, count, isDead);
    }
	
	private void evaluatePositionsRow() {
        int c = position.col - 1;
        int count = 1;
        // color
        PieceColor color;
        int emptyCount = 0;
        boolean lastIsEmpty = false;
        boolean isDead = false;
        ArrayList<Position> ps = new ArrayList<>();
        PieceColor[][] data = boardData.getData();
        while(c>=0) {
        	color = data[c][position.row];
            if (color != null && color.equals(getColor())) {
                count += 1;
                c -= 1;
                lastIsEmpty = false;
            } else if (color != null) {
                isDead = true;
                break;
            } else if (lastIsEmpty || emptyCount > 1) {
                break;
            } else {
                lastIsEmpty = true;
                ps.add(new Position(c, position.row));
                c -= 1;
                emptyCount += 1;
            }
        }
        c = position.col + 1;
        lastIsEmpty = false;
        emptyCount = 0;
        while(c<15) {
        	color = data[c][position.row];
            if (color != null && color.equals(getColor())) {
                count += 1;
                c += 1;
                lastIsEmpty = false;
            } else if (color != null) {
                isDead = true;
                break;
            } else if (lastIsEmpty || emptyCount > 1) {
                break;
            } else {
                lastIsEmpty = true;
                ps.add(new Position(c, position.row));
                c += 1;
                emptyCount += 1;
            }
        }
        addToDangerousPositions(ps, count, isDead);
    }
	
	private void evaluatePositionsLeftTop() {
        int c = position.col - 1;
        int r = position.row - 1;
        int count = 1;
        // color
        PieceColor color;
        int emptyCount = 0;
        boolean lastIsEmpty = false;
        boolean isDead = false;
        ArrayList<Position> ps = new ArrayList<>();
        PieceColor[][] data = boardData.getData();
        while(c>=0 && r >=0) {
            color = data[c][r];
            if (color != null && color.equals(getColor())) {
                count += 1;
                c -= 1;
                r -= 1;
                lastIsEmpty = false;
            } else if (color != null) {
                isDead = true;
                break;
            } else if (lastIsEmpty || emptyCount > 1) {
                break;
            }  else {
                lastIsEmpty = true;
                ps.add(new Position(c,r));
                c -= 1;
                r -= 1;
                emptyCount += 1;
            }
        }
        c = position.col + 1;
        r = position.row + 1;
        lastIsEmpty = false;
        emptyCount = 0;
        while(c<15 && r<15) {
        	color = data[c][r];
            if (color != null && color.equals(getColor())) {
                count += 1;
                c += 1;
                r += 1;
                lastIsEmpty = false;
            } else if (color != null) {
                isDead = true;
                break;
            } else if (lastIsEmpty || emptyCount > 1) {
                break;
            } else {
                lastIsEmpty = true;
                ps.add(new Position(c,r));
                c += 1;
                r += 1;
                emptyCount += 1;
            }
        }
        addToDangerousPositions(ps, count, isDead);
	}
	
	private void evaluatePositionsLeftBottom() {
        int c = position.col - 1;
        int r = position.row + 1;
        int count = 1;
        // color
        PieceColor color;
        int emptyCount = 0;
        boolean lastIsEmpty = false;
        boolean isDead = false;
        ArrayList<Position> ps = new ArrayList<>();
        PieceColor[][] data = boardData.getData();
        while(c>=0 && r<15) {
            color = data[c][r];
            if (color != null && color.equals(getColor())) {
                count += 1;
                c -= 1;
                r += 1;
                lastIsEmpty = false;
            } else if (color != null) {
                isDead = true;
                break;
            } else if (lastIsEmpty || emptyCount > 1) {
                break;
            } else {
                lastIsEmpty = true;
                ps.add(new Position(c,r));
                c -= 1;
                r += 1;
                emptyCount += 1;
            }
        }
        c = position.col + 1;
        r = position.row - 1;
        lastIsEmpty = false;
        emptyCount = 0;
        while(c<15 && r>=0) {
        	color = data[c][r];
            if (color != null && color.equals(getColor())) {
                count += 1;
                c += 1;
                r -= 1;
                lastIsEmpty = false;
            } else if (color != null) {
                isDead = true;
                break;
            } else if (lastIsEmpty || emptyCount > 1) {
                break;
            } else {
                lastIsEmpty = true;
                ps.add(new Position(c,r));
                c += 1;
                r -= 1;
                emptyCount += 1;
            }
        }
        addToDangerousPositions(ps, count, isDead);
	}
	
	private void addToDangerousPositions(ArrayList<Position> ps, int count, boolean isDead) {
		switch (count) {
        case 5:
            highDangerousPositions.addAll(ps);
            break;
        case 4:
            for(Position p: ps) {
                int pc = p.col;
                int pr = p.row;
                PieceColor[][] data = boardData.getData();
                if (pr+1<15 && data[pc][pr+1] != null
                        && pr-1>=0 && data[pc][pr-1] != null) {
                	highDangerousPositions.add(p);
                    return;
                }
            }
            highDangerousPositions.addAll(ps);
            break;
        case 3:
            if (!isDead) {
            	lowDangerousPositions.addAll(ps);
            }
            break;
        default:
            break;
		}
	}
	
	private boolean positionIsValid(int col, int row, int neighborDistace, int neighborCount) {
		PieceColor[][] data = boardData.getData();
		if (data[col][row] == null) {
			int count = 0;
			int colStart = col - neighborDistace >= 0 ? col - neighborDistace : 0;
			int colEnd = col + neighborDistace < 15 ? col + neighborDistace : 14;
			int rowStart = row - neighborDistace >= 0 ? row - neighborDistace : 0;
			int rowEnd = row + neighborDistace < 15 ? row + neighborDistace : 14;
			for(int c=colStart; c<=colEnd; c++) {
				for(int r=rowStart; r<=rowEnd; r++) {
					if (data[c][r] != null) {
						count += 1;
						if (count >= neighborCount) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean checkWinPositionAndFourInLine(Position position) {
		return checkWinPositionAndFourInLineByColumn(position) || checkWinPositionAndFourInLineByRow(position) 
				|| checkWinPositionAndFourInLineByLeftTop(position) || checkWinPositionAndFourInLineByLeftBottom(position);
	}
	
	private boolean checkWinPositionAndFourInLineByColumn(Position position) {
		int col = position.col;
		int row = position.row;
		PieceColor[][] data = boardData.getData();
		PieceColor color = getColor().changeColor();
		int emptyCount = 0;
		int r = row - 1;
        int count = 1;
        int countForFour = 1;
        while(r>=0) {
            if (data[col][r] != null && data[col][r].equals(color)) {
            	if (emptyCount <= 0) {
            		count += 1;
            	}
                countForFour += 1;
                r -= 1;
            } else if (data[col][r] == null) {
            	if (emptyCount > 1) {
            		break;
            	} else {
            		emptyCount += 1;
            	}
            } else {
            	break;
            }
        }
        if (count >= 5) {
            return true;
        }
        r = row + 1;
        int emptyCount_new = 0;
        while(r<15) {
            if (data[col][r] != null && data[col][r].equals(color)) {
            	if(emptyCount <= 1 && emptyCount_new <= 0) {
            		count += 1;
            	}
                countForFour += 1;
                r += 1;
            } else if (data[col][r] == null) {
            	if (emptyCount > 2 || emptyCount_new > 1) {
            		break;
            	} else {
            		emptyCount += 1;
            		emptyCount_new += 1;
            	}
            } else {
            	break;
            }
        }
        if (count >= 5) {
            return true;
        }
        if (countForFour >= 4 && emptyCount > 0) {
        	fourInLinePositions.add(position);
        }
        return false;
	}
	
	private boolean checkWinPositionAndFourInLineByRow(Position position) {
		int col = position.col;
		int row = position.row;
		PieceColor[][] data = boardData.getData();
		PieceColor color = getColor().changeColor();
		int emptyCount = 0;
		int c = col - 1;
        int count = 1;
        int countForFour = 1;
        while(c>=0) {
            if (data[c][row] != null && data[c][row].equals(color)) {
            	if(emptyCount <= 0) {
            		count += 1;
            	}
                countForFour += 1;
                c -= 1;
            } else if (data[c][row] == null) {
            	if (emptyCount > 1) {
            		break;
            	} else {
            		emptyCount += 1;
            	}
            } else {
            	break;
            }
        }
        if (count >= 5) {
            return true;
        }
        c = col + 1;
        int emptyCount_new = 0;
        while(c<15) {
            if (data[c][row] != null && data[c][row].equals(color)) {
            	if(emptyCount <= 1 && emptyCount_new <= 0) {
            		count += 1;
            	}
                countForFour += 1;
                c += 1;
            } else if (data[c][row] == null) {
            	if (emptyCount > 2 || emptyCount_new > 1) {
            		break;
            	} else {
            		emptyCount += 1;
            		emptyCount_new += 1;
            	}
            } else {
            	break;
            }
        }
        if (count >= 5) {
            return true;
        }
        if (countForFour >= 4 && emptyCount > 0) {
        	fourInLinePositions.add(position);
        }
        return false;
	}
	
	private boolean checkWinPositionAndFourInLineByLeftTop(Position position) {
		int col = position.col;
		int row = position.row;
		PieceColor[][] data = boardData.getData();
		PieceColor color = getColor().changeColor();
		int emptyCount = 0;
		int c = col - 1;
	    int r = row - 1;
        int count = 1;
        int countForFour = 1;
        while(c>=0 && r>=0) {
            if (data[c][r] != null && data[c][r].equals(color)) {
                if (emptyCount <= 0) {
                	count += 1;
                }
                countForFour += 1;
                c -= 1;
                r -= 1;
            } else if (data[c][r] == null) {
            	if (emptyCount > 1) {
            		break;
            	} else {
            		emptyCount += 1;
            	}
            } else {
            	break;
            }
        }
        if (count >= 5) {
            return true;
        }
        c = col + 1;
        r = row + 1;
        int emptyCount_new = 0;
        while(c<15 && r<15) {
            if (data[c][r] != null && data[c][r].equals(color)) {
            	if(emptyCount <= 1 && emptyCount_new <= 0) {
            		count += 1;
            	}
                countForFour += 1;
                c += 1;
                r +=1;
            } else if (data[c][r] == null) {
            	if (emptyCount > 2 || emptyCount_new > 1) {
            		break;
            	} else {
            		emptyCount += 1;
            		emptyCount_new += 1;
            	}
            } else {
            	break;
            }
        }
        if (count >= 5) {
            return true;
        }
        if (countForFour >= 4 && emptyCount > 0) {
        	fourInLinePositions.add(position);
        }
        return false;
	}
	
	private boolean checkWinPositionAndFourInLineByLeftBottom(Position position) {
		int col = position.col;
		int row = position.row;
		PieceColor[][] data = boardData.getData();
		PieceColor color = getColor().changeColor();
		int emptyCount = 0;
		int c = col - 1;
	    int r = row + 1;
        int count = 1;
        int countForFour = 1;
        while(c>=0 && r<15) {
            if (data[c][r] != null && data[c][r].equals(color)) {
            	if (emptyCount <= 0) {
            		 count += 1;	
            	}
                countForFour += 1;
                c -= 1;
                r += 1;
            } else if (data[c][r] == null) {
            	if (emptyCount > 1) {
            		break;
            	} else {
            		emptyCount += 1;
            	}
            } else {
            	break;
            }
        }
        if (count >= 5) {
            return true;
        }
        c = col + 1;
        r = row - 1;
        int emptyCount_new = 0;
        while(c<15 && r>=0) {
            if (data[c][r] != null && data[c][r].equals(color)) {
            	if(emptyCount <= 1 && emptyCount_new <= 0) {
            		count += 1;
            	}
                countForFour += 1;
                c += 1;
                r -=1;
            } else if (data[c][r] == null) {
            	if (emptyCount > 2 || emptyCount_new > 1) {
            		break;
            	} else {
            		emptyCount += 1;
            		emptyCount_new += 1;
            	}
            } else {
            	break;
            }
        }
        if (count >= 5) {
            return true;
        }
        if (countForFour >= 4 && emptyCount > 0) {
        	fourInLinePositions.add(position);
        }
        return false;
	}

	
	public int getSubNodeScore() {
		if (subNode == null) {
			if (score == null) {
				score = Score.getScore(boardData, position.col, position.row);
			}
			return score;
		}
		return subNode.getSubNodeScore();
	}
	
	public List<AINode> exploreNodes() {
		List<AINode> subNodes = new ArrayList<>();
		if (winPosition != null) {
			AINode node = new AINode(this, winPosition);
			node.setOnlyNode(true);
			if (node != null) {
				subNodes.add(node);
				return subNodes;
			}
		}
		if (highDangerousPositions.size() > 0) {
			for(Position dangerousPosition: highDangerousPositions) {
				AINode node = new AINode(this, dangerousPosition);
				if (node != null) {
					node.setOnlyNode(highDangerousPositions.size() == 1);
					subNodes.add(node);
				}
			}
			return subNodes;
		}
		if (lowDangerousPositions.size() > 0) {
			for(Position dangerousPosition: lowDangerousPositions) {
				AINode node = new AINode(this, dangerousPosition);
				if (node != null) {
					subNodes.add(node);
				}
			}
			return subNodes;
		}
		for(Position possiblePosition: possiblePositions) {
			AINode node = new AINode(this, possiblePosition);
			if (node != null) {
				subNodes.add(node);
			}
		}
		return subNodes;
	}
	
	public AINode unstoppableSubNode() {
		if (winPosition != null ||highDangerousPositions.size() > 0 || fourInLinePositions.size() <= 0) {
			return null;
		}
		for (Position position: fourInLinePositions) {
			AINode node = new AINode(this, position);
			if (node != null && checkUnstoppableMin(node, 12)) {
				return node;
			}
		}
		return null;
	}
	
	// OR win
	static private boolean checkUnstoppableMax(AINode currentNode, int deep) {
		if (deep <= 0) {
			return false;
		}
		for (Position position: currentNode.getFourInLinePositions()) {
			AINode node = new AINode(currentNode, position);
			if (node != null && checkUnstoppableMin(node, deep - 1)) {
				return true;
			}
		}
		return false;
	}
	
	// AND win
	static private boolean checkUnstoppableMin(AINode currentNode, int deep) {
		if (deep <= 0) {
			return false;
		}
		int score = Score.getChangedScore(currentNode.getBoardData(), currentNode.getPiece().getCol(), currentNode.getPiece().getRow());
		if (score > Score.UNSTOPPABLE_SCORE || score < -Score.UNSTOPPABLE_SCORE) {
			return true;
		}
		if (currentNode.getWinPosition() != null) {
			return false;
		}
		for (Position position: currentNode.getHighDangerousPositions()) {
			AINode node = new AINode(currentNode, position);
			if (node != null && !checkUnstoppableMax(node, deep - 1)) {
				return false;
			}
		}
		return false;
	}
	
	public Piece getPiece() {
		return new Piece(position.col, position.row, getColor());
	}
	
	
	class Position {
		int col;
		int row;
		public Position(int col, int row) {
			this.col = col;
			this.row = row;
		}
		public Position(Piece piece) {
			this.col = piece.getCol();
			this.row = piece.getRow();
		}
		@Override
		public String toString() {
			return "Col: " + col + " Row: " + row;
		}
	}
	 
}
