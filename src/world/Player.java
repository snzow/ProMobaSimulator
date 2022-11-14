package world;

public class Player {

    String name;
    int skill;
    int age;
    PlayerContract contract;
    Team team;

    int netPerformance;

    public Player(String s, int sk, int a){
        name = s;
        skill = sk;
        age = a;
        netPerformance = 0;
    }

    public Player(String s){
        name = s;
        skill = World.getRandomNumber(700,1300);
        age = World.getRandomNumber(16,21);
        netPerformance = 0;
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
    public String toString(){
        if (team == null){
            return name;
        }
        return (team.getTag() + "." + name);
    }


}
