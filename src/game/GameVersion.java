package game;

import world.World;

import java.util.HashMap;

public class GameVersion {
    int version10s;
    int version1s;

    public GameVersion(){
        version10s = 0;
        version1s = 0;
    }

    public void incrementGameVersion(){
        version1s += 4;
        if (version1s >= 10){
            version10s++;
            version1s = 0;
        }
    }

    public String toString(){
        return "V" + version10s + "." + version1s + World.getRandomNumber(0,9);
    }
}
