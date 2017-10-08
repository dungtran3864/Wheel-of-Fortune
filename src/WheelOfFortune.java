import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Zung on 10/5/17.
 */
public class WheelOfFortune {

    private ArrayList<Player> players;
    private ArrayList<String> guessedCharacters;
    private Player winner;

    private final ArrayList<String> wheelOfFortune = new ArrayList<>(Arrays.asList("$2500", "$900", "$700", "$300", "$800",
            "$500", "$400", "$600", "$350", "$900", "Bankrupt", "Free Play", "$650", "$700", "Lose a Turn", "$800", "$500",
            "$450", "$300", "Bankrupt"));
    private final ArrayList<String> vowels = new ArrayList<>(Arrays.asList("u", "o", "a", "i", "e"));

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
     * Handle vowel/non-vowel cases. If it's a vowel, check to see if player has sufficient fund to buy that
     * vowel.
     *
     * @param vowels list of vowels
     * @param guess given guess
     * @param player the player making the guess
     * @return true if player has sufficient fund to buy a vowel, or it's not a vowel case. False if otherwise.
     */
    public boolean checkIfVowelEligible(ArrayList<String> vowels, String guess, Player player) {
        if (vowels.contains(guess)) {
            return player.showScore() > 250;
        } else {
            return true; // If not a vowel, then no need to worry about
        }
    }

    /**
     * Encrypt the answer to string of "_" characters
     *
     * @param answer answer to be encrypted
     * @return an encrypted answer string
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
     * Take in a character guess, and change "_" characters that represent that guess character in the answer
     *
     * @param encrypted current encrypted answer
     * @param answer original answer to match
     * @param correctGuess input character guess
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
     * Complete Wheel of Fortune simulation. Take an answer from the database (NOTE: right now the database is not yet
     * developed, so I'm gonna take a random answer as a test)
     */
    public void gameSimulation(String answer) {

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
        System.out.println();

        String encryptedAnswer = encryptAnswer(answer);

        /*
            Now for the complete game simulation of Wheel of Fortune. The first player will start the game. He will spin
            the wheel to get some points,. He will make an either one-character guess, or a complete guess. He can guess
            a consonant for no charges, or buy a vowel for $250 and not getting the spun prize for the turn.
            If one-character guess is correct, the encrypted characters will appear in the word, and he will get another turn.
            If one-character guess is wrong, the next turn will be given to the next player. If a complete guess is made
            and correct, he wins the game. Any turn has similar process. If the player types in an invalid one-character,
            or complete guess, he will lose his turn. If he gets Free Play, he can buy a vowel for free, or every correct
            consonant guess, he gets $500. He will not lose his turn if he ever guesses or solves incorrectly. If he gets
            a bankrupt, he loses his turn and resets his fund to 0. If he gets Lose a Turn, he just loses his turn.
            The game ends when the last encrypted characters appear to reveal the final answer, or a player makes a
            correct complete guess. The score of the player playing will show before and after he makes a guess and/or
            spins the wheel. All scores will show when the game ends. A list guessed characters will show each turn as well.
        */

        int i = 0;  // Index of player in the game
        boolean validCommand = true;  // Process guess/solve command validation
        int prevDollar = 0;  // Store a temporary variable for prize if a command is invalid, so the player re-plays
        int invalidTimes = 0;  // Responsible for tracking the invalid times
        boolean freePlay = false;  // Free Play status
        int dollar = 0;  // Monetary prize for a spin. Once this goes into the loop, it is guaranteed to change, not 0.

        while (true) {

            // Introduce the puzzle, the current player playing, and his current prize

            if (validCommand) {
                System.out.println("Puzzle: " + encryptedAnswer);
                System.out.println();
                System.out.println("It's " + this.players.get(i).showName() + "'s turn");
                System.out.println("You currently have: $" + this.players.get(i).showScore());
            }

            // Spin the Wheel of Fortune

            String prize = this.wheelOfFortune.get(new Random().nextInt(this.wheelOfFortune.size()));
            if (validCommand) {
                System.out.println("WHEEL OF FORTUNE HAS BEEN SPUN!");
                System.out.println("You have landed on " + prize);
                System.out.println();
                if (prize.equals("Free Play")) {  // Prevent Free Play from lost due to invalid commands in process
                    freePlay = true;
                }
            }

            if (!(prize.equals("Bankrupt") || prize.equals("Lose a Turn"))) {

                // Player lands on a prize. Now take in further command, and process it.

                if (!freePlay) {
                    String processedPrizeString = prize.replace("$", "");
                    dollar = Integer.parseInt(processedPrizeString);
                }

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

                    // Ask for a character guess

                    if (!freePlay) {
                        System.out.println("CONSONANTS will not cost you any money. For VOWELS, " +
                                "you can buy one of them for $250, given you have sufficient fund.");
                    } else {
                        System.out.println("You will not lose your turn if you make an incorrect guess! You" +
                                "can buy a vowel for FREE, or every correct consonant guess you make grants you" +
                                "$500");
                    }
                    System.out.print("Type in a character you want to guess: ");
                    String guess = decisionScan.nextLine().trim();

                    // Check if the character guess satisfies the condition. Additionally, check to see if
                    // it is a vowel. If it is, then check to see if the player is eligible for buying that vowel.
                    // This process helps delivering the final clean guess to proceed.

                    if (!freePlay) {
                        while (guess.length() != 1 || !checkIfVowelEligible(this.vowels, guess, this.players.get(i))) {

                            if (guess.length() != 1) {
                                System.out.print("Invalid guess! Please try again: ");
                            } else {   // There are only 2 cases in this loop, so checkIfVowelEligible is the other one
                                System.out.print("You currently don't have enough money to buy this vowel! Please type in " +
                                        "another character guess: ");
                            }
                            guess = decisionScan.nextLine().trim();

                        }
                    } else {
                        while (guess.length() != 1) {

                            System.out.print("Invalid guess! Please try again: ");
                            guess = decisionScan.nextLine().trim();

                        }
                    }
                    System.out.println();

                    // Check if the character guess is correct, and process the outcomes

                    if (checkCharAnswer(answer, guess) && !checkIfAlreadyGuessed(this.guessedCharacters, guess)) {

                        int numberOfPosition = listOfCharPos(answer, guess).size();
                        System.out.println("Congratulations! There is/are " + numberOfPosition + " " + guess.toUpperCase()
                                + "'s in the answer!");
                        this.guessedCharacters.add(guess.toLowerCase());

                        if (!freePlay) {
                            if (!this.vowels.contains(guess)) {
                                if (validCommand) {
                                    this.players.get(i).gainScore(dollar * numberOfPosition);
                                } else {
                                    this.players.get(i).gainScore(prevDollar * numberOfPosition);
                                }
                            } else {
                                this.players.get(i).loseScore(250);
                            }
                        } else {
                            // Free Play is not affected by invalid commands, since it only prize is $500 for correct consonant.
                            if (!this.vowels.contains(guess)) {
                                this.players.get(i).gainScore(500 * numberOfPosition);
                            }
                        }

                        System.out.println("You now have: $" + this.players.get(i).showScore());
                        encryptedAnswer = decryptAnswer(encryptedAnswer, answer, guess);

                    } else {

                        if (!freePlay) {
                            if (!checkIfAlreadyGuessed(this.guessedCharacters, guess)) {
                                System.out.println("Sorry! " + guess.toUpperCase() + " is not in the answer!");
                                this.guessedCharacters.add(guess.toLowerCase());
                            } else {
                                System.out.println("Sorry! " + guess.toUpperCase() + " has already been taken!");
                            }
                            i++;
                        } else {
                            // Keep the turn if the player has Free Play
                            System.out.println("No worries! You still can keep your turn!");
                        }

                    }

                    // Reset the Free Play (if had) after use
                    freePlay = false;

                    // Reset the validation of command again
                    validCommand = true;
                    invalidTimes = 0;
                    System.out.println();

                } else if (decision.toUpperCase().equals("SOLVE")) {

                    // Take in the complete guess, and process the outcome

                    System.out.print("Please type in your solution to the puzzle: ");
                    String completeGuess = decisionScan.nextLine().trim();
                    if (checkWholeAnswer(answer, completeGuess)) {
                        this.winner = this.players.get(i);
                        System.out.println("OH MY GOD!!! You're the GENIUS!!!");
                        break;
                    } else {
                        if (!freePlay) {
                            System.out.println("Sorry! Your solution is incorrect!");
                            i++;
                        } else {
                            // Keep the turn if the player has Free Play
                            System.out.println("No worries! You still can keep your turn!");
                        }
                    }
                    System.out.println();

                    // Reset Free Play (if had) after use
                    freePlay = false;

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

            } else {

                if (validCommand) {
                    // Make sure that if a previous command of the user is invalid in the time the user lands on a
                    // prize, then user retypes the command again, not affected by any other spins.
                    if (prize.equals("Bankrupt")) {
                        // Player loses everything, and lost the turn
                        this.players.get(i).resetScore();
                        i++;
                    } else if (prize.equals("Lost a Turn")) {
                        //Player lost the turn
                        i++;
                    }
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

