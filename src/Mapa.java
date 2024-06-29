import world.Enemy;
import world.EnemyOnMap;
import world.Tiles;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Mapa implements Serializable {

    private final int mapWidth = 450;
    private final int mapHeight = 300;

    private Tiles[][] tiles;
    private ArrayList<EnemyOnMap> inimigosOnMap;

    public Mapa() {
        this.tiles = new Tiles[mapWidth][mapHeight];
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                tiles[x][y] = new Tiles((char) 0, true, Color.WHITE, Color.BLACK);
            }
        }
        this.inimigosOnMap = new ArrayList<>();
    }

    public void adicionarInimigoOnMap(int x, int y, char icon, String className){
        inimigosOnMap.add(new EnemyOnMap(icon, x, y, className));
    }

    public Tiles[][] getTiles() {
        return tiles;
    }

    public void setTiles(Tiles[][] tiles) {
        this.tiles = tiles;
    }

    public int getHeight() {
        return mapHeight;
    }

    public int getWidth() {
        return mapWidth;
    }

    public char getCharAt(int x, int y){
        return tiles[x][y].getIcon();
    }

    public void setCharAt(int x, int y, char c){
        tiles[x][y].setIcon(c);
    }

    public Color getForegroundAt(int x, int y){
        return tiles[x][y].getForegroundColor();
    }

    public void setForegorundAt(int x, int y, Color color){
        tiles[x][y].setForegroundColor(color);
    }

    public Color getBackgroundAt(int x, int y){
        return tiles[x][y].getBackgroundColor();
    }

    public void setBackgroudAt(int x, int y, Color color){
        tiles[x][y].setBackgroundColor(color);
    }

    public ArrayList<EnemyOnMap> getInimigosOnMap() {
        return inimigosOnMap;
    }

    public void setInimigosOnMap(ArrayList<EnemyOnMap> inimigosOnMap) {
        this.inimigosOnMap = inimigosOnMap;
    }
}
