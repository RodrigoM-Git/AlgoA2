/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.HashSet;
import java.util.List;

/**
 * Class implementing the grid for standard Sudoku. Extends SudokuGrid (hence
 * implements all abstract methods in that abstract class). You will need to
 * complete the implementation for this for task A and subsequently use it to
 * complete the other classes. See the comments in SudokuGrid to understand what
 * each overriden method is aiming to do (and hence what you should aim for in
 * your implementation).
 */
public class StdSudokuGrid extends SudokuGrid {

	private int[][] grid;
	private int puzzleSize;
	private int[] symbols;

	public StdSudokuGrid() {
		super();

	} // end of StdSudokuGrid()

	/* ********************************************************* */

	@Override
	public void initGrid(String filename) throws FileNotFoundException, IOException {

		// Loads file in
		File toLoad = new File("SampleGames/" + filename);
		BufferedReader br = new BufferedReader(new FileReader(toLoad));

		// puzzle size
		this.puzzleSize = Integer.parseInt(br.readLine());

		double root = Math.sqrt(puzzleSize);

		// checks if size is a valid size
		if (root == (int) root) {
			// initializes symbols string array to load in from file
			String[] loadedSymbols = br.readLine().split(" ");

			// initializes symbols integer array for grid
			this.symbols = new int[puzzleSize];

			// copies string array into int array
			for (int i = 0; i < puzzleSize; i++) {

				this.symbols[i] = Integer.parseInt(loadedSymbols[i]);

			}

			// initializes grid to have size as specified in file
			this.grid = new int[puzzleSize][puzzleSize];

			// sets all to -1 to show incomplete spaces (negative integer)
			for (int i = 0; i < puzzleSize; i++) {
				for (int j = 0; j < puzzleSize; j++) {
					grid[i][j] = -1;
				}
			}

			// read in each line in a loop to add them to the grid
			String line;
			while ((line = br.readLine()) != null) {
				// splits to get row number
				String[] split = line.split(",");
				int row = Integer.parseInt(split[0]);

				// splits to get column and value
				String[] secondSplit = split[1].split(" ");
				int column = Integer.parseInt(secondSplit[0]);
				int value = Integer.parseInt(secondSplit[1]);

				// add to grid array
				grid[row][column] = value;

			}

			System.out.println("(" + filename + ") >> Read Successfully!");

		} else {

			// Error message for if the puzzle size is invalid
			System.out.println("Error: File not read in because the size was invalid");

		}

	} // end of initBoard()

	@Override
	public void outputGrid(String filename) throws FileNotFoundException, IOException {

		// Gets file to write to
		File toSave = new File("TestOutputs/" + filename);
		BufferedWriter bw = new BufferedWriter(new FileWriter(toSave));

		// Getting information to write to file
		String sudokuGridOutput = toString();

		// If the output isn't empty, write it to a file
		if (sudokuGridOutput != "") {
			bw.write(sudokuGridOutput);
			bw.close();
			System.out.println("(" + filename + ") >> Written Successfully!");

		} else {

			// Error message for if the puzzle size is invalid
			System.out.println("Error: Could not write to file as puzzle size is invalid");

		}

	} // end of outputBoard()

	@Override
	public String toString() {
		String sudokuGrid = "";

		// validates to see if the file had a valid puzzle size before attempting
		// toString()
		if (grid != null) {
			for (int i = 0; i < puzzleSize; i++) {
				for (int j = 0; j < puzzleSize; j++) {

					// setting same format as the supplied output files
					if (j == puzzleSize - 1) {
						sudokuGrid += grid[i][j];
					} else {
						sudokuGrid += grid[i][j] + ",";
					}
				}

				// prevents creating empty line at end of file
				if (i != puzzleSize - 1) {
					sudokuGrid += "\n";
				}
			}
		} else {

			// Error message for if the puzzle size is invalid
			System.out.println("Error: Coud not create toString() because file was not read in");

		}

		return sudokuGrid;
	} // end of toString()

	@Override
	public boolean validate() {
		boolean validated = true;

		if (grid != null) {

			// check row
			for (int i = 0; validated && i < puzzleSize; i++) {

				// create new hash set for values in row (returns false if trying to add
				// duplicate value)
				HashSet<Integer> valuesInRow = new HashSet<Integer>();

				for (int j = 0; validated && j < puzzleSize; j++) {

					if (grid[i][j] != -1) {

						// if false, then it is a duplicate
						if (!valuesInRow.add(grid[i][j])) {
							validated = false;

						}

					}
				}
			}

			// check column
			for (int j = 0; validated && j < puzzleSize; j++) {

				// create new hash set for values in column (returns false if trying to add
				// duplicate value)
				HashSet<Integer> valuesInColumn = new HashSet<Integer>();

				for (int i = 0; validated && i < puzzleSize; i++) {

					if (grid[i][j] != -1) {

						// if false, then it is a duplicate
						if (!valuesInColumn.add(grid[i][j])) {
							validated = false;

						}

					}
				}
			}

			// check squares
			double squareSize = Math.sqrt(puzzleSize);
			for (int row = 0; validated && row < puzzleSize; row = row + (int) squareSize) {
				for (int col = 0; validated && col < puzzleSize; col = col + (int) squareSize) {

					// create new hash set for values in square (returns false if trying to add
					// duplicate value)
					HashSet<Integer> valuesInSquare = new HashSet<Integer>();

					// go through all of the square (row/col+squareSize keeps it within the current
					// square)
					for (int i = row; validated && i < row + squareSize; i++) {
						for (int j = col; validated && j < col + squareSize; j++) {

							if (grid[i][j] != -1) {
								// if false, then it is a duplicate
								if (!valuesInSquare.add(grid[i][j])) {
									validated = false;

								}
							}
						}
					}
				}
			}

			
		}
		return validated;
	} // end of validate()

	
	@Override
	public int getPuzzleSize() {
		return this.puzzleSize;
	}
	
	@Override
	public int[] getSymbols() {
		return this.symbols;
	}

	@Override
	public int[][] getStdGrid() { 
		return this.grid;
	}

	@Override
	public KillerCell[][] getKillerGrid() {
		// killer method
		return null;
	}

	@Override
	public List<KillerCage> getCages() {
		// killer method
		return null;
	}

} // end of class StdSudokuGrid
