import scala.annotation.tailrec
import scala.util.Random

object Main {
  def main(args: Array[String]): Unit = {
    val m = init_game(10, 10, 10)
    val game = new MineSweeper(m, 10)
    game.display()
    while (!game.end) {
      println("Entrez les coordonnées de la case à découvrir (i j):")
      val i = scala.io.StdIn.readInt()
      val j = scala.io.StdIn.readInt()

      game.interact(i, j)
      game.display()
    }

    println("Fin de partie !")
  }

  def get_dimension(m: Array[Array[Case]]): (Int, Int) = {
    (m.length, m(0).length)
  }

  def is_inside(m: Array[Array[Case]], i: Int, j: Int): Boolean = {
    val (n, p) = get_dimension(m)
    i >= 0 && i < n && j >= 0 && j < p
  }

  def get_neighbors(m: Array[Array[Case]], i: Int, j: Int): Array[(Int, Int)] = {
    val neighbors = Array((i - 1, j - 1), (i - 1, j), (i - 1, j + 1), (i, j - 1), (i, j + 1), (i + 1, j - 1), (i + 1, j), (i + 1, j + 1))
    neighbors.filter { case (x, y) => is_inside(m, x, y) }
  }

  def random_coords(m: Array[Array[Case]]): (Int, Int) = {
    val (n, p) = get_dimension(m)
    (Random.nextInt(n), Random.nextInt(p))
  }

  def incr_tab(m: Array[Array[Case]], i: Int, j: Int): Array[Array[Case]] = {
    if (m(i)(j).value != -1) {
      m(i)(j).value += 1
    }
    m
  }

  @tailrec
  def random_mine(m: Array[Array[Case]]): Array[Array[Case]] = {
    val (i, j) = random_coords(m)
    if (m(i)(j).value == -1) {
      random_mine(m)
    } else {
      m(i)(j) = init_case(-1)
      get_neighbors(m, i, j).foreach { case (i, j) => incr_tab(m, i, j) }
      m
    }
  }

  def init_case(k: Int): Case = {
    if (k == -1) {
      new Mine
    } else {
      new Empty(k)
    }
  }


  def init_game(w: Int, h: Int, n: Int): Array[Array[Case]] = {
    val m = Array.ofDim[Case](w, h)
    for (i <- 0 until w) {
      for (j <- 0 until h) {
        m(i)(j) = init_case(0)
      }
    }
    (1 to n).foldLeft(m)((m, _) => random_mine(m))
    m
  }

}
