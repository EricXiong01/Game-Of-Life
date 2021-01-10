/*
The universe of the Game of Life is an infinite, two-dimensional orthogonal grid of square cells, each of which is in one of two possible states, live or dead, (or populated and unpopulated, respectively). Every cell interacts with its eight neighbours, which are the cells that are horizontally, vertically, or diagonally adjacent. At each step in time, the following transitions occur:

Any live cell with fewer than two live neighbours dies, as if by underpopulation.
Any live cell with two or three live neighbours lives on to the next generation.
Any live cell with more than three live neighbours dies, as if by overpopulation.
Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
These rules, which compare the behavior of the automaton to real life, can be condensed into the following:

Any live cell with two or three live neighbours survives.
Any dead cell with three live neighbours becomes a live cell.
All other live cells die in the next generation. Similarly, all other dead cells stay dead.
The initial pattern constitutes the seed of the system. The first generation is created by applying the above rules simultaneously to every cell in the seed; births and deaths occur simultaneously, and the discrete moment at which this happens is sometimes called a tick. Each generation is a pure function of the preceding one. The rules continue to be applied repeatedly to create further generations.
 */
package model.core;

public class Matrix {
    private int width;
    private int height;
    private int[][] matrix;
    private int[][] previousMatrix;

    //EFFECTS: construct matrix and previousMatrix as all 0 matrices
    public Matrix(int width, int height) {
        this.width = width;
        this.height = height;
        matrix = new int[height][width];
        previousMatrix = new int[height][width];
    }

    //MODIFIES: this
    //EFFECTS: generate the next matrix and the previousMatrix
    public void generateNext() {
        int[][] newMatrix = new int[height][width];
        //the following magic numbers are for the original game of life. Can be implemented to make other rules.
        survivedCell(newMatrix, 1, 2, 3);
        regeneratedCell(newMatrix, 1, 3, 3);
        //dead cells are automatically 0 at this point
        previousMatrix = matrix;
        matrix = newMatrix;
    }

    //EFFECTS: get the element at given position, 0 position is at the upper-left corner
    public int getElement(int positionY, int positionX) {
        return matrix[positionY][positionX];
    }

    //MODIFIES: this
    //EFFECTS: change the element at given position (from 0 to 1, and vice versa), 0 position is at the upper-left corner
    public void changeElement(int positionY, int positionX) {
        if (matrix[positionY][positionX] == 0) {
            matrix[positionY][positionX] = 1;
        } else {
            matrix[positionY][positionX] = 0;
        }
    }

    //MODIFIES: newMatrix
    //EFFECTS: every cell has such adjacency requirement is plotted in the new matrix
    private void regeneratedCell(int[][] newMatrix, int detectRange, int lowerBound, int upperBound) {
        detectCells(newMatrix, detectRange, lowerBound, upperBound, false);
    }

    //MODIFIES: newMatrix
    //EFFECTS: every cell that has adjacent number of cells within a certain range is plotted in the new matrix
    private void survivedCell(int[][] newMatrix, int detectRange, int lowerBound, int upperBound) {
        detectCells(newMatrix, detectRange, lowerBound, upperBound, true);
    }

    //MODIFIES: newMatrix
    //EFFECTS: going through all cells and update the cell accordingly to newMatrix
    private void detectCells(int[][] newMatrix, int detectRange, int lowerBound, int upperBound, boolean checkSurvival) {
        for (int ri = 0; ri < matrix.length; ri++) {
            for (int i = 0; i < matrix[ri].length; i++) {
                if (checkSurvival) {
                    if (matrix[ri][i] == 1) {
                        detectSingleCell(newMatrix, detectRange, lowerBound + 1, upperBound + 1, ri, i);
                    }
                } else {
                    detectSingleCell(newMatrix, detectRange, lowerBound, upperBound, ri, i);
                }
            }
        }
    }

    //MODIFIES: newMatrix
    //EFFECTS: look at a single cell and determine its state in the next round in newMatrix
    private void detectSingleCell(int[][] newMatrix, int detectRange, int lowerBound, int upperBound, int ri, int i) {
        int counter = 0;
        int upperCorner = ri - detectRange;
        int leftCorner = i - detectRange;

        for (int riCounter = 0; riCounter < detectRange * 2 + 1; riCounter++) {
            for (int iCounter = 0; iCounter < detectRange * 2 + 1; iCounter++) {
                int curRow = upperCorner + riCounter;
                int curElement = leftCorner + iCounter;
                if (curRow >= 0 && curElement >= 0 && curRow < matrix.length && curElement < matrix[riCounter].length) {
                    if (matrix[curRow][curElement] == 1) {
                        counter++;
                    }
                }
            }
        }
        if (counter >= lowerBound && counter <= upperBound) {
            newMatrix[ri][i] = 1;
        }
    }

    //EFFECTS: subtract and return matrix by previousMatrix
    public int[][] difference() {
        int[][] differenceMatrix = new int[height][width];
        for (int ri = 0; ri < matrix.length; ri++) {
            for (int i = 0; i < matrix[ri].length; i++) {
                differenceMatrix[ri][i] = matrix[ri][i] - previousMatrix[ri][i];
            }
        }
        return differenceMatrix;
    }
}
