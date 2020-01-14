# Rock - Paper - Scissors

# To run
- Either of the following commmands: `sbt run` / `bloop run root`

# About the game
- It promps the valid moved
- You can either play against Bender or watch Bender play against C3PO

# Add new moves

To add new moves:
 - Add the new move to the io.barni.nicecactus.model.Move
 - Add the tests to have the expected behaviour
    - With the help of the `helper` function you can be sure that one beats the other then the other doesn't beat the one
    - Add `CanBeat` instances for the new move to pass the tests
 - Add extra valid inputs to the `Parse[Move]` implicit.
 - Add test to the `CanMoveSpec` to make sure it can be parsed

 - Add new move to the `Moves` so the robot can chose it as well
