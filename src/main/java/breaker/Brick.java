package breaker;
import java.util.*;
import processing.core.PApplet;
import processing.core.PImage;

public class Brick extends AbstractObject {

    private PImage img;
    private int hp;
    private String desc;

    public Brick(PImage img, int x, int y, int width, int height, double[] velocity, int hp, String desc) {
        super(x, y, width, height, velocity);
        this.img = img;
        this.hp = hp;
        this.desc = desc;
    }

    public void draw(PApplet app) {
        app.image(img, x, y, width, height);
    }

    public void getAttack() {
        hp -= 1;
    }

    public int getHp() {
        return hp;
    }

    public String getDesc() {
        return desc;
    }
}
