package io.barni.nicecactus.util

import io.barni.nicecactus.model.Move
import io.barni.nicecactus.model.Move._
import org.scalatest.{ Matchers, WordSpec }

import scala.annotation.tailrec
import scala.util.Random

class ParsableSpec extends WordSpec with Matchers {

  val usesCases: Map[Move, List[String]] = Map(
    Scissors -> List("Scissors", "S", "scissors", "s"),
    Paper -> List("Paper", "P", "paper", "p"),
    Rock -> List("Rock", "R", "rock", "r")
  )
  val validInputs: List[String] = usesCases.values.flatten.toList

  usesCases.foreach {
    case (result, inputs) =>
      s"$result" should {
        inputs.foreach { input =>
          s"be parsed from $input" in {
            Parsable[Move].parse(input) shouldBe Right(result)
          }
        }
      }
  }

  "Parsing" should {

    @tailrec
    def generateRandomInvalidInput: String = {
      val length = Random.nextInt(10)
      val str: String = Random.alphanumeric.take(length).mkString("")
      if (validInputs.contains(str)) {
        generateRandomInvalidInput
      } else {
        str
      }
    }

    val input = generateRandomInvalidInput
    s"fail for invalid $input" in {
      Parsable[Move].parse(input) shouldBe Left(input)
    }

  }

}
