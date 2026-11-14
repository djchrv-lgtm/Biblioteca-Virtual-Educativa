
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VentanaBiblioteca extends JFrame {

    private Biblioteca biblioteca;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel totalValor;
    private JLabel disponiblesValor;
    private JLabel prestadosValor;

    public VentanaBiblioteca() {

        biblioteca = new Biblioteca();

        setTitle("Biblioteca Virtual Educativa");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(240, 244, 249));

        crearInterfaz();
        actualizarTabla();

        setVisible(true);
    }

    private void crearInterfaz() {

        JLabel titulo = new JLabel(
                "BIBLIOTECA VIRTUAL EDUCATIVA",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 27));
        titulo.setForeground(new Color(255, 255, 255));
        titulo.setBackground(new Color(25, 45, 75));
        titulo.setOpaque(true);
        titulo.setBorder(BorderFactory.createEmptyBorder(25, 10, 25, 10));


        JPanel encabezado = new JPanel(new BorderLayout(10, 10));
        encabezado.setBackground(new Color(240, 244, 249));

        encabezado.add(titulo, BorderLayout.NORTH);

        JPanel tarjetas = new JPanel(new GridLayout(1, 3, 15, 10));
        tarjetas.setBackground(new Color(240, 244, 249));
        tarjetas.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        totalValor = new JLabel("0", SwingConstants.CENTER);
        disponiblesValor = new JLabel("0", SwingConstants.CENTER);
        prestadosValor = new JLabel("0", SwingConstants.CENTER);

        tarjetas.add(crearTarjeta("TOTAL DE LIBROS", totalValor, new Color(35, 65, 105)));
        tarjetas.add(crearTarjeta("DISPONIBLES", disponiblesValor, new Color(35, 125, 85)));
        tarjetas.add(crearTarjeta("PRESTADOS", prestadosValor, new Color(190, 125, 35)));

        encabezado.add(tarjetas, BorderLayout.CENTER);

        add(encabezado, BorderLayout.NORTH);


        String[] columnas = {
                "Código", "Título", "Autor", "Materia", "Disponibilidad"
        };

        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setSelectionBackground(new Color(190, 215, 245));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.setGridColor(new Color(220, 225, 230));

        tabla.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        tabla.getTableHeader().setBackground(
                new Color(35, 65, 105)
        );

        tabla.getTableHeader().setForeground(Color.WHITE);

        tabla.getTableHeader().setOpaque(true);

        JScrollPane desplazamiento = new JScrollPane(tabla);

        add(desplazamiento, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(2, 3, 10, 10));
        panelBotones.setBorder(
                BorderFactory.createEmptyBorder(10, 20, 20, 20)
        );

        JButton registrar = new JButton("Registrar libro");
        JButton catalogo = new JButton("Ver catálogo");
        JButton buscar = new JButton("Buscar libro");
        JButton prestar = new JButton("Prestar libro");
        JButton devolver = new JButton("Devolver libro");
        JButton estadisticas = new JButton("Estadísticas");
        JButton historial = new JButton("Historial");

        registrar.addActionListener(e -> registrarLibro());
        catalogo.addActionListener(e -> actualizarTabla());
        buscar.addActionListener(e -> buscarLibro());
        prestar.addActionListener(e -> prestarLibro());
        devolver.addActionListener(e -> devolverLibro());
        estadisticas.addActionListener(e -> mostrarEstadisticas());
        historial.addActionListener(e -> mostrarHistorial());

        panelBotones.add(registrar);
        panelBotones.add(catalogo);
        panelBotones.add(buscar);
        panelBotones.add(prestar);
        panelBotones.add(devolver);
        panelBotones.add(estadisticas);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(panelBotones, BorderLayout.CENTER);
        panelInferior.add(historial, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);
    }

    private void actualizarTabla() {

        modelo.setRowCount(0);

        for (Libro libro : biblioteca.getLibros()) {

            String disponibilidad = libro.isDisponible()
                    ? "Disponible"
                    : "Prestado";

            modelo.addRow(new Object[]{
                    libro.getCodigo(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getMateria(),
                    disponibilidad
            });
        }
        totalValor.setText(String.valueOf(biblioteca.contarLibros()));

        disponiblesValor.setText(
                String.valueOf(biblioteca.contarDisponibles())
        );

        prestadosValor.setText(
                String.valueOf(biblioteca.contarPrestados())
        );
    }

    private void registrarLibro() {

        JTextField titulo = new JTextField();
        JTextField autor = new JTextField();
        JTextField codigo = new JTextField();

        String[] materias = {
                "Matemáticas",
                "Ciencias Sociales",
                "Física",
                "Química",
                "Biología",
                "Informática",
                "Español",
                "Inglés",
                "Historia",
                "Geografía",
                "Otra"
        };

        JComboBox<String> materia = new JComboBox<>(materias);

        Object[] campos = {
                "Título del libro:", titulo,
                "Autor:", autor,
                "Código:", codigo,
                "Materia:", materia
        };

        int resultado = JOptionPane.showConfirmDialog(
                this,
                campos,
                "Registrar nuevo libro",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            String nombreTitulo = titulo.getText().trim();
            String nombreAutor = autor.getText().trim();
            int numeroCodigo = Integer.parseInt(codigo.getText().trim());
            String nombreMateria = (String) materia.getSelectedItem();

            if (nombreTitulo.isEmpty() || nombreAutor.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Debes completar el título y el autor."
                );
                return;
            }

            if (numeroCodigo <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "El código debe ser mayor que cero."
                );
                return;
            }

            Libro libro = new Libro(
                    nombreTitulo,
                    nombreAutor,
                    numeroCodigo,
                    nombreMateria
            );

            if (biblioteca.registrarLibro(libro)) {

                JOptionPane.showMessageDialog(
                        this,
                        "¡Libro registrado correctamente!"
                );

                actualizarTabla();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Ya existe un libro con ese código."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "El código debe ser un número entero."
            );
        }
    }

    private void buscarLibro() {

        String texto = JOptionPane.showInputDialog(
                this,
                "Escribe el título, autor, materia o código:"
        );

        if (texto == null || texto.trim().isEmpty()) {
            return;
        }

        ArrayList<Libro> encontrados = biblioteca.buscarLibros(texto);

        modelo.setRowCount(0);

        for (Libro libro : encontrados) {

            modelo.addRow(new Object[]{
                    libro.getCodigo(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getMateria(),
                    libro.isDisponible() ? "Disponible" : "Prestado"
            });
        }

        if (encontrados.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontraron libros."
            );
        }
    }

    private void prestarLibro() {

        Integer codigo = solicitarCodigo("Ingresa el código del libro que deseas prestar:");

        if (codigo == null) {
            return;
        }

        if (biblioteca.prestarLibro(codigo)) {

            JOptionPane.showMessageDialog(
                    this,
                    "El libro fue prestado correctamente."
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo realizar el préstamo. Verifica el código y la disponibilidad."
            );
        }

        actualizarTabla();
    }

    private void devolverLibro() {

        Integer codigo = solicitarCodigo("Ingresa el código del libro que deseas devolver:");

        if (codigo == null) {
            return;
        }

        if (biblioteca.devolverLibro(codigo)) {

            JOptionPane.showMessageDialog(
                    this,
                    "El libro fue devuelto correctamente.");

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo realizar la devolución. Verifica el código y el estado del libro.");
        }

        actualizarTabla();
    }

    private Integer solicitarCodigo(String mensaje) {

        String entrada = JOptionPane.showInputDialog(this, mensaje);

        if (entrada == null) {
            return null;
        }

        try {

            int codigo = Integer.parseInt(entrada.trim());

            if (codigo <= 0) {
                throw new NumberFormatException();
            }

            return codigo;

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes ingresar un código numérico válido.");

            return null;
        }
    }

    private void mostrarEstadisticas() {

        int total = biblioteca.contarLibros();
        int disponibles = biblioteca.contarDisponibles();
        int prestados = biblioteca.contarPrestados();

        String mensaje =
                "ESTADÍSTICAS DE LA BIBLIOTECA\n\n"
                        + "Total de libros: " + total + "\n"
                        + "Libros disponibles: " + disponibles + "\n"
                        + "Libros prestados: " + prestados;

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Estadísticas",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void mostrarHistorial() {

        ArrayList<String> movimientos = biblioteca.getHistorial();

        if (movimientos.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Todavía no hay movimientos registrados.");

            return;
        }

        JTextArea area = new JTextArea(15, 55);
        area.setEditable(false);

        for (String movimiento : movimientos) {
            area.append(movimiento + "\n");
        }

        JScrollPane desplazamiento = new JScrollPane(area);

        JOptionPane.showMessageDialog(
                this,
                desplazamiento,
                "Historial de préstamos y devoluciones",
                JOptionPane.INFORMATION_MESSAGE);
    }
    private void estilizarBoton(JButton boton) {

        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(35, 65, 105));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

    }

    private JPanel crearTarjeta(String titulo, JLabel valor, Color color) {

        JPanel tarjeta = new JPanel(new BorderLayout(5, 10));

        tarjeta.setBackground(color);
        tarjeta.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        );

        JLabel nombre = new JLabel(titulo, SwingConstants.CENTER);
        nombre.setFont(new Font("Arial", Font.BOLD, 14));
        nombre.setForeground(Color.WHITE);

        valor.setFont(new Font("Arial", Font.BOLD, 30));
        valor.setForeground(Color.WHITE);

        tarjeta.add(nombre, BorderLayout.NORTH);
        tarjeta.add(valor, BorderLayout.CENTER);

        return tarjeta;
    }

}
