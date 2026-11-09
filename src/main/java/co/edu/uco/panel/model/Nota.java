package co.edu.uco.panel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "notas",
        uniqueConstraints = @UniqueConstraint(columnNames = {"estudiante_id", "materia_id"})
)
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estudiante_id")
    private Estudiante estudiante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "materia_id")
    private Materia materia;

    @Column(nullable = false)
    private Double corte1;

    @Column(nullable = false)
    private Double corte2;

    @Column(nullable = false)
    private Double corte3;

    public Nota() {
    }

    public Nota(Estudiante estudiante, Materia materia, Double corte1, Double corte2, Double corte3) {
        this.estudiante = estudiante;
        this.materia = materia;
        this.corte1 = corte1;
        this.corte2 = corte2;
        this.corte3 = corte3;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Double getCorte1() {
        return corte1;
    }

    public void setCorte1(Double corte1) {
        this.corte1 = corte1;
    }

    public Double getCorte2() {
        return corte2;
    }

    public void setCorte2(Double corte2) {
        this.corte2 = corte2;
    }

    public Double getCorte3() {
        return corte3;
    }

    public void setCorte3(Double corte3) {
        this.corte3 = corte3;
    }
}
