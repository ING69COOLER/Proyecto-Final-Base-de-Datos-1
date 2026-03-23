package gui;

import dao.EquipoDAO;
import dao.JugadorDAO;
import model.Equipo;
import model.Jugador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Ventana Gráfica (GUI) para aplicar el Patrón CRUD sobre la entidad Jugador.
 * Implementa la lectura de datos, creación interactiva, actualización y borrado
 * interactuando directamente con la base de datos Oracle a través de JugadorDAO.
 */
public class CRUDJugadorForm extends JFrame {

  // Instancia del Objeto de Acceso a Datos (DAO) para Jugador
  private JugadorDAO jugadorDAO;
  // DAO necesario para cargar el JComboBox (Lista desplegable) de países
  private EquipoDAO equipoDAO;
  
  // Componentes visuales de la tabla donde se muestran los resultados SQL
  private JTable tabla;
  private DefaultTableModel modeloTabla;
  
  // Campos
  private JTextField txtId, txtNombre, txtApellido, txtFechaNac, txtPosicion, txtPeso, txtEstatura, txtValor;
  private JComboBox<Equipo> cmbEquipo;

  public CRUDJugadorForm() {
    jugadorDAO = new JugadorDAO();
    equipoDAO = new EquipoDAO();
    initComponents();

    cargarDatos();
  }

  private void initComponents() {
    setTitle("Módulo de Gestión de Jugadores [CRUD 100% Funcional]");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(1000, 600);
    setLocationRelativeTo(null);

    JPanel panelPrincipal = new JPanel(new BorderLayout());
    panelPrincipal.setBackground(new Color(25, 30, 40));
    setContentPane(panelPrincipal);

    JPanel panelNorte = new JPanel(new BorderLayout());
    panelNorte.setBackground(new Color(25, 30, 40));

    // Titulo
    JLabel lblTitulo = new JLabel("  Módulo Oficial de Jugadores", SwingConstants.LEFT);
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
    lblTitulo.setForeground(Color.WHITE);
    lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
    panelNorte.add(lblTitulo, BorderLayout.WEST);

    // Controles de Filtro
    JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
    panelFiltro.setOpaque(false);
    
    JButton btnFiltro = crearBotonNativo("Filtro Fisico", new Color(200, 110, 30));
    btnFiltro.addActionListener(e -> aplicarFiltro6());

    JButton btnFiltroEdad = crearBotonNativo("Rango Edad", new Color(110, 30, 200));
    btnFiltroEdad.addActionListener(e -> aplicarFiltroEdad());
    
    JButton btnReset = crearBotonNativo("Restablecer Tabla", new Color(80, 80, 80));
    btnReset.addActionListener(e -> cargarDatos());
    
    panelFiltro.add(btnFiltro);
    panelFiltro.add(btnFiltroEdad);
    panelFiltro.add(btnReset);
    panelNorte.add(panelFiltro, BorderLayout.EAST);

    panelPrincipal.add(panelNorte, BorderLayout.NORTH);

    // Tabla de datos (Se añade columna con el Nombre del Equipo)
    String[] columnas = {"ID", "Nombre", "Apellido", "F. Nacimiento", "Pos", "Peso", "Est", "Valor", "País Selecc.", "ID Eq"};
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
    JPanel formPanel = new JPanel(new GridLayout(3, 6, 10, 10));
    formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    formPanel.setOpaque(false);
    
    txtId = new JTextField(); txtId.setEditable(false);
    txtNombre = new JTextField(); txtApellido = new JTextField();
    txtFechaNac = new JTextField(); txtFechaNac.setToolTipText("yyyy-mm-dd");
    txtPosicion = new JTextField(); txtPeso = new JTextField();
    txtEstatura = new JTextField(); txtValor = new JTextField();
    cmbEquipo = new JComboBox<>();
    cargarComboEquipos(); // Llena el JComboBox con los equipos reales de Oracle

    agregarCampo(formPanel, "ID (Auto):", txtId);
    agregarCampo(formPanel, "Nombre:", txtNombre);
    agregarCampo(formPanel, "Apellido:", txtApellido);
    agregarCampo(formPanel, "Fecha Nac (yyyy-mm-dd):", txtFechaNac);
    agregarCampo(formPanel, "Posición:", txtPosicion);
    agregarCampo(formPanel, "Peso (kg):", txtPeso);
    agregarCampo(formPanel, "Estatura (m):", txtEstatura);
    agregarCampo(formPanel, "Valor ($):", txtValor);
    agregarCampo(formPanel, "Escoger Selección Nacional:", cmbEquipo);

    panelSur.add(formPanel, BorderLayout.CENTER);

    // Botones
    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
    btnPanel.setOpaque(false);
    
    JButton btnCrear = crearBotonNativo(" Nuevo Jugador", new Color(40, 120, 60));
    btnCrear.addActionListener(e -> crearRegistro());
    
    JButton btnActualizar = crearBotonNativo(" Actualizar Selección", new Color(40, 80, 140));
    btnActualizar.addActionListener(e -> actualizarRegistro());
    
    JButton btnEliminar = crearBotonNativo(" Eliminar Selección", new Color(140, 40, 40));
    btnEliminar.addActionListener(e -> eliminarRegistro());
    
    JButton btnLimpiar = crearBotonNativo("Limpiar Formulario", new Color(100, 100, 100));
    btnLimpiar.addActionListener(e -> limpiarFormulario());

    btnPanel.add(btnCrear);
    btnPanel.add(btnActualizar);
    btnPanel.add(btnEliminar);
    btnPanel.add(btnLimpiar);

    panelSur.add(btnPanel, BorderLayout.SOUTH);
    panelPrincipal.add(panelSur, BorderLayout.SOUTH);
  }

  private void agregarCampo(JPanel panel, String label, JComponent txt) {
    JLabel l = new JLabel(label);
    l.setForeground(Color.WHITE);
    panel.add(l);
    panel.add(txt);
  }
  
  private void cargarComboEquipos() {
    List<Equipo> equipos = equipoDAO.obtenerTodos();
    for (Equipo e : equipos) {
      cmbEquipo.addItem(e);
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

  // --- LOGICA CRUD Y FILTROS ---
  
  private void aplicarFiltro6() {
    List<Equipo> eqs = equipoDAO.obtenerTodos();
    JComboBox<Equipo> cmb = new JComboBox<>(eqs.toArray(new Equipo[0]));
    JTextField pesoMin=new JTextField("50"), pesoMax=new JTextField("100");
    JTextField estMin=new JTextField("1.50"), estMax=new JTextField("2.10");
    
    Object[] msg = {"Busqueda por Selección:", cmb, " ", "Masa Mínima (kg):", pesoMin, "Masa Máxima (kg):", pesoMax, 
            " ", "Altura Mínima (m):", estMin, "Altura Máxima (m):", estMax};
            
    if (JOptionPane.showConfirmDialog(this, msg, "Filtro Biomédico Preciso", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
      try {
        Equipo eq = (Equipo) cmb.getSelectedItem();
        List<Jugador> res = jugadorDAO.filtrarPorFisico(
            Double.parseDouble(pesoMin.getText().replace(',', '.')), Double.parseDouble(pesoMax.getText().replace(',', '.')),
            Double.parseDouble(estMin.getText().replace(',', '.')), Double.parseDouble(estMax.getText().replace(',', '.')), eq.getIdEquipo());
        
        if (res.isEmpty()) { 
          JOptionPane.showMessageDialog(this, "Resultados nulos para este equipo dentro de este rango exacto.", "Vacío", JOptionPane.INFORMATION_MESSAGE); 
          return; 
        }
        
        modeloTabla.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Jugador j : res) {
          modeloTabla.addRow(new Object[]{
            j.getIdJugador(), j.getNombre(), j.getApellido(),
            j.getFechaNacimiento() != null ? sdf.format(j.getFechaNacimiento()) : "",
            j.getPosicion(), j.getPeso(), j.getEstatura(), j.getValorMercado(), 
            eq.getNombre(), j.getIdEquipo()
          });
        }
        JOptionPane.showMessageDialog(this, "Filtro aplicado con éxito. Viendo " + res.size() + " resultados.", "Filtro Exitoso", JOptionPane.INFORMATION_MESSAGE);
      } catch (Exception ex) {
         JOptionPane.showMessageDialog(this, "Excepción analizando valores numéricos.");
      }
    }
  }

  private void aplicarFiltroEdad() {
    List<Equipo> eqs = equipoDAO.obtenerTodos();
    JComboBox<Equipo> cmb = new JComboBox<>(eqs.toArray(new Equipo[0]));
    JTextField edadMin=new JTextField("15"), edadMax=new JTextField("45");
    
    Object[] msg = {
        "Selección:", cmb, " ", 
        "Edad Mínima:", edadMin, 
        "Edad Máxima:", edadMax
    };

    if (JOptionPane.showConfirmDialog(this, msg, "Filtro Etario por Equipo", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
      try {
        Equipo eq = (Equipo) cmb.getSelectedItem();
        int min = Integer.parseInt(edadMin.getText());
        int max = Integer.parseInt(edadMax.getText());
        
        List<Jugador> res = jugadorDAO.filtrarPorEdad(eq.getIdEquipo(), min, max);
        
        if (res.isEmpty()) { 
          JOptionPane.showMessageDialog(this, "Nadie cumple con ese rango de edad en " + eq.getNombre(), "Vacío", JOptionPane.INFORMATION_MESSAGE); 
          return; 
        }
        
        modeloTabla.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Jugador j : res) {
          modeloTabla.addRow(new Object[]{
            j.getIdJugador(), j.getNombre(), j.getApellido(),
            j.getFechaNacimiento() != null ? sdf.format(j.getFechaNacimiento()) : "",
            j.getPosicion(), j.getPeso(), j.getEstatura(), j.getValorMercado(), 
            eq.getNombre(), j.getIdEquipo()
          });
        }
        JOptionPane.showMessageDialog(this, "Se encontraron " + res.size() + " jugadores.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
      } catch (Exception ex) {
         JOptionPane.showMessageDialog(this, "Error: Asegúrate de ingresar números enteros.");
      }
    }
  }

  /**
   * Extrae a todos los jugadores de la base de datos usando SELECT
   * y las pinta en la JTable de Java Swing fila por fila.
   */
  private void cargarDatos() {
    // Limpiar las filas actuales de la tabla visualmente
    modeloTabla.setRowCount(0);
    
    // El DAO hace la consulta a Oracle y nos devuelve una Lista Iterable
    List<Jugador> lista = jugadorDAO.obtenerTodos();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    for (Jugador j : lista) {
      // Buscamos a base del ID, qué equipo es para mostrar texto legible ("Brasil") en la tabla
      Equipo eq = equipoDAO.obtenerPorId(j.getIdEquipo());
      String nombreEq = (eq != null) ? eq.getNombre() : "N/A";
      
      // Agregar iterativamente los datos a las columnas programadas en initComponents()
      modeloTabla.addRow(new Object[]{
        j.getIdJugador(), j.getNombre(), j.getApellido(),
        j.getFechaNacimiento() != null ? sdf.format(j.getFechaNacimiento()) : "",
        j.getPosicion(), j.getPeso(), j.getEstatura(), j.getValorMercado(), 
        nombreEq, j.getIdEquipo()
      });
    }
  }

  /**
   * Método activado por EventListener: Cuando el usuario hace "clic"
   * sobre una fila de la tabla, este método clona todos los valores
   * hacia las cajas de texto (formulario inferior) para facilitar su actualización.
   */
  private void cargarDatosEnFormulario() {
    int r = tabla.getSelectedRow();
    if (r >= 0) {
      // Columna 0 es el ID, Columna 1 es Nombre, etc.
      txtId.setText(tabla.getValueAt(r, 0).toString());
      txtNombre.setText(tabla.getValueAt(r, 1).toString());
      txtApellido.setText(tabla.getValueAt(r, 2).toString());
      txtFechaNac.setText(tabla.getValueAt(r, 3).toString());
      txtPosicion.setText(tabla.getValueAt(r, 4).toString());
      txtPeso.setText(tabla.getValueAt(r, 5).toString());
      txtEstatura.setText(tabla.getValueAt(r, 6).toString());
      txtValor.setText(tabla.getValueAt(r, 7).toString());
      
      // Auto-seleccionar el JComboBox con el ID del equipo guardado (columna 9)
      int idEq = Integer.parseInt(tabla.getValueAt(r, 9).toString());
      for (int i = 0; i < cmbEquipo.getItemCount(); i++) {
        if (cmbEquipo.getItemAt(i).getIdEquipo() == idEq) {
          cmbEquipo.setSelectedIndex(i);
          break;
        }
      }
    }
  }

  private void limpiarFormulario() {
    txtId.setText(""); txtNombre.setText(""); txtApellido.setText("");
    txtFechaNac.setText(""); txtPosicion.setText(""); txtPeso.setText("");
    txtEstatura.setText(""); txtValor.setText("");
    if (cmbEquipo.getItemCount() > 0) cmbEquipo.setSelectedIndex(0);
    tabla.clearSelection();
  }

  /**
   * Valida que lo escrito en las cajas de texto no sea texto malicioso,
   * recolecta los datos en un Objeto `Jugador`, y llama al DAO `crear(j)`.
   */
  private void crearRegistro() {
    try {
      // Convierte todos los campos en un Objeto del modelo (POJO)
      Jugador j = leerFormulario();
      // Llama el método INSERT de la Base de datos
      if (jugadorDAO.crear(j)) {
        JOptionPane.showMessageDialog(this, "Jugador creado exitosamente.");
        cargarDatos(); // Refresca la tabla visual
        limpiarFormulario(); // Vacía las cajas de texto
      } else {
        JOptionPane.showMessageDialog(this, "Error al crear en BD.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    } catch (Exception e) {
      // Se invoca si por ejemplo en 'estatura' el usuario escribió "alto" en vez de "1.80"
      JOptionPane.showMessageDialog(this, "Revisa los campos numéricos y la fecha.", "Error de Envío", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void actualizarRegistro() {
    if (txtId.getText().isEmpty()) {
      JOptionPane.showMessageDialog(this, "Selecciona un jugador primero.");
      return;
    }
    try {
      Jugador j = leerFormulario();
      j.setIdJugador(Integer.parseInt(txtId.getText()));
      if (jugadorDAO.actualizar(j)) {
        JOptionPane.showMessageDialog(this, "Jugador actualizado exitosamente.");
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
    int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar?");
    if (confirm == JOptionPane.YES_OPTION) {
      if (jugadorDAO.eliminar(id)) {
        cargarDatos();
        limpiarFormulario();
      } else {
        JOptionPane.showMessageDialog(this, "Error al eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private Jugador leerFormulario() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Jugador j = new Jugador();
    j.setNombre(txtNombre.getText().trim());
    j.setApellido(txtApellido.getText().trim());
    j.setFechaNacimiento(sdf.parse(txtFechaNac.getText().trim()));
    j.setPosicion(txtPosicion.getText().trim());
    j.setPeso(Double.parseDouble(txtPeso.getText().trim()));
    j.setEstatura(Double.parseDouble(txtEstatura.getText().trim()));
    j.setValorMercado(Double.parseDouble(txtValor.getText().trim()));
    
    // Extraer el ID correctamente desde el ComoboBox de Orientación a Objetos
    Equipo eqSeleccionado = (Equipo) cmbEquipo.getSelectedItem();
    if (eqSeleccionado != null) {
      j.setIdEquipo(eqSeleccionado.getIdEquipo());
    }
    
    return j;
  }
}
