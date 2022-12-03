package game;

import world.World;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import static java.lang.Math.round;

public class Hero {

    String name;
    double dmg;

    double hp;

    int picksP;
    int winsP;
    int lossesP;
    int wins;
    int losses;
    int picks;
    int bans;
    int bansP;

    public Hero(String n){
        name = n;
        dmg = World.getRandomNumber(80,120);
        hp = World.getRandomNumber(800,1200);
        int picksP = 0;
        int winsP = 0;
        int lossesP = 0;
        int wins = 0;
        int losses = 0;
        int picks = 0;
        int bans = 0;
        int bansP = 0;
    }

    public void pickHero(){
        picksP++;
        picks++;
    }
    public void incrementWins(){
        winsP++;
        wins++;
    }

    public void incrementLosses(){
        lossesP++;
        losses++;
    }
    public void incrementBans(){
        bans++;
        bansP++;
    }

    public int getWins(){
        return winsP;
    }

    public int getLosses(){
        return lossesP;
    }

    public int getPicks(){
        return picksP;
    }

    public int getContested(){
        return picksP + bansP;
    }

    public double getWinrate(){
        if(winsP+lossesP == 0){
            return 0;
        }
        double toReturn = (double)winsP/((double)(winsP + lossesP));
        BigDecimal tmp = new BigDecimal(toReturn).setScale(2, RoundingMode.HALF_UP);
        toReturn = tmp.doubleValue();
        return toReturn;
    }

    public double getStrength(){
        return dmg*10 + hp;
    }

    public double getMetaStrength(){
        return dmg*10 + hp + picksP*5;
    }
    public void patchHero(){
        double statMod = World.getRandomNumber(0,30);
        if (getWinrate() <= .47){
            if(statMod >= 15){
                dmg += 25;
            }
            else{
                hp += 250;
            }

        }
        else if(.47 < getWinrate() && getWinrate() < .53){
            int seed = World.getRandomNumber(0,2);
            if(seed == 0){
                if(statMod >= 15){
                    dmg -= 25;
                }
                else{
                    hp -= 250;
                }

            }
            if (seed == 2){
                if(statMod >= 15){
                    dmg += 25;
                }
                else{
                    hp += 250;
                }
            }
        }
        else{
            if(statMod >= 15){
                dmg -= 25;
            }
            else{
                hp -= 250;
            }
        }
        picksP = 0;
        winsP = 0;
        lossesP = 0;
        bansP = 0;
    }

    public String toString(){
        return name + " " + picksP + "/" + bansP + " Picks/Bans | " + getWinrate() + " Winrate | " + (int)(dmg*10 + hp) + " SI";
    }

}
