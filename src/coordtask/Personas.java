package coordtask;

import componente.Conexion;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class Personas extends javax.swing.JFrame {
    DefaultTableModel modeloTabla;
    //Variables bbdd
    private Conexion conect = new Conexion();
    private Connection con = conect.getConexion();
    private Statement st;
    private ResultSet rs;
    
    private String codigoTemp = "";
    public Personas() {
        initComponents();
        this.setResizable(false);
        // Deshabilitar el redimensionamiento de columnas
        jTable1.getTableHeader().setReorderingAllowed(false);

        // Ajustar la altura de las filas
        jTable1.setRowHeight(60);

        // Personalizar el renderizado del encabezado
        JTableHeader header = jTable1.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 30));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(new Color(0, 51, 102));
                label.setForeground(Color.WHITE);
                label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);
                return label;
            }
        });
        modeloTabla = new DefaultTableModel(
            null, // Datos (vacío inicialmente)
            new String[]{"NOMBRE", "APELLIDOS","TELÉFONO","CORREO"} // Nombres de columnas
        ){
            @Override
            public boolean isCellEditable(int row, int column) {
                // Desactiva la edición para todas las celdas
                return false;
            }
        }; 
        jTable1.setModel(modeloTabla);
        this.ejecutarConsulta("SELECT Nombre, Apellidos, Telefono, Correo_electronico FROM Personas");
        // Configurar tamaño de columnas
        TableColumn descriptionColumn = jTable1.getColumnModel().getColumn(3);
        descriptionColumn.setPreferredWidth(150);
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Detectar doble clic
                if (e.getClickCount() == 2) {
                    // Obtener la fila seleccionada
                    int filaSeleccionada = jTable1.getSelectedRow();
                    if (filaSeleccionada != -1) { 
                        // Obtener valores de la fila seleccionada
                        String nombre = jTable1.getValueAt(filaSeleccionada, 0).toString();
                        String apellidos = jTable1.getValueAt(filaSeleccionada, 1).toString();                     
                        String telefono = jTable1.getValueAt(filaSeleccionada, 2).toString();
                        String correo = jTable1.getValueAt(filaSeleccionada, 3).toString();
                        DetallePersona dp = new DetallePersona(nombre,apellidos,telefono,correo);
                        dp.setVisible(true);
                        cerrarVentana();
                    }
                }
            }
        });
    }
    // MÉTODOS MANEJO BBDD
    public void ejecutarConsulta(String sql) {
        try{
            st = con.createStatement();
            rs = st.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int cantColumnas = rsmd.getColumnCount();
            
            while(rs.next()){
                Object[] filas = new Object[cantColumnas];
                for(int i = 0; i < cantColumnas; i++){                    
                        filas[i] = rs.getObject(i+1);
                }
                modeloTabla.addRow(filas);
            }
        }catch(SQLException e){
            System.out.println(e.toString());
        }                                     
    }
    public void cerrarVentana(){
        this.dispose();
    }
    public void limpiarTabla() {
        while (modeloTabla.getRowCount() > 0) {
            modeloTabla.removeRow(0);
        }
    }
    private void obtenerCodigo(String nombre, String apellidos, String telefono, String correo) throws SQLException{
        String tareaQuery = "SELECT Codigo FROM Personas WHERE Nombre = ? AND Apellidos = ? AND TELEFONO = ? AND Correo_electronico = ?";
            PreparedStatement ps = con.prepareStatement(tareaQuery);
            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setString(3, telefono);
            ps.setString(4, correo);
            rs = ps.executeQuery();
            if (rs.next()) {
                codigoTemp = rs.getString("Codigo");
            }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Delete.png"))); // NOI18N
        jButton2.setText("Eliminar tarea");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("Para EDITAR una persona primero \ntienes que hacer doble clic en la\n lista sobre la persona que \nquieres editar.");
        jTextArea1.setEnabled(false);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel4.setBackground(new java.awt.Color(204, 204, 204));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("LISTA PERSONAS");
        jLabel4.setOpaque(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Add.png"))); // NOI18N
        jButton1.setText("Nueva tarea");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        jMenu1.setText("Tareas");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Equipos");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Notificaciones y alarmas");
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu3);

        jMenu4.setText("Calendario");
        jMenu4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu4MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        try {
            Tareas t = new Tareas();
            t.setVisible(true);
            this.dispose();
        } catch (IOException ex) {
            Logger.getLogger(Personas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenu1MouseClicked

    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MouseClicked
        VentanaCrearNoti ra = new VentanaCrearNoti();
        ra.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenu3MouseClicked

    private void jMenu4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu4MouseClicked
        CalendarioVentana cv = new CalendarioVentana();
        cv.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenu4MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        AyadirPersonas ap = new AyadirPersonas();
        ap.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int filaSeleccionada = jTable1.getSelectedRow();
            if (filaSeleccionada != -1) { try {
                String nombre = jTable1.getValueAt(filaSeleccionada, 0).toString();
                String apellidos = jTable1.getValueAt(filaSeleccionada, 1).toString();
                String telefono = jTable1.getValueAt(filaSeleccionada, 2).toString();
                String correo = jTable1.getValueAt(filaSeleccionada, 3).toString();
                obtenerCodigo(nombre,apellidos, telefono,correo);
                String sql = "DELETE FROM Personas WHERE Codigo = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, codigoTemp);
                ps.executeUpdate();
                limpiarTabla();
                this.ejecutarConsulta("SELECT Nombre, Apellidos, Telefono, Correo_electronico FROM Personas");
            } catch (SQLException ex) {
                Logger.getLogger(Tareas.class.getName()).log(Level.SEVERE, null, ex);
            }
                    }
    }//GEN-LAST:event_jButton2ActionPerformed
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Personas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
