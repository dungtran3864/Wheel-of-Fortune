import java.lang.reflect.Array;
import java.util.*;

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
        return guessedCharacters.contains(guess.toLowerCase());
    }

    /**
     *
     *
     * @param answer
     * @return
     */
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

    /**
     *
     *
     * @param encrypted
     * @param answer
     * @param correctGuess
     * @return
     */
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
            System.out.print("Enter the name of Player " + (numberOfName + 1) + " : ");
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
        boolean validCommand = true;
        int prevDollar = 0;
        int invalidTimes = 0;

        while (true) {

            // Introduce the puzzle, the current player playing, and his current prize

            System.out.println();
            System.out.println("Puzzle: " + encryptedAnswer);
            System.out.println();
            System.out.println("It's " + this.players.get(i).showName() + "'s turn");
            System.out.println("Your currently have: $" + this.players.get(i).showScore());

            // Spin the Wheel of Fortune

            int dollar = this.wheelOfFortune.get(new Random().nextInt(this.wheelOfFortune.size()));
            if (validCommand) {
                System.out.println("WHEEL OF FORTUNE HAS BEEN SPUN!");
                System.out.println("You have landed on $" + dollar);
                System.out.println();
            }

            // Taking in the command, and process it

            Scanner decisionScan = new Scanner(System.in);
            System.out.print("Would you like to guess a character OR solve the puzzle? Type in either GUESS or SOLVE: ");
            String decision = decisionScan.nextLine().trim();

            if (decision.toUpperCase().equals("GUESS")) {

                // Check if there are any characters guessed in the game, if yes then show to the contestants

                if (this.guessedCharacters.size() > 0) {
                    System.out.print("Guessed characters are: ");
                    for (String guessed : guessedCharacters) {
                        System.out.print(guessed.toUpperCase() + " ");
                    }
                    System.out.println();
                }
                System.out.print("Type in a character you want to guess: ");
                String guess = decisionScan.nextLine().trim();
                System.out.println();

                // Check if the character guess satisfies the condition

                while (checkIfAlreadyGuessed(this.guessedCharacters, guess) || guess.length() != 1) {
                    System.out.println("Invalid guess! Please try again!");
                    System.out.print("Guessed characters are: ");
                    for (String guessed : this.guessedCharacters) {
                        System.out.print(guessed.toUpperCase() + " ");
                    }
                    System.out.println();
                    System.out.print("Please type in another your character guess: ");
                    guess = decisionScan.nextLine().trim();
                    System.out.println();
                }

                // Check if the character guess is correct, and process the outcomes

                if (checkCharAnswer(answer, guess)) {

                    int numberOfPosition = listOfCharPos(answer, guess).size();
                    System.out.println("Congratulations! There is/are " + numberOfPosition + " " + guess + "'s in the answer!");
                    this.guessedCharacters.add(guess.toLowerCase());
                    if (validCommand) {
                        this.players.get(i).gainScore(dollar * numberOfPosition);
                    } else {
                        this.players.get(i).gainScore(prevDollar * numberOfPosition);
                    }
                    System.out.println("You now have: $" + this.players.get(i).showScore());
                    encryptedAnswer = decryptAnswer(encryptedAnswer, answer, guess);

                } else {

                    System.out.println("Sorry! " + guess + " is not in the answer!");
                    this.guessedCharacters.add(guess.toLowerCase());
                    i++;
                }

                // Reset the validation of command again

                validCommand = true;
                invalidTimes = 0;

            } else if (decision.toUpperCase().equals("SOLVE")) {

                // Take in the complete guess, and process the outcome

                System.out.print("Please type in your solution to the puzzle: ");
                String completeGuess = decisionScan.nextLine().trim();
                if (checkWholeAnswer(answer, completeGuess)) {
                    this.winner = this.players.get(i);
                    System.out.println("OH MY GOD!!! You're the GENIUS!!!");
                    break;
                } else {
                    System.out.println("Sorry! Your solution is incorrect!");
                    i++;
                }

                // Reset the validation of command again

                validCommand = true;
                invalidTimes = 0;

            } else {

                // When a command is invalid, keep the spin prize, but re-process the command

                System.out.println("Invalid command! Try again!");
                validCommand = false;
                invalidTimes++;
                if (invalidTimes == 1) {   // If the user keeps typing wrong command more than once, the prize is still retained
                    prevDollar = dollar;
                }
            }

            // See if the answer is revealed yet

            if (!encryptedAnswer.contains("_")) {
                break;
            }

            // Go back to the first player if the last player has done his turn
            if (i >= this.players.size()) {
                i = 0;
            }

        }

        // Reveal the answer

        System.out.println();
        System.out.println("The answer to the Puzzle is: " + answer);

        // Show the winner

        if (this.winner == null) {

            Player winner = this.players.get(0);
            for (Player player : this.players) {
                if (player.showScore() > winner.showScore()) {
                    winner = player;
                }
            }
            System.out.println(winner.showName() + " has won the Wheel of Fortune with $" + winner.showScore());

        } else {
            System.out.println(this.winner.showName() + " has won the Wheel of Fortune!!!");
        }

    }

}

