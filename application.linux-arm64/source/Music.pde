void music() {
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
      music[0].amp(0.45);
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
          if (int(rng) % 2 == 0)
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
