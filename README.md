## Introduction

For the final project of the MSCI 240 course, I chose to write a java program to 
solve sudoku puzzles.

I used a recursive depth-first search algorithm to achieve this goal.

The sudokus are read from the [Sudokus.txt](Sudokus.txt) file and outputted to STDOUT upon solving.

### Overview of files

- [SudokuSolver.java](SudokuSolver.java)
    Main code
- [Sudokus.txt](Sudokus.txt)
    Examples of Sudoku puzzles as input

### Example:

#### Input:
```
Grid 01
003020600
900305001
001806400
008102900
700000008
006708200
002609500
800203009
005010300
```

#### Output:
```
Grid 01
╦═══════════╦═══════════╦═══════════╦
║ 4 │ 8 │ 3 ║ 9 │ 2 │ 1 ║ 6 │ 5 │ 7 ║
║───┼───┼───║───┼───┼───║───┼───┼───║
║ 9 │ 6 │ 7 ║ 3 │ 4 │ 5 ║ 8 │ 2 │ 1 ║
║───┼───┼───║───┼───┼───║───┼───┼───║
║ 2 │ 5 │ 1 ║ 8 │ 7 │ 6 ║ 4 │ 9 │ 3 ║
╬═══════════╬═══════════╬═══════════╬
║ 5 │ 4 │ 8 ║ 1 │ 3 │ 2 ║ 9 │ 7 │ 6 ║
║───┼───┼───║───┼───┼───║───┼───┼───║
║ 7 │ 2 │ 9 ║ 5 │ 6 │ 4 ║ 1 │ 3 │ 8 ║
║───┼───┼───║───┼───┼───║───┼───┼───║
║ 1 │ 3 │ 6 ║ 7 │ 9 │ 8 ║ 2 │ 4 │ 5 ║
╬═══════════╬═══════════╬═══════════╬
║ 3 │ 7 │ 2 ║ 6 │ 8 │ 9 ║ 5 │ 1 │ 4 ║
║───┼───┼───║───┼───┼───║───┼───┼───║
║ 8 │ 1 │ 4 ║ 2 │ 5 │ 3 ║ 7 │ 6 │ 9 ║
║───┼───┼───║───┼───┼───║───┼───┼───║
║ 6 │ 9 │ 5 ║ 4 │ 1 │ 7 ║ 3 │ 8 │ 2 ║
╩═══════════╩═══════════╩═══════════╩
```