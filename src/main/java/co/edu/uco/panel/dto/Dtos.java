package co.edu.uco.panel.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public final class Dtos {

    private Dtos() {
    }

    public record MateriaDto(Long id, String nombre, String codigo, Integer creditos, Long profesorId) {
    }

    public record ProfesorDto(Long id, String nombre, String apellido, String email, String departamento,
                              List<MateriaDto> materias) {
    }

    public record EstudianteDto(Long id, String nombre, String apellido, String email, String programa,
                                Integer semestre) {
    }

    public record InsigniaDto(String nombre, String descripcion, String icono) {
    }

    public record NotaDto(Long notaId, Long estudianteId, Long materiaId, String estudiante, String programa,
                          String materia, String profesor, Double corte1, Double corte2, Double corte3,
                          Double promedio, String color, String semaforo, String estado) {
    }

    public record EstudianteResumenDto(Long id, String nombre, String apellido, String email, String programa,
                                       Integer semestre, Double promedioGeneral, String color, String semaforo,
                                       Integer materiasCount, List<InsigniaDto> insignias) {
    }

    public record EstudianteDetalleDto(Long id, String nombre, String apellido, String email, String programa,
                                       Integer semestre, Double promedioGeneral, String color, String semaforo,
                                       Integer materiasCount, List<InsigniaDto> insignias, List<NotaDto> notas) {
    }

    public record PaginaEstudiantesDto(long total, int page, int perPage, int pages,
                                       List<EstudianteResumenDto> items) {
    }

    public record MateriaConProfesorDto(Long id, String nombre, String codigo, Integer creditos, Long profesorId,
                                        String profesor) {
    }

    public record EstadisticasDto(int totalProfesores, int totalEstudiantes, int totalMaterias, int totalNotas,
                                  Double promedioGeneral, int aprobados, int reprobados,
                                  Double porcentajeAprobacion) {
    }

    public record DistribucionDto(String rango, int cantidad) {
    }

    public record EstudianteMateriaDto(Long estudianteId, String estudiante, Integer semestre, Double corte1,
                                       Double corte2, Double corte3, Double promedio, String color,
                                       String semaforo) {
    }

    public record MateriaEstadisticasDto(Long materiaId, String materia, String codigo, String profesor,
                                         int totalEstudiantes, Double promedio, Double notaMaxima,
                                         Double notaMinima, int aprobados, int reprobados,
                                         Double porcentajeAprobacion, List<DistribucionDto> distribucion,
                                         List<EstudianteMateriaDto> estudiantes) {
    }

    public record SimuladorDto(Long estudianteId, String estudiante, Long materiaId, String materia, Double meta,
                               Double corte1, Double corte2, Double corte3Actual, Double promedioActual,
                               Double notaNecesariaCorte3, Double promedioMaximo, boolean alcanzable,
                               Double corte3Simulado, Double promedioSimulado, String semaforoSimulado,
                               String mensaje) {
    }

    public record RankingDto(int posicion, Long estudianteId, String estudiante, String programa, Double promedio,
                             String color, String semaforo, List<InsigniaDto> insignias) {
    }

    public record EstudianteRiesgoDto(Long estudianteId, String estudiante, String programa, Integer semestre,
                                      Double promedio, String semaforo, List<String> materiasEnRiesgo,
                                      List<InsigniaDto> insignias) {
    }

    public record MateriaCargaDto(Long materiaId, String materia, String codigo, int estudiantes, Double promedio,
                                  Double notaMaxima, Double notaMinima, Double porcentajeAprobacion) {
    }

    public record CargaProfesorDto(Long profesorId, String profesor, String departamento, int totalMaterias,
                                   int totalEstudiantes, Double promedioCursos,
                                   List<MateriaCargaDto> materias) {
    }

    public record EstudianteCreateDto(
            @NotBlank String nombre,
            @NotBlank String apellido,
            @NotBlank @Email String email,
            @NotBlank String programa,
            @NotNull @Min(1) @Max(12) Integer semestre) {
    }

    public record NotaCreateDto(
            @NotNull Long estudianteId,
            @NotNull Long materiaId,
            @NotNull @DecimalMin("0.0") @DecimalMax("5.0") Double corte1,
            @NotNull @DecimalMin("0.0") @DecimalMax("5.0") Double corte2,
            @NotNull @DecimalMin("0.0") @DecimalMax("5.0") Double corte3) {
    }

    public record NotaEntityDto(Long id, Long estudianteId, Long materiaId, Double corte1, Double corte2,
                                Double corte3) {
    }

    public record MensajeDto(String mensaje) {
    }
}
