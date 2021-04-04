void nextLevel() {
  inGameMusicBools[0] = true;
  if (mainMenuMusic)
    musicCounter = 2;
  else
    musicCounter++;
  sfxLineAnimation = false;
  gameOverBool = false;
  resetBool = false;
  turn = 6;
  lineAnimation = 0;
  enemyLastBlock = 0;
  playerPiecePlaced = 0;
  squares = new PVector[10]; //Eligible coordinates for pieces to move into 
  pieces = new char[10]; // '.' = Empty, 'E' = Enemy, 'F' = Friendly
  playerStarts = false;
  playerTurn = false;
  result = '.';
  gameOverAccentColor = color(255);
  savedTime = millis();

  for (int i = 1, x = 100, y = 100; i < squares.length; i++, x += 200) {
    if (x > width) {
      x = 100;
      y += 200;
    }
    squares[i] = new PVector(x, y);
  }

  for (int i = 0; i < pieces.length; i++)
    pieces[i] = '.';
  playerStarts = rngPlayerAssignment();
  playerTurn = playerStarts;
  lineAnimationPath();
}

void loading() {
  imageMode(CORNER);
  image(loadingTop, 0, -(height/2) + yTransition1);
  image(loadingBottom, 0, 650 + yTransition2);
  imageMode(CENTER);

  if (transition == "T1") {
    yTransition1 += yTransitionSpeed;
    yTransition2 -= yTransitionSpeed;
    if (yTransition1 > 325 && yTransition2 < -325 && transition == "T1") {
      if (page == "MainMenu") {
        leaderBoardBool = false;
        mainMenuBool = true;
        lineAnimation = 0;
        winCounter = 0; 
        loseCounter = 0;
        drawCounter = 0;
        pauseBool = false;
      } else if (page == "Game") {
        mainMenuBool = false;
        nextLevel();
      } else if (page == "ScoreBoard")
        playerNameTextInputBool = true;
      else if (page == "LeaderBoard") {
        mainMenuBool = false;
        leaderBoardBool = true;
    }
      else if (page == "Exit")
        exit();
      transition = "T2";
    }
  } 
  if (transition == "T2") {
    yTransition1 -= yTransitionSpeed;
    yTransition2 += yTransitionSpeed;
    if (yTransition1 < -10 && yTransition2 > 10) {
      yTransition1 = 0;
      yTransition2 = 0;
      transition = "T0";
    }
  }
}
