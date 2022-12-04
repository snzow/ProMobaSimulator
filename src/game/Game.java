package game;

import world.Player;
import world.Team;
import world.World;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Game {

    Team radiant;
    Team dire;

    ArrayList<Hero> pickPhase;

    public Game(Team to, Team tt) {
        radiant = to;
        dire = tt;
        pickPhase = (ArrayList<Hero>) World.heroList.clone();
    }

    public Team playSeries(int bo) {
        int radiantWins = 0;
        int direWins = 0;
        while (radiantWins < bo && direWins < bo) {
            pickPhase = (ArrayList<Hero>) World.heroList.clone();
            if (playGame().equals(radiant)) {
                radiantWins++;
            } else {
                direWins++;
            }
        }
        System.out.println(radiant + " " + radiantWins + " - " + direWins + " " + dire);
        if (radiantWins > direWins) {
            return radiant;
        } else {
            return dire;
        }

    }

    public Team playGame() {
        InGameTeam rad = new InGameTeam("Radiant",radiant);
        InGameTeam dir = new InGameTeam("Dire",dire);
        ArrayList<Player> radiantArrayList = radiant.getRoster();
        ArrayList<Player> direArrayList = dire.getRoster();
        pickPhase.sort(Comparator.comparing(Hero::getStrength,Comparator.reverseOrder()));
        int seed = World.getRandomNumber(0,6);
        banHero(pickPhase.get(seed));
        banHero(pickPhase.get(seed));
        seed = World.getRandomNumber(0,2);
        banHero(pickPhase.get(seed));
        banHero(pickPhase.get(seed));
        for (int i = 0; i < 5; i++) {
            InGamePlayer radTemp = new InGamePlayer(radiantArrayList.get(i));
            InGamePlayer dirTemp = new InGamePlayer(direArrayList.get(i));
            radTemp.pickHero();
            dirTemp.pickHero();
            rad.addPlayer(radTemp);
            dir.addPlayer(dirTemp);
        }
        int radP = World.getRandomNumber(0, 4);
        int dirP = World.getRandomNumber(0, 4);
        InGamePlayer radPlayer;
        InGamePlayer dirPlayer;
        while (true) {
            if (dir.noTowers() || rad.noTowers()) {
                if (rad.noTowers()) {
                    radiant.updateRunningPerf(-1);
                    dire.updateRunningPerf(1);
                    updatePerf(rad.getPlayers(), dir.getPlayers());
                    radiant.incrementLosses();
                    dire.incrementWins();
                    //printResults(dir,rad);
                    return dire;
                }
                else {
                    dire.updateRunningPerf(-1);
                    radiant.updateRunningPerf(1);
                    dire.incrementLosses();
                    radiant.incrementWins();
                    updatePerf(dir.getPlayers(), rad.getPlayers());
                    //printResults(rad,dir);
                    return radiant;
                }
            }
            else {
                radPlayer = rad.getPlayer(radP);
                dirPlayer = dir.getPlayer(dirP);
                ArrayList<InGamePlayer> battleResult = radPlayer.battlePlayer(dirPlayer);
                InGamePlayer winner = battleResult.get(0);
                InGamePlayer loser = battleResult.get(1);
                winner.kill(loser);
                loser.team.killPlayer(loser);
                rad.status(dir.getKills()-rad.getKills());
                dir.status(rad.getKills() - dir.getKills());
                radP = World.getRandomNumber(0, rad.getLivingPlayersSize());
                dirP = World.getRandomNumber(0, dir.getLivingPlayersSize());
            }
        }

    }

    public void playGame(Team radiantTeam, Team direTeam, String s) {
        InGameTeam rad = new InGameTeam("Radiant",radiantTeam);
        InGameTeam dir = new InGameTeam("Dire",direTeam);
        ArrayList<Player> radiantArrayList = radiantTeam.getRoster();
        ArrayList<Player> direArrayList = direTeam.getRoster();
        pickPhase.sort(Comparator.comparing(Hero::getStrength,Comparator.reverseOrder()));
        int seed = World.getRandomNumber(0,6);
        banHero(pickPhase.get(seed));
        banHero(pickPhase.get(seed));
        seed = World.getRandomNumber(0,2);
        banHero(pickPhase.get(seed));
        banHero(pickPhase.get(seed));
        for (int i = 0; i < 5; i++) {
            InGamePlayer radTemp = new InGamePlayer(radiantArrayList.get(i));
            InGamePlayer dirTemp = new InGamePlayer(direArrayList.get(i));
            radTemp.pickHero();
            dirTemp.pickHero();
            rad.addPlayer(radTemp);
            dir.addPlayer(dirTemp);
        }
        int radP = World.getRandomNumber(0, 4);
        int dirP = World.getRandomNumber(0, 4);
        InGamePlayer radPlayer;
        InGamePlayer dirPlayer;
        while (true) {
            if (dir.noTowers() || rad.noTowers()) {
                if (rad.noTowers()) {
                    for(int i = 0; i < 5; i++){
                        radiantArrayList.get(i).updateMmr(-25);
                        direArrayList.get(i).updateMmr(25);
                    }
                    if(s == "print"){
                        printResults(dir,rad,"s");
                    }
                    return;
                }
                else {
                    for(int i = 0; i < 5; i++){
                        radiantArrayList.get(i).updateMmr(25);
                        direArrayList.get(i).updateMmr(-25);
                    }
                    if(s == "print"){
                        printResults(dir,rad,"s");
                    }
                    return;
                }
            }
            else {
                radPlayer = rad.getPlayer(radP);
                dirPlayer = dir.getPlayer(dirP);
                ArrayList<InGamePlayer> battleResult = radPlayer.battlePlayer(dirPlayer);
                InGamePlayer winner = battleResult.get(0);
                InGamePlayer loser = battleResult.get(1);
                winner.kill(loser);
                loser.team.killPlayer(loser);
                rad.status(dir.getKills()-rad.getKills());
                dir.status(rad.getKills() - dir.getKills());
                radP = World.getRandomNumber(0, rad.getLivingPlayersSize());
                dirP = World.getRandomNumber(0, dir.getLivingPlayersSize());
            }
        }

    }

    public void banHero(Hero hero){
        pickPhase.remove(hero);
        hero.incrementBans();
    }

    public void updatePerf(ArrayList<InGamePlayer> a, ArrayList<InGamePlayer> b) {
        for (int i = 0; i < 5; i++) {
            Player player1 = a.get(i).player;
            Player player2 = b.get(i).player;
            a.get(i).hero.incrementLosses();
            b.get(i).hero.incrementWins();
            /*
            player1.updateAvgK(a.get(i).kills);
            player1.updateAvgD(a.get(i).deaths);
            player1.updateAvgNW((long)a.get(i).getNetWorth());
            player2.updateAvgK(b.get(i).kills);
            player2.updateAvgD(b.get(i).deaths);
            player2.updateAvgNW((long)b.get(i).getNetWorth());
            */
            if (a.get(i).kills > a.get(i).deaths) {
                player1.updateNetPerf(1);
            } else if (a.get(i).kills < a.get(i).deaths) {
                player1.updateNetPerf(-1);
            }
            if (b.get(i).kills > b.get(i).deaths) {
                player2.updateNetPerf(1);
            } else if (b.get(i).kills < b.get(i).deaths) {
                player2.updateNetPerf(-1);
            }
        }
    }

    public void printResults(InGameTeam winner, InGameTeam loser){
        System.out.println("-------------");
        System.out.println(winner.getTeam().toString() + " Defeat " + loser.getTeam().toString());
        System.out.println("-------------");
        for(InGamePlayer p : winner.getPlayers()){
            System.out.println(p.toString());
        }
        System.out.println("-------------");
        for(InGamePlayer p : loser.getPlayers()){
            System.out.println(p.toString());
        }
    }

    public void printResults(InGameTeam winner, InGameTeam loser,String s){
        System.out.println("-------------");
        System.out.println(winner.getTeam().toString() + " Defeat " + loser.getTeam().toString());
        System.out.println("-------------");
        for (InGamePlayer p : winner.getPlayers()) {
            System.out.println(p.toString() + " | " + p.getPlayer().getMmr());
        }
        System.out.println("-------------");
        for (InGamePlayer p : loser.getPlayers()) {
            System.out.println(p.toString() + " | " + p.getPlayer().getMmr());
        }

    }

    private class InGamePlayer {
        int kills;
        int deaths;
        Player player;
        double skill;
        int id;

        int assists;

        double netWorth;
        boolean alive;
        InGameTeam team;
        Hero hero;

        double killBounty;

        public InGamePlayer(Player p) {
            kills = 0;
            deaths = 0;
            player = p;
            double seed = World.getRandomNumber(8, 12) / 10.0;
            skill = p.getSkill() * seed;
            id = -1;
            netWorth = 1000;
            assists = 0;
            alive = true;
            killBounty = 0;
        }
        public void setTeam(InGameTeam team){
            this.team = team;
        }

        public Player getPlayer(){
            return player;
        }

        public void pickHero(){
            int seed = World.getRandomNumber(Math.max(0,pickPhase.size()-5),pickPhase.size()-1);
            int rand = World.getRandomNumber(0,5);
            if (rand >= 3) {
                if(rand == 4){
                    pickHero(pickPhase.remove(seed));
                }
                else{
                    pickHero(pickPhase.remove(rand));
                }
            }
            else{
                pickHero(pickPhase.remove(0));
            }
        }
        public void pickHero(Hero hero){
            this.hero = hero;
            hero.pickHero();
        }

        public ArrayList<InGamePlayer> battlePlayer(InGamePlayer enemy){
            ArrayList<InGamePlayer> results = new ArrayList<>();
            double myHp = this.hero.hp;
            double enemyHp = enemy.hero.hp;
            double mySkill = this.getSkill() * ((double) World.getRandomNumber(8,12)/10);
            double enemySkill = enemy.getSkill() * ((double) World.getRandomNumber(8,12)/10);
            int seed = World.getRandomNumber(0,(int)mySkill + (int)enemySkill);
            while(enemyHp > 0 && myHp > 0){
                if(seed <= mySkill){
                    if (seed <= mySkill/2){
                        enemyHp -= this.hero.dmg;
                    }
                }
                else{
                    if (seed >= mySkill + enemySkill/2){
                        myHp -= enemy.hero.dmg;
                    }
                }
                seed = World.getRandomNumber(0,(int)mySkill + (int)enemySkill);
            }
            if(enemyHp <= 0){
                results.add(this);
                results.add(enemy);
            }
            else{
                results.add(enemy);
                results.add(this);
            }
            return results;


        }


        public void addNetWorth(double nw){
            netWorth += nw;
            if(netWorth <= 0){
                netWorth = 1;
            }
        }
        public int getID(){
            return id;
        }

        public void setID(int id){
            this.id = id;
        }
        public double getSkill() {
            return (skill + getNetWorth())*((hero.getStrength()*player.getHeroMap().get(hero))/1000);
        }

        public void die() {
            deaths++;
        }
        public double getNetWorth(){
            return netWorth;
        }

        public void kill(InGamePlayer p) {
            kills++;
            double formula = p.getNetWorth() - getNetWorth();
            if(formula >= -200){
                if (formula >= 0){
                    addNetWorth(100 + formula/2);
                    p.addNetWorth(-formula/2);
                }
                else{
                    addNetWorth(100);
                    p.addNetWorth(-50);
                }
            }
            else{
                addNetWorth(50);
                p.addNetWorth(-25);
            }
            addNetWorth(p.killBounty*100);
            p.killBounty = 0;
            killBounty *= 2;
            if(killBounty >= 20){
                killBounty = 20;
            }
            for(InGamePlayer teammate : team.getPlayers()){
                if(teammate.alive){
                    if(World.getRandomNumber(0,2) >= 1){
                        teammate.addNetWorth(50);
                        teammate.assists++;
                    }

                }

            }
            p.die();
        }

        public String toString() {
            return (player.toString() + " as " + hero.name + " | " + kills + "/" + deaths + "/" +  assists + " | " + (int)getNetWorth());
        }
    }

    private class InGameTeam {
        HashMap<Integer, InGamePlayer> livingPlayers;
        HashMap<Integer, InGamePlayer> deadPlayers;

        ArrayList<InGamePlayer> players;
        int kills;
        int towers;
        String name;
        int ticks;
        int deathTicker;
        Team team;

        public InGameTeam(String name,Team team) {
            this.name = name;
            kills = 0;
            towers = 9;
            players = new ArrayList<>();
            livingPlayers = new HashMap<>();
            deadPlayers = new HashMap<>();
            ticks = 0;
            deathTicker = 0;
            this.team = team;
        }

        public int getKills() {
            return kills;
        }

        public Team getTeam(){
            return team;
        }

        public void setKills(int kills) {
            this.kills = kills;
        }

        public int getTowers() {
            return towers;
        }

        public void setTowers(int towers) {
            this.towers = towers;
        }

        public boolean noTowers(){
            if (getTowers() == 0){
                return true;
            }
            return false;
        }

        public int getLivingPlayersSize(){
            return livingPlayers.size();
        }

        public void addPlayer(InGamePlayer player) {
            players.add(player);
            player.setTeam(this);
            player.setID(players.indexOf(player));
            livingPlayers.put(player.getID(), player);

        }

        public void killPlayer(InGamePlayer player) {
            toDead(player);
            deathTicker++;
        }

        public InGamePlayer getPlayer(int i){
            InGamePlayer toReturn = livingPlayers.get(i);
            if(toReturn != null){
                return toReturn;
            }
            i = 4;
            while(toReturn == null){
                toReturn = livingPlayers.get(i);
                i--;
            }
            return toReturn;
        }

        private void toDead(InGamePlayer player) {
            player.alive = false;
            livingPlayers.remove(player.getID());
            deadPlayers.put(player.getID(), player);
        }

        private void toLiving(InGamePlayer player) {
            player.alive = true;
            deadPlayers.remove(player.getID());
            livingPlayers.put(player.getID(), player);
        }

        public void respawnPlayer(){
            if(deadPlayers.size() != 0){
                int seed = World.getRandomNumber(0,4);
                InGamePlayer toRespawn = deadPlayers.get(seed);
                if(toRespawn != null){
                    toLiving(toRespawn);
                    return;
                }
                while(toRespawn == null){
                    seed = World.getRandomNumber(0,4);
                    toRespawn = deadPlayers.get(seed);
                }
                toLiving(toRespawn);
            }
        }

        public ArrayList<InGamePlayer> getPlayers(){
            return players;
        }
        public void status(int killDiff) {
            int nwMod = 0;
            if(killDiff >= 0){
                nwMod = 50* killDiff;
            }
            for(InGamePlayer p : players){
                if(!deadPlayers.containsKey(p.getID())){
                    p.addNetWorth(750 + nwMod);
                }
                else{
                    p.addNetWorth(250 + nwMod);
                }
            }
            if(deadPlayers.size() == 5){
                respawnPlayer();
                towers--;
                deathTicker = 0;
            }
            else if (deathTicker == 7) {
                respawnPlayer();
                towers--;
                deathTicker = 0;
            }
            else{
                if(ticks == 3){
                    respawnPlayer();
                    ticks = 0;
                }
                ticks++;
            }
        }
    }

}
