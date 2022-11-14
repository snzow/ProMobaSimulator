package world;

import java.text.NumberFormat;

public class Player {

    String name;
    int skill;
    int age;
    PlayerContract contract;
    Team team;

    Team prevTeam;

    double earningsY;

    double earnings;

    int netPerformance;

    public Player(String s, int sk, int a){
        name = s;
        skill = sk;
        age = a;
        netPerformance = 0;
        earningsY = 0;
        earnings = 0;
        prevTeam = new Team("T","T");
    }

    public Player(String s){
        name = s;
        skill = World.getRandomNumber(700,1300);
        age = World.getRandomNumber(16,21);
        netPerformance = 0;
        earningsY = 0;
        earnings = 0;
        prevTeam = new Team("T","T");
    }

    public void signContract(Team t, int sal, int years){
        team = t;
        contract = new PlayerContract(sal,years,t);
    }

    public void setTeam(Team t){
        team = t;
    }
    public int getSkill(){
        return skill;
    }

    public void updateNetPerf(int x){
        netPerformance += x;
    }

    public int getNetPerf(){
        return netPerformance;
    }

    public void payPlayer(double p){
        earnings += p;
        earningsY += p;
    }

    public double getSalary(){
        return contract.getSalary();
    }

    public void yearEndStats(){
        earningsY = 0;
        age++;
        contract.tickYear();
    }

    public void updateSkill(int x){
        skill += x;
    }
    public int getContractYearsRemaining(){
        if (contract == null){
            return 0;
        }
        return contract.getYearsRemaining();
    }

    public void extendContract(double s, int y){
        contract.extendContract(s,y);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        System.out.println(name + " has been extended by " + team + " for " + formatter.format(getSalary()) + "/" + y);
    }

    public Team getTeam(){
        return team;
    }
    public void cut(){
        for(int i = 0; i < getContractYearsRemaining(); i++){
            payPlayer(getSalary()/2);
        }
        team.players.remove(this);
        prevTeam = team;
        team = null;
        contract = null;
        World.freeAgents.add(this);
    }

    public Team getPrevTeam(){
        return prevTeam;
    }
    public String toString(){
        if (team == null){
            return name;
        }
        return (team.getTag() + "." + name);
    }


}
