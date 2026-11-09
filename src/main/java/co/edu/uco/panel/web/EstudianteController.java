package co.edu.uco.panel.web;

import co.edu.uco.panel.dto.Dtos;
import co.edu.uco.panel.service.AcademicoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Estudiantes", description = "Búsqueda, filtros, paginación, carnet y boletín")
public class EstudianteController {

    private final AcademicoService service;

    public EstudianteController(AcademicoService service) {
        this.service = service;
    }

    @GetMapping({"/api/estudiantes", "/estudiantes"})
    public Dtos.PaginaEstudiantesDto listar(
            @RequestParam(required = false) Integer semestre,
            @RequestParam(required = false) String buscar,
            @RequestParam(name = "profesor_id", required = false) Long profesorId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(name = "per_page", defaultValue = "8") int perPage) {
        return service.paginaEstudiantes(semestre, buscar, profesorId, page, perPage);
    }

    @GetMapping({"/api/estudiantes/en-riesgo", "/estudiantes/en-riesgo"})
    public List<Dtos.EstudianteRiesgoDto> enRiesgo() {
        return service.estudiantesEnRiesgo();
    }

    @GetMapping({"/api/estudiantes/{id}/simulador", "/estudiantes/{id}/simulador"})
    public Dtos.SimuladorDto simulador(
            @PathVariable Long id,
            @RequestParam Long materia,
            @RequestParam(defaultValue = "3.0") double meta,
            @RequestParam(required = false) Double corte3) {
        return service.simulador(id, materia, meta, corte3);
    }

    @GetMapping("/api/estudiantes/{id}")
    public Dtos.EstudianteDetalleDto obtener(@PathVariable Long id) {
        return service.obtenerEstudiante(id);
    }

    @PostMapping("/api/estudiantes")
    @ResponseStatus(HttpStatus.CREATED)
    public Dtos.EstudianteDto crear(@Valid @RequestBody Dtos.EstudianteCreateDto datos) {
        return service.crearEstudiante(datos);
    }
}
