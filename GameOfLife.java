package com.gol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


public class GameOfLife {
	
	/*
	 * There are 8 immediate neighbors of any given location in board, if all boundary conditions met
	 * given location : [row, col]
	 * 
	 * 					   
	 * 			[r-1, c-1]  [r-1, c]    [r-1, c+1]
	 * 						   |
	 * 			[r, c-1] <---[r,c]--->  [r, c+1]
	 * 						   |
	 * 			[r+1, c-1]	[r+1, c]    [r+1, c+1]
	 * 
	 * We can count neighbor either using 8 if else or using loops as well.
	 * I think is loop is better option, it will take care of boundary condition as well, otherwise in
	 * case of if-else we have to go through all 8 if-else irrespective of the given loc.
	 * eg : [0,0]
	 * Assuming row and col cannot be -ve i.e less then zero(<0)
	 * @return the numbers of live cell in the neighbor of given cell	
	 * Time : both loops executes 9 times if row and col both are  >0       
	 */
	public int getNeighbourCount(boolean board[][], int rc, int cc, int row, int col){
		int r = rc==0 ? 0 : rc-1; // to check boundary condition
		int c = cc==0 ? 0 : cc-1;
		int i, j, liveCount=0;
		for (i=r; i<=r+1 && i<row; i++) {
			for (j=c; j<=c+1 && j<col; j++) {
				if (i==row && j==col) {
					continue; // no need to count for the given location on the board
				}
				if (board[i][j]){ //counting live neighbors only
					liveCount++;
				}
			}
		}
		return liveCount;
	}
	
	/*
	 * Game Rules :
	 *	1. Any live cell with fewer than two live neighbors dies, as if caused by under-population.
	 * 	2. Any live cell with two or three live neighbors lives on to the next generation.
	 * 	3. Any live cell with more than three live neighbors dies, as if by overcrowding.
	 *	4. Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
	 *  5. In any other state cell dies or remains quiescent/dead.
	 * @return the state of the given cell on the basis of rules
	 */
	
	public boolean getState(boolean cellState, int liveCount){
		
		if (cellState) {//live cell rules-1,2,3
			if (liveCount<2)return false; 
			if (liveCount==2 || liveCount==3)return true;
			if (liveCount>3)return false;
		}else{ // dead cell rules-4
			if (liveCount==3)return true;
		}
		return false; //otherwise return the existing state, means state will not change or if there is rule 5 return false
	}
	
	/*
	 * Input 1 : Board row and col
	 * Input 2 : Live cell location in board
	 */
	public Board getInput() throws IOException{
		System.out.println("Input 1 : Board row and col");
		System.out.println("Input 2 : Live cell location in board, comma seprated list");
		
		InputStreamReader in = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(in);
		String rowStr = br.readLine();
		int row = Integer.parseInt(rowStr);
		String colStr = br.readLine();
		int col = Integer.parseInt(colStr);
		String liveCellLoc = br.readLine(); // comma separated list
		
		
		StringTokenizer st = new StringTokenizer(liveCellLoc, ",");
		int size = st.countTokens();
		int liveCell[] = new int[size];
		int i=0;
		while(st.hasMoreElements()) {
			liveCell[i] = Integer.parseInt((String)st.nextElement());
			i++;
		}
		Board board = new Board(row, col);
		board.constructBoard(liveCell);
		return board;
	}
	
	public void start(Board board){
		int i, j, liveCount;
		int count=0;
		while(true){
			for (i=0; i<board.row; i++) {
				for (j=0; j<board.col; j++) {
					liveCount = getNeighbourCount(board.board, i, j, board.row, board.col);
					board.board[i][j] = getState(board.board[i][j], liveCount);
				}
			}
			//System.out.println("\r");
			System.out.print(boardToString(board.board, board.row, board.col));
			//System.out.flush();
			//count++;
		}
	}
	
	
	private String boardToString(boolean[][] board, int row, int col) {
		StringBuffer sb = new StringBuffer();
		int i,j;
		for (i=0; i<row; i++) {
			for (j=0; j<col; j++) {
				if (board[i][j]){
					sb.append("*"); //append * for live state
				}else{
					sb.append("-"); // append space for dead state;
				}
			}
			sb.append("\n");
		}
		//System.out.println(sb.toString());
		return sb.toString();
	}

	public class Board{
		private int row;
		private int col;
		private boolean[][]board;
		
		public Board(int row, int col){
			this.row = row;
			this.col = col;
			board =  new boolean[row][col];
		}
		
		public void constructBoard(int []liveCell){
			int i,r,c,loc;
			for (i=0; i<liveCell.length; i++) {
				loc = liveCell[i];
				r = loc/col;
				c = loc%col;
				board[r][c]=true;
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		GameOfLife gol = new GameOfLife();
		Board board = gol.getInput();
		gol.start(board);
	}

}
