
import java.awt.Graphics2D;

public class Nodo {

    String linea;
    String rectangulo;
    String circulo;
    String x;
    String y;
    String ancho;
    String alto;

    Nodo siguiente;

    public Nodo() {
        linea = "";
        rectangulo = "";
        circulo = "";
        x = "";
        y = "";
        ancho = "";
        alto = "";

        siguiente = null;
    }

    public Nodo(String linea, String rectangulo, String circulo, String x, String y, String ancho, String alto) {
        this.linea = linea;
        this.rectangulo = rectangulo;
        this.circulo = circulo;
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;

        siguiente = null;
    }

    public String getLinea() {
        return linea;
    }

    public String getCirculo() {
        return circulo;
    }

    public String getRectangulo() {
        return rectangulo;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public String getAncho() {
        return ancho;
    }

    public String getAlto() {
        return alto;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    // Setter para enlazar con el siguiente nodo
    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }

    @Override
    public String toString() {
        return "Nodo [" + linea + ", " + rectangulo
                + "," + circulo + ", x = " + x
                + ", y = " + y + ", ancho = " + ancho
                + ", alto = " + alto + "]";
    }

    public void actualizar(String linea, String rectangulo, String circulo, String x, String y, String ancho,
            String alto) {
        this.linea = linea;
        this.rectangulo = rectangulo;
        this.circulo = circulo;
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;

    }

    public void dibujar(Graphics2D g) {
        try {
            int x1 = Integer.parseInt(x);
            int y1 = Integer.parseInt(y);
            int anchoInt = Integer.parseInt(ancho);
            int altoInt = Integer.parseInt(alto);

            if (linea != null && linea.equalsIgnoreCase("Linea")) {
                g.drawLine(x1, y1, x1 + anchoInt, y1 + altoInt);
            } else if (rectangulo != null && rectangulo.equalsIgnoreCase("Rectangulo")) {
                g.drawRect(x1, y1, anchoInt, altoInt);
            } else if (circulo != null && circulo.equalsIgnoreCase("Circulo")) {
                g.drawOval(x1, y1, anchoInt, altoInt);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error al convertir coordenadas: " + e.getMessage());
        }
    }
}
