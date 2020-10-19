/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import grid.SudokuGrid;

/**
 * Dancing links solver for standard Sudoku.
 */
public class DancingLinksSolver extends StdSudokuSolver {

	private int puzzleSize;
	private int[] symbols;
	private int squareSize;
	private int matrixRowLength;
	private int matrixColLength;

	public DancingLinksSolver() {} // end of DancingLinksSolver()

	@Override
	public boolean solve(SudokuGrid grid) {
		// create everything needed for solving
		this.puzzleSize = grid.getPuzzleSize();
		this.symbols = grid.getSymbols();
		this.squareSize = (int)Math.sqrt(puzzleSize);
		int[][] sudokuGrid = grid.getStdGrid();
		
		// initialize matrix
		boolean[][] matrix = initializeMatrix(sudokuGrid);
		
		// initialize dancing links grid
		ColumnNode cNode = createDancingLinksGrid(matrix);
		
		// create list of solutions
		List<DancingNode> solution = new LinkedList<>();
		
		// start solving
		sudokuGrid = startSolving(sudokuGrid, solution, cNode);
		
		
		// iterate through array and see if it is solved (not solved if there is a -1)
		boolean solved = true;
		for(int i = 0; i < puzzleSize; i++) {
			for(int j = 0; j < puzzleSize; j++) {
				if(sudokuGrid[i][j] == -1) {
					solved = false;
				}
			}
		}
		return solved;
	} // end of solve()

	private int[][] startSolving(int[][] sudokuGrid, List<DancingNode> solution, ColumnNode cNode) {

		// checks if already solved
		if (cNode.right == cNode) {
			System.out.println("Successfully solved the puzzle via dancing links");
			sudokuGrid = translateToSudokuBoard(solution, sudokuGrid);
		} else {

			// gets column node with the smallest amount of nodes connected
			ColumnNode colNode = getSmallestColumnNode(cNode);

			// cover the column node
			colNode.cover();

			// iterate down the column node until node's down is itself
			for (DancingNode node = colNode.down; node != colNode; node = node.down) {

				// add it to the potential answers
				solution.add(node);

				// iterate right to the next node
				for (DancingNode otherNode = node.right; otherNode != node; otherNode = otherNode.right) {

					// cover each node untill otherNode's right is itself
					otherNode.columnNode.cover();
				}

				// recursion to next
				startSolving(sudokuGrid, solution, cNode);

				// remove from potential answers
				node = solution.remove(solution.size() - 1);
				colNode = node.columnNode;

				// uncover nodes to left
				for (DancingNode otherNode = node.left; otherNode != node; otherNode = otherNode.left) {
					otherNode.columnNode.uncover();
				}
			}

			// uncover the column node
			colNode.uncover();
		}
		
		return sudokuGrid;
	}

	private ColumnNode getSmallestColumnNode(ColumnNode cNode) {
		// initialize smallest size to max value to ensure nothing can be greater
		int smallestSize = Integer.MAX_VALUE;

		// column node to return
		ColumnNode smallest = null;

		// iterate through all column nodes and replace smallest when found
		for (ColumnNode node = (ColumnNode) cNode.right; node != cNode; node = (ColumnNode) node.right) {

			// if smaller
			if (node.getSize() < smallestSize) {

				// set to smallest
				smallestSize = node.getSize();
				smallest = node;
			}
		}

		return smallest;
	}
	
	private int[][] translateToSudokuBoard(List<DancingNode> solution, int[][] sudokuGrid){
		
		// iterate through answer
		for(DancingNode node : solution) {
			DancingNode currentNode = node;
			
			// set minimum value to current columnNode
			int minVal = Integer.parseInt(currentNode.columnNode.getName());
			
			// iterate right until tempNode's right is node
			for(DancingNode tempNode = node.right; tempNode != node; tempNode = tempNode.right) {
				
				// set current value to the current node
				int currVal = Integer.parseInt(tempNode.columnNode.getName());
				
				// iff current is smaller than minimum, replace currentNode with tempNode
				if(currVal < minVal) {
					minVal = currVal;
					currentNode = tempNode;
				}
			}
			
			// set values for calculating
			int value1 = Integer.parseInt(currentNode.columnNode.getName());
			int value2 = Integer.parseInt(currentNode.right.columnNode.getName());
			
			// get row and column value for placing number into sudoku grid
			int row = value1 / puzzleSize;
			int column = value1 % puzzleSize;
			
			// get number to place into sudoku grid
			int num = (value2 % puzzleSize);
			
			//place number into the grid
			sudokuGrid[row][column] = symbols[num];
		}
		
		return sudokuGrid;
	}

	private ColumnNode createDancingLinksGrid(boolean[][] matrix) {

		// initialize head column node
		ColumnNode headColumnNode = new ColumnNode("Head");
		
		// create list to store column nodes
		ArrayList<ColumnNode> columns = new ArrayList<ColumnNode>();

		
		// create column nodes for as long as the matrix column is and connect them to the head column node
		for (int i = 0; i < matrixColLength; i++) {
			ColumnNode node = new ColumnNode(Integer.toString(i));
			columns.add(node);
			headColumnNode = (ColumnNode) headColumnNode.setRight(node);
		}

		// get the first column node after the head column node
		headColumnNode = headColumnNode.right.columnNode;

		
		// iterate through matrix row
		for (int i = 0; i < matrixRowLength; i++) {
			
			//initialize a previous node
			DancingNode previousNode = null;

			
			// iterate through matrix columns
			for (int j = 0; j < matrixColLength; j++) {
				
				// if the value is true connect it
				if (matrix[i][j] == true) {
					
					// get column node of true node
					ColumnNode node = columns.get(j);
					
					// create a new node with the column node
					DancingNode addNode = new DancingNode(node);

					// if the previous hasn't been set before, make the new node the previous
					if (previousNode == null) {
						previousNode = addNode;
					}
					
					// connect node
					node.up.setDown(addNode);
					previousNode = previousNode.setRight(addNode);
					
					// up the size of connected nodes
					node.setSize(node.getSize() + 1);
				}
			}
		}

		// set size of the head column node to the length of the columns in matrix
		headColumnNode.setSize(matrixColLength);

		return headColumnNode;
	}

	private boolean[][] createMatrix() {

		// Row is puzzleSize^3 since there are x rows, and x columns, and x values
		this.matrixRowLength = puzzleSize * puzzleSize * puzzleSize;

		// Columns is puzzleSize^2 * 4 since there are x rows, and x columns, and 4
		// constraints
		this.matrixColLength = puzzleSize * puzzleSize * 4;

		// initialize grid matrix with boolean (since can either be 0 or 1)
		boolean[][] matrix = new boolean[matrixRowLength][matrixColLength];

		// set constraints
		int currColumn = 0;

		// single cell constraint
		for (int i = 0; i < puzzleSize; i++) {
			for (int j = 0; j < puzzleSize; j++, currColumn++) {
				for (int k = 0; k < symbols.length; k++) {
					matrix[getMatrixIndex(i, j, k)][currColumn] = true;
				}
			}
		}

		// row constraint
		for (int i = 0; i < puzzleSize; i++) {
			for (int k = 0; k < symbols.length; k++, currColumn++) {
				for (int j = 0; j < puzzleSize; j++) {
					matrix[getMatrixIndex(i, j, k)][currColumn] = true;
				}
			}
		}

		// column constraint
		for (int j = 0; j < puzzleSize; j++) {
			for (int k = 0; k < symbols.length; k++, currColumn++) {
				for (int i = 0; i < puzzleSize; i++) {
					matrix[getMatrixIndex(i, j, k)][currColumn] = true;
				}
			}
		}

		// square constraint
		for (int i = 0; i < puzzleSize; i += squareSize) {
			for (int j = 0; j < puzzleSize; j += squareSize) {
				for (int k = 0; k < symbols.length; k++, currColumn++) {
					for (int r = 0; r < squareSize; r++) {
						for (int c = 0; c < squareSize; c++) {
							matrix[getMatrixIndex(i + r, j + c, k)][currColumn] = true;
						}
					}
				}
			}
		}

		return matrix;

	}

	private boolean[][] initializeMatrix(int[][] sudokuGrid) {
		boolean[][] matrix = createMatrix();

		// iterate through the sudoku grid loaded in
		for (int i = 0; i < puzzleSize; i++) {
			for (int j = 0; j < puzzleSize; j++) {

				// check if the cell already has a value
				if (sudokuGrid[i][j] != -1) {

					// go through list of symbols
					for (int k = 0; k < symbols.length; k++) {

						// set everything that doesn't already have a value in the row to false
						if (sudokuGrid[i][j] != symbols[k]) {

							Arrays.fill(matrix[getMatrixIndex(i, j, k)], false);

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

} // end of class DancingLinksSolver