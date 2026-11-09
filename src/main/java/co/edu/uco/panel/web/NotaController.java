package co.edu.uco.panel.web;

import co.edu.uco.panel.dto.Dtos;
import co.edu.uco.panel.service.AcademicoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Notas y promedios", description = "Cortes, promedio, semáforo y simulador")
public class NotaController {

    private final AcademicoService service;

    public NotaController(AcademicoService service) {
        this.service = service;
    }

    @GetMapping("/api/promedios")
    public List<Dtos.NotaDto> promedios(
            @RequestParam(name = "estudiante_id", required = false) Long estudianteId,
            @RequestParam(name = "materia_id", required = false) Long materiaId,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String semaforo) {
        return service.listarPromedios(estudianteId, materiaId, color, semaforo);
    }

    @GetMapping("/api/notas")
    public List<Dtos.NotaDto> notas() {
        return service.listarPromedios(null, null, null, null);
    }

    @PostMapping("/api/notas")
    @ResponseStatus(HttpStatus.CREATED)
    public Dtos.NotaEntityDto crear(@Valid @RequestBody Dtos.NotaCreateDto datos) {
        return service.crearNota(datos);
    }
}
