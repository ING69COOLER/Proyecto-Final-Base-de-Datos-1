package gui;

import dao.BitacoraDAO;
import dao.UsuarioDAO;
import model.Usuario;
import security.SeguridadService;
import security.SesionActual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginForm extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JLabel lblError;

    private UsuarioDAO usuarioDAO;
    private BitacoraDAO bitacoraDAO;

    public LoginForm() {
        usuarioDAO = new UsuarioDAO();
        bitacoraDAO = new BitacoraDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Mundial de Fútbol 2026 - Autenticación");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(750, 450);
        setLocationRelativeTo(null);

        // Contenedor principal dividido en dos mitades
        JPanel mainContainer = new JPanel(new BorderLayout());
        setContentPane(mainContainer);

        // ==========================================
        // PANEL IZQUIERDO (Branding / Diseño Gráfico)
        // ==========================================
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(25, 40, 80));
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(350, 450));

        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0; gbcLeft.gridy = 0;
        gbcLeft.insets = new Insets(10, 20, 10, 20);

        JLabel lblFifa = new JLabel("FIFA WORLD CUP");
        lblFifa.setFont(new Font("Segoe UI Black", Font.BOLD, 28));
        lblFifa.setForeground(Color.WHITE);
        leftPanel.add(lblFifa, gbcLeft);

        gbcLeft.gridy++;
        JLabel lblAnio = new JLabel("CANADA - MEXICO - USA");
        lblAnio.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblAnio.setForeground(new Color(255, 204, 0));
        leftPanel.add(lblAnio, gbcLeft);

        gbcLeft.gridy++;
        gbcLeft.insets = new Insets(30, 20, 10, 20);
        JLabel lblDesc = new JLabel("<html><div style='text-align: center; color: #BBCCDD;'>Sistema Oficial de Gestión<br>Logística y Base de Datos<br>Universitaria</div></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        leftPanel.add(lblDesc, gbcLeft);

        // ==========================================
        // PANEL DERECHO (Formulario de Login)
        // ==========================================
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(245, 247, 250));
        rightPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.fill = GridBagConstraints.HORIZONTAL;
        gbcRight.insets = new Insets(10, 40, 5, 40);
        gbcRight.weightx = 1.0;

        // Saludo
        gbcRight.gridx = 0; gbcRight.gridy = 0;
        gbcRight.insets = new Insets(20, 40, 30, 40);
        JLabel lblBienvenido = new JLabel("Iniciar Sesión", SwingConstants.LEFT);
        lblBienvenido.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblBienvenido.setForeground(new Color(30, 40, 60));
        rightPanel.add(lblBienvenido, gbcRight);

        // Campo Usuario
        gbcRight.gridy = 1;
        gbcRight.insets = new Insets(5, 40, 2, 40);
        JLabel lblU = new JLabel("Nombre de Usuario");
        lblU.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblU.setForeground(new Color(100, 110, 130));
        rightPanel.add(lblU, gbcRight);

        gbcRight.gridy = 2;
        gbcRight.insets = new Insets(0, 40, 20, 40);
        txtUsuario = new JTextField();
        estilizarCampo(txtUsuario);
        rightPanel.add(txtUsuario, gbcRight);

        // Campo Contraseña
        gbcRight.gridy = 3;
        gbcRight.insets = new Insets(5, 40, 2, 40);
        JLabel lblP = new JLabel("Contraseña de Acceso");
        lblP.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblP.setForeground(new Color(100, 110, 130));
        rightPanel.add(lblP, gbcRight);

        gbcRight.gridy = 4;
        gbcRight.insets = new Insets(0, 40, 10, 40);
        txtPassword = new JPasswordField();
        estilizarCampo(txtPassword);
        rightPanel.add(txtPassword, gbcRight);

        // Boton Entrar
        gbcRight.gridy = 5;
        gbcRight.insets = new Insets(20, 40, 10, 40);
        btnIngresar = new JButton("Ingresar al Sistema");
        btnIngresar.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnIngresar.setBackground(new Color(30, 100, 220));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnIngresar.setFocusPainted(false);
        btnIngresar.setBorder(new EmptyBorder(12, 0, 12, 0));
        btnIngresar.addActionListener(e -> validarCredenciales());
        
        // Efecto Hover Boton
        btnIngresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btnIngresar.setBackground(new Color(40, 120, 240)); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btnIngresar.setBackground(new Color(30, 100, 220)); }
        });

        rightPanel.add(btnIngresar, gbcRight);

        // Etiqueta Error
        gbcRight.gridy = 6;
        gbcRight.insets = new Insets(10, 40, 20, 40);
        lblError = new JLabel(" ");
        lblError.setForeground(new Color(220, 50, 50));
        lblError.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.add(lblError, gbcRight);

        txtPassword.addActionListener(e -> validarCredenciales());

        mainContainer.add(leftPanel, BorderLayout.WEST);
        mainContainer.add(rightPanel, BorderLayout.CENTER);
    }

    private void estilizarCampo(JTextField campo) {
        campo.setBackground(Color.WHITE);
        campo.setForeground(new Color(40, 40, 40));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 205, 215), 1, true),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
    }

    private void validarCredenciales() {
        String nombre = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (nombre.isEmpty() || password.isEmpty()) {
            lblError.setText("Por favor, complete todos los campos requeridos.");
            return;
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            Usuario usuario = usuarioDAO.obtenerPorNombre(nombre);

            if (usuario == null || !SeguridadService.verificarPassword(password, usuario.getContrasenaHash())) {
                lblError.setText("Credenciales inválidas. Intente nuevamente.");
                txtPassword.setText("");
            } else {
                int idRegistro = bitacoraDAO.registrarIngreso(usuario.getIdUsuario());
                SesionActual.iniciarSesion(usuario, idRegistro);

                this.dispose();
                SwingUtilities.invokeLater(() -> {
                    new MainMenu().setVisible(true);
                });
            }
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    public static void main(String[] args) {
        // Inicializar UI nativa
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
        catch (Exception ignored) {}

        // Test de Conexión Silenciosa en Hilo Secundario
        new Thread(() -> {
            try { db.ConexionOracle.getInstance().testConnection(); }
            catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, 
                        "Error Crítico BD:\n" + ex.getMessage(), "Fallo", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();

        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
