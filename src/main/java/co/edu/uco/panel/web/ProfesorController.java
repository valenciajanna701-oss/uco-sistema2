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
@Tag(name = "Profesores", description = "Docentes, materias y carga académica")
public class ProfesorController {

    private final AcademicoService service;

    public ProfesorController(AcademicoService service) {
        this.service = service;
    }

    @GetMapping("/api/profesores")
    public List<Dtos.ProfesorDto> listar(
            @RequestParam(required = false) String buscar) {
        return service.listarProfesores(buscar);
    }

    @GetMapping("/api/profesores/{id}")
    public Dtos.ProfesorDto obtener(@PathVariable Long id) {
        return service.obtenerProfesor(id);
    }

    @GetMapping({"/api/profesores/{id}/carga", "/profesores/{id}/carga"})
    public Dtos.CargaProfesorDto carga(@PathVariable Long id) {
        return service.cargaProfesor(id);
    }
}
