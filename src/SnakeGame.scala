import hevs.graphics.FunGraphics

import java.awt.event.{KeyAdapter, KeyEvent}
import java.awt.{Color, Rectangle}

object SnakeGame extends App {
  class Position(var x: Int, var y: Int);

  val fg: FunGraphics = new FunGraphics(640, 480)
  var snakeLength: Int = 3 // Current size of the snake
  val snakeMaxLength: Int = 16
  val gridSizePx: Int = 32
  var snakeParts: Array[Position] = new Array(snakeMaxLength)
  snakeParts(0) = new Position(10, 10)
  snakeParts(1) = new Position(11, 10)
  snakeParts(2) = new Position(12, 10)
  var direction = 1

  // Do something when a key has been pressed
  fg.setKeyManager(new KeyAdapter() {
    override def keyPressed(e: KeyEvent): Unit = {
      if (e.getKeyChar == 'w') direction = 1
      if (e.getKeyChar == 's') direction = 2
      if (e.getKeyChar == 'a') direction = 4
      if (e.getKeyChar == 'd') direction = 3
    }
  })


  while (true) {
    fg.clear(Color.white)

    //
    val (dx, dy) = direction match { //WASD
      case 1 => (0 ,-1) //haut
      case 2 => (0, +1) //bas
      case 3 => (+1, 0) //droite
      case _ => (-1, 0) //gauche
    }

    // Move the snake body parts by shifting each part towards the end of the array
    val oldSnakeParts = snakeParts.clone()
    for (partIndex <- 0 until snakeLength) {
      snakeParts(partIndex+1) = oldSnakeParts(partIndex)
    }

    // Move the head
    snakeParts(0) = new Position(snakeParts(0).x + dx, snakeParts(0).y + dy)


    // Display snake
    for(p <- 0 until snakeLength) {
      val part = snakeParts(p)
      // Set the head's color different than the body color
      if(p == 0) fg.setColor(Color.blue) else fg.setColor(Color.red)
      fg.drawRect(new Rectangle(part.x*gridSizePx, part.y*gridSizePx, gridSizePx, gridSizePx))
    }

    Thread.sleep(100)
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