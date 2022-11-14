package world;

import game.Game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

import static java.util.Comparator.*;

public class World {

    static ArrayList<Player> allPlayers;
    static ArrayList<Player> freeAgents;
    static ArrayList<Team> teams;

    static ArrayList<Tournament> events;

    public static void main(String[] args) {
        freeAgents = initializeFreeAgents();
        teams = initializeTeams();
        events = initializeTournaments();
        Scanner kb = new Scanner(System.in);
        runFreeAgency();
        int seasonProg = 0;
        while(true) {
            System.out.println("Menu");
            System.out.println("1. Play Next Event");
            System.out.println("2. Show Standings");
            int inp = kb.nextInt();
            if (inp == 1) {
                events.get(seasonProg).runTournament(teams);
                seasonProg++;
                if (seasonProg == 3){
                    seasonProg = 0;
                    runFreeAgency();
                }
            }
            else if (inp == 2){
                showStandingsAwards();
            }
        }

    }


    public static void runFreeAgency(){
        showStandingsAwards();
        for(Team t : teams){
            t.tickYear();
        }
        for (Player p : allPlayers){
            if(p.getTeam() != null){
                int mod = p.getNetPerf() * 3;
                int floor = -75 + mod;
                int ceiling = 75 + mod;
                p.updateSkill(getRandomNumber(floor,ceiling));
            }
            else{
                p.updateSkill(getRandomNumber(-150,150));
            }
        }
        for (int i = 0; i < teams.size(); i++) {
            for (int j = 0; j < teams.get(i).getRosterSize(); j++) {
                Player p = teams.get(i).players.get(j);
                if (p.netPerformance < -10) {
                    teams.get(i).dropPlayer(p);
                    p.netPerformance = 0;
                }
            }
        }
        int teamsFull = 0;
        boolean fa = true;
        int looped = 0;
        freeAgents.sort(Comparator.comparing(Player::getSkill,reverseOrder()));
        while (fa) {
            boolean something = false;
            for (int i = 0; i < teams.size(); i++) {
                if (teams.get(i).getRosterSize() == 5){
                    teamsFull++;
                }
                else{
                    int cur = 0;
                    while(freeAgents.get(cur).getPrevTeam().equals(teams.get(i))){
                        cur++;
                    }
                    teams.get(i).signPlayer(freeAgents.remove(cur));

                }
            }
            if (teamsFull == teams.size()){
                fa = false;
            }
            looped++;
            if (looped == freeAgents.size()){
                fa = false;
            }
        }
        System.out.println("free agency complete");
    }

    public static void showStandingsAwards(){
        System.out.println("Year end awards");
        System.out.println("World Ranking Players");
        allPlayers.sort(Comparator.comparing(Player::getNetPerf,reverseOrder()));
        for(int i = 0; i < 10; i++){
            Player p = allPlayers.get(i);
            System.out.println( (i + 1) + ". " + p);
        }
        System.out.println("World Rankings Teams");
        teams.sort(Comparator.comparing(Team::getPoints,reverseOrder()));
        for(int i = 0; i < teams.size(); i++){
            Team t = teams.get(i);
            System.out.println( (i + 1) + ". " + t + " | " + t.getPoints());
        }
    }
    public static ArrayList<Player> initializeFreeAgents(){
        ArrayList<Player> playerList = new ArrayList<Player>();
        playerList.add(new Player("AodhaN",1500,22));
        playerList.add(new Player("Fiyah",1500,22));
        playerList.add(new Player("Malco85",1500,19));
        playerList.add(new Player("charfra",1500,22));
        playerList.add(new Player("SGod"));
        playerList.add(new Player("Mysterio101"));
        playerList.add(new Player("Iansanity"));
        playerList.add(new Player("xyz"));
        playerList.add(new Player("Trix"));
        playerList.add(new Player("mizeR"));
        playerList.add(new Player("Tesi"));
        playerList.add(new Player("simz"));
        playerList.add(new Player("x5"));
        playerList.add(new Player("woi9"));
        playerList.add(new Player("candyy"));
        playerList.add(new Player("dzst"));
        playerList.add(new Player("sync"));
        playerList.add(new Player("FleX"));
        playerList.add(new Player("twY"));
        playerList.add(new Player("siZZ"));
        playerList.add(new Player("xts"));
        playerList.add(new Player("ClouD"));
        playerList.add(new Player("Stylez"));
        playerList.add(new Player("escroW"));
        playerList.add(new Player("woqs"));
        playerList.add(new Player("DELETiON"));
        playerList.add(new Player("AzRes"));
        playerList.add(new Player("lights"));
        playerList.add(new Player("flowSTATE"));
        playerList.add(new Player("mux"));
        playerList.add(new Player("blaST"));
        playerList.add(new Player("tranqs"));
        playerList.add(new Player("henry"));
        playerList.add(new Player("kR"));
        playerList.add(new Player("zipsY"));
        playerList.add(new Player("space"));
        playerList.add(new Player("AXIS"));
        playerList.add(new Player("zitrus"));
        playerList.add(new Player("skies"));
        playerList.add(new Player("YunG_G0D"));
        playerList.add(new Player("MasterminD"));
        playerList.add(new Player("dc-"));
        playerList.add(new Player("nylon"));
        playerList.add(new Player("taLismaN"));
        playerList.add(new Player("shoutout"));
        playerList.add(new Player("mornE"));
        playerList.add(new Player("NatuRaL"));
        playerList.add(new Player("gHost"));
        playerList.add(new Player("KuZo"));
        playerList.add(new Player("dampur"));
        playerList.add(new Player("task"));
        playerList.add(new Player("44atlas"));
        playerList.add(new Player("honoR"));
        playerList.add(new Player("glimpses"));
        playerList.add(new Player("pq"));
        playerList.add(new Player("exc"));
        playerList.add(new Player("mesk"));
        playerList.add(new Player("emerald"));
        playerList.add(new Player("else"));
        playerList.add(new Player("Kcin"));
        playerList.add(new Player("cory"));
        playerList.add(new Player("aVoS"));
        playerList.add(new Player("qrtrmstr"));
        playerList.add(new Player("bozza"));
        playerList.add(new Player("vic"));
        playerList.add(new Player("MeowW"));
        playerList.add(new Player("patience"));
        playerList.add(new Player("tsper"));

        allPlayers = new ArrayList<Player>();
        for (Player p : playerList){
            allPlayers.add(p);
        }
        System.out.println(playerList.size() + " free agents initialized");
        return playerList;
    }

    public static ArrayList<Team> initializeTeams(){
        ArrayList<Team> tmp = new ArrayList<>();
        tmp.add(new Team("Cloud9","C9"));
        tmp.add(new Team("Team Liquid","Liquid"));
        tmp.add(new Team("Tundra","Tundra"));
        tmp.add(new Team("Team Solo Mid","TSM"));
        tmp.add(new Team("LGD","LGD"));
        tmp.add(new Team("Counter Logic Gaming","CLG"));
        tmp.add(new Team("Team Secret", "Secret"));
        tmp.add(new Team("OG","OG"));
        System.out.println(tmp.size() + " teams initialized");
        return tmp;
    }

    public static ArrayList<Tournament> initializeTournaments(){
        ArrayList<Tournament> tmp = new ArrayList<>();
        tmp.add(new Tournament("Season Open Invitational",1000000, 1000));
        tmp.add(new Tournament("Mid-Season Invitational",1000000, 1000));
        tmp.add(new Tournament("The International",4000000, 4000));
        return tmp;
    }
    public static int getRandomNumber(int min, int max){
        return (int)Math.floor(Math.random()*(max-min+1)+min);
    }
}
