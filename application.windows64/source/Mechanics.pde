void enemyMove() {
  currentTime = millis();
  if (currentTime - savedTime > random(900, 1700))
  {
    if (turn != 0 && !pauseBool) {
      savedTime = currentTime;
      int block = int(random(1, squares.length));
      if (!playerTurn && pieces[block] == '.') {
        image(o, squares[block].x, squares[block].y);
        sfx[4].play();
        turn--;
        pieces[block] = 'E';
        enemyLastBlock = block;

        switch (block) {
        case 1: 
          if ((pieces[2]=='E' && pieces[3]=='E') || (pieces[4]=='E' && pieces[7]=='E') || (pieces[5]=='E' && pieces[9]=='E'))
            gameOver('L');
          break;
        case 2: 
          if ((pieces[1]=='E' && pieces[3]=='E') || (pieces[5]=='E' && pieces[8]=='E'))
            gameOver('L');
          break;
        case 3: 
          if ((pieces[1]=='E' && pieces[2]=='E') || (pieces[6]=='E' && pieces[9]=='E') || (pieces[5]=='E' && pieces[7]=='E'))
            gameOver('L');
          break;
        case 4: 
          if ((pieces[5]=='E' && pieces[6]=='E') || (pieces[1]=='E' && pieces[7]=='E'))
            gameOver('L');
          break;
        case 5: 
          if ((pieces[4]=='E' && pieces[6]=='E') || (pieces[2]=='E' && pieces[8]=='E') || (pieces[1]=='E' && pieces[9]=='E') || (pieces[3]=='E' && pieces[7]=='E'))
            gameOver('L');
          break;
        case 6: 
          if ((pieces[4]=='E' && pieces[5]=='E') || (pieces[3]=='E' && pieces[9]=='E'))
            gameOver('L');
          break;
        case 7: 
          if ((pieces[8]=='E' && pieces[9]=='E') || (pieces[1]=='E' && pieces[4]=='E') || (pieces[3]=='E' && pieces[5]=='E'))
            gameOver('L');
          break;
        case 8: 
          if ((pieces[7]=='E' && pieces[9]=='E') || (pieces[2]=='E' && pieces[5]=='E'))
            gameOver('L');
          break;
        case 9: 
          if ((pieces[7]=='E' && pieces[8]=='E') || (pieces[3]=='E' && pieces[6]=='E') || (pieces[1]=='E' && pieces[5]=='E'))
            gameOver('L');
          break;
        }
        playerTurn = !playerTurn;
      }
    }
  }
}

void playerMove() {
  if (turn != 0 && !pauseBool) {
    selectedBlock();
    highlightBlock();
    if (mousePressed && playerTurn && selectedBlock() != 0 && pieces[selectedBlock()] == '.') {
      sfx[3].play();
      turn--;
      playerPiecePlaced++;
      pieces[selectedBlock()] = 'F';

      switch (selectedBlock()) {
      case 1: 
        if ((pieces[2]=='F' && pieces[3]=='F') || (pieces[4]=='F' && pieces[7]=='F') || (pieces[5]=='F' && pieces[9]=='F'))
          gameOver('W');
        break;
      case 2: 
        if ((pieces[1]=='F' && pieces[3]=='F') || (pieces[5]=='F' && pieces[8]=='F'))
          gameOver('W');
        break;
      case 3: 
        if ((pieces[1]=='F' && pieces[2]=='F') || (pieces[6]=='F' && pieces[9]=='F') || (pieces[5]=='F' && pieces[7]=='F'))
          gameOver('W');
        break;
      case 4: 
        if ((pieces[5]=='F' && pieces[6]=='F') || (pieces[1]=='F' && pieces[7]=='F'))
          gameOver('W');
        break;
      case 5: 
        if ((pieces[4]=='F' && pieces[6]=='F') || (pieces[2]=='F' && pieces[8]=='F') || (pieces[1]=='F' && pieces[9]=='F') || (pieces[3]=='F' && pieces[7]=='F'))
          gameOver('W');
        break;
      case 6: 
        if ((pieces[4]=='F' && pieces[5]=='F') || (pieces[3]=='F' && pieces[9]=='F'))
          gameOver('W');
        break;
      case 7: 
        if ((pieces[8]=='F' && pieces[9]=='F') || (pieces[1]=='F' && pieces[4]=='F') || (pieces[3]=='F' && pieces[5]=='F'))
          gameOver('W');
        break;
      case 8: 
        if ((pieces[7]=='F' && pieces[9]=='F') || (pieces[2]=='F' && pieces[5]=='F'))
          gameOver('W');
        break;
      case 9: 
        if ((pieces[7]=='F' && pieces[8]=='F') || (pieces[3]=='F' && pieces[6]=='F') || (pieces[1]=='F' && pieces[5]=='F'))
          gameOver('W');
        break;
      }
      savedTime = currentTime;
      playerTurn = !playerTurn;
    }
  }
}

void highlightBlock() {
  if (playerTurn && selectedBlock() != 0 && pieces[selectedBlock()] == '.') {
    fill(55);
    rectMode(CENTER);
    rect(squares[selectedBlock()].x, squares[selectedBlock()].y, 200, 200);
    rectMode(CORNER);
  }
}

int selectedBlock() {
  int blockNumber = 0;
  if (mouseX > 0 && mouseX < 200 && mouseY > 0 && mouseY < 200)
    blockNumber = 1;
  else if (mouseX > 200 && mouseX < 400 && mouseY > 0 && mouseY < 200)
    blockNumber = 2;
  else if (mouseX > 400 && mouseX < 600 && mouseY > 0 && mouseY < 200)
    blockNumber = 3;
  else if (mouseX > 0 && mouseX < 200 && mouseY > 200 && mouseY < 400)
    blockNumber = 4;
  else if (mouseX > 200 && mouseX < 400 && mouseY > 200 && mouseY < 400)
    blockNumber = 5;
  else if (mouseX > 400 && mouseX < 600 && mouseY > 200 && mouseY < 400)
    blockNumber = 6;
  else if (mouseX > 0 && mouseX < 200 && mouseY > 400 && mouseY < 600)
    blockNumber = 7;
  else if (mouseX > 200 && mouseX < 400 && mouseY > 400 && mouseY < 600)
    blockNumber = 8;
  else if (mouseX > 400 && mouseX < 600 && mouseY > 400 && mouseY < 600)
    blockNumber = 9;
  return blockNumber;
}

void drawCursor() {
  if (playerStarts && turn != 0 && playerTurn && !pauseBool)
    cursor(o, 31, 31);
  else if (!playerStarts && turn != 0 && playerTurn && !pauseBool)
    cursor(x, 31, 31);
  else
    cursor(ARROW);
}

boolean rngPlayerAssignment() {
  float rng = random(10);
  if (int(rng) % 2 == 0)
    return true;
  else
    return false;
}
