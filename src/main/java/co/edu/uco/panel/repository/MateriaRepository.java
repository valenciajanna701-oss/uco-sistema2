package co.edu.uco.panel.repository;

import co.edu.uco.panel.model.Materia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateriaRepository extends JpaRepository<Materia, Long> {

    List<Materia> findAllByOrderByNombreAsc();

    List<Materia> findByProfesorIdOrderByNombreAsc(Long profesorId);
}
