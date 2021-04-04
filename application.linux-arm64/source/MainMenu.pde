void mainMenu() {
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
  text("Ver. " + ver, width/2 + 150, height/2 - ((height/2)/2.5) - 15);
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
  text("Start Game", 0, -62.5);
  text("Leaderboard", 0, 37.5);
  text("Sound: " + soundSetting, 0, 137.5);
  text("Exit", 0, 237.5);
  translate(-width/2, -height/2);
}
