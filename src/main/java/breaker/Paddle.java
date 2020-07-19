package breaker;
import java.util.*;
import processing.core.PApplet;
import processing.core.PImage;

public class Paddle extends AbstractObject {

    private PImage img;

    public Paddle(PImage img, int x, int y, int width, int height, double[] velocity) {
        super(x, y, width, height, velocity);
        this.img = img;
    }

    public void draw(PApplet app) {
        app.image(img, x, y, width, height);
    }

    public void moveLeft(boolean moveOrNot) {
        if (moveOrNot && x > 0) {
            x -= velocity[0];
        }
    }

    public void moveRight(boolean moveOrNot) {
        if (moveOrNot && x + width < 520) {
            x += velocity[0];
        }
    }

    public void setWidth(int newWidth) {
        width = newWidth;
    }
}
