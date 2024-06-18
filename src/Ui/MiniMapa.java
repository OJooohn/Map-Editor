package Ui;

import Entity.Player;
import asciiPanel.AsciiFont;
import world.World;

import java.awt.*;

public class MiniMapa extends Tela {

    public MiniMapa(int width, int height, AsciiFont font, int FONT_SIZE) {
        super(width, height, font, FONT_SIZE);
    }

    public void printMiniMapa(World world, Player player){
        char[][] tile = new char[getWidth()][getHeight()];

        float proporcaoX = (float) getWidth() / world.getWidth();
        float proporcaoY = (float) getHeight() / world.getHeight();

        int xLinha = (int) (player.getX() * proporcaoX);
        int yLinha = (int) (player.getY() * proporcaoY);

        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                tile[i][j] = (char)253;
            }
        }

        int x = 0, y = 0;

        for(int j = 0; j < getHeight(); j++, y++){
            x = 0;
            for (int i = 0; i < getWidth(); i++, x++) {
                if(x == xLinha && y == yLinha)
                    getTela().write((char)254, x, y, Color.GREEN, Color.BLACK);
                else
                    getTela().write(tile[i][j], x, y, Color.WHITE, Color.BLACK);
            }
        }
    }

    public void printTela(String texto, int x, int y){
        getTela().write("MINI MAPA", x, y);
        getTela().write("49 x 35", x, y + 1);
        getTela().repaint();
    }
}
