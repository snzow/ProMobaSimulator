package world;

import game.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Tournament {
    ArrayList<Team> teams;
    ArrayList<TournamentResult> pastResults;
    String name;
    double prizePool;
    double pointsPool;
    boolean major;


    public Tournament(String n, double pr, double po, boolean maj){
        name = n;
        prizePool = pr;
        pointsPool = po;
        pastResults = new ArrayList<>();
        major = maj;

    }

    public void runTournament(ArrayList<Team> t){
        TournamentResult thisTourney = new TournamentResult();
        pastResults.add(thisTourney);
        boolean ti = false;
        if(!major){
            int numTeams = 16;
            teams = new ArrayList<Team>();
            if (pointsPool < 700){
                t.sort(Comparator.comparing(Team::getPoints));
                numTeams = 8;
            }
            else{
                Collections.shuffle(t);
            }
            for(int i = 0; i < numTeams; i++){
                teams.add(t.get(i));
            }
        }
        else{
            t.sort(Comparator.comparing(Team::getPoints,Comparator.reverseOrder()));
            teams = t;
            if (name == "The International"){
                ti = true;
            }
        }

        int roundsToPlay = 1;
        int numCheck = 2;
        int round = 1;

        //find out how many rounds the event needs. log base 2 of teams length
        while (numCheck <= teams.size()){
            roundsToPlay++;
            numCheck *= 2;
        }
        //an array list of arraylists that will hold the competitors for the rounds
        ArrayList<ArrayList> rounds = new ArrayList<>();
        System.out.println(name);
        //putting the array lists in
        for (int i = 0; i < roundsToPlay; i++){
            ArrayList<Team> tmp = new ArrayList<>();
            rounds.add(tmp);
        }
        //putting the teams in the first round -- WORKS
        for(int i = 0; i < numCheck /2; i++){
            Team tmp = teams.get(i);
            rounds.get(0).add(tmp);

        }

        for(int i = 0; i < roundsToPlay; i++){
            System.out.println("------------");
            System.out.println("Round: " + (i+1));
            System.out.println("------------");
            ArrayList<Team> roundNow = rounds.get(i);
            int sizeForRound = roundNow.size() / 2;
            for (Team b : roundNow){
                b.updatePoints(pointsPool/roundNow.size());
                b.receivePrizeMoney(prizePool/roundNow.size());
            }
            for (int j = 0; j < sizeForRound; j++){
                int q = roundNow.size() - 1 - j;
                Game g = new Game(roundNow.get(j),roundNow.get(q));
                if (i >= roundsToPlay - 2){
                    Team one = roundNow.get(j);
                    Team two = roundNow.get(q);
                    Team win = g.playSeries(3);
                    Team loss;
                    if(win.equals(one)){
                        loss = two;
                    }
                    else{
                        loss = one;
                    }
                    thisTourney.winner = win;
                    thisTourney.setSecond(loss);
                    System.out.println("------------");
                    System.out.println(win + " has won " + name);
                    System.out.println("------------");
                    win.winTourney(major,ti);
                    win.updatePoints(pointsPool);
                    win.receivePrizeMoney(prizePool);
                    return;
                }
                else if(i >= roundsToPlay -3){
                    Team one = roundNow.get(j);
                    Team two = roundNow.get(q);
                    Team win = g.playSeries(2);
                    Team loss;
                    if(win.equals(one)){
                        loss = two;
                    }
                    else{
                        loss = one;
                    }
                    if (thisTourney.thirdFourthTwo != null){
                        thisTourney.setThirdOne(loss);
                    }
                    else{
                        thisTourney.setThirdTwo(loss);
                    }
                    rounds.get(i+1).add(win);
                }
                else{
                    rounds.get(i+1).add(g.playSeries(2));
                }
            }
        }
    }

    public void printTournamentHistory(){
        System.out.println("History: ");
        int year = 1;
        for(TournamentResult t : pastResults){
            System.out.println("year " + year + ": " + t.toString());
            year++;
        }
    }

    private class TournamentResult{
        Team winner;
        Team second;
        Team thirdFourthOne;
        Team thirdFourthTwo;

        public TournamentResult(Team first, Team sec, Team thirdOne, Team thirdTwo){
            winner = first;
            second = sec;
            thirdFourthOne = thirdOne;
            thirdFourthTwo = thirdTwo;
        }

        public TournamentResult(){
        }
        public TournamentResult(Team w){
            winner = w;
        }

        public void setSecond(Team w){
            second = w;
        }

        public void setThirdOne(Team w){
            thirdFourthOne = w;
        }

        public void setThirdTwo(Team w){
            thirdFourthTwo = w;
        }

        public String toString(){
            return "1st: " + winner + " | 2nd: " + second + " | 3rd/4th: " + thirdFourthOne + ", " + thirdFourthTwo;
        }
    }
}
