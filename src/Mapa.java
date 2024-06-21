import java.awt.*;

public class Mapa {

    private final int mapWidth = 450;
    private final int mapHeight = 300;

    private char[][] tiles;
    private Color[][] foregroundColors; // Matriz para armazenar as cores do caractere
    private Color[][] backgroundColors; // Matriz para armazenar as cores de fundo do caractere

    public Mapa() {
        this.tiles = new char[mapWidth][mapHeight];
        this.foregroundColors = new Color[mapWidth][mapHeight];
        this.backgroundColors = new Color[mapWidth][mapHeight];
    }

    public Color[][] getBackgroundColors() {
        return backgroundColors;
    }

    public void setBackgroundColors(Color[][] backgroundColors) {
        this.backgroundColors = backgroundColors;
    }

    public Color[][] getForegroundColors() {
        return foregroundColors;
    }

    public void setForegroundColors(Color[][] foregroundColors) {
        this.foregroundColors = foregroundColors;
    }

    public char[][] getTiles() {
        return tiles;
    }

    public void setTiles(char[][] tiles) {
        this.tiles = tiles;
    }

    public int getHeight() {
        return mapHeight;
    }

    public int getWidth() {
        return mapWidth;
    }

    public char getCharAt(int x, int y){
        return tiles[x][y];
    }

    public void setCharAt(int x, int y, char c){
        tiles[x][y] = c;
    }

    public Color getForegroundAt(int x, int y){
        return foregroundColors[x][y];
    }

    public void setForegorundAt(int x, int y, Color color){
        foregroundColors[x][y] = color;
    }

    public Color getBackgroundAt(int x, int y){
        return backgroundColors[x][y];
    }

    public void setBackgroudAt(int x, int y, Color color){
        backgroundColors[x][y] = color;
    }

}
