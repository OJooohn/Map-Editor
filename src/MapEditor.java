import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;
import world.World;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class MapEditor extends JFrame {
    private AsciiPanel terminal;
    private char selectedChar = 0; // Caractere selecionado inicialmente
    private final int panelWidth = 50; // Largura do painel AsciiPanel
    private final int panelHeight = 50; // Altura do painel AsciiPanel
    private final int mapWidth = 450;
    private final int mapHeight = 300;
    private char[][] map;
    private int viewX = 0; // Coordenada X da visão
    private int viewY = 0; // Coordenada Y da visão
    private int previewX = -1;
    private int previewY = -1;
    private JLabel positionLabel; // Label para mostrar as coordenadas

    public MapEditor() {
        super("Map Editor");
        setResizable(false);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        map = new char[mapWidth][mapHeight];
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                if((x == 0 || y == 0) || (x == (mapWidth - 1) || y == (mapHeight - 1)))
                    map[x][y] = (char) 171;
                else
                    map[x][y] = (char) 0;
            }
        }

        // Configurar o painel AsciiPanel
        terminal = new AsciiPanel(panelWidth, panelHeight, AsciiFont.CP437_16x16);
        terminal.setBorder(new LineBorder(Color.RED, 1)); // Adiciona uma borda vermelha de 1 pixel ao terminal
        terminal.setFocusable(true);

        // Configurar o contêiner do terminal para garantir que a borda apareça corretamente
        JPanel terminalPanel = new JPanel(new BorderLayout());
        terminalPanel.setBorder(new LineBorder(Color.getHSBColor(0, 0, 0.3f), 5));
        terminalPanel.add(terminal, BorderLayout.CENTER);

        add(terminalPanel, BorderLayout.CENTER);

        // Configurar o banco de caracteres
        JPanel charBankPanel = new JPanel();
        charBankPanel.setLayout(new GridLayout(16, 16));
        for (int i = 0; i < 256; i++) {
            char c = (char) i;
            JButton charButton = new JButton();
            charButton.setBackground(Color.BLACK);

            charButton.setText(null);

            // Carregar a imagem correspondente ao caractere
            String imagePath = "images/" + i + ".png";
            File imageFile = new File(imagePath);

            ImageIcon icon = new ImageIcon(imagePath);

            Image img = icon.getImage();
            Image resized = img.getScaledInstance(48, 48, java.awt.Image.SCALE_SMOOTH);

            ImageIcon resizedIcon = new ImageIcon(resized);

            charButton.setIcon(resizedIcon);

            charButton.setPreferredSize(new Dimension(resizedIcon.getIconWidth(), resizedIcon.getIconHeight()));
            charButton.setMinimumSize(new Dimension(resizedIcon.getIconWidth(), resizedIcon.getIconHeight()));
            charButton.setMaximumSize(new Dimension(resizedIcon.getIconWidth(), resizedIcon.getIconHeight()));

            charButton.setOpaque(false);
            charButton.setContentAreaFilled(false);

            charButton.addActionListener(e -> selectedChar = c);
            charBankPanel.add(charButton);
        }
        JScrollPane scrollPane = new JScrollPane(charBankPanel);
        scrollPane.setBorder(new LineBorder(Color.getHSBColor(0, 0, 0.3f), 3));

        scrollPane.getHorizontalScrollBar().setUnitIncrement(18);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);

        scrollPane.setPreferredSize(new Dimension(774, 200));

        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.getHSBColor(0, 0.35f, 1);
                this.trackColor = Color.getHSBColor(0, 0, 0.5f);
            }
        });
        scrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        add(scrollPane, BorderLayout.WEST);

        // Adicionar painel à direita para mostrar as coordenadas
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        positionLabel = new JLabel("Position: (000, 000)");
        positionLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centraliza o texto horizontalmente
        positionLabel.setVerticalAlignment(SwingConstants.CENTER); // Centraliza o texto verticalmente
        positionLabel.setForeground(Color.GREEN);
        try {
            InputStream is = getClass().getResourceAsStream("/consola.ttf");
            Font unispaceFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(15f);
            positionLabel.setFont(unispaceFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        infoPanel.setBorder(new LineBorder(Color.getHSBColor(0, 0, 0.3f), 3));
        infoPanel.setBackground(Color.BLACK);

        infoPanel.add(positionLabel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.EAST);

        // Adicionar ouvinte de mouse para o painel AsciiPanel
        terminal.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / terminal.getCharWidth() + viewX;
                int y = e.getY() / terminal.getCharHeight() + viewY;
                if (x < mapWidth && y < mapHeight) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        map[x][y] = selectedChar;
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        map[x][y] = 0;
                    }
                    renderView();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                previewX = -1;
                previewY = -1;
                updatePositionLabel(-1, -1);
                renderView();
            }
        });

        terminal.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX() / terminal.getCharWidth() + viewX;
                int y = e.getY() / terminal.getCharHeight() + viewY;
                if (x < mapWidth && y < mapHeight) {
                    previewX = x;
                    previewY = y;
                    updatePositionLabel(x, y);
                    renderView();
                }
            }
        });

        // Adicionar ouvinte de teclado para movimentação e criação do objeto World
        terminal.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: // Move up
                        if (viewY > 0) viewY--;
                        break;
                    case KeyEvent.VK_S: // Move down
                        if (viewY < mapHeight - panelHeight) viewY++;
                        break;
                    case KeyEvent.VK_A: // Move left
                        if (viewX > 0) viewX--;
                        break;
                    case KeyEvent.VK_D: // Move right
                        if (viewX < mapWidth - panelWidth) viewX++;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                    case KeyEvent.VK_ENTER: // Create World object and save to file
                        createAndSaveWorld();
                        break;
                }
                // Limpar as coordenadas de pré-visualização ao mover a matriz
                previewX = -1;
                previewY = -1;
                updatePositionLabel(-1, -1);
                renderView();
            }
        });

        pack();
        setVisible(true);
        terminal.requestFocusInWindow(); // Certificar-se de que o terminal está em foco
        renderView();
    }

    private void updatePositionLabel(int x, int y) {
        if (x >= 0 && y >= 0) {
            positionLabel.setText(String.format("Position: (%03d, %03d)", x, y));
        } else {
            positionLabel.setText("Position: (N/A, N/A)");
        }
    }

    private void renderView() {
        terminal.clear();
        for (int x = 0; x < panelWidth; x++) {
            for (int y = 0; y < panelHeight; y++) {
                if (x + viewX < mapWidth && y + viewY < mapHeight) {
                    terminal.write(map[x + viewX][y + viewY], x, y, Color.WHITE, Color.BLACK);
                }
            }
        }
        // Desenhar o caractere de pré-visualização
        if (previewX >= 0 && previewY >= 0 && previewX < mapWidth && previewY < mapHeight) {
            terminal.write(selectedChar, previewX - viewX, previewY - viewY, Color.LIGHT_GRAY);
        }
        terminal.repaint();
        terminal.requestFocusInWindow(); // Certificar-se de que o terminal está em foco após cada atualização
    }

    private void createAndSaveWorld() {
        // Criação do objeto World com a largura, altura e matriz do mapa
        World world = new World(mapWidth, mapHeight);
        world.setTiles(map);
        System.out.println("Objeto World criado com sucesso!");

        // Salvar o objeto World em um arquivo usando OutputStream
        saveWorldToFile(world, "world.save");
    }

    private void saveWorldToFile(World world, String fileName) {
        try (FileOutputStream file = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(file)) {
            out.writeObject(world);
            System.out.println("World salvo em " + fileName);
        } catch (IOException e) {
            System.err.println("Erro ao salvar o mundo: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MapEditor::new);
    }
}
