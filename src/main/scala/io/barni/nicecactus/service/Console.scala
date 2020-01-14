package io.barni.nicecactus.service

import io.barni.nicecactus.util.IO

import scala.io.StdIn.readLine

trait Input {
  def getStrLn: IO[String]
}

trait Output {
  def putStrLn(line: String): IO[Unit]
  def putStr(line: String): IO[Unit]
}

trait Console extends Input with Output

trait LiveConsole extends Console {
  override def putStrLn(line: String): IO[Unit] = IO.unit(println(line))
  override def putStr(line: String): IO[Unit] = IO.unit(print(line))

  override def getStrLn: IO[String] = IO.unit(readLine())
}

object LiveConsole extends LiveConsole