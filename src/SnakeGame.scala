import hevs.graphics.FunGraphics
import java.awt.event.{KeyAdapter, KeyEvent}
import java.awt.{Color, Rectangle}

object SnakeGame extends App {
  case class Position(var x: Int, var y: Int) //case class implements the equals method for you while a class does not. Test les valeurs des propriétés plus tot que l'adresse mémoire.

  val snakeMaxLength: Int = 32

  val boardWidth = 20
  val boardHeight = 18

  val DirUp = 1
  val DirDown = 2
  val DirLeft = 4
  val DirRight = 3

  val gridSizePx: Int = 32

  val fg: FunGraphics = new FunGraphics(gridSizePx*boardWidth, gridSizePx*boardHeight)

  var direction = 1

  // Returns a random position in the board
  def getRandomPositionInBoard() : Position = {
    new Position(
      (math.random()*boardWidth).toInt,
      (math.random()*boardHeight).toInt
    )
  }

  //Do something when a key has been pressed
  fg.setKeyManager(new KeyAdapter() {
    override def keyPressed(e: KeyEvent): Unit = {
      if (e.getKeyChar == 'w' && direction != DirDown) direction = DirUp
      if (e.getKeyChar == 's' && direction != DirUp) direction = DirDown
      if (e.getKeyChar == 'a' && direction != DirRight) direction = DirLeft
      if (e.getKeyChar == 'd' && direction != DirLeft) direction = DirRight
    }
  })

  while (true) {
    var gameOver = false

    var snakeParts: Array[Position] = new Array(snakeMaxLength)
    snakeParts(0) = new Position(10, 10)
    snakeParts(1) = new Position(11, 10)
    snakeParts(2) = new Position(12, 10)
    var snakeLength = 3
    direction = DirUp

    var foodPosition: Position = getRandomPositionInBoard()

    while (!gameOver) {
      fg.clear(Color.black)

      val (dx, dy) = direction match {
        case DirUp => (0, -1)
        case DirDown => (0, 1)
        case DirLeft => (-1 ,0)
        case DirRight => (1, 0)
      }

      val oldSnakeParts = snakeParts.clone()

      // Move the head to its new position
      snakeParts(0) = new Position(snakeParts(0).x + dx, snakeParts(0).y + dy)

      // Move the body parts
      for (partIndex <- 0 until snakeLength - 1) {
        // Shift each part towards the end of the array
        snakeParts(partIndex + 1) = oldSnakeParts(partIndex)

        // Check for collision between the head and this body part
        if ( snakeParts(0) == snakeParts(partIndex+1) ) {
          gameOver = true
        }
      }

      // Checks for collision between the head and the board borders
      gameOver = gameOver || snakeParts(0).x >= boardWidth ||
        snakeParts(0).y >= boardHeight ||
        snakeParts(0).x < 0 ||
        snakeParts(0).y < 0

      // Check if the player just ate the food
      if (snakeParts(0) == foodPosition) {
        do {
          foodPosition = getRandomPositionInBoard()
        }
        while(snakeParts.contains(foodPosition))

        // Grow the snake by adding a new part at its end, only if it's not at max length
        if (snakeLength < snakeMaxLength) {
          snakeParts(snakeLength) = oldSnakeParts(snakeLength - 1)
          snakeLength += 1
        }
      }

      // Display the food
      fg.setColor(Color.red)
      fg.drawFillRect(foodPosition.x*gridSizePx,foodPosition.y*gridSizePx,gridSizePx,gridSizePx)

      // Display the snake
      for (p <- 0 until snakeLength) {
        val part = snakeParts(p)

        // Set the head's color different than the body color
        fg.setColor(if(p == 0) Color.blue else Color.green)

        fg.drawFillRect(part.x * gridSizePx, part.y * gridSizePx, gridSizePx, gridSizePx)
      }

      Thread.sleep(200)
    }

  }
}

// Use of case class because on 105 we have to compare to Position. Without case class it would be trickier. (we would have to manually compare the x and y attributes
// Code architecture: we have 2 loops, 1 infinite and 1 which ends with GameOver
// State of the game needs to be the same at each new start: some variables need to be reassign at their default values
// For code writing: it's an array of ( var x, var y )
// In order to move the snake: we move the head position in the chosen direction then we move each bodypart N where bodypart N-1 was last iteration
// The game ends when the snake tries to go out of bound or the head's position is the same as a bodypart.