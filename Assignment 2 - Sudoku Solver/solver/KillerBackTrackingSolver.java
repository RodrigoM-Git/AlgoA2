/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.*;

/**
 * Backtracking solver for Killer Sudoku.
 */
public class KillerBackTrackingSolver extends KillerSudokuSolver {

	public KillerBackTrackingSolver() {} // end of KillerBackTrackingSolver()

	@Override
	public boolean solve(SudokuGrid grid) {
		
		// get all variables from the grid
		KillerCell[][] sudokuGrid = grid.getKillerGrid();
		int[] symbols = grid.getSymbols();
		int puzzleSize = grid.getPuzzleSize();

		// iterate through all cells in the grid
		for (int i = 0; i < puzzleSize; i++) {
			for (int j = 0; j < puzzleSize; j++) {

				// check if the cell is "empty"
				if (sudokuGrid[i][j].getValue() == -1) {

					// go through all symbols and see if it can be inserted
					for (int k = 0; k < symbols.length; k++) {
						sudokuGrid[i][j].setValue(symbols[k]);

						// check if the inserted symbol is valid
						if (grid.validate()) {

							// recursion on solve method to set next empty cell
							if (solve(grid)) {
								// if the whole grid is solved, return true
								return true;
							}
						}

						// if no symbols work, set it to "empty" and then backtrack to previous cell
						sudokuGrid[i][j].setValue(-1);
					}

					// if no possible solution, return false
					return false;
				}
			}
		}

		// return true for successful solution (no more empty cells in the grid)
		System.out.println("Successfully solved the puzzle via backtracking");
		return true;
	} // end of solve()

} // end of class KillerBackTrackingSolver()
