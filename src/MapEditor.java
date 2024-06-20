import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;
import world.World;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MapEditor extends JFrame {

    private AsciiPanel terminal;
    private char selectedChar = 0; // Caractere selecionado inicialmente
    private Color selectedForegroundColor = Color.WHITE; // Cor do caractere selecionado
    private Color selectedBackgroundColor = Color.BLACK; // Cor de fundo do caractere selecionado
    private final int panelWidth = 50; // Largura do painel AsciiPanel
    private final int panelHeight = 50; // Altura do painel AsciiPanel
    private final int mapWidth = 450;
    private final int mapHeight = 300;
    private char[][] map;
    private Color[][] foregroundColors; // Matriz para armazenar as cores do caractere
    private Color[][] backgroundColors; // Matriz para armazenar as cores de fundo do caractere
    private int viewX = 0; // Coordenada X da visão
    private int viewY = 0; // Coordenada Y da visão
    private int previewX = -1;
    private int previewY = -1;
    private int startX = -1; // Coordenada X do início do arrasto
    private int startY = -1; // Coordenada Y do início do arrasto
    private int currentX = -1; // Coordenada X atual durante o arrasto
    private int currentY = -1; // Coordenada Y atual durante o arrasto
    private JLabel positionLabel; // Label para mostrar as coordenadas
    private JTextArea logTextArea; // TextArea para mostrar os logs
    private boolean isLeftMouseButton = false; // Estado do botão esquerdo do mouse
    private boolean isRightMouseButton = false; // Estado do botão direito do mouse
    private boolean isCtrlPressed = false; // Estado da tecla Ctrl
    private World dungeonMap;
    private Map<String, Color> colorMap; // Mapa para associar nomes de cores às cores reais

    private boolean isForegroundSelected = true; // Novo: Estado para modificar cor do caractere

    public void readWorld() {
        String userHome = System.getProperty("user.home");
        String desktopPath = userHome + File.separator + "Desktop";

        Path worldPath = Paths.get(desktopPath + "\\World\\world.save").getParent();

        File newFolder = new File(desktopPath + "\\World");

        if (newFolder.exists()) {
            if (Files.exists(worldPath)) {
                try {
                    ObjectInputStream os = new ObjectInputStream(new FileInputStream(desktopPath + "\\World\\world.save"));
                    dungeonMap = (World) os.readObject();
                    showPopup("Mundo carregado", desktopPath + "\\World\\world.save" + " carregado com sucesso!");

                    map = dungeonMap.getTiles();
                    foregroundColors = dungeonMap.foregroundColor();
                    backgroundColors = dungeonMap.backgroundColor();
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            } else {
                showPopup("Erro ao carregar mundo", "Arquivo nao encontrado: " + desktopPath + "\\World\\world.save");
            }

        } else {
            showPopup("Erro ao carregar mundo", "Caminho nao encontrado: " + desktopPath + "\\World");
            newFolder.mkdirs();
        }
    }

    public MapEditor() {
        super("Map Editor");
        setResizable(false);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        map = new char[mapWidth][mapHeight];
        foregroundColors = new Color[mapWidth][mapHeight];
        backgroundColors = new Color[mapWidth][mapHeight];
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                map[x][y] = (char) 0;
                foregroundColors[x][y] = Color.WHITE;
                backgroundColors[x][y] = Color.BLACK;
            }
        }
        // White Default Border
        fillBorder((char) 171, Color.WHITE, Color.BLACK);

        // Configurar o painel AsciiPanel
        terminal = new AsciiPanel(panelWidth, panelHeight, AsciiFont.CP437_16x16);
        terminal.setFocusable(true);

        // Configurar o contêiner do terminal para garantir que a borda apareça corretamente
        JPanel terminalPanel = new JPanel(new BorderLayout());
        terminalPanel.setBorder(new LineBorder(Color.getHSBColor(0.7f, 0.5f, 1), 5));
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

        scrollPane.getHorizontalScrollBar().setUnitIncrement(18);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);

        scrollPane.setBorder(new MatteBorder(5, 5, 5, 0, Color.getHSBColor(0.7f, 0.5f, 1)));

        scrollPane.setPreferredSize(new Dimension(778, 200));

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

        // Adicionar painel à direita para mostrar as coordenadas e logs
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        positionLabel = new JLabel("Position: (000, 000)");
        positionLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centraliza o texto horizontalmente
        positionLabel.setVerticalAlignment(SwingConstants.CENTER); // Centraliza o texto verticalmente
        positionLabel.setForeground(Color.WHITE);
        try {
            InputStream is = getClass().getResourceAsStream("/SpaceMono-Regular.ttf");
            Font unispaceFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(15f);
            positionLabel.setFont(unispaceFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        positionLabel.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(5, 0, 5, 5, Color.getHSBColor(0.7f, 0.5f, 1)),
                new EmptyBorder(10, 0, 10, 0)
        ));


        infoPanel.setBackground(Color.BLACK);
        infoPanel.add(positionLabel, BorderLayout.NORTH);

        // Adicionar JTextArea para logs
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        logTextArea.setBackground(Color.BLACK);
        logTextArea.setForeground(Color.WHITE);
        try {
            InputStream is = getClass().getResourceAsStream("/SpaceMono-Regular.ttf");
            Font unispaceFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(12f);
            logTextArea.setFont(unispaceFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        logTextArea.append("----------- CONTROLES MAPA ----------\n");
        logTextArea.append("\n");
        logTextArea.append("W: Cima\n");
        logTextArea.append("A: Esquerda\n");
        logTextArea.append("S: Baixo\n");
        logTextArea.append("D: Direita\n");
        logTextArea.append("\n");

        logTextArea.append("---------- MAPA UTILIDADES ----------\n");
        logTextArea.append("\n");
        logTextArea.append("ENTER: Salvar Mapa\n");
        logTextArea.append("R: Carregar Mapa\n");
        logTextArea.append("\n");

        logTextArea.append("------- CARACTERE UTILIDADES -------\n");
        logTextArea.append("\n");
        logTextArea.append("Q: Mudar Caractere Borda\n");
        logTextArea.append("\n");
        logTextArea.append("Mouse Esquerdo: Adicionar Caractere\n");
        logTextArea.append("Segurar: Retangulo nao preenchido\n");
        logTextArea.append(" + CTRL: Retangulo preenchido\n");
        logTextArea.append("\n");
        logTextArea.append("Mouse Meio: \n");
        logTextArea.append("Selecionar Caractere na Posicao\n");
        logTextArea.append("\n");
        logTextArea.append("Mouse Direito: Remover Caractere\n");
        logTextArea.append("Segurar: Remover Area (Retangulo)\n");


        JScrollPane logScrollPane = new JScrollPane(logTextArea);
        logScrollPane.setBorder(new LineBorder(Color.BLACK, 1));

        // Adicionar margem ao JTextArea
        logTextArea.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 5, 5, Color.getHSBColor(0.7f, 0.5f, 1)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        infoPanel.add(logScrollPane, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);

        // Criar e adicionar a paleta de cores
        JPanel colorPalettePanel = new JPanel();
        colorPalettePanel.setLayout(new GridLayout(4, 7));
        String[] colorNames = {
                "Branco", "Amarelo", "Dourado", "Laranja", "Rosa", "Magenta", "Salmão", "Bege",
                "Marrom Claro", "Prata", "Verde Claro", "Vermelho", "Cinza Claro", "Azul Claro",
                "Oliva", "Roxo", "Marrom", "Cinza Escuro", "Turquesa", "Verde", "Aqua", "Lime",
                "Teal", "Verde Escuro", "Azul", "Azul Escuro", "Navy", "Preto"
        };

        Color[] colors = {
                new Color(255, 255, 255), new Color(255, 255, 0), new Color(255, 215, 0), new Color(255, 140, 0), new Color(255, 105, 180), new Color(107, 0, 107), new Color(250, 128, 114), new Color(245, 245, 220), new Color(205, 133, 63), new Color(192, 192, 192), new Color(144, 238, 144), new Color(139, 0, 0), new Color(136, 136, 136), new Color(135, 206, 250), new Color(128, 128, 0), new Color(128, 0, 128), new Color(114, 56, 0, 255), new Color(72, 72, 72), new Color(64, 224, 208), new Color(93, 176, 3), new Color(0, 255, 255), new Color(0, 255, 0), new Color(61, 130, 117), new Color(51, 139, 0), new Color(0, 0, 255), new Color(0, 0, 139), new Color(0, 0, 128), new Color(0, 0, 0)
        };
        colorMap = new HashMap<>();
        for (int i = 0; i < colorNames.length; i++) {
            colorMap.put(colorNames[i], colors[i]);
        }

        for (String colorName : colorNames) {
            Color color = colorMap.get(colorName);
            JButton colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.setOpaque(true);
            colorButton.setBorderPainted(false);
            colorButton.setPreferredSize(new Dimension(40, 40));
            colorButton.addActionListener(e -> {
                if (isForegroundSelected) {
                    selectedForegroundColor = color;
                } else {
                    selectedBackgroundColor = color;
                }
                renderView(); // Atualiza a visualização para refletir as mudanças na cor selecionada
            });
            colorPalettePanel.add(colorButton);
        }

        JPanel controlsPanel = new JPanel(new BorderLayout());

        // Adicionar botões para selecionar o modo de edição
        JPanel modePanel = new JPanel(new GridLayout(1, 2));
        JButton foregroundButton = new JButton("Foreground");
        JButton backgroundButton = new JButton("Background");

        foregroundButton.addActionListener(e -> isForegroundSelected = true);

        backgroundButton.addActionListener(e -> isForegroundSelected = false);

        modePanel.add(foregroundButton);
        modePanel.add(backgroundButton);

        controlsPanel.add(modePanel, BorderLayout.NORTH);
        controlsPanel.add(colorPalettePanel, BorderLayout.SOUTH);
        infoPanel.add(controlsPanel, BorderLayout.SOUTH);

        // Adicionar ouvinte de mouse para o painel AsciiPanel
        terminal.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    startX = e.getX() / terminal.getCharWidth() + viewX;
                    startY = e.getY() / terminal.getCharHeight() + viewY;
                    isLeftMouseButton = true;
                    isRightMouseButton = false;
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    startX = e.getX() / terminal.getCharWidth() + viewX;
                    startY = e.getY() / terminal.getCharHeight() + viewY;
                    isLeftMouseButton = false;
                    isRightMouseButton = true;
                } else if (SwingUtilities.isRightMouseButton(e) && SwingUtilities.isLeftMouseButton(e)) {
                    startX = e.getX() / terminal.getCharWidth() + viewX;
                    startY = e.getY() / terminal.getCharHeight() + viewY;
                    isLeftMouseButton = true;
                    isRightMouseButton = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isCtrlPressed && isLeftMouseButton) {
                    int endX = e.getX() / terminal.getCharWidth() + viewX;
                    int endY = e.getY() / terminal.getCharHeight() + viewY;
                    drawRectangleFull(startX, startY, endX, endY);
                    startX = -1;
                    startY = -1;
                    currentX = -1;
                    currentY = -1;
                    isCtrlPressed = false;
                    renderView();
                } else if (isLeftMouseButton && startX >= 0 && startY >= 0) {
                    int endX = e.getX() / terminal.getCharWidth() + viewX;
                    int endY = e.getY() / terminal.getCharHeight() + viewY;
                    drawRectangle(startX, startY, endX, endY);
                    startX = -1;
                    startY = -1;
                    currentX = -1;
                    currentY = -1;
                    isLeftMouseButton = false;
                    renderView();
                } else if (isRightMouseButton && startX >= 0 && startY >= 0) {
                    int endX = e.getX() / terminal.getCharWidth() + viewX;
                    int endY = e.getY() / terminal.getCharHeight() + viewY;
                    deleteInRectangle(startX, startY, endX, endY);
                    startX = -1;
                    startY = -1;
                    currentX = -1;
                    currentY = -1;
                    isRightMouseButton = false;
                    renderView();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / terminal.getCharWidth() + viewX;
                int y = e.getY() / terminal.getCharHeight() + viewY;
                if (x < mapWidth && y < mapHeight) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        map[x][y] = selectedChar;
                        foregroundColors[x][y] = selectedForegroundColor;
                        backgroundColors[x][y] = selectedBackgroundColor;
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        map[x][y] = (char) 0;
                        foregroundColors[x][y] = Color.WHITE;
                        backgroundColors[x][y] = Color.BLACK;
                    } else if (SwingUtilities.isMiddleMouseButton(e)) {
                        selectedChar = map[x][y];
                        selectedForegroundColor = foregroundColors[x][y];
                        selectedBackgroundColor = backgroundColors[x][y];
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

            @Override
            public void mouseDragged(MouseEvent e) {
                if (startX >= 0 && startY >= 0) {
                    currentX = e.getX() / terminal.getCharWidth() + viewX;
                    currentY = e.getY() / terminal.getCharHeight() + viewY;
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
                    case KeyEvent.VK_Q:
                        fillBorder(selectedChar, selectedForegroundColor, selectedBackgroundColor);
                        break;
                    case KeyEvent.VK_R:
                        readWorld();
                        break;
                    case KeyEvent.VK_CONTROL: // Ctrl key pressed
                        isCtrlPressed = true;
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

        terminal.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    isCtrlPressed = false;
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

    public void fillBorder(char character, Color selectedForegroundColor, Color selectedBackgroundColor) {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                if ((x == 0 || y == 0) || (x == (mapWidth - 1) || y == (mapHeight - 1))){
                    map[x][y] = character;
                    foregroundColors[x][y] = selectedForegroundColor;
                    backgroundColors[x][y] = selectedBackgroundColor;
                }
            }
        }
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
                    terminal.write(map[x + viewX][y + viewY], x, y, foregroundColors[x + viewX][y + viewY], backgroundColors[x + viewX][y + viewY]);
                }
            }
        }
        // Desenhar o caractere de pré-visualização
        if (previewX >= 0 && previewY >= 0 && previewX < mapWidth && previewY < mapHeight) {
            terminal.write(selectedChar, previewX - viewX, previewY - viewY, selectedForegroundColor, selectedBackgroundColor);
        }
        // Desenhar o retângulo de pré-visualização
        if (startX >= 0 && startY >= 0 && currentX >= 0 && currentY >= 0) {
            if (isLeftMouseButton) {
                drawPreviewRectangle(startX, startY, currentX, currentY, selectedChar, selectedForegroundColor, selectedBackgroundColor, false);
            } else if (isRightMouseButton) {
                drawPreviewRectangle(startX, startY, currentX, currentY, (char) 0, Color.WHITE, Color.BLACK, true);
            }
        }
        terminal.repaint();
        terminal.requestFocusInWindow(); // Certificar-se de que o terminal está em foco após cada atualização
    }

    private void drawRectangle(int startX, int startY, int endX, int endY) {
        int minX = Math.min(startX, endX);
        int maxX = Math.max(startX, endX);
        int minY = Math.min(startY, endY);
        int maxY = Math.max(startY, endY);

        for (int x = minX; x <= maxX; x++) {
            if (x < mapWidth && minY < mapHeight) {
                map[x][minY] = selectedChar;
                foregroundColors[x][minY] = selectedForegroundColor;
                backgroundColors[x][minY] = selectedBackgroundColor;
            }
            if (x < mapWidth && maxY < mapHeight) {
                map[x][maxY] = selectedChar;
                foregroundColors[x][maxY] = selectedForegroundColor;
                backgroundColors[x][maxY] = selectedBackgroundColor;
            }
        }

        for (int y = minY; y <= maxY; y++) {
            if (minX < mapWidth && y < mapHeight) {
                map[minX][y] = selectedChar;
                foregroundColors[minX][y] = selectedForegroundColor;
                backgroundColors[minX][y] = selectedBackgroundColor;
            }
            if (maxX < mapWidth && y < mapHeight) {
                map[maxX][y] = selectedChar;
                foregroundColors[maxX][y] = selectedForegroundColor;
                backgroundColors[maxX][y] = selectedBackgroundColor;
            }
        }
    }

    private void drawRectangleFull(int startX, int startY, int endX, int endY) {
        int minX = Math.min(startX, endX);
        int maxX = Math.max(startX, endX);
        int minY = Math.min(startY, endY);
        int maxY = Math.max(startY, endY);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (x < mapWidth && y < mapHeight) {
                    map[x][y] = selectedChar;
                    foregroundColors[x][y] = selectedForegroundColor;
                    backgroundColors[x][y] = selectedBackgroundColor;
                }
            }
        }
    }

    private void deleteInRectangle(int startX, int startY, int endX, int endY) {
        int minX = Math.min(startX, endX);
        int maxX = Math.max(startX, endX);
        int minY = Math.min(startY, endY);
        int maxY = Math.max(startY, endY);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (x < mapWidth && y < mapHeight) {
                    map[x][y] = (char) 0;
                    foregroundColors[x][y] = Color.WHITE;
                    backgroundColors[x][y] = Color.BLACK;
                }
            }
        }
    }

    private void drawPreviewRectangle(int startX, int startY, int endX, int endY, char previewChar, Color fgColor, Color bgColor, boolean fill) {
        int minX = Math.min(startX, endX) - viewX;
        int maxX = Math.max(startX, endX) - viewX;
        int minY = Math.min(startY, endY) - viewY;
        int maxY = Math.max(startY, endY) - viewY;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (x >= 0 && x < panelWidth && y >= 0 && y < panelHeight) {
                    if (fill || x == minX || x == maxX || y == minY || y == maxY) {
                        terminal.write(previewChar, x, y, fgColor, bgColor);
                    }
                }
            }
        }
    }

    private void createAndSaveWorld() {
        // Criação do objeto World com a largura, altura e matriz do mapa
        World world = new World(mapWidth, mapHeight);
        world.setTiles(map);
        world.setForegroundColor(foregroundColors);
        world.setBackgroundColor(backgroundColors);

        String userHome = System.getProperty("user.home");
        String desktopPath = userHome + File.separator + "Desktop";

        File newFolder = new File(desktopPath + "\\World");

        if (!newFolder.exists()) {
            // Tenta criar o diretório
            if (newFolder.mkdirs()) {
                System.out.println("Diretório criado com sucesso: " + desktopPath + "\\World");
            } else {
                System.out.println("Falha ao criar o diretório: " + desktopPath + "\\World");
            }
        }

        // Salvar o objeto World em um arquivo usando OutputStream
        saveWorldToFile(world, desktopPath + "\\World\\world.save");
    }

    private void saveWorldToFile(World world, String fileName) {
        try (FileOutputStream file = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(file)) {
            out.writeObject(world);
            showPopup("World criado e salvo com sucesso!", "O arquivo foi salvo no diretório: " + new File(fileName).getAbsolutePath());
        } catch (IOException e) {
            showPopup("Erro ao salvar o mundo: ", e.getMessage());
        }
    }

    private void showPopup(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void log(String message) {
        System.out.println(message);
        logTextArea.append(message + "\n");
        logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MapEditor::new);
    }
}
