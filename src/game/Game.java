package game;

import world.Player;
import world.Team;
import world.World;

import java.util.ArrayList;
import java.util.Random;

public class Game {

    static ArrayList<Hero> heroes;
    Team teamOne;
    Team teamTwo;

    public Game(Team to, Team tt){
        teamOne = to;
        teamTwo = tt;
    }

    public Team playSeries(int bo){
        int t1w = 0;
        int t2w = 0;
        while(t1w < bo && t2w < bo){
            if(playGame().equals(teamOne)){
                t1w++;
            }
            else {
                t2w++;
            }
        }
        System.out.println(teamOne + " " + t2w + " - " + t1w + " " + teamTwo);
        if(t1w > t2w){
            return teamTwo;
        }
        else{
            return teamOne;
        }

    }
    public Team playGame(){
        ArrayList<InGamePlayer> radiant = new ArrayList<>();
        ArrayList<InGamePlayer> dire = new ArrayList<>();
        ArrayList tmpA = teamOne.getRoster();
        ArrayList tmpB = teamTwo.getRoster();
        for(int i = 0; i < 5; i++){
            InGamePlayer rad = new InGamePlayer((Player)tmpA.get(i));
            InGamePlayer dir = new InGamePlayer((Player)tmpB.get(i));
            radiant.add(rad);
            dire.add(dir);
        }
        int direKills = 0;
        int radiantKills = 0;
        int totalKills = 0;
        int radP = World.getRandomNumber(0,4);
        int dirP = World.getRandomNumber(0,4);
        while(true){
            if (totalKills > 50){
                if (direKills > radiantKills){
                    teamOne.updateRunningPerf(-1);
                    teamTwo.updateRunningPerf(1);
                    updatePerf(radiant,dire);
                    return teamOne;
                }
                else{
                    teamTwo.updateRunningPerf(-1);
                    teamOne.updateRunningPerf(1);
                    updatePerf(radiant,dire);
                    return teamTwo;
                }
            }
            else{
                double total = radiant.get(radP).getSkill() + dire.get(dirP).getSkill();
                int seed = World.getRandomNumber(0,(int)total);
                double mid = total / 2;
                if (seed >= mid + 100){
                    dire.get(dirP).kill();
                    dire.get(dirP).skill += 200;
                    radiant.get(radP).skill -= 200;
                    radiant.get(radP).die();
                    direKills++;
                    totalKills++;
                }
                else if (seed <= mid - 100){
                    dire.get(dirP).die();
                    radiant.get(radP).skill += 200;
                    dire.get(dirP).skill -= 200;
                    radiant.get(radP).kill();
                    radiantKills++;
                    totalKills++;
                }
                radP = World.getRandomNumber(0,4);
                dirP = World.getRandomNumber(0,4);
            }
        }

    }

    public void updatePerf(ArrayList<InGamePlayer> a, ArrayList<InGamePlayer> b){
        for(int i = 0; i < 5; i++){
            if(a.get(i).kills > a.get(i).deaths){
                a.get(i).player.updateNetPerf(1);
            }
            else if(a.get(i).kills < a.get(i).deaths){
                a.get(i).player.updateNetPerf(-1);
            }
            if(b.get(i).kills > b.get(i).deaths){
                b.get(i).player.updateNetPerf(1);
            }
            else if(b.get(i).kills < b.get(i).deaths){
                b.get(i).player.updateNetPerf(-1);
            }
        }





    }
    private class InGamePlayer{
        int kills;
        int deaths;
        Player player;
        double skill;

        public InGamePlayer(Player p){
            kills = 0;
            deaths = 0;
            player = p;
            double seed = World.getRandomNumber(8,12)/10.0;
            skill = p.getSkill()*seed;
        }

        public double getSkill(){
            return skill;
        }

        public void die(){
            deaths++;
        }

        public void kill(){
            kills++;
        }

        public String toString(){
            return (player.toString() + " | " + kills + "/" + deaths);
        }
    }

}
