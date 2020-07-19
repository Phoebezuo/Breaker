# Breaker App

A simple desktop game called Breaker. Breaker is a simple Breakout style game where the aim of game is break the bricks within the stage and to continue until the player loses. 

## Install Gradle

### Linux & MacOS

Use SDK to install gradl

```bash
# intall vis sdk
sdk install gradle

# install java specific version 
sdk install java 8.0.262-zulu

# tell sdkman to use a specific version
sdk use java 8.0.262-zulu
```

### Windows

Install [this]([]()) package manager

Follow a set of instructions [here]([]())

Then install gradle via chocolatey

```bash
choco install gradle
```

More information on [how to install gradle]([]())

## Run the Programming Using Gradle

Change to current folder and run the program

```bash
# build the java program 
gradle build

# run the program
gradle run
```

## Game Objects and Mechnics

### Paddle

The player controls a paddle which is in charge of launching the ball. The player will interact with the paddle using the keyboard arrow keys. When the game is launched, the paddle will be in the bottom-centre of the screen with a ball resting on top of it. The player can move the paddle in this state, using the left arrow key to move left and the right arrow key to move right. Once the space key has been pressed, the ball will launch.

The paddle is the determining factor for changing the vector of the ball. When the ball lands on the paddle it will interpolate and adjust the x and y component based on the centre of the paddle. When the ball lands in the centre of the paddle, the x-component will be 0 and the ycomponent will be 5 , when landing on the edge or corner of the paddle, the x-component will be 5 and the y-component will be 0 (it cannot result in 0 as the ball would continue to bound between the side of the wall and the paddle). You can refer to the algorithim section to assist with calculating the new x component and y component of the vector.

The paddle will be 40 pixels wide and 10 pixels high (40x10).

### Ball

The ball is the primary object that determines if the player wins or loses the game. It is used to destroy Brick objects within the level and will determine if the player wins by destroying all bricks or loses when the ball y-coordinate exceeds the screen's height. The Ball has a vector which dictates its direction and velocity, you will need to use this vector as part of your rebound calculations.

We will provide a rebound algorithm to deal handle collision detection and rebounds. To note, the collision detection component of this game is typically the most diﬃcult part as there are corner cases to consider. Getting this component absolutely correctly is not necessary and we do encourage you to use a simple collision detection as noted in the algorithms section.

### Brick

A level or stage is constructed with bricks. When a Ball contacts a Brick, it will be destroyed and will no longer be collidable by the ball. A Brick will have 20 pixels wide and 10 pixels hight (20x10). A Brick will have an assigned assigned sprite which you will be able to access from the resources folder in your gradle project.

### Level

To allow other employees to work on the game after the prototype, you will need to use a JSON parser and load a level. JSON is a serialisable and human readable data format which can be communicated over a network and used as a ﬁle for applications. Using the json-simple package and the following documentation, you will be able to load the level and construct it prior to gameplay.

Each level will contain the following ﬁelds:

- name - String, will be the name of the level
- bricks - Array, will store individual bricks with their own x-y coordinates 
- brick - Object, will hold an x and y coordinate and a sprite id. All brick objects will use maintain the same size as deﬁned in the Brick section.

```json
{
  	"name" : "Level1", 
  	"bricks" : [
		{ "x" : 0, "y" : 0, "id" : "brick_blue" }, 
      		{ "x" : 20, "y" : 0, "id" : "brick_red" }, 
      		{ "x" : 40, "y" : 0, "id" : "brick_pink" },
      		...
	]
}
```

The walls of a level will be the boundary of the screen itself, if the ball exceeds screen width or ball x is 0, the ball will be rebound. If the ball hits the top of the screen it will also rebound, however the ball will not rebound if it hits the bottom of the screen.

## Game Conditions

The goal of the is for the player to destroy all bricks. The player wins when the following conditions have been met.

- All bricks have been destroyed

The player will lose when

- The ball reches the bottom of the screen and cannot be hit by the paddle.

## UML

![UML](Breaker/UML.png)

## Game UI

![StartPage](Breaker/StartPage.png)

![PlayPage](Breaker/PlayPage.png)

## Extension 

### Single Player Mode

-   Have only one ball and one paddle
-   Use arrow key to move left and right, use space to launch the ball at the begining 
-   When the paddle hit multiball, there will be additional two ball 
-   When the paddle hit paddleup, the paddle will be wider

### Double Players Mode 

-   Have two balls and two paddles 
-   Player A use arrow key to move left and right, player B use A and D to move, use space key to launch the ball at the same time 
-   When one of paddles hit multiball, there will be only one additional ball 
-   When one of paddles hit paddleup, the paddle that was rebounded get wider. 

## App Class

-   Primary setting for the game

-   Draw function to update 

-   Display Gmae user interface, including level, time, chosing number of players, etc. 

-   Load data from json file (e.g. bricks & level) or indicate by user (e.g. how many players), default level start from level 1 

    JSON file example 

    ``` json
    {
        "name" : "level1",
        "next_level" : "level2",
        "bricks" : [
    
            {"x" : 0, "y" : 0, "id" : "brick_red", "hp" : 1},
            {"x" : 0, "y" : 10, "id" : "brick_red", "hp" : 3},
            {"x" : 0, "y" : 20, "id" : "brick_red", "hp" : 1},
    
            {"x" : 20, "y" : 0, "id" : "brick_blue", "hp" : 1, "powerup" : "paddleup"},
            {"x" : 20, "y" : 10, "id" : "brick_blue", "hp" : 4},
            {"x" : 20, "y" : 20, "id" : "brick_blue", "hp" : 1},
    
            {"x" : 40, "y" : 0, "id" : "brick_pink", "hp" : 2},
            {"x" : 40, "y" : 10, "id" : "brick_pink", "hp" : 2, "powerup" : "multiball"},
            {"x" : 40, "y" : 20, "id" : "brick_pink", "hp" : 1},
            
    		{"x" : 520, "y" : 0, "id" : "brick_pink", "hp" : 1},
            {"x" : 520, "y" : 10, "id" : "brick_pink", "hp" : 6},
            {"x" : 520, "y" : 20, "id" : "brick_pink", "hp" : 1},
        ]
    }
    ```

-   Check the situation of the game, either win or lose 

-   Override functions such as mouseClicked, keyPressed, keyReleased, etc. 

## AbstractObject Class

-   Parent class of ball, paddle and brick 

-   Contains attributes of x, y, width, height and velocity 

-   An abstract class draw(PApplet app), needed to be implement in each different child class 

-   Rebound alogrithem 

    ``` java
    public boolean rebound(AbstractObject rect) {
            if (x + width + velocity[0] > rect.x &&
                x + velocity[0] < rect.x + rect.width &&
                y + height > rect.y &&
                y < rect.y + rect.height) {
                    // interpolation
                    if (rect instanceof Paddle) {
                        velocity[0] = - 5 + (Math.abs(x - rect.x) * (10 / (double) rect.width));
                        velocity[1] =  - Math.abs(-5 - velocity[0]);
                    } else {
                        // rebound from the bricks or paddles
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
    ```

     -    When object 1 rebound with object 2, such as ball with bricks, it changes its velocity by times -1 

          ``` java
          velocity[0] *= -1;
          velocity[1] *= -1;
          ```

    -  When object rebound with paddle, it implement the interpolation alogarithm

        ``` java
         velocity[0] = - 5 + (Math.abs(x - rect.x) * (10 / (double) rect.width));
        // double is used due to / sign of two integers only gives integer, but I want double, so I type casting one of them, java can automate type casting from int to double 
         velocity[1] =  - Math.abs(-5 - velocity[0]);
        ```

## Ball Class

-   Move function to change x, y depends on its velocity 
-   Refect function is used when rebound with boundary 
-   Launch function is determined after user press space
-   MoveLeftWithPaddle and MoveRightWithPaddle also determine by arrow key before launch 

## Brick Class 

-   GetAttack class is used when ball is rebound with bricks, so that the brick's hp minus one 

## Paddle Class

-   MoveLeft and MoveRight determined by the arrow key 
-   setWidth is triggered when it rebound with paddleup powerup
-   Has child class Powerup 

## Powerup Class

-   Move function so that after bricks is removed, it falling downwards 

