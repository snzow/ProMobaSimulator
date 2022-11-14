package world;

import game.Game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

import static java.util.Comparator.reverseOrder;

public class World {

    static ArrayList<Player> allPlayers;
    static ArrayList<Player> freeAgents;
    static ArrayList<Team> teams;

    public static void main(String[] args) {
        freeAgents = initializeFreeAgents();
        teams = initializeTeams();
        Tournament tn = new Tournament("The International",500, 5000000);
        Scanner kb = new Scanner(System.in);
        while(true) {
            System.out.println("Menu");
            System.out.println("1. Play Game");
            System.out.println("2. Run Free Agency");
            int inp = kb.nextInt();
            if (inp == 1) {
                tn.runTournament(teams);
            }
            else if (inp == 2) {
                System.out.println(teams.size());
                for (int i = 0; i < teams.size(); i++) {
                    for (int j = 0; j < teams.get(i).getRosterSize(); j++) {
                        Player p = teams.get(i).players.get(j);
                        if (p.netPerformance < -10) {
                            freeAgents.add(teams.get(i).dropPlayer(p));
                            p.netPerformance = 0;
                        }
                    }
                }
                int teamsFull = 0;
                boolean fa = true;
                int looped = 0;
                while (fa) {
                    boolean something = false;
                    for (int i = 0; i < teams.size(); i++) {
                        if (teams.get(i).getRosterSize() == 5){
                            teamsFull++;
                        }
                        else{
                            teams.get(i).signPlayer(freeAgents.remove(0));
                        }
                    }
                    if (teamsFull == teams.size()){
                        fa = false;
                    }
                    looped++;
                    if (looped == 30){
                        fa = false;
                    }
                }

                System.out.println("free agency complete");
            }
            else if (inp == 3){
                System.out.println("Year end awards");
                System.out.println("Best Players:");
                allPlayers.sort(Comparator.comparing(Player::getNetPerf,reverseOrder()));
                for(int i = 0; i < 10; i++){
                    Player p = allPlayers.get(i);
                    System.out.println( (i + 1) + ". " + p + " " + p.getNetPerf() + " " + p.getSkill());
                }
                System.out.println("Worst Players:");
                allPlayers.sort(Comparator.comparing(Player::getNetPerf));
                for(int i = 0; i < 10; i++){
                    Player p = allPlayers.get(i);
                    System.out.println( (i + 1) + ". " + p + " " + p.getNetPerf() + " " + p.getSkill());
                }
            }
        }

    }


    public static ArrayList<Player> initializeFreeAgents(){
        ArrayList<Player> playerList = new ArrayList<Player>();
        playerList.add(new Player("AodhaN"));
        playerList.add(new Player("Fiyah"));
        playerList.add(new Player("Malco85"));
        playerList.add(new Player("charfra"));
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
        System.out.println(tmp.size() + " teams initialized");
        return tmp;
    }
    public static int getRandomNumber(int min, int max){
        return (int)Math.floor(Math.random()*(max-min+1)+min);
    }
}
