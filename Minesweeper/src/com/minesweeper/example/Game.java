package com.minesweeper.example;

import java.util.Random;
import java.util.Scanner;

public class Game {
    private static int MAX_ROW = 8;
    private static int MAX_COl = 8;
    private static int NO_OF_MINES = 10;
    private static char MINE = '*';
    private static char UNREVEALED = '-';
    private static char MARK = '!';
    private static int row_diagonal_ctr[] = {-1, -1, -1, 0, 0, 0, 1, 1, 1};
    private static int col_diagonal_ctr[] = {-1, 0, 1, -1, 0, 1, -1, 0, 1};

    public static void main(String args[]) {
        System.out.println("Creating Game...");
        Game game = new Game();
        char gameArray[][] = new char[MAX_ROW][MAX_COl];
        char visibleArray[][] = new char[MAX_ROW][MAX_COl];
        game.initialize(gameArray);
        game.print(gameArray, 10);
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_COl; j++) {
                visibleArray[i][j] = UNREVEALED;
            }
        }
        boolean win = true;
        Scanner sc = new Scanner(System.in);
        int marked = 0;
        while (true) {
            if (marked == NO_OF_MINES && game.checkIfWon(visibleArray, gameArray))
                break;
            game.print(visibleArray, marked);
            System.out.println("Enter coordinates of cell with 1 to reveal , -1 to mark/Unmark a mine");
            int row = sc.nextInt() - 1;
            int col = sc.nextInt() - 1;
            int action = sc.nextInt();
            if (action == 1) {
                if (gameArray[row][col] == MINE) {
                    win = false;
                    break;
                }
                visibleArray[row][col] = gameArray[row][col];
                int visited[][] = new int[MAX_ROW][MAX_COl];
                for (int i = 0; i < MAX_ROW; i++) {
                    for (int j = 0; j < MAX_COl; j++) {
                        visited[i][j] = 0;
                    }
                }
                game.flood(gameArray, row, col, visibleArray, visited);
            } else if (action == -1) {
                if (visibleArray[row][col] == MARK) {
                    visibleArray[row][col] = UNREVEALED;
                    marked--;
                    continue;
                }
                if (visibleArray[row][col] != UNREVEALED) {
                    System.out.println("Invalid Coordinates");
                    continue;
                }
                visibleArray[row][col] = MARK;
                marked++;
            }
        }
        if (win)
            System.out.println("Congratulations! You have won the game");
        else System.out.println("You Lose!");
    }

    public boolean checkIfWon(char[][] visibleArray, char[][] gameArray) {
        boolean win = true;
        for (int i = 0; i < visibleArray.length; i++) {
            for (int j = 0; j < visibleArray[i].length; j++) {
                if (visibleArray[i][j] == MARK && gameArray[i][j] != MINE) {
                    win = false;
                    break;
                }
            }
        }
        return win;
    }

    public void print(char[][] array, int marked) {
        System.out.println("Current Game Board :\t" + "Mines left = " + (NO_OF_MINES - marked));
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_COl; j++) {
                System.out.print(array[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public void initialize(char[][] gameArray) {
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_COl; j++) {
                gameArray[i][j] = '0';
            }
        }
        int mines_placed = 0;
        while (mines_placed < NO_OF_MINES) {
            Random random = new Random();
            int row_pos = random.nextInt(MAX_ROW);
            int col_pos = random.nextInt(MAX_COl);
            if (gameArray[row_pos][col_pos] == MINE)
                continue;
            gameArray[row_pos][col_pos] = MINE;
            mines_placed++;
        }
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_COl; j++) {
                if (gameArray[i][j] != MINE)
                    gameArray[i][j] = (char) (countMines(gameArray, i, j) + '0');
            }
        }
    }

    public char countMines(char[][] gameArray, int row, int col) {
        char ctr = 0;
        for (int i = 0; i < row_diagonal_ctr.length; i++) {
            if ((row + row_diagonal_ctr[i] >= 0) && (row + row_diagonal_ctr[i] < gameArray.length) &&
                    (col + col_diagonal_ctr[i] >= 0) && (col + col_diagonal_ctr[i] < gameArray.length) &&
                    (gameArray[row + row_diagonal_ctr[i]][col + col_diagonal_ctr[i]] == MINE)) {
                ctr++;
            }
        }
        return ctr;
    }

    public void flood(char[][] gameArray, int row, int col, char[][] visibleArray, int[][] visited) {
        if (gameArray[row][col] != '0')
            return;
        for (int i = 0; i < row_diagonal_ctr.length; i++) {
            if ((row + row_diagonal_ctr[i] >= 0) && (row + row_diagonal_ctr[i] < gameArray.length) &&
                    (col + col_diagonal_ctr[i] >= 0) && (col + col_diagonal_ctr[i] < gameArray.length) &&
                    (visited[row + row_diagonal_ctr[i]][col + col_diagonal_ctr[i]] == 0)) {
                visibleArray[row + row_diagonal_ctr[i]][col + col_diagonal_ctr[i]]
                        = gameArray[row + row_diagonal_ctr[i]][col + col_diagonal_ctr[i]];
                visited[row + row_diagonal_ctr[i]][col + col_diagonal_ctr[i]]++;
                flood(gameArray, row + row_diagonal_ctr[i], col + col_diagonal_ctr[i], visibleArray, visited);
            }
        }
    }
}
