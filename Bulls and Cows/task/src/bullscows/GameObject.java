package bullscows;

import java.util.*;

public class GameObject {
    private final String SECRET_NUMBER;
    private final List<Character> CHARACTER_SET = new ArrayList<>();
    private int bulls;
    private int cows;

    public GameObject(int length, int charsetSize) throws Exception {
        final int maxCharsetSize = 36;
        if (charsetSize > maxCharsetSize) {
            throw new Exception("error: maximum character set size is " + maxCharsetSize);
        }

        if (length > charsetSize) {
            throw new Exception("error: can't generate a secret number with a length " +
                                "greater than " + charsetSize +
                                " because there aren't enough unique digits.");
        }

        if (length <= 0) {
            throw new Exception("error: size must be a positive integer.");
        }

        char c = 'a';
        for (int i = 0; i < charsetSize; i++) {
            if (i < 10) {
                CHARACTER_SET.add((char) (i + '0'));
            } else {
                CHARACTER_SET.add(c++);
            }
        }

        SECRET_NUMBER = getUniqueDigitString(length);
    }

    private String getUniqueDigitString(int codeLength) {
        Random random = new Random();
        StringBuilder uniqueDigits = new StringBuilder();

        while (uniqueDigits.length() < codeLength) {
            char c = CHARACTER_SET.get(random.nextInt(CHARACTER_SET.size()));
            if (uniqueDigits.indexOf(String.valueOf(c)) < 0) {
                uniqueDigits.append(c);
            }
        }

        return uniqueDigits.toString();
    }

    private HashMap<Character, ArrayList<Integer>> getLetterMap(String code) {
        char[] characters = code.toCharArray();
        return getLetterMap(characters);
    }

    private HashMap<Character, ArrayList<Integer>> getLetterMap(char[] chArray) {
        HashMap<Character, ArrayList<Integer>> letterMap = new HashMap<>();
        char letter;

        for (int i = 0; i < chArray.length; i++) {
            letter = chArray[i];

            if (letter == '_') {
                continue;
            }

            if (!letterMap.containsKey(letter)) {
                letterMap.put(letter, new ArrayList<>());
            }

            letterMap.get(letter).add(i);
        }

        return letterMap;
    }

    public boolean gameOver() {
        return bulls == SECRET_NUMBER.length();
    }

    public String getGrade() {
        StringBuilder sb = new StringBuilder("Grade: ");

        if (bulls == 0 && cows == 0) {
            sb.append("None.");
        } else {
            sb.append(
                bulls > 0
                    ? bulls + " bull(s)" + ((cows > 0) ? " and " : ".")
                    : "");

            sb.append(
                cows > 0
                    ? cows + " cow(s)."
                    : ""
            );
        }

        return sb.toString();
    }

    public void processGuess(String guess) throws Exception {
        if (guess.length() != SECRET_NUMBER.length()) {
            throw new Exception(
                "error: guess must be " + SECRET_NUMBER.length() + " long."
            );
        }

        char[] guessArray = guess.toCharArray();

        for (char c : guessArray) {
            if (!CHARACTER_SET.contains(c)) {
                throw new Exception("error: guess contains an invalid character");
            }
        }

        HashMap<Character, ArrayList<Integer>> solutionMap = getLetterMap(SECRET_NUMBER);
        bulls = 0;
        cows = 0;

        // Find the bulls and remove from the solution map.
        // Replace letters with an underscore in the character array.
        for (int i = 0; i < SECRET_NUMBER.length(); i++) {
            char letter = guessArray[i];
            Integer position = i;

            if (Objects.equals(letter, SECRET_NUMBER.charAt(i))) {
                bulls++;
                guessArray[i] = '_';
                solutionMap.get(letter).remove(position);
                if (solutionMap.get(letter).size() == 0) {
                    solutionMap.remove(letter);
                }
            }
        }

        // Find the cows.
        if (solutionMap.size() > 0) {
            HashMap<Character, ArrayList<Integer>> remainingMap = getLetterMap(guessArray);
            for (char letter : remainingMap.keySet()) {
                try {
                    cows += Math.min(
                        solutionMap.get(letter).size(),
                        remainingMap.get(letter).size()
                    );
                } catch (Exception ignored) {
                }
            }
        }
    }

    public String getSecretNumber() {
        return SECRET_NUMBER;
    }

    public String symbolRangeToString() {
        return " (0-" +
               (CHARACTER_SET.size() <= 10
                   ? CHARACTER_SET.size() - 1
                   : "9, a-" + CHARACTER_SET.get(CHARACTER_SET.size() - 1)) +
               ").";
    }

}
