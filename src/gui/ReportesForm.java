package gui;

import dao.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportesForm extends JFrame {

  private JugadorDAO jugadorDAO;
  private PartidoDAO partidoDAO;
  private BitacoraDAO bitacoraDAO;
  private EquipoDAO equipoDAO;
  private EstadioDAO estadioDAO;
  private ConfederacionDAO confDAO;

  public ReportesForm() {
    jugadorDAO = new JugadorDAO();
    partidoDAO = new PartidoDAO();
    bitacoraDAO = new BitacoraDAO();
    equipoDAO = new EquipoDAO();
    estadioDAO = new EstadioDAO();
    confDAO = new ConfederacionDAO();

    setTitle("Módulo de Reportes e Inteligencia de Negocios");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(950, 650);
    setLocationRelativeTo(null);

    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(new Color(25, 30, 40));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JLabel lblTitulo = new JLabel(" Panel de Inteligencia Financiera y Deportiva", SwingConstants.CENTER);
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
    lblTitulo.setForeground(new Color(250, 200, 80));
    panel.add(lblTitulo, BorderLayout.NORTH);

    JPanel btnPanel = new JPanel(new GridLayout(4, 2, 20, 20));
    btnPanel.setOpaque(false);
    btnPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

    String[] titulosReporte = {
      "1. Jugador más caro por Confederación",
      "2. Partidos jugados en un Estadio",
      "3. Equipo más caro por País Anfitrión",
      "4. Jugadores menores de 20 años",
      "5. Auditoría de Bitácora (Fechas)",
      "6. Filtro avanzado de Estatura/Peso",
      "7. Valoración total de Confederación",
      "8. Partidos de Visitante por País"
    };

    for (int i = 0; i < titulosReporte.length; i++) {
      final int index = i;
      JButton b = new JButton(titulosReporte[i]);
      b.setUI(new javax.swing.plaf.basic.BasicButtonUI());
      b.setFont(new Font("Segoe UI", Font.BOLD, 15));
      b.setBackground(new Color(40, 65, 105));
      b.setForeground(Color.WHITE);
      b.setFocusPainted(false);
      b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

      b.addActionListener(e -> ejecutarReporte(index));

      b.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(new Color(60, 95, 155)); }
        public void mouseExited(java.awt.event.MouseEvent e) { b.setBackground(new Color(40, 65, 105)); }
      });
      btnPanel.add(b);
    }

    panel.add(btnPanel, BorderLayout.CENTER);
    setContentPane(panel);
  }

  private void ejecutarReporte(int opc) {
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    try {
      switch (opc) {
        case 0: rep1(); break;
        case 1: rep2(); break;
        case 2: rep3(); break;
        case 3: rep4(); break;
        case 4: rep5(); break;
        case 5: rep6(); break;
        case 6: rep7(); break;
        case 7: rep8(); break;
      }
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "Operación cancelada o hubo un error: " + ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
    } finally {
      setCursor(Cursor.getDefaultCursor());
    }
  }

  private void mostrarTabla(String titulo, String[] columnas, Object[][] datos) {
    JTable table = new JTable(new DefaultTableModel(datos, columnas) {
      @Override public boolean isCellEditable(int r, int c) { return false; }
    });
    table.setRowHeight(30);
    table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    for(int x=0;x<table.getColumnCount();x++){
      table.getColumnModel().getColumn(x).setCellRenderer(centerRenderer);
    }

    JScrollPane scroll = new JScrollPane(table);
    scroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), titulo, 
    		javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, 
    		new Font("Segoe UI", Font.BOLD, 18), new Color(40, 60, 90)));
    scroll.setPreferredSize(new Dimension(850, 450));
    JOptionPane.showMessageDialog(this, scroll, "Resultados del Reporte", JOptionPane.PLAIN_MESSAGE);
  }

  // ==============================================================
  // REPORTES
  // ==============================================================
  
  // 1. Jugador top por confederación
  private void rep1() {
    List<Object[]> res = jugadorDAO.jugadorMasCaroPorConfederacion();
    Object[][] datos = new Object[res.size()][3];
    for (int i=0; i<res.size(); i++) {
      datos[i][0] = res.get(i)[0]; datos[i][1] = res.get(i)[1]; datos[i][2] = "$ " + res.get(i)[2];
    }
    mostrarTabla("Jugador más caro de cada Confederación", new String[]{"Confederación", "Nombre del Jugador", "Valor de Mercado"}, datos);
  }

  // 2. Partidos por Estadio usando Dropdown
  private void rep2() {
    List<Estadio> estadios = estadioDAO.obtenerTodos();
    if (estadios.isEmpty()) { JOptionPane.showMessageDialog(this, "No hay estadios registrados."); return; }

    JComboBox<Estadio> cmb = new JComboBox<>(estadios.toArray(new Estadio[0]));
    Object[] msg = {" Seleccione el Estadio a Consultar:", cmb};
    
    if (JOptionPane.showConfirmDialog(this, msg, "Filtro", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
      Estadio e = (Estadio) cmb.getSelectedItem();
      List<Object[]> res = partidoDAO.obtenerPorEstadio(e.getIdEstadio());
      Object[][] datos = new Object[res.size()][5];
      for (int i=0; i<res.size(); i++) {
        datos[i][0]=res.get(i)[0]; datos[i][1]=res.get(i)[1]; datos[i][2]=res.get(i)[2];
        datos[i][3]=res.get(i)[3].toString(); datos[i][4]=res.get(i)[4];
      }
      mostrarTabla("Historial de Partidos - " + e.getNombre(), new String[]{"ID Partido", "Equipo Local", "Equipo Visitante", "Fecha y Hora", "Sede"}, datos);
    }
  }
  
  // 3. Equipo más caro de un País Anfitrión
  private void rep3() {
    List<Object[]> res = equipoDAO.equipoMasCaroPorPaisAnfitrion();
    Object[][] datos = new Object[res.size()][3];
    for (int i=0; i<res.size(); i++) {
      datos[i][0]=res.get(i)[0]; datos[i][1]=res.get(i)[1]; datos[i][2]= "$ " + res.get(i)[2];
    }
    mostrarTabla("Selección más cara del País Organizador", new String[]{"País", "Selección Nacional", "Valor Total"}, datos);
  }

  // 4. Jugadores Menores de 20 (Filtro por Equipo Combobox)
  private void rep4() {
    List<Equipo> eqs = equipoDAO.obtenerTodos();
    if (eqs.isEmpty()) { JOptionPane.showMessageDialog(this, "No hay equipos."); return; }

    JComboBox<Equipo> cmb = new JComboBox<>(eqs.toArray(new Equipo[0]));
    Object[] msg = {"Seleccione el Equipo a Consultar:", cmb};
    
    if (JOptionPane.showConfirmDialog(this, msg, "Jugadores Menores de 20 Años", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
      Equipo eq = (Equipo) cmb.getSelectedItem();
      List<Jugador> res = jugadorDAO.jugadoresMenoresDe20(eq.getIdEquipo());
      if (res.isEmpty()) { JOptionPane.showMessageDialog(this, "Esta selección no tiene jugadores inscritos menores de 20 años.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE); return; }
      Object[][] datos = new Object[res.size()][4];
      for (int i=0; i<res.size(); i++) {
        datos[i][0] = res.get(i).getIdJugador(); datos[i][1] = res.get(i).getNombreCompleto();
        datos[i][2] = res.get(i).getEdad() + " Años"; datos[i][3] = res.get(i).getFechaNacimiento().toString();
      }
      mostrarTabla("Jugadores Menores de 20 Años - " + eq.getNombre(), new String[]{"ID", "Nombre Completo", "Edad", "Fecha de Nacimiento"}, datos);
    }
  }

  // 5. Bitácora por Rango
  private void rep5() throws Exception {
    JTextField txtDe = new JTextField("2026-01-01");
    JTextField txtHasta = new JTextField("2026-12-31");
    Object[] m = {"Fecha Desde (yyyy-MM-dd):", txtDe, "Fecha Hasta (yyyy-MM-dd):", txtHasta};
        if (JOptionPane.showConfirmDialog(this, m, "Filtrar Bitácora", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = sdf.parse(txtDe.getText()); Date d2 = sdf.parse(txtHasta.getText());
            List<Object[]> res = bitacoraDAO.obtenerAuditoria(d1, d2);
            if (res.isEmpty()) { JOptionPane.showMessageDialog(this, "No hay registros de conexión en el rango proporcionado.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE); return; }
            Object[][] datos = new Object[res.size()][4];
            for (int i=0; i<res.size(); i++) {
                datos[i][0]=res.get(i)[0]; datos[i][1]=res.get(i)[1];
                datos[i][2]=res.get(i)[2]; datos[i][3]=(res.get(i)[3]==null?"En sesión":res.get(i)[3]);
            }
            mostrarTabla("Auditoria Registros de Conexión", new String[]{"Cód.", "Nombre de Usuario", "Ingreso", "Salida"}, datos);
        }
  }

  // 6. Filtro Físico Inteligente
  private void rep6() {
    List<Equipo> eqs = equipoDAO.obtenerTodos();
    JComboBox<Equipo> cmb = new JComboBox<>(eqs.toArray(new Equipo[0]));
    JTextField pesoMin=new JTextField("50"), pesoMax=new JTextField("100");
    JTextField estMin=new JTextField("1.50"), estMax=new JTextField("2.10");
    
    Object[] msg = {"De la Selección:", cmb, " ", "Masa Mínima (kg):", pesoMin, "Masa Máxima (kg):", pesoMax, 
            " ", "Altura Mínima (m):", estMin, "Altura Máxima (m):", estMax};
                if (JOptionPane.showConfirmDialog(this, msg, "Criterios Atléticos", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Equipo eq = (Equipo) cmb.getSelectedItem();
            List<Jugador> res = jugadorDAO.filtrarPorFisico(
                Double.parseDouble(pesoMin.getText().replace(',', '.')), Double.parseDouble(pesoMax.getText().replace(',', '.')),
                Double.parseDouble(estMin.getText().replace(',', '.')), Double.parseDouble(estMax.getText().replace(',', '.')), eq.getIdEquipo());
            
            if (res.isEmpty()) { JOptionPane.showMessageDialog(this, "La selección " + eq.getNombre() + " no tiene jugadores registrados dentro de este rango físico.", "Sin Coincidencias", JOptionPane.INFORMATION_MESSAGE); return; }
            Object[][] datos = new Object[res.size()][5];
      for (int i=0; i<res.size(); i++) {
        datos[i][0]=res.get(i).getNombreCompleto(); datos[i][1]=res.get(i).getPeso() + " kg";
        datos[i][2]=res.get(i).getEstatura() + " m"; datos[i][3]=res.get(i).getPosicion(); datos[i][4]=eq.getNombre();
      }
      mostrarTabla("Resultados Biomédicos", new String[]{"Jugador", "Peso", "Estatura", "Posición", "Selección"}, datos);
    }
  }

  // 7. Valor por Confederación
  private void rep7() {
    List<Confederacion> confs = confDAO.obtenerTodos();
    if (confs.isEmpty()) { JOptionPane.showMessageDialog(this, "No hay confederaciones."); return; }

    JComboBox<Confederacion> cmb = new JComboBox<>(confs.toArray(new Confederacion[0]));
    Object[] msg = {" Escoge el Bloque Internacional:", cmb};
    
    if (JOptionPane.showConfirmDialog(this, msg, "Capitalización Continente", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
      Confederacion c = (Confederacion) cmb.getSelectedItem();
      double v = jugadorDAO.valorTotalPorConfederacion(c.getIdConfederacion());
      
      JLabel ans = new JLabel("<html>La carta de capital y activos totales asegurados<br>de la confederación <b>" + c.getNombre() + "</b> es de:<br><br><h1 style='color:#009933; text-align:center;'>$ " + String.format("%.2f", v) + "</h1></html>");
      ans.setFont(new Font("Segoe UI", Font.PLAIN, 16));
      JOptionPane.showMessageDialog(this, ans, "Capitalización de Mercado", JOptionPane.INFORMATION_MESSAGE);
    }
  }

  // 8. Partidos en Anfitriones
  private void rep8() {
    List<Object[]> res = partidoDAO.partidosPorEquipoPorPais();
    Object[][] datos = new Object[res.size()][3];
    for (int i=0; i<res.size(); i++) {
      datos[i][0]=res.get(i)[0]; datos[i][1]=res.get(i)[1]; datos[i][2]=res.get(i)[2] + " Juegos Totales";
    }
    mostrarTabla("Partidos Jugados por País (Excluyendo equipo nacional)", new String[]{"Selección", "País Organizador", "Partidos Jugados"}, datos);
  }
}
