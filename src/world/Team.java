package world;

import java.util.ArrayList;

public class Team {

    ArrayList<Player> players;
    Organization sponsor;
    String name;

    String tag;
    double balance;
    double prizeMoneyY;
    int majorsWon;
    ArrayList<Integer> resultsY;

    double pointsY;
    double pointsL;

    int runningPerf;

    public Team(String n,String t){
        name = n;
        tag = t;
        balance = 100000;
        prizeMoneyY = 0;
        majorsWon = 0;
        players = new ArrayList<>();
        resultsY = new ArrayList<>();
        pointsY = 0;
        pointsL = 0;
        runningPerf = 0;
    }

    public void signPlayer(Player p){
        System.out.println(name + " have signed " + p);
        players.add(p);
        p.signContract(this, (int) (balance/10),2);

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
            p.yearEndStats();
            p.updateNetPerf(getRunningPerf());
            if (getRunningPerf() < -10){
                dropPlayer(p);
            }
            else{
                if(p.getContractYearsRemaining() == 0){
                    if (p.getNetPerf() > 10){
                        p.extendContract(1.5 ,3);
                    }
                    else if(p.getNetPerf() > 5){
                        p.extendContract(1.25,2);
                    }
                    else if(p.getNetPerf() > 0){
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

    public double getPoints(){
        return pointsY + pointsL;
    }

    public void updatePoints(double p){
        pointsY += p;
    }

    public void tickYear(){
        pointsL = pointsY/2;
        pointsY = 0;
        payPlayers();
    }

    public String getTag(){
        return tag;
    }

    public ArrayList<Player> getRoster(){
        return players;
    }

    public int getRosterSize(){
        return players.size();
    }
    public String toString(){
        return name;
    }


}
