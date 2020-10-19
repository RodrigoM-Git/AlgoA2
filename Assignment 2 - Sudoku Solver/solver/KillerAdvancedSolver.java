/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import grid.KillerCage;
import grid.KillerCell;
import grid.SudokuGrid;

/**
 * Your advanced solver for Killer Sudoku.
 */
public class KillerAdvancedSolver extends KillerSudokuSolver {
	
	private int[] symbols;
	private int puzzleSize;

	
	public KillerAdvancedSolver() {} // end of KillerAdvancedSolver()

	
	@Override
	public boolean solve(SudokuGrid grid) {
		// get all variables from the grid
				KillerCell[][] sudokuGrid = grid.getKillerGrid();
				this.symbols = grid.getSymbols();
				this.puzzleSize = grid.getPuzzleSize();

				// iterate through all cells in the grid
				for (int i = 0; i < puzzleSize; i++) {
					for (int j = 0; j < puzzleSize; j++) {

						// check if the cell is "empty"
						if (sudokuGrid[i][j].getValue() == -1) {
							
							// create Hash Set of integers (can only add once)
							HashSet<Integer> validNumbers = new HashSet<>();
							
							// get the cage the cell is in
							KillerCage cage = sudokuGrid[i][j].getCage();
							
							// get all valid numbers available in the cage
							getValidNumbers(cage.getCells().size(), cage.getTarget(), validNumbers);
							
							// for every valid number
							for(Integer number : validNumbers) {
								// set the value into the cell
								sudokuGrid[i][j].setValue(number);
								
								// if validated and solved return true
								if(grid.validate()) {
									if(solve(grid))
										return true;
								}
								
								// if not then backtrack by setting to -1
								sudokuGrid[i][j].setValue(-1);
							}
							return false;
						}
					}
				}

				// return true for successful solution (no more empty cells in the grid)
				System.out.println("Successfully solved the puzzle via backtracking");
				return true;
	} // end of solve()
	
	private void getValidNumbers(int cageSize, int target, HashSet<Integer> validNumbers) {
		// create list of combinations 
		List<Integer> combinations = new ArrayList<>();
		findNumbers(cageSize, target, 0, 0, combinations, validNumbers);
	}
	
	private void findNumbers(int cageSize, int target, int sum, int start, List<Integer> combinations, HashSet<Integer> validNumbers) {
		
		// if empty 
		if(cageSize == 0) {
			// if the sum matches target
			if(sum == target) {
				
				// if the numbers are in symbols, add it to the HashSet
				for(int i = 0; i < symbols.length; i++) {
					if(combinations.contains(symbols[i])) {
						validNumbers.add(symbols[i]);
					}
				}
			}
			
			return;
		}
		
		// iterate through symbols
		for(int i = start; i < symbols.length; i++) {
			// add it to combinations
			combinations.add(symbols[i]);
			
			// recursion for next symbol
			findNumbers(cageSize - 1, target, sum + symbols[i], i+1, combinations, validNumbers);
			
			// remove if not valid combination
			combinations.remove(combinations.size()-1);
		}
	}

} // end of class KillerAdvancedSolver
