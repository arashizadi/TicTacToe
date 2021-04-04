//TicTacToe by A.I.
String ver = "1.0";

import processing.sound.*;
SoundFile[] music = new SoundFile[8], sfx = new SoundFile[9];
PImage o, x, loadingTop, loadingBottom;
int _height, turn = 6, currentTime = 0, savedTime = 0, enemyLastBlock = 0, playerPiecePlaced = 0, xGradient = 0, winCounter = 0, loseCounter = 0, drawCounter = 0, playerScore, yTransition1 = 0, yTransition2 = 0, yTransitionSpeed = 7, musicCounter = 0;
PVector[] squares = new PVector[10]; //Eligible coordinates for pieces to move into 
char[] pieces = new char[10]; // '.' = Empty, 'E' = Enemy, 'F' = Friendly
boolean playerStarts, playerTurn, gameOverBool, resetBool, mainMenuBool = true, playerNameTextInputBool, leaderBoardBool, showCaret, pauseBool, mainMenuMusic, soundOn = true, sfxLineAnimation;
char result = '.', charInput;
String status, transition = "T0", page = "", playerName, textInput = "", soundSetting;
color gameOverAccentColor = color(255);
float lineAnimation = 0;
PFont font;
XML savedData;
XML[] records;
boolean[] inGameMusicBools = new boolean[7]; //first index checks if we already randomize the rest of the bools after each level 

void setup() {
  size(600, 650);
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
  sfx[5].amp(0.3);
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

void draw() {
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
