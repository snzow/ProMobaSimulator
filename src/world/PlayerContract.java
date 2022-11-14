package world;

public class PlayerContract {

    int salary;
    int yearsRemaining;
    Team team;


    public PlayerContract(int s, int y, Team t){
        salary = s;
        yearsRemaining = y;
        team = t;
    }

    public void tickYear(){
        yearsRemaining--;
    }

}
