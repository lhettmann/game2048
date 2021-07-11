package spw4.game2048;

import java.util.Random;

public class Game {

    //region Constants

    private static final int BOARD_SIZE = 4;
    private static final int WIN_TILE_VALUE = 2048;
    private static final int EMPTY_TILE_VALUE = 0;

    //endregion

    //region Private Fields

    private int[][] board;
    private int score = 0;

    //endregion

    //region Ctor

    public Game() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
    }

    public Game(int[][] initBoard) {
        board = new int[BOARD_SIZE][BOARD_SIZE];

        if (initBoard.length == BOARD_SIZE)
            for (int i = 0; i < BOARD_SIZE; i++)
                for (int j = 0; j < BOARD_SIZE; j++)
                    board[i][j] = initBoard[i][j];
    }

    //endregion

    //region Public Methods

    public int[][] getBoard() {
        return board;
    }

    public int getScore() {
        return score;
    }

    public boolean isOver() {
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                if (board[i][j] == EMPTY_TILE_VALUE)
                    return false;

        return true;
    }

    public boolean isWon() {
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                if (board[i][j] == WIN_TILE_VALUE)
                    return true;

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                sb.append(board[i][j]);
                sb.append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void initialize() {
        Random r = new Random();

        for (int i = 0; i < 2; ) {
            int rowIdx = r.nextInt(BOARD_SIZE);
            int colIdx = r.nextInt(BOARD_SIZE);

            if (board[rowIdx][colIdx] == EMPTY_TILE_VALUE) {
                board[rowIdx][colIdx] = generateNewTileRandomValue(r);
                i++;
            }
        }
    }

    public void move(Direction direction) {
        switch (direction) {
            case right:
                for (int i = 0; i < BOARD_SIZE; i++) {
                    int[] movedRow = performMovingRightAndDown(board[i]);
                    setRow(movedRow, i);
                }
                break;
            case down:
                for (int i = 0; i < BOARD_SIZE; i++) {
                    int[] movedColumn = performMovingRightAndDown(getColumnOfBoard(i));
                    setColumn(movedColumn, i);
                }
                break;

            case left:
                for (int i = 0; i < BOARD_SIZE; i++) {
                    int[] movedRow = performMovingLeftAndUp(board[i]);
                    setRow(movedRow, i);
                }
                break;
            case up:
                for (int i = 0; i < BOARD_SIZE; i++) {
                    int[] movedColumn = performMovingLeftAndUp(getColumnOfBoard(i));
                    setColumn(movedColumn, i);
                }
                break;
            default:
                break;
        }

        setNewRandomTile();
    }

public int getValueAt(int x, int y) { return board[x][y]; }

    //endregion

    //region Private Methods

    private void setNewRandomTile() {
        Random r = new Random();

        while (true) {
            int rowIdx = r.nextInt(BOARD_SIZE);
            int colIdx = r.nextInt(BOARD_SIZE);
            if (board[rowIdx][colIdx] == EMPTY_TILE_VALUE) {
                board[rowIdx][colIdx] = generateNewTileRandomValue(r);
                break;
            }
        }

    }

    // Chances for new TileValue
    // 90% --> 2
    // 10% --> 4
    private int generateNewTileRandomValue(Random r) {
        int randomValue = r.nextInt(10);
        return randomValue % 10 == 0 ? 4 : 2;
    }

    private int[] performMovingLeftAndUp(int[] originalLine) {
        int[] line = copyIntArray(originalLine);

        for (int i = 0; i < BOARD_SIZE; i++) {
            if (line[i] == EMPTY_TILE_VALUE) continue;
            else {
                if (i >= BOARD_SIZE - 1) continue;
                for (int j = 1; j < BOARD_SIZE - i; j++) {
                    if (line[i + j] == EMPTY_TILE_VALUE) continue;
                    if (line[i] != line[i + j]) break;
                    if (line[i] == line[i + j]) {
                        line[i] = 2 * line[i];
                        line[i + j] = EMPTY_TILE_VALUE;
                        score += line[i];
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            if (i + 1 >= BOARD_SIZE) continue;
            if (line[i] != EMPTY_TILE_VALUE) continue;
            for (int j = 1; j < BOARD_SIZE - i; j++) {
                if (line[i + j] != EMPTY_TILE_VALUE && line[i] == EMPTY_TILE_VALUE) {
                    int a = line[i + j];
                    int b = line[i];
                    line[i] = a;
                    line[i + j] = b;
                }
            }
        }
        return line;
    }

    private int[] performMovingRightAndDown(int[] line) {
        int[] newLine = copyIntArray(line);

        for (int i = BOARD_SIZE - 1; i > 0; i--) {
            if (newLine[i] == EMPTY_TILE_VALUE) continue;
            else {
                if (i == 0) continue;
                for (int j = 1; j <= i; j++) {
                    if (newLine[i - j] == EMPTY_TILE_VALUE) continue;
                    if (newLine[i] != newLine[i - j]) break;
                    if (newLine[i] == newLine[i - j]) {
                        newLine[i] = 2 * newLine[i];
                        newLine[i - j] = EMPTY_TILE_VALUE;
                        score += newLine[i];
                        break;
                    }
                }
            }
        }

        for (int i = BOARD_SIZE - 1; i > 0; i--) {
            if (i - 1 < 0) continue;
            for (int j = 1; j <= i; j++) {
                if (newLine[i - j] != EMPTY_TILE_VALUE && newLine[i] == EMPTY_TILE_VALUE) {
                    int a = newLine[i - j];
                    int b = newLine[i];
                    newLine[i] = a;
                    newLine[i - j] = b;
                }
            }
        }
        return newLine;
    }

    private int[] getColumnOfBoard(int index) {
        int[] column = new int[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            column[i] = board[i][index];
        }
        return column;
    }

    private void setRow(int[] values, int index) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            board[index][i] = values[i];
        }
    }

    private void setColumn(int[] values, int index) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            board[i][index] = values[i];
        }
    }

    private int[] copyIntArray(int[] original) {
        int[] copy = new int[original.length];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i];
        }
        return copy;
    }

    //endregion
}
