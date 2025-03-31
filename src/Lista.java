import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import java.awt.Rectangle;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class Lista {

    private Nodo cabeza;

    public Lista() {
        cabeza = null;

    }

    public void agregar(Nodo n) {
        if (n != null) {
            if (cabeza == null) {
                cabeza = n;
            } else {
                // ubicar el último nodo
                Nodo apuntador = cabeza;
                while (apuntador.siguiente != null) {
                    apuntador = apuntador.siguiente;
                }
                // unir el nuevo nodo
                apuntador.siguiente = n;
            }
            n.siguiente = null;
        }
    }

    public void eliminar(Nodo n) {
        if (n != null && cabeza != null) {
            // buscar el nodo y su antecesor
            Nodo antecesor = null;
            Nodo apuntador = cabeza;
            while (apuntador != null && apuntador != n) {
                antecesor = apuntador;
                apuntador = apuntador.siguiente;
            }
            if (apuntador != null) {
                if (antecesor == null) {
                    cabeza = cabeza.siguiente;
                } else {
                    antecesor.siguiente = apuntador.siguiente;
                }
            }
        }

        guardarArchivo("src/datos/datos.txt");
    }

    public void desdeArchivo(String nombreArchivo) {
        cabeza = null;
        BufferedReader br = Archivo.abrirArchivo(nombreArchivo);
        if (br != null) {
            try {
                String linea = br.readLine();
                while (linea != null) {
                    String[] datos = linea.split(";");
                    if (datos.length >= 5) {
                        Nodo n = new Nodo(datos[0], datos[1], datos[2], datos[3], datos[4], datos[5], datos[6]);
                        agregar(n);
                    }
                    linea = br.readLine();
                }

            } catch (IOException ex) {

            } catch (Exception ex) {

            }
        }
    }

    public void guardarArchivo(String nombreArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            Nodo actual = cabeza;
            while (actual != null) {

                bw.write(actual.getLinea() + ";" + actual.getRectangulo() + ";" + actual.getCirculo() + ";"
                        + actual.getX() + ";" + actual.getY() + ";" + actual.getAncho() + ";"
                        + actual.getAlto());
                bw.newLine();
                actual = actual.getSiguiente();
            }
        } catch (IOException ex) {
            System.err.println("Error al guardar en el archivo: " + ex.getMessage());
        }
    }

    public void mostrarTrazosDesdeArchivo() {
        File file = new File("src/datos/datos.txt"); // Asegurar acceso al archivo más reciente

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            System.out.println("Leyendo trazos desde el archivo...");
            cabeza = null; // Reiniciar la lista antes de cargar nuevos datos
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty())
                    continue; // Ignorar líneas vacías
                String[] datos = linea.split(";");

                Nodo nuevoNodo = new Nodo(datos[0], datos[1], datos[2], datos[3], datos[4], datos[5], datos[6]);
                agregar(nuevoNodo); // Agregar a la lista

            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    public void imprimirLista() {
        Nodo actual = cabeza;
        while (actual != null) {
            System.out.println(actual);
            actual = actual.getSiguiente();
        }
    }

    public Nodo seleccionarTrazo(int x, int y) {

        if (cabeza != null) {
            Nodo apuntador = cabeza;
            while (apuntador != null) {
                int coordX = Integer.parseInt(apuntador.x);
                int coordY = Integer.parseInt(apuntador.y);
                int ancho = Integer.parseInt(apuntador.ancho);
                int alto = Integer.parseInt(apuntador.alto);

                if (coordX <= x && x <= coordX + ancho &&
                        coordY <= y && y <= coordY + alto) {
                    return apuntador;
                }
                apuntador = apuntador.siguiente;
            }

        }
        return null;
    }

    public void dibujarTodos(Graphics2D g, Nodo nodoSeleccionado) {
        Nodo actual = cabeza;

        while (actual != null) {
            if (actual == nodoSeleccionado) {
                g.setColor(Color.RED); // Color para el trazo seleccionado
            } else {
                g.setColor(Color.GREEN); // Color normal de los trazos
            }

            actual.dibujar(g);
            actual = actual.siguiente;
        }

    }

}
