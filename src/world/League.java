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

    LeagueTeam defendingChamp;

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
        homeTeams = new ArrayList<>();
        awayTeams = new ArrayList<>();
        for(int i = 0; i < 16; i++){
            teamList.get(i).getTeam().setLeague(this);
            if (i < 8){
                homeTeams.add(teamList.get(i));
            }
            else{
                awayTeams.add(teamList.get(i));
            }
        }
    }

    public LeagueTeam getDefendingChamp(){
        if(defendingChamp == null){
            return null;
        }
        else{
            return defendingChamp;
        }
    }

    public ArrayList<Team> getTeams(){
        ArrayList<Team> toReturn = new ArrayList<>();
        for (LeagueTeam t : teamList){
            toReturn.add(t.getTeam());
        }
        return toReturn;
    }

    public boolean isDefendingChamp(LeagueTeam team){
        if(defendingChamp == team){
            return true;
        }
        return false;
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
        if(week < 16) {
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
    }

    public ArrayList<Team> getRelegationCandidates(){
        teamList.sort(Comparator.comparing(LeagueTeam::getSeriesWins));
        ArrayList<Team> toReturn = new ArrayList<>();
        ArrayList<LeagueTeam> toResults = (ArrayList<LeagueTeam>) toReturn.clone();
        toResults.sort(Comparator.comparing(LeagueTeam::getSeriesWins,Comparator.reverseOrder()));
        LeagueResults thisYear = new LeagueResults(toResults);
        pastResults.add(thisYear);
        for(LeagueTeam t : teamList){
            t.setSeriesLosses(0);
            t.setSeriesWins(0);
            t.getTeam().updatePoints(this.pointsPool/toResults.indexOf(t) + 1);
            if(teamList.indexOf(t) == 15){
                this.defendingChamp = t;
            }
        }
        for (int i = 0; i < 4; i ++) {
            toReturn.add(teamList.remove(i).getTeam());
        }
        week = 1;
        return toReturn;
    }

    public ArrayList<Team> getPromotionCandidates(){
        teamList.sort(Comparator.comparing(LeagueTeam::getSeriesWins,Comparator.reverseOrder()));
        ArrayList<Team> toReturn = new ArrayList<>();
        ArrayList<LeagueTeam> toResults = (ArrayList<LeagueTeam>) toReturn.clone();
        LeagueResults thisYear = new LeagueResults(toResults);
        pastResults.add(thisYear);
        for(LeagueTeam t : teamList){
            t.setSeriesLosses(0);
            t.setSeriesWins(0);
            t.getTeam().updatePoints(this.pointsPool/teamList.indexOf(t) + 1);
            if(teamList.indexOf(t) == 0){
                this.defendingChamp = t;
            }
        }
        for (int i = 0; i < 4; i ++){
            toReturn.add(teamList.remove(i).getTeam());
        }
        week = 1;
        return toReturn;
    }

    public ArrayList<League.LeagueResults> getPastResults() {
        return pastResults;
    }

    public void printStandings(){
        teamList.sort(Comparator.comparing(LeagueTeam::getSeriesWins,Comparator.reverseOrder()));
        System.out.println("---------------");
        System.out.println(name + " Standings after week " + week);
        for(LeagueTeam t : teamList){
            System.out.println((teamList.indexOf(t) + 1) + ". " + t.toString() + " - " + t.getSeriesWins() + "-" + t.getSeriesLosses());
        }
        System.out.println("---------------");
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

        public String toString(){
            String mod = "";
            if(isDefendingChamp(this) == true){
                mod = "*";
            }
            return mod + this.getTeam().name + mod;
        }
    }
    private class LeagueResults{
        Team[] standings;
        int year;

        public LeagueResults(ArrayList<LeagueTeam> result){
            this.standings = new Team[16];
            for (LeagueTeam team : result){
                standings[result.indexOf(team)] = team.getTeam();
            }
            year = World.year;
        }

    }



}
