package world;

import game.Game;

import java.util.ArrayList;

public class Tournament {
    ArrayList<Team> teams;
    ArrayList<TournamentResult> pastResults;
    String name;
    double prizePool;
    double pointsPool;

    public Tournament(String n, double pr, double po){
        name = n;
        prizePool = pr;
        pointsPool = po;
        pastResults = new ArrayList<>();

    }

    public void runTournament(ArrayList<Team> t){
        teams = t;
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
            System.out.println(sizeForRound);
            for (Team b : roundNow){
                b.updatePoints(pointsPool);
                b.receivePrizeMoney(prizePool/10);
            }
            for (int j = 0; j < sizeForRound; j++){
                int q = roundNow.size() - 1 - j;
                Game g = new Game(roundNow.get(j),roundNow.get(q));
                if (i >= roundsToPlay - 2){
                    pastResults.add(new TournamentResult(g.playSeries(3)));
                    TournamentResult toEdit = pastResults.get(pastResults.size() - 1);
                    System.out.println("------------");
                    System.out.println(toEdit.winner + " has won " + name);
                    System.out.println("------------");
                    return;
                }
                else{
                    rounds.get(i+1).add(g.playSeries(2));
                }
            }
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
    }
}
