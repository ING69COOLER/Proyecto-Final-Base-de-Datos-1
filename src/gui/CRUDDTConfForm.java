package gui;

import dao.ConfederacionDAO;
import dao.DirectorTecnicoDAO;
import dao.EquipoDAO;
import model.Confederacion;
import model.DirectorTecnico;
import model.Equipo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class CRUDDTConfForm extends JFrame {

  // DAOs
  private DirectorTecnicoDAO dtDAO;
  private ConfederacionDAO confDAO;
  private EquipoDAO equipoDAO;

  // Componentes DT
  private JTable tablaDT;
  private DefaultTableModel modeloDT;
  private JTextField txtIdDT, txtNombreDT, txtNacionalidadDT, txtFechaDT;
  private JComboBox<Equipo> cmbEquipoDT;

  // Componentes Conf
  private JTable tablaConf;
  private DefaultTableModel modeloConf;
  private JTextField txtIdConf, txtNombreConf;

  public CRUDDTConfForm() {
    dtDAO = new DirectorTecnicoDAO();
    confDAO = new ConfederacionDAO();
    equipoDAO = new EquipoDAO();

    setTitle("Módulo Dual: Directores Técnicos y Confederaciones");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(1000, 600);
    setLocationRelativeTo(null);

    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

    tabbedPane.addTab("‍ Gestión de Directores Técnicos", crearPanelDT());
    tabbedPane.addTab(" Gestión de Confederaciones Mundiales", crearPanelConfederaciones());

    setContentPane(tabbedPane);

    cargarDatosDT();
    cargarDatosConf();
  }

  // ==============================================================
  // PANEL DIRECTOR TÉCNICO
  // ==============================================================
  private JPanel crearPanelDT() {
    JPanel panelPrincipal = new JPanel(new BorderLayout());
    panelPrincipal.setBackground(new Color(25, 30, 40));

    // Tabla
    String[] colDT = {"ID", "Nombre DT", "Nacionalidad", "F. Nacimiento", "Selección Dirigida", "ID Equipo"};
    modeloDT = new DefaultTableModel(colDT, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    tablaDT = new JTable(modeloDT);
    tablaDT.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tablaDT.getSelectionModel().addListSelectionListener(e -> cargarFilaFormularioDT());
    panelPrincipal.add(new JScrollPane(tablaDT), BorderLayout.CENTER);

    // Formulario
    JPanel formWrapper = new JPanel(new BorderLayout());
    formWrapper.setBackground(new Color(30, 35, 50));
    
    JPanel formGrid = new JPanel(new GridLayout(3, 4, 15, 15));
    formGrid.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
    formGrid.setOpaque(false);

    txtIdDT = new JTextField(); txtIdDT.setEditable(false);
    txtNombreDT = new JTextField();
    txtNacionalidadDT = new JTextField();
    txtFechaDT = new JTextField(); txtFechaDT.setToolTipText("yyyy-mm-dd");
    cmbEquipoDT = new JComboBox<>();
    List<Equipo> eqs = equipoDAO.obtenerTodos();
    for (Equipo eq : eqs) cmbEquipoDT.addItem(eq);

    agregarCampo(formGrid, "ID (Auto):", txtIdDT);
    agregarCampo(formGrid, "Nombre Completo:", txtNombreDT);
    agregarCampo(formGrid, "Nacionalidad:", txtNacionalidadDT);
    agregarCampo(formGrid, "Fecha Nac (yyyy-mm-dd):", txtFechaDT);
    agregarCampo(formGrid, "Selección que Dirige:", cmbEquipoDT);
    agregarCampo(formGrid, "", new JLabel("")); // Filler

    formWrapper.add(formGrid, BorderLayout.CENTER);

    // Botones
    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
    btnPanel.setOpaque(false);
    JButton btnCrear = crearBotonNativo(" Nuevo DT", new Color(40, 120, 60));
    btnCrear.addActionListener(e -> crearDT());
    JButton btnActualizar = crearBotonNativo(" Actualizar", new Color(40, 80, 140));
    btnActualizar.addActionListener(e -> actualizarDT());
    JButton btnEliminar = crearBotonNativo(" Eliminar", new Color(140, 40, 40));
    btnEliminar.addActionListener(e -> eliminarDT());
    JButton btnLimpiar = crearBotonNativo("🧹 Limpiar", new Color(100, 100, 100));
    btnLimpiar.addActionListener(e -> limpiarFormDT());

    btnPanel.add(btnCrear); btnPanel.add(btnActualizar); btnPanel.add(btnEliminar); btnPanel.add(btnLimpiar);
    formWrapper.add(btnPanel, BorderLayout.SOUTH);
    
    panelPrincipal.add(formWrapper, BorderLayout.SOUTH);
    return panelPrincipal;
  }

  private void cargarDatosDT() {
    modeloDT.setRowCount(0);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    for (DirectorTecnico dt : dtDAO.obtenerTodos()) {
      Equipo eq = equipoDAO.obtenerPorId(dt.getIdEquipo());
      modeloDT.addRow(new Object[]{
        dt.getIdDt(), dt.getNombre(), dt.getNacionalidad(),
        sdf.format(dt.getFechaNacimiento()),
        (eq != null ? eq.getNombre() : "N/A"), dt.getIdEquipo()
      });
    }
  }

  private void cargarFilaFormularioDT() {
    int r = tablaDT.getSelectedRow();
    if (r >= 0) {
      txtIdDT.setText(tablaDT.getValueAt(r, 0).toString());
      txtNombreDT.setText(tablaDT.getValueAt(r, 1).toString());
      txtNacionalidadDT.setText(tablaDT.getValueAt(r, 2).toString());
      txtFechaDT.setText(tablaDT.getValueAt(r, 3).toString());
      int idEq = Integer.parseInt(tablaDT.getValueAt(r, 5).toString());
      for (int i=0; i < cmbEquipoDT.getItemCount(); i++) {
        if (cmbEquipoDT.getItemAt(i).getIdEquipo() == idEq) {
          cmbEquipoDT.setSelectedIndex(i); break;
        }
      }
    }
  }

  private void limpiarFormDT() {
    txtIdDT.setText(""); txtNombreDT.setText(""); txtNacionalidadDT.setText(""); txtFechaDT.setText("");
    if(cmbEquipoDT.getItemCount()>0) cmbEquipoDT.setSelectedIndex(0);
    tablaDT.clearSelection();
  }

  private void crearDT() {
    try {
      DirectorTecnico dt = leerFormDT();
      if (dtDAO.crear(dt)) {
        JOptionPane.showMessageDialog(this, "Director Técnico registrado.");
        cargarDatosDT(); limpiarFormDT();
      } else JOptionPane.showMessageDialog(this, "Error. Nota: Un equipo solo puede tener 1 DT (Relación 1 a 1).", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) { JOptionPane.showMessageDialog(this, "Revisa la fecha y los datos.", "Error", JOptionPane.ERROR_MESSAGE); }
  }

  private void actualizarDT() {
    if (txtIdDT.getText().isEmpty()) return;
    try {
      DirectorTecnico dt = leerFormDT();
      dt.setIdDt(Integer.parseInt(txtIdDT.getText()));
      if (dtDAO.actualizar(dt)) {
        JOptionPane.showMessageDialog(this, "Director Técnico actualizado.");
        cargarDatosDT();
      } else JOptionPane.showMessageDialog(this, "Error al actualizar. ¿Este equipo ya tiene otro DT?", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) { JOptionPane.showMessageDialog(this, "Revisa la fecha y los datos.", "Error", JOptionPane.ERROR_MESSAGE); }
  }

  private void eliminarDT() {
    if (txtIdDT.getText().isEmpty()) return;
    if (JOptionPane.showConfirmDialog(this, "¿Seguro?") == JOptionPane.YES_OPTION) {
      if (dtDAO.eliminar(Integer.parseInt(txtIdDT.getText()))) {
        cargarDatosDT(); limpiarFormDT();
      } else JOptionPane.showMessageDialog(this, "Error al eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private DirectorTecnico leerFormDT() throws Exception {
    DirectorTecnico dt = new DirectorTecnico();
    dt.setNombre(txtNombreDT.getText().trim());
    dt.setNacionalidad(txtNacionalidadDT.getText().trim());
    dt.setFechaNacimiento(new SimpleDateFormat("yyyy-MM-dd").parse(txtFechaDT.getText().trim()));
    dt.setIdEquipo(((Equipo)cmbEquipoDT.getSelectedItem()).getIdEquipo());
    return dt;
  }


  // ==============================================================
  // PANEL CONFEDERACION
  // ==============================================================
  private JPanel crearPanelConfederaciones() {
    JPanel panelPrincipal = new JPanel(new BorderLayout());
    panelPrincipal.setBackground(new Color(25, 30, 40));

    String[] colConf = {"ID Confederación", "Siglas / Nombre Oficial"};
    modeloConf = new DefaultTableModel(colConf, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    tablaConf = new JTable(modeloConf);
    tablaConf.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tablaConf.getSelectionModel().addListSelectionListener(e -> cargarFilaFormularioConf());
    panelPrincipal.add(new JScrollPane(tablaConf), BorderLayout.CENTER);

    JPanel formWrapper = new JPanel(new BorderLayout());
    formWrapper.setBackground(new Color(30, 35, 50));
    
    JPanel formGrid = new JPanel(new GridLayout(1, 4, 15, 15));
    formGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    formGrid.setOpaque(false);

    txtIdConf = new JTextField(); txtIdConf.setEditable(false);
    txtNombreConf = new JTextField();

    agregarCampo(formGrid, "ID:", txtIdConf);
    agregarCampo(formGrid, "Nombre:", txtNombreConf);
    formWrapper.add(formGrid, BorderLayout.CENTER);

    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
    btnPanel.setOpaque(false);
    JButton btnCrear = crearBotonNativo(" Nueva Conf", new Color(40, 120, 60));
    btnCrear.addActionListener(e -> crearConf());
    JButton btnActualizar = crearBotonNativo(" Actualizar", new Color(40, 80, 140));
    btnActualizar.addActionListener(e -> actualizarConf());
    JButton btnEliminar = crearBotonNativo(" Eliminar", new Color(140, 40, 40));
    btnEliminar.addActionListener(e -> eliminarConf());
    
    btnPanel.add(btnCrear); btnPanel.add(btnActualizar); btnPanel.add(btnEliminar);
    formWrapper.add(btnPanel, BorderLayout.SOUTH);
    
    panelPrincipal.add(formWrapper, BorderLayout.SOUTH);
    return panelPrincipal;
  }

  private void cargarDatosConf() {
    modeloConf.setRowCount(0);
    for (Confederacion c : confDAO.obtenerTodos()) modeloConf.addRow(new Object[]{c.getIdConfederacion(), c.getNombre()});
  }

  private void cargarFilaFormularioConf() {
    int r = tablaConf.getSelectedRow();
    if (r >= 0) {
      txtIdConf.setText(tablaConf.getValueAt(r, 0).toString());
      txtNombreConf.setText(tablaConf.getValueAt(r, 1).toString());
    }
  }

  private void limpiarFormConf() { txtIdConf.setText(""); txtNombreConf.setText(""); tablaConf.clearSelection(); }

  private void crearConf() {
    if(txtNombreConf.getText().trim().isEmpty()) {
      JOptionPane.showMessageDialog(this, "Debe escribir el nombre de la confederación.", "Advertencia", JOptionPane.WARNING_MESSAGE);
      return;
    }
    if (confDAO.crear(new Confederacion(0, txtNombreConf.getText().trim()))) { 
      JOptionPane.showMessageDialog(this, "Confederación creada exitosamente.");
      cargarDatosConf(); 
      limpiarFormConf(); 
    } else {
      JOptionPane.showMessageDialog(this, "Error de base de datos al intentar crear.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void actualizarConf() {
    if (txtIdConf.getText().isEmpty()) return;
    if (confDAO.actualizar(new Confederacion(Integer.parseInt(txtIdConf.getText()), txtNombreConf.getText().trim()))) {
      JOptionPane.showMessageDialog(this, "Confederación actualizada.");
      cargarDatosConf();
    } else {
      JOptionPane.showMessageDialog(this, "Error al actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void eliminarConf() {
    if (txtIdConf.getText().isEmpty()) return;
    if (JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar?\nFallará si tiene equipos vinculados.") == JOptionPane.YES_OPTION) {
      if (confDAO.eliminar(Integer.parseInt(txtIdConf.getText()))) { cargarDatosConf(); limpiarFormConf(); }
      else JOptionPane.showMessageDialog(this, "No se pudo eliminar. Presumiblemente hay Equipos pertenecientes a esta confederación.", "Restricción de Integridad", JOptionPane.ERROR_MESSAGE);
    }
  }

  // ==============================================================
  // UTILIDADES UI
  // ==============================================================
  private void agregarCampo(JPanel panel, String label, JComponent comp) {
    JLabel l = new JLabel(label);
    l.setForeground(Color.WHITE);
    panel.add(l); panel.add(comp);
  }

  private JButton crearBotonNativo(String texto, Color bg) {
    JButton b = new JButton(texto);
    b.setUI(new javax.swing.plaf.basic.BasicButtonUI()); 
    b.setBackground(bg);
    b.setForeground(Color.WHITE);
    b.setFocusPainted(false);
    return b;
  }
}
