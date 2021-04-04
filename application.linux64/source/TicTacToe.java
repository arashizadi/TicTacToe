import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class TicTacToe extends PApplet {

//TicTacToe by A.I.
String ver = "1.0";


SoundFile[] music = new SoundFile[8], sfx = new SoundFile[9];
PImage o, x, loadingTop, loadingBottom;
int _height, turn = 6, currentTime = 0, savedTime = 0, enemyLastBlock = 0, playerPiecePlaced = 0, xGradient = 0, winCounter = 0, loseCounter = 0, drawCounter = 0, playerScore, yTransition1 = 0, yTransition2 = 0, yTransitionSpeed = 7, musicCounter = 0;
PVector[] squares = new PVector[10]; //Eligible coordinates for pieces to move into 
char[] pieces = new char[10]; // '.' = Empty, 'E' = Enemy, 'F' = Friendly
boolean playerStarts, playerTurn, gameOverBool, resetBool, mainMenuBool = true, playerNameTextInputBool, leaderBoardBool, showCaret, pauseBool, mainMenuMusic, soundOn = true, sfxLineAnimation;
char result = '.', charInput;
String status, transition = "T0", page = "", playerName, textInput = "", soundSetting;
int gameOverAccentColor = color(255);
float lineAnimation = 0;
PFont font;
XML savedData;
XML[] records;
boolean[] inGameMusicBools = new boolean[7]; //first index checks if we already randomize the rest of the bools after each level 

public void setup() {
  
  o = loadImage("O.png");
  x = loadImage("X.png");
  loadingTop = loadImage("loadingtop.png");
  loadingBottom = loadImage("loadingbottom.png");
  savedData = loadXML("./data/Save.xml");
  music[0] = new SoundFile(this, "./Audio/Music/MainMenu.wav");
  music[1] = new SoundFile(this, "./Audio/Music/Leaderboard.wav");
  for (int i = 2; i < music.length; i++)
    music[i] = new SoundFile(this, "./Audio/Music/BG" + (i - 1) + ".wav");

  for (int i = 0; i < music.length; i++) {
    music[i].loop();
    music[i].amp(0);
  }

  sfx[0] = new SoundFile(this, "./Audio/SFX/Button.wav");
  sfx[1] = new SoundFile(this, "./Audio/SFX/StartGame.wav");
  sfx[2] = new SoundFile(this, "./Audio/SFX/NewRecord.wav");
  sfx[3] = new SoundFile(this, "./Audio/SFX/PlayerMove.wav");
  sfx[4] = new SoundFile(this, "./Audio/SFX/EnemyMove.wav");
  sfx[5] = new SoundFile(this, "./Audio/SFX/LineAnimation.wav");
  sfx[6] = new SoundFile(this, "./Audio/SFX/Win.wav");
  sfx[7] = new SoundFile(this, "./Audio/SFX/Draw.wav");
  sfx[8] = new SoundFile(this, "./Audio/SFX/Lose.wav");

  for (int i = 0; i < sfx.length; i++) {
    sfx[i].play();
    sfx[i].amp(0);
    sfx[i].stop();
    sfx[i].amp(1);
  }
  sfx[5].amp(0.3f);
  //lpf = new LowPass(this);
  
  font = createFont("BAUHS93.TTF", 64);
  imageMode(CENTER);
  strokeCap(ROUND);
  textFont(font);
  _height = height - 50;

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
}

public void draw() {
  if (mainMenuBool)
    mainMenu();
  else if (playerNameTextInputBool || leaderBoardBool)
    leaderBoard();
  else {
    drawBoard();
    statusBar();
    enemyMove();
    playerMove();
    drawPieces();
    drawCursor();
    if (turn == 0)
      drawVictoryLine(lineAnimationPath());
    else if (pauseBool)
      pause();
  }
  loading();
  music();
}
public void drawBoard() {
  strokeWeight(1);
  rectMode(CORNER);
  stroke(200);
  fill(40);
  for (int i = 0; i < width; i+=200)
    for (int j = 0; j < _height; j+=200)
      rect(i, j, i+200, j+200);
}

public void drawPieces() {
  for (int i = 0; i < pieces.length; i++)
    if (pieces[i] == 'E')
      if (playerStarts)
        image(x, squares[i].x, squares[i].y);
      else
        image(o, squares[i].x, squares[i].y);
    else if (pieces[i] == 'F')
      if (playerStarts)
        image(o, squares[i].x, squares[i].y);
      else
        image(x, squares[i].x, squares[i].y);
}

public String lineAnimationPath() {
  if (pieces[1]=='F' && pieces[2]=='F' && pieces[3]=='F' || pieces[1]=='E' && pieces[2]=='E' && pieces[3]=='E')
    return "13";
  else if (pieces[4]=='F' && pieces[5]=='F' && pieces[6]=='F' || pieces[4]=='E' && pieces[5]=='E' && pieces[6]=='E')
    return "46";
  else if (pieces[7]=='F' && pieces[8]=='F' && pieces[9]=='F' || pieces[7]=='E' && pieces[8]=='E' && pieces[9]=='E')
    return "79";
  else if (pieces[1]=='F' && pieces[4]=='F' && pieces[7]=='F' || pieces[1]=='E' && pieces[4]=='E' && pieces[7]=='E')
    return "17";
  else if (pieces[2]=='F' && pieces[5]=='F' && pieces[8]=='F' || pieces[2]=='E' && pieces[5]=='E' && pieces[8]=='E')
    return "28";
  else if (pieces[3]=='F' && pieces[6]=='F' && pieces[9]=='F' || pieces[3]=='E' && pieces[6]=='E' && pieces[9]=='E')
    return "39";
  else if (pieces[1]=='F' && pieces[5]=='F' && pieces[9]=='F' || pieces[1]=='E' && pieces[5]=='E' && pieces[9]=='E')
    return "19";
  else if (pieces[3]=='F' && pieces[5]=='F' && pieces[7]=='F' || pieces[3]=='E' && pieces[5]=='E' && pieces[7]=='E')
    return "73";
  else
    return "";
}

public boolean drawVictoryLine(String a) {
  stroke(0 + (lineAnimation / 2));
  strokeWeight(0 + (lineAnimation / 10));
  switch(a) {
    case("13"): if (!sfxLineAnimation) sfx[5].play(); sfxLineAnimation = true; line(100, 100, 100 + lineAnimation, 100); break;
    case("46"): if (!sfxLineAnimation) sfx[5].play(); sfxLineAnimation = true; line(100, 300, 100 + lineAnimation, 300); break;
    case("79"): if (!sfxLineAnimation) sfx[5].play(); sfxLineAnimation = true; line(100, 500, 100 + lineAnimation, 500); break;
    case("17"): if (!sfxLineAnimation) sfx[5].play(); sfxLineAnimation = true; line(100, 100, 100, 100 + lineAnimation); break;
    case("28"): if (!sfxLineAnimation) sfx[5].play(); sfxLineAnimation = true; line(300, 100, 300, 100 + lineAnimation); break;
    case("39"): if (!sfxLineAnimation) sfx[5].play(); sfxLineAnimation = true; line(500, 100, 500, 100 + lineAnimation); break;
    case("19"): if (!sfxLineAnimation) sfx[5].play(); sfxLineAnimation = true; line(100, 100, 100 + lineAnimation, 100 + lineAnimation); break;
    case("73"): if (!sfxLineAnimation) sfx[5].play(); sfxLineAnimation = true; line(100, 500, 100 + lineAnimation, 500 - lineAnimation); break;
    case(""): lineAnimation = 401; break;
  }
  if (lineAnimation > 400) {
    strokeWeight(1);
    gameOver(result);
    return true;
  } else {
    lineAnimation += 4;
    return false;
  }
}

//SOURCE: https://processing.org/examples/lineargradient.html
public void setGradient(int x, int y, float w, float h, int c1, int c2) {
  noFill();
  for (int i = x; i <= x+w; i++) {
    float inter = map(i, x, x+w, 0, 1);
    int c = lerpColor(c1, c2, inter);
    stroke(c);
    line(i, y, i, y+h);
    stroke(255);
  }
}
public void leaderBoard() {
  currentTime = millis();
  musicCounter = 1;
  textAlign(CORNER);
  setGradient(xGradient, 0, width, height, color(102, 102, 153), color(153, 51, 77));
  setGradient(xGradient + width, 0, width, height, color(153, 51, 77), color(102, 102, 153));
  setGradient(xGradient + width * 2, 0, width, height, color(102, 102, 153), color(153, 51, 77));
  xGradient--;
  if (xGradient <= width * -2)
    xGradient = 0;
  
  if (playerNameTextInputBool && !leaderBoardBool)
  {
    fill(255);
    textSize(18);
    text("Type your name and press ENTER to Continue", 120, 280);

    translate(width/2, height/2);
    textSize(14);
    rect(0 - 100, 0 - 20, 200, 40);
    fill(0);
    textSize(16);
    text(textInput, -90, 5);
    
    if (currentTime - savedTime > 500) {
      showCaret = !showCaret;
      savedTime = currentTime;
    }
    if (showCaret) {
      stroke(0);
      line (-90 + textWidth(textInput), -10, -90 + textWidth(textInput), 10);
    }
    
    translate(-width/2, -height/2);
  } else if (leaderBoardBool) {
    fill(255);
    textAlign(CENTER);
    textSize(24);
    text("LEADERBOARD", width/2, 100);
    textSize(14);
    text("Press any key to Main Menu", width/2, 120);
    textSize(16);
    if (xGradient <= width * -2)
      xGradient = 0;
    
    savedData = loadXML("./data/Save.xml");
    records = savedData.getChildren("Record");
    String[] orderedScores = new String[records.length];
    String[] originalScores = new String[records.length];
    String[] names = new String[records.length];
    int[] orderIndexScores = new int[records.length];

    for (int i = 0; i < records.length; i++) {
      originalScores[i] = records[i].getChild("Score").getContent();
      orderedScores[i] = records[i].getChild("Score").getContent();
      names[i] = records[i].getChild("Name").getContent();
    }
    orderedScores = reverse(orderedScores);
    
    for (int i = 0; i < orderedScores.length; i++)
    {
      for (int j = 0; j < originalScores.length; j++)
      {
        if (originalScores[j] == orderedScores[i])
          orderIndexScores[i] = j;
      }
    }

    for (int i = 0; i < orderedScores.length; i++) {
      fill(255);
      textAlign(LEFT);
      text(names[orderIndexScores[i]], 180, (height/4) + 50 + i * 20);
      textAlign(RIGHT);
      text(orderedScores[i], 180  + (width/2 - 60), (height/4) + 50 + i * 20);
    }
  }
}

//SOURCE: https://www.javatpoint.com/how-to-remove-last-character-from-string-in-java#:~:text=%20There%20are%20four%20ways%20to%20remove%20the,%28%29%20Method%204%20Using%20Regular%20Expression%20More%20
public String removeLastCharacter(String str)   
{  
  return (str == null) ? null : str.replaceAll(".$", "");
} 
public void mainMenu() {
  musicCounter = 0;
  soundSetting = savedData.getChild("SoundSetting").getContent();
  setGradient(xGradient, 0, width, height, color(102, 102, 153), color(153, 51, 77));
  setGradient(xGradient + width, 0, width, height, color(153, 51, 77), color(102, 102, 153));
  setGradient(xGradient + width * 2, 0, width, height, color(102, 102, 153), color(153, 51, 77));
  xGradient--;
  if (xGradient <= width * -2)
    xGradient = 0;
  textAlign(CENTER);
  textSize(64);
  fill(255);
  text("Tic Tac Toe", width/2, height/2 - ((height/2)/2));
  textSize(16);
  text("Arash Izadi's", width/2, height/4 - ((height/3)/3));
  textSize(8);
  text("Ver. " + ver, width/2 + 150, height/2 - ((height/2)/2.5f) - 15);
  translate(width/2, height/2);
  noFill();
  rect(-100, -100, 200, 60);
  rect(-100, 0, 200, 60);
  rect(-100, 100, 200, 60);
  rect(-100, 200, 200, 60);

  //Start Game button mouseover
  if (mouseX - (width/2) >= -100 && mouseX - (width/2) <= 100 && mouseY - (height/2) >= -100 && mouseY - (height/2) <= -40) {
    fill(255, 100);
    rect(-100, -100, 200, 60);
    fill(255);
  } 
  //Leaderboard button mouseover
  else if (mouseX - (width/2) >= -100 && mouseX - (width/2) <= 100 && mouseY - (height/2) >= 0 && mouseY - (height/2) <= 60) {
    fill(255, 100);
    rect(-100, 0, 200, 60);
    fill(255);
  }  //Option button mouseover
  else if (mouseX - (width/2) >= -100 && mouseX - (width/2) <= 100 && mouseY - (height/2) >= 100 && mouseY - (height/2) <= 160) {
    fill(255, 100);
    rect(-100, 100, 200, 60);
    fill(255);
  }  //Exit button mouseover
  else if (mouseX - (width/2) >= -100 && mouseX - (width/2) <= 100 && mouseY - (height/2) >= 200 && mouseY - (height/2) <= 260) {
    fill(255, 100);
    rect(-100, 200, 200, 60);
    fill(255);
  }

  textSize(24);
  text("Start Game", 0, -62.5f);
  text("Leaderboard", 0, 37.5f);
  text("Sound: " + soundSetting, 0, 137.5f);
  text("Exit", 0, 237.5f);
  translate(-width/2, -height/2);
}
public void enemyMove() {
  currentTime = millis();
  if (currentTime - savedTime > random(900, 1700))
  {
    if (turn != 0 && !pauseBool) {
      savedTime = currentTime;
      int block = PApplet.parseInt(random(1, squares.length));
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

public void playerMove() {
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

public void highlightBlock() {
  if (playerTurn && selectedBlock() != 0 && pieces[selectedBlock()] == '.') {
    fill(55);
    rectMode(CENTER);
    rect(squares[selectedBlock()].x, squares[selectedBlock()].y, 200, 200);
    rectMode(CORNER);
  }
}

public int selectedBlock() {
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

public void drawCursor() {
  if (playerStarts && turn != 0 && playerTurn && !pauseBool)
    cursor(o, 31, 31);
  else if (!playerStarts && turn != 0 && playerTurn && !pauseBool)
    cursor(x, 31, 31);
  else
    cursor(ARROW);
}

public boolean rngPlayerAssignment() {
  float rng = random(10);
  if (PApplet.parseInt(rng) % 2 == 0)
    return true;
  else
    return false;
}
public void music() {
  soundSetting = savedData.getChild("SoundSetting").getContent();
  if (soundSetting.length() == 3) {
    for (int i = 0; i < music.length; i++)
      music[i].amp(0);
          for (int i = 0; i < sfx.length; i++)
      sfx[i].amp(0);
    }
  else {
      for (int i = 0; i < sfx.length; i++)
      sfx[i].amp(1);
    switch(musicCounter) {
    case 0:
      music[0].amp(1);
      for (int i = 1; i < music.length; i++)
        music[i].amp(0);
      if (!mainMenuMusic) {
        music[0].loop();
        music[1].loop();
        mainMenuMusic = true;
      }
      break;

    case 1:
      music[0].amp(0.45f);
      music[1].amp(1);
      for (int i = 2; i < music.length; i++)
        music[i].amp(0);
      if (!mainMenuMusic) {
        music[0].loop();
        music[1].loop();
        mainMenuMusic = true;
      }
      break;
    case 2:
      if (mainMenuMusic) {
        for (int i = 0; i < music.length; i++) {
          music[i].amp(0);
          music[i].loop();
        }
        mainMenuMusic = false;
      }
      music[2].amp(1);
      break;

    case 3:
    case 4:
    case 5:
    case 6:
    case 7:
      music[musicCounter].amp(1);
      break;
    }

    if (musicCounter > 7) {
      if (musicCounter % 6 < 2)
        musicCounter++;

      if (inGameMusicBools[0]) {
        for (int i = 1; i < inGameMusicBools.length; i++) {
          float rng = random(10);
          if (PApplet.parseInt(rng) % 2 == 0)
            inGameMusicBools[i] = true;
          else
            inGameMusicBools[i] = false;
        }
        inGameMusicBools[0] = false;

        for (int i = 1; i < inGameMusicBools.length; i++)
          if (inGameMusicBools[i] == true)
            music[i+1].amp(1);
          else
            music[i+1].amp(0);
      }
    }
  }
}
public void mouseReleased() {
  if (leaderBoardBool) {
    transition = "T1";
    page = "MainMenu";
    sfx[0].play();
  } else if (mainMenuBool && transition == "T0") {
    translate(width/2, height/2);
    //Start Game click event
    if (mouseX - (width/2) >= -100 && mouseX - (width/2) <= 100 && mouseY - (height/2) >= -100 && mouseY - (height/2) <= -40) {
      sfx[1].play();
      transition = "T1";
      page = "Game";
    }  //Leaderboard click event
    else if (mouseX - (width/2) >= -100 && mouseX - (width/2) <= 100 && mouseY - (height/2) >= 0 && mouseY - (height/2) <= 60) {
      sfx[0].play();
      transition = "T1";
      page = "LeaderBoard";
    }  //Option click event
    else if (mouseX - (width/2) >= -100 && mouseX - (width/2) <= 100 && mouseY - (height/2) >= 100 && mouseY - (height/2) <= 160) {
      if (soundSetting.length() == 2)
        savedData.getChild("SoundSetting").setContent("OFF");
      else {
        savedData.getChild("SoundSetting").setContent("ON");
        sfx[0].play();
      }
      saveXML(savedData, "./data/Save.xml");
    }  //Exit click event
    else if (mouseX - (width/2) >= -100 && mouseX - (width/2) <= 100 && mouseY - (height/2) >= 200 && mouseY - (height/2) <= 260) {
      sfx[0].play();
      transition = "T1";
      page = "Exit";
    }
    translate(-width/2, -height/2);
  } else if (gameOverBool && transition == "T0" && !playerNameTextInputBool) {
    translate(width/2, height/2);
    //Continue event click
    if (turn == 0 && mouseX - (width/2) >= 50 - (width/2) && mouseX - (width/2) <= (50 - (width/2) + 200) && mouseY - (height/2) >= 32 && mouseY - (height/2) <= 32 + 60) {    
      sfx[0].play();
      resetBool = true;
    }
    //Main Menu click event
    else if (mouseX - (width/2) >= 50 && mouseX - (width/2) <= (50 + 200) && mouseY - (height/2) >= 32 && mouseY - (height/2) <= 32 + 60) {
      sfx[0].play();
      transition = "T1";
      page = "ScoreBoard";
    }
    translate(-width/2, -height/2);
  } else if (pauseBool && transition == "T0" && !playerNameTextInputBool) {
    translate(width/2, height/2);
    //Resume event click
    if (mouseX - (width/2) >= 50 - (width/2) && mouseX - (width/2) <= (50 - (width/2) + 200) && mouseY - (height/2) >= 32 && mouseY - (height/2) <= 32 + 60) {    
      sfx[0].play();
      pauseBool = false;
    }
    //Main Menu click event
    else if (mouseX - (width/2) >= 50 && mouseX - (width/2) <= (50 + 200) && mouseY - (height/2) >= 32 && mouseY - (height/2) <= 32 + 60) {
      sfx[0].play();
      transition = "T1";
      page = "ScoreBoard";
    }
    translate(-width/2, -height/2);
  }
}

public void keyPressed() {
  if (key == ESC && !mainMenuBool) {
    key = 0;
    sfx[0].play();
  }
}
public void keyTyped() {
  if (key == ESC) {
    if (!playerNameTextInputBool && !leaderBoardBool && transition == "T0") {
      if (!pauseBool)
        pauseBool = true;
      else if (pauseBool)
        pauseBool = false; 
      }
      else if (playerNameTextInputBool && !leaderBoardBool && transition == "T0") {
        leaderBoardBool = true;
        playerNameTextInputBool = false;
      } else if (transition == "T0" && leaderBoardBool) {
        page = "MainMenu";
        transition = "T1";
      }
  } else if (playerNameTextInputBool && !leaderBoardBool && transition == "T0") {
    if (key == BACKSPACE) {
      textInput = removeLastCharacter(textInput);
    } else if (key == ENTER) {
      sfx[2].play();
      playerName = textInput;
      XML newRecord = savedData.addChild("Record");
      XML newName = newRecord.addChild("Name");
      XML newScore = newRecord.addChild("Score");
      newName.setContent(textInput);
      newScore.setContent(nf(playerScore, 4));
      saveXML(savedData, "./data/Save.xml");
      leaderBoardBool = true;
      playerNameTextInputBool = false;
    } else if (textWidth(textInput) < 178.0625f) {
      charInput = key;
      textInput += str(charInput);
    }
  } else if (transition == "T0" && leaderBoardBool) {
    sfx[0].play();
    page = "MainMenu";
    transition = "T1";
  }
}
public void statusBar() {
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
        image(o, width - i, height - 23.5f, 20, 20);
        tint(255, 255);
      } else
        image(o, width - i, height - 23.5f, 20, 20);

    else
      if (playerTurn && (75 - (playerPiecePlaced * 25) - i) == 0) { //while piece is in hand, apply transparency
        tint(255, 100);
        image(x, width - i, height - 23.5f, 20, 20);
        tint(255, 255);
      } else
        image(x, width - i, height - 23.5f, 20, 20);
  }
}

public void pause() {
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

public void gameOver(char a) {
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
public void nextLevel() {
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

public void loading() {
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
  public void settings() {  size(600, 650); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "TicTacToe" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
