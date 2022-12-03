package world;

import game.Hero;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Player {

    String name;
    int skill;
    int age;
    PlayerContract contract;
    Team team;

    Team prevTeam;

    double earningsY;

    double earnings;

    long avgK;

    long avgD;

    long avgNW;

    int netPerformance;
    int yearsWithTeam;
    ArrayList<yearResults> history;

    HashMap<Hero, Double> heroMap;

    public Player(String s, int sk, int a){
        name = s;
        skill = sk;
        age = a;
        netPerformance = 0;
        earningsY = 0;
        earnings = 0;
        prevTeam = World.FREE_AGENT;
        team = World.FREE_AGENT;
        yearsWithTeam = 0;
        history = new ArrayList<>();
        heroMap =  new HashMap<>(World.heroMap);
        for(Hero h : World.heroList){
            double seed = World.getRandomNumber(7,13)/10.0;
            heroMap.put(h,seed);
        }
        avgD = 0;
        avgK = 0;
        avgNW = 0;
    }

    public Player(String s){
        name = s;
        skill = World.getRandomNumber(700,1300);
        age = World.getRandomNumber(13,19);
        netPerformance = 0;
        earningsY = 0;
        earnings = 0;
        team = World.FREE_AGENT;
        prevTeam = World.FREE_AGENT;
        heroMap =  new HashMap<>(World.heroMap);
        yearsWithTeam = -1;
        history = new ArrayList<>();
        for(Hero h : World.heroList) {
            double seed = World.getRandomNumber(7, 13) / 10.0;
            heroMap.put(h, seed);
        }
    }

    public long getAvgK() {
        return avgK;
    }

    public void updateAvgK(long avgK) {
        if(this.avgK == 0){
            this.avgK = avgK;
        }
        else{
            this.avgK = (avgK + this.avgK)/2;
        }
    }

    public long getAvgD() {
        return avgD;
    }

    public void updateAvgD(long avgD) {
        if(this.avgD == 0){
            this.avgD = avgD;
        }
        else{
            this.avgD = (avgD + this.avgD)/2;
        }
    }

    public long getAvgNW() {
        return avgNW;
    }

    public void updateAvgNW(long avgNW) {
        if(this.avgNW == 0){
            this.avgNW = avgNW;
        }
        else{
            this.avgNW = (avgNW + this.avgNW)/2;
        }
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
        team.addExtension(this);
    }


    public Team getTeam(){
        return team;
    }
    public void cut(){
        for(int i = 0; i < getContractYearsRemaining(); i++){
            payPlayer(getSalary()/5);
        }
        team.players.remove(this);
        prevTeam = team;
        team = World.FREE_AGENT;
        contract = null;
        netPerformance = 0;
        World.freeAgents.add(this);
        yearsWithTeam = 0;
    }

    public Team getPrevTeam(){
        return prevTeam;
    }

    public void setPrevTeam(Team t){ prevTeam = t;}

    public void printHistory(){
        System.out.println("------------");
        System.out.println(name + " Team | Skill | Tournaments Won(Majors Won) + Salary");
        int majorsWon = 0;
        int tourneysWon = 0;
        double totalEarnings = 0;
        int tiWon = 0;
        int year = 0;
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        for (yearResults r : history){
            if (year != 0){
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

    public HashMap<Hero,Double> getHeroMap(){
        return heroMap;
    }

    public void ageUp(){
        age++;
    }

    public int getAge(){
        return age;
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
}
