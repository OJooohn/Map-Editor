package Ui;

import asciiPanel.AsciiFont;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Queue;

public class Interface extends JPanel implements KeyListener{

    private Queue<InputEvent> inputQueue;

    /* Declarando as telas do jogo */
    private final TelaDeJogo telaDeJogo;
    private final StatusJogador statusJogador;
    private final RelatorioJogo relatorioJogo;
    private final MiniMapa miniMapa;

    /* Tamanhos dos painéis */

    /* - Tamanho da Tela do Jogo */
    private final int telaDeJogoWidth = 45; // largura do painel ASCII (em caracteres)
    private final int telaDeJogoHeight = 30;

    /* - Tamanho da Tela do Status do Jogador */
    private final int statusJogadorWidth = 80;
    private final int statusJogadorHeight = 12;

    /* - Tamanho da Tela do Relatório do Jogo */
    private final int relatorioJogoWidth = 49;
    private final int relatorioJogoHeight = 30;

    /* - Tamanho da Tela do Mini Mapa */
    private final int miniMapaWidth = 44;
    private final int miniMapaHeight = 32;

    /* Tamanho das fontes */
    private static final int FONT_AEROSMATICA_SIZE = 16;
    private static final int FONT_DRAKE_SIZE = 9;

    public Interface() {
        this.inputQueue = new LinkedList<>();

        this.setFocusable(true); // Para o KeyListener funcionar - Quando o painel é focado, ele pode receber eventos de teclado

        /* Inicializando as telas do jogo e adicionando no painel */
        telaDeJogo = new TelaDeJogo(telaDeJogoWidth, telaDeJogoHeight, AsciiFont.CP437_16x16, FONT_AEROSMATICA_SIZE);
        this.add(telaDeJogo.getTela());
        statusJogador = new StatusJogador(statusJogadorWidth, statusJogadorHeight, AsciiFont.DRAKE_10x10, FONT_DRAKE_SIZE);
        this.add(statusJogador.getTela());
        relatorioJogo = new RelatorioJogo(relatorioJogoWidth, relatorioJogoHeight, AsciiFont.DRAKE_10x10, FONT_DRAKE_SIZE);
        this.add(relatorioJogo.getTela());

        miniMapa = new MiniMapa(miniMapaWidth, miniMapaHeight, AsciiFont.CP437_10x10 , 10);
        this.add(miniMapa.getTela());

        super.addKeyListener(this); // Adicionando o KeyListener ao painel - "Registrando" a classe Interface como ouvinte de eventos de teclado
    }

    public MiniMapa getMiniMapa() { return miniMapa; }

    public int getMiniMapaHeight() { return miniMapaHeight; }

    public int getMiniMapaWidth() { return miniMapaWidth; }

    public RelatorioJogo getRelatorioJogo() { return relatorioJogo; }

    public int getRelatorioJogoHeight() { return relatorioJogoHeight; }

    public int getRelatorioJogoWidth() { return relatorioJogoWidth; }

    public StatusJogador getStatusJogador() { return statusJogador; }

    public int getStatusJogadorHeight() { return statusJogadorHeight; }

    public int getStatusJogadorWidth() { return statusJogadorWidth; }

    public TelaDeJogo getTelaDeJogo() { return telaDeJogo; }

    public int getTelaDeJogoHeight() { return telaDeJogoHeight; }

    public int getTelaDeJogoWidth() { return telaDeJogoWidth; }

    public InputEvent getNextInput() { return inputQueue.poll(); }

    public void refresh(){
        telaDeJogo.getTela().repaint();
        relatorioJogo.getTela().repaint();
        statusJogador.getTela().repaint();
        miniMapa.getTela().repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Desenha fundo preto
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Desenha as bordas das telas
        g.setColor(Color.GRAY);

        // Tela de jogo
        g.fillRect(20, 20, telaDeJogoWidth * FONT_AEROSMATICA_SIZE + 20, telaDeJogoHeight * FONT_AEROSMATICA_SIZE + 20);

        // Status jogador
        g.fillRect(20, 535, statusJogadorWidth * FONT_DRAKE_SIZE + 20, statusJogadorHeight * FONT_DRAKE_SIZE + 20);

        // Relatório jogo
        g.fillRect(780, 20, relatorioJogoWidth * FONT_DRAKE_SIZE + 20, relatorioJogoHeight * FONT_DRAKE_SIZE + 20);

        // Mini mapa
        g.fillRect(780, 325, miniMapaWidth * 10 + 20, miniMapaHeight * 10 + 20);

        telaDeJogo.setBounds(30, 30);
        statusJogador.setBounds(30, 545);
        relatorioJogo.setBounds(790, 30);
        miniMapa.setBounds(790, 333);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        inputQueue.add(e);
        System.out.println("Pressed Key: " + e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }

}
