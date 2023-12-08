package tictactoe

import cats.effect.{ExitCode, IO, IOApp}
import scala.util.{Failure, Success, Try}

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    print("\n+---+---+---+\n| 6 | 7 | 8 |\n| 3 | 4 | 5 |\n| 0 | 1 | 2 |\n+---+---+---+\n")
    val initialBoard: Map[Int, String] = (0 to 8).flatMap(i => Map(i -> "")).toMap

    def placeMarker(space: String, spaceNumber: String): String =
      if (space.isBlank) spaceNumber else space

    def drawBoard(boardState: Map[Int, String]): String =
      s"\n+---+---+---+\n| ${placeMarker(boardState(6), "6")} | ${placeMarker(boardState(7),"7")} | ${placeMarker(boardState(8), "8")} |\n| ${placeMarker(boardState(3), "3")} | ${placeMarker(boardState(4), "4")} | ${placeMarker(boardState(5), "5")} |\n| ${placeMarker(boardState(0), "0")} | ${placeMarker(boardState(1), "1")} | ${placeMarker(boardState(2), "2")} |\n+---+---+---+\n"

    def takePlayerInput(player: String): IO[Int] = {
      val input = io.StdIn.readLine(s"Player $player, choose your space between 0 and 8 ")
      Try(input.toInt) match {
        case Success(i) => IO(i)
        case Failure(e) => IO.raiseError(e)
      }
    }

    def playGame(player: String, boardState: Map[Int, String]): IO[Unit] = {
      (for {
        move <- takePlayerInput(player)
        newState <- Game.takeTurn(player, boardState, move)
        res <- Game.wins(newState, player) match {
          case true => IO(println(s"Congrats player $player, you win!"))
          case false if newState.values.forall(_.nonEmpty) =>
            IO(println("It's a draw!"))
          case false =>
            println(s"No wins this turn.")
            println(drawBoard(newState))
            playGame(Game.alternatePlayer(player), newState)
        }
      } yield res)
        .handleErrorWith { err =>
          println(s"Service terminating with error: ${err.getMessage}")
          IO.raiseError(err)
        }
    }

    playGame("x", initialBoard).as(ExitCode.Success)
  }

}
