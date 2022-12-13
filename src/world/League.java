package world;

import game.Game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class League {
    ArrayList<LeagueTeam> teamList;
    ArrayList<League.LeagueResults> pastResults;
    String name;
    double prizePool;
    double pointsPool;
    boolean major;

    int week;

    HashMap<Team,LeagueTeam> teamMap;

    ArrayList<LeagueTeam> homeTeams;

    ArrayList<LeagueTeam> awayTeams;

    HashMap<Team,Integer> gamesPlayed;


    public League(String name, double prizePool, double pointsPool, boolean major) {
        this.name = name;
        this.prizePool = prizePool;
        this.pointsPool = pointsPool;
        this.major = major;
        week = 1;
        gamesPlayed = new HashMap<>();
        pastResults = new ArrayList<LeagueResults>();
        teamList = new ArrayList<>();
        awayTeams = new ArrayList<>();
        homeTeams = new ArrayList<>();
        teamMap = new HashMap<>();
    }

    public void startSeason(){
        for(int i = 0; i < 16; i++){
            if (i < 8){
                homeTeams.add(teamList.get(i));
            }
            else{
                awayTeams.add(teamList.get(i));
            }
        }
    }
    public void addTeam(Team team){
        LeagueTeam tmp = new LeagueTeam(team);
        teamList.add(tmp);
        teamMap.put(team,tmp);
    }

    public void playWeek(){
        if(week == 1){
            startSeason();
        }
        if(week < 25) {
            for (int i = 0; i < 8; i++) {
                Team homeTeam = homeTeams.get(i).getTeam();
                Team awayTeam = awayTeams.get(i).getTeam();
                homeTeam.receivePrizeMoney(prizePool);
                awayTeam.receivePrizeMoney(prizePool);
                Game tmp = new Game(homeTeam, awayTeam);
                Team winner = tmp.playSeries(3);
                if (winner.equals(homeTeam)) {
                    teamMap.get(homeTeam).seriesWins++;
                    teamMap.get(awayTeam).seriesLosses++;
                } else {
                    teamMap.get(awayTeam).seriesWins++;
                    teamMap.get(homeTeam).seriesLosses++;
                }
            }
            printStandings();
            awayTeams.add(homeTeams.remove(homeTeams.size() - 1));
            homeTeams.add(0, awayTeams.remove(0));
            week++;
        }
        else{
            week = 1;
        }
    }

    public ArrayList<League.LeagueResults> getPastResults() {
        return pastResults;
    }

    public void printStandings(){
        teamList.sort(Comparator.comparing(LeagueTeam::getSeriesWins,Comparator.reverseOrder()));
        for(LeagueTeam t : teamList){
            System.out.println((teamList.indexOf(t) + 1) + ". " + t.getTeam().toString() + " - " + t.getSeriesWins() + "-" + t.getSeriesLosses());
        }
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
