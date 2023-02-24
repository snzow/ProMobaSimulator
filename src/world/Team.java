package world;

import java.text.NumberFormat;
import java.util.ArrayList;

public class Team {

    ArrayList<Player> players;
    ArrayList<TeamSeasonRecord> history;
    Sponsor sponsor;
    String name;

    String tag;

    League league;

    FreeAgencyInstance freeAgency;
    double balance;
    double prizeMoneyY;
    int majorsWon;
    int majorsWonY;
    int tiWon;
    int tiWonY;
    int tourneysWon;
    int tourneysWonY;

    double pointsY;
    double pointsL;

    int runningPerf;
    int wins;
    int winsY;
    int losses;
    int lossesY;
    int worldRanking;

    public Team(String n,String t){
        name = n;
        tag = t;
        balance = World.getRandomNumber(250000,500000);
        prizeMoneyY = 0;
        majorsWon = 0;
        players = new ArrayList<>();
        history = new ArrayList<>();
        pointsY = 0;
        pointsL = 0;
        runningPerf = 0;
        wins = 0;
        winsY = 0;
        losses = 0;
        lossesY = 0;
        worldRanking = 0;
        tiWon = 0;
        tiWonY = 0;
        freeAgency = new FreeAgencyInstance();
    }

    public void signPlayer(Player p){
        addSigning(p);
        p.yearsWithTeam = 0;
        players.add(p);
        //p.signContract(this, Math.min((int) (balance/9),650_000),1);
        p.signContract(this,(int)balance/20,1);

    }

    public void setLeague(League league){
        this.league = league;
    }

    public League getLeague(){
        return this.league;
    }

    public double getContractOffer(){
        return balance/7;
    }
    public void winTourney(boolean maj,boolean ti){
        tourneysWon++;
        tourneysWonY++;
        if(maj){
            majorsWon++;
            majorsWonY++;
        }
        if(ti){
            tiWon++;
            tiWonY++;
        }
    }

    public Sponsor getSponsor(){
        return sponsor;
    }

    public Player dropPlayer(Player p){
        p.cut();
        addDropped(p);
        return p;
    }

    public void updateRunningPerf(int x){
        runningPerf += x;
    }
    public int getRunningPerf(){
        return runningPerf;
    }
    public void signSponsor(Sponsor s){
        sponsor = s;
        System.out.println(name + " have been picked up by " + sponsor.toString());
        name = sponsor.toString();
    }

    public void receivePrizeMoney(double pm){
        prizeMoneyY += pm;
        balance += pm;
    }

    public void payPlayers(){
        boolean broke = false;
        for (int i = 0; i < players.size(); i++){
            Player p = players.get(i);
            p.payPlayer(p.getSalary());
            balance -= p.getSalary();
        }
        if(getBalance() < 0){
            while(players.size() > 0){
                dropPlayer(players.get(0));
            }
            broke = true;
        }
        else{
            for (int i = 0; i < players.size(); i ++){
                Player p = players.get(i);
                //makes teams make more accurate judgements on extending players
                //i.e. less likely to extend after a bad year, more after a good one
                int moddedPerf = p.getNetPerf() + getRunningPerf();
                if (moddedPerf < -10){
                    if(tiWonY == 0){
                        dropPlayer(p);
                    }


                }
                else{
                    if(p.getContractYearsRemaining() <= 0){
                        if (moddedPerf > 10){
                            p.extendContract(1.5 ,3);
                        }
                        else if(moddedPerf > 5){
                            p.extendContract(1.25,2);
                        }
                        else if(moddedPerf > 0){
                            p.extendContract(1.1,1);
                        }
                        else if (tiWonY == 0){
                            dropPlayer(p);
                        }
                        else{
                            p.extendContract(1,1);
                        }
                    }
                }

            }
        }
        if(broke == true){
            balance = 50000; //50,000
        }
        runningPerf = 0;
    }
    public int getWorldRanking(){
        return worldRanking;
    }

    public void setWorldRanking(int x){
        worldRanking = x;
    }

    public double getPoints(){
        return pointsY + pointsL;
    }

    public void updatePoints(double p){
        pointsY += p;
    }

    public void tickYear(){
        TeamSeasonRecord tmp = new TeamSeasonRecord(winsY,lossesY,prizeMoneyY);
        history.add(tmp);
        pointsL = pointsY/2;
        pointsY = 0;
        payPlayers();
        if(this.getLeague() != null && this.getLeague().major){
            this.balance -= 400_000;
        }
        else{
            this.balance -= 100_000;
        }
        tourneysWonY = 0;
        majorsWonY = 0;
        tiWonY = 0;
        winsY = 0;
        lossesY = 0;
        prizeMoneyY = 0;

    }

    public void incrementWins(){
        wins++;
        winsY++;
    }

    public void incrementLosses(){
        losses++;
        lossesY++;
    }

    public String getTag(){
        return tag;
    }
    public double getBalance(){
        return balance;
    }

    public ArrayList<Player> getRoster(){
        return (ArrayList<Player>) players.clone();
    }

    public boolean isFull(){
        return players.size() == 5;
    }

    /**
     *
     * @param y 0 for tourneysY, 1 for tourneys all time
     * @return tourneys
     */
    public int getTourneysWon(int y){
        if (y == 1){
            return tourneysWon;
        }
        else{
            return tourneysWonY;
        }
    }

    /**
     *
     * @param y 0 for tourneysY, 1 for tourneys all time
     * @return tourneys
     */
    public int getMajorsWon(int y){
        if (y == 1){
            return majorsWon;
        }
        else{
            return majorsWonY;
        }
    }
    /**
     *
     * @param y 0 for tourneysY, 1 for tourneys all time
     * @return tourneys
     */
    public int getTiWon(int y){
        if (y == 1){
            return tiWon;
        }
        else{
            return tiWonY;
        }
    }
    public void printRoster(){
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        System.out.println("-------------");
        String correctYear = "year";
        for (int i = 0; i < 5; i++){
            Player p = players.get(i);
            if (p.getYearsWithTeam() == 1){
                correctYear = "year";
            }
            else{
                correctYear = "years";
            }
            if (p.getYearsWithTeam() == 0){
                System.out.println(players.get(i) + " - " + formatter.format(p.getSalary()) );
            }
            else{
                System.out.println(players.get(i) + " | " + p.getYearsWithTeam() + " " + correctYear + " with team");
            }

        }
    }
    public void printRoster(String s){
        System.out.println("-------------");
        System.out.println(name);
        printRoster();
    }


    public int getRosterSize(){
        return players.size();
    }
    public String toString(){
        String z = "";
        if(tiWonY == 1){
            z = "*";
        }
        return z + name + z;
    }

    public void printRosterShort(){
        for (Player p : getRoster()){
            if(getRoster().indexOf(p) < 4){
                if(p.getYearsWithTeam() == 0){
                    System.out.print(p.getName() + "(!) - ");
                }
                else{
                    System.out.print(p.getName() + " - ");
                }
            }
            else{
                if(p.getYearsWithTeam() == 0){
                    System.out.println(p.getName() + "(!)");
                }
                else{
                    System.out.println(p.getName());
                }
            }
        }
        //System.out.println();
    }

    public void printHistory(){
        int year = 0;
        System.out.println("---" + name + "---");
        for (TeamSeasonRecord r : history){
            if(year != 0){
                System.out.println("Year " + year + " | " + r.toString());
                System.out.println("-----------");
                r.printRoster();
                System.out.println("-----------");
            }
            year++;
        }
        System.out.println("Current Roster");
        for (Player p : getRoster()){
            System.out.println(p.getName());
        }
        System.out.println("-----------");
    }

    public FreeAgencyInstance getFA(){
        return freeAgency;
    }

    public void clearFA(){
        freeAgency.clearInstance();
    }

    public void printFA(){
        freeAgency.printTransactions();
    }

    public void addExtension(Player p){
        freeAgency.addExtension(p);
    }
    public void addDropped(Player p){
        freeAgency.addDropped(p);
    }
    public void addSigning(Player p){
        freeAgency.addPickedUp(p);
    }

    public void setRoster(ArrayList<Player> players){
        this.players = players;
    }

    private class TeamSeasonRecord{
        int wins;
        int losses;
        int tourneyWins;
        int majorWins;
        int tiWins;
        double prizeWinnings;
        ArrayList<Player> roster;
        int worldRanking;

        League league;

        public TeamSeasonRecord(int w, int l, double pw){
            wins = w;
            losses = l;
            tourneyWins = getTourneysWon(0);
            majorWins = getMajorsWon(0);
            tiWins = getTiWon(0);
            prizeWinnings = pw;
            roster = getRoster();
            worldRanking = getWorldRanking();
            league = getLeague();


        }

        public void printRoster(){
            for (Player p : roster){
                if(roster.indexOf(p) < roster.size() - 1){
                    System.out.print(p.getName() + " - ");
                }
                else{
                    System.out.println(p.getName());
                }
            }
            //System.out.println();
        }



        public String toString(){
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String toPrint = " | No League | ";
            if(league != null){
                toPrint = " | " + league + " | ";
            }
            return "Rank " + worldRanking + toPrint + wins + "-" +
                    losses + " | " + formatter.format(prizeWinnings);
        }
    }
    private class FreeAgencyInstance {
        ArrayList<Player> dropped;
        ArrayList<Player> pickedUp;

        ArrayList<Player> extensions;

        public FreeAgencyInstance() {
            dropped = new ArrayList<>();
            pickedUp = new ArrayList<>();
            extensions = new ArrayList<>();
        }

        private void addDropped(Player p) {
            dropped.add(p);
        }

        private void addPickedUp(Player p) {
            pickedUp.add(p);
        }

        private void addExtension(Player p) {
            extensions.add(p);
        }

        private void clearInstance() {
            dropped.clear();
            pickedUp.clear();
            extensions.clear();
        }

        private void printTransactions() {
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            System.out.println("--------------");
            System.out.println(name + " " + formatter.format(getBalance()));
            System.out.println("--------------");
            if(dropped.size() > 0){
                System.out.println("--- Out ---");
            }
            for (Player p : dropped) {
                System.out.println(p.getName() + " -> " + p.getTeam());
            }
            if(pickedUp.size() > 0){
                System.out.println("--- In ---");
            }
            for (Player p : pickedUp) {
                System.out.println(p.getName() + " <- " + p.getPrevTeam());
            }
            if(extensions.size() > 0){
                System.out.println("--- Extensions ---");
            }
            for (Player p : extensions) {
                System.out.println(p.getName() + " - " +
                        formatter.format(p.getSalary()) + "/" + p.getContractYearsRemaining());
            }

        }
    }
}
