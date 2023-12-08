package tictactoe

import cats.effect.IO
import cats.syntax.all._

object Game {

  def wins(board: Map[Int,String], player: String): Boolean = {
    val horizontal1 = List(board(0), board(1), board(2))
    val horizontal2 = List(board(3), board(4), board(5))
    val horizontal3 = List(board(6), board(7), board(8))
    val vertical1   = List(board(0), board(3), board(6))
    val vertical2   = List(board(1), board(4), board(7))
    val vertical3   = List(board(2), board(5), board(8))
    val diagonal1   = List(board(0), board(4), board(8))
    val diagonal2   = List(board(2), board(4), board(6))

    List(horizontal1, horizontal2, horizontal3, vertical1, vertical2, vertical3, diagonal1, diagonal2)
      .exists(_.forall(_ == player))
  }

  def takeTurn(player: String, board: Map[Int, String], space: Int): IO[Map[Int,String]] = {
    board.toList.map {
      case (i,s) if i == space && !s.isBlank =>
        IO.raiseError(Error(s"This space is already filled with $s"))
      case (i,_) if i == space =>
        IO(i,player)
      case (i,s) =>
        IO(i,s)
    }.parUnorderedSequence.map(_.toMap)
  }

  def alternatePlayer(player: String): String =
    if (player == "x") "o" else "x"

}
