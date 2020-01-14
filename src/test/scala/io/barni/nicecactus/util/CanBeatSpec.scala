package io.barni.nicecactus.util

import io.barni.nicecactus.model.Move
import io.barni.nicecactus.model.Move._
import io.barni.nicecactus.util.CanBeat.syntax._
import org.scalatest.{Matchers, WordSpec}

class CanBeatSpec extends WordSpec with Matchers {
  "Scissors" should {
    "beat Paper" in {
      beatsButNotBeaten(Scissors,Set(Paper))
    }
  }

  "Paper" should {
    "beat Rock" in {
      beatsButNotBeaten(Paper,Set(Rock))
    }
  }

  "Rock" should {
    "beat Scissors" in {
      beatsButNotBeaten(Rock,Set(Scissors))
    }
  }

  def beatsButNotBeaten(input: Move, beaten: Set[Move]) = {
    CanBeat[Move].beats(input) shouldBe beaten
    input.beats shouldBe beaten
    beaten.foreach { move =>
      assert(!move.beats.contains(input))
    }
  }
}
