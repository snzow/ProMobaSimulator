package world;

import game.Hero;

import java.text.NumberFormat;
import java.util.ArrayList;

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
    int yearsWithTeam;
    ArrayList<yearResults> history;

    public Player(String s, int sk, int a){
        name = s;
        skill = sk;
        age = a;
        netPerformance = 0;
        earningsY = 0;
        earnings = 0;
        prevTeam = new Team("Free Agent","Free Agent");
        team = new Team("Free Agent","Free Agent");
        yearsWithTeam = 0;
        history = new ArrayList<>();
    }

    public Player(String s){
        name = s;
        skill = World.getRandomNumber(700,1300);
        age = World.getRandomNumber(16,21);
        netPerformance = 0;
        earningsY = 0;
        earnings = 0;
        team = new Team("Free Agent","Free Agent");
        prevTeam = new Team("Free Agent","Free Agent");
        yearsWithTeam = 0;
        history = new ArrayList<>();
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
        if (contract == null){
            return 0;
        }
        return contract.getSalary();
    }

    public void yearEndStats(){
        earningsY = 0;
        age++;
        if(contract != null){
            contract.tickYear();
        }
        yearsWithTeam++;
        history.add(new yearResults());
    }

    public int getYearsWithTeam(){
        return yearsWithTeam;
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
        team = new Team("Free Agent","Free Agent");
        contract = null;
        World.freeAgents.add(this);
        yearsWithTeam = 0;
    }

    public Team getPrevTeam(){
        return prevTeam;
    }

    public void printHistory(){
        System.out.println("------------");
        System.out.println(name + " Team | Skill | Tournaments Won(Majors Won) + Salary");
        int majorsWon = 0;
        int tourneysWon = 0;
        double totalEarnings = 0;
        int tiWon = 0;
        int year = 1;
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        for (yearResults r : history){
            if (year != 1){
                System.out.println( "year " + year + ": " + r.toString());
            }
            year++;
            majorsWon += r.majorsWon;
            tourneysWon += r.tourneysWon;
            totalEarnings += r.salary;
            tiWon += r.ti;
        }
        System.out.println("------------");
        System.out.println(name + " | " + team + " | " + tourneysWon + "(" + majorsWon + ") | " + formatter.format(totalEarnings) + " | " + tiWon + " TI's Won");

        System.out.println("------------");
    }
    public String getName(){
        return name;
    }
    public String toString(){
        if (team.toString().equals("Free Agent")){
            return name;
        }
        return (team.getTag() + "." + name);
    }

    private class yearResults{
        Team team;
        double ySkill;
        int tourneysWon;
        int majorsWon;
        int ti;
        double salary;

        public yearResults(){
            team = getTeam();
            ySkill = getSkill();
            tourneysWon = team.getTourneysWon(0);
            majorsWon = team.getMajorsWon(0);
            ti = team.getTiWon(0);
            salary = getSalary();
        }

        public String toString(){
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String z = "";
            if (ti == 1){
                z = " TI Winner";
            }
            return (name + " | " + team + " | " + ySkill + " | " + tourneysWon + "(" + majorsWon + ") | " + formatter.format(salary) + " | " + z);
        }
    }
    private class HeroAptitude{
        Hero hero;
        double aptitude;

        public HeroAptitude(Hero h, double ap){
            hero = h;
            aptitude = ap;
        }
    }

}
