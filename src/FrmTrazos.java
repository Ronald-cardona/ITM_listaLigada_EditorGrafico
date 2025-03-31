import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.PlainDocument;

public class FrmTrazos extends JFrame {
    private String[] tipoTrazo = new String[] { "Linea", "Rectangulo", "Ovalo" };

    private JButton btnAgregar;
    private JButton btnSeleccionar;
    private JButton btnEliminar;
    private JButton btnGuardar;
    private JButton btnCargar;

    private JToolBar tbTrazos;
    private JTable tblTrazos;

    private Lista listaTrazos = new Lista();
    private String nombreArchivo;

    JComboBox cmbTipoTrazo;
    JTextField txtInfo;
    JPanel pnlDibujo;
    BufferedImage dibujo; // variable para poder guardar la imagen
    Graphics2D g2d; // Objeto Graphics2D para dibujar en la imagen
    int x, y;

    boolean trazando = false;
    private boolean modoSeleccionActivo = false;
    private Nodo nodoSeleccionado = null;

    public FrmTrazos() {

        tbTrazos = new JToolBar();
        btnAgregar = new JButton();
        btnSeleccionar = new JButton();
        btnEliminar = new JButton();
        btnGuardar = new JButton();
        tblTrazos = new JTable();
        btnCargar = new JButton();

        setSize(500, 400);
        setTitle("Editor de gráficas");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JToolBar tbTrazos = new JToolBar();
        cmbTipoTrazo = new JComboBox();
        DefaultComboBoxModel dcm = new DefaultComboBoxModel(tipoTrazo);
        cmbTipoTrazo.setModel(dcm);
        tbTrazos.add(cmbTipoTrazo);

        btnSeleccionar.setIcon(new ImageIcon(getClass().getResource("/Iconos/seleccionar.png")));
        btnSeleccionar.setToolTipText("Seleccionar trazo");
        btnSeleccionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

            }
        });
        tbTrazos.add(btnSeleccionar);

        btnEliminar.setIcon(new ImageIcon(getClass().getResource("/Iconos/Eliminar.png")));
        btnEliminar.setToolTipText("Eliminar trazo");
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

            }
        });
        tbTrazos.add(btnEliminar);

        btnGuardar.setIcon(new ImageIcon(getClass().getResource("/Iconos/Guardar.png")));
        btnGuardar.setToolTipText("Guardar dibujo");
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnGuardarClick(evt);
            }
        });
        tbTrazos.add(btnGuardar);

        btnCargar.setIcon(new ImageIcon(getClass().getResource("/Iconos/cargar.png")));
        btnCargar.setToolTipText("Cargar dibujo");
        btnCargar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnCargarClick(evt);
            }
        });
        tbTrazos.add(btnCargar);

        pnlDibujo = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(dibujo, 0, 0, null); // Dibuja la imagen en el panel

            }

        };

        pnlDibujo.setBackground(Color.BLACK);

        getContentPane().add(tbTrazos, BorderLayout.NORTH);
        getContentPane().add(pnlDibujo, BorderLayout.CENTER);

        // Crear imagen en memoria para los dibujos
        dibujo = new BufferedImage(500, 400, BufferedImage.TYPE_INT_RGB);
        g2d = dibujo.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, 500, 400);
        g2d.setColor(Color.GREEN); // Color de dibujo

        // eventos
        // evento cuando haga CLIC con el raton
        pnlDibujo.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (modoSeleccionActivo) { // Solo selecciona si el modo está activo
                    nodoSeleccionado = listaTrazos.seleccionarTrazo(e.getX(), e.getY());

                    if (nodoSeleccionado != null) {
                        System.out.println("Nodo seleccionado correctamente: " + nodoSeleccionado);

                        // Redibujar con el nodo resaltado
                        listaTrazos.dibujarTodos(g2d, nodoSeleccionado);

                        pnlDibujo.repaint(); // Redibujar el panel

                        modoSeleccionActivo = false;

                    } else {
                        System.out.println("No se encontró un trazo en esa posición.");
                    }

                } else {
                    // Si el modo no está activo, solo dibuja
                    dibujar(e.getX(), e.getY());
                }
            }
        });

        nombreArchivo = System.getProperty("user.dir") + "/src/datos/datos.txt";
        listaTrazos.desdeArchivo(nombreArchivo); // Cargar datos al iniciar

        btnSeleccionar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                modoSeleccionActivo = true; // Activar modo de selección
                System.out.println("Modo selección activado. Haz clic en un trazo para seleccionarlo.");
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nodoSeleccionado != null) { // Solo verificar si hay un nodo seleccionado
                    System.out.println("Nodo a eliminar: " + nodoSeleccionado);

                    listaTrazos.eliminar(nodoSeleccionado); // Método para eliminar el nodo de la lista

                    System.out.println("Nodo eliminado correctamente y archivo actualizado");

                    // Limpiar la selección
                    nodoSeleccionado = null;

                    // Limpiar el lienzo antes de redibujar
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(0, 0, pnlDibujo.getWidth(), pnlDibujo.getHeight());
                    g2d.setColor(Color.GREEN); // Restaurar el color de dibujo

                    // Redibujar los trazos restantes
                    listaTrazos.dibujarTodos(g2d, null);

                    pnlDibujo.repaint(); // Refrescar la pantalla

                } else {
                    System.out.println("No hay trazo seleccionado para eliminar.");
                }
            }
        });
    }

    private void dibujar(int x, int y) {
        if (!trazando) {
            trazando = true;
            this.x = x;
            this.y = y;

        } else {
            trazando = false;
            Graphics g = pnlDibujo.getGraphics();
            g2d.setColor(Color.GREEN);
            int ancho = Math.abs(this.x - x);
            int alto = Math.abs(this.y - y);
            String linea = "";
            String rectangulo = "";
            String circulo = "";

            switch (cmbTipoTrazo.getSelectedIndex()) {
                case 0:
                    g2d.drawLine(this.x, this.y, x, y);
                    linea = "Linea";
                    break;
                case 1:
                    this.x = x < this.x ? x : this.x;
                    this.y = y < this.y ? y : this.y;
                    g2d.drawRect(this.x, this.y, ancho, alto);
                    rectangulo = "Rectangulo";
                    break;
                case 2:
                    this.x = x < this.x ? x : this.x;
                    this.y = y < this.y ? y : this.y;
                    g2d.drawOval(this.x, this.y, ancho, alto);
                    circulo = "Circulo";
                    break;
            }
            pnlDibujo.repaint(); // Redibujar el panel con la imagen actualizada

            Nodo nuevoNodo = new Nodo(linea, rectangulo, circulo, String.valueOf(this.x), String.valueOf(this.y),
                    String.valueOf(ancho), String.valueOf(alto));
            listaTrazos.agregar(nuevoNodo);

            // Guardar en el archivo
            listaTrazos.guardarArchivo(nombreArchivo);

            nombreArchivo = System.getProperty("user.dir") + "/src/datos/datos.txt";
            listaTrazos.desdeArchivo(nombreArchivo); // Cargar datos al iniciar

            listaTrazos.mostrarTrazosDesdeArchivo(); // Carga los datos en la lista
            listaTrazos.imprimirLista(); // Muestra los nodos en consola

        }

    }

    private void guardarDibujo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Dibujo");

        // guardar solo imágenes PNG
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imagen PNG", "png"));

        int seleccion = fileChooser.showSaveDialog(this);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            String ruta = archivo.getAbsolutePath();

            // Asegurar que el archivo tenga la extensión .png
            if (!ruta.toLowerCase().endsWith(".png")) {
                ruta += ".png";
            }

            // Crear la imagen a partir del JPanel
            BufferedImage dibujo = new BufferedImage(pnlDibujo.getWidth(), pnlDibujo.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = dibujo.getGraphics();
            pnlDibujo.printAll(g); // Captura lo que está dibujado en pnlDibujo

            try {
                ImageIO.write(dibujo, "png", new File(ruta));
                JOptionPane.showMessageDialog(this, "Imagen guardada en: " + ruta);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al guardar la imagen.");
            }
        }
    }

    private void btnGuardarClick(ActionEvent evt) {
        guardarDibujo();
    }

    private void cargarDibujo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona un Dibujo");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "png"));

        int seleccion = fileChooser.showOpenDialog(this);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            try {
                dibujo = ImageIO.read(archivoSeleccionado); // Cargar la imagen en el BufferedImage
                g2d = dibujo.createGraphics(); // Obtener el Graphics2D para dibujar sobre la imagen
                repaint(); // Volver a pintar el panel con la imagen cargada
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void btnCargarClick(ActionEvent evt) {
        cargarDibujo();
    }

}
