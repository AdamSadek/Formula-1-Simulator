package Coursework;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
/*
    Author: Adam Sadek
    ID:     w1738889
 */
public class Formula1ChampionshipManager extends JFrame implements ChampionshipManager, ActionListener {

    Formula1Driver driver = new Formula1Driver("", "", "", "", 0, 0, 0);
    String[] raceDates = {"28/4/2022", "11/4/2022", "18/4/2022", "25/4/2022", "1/5/2022", "8/5/2022", "15/5/2022", "22/5/2022", "29/5/2022", "5/6/2022"};
    JButton buttonDecOrder, buttonAscOrder, buttonRace, buttonWinsDec, buttonReset, buttonBoostedRace, buttonRaceHistory, buttonSubmit;
    JMenuItem loadData, saveData;
    JLabel textOnScreen = new JLabel();
    JLabel[] raceResults = new JLabel[10];
    ArrayList<JLabel> racesHistory = new ArrayList<>();
    JLabel[] showDrivers = new JLabel[10];
    JPanel panel = new JPanel();
    JPanel buttons = new JPanel();
    JTextField search = new JTextField(10);
    GridBagConstraints gridEditor = new GridBagConstraints();

    public static void main(String[] args) throws IOException {
        new Formula1ChampionshipManager();
    }

    Formula1ChampionshipManager() throws IOException {
        this.setLayout(new GridBagLayout());
        // creating a title for the app
        this.setTitle("Formula 1 Simulator");
        // When button is pressed it will exit from the app
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Making sure the user does not play around with the frame size
        this.setResizable(false);
        // setting the size by editing the dimensions
        this.setSize(1200, 900);
        // set favicon icon and change it to fit appropriately
        ImageIcon logo = new ImageIcon(new ImageIcon("src/Coursework/icons/logo.png").getImage().getScaledInstance(100, 70, Image.SCALE_DEFAULT));
        this.setIconImage(logo.getImage());

        TitledBorder panelTitle = BorderFactory.createTitledBorder("Output");
        panelTitle.setTitleFont(changeFont(13));
        TitledBorder buttonsTitle = BorderFactory.createTitledBorder("Buttons");
        buttonsTitle.setTitleFont(changeFont(13));
        TitledBorder searchBarTitle = BorderFactory.createTitledBorder("Search Bar");
        searchBarTitle.setTitleFont(changeFont(13));

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(500, 800));
        panel.setMaximumSize(new Dimension(500, 800));
        panel.setBorder(panelTitle);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setBorder(buttonsTitle);
        buttons.setAlignmentY(Component.RIGHT_ALIGNMENT);
        this.getContentPane().add(panel);
        menuBar();
        intro();
        this.getContentPane().add(buttons);

        importTracks();
        searchBar();
        ascOrderPointsButton();
        decOrderPointsButton();
        decOrderWinsButton();
        boostedRaceButton();
        raceHistoryButton();
        raceButton();

        resetButton();
        resetProgress();
        sortByPointsDec();

        this.setVisible(true);
    }
    // saves all the data inputted by the user.
    public void saveData() throws IOException {
        String dataFile = "src/Coursework/database/data.txt";
        String tracksFile = "src/Coursework/database/tracks.txt";
        String trackerFile = "src/Coursework/database/trackTracker.txt";
        String raceDateFile = "src/Coursework/database/raceDate.txt";
        String raceDriverFile = "src/Coursework/database/race&driverPositions(for file save).txt";
        BufferedWriter writeDataFile = new BufferedWriter(new FileWriter(dataFile));
        BufferedWriter writeTracksFile = new BufferedWriter(new FileWriter(tracksFile));
        BufferedWriter writeTrackerFile = new BufferedWriter(new FileWriter(trackerFile));
        BufferedWriter writeRaceDateFile = new BufferedWriter(new FileWriter(raceDateFile));
        BufferedWriter writeRaceDriverFile = new BufferedWriter(new FileWriter(raceDriverFile));
        // save data on driver
        for (int i = 0; i < driver.drivers.size(); i++) {
            writeDataFile.write(driver.drivers.get(i).getDriverFirstName() + "\n");
            writeDataFile.write(driver.drivers.get(i).getDriverLastName() + "\n");
            writeDataFile.write(driver.drivers.get(i).getDriverCountry() + "\n");
            writeDataFile.write(driver.drivers.get(i).getDriverTeam() + "\n");
            writeDataFile.write(driver.drivers.get(i).getDriverPoints() + "\n");
            writeDataFile.write(driver.drivers.get(i).getDriverCompletedRaces() + "\n");
            writeDataFile.write(driver.drivers.get(i).getDriverWins() + "\n");
        }
        // save tracks
        for (int i = 1; i <= driver.f1Tracks.size(); i++) {
            writeTracksFile.write(driver.f1Tracks.get(i) + "\n");
        }
        // save tracker that keeps track of the tracks
        writeTrackerFile.write(driver.currentTrack + "\n");
        // saves the dates of the races
        writeRaceDateFile.write(driver.startDay + "\n");
        writeRaceDateFile.write(driver.startMonth + "\n");
        writeRaceDateFile.write(driver.championshipYear + "\n");

        String raceDriverPos = "src/Coursework/database/race&driverPositions.txt";
        Scanner readDataFile = null;
        try {
            readDataFile = new Scanner(new File(raceDriverPos));
        } catch (Exception e) {
            textOnScreen.setText("File not found.");
        }
        while(readDataFile.hasNext()){
            writeRaceDriverFile.write(readDataFile.nextLine() + "\n");
        }

        if (showDrivers[0] != null) {
            for (int i = 0; i < showDrivers.length; i++) {
                showDrivers[i].setText("");
            }
        }
        textOnScreen.setText("Data has been saved");
        gridEditor.anchor = GridBagConstraints.PAGE_START;
        gridEditor.weightx = 0;
        gridEditor.weighty = 0;
        gridEditor.gridx = 1;
        gridEditor.gridy = 1;
        textOnScreen.setFont(changeFontBold(15));
        panel.add(textOnScreen, gridEditor);
        writeDataFile.close();
        writeTracksFile.close();
        writeTrackerFile.close();
        writeRaceDateFile.close();
        writeRaceDriverFile.close();
    }
    public void loadData() throws IOException {
        driver.drivers.clear();
        String dataFile = "src/Coursework/database/data.txt";
        String trackerFile = "src/Coursework/database/trackTracker.txt";
        String raceDateFile = "src/Coursework/database/raceDate.txt";
        String raceDriverPos = "src/Coursework/database/race&driverPositions(for file save).txt";
        String raceDriverPosSwap = "src/Coursework/database/race&driverPositions.txt";

        String line1, line2, line3, line4, line5, line6, line7;
        BufferedWriter writeDriverPosSwap = new BufferedWriter(new FileWriter(raceDriverPosSwap));
        Scanner readTrackerFile = null;
        Scanner readDateFile = null;
        Scanner readDataFile = null;
        Scanner readRaceDriverPos = null;
        try {
            readDataFile = new Scanner(new File(dataFile));
            readTrackerFile = new Scanner(new File(trackerFile));
            readDateFile = new Scanner(new File(raceDateFile));
            readRaceDriverPos = new Scanner(new File(raceDriverPos));
        } catch (Exception e) {
            textOnScreen.setText("File not found.");
        }
        int driverLine = 0;
        while (readDataFile.hasNext()) {
            line1 = readDataFile.nextLine();
            line2 = readDataFile.nextLine();
            line3 = readDataFile.nextLine();
            line4 = readDataFile.nextLine();
            line5 = readDataFile.nextLine();
            line6 = readDataFile.nextLine();
            line7 = readDataFile.nextLine();
            driver.drivers.add(new Formula1Driver(line1, line2, line3, line4, Integer.parseInt(line5), Integer.parseInt(line6), Integer.parseInt(line7)));
            driverLine++;
        }
        while(readRaceDriverPos.hasNext()){
            String name = readRaceDriverPos.nextLine() + "\n";
            System.out.print(name);
            writeDriverPosSwap.write(name);
        }
        if (!(driverLine < 1)) {
            for (int i = 0; i < driverLine; i++) {
                driver.f1Teams.remove(driver.drivers.get(driverLine - 1).getDriverTeam());
            }
        }
        while (readTrackerFile.hasNext()) {
            driver.currentTrack = Integer.parseInt(readTrackerFile.nextLine());
        }
        while (readDateFile.hasNext()) {
            driver.startDay = Integer.parseInt(readDateFile.nextLine());
            driver.startMonth = Integer.parseInt(readDateFile.nextLine());
            driver.championshipYear = Integer.parseInt(readDateFile.nextLine());
        }
        if (showDrivers[0] != null) {
            for (int i = 0; i < 10; i++) {
                showDrivers[i].setText("");
            }
        }
        textOnScreen.setText("Data has been loaded");
        gridEditor.anchor = GridBagConstraints.PAGE_START;
        gridEditor.weightx = 0;
        gridEditor.weighty = 0;
        gridEditor.gridx = 1;
        gridEditor.gridy = 1;
        textOnScreen.setFont(changeFontBold(15));
        panel.add(textOnScreen, gridEditor);
        readDateFile.close();
        readDataFile.close();
        readTrackerFile.close();
        readRaceDriverPos.close();
        writeDriverPosSwap.close();

    }
    public void clearScreen(){
        textOnScreen.setText("");
        if (showDrivers[0] != null) {
            for (int i = 0; i < showDrivers.length; i++) {
                showDrivers[i].setText("");
            }
        }
        for (int i = 0; i < racesHistory.size(); i++) {
            racesHistory.get(i).setText("");
        }

        if(raceResults[0] != null){
            for (int i = 0; i < raceResults.length; i++) {
                raceResults[i].setText("");
            }
        }
        this.add(textOnScreen);
    }
    // when the app is relaunched it should read all the info saved in the prev file and continue the same operations.
    public void importTracks() {
        String tracksFile = "src/Coursework/database/tracks.txt";
        String trackerFile = "src/Coursework/database/trackTracker.txt";
        Scanner readTracksFile = null;
        Scanner readTrackerFile = null;
        try {
            readTracksFile = new Scanner(new File(tracksFile));
            readTrackerFile = new Scanner((new File(trackerFile)));
        } catch (Exception e) {
            System.out.println("File not found.");
        }
        int trackNum = 1;
        while (readTracksFile.hasNext()) {
            driver.f1Tracks.put(trackNum++, readTracksFile.nextLine());
        }
        readTracksFile.close();
        readTrackerFile.close();
    }

    public void intro() throws IOException {
        JLabel label = new JLabel();
        label.setText("Formula 1 2022 Championship");
        // https://stackoverflow.com/questions/5652344/how-can-i-use-a-custom-font-in-java
        Font customFont = null;
        try {
            //create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Coursework/fonts/Formula1Font.otf")).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }

        label.setFont(customFont);

        ImageIcon icon = new ImageIcon(new ImageIcon
                ("src/Coursework/icons/transparentLogo.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));
        label.setIcon(icon);
        gridEditor.anchor = GridBagConstraints.CENTER;
        gridEditor.weighty = 1;
        gridEditor.weightx = 1;
        gridEditor.gridx = 1;
        this.add(label, gridEditor);

    }
    public void decOrderPointsButton() {
        ImageIcon p10 = new ImageIcon(new ImageIcon
                ("src/Coursework/icons/10.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        buttonDecOrder = new JButton("Sort Table (Dec)");
        buttonDecOrder.addActionListener(this);
        buttonDecOrder.setFocusable(false);
        buttonDecOrder.setIcon(p10);
        buttonDecOrder.setHorizontalTextPosition(JButton.RIGHT);
        buttonDecOrder.setVerticalTextPosition(JButton.CENTER);
        // https://stackoverflow.com/questions/5652344/how-can-i-use-a-custom-font-in-java
        Font customFont = null;
        try {
            //create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Coursework/fonts/Formula1Font.otf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        buttonDecOrder.setFont(customFont);
        buttonDecOrder.setIconTextGap(1);
        buttonDecOrder.setPreferredSize(new Dimension(buttonAscOrder.getSize()));
        buttons.add(buttonDecOrder);


    }
    public void ascOrderPointsButton() {
        ImageIcon p10 = new ImageIcon(new ImageIcon
                ("src/Coursework/icons/1.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        buttonAscOrder = new JButton("Sort Table (Asc)");
        buttonAscOrder.setBounds(40, 100, 150, 30);
        buttonAscOrder.addActionListener(this);
        buttonAscOrder.setFocusable(false);
        buttonAscOrder.setIcon(p10);
        buttonAscOrder.setHorizontalTextPosition(JButton.RIGHT);
        buttonAscOrder.setVerticalTextPosition(JButton.CENTER);
        Font customFont = null;
        try {
            //create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Coursework/fonts/Formula1Font.otf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        buttonAscOrder.setFont(customFont);
        buttonAscOrder.setIconTextGap(1);
        buttons.add(buttonAscOrder);


    }
    public void decOrderWinsButton() {
        ImageIcon p1 = new ImageIcon(new ImageIcon
                ("src/Coursework/icons/p1.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        buttonWinsDec = new JButton("Sort Wins (Dec)");
        buttonWinsDec.addActionListener(this);
        buttonWinsDec.setFocusable(false);
        buttonWinsDec.setIcon(p1);
        buttonWinsDec.setHorizontalTextPosition(JButton.RIGHT);
        buttonWinsDec.setVerticalTextPosition(JButton.CENTER);
        Font customFont = null;
        try {
            //create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Coursework/fonts/Formula1Font.otf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        buttonWinsDec.setFont(customFont);
        buttonWinsDec.setIconTextGap(1);
        buttonWinsDec.setPreferredSize(new Dimension(buttonAscOrder.getSize()));
        buttons.add(buttonWinsDec);
    }
    public void raceButton() {
        ImageIcon race = new ImageIcon(new ImageIcon
                ("src/Coursework/icons/f1car.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
        buttonRace = new JButton("Race");
        buttonRace.addActionListener(this);
        buttonRace.setFocusable(false);
        buttonRace.setIcon(race);
        buttonRace.setHorizontalTextPosition(JButton.RIGHT);
        buttonRace.setVerticalTextPosition(JButton.CENTER);
        Font customFont = null;
        try {
            //create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Coursework/fonts/Formula1Font.otf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        buttonRace.setFont(customFont);
        buttonRace.setIconTextGap(20);
        buttonRace.setPreferredSize(new Dimension(buttonAscOrder.getSize()));
        buttons.add(buttonRace);

    }
    public void boostedRaceButton() {
        ImageIcon race = new ImageIcon(new ImageIcon
                ("src/Coursework/icons/f1car.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
        buttonBoostedRace = new JButton("Boosted Race");
        buttonBoostedRace.addActionListener(this);
        buttonBoostedRace.setFocusable(false);
        buttonBoostedRace.setIcon(race);
        buttonBoostedRace.setHorizontalTextPosition(JButton.RIGHT);
        buttonBoostedRace.setVerticalTextPosition(JButton.CENTER);
        Font customFont = null;
        try {
            //create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Coursework/fonts/Formula1Font.otf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        buttonBoostedRace.setFont(customFont);
        buttonBoostedRace.setPreferredSize(new Dimension(buttonAscOrder.getSize()));
        buttons.add(buttonBoostedRace);
    }
    public void raceHistoryButton() {
        ImageIcon p1 = new ImageIcon(new ImageIcon
                ("src/Coursework/icons/history.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        buttonRaceHistory = new JButton("Race History");
        buttonRaceHistory.setBounds(40, 100, 150, 30);
        buttonRaceHistory.addActionListener(this);
        buttonRaceHistory.setFocusable(false);
        buttonRaceHistory.setIcon(p1);
        buttonRaceHistory.setHorizontalTextPosition(JButton.RIGHT);
        buttonRaceHistory.setVerticalTextPosition(JButton.CENTER);
        Font customFont = null;
        try {
            //create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Coursework/fonts/Formula1Font.otf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        buttonRaceHistory.setFont(customFont);
        buttonRaceHistory.setIconTextGap(1);
        buttonRaceHistory.setPreferredSize(new Dimension(buttonAscOrder.getSize()));
        buttons.add(buttonRaceHistory);
    }
    public void resetButton(){
        ImageIcon p1 = new ImageIcon(new ImageIcon
                ("src/Coursework/icons/reset.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        buttonReset = new JButton("Reset Progress");
        buttonReset.setBounds(40, 100, 150, 30);
        buttonReset.addActionListener(this);
        buttonReset.setFocusable(false);
        buttonReset.setIcon(p1);
        buttonReset.setHorizontalTextPosition(JButton.RIGHT);
        buttonReset.setVerticalTextPosition(JButton.CENTER);
        Font customFont = null;
        try {
            //create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Coursework/fonts/Formula1Font.otf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        buttonReset.setFont(customFont);
        buttonReset.setIconTextGap(1);
        buttonReset.setPreferredSize(new Dimension(buttonAscOrder.getSize()));
        buttons.add(buttonReset);
    }
    public void menuBar() {
        JMenuBar menu = new JMenuBar();
        menu.setFont(changeFont(12));
        this.setJMenuBar(menu);
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(changeFont(12));

        loadData = new JMenuItem("Load");
        loadData.setFont(changeFont(12));

        saveData = new JMenuItem("Save");
        saveData.setFont(changeFont(12));

        saveData.addActionListener(this);
        loadData.addActionListener(this);

        fileMenu.add(saveData);
        fileMenu.add(loadData);

        menu.add(fileMenu);

    }
    public void searchBar(){
        buttonSubmit = new JButton("Search");
        buttonSubmit.setSize(5,5);
        buttonSubmit.addActionListener(this);
        buttonSubmit.setFocusable(false);
        buttonSubmit.setHorizontalTextPosition(JButton.RIGHT);
        buttonSubmit.setVerticalTextPosition(JButton.CENTER);

        search.setFont(changeFont(12));
        buttonSubmit.setFont(changeFont(12));
        gridEditor.anchor = GridBagConstraints.PAGE_END;
        gridEditor.weightx = 0;
        gridEditor.weighty = 0;
        gridEditor.gridx = 0;
        gridEditor.gridy = 1;
        this.add(buttonSubmit, gridEditor);
        gridEditor.anchor = GridBagConstraints.LAST_LINE_END;
        gridEditor.weightx = 0;
        gridEditor.weighty = 1;
        gridEditor.gridx = 0;
        gridEditor.gridy = 1;
        this.add(search, gridEditor);
    }

    public void sortByPointsDec() {
        clearScreen();
        this.add(textOnScreen);
        textOnScreen.setFont(changeFontBold(12));
        if (driver.drivers.isEmpty()) {
            textOnScreen.setText("There are no drivers!");
            gridEditor.anchor = GridBagConstraints.PAGE_START;
            gridEditor.weightx = 0;
            gridEditor.weighty = 0;
            gridEditor.gridx = 1;
            gridEditor.gridy = 1;
            panel.add(textOnScreen, gridEditor);
        } else {
            int driverNum = 0;
            Collections.sort(driver.drivers, new Comparator<Driver>() {
                public int compare(Driver driver1, Driver driver2) {
                    return driver2.getDriverPoints() - driver1.getDriverPoints();
                }
            });
            while (driverNum < driver.drivers.size()) {
                panel.add(showDrivers[driverNum] = new JLabel("<html>Driver #" + (driverNum + 1) + "<BR>Full Name - " + driver.drivers.get(driverNum).getDriverFirstName()  + " " + driver.drivers.get(driverNum).getDriverLastName() +
                        "<BR>Team - " + driver.drivers.get(driverNum).getDriverTeam() + "<BR> Points - " + driver.drivers.get(driverNum).getDriverPoints() + "<BR> <BR>" + "</html>"));
                showDrivers[driverNum].setFont(changeFont(11));
                driverNum++;
            }
        }
    }
    public void sortByWinsDec() {
        clearScreen();
        textOnScreen.setFont(changeFontBold(15));
        if (driver.drivers.isEmpty()) {
            textOnScreen.setText("There are no drivers!");
            gridEditor.anchor = GridBagConstraints.PAGE_START;
            gridEditor.weightx = 0;
            gridEditor.weighty = 0;
            gridEditor.gridx = 1;
            gridEditor.gridy = 1;
            panel.add(textOnScreen, gridEditor);
        } else {
            int driverNum = 0;
            Collections.sort(driver.drivers, new Comparator<Driver>() {
                public int compare(Driver driver1, Driver driver2) {
                    return driver2.getDriverWins() - driver1.getDriverWins();
                }
            });
            while (driverNum < driver.drivers.size()) {
                panel.add(showDrivers[driverNum] = new JLabel("<html>Driver #" + (driverNum + 1) + "<BR>Full Name - " + driver.drivers.get(driverNum).getDriverFirstName() + " " + driver.drivers.get(driverNum).getDriverLastName() +
                        "<BR>Team - " + driver.drivers.get(driverNum).getDriverTeam() + "<BR> Wins - " + driver.drivers.get(driverNum).getDriverWins() + "<BR> <BR>" + "</html>"));
                showDrivers[driverNum].setFont(changeFont(11));
                driverNum++;
            }
        }
    }
    public void sortByPointsAsc() {
        clearScreen();
        this.add(textOnScreen);
        if (driver.drivers.isEmpty()) {
            textOnScreen.setText("There are no drivers!");
            gridEditor.anchor = GridBagConstraints.PAGE_START;
            gridEditor.weightx = 0;
            gridEditor.weighty = 0;
            gridEditor.gridx = 1;
            gridEditor.gridy = 1;
            textOnScreen.setFont(changeFont(12));
            panel.add(textOnScreen, gridEditor);
        } else {
            int driverNum = 0;
            Collections.sort(driver.drivers, new Comparator<Driver>() {
                public int compare(Driver driver1, Driver driver2) {
                    return driver1.getDriverPoints() - driver2.getDriverPoints();
                }
            });
            while (driverNum < driver.drivers.size()) {
                panel.add(showDrivers[driverNum] = new JLabel("<html>Driver #" + (driverNum + 1) + "<BR>Full Name - " + driver.drivers.get(driverNum).getDriverFirstName() + " " + driver.drivers.get(driverNum).getDriverLastName() +
                        "<BR>Team - " + driver.drivers.get(driverNum).getDriverTeam() + "<BR> Points - " + driver.drivers.get(driverNum).getDriverPoints() + "<BR> <BR>" + "</html>"));
                showDrivers[driverNum].setFont(changeFont(11));
                driverNum++;
            }
        }
    }

    public void resetProgress() throws IOException {
        driver.drivers.clear();
        String dataFile = "src/Coursework/database/data(reset).txt";
        String trackerFile = "src/Coursework/database/trackTracker(reset).txt";
        String raceDateFile = "src/Coursework/database/raceDate(reset).txt";
        String raceDriverPosFile = "src/Coursework/database/race&driverPositions.txt";
        String line1, line2, line3, line4, line5, line6, line7;
        Scanner readTrackerFile = null;
        Scanner readDateFile = null;
        Scanner readDataFile = null;
        BufferedWriter writeRaceDriverPosFile = new BufferedWriter(new FileWriter(raceDriverPosFile));
        Scanner readRaceDriverPosFile = null;
        try {
            readDataFile = new Scanner(new File(dataFile));
            readTrackerFile = new Scanner(new File(trackerFile));
            readDateFile = new Scanner(new File(raceDateFile));
            readRaceDriverPosFile = new Scanner(new File(raceDriverPosFile));
        } catch (Exception e) {
            System.out.println("File not found.");
        }
        int driverLine = 0;
        while (readDataFile.hasNext()) {
            line1 = readDataFile.nextLine();
            line2 = readDataFile.nextLine();
            line3 = readDataFile.nextLine();
            line4 = readDataFile.nextLine();
            line5 = readDataFile.nextLine();
            line6 = readDataFile.nextLine();
            line7 = readDataFile.nextLine();
            driver.drivers.add(new Formula1Driver(line1, line2, line3, line4, Integer.parseInt(line5), Integer.parseInt(line6), Integer.parseInt(line7)));
            driverLine++;
        }
        if (!(driverLine < 1)) {
            for (int i = 0; i < driverLine; i++) {
                driver.f1Teams.remove(driver.drivers.get(driverLine - 1).getDriverTeam());
            }
        }

        while (readTrackerFile.hasNext()) {
            driver.currentTrack = Integer.parseInt(readTrackerFile.nextLine());
        }
        while (readDateFile.hasNext()) {
            driver.startDay = Integer.parseInt(readDateFile.nextLine());
            driver.startMonth = Integer.parseInt(readDateFile.nextLine());
            driver.championshipYear = Integer.parseInt(readDateFile.nextLine());
        }
        while(readRaceDriverPosFile.hasNext()){
            writeRaceDriverPosFile.flush();
        }
        if (showDrivers[0] != null) {
            for (int i = 0; i < 10; i++) {
                showDrivers[i].setText("");
            }
        }
        textOnScreen.setText("Progress has been reset");
        gridEditor.anchor = GridBagConstraints.PAGE_START;
        gridEditor.weightx = 0;
        gridEditor.weighty = 0;
        gridEditor.gridx = 1;
        gridEditor.gridy = 1;
        textOnScreen.setFont(changeFontBold(15));
        panel.add(textOnScreen, gridEditor);
        readDateFile.close();
        readDataFile.close();
        readTrackerFile.close();
    }
    public Font changeFontBold(int size) {
        Font customFont = null;
        //create the font to use. Specify the size!
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Coursework/fonts/Formula1-Bold.otf")).deriveFont(size + 0f);
        } catch (FontFormatException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //register the font
        ge.registerFont(customFont);
        return customFont;
    }
    public Font changeFont(int size) {
        Font customFont = null;
        //create the font to use. Specify the size!
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Coursework/fonts/Formula1Font.otf")).deriveFont(size + 0f);
        } catch (FontFormatException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //register the font
        ge.registerFont(customFont);
        return customFont;
    }

    public void raceDay() throws IOException {
        Random rand = new Random();
        String raceData = "src/Coursework/database/race&driverPositions.txt";
        BufferedWriter writeToFile = new BufferedWriter(new FileWriter(raceData, true));
        clearScreen();
        if(driver.currentTrack == 11){
            seasonWinner();
        }else{
            if(driver.drivers.size() == 10) {
                String track = driver.f1Tracks.get(driver.currentTrack);
                System.out.println(driver.f1Tracks);
                textOnScreen.setText(track + "       " + driver.startDay + "/" + driver.startMonth + "/" + driver.championshipYear);
                gridEditor.anchor = GridBagConstraints.PAGE_START;
                gridEditor.weightx = 0;
                gridEditor.weighty = 0;
                gridEditor.gridx = 1;
                gridEditor.gridy = 1;
                textOnScreen.setFont(changeFontBold(15));
                panel.add(textOnScreen, gridEditor);
                driver.changeDate();
                HashMap<Integer, String> results = new HashMap<>();
                ArrayList<Integer> positions = new ArrayList<>();
                    for (int pos = 0; pos < driver.MAX_NUM_OF_DRIVERS; pos++) {
                        int randomPos = rand.nextInt(10);
                        if (results.containsKey(randomPos)) {
                            while (results.containsKey(randomPos)) {
                                randomPos = rand.nextInt(10);
                            }
                            results.put(randomPos, driver.drivers.get(randomPos).getDriverFirstName());
                        } else {
                            results.put(randomPos, driver.drivers.get(randomPos).getDriverFirstName());
                        }
                        panel.add(raceResults[pos] = new JLabel("<html>" +
                                "P" + (pos + 1) + " - " + results.get(randomPos) +
                                " " + driver.drivers.get(randomPos).getDriverTeam() + "<BR> <BR>" + "</html>"));
                        raceResults[pos].setFont(changeFont(12));
                        positions.add(randomPos);
                    }
                    for (int i = 0; i < results.size(); i++) {
                        writeToFile.write(driver.drivers.get(positions.get(i)).getDriverFirstName() + "\n");
                    }
                    System.out.println(positions);
                    for (int i = 0; i < positions.size(); i++) {
                        racePositions(i, positions);
                    }
                    if(driver.currentTrack == 11){
                        seasonWinner();
                    }
                }
            else{
                textOnScreen.setText("Not enough drivers to start the race!");
            }
            writeToFile.close();
            driver.currentTrack++;
        }
    }
    // fix duplicate problem
    public void raceDayTopThreeBoost() throws IOException {
        Random rand = new Random();
        String raceData = "src/Coursework/database/race&driverPositions.txt";
        BufferedWriter writeToFile = new BufferedWriter(new FileWriter(raceData, true));
        clearScreen();
        if(driver.currentTrack == 11){
            seasonWinner();
        }else{
            if(driver.drivers.size() == 10) {
                String track = driver.f1Tracks.get(driver.currentTrack);
                System.out.println(driver.f1Tracks);
                textOnScreen.setText("<html>" + track + "       " + driver.startDay + "/" + driver.startMonth + "/" + driver.championshipYear);
                gridEditor.anchor = GridBagConstraints.PAGE_START;
                gridEditor.weightx = 0;
                gridEditor.weighty = 0;
                gridEditor.gridx = 1;
                gridEditor.gridy = 1;
                textOnScreen.setFont(changeFontBold(15));
                panel.add(textOnScreen, gridEditor);
                driver.changeDate();

                HashMap<Integer, String> quali = new HashMap<>();
                ArrayList<Integer> qualiPos = new ArrayList<>();
                driver.currentTrack++;
                for (int pos = 0; pos < driver.MAX_NUM_OF_DRIVERS; pos++) {
                    int randomPos = rand.nextInt(10);
                    if (quali.containsKey(randomPos)) {
                        while (quali.containsKey(randomPos)) {
                            randomPos = rand.nextInt(10);
                        }
                        qualiPos.add(randomPos);
                        quali.put(randomPos, driver.drivers.get(randomPos).getDriverFirstName());
                    } else {
                        quali.put(randomPos, driver.drivers.get(randomPos).getDriverFirstName());
                        qualiPos.add(randomPos);
                    }
                    System.out.println("P" + (pos + 1) + " - " + quali.get(randomPos) +
                            " " + driver.drivers.get(randomPos).getDriverTeam());
                }
                double probability = Math.round(rand.nextDouble() * 100) / 100.0;
                System.out.println(probability);
                HashMap<Integer, String> f1Race = new HashMap<>();
                ArrayList<Integer> racePos = new ArrayList<>();


                    if (probability <= 1.0 && probability >= 0.60) { // p1
                        f1Race.put(qualiPos.get(0), quali.get(0));
                        racePos.add(qualiPos.get(0));
                    }
                    if (probability <= 0.60 && probability >= 0.30) { // p2
                        f1Race.put(qualiPos.get(1), quali.get(1));
                        racePos.add(qualiPos.get(1));
                    }
                    if (probability <= 0.30 && probability >= 0.20) { // p3-4
                        f1Race.put(qualiPos.get(2), quali.get(2));
                        racePos.add(qualiPos.get(2));
                    }
                    if (probability <= 0.20 && probability >= 0.10) { // p3-4
                        f1Race.put(qualiPos.get(3), quali.get(3));
                        racePos.add(qualiPos.get(3));
                    }
                    if (probability <= 0.10 && probability >= 0.8) { // p5-9
                        f1Race.put(qualiPos.get(4), quali.get(4));
                        racePos.add(qualiPos.get(4));
                    }
                    if (probability <= 0.8 && probability >= 0.06) { // p5-9
                        f1Race.put(qualiPos.get(5), quali.get(5));
                        racePos.add(qualiPos.get(5));
                    }
                    if (probability <= 0.06 && probability >= 0.04) { // p5-9
                        f1Race.put(qualiPos.get(6), quali.get(6));
                        racePos.add(qualiPos.get(6));
                    }
                    if (probability <= 0.04 && probability >= 0.02) { // p5-9
                        f1Race.put(qualiPos.get(7), quali.get(7));
                        racePos.add(qualiPos.get(7));

                    }
                    if (probability <= 0.02 && probability >= 0.00) { // p5-9
                        f1Race.put(qualiPos.get(8), quali.get(8));
                        racePos.add(qualiPos.get(8));
                    }
                    if (probability <= 0.00 && probability >= 0.00) { // p5-9
                        f1Race.put(qualiPos.get(8), quali.get(0));
                        racePos.add(qualiPos.get(8));
                    }

                for (int pos = 0; pos < driver.MAX_NUM_OF_DRIVERS; pos++){
                    int randomIndex = rand.nextInt(10);
                    if(f1Race.containsKey(randomIndex)){
                        int i = 0;
                        while(f1Race.containsKey(randomIndex) && i++ < 100){
                            randomIndex = rand.nextInt(10);
                        }
                        f1Race.putIfAbsent(randomIndex, quali.get(randomIndex));
                        racePos.add(randomIndex);
                    }else{
                        f1Race.putIfAbsent(randomIndex, quali.get(randomIndex));
                        racePos.add(randomIndex);
                    }
                }
                for (int pos = 0; pos < driver.MAX_NUM_OF_DRIVERS; pos++) {
                    panel.add(raceResults[pos] = new JLabel("<html>" +
                            "P" + (pos + 1) + " - " + driver.drivers.get(racePos.get(pos)).getDriverFirstName() +
                            " " + driver.drivers.get(racePos.get(pos)).getDriverTeam() + "<BR> <BR>" + "</html>"));
                    raceResults[pos].setFont(changeFont(12));

                }
                for (int i = 0; i < f1Race.size(); i++) {
                    writeToFile.write(driver.drivers.get(racePos.get(i)).getDriverFirstName() + "\n");
                }
                for (int pos = 0; pos < driver.drivers.size(); pos++) {
                    racePositions(pos, racePos);
                }
                if(driver.currentTrack == 12){
                    seasonWinner();
                }
            }
            else{
                textOnScreen.setText("Not enough drivers to start the race!");
            }
        }
        writeToFile.close();
    }
    public void racePositions(int pos, ArrayList<Integer> positions){
        switch (pos) {
            case 0:
                driver.drivers.get(positions.get(pos)).setDriverWins(1);
                driver.drivers.get(positions.get(pos)).setDriverPoints(25);
                driver.drivers.get(positions.get(pos)).setDriverCompletedRaces(1);
                break;
            case 1:
                driver.drivers.get(positions.get(pos)).setDriverPoints(18);
                driver.drivers.get(positions.get(pos)).setDriverCompletedRaces(1);
                break;
            case 2:
                driver.drivers.get(positions.get(pos)).setDriverPoints(5);
                driver.drivers.get(positions.get(pos)).setDriverCompletedRaces(1);
                break;
            case 3:
                driver.drivers.get(positions.get(pos)).setDriverPoints(12);
                driver.drivers.get(positions.get(pos)).setDriverCompletedRaces(1);
                break;
            case 4:
                driver.drivers.get(positions.get(pos)).setDriverPoints(10);
                driver.drivers.get(positions.get(pos)).setDriverCompletedRaces(1);
                break;
            case 5:
                driver.drivers.get(positions.get(pos)).setDriverPoints(8);
                driver.drivers.get(positions.get(pos)).setDriverCompletedRaces(1);
                break;
            case 6:
                driver.drivers.get(positions.get(pos)).setDriverPoints(6);
                driver.drivers.get(positions.get(pos)).setDriverCompletedRaces(1);
                break;
            case 7:
                driver.drivers.get(positions.get(pos)).setDriverPoints(4);
                driver.drivers.get(positions.get(pos)).setDriverCompletedRaces(1);
                break;
            case 8:
                driver.drivers.get(positions.get(pos)).setDriverPoints(2);
                driver.drivers.get(positions.get(pos)).setDriverCompletedRaces(1);
                break;
            case 9:
                driver.drivers.get(positions.get(pos)).setDriverPoints(1);
                driver.drivers.get(positions.get(pos)).setDriverCompletedRaces(1);
                break;
        }
    }
    public void seasonWinner(){
        driver.sortByPointsDec();
        textOnScreen.setText("<html>" + "- Winner of the 2022 Formula Championship - <BR><BR>" +
                "         " + driver.drivers.get(9).getDriverFirstName() + " " + driver.drivers.get(9).getDriverLastName()
                + " with " + driver.drivers.get(9).getDriverTeam() + "<BR> <BR>");
        gridEditor.anchor = GridBagConstraints.CENTER;
        gridEditor.weightx = 0;
        gridEditor.weighty = 0;
        gridEditor.gridx = 1;
        gridEditor.gridy = 1;
        textOnScreen.setFont(changeFontBold(15));
        panel.add(textOnScreen, gridEditor);

    }
    public void raceHistory() {
        clearScreen();
        if(driver.currentTrack == 1){
            textOnScreen.setText("No races have taken place");
            gridEditor.anchor = GridBagConstraints.PAGE_START;
            gridEditor.weightx = 0;
            gridEditor.weighty = 0;
            gridEditor.gridx = 1;
            gridEditor.gridy = 1;
            textOnScreen.setFont(changeFontBold(15));
            panel.add(textOnScreen, gridEditor);
        }else{
            int driverNum = 0;
            while (driverNum < driver.currentTrack - 1) {
                JLabel location = new JLabel(driver.f1Tracks.get(driverNum+1) + "                            " + raceDates[driverNum]);
                racesHistory.add(location);
                driverNum++;
            }
            for(JLabel loc : racesHistory){
                loc.setFont(changeFont(15));
                panel.add(loc);
            }
        }

    }
    public void driverDetails() {
        clearScreen();
        String dataFile = "src/Coursework/database/race&driverPositions.txt";
        Scanner readDataFile = null;
        try {
            readDataFile = new Scanner(new File(dataFile));
        } catch (Exception e) {
            textOnScreen.setText("File not found.");
        }
        HashMap<String, Integer> f1Drivers = new HashMap<>();
        for (int i = 0; i < driver.MAX_NUM_OF_DRIVERS; i++) {
            f1Drivers.put(driver.drivers.get(i).driverFirstName, i);
        }
        String userChoice = "";

                userChoice = search.getText();
                userChoice = userChoice.toUpperCase();
                if (!f1Drivers.containsKey(userChoice)) {
                    textOnScreen.setText("Please enter a valid driver");
                    gridEditor.anchor = GridBagConstraints.PAGE_START;
                    gridEditor.weightx = 0;
                    gridEditor.weighty = 0;
                    gridEditor.gridx = 1;
                    gridEditor.gridy = 1;
                    textOnScreen.setFont(changeFontBold(15));
                    panel.add(textOnScreen, gridEditor);
                } else {
                    if(driver.currentTrack == 1){
                        textOnScreen.setText("No races have taken place");
                        gridEditor.anchor = GridBagConstraints.PAGE_START;
                        gridEditor.weightx = 0;
                        gridEditor.weighty = 0;
                        gridEditor.gridx = 1;
                        gridEditor.gridy = 1;
                        textOnScreen.setFont(changeFontBold(15));
                        panel.add(textOnScreen, gridEditor);
                    }
                    switch (userChoice) {
                        case "ADAM":
                            int count = 0;
                            int index = 0;
                            int trackAndDate = 0;
                            while (readDataFile.hasNext()) {
                                if(count == 10){
                                    count = 0;
                                }
                                if((readDataFile.nextLine().equals("ADAM"))){
                                    panel.add(showDrivers[index] = new JLabel("<html>" + "ADAM SADEK <BR><BR>" +driver.f1Tracks.get(trackAndDate+1)
                                            + " " + raceDates[trackAndDate] + "<BR>Position- P" + (count+1) + "<BR><BR>"));
                                    showDrivers[index].setFont(changeFont(11));
                                    trackAndDate++;
                                    index++;
                                }
                                count++;
                            }
                            break;
                        case "NADER":
                            count = index = trackAndDate = 0;
                            while (readDataFile.hasNext()) {
                                if(count == 10){
                                    count = 0;
                                }
                                if((readDataFile.nextLine().equals("NADER"))){
                                    panel.add(showDrivers[index] = new JLabel("<html>" + "NADER ARAB <BR><BR>" +driver.f1Tracks.get(trackAndDate+1)
                                            + " " + raceDates[trackAndDate] + "<BR>Position- P" + (count+1) + "<BR><BR>"));
                                    showDrivers[index].setFont(changeFont(11));
                                    trackAndDate++;
                                    index++;
                                }
                                count++;
                            }
                            break;
                        case "SEBASTIAN":
                            count = index = trackAndDate = 0;
                            while (readDataFile.hasNext()) {
                                if(count == 10){
                                    count = 0;
                                }
                                if((readDataFile.nextLine().equals("SEBASTIAN"))){
                                    panel.add(showDrivers[index] = new JLabel("<html>" + "SEBASTIAN VETTEL <BR><BR>" +driver.f1Tracks.get(trackAndDate+1)
                                            + " " + raceDates[trackAndDate] + "<BR>Position- P" + (count+1) + "<BR><BR>"));
                                    showDrivers[index].setFont(changeFont(11));
                                    trackAndDate++;
                                    index++;
                                }
                                count++;
                            }
                            break;
                        case "FERNANDO":
                            count = index = trackAndDate = 0;
                            while (readDataFile.hasNext()) {
                                if(count == 10){
                                    count = 0;
                                }
                                if((readDataFile.nextLine().equals("FERNANDO"))){
                                    panel.add(showDrivers[index] = new JLabel("<html>" + "FERNANDO ALONSO <BR><BR>" +driver.f1Tracks.get(trackAndDate+1)
                                            + " " + raceDates[trackAndDate] + "<BR>Position- P" + (count+1) + "<BR><BR>"));
                                    showDrivers[index].setFont(changeFont(11));
                                    trackAndDate++;
                                    index++;
                                }
                                count++;
                            }
                            break;
                        case "ANTONIO":
                            count = index = trackAndDate = 0;
                            while (readDataFile.hasNext()) {
                                if(count == 10){
                                    count = 0;
                                }
                                if((readDataFile.nextLine().equals("ANTONIO"))){
                                    panel.add(showDrivers[index] = new JLabel("<html>" + "ANTONIO GIOVINAZZI <BR><BR>" +driver.f1Tracks.get(trackAndDate+1)
                                            + " " + raceDates[trackAndDate] + "<BR>Position- P" + (count+1) + "<BR><BR>"));
                                    showDrivers[index].setFont(changeFont(11));
                                    trackAndDate++;
                                    index++;
                                }
                                count++;
                            }
                            break;
                        case "TIMO":
                            count = index = trackAndDate = 0;
                            while (readDataFile.hasNext()) {
                                if(count == 10){
                                    count = 0;
                                }
                                if((readDataFile.nextLine().equals("TIMO"))){
                                    panel.add(showDrivers[index] = new JLabel("<html>" + "TIMO ARAB <BR><BR>" +driver.f1Tracks.get(trackAndDate+1)
                                            + " " + raceDates[trackAndDate] + "<BR>Position- P" + (count+1) + "<BR><BR>"));
                                    showDrivers[index].setFont(changeFont(11));
                                    trackAndDate++;
                                    index++;
                                }
                                count++;
                            }
                            break;
                        case "MAX":
                            count = index = trackAndDate = 0;
                            while (readDataFile.hasNext()) {
                                if(count == 10){
                                    count = 0;
                                }
                                if((readDataFile.nextLine().equals("MAX"))){
                                    panel.add(showDrivers[index] = new JLabel("<html>" + "MAX VERSTAPPEN <BR><BR>" +driver.f1Tracks.get(trackAndDate+1)
                                            + " " + raceDates[trackAndDate] + "<BR>Position- P" + (count+1) + "<BR><BR>"));
                                    showDrivers[index].setFont(changeFont(11));
                                    trackAndDate++;
                                    index++;
                                }
                                count++;
                            }
                            break;
                        case "LEWIS":
                            count = index = trackAndDate = 0;
                            while (readDataFile.hasNext()) {
                                if(count == 10){
                                    count = 0;
                                }
                                if((readDataFile.nextLine().equals("LEWIS"))){
                                    panel.add(showDrivers[index] = new JLabel("<html>" + "LEWIS HAMILTON <BR><BR>" +driver.f1Tracks.get(trackAndDate+1)
                                            + " " + raceDates[trackAndDate] + "<BR>Position- P" + (count+1) + "<BR><BR>"));
                                    showDrivers[index].setFont(changeFont(11));
                                    trackAndDate++;
                                    index++;
                                }
                                count++;
                            }
                            break;
                        case "MICK":
                            count = index = trackAndDate = 0;
                            while (readDataFile.hasNext()) {
                                if(count == 10){
                                    count = 0;
                                }
                                if((readDataFile.nextLine().equals("MICK"))){
                                    panel.add(showDrivers[index] = new JLabel("<html>" + "MICK SCHUMACHER <BR><BR>" +driver.f1Tracks.get(trackAndDate+1)
                                            + " " + raceDates[trackAndDate] + "<BR>Position- P" + (count+1) + "<BR><BR>"));
                                    showDrivers[index].setFont(changeFont(11));
                                    trackAndDate++;
                                    index++;
                                }
                                count++;
                            }
                            break;
                        case "PIERRE":
                            count = index = trackAndDate = 0;
                            while (readDataFile.hasNext()) {
                                if(count == 10){
                                    count = 0;
                                }
                                if((readDataFile.nextLine().equals("PIERRE"))){
                                    panel.add(showDrivers[index] = new JLabel("<html>" + "PIERRE GASLY <BR><BR>" +driver.f1Tracks.get(trackAndDate+1)
                                            + " " + raceDates[trackAndDate] + "<BR>Position- P" + (count+1) + "<BR><BR>"));
                                    showDrivers[index].setFont(changeFont(11));
                                    trackAndDate++;
                                    index++;
                                }
                                count++;
                            }
                            break;
                        default:
                            textOnScreen.setText("Please enter a real driver");
                            gridEditor.anchor = GridBagConstraints.PAGE_START;
                            gridEditor.weightx = 0;
                            gridEditor.weighty = 0;
                            gridEditor.gridx = 1;
                            gridEditor.gridy = 1;
                            textOnScreen.setFont(changeFontBold(15));
                            panel.add(textOnScreen, gridEditor);
                            break;
                    }
                }

    }

    public void actionPerformed(ActionEvent e) {
        int buttonDecOrderClicks = 0, buttonAscOrderClicks = 0, buttonDecOrderWinsClicks = 0;
            if (e.getSource() == buttonDecOrder) {
                buttonAscOrderClicks = 0;
                if (buttonDecOrderClicks == 0) {
                    sortByPointsDec();
                    buttonDecOrderClicks++;
                }
            }
            if (e.getSource() == buttonAscOrder) {
                buttonDecOrderClicks = 0;
                if (buttonAscOrderClicks == 0) {
                    sortByPointsAsc();
                    buttonAscOrderClicks++;
                }
            }
            if (e.getSource() == saveData) {
                try {
                    saveData();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (e.getSource() == loadData) {
                try {
                    loadData();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                importTracks();
            }
            if(e.getSource() ==  buttonWinsDec){
                buttonAscOrderClicks =  buttonDecOrderClicks = 0;
                if (buttonDecOrderWinsClicks == 0) {
                    sortByWinsDec();
                    buttonDecOrderWinsClicks++;
                }
            }
            if(e.getSource() == buttonBoostedRace){
                buttonAscOrderClicks =  buttonDecOrderClicks = buttonDecOrderWinsClicks = 0;
                try {
                    raceDayTopThreeBoost();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (e.getSource() == buttonRace) {
                buttonAscOrderClicks =  buttonDecOrderClicks = buttonDecOrderWinsClicks = 0;
                try {
                    raceDay();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (e.getSource() == buttonReset) {
                buttonAscOrderClicks =  buttonDecOrderClicks = buttonDecOrderWinsClicks = 0;
                try {
                    resetProgress();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (e.getSource() == buttonRaceHistory) {
                buttonAscOrderClicks =  buttonDecOrderClicks = buttonDecOrderWinsClicks = 0;
                raceHistory();
            }
            if(e.getSource() == buttonSubmit){
                buttonAscOrderClicks =  buttonDecOrderClicks = buttonDecOrderWinsClicks = 0;
                driverDetails();
            }



    }
}

