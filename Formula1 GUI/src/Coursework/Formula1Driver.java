package Coursework;

import java.awt.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
/*
    Author: Adam Sadek
    ID:     w1738889
 */
public class Formula1Driver extends Driver{
    int startDay = 28, startMonth = 3, championshipYear = 2022;
    HashMap<Integer, String> results = new HashMap<Integer, String>();
    ArrayList<Integer> positions = new ArrayList<Integer>();
    public Formula1Driver(String driverFirstName, String driverLastName, String driverCountry, String teamName,int driverPoints, int driverCompletedRaces, int driverWins) {
        this.driverPoints = driverPoints;
        this.driverCompletedRaces = driverCompletedRaces;
        this.driverWins = driverWins;
        this.driverFirstName = driverFirstName;
        this.driverLastName = driverLastName;
        this.driverCountry = driverCountry;
        this.teamName = teamName;
    }
    public void changeDate(){
        switch(currentTrack-1){
            case 0:
                 startDay = 28;
                 startMonth = 4;
                championshipYear = 2022;
                 break;
            case 1:
                startDay = 11;
                startMonth = 4;
                championshipYear = 2022;
                break;
            case 2:
                startDay = 18;
                startMonth = 4;
                championshipYear = 2022;
                break;
            case 3:
                startDay = 25;
                startMonth = 4;
                championshipYear = 2022;
                break;
            case 4:
                startDay = 1;
                startMonth = 5;
                championshipYear = 2022;
                break;
            case 5:
                startDay = 8;
                startMonth = 5;
                championshipYear = 2022;
                break;
            case 6:
                startDay = 15;
                startMonth = 5;
                championshipYear = 2022;
                break;
            case 7:
                startDay = 22;
                startMonth = 5;
                championshipYear = 2022;
                break;
            case 8:
                startDay = 29;
                startMonth = 5;
                championshipYear = 2022;
                break;
            case 9:
                startDay = 5;
                startMonth = 6;
                championshipYear = 2022;
                break;
        }
    }
    public void sortByPointsDec(){
        if(drivers.isEmpty()){
            System.out.println("There are no drivers!");
        }else {
            //int driverNum = 0;
            Collections.sort(drivers, new Comparator<Driver>() {
                public int compare(Driver driver1, Driver driver2) {
                    return driver1.getDriverPoints() - driver2.getDriverPoints();
                }
            });
//            while (driverNum < drivers.size()) {
//                System.out.println("\n    Driver #" + (driverNum + 1));
//                System.out.println("Full Name: " + drivers.get(driverNum).getDriverFirstName() + " " + drivers.get(driverNum).getDriverLastName());
//                System.out.println("Team : " + drivers.get(driverNum).getDriverTeam());
//                System.out.println("Points: " + drivers.get(driverNum).getDriverPoints());
//                driverNum++;
//            }
        }

    }
}
