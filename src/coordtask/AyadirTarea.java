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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
public class AyadirTarea extends javax.swing.JFrame {
    private String link = "NULL";
    private int codTarea = -1;
    //Variables bbdd
    private Conexion conect = new Conexion();
    private Connection con = conect.getConexion();
    private Statement st;
    private ResultSet rs;
    private boolean editarOcrear = false;
    public AyadirTarea() throws SQLException {
        initComponents(); 
        this.listasHorizon1.setEncabezadoListaLlena("TODOS LOS EQUIPOS");
        this.listasHorizon1.setEncabezadoListaVacia("EQUIPOS ASOCIADOS");
        this.setTitle("AÑADIR TAREAS");
        rellenarLista(-1);
        jComboBoxPrioridad.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight); 
                //Despegable                
                if(index == 2){
                    label.setBackground(new Color(255, 121, 163));
                } 
                if(index == 1){
                    label.setBackground(new Color(255, 204, 153));
                } 
                if(index == 0){
                    label.setBackground(new Color(255,252,184));
                }// caja                  
                if(jComboBoxPrioridad.getSelectedIndex() == 2){
                    jComboBoxPrioridad.setBackground(new Color(255, 121, 163));
                } 
                if(jComboBoxPrioridad.getSelectedIndex() == 1){
                    jComboBoxPrioridad.setBackground(new Color(255, 204, 153));
                } 
                if(jComboBoxPrioridad.getSelectedIndex() == 0){
                    jComboBoxPrioridad.setBackground(new Color(255,252,184));
                }
                return label;
            }
        });
    }
    public AyadirTarea(String titulo, String descripcion, String prioridad, String fechaInicio, String fechaFin, 
                        String estado, boolean editarOañadir, String URL) throws SQLException {
        initComponents();  
        this.listasHorizon1.setEncabezadoListaLlena("TODOS LOS EQUIPOS");
        this.listasHorizon1.setEncabezadoListaVacia("EQUIPOS ASOCIADOS");
        editarOcrear = true;
        this.setTitle("AÑADIR TAREA");
        String url = "";
        this.jTextFieldTitulo.setText(titulo);
        this.jTextAreaDescripcion.setText(descripcion);       
        switch (prioridad) {
            case "ALTA":
                this.jComboBoxPrioridad.setSelectedIndex(2);
            break;
            case "MEDIA":
                this.jComboBoxPrioridad.setSelectedIndex(1);
            break;
            case "BAJA":
                this.jComboBoxPrioridad.setSelectedIndex(0);
            break;
        }
        switch (estado) {
            case "PENDIENTE":
                this.jComboBoxEstado.setSelectedIndex(2);
            break;
            case "EN PROGRESO":
                this.jComboBoxEstado.setSelectedIndex(1);
            break;
            case "COMPLETADA":
                this.jComboBoxEstado.setSelectedIndex(0);
            break;
        }
        horaMinutosInicio.setHorasMinutos(obtenerH(fechaInicio), obtenerM(fechaInicio));
        horaMinutosFin.setHorasMinutos(obtenerH(fechaFin), obtenerM(fechaFin));        
        this.fechaInicio.establezerFecha(obtenerFech(fechaInicio));
        this.fechaFin.establezerFecha(obtenerFech(fechaFin));  
        if(this.adjuntosSubida1.getLinkArchivo() != null){
         url = this.adjuntosSubida1.getLinkArchivo();
        }
        if(url != ""){
            link = this.adjuntosSubida1.getLinkArchivo();
        }       
        jComboBoxPrioridad.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight); 
                //Despegable                
                if(index == 2){
                    label.setBackground(new Color(255, 121, 163));
                } 
                if(index == 1){
                    label.setBackground(new Color(255, 204, 153));
                } 
                if(index == 0){
                    label.setBackground(new Color(255,252,184));
                }// caja                  
                if(jComboBoxPrioridad.getSelectedIndex() == 2){
                    jComboBoxPrioridad.setBackground(new Color(255, 121, 163));
                } 
                if(jComboBoxPrioridad.getSelectedIndex() == 1){
                    jComboBoxPrioridad.setBackground(new Color(255, 204, 153));
                } 
                if(jComboBoxPrioridad.getSelectedIndex() == 0){
                    jComboBoxPrioridad.setBackground(new Color(255,252,184));
                }
                return label;
            }
        });
        rellenarLista(obtenerCodigoTarea(jTextFieldTitulo.getText(), jTextAreaDescripcion.getText()));
    }
    public void editarOcrear() throws SQLException{        
            st = con.createStatement();
            if(editarOcrear == true){
            String sql = "UPDATE Tareas "+
            "SET Titulo = '"+jTextFieldTitulo.getText()+"', "+
            "Descripcion = '"+jTextAreaDescripcion.getText()+"', "+
            "Prioridad = '"+jComboBoxPrioridad.getSelectedItem().toString()+"', "+
            "Estado = '"+jComboBoxEstado.getSelectedItem().toString()+"', "+
            "Fecha_inicio = '"+fechaInicio.obtenerDateTime()+" "+horaMinutosInicio.getHorasMinutos()+"', "+
            "Fecha_vencimiento = '"+fechaFin.obtenerDateTime()+" "+horaMinutosFin.getHorasMinutos()+"', "+
            "URL = '"+this.link+"'"+
            "WHERE codigo = "+this.codTarea;
            st.executeUpdate(sql);
            } else{
                String archivo = "NULL";
                if(adjuntosSubida1.getLinkArchivo() != null){
                    archivo =  adjuntosSubida1.getLinkArchivo();
                }
                String sql = "INSERT INTO TAREAS (Titulo, Descripcion, Prioridad, Estado, Fecha_inicio, Fecha_vencimiento, URL) VALUES\n" +
                 "('" + jTextFieldTitulo.getText() + "', '" + jTextAreaDescripcion.getText() + "', '" + 
                 jComboBoxPrioridad.getSelectedItem().toString() + "', '" + 
                 jComboBoxEstado.getSelectedItem().toString() + "', '" + 
                 fechaInicio.obtenerDateTime() + " " + horaMinutosInicio.getHorasMinutos() + "', '" + 
                 fechaFin.obtenerDateTime() + " " + horaMinutosFin.getHorasMinutos() + "', '" + archivo + "')";
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.executeUpdate();
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    codTarea = rs.getInt(1);  
                }
            }
    }
    public void rellenarLista(int cod) throws SQLException {
    st = con.createStatement();
    if (!editarOcrear) {
        rs = st.executeQuery("SELECT DISTINCT Nombre FROM Equipos");
        listasHorizon1.listaLlenaRellenarListaEquipos(rs);
    } else {
        // Obtener todos los equipos no asociados a esta tarea
        String sqlListaLlena = "SELECT DISTINCT e.Nombre FROM Equipos e " +
            "LEFT JOIN EQUIPOS_TAREAS te ON e.Codigo = te.Cod_equipo " +
            "WHERE te.Cod_equipo IS NULL OR te.Cod_tarea != ?";
        PreparedStatement ps = con.prepareStatement(sqlListaLlena);
        ps.setInt(1, cod);
        rs = ps.executeQuery();
        listasHorizon1.listaLlenaRellenarListaEquipos(rs);

        // Obtener los equipos asociados a esta tarea
        String sqlListaVacia = "SELECT DISTINCT e.Nombre FROM Equipos e " +
            "INNER JOIN EQUIPOS_TAREAS te ON e.Codigo = te.Cod_equipo " +
            "WHERE te.Cod_tarea = ?";
        PreparedStatement ps2 = con.prepareStatement(sqlListaVacia);
        ps2.setInt(1, cod);
        rs = ps2.executeQuery();

        // Verificar si el ResultSet está vacío
        if (!rs.next()) {
            System.out.println("La consulta sqlListaVacia no devolvió resultados para cod: " + cod);
        } else {
            listasHorizon1.listaVaciaRellenarListaEquipos(rs);
        }
    }
}
    // LISTAS 
    public void insertarRelacionEquipoTarea(int codEquipo, int codTarea) {
        String sql = "INSERT INTO EQUIPOS_TAREAS (Cod_equipo, Cod_tarea) VALUES (?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);  
            ps.setInt(1, codEquipo);
            ps.setInt(2, codTarea);     
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private int obtenerCodigoEquipo(String nombre) {
        int cod = -1;
        String sql = "SELECT Codigo FROM Equipos WHERE Nombre = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cod = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cod;
    }
    private int obtenerCodigoTarea(String titulo, String descripcion) {
        int cod = -1;
        String sql = "SELECT Codigo FROM TAREAS WHERE Titulo = ? AND Descripcion = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, titulo);
            ps.setString(2, descripcion);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cod = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cod;
    }
    private void crearRelaciones(){
        int codTarea = obtenerCodigoTarea(jTextFieldTitulo.getText(), jTextAreaDescripcion.getText());
        eliminarRelacionesEquipo(codTarea);
        String[] nombresEquipos = listasHorizon1.getListaVaciaEquipos(); 
        for (int i = 0; i < nombresEquipos.length; i++) {
            int codEquipo = obtenerCodigoEquipo(nombresEquipos[i]); 
            if(editarOcrear == false){
                codTarea = this.codTarea;  
            }
            insertarRelacionEquipoTarea(codEquipo, codTarea); 
        }
    }
    private void eliminarRelacionesEquipo(int codTarea) {
    String sql = "DELETE FROM EQUIPOS_TAREAS WHERE Cod_tarea = ?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, codTarea);
        ps.executeUpdate(); 
    } catch (SQLException e) {
        e.printStackTrace(); 
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
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBoxEstado = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldTitulo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaDescripcion = new javax.swing.JTextArea();
        jComboBoxPrioridad = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        fechaInicio = new componente.Fecha();
        fechaFin = new componente.Fecha();
        horaMinutosInicio = new componente.HoraMinutos();
        horaMinutosFin = new componente.HoraMinutos();
        jButton1 = new javax.swing.JButton();
        jButtonEditarCrear = new javax.swing.JButton();
        adjuntosSubida1 = new componente.AdjuntosSubida();
        listasHorizon1 = new listas.ListasHorizon();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jComboBoxEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PENDIENTE", "EN PROGRESO", "COMPLETADA" }));
        getContentPane().add(jComboBoxEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 185, -1, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pngegg(3).png"))); // NOI18N
        jLabel12.setText("EQUIPOS");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(523, 26, 161, 65));

        jLabel8.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel8.setText("Fecha inicio:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 219, 118, -1));
        getContentPane().add(jTextFieldTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 132, 261, -1));

        jTextAreaDescripcion.setColumns(20);
        jTextAreaDescripcion.setRows(5);
        jScrollPane1.setViewportView(jTextAreaDescripcion);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 433, 505, 97));

        jComboBoxPrioridad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BAJA", "MEDIA", "ALTA" }));
        getContentPane().add(jComboBoxPrioridad, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 185, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pngegg(1).png"))); // NOI18N
        jLabel1.setText("TAREA");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 505, 89));

        jLabel2.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel2.setText("Descripción:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 403, 119, -1));

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
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 219, 172, -1));

        jLabel9.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel9.setText("Hora inicio:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 279, 119, -1));

        jLabel10.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel10.setText("Hora vencimiento:");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 279, 187, -1));
        getContentPane().add(fechaInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 249, -1, -1));
        getContentPane().add(fechaFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 249, -1, -1));
        getContentPane().add(horaMinutosInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 313, -1, -1));
        getContentPane().add(horaMinutosFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 313, -1, -1));

        jButton1.setBackground(new java.awt.Color(255, 102, 102));
        jButton1.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("SALIR");
        jButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 460, 240, 50));

        jButtonEditarCrear.setBackground(new java.awt.Color(153, 255, 153));
        jButtonEditarCrear.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jButtonEditarCrear.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEditarCrear.setText("GUARDAR");
        jButtonEditarCrear.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonEditarCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditarCrearActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonEditarCrear, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 380, 238, 50));
        getContentPane().add(adjuntosSubida1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 100, -1, -1));
        getContentPane().add(listasHorizon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 110, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            Tareas t = new Tareas();
            t.setVisible(true);
            this.dispose();
        } catch (IOException ex) {
            Logger.getLogger(AyadirTarea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonEditarCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditarCrearActionPerformed
        try {
        editarOcrear();
        crearRelaciones();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, 
                    "No se pudo añadir o modificar el equipo. Intenta de nuevo.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    }//GEN-LAST:event_jButtonEditarCrearActionPerformed
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new AyadirTarea().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(AyadirTarea.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componente.AdjuntosSubida adjuntosSubida1;
    private componente.Fecha fechaFin;
    private componente.Fecha fechaInicio;
    private componente.HoraMinutos horaMinutosFin;
    private componente.HoraMinutos horaMinutosInicio;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonEditarCrear;
    private javax.swing.JComboBox<String> jComboBoxEstado;
    private javax.swing.JComboBox<String> jComboBoxPrioridad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaDescripcion;
    private javax.swing.JTextField jTextFieldTitulo;
    private listas.ListasHorizon listasHorizon1;
    // End of variables declaration//GEN-END:variables
}
