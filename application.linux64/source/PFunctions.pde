void mouseReleased() {
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

void keyPressed() {
  if (key == ESC && !mainMenuBool) {
    key = 0;
    sfx[0].play();
  }
}
void keyTyped() {
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
    } else if (textWidth(textInput) < 178.0625) {
      charInput = key;
      textInput += str(charInput);
    }
  } else if (transition == "T0" && leaderBoardBool) {
    sfx[0].play();
    page = "MainMenu";
    transition = "T1";
  }
}
