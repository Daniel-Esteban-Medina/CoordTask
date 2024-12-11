package coordtask;
import static com.sun.tools.attach.VirtualMachine.list;
import componente.Conexion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import static java.nio.file.Files.list;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import static java.util.Collections.list;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private String codigoTemp = "";
    public Tareas() throws IOException {
        initComponents();
        this.setTitle("Tareas");
        this.setResizable(false);
        this.setIconImage(img);
        jComboBoxPrioridad.setRenderer(new DefaultListCellRenderer() {
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
                if(jComboBoxPrioridad.getSelectedIndex() == 0){
                    jComboBoxPrioridad.setBackground(new Color(240, 240, 240));
                } 
                if(jComboBoxPrioridad.getSelectedIndex() == 1){
                    jComboBoxPrioridad.setBackground(new Color(255, 121, 163));
                } 
                if(jComboBoxPrioridad.getSelectedIndex() == 2){
                    jComboBoxPrioridad.setBackground(new Color(255, 204, 153));
                } 
                if(jComboBoxPrioridad.getSelectedIndex() == 3){
                    jComboBoxPrioridad.setBackground(new Color(255,252,184));
                }
                return label;
            }
        });
        jComboBoxPrioridad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                   ejecutarFiltros();
            }
        });
        jComboBoxEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                   ejecutarFiltros();
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
        ){
            @Override
            public boolean isCellEditable(int row, int column) {
                // Desactiva la edición para todas las celdas
                return false;
            }
        };
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
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Detectar doble clic
                if (e.getClickCount() == 2) {
                    // Obtener la fila seleccionada
                    int filaSeleccionada = jTable1.getSelectedRow();
                    if (filaSeleccionada != -1) { // Asegurarse de que hay una fila seleccionada
                        // Obtener valores de la fila seleccionada
                        String prioridad = jTable1.getValueAt(filaSeleccionada, 0).toString();
                        String titulo = jTable1.getValueAt(filaSeleccionada, 1).toString();
                        String descripcion = jTable1.getValueAt(filaSeleccionada, 2).toString();
                        String estado = jTable1.getValueAt(filaSeleccionada, 3).toString();
                        String fechaInicio = jTable1.getValueAt(filaSeleccionada, 4).toString();
                        String fechaFin = jTable1.getValueAt(filaSeleccionada, 5).toString();
                        DetalleTarea dt = new DetalleTarea(titulo,descripcion, prioridad,convertirHtmlAFecha(fechaInicio),convertirHtmlAFecha(fechaFin), estado);
                        dt.setVisible(true);
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
    public static String convertirHtmlAFecha(String html) {
        // Expresión regular para extraer fecha y hora del HTML
        String regex = "(\\d{2})/(\\d{2})/(\\d{4})<br>(\\d{2}:\\d{2})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            //Extraer fecha y hora
            String dia = matcher.group(1);
            String mes = matcher.group(2);
            String anio = matcher.group(3);
            String hora = matcher.group(4);
            //formato "yyyy-MM-dd HH:mm"
            return anio + "-" + mes + "-" + dia + " " + hora;
        }
        throw new IllegalArgumentException("Formato de entrada no válido");
    }
    private void obtenerCodigo(String titulo, String descripcion, String prioridad, String estado, String fechaInicio, String fechaFin) throws SQLException{
        String tareaQuery = "SELECT Codigo FROM TAREAS WHERE Titulo = ? AND Descripcion = ? AND Prioridad = ? AND Estado = ?"+
                " AND Fecha_inicio = ? AND Fecha_vencimiento = ?";
            PreparedStatement ps = con.prepareStatement(tareaQuery);
            ps.setString(1, titulo);
            ps.setString(2, descripcion);
            ps.setString(3, prioridad);
            ps.setString(4, estado);
            ps.setString(5, fechaInicio);
            ps.setString(6, fechaFin);
            rs = ps.executeQuery();
            if (rs.next()) {
                codigoTemp = rs.getString("Codigo");
            }
    }
    public void limpiarTabla() {
        while (modeloTabla.getRowCount() > 0) {
            modeloTabla.removeRow(0);
        }
    }
    public void ejecutarFiltros(){
        String estado = jComboBoxEstado.getSelectedItem().toString();
        String prioridad = jComboBoxPrioridad.getSelectedItem().toString();
        if(estado != "Selecciona Opción" && prioridad != "Selecciona Opción"){
            limpiarTabla();
            String select = "SELECT Prioridad, Titulo, Descripcion, estado, Fecha_inicio, Fecha_vencimiento FROM Tareas ";
            String where = "WHERE Estado = '"+estado+"' AND Prioridad = '"+prioridad+"'";
            this.ejecutarConsulta(select+where);
        }
        if(estado != "Selecciona Opción" && prioridad == "Selecciona Opción"){
            limpiarTabla();
            String select = "SELECT Prioridad, Titulo, Descripcion, estado, Fecha_inicio, Fecha_vencimiento FROM Tareas ";
            String where = "WHERE Estado = '"+estado+"'";
            this.ejecutarConsulta(select+where);
        }
        if(estado == "Selecciona Opción" && prioridad != "Selecciona Opción"){
            limpiarTabla();
            String select = "SELECT Prioridad, Titulo, Descripcion, estado, Fecha_inicio, Fecha_vencimiento FROM Tareas ";
            String where = "WHERE Prioridad = '"+prioridad+"'";
            this.ejecutarConsulta(select+where);
        }
        if(estado == "Selecciona Opción" && prioridad == "Selecciona Opción"){
            limpiarTabla();
            this.ejecutarConsulta("SELECT Prioridad, Titulo, Descripcion, estado, Fecha_inicio, Fecha_vencimiento FROM Tareas");
        }
    }
    public void cerrarVentana(){
        this.dispose();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxPrioridad = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxEstado = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButtonEliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Funnel.png"))); // NOI18N
        jLabel1.setText("FILTROS");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, -1, -1));

        jComboBoxPrioridad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecciona Opción", "ALTA", "MEDIA", "BAJA" }));
        getContentPane().add(jComboBoxPrioridad, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, -1, -1));

        jLabel2.setText("Prioridad");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, -1, -1));

        jComboBoxEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecciona Opción", "PENDIENTE", "EN PROGRESO", "COMPLETADA" }));
        getContentPane().add(jComboBoxEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, -1, -1));

        jLabel3.setText("Estado");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 10, -1, -1));

        jLabel4.setBackground(new java.awt.Color(204, 204, 204));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("LISTA TAREAS");
        jLabel4.setOpaque(true);
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 65, 980, 30));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Add.png"))); // NOI18N
        jButton1.setText("Nueva tarea");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 120, 138, 51));

        jButtonEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Delete.png"))); // NOI18N
        jButtonEliminar.setText("Eliminar tarea");
        jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 200, -1, 51));

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

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        AyadirTarea at;
        try {
            at = new AyadirTarea();
            at.setVisible(true);
            this.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(Tareas.class.getName()).log(Level.SEVERE, null, ex);
        }       
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

    private void jMenu4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu4MouseClicked
        CalendarioVentana cv = new CalendarioVentana();
        cv.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenu4MouseClicked

    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MouseClicked
        VentanaCrearNoti ra = new VentanaCrearNoti();
        ra.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenu3MouseClicked

    private void jButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarActionPerformed
        int filaSeleccionada = jTable1.getSelectedRow();
                    if (filaSeleccionada != -1) { try {
                        // Asegurarse de que hay una fila seleccionada
                        // Obtener valores de la fila seleccionada
                        String prioridad = jTable1.getValueAt(filaSeleccionada, 0).toString();
                        String titulo = jTable1.getValueAt(filaSeleccionada, 1).toString();
                        String descripcion = jTable1.getValueAt(filaSeleccionada, 2).toString();
                        String estado = jTable1.getValueAt(filaSeleccionada, 3).toString();
                        String fechaInicio = jTable1.getValueAt(filaSeleccionada, 4).toString();
                        String fechaFin = jTable1.getValueAt(filaSeleccionada, 5).toString();
                        obtenerCodigo(titulo,descripcion,prioridad,estado,convertirHtmlAFecha(fechaInicio),convertirHtmlAFecha(fechaFin));
                        String sql = "DELETE FROM Tareas WHERE Codigo = ?";
                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.setString(1, codigoTemp);
                        ps.executeUpdate();
                        limpiarTabla();
                        this.ejecutarConsulta("SELECT Prioridad, Titulo, Descripcion, estado, Fecha_inicio, Fecha_vencimiento FROM Tareas");
            } catch (SQLException ex) {
                Logger.getLogger(Tareas.class.getName()).log(Level.SEVERE, null, ex);
            }
                    }
    }//GEN-LAST:event_jButtonEliminarActionPerformed
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonEliminar;
    private javax.swing.JComboBox<String> jComboBoxEstado;
    private javax.swing.JComboBox<String> jComboBoxPrioridad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}