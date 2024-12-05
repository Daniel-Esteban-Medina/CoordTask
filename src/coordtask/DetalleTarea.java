package coordtask;

import componente.Conexion;
import java.awt.Color;

import java.awt.Component;
import java.awt.Desktop;
import java.net.URI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
public class DetalleTarea extends javax.swing.JFrame {
    private DefaultListModel<String> modeloLista = new DefaultListModel<String>();
    private String codigo = null;
    //Variables bbdd
    private Conexion conect = new Conexion();
    private Connection con = conect.getConexion();
    private Statement st;
    private ResultSet rs;
    public DetalleTarea() {
        this.setTitle("Detalles tarea");
        initComponents();                       
    }
    public DetalleTarea(String titulo, String descripcion, String prioridad, String fechaInicio, String fechaFin, String estado) {
        this.setTitle("Detalles tarea");  
        initComponents();
        // METER DATOS EN LOS CAMPOS
        this.jTextFieldTitulo.setText(titulo);      
        this.jTextAreaDescripcion.setText(descripcion);
        jLabelRellenarPrioridad.setText(prioridad);
        jLabelRellenarEstado.setText(estado);       
        horaMinutosInicio.setHorasMinutos(obtenerH(fechaInicio), obtenerM(fechaInicio));
        horaMinutosFin.setHorasMinutos(obtenerH(fechaFin), obtenerM(fechaFin));        
        this.fechaInicio.establezerFecha(obtenerFech(fechaInicio));
        this.fechaVencimiento.establezerFecha(obtenerFech(fechaFin));        
        rellenarLista(titulo, descripcion, prioridad, fechaInicio, fechaFin, estado);
        adjuntosBajada1.setDownloadUrl(obtenerUrl(codigo));
        // BLOQUEAR COMPONENTES
        horaMinutosInicio.setEditable(false);  horaMinutosFin.setEditable(false);
        this.fechaInicio.setEditable(false);   this.fechaVencimiento.setEditable(false);               
        if(jLabelRellenarPrioridad.getText().equals("ALTA")){
            jLabelRellenarPrioridad.setBackground(new Color(255, 121, 163));
        } 
        if(jLabelRellenarPrioridad.getText().equals("MEDIA")){
            jLabelRellenarPrioridad.setBackground(new Color(255, 204, 153));
        } 
        if(jLabelRellenarPrioridad.getText().equals("BAJA")){
            jLabelRellenarPrioridad.setBackground(new Color(255,252,184));
        }
    }
    private static int obtenerH(String fechaHora){
        //formato fechaHora "yyyy-mm-dd hh:mm"
        String[] partes = fechaHora.split(" ");
        String tiempo = partes[1];
        String[] tiempoPartes = tiempo.split(":");
        String horas = tiempoPartes[0];
        if(horas == "00"){
            horas = "24";
        }
        return Integer.parseInt(horas);
    }
    private static int obtenerM(String fechaHora){
        //formato fechaHora "yyyy-mm-dd hh:mm"
        String[] partes = fechaHora.split(" ");
        String tiempo = partes[1];
        String[] tiempoPartes = tiempo.split(":");
        String minutos = tiempoPartes[1];
        return Integer.parseInt(minutos);
    }
    private static String obtenerFech(String fechaHora){
        //formato fechaHora "yyyy-mm-dd hh:mm"
        String[] partes = fechaHora.split(" ");
        String tiempo = partes[0];
        return tiempo;
    }
    private void rellenarLista(String titulo, String descripcion, String prioridad, String fechaInicio, String fechaFin, String estado) {
        try {
            String tareaQuery = "SELECT Codigo FROM TAREAS WHERE Titulo = ? AND Descripcion = ? AND Prioridad = ? AND Fecha_inicio = ? AND Fecha_vencimiento = ? AND Estado = ?";
            PreparedStatement ps = con.prepareStatement(tareaQuery);
            ps.setString(1, titulo);
            ps.setString(2, descripcion);
            ps.setString(3, prioridad);
            ps.setString(4, fechaInicio);
            ps.setString(5, fechaFin);
            ps.setString(6, estado);
            rs = ps.executeQuery();
            if (rs.next()) {
                codigo = rs.getString("Codigo");
            }
            if (codigo != null) {
                String equiposQuery = "SELECT E.Nombre FROM EQUIPOS E JOIN EQUIPOS_TAREAS ET ON E.Codigo = ET.Cod_equipo WHERE ET.Cod_tarea = ?";
                PreparedStatement psEquipos = con.prepareStatement(equiposQuery);
                psEquipos.setString(1, codigo);
                rs = psEquipos.executeQuery();
                while (rs.next()) {
                    modeloLista.addElement((String) rs.getObject("Nombre"));                  
                }
                listaGruposTarea.setModel(modeloLista);
            } else {
                System.out.println("No se encontró ninguna tarea con los criterios proporcionados.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private String obtenerUrl(String codigo){
        String url = "";
        try {          
            st = con.createStatement();
            rs = st.executeQuery("Select URL From Tareas where Codigo = "+codigo);
        if (rs.next()) {
                url = rs.getString("URL");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return url;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel12 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldTitulo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaDescripcion = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        listaGruposTarea = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        fechaInicio = new componente.Fecha();
        fechaVencimiento = new componente.Fecha();
        horaMinutosInicio = new componente.HoraMinutos();
        horaMinutosFin = new componente.HoraMinutos();
        jButtonSalir = new javax.swing.JButton();
        jButtonModificar = new javax.swing.JButton();
        jLabelRellenarEstado = new javax.swing.JLabel();
        jLabelRellenarPrioridad = new javax.swing.JLabel();
        adjuntosBajada1 = new componente.AdjuntosBajada();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pngegg(3).png"))); // NOI18N
        jLabel12.setText("EQUIPOS");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(561, 26, 161, 65));

        jLabel8.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel8.setText("Fecha inicio:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 118, -1));

        jTextFieldTitulo.setEditable(false);
        jTextFieldTitulo.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        getContentPane().add(jTextFieldTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 132, 261, -1));

        jTextAreaDescripcion.setEditable(false);
        jTextAreaDescripcion.setColumns(20);
        jTextAreaDescripcion.setRows(5);
        jScrollPane1.setViewportView(jTextAreaDescripcion);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 750, -1));

        listaGruposTarea.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(listaGruposTarea);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(563, 180, 198, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pngegg(1).png"))); // NOI18N
        jLabel1.setText("TAREA");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 505, 89));

        jLabel2.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel2.setText("Descripción:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 119, -1));

        jLabel3.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel3.setText("Título:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 101, 89, -1));

        jLabel4.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel4.setText("Estado:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 154, 90, -1));

        jLabel5.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel5.setText("Prioridad:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 154, 91, -1));

        jLabel7.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel7.setText("Fecha vencimiento:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(293, 230, 172, -1));

        jLabel14.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel14.setText("Equipos asignados ");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(573, 120, 168, -1));

        jLabel9.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel9.setText("Hora inicio:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 119, -1));

        jLabel15.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel15.setText("a esta tarea:");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(573, 150, 141, 20));

        jLabel10.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel10.setText("Hora vencimiento:");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(293, 290, 187, -1));
        getContentPane().add(fechaInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, -1));
        getContentPane().add(fechaVencimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(293, 260, -1, -1));
        getContentPane().add(horaMinutosInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, -1, -1));
        getContentPane().add(horaMinutosFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(293, 320, -1, -1));

        jButtonSalir.setBackground(new java.awt.Color(255, 102, 102));
        jButtonSalir.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jButtonSalir.setForeground(new java.awt.Color(255, 255, 255));
        jButtonSalir.setText("SALIR");
        jButtonSalir.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(283, 530, 137, 50));

        jButtonModificar.setBackground(new java.awt.Color(51, 51, 255));
        jButtonModificar.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jButtonModificar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonModificar.setText("MODIFICAR");
        jButtonModificar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificarActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 260, 50));

        jLabelRellenarEstado.setBackground(new java.awt.Color(255, 255, 255));
        jLabelRellenarEstado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabelRellenarEstado.setOpaque(true);
        getContentPane().add(jLabelRellenarEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 110, 20));

        jLabelRellenarPrioridad.setBackground(new java.awt.Color(255, 255, 255));
        jLabelRellenarPrioridad.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelRellenarPrioridad.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabelRellenarPrioridad.setOpaque(true);
        getContentPane().add(jLabelRellenarPrioridad, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 190, 120, 20));
        getContentPane().add(adjuntosBajada1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 120, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        try {
            Tareas t = new Tareas();
            t.setVisible(true);
            this.dispose();
        } catch (IOException ex) {
            Logger.getLogger(DetalleTarea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jButtonModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificarActionPerformed
        String prioridad = jLabelRellenarPrioridad.getText();
        String titulo = jTextFieldTitulo.getText();
        String descripcion = jTextAreaDescripcion.getText();
        String estado = jLabelRellenarEstado.getText();
        String fechaInicio = this.fechaInicio.obtenerDateTime()+" "+horaMinutosInicio.getHorasMinutos();
        String fechaFin = fechaVencimiento.obtenerDateTime()+" "+horaMinutosFin.getHorasMinutos();
        String cod = codigo;
        String url = "NULL";
        if(obtenerUrl(codigo) != null){
            url = obtenerUrl(codigo);
        }
        try {
            AyadirTarea at = new AyadirTarea( titulo,  descripcion,  prioridad,  fechaInicio,  fechaFin,
                    estado,  true, url);
            at.setVisible(true);
            this.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(DetalleTarea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonModificarActionPerformed
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DetalleTarea().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componente.AdjuntosBajada adjuntosBajada1;
    private componente.Fecha fechaInicio;
    private componente.Fecha fechaVencimiento;
    private componente.HoraMinutos horaMinutosFin;
    private componente.HoraMinutos horaMinutosInicio;
    private javax.swing.JButton jButtonModificar;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelRellenarEstado;
    private javax.swing.JLabel jLabelRellenarPrioridad;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextAreaDescripcion;
    private javax.swing.JTextField jTextFieldTitulo;
    private javax.swing.JList<String> listaGruposTarea;
    // End of variables declaration//GEN-END:variables
// OBTENER NULL
}