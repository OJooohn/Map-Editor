import world.Tiles;

import java.awt.*;

public class Mapa {

    private final int mapWidth = 450;
    private final int mapHeight = 300;

    private Tiles[][] tiles;

    public Mapa() {
        this.tiles = new Tiles[mapWidth][mapHeight];
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                tiles[x][y] = new Tiles((char) 0, true, Color.WHITE, Color.BLACK);
            }
        }
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

}
