package Ui;

import Entity.Entidade;
import Entity.Player;
import asciiPanel.AsciiFont;
import world.World;

import java.awt.*;
import java.util.List;

public class TelaDeJogo extends Tela {

    public TelaDeJogo(int width, int height, AsciiFont font, int FONT_SIZE) {
        super(width, height, font, FONT_SIZE);
    }

    public void printTexto(String texto, int x, int y){
        getTela().write(texto, x, y);
        // getTela().write("TELA DE JOGO 45 x 30", x, y);
        // getTela().write("45 x 30", x, y + 1);
    }

    public void printMundo(char[][] dungeonTiles, Player player, List<Entidade> entidades, World world){
            lookAt(dungeonTiles, player, entidades, world);
    }

    public Point GetCameraOrigin(int xPlayer, int yPlayer, World world) {
        // Calculo para caso, o player esteja nas extremidades do mapa, nao ocorra nenhum erro de OutOfBounds
        int x = Math.max(0, Math.min(xPlayer - this.getWidth() / 2, world.getWidth() - 45));
        int y = Math.max(0, Math.min(yPlayer - this.getHeight() / 2, world.getHeight() - 30));
        return new Point(x, y);
    }

    public void lookAt(char[][] dungeonTiles, Player player, List<Entidade> entidades, World world){

        char tile;
        Point origin;

        // Vai calcular o ponto de origem da camera, baseado na posicao do player
        origin = GetCameraOrigin(player.getX(), player.getY(), world);

        for (int x = 0; x < getWidth(); x++){
            for (int y = 0; y < getHeight(); y++){
                tile = dungeonTiles[origin.x + x][origin.y + y];

                boolean isEntityPresent = false;
                for (Entidade entidade : entidades) {
                    if (entidade.getX() == (origin.x + x) && entidade.getY() == (origin.y + y)) {
                        getTela().write((char) entidade.getIcone(), x, y);
                        isEntityPresent = true;
                        break;  // Exit the loop once an entity is found at this position
                    }
                }
                if (!isEntityPresent) {
                    getTela().write(tile, x, y);
                }
            }
        }

    }
}
