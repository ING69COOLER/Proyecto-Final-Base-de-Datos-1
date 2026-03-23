package gui;

import dao.UsuarioDAO;
import model.Usuario;
import security.SeguridadService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class CRUDUsuarioForm extends JFrame {

  private UsuarioDAO usuarioDAO;
  private JTable tabla;
  private DefaultTableModel modeloTabla;
  
  // Campos del formulario
  private JTextField txtId, txtUsername;
  private JPasswordField txtPassword;
  private JComboBox<String> cmbTipoUsuario;

  public CRUDUsuarioForm() {
    usuarioDAO = new UsuarioDAO();
    initComponents();
    cargarDatos();
  }

  private void initComponents() {
    setTitle("Módulo de Gestión de Usuarios y Roles");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(850, 500);
    setLocationRelativeTo(null);

    JPanel panelPrincipal = new JPanel(new BorderLayout());
    panelPrincipal.setBackground(new Color(25, 30, 40));
    setContentPane(panelPrincipal);

    // Titulo
    JLabel lblTitulo = new JLabel("  Módulo Exclusivo de Administrador: Usuarios", SwingConstants.LEFT);
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
    lblTitulo.setForeground(new Color(255, 100, 100));
    lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
    panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

    // Tabla de datos
    String[] columnas = {"ID", "Nombre de Usuario", "Rol (Tipo de Usuario)", "Hash de Acceso (Protegido)", "Fecha de Creado"};
    modeloTabla = new DefaultTableModel(columnas, 0) {
      @Override
      public boolean isCellEditable(int row, int column) { return false; }
    };
    tabla = new JTable(modeloTabla);
    tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tabla.getSelectionModel().addListSelectionListener(e -> cargarDatosEnFormulario());
    JScrollPane scrollPane = new JScrollPane(tabla);
    panelPrincipal.add(scrollPane, BorderLayout.CENTER);

    // Panel de form y botones (Sur)
    JPanel panelSur = new JPanel(new BorderLayout());
    panelSur.setBackground(new Color(30, 35, 50));
    
    // Formulario
    JPanel formPanel = new JPanel(new GridLayout(2, 4, 15, 15));
    formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
    formPanel.setOpaque(false);
    
    txtId = new JTextField(); txtId.setEditable(false);
    txtUsername = new JTextField(); 
    txtPassword = new JPasswordField(); 
    txtPassword.setToolTipText("Dejar en blanco si no deseas cambiar la contraseña de este usuario.");
    
    cmbTipoUsuario = new JComboBox<>(new String[]{"Tradicional", "Esporadico", "Admin"});

    agregarCampo(formPanel, "ID (Auto):", txtId);
    agregarCampo(formPanel, "Nombre de Usuario:", txtUsername);
    agregarCampo(formPanel, "Contraseña (Nueva):", txtPassword);
    agregarCampo(formPanel, "Rol o Permisos:", cmbTipoUsuario);

    panelSur.add(formPanel, BorderLayout.CENTER);

    // Botones
    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
    btnPanel.setOpaque(false);
    
    JButton btnCrear = crearBotonNativo(" Nuevo Usuario", new Color(40, 120, 60));
    btnCrear.addActionListener(e -> crearRegistro());
    
    JButton btnActualizar = crearBotonNativo(" Actualizar Usuario", new Color(40, 80, 140));
    btnActualizar.addActionListener(e -> actualizarRegistro());
    
    JButton btnEliminar = crearBotonNativo(" Eliminar Usuario", new Color(140, 40, 40));
    btnEliminar.addActionListener(e -> eliminarRegistro());
    
    JButton btnLimpiar = crearBotonNativo(" Limpiar Cajas", new Color(100, 100, 100));
    btnLimpiar.addActionListener(e -> limpiarFormulario());

    btnPanel.add(btnCrear);
    btnPanel.add(btnActualizar);
    btnPanel.add(btnEliminar);
    btnPanel.add(btnLimpiar);

    panelSur.add(btnPanel, BorderLayout.SOUTH);
    panelPrincipal.add(panelSur, BorderLayout.SOUTH);
  }

  private void agregarCampo(JPanel panel, String label, JComponent comp) {
    JLabel l = new JLabel(label);
    l.setForeground(Color.WHITE);
    panel.add(l);
    panel.add(comp);
  }

  private JButton crearBotonNativo(String texto, Color bg) {
    JButton b = new JButton(texto);
    b.setUI(new javax.swing.plaf.basic.BasicButtonUI()); 
    b.setBackground(bg);
    b.setForeground(Color.WHITE);
    b.setFocusPainted(false);
    return b;
  }

  // --- LOGICA CRUD ---

  private void cargarDatos() {
    modeloTabla.setRowCount(0);
    List<Usuario> lista = usuarioDAO.obtenerTodos();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    for (Usuario u : lista) {
      String hashProtegido = u.getContrasenaHash().substring(0, Math.min(u.getContrasenaHash().length(), 15)) + "...";
      modeloTabla.addRow(new Object[]{
        u.getIdUsuario(), u.getNombreUsuario(), u.getTipoUsuario(), 
        hashProtegido, 
        u.getFechaCreacion() != null ? sdf.format(u.getFechaCreacion()) : "N/A"
      });
    }
  }

  private void cargarDatosEnFormulario() {
    int r = tabla.getSelectedRow();
    if (r >= 0) {
      txtId.setText(tabla.getValueAt(r, 0).toString());
      txtUsername.setText(tabla.getValueAt(r, 1).toString());
      cmbTipoUsuario.setSelectedItem(tabla.getValueAt(r, 2).toString());
      txtPassword.setText(""); // Por seguridad no se debe rellenar el password
    }
  }

  private void limpiarFormulario() {
    txtId.setText(""); txtUsername.setText(""); txtPassword.setText("");
    cmbTipoUsuario.setSelectedIndex(0);
    tabla.clearSelection();
  }

  private void crearRegistro() {
    if (txtUsername.getText().trim().isEmpty() || new String(txtPassword.getPassword()).trim().isEmpty()) {
      JOptionPane.showMessageDialog(this, "El nombre de usuario y contraseña son obligatorios para crearlo.");
      return;
    }
    
    Usuario u = new Usuario();
    u.setNombreUsuario(txtUsername.getText().trim());
    u.setTipoUsuario(cmbTipoUsuario.getSelectedItem().toString());
    u.setContrasenaHash(SeguridadService.hashPassword(new String(txtPassword.getPassword())));
    
    if (usuarioDAO.crear(u)) {
      JOptionPane.showMessageDialog(this, "Usuario creado exitosamente con encriptación SHA-256.");
      cargarDatos();
      limpiarFormulario();
    } else {
      JOptionPane.showMessageDialog(this, "Error al crear en BD (Puede que el usuario ya exista).", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void actualizarRegistro() {
    if (txtId.getText().isEmpty()) {
      JOptionPane.showMessageDialog(this, "Selecciona un usuario primero.");
      return;
    }
    
    int id = Integer.parseInt(txtId.getText());
    Usuario uViejo = usuarioDAO.obtenerPorId(id);
    
    Usuario u = new Usuario();
    u.setIdUsuario(id);
    u.setNombreUsuario(txtUsername.getText().trim());
    u.setTipoUsuario(cmbTipoUsuario.getSelectedItem().toString());
    
    // Magia para conservar la contraseña antigua si el campo se dejó en blanco
    String nuevaClave = new String(txtPassword.getPassword()).trim();
    if (nuevaClave.isEmpty()) {
      u.setContrasenaHash(uViejo.getContrasenaHash()); // Mantener vieja
    } else {
      u.setContrasenaHash(SeguridadService.hashPassword(nuevaClave)); // Hashear nueva
    }

    if (usuarioDAO.actualizar(u)) {
      JOptionPane.showMessageDialog(this, "Usuario actualizado exitosamente.");
      cargarDatos();
    } else {
      JOptionPane.showMessageDialog(this, "Error al actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void eliminarRegistro() {
    if (txtId.getText().isEmpty()) return;
    int id = Integer.parseInt(txtId.getText());
    
    // Evitar que el admin activo se elimine a sí mismo
    Usuario dbUser = usuarioDAO.obtenerPorId(id);
    if (dbUser.getNombreUsuario().equalsIgnoreCase(security.SesionActual.getUsuario().getNombreUsuario())) {
      JOptionPane.showMessageDialog(this, "No puedes eliminar tu propio usuario mientras estás conectado.", "Operación Denegada", JOptionPane.WARNING_MESSAGE);
      return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar a " + dbUser.getNombreUsuario() + "?\nSe perderán todas sus bitácoras de auditoría también.");
    if (confirm == JOptionPane.YES_OPTION) {
      if (usuarioDAO.eliminar(id)) {
        cargarDatos();
        limpiarFormulario();
      } else {
        JOptionPane.showMessageDialog(this, "Error al eliminar en la Base de Datos.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
}
