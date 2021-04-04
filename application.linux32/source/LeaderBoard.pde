void leaderBoard() {
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
String removeLastCharacter(String str)   
{  
  return (str == null) ? null : str.replaceAll(".$", "");
} 
