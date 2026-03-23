package gui;

import dao.BitacoraDAO;
import dao.UsuarioDAO;
import model.Usuario;
import security.SeguridadService;
import security.SesionActual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Ventana de Login del sistema Mundial de Futbol 2026.
 */
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
        setTitle("Mundial de Fútbol 2026 - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(420, 340);
        setLocationRelativeTo(null);

        // Panel principal con fondo oscuro
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(20, 25, 45));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        setContentPane(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        // Titulo
        JLabel lblTitulo = new JLabel("⚽ MUNDIAL 2026", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(255, 200, 50));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(lblTitulo, gbc);

        JLabel lblSubtitulo = new JLabel("Sistema de Gestión", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(160, 170, 200));
        gbc.gridy = 1;
        mainPanel.add(lblSubtitulo, gbc);

        // Separador
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(60, 70, 100));
        gbc.gridy = 2;
        mainPanel.add(sep, gbc);

        // Campo usuario
        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        JLabel lblU = new JLabel("Usuario:");
        lblU.setForeground(Color.WHITE);
        lblU.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        mainPanel.add(lblU, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        txtUsuario = new JTextField(15);
        estilizarCampo(txtUsuario);
        mainPanel.add(txtUsuario, gbc);

        // Campo password
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        JLabel lblP = new JLabel("Contraseña:");
        lblP.setForeground(Color.WHITE);
        lblP.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        mainPanel.add(lblP, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        txtPassword = new JPasswordField(15);
        estilizarCampo(txtPassword);
        mainPanel.add(txtPassword, gbc);

        // Label error
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        lblError = new JLabel(" ");
        lblError.setForeground(new Color(255, 80, 80));
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(lblError, gbc);

        // Boton ingresar
        gbc.gridy = 6;
        btnIngresar = new JButton("INGRESAR");
        btnIngresar.setBackground(new Color(50, 130, 220));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnIngresar.addActionListener(this::validarCredenciales);

        // Enter key en password
        txtPassword.addActionListener(this::validarCredenciales);

        mainPanel.add(btnIngresar, gbc);
    }

    private void estilizarCampo(JTextField campo) {
        campo.setBackground(new Color(35, 40, 65));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 80, 120)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    private void validarCredenciales(ActionEvent e) {
        String nombre = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (nombre.isEmpty() || password.isEmpty()) {
            lblError.setText("Complete todos los campos.");
            return;
        }

        Usuario usuario = usuarioDAO.obtenerPorNombre(nombre);

        if (usuario == null || !SeguridadService.verificarPassword(password, usuario.getContrasenaHash())) {
            lblError.setText("Usuario o contraseña incorrectos.");
            txtPassword.setText("");
            return;
        }

        // Registrar ingreso en bitácora
        int idRegistro = bitacoraDAO.registrarIngreso(usuario.getIdUsuario());
        SesionActual.iniciarSesion(usuario, idRegistro);

        // Abrir menú principal
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            MainMenu menu = new MainMenu();
            menu.setVisible(true);
        });
    }

    public static void main(String[] args) {
        // Test rápido de conexión antes de abrir la UI
        try {
            db.ConexionOracle.getInstance().testConnection();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                "Error de conexión a Oracle:\n" + ex.getMessage() +
                "\n\nVerifica que Oracle esté corriendo y las credenciales\nen db/ConexionOracle.java sean correctas.",
                "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            LoginForm form = new LoginForm();
            form.setVisible(true);
        });
    }
}
