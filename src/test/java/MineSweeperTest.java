import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MineSweeperTest {


    @BeforeEach
    void setUp() {

    }

    @Test
    void itShould_CreateRightGridLayout_FromFile() {
        // Given
        MineSweeper mineSweeper = new MineSweeper("custom-layout");
        char[][] expected = {
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', 'X', '.'},
                {'X', '.', '.', '.', '.'}
        };

        // When
        char[][] actual = mineSweeper.createMineFieldFromFile();
        // Then
        assertArrayEquals(actual, expected);
    }

    @Test
    void itShould_ThrowExceptionWhen_NumberOfMinesExceedsAvailableSpaces() {
        // Given
        MineSweeper mineSweeper = new MineSweeper();
        mineSweeper.setNumberOfMines(100);
        // When
        Executable actual = mineSweeper::createMineField;
        // Then
        assertThrows(IllegalStateException.class, actual);
    }
}