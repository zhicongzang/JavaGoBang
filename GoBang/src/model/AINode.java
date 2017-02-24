package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.print.attribute.standard.RequestingUserName;

public class AINode {
	
	private Integer score = null;
	private PieceColor[][] boardData = new PieceColor[15][15];
	private Position position;
	private Position winPosition;
	private List<Position> dangerousPositions = new ArrayList<>();
	private List<Position> possiblePositions = new ArrayList<>();
	private AINode subNode;
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return Arrays.hashCode(boardData) * 7 + 13;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj instanceof AINode) {
			AINode node = (AINode) obj;
			return Arrays.equals(boardData, node.getBoardData());
		}
		return false;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Col: " + position.col + " Row: " + position.row +  " Socre: " + getScore();
	}

	public PieceColor[][] getBoardData() {
		return boardData;
	}

	public void setBoardData(PieceColor[][] boardData) {
		this.boardData = boardData;
	}
	
	public AINode getSubNode() {
		return subNode;
	}

	public void setSubNode(AINode subNode) {
		this.subNode = subNode;
	}
	
	public boolean needMoreThink() {
		return dangerousPositions.size() > 0;
	}
	
	

	/*
	 * Get color
	 */
	public PieceColor getColor(){
		if (position.col < 0 || position.col > 14 || position.row < 0 || position.row > 14) {
			return PieceColor.Black;
		}
		return boardData[position.col][position.row];
	}
	
	public boolean isExplorable() {
		if (!Utils.checkIsWin(boardData, getColor(), position.col, position.row)) {
			return true;
		}
		return false;
	}
	
	/*
	 *  First Node.
	 */
	public AINode(Board board) {
		this.boardData = Arrays.stream(board.getBoardData()).map(PieceColor[]::clone).toArray(PieceColor[][]::new);
		this.position = new Position(board.getLatestPiece());
	}
	
	/*
	 * 	Construct node from parent node.
	 */
	public AINode(AINode parentNode, int col, int row) {
		this.boardData = Arrays.stream(parentNode.getBoardData()).map(PieceColor[]::clone).toArray(PieceColor[][]::new);
		this.position = new Position(col, row);
		boardData[col][row] = parentNode.getColor().changeColor();
	}
	
	public AINode(AINode parentNode, Position position) {
		this.boardData = Arrays.stream(parentNode.getBoardData()).map(PieceColor[]::clone).toArray(PieceColor[][]::new);
		this.position = new Position(position.col, position.row);
		boardData[position.col][position.row] = parentNode.getColor().changeColor();
	}
	
	private void generatePossiblePositions() {
		possiblePositions.clear();
		dangerousPositions.clear();
		winPosition = null;
		// Check win Position
		for(int c=0; c<15; c++) {
			for(int r=0; r< 15; r++) {
				if (positionIsValid(c, r, 1, 1)) {
					Position p = new Position(c, r);
					possiblePositions.add(p);
					if (Utils.checkIsWin(boardData, getColor().changeColor(), c, r)) {
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
		if (possiblePositions.size() > 0) {
			return;
		}
		// Generate neighbor positions
		for(int c=0; c<15; c++) {
			for(int r=0; r< 15; r++) {
				if (positionIsValid(c, r, 2, 2)) {
					possiblePositions.add(new Position(c, r));
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
        while(r>=0) {
        	color = boardData[position.col][r];
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
        	color = boardData[position.col][r];
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
        addToPossiblePositions(ps, count, isDead);
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
        while(c>=0) {
        	color = boardData[c][position.row];
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
        	color = boardData[c][position.row];
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
        addToPossiblePositions(ps, count, isDead);
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
        while(c>=0 && r >=0) {
            color = boardData[c][r];
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
        	color = boardData[c][r];
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
        addToPossiblePositions(ps, count, isDead);
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
        while(c>=0 && r<15) {
            color = boardData[c][r];
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
        	color = boardData[c][r];
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
        addToPossiblePositions(ps, count, isDead);
	}
	
	private void addToPossiblePositions(ArrayList<Position> ps, int count, boolean isDead) {
		switch (count) {
        case 5:
            dangerousPositions.addAll(ps);
            break;
        case 4:
            for(Position p: ps) {
                int pc = p.col;
                int pr = p.row;
                if (pr+1<15 && boardData[pc][pr+1] != null
                        && pr-1>=0 && boardData[pc][pr-1] != null) {
                    possiblePositions.add(p);
                    return;
                }
            }
            dangerousPositions.addAll(ps);
            break;
        case 3:
            if (!isDead) {
            	dangerousPositions.addAll(ps);
            }
            break;
        default:
            break;
		}
	}
	
	private boolean positionIsValid(int col, int row, int neighborDistace, int neighborCount) {
		if (boardData[col][row] == null) {
			int count = 0;
			int colStart = col - neighborDistace >= 0 ? col - neighborDistace : 0;
			int colEnd = col + neighborDistace < 15 ? col + neighborDistace : 14;
			int rowStart = row - neighborDistace >= 0 ? row - neighborDistace : 0;
			int rowEnd = row + neighborDistace < 15 ? row + neighborDistace : 14;
			for(int c=colStart; c<=colEnd; c++) {
				for(int r=rowStart; r<=rowEnd; r++) {
					if (boardData[c][r] != null) {
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
	
	public int getScore() {
		if (subNode == null) {
			if (score == null) {
				score = Score.getScore(boardData);
			}
			return score;
		}
		return subNode.getScore();
	}
	
	public List<AINode> exploreNodes() {
		List<AINode> subNodes = new ArrayList<>();
		generatePossiblePositions();
		if (winPosition != null) {
			AINode node = new AINode(this, winPosition);
			if (node != null) {
				subNodes.add(node);
				return subNodes;
			}
		}
		if (dangerousPositions.size() > 0) {
			for(Position dangerousPosition: dangerousPositions) {
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
			// TODO Auto-generated method stub
			return "Col: " + col + " Row: " + row;
		}
	}
	 
}
