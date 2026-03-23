package gui;

import dao.BitacoraDAO;
import security.SesionActual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Pantalla Principal del sistema que solo se le revela al usuario
 * después de superar la barrera del LoginForm.
 *
 * NOTA DE ARQUITECTURA: Esta clase utiliza el patrón MDI o de Módulos (Paneles Centrales).
 * Implementa Role-Based Access Control (RBAC) escondiendo los botones si el `SesionActual`
 * indica que el usuario NO TIENE permisos de administrador.
 */
public class MainMenu extends JFrame {

  private BitacoraDAO bitacoraDAO;

  public MainMenu() {
    bitacoraDAO = new BitacoraDAO();
    setTitle("Sistema de Gestion - Copa Mundial de la FIFA 2026");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    initComponents();

    // Registrar salida en bitácora al cerrar
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        if (SesionActual.getIdRegistroBitacora() > 0) {
          bitacoraDAO.registrarSalida(SesionActual.getIdRegistroBitacora());
        }
        SesionActual.cerrarSesion();
        System.exit(0);
      }
    });
  }

  private void initComponents() {
    String nombreUsuario = SesionActual.getUsuario() != null
      ? SesionActual.getUsuario().getNombreUsuario() : "Desconocido";
    String tipoUsuario = SesionActual.getUsuario() != null
      ? SesionActual.getUsuario().getTipoUsuario() : "";

    // Original setTitle and setDefaultCloseOperation moved to constructor
    setSize(850, 600);
    setLocationRelativeTo(null);

    // Panel principal
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(new Color(15, 20, 40));
    setContentPane(mainPanel);

    // Header
    JPanel header = createHeader(nombreUsuario, tipoUsuario);
    mainPanel.add(header, BorderLayout.NORTH);

    // Content con botones
    JPanel content = new JPanel(new GridLayout(2, 3, 20, 20));
    content.setBackground(new Color(15, 20, 40));
    content.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

    // ==========================================
    // SISTEMA DE SEGURIDAD (Permisos Dinamicos)
    // ==========================================

    // 1. Módulos CRUD disponibles SOLAMENTE para Admin y Tradicional (Escritura)
    if (SesionActual.puedeEscribir()) {
      content.add(crearBotonModulo("Equipos", "Gestionar equipos nacionales", new Color(30, 80, 150),
          () -> new CRUDEquipoForm().setVisible(true)));

      content.add(crearBotonModulo("Jugadores", "Gestionar jugadores y posiciones", new Color(30, 100, 80),
          () -> new CRUDJugadorForm().setVisible(true))); // Llama a la ventana real

      content.add(crearBotonModulo("Estadios", "Gestionar estadios y ciudades", new Color(100, 50, 120),
          () -> new CRUDEstadioForm().setVisible(true)));

      content.add(crearBotonModulo("Partidos", "Gestionar partidos del mundial", new Color(120, 80, 30),
          () -> new CRUDPartidoForm().setVisible(true)));

      content.add(crearBotonModulo("DT / Conf.", "Directores técnicos y confederaciones", new Color(30, 100, 120),
          () -> new CRUDDTConfForm().setVisible(true)));
    }

    // 2. Reportes universitarios. Siempre están disponibles para TODOS los usuarios ("Lectura Global")
    JButton btnReportes = crearBotonModulo("Reportes Avanzados", "Consultas complejas SQL", new Color(120, 50, 50),
        () -> new ReportesForm().setVisible(true)); // Llama a la ventana de 8 reportes
    content.add(btnReportes);

    // 3. Gestión de usuarios exclusiva y oculta: SOLO para Admin
    if (SesionActual.esAdmin()) {
      content.add(crearBotonModulo("Usuarios", "Gestionar cuentas y roles", new Color(80, 80, 30),
          () -> new CRUDUsuarioForm().setVisible(true)));
    }

    mainPanel.add(content, BorderLayout.CENTER);

    // Footer con botón cerrar sesión
    JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    footer.setBackground(new Color(10, 13, 28));
    footer.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 20));
    JButton btnCerrar = new JButton("Cerrar Sesión");
    btnCerrar.setUI(new javax.swing.plaf.basic.BasicButtonUI()); // <- Fix para color de fondo en Windows
    btnCerrar.setBackground(new Color(180, 40, 40));
    btnCerrar.setForeground(Color.WHITE);
    btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
    btnCerrar.setFocusPainted(false);
    btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    btnCerrar.addActionListener(e -> cerrarSesion());
    footer.add(btnCerrar);
    mainPanel.add(footer, BorderLayout.SOUTH);
  }

  private JPanel createHeader(String nombre, String tipo) {
    JPanel header = new JPanel(new BorderLayout());
    header.setBackground(new Color(10, 15, 35));
    header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

    JLabel lblTitulo = new JLabel(" CAMPEONATO MUNDIAL DE FÚTBOL 2026");
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
    lblTitulo.setForeground(new Color(255, 200, 50));
    header.add(lblTitulo, BorderLayout.WEST);

    JLabel lblUsuario = new JLabel(" " + nombre + " | " + tipo);
    lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    lblUsuario.setForeground(new Color(120, 200, 120));
    header.add(lblUsuario, BorderLayout.EAST);

    return header;
  }

  private JButton crearBotonModulo(String titulo, String descripcion, Color color, Runnable accion) {
    JButton btn = new JButton("<html><center><b style='font-size:18px'>" + titulo + "</b>"
      + "<br><br><span style='font-size:12px;color:#f0f0f0'>" + descripcion + "</span></center></html>");
      
    // MUY IMPORTANTE PARA QUE WINDOWS NO INTERFIERA CON EL COLOR
    btn.setUI(new javax.swing.plaf.basic.BasicButtonUI()); 
    btn.setOpaque(true);
    btn.setContentAreaFilled(true);
    btn.setBackground(color);
    btn.setForeground(Color.WHITE);
    btn.setFocusPainted(false);
    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    
    // Bordes suaves
    btn.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(color.darker(), 2, true),
      BorderFactory.createEmptyBorder(15, 10, 15, 10)
    ));
    
    // Cargar el Runnable al ActionListener
    btn.addActionListener(e -> {
      if (accion != null) {
        accion.run();
      }
    });

    // Hover effect estetico premium
    btn.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent e) {
        btn.setBackground(color.brighter());
        btn.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createLineBorder(Color.WHITE, 2, true),
          BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
      }
      public void mouseExited(java.awt.event.MouseEvent e) {
        btn.setBackground(color);
        btn.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createLineBorder(color.darker(), 2, true),
          BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
      }
    });
    return btn;
  }

  private void cerrarSesion() {
    int confirm = JOptionPane.showConfirmDialog(this,
      "¿Desea cerrar la sesión?", "Cerrar Sesión",
      JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      if (SesionActual.getIdRegistroBitacora() > 0) {
        bitacoraDAO.registrarSalida(SesionActual.getIdRegistroBitacora());
      }
      SesionActual.cerrarSesion();
      this.dispose();
      SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
  }
}
