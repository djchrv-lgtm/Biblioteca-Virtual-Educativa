import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;

    public class Biblioteca {

        private ArrayList<Libro> libros;
        private ArrayList<String> historial;

        private final String ARCHIVO_LIBROS = "libros.txt";
        private final String ARCHIVO_HISTORIAL = "historial_prestamos.txt";

        public Biblioteca() {
            libros = new ArrayList<>();
            historial = new ArrayList<>();

            cargarLibros();
            cargarHistorial();
        }

        public boolean registrarLibro(Libro libro) {

            if (buscarPorCodigo(libro.getCodigo()) != null) {
                return false;
            }

            libros.add(libro);
            guardarLibros();

            return true;
        }

        public ArrayList<Libro> getLibros() {
            return new ArrayList<>(libros);
        }

        public Libro buscarPorCodigo(int codigo) {

            for (Libro libro : libros) {
                if (libro.getCodigo() == codigo) {
                    return libro;
                }
            }

            return null;
        }

        public ArrayList<Libro> buscarLibros(String texto) {

            ArrayList<Libro> encontrados = new ArrayList<>();

            String busqueda = texto.toLowerCase().trim();

            for (Libro libro : libros) {

                if (libro.getTitulo().toLowerCase().contains(busqueda)
                        || libro.getAutor().toLowerCase().contains(busqueda)
                        || libro.getMateria().toLowerCase().contains(busqueda)
                        || String.valueOf(libro.getCodigo()).contains(busqueda)) {

                    encontrados.add(libro);
                }
            }

            return encontrados;
        }

        public boolean prestarLibro(int codigo) {

            Libro libro = buscarPorCodigo(codigo);

            if (libro != null && libro.prestar()) {

                registrarMovimiento("PRÉSTAMO", libro);
                guardarLibros();

                return true;
            }

            return false;
        }

        public boolean devolverLibro(int codigo) {

            Libro libro = buscarPorCodigo(codigo);

            if (libro != null && libro.devolver()) {

                registrarMovimiento("DEVOLUCIÓN", libro);
                guardarLibros();

                return true;
            }

            return false;
        }

        public int contarLibros() {
            return libros.size();
        }

        public int contarDisponibles() {

            int contador = 0;

            for (Libro libro : libros) {
                if (libro.isDisponible()) {
                    contador++;
                }
            }

            return contador;
        }

        public int contarPrestados() {
            return contarLibros() - contarDisponibles();
        }

        private void registrarMovimiento(String accion, Libro libro) {

            String fecha = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            );

            String movimiento = fecha + " | " + accion
                    + " | Código: " + libro.getCodigo()
                    + " | Libro: " + libro.getTitulo();

            historial.add(movimiento);

            guardarHistorial();
        }

        public ArrayList<String> getHistorial() {
            return new ArrayList<>(historial);
        }

        private String codificar(String texto) {
            return Base64.getEncoder().encodeToString(
                    texto.getBytes(StandardCharsets.UTF_8)
            );
        }

        private String decodificar(String texto) {
            return new String(
                    Base64.getDecoder().decode(texto),
                    StandardCharsets.UTF_8
            );
        }

        private void guardarLibros() {

            try (PrintWriter escritor = new PrintWriter(
                    new FileWriter(ARCHIVO_LIBROS, StandardCharsets.UTF_8))) {

                for (Libro libro : libros) {

                    escritor.println(
                            libro.getCodigo() + "|"
                                    + codificar(libro.getTitulo()) + "|"
                                    + codificar(libro.getAutor()) + "|"
                                    + codificar(libro.getMateria()) + "|"
                                    + libro.isDisponible()
                    );
                }

            } catch (IOException e) {
                System.out.println("Error al guardar los libros: " + e.getMessage());
            }
        }

        private void cargarLibros() {

            File archivo = new File(ARCHIVO_LIBROS);

            if (!archivo.exists()) {
                return;
            }

            try (BufferedReader lector = new BufferedReader(
                    new FileReader(archivo, StandardCharsets.UTF_8))) {

                String linea;

                while ((linea = lector.readLine()) != null) {

                    String[] datos = linea.split("\\|", -1);

                    if (datos.length == 5) {

                        try {
                            int codigo = Integer.parseInt(datos[0]);

                            if (buscarPorCodigo(codigo) == null) {

                                Libro libro = new Libro(
                                        decodificar(datos[1]),
                                        decodificar(datos[2]),
                                        codigo,
                                        decodificar(datos[3])
                                );

                                libro.setDisponible(Boolean.parseBoolean(datos[4]));

                                libros.add(libro);
                            }

                        } catch (IllegalArgumentException e) {
                            System.out.println("Registro de libro inválido.");
                        }
                    }
                }

            } catch (IOException e) {
                System.out.println("Error al cargar los libros: " + e.getMessage());
            }
        }

        private void guardarHistorial() {

            try (PrintWriter escritor = new PrintWriter(
                    new FileWriter(ARCHIVO_HISTORIAL, StandardCharsets.UTF_8))) {

                for (String movimiento : historial) {
                    escritor.println(movimiento);
                }

            } catch (IOException e) {
                System.out.println("Error al guardar el historial: " + e.getMessage());
            }
        }

        private void cargarHistorial() {

            File archivo = new File(ARCHIVO_HISTORIAL);

            if (!archivo.exists()) {
                return;
            }

            try (BufferedReader lector = new BufferedReader(
                    new FileReader(archivo, StandardCharsets.UTF_8))) {

                String linea;

                while ((linea = lector.readLine()) != null) {
                    historial.add(linea);
                }

            } catch (IOException e) {
                System.out.println("Error al cargar el historial: " + e.getMessage());
            }
        }
    }

