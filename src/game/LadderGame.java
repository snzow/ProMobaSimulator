package game;

import world.Player;
import world.Team;
import world.World;

import java.util.ArrayList;
import java.util.Comparator;

public class LadderGame {
    ArrayList<Player> radiant;
    ArrayList<Player> dire;

    public LadderGame(){

        radiant = new ArrayList<>();
        dire = new ArrayList<>();

    }

    public void playGame(String s){
        ArrayList<Player> matchmakingPool = World.getPlayerList();
        ArrayList<Player> matchmakingPoolNoTeam = World.getFreeAgents();
        matchmakingPool.sort(Comparator.comparing(Player::getMmr,Comparator.reverseOrder()));
        int seed = World.getRandomNumber(0,matchmakingPool.size()-10);
        int numProsRadiant = World.getRandomNumber(1,5);
        int numProsDire = World.getRandomNumber(1,5);
        for(int i = 0; i < numProsRadiant; i++){
            radiant.add(matchmakingPool.remove(seed));
        }
        for(int i = 0; i < numProsDire; i++) {
            dire.add(matchmakingPool.remove(seed));
        }
        Team radiantTeam = World.RADIANT;
        Team direTeam = World.DIRE;
        while(dire.size() < 5){
            seed = World.getRandomNumber(0,matchmakingPoolNoTeam.size()-1);
            dire.add(matchmakingPoolNoTeam.remove(seed));
        }
        while(radiant.size() < 5){
            seed = World.getRandomNumber(0,matchmakingPoolNoTeam.size()-1);
            radiant.add(matchmakingPoolNoTeam.remove(seed));
        }

        radiantTeam.setRoster(radiant);
        direTeam.setRoster(dire);
        Game nextGame = new Game(radiantTeam,direTeam);
        nextGame.playGame(radiantTeam,direTeam,s);
    }


}
