package world;

import java.sql.Array;
import java.text.NumberFormat;
import java.util.ArrayList;

public class Team {

    ArrayList<Player> players;
    ArrayList<TeamSeasonRecord> history;
    Organization sponsor;
    String name;

    String tag;
    double balance;
    double prizeMoneyY;
    int majorsWon;
    int majorsWonY;
    int tiWon;
    int tiWonY;
    int tourneysWon;
    int tourneysWonY;

    double pointsY;
    double pointsL;

    int runningPerf;
    int wins;
    int winsY;
    int losses;
    int lossesY;
    int worldRanking;

    public Team(String n,String t){
        name = n;
        tag = t;
        balance = 100000;
        prizeMoneyY = 0;
        majorsWon = 0;
        players = new ArrayList<>();
        history = new ArrayList<>();
        pointsY = 0;
        pointsL = 0;
        runningPerf = 0;
        wins = 0;
        winsY = 0;
        losses = 0;
        lossesY = 0;
        worldRanking = 0;
    }

    public void signPlayer(Player p){
        System.out.println(name + " have signed " + p);
        players.add(p);
        p.signContract(this, (int) (balance/8),2);

    }
    public void winTourney(boolean maj,boolean ti){
        tourneysWon++;
        tourneysWonY++;
        if(maj){
            majorsWon++;
            majorsWonY++;
        }
        if(ti){
            tiWon++;
            tiWonY++;
        }
    }

    public Player dropPlayer(Player p){
        p.cut();
        System.out.println(p + " has been dropped by " + name);
        return p;
    }

    public void updateRunningPerf(int x){
        runningPerf += x;
    }
    public int getRunningPerf(){
        return runningPerf;
    }
    public void signSponsor(Organization s){
        sponsor = s;
        System.out.println(name + " have been picked up by " + sponsor.toString());
        name = sponsor.toString();
    }

    public void receivePrizeMoney(double pm){
        prizeMoneyY += pm;
        balance += pm;
    }

    public void payPlayers(){
        System.out.println("--------------");
        System.out.println(name + " transactions");
        System.out.println("--------------");
        for (int i = 0; i < players.size(); i ++){
            Player p = players.get(i);
            p.payPlayer(p.getSalary());
            balance -= p.getSalary();
            //makes teams make more accurate judgements on extending players
            //i.e. less likely to extend after a bad year, more after a good one
            int moddedPerf = p.getNetPerf() + getRunningPerf();
            if (moddedPerf < -10){
                dropPlayer(p);
            }
            else{
                if(p.getContractYearsRemaining() == 0){
                    if (moddedPerf > 10){
                        p.extendContract(1.5 ,3);
                    }
                    else if(moddedPerf > 5){
                        p.extendContract(1.25,2);
                    }
                    else if(moddedPerf > 0){
                        p.extendContract(1.1,1);
                    }
                    else{
                        dropPlayer(p);
                    }
                }

            }
        }
        runningPerf = 0;
        System.out.println("--------------");

    }
    public int getWorldRanking(){
        return worldRanking;
    }

    public void setWorldRanking(int x){
        worldRanking = x;
    }

    public double getPoints(){
        return pointsY + pointsL;
    }

    public void updatePoints(double p){
        pointsY += p;
    }

    public void tickYear(){
        TeamSeasonRecord tmp = new TeamSeasonRecord(winsY,lossesY,prizeMoneyY);
        history.add(tmp);
        pointsL = pointsY/2;
        pointsY = 0;
        payPlayers();
        tourneysWonY = 0;
        majorsWonY = 0;
        tiWonY = 0;
        winsY = 0;
        lossesY = 0;
        prizeMoneyY = 0;
    }

    public void incrementWins(){
        wins++;
        winsY++;
    }

    public void incrementLosses(){
        losses++;
        lossesY++;
    }

    public String getTag(){
        return tag;
    }
    public double getBalance(){
        return balance;
    }

    public ArrayList<Player> getRoster(){
        return (ArrayList<Player>) players.clone();
    }

    public boolean isFull(){
        return players.size() == 5;
    }

    /**
     *
     * @param y 0 for tourneysY, 1 for tourneys all time
     * @return tourneys
     */
    public int getTourneysWon(int y){
        if (y == 1){
            return tourneysWon;
        }
        else{
            return tourneysWonY;
        }
    }

    /**
     *
     * @param y 0 for tourneysY, 1 for tourneys all time
     * @return tourneys
     */
    public int getMajorsWon(int y){
        if (y == 1){
            return majorsWon;
        }
        else{
            return majorsWonY;
        }
    }
    /**
     *
     * @param y 0 for tourneysY, 1 for tourneys all time
     * @return tourneys
     */
    public int getTiWon(int y){
        if (y == 1){
            return tiWon;
        }
        else{
            return tiWonY;
        }
    }
    public void printRoster(){
        System.out.println("-------------");
        System.out.println(name);
        System.out.println("-------------");
        for (int i = 0; i < 5; i++){
            Player p = players.get(i);
            if (p.getYearsWithTeam() == 0){
                System.out.println(players.get(i) + "(new)");
            }
            else{
                System.out.println(players.get(i) + " " + p.getYearsWithTeam() + " year(s) with team");
            }

        }
    }

    public int getRosterSize(){
        return players.size();
    }
    public String toString(){
        String z = "";
        if(tiWonY == 1){
            z = "*";
        }
        return z + name + z;
    }

    public void printHistory(){
        int year = 0;
        System.out.println("---" + name + "---");
        for (TeamSeasonRecord r : history){
            if(year != 0){
                System.out.println("Year " + year + " | " + r.toString());
                System.out.println("-----------");
                r.printRoster();
                System.out.println("-----------");
            }
            year++;
        }
        System.out.println("-----------");
    }

    private class TeamSeasonRecord{
        int wins;
        int losses;
        int tourneyWins;
        int majorWins;
        int tiWins;
        double prizeWinnings;
        ArrayList<Player> roster;
        int worldRanking;

        public TeamSeasonRecord(int w, int l, double pw){
            wins = w;
            losses = l;
            tourneyWins = getTourneysWon(0);
            majorWins = getMajorsWon(0);
            tiWins = getTiWon(0);
            prizeWinnings = pw;
            roster = getRoster();
            worldRanking = getWorldRanking();
        }

        public void printRoster(){
            for (Player p : roster){
                System.out.println(p.getName());
            }
        }

        public String toString(){
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            return tourneyWins + "(" + majorWins + ") | " + wins + "-" + losses + " | " + formatter.format(prizeWinnings);
        }
    }

}
