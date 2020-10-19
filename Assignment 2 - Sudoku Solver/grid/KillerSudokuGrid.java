/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Class implementing the grid for Killer Sudoku. Extends SudokuGrid (hence
 * implements all abstract methods in that abstract class). You will need to
 * complete the implementation for this for task E and subsequently use it to
 * complete the other classes. See the comments in SudokuGrid to understand what
 * each overriden method is aiming to do (and hence what you should aim for in
 * your implementation).
 */
public class KillerSudokuGrid extends SudokuGrid {

	private KillerCell[][] grid;
	private int puzzleSize;
	private int[] symbols;
	private List<KillerCage> cages;
	private int numOfCages;

	public KillerSudokuGrid() {
		super();

		// TODO: any necessary initialisation at the constructor
	} // end of KillerSudokuGrid()

	/* ********************************************************* */

	@Override
	public void initGrid(String filename) throws FileNotFoundException, IOException {

		// initialize cage array to store all cages in the puzzle
		cages = new ArrayList<KillerCage>();
		
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
			this.grid = new KillerCell[puzzleSize][puzzleSize];

			// adds cells into the sudoku grid with default value of -1
			for (int i = 0; i < puzzleSize; i++) {
				for (int j = 0; j < puzzleSize; j++) {
					grid[i][j] = new KillerCell(i, j);
				}
			}
			
			// gets number of cages from file
			this.numOfCages = Integer.parseInt(br.readLine());
			
			
			// read in each line until end of file
			String line;
			while((line = br.readLine()) != null) {
				
				// split to get cage values
				String[] split = line.split(" ");
				
				
				// get target value from first split
				int target = Integer.parseInt(split[0]);
				
				
				// initialize cage object to store rows
				KillerCage cage = new KillerCage(target);
				
				// repeat until no more cells for the cage
				for(int i = 1; i < split.length; i++) {
					
					// split to get row and column
					String[] split2 = split[i].split(",");
					int row = Integer.parseInt(split2[0]);
					int column = Integer.parseInt(split2[1]);
					
					// add specific cell from the grid into the cage
					cage.addCell(grid[row][column]);
					grid[row][column].setCage(cage);
				}
				
				// add cage just made into the list of cages for the puzzle
				cages.add(cage);
				
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
		
		// checks to see if the puzzle was read in before attempting toString()
		if(grid != null) {
			
			for(int i = 0; i < puzzleSize; i++) {
				for(int j = 0; j < puzzleSize; j++) {
					
					// setting same format as given in example out files
					if(j == puzzleSize - 1) {
						sudokuGrid += grid[i][j].getValue();
					}else {
						sudokuGrid += grid[i][j].getValue() + ",";
					}
				}
				
				// prevents creating empty line at the end of the file
				if(i != puzzleSize -1) {
					sudokuGrid += "\n";
				}
			}
			
		}else {
			
			// Error message for if the puzzle size is invalid
			System.out.println("Error: Coud not create toString() because file was not read in");
			
		}

		return sudokuGrid;
	} // end of toString()

	@Override
	public boolean validate() {
		boolean validated = true;
		
		if(grid != null) {

			// check row
			for(int i = 0; i < puzzleSize; i++) {
				
				// create hash set to check if any duplicates by adding to it
				HashSet<Integer> valuesInRow = new HashSet<>();
				
				for(int j = 0; j < puzzleSize; j++) {
					
					if(grid[i][j].getValue() != -1) {
						
						// if false, then it is a duplicate
						if(!valuesInRow.add(grid[i][j].getValue())) {
							validated = false;
						}
					}
				}
			}
			
			
			// check column
			for (int j = 0; validated && j < puzzleSize; j++) {

				// create hash set to check if any duplicates by adding to it
				HashSet<Integer> valuesInColumn = new HashSet<>();

				for (int i = 0; validated && i < puzzleSize; i++) {

					if (grid[i][j].getValue() != -1) {

						// if false, then it is a duplicate
						if (!valuesInColumn.add(grid[i][j].getValue())) {
							validated = false;

						}

					}
				}
			}
			
			
			// check squares
			double squareSize = Math.sqrt(puzzleSize);
			for (int row = 0; validated && row < puzzleSize; row = row + (int) squareSize) {
				for (int col = 0; validated && col < puzzleSize; col = col + (int) squareSize) {

					// create hash set to check if any duplicates by adding to it
					HashSet<Integer> valuesInSquare = new HashSet<>();

					// go through all of the square (row/col+squareSize keeps it within the current
					// square)
					for (int i = row; validated && i < row + squareSize; i++) {
						for (int j = col; validated && j < col + squareSize; j++) {

							if (grid[i][j].getValue() != -1) {
								// if false, then it is a duplicate
								if (!valuesInSquare.add(grid[i][j].getValue())) {
									validated = false;

								}
							}
						}
					}
				}
			}
		}
		
		
		// loop through all cage
		for(KillerCage cage : cages) {
			
			// get all cells in the cage
			List<KillerCell> cells = cage.getCells();
			boolean fullCage = true;
			int totalOfCage = 0;
			
			// check if the cage is full
			for(int i = 0; i < cells.size(); i++) {
				int value = cells.get(i).getValue();
				
				// if cage has -1, set to false
				if(value == -1) {

					fullCage = false;
					
				// else add value to totalValue
				}else {
					totalOfCage += value;
				}
			}
			
			
			// if the cage is full (has no -1 value)
			if(fullCage) {
				
				// check if totalValue = totalTarget
				if(totalOfCage != cage.getTarget()) {
					validated = false;
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
		return null;
	}

	@Override
	public KillerCell[][] getKillerGrid() {
		return this.grid;
	}

	@Override
	public List<KillerCage> getCages() {
		return this.cages;
	}
} // end of class KillerSudokuGrid

