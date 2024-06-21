import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

public class TerminalMapa {

    private AsciiPanel terminal; // Terminal ASCII
    private final int panelWidth = 50; // Largura do painel AsciiPanel
    private final int panelHeight = 50; // Altura do painel AsciiPanel

    public TerminalMapa() {
        terminal = new AsciiPanel(panelWidth, panelHeight, AsciiFont.CP437_16x16);
        terminal.setFocusable(true);
    }

    public int getPanelHeight() {
        return panelHeight;
    }

    public int getPanelWidth() {
        return panelWidth;
    }

    public AsciiPanel getTerminal() {
        return terminal;
    }

    public void setTerminal(AsciiPanel terminal) {
        this.terminal = terminal;
    }
}
