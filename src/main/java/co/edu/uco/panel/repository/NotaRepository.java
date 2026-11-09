package co.edu.uco.panel.repository;

import co.edu.uco.panel.model.Nota;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotaRepository extends JpaRepository<Nota, Long> {

    @Query("""
            select n from Nota n
            join fetch n.materia m
            join fetch m.profesor p
            where n.estudiante.id = :id
            order by m.nombre
            """)
    List<Nota> notasDeEstudiante(@Param("id") Long id);

    @Query("""
            select n from Nota n
            join fetch n.materia m
            join fetch m.profesor p
            where n.materia.id = :id
            order by n.estudiante.apellido, n.estudiante.nombre
            """)
    List<Nota> notasDeMateria(@Param("id") Long id);

    @Query("""
            select n from Nota n
            join fetch n.materia m
            join fetch m.profesor p
            where m.profesor.id = :id
            order by n.estudiante.apellido, n.estudiante.nombre
            """)
    List<Nota> notasDeProfesor(@Param("id") Long id);

    Optional<Nota> findByEstudianteIdAndMateriaId(Long estudianteId, Long materiaId);

    boolean existsByEstudianteIdAndMateriaId(Long estudianteId, Long materiaId);
}
