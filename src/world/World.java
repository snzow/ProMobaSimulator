package world;

import game.GameVersion;
import game.Hero;
import game.LadderGame;

import java.io.*;
import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.*;

import static java.util.Comparator.*;

public class World {

    static Map<String,Player> playerMap;
    static ArrayList<Player> playerList;
    static ArrayList<Player> freeAgents;
    static ArrayList<Team> teams;

    static Map<String, Team> teamMap;

    static Map<String, Tournament> tournamentMap;
    static ArrayList<Tournament> events;

    public static ArrayList<Hero> heroList;

    static Map<Hero,Double> heroMap;

    static Team FREE_AGENT = new Team("Free Agent", "FA");
    public static Team RADIANT = new Team("Radiant", "");
    public static Team DIRE = new Team("Dire", "");

    static GameVersion patch;

    public static ArrayList<Player> matchmakingPool;


    public static void main(String[] args) throws IOException {
        playerMap = new HashMap<>();
        initializeHeroes();
        initializeFreeAgents();
        initializeTeams();
        initializeTournaments();
        matchmakingPool = (ArrayList<Player>) playerList.clone();
        patch = new GameVersion();
        Scanner kb = new Scanner(System.in);
        runFreeAgency(true);
        int seasonProg = 0;
        boolean patchThisYear = false;
        while(true) {
            System.out.println("----------");
            System.out.println("Menu");
            System.out.println("1. Play Next Event");
            System.out.println("2. Show Standings");
            System.out.println("3. Show Player History");
            System.out.println("4. Show Team History");
            System.out.println("5. Show Tournament History");
            System.out.println("6. Play Ladder Game");
            System.out.println("----------");
            int inp = kb.nextInt();
            LadderGame ladderGame = new LadderGame();
            if (inp == 1) {
                if (seasonProg == events.size()){
                    seasonProg = 0;
                    runFreeAgency(false);
                    patchThisYear= false;
                    continue;
                }
                if(seasonProg == events.size()/2 && !patchThisYear){
                    patchHeroes();
                    patchThisYear = true;
                    continue;
                }
                events.get(seasonProg).runTournament(teams);
                seasonProg++;
                playerList.sort(Comparator.comparing(Player::getMmr,reverseOrder()));
                for(int i = 0; i < 10; i++){
                    while (matchmakingPool.size() >= 10){
                        ladderGame.playGame("x",1);
                    }
                    matchmakingPool = (ArrayList<Player>) playerList.clone();
                }


            }
            else if (inp == 2){
                showStandingsAwards(false);
            }
            else if (inp == 3){
                System.out.println("enter player to show history of:");
                String tp = kb.next();
                Player tmp = playerMap.get(tp);
                if (tmp != null){
                    tmp.printHistory();
                }
                else{
                    System.out.println("Player Not Found");
                }

            }
            else if (inp == 4){
                System.out.println("enter team to show history of:");
                kb.nextLine();
                String tp = kb.nextLine();
                Team tmp = teamMap.get(tp);
                if (tmp != null){
                    tmp.printHistory();
                }
                else{
                    System.out.println("Tournament Not Found");
                }
            }
            else if (inp == 5){
                System.out.println("enter tournament to show history of:");
                kb.nextLine();
                String tp = kb.nextLine();
                Tournament tmp = tournamentMap.get(tp);
                if (tmp != null){
                    tmp.printTournamentHistory();
                }
                else{
                    System.out.println("Tournament Not Found");
                }
            }
            else if (inp == 6){
                ladderGame.playGame("print");
            }
        }

    }

    public static ArrayList<Player> getFreeAgents(){
        return (ArrayList<Player>) freeAgents.clone();
    }
    /**
     * runs the free agency process, meant to be done at the end of the year
     */
    public static void runFreeAgency(boolean firstTime){
        ArrayList<Player> signed = new ArrayList<>();
        showStandingsAwards(true);
        teams.sort(Comparator.comparing(Team::getPoints,reverseOrder()));
        for (Player p : playerList){
            p.yearEndStats();
        }
        for(Team t : teams){
            t.setWorldRanking(teams.indexOf(t) + 1);
            t.tickYear();
        }
        //changes player skill
        for (Player p : playerList){
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
        for (Team team : teams) {
            for (int j = 0; j < team.getRosterSize(); j++) {
                Player p = team.players.get(j);
            }
        }
        freeAgents.sort(Comparator.comparing(Player::getMmr,reverseOrder()));
        teams.sort(Comparator.comparing(Team::getPoints,reverseOrder()));
        @SuppressWarnings("unchecked")
        ArrayList<Team> teamsToFA = (ArrayList<Team>) teams.clone();
        System.out.println("---Top Free Agents---");
        for(int i = 0; i < 5; i++){
            System.out.println((i+1)+ ". " + freeAgents.get(i));
        }
        System.out.println("----------");
        if(!firstTime) {
            while (teamsToFA.size() > 0) {
                Team t = teamsToFA.get(0);
                if (t.isFull()) {
                    teamsToFA.remove(t);
                } else {
                    int cur = 0;
                    while (freeAgents.get(cur).getPrevTeam().equals(t)) {
                        cur++;
                    }
                    t.signPlayer(freeAgents.get(cur));
                    signed.add(freeAgents.remove(cur));
                }
                teamsToFA.sort(Comparator.comparing(Team::getContractOffer, reverseOrder()));
            }
        }
        else{
            while(teamsToFA.size() > 0){
                for(int i = 0; i < teamsToFA.size(); i++){
                    Team t = teamsToFA.get(i);
                    if(t.isFull()){
                        teamsToFA.remove(t);
                        i--;
                    }
                    else{
                        t.signPlayer(freeAgents.get(0));
                        signed.add(freeAgents.remove(0));
                    }
                }
            }
        }
        teams.sort(Comparator.comparing(Team::getPoints,reverseOrder()));
        for(Team t : teams){
            if(teams.indexOf(t) <= 10){
                t.printFA();
                t.printRoster();
            }
            t.clearFA();
        }
        for(Player p : freeAgents){
            p.setPrevTeam(FREE_AGENT);
        }
        System.out.println("---Rookies For Coming Season---");
        for(Player p : signed){
            if(!firstTime){
                if(!p.playedProfessionally){
                    System.out.println(p.getName() + " -> " + p.getTeam().getTag() + " | MMR: " + p.getMmr());
                }
            }
        }
    }

    /**
     * shows the top 10 ranked players by netperformance and top 10 teams by points
     */
    public static void showStandingsAwards(boolean yearEnd){
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        playerList.sort(Comparator.comparing(Player::getNetPerf,reverseOrder()));
        int playersToList = 25;
        if(yearEnd){
            playersToList = 10;
            System.out.println("----------------");
            System.out.println("Year end awards");
        }
        System.out.println("Top " + playersToList + " players");
        System.out.println("----------------");
        for(int i = 0; i < playersToList; i++){
            Player p = playerList.get(i);System.out.println( (i + 1) + ". " + p);
            }
        System.out.println("----------------");
        System.out.println("Global MMR Leaderboards");
        System.out.println("----------------");
        playerList.sort(Comparator.comparing(Player::getMmr, reverseOrder()));
        for(int i = 0; i < playersToList; i++){
            Player p = playerList.get(i);
            p.setGlobalMmrRank(playerList.indexOf(p) + 1);
            System.out.println( (i + 1) + ". " + p + " | " + p.getMmr());
        }
        System.out.println("----------------");
        System.out.println("Unsigned MMR Leaderboards");
        System.out.println("----------------");
        freeAgents.sort(Comparator.comparing(Player::getMmr, reverseOrder()));
        for(int i = 0; i < playersToList; i++){
            Player p = freeAgents.get(i);
            p.setGlobalMmrRank(playerList.indexOf(p) + 1);
            System.out.println( "(Rank " + p.getGlobalMmrRank() + ") " + p + " | " + p.getMmr());
        }
        System.out.println("----------------");
        System.out.println("Team Rankings | Tournaments Won (Majors won) | Team Balance");
        System.out.println("----------------");
        if(yearEnd){
            teams.sort(Comparator.comparing(Team::getPoints,reverseOrder()));
        }
        else{
            teams.sort(Comparator.comparing(Team::getBalance,reverseOrder()));
        }
        if(yearEnd){
            for(int i = 0; i < 10; i++){
                Team t = teams.get(i);
                System.out.println( (i + 1) + ". " + t + " | " + t.getTourneysWon(0) + "(" +
                        t.getMajorsWon(0) + ") | " + formatter.format(t.getBalance()));
            }
        }
        else{
            for(int i = 0; i < teams.size(); i++){
                Team t = teams.get(i);
                System.out.println( (i + 1) + ". " + t + " | " + t.getTourneysWon(1) +
                        "(" + t.getMajorsWon(1) + ") | " + formatter.format(t.getBalance()));
            }
        }
    }

    public static void patchHeroes(){

        int heroListSize = heroList.size();
        patch.incrementGameVersion();
        System.out.println("---Major Patch---");
        heroList.sort(Comparator.comparing(Hero::getMetaStrength,Comparator.reverseOrder()));
        System.out.println("Version " + patch.toString());
        System.out.println("---Previous Version Strongest Heroes---");
        double patchMetaStrengthMax = 0;
        double patchMetaStrengthMin = 10000000;
        for (int i = 0; i < heroListSize; i++){
            if (heroList.get(i).getMetaStrength() > patchMetaStrengthMax){
                patchMetaStrengthMax = heroList.get(i).getMetaStrength();
            }
            else if (heroList.get(i).getMetaStrength() < patchMetaStrengthMin){
                patchMetaStrengthMin = heroList.get(i).getMetaStrength();
            }

        }
        for(int i = 0; i < 5; i++){
            System.out.println((i+1) + ". " + heroList.get(i).toStringAllStats());
        }
        /*heroList.sort(Comparator.comparing(Hero::getPicks));
        System.out.println("---Previous Version Least Picked---");
        for (int i = 0; i < 5; i++){
            System.out.println((i+1) + ". " + heroList.get(i).toString());
        }
        heroList.sort(Comparator.comparing(Hero::getMetaStrength,Comparator.reverseOrder()));
         */
        for(Hero h : heroList){
            h.patchHero(patchMetaStrengthMin,patchMetaStrengthMax);
        }
        System.out.println("---New Patch Expected Top Heroes---");
        heroList.sort(Comparator.comparing(Hero::getStrength,Comparator.reverseOrder()));
        for (int i = 0; i < 5; i++){
            System.out.println((i+1) + ". " + heroList.get(i).toStringNoWinrate());
        }
    }

    public static ArrayList<Player> getPlayerList(){
        return (ArrayList<Player>) playerList.clone();
    }
    public static void initializeTeams() throws IOException {
        teams = new ArrayList<>();
        teamMap = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader("src/TeamNames.txt"));
        String toName;
        while( (toName = br.readLine()) != null) {
            String tag = br.readLine();
            createTeam(toName,tag);
        }
        System.out.println(teams.size() + " teams initialized");
    }

    public static void initializeTournaments(){
        events = new ArrayList<>();
        tournamentMap = new HashMap<>();
        createTournament("Xenon Grand Opening", 1000000,2000,true);
        createTournament("Nvidia Early Days",100000,1000,false);
        createTournament("Season Open Minor",50000,500,false);
        createTournament("PDL Boston",100000,1000,false);
        createTournament("Winter Major",400000, 2000,true);
        createTournament("Razer ProSeries Finals",100000,1000,false);
        createTournament("Mid-Season Minor",50000,500,false);
        createTournament("Spring Major",400000, 2000,true);
        createTournament("PerfectWorld Beijing",100000,1000,false);
        createTournament("PDL Kyiv",100000,1000,false);
        createTournament("Season End Minor",50000,500,false);
        createTournament("The International",3000000, 4000,true);

    }

    public static void initializeHeroes(){
        heroList = new ArrayList<>();
        heroMap = new HashMap<>();
        //1-5
        ch("Harbinger");
        ch("Portal Keeper");
        ch("Behemoth");
        ch("Stormhunter");
        ch("Smoke Wraith");
        //6-10
        ch("Gambler");
        ch("Monarch");
        ch("Crystal Scholar");
        ch("Sorceress");
        ch("Twilight Demon");
        //11-15
        ch("Piper");
        ch("Blood Mage");
        ch("Elder Knight");
        ch("Enforcer");
        ch("Scrivener");
        //16-20
        ch("Ghost Merchant");
        ch("River Guardian");
        ch("Warden of the Grove");
        ch("Gatherer");
        ch("Spirit of Calamity");
        //21-25
        ch("Water Spirit");
        ch("Fire Spirit");
        ch("Air Spirit");
        ch("Earth Spirit");
        ch("Forgotten Toy");
    }

    private static void ch(String name){
        createHero(name);
    }
    private static void createHero(String name){
        Hero tmp = new Hero(name);
        heroList.add(tmp);
        heroMap.put(tmp,1.0);
    }

    public static int getRandomNumber(int min, int max){
        return (int)Math.floor(Math.random()*(max-min+1)+min);
    }

    public static void createPlayer(String s){
        Player tmp = new Player(s);
        playerMap.put(s,tmp);
        playerList.add(tmp);
        freeAgents.add(tmp);
    }

    public static void createTeam(String n, String t){
        Team tmp = new Team(n,t);
        teams.add(tmp);
        teamMap.put(n,tmp);
    }

    public static void createTournament(String s, int pr, int po, boolean maj){
        Tournament tmp = new Tournament(s,pr,po,maj);
        events.add(tmp);
        tournamentMap.put(s,tmp);
    }
    public static void initializeFreeAgents() throws IOException {
        freeAgents = new ArrayList<>();
        playerList = new ArrayList<>();
        //1-5
        Player tmp = new Player("AodhaN",1500,22);
        playerMap.put("Aodhan",tmp);
        freeAgents.add(tmp);
        playerList.add(tmp);
        tmp =  new Player("Fiyah",1500,22);
        playerMap.put("Fiyah",tmp);
        playerList.add(tmp);
        freeAgents.add(tmp);
        tmp = new Player("Malco85",1500,19);
        playerMap.put("Malco85",tmp);
        playerList.add(tmp);
        freeAgents.add(tmp);
        tmp = new Player("charfra",1500,22);
        playerMap.put("charfra",tmp);
        playerList.add(tmp);
        freeAgents.add(tmp);
        tmp = new Player("Mysterio",1500,22);
        playerList.add(tmp);
        freeAgents.add(tmp);

        BufferedReader br = new BufferedReader(new FileReader("src/PlayerNames.txt"));
        String toName;
       while( (toName = br.readLine()) != null) {
            createPlayer(toName);
        }
        System.out.println(playerList.size());

    }
}
