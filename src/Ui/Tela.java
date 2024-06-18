package Ui;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

public class Tela implements ComandosTela{

    private int width;
    private int height;
    protected char[][] tiles;

    private AsciiPanel tela;
    private final int FONT_SIZE;

    public Tela(int width, int height, AsciiFont font, int FONT_SIZE) {
        this.width = width;
        this.height = height;
        this.tiles = new char[width][height];
        this.FONT_SIZE = FONT_SIZE;
        this.tela = new AsciiPanel(width, height, font);
        this.tela.setSize(width * FONT_SIZE, height * FONT_SIZE);
        this.tela.setBounds(width * FONT_SIZE, height * FONT_SIZE, width * FONT_SIZE, height * FONT_SIZE);

        tiles = new char[width][height];
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                tiles[i][j] = ' ';
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public AsciiPanel getTela() {
        return tela;
    }

    public void setTela(AsciiPanel tela) {
        this.tela = tela;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public char[][] getTiles() {
        return tiles;
    }
    public void setTiles(char[][] tiles) {
        this.tiles = tiles;
    }

    @Override
    public void setBounds(int x, int y) {
        tela.setBounds(x, y, this.width * FONT_SIZE, this.height * FONT_SIZE);
    }

    @Override
    public void clear() {
        tela.clear();
    }
}
