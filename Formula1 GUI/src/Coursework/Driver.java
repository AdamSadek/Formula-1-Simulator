package Coursework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
/*
    Author: Adam Sadek
    ID:     w1738889
 */
abstract class Driver  {

    public static final int MIN_NUM_OF_DRIVERS = 1, MAX_NUM_OF_DRIVERS = 10, MIN_NUM_OF_TEAMS = 1;
    public int driverTeam, driverPoints, driverCompletedRaces, driverWins;
    public int currentTrack = 1;
    public String driverFirstName, driverLastName, driverCountry, teamName;
    public String[] teams = {"Mercedes", "Ferrari", "Mclaren", "RedBull", "Haas", "Williams", "Alpine", "Alfa Romeo", "Alfa Tauri", "Aston Martin"};
    public ArrayList<Driver> drivers = new ArrayList<>(MAX_NUM_OF_DRIVERS);
    public ArrayList<String> f1Teams = new ArrayList<>(Arrays.asList(teams));
    public HashMap<Integer, String> f1Tracks = new HashMap<>();

    public void setDriverFirstName(String driverFirstName){
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter the driver's First Name: ");
        while (true)
        {
            String StringRegex = "^[a-zA-Z]+$";
            driverFirstName = input.nextLine();
            driverFirstName = driverFirstName.toUpperCase();
            if (!driverFirstName.matches(StringRegex)) {
                System.out.print("Please enter an appropriate First Name: ");
            } else {
                this.driverFirstName = driverFirstName;
                break;
            }
        }
    }
    public void setDriverLastName(String driverLastName){
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter the driver's Last Name: ");
        while(true)
        {
            String StringRegex = "^[a-zA-Z]+$";
            driverLastName = input.nextLine();
            driverLastName = driverLastName.toUpperCase();
            if(!driverLastName.matches(StringRegex))
            {
                System.out.print("Please enter an appropriate Last Name: ");
            }
            else {
                this.driverLastName = driverLastName;
                break;
            }
        }
    }
    public void setDriverCountry(String driverCountry){
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter the driver's origin: ");
        while(true)
        {
            String StringRegex = "^[a-zA-Z]+$";
            driverCountry = input.nextLine();
            driverCountry = driverCountry.toUpperCase();
            if(!driverCountry.matches(StringRegex))
            {
                System.out.print("Please enter an appropriate country: ");
            }
            else{
                this.driverCountry = driverCountry;
                break;
            }
        }
    }
    public void setDriverTeam(String teamName){
        Scanner input = new Scanner(System.in);
        int maxNumOfTeams = f1Teams.size(), chosenTeam;
        System.out.println("=====Teams available=====");
        for(int i = 0; i < maxNumOfTeams; i++){
            System.out.println(" " + (i+1) + ". " + f1Teams.get(i));
        }
        System.out.print("Please select a team: ");
        while(true)
        {
            try {
                chosenTeam = input.nextInt();
                if (chosenTeam < MIN_NUM_OF_TEAMS || chosenTeam > maxNumOfTeams)
                {
                    System.out.print("Please enter a valid number(1-"+ maxNumOfTeams + "): ");
                }
                else{
                    this.driverTeam = chosenTeam;
                    this.teamName = f1Teams.get(chosenTeam-1);
                    break;
                }
            } catch (Exception e) {
                System.out.print("Please enter a valid number: ");
                input.next();
            }
        }
    }
    public void setDriverPoints(int driverPoints) {
        this.driverPoints += driverPoints;
    }
    public void setDriverCompletedRaces(int driverCompletedRaces) {
        this.driverCompletedRaces += driverCompletedRaces;
    }
    public void setDriverWins(int driverWins) {
        this.driverWins += driverWins;
    }

    public String getDriverFirstName(){
        return driverFirstName;
    }
    public String getDriverLastName(){
        return driverLastName;
    }
    public String getDriverCountry(){
        return driverCountry;
    }
    public String getDriverTeam(){
        return teamName;
    }
    public int getDriverPoints(){
        return driverPoints;
    }
    public int getDriverCompletedRaces(){
        return driverCompletedRaces;
    }
    public int getDriverWins(){
        return driverWins;
    }

}
