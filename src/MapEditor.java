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

    private World dungeonMap;

    private Mapa mapa;
    private TerminalMapa terminalMapa;

    private char selectedChar = 0; // Caractere selecionado inicialmente
    private Color selectedForegroundColor = Color.WHITE; // Cor do caractere selecionado
    private Color selectedBackgroundColor = Color.BLACK; // Cor de fundo do caractere selecionado

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
    private boolean isForegroundSelected = true; // Novo: Estado para modificar cor do caractere

    private Map<String, Color> colorMap; // Mapa para associar nomes de cores às cores reais

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

                    mapa.setTiles(dungeonMap.getTiles());
                    mapa.setForegroundColors(dungeonMap.foregroundColor());
                    mapa.setBackgroundColors(dungeonMap.backgroundColor());
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

    public void fillBorder(char character, Color selectedForegroundColor, Color selectedBackgroundColor) {
        for (int x = 0; x < mapa.getWidth(); x++) {
            for (int y = 0; y < mapa.getHeight(); y++) {
                if ((x == 0 || y == 0) || (x == (mapa.getWidth() - 1) || y == (mapa.getHeight() - 1))){
                    mapa.setCharAt(x, y, character);
                    mapa.setForegorundAt(x, y, selectedForegroundColor);
                    mapa.setBackgroudAt(x, y, selectedBackgroundColor);
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
        terminalMapa.getTerminal().clear();

        int width = mapa.getWidth(), height = mapa.getHeight();

        for (int x = 0; x < terminalMapa.getPanelWidth(); x++) {
            for (int y = 0; y < terminalMapa.getPanelHeight(); y++) {
                if (x + viewX < width && y + viewY < height) {
                    char tile = mapa.getCharAt(x + viewX, y + viewY);
                    Color foreground = mapa.getForegroundAt(x + viewX, y + viewY);
                    Color background = mapa.getBackgroundAt(x + viewY, y + viewY);
                    terminalMapa.getTerminal().write(tile, x, y, foreground, background);
                }
            }
        }
        // Desenhar o caractere de pré-visualização
        if (previewX >= 0 && previewY >= 0 && previewX < width && previewY < height) {
            terminalMapa.getTerminal().write(selectedChar, previewX - viewX, previewY - viewY, selectedForegroundColor, selectedBackgroundColor);
        }
        // Desenhar o retângulo de pré-visualização
        if (startX >= 0 && startY >= 0 && currentX >= 0 && currentY >= 0) {
            if (isLeftMouseButton) {
                drawPreviewRectangle(startX, startY, currentX, currentY, selectedChar, selectedForegroundColor, selectedBackgroundColor, false);
            } else if (isRightMouseButton) {
                drawPreviewRectangle(startX, startY, currentX, currentY, (char) 0, Color.WHITE, Color.BLACK, true);
            }
        }
        terminalMapa.getTerminal().repaint();
        terminalMapa.getTerminal().requestFocusInWindow(); // Certificar-se de que o terminal está em foco após cada atualização
    }

    private void drawRectangle(int startX, int startY, int endX, int endY) {
        int minX = Math.min(startX, endX);
        int maxX = Math.max(startX, endX);
        int minY = Math.min(startY, endY);
        int maxY = Math.max(startY, endY);

        int width = mapa.getWidth(), height = mapa.getHeight();

        for (int x = minX; x <= maxX; x++) {
            if (x < width && minY < height) {
                mapa.setCharAt(x, minY, selectedChar);
                mapa.setForegorundAt(x, minY, selectedForegroundColor);
                mapa.setBackgroudAt(x, minY, selectedBackgroundColor);
            }
            if (x < width && maxY < height) {
                mapa.setCharAt(x, maxY, selectedChar);
                mapa.setForegorundAt(x, maxY, selectedForegroundColor);
                mapa.setBackgroudAt(x, maxY, selectedBackgroundColor);
            }
        }

        for (int y = minY; y <= maxY; y++) {
            if (minX < width && y < height) {
                mapa.setCharAt(minX, y, selectedChar);
                mapa.setForegorundAt(minX, y, selectedForegroundColor);
                mapa.setBackgroudAt(minX, y, selectedBackgroundColor);
            }
            if (maxX < width && y < height) {
                mapa.setCharAt(maxX, y, selectedChar);
                mapa.setForegorundAt(maxX, y, selectedForegroundColor);
                mapa.setBackgroudAt(maxX, y, selectedBackgroundColor);
            }
        }
    }

    private void drawRectangleFull(int startX, int startY, int endX, int endY) {
        int minX = Math.min(startX, endX);
        int maxX = Math.max(startX, endX);
        int minY = Math.min(startY, endY);
        int maxY = Math.max(startY, endY);

        int width = mapa.getWidth(), height = mapa.getHeight();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (x < width && y < height) {
                    mapa.setCharAt(x, y, selectedChar);
                    mapa.setForegorundAt(x, y, selectedForegroundColor);
                    mapa.setBackgroudAt(x, y, selectedBackgroundColor);
                }
            }
        }
    }

    private void deleteInRectangle(int startX, int startY, int endX, int endY) {
        int minX = Math.min(startX, endX);
        int maxX = Math.max(startX, endX);
        int minY = Math.min(startY, endY);
        int maxY = Math.max(startY, endY);

        int width = mapa.getWidth(), height = mapa.getHeight();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (x < width && y < height) {
                    mapa.setCharAt(x, y, (char)0);
                    mapa.setForegorundAt(x, y, Color.WHITE);
                    mapa.setBackgroudAt(x, y, Color.BLACK);
                }
            }
        }
    }

    private void drawPreviewRectangle(int startX, int startY, int endX, int endY, char previewChar, Color fgColor, Color bgColor, boolean fill) {
        int minX = Math.min(startX, endX) - viewX;
        int maxX = Math.max(startX, endX) - viewX;
        int minY = Math.min(startY, endY) - viewY;
        int maxY = Math.max(startY, endY) - viewY;

        int paneWidth = terminalMapa.getPanelWidth(), paneHeight = terminalMapa.getPanelHeight();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (x >= 0 && x < paneWidth && y >= 0 && y < paneHeight) {
                    if (fill || x == minX || x == maxX || y == minY || y == maxY) {
                        terminalMapa.getTerminal().write(previewChar, x, y, fgColor, bgColor);
                    }
                }
            }
        }
    }

    private void createAndSaveWorld() {
        // Criação do objeto World com a largura, altura e matriz do mapa
        World world = new World(mapa.getWidth(), mapa.getHeight());

        world.setTiles(mapa.getTiles());
        world.setForegroundColor(mapa.getForegroundColors());
        world.setBackgroundColor(mapa.getBackgroundColors());

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

    public MapEditor() {
        super("Map Editor");

        mapa = new Mapa();

        setResizable(false);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for (int x = 0; x < mapa.getWidth(); x++) {
            for (int y = 0; y < mapa.getHeight(); y++) {
                mapa.setCharAt(x, y, (char)0);
                mapa.setForegorundAt(x, y, Color.WHITE);
                mapa.setBackgroudAt(x, y, Color.BLACK);
            }
        }
        // Borda branca por padrao
        fillBorder((char) 171, Color.WHITE, Color.BLACK);

        // Configurar o painel AsciiPanel
        terminalMapa = new TerminalMapa();

        // Configurar o contêiner do terminal para garantir que a borda apareça corretamente
        JPanel terminalPanel = new JPanel(new BorderLayout());
        terminalPanel.setBorder(new LineBorder(Color.getHSBColor(0.7f, 0.5f, 1), 5));

        terminalPanel.add(terminalMapa.getTerminal(), BorderLayout.CENTER);

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

            if(imageFile.exists()){
                ImageIcon icon = new ImageIcon(imagePath);

                Image img = icon.getImage();
                Image resized = img.getScaledInstance(48, 48, java.awt.Image.SCALE_SMOOTH);

                ImageIcon resizedIcon = new ImageIcon(resized);

                charButton.setIcon(resizedIcon);

                charButton.setPreferredSize(new Dimension(resizedIcon.getIconWidth(), resizedIcon.getIconHeight()));
                charButton.setMinimumSize(new Dimension(resizedIcon.getIconWidth(), resizedIcon.getIconHeight()));
                charButton.setMaximumSize(new Dimension(resizedIcon.getIconWidth(), resizedIcon.getIconHeight()));
            } else {

                String userHome = System.getProperty("user.home");
                String desktopPath = userHome + File.separator + "Desktop";

                Path worldPath = Paths.get(desktopPath + "\\MapEditor\\").getParent();

                Path imagesPath = Paths.get(desktopPath + "\\MapEditor\\images");


                if (Files.exists(imagesPath)) {
                    Path imgPath = Paths.get(desktopPath + "\\MapEditor\\images\\" + i + ".png");

                    ImageIcon icon = new ImageIcon(String.valueOf(imgPath));
                    Image img = icon.getImage();
                    Image resized = img.getScaledInstance(48, 48, java.awt.Image.SCALE_SMOOTH);

                    ImageIcon resizedIcon = new ImageIcon(resized);

                    charButton.setIcon(resizedIcon);

                    charButton.setPreferredSize(new Dimension(resizedIcon.getIconWidth(), resizedIcon.getIconHeight()));
                    charButton.setMinimumSize(new Dimension(resizedIcon.getIconWidth(), resizedIcon.getIconHeight()));
                    charButton.setMaximumSize(new Dimension(resizedIcon.getIconWidth(), resizedIcon.getIconHeight()));
                } else {
                    charButton.setText(String.valueOf(c));
                }
            }

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
                "Vermelho1", "Vermelho2", "Vermelho3", "Laranja1", "Laranja2", "Laranja3", "Amarelo1", "Amarelo2",
                "Verde1", "Verde2", "Verde3", "Azul1", "Azul2", "Azul3",
                "Roxo1", "Roxo2", "Rosa1", "Rosa2", "Marrom1", "Marrom2", "Bege1", "Bege2",
                "Cinza1", "Cinza2", "Preto1", "Preto2", "Branco1", "Branco2"
        };

        Color[] colors = {
                // Vermelho
                new Color(133, 0, 0),
                new Color(179, 32, 18),
                new Color(255, 241, 186),

                // Laranja
                new Color(250, 167, 41),
                new Color(253, 193, 62),
                new Color(102, 51, 0),

                // Amarelo
                new Color(254, 255, 78),
                new Color(253, 255, 60),

                // Verde
                new Color(15, 84, 10),
                new Color(73, 173, 58),
                new Color(125, 207, 106),

                // Azul
                new Color(17, 9, 129),
                new Color(33, 17, 158),
                new Color(65, 34, 216),

                // Roxo
                new Color(79, 11, 75),
                new Color(204, 126, 200),

                // Rosa
                new Color(255, 213, 226, 255),
                new Color(182, 105, 131),

                // Marrom
                new Color(33, 18, 16),
                new Color(225, 177, 167),

                // Bege
                new Color(250, 251, 228),
                new Color(246, 246, 202),

                // Cinza
                new Color(86, 86, 86),
                new Color(223, 223, 223),

                // Preto
                new Color(0, 0, 0),
                new Color(14, 14, 14),

                // Branco
                new Color(255, 255, 255),
                new Color(236, 233, 236)
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
        terminalMapa.getTerminal().addMouseListener(new MouseAdapter() {
            int charWidth = terminalMapa.getTerminal().getCharWidth(), charHeight = terminalMapa.getTerminal().getCharHeight();

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    startX = e.getX() / charWidth + viewX;
                    startY = e.getY() / charHeight + viewY;
                    isLeftMouseButton = true;
                    isRightMouseButton = false;
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    startX = e.getX() / charWidth + viewX;
                    startY = e.getY() / charHeight + viewY;
                    isLeftMouseButton = false;
                    isRightMouseButton = true;
                } else if (SwingUtilities.isRightMouseButton(e) && SwingUtilities.isLeftMouseButton(e)) {
                    startX = e.getX() / charWidth + viewX;
                    startY = e.getY() / charHeight + viewY;
                    isLeftMouseButton = true;
                    isRightMouseButton = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isCtrlPressed && isLeftMouseButton) {
                    int endX = e.getX() / charWidth + viewX;
                    int endY = e.getY() / charHeight + viewY;
                    drawRectangleFull(startX, startY, endX, endY);
                    startX = -1;
                    startY = -1;
                    currentX = -1;
                    currentY = -1;
                    isCtrlPressed = false;
                    renderView();
                } else if (isLeftMouseButton && startX >= 0 && startY >= 0) {
                    int endX = e.getX() / charWidth + viewX;
                    int endY = e.getY() / charHeight + viewY;
                    drawRectangle(startX, startY, endX, endY);
                    startX = -1;
                    startY = -1;
                    currentX = -1;
                    currentY = -1;
                    isLeftMouseButton = false;
                    renderView();
                } else if (isRightMouseButton && startX >= 0 && startY >= 0) {
                    int endX = e.getX() / charWidth + viewX;
                    int endY = e.getY() / charHeight + viewY;
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
                int x = e.getX() / charWidth + viewX;
                int y = e.getY() / charHeight + viewY;
                if (x < mapa.getWidth() && y < mapa.getHeight()) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        mapa.setCharAt(x, y, selectedChar);
                        mapa.setForegorundAt(x, y, selectedForegroundColor);
                        mapa.setBackgroudAt(x, y, selectedBackgroundColor);
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        mapa.setCharAt(x, y, (char) 0);
                        mapa.setForegorundAt(x, y, Color.WHITE);
                        mapa.setBackgroudAt(x, y, Color.BLACK);
                    } else if (SwingUtilities.isMiddleMouseButton(e)) {
                        selectedChar = mapa.getCharAt(x, y);
                        selectedForegroundColor = mapa.getForegroundAt(x ,y);
                        selectedBackgroundColor = mapa.getBackgroundAt(x, y);
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

        terminalMapa.getTerminal().addMouseMotionListener(new MouseAdapter() {
            int charWidth = terminalMapa.getTerminal().getCharWidth(), charHeight = terminalMapa.getTerminal().getCharHeight();

            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX() / charWidth + viewX;
                int y = e.getY() / charHeight + viewY;
                if (x < mapa.getWidth() && y < mapa.getHeight()) {
                    previewX = x;
                    previewY = y;
                    updatePositionLabel(x, y);
                    renderView();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (startX >= 0 && startY >= 0) {
                    currentX = e.getX() / charWidth + viewX;
                    currentY = e.getY() / charHeight + viewY;
                    renderView();
                }
            }
        });

        // Adicionar ouvinte de teclado para movimentação e criação do objeto World
        terminalMapa.getTerminal().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: // Move up
                        if (viewY > 0) viewY--;
                        break;
                    case KeyEvent.VK_S: // Move down
                        if (viewY < mapa.getHeight() - terminalMapa.getPanelHeight()) viewY++;
                        break;
                    case KeyEvent.VK_A: // Move left
                        if (viewX > 0) viewX--;
                        break;
                    case KeyEvent.VK_D: // Move right
                        if (viewX < mapa.getWidth() - terminalMapa.getPanelWidth()) viewX++;
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

        terminalMapa.getTerminal().addKeyListener(new KeyAdapter() {
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

        terminalMapa.getTerminal().requestFocusInWindow(); // Certificar-se de que o terminal está em foco
        renderView();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MapEditor::new);
    }
}
