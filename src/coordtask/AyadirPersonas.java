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
public class AyadirPersonas extends javax.swing.JFrame {
    private Conexion conect = new Conexion();
    private Connection con = conect.getConexion();
    private Statement st;
    private ResultSet rs;
    private boolean editarOcrear = false;
    private int cod = -1;
    public AyadirPersonas() {
        this.setTitle("CREAR PERSONA");
        initComponents();               
        this.listasHorizon1.setEncabezadoListaLlena("TODOS LOS EQUIPOS");
        this.listasHorizon1.setEncabezadoListaVacia("EQUIPOS ASOCIADOS");
        try {
            rellenarLista(-1);
        } catch (SQLException ex) {
            Logger.getLogger(AyadirPersonas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public AyadirPersonas(String nombre, String apellidos, String telefono, String correo) {
        this.setTitle("MODIFICAR PERSONA");       
        initComponents();  
        this.listasHorizon1.setEncabezadoListaLlena("TODOS LOS EQUIPOS");
        this.listasHorizon1.setEncabezadoListaVacia("EQUIPOS ASOCIADOS");
        editarOcrear = true;
        this.jTextFieldNombre.setText(nombre);
        this.jTextFieldApellidos.setText(apellidos);
        this.jTextFieldTelefono.setText(telefono);
        this.jTextFieldCorreo.setText(correo);
        try {
            rellenarLista(obtenerCodigoPersona(this.jTextFieldNombre.getText(),this.jTextFieldApellidos.getText()));
        } catch (SQLException ex) {
            Logger.getLogger(AyadirPersonas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void editarOcrear(){
        try {          
            st = con.createStatement();
            if(editarOcrear == true){
            String sql = "UPDATE PERSONAS " +
                "SET Nombre = '"+jTextFieldNombre.getText()+"', " +
                "Apellidos = '"+jTextFieldApellidos.getText()+"', " +
                "Telefono = '"+jTextFieldTelefono.getText()+"', " +
                "Correo_electronico = '"+jTextFieldCorreo.getText()+"' " +
                "WHERE Codigo = " + cod;
            st.executeUpdate(sql);
            } else{
                String sql = 
            "INSERT INTO Personas (Nombre, Apellidos, Telefono, Correo_electronico) VALUES\n" +
            "('"+jTextFieldNombre.getText()+"', '"+jTextFieldApellidos.getText()+"', "+
              "'"+jTextFieldTelefono.getText()+"', '"+jTextFieldCorreo.getText()+"')";
            st.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void rellenarLista(int codPersona) throws SQLException {
    st = con.createStatement();
    if (!editarOcrear) {            
        String sqlTodosEquipos = "SELECT Nombre FROM Equipos";
        rs = st.executeQuery(sqlTodosEquipos);
        listasHorizon1.listaLlenaRellenarListaEquipos(rs);
    } else {
        String sqlEquiposNoAsociados = "SELECT e.Nombre FROM Equipos e " +
            "LEFT JOIN PERSONAS_EQUIPOS pe ON e.Codigo = pe.Cod_equipo " +
            "AND pe.Cod_persona = " + codPersona +
            " WHERE pe.Cod_persona IS NULL";
        rs = st.executeQuery(sqlEquiposNoAsociados);
        listasHorizon1.listaLlenaRellenarListaEquipos(rs);
        String sqlEquiposAsociados = "SELECT e.Nombre FROM Equipos e " +
            "INNER JOIN PERSONAS_EQUIPOS pe ON e.Codigo = pe.Cod_equipo " +
            "WHERE pe.Cod_persona = " + codPersona;
        rs = st.executeQuery(sqlEquiposAsociados);
        listasHorizon1.listaVaciaRellenarListaEquipos(rs);
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

private void crearRelaciones() {
    int codPersona = obtenerCodigoPersona(this.jTextFieldNombre.getText(),this.jTextFieldApellidos.getText());  
    eliminarRelacionesEquipo(codPersona);
    String[] equipos = listasHorizon1.getListaVaciaEquipos();
    for (int i = 0; i < equipos.length; i++) {
        int codEquipo = obtenerCodigoEquipo(equipos[i]);
        if (!editarOcrear) {
            codEquipo = this.cod;
        }
        insertarRelacionPersonaEquipo(codPersona, codEquipo);
    }
}

public boolean comprobacionFormato() {
    boolean formatoAceptable = true;
    if (this.jTextFieldNombre.getText().equals("")) {
        formatoAceptable = false;
    }
    if (this.jTextFieldApellidos.getText().equals("")) {
        formatoAceptable = false;
    }
    if (this.jTextFieldCorreo.getText().equals("")) {
        formatoAceptable = false;
    }
    if (this.jTextFieldTelefono.getText().equals("")) {
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
        jTextFieldApellidos = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButtonCancelar = new javax.swing.JButton();
        jButtonGuardar = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldCorreo = new javax.swing.JTextField();
        jTextFieldNombre = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldTelefono = new javax.swing.JTextField();
        listasHorizon1 = new listas.ListasHorizon();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pngegg(3).png"))); // NOI18N
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 90, 80));
        getContentPane().add(jTextFieldApellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 210, -1));

        jLabel3.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel3.setText("Apellidos:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 90, 90, -1));

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
        getContentPane().add(jButtonCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 120, 230, 50));

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
        getContentPane().add(jButtonGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 40, 230, 50));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pngegg(1)(2).png"))); // NOI18N
        jLabel16.setText("PERSONA");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabel4.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel4.setText("Correo electronico:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 160, 170, -1));

        jLabel5.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel5.setText("Nombre:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 90, -1));
        getContentPane().add(jTextFieldCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 190, 210, -1));
        getContentPane().add(jTextFieldNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 160, -1));

        jLabel6.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel6.setText("DE:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 330, 100, -1));
        getContentPane().add(jTextFieldTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 160, -1));
        getContentPane().add(listasHorizon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 230, -1, -1));

        jLabel7.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel7.setText("Teléfono:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 120, -1));

        jLabel8.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel8.setText("MIEMBRO");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 100, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        Personas p = new Personas();
        p.setVisible(true);
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
                new AyadirPersonas().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField jTextFieldApellidos;
    private javax.swing.JTextField jTextFieldCorreo;
    private javax.swing.JTextField jTextFieldNombre;
    private javax.swing.JTextField jTextFieldTelefono;
    private listas.ListasHorizon listasHorizon1;
    // End of variables declaration//GEN-END:variables
}
