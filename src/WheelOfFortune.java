import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Zung on 10/5/17.
 */
public class WheelOfFortune {

    private ArrayList<Player> players;
    private ArrayList<String> guessedCharacters;
    private Player winner;

    private final ArrayList<Integer> wheelOfFortune = new ArrayList<>(Arrays.asList(2500, 900, 700, 300, 800, 500, 400,
            600, 350, 900, 650, 700, 800, 500, 450, 300));

    /**
     * Initiate a wheel of fortune game object
     */
    public WheelOfFortune() {
        this.players = new ArrayList<>();
        this.guessedCharacters = new ArrayList<>();
        this.winner = null;
    }

    /**
     * Check to see if the one-character guess from the player is in the answer of the show
     *
     * @param answer the answer
     * @param guess the one-character guess
     * @return true if contained, false if not
     */
    public boolean checkCharAnswer(String answer, String guess) {
        return answer.contains(guess.toUpperCase());
    }

    /**
     * Check to see if the complete guess from the player matches the answer of the show
     *
     * @param answer the answer
     * @param guess the complete guess
     * @return true if match, false if not
     */
    public boolean checkWholeAnswer(String answer, String guess) {
        return answer.equals(guess.toUpperCase());
    }

    /**
     * Get a list of all the ordered positions of matched one-character guess from the player
     * within the answer of the show
     *
     * @param answer the answer
     * @param guess the one-character guess
     * @return a list of positions of appeared one-character guess in the answer if the answer DOES contain
     */
    public ArrayList<Integer> listOfCharPos (String answer, String guess) {
        ArrayList<Integer> list = new ArrayList<>();
        int index = answer.indexOf(guess.toUpperCase());
        while (index >= 0) {
            list.add(index);
            index = answer.indexOf(guess.toUpperCase(), index + 1);
        }
        return list;
    }

    /**
     * Check if the guess is already guessed
     *
     * @param guessedCharacters list of already guessed
     * @param guess a made guess
     * @return true if yes, false if not
     */
    public boolean checkIfAlreadyGuessed(ArrayList<String> guessedCharacters, String guess) {
        return guessedCharacters.contains(guess);
    }

    public String encryptAnswer(String answer) {
        String encrypted = "";
        for (int i=0; i<answer.length(); i++) {
            if (!Character.toString(answer.charAt(i)).equals(" ")) {
                encrypted += "_";
            } else {
                encrypted += " ";
            }
        }
        return encrypted;
    }

    public String decryptAnswer(String encrypted, String answer, String correctGuess) {
        StringBuilder decrypted = new StringBuilder(encrypted);
        char guessChar = correctGuess.toUpperCase().charAt(0);
        ArrayList<Integer> guessCharPositions = listOfCharPos(answer, correctGuess);
        for (Integer position : guessCharPositions) {
            decrypted.setCharAt(position, guessChar);
        }
        return decrypted.toString();
    }

    /**
     * Complete Wheel of Fortune simulation
     */
    public void gameSimulation() {

        //  Creating 3 new players by asking them to input

        int numberOfName = 0;
        Scanner nameScan = new Scanner(System.in);
        while (numberOfName < 3) {
            System.out.println("Enter the name of Player " + (numberOfName + 1) + " :");
            String playerName = nameScan.nextLine();
            Player player = new Player(playerName);
            this.players.add(player);
            numberOfName++;
        }

        //  Take an answer from the database (NOTE: right now the database is not yet developed,
        //  so I'm gonna take a random answer as a test)

        String answer = "GEORGE WASHINGTON";
        String encryptedAnswer = encryptAnswer(answer);

        /*
            Now for the complete game simulation of Wheel of Fortune. The first player will start the game. He will spin
            the wheel to get some points, or lose points. He will make an either one-character guess, or a complete guess.
            If one-character guess is correct, the encrypted characters will appear in the word, and he will get another turn.
            If one-character guess is wrong, the next turn will be given to the next player. If a complete guess is made
            and correct, he wins the game. Any turn has similar process. If the player types in an invalid
            one-character guess, he will get another chance to retype until it's valid. If the player types in an invalid
            complete guess, he will NOT have a change to retype. The game ends when the last encrypted characters appear
            to reveal the final answer, or a player makes a correct complete guess. The score of the player playing will
            show before and after he makes a guess and/or spins the wheel. All scores will show when the game ends. A
            list guessed characters will show each turn as well.
        */

        int i = 0;


    }

}

