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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
public class DetallePersona extends javax.swing.JFrame {
    private Conexion conect = new Conexion();
    private Connection con = conect.getConexion();
    private Statement st;
    private ResultSet rs;
    private String codigo = "";
    private DefaultListModel<String> modeloLista = new DefaultListModel<String>();
    public DetallePersona(String nombre, String apellidos, String telefono, String correo) {
        initComponents();  
        this.setTitle("DETALLES PESRONA");
        this.jTextFieldNombre.setText(nombre);
        this.jTextFieldApellidos.setText(apellidos);
        this.jTextFieldTelefono.setText(telefono);
        this.jTextFieldCorreo.setText(correo);
        try {
            rellenarLista();
        } catch (SQLException ex) {
            Logger.getLogger(DetallePersona.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private DetallePersona() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    private void rellenarLista() throws SQLException{
        obtenerCodigo(this.jTextFieldNombre.getText(), this.jTextFieldApellidos.getText(),this.jTextFieldTelefono.getText(),this.jTextFieldCorreo.getText());
        String equiposQuery = "SELECT E.Nombre FROM Equipos E JOIN PERSONAS_EQUIPOS PE ON E.Codigo = PE.Cod_equipo WHERE PE.Cod_persona = ?";
                PreparedStatement psEquipos = con.prepareStatement(equiposQuery);
                psEquipos.setString(1, codigo);
                rs = psEquipos.executeQuery();
                while (rs.next()) {
                    modeloLista.addElement((String) rs.getObject("E.Nombre"));                  
                }
                listaEquiposPersonas.setModel(modeloLista);
    }
    private void obtenerCodigo(String nombre, String apellidos,String telefono,String correo) throws SQLException{
        String tareaQuery = "SELECT Codigo FROM Personas WHERE Nombre = ? AND Apellidos = ? AND Telefono = ? AND Correo_electronico = ?";
            PreparedStatement ps = con.prepareStatement(tareaQuery);
            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setString(3, telefono);
            ps.setString(4, correo);
            rs = ps.executeQuery();
            if (rs.next()) {
                codigo = rs.getString("Codigo");
            }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel12 = new javax.swing.JLabel();
        jTextFieldApellidos = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        listaEquiposPersonas = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldCorreo = new javax.swing.JTextField();
        jTextFieldNombre = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldTelefono = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pngegg(3).png"))); // NOI18N
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, 90, 80));

        jTextFieldApellidos.setEditable(false);
        getContentPane().add(jTextFieldApellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 210, -1));

        listaEquiposPersonas.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(listaEquiposPersonas);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 110, 180, 100));

        jLabel3.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel3.setText("Apellidos:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 90, 90, -1));

        jLabel14.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel14.setText("Miembro del equipo:");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 80, 190, -1));

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
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 230, 230, 50));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pngegg(1)(2).png"))); // NOI18N
        jLabel16.setText("PERSONA");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabel4.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel4.setText("Correo electronico:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 160, 170, -1));

        jLabel5.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel5.setText("Nombre:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 90, -1));

        jTextFieldCorreo.setEditable(false);
        getContentPane().add(jTextFieldCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 190, 210, -1));

        jTextFieldNombre.setEditable(false);
        getContentPane().add(jTextFieldNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 160, -1));

        jLabel6.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel6.setText("Teléfono:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 120, -1));

        jTextFieldTelefono.setEditable(false);
        getContentPane().add(jTextFieldTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 160, -1));

        jButton3.setBackground(new java.awt.Color(51, 51, 255));
        jButton3.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("MODIFICAR");
        jButton3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 230, -1, 50));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Personas p = new Personas();
        p.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        AyadirPersonas ap = new AyadirPersonas(this.jTextFieldNombre.getText(),this.jTextFieldApellidos.getText(),this.jTextFieldTelefono.getText(),this.jTextFieldCorreo.getText());
        ap.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DetallePersona().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextFieldApellidos;
    private javax.swing.JTextField jTextFieldCorreo;
    private javax.swing.JTextField jTextFieldNombre;
    private javax.swing.JTextField jTextFieldTelefono;
    private javax.swing.JList<String> listaEquiposPersonas;
    // End of variables declaration//GEN-END:variables
}
