package coordtask;
import static com.sun.tools.attach.VirtualMachine.list;
import componente.Conexion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import static java.nio.file.Files.list;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import static java.util.Collections.list;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
public class Tareas extends javax.swing.JFrame {
    //Variables bbdd
    private Conexion conect = new Conexion();
    private Connection con = conect.getConexion();
    private Statement st;
    private ResultSet rs;
    //Logo
    BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/imagenes/logoApp.png"));
    DefaultTableModel modeloTabla;    
    public Tareas() throws IOException {
        initComponents();
        this.setTitle("Tareas");
        this.setIconImage(img);
        jComboBox1.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight); 
                //Despegable
                if(index == 0){
                    label.setBackground(new Color(240, 240, 240));
                } 
                if(index == 1){
                    label.setBackground(new Color(255, 121, 163));
                } 
                if(index == 2){
                    label.setBackground(new Color(255, 204, 153));
                } 
                if(index == 3){
                    label.setBackground(new Color(255,252,184));
                }// caja  
                if(jComboBox1.getSelectedIndex() == 0){
                    jComboBox1.setBackground(new Color(240, 240, 240));
                } 
                if(jComboBox1.getSelectedIndex() == 1){
                    jComboBox1.setBackground(new Color(255, 121, 163));
                } 
                if(jComboBox1.getSelectedIndex() == 2){
                    jComboBox1.setBackground(new Color(255, 204, 153));
                } 
                if(jComboBox1.getSelectedIndex() == 3){
                    jComboBox1.setBackground(new Color(255,252,184));
                }
                return label;
            }
        });
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
            new String[]{"PRIORIDAD", "TÍTULO", "DESCRIPCIÓN", "ESTADO", "FECHA INICIO", "FECHA FIN"} // Nombres de columnas
        );
        jTable1.setModel(modeloTabla);
        this.ejecutarConsulta("SELECT Prioridad, Titulo, Descripcion, estado, Fecha_inicio, Fecha_vencimiento FROM Tareas");
        // Renderer personalizado para colorear la columna "Prioridad"
        jTable1.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                switch (value.toString()) {
                    case "ALTA":
                        setBackground(new Color(255,121,163));
                        break;
                    case "MEDIA":
                        setBackground(new Color(255, 204, 153));
                        break;
                    case "BAJA":
                        setBackground(new Color(255,252,184));
                        break;
                    default:
                        setBackground(Color.WHITE);
                        break;
                }
                setForeground(Color.BLACK);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(getFont().deriveFont(Font.BOLD));
                return this;
            }
        });

        // Renderer para la descripción
        jTable1.getColumnModel().getColumn(2).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JTextArea textArea = new JTextArea(value.toString());
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setOpaque(true);
                
                if (isSelected) {
                    textArea.setBackground(table.getSelectionBackground());
                    textArea.setForeground(table.getSelectionForeground());
                } else {
                    textArea.setBackground(table.getBackground());
                    textArea.setForeground(table.getForeground());
                }

                int textHeight = textArea.getPreferredSize().height;
                if (table.getRowHeight(row) < textHeight) {
                    table.setRowHeight(row, textHeight);
                }

                return textArea;
            }
        });

        // Renderer para fechas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        jTable1.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        jTable1.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

        // Configurar tamaño de columnas
        TableColumn descriptionColumn = jTable1.getColumnModel().getColumn(2);
        descriptionColumn.setPreferredWidth(150);
        TableColumn priColumn = jTable1.getColumnModel().getColumn(0);
        priColumn.setPreferredWidth(15);
        TableColumn esColumn = jTable1.getColumnModel().getColumn(3);
        esColumn.setPreferredWidth(15);
        TableColumn tituloColumn = jTable1.getColumnModel().getColumn(1);
        tituloColumn.setPreferredWidth(150);
        TableColumn fechColumn = jTable1.getColumnModel().getColumn(4);
        fechColumn.setPreferredWidth(25);
        TableColumn fechFinColumn = jTable1.getColumnModel().getColumn(5);
        fechFinColumn.setPreferredWidth(25);
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
                    if (i == 4 || i == 5) {
                        String fecha = getFecha((LocalDateTime) rs.getObject(i+1));
                        String hora = getHora((LocalDateTime) rs.getObject(i+1));
                        filas[i] = fechaHora(fecha, hora);
                    }else{   
                        filas[i] = rs.getObject(i+1);
                    }
                }
                modeloTabla.addRow(filas);
            }
        }catch(SQLException e){
            System.out.println(e.toString());
        }                                     
    }
    //Formato Tabla
    public String fechaHora(String fecha, String hora) {
        String fechaHora = "<html><div style='text-align: center;'>"
                + fecha + "<br>" + hora + "</div></html>";
        return fechaHora;
    }
    // Métodos para formatear fechas y horas
    public String getFecha(LocalDateTime dateTime){
        String fecha = dateTime.toLocalDate().toString(); // Obtiene la fecha como string "yyyy-MM-dd"
        String[] partesFecha = fecha.split("-");
        String ayo = partesFecha[0];
        String mes = partesFecha[1];
        String dia = partesFecha[2];
        return dia + "/" + mes + "/" + ayo;
    }

    public String getHora(LocalDateTime dateTime){
        String hora = dateTime.toLocalTime().toString(); // Obtiene la hora como string "HH:mm:ss"
        String[] partesHora = hora.split(":");
        String horas = partesHora[0];
        String minutos = partesHora[1];
        return horas + ":" + minutos;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        buscadorSql1 = new componente.BuscadorSql();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Funnel.png"))); // NOI18N
        jLabel1.setText("FILTROS");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecciona Opción", "ALTA", "MEDIA", "BAJA" }));
        getContentPane().add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, -1, -1));

        jLabel2.setText("Prioridad");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 10, -1, -1));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecciona Opción", "PENDIENTE", "EN PROGRESO", "COMPLETADA" }));
        getContentPane().add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 30, -1, -1));

        jLabel3.setText("Estado");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 10, -1, -1));

        jLabel4.setBackground(new java.awt.Color(204, 204, 204));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("LISTA TAREAS");
        jLabel4.setOpaque(true);
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 65, 820, 30));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Add.png"))); // NOI18N
        jButton1.setText("Nueva tarea");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 120, 138, 51));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Delete.png"))); // NOI18N
        jButton2.setText("Eliminar tarea");
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 200, -1, 51));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Revert.png"))); // NOI18N
        jButton3.setText("Reiniciar filtros & busqueda");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 260, -1, 49));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("Para EDITAR una tarea primero \ntienes que hacer doble clic en la\n lista sobre la tarea que \nquieres editar.");
        jTextArea1.setEnabled(false);
        jScrollPane1.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 330, 191, 104));

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
        jTable1.setAutoscrolls(false);
        jScrollPane2.setViewportView(jTable1);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 980, 330));
        getContentPane().add(buscadorSql1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 360, -1));

        jMenu1.setText("Equipos");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Personas");
        jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu2MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Notificaciones y alarmas");
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        AyadirTarea at = new AyadirTarea();
        at.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        Equipos e = new Equipos();
        e.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenu1MouseClicked

    private void jMenu2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu2MouseClicked
        Personas p = new Personas();
        p.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenu2MouseClicked
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Tareas().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(Tareas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componente.BuscadorSql buscadorSql1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}