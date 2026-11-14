
public class Libro {

    private String titulo;
    private String autor;
    private int codigo;
    private String materia;
    private boolean disponible;

    public Libro(String titulo, String autor, int codigo, String materia) {
        this.titulo = titulo;
        this.autor = autor;
        this.codigo = codigo;
        this.materia = materia;
        this.disponible = true;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getMateria() {
        return materia;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public boolean prestar() {
        if (disponible) {
            disponible = false;
            return true;
        }
        return false;
    }

    public boolean devolver() {
        if (!disponible) {
            disponible = true;
            return true;
        }
        return false;
    }

}
