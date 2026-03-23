package gui;

import dao.CiudadDAO;
import dao.EstadioDAO;
import dao.PaisAnfitrionDAO;
import model.Ciudad;
import model.Estadio;
import model.PaisAnfitrion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CRUDEstadioForm extends JFrame {

  private EstadioDAO estadioDAO;
  private CiudadDAO ciudadDAO;
  private PaisAnfitrionDAO paisDAO;

  // Componentes Estadio
  private JTable tablaEst;
  private DefaultTableModel modeloEst;
  private JTextField txtIdEst, txtNombreEst, txtCapacidadEst;
  private JComboBox<Ciudad> cmbCiudadEst;

  // Componentes Ciudad
  private JTable tablaCiu;
  private DefaultTableModel modeloCiu;
  private JTextField txtIdCiu, txtNombreCiu;
  private JComboBox<PaisAnfitrion> cmbPaisCiu;

  // Componentes Pais
  private JTable tablaPais;
  private DefaultTableModel modeloPais;
  private JTextField txtIdPais, txtNombrePais;

  public CRUDEstadioForm() {
    estadioDAO = new EstadioDAO();
    ciudadDAO = new CiudadDAO();
    paisDAO = new PaisAnfitrionDAO();

    setTitle("Gestión de Infraestructura: Ciudades y Estadios Sede");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(1000, 600);
    setLocationRelativeTo(null);

    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

    tabbedPane.addTab(" Gestión de Estadios", crearPanelEstadios());
    tabbedPane.addTab(" Gestión de Ciudades Anfitrionas", crearPanelCiudades());
    tabbedPane.addTab(" Gestión de Países", crearPanelPaises());

    setContentPane(tabbedPane);

    cargarDatosPaises();
    cargarDatosCiudades();
    cargarDatosEstadios();
  }

  // =========================================================
  // MODULO ESTADIOS
  // =========================================================
  private JPanel crearPanelEstadios() {
    JPanel panelPrincipal = new JPanel(new BorderLayout());
    panelPrincipal.setBackground(new Color(25, 30, 40));

    String[] columnas = {"ID", "Nombre Oficial", "Aforo / Capacidad", "Ubicación (Ciudad y País)", "ID Ciudad"};
    modeloEst = new DefaultTableModel(columnas, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    tablaEst = new JTable(modeloEst);
    tablaEst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tablaEst.getSelectionModel().addListSelectionListener(e -> cargarFormEst());
    panelPrincipal.add(new JScrollPane(tablaEst), BorderLayout.CENTER);

    JPanel formWrapper = new JPanel(new BorderLayout());
    formWrapper.setBackground(new Color(30, 35, 50));
    
    JPanel formGrid = new JPanel(new GridLayout(2, 4, 15, 15));
    formGrid.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
    formGrid.setOpaque(false);
    
    txtIdEst = new JTextField(); txtIdEst.setEditable(false);
    txtNombreEst = new JTextField(); 
    txtCapacidadEst = new JTextField(); 
    cmbCiudadEst = new JComboBox<>();
    
    agregarCampo(formGrid, "ID (Auto):", txtIdEst);
    agregarCampo(formGrid, "Nombre del Estadio:", txtNombreEst);
    agregarCampo(formGrid, "Capacidad (Espectadores):", txtCapacidadEst);
    agregarCampo(formGrid, "Localidad Asignada:", cmbCiudadEst);

    formWrapper.add(formGrid, BorderLayout.CENTER);

    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
    btnPanel.setOpaque(false);
    JButton btnCrear = crearBotonNativo(" Nuevo Estadio", new Color(40, 120, 60));
    btnCrear.addActionListener(e -> crearEst());
    JButton btnActualizar = crearBotonNativo(" Actualizar Estadio", new Color(40, 80, 140));
    btnActualizar.addActionListener(e -> actualizarEst());
    JButton btnEliminar = crearBotonNativo(" Eliminar Instalación", new Color(140, 40, 40));
    btnEliminar.addActionListener(e -> eliminarEst());
    JButton btnLimpiar = crearBotonNativo(" Limpiar Cajas", new Color(100, 100, 100));
    btnLimpiar.addActionListener(e -> limpiarFormEst());

    btnPanel.add(btnCrear); btnPanel.add(btnActualizar); btnPanel.add(btnEliminar); btnPanel.add(btnLimpiar);
    formWrapper.add(btnPanel, BorderLayout.SOUTH);
    panelPrincipal.add(formWrapper, BorderLayout.SOUTH);

    return panelPrincipal;
  }

  private void cargarDatosEstadios() {
    modeloEst.setRowCount(0);
    cmbCiudadEst.removeAllItems();
    for (Ciudad c : ciudadDAO.obtenerTodos()) cmbCiudadEst.addItem(c);
    
    for (Estadio e : estadioDAO.obtenerTodos()) {
      Ciudad c = ciudadDAO.obtenerPorId(e.getIdCiudad());
      modeloEst.addRow(new Object[]{
        e.getIdEstadio(), e.getNombre(), e.getCapacidad(), 
        (c != null ? c.toString() : "N/A"), e.getIdCiudad()
      });
    }
  }

  private void cargarFormEst() {
    int r = tablaEst.getSelectedRow();
    if (r >= 0) {
      txtIdEst.setText(tablaEst.getValueAt(r, 0).toString());
      txtNombreEst.setText(tablaEst.getValueAt(r, 1).toString());
      txtCapacidadEst.setText(tablaEst.getValueAt(r, 2).toString());
      int idCiudad = Integer.parseInt(tablaEst.getValueAt(r, 4).toString());
      for (int i = 0; i < cmbCiudadEst.getItemCount(); i++) {
        if (cmbCiudadEst.getItemAt(i).getIdCiudad() == idCiudad) {
          cmbCiudadEst.setSelectedIndex(i); break;
        }
      }
    }
  }

  private void limpiarFormEst() { 
    txtIdEst.setText(""); txtNombreEst.setText(""); txtCapacidadEst.setText("");
    if (cmbCiudadEst.getItemCount() > 0) cmbCiudadEst.setSelectedIndex(0);
    tablaEst.clearSelection(); 
  }

  private void crearEst() {
    try {
      Estadio e = new Estadio();
      e.setNombre(txtNombreEst.getText().trim());
      e.setCapacidad(Integer.parseInt(txtCapacidadEst.getText().trim()));
      e.setIdCiudad(((Ciudad)cmbCiudadEst.getSelectedItem()).getIdCiudad());
      if (estadioDAO.crear(e)) { JOptionPane.showMessageDialog(this, "Estadio creado."); cargarDatosEstadios(); limpiarFormEst(); }
      else JOptionPane.showMessageDialog(this, "Error. Ya existe o incumple reglas.", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Capacidad inválida.", "Error", JOptionPane.WARNING_MESSAGE); }
  }

  private void actualizarEst() {
    if (txtIdEst.getText().isEmpty()) return;
    try {
      Estadio e = new Estadio();
      e.setIdEstadio(Integer.parseInt(txtIdEst.getText()));
      e.setNombre(txtNombreEst.getText().trim());
      e.setCapacidad(Integer.parseInt(txtCapacidadEst.getText().trim()));
      e.setIdCiudad(((Ciudad)cmbCiudadEst.getSelectedItem()).getIdCiudad());
      if (estadioDAO.actualizar(e)) { JOptionPane.showMessageDialog(this, "Estadio actualizado."); cargarDatosEstadios(); }
      else JOptionPane.showMessageDialog(this, "Error al actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Verifica los datos.", "Error", JOptionPane.ERROR_MESSAGE); }
  }

  private void eliminarEst() {
    if (txtIdEst.getText().isEmpty()) return;
    if (JOptionPane.showConfirmDialog(this, "¿Borrar Estadio? Fallará si hay partidos ahí.") == JOptionPane.YES_OPTION) {
      if (estadioDAO.eliminar(Integer.parseInt(txtIdEst.getText()))) { cargarDatosEstadios(); limpiarFormEst(); }
      else JOptionPane.showMessageDialog(this, "Tiene partidos vinculados.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  // =========================================================
  // MODULO CIUDADES
  // =========================================================
  private JPanel crearPanelCiudades() {
    JPanel panelPrincipal = new JPanel(new BorderLayout());
    panelPrincipal.setBackground(new Color(25, 30, 40));

    String[] columnas = {"ID", "Nombre Ciudad", "País Anfitrión", "ID País"};
    modeloCiu = new DefaultTableModel(columnas, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    tablaCiu = new JTable(modeloCiu);
    tablaCiu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tablaCiu.getSelectionModel().addListSelectionListener(e -> cargarFormCiu());
    panelPrincipal.add(new JScrollPane(tablaCiu), BorderLayout.CENTER);

    JPanel formWrapper = new JPanel(new BorderLayout());
    formWrapper.setBackground(new Color(30, 35, 50));
    
    JPanel formGrid = new JPanel(new GridLayout(2, 4, 15, 15));
    formGrid.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
    formGrid.setOpaque(false);
    
    txtIdCiu = new JTextField(); txtIdCiu.setEditable(false);
    txtNombreCiu = new JTextField(); 
    cmbPaisCiu = new JComboBox<>();
    List<PaisAnfitrion> paises = paisDAO.obtenerTodos();
    for (PaisAnfitrion p : paises) cmbPaisCiu.addItem(p);

    agregarCampo(formGrid, "ID (Auto):", txtIdCiu);
    agregarCampo(formGrid, "Nombre Ciudad:", txtNombreCiu);
    agregarCampo(formGrid, "País al que pertenece:", cmbPaisCiu);
    agregarCampo(formGrid, "", new JLabel("")); // Filler

    formWrapper.add(formGrid, BorderLayout.CENTER);

    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
    btnPanel.setOpaque(false);
    JButton btnCrear = crearBotonNativo(" Nueva Ciudad", new Color(40, 120, 60));
    btnCrear.addActionListener(e -> crearCiu());
    JButton btnActualizar = crearBotonNativo(" Actualizar", new Color(40, 80, 140));
    btnActualizar.addActionListener(e -> actualizarCiu());
    JButton btnEliminar = crearBotonNativo(" Eliminar Ciudad", new Color(140, 40, 40));
    btnEliminar.addActionListener(e -> eliminarCiu());
    
    btnPanel.add(btnCrear); btnPanel.add(btnActualizar); btnPanel.add(btnEliminar);
    formWrapper.add(btnPanel, BorderLayout.SOUTH);
    panelPrincipal.add(formWrapper, BorderLayout.SOUTH);

    return panelPrincipal;
  }

  private void cargarDatosCiudades() {
    modeloCiu.setRowCount(0);
    for (Ciudad c : ciudadDAO.obtenerTodos()) {
      modeloCiu.addRow(new Object[]{ c.getIdCiudad(), c.getNombre(), c.getNombrePais(), c.getIdPaisAnfitrion() });
    }
    // Actualizar el JComboBox de estadios si se modificaron ciudades
    if (cmbCiudadEst != null) {
      cmbCiudadEst.removeAllItems();
      for (Ciudad c : ciudadDAO.obtenerTodos()) cmbCiudadEst.addItem(c);
    }
  }

  private void cargarFormCiu() {
    int r = tablaCiu.getSelectedRow();
    if (r >= 0) {
      txtIdCiu.setText(tablaCiu.getValueAt(r, 0).toString());
      txtNombreCiu.setText(tablaCiu.getValueAt(r, 1).toString());
      int idPais = Integer.parseInt(tablaCiu.getValueAt(r, 3).toString());
      for (int i=0; i < cmbPaisCiu.getItemCount(); i++) {
        if(cmbPaisCiu.getItemAt(i).getIdPaisAnfitrion() == idPais) {
          cmbPaisCiu.setSelectedIndex(i); break;
        }
      }
    }
  }

  private void limpiarFormCiu() { txtIdCiu.setText(""); txtNombreCiu.setText(""); tablaCiu.clearSelection(); }

  private void crearCiu() {
    if(txtNombreCiu.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Escribe nombre"); return; }
    Ciudad c = new Ciudad(); c.setNombre(txtNombreCiu.getText().trim());
    c.setIdPaisAnfitrion(((PaisAnfitrion)cmbPaisCiu.getSelectedItem()).getIdPaisAnfitrion());
    if (ciudadDAO.crear(c)) { JOptionPane.showMessageDialog(this, "Ciudad agregada."); cargarDatosCiudades(); limpiarFormCiu(); }
    else JOptionPane.showMessageDialog(this, "Error al crear.", "Error", JOptionPane.ERROR_MESSAGE);
  }

  private void actualizarCiu() {
    if (txtIdCiu.getText().isEmpty()) return;
    Ciudad c = new Ciudad(); c.setIdCiudad(Integer.parseInt(txtIdCiu.getText())); 
    c.setNombre(txtNombreCiu.getText().trim());
    c.setIdPaisAnfitrion(((PaisAnfitrion)cmbPaisCiu.getSelectedItem()).getIdPaisAnfitrion());
    if (ciudadDAO.actualizar(c)) { JOptionPane.showMessageDialog(this, "Ciudad actualizada."); cargarDatosCiudades(); }
    else JOptionPane.showMessageDialog(this, "Error.", "Error", JOptionPane.ERROR_MESSAGE);
  }

  private void eliminarCiu() {
    if (txtIdCiu.getText().isEmpty()) return;
    if (JOptionPane.showConfirmDialog(this, "¿Borrar Ciudad? Fallará si tiene Estadios.") == JOptionPane.YES_OPTION) {
      if (ciudadDAO.eliminar(Integer.parseInt(txtIdCiu.getText()))) { cargarDatosCiudades(); limpiarFormCiu(); }
      else JOptionPane.showMessageDialog(this, "Tiene Estadios vinculados.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  // =========================================================
  // MODULO PAISES
  // =========================================================
  private JPanel crearPanelPaises() {
    JPanel panelPrincipal = new JPanel(new BorderLayout());
    panelPrincipal.setBackground(new Color(25, 30, 40));

    String[] columnas = {"ID País", "Nombre del País"};
    modeloPais = new DefaultTableModel(columnas, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    tablaPais = new JTable(modeloPais);
    tablaPais.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tablaPais.getSelectionModel().addListSelectionListener(e -> cargarFormPais());
    panelPrincipal.add(new JScrollPane(tablaPais), BorderLayout.CENTER);

    JPanel formWrapper = new JPanel(new BorderLayout());
    formWrapper.setBackground(new Color(30, 35, 50));
    
    JPanel formGrid = new JPanel(new GridLayout(1, 4, 15, 15));
    formGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    formGrid.setOpaque(false);
    
    txtIdPais = new JTextField(); txtIdPais.setEditable(false);
    txtNombrePais = new JTextField(); 

    agregarCampo(formGrid, "ID:", txtIdPais);
    agregarCampo(formGrid, "Nombre (Mexico, USA, Canada):", txtNombrePais);

    formWrapper.add(formGrid, BorderLayout.CENTER);

    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
    btnPanel.setOpaque(false);
    JButton btnCrear = crearBotonNativo(" Nuevo País", new Color(40, 120, 60));
    btnCrear.addActionListener(e -> crearPais());
    JButton btnActualizar = crearBotonNativo(" Actualizar", new Color(40, 80, 140));
    btnActualizar.addActionListener(e -> actualizarPais());
    JButton btnEliminar = crearBotonNativo(" Eliminar País", new Color(140, 40, 40));
    btnEliminar.addActionListener(e -> eliminarPais());
    
    btnPanel.add(btnCrear); btnPanel.add(btnActualizar); btnPanel.add(btnEliminar);
    formWrapper.add(btnPanel, BorderLayout.SOUTH);
    panelPrincipal.add(formWrapper, BorderLayout.SOUTH);

    return panelPrincipal;
  }

  private void cargarDatosPaises() {
    modeloPais.setRowCount(0);
    for (PaisAnfitrion p : paisDAO.obtenerTodos()) modeloPais.addRow(new Object[]{p.getIdPaisAnfitrion(), p.getNombre()});
    
    // Refrescar JComboBox de Ciudades
    if (cmbPaisCiu != null) {
      cmbPaisCiu.removeAllItems();
      for (PaisAnfitrion p : paisDAO.obtenerTodos()) cmbPaisCiu.addItem(p);
    }
  }

  private void cargarFormPais() {
    int r = tablaPais.getSelectedRow();
    if (r >= 0) {
      txtIdPais.setText(tablaPais.getValueAt(r, 0).toString());
      txtNombrePais.setText(tablaPais.getValueAt(r, 1).toString());
    }
  }

  private void limpiarFormPais() { txtIdPais.setText(""); txtNombrePais.setText(""); tablaPais.clearSelection(); }

  private void crearPais() {
    if(txtNombrePais.getText().trim().isEmpty()) return;
    PaisAnfitrion p = new PaisAnfitrion(0, txtNombrePais.getText().trim());
    if (paisDAO.crear(p)) { JOptionPane.showMessageDialog(this, "País creado."); cargarDatosPaises(); limpiarFormPais(); }
    else JOptionPane.showMessageDialog(this, "Error. Recordatorio: Oracle tiene un CHECK CONSTRAINT que dice que un país SOLO PUEDE SER: Mexico, USA o Canada. Si intentas meter otro, la base de datos te bloqueará por seguridad.", "Filtro de Seguridad de Oracle", JOptionPane.ERROR_MESSAGE);
  }

  private void actualizarPais() {
    if(txtIdPais.getText().isEmpty()) return;
    PaisAnfitrion p = new PaisAnfitrion(Integer.parseInt(txtIdPais.getText()), txtNombrePais.getText().trim());
    if (paisDAO.actualizar(p)) { JOptionPane.showMessageDialog(this, "País actualizado."); cargarDatosPaises(); }
    else JOptionPane.showMessageDialog(this, "Error. Seguramente estás intentando ingresar un país no permitido por la DB.", "Error", JOptionPane.ERROR_MESSAGE);
  }

  private void eliminarPais() {
    if(txtIdPais.getText().isEmpty()) return;
    if (JOptionPane.showConfirmDialog(this, "¿Seguro? Fallará si tiene ciudades.") == JOptionPane.YES_OPTION) {
      if (paisDAO.eliminar(Integer.parseInt(txtIdPais.getText()))) { cargarDatosPaises(); limpiarFormPais(); }
      else JOptionPane.showMessageDialog(this, "No se puede eliminar porque tiene Ciudades vinculadas.", "Error de Llave Foránea", JOptionPane.ERROR_MESSAGE);
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
    b.setBackground(bg); b.setForeground(Color.WHITE); b.setFocusPainted(false);
    return b;
  }
}
