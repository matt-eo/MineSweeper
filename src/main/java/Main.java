import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to import a custom layout from file? (Y/N)");
        String input = scanner.nextLine().toUpperCase(Locale.ROOT);
        if (input.equals("Y")) {
            System.out.println("What is the file name?");
            String fileName = scanner.nextLine();
            System.out.println("(-- Type 'exit' to quit the game --)");
            System.out.println();
            MineSweeper mineSweeper = new MineSweeper(fileName);
            mineSweeper.startGame();
        } else {
            System.out.println("NO FILE SPECIFIED - Starting game in default mode...");
            System.out.println("====================================================");
            System.out.println("(-- Type 'exit' to quit the game --)");
            System.out.println();
            MineSweeper mineSweeper = new MineSweeper();
            mineSweeper.startGame();
        }
    }
}