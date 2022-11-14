package world;

import java.util.ArrayList;

public class Team {

    ArrayList<Player> players;
    Organization sponsor;
    String name;

    String tag;
    double balance;
    double prizeMoneyY;
    int majorsWon;
    ArrayList<Integer> resultsY;

    public Team(String n,String t){
        name = n;
        tag = t;
        balance = 0;
        prizeMoneyY = 0;
        majorsWon = 0;
        players = new ArrayList<>();
        resultsY = new ArrayList<>();
    }

    public void signPlayer(Player p){
        players.add(p);
        p.setTeam(this);
        System.out.println(name + " have signed " + p);
    }

    public Player dropPlayer(Player p){
        players.remove(p);
        System.out.println(p + " has been dropped by " + name);
        p.setTeam(null);
        return p;
    }

    public void signSponsor(Organization s){
        sponsor = s;
        System.out.println(name + " have been picked up by " + sponsor.toString());
        name = sponsor.toString();
    }

    public String getTag(){
        return tag;
    }

    public ArrayList<Player> getRoster(){
        return players;
    }

    public int getRosterSize(){
        return players.size();
    }
    public String toString(){
        return name;
    }


}
