package co.edu.uco.panel.web;

import co.edu.uco.panel.dto.Dtos;
import co.edu.uco.panel.service.AcademicoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Analítica", description = "Ranking, estadísticas generales y estado del servicio")
public class AnaliticaController {

    private final AcademicoService service;

    public AnaliticaController(AcademicoService service) {
        this.service = service;
    }

    @GetMapping({"/api/ranking", "/ranking"})
    public List<Dtos.RankingDto> ranking() {
        return service.ranking();
    }

    @GetMapping("/api/estadisticas")
    public Dtos.EstadisticasDto estadisticas() {
        return service.estadisticas();
    }

    @GetMapping("/api/salud")
    public Dtos.MensajeDto salud() {
        return new Dtos.MensajeDto("API UCO funcionando correctamente");
    }
}
