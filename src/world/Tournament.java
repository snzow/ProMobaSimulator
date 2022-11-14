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
        System.out.println(name);
        System.out.println("Round One:");
        Game semi1 = new Game(teams.get(0),teams.get(3));
        Game semi2 = new Game(teams.get(1), teams.get(2));
        ArrayList<Team> grands = new ArrayList<>();
        grands.add(semi1.playSeries(2));
        grands.add(semi2.playSeries(2));
        Game gs = new Game(grands.get(0),grands.get(1));
        pastResults.add(new TournamentResult(gs.playSeries(3)));
        TournamentResult toEdit = pastResults.get(pastResults.size() - 1);
        grands.remove(toEdit.winner);
        toEdit.setSecond(grands.get(0));
        System.out.println(toEdit.winner + " has won " + name);



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
