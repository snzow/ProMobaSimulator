package game;

import world.World;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import static java.lang.Math.random;
import static java.lang.Math.round;

public class Hero {

    String name;
    double dmg;

    double hp;

    int picksThisPatch;
    int winsThisPatch;
    int lossesThisPatch;
    int wins;
    int losses;
    int picks;
    int bans;
    int bansThisPatch;

    double previousPatchStrength;

    public Hero(String n){
        name = n;
        dmg = World.getRandomNumber(80,120);
        hp = World.getRandomNumber(800,1200);
        picksThisPatch = 0;
        winsThisPatch = 0;
        lossesThisPatch = 0;
        wins = 0;
        losses = 0;
        picks = 0;
        bans = 0;
        bansThisPatch = 0;
        previousPatchStrength = dmg*10 + hp;
    }

    public void pickHero(){
        picksThisPatch++;
        picks++;
    }
    public void incrementWins(){
        winsThisPatch++;
        wins++;
    }

    public void incrementLosses(){
        lossesThisPatch++;
        losses++;
    }
    public void incrementBans(){
        bans++;
        bansThisPatch++;
    }

    public int getWins(){
        return winsThisPatch;
    }

    public int getLosses(){
        return lossesThisPatch;
    }

    public int getPicks(){
        return picksThisPatch;
    }

    public int getContested(){
        return picksThisPatch + bansThisPatch;
    }

    public double getWinrate(){
        if(winsThisPatch+lossesThisPatch == 0){
            return 0;
        }
        double toReturn = (double)winsThisPatch/((double)(winsThisPatch + lossesThisPatch));
        BigDecimal tmp = new BigDecimal(toReturn).setScale(2, RoundingMode.HALF_UP);
        toReturn = tmp.doubleValue();
        return toReturn;
    }

    public double getStrength(){
        return dmg*10 + hp;
    }

    public double getMetaStrength(){
        return dmg*10 + hp * getWinrate();
    }
    public void patchHero(double minStrength, double maxStrength){
        double medianStrength = (maxStrength + minStrength) / 2;
        previousPatchStrength = getStrength();
        double statMod = World.getRandomNumber(0,30);
        double randomSeed = (double)World.getRandomNumber(8,12)/10;
        if ( getMetaStrength() <= medianStrength - 100){
            if(statMod >= 15){
                dmg += 15*randomSeed;
            }
            else{
                hp += 150*randomSeed;
            }

        }
        else if(getMetaStrength() > medianStrength - 100 && getMetaStrength() < medianStrength + 100){
            int seed = World.getRandomNumber(0,2);
            if(seed == 0){
                if(statMod >= 15){
                    dmg -= 10*randomSeed;
                }
                else{
                    hp -= 100*randomSeed;
                }

            }
            if (seed == 2){
                if(statMod >= 15){
                    dmg += 10*randomSeed;
                }
                else{
                    hp += 100*randomSeed;
                }
            }
        }
        else{
            if(statMod >= 15){
                dmg -= 15*randomSeed;
            }
            else{
                hp -= 150*randomSeed;
            }
        }
        picksThisPatch = 0;
        winsThisPatch = 0;
        lossesThisPatch = 0;
        bansThisPatch = 0;
    }

    public String toStringAllStats(){
        return name + " " + picksThisPatch + "/" + bansThisPatch + " Picks/Bans | " + (int)(100*getWinrate()) + "% Winrate | " + (int)(dmg*10 + hp) + " SI";
    }

    public String toStringNoWinrate(){
        return name + " "  + (int)(dmg*10 + hp) + " SI | " + (int)(getStrength() - previousPatchStrength);
    }

}
