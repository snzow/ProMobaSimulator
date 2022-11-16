package world;

import java.util.*;

import static java.util.Comparator.*;

public class World {

    static Map<String,Player> allPlayers;
    static ArrayList<Player> playerList;
    static ArrayList<Player> freeAgents;
    static ArrayList<Team> teams;

    static ArrayList<Tournament> events;

    public static void main(String[] args) {
        allPlayers = new HashMap<>();
        initializeFreeAgents();
        initializeTeams();
        initializeTournaments();
        Scanner kb = new Scanner(System.in);
        runFreeAgency();
        int seasonProg = 0;
        while(true) {
            System.out.println("----------");
            System.out.println("Menu");
            System.out.println("1. Play Next Event");
            System.out.println("2. Show Standings");
            System.out.println("3. Show Player History");
            System.out.println("----------");
            int inp = kb.nextInt();
            if (inp == 1) {
                events.get(seasonProg).runTournament(teams);
                seasonProg++;
                if (seasonProg == events.size()){
                    seasonProg = 0;
                    runFreeAgency();
                }
            }
            else if (inp == 2){
                showStandingsAwards();
            }
            else if (inp == 3){
                System.out.println("enter player to show history of:");
                String tp = kb.next();
                Player tmp = allPlayers.get(tp);
                if (tmp != null){
                    tmp.printHistory();
                }
                else{
                    System.out.println("Player Not Found");
                }

            }
            else if (inp == 4){
                break;
            }
        }

    }

    /**
     * runs the free agency process, meant to be done at the end of the year
     */
    public static void runFreeAgency(){
        showStandingsAwards();
        for (Player p : playerList){
            p.yearEndStats();
        }
        for(Team t : teams){
            t.tickYear();
        }
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
                if (p.netPerformance < -10) {
                    team.dropPlayer(p);
                    p.netPerformance = 0;
                }
            }
        }
        freeAgents.sort(Comparator.comparing(Player::getSkill,reverseOrder()));
        teams.sort(Comparator.comparing(Team::getPoints,reverseOrder()));
        @SuppressWarnings("unchecked")
        ArrayList<Team> teamsToFA = (ArrayList<Team>) teams.clone();

        while (teamsToFA.size() > 0) {
            for (int i = 0; i < teamsToFA.size(); i++) {
                Team t = teamsToFA.get(i);
                if(t.isFull()){
                    teamsToFA.remove(t);
                    i--;
                }
                else {
                    int cur = 0;
                    while (freeAgents.get(cur).getPrevTeam().equals(t)) {
                        cur++;
                    }
                    t.signPlayer(freeAgents.remove(cur));
                }
            }
        }
        for(Team t : teams){
            t.printRoster();
        }
        System.out.println("free agency complete");
    }

    /**
     * shows the top 10 ranked players by netperformance and top 10 teams by points
     */
    public static void showStandingsAwards(){
        System.out.println("Year end awards");
        System.out.println("Player Rankings");
        playerList.sort(Comparator.comparing(Player::getNetPerf,reverseOrder()));
        for(int i = 0; i < 10; i++){
            Player p = playerList.get(i);
            System.out.println( (i + 1) + ". " + p);
        }
        System.out.println("Team Rankings | Tournaments Won (Majors won)");
        teams.sort(Comparator.comparing(Team::getPoints,reverseOrder()));
        for(int i = 0; i < 10; i++){
            Team t = teams.get(i);
            System.out.println( (i + 1) + ". " + t + " | " + t.tourneysWonY + "(" + t.majorsWonY + ")");
        }
    }
    public static void initializeFreeAgents(){
        freeAgents = new ArrayList<>();
        playerList = new ArrayList<>();
        //1-5
        Player tmp = new Player("AodhaN",1500,22);
        allPlayers.put("Aodhan",tmp);
        freeAgents.add(tmp);
        playerList.add(tmp);
        tmp =  new Player("Fiyah",1500,22);
        allPlayers.put("Fiyah",tmp);
        playerList.add(tmp);
        freeAgents.add(tmp);
        tmp = new Player("Malco85",1500,19);
        allPlayers.put("Malco85",tmp);
        playerList.add(tmp);
        freeAgents.add(tmp);
        tmp = new Player("charfra",1500,22);
        allPlayers.put("charfra",tmp);
        playerList.add(tmp);
        freeAgents.add(tmp);
        createPlayer("SGod");
        //6-10
       createPlayer("Mysterio101");
       createPlayer("Iansanity");
       createPlayer("xyz");
       createPlayer("Trix");
       createPlayer("mizeR");
        //11-15
       createPlayer("Tesi");
       createPlayer("simz");
       createPlayer("x5");
       createPlayer("woi9");
       createPlayer("candyy");
        //16-20
       createPlayer("dzst");
       createPlayer("sync");
       createPlayer("FleX");
       createPlayer("twY");
       createPlayer("siZZ");
        //21-25
       createPlayer("xts");
       createPlayer("ClouD");
       createPlayer("Stylez");
       createPlayer("escroW");
       createPlayer("woqs");
        //26-30
       createPlayer("DELETiON");
       createPlayer("AzRes");
       createPlayer("lights");
       createPlayer("flowSTATE");
       createPlayer("mux");
        //31-35
       createPlayer("blaST");
       createPlayer("tranqs");
       createPlayer("henry");
       createPlayer("kR");
       createPlayer("zipsY");
        //36-40
       createPlayer("space");
       createPlayer("AXIS");
       createPlayer("zitrus");
       createPlayer("skies");
       createPlayer("YunG_G0D");
        //41-45
       createPlayer("MasterminD");
       createPlayer("dc-");
       createPlayer("nylon");
       createPlayer("taLismaN");
       createPlayer("shoutout");
        //46-50
       createPlayer("mornE");
       createPlayer("NatuRaL");
       createPlayer("gHost");
       createPlayer("KuZo");
       createPlayer("dampur");
        //51-55
       createPlayer("task");
       createPlayer("44atlas");
       createPlayer("honoR");
       createPlayer("glimpses");
       createPlayer("pq");
        //56-60
       createPlayer("exc");
       createPlayer("mesk");
       createPlayer("emerald");
       createPlayer("else");
       createPlayer("Kcin");
        //61-65
       createPlayer("cory");
       createPlayer("aVoS");
       createPlayer("qrtrmstr");
       createPlayer("bozza");
       createPlayer("vic");
        //66-70
       createPlayer("MeowW");
       createPlayer("patience");
       createPlayer("tsper");
       createPlayer("tsp");
       createPlayer("cosmo");
       //71-75
        createPlayer("tootsie");
        createPlayer("caverN");
        createPlayer("SpaceMEX");
        createPlayer("TransLatoR");
        createPlayer("Spam");
        //76-80
        createPlayer("ChrisG");
        createPlayer("placebo");
        createPlayer("pauSe");
        createPlayer("Smirrn");
        createPlayer("curzos");
        //81-85
        createPlayer("SmolensK");
        createPlayer("durs");
        createPlayer("prl");
        createPlayer("Z");
        createPlayer("Sparrow");
        //86-90
        createPlayer("con");
        createPlayer("smirk");
        createPlayer("peezy");
        createPlayer("more4");
        createPlayer("zip");
        //91-95
        createPlayer("flipflop");
        createPlayer("FireStorm");
        createPlayer("woof");
        createPlayer("planZ");
        createPlayer("spankr");
        //96-100
        createPlayer("bossy");
        createPlayer("metriculate");
        createPlayer("op");
        createPlayer("tr");
        createPlayer("15");
        //101-105
        createPlayer("SmackerZ");
        createPlayer("Study");
        createPlayer("0mega");
        createPlayer("smolladolla");
        createPlayer("routines");
        //106-110
        createPlayer("dozer");
        createPlayer("slowtoxic");
        createPlayer("dreemz");
        createPlayer("resQ");
        createPlayer("@1");
        //111-115
        createPlayer("zone");
        createPlayer("Ricky");
        createPlayer("scoooba");
        createPlayer("oez");
        createPlayer("woah");
        //116-120
        createPlayer("brekfest");
        createPlayer("plns");
        createPlayer("oddity");
        createPlayer("normal");
        createPlayer("goose");
        //121-125
        createPlayer("dawning");
        createPlayer("wreckeR");
        createPlayer("HardCarryOnly");
        createPlayer("benzboy");
        createPlayer("VooDoo");
    }


    public static void initializeTeams(){
        teams = new ArrayList<>();
        //1-4
        createTeam("Cloud9","C9");
        createTeam("Team Liquid","Liquid");
        createTeam("Tundra","Tundra");
        createTeam("Team Solo Mid","TSM");
        //5-8
        createTeam("LGD","LGD");
        createTeam("Counter Logic Gaming","CLG");
        createTeam("Team Secret", "Secret");
        createTeam("OG","OG");
        //9-12
        createTeam("OpTic Gaming", "OpTic");
        createTeam("Beastcoast","bc");
        createTeam("FaZe Clan", "FaZe");
        createTeam("NRG eSports", "NRG");
        //13-16
        createTeam("G2 eSports", "G2");
        createTeam("FURIA", "FURIA");
        createTeam("Fnatic", "fnc");
        createTeam("Natus Vincere", "Na'Vi");
        //17-20
        createTeam("100 Thieves", "100T");
        createTeam("T1", "T1");
        createTeam("Alliance","[A]");
        createTeam("Team Spirit","Spirit");

        System.out.println(teams.size() + " teams initialized");
    }

    public static void initializeTournaments(){
        events = new ArrayList<>();
        createTournament("Season Open Invitational",500000, 1000,true);
        createTournament("Season Open Minor",100000,500,false);
        createTournament("Regional Tournament",150000,800,false);
        createTournament("Mid-Season Invitational",500000, 1000,true);
        createTournament("Regional Tournament",150000,800,false);
        createTournament("Mid-Season Minor",100000,500,false);
        createTournament("Regional Tournament",150000,800,false);
        createTournament("Regional Tournament",150000,800,false);
        createTournament("Season End Minor",100000,500,false);
        createTournament("The International",1000000, 2000,true);

    }

    public static int getRandomNumber(int min, int max){
        return (int)Math.floor(Math.random()*(max-min+1)+min);
    }

    public static void createPlayer(String s){
        Player tmp = new Player(s);
        allPlayers.put(s,tmp);
        playerList.add(tmp);
        freeAgents.add(tmp);
    }

    public static void createTeam(String n, String t){
        Team tmp = new Team(n,t);
        teams.add(tmp);
    }

    public static void createTournament(String s, int pr, int po, boolean maj){
        events.add(new Tournament(s,pr,po,maj));
    }
}
