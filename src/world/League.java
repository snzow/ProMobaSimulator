package world;

import java.util.ArrayList;
import java.util.HashMap;

public class League {
    ArrayList<LeagueTeam> teams;
    ArrayList<League.LeagueResults> pastResults;
    String name;
    double prizePool;
    double pointsPool;
    boolean major;

    HashMap<Team,Integer> gamesPlayed;

    public League(String name, double prizePool, double pointsPool, boolean major) {
        this.name = name;
        this.prizePool = prizePool;
        this.pointsPool = pointsPool;
        this.major = major;
        gamesPlayed = new HashMap<>();
        pastResults = new ArrayList<LeagueResults>();
    }

    public void addTeam(Team team){
        teams.add(new LeagueTeam(team));
    }

    public void playWeek(){
        for(LeagueTeam team : teams){
            if(!team.playedThisWeek){

            }
        }
    }

    public ArrayList<League.LeagueResults> getPastResults() {
        return pastResults;
    }

    public void setPastResults(ArrayList<League.LeagueResults> pastResults) {
        this.pastResults = pastResults;
    }

    public double getPrizePool() {
        return prizePool;
    }

    public void setPrizePool(double prizePool) {
        this.prizePool = prizePool;
    }

    public String toString(){
        return this.name;
    }


    private class LeagueTeam{
        Team team;
        int seriesWins;
        int seriesLosses;
        int gameWins;
        int gameLosses;

        ArrayList<Integer> timesPlayed;

        boolean playedThisWeek;

        public LeagueTeam(Team team){
            this.team = team;
            seriesWins = 0;
            seriesLosses = 0;
            gameWins = 0;
            gameLosses = 0;
            playedThisWeek = false;
        }

        public Team getTeam() {
            return team;
        }

        public void setTeam(Team team) {
            this.team = team;
        }

        public int getSeriesWins() {
            return seriesWins;
        }

        public void setSeriesWins(int seriesWins) {
            this.seriesWins = seriesWins;
        }

        public int getSeriesLosses() {
            return seriesLosses;
        }

        public void setSeriesLosses(int seriesLosses) {
            this.seriesLosses = seriesLosses;
        }

        public int getGameWins() {
            return gameWins;
        }

        public void setGameWins(int gameWins) {
            this.gameWins = gameWins;
        }

        public int getGameLosses() {
            return gameLosses;
        }

        public void setGameLosses(int gameLosses) {
            this.gameLosses = gameLosses;
        }
    }
    private class LeagueResults{
        Team[] standings;

        public LeagueResults(ArrayList<LeagueTeam> result){
            this.standings = new Team[16];
            for (LeagueTeam team : result){
                standings[result.indexOf(team)] = team.getTeam();
            }
        }

    }



}
