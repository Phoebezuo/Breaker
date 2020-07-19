package breaker;

import java.util.*;
import java.lang.*;
import processing.core.PApplet;
import processing.core.PImage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class App extends PApplet {

    // make them to be global variables
    protected ArrayList<Ball> balls;
    protected ArrayList<Paddle> paddles;
    protected ArrayList<Brick> bricks;
    protected ArrayList<Powerup> powerups;
    protected boolean won, lost, dataHasLoaded;
    protected int frameOfWin, frameOfLose, gameStart, playersNo;
    protected String currentLevel, nextLevel, maxLevel;
    protected double gameTime;

    public void settings() {
        size(520, 400);
    }

    public static void main(String[] args) {
        PApplet.main("breaker.App");
    }

    // set for default level
    public void setup() {
        textFont(createFont("src/main/resources/PressStart2P-Regular.ttf", 12));
        surface.setTitle("Breaker");
        frameRate(60);
        dataHasLoaded = false; // a flag to restart
        won = false;
        lost = false;
        playersNo = 0; // no player for default value
        currentLevel = "level1";
        nextLevel = null;
        maxLevel = "level2";
        gameStart = frameCount;
    }

    public void draw() {
        background(0);

        // choose number of player is has no player
        if (playersNo == 0) {
            choosePlayers();
            gameStart = frameCount;
            return;
        }

        // reload data at the begining of new level
        if (!dataHasLoaded) {
            loadData();
        }

        if (lost) {
            gameOver();
        } else if (won) {
            if (currentLevel.equals(maxLevel)) {
                displayYouWin();
            } else {
                displayNextLevel();
            }
        } else {
            displayGameUI();

            // balls interact with bricks and paddles
            for (int i = 0; i < balls.size(); i++) {
                balls.get(i).draw(this);
                // remove the ball which is falling off
                if (balls.get(i).y > 400 && balls.size() > 1) {
                    balls.remove(i);
                    break;
                }

                // ball interact with bricks
                for (int j = 0; j < bricks.size(); j++) {
                    bricks.get(j).draw(this);
                    if (balls.get(i).rebound(bricks.get(j))) {
                        bricks.get(j).getAttack();
                        // check wheather the brick contains powerup when its hp is 0
                        if (bricks.get(j).getHp() == 0) {
                            if (bricks.get(j).getDesc().equals("paddleup")) {
                                powerups.add(new Powerup(loadImage("src/main/resources/paddle_powerup.png"), bricks.get(j).x, bricks.get(j).y, 20, 20, new double[] {0, 1}, "paddleup"));
                            } else if (bricks.get(j).getDesc().equals("multiball")) {
                                powerups.add(new Powerup(loadImage("src/main/resources/ball_powerup.png"), bricks.get(j).x, bricks.get(j).y, 20, 20, new double[] {0, 1}, "multiball"));
                            }
                            bricks.remove(j);
                        }
                    }
                }

                // ball interact with paddles
                for (int k = 0; k < paddles.size(); k++) {
                    paddles.get(k).draw(this);
                    balls.get(i).rebound(paddles.get(k));
                }
            }

            // powerups interact with paddles
            for (int i = 0; i < powerups.size(); i++) {
                powerups.get(i).draw(this);

                for (int j = 0; j < paddles.size(); j++) {
                    if (powerups.get(i).rebound(paddles.get(j))) {
                        if (powerups.get(i).getDesc() == "paddleup") {
                            // change the width of paddle
                            paddles.get(j).setWidth(paddles.get(0).width + 10);
                        } else if (powerups.get(i).getDesc() == "multiball") {
                            // load more balls
                            if (playersNo == 1) {
                                balls.add(new Ball(loadImage("src/main/resources/ball.png"), 390, 250, 10, 10, new double[] {0, -5}));
                                balls.get(balls.size()-1).launch(true);

                                balls.add(new Ball(loadImage("src/main/resources/ball.png"), 130, 250, 10, 10, new double[] {0, -5}));
                                balls.get(balls.size()-1).launch(true);
                            } else if (playersNo == 2) {
                                balls.add(new Ball(loadImage("src/main/resources/ball.png"), 260, 250, 10, 10, new double[] {0, -5}));
                                balls.get(balls.size()-1).launch(true);
                            }
                        }
                        powerups.remove(i);
                        break;
                    }
                }
            }
            checkSituation();
        }
    }

    public void loadData() {
        bricks = new ArrayList<>();
        paddles = new ArrayList<>();
        balls = new ArrayList<>();
        powerups = new ArrayList<>();

        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(currentLevel + ".json")) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            String name = (String) jsonObject.get("name");
            nextLevel = (String) jsonObject.get("next_level");
            JSONArray bricksArray = (JSONArray) jsonObject.get("bricks");

            for (int i = 0; i < bricksArray.size(); i++) {
                JSONObject brick = (JSONObject) bricksArray.get(i);
                String desc = "normal"; // default for desc
                if (brick.size() == 5) {
                    desc = (String) brick.get("powerup");
                }
                Integer x = (int)(long) brick.get("x");
                Integer y = (int)(long) brick.get("y");
                String id = (String) brick.get("id");
                Integer hp = (int)(long) brick.get("hp");
                bricks.add(new Brick(loadImage("src/main/resources/" + id + ".png"), x, y, 20, 10, new double[] {0, 0}, hp, desc));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (playersNo == 1) {
            paddles.add(new Paddle(loadImage("src/main/resources/paddle.png"), 240, 350, 40, 10, new double[] {4, 0}));
            balls.add(new Ball(loadImage("src/main/resources/ball.png"), 260, 340, 10, 10, new double[] {0, -5}));
        } else if (playersNo == 2) {
            paddles.add(new Paddle(loadImage("src/main/resources/paddle.png"), 370, 350, 40, 10, new double[] {4, 0}));
            paddles.add(new Paddle(loadImage("src/main/resources/paddle.png"), 110, 350, 40, 10, new double[] {4, 0}));
            balls.add(new Ball(loadImage("src/main/resources/ball.png"), 390, 340, 10, 10, new double[] {0, -5}));
            balls.add(new Ball(loadImage("src/main/resources/ball.png"), 130, 340, 10, 10, new double[] {0, -5}));
        }
        dataHasLoaded = true;
    }

    public void checkSituation() {
        if (bricks.size() == 0) {
            won = true;
            frameOfWin = frameCount;
        } else if (balls.get(0).y > 400) {
            lost = true;
            frameOfLose = frameCount;
        }
    }

    public void gameOver() {
        // stays for 2 seconds
        if (frameCount - frameOfLose < 120) {
            text("GAME OVER", 125, 200);
            textSize(30);
        } else {
            currentLevel = "Level1";
            dataHasLoaded = false;
            lost = false;
            gameStart = frameCount;
        }
    }

    public void displayNextLevel() {
        // stays for 2 seconds
        if (frameCount - frameOfWin < 120) {
            text("NEXT LEVEL", 115, 200);
            textSize(30);
        } else {
            currentLevel = nextLevel;
            dataHasLoaded = false;
            won = false;
            gameStart = frameCount;
        }
    }

    public void displayYouWin() {
        // stays for 2 seconds
        if (frameCount - frameOfWin < 120) {
            text("YOU WIN", 160, 200);
            textSize(30);
        } else {
            currentLevel = "Level1";
            dataHasLoaded = false;
            won = false;
            gameStart = frameCount;
        }
    }

    public void choosePlayers() {
        fill(0, 153, 0); // selection color
        rect(120, 200, 110, 40, 8); // 1 player box
        rect(280, 200, 110, 40, 8); // 2 player box
        fill(255, 255, 255); // text color
        textSize(14);
        text("Select Number of Players", 100, 180);
        textSize(10);
        text("1 Player", 130, 225);
        text("2 Players", 290, 225);
    }

    public void displayGameUI() {
        // green rectangle at the bottom of screen
        fill(0, 153, 0);
        rect(0, 370, 520, 30, 8);
        fill(255, 255, 255);

        // bottem left corner
        text(currentLevel, 20, 390);
        textSize(10);

        // bottem right corner
        gameTime = Math.round(((frameCount - gameStart) / 60.0) * 100) / 100.0;
        text(String.format("Time %.2f", gameTime), 410, 390);
        textSize(10);
    }

    // detect where the mouse is clicked
    public void mouseClicked() {
        if (mouseX > 120 && mouseX < 330 && mouseY > 200 && mouseY < 240) {
            playersNo = 1;
        }

        if (mouseX > 280 && mouseX < 390 && mouseY > 200 && mouseY < 240) {
            playersNo = 2;
        }
    }

    // detect when the keyboard is pressed
    public void keyPressed() {
        if (keyCode == 37) { // arrow left
            paddles.get(0).moveLeft(true);
            // ball is moving with paddle before launching 
            balls.get(0).moveLeftWithPaddle(paddles.get(0));
        }
        if (keyCode == 39) { // arrow right
            paddles.get(0).moveRight(true);
            balls.get(0).moveRightWithPaddle(paddles.get(0));
        }
        if (keyCode == 32) { // space
            balls.get(0).launch(true);
        }
        if (playersNo == 2) {
            if (keyCode == 65) { // A to move left
                paddles.get(1).moveLeft(true);
                if (balls.size() > 1) {
                    balls.get(1).moveLeftWithPaddle(paddles.get(1));
                }
            }
            if (keyCode == 68) { // D to move right
                paddles.get(1).moveRight(true);
                if (balls.size() > 1) {
                    balls.get(1).moveRightWithPaddle(paddles.get(1));
                }
            }
            if (keyCode == 32) { // space
                balls.get(1).launch(true);
            }
        }
    }

    public void keyReleased() {
        if (keyCode == 37) { // arrow left
            paddles.get(0).moveLeft(false);
        }
        if (keyCode == 39) { // arrow right
            paddles.get(0).moveRight(false);
        }
        if (keyCode == 32) { // space
            balls.get(0).launch(false);
        }
        if (playersNo == 2) {
            if (keyCode == 65) { // A to move left
                paddles.get(1).moveLeft(false);
            }
            if (keyCode == 68) { // D to move right
                paddles.get(1).moveRight(false);
            }
            if (keyCode == 32) { // space
                balls.get(1).launch(true);
            }
        }
    }
}
