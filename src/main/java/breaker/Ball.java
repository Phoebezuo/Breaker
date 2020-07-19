package breaker;
import java.util.*;
import processing.core.PApplet;
import processing.core.PImage;

public class Ball extends AbstractObject {

    private PImage img;
    private boolean launched;
    private boolean moveWithPaddle;

    public Ball(PImage img, int x, int y, int width, int height, double[] velocity) {
        super(x, y, width, height, velocity);
        this.img = img;
        launched = false;
        moveWithPaddle = true;
    }

    public void draw(PApplet app) {
        app.image(img, x, y, width, height);
        if (launched & !moveWithPaddle) {
            move();
        }
        reflect();
    }

    public void move() {
        x += velocity[0];
        y += velocity[1];
    }

    public void reflect() {
        if (x < 0) {
            velocity[0] = - velocity[0];
            x = 0;
        } else if (x > 520 - width) {
            velocity[0] = - velocity[0];
            x = 520 - width;
        }

        if (y < 0) {
            velocity[1] = - velocity[1];
            y = 0;
        }
    }

    public void launch(boolean launchOrNot) {
        if (launchOrNot && !launched) {
            move();
            launched = true;
            moveWithPaddle = false;
        }
    }

    public void moveLeftWithPaddle(Paddle paddle) {
        if (moveWithPaddle) {
            x -= paddle.velocity[0];
        }
    }

    public void moveRightWithPaddle(Paddle paddle) {
        if (moveWithPaddle) {
            x += paddle.velocity[0];
        }
    }
}
