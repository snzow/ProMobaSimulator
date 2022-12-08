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
        salary += team.balance/15;
        salary *= s;
        salary = Math.min(salary,650000);
        yearsRemaining += y;
    }


}
