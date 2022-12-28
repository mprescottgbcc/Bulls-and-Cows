package bullscows;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int codeLength;
        int symbolSetSize;
        GameObject go;
        int turn;
        String guess;

        try {
            System.out.println("Input the length of the secret code:");
            codeLength = scanner.nextInt();
            System.out.println("Input the number of possible symbols in the code:");
            symbolSetSize = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("an input error has occurred.");
            return;
        }

        try {
            go = new GameObject(codeLength, symbolSetSize);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.print("The secret is prepared: ");
        for (int i = 1; i <= codeLength; i++) {
            System.out.print('*');
        }
        System.out.println(go.symbolRangeToString());
        System.out.println("Okay, let's start a game!");
        turn = 1;

        while (!go.gameOver()) {
            System.out.printf("Turn %d:\n", turn);
            guess = scanner.nextLine();
            if (guess.equals("exit")) {
                break;
            }

            try {
                go.processGuess(guess);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            System.out.println(go.getGrade());
            turn++;
        }

        if (go.gameOver()) {
            System.out.println("Congratulations! You guessed the secret code.");
        } else {
            System.out.println("Sorry to see you go. " +
                    "The secret code is " + go.getSecretNumber());
        }
    }
}
