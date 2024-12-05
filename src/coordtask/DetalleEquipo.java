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
public class DetalleEquipo extends javax.swing.JFrame {
    private Conexion conect = new Conexion();
    private Connection con = conect.getConexion();
    private Statement st;
    private ResultSet rs;
    private String codigo = "";
    private DefaultListModel<String> modeloLista = new DefaultListModel<String>();
    public DetalleEquipo(String nombre, String descripcion) {
        initComponents();      
        this.setTitle("MODIFICAR EQUIPO");
        this.setResizable(false);
        this.jTextFieldTitulo.setText(nombre);
        this.jTextAreaDescripcion.setText(descripcion);
        try{
            this.rellenarLista();
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    private DetalleEquipo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    private void rellenarLista() throws SQLException{
        obtenerCodigo(this.jTextFieldTitulo.getText(), this.jTextAreaDescripcion.getText());
        String equiposQuery = "SELECT P.Nombre, P.Apellidos FROM PERSONAS P JOIN PERSONAS_EQUIPOS PE ON P.Codigo = PE.Cod_persona WHERE PE.Cod_equipo = ?";
                PreparedStatement psEquipos = con.prepareStatement(equiposQuery);
                psEquipos.setString(1, codigo);
                rs = psEquipos.executeQuery();
                while (rs.next()) {
                    modeloLista.addElement((String) rs.getObject("Nombre")+" "+rs.getObject("Apellidos"));                  
                }
                listaEquiposPersonas.setModel(modeloLista);
    }
    private void obtenerCodigo(String nombre, String descripcion) throws SQLException{
        String tareaQuery = "SELECT Codigo FROM EQUIPOS WHERE Nombre = ? AND Descripcion = ?";
            PreparedStatement ps = con.prepareStatement(tareaQuery);
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            rs = ps.executeQuery();
            if (rs.next()) {
                codigo = rs.getString("Codigo");
            }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel12 = new javax.swing.JLabel();
        jTextFieldTitulo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaDescripcion = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        listaEquiposPersonas = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pngegg(1)(2).png"))); // NOI18N
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 10, 90, 80));

        jTextFieldTitulo.setEditable(false);
        getContentPane().add(jTextFieldTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 125, 261, -1));

        jTextAreaDescripcion.setEditable(false);
        jTextAreaDescripcion.setColumns(20);
        jTextAreaDescripcion.setRows(5);
        jScrollPane1.setViewportView(jTextAreaDescripcion);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 505, 97));

        listaEquiposPersonas.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(listaEquiposPersonas);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 120, 190, 170));

        jLabel3.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel3.setText("Nombre:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 94, 90, -1));

        jLabel14.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel14.setText("Miembros del equipo:");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 90, 190, -1));

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
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 300, 230, 50));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pngegg(3).png"))); // NOI18N
        jLabel16.setText("EQUIPO");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabel4.setFont(new java.awt.Font("Malgun Gothic", 1, 18)); // NOI18N
        jLabel4.setText("Descripción:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 120, -1));

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
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, -1, 50));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Equipos e = new Equipos();
        e.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            AyadirEquipo ae = new AyadirEquipo(jTextFieldTitulo.getText(),jTextAreaDescripcion.getText());
            ae.setVisible(true);
            this.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(DetalleEquipo.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }//GEN-LAST:event_jButton3ActionPerformed
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DetalleEquipo().setVisible(true);
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextAreaDescripcion;
    private javax.swing.JTextField jTextFieldTitulo;
    private javax.swing.JList<String> listaEquiposPersonas;
    // End of variables declaration//GEN-END:variables
}
