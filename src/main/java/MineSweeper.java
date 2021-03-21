import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class MineSweeper {

    private int rows;
    private int columns;
    private char[][] mineField;
    private int numberOfMines;
    private File file;

    private char[][] fieldBackup;
    private int minesDiscoveredByPlayer = 0;
    private int numberOfMarkersPlaced = 0;

    private final static Scanner scanner = new Scanner(System.in);

    public MineSweeper(int rows, int columns, int numberOfMines) {
        this.rows = rows;
        this.columns = columns;
        this.numberOfMines = numberOfMines;
        this.mineField = new char[rows][columns];
    }

    public MineSweeper(String pathToFile) {
        this.file = new File(pathToFile);
    }

    public MineSweeper() {
        this(9, 9, 15);
    }

    public void startGame() {
        if (file != null) {
            createMineFieldFromFile();
        } else {
            System.out.println("How many mines do you want on the field?");
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                System.out.println("Thanks for playing. Bye!");
                System.exit(0);
            }
            numberOfMines = Integer.parseInt(input);
            createMineField();
        }
        scanForMines();
        fieldBackup = mineField.clone();

        while (!isGameOver()) {
            printMineField(fieldBackup);
            takeInputFromPlayerAndSetMarker();
        }

        System.out.println("Congratulations! You found all the mines!");
    }

    private void takeInputFromPlayerAndSetMarker() {
        while (true) {
            System.out.println("Set/delete mine marks (x and y coordinates):");
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                System.out.println("Thanks for playing. Bye!");
                System.exit(0);
            }
            String[] parts = input.split(" ");
            int column = Integer.parseInt(parts[0]) - 1;
            int row = Integer.parseInt(parts[1]) - 1;

            if (mineField[row][column] == 'X') {
                minesDiscoveredByPlayer++;
            }

            if (isCellANumber(fieldBackup[row][column], row, column)) {
                System.out.println("There is a number here!");
            } else {
                break;
            }
        }
    }

    /**
     * If the cell is a number it returns true while
     * if the cell is not a number it will update cell
     * value and numberOfMarkersPlaced count accordingly
     */
    private boolean isCellANumber(char cellValue, int row, int column) {
        if (cellValue == '.' || cellValue == '*' || cellValue == 'X') {
            if (cellValue == '*') {
                fieldBackup[row][column] = '.';
                numberOfMarkersPlaced--;
                return false;
            } else if (cellValue == '.') {
                fieldBackup[row][column] = '*';
                numberOfMarkersPlaced++;
                return false;
            } else {
                fieldBackup[row][column] = '*';
                numberOfMarkersPlaced++;
                return false;
            }
        }
        return true;
    }

    private boolean isGameOver() {
        return numberOfMarkersPlaced == numberOfMines && minesDiscoveredByPlayer == numberOfMines;
    }

    private void createMineField() {
        if (numberOfMines > (rows * columns)) {
            System.out.println("ERROR - The number of mines is greater than the available spaces!");
            return;
        }

        // Populate array
        for (char[] array : mineField) {
            Arrays.fill(array, '.');
        }

        // Place mines
        int mineCounter = 0;
        while (mineCounter < numberOfMines) {
            int row = ThreadLocalRandom.current().nextInt(rows);
            int column = ThreadLocalRandom.current().nextInt(columns);
            if (mineField[row][column] != 'X') {
                mineField[row][column] = 'X';
                mineCounter++;
            }
        }
    }

    private void createMineFieldFromFile() {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String currentLine = bufferedReader.readLine();

            String[] parts = currentLine.split(" ");
            rows = Integer.parseInt(parts[0]);
            columns = Integer.parseInt(parts[1]);
            mineField = new char[rows][columns];

            int i = 0;
            while ((currentLine = bufferedReader.readLine()) != null) {
                for (int j = 0; j < columns; j++) {
                    if (currentLine.charAt(j) == 'X') {
                        numberOfMines++;
                    }
                    mineField[i][j] = currentLine.charAt(j);
                }
                i++;
            }

            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scanForMines() {
        for (int y = 0; y < mineField.length; y++) {
            for (int x = 0; x < mineField[y].length; x++) {
                if (mineField[y][x] != 'X') {
                    placeNumberOfMinesAroundCell(y, x);
                }
            }
        }
    }

    // 8 cells check
    private void placeNumberOfMinesAroundCell(int y, int x) {
        int numberOfSurroundingMines = 0;

        if (x != 0) {
            if (mineField[y][x - 1] == 'X') {
                numberOfSurroundingMines++;
            }
        }
        if (x != columns - 1) {
            if (mineField[y][x + 1] == 'X') {
                numberOfSurroundingMines++;
            }
        }
        if (y != rows - 1 && x != 0) {
            if (mineField[y + 1][x - 1] == 'X') {
                numberOfSurroundingMines++;
            }
        }
        if (y != rows - 1 && x != columns - 1) {
            if (mineField[y + 1][x + 1] == 'X') {
                numberOfSurroundingMines++;
            }
        }
        if (y != 0 && x != 0) {
            if (mineField[y - 1][x - 1] == 'X') {
                numberOfSurroundingMines++;
            }
        }
        if (y != 0 && x != columns - 1) {
            if (mineField[y - 1][x + 1] == 'X') {
                numberOfSurroundingMines++;
            }
        }
        if (y != 0) {
            if (mineField[y - 1][x] == 'X') {
                numberOfSurroundingMines++;
            }
        }
        if (y != rows - 1) {
            if (mineField[y + 1][x] == 'X') {
                numberOfSurroundingMines++;
            }
        }
        if (numberOfSurroundingMines == 0) {
            return;
        }

        mineField[y][x] = Character.forDigit(numberOfSurroundingMines, 10);

    }

    private void printMineField(char[][] mineField) {
        printHeader();
        int i = 1;
        for (char[] c : mineField) {
            System.out.print(i + " | ");
            for (char x : c) {
                if (x == 'X') {
                    System.out.print('.');
                } else {
                    System.out.print(x);
                }
                System.out.print(' ');
            }
            System.out.print("|");
            System.out.println();
            i++;
        }
    }

    private void printHeader() {
        System.out.print("  |");
        for (int i = 1; i <= columns; i++) {

            System.out.print(" " + i);
        }
        System.out.print(" |");
        System.out.println();

        System.out.print("   ");
        for (int i = 1; i <= columns; i++) {
            System.out.print("--");
        }
        System.out.print("-");
        System.out.println();
    }
}