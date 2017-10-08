/**
 * Created by Zung on 10/5/17.
 */
import java.sql.*;

public class Main {

    public static void main(String[] args) {

        String answer = "GEORGE WASHINGTON";

        WheelOfFortune wheelOfFortune = new WheelOfFortune();
        wheelOfFortune.gameSimulation(answer);
    }

}
