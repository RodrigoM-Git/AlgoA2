/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import grid.SudokuGrid;

/**
 * Algorithm X solver for standard Sudoku.
 */
public class AlgorXSolver extends StdSudokuSolver {

	private int puzzleSize;
	private int[] symbols;
	private int squareSize;
	private int matrixRowLength;
	private int matrixColLength;

	public AlgorXSolver() {} // end of AlgorXSolver()

	@Override
	public boolean solve(SudokuGrid grid) {
		// create everything needed for solving
		this.puzzleSize = grid.getPuzzleSize();
		this.symbols = grid.getSymbols();
		this.squareSize = (int)Math.sqrt(puzzleSize);
		int[][] sudokuGrid = grid.getStdGrid();
		
		
		// create matrix
		boolean[][] matrix = initializeMatrix(sudokuGrid);
		
		// create answers list
		List<Integer> answers = new ArrayList<>();
		
		// create list of columns
		List<Integer> columns = createHeadersList();
		
		// start solving
		sudokuGrid = findExactCover(columns, answers, sudokuGrid, matrix);
		
		
		// check if solved
		boolean solved = true;
		for(int i = 0; i < puzzleSize; i++) {
			for(int j = 0; j < puzzleSize; j++) {
				// not solved if -1 is present
				if(sudokuGrid[i][j] == -1) {
					solved = false;
				}
			}
		}
		
		
		return solved;
	} // end of solve()
	
	
	private List<Integer> createHeadersList() {
		
		// create a list of all columns in the matrix
		List<Integer> columns = new ArrayList<>();
		
		// add the number of columns into the list
		for(int j = 0; j < matrixColLength; j++) {
			columns.add(j);
		}
		
		return columns;
	}
	
	
	
	private int[][] findExactCover(List<Integer> columns, List<Integer>answers, int[][] sudokuGrid, boolean[][] matrix) {
		
		// if columns list is empty then the puzzle is solved
		if(columns.size() == 0) {
			System.out.println("Successfully solved the puzzle via AlgorithmX");
			
			// translate to sudoku board
			sudokuGrid = translateToSudokuBoard(answers, sudokuGrid);
			
		}else {
			
			// get column with smallest amount of true
			int column = selectSmallestColumn(columns, matrix);
			
			// iterate through rows
			for(int i = 0; i < matrixRowLength; i++) {
				
				// if the row in that column is true
				if(matrix[i][column] == true) {
					
					// add row to answers
					answers.add(i);
					
					// save previous states for backtracking
					boolean[][] matrixQuickSave = matrix;
					List<Integer> columnsQuickSave = columns;
					
					// iterate through columns
					for(int j = 0; j < matrixColLength; j++) {
						
						// if contains true
						if(matrix[i][j] == true) {
							
							// iterate through rows
							for(int i2 = 0; i2 < matrixRowLength; i2++) {
								
								// if contains true and is not equal to original row
								if(matrix[i2][j] == true) {
									if(i2 != i) {
										// cover row (set it all to false)
										Arrays.fill(matrix[i2], false);
									}
								}
							}
							
							// cover the column (set it all to false)
							matrix = coverColumn(j, matrix);
							
							// remove the column from the columns list
							columns.remove(columns.indexOf(j));

							
						}
						
					}
					
					// cover the row (set it all to false)
					Arrays.fill(matrix[i], false);
					
					// recursion
					findExactCover(columns, answers, sudokuGrid, matrix);
					
					// if not right then restore previous states (backtrack) and remove row from answers
					matrix = matrixQuickSave;
					columns = columnsQuickSave;
					answers.remove(answers.indexOf(i));
					
				}
				
			}
		}
		
		return sudokuGrid;
	}
	
	
	private int[][] translateToSudokuBoard(List<Integer> answers, int[][] sudokuGrid) {
		
		// iterate through answers list
		for(int answer : answers) {
			
			// iterate through the board
			for(int i = 0; i < puzzleSize; i++) {
				for(int j = 0; j < puzzleSize; j++) {
					
					// iterate through all symbols
					for(int k = 0; k < symbols.length; k++) {
						
						// if the index in the matrix = the answer 
						if(getMatrixIndex(i,j,k) == answer) {
							
							// add the answer to that specific cell
							sudokuGrid[i][j] = this.symbols[k];
							
						}	
					}
				}
			}
		}
		
		return sudokuGrid;
	}
	
	
	private int selectSmallestColumn(List<Integer> columns, boolean[][] matrix) {
		// set minimum value to highest possible value
		int minimum = Integer.MAX_VALUE;
		int smallest = -1;
		
		// iterate through all columns
		for(int j = 0; j < matrixColLength; j++) {
			
			// check if column is in columns list
			if(columns.contains(j)) {
				int numTrue = 0;
				
				// iterate through and count the number of true
				for(int i = 0; i < matrixRowLength; i++) {
					
					if(matrix[i][j] == true) {
						numTrue++;
					}
				}
				
				// if smaller than current stored, replace it
				if(numTrue < minimum) {
					minimum = numTrue;
					smallest = j;
				}
			}
		}

		return smallest;
	}
	
	
	private boolean[][] coverColumn(int column, boolean[][] matrix) {
		// iterate through column and set all true to false
		for(int i = 0; i < matrixRowLength; i++) {
			if(matrix[i][column] == true)
				matrix[i][column] = false;
		}
		
		return matrix;
	}
	

	private boolean[][] createMatrix() {
		
		// Row is puzzleSize^3 since there are x rows, and x columns, and x values
		this.matrixRowLength = puzzleSize*puzzleSize*puzzleSize;
		
		// Columns is puzzleSize^2 * 4 since there are x rows, and x columns, and 4 constraints
		this.matrixColLength = puzzleSize*puzzleSize*4;
		
		// initialize grid matrix with boolean (since can either be 0 or 1)
		boolean[][] matrix = new boolean[matrixRowLength][matrixColLength];
		
		//set constraints 
		int currColumn = 0;
		
		//single cell constraint
		for(int i = 0; i < puzzleSize; i++) {
			for(int j = 0; j < puzzleSize; j++, currColumn++) {
				for(int k = 0; k < symbols.length; k++) {
					matrix[getMatrixIndex(i,j,k)][currColumn] = true;
				}
			}
		}
		
		//row constraint
		for(int i = 0; i < puzzleSize; i++) {
			for(int k = 0; k < symbols.length; k++, currColumn++) {
				for(int j = 0; j < puzzleSize; j++) {
					matrix[getMatrixIndex(i,j,k)][currColumn] = true;
				}
			}
		}
		
		//column constraint
		for(int j = 0; j < puzzleSize; j++) {
			for(int k = 0; k < symbols.length; k++, currColumn++) {
				for(int i = 0; i < puzzleSize; i++) {
					matrix[getMatrixIndex(i,j,k)][currColumn] = true;
				}
			}
		}
		
		//square constraint
		for(int i = 0; i < puzzleSize; i += squareSize) {
			for(int j = 0; j < puzzleSize; j += squareSize) {
				for(int k = 0; k < symbols.length; k++, currColumn++) {
					for(int r = 0; r < squareSize; r++) {
						for(int c = 0; c < squareSize; c++) {
							matrix[getMatrixIndex(i+r, j+c, k)][currColumn] = true;
						}
					}
				}
			}
		}

		return matrix;
		
	}
	
	
	private boolean[][] initializeMatrix(int[][] sudokuGrid) {
		boolean[][] matrix = createMatrix();
		
		//iterate through the sudoku grid loaded in
		for(int i = 0; i < puzzleSize; i++) {
			for(int j = 0; j < puzzleSize; j++) {
				
				//check if the cell already has a value
				if(sudokuGrid[i][j] != -1) {
					
					//go through list of symbols
					for(int k = 0; k < symbols.length; k++) {
						
						//set everything that doesn't already have a value in the row to false
						if(sudokuGrid[i][j] != symbols[k]) {
							
							Arrays.fill(matrix[getMatrixIndex(i,j,k)], false);
							
						}
					}
				}
			}
		}
		
		return matrix;
	}
	
	
	private int getMatrixIndex(int row, int col, int value) {
		return row * puzzleSize * puzzleSize + col * puzzleSize + value;
	}

} // end of class AlgorXSolver
