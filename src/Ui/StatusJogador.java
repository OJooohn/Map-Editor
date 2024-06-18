package Ui;

import asciiPanel.AsciiFont;

public class StatusJogador extends Tela{

    public StatusJogador(int width, int height, AsciiFont font, int FONT_SIZE) {
        super(width, height, font, FONT_SIZE);
    }

    public void printTela(String texto, int x, int y){
        getTela().write(texto, x, y);
        // getTela().write(getWidth() + " x " + getHeight(), 1, 2);
    }
}
