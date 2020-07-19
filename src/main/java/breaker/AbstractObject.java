package breaker;
import java.util.*;
import processing.core.PApplet;
import processing.core.PImage;

public abstract class AbstractObject {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected double[] velocity;

    public AbstractObject(int x, int y, int width, int height, double[] velocity) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocity = velocity;
    }

    public abstract void draw(PApplet app);

    public boolean rebound(AbstractObject rect) {
        if (x + width + velocity[0] > rect.x &&
            x + velocity[0] < rect.x + rect.width &&
            y + height > rect.y &&
            y < rect.y + rect.height) {
                if (rect instanceof Paddle) {
                    velocity[0] = - 5 + (Math.abs(x - rect.x) * (10 / (double) rect.width));
                    velocity[1] =  - Math.abs(-5 - velocity[0]);
                } else {
                    velocity[0] *= -1;
                }
                return true;
            }

        if (x + width > rect.x &&
            x < rect.x + rect.width &&
            y + height + velocity[1] > rect.y &&
            y + velocity[1] < rect.y + rect.height) {
                if (rect instanceof Paddle) {
                    velocity[0] = - 5 + (Math.abs(x - rect.x) * (10 / (double) rect.width));
                    velocity[1] =  - Math.abs(-5 - velocity[0]);
                } else {
                    velocity[1] *= -1;
                }
                return true;
            }
        return false;
    }
}
