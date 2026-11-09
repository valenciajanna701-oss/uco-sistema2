package co.edu.uco.panel.web;

import co.edu.uco.panel.dto.Dtos;
import co.edu.uco.panel.service.AcademicoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Materias", description = "Materias, profesor y estadísticas por curso")
public class MateriaController {

    private final AcademicoService service;

    public MateriaController(AcademicoService service) {
        this.service = service;
    }

    @GetMapping("/api/materias")
    public List<Dtos.MateriaConProfesorDto> listar(
            @RequestParam(name = "profesor_id", required = false) Long profesorId,
            @RequestParam(required = false) String buscar) {
        return service.listarMaterias(profesorId, buscar);
    }

    @GetMapping({"/api/materias/{id}/estadisticas", "/materias/{id}/estadisticas"})
    public Dtos.MateriaEstadisticasDto estadisticas(@PathVariable Long id) {
        return service.estadisticasMateria(id);
    }
}
