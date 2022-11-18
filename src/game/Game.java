package game;

import world.Player;
import world.Team;
import world.World;

import java.util.ArrayList;

public class Game {

    static ArrayList<Hero> heroes;
    Team radiant;
    Team dire;

    public Game(Team to, Team tt){
        radiant = to;
        dire = tt;
    }

    public Team playSeries(int bo){
        int radiantWins = 0;
        int direWins = 0;
        while(radiantWins < bo && direWins < bo){
            if(playGame().equals(radiant)){
                radiantWins++;
            }
            else {
                direWins++;
            }
        }
        System.out.println(radiant + " " + radiantWins + " - " + direWins + " " + dire);
        if(radiantWins > direWins){
            return radiant;
        }
        else{
            return dire;
        }

    }
    public Team playGame(){
        ArrayList<InGamePlayer> radiantInGame = new ArrayList<>();
        ArrayList<InGamePlayer> direInGame = new ArrayList<>();
        ArrayList<Player> tmpA = radiant.getRoster();
        ArrayList<Player> tmpB = dire.getRoster();
        for(int i = 0; i < 5; i++){
            InGamePlayer rad = new InGamePlayer(tmpA.get(i));
            InGamePlayer dir = new InGamePlayer(tmpB.get(i));
            radiantInGame.add(rad);
            direInGame.add(dir);
        }
        int direInGameKills = 0;
        int radiantKills = 0;
        int totalKills = 0;
        int radP = World.getRandomNumber(0,4);
        int dirP = World.getRandomNumber(0,4);
        while(true){
            if (totalKills > 50){
                if (direInGameKills > radiantKills){
                    radiant.updateRunningPerf(-1);
                    dire.updateRunningPerf(1);
                    updatePerf(radiantInGame,direInGame);
                    radiant.incrementLosses();
                    dire.incrementWins();
                    return dire;
                }
                else{
                    dire.updateRunningPerf(-1);
                    radiant.updateRunningPerf(1);
                    dire.incrementLosses();
                    radiant.incrementWins();
                    updatePerf(radiantInGame,direInGame);
                    return radiant;
                }
            }
            else{
                double total = radiantInGame.get(radP).getSkill() + direInGame.get(dirP).getSkill();
                int seed = World.getRandomNumber(0,(int)total);
                double mid = total / 2;
                if (seed >= mid + 100){
                    direInGame.get(dirP).kill();
                    direInGame.get(dirP).skill += 200;
                    radiantInGame.get(radP).skill -= 200;
                    radiantInGame.get(radP).die();
                    direInGameKills++;
                    totalKills++;
                }
                else if (seed <= mid - 100){
                    direInGame.get(dirP).die();
                    radiantInGame.get(radP).skill += 200;
                    direInGame.get(dirP).skill -= 200;
                    radiantInGame.get(radP).kill();
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
