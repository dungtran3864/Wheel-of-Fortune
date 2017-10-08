/**
 * Created by Zung on 10/5/17.
 */
public class Player {

    private int score;
    private String name;

    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    public void gainScore(int point) {
        this.score += point;
    }

    public void loseScore(int point) { this.score -= point; }

    public int showScore() {
        return this.score;
    }

    public String showName() {
        return this.name;
    }

    public void resetScore() {
        this.score = 0;
    }
}
