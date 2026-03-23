package gui;

import dao.BitacoraDAO;
import dao.JugadorDAO;
import dao.PartidoDAO;
import model.Bitacora;
import model.Jugador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportesForm extends JFrame {

    private JugadorDAO jugadorDAO;
    private PartidoDAO partidoDAO;
    private BitacoraDAO bitacoraDAO;
    private dao.EquipoDAO equipoDAO;

    public ReportesForm() {
        jugadorDAO = new JugadorDAO();
        partidoDAO = new PartidoDAO();
        bitacoraDAO = new BitacoraDAO();
        equipoDAO = new dao.EquipoDAO();

        setTitle("Módulo Avanzado de Reportes Universitarios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(25, 30, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("📊 Panel Integrado de Consultas SQL Avanzadas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(255, 200, 80));
        panel.add(lblTitulo, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        String[] titulosReporte = {
            "1. Jugador más caro por cada Confederación",
            "2. Todos los Partidos Jugados en un Estadio",
            "3. Equipo más caro de un País Anfitrión",
            "4. Jugadores menores de 20 años",
            "5. Bitácoras por rango de fechas",
            "6. Jugadores en rango de Estatura/Peso",
            "7. Valor del mercado total por Confederación",
            "8. Partidos jugados por un País Anfitrión"
        };

        for (int i = 0; i < titulosReporte.length; i++) {
            final int index = i;
            JButton b = new JButton(titulosReporte[i]);
            b.setUI(new javax.swing.plaf.basic.BasicButtonUI());
            b.setFont(new Font("Segoe UI", Font.BOLD, 14));
            b.setBackground(new Color(40, 60, 100));
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            b.addActionListener(e -> ejecutarReporte(index));

            b.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(new Color(60, 90, 150)); }
                public void mouseExited(java.awt.event.MouseEvent e) { b.setBackground(new Color(40, 60, 100)); }
            });
            btnPanel.add(b);
        }

        panel.add(btnPanel, BorderLayout.CENTER);
        setContentPane(panel);
    }

    private void ejecutarReporte(int opc) {
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
            JOptionPane.showMessageDialog(this, "Error de entrada: " + ex.getMessage());
        }
    }

    private void mostrarTabla(String titulo, String[] columnas, Object[][] datos) {
        JTable table = new JTable(new DefaultTableModel(datos, columnas) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(800, 400));
        JOptionPane.showMessageDialog(this, scroll, titulo, JOptionPane.PLAIN_MESSAGE);
    }

    private void rep1() {
        List<Object[]> res = jugadorDAO.jugadorMasCaroPorConfederacion();
        Object[][] datos = new Object[res.size()][3];
        for (int i=0; i<res.size(); i++) {
            datos[i][0] = res.get(i)[0]; datos[i][1] = res.get(i)[1]; datos[i][2] = res.get(i)[2];
        }
        mostrarTabla("Jugador más caro por Confederación", new String[]{"Confederación", "Jugador", "Valor ($)"}, datos);
    }

    private void rep2() {
        String input = JOptionPane.showInputDialog(this, "Ingrese el ID del Estadio (Ej: 1, 4, 6):");
        if (input == null || input.isEmpty()) return;
        List<Object[]> res = partidoDAO.obtenerPorEstadio(Integer.parseInt(input));
        Object[][] datos = new Object[res.size()][5];
        for (int i=0; i<res.size(); i++) {
            datos[i][0]=res.get(i)[0]; datos[i][1]=res.get(i)[1]; datos[i][2]=res.get(i)[2];
            datos[i][3]=res.get(i)[3].toString(); datos[i][4]=res.get(i)[4];
        }
        mostrarTabla("Partidos en Estadio", new String[]{"ID", "Local", "Visitante", "Fecha", "Estadio"}, datos);
    }
    
    private void rep3() {
        List<Object[]> res = equipoDAO.equipoMasCaroPorPaisAnfitrion();
        Object[][] datos = new Object[res.size()][3];
        for (int i=0; i<res.size(); i++) {
            datos[i][0]=res.get(i)[0]; datos[i][1]=res.get(i)[1]; datos[i][2]=res.get(i)[2];
        }
        mostrarTabla("Equipo más caro de país anfitrión", new String[]{"País", "Equipo (Selección)", "Valor Plantilla ($)"}, datos);
    }

    private void rep4() {
        String input = JOptionPane.showInputDialog(this, "Ingrese el ID del Equipo (Ej: 1=Brasil, 10=Espana):");
        if (input == null || input.isEmpty()) return;
        List<Jugador> res = jugadorDAO.jugadoresMenoresDe20(Integer.parseInt(input));
        Object[][] datos = new Object[res.size()][4];
        for (int i=0; i<res.size(); i++) {
            datos[i][0] = res.get(i).getIdJugador(); datos[i][1] = res.get(i).getNombreCompleto();
            datos[i][2] = res.get(i).getEdad(); datos[i][3] = res.get(i).getFechaNacimiento().toString();
        }
        mostrarTabla("Jugadores Menores de 20 (Equipo " + input + ")", new String[]{"ID", "Nombre", "Edad", "F. Nac"}, datos);
    }

    private void rep5() throws Exception {
        String f1 = JOptionPane.showInputDialog(this, "Fecha Inicio (yyyy-MM-dd):");
        if (f1 == null) return;
        String f2 = JOptionPane.showInputDialog(this, "Fecha Fin (yyyy-MM-dd):");
        if (f2 == null) return;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse(f1); Date d2 = sdf.parse(f2);
        List<Bitacora> res = bitacoraDAO.obtenerPorRango(d1, d2);
        Object[][] datos = new Object[res.size()][4];
        for (int i=0; i<res.size(); i++) {
            datos[i][0]=res.get(i).getIdRegistro(); datos[i][1]=res.get(i).getIdUsuario();
            datos[i][2]=res.get(i).getFechaHoraIngreso(); datos[i][3]=res.get(i).getFechaHoraSalida();
        }
        mostrarTabla("Auditoria de Bitácora", new String[]{"ID", "Usuario", "Ingreso", "Salida"}, datos);
    }

    private void rep6() {
        JTextField pesoMin=new JTextField("60"), pesoMax=new JTextField("80");
        JTextField estMin=new JTextField("1.70"), estMax=new JTextField("1.90");
        JTextField idEq=new JTextField("4");
        Object[] msg = {"Peso Min:", pesoMin, "Peso Max:", pesoMax, "Estatura Min:", estMin, "Estatura Max:", estMax, "ID Equipo:", idEq};
        if (JOptionPane.showConfirmDialog(this, msg, "Filtro Físico", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            List<Jugador> res = jugadorDAO.filtrarPorFisico(
                Double.parseDouble(pesoMin.getText()), Double.parseDouble(pesoMax.getText()),
                Double.parseDouble(estMin.getText()), Double.parseDouble(estMax.getText()), Integer.parseInt(idEq.getText()));
            Object[][] datos = new Object[res.size()][5];
            for (int i=0; i<res.size(); i++) {
                datos[i][0]=res.get(i).getNombreCompleto(); datos[i][1]=res.get(i).getPeso();
                datos[i][2]=res.get(i).getEstatura(); datos[i][3]=res.get(i).getPosicion(); datos[i][4]=res.get(i).getIdEquipo();
            }
            mostrarTabla("Resultados Físicos", new String[]{"Nombre", "Peso", "Estatura", "Posicion", "ID Equipo"}, datos);
        }
    }

    private void rep7() {
        String input = JOptionPane.showInputDialog(this, "Ingrese el ID de la Confederacion (1=UEFA, 2=CONMEBOL...):");
        if (input != null && !input.isEmpty()) {
            double v = jugadorDAO.valorTotalPorConfederacion(Integer.parseInt(input));
            JOptionPane.showMessageDialog(this, "El valor de todos los jugadores de los equipos registrados\nen esa confederación es: $" + v, "Confederación " + input, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void rep8() {
        List<Object[]> res = partidoDAO.partidosPorEquipoPorPais();
        Object[][] datos = new Object[res.size()][3];
        for (int i=0; i<res.size(); i++) {
            datos[i][0]=res.get(i)[0]; datos[i][1]=res.get(i)[1]; datos[i][2]=res.get(i)[2];
        }
        mostrarTabla("Partidos Jugados por País (Excluyendo los suyos)", new String[]{"Equipo", "País Organizador", "Partidos Jugados"}, datos);
    }
}
