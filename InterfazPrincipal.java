/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * InterfazPrincipal.java
 *
 * Created on 28/05/2011, 11:33:03 AM
 */

import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.io.*;

/**
 *
 * @author Jorge Morfinez Mojica
 */

public class InterfazPrincipal extends javax.swing.JFrame {

    private JFileChooser selectorArchivo;   // ventana de dialogo
    private File fArchivo;                  // archivo leido para el analisis
    private FileOutputStream file;          // para guardar el contenido en el archivo
    private DataOutputStream data;          // para guardar el contenido en el archivo
    private String noCambios;               // mantiene el texto tal cual antes de hacerle algun cambio
    private String texto;                   // texto que se guardara en el archivo
    private String archivoActual;           // nombre con todo y ruta del archivo abierto
    private String carpetaActual;           // ruta de la carpeta donde se ubica el archivo abierto

    /** Creates new form InterfazPrincipal */
    public InterfazPrincipal() {
        initComponents();
        this.setLocationRelativeTo(null);       // coloca el JFrame al centro de la pantalla
        selectorArchivo = new JFileChooser();   // crea un objeto para la ventana de dialogo
        selectorArchivo.addChoosableFileFilter(new MiFiltro()); // agrega filtro de archivos .rb
        noCambios = "";
        num = new JLabel("1");
        num.setOpaque(true);
        jTextAreaEditor.setEditable(true);
        jTextAreaEditor.setEditorKit(new NumberedEditorKit());
        jTextAreaEditor.setFont(new java.awt.Font("Tahoma", 0, 12));
         // se agrega un oyente para cuando se haga clic en la X
         // para cerrar la ventana
        
        //this.addWindowListener(new WindowAdapter(){
         //   public void windowClosing(WindowEvent e){
         //       cerrarAplicacion();
         //   }
       // });
    }

     /*
     * obtiene la extension  de un archivo
     */  
    public static String getExtension(File f) {
        String ext = "r";
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
    /*
     * obtiene la extension a partir de un String del nombre del archivo
     */  
    public static String getExtension(String s) {
        String ext = "r";
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
    /**
     * Cierra la ventana. Primero se confirma la accion; en caso de confirmarlo y de que
     * se hayan hecho cambios en el texto, se pregunta si se desean guardar los cambios.
     */
    public void cerrarAplicacion(){
        // se confirma la terminacion de la aplicacion
        int resp = JOptionPane.showConfirmDialog(this, "¿Desea salir de la aplicación?", ":: Cerrar",
                JOptionPane.YES_NO_OPTION);
        
        // si la respuesta es SI
        if(resp == 0){
            try {
                // en caso de que se hayan hechos cambios
                if(!noCambios.equals(jTextAreaEditor.getText())){
                    resp = JOptionPane.showConfirmDialog(this, "¿Guardar los cambios en el archivo?",
                            ":: Guardar cambios", JOptionPane.YES_NO_OPTION);
                    // si se confirma el guardado de los cambios y esta abierto un archivo...
                    if(resp == 0 && (fArchivo != null)){
                        guardar(jTextAreaEditor.getText());  // guarda el contenido de jTextAreaEditor
                    }else if(resp == 1){    // si se elige NO, no se guardan los cambios
                        System.exit(0); // termina la aplicacion
                    }else{      // en caso de elegir SI, y no se haya abierto un archivo, se guarda por primera vez
                        guardarComo();
                    }
                }
                
                if(file != null)
                    file.close();   // cierra el archivo abierto
                System.exit(0);     // termina la aplicacion
            } catch (IOException ex) {
                //Logger.getLogger(EntornoCompilacionPHP.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Error al cerrar el archivo.", ":: Error al cerrar", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void abrirArchivo(){
        try {
            String linea;               // cadena de la linea leida en el BufferedReader
            BufferedReader br = null;   // el BufferedReader servira para leer el archivo indicado

            selectorArchivo.showOpenDialog(this);           // muestra una ventana de Abrir

            fArchivo = selectorArchivo.getSelectedFile();   // se almacena el archivo seleccionado

            // si no se selecciona ningun archivo para abrir, se sale del metodo
            if(fArchivo == null){
                return;
            }

            archivoActual = fArchivo.toString();            // se guarda la ruta del archivo abierto
           
            // si la extensión del archivo es txt
            if(getExtension(fArchivo).equals("rb")){
                carpetaActual = selectorArchivo.getCurrentDirectory().toString();   // carpeta del archivo abierto

                // en caso de haber seleccionado y abierto un archivo
                if(fArchivo != null){
                    // FileReader lee el archivo fArchivo y se almacena en br
                    br = new BufferedReader(new FileReader(fArchivo));
                    jTextAreaEditor.setText("");     // borrar el contenido de jTextAreaEditor, que es donde se muestra
                                                // el texto del archivo abierto

                    // se lee linea a linea lo almacenado en br y se coloca en jTextAreaEditor
                    // para poder visualizarlo y modificarlo
                    while ((linea = br.readLine()) != null) {
                        jTextAreaEditor.setText(jTextAreaEditor.getText() + linea + "\n");
                    }

                    noCambios = jTextAreaEditor.getText();       // se almacena el texto inicial, sin cambios
                    this.setTitle("Compilador Ruby v1.0 " + archivoActual); // muestra en barra de titulo la ruta del archivo abierto
                   // etiketa.setText("Archivo | " + archivoActual);  //muestra en etiketa la ruta del archivo abierto

                    // se activan los items del menu para poder trabajar con el archivo abierto
                    jMenuItem3.setEnabled(true);
                    jMenuItem5.setEnabled(true);

                    //jTextAreaEditor.setBackground(Color.gray);   // al abrir el archivo, cambia el color de fondo de jTextAreaEditor
                    //jTextAreaEditor.setForeground(new Color(255, 255, 255)); // al abrir el archivo, cambia el color de texto de jTextAreaEditor
                    jTextAreaTokens.setText("");
                    jTextAreaError.setText("");
                }
            }else{
                JOptionPane.showMessageDialog(this, "Solo se permite abrir archivos .rb\n" +
                        "Elija un archivo .rb", ":: Error de extensión de archivo",
                        JOptionPane.ERROR_MESSAGE);
                abrirArchivo();
            }
        } catch (IOException ex) {
            //Logger.getLogger(EntornoCompilacionPHP.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "No se pudo abrir el archivo.\n" + ex, ":: Error al abrir",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NullPointerException ex){

        }
    }

     /**
     * Metodo para guardar el contenido de jTextAreaEditor en un nuevo archivo.
     * Se muestra la ventana de dialogo Guardar como... para elegir la ruta
     * en donde se desea almacenar el archivo.
     * En caso de que el archivo exista, se pide confirmacion de reemplazo.
     */
    public void guardarComo(){
        int resp;   // respuesta de si se eligio o no guardar el archivo
        int resp2;  // respuesta de si se reemplaza el archivo existente

        selectorArchivo.setDialogTitle("Guardar como...");
        resp = selectorArchivo.showSaveDialog(this);    // muestra ventana de  dialogo Guardar como...
        // si se hizo clic en el boton Guardar...
        if(resp == JFileChooser.APPROVE_OPTION){
            fArchivo = selectorArchivo.getSelectedFile();   // se asigna el archivo elegido

            // si se cancelo la ventana de dialogo...
            if(fArchivo == null){
                return;     // vuelve a la ventana principal y se sale del metodo
            }

            archivoActual = fArchivo.toString();    // nombre completo del archivo guardado

            // aseguramos que el archivo sea .rb
            System.out.println(archivoActual);
            if(!getExtension(archivoActual).equals("rb")){
                archivoActual += ".rb";
                fArchivo = new File(archivoActual);
            }
            System.out.println(archivoActual);
            carpetaActual = selectorArchivo.getCurrentDirectory().toString();   // carpeta donde se almacena
            texto = jTextAreaEditor.getText();   // variable k contiene el texto de jTextAreaEditor


            // si el archivo ya existe...
            if(fArchivo.exists()){
                // pregunta confirmacion de reemplazo
                resp2 = JOptionPane.showConfirmDialog(this, "¿Desea reemplazar el archivo existente?",
                        ":: Archivo existente", JOptionPane.YES_NO_OPTION);

                // si la respuesta es afirmativa...
                if(resp2 == JOptionPane.YES_OPTION){
                    // almacena en disco el texto (texto) en la ruta especificada (archivoActual)
                    crearArchivo(archivoActual, texto);
                }else{
                    guardarComo();  // abre de nuevo la ventana de dialogo Guardar como...
                }
            }
            // si el archivo no existe
            else{
                // almacena en disco el texto (texto) en la ruta especificada (archivoActual)
                crearArchivo(archivoActual, texto);
            }

            this.setTitle("Compilador Ruby v1.0 " + archivoActual); // muestra en la barra de titulo la ruta del archivo actual
           // etiketa.setText("Archivo | " + archivoActual);  // muestra en etiketa la ruta del archivo actual

            //jTextAreaEditor.setBackground(new Color(0, 102, 255));   // al abrir el archivo, cambia el color de fondo de jTextAreaEditor
            //jTextAreaEditor.setForeground(new Color(255, 255, 255)); // al abrir el archivo, cambia el color de texto de jTextAreaEditor
            jMenuItem3.setEnabled(true);
            jMenuItem5.setEnabled(true);
        }
    }

    /**
     * Metodo para guardar el texto en la misma ruta del archivo abierto.
     * De esta manera se actualiza el archivo en disco sin necesidad de
     * elegir la ruta, pues se toma la ruta del archivo abierto.
     * @param contenido - texto que se va almacenar en el archivo
     */
    public void guardar(String contenido){
        /* guardar en el archivo:
         *      archivoActual - ruta del archivo abierto
         *      conenido      - texto a guardar
        */
        crearArchivo(archivoActual, contenido);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextAreaEditor = new javax.swing.JEditorPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaTokens = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaError = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItem9 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Compilador de Ruby v.1.0");

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${iconImage}"), this, org.jdesktop.beansbinding.BeanProperty.create("iconImage"));
        bindingGroup.addBinding(binding);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editor: ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(255, 0, 0))); // NOI18N

        jTextAreaEditor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jScrollPane4.setViewportView(jTextAreaEditor);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tokens: ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(255, 0, 0))); // NOI18N

        jTextAreaTokens.setColumns(20);
        jTextAreaTokens.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextAreaTokens.setRows(5);
        jScrollPane2.setViewportView(jTextAreaTokens);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Errores: ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(255, 0, 0))); // NOI18N

        jTextAreaError.setColumns(20);
        jTextAreaError.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextAreaError.setRows(5);
        jScrollPane3.setViewportView(jTextAreaError);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/folder_home.png"))); // NOI18N
        jMenu1.setText("Inicio");
        jMenu1.setFont(new java.awt.Font("Verdana", 0, 14));

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kedit.png"))); // NOI18N
        jMenuItem1.setText("Nuevo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);
        jMenu1.add(jSeparator3);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/folder_blue.png"))); // NOI18N
        jMenuItem2.setText("Abrir");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/db_add.png"))); // NOI18N
        jMenuItem3.setText("Guardar");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/3floppy_unmount.png"))); // NOI18N
        jMenuItem4.setText("Guardar como...");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);
        jMenu1.add(jSeparator1);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/button_cancel.png"))); // NOI18N
        jMenuItem5.setText("Cerrar");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);
        jMenu1.add(jSeparator2);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem6.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/exit.png"))); // NOI18N
        jMenuItem6.setText("Salir");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuBar1.add(jMenu1);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/konsole.png"))); // NOI18N
        jMenu2.setText("Compilar");
        jMenu2.setFont(new java.awt.Font("Verdana", 0, 14));

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ruby-icon.png"))); // NOI18N
        jMenuItem7.setText("Compilar código");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuBar1.add(jMenu2);

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/advancedsettings.png"))); // NOI18N
        jMenu3.setText("Ayuda");
        jMenu3.setFont(new java.awt.Font("Verdana", 0, 14));

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/khelpcenter.png"))); // NOI18N
        jMenuItem8.setText("Ayuda");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem8);
        jMenu3.add(jSeparator4);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem9.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bookmark.png"))); // NOI18N
        jMenuItem9.setText("Acerca de...");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem9);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Cirra el aarchivo abierto. En caso de haberse realizado cambios,
     * se confirma el guardado de los cambios.
     */
    public void cerrarArchivo(){
        // si se hicieron cambios...
        if(!noCambios.equals(jTextAreaEditor.getText())){
            // se pide confirmacion de guardado de cambios
            int resp = JOptionPane.showConfirmDialog(this, "¿Desea guardar los cambios en el archivo?",
                    ":: Guardar cambios", JOptionPane.YES_NO_CANCEL_OPTION);

            // si la respuesta es positiva...
            if(resp == JOptionPane.YES_OPTION){
                guardar(jTextAreaEditor.getText());  // se guarda el texto de jTextAreaEditor
                limpiar(1); // se limpia el texto del analisis lexico y sintactico
            }
            // si la respuesta es negativa...
            else if(resp == JOptionPane.NO_OPTION){
                limpiar(1); // se limpia el texto del analisis lexico y sintactico, y no se guardan los cambios
            }else{
                return;
            }
        }

        fArchivo = null;    // se "borra" el contenido del archivo
        limpiar(1);         // se limpia el texto del analisis lexico y sintactico

        // se inicializan los mensajes a mostrar
        this.setTitle("Compilador Ruby v1.0 ");
       // etiketa.setText("Archivo | ");
        noCambios = "";

        // se deshabiltan los items empleados para el archivo
        jMenuItem3.setEnabled(false);
        jMenuItem5.setEnabled(false);
    }

    /**
     * Limpia el contenido de los JTextArea dependiendo del valor de opc
     * @param opc - se emplea para saber cuales JTextArea borrar su contenido
     *              1 para borrar los tres JTextArea
     *              2 para borrar solo los de los analisis lexico y sintactico
     */
    public void limpiar(int opc){
        switch(opc){
            case 1:
                jTextAreaEditor.setText("");
                jTextAreaTokens.setText("");
                jTextAreaError.setText("");
                break;
            case 2:
                jTextAreaTokens.setText("");
                jTextAreaError.setText("");
                break;
        }
    }

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // TODO add your handling code here:
                                            //ACERCA DE...
        String creditos;
        creditos = " Aplicación: Compilador del lenguaje Ruby™ Versión 1.0\n";
        creditos += " Fecha de creación: 28 de Mayo de 2011\n";
        creditos += " Copyright © Morfínez Mojica Jorge, Escamilla G. Vicente,\n";
        creditos += " Marcos Ramírez Jovany & García Almanza Irving Rafael.\n";
        creditos += " ® Todos los derechos reservados.\n\n";
        creditos += " Aplicación controlada y supervizada por la\n";
        creditos += " Profra. Lic. Martínez Moreno Martha. \n";
        creditos += " Materia: Programación de sistemas - 5H1A. \n";
        creditos += " Especialidad: Ing. en Sistemas Computacionales \n\n";
        creditos += "           Instituto Tecnológico de Veracruz\n";
        creditos += "       'Antorcha y Luz de Fuego Permanente'\n";
        
        javax.swing.JOptionPane.showMessageDialog(null, creditos, " Acerca de Compilador Ruby", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
                                            //NUEVO ARCHIVO:
        cerrarArchivo();    // cierra el archivo abierto
        jTextAreaEditor.setText("");
        jTextAreaError.setText("");
        jTextAreaTokens.setText("");
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
                                            //ABRIR:
        abrirArchivo();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
                                            //GUARDAR:
        guardar(jTextAreaEditor.getText());  // guarda el conenido de txtEditor en el archivo abierto
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
                                            //GUARDAR COMO...
        guardarComo();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
                                            //CERRAR:
        cerrarArchivo();    // cierra el archivo abierto
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
                                            //SALIR:
         cerrarAplicacion(); // metodo para cerrar la aplicacion
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
                                            //COMPILAR:
         try
         {
            // antes de compilar se guardan los cambios:
            if(fArchivo != null)
            {
                guardar(jTextAreaEditor.getText());  // actualiza el archivo
            }
            else
            {
                // si no se ha abierto un archivo, entonces se guarda por primera vez antes de compilarlo:
                guardarComo();
            }

            if(fArchivo == null)
                return;

            // se crean los archivos de los analisis sintactico y lexico en la carpeta del proyecto:
            ruby compilador = new ruby(new FileInputStream(archivoActual));   // crea el objeto para realizar el analisis
            compilador.analizarLexico(compilador);  // manda a llamar la ejecucion del analisis lexico
            compilador = new ruby(new FileInputStream(archivoActual));       // crea el objeto para realizar el analisis
            compilador.analizarSintactico(compilador);  // manda a llamar la ejecucion del analisis sintactico

            // se crean los archivos de los analisis sintactico y lexico en una carpeta especifica
            compilador = new ruby(new FileInputStream(archivoActual));   // crea el objeto para realizar el analisis
            compilador.analizarLexico(compilador, "Lexico.txt");  // manda a llamar la ejecucion del analisis lexico
            compilador = new ruby(new FileInputStream(archivoActual));       // crea el objeto para realizar el analisis
            compilador.analizarSintactico(compilador, "Sintactico.txt");  // manda a llamar la ejecucion del analisis sintactico

            // guarda en un BufferedReader el contenido del analisis sintactico para mostrarlo en txtSintactico
            BufferedReader br = new BufferedReader(new FileReader("Sintactico.txt"));
            String linea;   // almacena una linea del buffer
            /*String [] datos;
            int lin=0;
            datos = Archivo.leerCadenas(archivoActual);
            for(int i=0; i<datos.length; i++){ lin++;}
            linea = String.valueOf(lin);*/
            jTextAreaError.setText("");  // borra el contenido de txtSintactico

            // lee linea a linea el arhivo del analisis sintactico y muestra su contenido en txtSintactico
            while((linea = br.readLine()) != null){
                jTextAreaError.setText(jTextAreaError.getText() + linea + "\n");
            }

            // guarda en un BufferedReader el contenido del analisis lexico para mostrarlo en txtLexico
            br = new BufferedReader(new FileReader("Lexico.txt"));
            jTextAreaTokens.setText("");   // borra el contenido de txtToken

            // lee linea a linea el arhivo del analisis lexico y muestra su contenido en txtLexico
            while((linea = br.readLine()) != null){
                jTextAreaTokens.setText(jTextAreaTokens.getText() + linea + "\n");
            }

        } catch (IOException ex) {
            //Logger.getLogger(EntornoCompilacionPHP.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex, ":: No se encontró el archivo",
                    JOptionPane.ERROR_MESSAGE);
        } catch(NullPointerException ex){
            JOptionPane.showMessageDialog(this, ex, ":: No se encontró el archivo",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:
                                            //AYUDA:
        String instrucciones;
        instrucciones = "  Vaya al menú Inicio y de clic en la opción Nuevo si \n";
        instrucciones += " quiere comenzar a programar un código fuente de Ruby nuevo, \n";
        instrucciones += " posteriormente proceda a guardar el archivo, en el mismo menú \n";
        instrucciones += " Inicio vaya a la opción Guardar como... para guardar el nuevo \n";
        instrucciones += " archivo. \n";
        instrucciones += " En caso de que quiera abrir un archivo fuente existente en el \n";
        instrucciones += " mismo menú Inicio vaya a la opción Abrir y ubique el directorio \n";
        instrucciones += " de su archivo. \n";
        instrucciones += " Una ves que en el área del editor visualice el codigo fuente elegido \n";
        instrucciones += " o escrito, dirijase al menú Compilar, y de clic en la opción Compilar código \n";
        instrucciones += " En pocos segundos visualizará los tokens leídos de su código fuente y, \n";
        instrucciones += " en caso de tener errores, en la parte de Errores le mencionará los mismos. \n";
        instrucciones += " Nota: No puede compilar un archivo de código fuente Ruby sin haberlo \n";
        instrucciones += " previamente guardado.";
        instrucciones += " En el menú Inicio, haga clic en la opción Salir \n";
        instrucciones += " para salir de la aplicación, el sistema le preguntará si desea salir.\n";
        
        javax.swing.JOptionPane.showMessageDialog(null, instrucciones, " Instrucciones", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    /**
     * Guarda en disco el contenido del archivo
     * @param nombre - ruta completa del archivo que se va guardar
     * @param texto  - texto que se va guardar en el archivos
     */
    public void crearArchivo(String nombre, String texto){
        try {
            limpiar(2);     // limpia los JTextArea de los analisis lexico y sintactico
            file = new FileOutputStream(nombre);    // crea un archivo
            data = new DataOutputStream(file);

            data.writeBytes(texto);     // escribe el contenido en el archivo
            noCambios = jTextAreaEditor.getText();   // noCambios se borra
            carpetaActual = selectorArchivo.getCurrentDirectory().toString();   // carpeta del archivo creado
            file.close();   // cierra el archivo

        } catch (IOException ex) {
            //Logger.getLogger(EntornoCompilacionPHP.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "No se encontró el archivo " + nombre, ":: Error al abrir",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfazPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    public javax.swing.JMenuItem jMenuItem1;
    public javax.swing.JMenuItem jMenuItem2;
    public javax.swing.JMenuItem jMenuItem3;
    public javax.swing.JMenuItem jMenuItem4;
    public javax.swing.JMenuItem jMenuItem5;
    public javax.swing.JMenuItem jMenuItem6;
    public javax.swing.JMenuItem jMenuItem7;
    public javax.swing.JMenuItem jMenuItem8;
    public javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    public javax.swing.JEditorPane jTextAreaEditor;
    public javax.swing.JTextArea jTextAreaError;
    public javax.swing.JTextArea jTextAreaTokens;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
    public JLabel num;
}

class MiFiltro extends javax.swing.filechooser.FileFilter {
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }

        String filename = file.getName();
        return (filename.endsWith(".rb"));
    }

    public String getDescription() {
        return "Archivos fuente Ruby (*.rb)";
    }
}