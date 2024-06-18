package Entity;

import java.awt.*;
import java.util.Queue;

public class Aliado extends Entidade{
    private Player player;


    public Aliado(String Nome, int x, int y, int icone, Player player) {
        super(true, Nome, x, y,icone);
        this.player = player;
    }

    public void seguirJogador(Queue<Point> posicoesAnteriores) {
        if (!posicoesAnteriores.isEmpty()) {
            Point proximaPosicao = posicoesAnteriores.poll();
            if (proximaPosicao != null) {
                setX(proximaPosicao.x);
                setY(proximaPosicao.y);
            }
        }
    }
    }

