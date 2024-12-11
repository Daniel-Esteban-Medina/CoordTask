package coordtask;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class VentanaCrearNoti extends javax.swing.JFrame {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    public VentanaCrearNoti() {
        initComponents();
    }
    @SuppressWarnings("unchecked")
    public void crearNoti(String titulo, String mensaje, TrayIcon.MessageType tipo) {
    if (SystemTray.isSupported()) {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().getImage("icono.png");
        TrayIcon trayIcon = new TrayIcon(image, "Mi Aplicación");
        trayIcon.setImageAutoSize(true);
        try {
            tray.add(trayIcon);
            trayIcon.displayMessage(titulo, mensaje, tipo);
            Thread.sleep(10000);
            tray.remove(trayIcon);
        } catch (AWTException | InterruptedException e) {
            e.printStackTrace();
        }
    } else {
        System.out.println("La bandeja del sistema no es soportada en esta plataforma.");
    }
}
    private void programarNotificacion() {
    try {
        String fechaUsuario = fecha1.obtenerDateTime();
        String horaUsuario = horaMinutos1.getHorasMinutos();
        String fechaHoraStr = fechaUsuario + "T" + horaUsuario;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime fechaHoraProgramada = LocalDateTime.parse(fechaHoraStr, formatter);
        LocalDateTime ahora = LocalDateTime.now();
        Duration duracion = Duration.between(ahora, fechaHoraProgramada);
        if (duracion.isNegative()) {
            JOptionPane.showMessageDialog(this, "La fecha y hora ya han pasado. Seleccione una futura.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        long delay = duracion.toMillis();
        System.out.println("Notificación programada en " + delay + " ms.");
        String titulo = jTextFieldTitulo.getText();
        String mensaje = jTextAreaDescripcion.getText();
        String seleccionCombo = jComboBox1.getSelectedItem().toString().toUpperCase();
        TrayIcon.MessageType tipo;
        switch (seleccionCombo) {
            case "RECORDATORIO":
                tipo = TrayIcon.MessageType.INFO; 
                break;
            case "ADVERTENCIA":
                tipo = TrayIcon.MessageType.WARNING; 
                break;
            case "AVISO":
                tipo = TrayIcon.MessageType.ERROR; 
                break;
            default:
                tipo = TrayIcon.MessageType.NONE; 
                break;
        }
        scheduler.schedule(() -> crearNoti(titulo, mensaje, tipo), delay, TimeUnit.MILLISECONDS);
        JOptionPane.showMessageDialog(this, "Notificación programada para el " + fechaHoraProgramada, "Programada", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception e) {
        System.err.println("Error al programar la notificación: " + e.getMessage());
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Formato de fecha/hora inválido. Usa: yyyy-MM-dd y hh:mm", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextFieldTitulo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaDescripcion = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        horaMinutos1 = new componente.HoraMinutos();
        fecha1 = new componente.Fecha();
        jButtonCrear = new javax.swing.JButton();
        jButtonGetHora = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Formulario creación notificación"));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jTextFieldTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 61, 357, -1));

        jTextAreaDescripcion.setColumns(20);
        jTextAreaDescripcion.setRows(5);
        jScrollPane1.setViewportView(jTextAreaDescripcion);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 159, 357, 141));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("MENSAGE DE LA NOTIFICACIÓN:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 126, 249, 27));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("TÍTULO DE LA NOTIFICACIÓN:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 28, 213, 27));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecciona un tipo", "Recordatorio", "Advertencia", "Aviso" }));
        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 50, -1, -1));
        jPanel1.add(horaMinutos1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 160, -1, -1));
        jPanel1.add(fecha1, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 110, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        jButtonCrear.setText("OK");
        jButtonCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCrearActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonCrear, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 312, -1, -1));

        jButtonGetHora.setText("Ver Notificaciones");
        jButtonGetHora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGetHoraActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonGetHora, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 310, -1, -1));

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

        jMenu4.setText("Tareas");
        jMenu4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu4MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCrearActionPerformed
    String titulo = jTextFieldTitulo.getText();
    String mensaje = jTextAreaDescripcion.getText();
    String fecha = fecha1.obtenerDateTime(); 
    String hora = horaMinutos1.getHorasMinutos(); 
    TrayIcon.MessageType tipo = TrayIcon.MessageType.NONE;
    switch(jComboBox1.getSelectedItem().toString()){
        case "Recordatorio":
            tipo = TrayIcon.MessageType.INFO;
        break;
        case "Advertencia":
            tipo = TrayIcon.MessageType.WARNING;
        break;
        case "Aviso":
            tipo = TrayIcon.MessageType.ERROR;
        break;
    }
    guardarNotificacionEnArchivo(titulo, mensaje, fecha, hora,tipo);
    programarNotificacion();
    }//GEN-LAST:event_jButtonCrearActionPerformed

    private void jButtonGetHoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGetHoraActionPerformed
        imprimirNotificaciones();
    }//GEN-LAST:event_jButtonGetHoraActionPerformed

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

    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MouseClicked
        CalendarioVentana ra = new CalendarioVentana();
        ra.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenu3MouseClicked

    private void jMenu4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu4MouseClicked
        Tareas t;
        try {
            t = new Tareas();
            t.setVisible(true);
            this.dispose();
        } catch (IOException ex) {
            //Logger.getLogger(CalendarioVentana.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenu4MouseClicked
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VentanaCrearNoti ventana = new VentanaCrearNoti();
                ventana.setVisible(true);
                ventana.scheduler.scheduleAtFixedRate(() -> ventana.revisarNotificaciones(), 0, 1, TimeUnit.SECONDS);
            }
        });
    }
    public void guardarNotificacionEnArchivo(String titulo, String mensaje, String fecha, String hora, TrayIcon.MessageType tipo) {
    String fechaHora = fecha + "T" + hora; 
    String tipoMensaje = tipo.name(); 

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("notificaciones.txt", true))) {
        writer.write("Título: " + titulo);
        writer.newLine();
        writer.write("Mensaje: " + mensaje);
        writer.newLine();
        writer.write("Fecha y Hora: " + fechaHora);
        writer.newLine();
        writer.write("Tipo: " + tipoMensaje);
        writer.newLine();
        writer.write("--------------------------------------------------");
        writer.newLine();
        writer.flush(); 
        System.out.println("Notificación guardada en: " + new File("notificaciones.txt"));
    } catch (IOException e) {
        System.err.println("Error al guardar la notificación: " + e.getMessage());
        e.printStackTrace();
    }
}
public void revisarNotificaciones() {
    try (BufferedReader reader = new BufferedReader(new FileReader("notificaciones.txt"))) {
        String line;
        String titulo = null, mensaje = null, fechaHoraStr = null;
        TrayIcon.MessageType tipo = TrayIcon.MessageType.NONE;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Título: ") || line.startsWith("Titulo: ")) {
                titulo = line.replaceFirst("Título: |Titulo: ", "").trim();
            } else if (line.startsWith("Mensaje: ")) {
                mensaje = line.replace("Mensaje: ", "").trim();
            } else if (line.startsWith("Fecha y Hora: ") || line.startsWith("Fecha y Hora Programada: ")) {
                fechaHoraStr = line.replaceFirst("Fecha y Hora: |Fecha y Hora Programada: ", "").trim();
            } else if (line.startsWith("Tipo: ")) {
                String tipoStr = line.replace("Tipo: ", "").trim().toUpperCase();
                tipo = TrayIcon.MessageType.valueOf(tipoStr);
            }
            if (line.startsWith("--------------------------------------------------") && titulo != null && mensaje != null && fechaHoraStr != null) {
                LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                if (fechaHora.isBefore(LocalDateTime.now()) || fechaHora.isEqual(LocalDateTime.now())) {
                    crearNoti(titulo, mensaje, tipo); 
                }
                titulo = null;
                mensaje = null;
                fechaHoraStr = null;
                tipo = TrayIcon.MessageType.NONE;
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    borrarNotificacionesPasadas();
}
public void borrarNotificacionesPasadas() {
    File archivo = new File("notificaciones.txt");
    if (!archivo.exists() || archivo.length() == 0) {
        System.out.println("No hay notificaciones para borrar.");
        return;
    }
    List<String> lineas = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
        String line;
        while ((line = reader.readLine()) != null) {
            lineas.add(line);
        }
    } catch (IOException e) {
        System.err.println("Error al leer el archivo para borrar: " + e.getMessage());
        e.printStackTrace();
        return;
    }
    List<String> lineasGuardadas = new ArrayList<>();
    boolean isNotificationDateTime = false;
    LocalDateTime currentDateTime = LocalDateTime.now();
    for (int i = 0; i < lineas.size(); i++) {
        String line = lineas.get(i);

        if (line.startsWith("Fecha y Hora: ")) {
            isNotificationDateTime = true;
            String fechaHoraStr = line.replace("Fecha y Hora: ", "").trim();
            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            if (fechaHora.isBefore(currentDateTime)) {
                i += 3; // Saltamos la notificación
                continue;
            }
        }
        if (!isNotificationDateTime || !line.startsWith("Fecha y Hora: ")) {
            lineasGuardadas.add(line);
        }
        isNotificationDateTime = false;
    }
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, false))) {
        for (String line : lineasGuardadas) {
            writer.write(line);
            writer.newLine();
        }
        writer.flush(); // Forzar la escritura
        System.out.println("Notificaciones pasadas borradas.");
    } catch (IOException e) {
        System.err.println("Error al escribir el archivo actualizado: " + e.getMessage());
        e.printStackTrace();
    }
}
public void imprimirNotificaciones() {
    File archivo = new File("notificaciones.txt");
    System.out.println("Intentando leer desde: " + archivo.getAbsolutePath());
    if (!archivo.exists() || archivo.length() == 0) {
        System.out.println("El archivo está vacío o no existe.");
        return;
    }
    try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    } catch (IOException e) {
        System.err.println("Error al leer las notificaciones: " + e.getMessage());
        e.printStackTrace();
    }
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componente.Fecha fecha1;
    private componente.HoraMinutos horaMinutos1;
    private javax.swing.JButton jButtonCrear;
    private javax.swing.JButton jButtonGetHora;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaDescripcion;
    private javax.swing.JTextField jTextFieldTitulo;
    // End of variables declaration//GEN-END:variables
}
