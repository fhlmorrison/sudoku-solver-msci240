/**
 * Fraser Morrison
 * MSCI 240 Final Project - Sudoku Solver
 * December 16, 2021
 * Description:
 * Input: A text file (example: Sudokus.txt) with sudoku grids following the format
 *      of "Grid ##" on one line followed by 9 lines of 9 digit integers representing
 *      a sudoku puzzle grid.
 * Output: The solved grid are outputted to STDOUT in the format of "Grid ##" followed
 *      by a grid representation of the sudoku puzzle. The format of the grid can be
 *      changed through the PRINT_FANCY_GRID constant.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class SudokuSolver {
    static final String FILE_NAME = "Sudokus.txt";
    static final boolean PRINT_FANCY_GRID = true;

    private int[][] board;
    // Saves the position of initial values
    private boolean[][] safeSquares;

    /**
     * Read in sudoku boards, solve them, and output result
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(new File(FILE_NAME));
        while (input.hasNextLine()) {
            String gridName = input.nextLine();
            int[][] gridArray = new int[9][9];
            for (int i = 0; i < 9; i++) {
                // Read row as a single number
                long rowNum = input.nextLong();
                for (int j = 0; j < 9; j++) {
                    // Split row into squares
                    int n = (int) rowNum%10;
                    gridArray[i][8-j] = n;
                    rowNum /= 10;
                }
            }
            // Create SudokuSolver object and solve
            SudokuSolver sudoku = new SudokuSolver(gridArray);
            sudoku.solve();
            System.out.println(gridName);
            // Print out
            sudoku.printBoard();
            if (input.hasNext()) {
                gridName = input.nextLine();
            }
        }

    }

    /**
     * Constructs a SudokuSolver object based off of a grid
     * @param board 9x9 2D Array of digits representing a sudoku puzzle
     */
    public SudokuSolver(int[][] board) {
        this.board = board;
        validateBoard();
        safeSquares = new boolean[9][9];
        initializeSafeSquares();
    }

    /**
     * Validates that the sudoku board is legal
     * Throws exception if value appears twice in row, column, or box
     */
    private void validateBoard() {
        // Sets store values already seen, if a value is seen twice, 
        // throw IllegalArgumentException
        List<Set<Integer>> rowMaps = new ArrayList<Set<Integer>>();
        List<Set<Integer>> colMaps = new ArrayList<Set<Integer>>();
        List<Set<Integer>> boxMaps = new ArrayList<Set<Integer>>();    
        
        // Set size of sets to 11 (0 to 9, + 1) and load factor to 1 to avoid rehashing

        // Check rows
        for (int i = 0; i < 9; i++) {
            rowMaps.add(new HashSet<Integer>(11, 1));
            for (int j = 0; j < 9; j++) {
                if (this.board[i][j] != 0) {
                    if (rowMaps.get(i).contains(this.board[i][j])) {
                        throw new IllegalArgumentException(
                            "The number " + this.board[i][j] + " appears multiple times in row " + j + ".");
                    }
                    rowMaps.get(i).add(this.board[i][j]);
                }
            }
        }

        // Check columns
        for (int i = 0; i < 9; i++) {
            colMaps.add(new HashSet<Integer>(11, 1));
            for (int j = 0; j < 9; j++) {
                if (this.board[j][i] != 0) {
                    if (colMaps.get(i).contains(this.board[j][i])) {
                        throw new IllegalArgumentException(
                            "The number " + this.board[j][i] + " appears multiple times in column " + i + "." + j);
                    }
                    colMaps.get(i).add(this.board[j][i]);
                }
            }
        }

        // Check boxes
        for (int i = 0; i < 9; i++) {
            boxMaps.add(new HashSet<Integer>(11, 1));
            for (int j = 0; j < 9; j++) {
                if (this.board[i][j] != 0) {
                    if (boxMaps.get(i).contains(this.board[i][j])) {
                        throw new IllegalArgumentException(
                            "The number " + this.board[i/3 + j%3][j/3 + i%3] + " appears multiple times in box " + i + ".");
                    }
                    boxMaps.get(i).add(this.board[i][j]);
                }
            }
        }
    }

    /**
     * Iterate over board to save positions of initial values
     */
    private void initializeSafeSquares() {
        // Set value to true if the number is not 0
        // false if it is 0
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] > 0) {
                    safeSquares[i][j] = true;
                } else {
                    safeSquares[i][j] = false;
                }
            }
        }
    }

    /**
     * Encapsulation for the recursive function that does the actual solving
     */
    public void solve() {
        this.solve(0, 0);
    }

    /**
     * Solves the sudoku with a recursive depth-first search
     * @param row
     * @param col
     * @return
     */
    private boolean solve(int row, int col) {
        //System.out.println("Solving [" + row + ", " + col + "] for n = " + n);
        if (row == 9){
            return true;
        }
        if (checkSafe(row, col)) {
            // If square is safe (original value)
            // Solve next square
            return solve(row + (col+1)/9, (col +1)%9);
        }

        // Try numbers (creates branches)
        for (int i = board[row][col]; i <=9; i++) {
            // Check if number works
            if (checkNum(i, row, col)) {
                board[row][col] = i;
                // Recursively check the branch
                if (solve(row + (col+1)/9, (col +1)%9)) {
                    // End, return up stack
                    return true;
                }
            }
        }
        // No numbers work, backtrack up to last node
        board[row][col] = 0;        
        return false;
    }

    /**
     * Checks rows, column, and box to see if number is legal at given square
     * @param n number to check
     * @param row row of square
     * @param col column of square
     * @return
     */
    private boolean checkNum(int n, int row, int col) {
        // Check row
        boolean inRow = false;
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == n) {
                inRow = true;
            }
        }

        // Check col
        boolean inCol = false;
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == n) {
                inCol = true;
            }
        }

        // Check box
        boolean inBox = false;
        int box = row/3*3+col/3;
        for (int i = 0; i < 9; i++) {
            if (board[box/3*3+i/3][box%3*3+i%3] == n) {
                inBox = true;
            }
        }

        return !(inRow || inCol || inBox);
    }

    /**
     * Checks square to see if it was an initial value
     * @param row row of square
     * @param col column of square
     * @return returns true if it is an initial value, false otherwise
     */
    private boolean checkSafe(int row, int col) {
        return safeSquares[row][col];
    }
    
    /**
     * Picks what format to print sudoku based on PRINT_FANCY_GRID constant
     */
    public void printBoard() {
        if (PRINT_FANCY_GRID) {
            printFancyBoard();
        } else {
            printBasicBoard();
        }
    }

    /**
     * Prints sudoku in a fancy grid that represents the puzzle well
     * I experimented with ASCII characters and found the box drawing characters to fit well
     */
    public void printFancyBoard() {
       
        // Print horizontal lines, special cases for boxes and connecting with vertical lines
        for(int i = 0; i < 9; i++){
            for (int j = 0; j < 9*4+1; j++) {
                if (i == 0 && j%12 == 0){
                    System.out.print("╦");
                } else if(i%3 == 0 && j%12 != 0){
                    System.out.print("═");                
                } else if(j%12==0 && i%3==0){
                    System.out.print("╬");                    
                } else if(j%12==0){
                    System.out.print("║");                
                } else if(j%4==0){
                    System.out.print("┼");
                } else {
                    System.out.print("─");
                }
            }
            System.out.println();

            // Create vertical lines and numbers, special cases for boxes
            for (int j = 0; j < 9; j++) {
                // Double up lines on every 3rd to make boxes
                if (j%3==0) {
                    System.out.print("║");
                } else {
                    System.out.print("│");
                }
                System.out.print(" " + this.board[i][j] + " ");
            }
            
            System.out.println("║");
        }
        
            // Make final horizontal line
            for (int j = 0; j < 9*4+1; j++) {
                if (j%12 == 0) {
                    System.out.print("╩");
                } else {
                    System.out.print("═");
                }
            }
            System.out.println();
        }
    
    /**
     * Prints Sudoku as 9 rows of 9 digit integers
     * Same format as input
     */
    public void printBasicBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }
}
