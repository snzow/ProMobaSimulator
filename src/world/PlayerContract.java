package world;

public class PlayerContract {

    double salary;
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

    public double getSalary(){
        return salary;
    }

    public int getYearsRemaining(){
        return yearsRemaining;
    }

    public void extendContract(double s, int y){
        salary = Math.max(salary, team.balance/15*s*World.getRandomNumber(900,1100)/1000);
        //salary = Math.min(salary,850_000)
        yearsRemaining += y;
    }


}
