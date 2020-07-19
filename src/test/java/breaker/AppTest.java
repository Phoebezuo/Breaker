package breaker;
import static org.junit.Assert.*;
import processing.core.PApplet;
import java.util.*;
import org.junit.Before;
import org.junit.Test;

public class AppTest extends App {

    protected ArrayList<Ball> balls;
    protected ArrayList<Paddle> paddles;
    protected ArrayList<Brick> bricks;
    protected ArrayList<Powerup> powerups;

    @Test public void testApp() {
        App classTest = new App();
    }
}
