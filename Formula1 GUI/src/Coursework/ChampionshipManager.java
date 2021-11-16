package Coursework;

import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

/*
    Author: Adam Sadek
    ID:     w1738889
 */
interface ChampionshipManager {
    // used to group related methods with empty bodies
    void saveData() throws IOException;
    void loadData() throws IOException;
    void importTracks();
    void seasonWinner();
    void sortByPointsAsc();
}
