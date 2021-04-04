void statusBar() {
  if (!playerTurn && !gameOverBool && turn != 0)
    status = "Enemy is thinking...";
  else if (enemyLastBlock == 0 && !gameOverBool && turn != 0)
    status = "Your turn.";
  else if (enemyLastBlock > 0 && !gameOverBool && turn != 0)
    status = "Your turn. Enemy moved to block #" + enemyLastBlock + ".";
  else
    status = "Game over.";  
  textAlign(CORNER);
  fill(50, 102, 102);
  rect(0, _height, width, height - _height);

  if (playerTurn || gameOverBool) {
    setGradient(xGradient, _height, width, height - _height, color(102, 102, 153), color(153, 51, 77));
    setGradient(xGradient + width, _height, width, height - _height, color(153, 51, 77), color(102, 102, 153));
    setGradient(xGradient + width * 2, _height, width, height - _height, color(102, 102, 153), color(153, 51, 77));
  } else {
    setGradient(xGradient, _height, width, height - _height, color(102, 102, 153, 100), color(153, 51, 77, 100));
    setGradient(xGradient + width, _height, width, height - _height, color(153, 51, 77, 100), color(102, 102, 153, 100));
    setGradient(xGradient + width * 2, _height, width, height - _height, color(102, 102, 153, 100), color(153, 51, 77, 100));
  }

  xGradient -= 5;
  if (xGradient <= width * -2)
    xGradient = 0;
  textAlign(CORNER);
  fill(255);
  textSize(26);
  text(status, 20, height - 15);
  textAlign(CENTER);

  for (int i = 25; i <= 75 - (playerPiecePlaced * 25); i += 25) {
    if (playerStarts)
      if (playerTurn && (75 - (playerPiecePlaced * 25) - i) == 0) { //while piece is in hand, apply transparency
        tint(255, 100);
        image(o, width - i, height - 23.5, 20, 20);
        tint(255, 255);
      } else
        image(o, width - i, height - 23.5, 20, 20);

    else
      if (playerTurn && (75 - (playerPiecePlaced * 25) - i) == 0) { //while piece is in hand, apply transparency
        tint(255, 100);
        image(x, width - i, height - 23.5, 20, 20);
        tint(255, 255);
      } else
        image(x, width - i, height - 23.5, 20, 20);
  }
}

void pause() {
  setGradient(xGradient, 0, width, height, color(102, 102, 153, 130), color(153, 51, 77, 130));
  setGradient(xGradient + width, 0, width, height, color(153, 51, 77, 130), color(102, 102, 153, 130));
  setGradient(xGradient + width * 2, 0, width, height, color(102, 102, 153, 130), color(153, 51, 77, 130));
  xGradient -= 3;
  if (xGradient <= width * -2)
    xGradient = 0;
  rectMode(CORNER);
  rect(0, 0, width, _height);
  fill(30, 170);
  rectMode(CENTER);
  stroke(230);
  rect(width/2, height/2 - 25, width, 300);
  textSize(64);
  fill(230);
  text("GAME PAUSED", width/2, height/3+25);
  textSize(24);
  text("Wins: " + winCounter + " | Draws: " + drawCounter + " | Losses: " + loseCounter, width/2, height/2); 
  rectMode(CORNER);
  translate(width/2, height/2);
  noFill();
  rect(50 - (width/2), 32, 200, 60);
  rect(50, 32, 200, 60);
  //Resume button mouseover
  if (mouseX - (width/2) >= 50 - (width/2) && mouseX - (width/2) <= (50 - (width/2) + 200) && mouseY - (height/2) >= 32 && mouseY - (height/2) <= 32 + 60 && transition == "T0") {
    fill(230, 100);
    rect(50 - (width/2), 32, 200, 60);
   fill(230);
  }  //MainMenu button mouseover
  else if (mouseX - (width/2) >= 50 && mouseX - (width/2) <= (50 + 200) && mouseY - (height/2) >= 32 && mouseY - (height/2) <= 32 + 60 && transition == "T0") {
    fill(230, 100);
    rect(50, 32, 200, 60);
    fill(230);
  }
  translate(-width/2, -height/2);
  text("Resume", (width/2)/2, (height*2)/3-38); 
  text("Conclude", (width/2)/2 + width/2, (height*2)/3-38); 
  //turn = 0;
  stroke(255);
}

void gameOver(char a) {
  rectMode(CORNER);
  if (lineAnimation >= 400 || a == 'D') {
    fill(gameOverAccentColor, 30);
    rect(0, 0, width, _height);
    fill(0, 200);
    rectMode(CENTER);
    stroke(gameOverAccentColor);
    rect(width/2, height/2 - 25, width, 300);
    textSize(64);
  }
  if (a == 'W') {
    if (!gameOverBool && lineAnimation >= 400) {
      sfx[6].play();
      winCounter++;
      gameOverBool = true;
    }
    gameOverAccentColor = color(102, 255, 25);
    fill(gameOverAccentColor);
    if (lineAnimation >= 400 || a == 'D')
      text("YOU WON!", width/2, height/3+25);
  } else if (a == 'L') {
    if (!gameOverBool && lineAnimation >= 400) {
      sfx[8].play();
      loseCounter++;
      gameOverBool = true;
    }
    gameOverAccentColor = color(210, 40, 30);
    fill(gameOverAccentColor);
    if (lineAnimation >= 400 || a == 'D')
      text("YOU LOST!", width/2, height/3+25);
  } else
  {
    if (!gameOverBool) {
      sfx[7].play();
      drawCounter++;
      gameOverBool = true;
    }
    gameOverAccentColor = color(90, 200, 230);
    fill(gameOverAccentColor);
    text("DRAW!", width/2, height/3+25);
  }
  textSize(24);
  if (lineAnimation >= 400)
    text("Wins: " + winCounter + " | Draws: " + drawCounter + " | Losses: " + loseCounter, width/2, height/2); 

  rectMode(CORNER);
  if (lineAnimation >= 400)
    translate(width/2, height/2);
  noFill();
  if (lineAnimation >= 400 || a == 'D')
  {
    rect(50 - (width/2), 32, 200, 60);
    rect(50, 32, 200, 60);
    //Next Round button mouseover
    if (mouseX - (width/2) >= 50 - (width/2) && mouseX - (width/2) <= (50 - (width/2) + 200) && mouseY - (height/2) >= 32 && mouseY - (height/2) <= 32 + 60) {
      fill(gameOverAccentColor, 100);
      rect(50 - (width/2), 32, 200, 60);
      fill(gameOverAccentColor);
    }  //MainMenu button mouseover
    else if (mouseX - (width/2) >= 50 && mouseX - (width/2) <= (50 + 200) && mouseY - (height/2) >= 32 && mouseY - (height/2) <= 32 + 60) {
      fill(gameOverAccentColor, 100);
      rect(50, 32, 200, 60);
      fill(gameOverAccentColor);
    }
    translate(-width/2, -height/2);
    text("Next Round", (width/2)/2, (height*2)/3-38); 
    text("Conclude", (width/2)/2 + width/2, (height*2)/3-38);
  }
  turn = 0;
  if (result == '.')
    result = a;
  stroke(255);
  playerScore = (6 * winCounter) - (1 * drawCounter) - (7 * loseCounter);
  if (turn == 0 && resetBool) {
    nextLevel();
  }
}
