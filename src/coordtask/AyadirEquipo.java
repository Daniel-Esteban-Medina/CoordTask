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
import java.sql.SQLException;
import java.sql.PreparedStatement;
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
public class AyadirEquipo extends javax.swing.JFrame {
    private Conexion conect = new Conexion();
    private Connection con = conect.getConexion();
    private Statement st;
    private ResultSet rs;
    private boolean editarOcrear = false;
    private int codEquipo = -1;
    public AyadirEquipo(String nombre, String descripcion) throws SQLException {
        initComponents();  
        this.setTitle("EDITAR EQUIPO");
        this.setResizable(false);
        editarOcrear = true;
        this.listasHorizon1.setEncabezadoListaLlena("TODAS LAS PERSONAS");
        this.listasHorizon1.setEncabezadoListaVacia("PERSONAS ASOCIADAS");
        jTextFieldTitulo.setText(nombre);
        jTextAreaDescripcion.setText(descripcion);
        int cod = obtenerCodigoEquipo(jTextFieldTitulo.getText(),jTextAreaDescripcion.getText());
        this.rellenarLista(cod); 
    }

    public AyadirEquipo() throws SQLException {
        initComponents();
        this.setTitle("CREAR EQUIPO");
        this.rellenarLista(1);
        this.listasHorizon1.setEncabezadoListaLlena("TODAS LAS PERSONAS");
        this.listasHorizon1.setEncabezadoListaVacia("PERSONAS ASOCIADAS");
    }
    public void editarOcrear() throws SQLException{       
            if(comprobacionFormato()){
            if(editarOcrear == true){
            st = con.createStatement();
            int codigo = obtenerCodigoEquipo(jTextFieldTitulo.getText(),jTextAreaDescripcion.getText());
            String sql = "UPDATE Equipos "+
            "SET Nombre = '"+jTextFieldTitulo.getText()+"', "+
            "Descripcion = '"+jTextAreaDescripcion.getText()+"' "+
            "WHERE codigo = "+Integer.toString(codigo);
            st.executeUpdate(sql);
            } else{
                String sql = "INSERT INTO Equipos (Nombre, Descripcion) VALUES (?, ?)";
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, jTextFieldTitulo.getText());
                ps.setString(2, jTextAreaDescripcion.getText());
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    codEquipo = rs.getInt(1); 
                }
            }
            }else{
                JOptionPane.showMessageDialog(null, 
                    "No se pudo añadir o modificar el equipo. Intenta de nuevo.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
    }
    public void rellenarLista(int cod) throws SQLException{
        st = con.createStatement();
        if(editarOcrear == false){            
            rs = st.executeQuery("Select Nombre, Apellidos From Personas");
            listasHorizon1.listaLlenaRellenarListaPersonas(rs);
        }else{
            // Obtener todas las personas no asociadas a ese equipo
            String sqlListaLlena = "SELECT p.Nombre, p.Apellidos FROM PERSONAS p "+
            "LEFT JOIN PERSONAS_EQUIPOS pe ON p.Codigo = pe.Cod_persona AND pe.Cod_equipo = "+cod+
            " WHERE pe.Cod_equipo IS NULL";
            rs = st.executeQuery(sqlListaLlena);
            listasHorizon1.listaLlenaRellenarListaPersonas(rs);
            // Obtener personas asociadas a este equipo
            String sqlListaVacia = "SELECT p.Nombre, p.Apellidos FROM PERSONAS p "+
            "INNER JOIN PERSONAS_EQUIPOS pe ON p.Codigo = pe.Cod_persona "+
            "WHERE pe.Cod_equipo = "+cod;
            rs = st.executeQuery(sqlListaVacia);
            listasHorizon1.listaVaciaRellenarListaPersonas(rs);
        }
    }
    public void insertarRelacionPersonaEquipo(int codPersona, int codEquipo) {
    String sql = "INSERT INTO PERSONAS_EQUIPOS (Cod_persona, Cod_equipo) VALUES (?, ?)";
    try {
        PreparedStatement ps = con.prepareStatement(sql);  
        ps.setInt(1, codPersona);
        ps.setInt(2, codEquipo);     
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    private int obtenerCodigoPersona(String nombres, String apellidos) {
    int cod = -1;
    String sql = "SELECT Codigo FROM Personas WHERE Nombre = ? AND Apellidos = ?";
    try {
        PreparedStatement ps = con.prepareStatement(sql);  
        ps.setString(1, nombres);
        ps.setString(2, apellidos);     
        ResultSet rs = ps.executeQuery(); 
        if (rs.next()) {
            cod = rs.getInt(1);
        } else {
            System.err.println("No se encontró una persona con nombre: " + nombres + " y apellidos: " + apellidos);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return cod;
    }
    private int obtenerCodigoEquipo(String nombre, String descripcion) {
    int cod = -1;
    String sql = "SELECT Codigo FROM Equipos WHERE Nombre = ? AND Descripcion = ?";
    try {
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, nombre);
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
        int codEquipo = obtenerCodigoEquipo(jTextFieldTitulo.getText(),jTextAreaDescripcion.getText());
            eliminarRelacionesEquipo(codEquipo);
            String[] nombres = listasHorizon1.getListaVaciaPersonasNombre();
            String[] apellidos = listasHorizon1.getListaVaciaPersonasApellidos();
        for (int i = 0; i < nombres.length; i++) {
            int codPersona = -1;
            codPersona = obtenerCodigoPersona(nombres[i], apellidos[i]);
            if(editarOcrear == false){
                codEquipo = this.codEquipo;
            }
            insertarRelacionPersonaEquipo(codPersona, codEquipo);
        }
    }
    public boolean comprobacionFormato(){
        boolean formatoAceptable = true;
        if(this.jTextFieldTitulo.getText().equals("")){
            formatoAceptable = false;
        }
        if(this.jTextAreaDescripcion.getText().equals("")){
            formatoAceptable = false;
        }
        return formatoAceptable;
    }
    private void eliminarRelacionesEquipo(int codEquipo) {
    String sql = "DELETE FROM PERSONAS_EQUIPOS WHERE Cod_equipo = ?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, codEquipo);
        ps.executeUpdate(); // Ejecuta la eliminación
    } catch (SQLException e) {
        e.printStackTrace(); // Manejo de excepciones
    }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel12 = new javax.swing.JLabel();
        jTextFieldTitulo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaDescripcion = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButtonCancelar = new javax.swing.JButton();
        jButtonGuardar = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        listasHorizon1 = new listas.ListasHorizon();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pngegg(1)(2).png"))); // NOI18N
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 90, 80));
        getContentPane().add(jTextFieldTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 125, 261, -1));

        jTextAreaDescripcion.setColumns(20);
        jTextAreaDescripcion.setRows(5);
        jScrollPane1.setViewportView(jTextAreaDescripcion);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 505, 97));

        jLabel2.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel2.setText("Miembros");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 90, -1));

        jLabel3.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel3.setText("Nombre:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 94, 90, -1));

        jButtonCancelar.setBackground(new java.awt.Color(255, 102, 102));
        jButtonCancelar.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jButtonCancelar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCancelar.setText("SALIR");
        jButtonCancelar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 70, 230, 50));

        jButtonGuardar.setBackground(new java.awt.Color(153, 255, 153));
        jButtonGuardar.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jButtonGuardar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonGuardar.setText("GUARDAR");
        jButtonGuardar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, 230, 50));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pngegg(3).png"))); // NOI18N
        jLabel16.setText("EQUIPO");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabel4.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel4.setText("Descripción:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 120, -1));
        getContentPane().add(listasHorizon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 310, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        Equipos e = new Equipos();
        e.setVisible(true);
        this.dispose();        
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        try {
        editarOcrear();
        crearRelaciones();
        if(comprobacionFormato()){
        if(editarOcrear == false){
        JOptionPane.showMessageDialog(null, 
            "El equipo ha sido añadido exitosamente.", 
            "Éxito", 
            JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, 
            "El equipo ha sido modificado exitosamente.", 
            "Éxito", 
            JOptionPane.INFORMATION_MESSAGE);
        }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, 
                    "No se pudo añadir o modificar el equipo. Intenta de nuevo.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    }//GEN-LAST:event_jButtonGuardarActionPerformed
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new AyadirEquipo().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(AyadirEquipo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaDescripcion;
    private javax.swing.JTextField jTextFieldTitulo;
    private listas.ListasHorizon listasHorizon1;
    // End of variables declaration//GEN-END:variables
}
