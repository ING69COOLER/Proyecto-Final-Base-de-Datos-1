package gui;

import dao.EquipoDAO;
import dao.ConfederacionDAO;
import model.Equipo;
import model.Confederacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CRUDEquipoForm extends JFrame {

  private EquipoDAO equipoDAO;
  private ConfederacionDAO confederacionDAO;
  private JTable tabla;
  private DefaultTableModel modeloTabla;
  
  // Campos del formulario
  private JTextField txtId, txtNombre, txtRanking, txtValor;
  private JComboBox<Confederacion> cmbConfederacion;

  public CRUDEquipoForm() {
    equipoDAO = new EquipoDAO();
    confederacionDAO = new ConfederacionDAO();
    initComponents();
    cargarDatos();
  }

  private void initComponents() {
    setTitle("Módulo de Gestión de Equipos (Selecciones)");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(900, 500);
    setLocationRelativeTo(null);

    JPanel panelPrincipal = new JPanel(new BorderLayout());
    panelPrincipal.setBackground(new Color(25, 30, 40));
    setContentPane(panelPrincipal);

    // Titulo
    JLabel lblTitulo = new JLabel("  Módulo Oficial de Selecciones Nacionales", SwingConstants.LEFT);
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
    lblTitulo.setForeground(Color.WHITE);
    lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
    panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

    // Tabla de datos
    String[] columnas = {"ID", "País / Selección", "Ranking FIFA", "Valor Mercado Extra ($)", "Confederación", "ID Conf"};
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
    JPanel formPanel = new JPanel(new GridLayout(3, 4, 15, 15));
    formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    formPanel.setOpaque(false);
    
    txtId = new JTextField(); txtId.setEditable(false);
    txtNombre = new JTextField(); 
    txtRanking = new JTextField(); 
    txtValor = new JTextField();
    cmbConfederacion = new JComboBox<>();
    cargarComboConfederacion();

    agregarCampo(formPanel, "ID (Auto):", txtId);
    agregarCampo(formPanel, "Nombre País / Selección:", txtNombre);
    agregarCampo(formPanel, "Ranking FIFA (1, 2, 3...):", txtRanking);
    agregarCampo(formPanel, "Valor Plantilla USD ($):", txtValor);
    agregarCampo(formPanel, "Confederación Continental:", cmbConfederacion);
    
    // Rellenar las 2 celdas vacías (1 campo fantasma) para mantener cuadricula perfecta
    agregarCampo(formPanel, "", new JLabel(""));

    panelSur.add(formPanel, BorderLayout.CENTER);

    // Botones
    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
    btnPanel.setOpaque(false);
    
    JButton btnCrear = crearBotonNativo(" Nuevo Equipo", new Color(40, 120, 60));
    btnCrear.addActionListener(e -> crearRegistro());
    
    JButton btnActualizar = crearBotonNativo(" Actualizar Selección", new Color(40, 80, 140));
    btnActualizar.addActionListener(e -> actualizarRegistro());
    
    JButton btnEliminar = crearBotonNativo(" Eliminar Selección", new Color(140, 40, 40));
    btnEliminar.addActionListener(e -> eliminarRegistro());
    
    JButton btnLimpiar = crearBotonNativo(" Limpiar", new Color(100, 100, 100));
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
  
  private void cargarComboConfederacion() {
    List<Confederacion> list = confederacionDAO.obtenerTodos();
    for (Confederacion c : list) {
      cmbConfederacion.addItem(c);
    }
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
    List<Equipo> lista = equipoDAO.obtenerTodos();
    for (Equipo e : lista) {
      Confederacion conf = confederacionDAO.obtenerPorId(e.getIdConfederacion());
      String nombreConf = (conf != null) ? conf.getNombre() : "N/A";
      
      modeloTabla.addRow(new Object[]{
        e.getIdEquipo(), e.getNombre(), e.getRankingFifa(), e.getValorMercado(), 
        nombreConf, e.getIdConfederacion()
      });
    }
  }

  private void cargarDatosEnFormulario() {
    int r = tabla.getSelectedRow();
    if (r >= 0) {
      txtId.setText(tabla.getValueAt(r, 0).toString());
      txtNombre.setText(tabla.getValueAt(r, 1).toString());
      txtRanking.setText(tabla.getValueAt(r, 2).toString());
      txtValor.setText(tabla.getValueAt(r, 3).toString());
      
      int idConf = Integer.parseInt(tabla.getValueAt(r, 5).toString());
      for (int i = 0; i < cmbConfederacion.getItemCount(); i++) {
        if (cmbConfederacion.getItemAt(i).getIdConfederacion() == idConf) {
          cmbConfederacion.setSelectedIndex(i);
          break;
        }
      }
    }
  }

  private void limpiarFormulario() {
    txtId.setText(""); txtNombre.setText(""); txtRanking.setText(""); txtValor.setText("");
    if (cmbConfederacion.getItemCount() > 0) cmbConfederacion.setSelectedIndex(0);
    tabla.clearSelection();
  }

  private void crearRegistro() {
    try {
      Equipo eq = leerFormulario();
      if (equipoDAO.crear(eq)) {
        JOptionPane.showMessageDialog(this, "Equipo registrado exitosamente.");
        cargarDatos();
        limpiarFormulario();
      } else {
        JOptionPane.showMessageDialog(this, "Error al crear en BD.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Revisa los campos numéricos.", "Error de Envío", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void actualizarRegistro() {
    if (txtId.getText().isEmpty()) {
      JOptionPane.showMessageDialog(this, "Selecciona un equipo primero.");
      return;
    }
    try {
      Equipo eq = leerFormulario();
      eq.setIdEquipo(Integer.parseInt(txtId.getText()));
      if (equipoDAO.actualizar(eq)) {
        JOptionPane.showMessageDialog(this, "Equipo actualizado exitosamente.");
        cargarDatos();
      } else {
        JOptionPane.showMessageDialog(this, "Error al actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Revisa los campos.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void eliminarRegistro() {
    if (txtId.getText().isEmpty()) return;
    int id = Integer.parseInt(txtId.getText());
    int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar este equipo?\nNota: Fallará si tiene jugadores o partidos vinculados (llave foránea).");
    if (confirm == JOptionPane.YES_OPTION) {
      if (equipoDAO.eliminar(id)) {
        cargarDatos();
        limpiarFormulario();
      } else {
        JOptionPane.showMessageDialog(this, "Error al eliminar. Primero debes eliminar a sus jugadores y partidos vinculados.", "Error de Llave Foránea", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private Equipo leerFormulario() throws Exception {
    Equipo e = new Equipo();
    e.setNombre(txtNombre.getText().trim());
    e.setRankingFifa(Integer.parseInt(txtRanking.getText().trim()));
    e.setValorMercado(Double.parseDouble(txtValor.getText().trim()));
    
    Confederacion c = (Confederacion) cmbConfederacion.getSelectedItem();
    if (c != null) {
      e.setIdConfederacion(c.getIdConfederacion());
    }
    return e;
  }
}
