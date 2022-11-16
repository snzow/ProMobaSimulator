package game;

import world.World;

import java.util.Random;

public class Hero {

    String name;
    double strength;
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
        strength = World.getRandomNumber(40,60);
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
    public void patchHero(int kindOfPatch){
        System.out.println("--- " +  name + " ---");
        Random rand = new Random();
        String patchType;
        double statMod = (double)rand.nextInt(5);
        if(kindOfPatch == 1){
            patchType = "buff";
        }
        else{
            patchType = "nerf";
        }
        if (statMod < 3){
            System.out.println("receives a minor " + patchType);
        }
        else{
            System.out.println("receives a major " + patchType);
        }
        if (kindOfPatch == 1){
            strength += statMod;
        }
        else{
            strength -= statMod;
        }
        picksP = 0;
        winsP = 0;
        lossesP = 0;
        bansP = 0;
    }



}
