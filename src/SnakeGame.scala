import hevs.graphics.FunGraphics
import java.awt.event.{KeyAdapter, KeyEvent}
import java.awt.{Color, Rectangle}

object SnakeGame extends App {
  case class Position(var x: Int, var y: Int) //case class implements the equals method for you while a class does not. Test les valeurs des propriétés plus tot que l'adresse mémoire.

  val snakeMaxLength: Int = 256
  val boardWidth = 20
  val boardHeight = 18

  val dir_up = 1
  val dir_down = 2
  val dir_left = 4
  val dir_right = 3

  val gridSizePx: Int = 32

  val fg: FunGraphics = new FunGraphics(gridSizePx*boardWidth, gridSizePx*boardHeight)

  var snakeLength: Int = 3 // Current size of the snake
  var direction = 1

  //randomfood
  def randomfood() : Position = {
    new Position((math.random()*boardWidth).toInt,(math.random()*boardHeight).toInt)
  }

  //Do something when a key has been pressed
  fg.setKeyManager(new KeyAdapter() {
    override def keyPressed(e: KeyEvent): Unit = {
      if (e.getKeyChar == 'w' && direction != dir_down) direction = dir_up
      if (e.getKeyChar == 's' && direction != dir_up) direction = dir_down
      if (e.getKeyChar == 'a' && direction != dir_right) direction = dir_left
      if (e.getKeyChar == 'd' && direction != dir_left) direction = dir_right
    }
  })

  while (true) {
    var GameOver = false

    var snakeParts: Array[Position] = new Array(snakeMaxLength)
    snakeParts(0) = new Position(10, 10)
    snakeParts(1) = new Position(11, 10)
    snakeParts(2) = new Position(12, 10)
    direction = dir_up

    var foodAppearance: Position = randomfood()
    snakeLength = 3

    while (GameOver == false) {
      fg.clear(Color.black)


      val (dx, dy) =  //WASD
        if(direction == dir_up)  (0, -1)   //match_case puis if_else
        else if (direction == dir_down) (0, +1)
        else if (direction == dir_right)  (+1, 0)
        else  (-1, 0) //gauche

      // Move the snake body parts by shifting each part towards the end of the array
      val oldSnakeParts = snakeParts.clone()

      // Move the head
      snakeParts(0) = new Position(snakeParts(0).x + dx, snakeParts(0).y + dy)

      for (partIndex <- 0 until snakeLength) {
        snakeParts(partIndex + 1) = oldSnakeParts(partIndex)
        if ( snakeParts(0) == snakeParts(partIndex+1) ) {
          GameOver = true
        }
      }

      // Snake moving and GameOver
      if (boardWidth <= snakeParts(0).x) {
        snakeParts(0).x = boardWidth - 1
        GameOver = true
      }
      if (boardHeight <= snakeParts(0).y) {
        snakeParts(0).y = boardHeight - 1
        GameOver = true
      }
      if (snakeParts(0).x <= 0) {
        snakeParts(0).x = 0
        GameOver = true
      }
      if (snakeParts(0).y <= 0) {
        snakeParts(0).y = 0
        GameOver = true
      }

      // Display food
      fg.setColor(Color.red)
      val food = fg.drawFillRect(foodAppearance.x*gridSizePx,foodAppearance.y*gridSizePx,gridSizePx,gridSizePx)

      // Display snake
      for (p <- 0 until snakeLength) {
        val part = snakeParts(p)
        // Set the head's color different than the body color
        if (p == 0) fg.setColor(Color.blue) else fg.setColor(Color.green)
        fg.drawFillRect(part.x * gridSizePx, part.y * gridSizePx, gridSizePx, gridSizePx)
      }

      //Food gets respawned and snake grows
      if (snakeParts(0) == foodAppearance) {
        foodAppearance = randomfood()
        if (snakeLength < snakeMaxLength) {
          snakeParts(snakeLength) = oldSnakeParts(snakeLength - 1)
          snakeLength += 1
        }
      }

      Thread.sleep(1000)
    }

  }
}

//Use of case class because on 105 we have to compare to Position. Without case class it would be trickier.
//Code architecture: we have 2 loops, 1 infinite and 1 which ends with GameOver
//State of the game needs to be the same at each new start: some variables need to be reassign
//For code writing: it's an array of ( var x, var y )
//In order to move the snake: we copy the old position and reassign the head position then we move each part of the new snake by the direction choosen
//The game ends when the snake tries to go out of bound or the head's position is the same as a bodypart.