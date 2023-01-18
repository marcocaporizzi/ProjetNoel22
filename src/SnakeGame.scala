import hevs.graphics.FunGraphics
import java.awt.event.{KeyAdapter, KeyEvent}
import java.awt.{Color, Rectangle}

object SnakeGame extends App {
  case class Position(var x: Int, var y: Int); //case class implements the equals method for you while a class does not. Test les valeurs des propriétés plus tot que l'adresse mémoire.

  val snakeMaxLength: Int = 16
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


  // Do somethsing when a key has been pressed
  fg.setKeyManager(new KeyAdapter() {
    override def keyPressed(e: KeyEvent): Unit = {
      if (e.getKeyChar == 'w' && direction != dir_down) direction = dir_up
      if (e.getKeyChar == 's' && direction != dir_up) direction = dir_down
      if (e.getKeyChar == 'a' && direction != dir_right) direction = dir_left
      if (e.getKeyChar == 'd' && direction != dir_left) direction = dir_right
    }


  })

  //Snake boarders

  while (true) {

    var GameOver = false

    var snakeParts: Array[Position] = new Array(snakeMaxLength)
    snakeParts(0) = new Position(10, 10)
    snakeParts(1) = new Position(11, 10)
    snakeParts(2) = new Position(12, 10)
    direction = 1

    var foodAppearance: Position = randomfood()

    snakeLength = 3

    while (GameOver == false) {
      fg.clear(Color.white)

      //
      val (dx, dy) =  //WASD
        if(direction == dir_up)  (0, -1) //haut  //match_case puis if_else
        else if (direction == dir_down) (0, +1) //bas
        else if (direction == dir_right)  (+1, 0) //droite
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
      val food = fg.drawRect(new Rectangle(foodAppearance.x*gridSizePx,foodAppearance.y*gridSizePx,gridSizePx,gridSizePx))

      // Display snake
      for (p <- 0 until snakeLength) {
        val part = snakeParts(p)
        // Set the head's color different than the body color
        if (p == 0) fg.setColor(Color.blue) else fg.setColor(Color.red)
        fg.drawRect(new Rectangle(part.x * gridSizePx, part.y * gridSizePx, gridSizePx, gridSizePx))
      }

      //Food gets respawned
      if (snakeParts(0) == foodAppearance) {
        foodAppearance = randomfood()
        snakeParts(snakeLength) = oldSnakeParts (snakeLength-1)
        snakeLength += 1
      }

      Thread.sleep(200)
    }

  }
}
/*
  case class State(snake: Array[Array[Double]], food: Array[Array[Double]]) {
    def newState(dir: Int): State = {



      val (x, y) = snake.head
val (newx, newy) = dir match { //WASD
        case 1 => (x, y - 1) //haut
        case 2 => (x, y + 1) //bas
        case 3 => (x - 1, y) //droite
        case 4 => (x + 1, y) //gauche
        case _ => (x, y)
      }

      val newSnake = {

        if (newx < 0 || newx >=  || newy > 0 || newy >=  || snake.tail.contains((newx, newy))) //out of bound or head hitting my tail
          initialSnake //return to initial position

      }
    }
  }
}

*/