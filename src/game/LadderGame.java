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
        int numProsRadiant = 0;
        int numProsDire = 0;
        if(s == "pro"){

            numProsRadiant = 5;
            numProsDire = 5;
        }
        else if(s != "fa"){
            numProsRadiant = World.getRandomNumber(1, 5);
            numProsDire = World.getRandomNumber(1, 5);
        }
        for(int i = 0; i < numProsRadiant; i++){
            Player tmp = matchmakingPool.get(seed);
            while(matchmakingPoolNoTeam.contains(tmp)){
                seed = World.getRandomNumber(0,matchmakingPool.size()-10);
                tmp = matchmakingPool.get(seed);
            }
            radiant.add(matchmakingPool.remove(seed));
        }
        for(int i = 0; i < numProsDire; i++) {
            Player tmp = matchmakingPool.get(seed);
            while(matchmakingPoolNoTeam.contains(tmp)){
                seed = World.getRandomNumber(0,matchmakingPool.size()-10);
                tmp = matchmakingPool.get(seed);
            }
            dire.add(matchmakingPool.remove(seed));
        }
        Team radiantTeam = World.RADIANT;
        Team direTeam = World.DIRE;
        seed = World.getRandomNumber(0,matchmakingPoolNoTeam.size()-10);
        while(dire.size() < 5){
            dire.add(matchmakingPoolNoTeam.remove(seed));
        }
        while(radiant.size() < 5){
            radiant.add(matchmakingPoolNoTeam.remove(seed));
        }

        radiantTeam.setRoster(radiant);
        direTeam.setRoster(dire);
        Game nextGame = new Game(radiantTeam,direTeam);
        nextGame.playGame(radiantTeam,direTeam,s);
    }

    public void playGame(String s,int x){
        World.matchmakingPool.sort(Comparator.comparing(Player::getMmr,Comparator.reverseOrder()));
        int seed = World.getRandomNumber(0,World.matchmakingPool.size()-10);
        Team radiantTeam = World.RADIANT;
        Team direTeam = World.DIRE;
        for(int i = 0; i < 5; i++){
            Player tmp = World.matchmakingPool.remove(seed);
            tmp.incrementPubGamesY();
            radiant.add(tmp);
            tmp = World.matchmakingPool.remove(seed);
            tmp.incrementPubGamesY();
            dire.add(tmp);
        }
        radiantTeam.setRoster(radiant);
        direTeam.setRoster(dire);
        Game nextGame = new Game(radiantTeam,direTeam);
        nextGame.playGame(radiantTeam,direTeam,s);
    }

}
