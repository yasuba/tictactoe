package tictactoe

import org.scalatest.funsuite.AnyFunSuite
import cats.effect.unsafe.IORuntime

class TicTacToeSuite extends AnyFunSuite {


  /*
    Game starts with 9 empty spaces
    2 players take turns to fill spaces
    First player places an 'x' in one of the spaces
    Second player places an 'o' in another space
    Game ends when one player first creates a 'row' of their symbol

    A winning 'row' can be the same symbol
    on "0,1,2", "3,4,5", "6,7,8" (horizontals)
    Or "0,3,6", "1,4,7", "2,5,8" (verticals)
    Or "0,4,8", "2,4,6" (diagonals)
   */

  implicit val runtime: IORuntime = IORuntime.global

  val board: Map[Int, String] = (0 to 8).flatMap(i => Map(i -> "")).toMap

  def winningBoard(space1: Int, space2: Int, space3: Int, board: Map[Int, String]): Map[Int, String] =
    board.map{
      case (i,_) if i == space1 || i == space2 || i == space3 => (i, "x")
      case other => other
    }

  test("player can win by filling spaces horizontally on '0,1,2'") {
    assert(Game.wins(winningBoard(0,1,2,board), "x"))
  }

  test("player can win by filling spaces horizontally on '3,4,5'") {
    assert(Game.wins(winningBoard(3,4,5,board), "x"))
  }

  test("player can win by filling spaces horizontally on '6,7,8'") {
    assert(Game.wins(winningBoard(6,7,8,board), "x"))
  }

  test("player can win by filling spaces vertically on '0,3,6'") {
    assert(Game.wins(winningBoard(0,3,6,board), "x"))
  }

  test("player can win by filling spaces vertically on '1,4,7'") {
    assert(Game.wins(winningBoard(1,4,7,board), "x"))
  }

  test("player can win by filling spaces vertically on '2,5,8'") {
    assert(Game.wins(winningBoard(2,5,8,board), "x"))
  }

  test("player can win by filling spaces diagonally on '0,4,8'") {
    assert(Game.wins(winningBoard(0,3,6,board), "x"))
  }

  test("player can win by filling spaces diagonally on '2,4,6'") {
    assert(Game.wins(winningBoard(2,4,6,board), "x"))
  }

  test("player1 can place 'x' in 'middle' space") {
    val result: Map[Int, String] = Game.takeTurn("x", board, 4).unsafeRunSync()
    assert(result(4) == "x")
  }

  test("player1 makes first move but does not win immediately") {
    val firstMove = Game.takeTurn("x", board, 4).unsafeRunSync()
    assert(!Game.wins(firstMove, "x"))
  }

  test("a player cannot fill a space which is already filled") {
    val firstMove  = Game.takeTurn("x", board, 4).unsafeRunSync()
    assertThrows[Error](Game.takeTurn("o", firstMove, 4).unsafeRunSync())
  }

  test("player2 makes second move but does not win yet") {
    val firstMove  = Game.takeTurn("x", board, 4).unsafeRunSync()
    val secondMove = Game.takeTurn("o", firstMove, 3).unsafeRunSync()
    assert(!Game.wins(secondMove, "o"))
  }

  test("player1 can win after three goes") {
    val firstMove  = Game.takeTurn("x", board, 4).unsafeRunSync()
    val firstMove2 = Game.takeTurn("o", firstMove, 3).unsafeRunSync()
    val secondMove = Game.takeTurn("x", firstMove2, 1).unsafeRunSync()
    val secondMove2 = Game.takeTurn("o", secondMove, 2).unsafeRunSync()
    val thirdMove = Game.takeTurn("x", secondMove2, 7).unsafeRunSync()
    assert(Game.wins(thirdMove, "x"))
  }
}
