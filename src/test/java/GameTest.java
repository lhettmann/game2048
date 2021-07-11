package spw4.game2048;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    //region Tests

    @DisplayName("test Empty Ctor")
    @Test
    void testEmptyConstructor() {
        Game g = new Game();
        int[][] board = g.getBoard();
        assertAll(
                () -> assertNotNull(board),
                () -> assertEquals(4, board.length),
                () -> assertEquals(4, board[0].length),
                () -> assertEquals(4, board[1].length),
                () -> assertEquals(4, board[2].length),
                () -> assertEquals(4, board[3].length));
    }

    @DisplayName("test Ctor with input board")
    @Test
    void testConstructorWithInputBoard() {
        int[][] initBoard = new int[][]{
                {0, 8, 4, 4},
                {16, 8, 32, 0},
                {8, 8, 32, 4},
                {16, 8, 4, 4}};

        Game g = new Game(initBoard);

        int[][] board = g.getBoard();
        assertAll(
                () -> assertNotNull(board),
                () -> assertEquals(4, board.length),
                () -> assertEquals(4, board[0].length),
                () -> assertEquals(4, board[1].length),
                () -> assertEquals(4, board[2].length),
                () -> assertEquals(4, board[3].length));

        assertArrayEquals(initBoard, board);
    }

    @DisplayName("test Board Initialization")
    @Test
    void testInitializeBoard() {
        Game g = new Game();
        g.initialize();
        int[][] board = g.getBoard();

        int count = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != 0) count++;
            }
        }

        assertEquals(2, count);
    }

    @DisplayName("test ToString Method")
    @Test
    void testToStringMethod() {
        Game g = new Game();
        String res = g.toString();
        assertEquals("0\t0\t0\t0\t\n" +
                "0\t0\t0\t0\t\n" +
                "0\t0\t0\t0\t\n" +
                "0\t0\t0\t0\t\n", res);

        int[][] initBoard = new int[][]{
                {0, 8, 4, 4},
                {16, 8, 32, 0},
                {8, 8, 32, 4},
                {16, 8, 4, 4}};
        Game g2 = new Game(initBoard);
        String res2 = g2.toString();
        assertEquals("0\t8\t4\t4\t\n" +
                "16\t8\t32\t0\t\n" +
                "8\t8\t32\t4\t\n" +
                "16\t8\t4\t4\t\n", res2);
    }

    @DisplayName("test Score after Game Creation")
    @Test
    void getScoreInitTest() {
        Game g = new Game();
        int score = g.getScore();
        assertEquals(0, score);
    }

    @DisplayName("test Win-Condition true")
    @Test
    void isWonTest() {
        Game g = new Game();
        g.getBoard()[0][0] = 2048;
        assertEquals(true, g.isWon());
    }

    @DisplayName("test Win-Condition false")
    @Test
    void isWonTestFalse() {
        Game g = new Game();
        g.getBoard()[0][0] = 0;
        assertEquals(false, g.isWon());
    }

    @DisplayName("test GameOver Condition true")
    @Test
    void isOverTest() {
        int[][] initBoard = new int[][]{
                {2, 2, 2, 2},
                {2, 2, 2, 2},
                {2, 2, 2, 2},
                {2, 2, 2, 2}};

        Game g = new Game(initBoard);
        assertEquals(true, g.isOver());
    }

    @DisplayName("test GameOver Condition false")
    @Test
    void isOverTestFalse() {
        Game g = new Game();
        assertEquals(false, g.isOver());
    }

    @DisplayName("moveBoard Test")
    @ParameterizedTest(name = "direction: {0}, input: {1}, expectedBoard: {2}, expectedScore: {3}")
    @MethodSource("boardsData")
    void moveTest(Direction direction, int[][] inputBoard, int[][] expected, int expectedScore) {
        Game g = new Game(inputBoard);
        g.move(direction);
        int[][] movedBoard = g.getBoard();

        for (int i = 0; i < movedBoard.length; i++) {
            for (int j = 0; j < movedBoard.length; j++) {
                if (expected[i][j] == 0)
                    continue; // ignore positions, which are expected to be empty, they could contain new random Tile Value
                assertEquals(expected[i][j], movedBoard[i][j]);
            }
        }

        if (expectedScore != 0)
            assertEquals(expectedScore, g.getScore());
    }

    //endregion

    //region Test-Data

    static Stream<Arguments> boardsData() {
        return Stream.of(
                Arguments.of(Direction.up,
                        new int[][]{
                                {0, 8, 0, 4},
                                {0, 8, 32, 0},
                                {16, 0, 32, 0},
                                {16, 0, 0, 4}},
                        new int[][]{
                                {32, 16, 64, 8},
                                {0, 0, 0, 0},
                                {0, 0, 0, 0},
                                {0, 0, 0, 0}
                        },
                        120), // 32 + 16 + 64 + 8
                Arguments.of(Direction.up,
                        new int[][]{
                                {0, 8, 4, 4},
                                {16, 8, 32, 0},
                                {8, 8, 32, 4},
                                {16, 8, 4, 4}},
                        new int[][]{
                                {16, 16, 4, 8},
                                {8, 16, 64, 4},
                                {16, 0, 4, 0},
                                {0, 0, 0, 0}
                        },
                        104), // 16 + 16 + 64 + 8
                Arguments.of(Direction.left,
                        new int[][]{
                                {2, 2, 4, 4},
                                {0, 8, 32, 0},
                                {16, 16, 32, 0},
                                {16, 4, 0, 4}},
                        new int[][]{
                                {4, 8, 0, 0},
                                {8, 32, 0, 0},
                                {32, 32, 0, 0},
                                {16, 8, 0, 0}
                        },
                        52), // 12 + 32 + 8
                Arguments.of(Direction.left,
                        new int[][]{
                                {2, 4, 4, 4},
                                {8, 8, 32, 0},
                                {4, 0, 0, 2},
                                {0, 0, 0, 4}},
                        new int[][]{
                                {2, 8, 4, 0},
                                {16, 32, 0, 0},
                                {4, 2, 0, 0},
                                {4, 0, 0, 0}
                        },
                        0), // 0 + 0 + 0 + 0
                Arguments.of(Direction.right,
                        new int[][]{
                                {2, 0, 4, 4},
                                {0, 8, 0, 8},
                                {8, 0, 0, 8},
                                {0, 0, 0, 0}},
                        new int[][]{
                                {0, 0, 2, 8},
                                {0, 0, 0, 16},
                                {0, 0, 0, 16},
                                {0, 0, 0, 0}
                        },
                        40), // 8 + 16 + 16
                Arguments.of(Direction.right,
                        new int[][]{
                                {4, 4, 0, 0},
                                {8, 0, 0, 8},
                                {8, 0, 0, 0},
                                {0, 16, 16, 0}},
                        new int[][]{
                                {0, 0, 0, 8},
                                {0, 0, 0, 16},
                                {0, 0, 0, 8},
                                {0, 0, 0, 32}
                        },
                        56), // 8 + 16 + 0 + 32
                Arguments.of(Direction.down,
                        new int[][]{
                                {2, 0, 4, 4},
                                {0, 8, 0, 8},
                                {8, 0, 0, 8},
                                {0, 0, 0, 0}},
                        new int[][]{
                                {0, 0, 0, 0},
                                {0, 0, 0, 0},
                                {2, 0, 0, 4},
                                {8, 8, 4, 16}
                        },
                        16), // 0 + 0 + 0 + 16
                Arguments.of(Direction.down,
                        new int[][]{
                                {2, 8, 4, 4},
                                {0, 8, 0, 8},
                                {8, 0, 0, 8},
                                {8, 0, 4, 4}},
                        new int[][]{
                                {0, 0, 0, 0},
                                {0, 0, 0, 4},
                                {2, 0, 0, 16},
                                {16, 16, 8, 4}
                        },
                        56) // 16 + 16 + 8 + 16
        );
    }

    //endregion
}
