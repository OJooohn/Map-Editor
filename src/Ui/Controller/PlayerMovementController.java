package Ui.Controller;

import Entity.Aliado;
import Entity.Entidade;
import Entity.Player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Queue;

public class PlayerMovementController {

    private final Player player;
    private final ArrayList<Aliado> aliados = new ArrayList<>();

    public PlayerMovementController(Player player, ArrayList<Entidade> entidades) {
        this.player = player;
        for (Entidade entidade : entidades) {
            if (entidade instanceof Aliado) {
                aliados.add((Aliado) entidade);
            }
        }
    }

    public int getPlayerX() {
        return player.getX();
    }
    public int getPlayerY() {
        return player.getY();
    }

    public void processKeyEvent(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()){
            case KeyEvent.VK_W:
                player.moveUp();
                break;
            case KeyEvent.VK_S:
                player.moveDown();
                break;
            case KeyEvent.VK_A:
                player.moveLeft();
                break;
            case KeyEvent.VK_D:
                player.moveRight();
                break;

        }
        Queue<Point> posicoesAnteriores = player.getPosicoes();
        for(Aliado aliado : aliados) {
            aliado.seguirJogador(posicoesAnteriores);
        }
    }
}