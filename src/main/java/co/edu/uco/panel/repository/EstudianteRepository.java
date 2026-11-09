package co.edu.uco.panel.repository;

import co.edu.uco.panel.model.Estudiante;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    List<Estudiante> findAllByOrderByApellidoAscNombreAsc();

    boolean existsByEmail(String email);
}
