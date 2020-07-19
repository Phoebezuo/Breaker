package breaker;
import java.util.*;
import processing.core.PApplet;
import processing.core.PImage;

public class Powerup extends AbstractObject {

    private PImage img;
    private String desc;

    public Powerup(PImage img, int x, int y, int width, int height, double[] velocity, String desc) {
        super(x, y, width, height, velocity);
        this.img = img;
        this.desc = desc;
    }

    public void draw(PApplet app) {
        app.image(img, x, y, width, height);
        move();
    }

    public void move() {
        y += velocity[1];
    }

    public String getDesc() {
        return desc;
    }
}
