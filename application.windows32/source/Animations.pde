void drawBoard() {
  strokeWeight(1);
  rectMode(CORNER);
  stroke(200);
  fill(40);
  for (int i = 0; i < width; i+=200)
    for (int j = 0; j < _height; j+=200)
      rect(i, j, i+200, j+200);
}

void drawPieces() {
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

String lineAnimationPath() {
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

boolean drawVictoryLine(String a) {
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
void setGradient(int x, int y, float w, float h, color c1, color c2) {
  noFill();
  for (int i = x; i <= x+w; i++) {
    float inter = map(i, x, x+w, 0, 1);
    color c = lerpColor(c1, c2, inter);
    stroke(c);
    line(i, y, i, y+h);
    stroke(255);
  }
}
