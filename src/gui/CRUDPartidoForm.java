package gui;

import dao.EquipoDAO;
import dao.EstadioDAO;
import dao.GrupoDAO;
import dao.PartidoDAO;
import model.Equipo;
import model.Estadio;
import model.Grupo;
import model.Partido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class CRUDPartidoForm extends JFrame {

  private PartidoDAO partidoDAO;
  private GrupoDAO grupoDAO;
  private EquipoDAO equipoDAO;
  private EstadioDAO estadioDAO;

  // Partidos
  private JTable tablaPar;
  private DefaultTableModel modeloPar;
  private JTextField txtIdPar, txtFechaPar;
  private JComboBox<Equipo> cmbEqLocal, cmbEqVisit;
  private JComboBox<Estadio> cmbEstadio;
  private JComboBox<Grupo> cmbGrupoP;

  // Grupos
  private JTable tablaGru;
  private DefaultTableModel modeloGru;
  private JTextField txtIdGru, txtNombreGru;

  public CRUDPartidoForm() {
    partidoDAO = new PartidoDAO();
    grupoDAO = new GrupoDAO();
    equipoDAO = new EquipoDAO();
    estadioDAO = new EstadioDAO();

    setTitle("Módulo de Competición: Partidos y Grupos");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(1000, 600);
    setLocationRelativeTo(null);

    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

    tabbedPane.addTab(" Gestión de Partidos", crearPanelPartidos());
    tabbedPane.addTab(" Gestión de Grupos Fase Inicial", crearPanelGrupos());

    setContentPane(tabbedPane);

    cargarDatosGrupos();
    cargarDatosPartidos();
  }

  // =========================================================
  // MODULO PARTIDOS
  // =========================================================
  private JPanel crearPanelPartidos() {
    JPanel panelPrincipal = new JPanel(new BorderLayout());
    panelPrincipal.setBackground(new Color(25, 30, 40));

    String[] cols = {"ID Partido", "Fecha/Hora", "Equipo Local", "Equipo Visitante", "Estadio", "Letra Grupo"};
    modeloPar = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    tablaPar = new JTable(modeloPar);
    tablaPar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tablaPar.getSelectionModel().addListSelectionListener(e -> cargarFormPar());
    panelPrincipal.add(new JScrollPane(tablaPar), BorderLayout.CENTER);

    JPanel formWrapper = new JPanel(new BorderLayout());
    formWrapper.setBackground(new Color(30, 35, 50));
    
    JPanel formGrid = new JPanel(new GridLayout(2, 6, 10, 15));
    formGrid.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    formGrid.setOpaque(false);
    
    txtIdPar = new JTextField(); txtIdPar.setEditable(false);
    txtFechaPar = new JTextField(); txtFechaPar.setToolTipText("yyyy-mm-dd hh:mm:00");

    cmbEqLocal = new JComboBox<>();
    cmbEqVisit = new JComboBox<>();
    List<Equipo> eqs = equipoDAO.obtenerTodos();
    for(Equipo eq : eqs) { cmbEqLocal.addItem(eq); cmbEqVisit.addItem(eq); }

    cmbEstadio = new JComboBox<>();
    for(Estadio es : estadioDAO.obtenerTodos()) cmbEstadio.addItem(es);

    cmbGrupoP = new JComboBox<>();

    agregarCampo(formGrid, "ID:", txtIdPar);
    agregarCampo(formGrid, "Fecha (yyyy-mm-dd hh:mm:00):", txtFechaPar);
    agregarCampo(formGrid, "Estadio:", cmbEstadio);
    agregarCampo(formGrid, "Equipo Local:", cmbEqLocal);
    agregarCampo(formGrid, "Equipo Visitante:", cmbEqVisit);
    agregarCampo(formGrid, "Grupo Asociado:", cmbGrupoP);

    formWrapper.add(formGrid, BorderLayout.CENTER);

    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
    btnPanel.setOpaque(false);
    JButton btnCrear = crearBotonNativo(" Nuevo Partido", new Color(40, 120, 60));
    btnCrear.addActionListener(e -> crearPar());
    JButton btnActualizar = crearBotonNativo(" Actualizar BD", new Color(40, 80, 140));
    btnActualizar.addActionListener(e -> actualizarPar());
    JButton btnEliminar = crearBotonNativo(" Eliminar", new Color(140, 40, 40));
    btnEliminar.addActionListener(e -> eliminarPar());
    JButton btnLimpiar = crearBotonNativo("Limpiar", new Color(100, 100, 100));
    btnLimpiar.addActionListener(e -> limpiarFormPar());

    btnPanel.add(btnCrear); btnPanel.add(btnActualizar); btnPanel.add(btnEliminar); btnPanel.add(btnLimpiar);
    formWrapper.add(btnPanel, BorderLayout.SOUTH);
    panelPrincipal.add(formWrapper, BorderLayout.SOUTH);

    return panelPrincipal;
  }

  private void cargarDatosPartidos() {
    modeloPar.setRowCount(0);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    for (Partido p : partidoDAO.obtenerTodos()) {
      Equipo eqL = equipoDAO.obtenerPorId(p.getIdEqLocal());
      Equipo eqV = equipoDAO.obtenerPorId(p.getIdEqVisit());
      Estadio es = estadioDAO.obtenerPorId(p.getIdEstadio());
      Grupo gr = grupoDAO.obtenerPorId(p.getIdGrupo());

      modeloPar.addRow(new Object[]{
        p.getIdPartido(),
        sdf.format(p.getFechaHora()),
        (eqL != null ? eqL.getNombre() : "N/A"),
        (eqV != null ? eqV.getNombre() : "N/A"),
        (es != null ? es.getNombre() : "N/A"),
        (gr != null ? gr.getNombre() : "N/A")
      });
    }
  }

  private void cargarFormPar() {
    int r = tablaPar.getSelectedRow();
    if (r >= 0) {
      String idString = tablaPar.getValueAt(r, 0).toString();
      Partido p = partidoDAO.obtenerPorId(Integer.parseInt(idString));
      if (p != null) {
        txtIdPar.setText(String.valueOf(p.getIdPartido()));
        txtFechaPar.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(p.getFechaHora()));
        seleccionarComboPorId(cmbEqLocal, p.getIdEqLocal(), "Equipo");
        seleccionarComboPorId(cmbEqVisit, p.getIdEqVisit(), "Equipo");
        seleccionarComboPorId(cmbEstadio, p.getIdEstadio(), "Estadio");
        seleccionarComboPorId(cmbGrupoP, p.getIdGrupo(), "Grupo");
      }
    }
  }

  private void limpiarFormPar() {
    txtIdPar.setText(""); txtFechaPar.setText(""); tablaPar.clearSelection();
  }

  private void crearPar() {
    try {
      Partido p = leerFormPartido();
      if (partidoDAO.crear(p)) { JOptionPane.showMessageDialog(this, "Partido registrado."); cargarDatosPartidos(); limpiarFormPar(); }
      else JOptionPane.showMessageDialog(this, "Error. Revisar que los equipos no sean el mismo.", "Violación de Regla", JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Formato fecha incorrecto (Usa: AAAA-MM-DD HH:MM:00)", "Error de Tipo", JOptionPane.WARNING_MESSAGE); }
  }

  private void actualizarPar() {
    if(txtIdPar.getText().isEmpty()) return;
    try {
      Partido p = leerFormPartido();
      p.setIdPartido(Integer.parseInt(txtIdPar.getText()));
      if (partidoDAO.actualizar(p)) { JOptionPane.showMessageDialog(this, "Partido actualizado."); cargarDatosPartidos(); }
      else JOptionPane.showMessageDialog(this, "Error de DB.", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Validar fecha.", "Error", JOptionPane.ERROR_MESSAGE); }
  }

  private void eliminarPar() {
    if(txtIdPar.getText().isEmpty()) return;
    if(JOptionPane.showConfirmDialog(this, "¿Borrar Partido?") == JOptionPane.YES_OPTION) {
      if(partidoDAO.eliminar(Integer.parseInt(txtIdPar.getText()))) { cargarDatosPartidos(); limpiarFormPar(); }
      else JOptionPane.showMessageDialog(this, "No se pudo borrar.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private Partido leerFormPartido() throws Exception {
    Partido p = new Partido();
    p.setFechaHora(Timestamp.valueOf(txtFechaPar.getText().trim()));
    
    p.setIdEqLocal(((Equipo)cmbEqLocal.getSelectedItem()).getIdEquipo());
    p.setIdEqVisit(((Equipo)cmbEqVisit.getSelectedItem()).getIdEquipo());
    // Regla de Negocio
    if(p.getIdEqLocal() == p.getIdEqVisit()) throw new Exception("Local y visitante no pueden ser el mismo");
    
    p.setIdEstadio(((Estadio)cmbEstadio.getSelectedItem()).getIdEstadio());
    p.setIdGrupo(((Grupo)cmbGrupoP.getSelectedItem()).getIdGrupo());
    return p;
  }

  // =========================================================
  // MODULO GRUPOS
  // =========================================================
  private JPanel crearPanelGrupos() {
    JPanel panelPrincipal = new JPanel(new BorderLayout());
    panelPrincipal.setBackground(new Color(25, 30, 40));

    String[] columnas = {"ID", "Letra Representativa del Grupo"};
    modeloGru = new DefaultTableModel(columnas, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    tablaGru = new JTable(modeloGru);
    tablaGru.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tablaGru.getSelectionModel().addListSelectionListener(e -> cargarFormGru());
    panelPrincipal.add(new JScrollPane(tablaGru), BorderLayout.CENTER);

    JPanel formWrapper = new JPanel(new BorderLayout());
    formWrapper.setBackground(new Color(30, 35, 50));
    
    JPanel formGrid = new JPanel(new GridLayout(1, 4, 15, 15));
    formGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    formGrid.setOpaque(false);
    
    txtIdGru = new JTextField(); txtIdGru.setEditable(false);
    txtNombreGru = new JTextField(); 

    agregarCampo(formGrid, "ID:", txtIdGru);
    agregarCampo(formGrid, "Letra Única (A, B, C...):", txtNombreGru);

    formWrapper.add(formGrid, BorderLayout.CENTER);

    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
    btnPanel.setOpaque(false);
    JButton btnCrear = crearBotonNativo(" Nuevo Grupo", new Color(40, 120, 60));
    btnCrear.addActionListener(e -> crearGru());
    JButton btnActualizar = crearBotonNativo(" Guardar/Modificar", new Color(40, 80, 140));
    btnActualizar.addActionListener(e -> actualizarGru());
    JButton btnEliminar = crearBotonNativo(" Eliminar Grupo", new Color(140, 40, 40));
    btnEliminar.addActionListener(e -> eliminarGru());
    
    btnPanel.add(btnCrear); btnPanel.add(btnActualizar); btnPanel.add(btnEliminar);
    formWrapper.add(btnPanel, BorderLayout.SOUTH);
    panelPrincipal.add(formWrapper, BorderLayout.SOUTH);

    return panelPrincipal;
  }

  private void cargarDatosGrupos() {
    modeloGru.setRowCount(0);
    cmbGrupoP.removeAllItems();
    for (Grupo g : grupoDAO.obtenerTodos()) {
      modeloGru.addRow(new Object[]{g.getIdGrupo(), g.getNombre()});
      cmbGrupoP.addItem(g);
    }
  }

  private void cargarFormGru() {
    int r = tablaGru.getSelectedRow();
    if (r >= 0) {
      txtIdGru.setText(tablaGru.getValueAt(r, 0).toString());
      txtNombreGru.setText(tablaGru.getValueAt(r, 1).toString());
    }
  }

  private void limpiarFormGru() { txtIdGru.setText(""); txtNombreGru.setText(""); tablaGru.clearSelection(); }

  private void crearGru() {
    String input = txtNombreGru.getText().trim().toUpperCase();
    if(input.isEmpty()) return;
    if(input.length() > 1) { JOptionPane.showMessageDialog(this, "Solo debe ser UNA letra (A-L).", "Error de Regla DB", JOptionPane.WARNING_MESSAGE); return; }
    
    if (grupoDAO.crear(new Grupo(0, input))) { JOptionPane.showMessageDialog(this, "Grupo creado con éxito."); cargarDatosGrupos(); limpiarFormGru(); }
    else JOptionPane.showMessageDialog(this, "Error de DB. Verifica que la Letra esté permitida y no exista ya.", "Denegado", JOptionPane.ERROR_MESSAGE);
  }

  private void actualizarGru() {
    if(txtIdGru.getText().isEmpty()) return;
    String input = txtNombreGru.getText().trim().toUpperCase();
    if(input.length() > 1) { JOptionPane.showMessageDialog(this, "Solo UNA letra (A-L).", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
    
    if (grupoDAO.actualizar(new Grupo(Integer.parseInt(txtIdGru.getText()), input))) { JOptionPane.showMessageDialog(this, "Lista actualizada."); cargarDatosGrupos(); }
    else JOptionPane.showMessageDialog(this, "Restricción de Integridad.", "Error", JOptionPane.ERROR_MESSAGE);
  }

  private void eliminarGru() {
    if(txtIdGru.getText().isEmpty()) return;
    if (grupoDAO.eliminar(Integer.parseInt(txtIdGru.getText()))) { cargarDatosGrupos(); limpiarFormGru(); }
    else JOptionPane.showMessageDialog(this, "Tiene Partidos vinculados, Imposible eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
  }

  // ==============================================================
  // UTILIDADES UI
  // ==============================================================
  private void agregarCampo(JPanel panel, String label, JComponent comp) {
    JLabel l = new JLabel(label); l.setForeground(Color.WHITE);
    panel.add(l); panel.add(comp);
  }

  private JButton crearBotonNativo(String texto, Color bg) {
    JButton b = new JButton(texto); b.setUI(new javax.swing.plaf.basic.BasicButtonUI()); 
    b.setBackground(bg); b.setForeground(Color.WHITE); b.setFocusPainted(false);
    return b;
  }

  private void seleccionarComboPorId(JComboBox<?> combo, int id, String type) {
    for(int i=0; i<combo.getItemCount(); i++) {
      Object o = combo.getItemAt(i);
      if(type.equals("Equipo") && o instanceof Equipo && ((Equipo)o).getIdEquipo() == id) { combo.setSelectedIndex(i); return; }
      if(type.equals("Estadio") && o instanceof Estadio && ((Estadio)o).getIdEstadio() == id) { combo.setSelectedIndex(i); return; }
      if(type.equals("Grupo") && o instanceof Grupo && ((Grupo)o).getIdGrupo() == id) { combo.setSelectedIndex(i); return; }
    }
  }
}
