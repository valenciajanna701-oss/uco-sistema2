package co.edu.uco.panel.service;

import co.edu.uco.panel.dto.Dtos;
import co.edu.uco.panel.model.Estudiante;
import co.edu.uco.panel.model.Materia;
import co.edu.uco.panel.model.Nota;
import co.edu.uco.panel.model.Profesor;
import co.edu.uco.panel.repository.EstudianteRepository;
import co.edu.uco.panel.repository.MateriaRepository;
import co.edu.uco.panel.repository.NotaRepository;
import co.edu.uco.panel.repository.ProfesorRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class AcademicoService {

    private final ProfesorRepository profesorRepository;
    private final MateriaRepository materiaRepository;
    private final EstudianteRepository estudianteRepository;
    private final NotaRepository notaRepository;

    public AcademicoService(ProfesorRepository profesorRepository,
                            MateriaRepository materiaRepository,
                            EstudianteRepository estudianteRepository,
                            NotaRepository notaRepository) {
        this.profesorRepository = profesorRepository;
        this.materiaRepository = materiaRepository;
        this.estudianteRepository = estudianteRepository;
        this.notaRepository = notaRepository;
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    private double promedio(Nota nota) {
        return redondear((nota.getCorte1() + nota.getCorte2() + nota.getCorte3()) / 3.0);
    }

    private String color(double valor) {
        return valor >= 3 ? "verde" : "rojo";
    }

    private String semaforo(double valor) {
        if (valor < 3) {
            return "rojo";
        }
        if (valor < 3.5) {
            return "amarillo";
        }
        return "verde";
    }

    private String nombre(Estudiante estudiante) {
        return estudiante.getNombre() + " " + estudiante.getApellido();
    }

    private String nombre(Profesor profesor) {
        return profesor.getNombre() + " " + profesor.getApellido();
    }

    private double promedio(List<Double> valores) {
        return valores.isEmpty() ? 0.0 : redondear(valores.stream().mapToDouble(Double::doubleValue).average().orElse(0));
    }

    private List<Dtos.InsigniaDto> insignias(List<Nota> notas) {
        if (notas.isEmpty()) {
            return List.of();
        }
        List<Dtos.InsigniaDto> resultado = new ArrayList<>();
        double general = promedio(notas.stream().map(this::promedio).toList());

        if (general > 4.5) {
            resultado.add(new Dtos.InsigniaDto("Excelencia", "Promedio general superior a 4.5", "star"));
        }
        boolean constante = notas.stream()
                .allMatch(n -> n.getCorte1() < n.getCorte2() && n.getCorte2() < n.getCorte3());
        if (constante) {
            resultado.add(new Dtos.InsigniaDto("Constante", "Sube de corte en corte en todas las materias", "trend"));
        } else if (notas.stream().anyMatch(n -> n.getCorte3() > n.getCorte1())) {
            resultado.add(new Dtos.InsigniaDto("Mejoría", "Subió su nota del primer al último corte", "up"));
        }
        if (notas.stream().allMatch(n -> promedio(n) >= 3)) {
            resultado.add(new Dtos.InsigniaDto("Aprobado", "Sin materias pendientes por recuperar", "check"));
        }
        if (general < 3) {
            resultado.add(new Dtos.InsigniaDto("En riesgo", "Promedio general por debajo de 3.0", "alert"));
        }
        return resultado;
    }

    private Dtos.MateriaDto materiaDto(Materia materia) {
        return new Dtos.MateriaDto(materia.getId(), materia.getNombre(), materia.getCodigo(),
                materia.getCreditos(), materia.getProfesor().getId());
    }

    private Dtos.NotaDto notaDto(Nota nota) {
        double valor = promedio(nota);
        return new Dtos.NotaDto(
                nota.getId(),
                nota.getEstudiante().getId(),
                nota.getMateria().getId(),
                nombre(nota.getEstudiante()),
                nota.getEstudiante().getPrograma(),
                nota.getMateria().getNombre(),
                nombre(nota.getMateria().getProfesor()),
                nota.getCorte1(),
                nota.getCorte2(),
                nota.getCorte3(),
                valor,
                color(valor),
                semaforo(valor),
                valor >= 3 ? "Aprobado" : "Reprobado");
    }

    private Dtos.ProfesorDto profesorDto(Profesor profesor) {
        List<Dtos.MateriaDto> materias = materiaRepository.findByProfesorIdOrderByNombreAsc(profesor.getId())
                .stream().map(this::materiaDto).toList();
        return new Dtos.ProfesorDto(profesor.getId(), profesor.getNombre(), profesor.getApellido(),
                profesor.getEmail(), profesor.getDepartamento(), materias);
    }

    public Dtos.EstudianteResumenDto resumen(Estudiante estudiante) {
        List<Nota> notas = notaRepository.notasDeEstudiante(estudiante.getId());
        Double general = notas.isEmpty() ? null
                : promedio(notas.stream().map(this::promedio).toList());
        return new Dtos.EstudianteResumenDto(
                estudiante.getId(),
                estudiante.getNombre(),
                estudiante.getApellido(),
                estudiante.getEmail(),
                estudiante.getPrograma(),
                estudiante.getSemestre(),
                general,
                general == null ? null : color(general),
                general == null ? null : semaforo(general),
                notas.size(),
                insignias(notas));
    }

    /* ------------------------------ Profesores ------------------------------ */
    public List<Dtos.ProfesorDto> listarProfesores(String buscar) {
        String filtro = buscar == null ? "" : buscar.trim().toLowerCase();
        return profesorRepository.findAll(Sort.by("apellido").and(Sort.by("nombre"))).stream()
                .filter(p -> filtro.isEmpty()
                        || (p.getNombre() + " " + p.getApellido() + " " + p.getDepartamento())
                        .toLowerCase().contains(filtro))
                .map(this::profesorDto)
                .toList();
    }

    public Dtos.ProfesorDto obtenerProfesor(Long id) {
        Profesor profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado"));
        return profesorDto(profesor);
    }

    public Dtos.CargaProfesorDto cargaProfesor(Long id) {
        Profesor profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado"));
        List<Materia> materias = materiaRepository.findByProfesorIdOrderByNombreAsc(id);
        List<Nota> filas = notaRepository.notasDeProfesor(id);

        Map<Long, List<Double>> porMateria = new HashMap<>();
        for (Nota nota : filas) {
            porMateria.computeIfAbsent(nota.getMateria().getId(), clave -> new ArrayList<>())
                    .add(promedio(nota));
        }

        List<Dtos.MateriaCargaDto> detalle = new ArrayList<>();
        for (Materia materia : materias) {
            List<Double> valores = porMateria.getOrDefault(materia.getId(), List.of());
            long aprobados = valores.stream().filter(valor -> valor >= 3).count();
            detalle.add(new Dtos.MateriaCargaDto(
                    materia.getId(),
                    materia.getNombre(),
                    materia.getCodigo(),
                    valores.size(),
                    promedio(valores),
                    valores.isEmpty() ? 0.0 : redondear(valores.stream().mapToDouble(Double::doubleValue).max().orElse(0)),
                    valores.isEmpty() ? 0.0 : redondear(valores.stream().mapToDouble(Double::doubleValue).min().orElse(0)),
                    valores.isEmpty() ? 0.0 : redondear(aprobados * 100.0 / valores.size())));
        }

        Set<Long> estudiantes = new HashSet<>();
        filas.forEach(nota -> estudiantes.add(nota.getEstudiante().getId()));
        double promedioCursos = promedio(filas.stream().map(this::promedio).toList());

        return new Dtos.CargaProfesorDto(profesor.getId(), nombre(profesor), profesor.getDepartamento(),
                materias.size(), estudiantes.size(), promedioCursos, detalle);
    }

    /* ------------------------------- Materias ------------------------------- */
    public List<Dtos.MateriaConProfesorDto> listarMaterias(Long profesorId, String buscar) {
        String filtro = buscar == null ? "" : buscar.trim().toLowerCase();
        List<Materia> base = profesorId == null
                ? materiaRepository.findAllByOrderByNombreAsc()
                : materiaRepository.findByProfesorIdOrderByNombreAsc(profesorId);
        return base.stream()
                .filter(m -> filtro.isEmpty()
                        || (m.getNombre() + " " + m.getCodigo()).toLowerCase().contains(filtro))
                .map(m -> new Dtos.MateriaConProfesorDto(m.getId(), m.getNombre(), m.getCodigo(),
                        m.getCreditos(), m.getProfesor().getId(), nombre(m.getProfesor())))
                .toList();
    }

    public Dtos.MateriaEstadisticasDto estadisticasMateria(Long id) {
        Materia materia = materiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Materia no encontrada"));
        List<Nota> filas = notaRepository.notasDeMateria(id);
        List<Double> valores = filas.stream().map(this::promedio).toList();
        long aprobados = valores.stream().filter(valor -> valor >= 3).count();

        String[] etiquetas = {"0.0 - 0.9", "1.0 - 1.9", "2.0 - 2.9", "3.0 - 3.9", "4.0 - 5.0"};
        List<Dtos.DistribucionDto> distribucion = new ArrayList<>();
        for (int i = 0; i < etiquetas.length; i++) {
            final int indice = i;
            long cantidad = valores.stream()
                    .filter(valor -> valor >= indice && (indice == 4 ? valor <= 5.0 : valor < indice + 1))
                    .count();
            distribucion.add(new Dtos.DistribucionDto(etiquetas[i], (int) cantidad));
        }

        List<Dtos.EstudianteMateriaDto> estudiantes = filas.stream().map(nota -> {
            double valor = promedio(nota);
            return new Dtos.EstudianteMateriaDto(
                    nota.getEstudiante().getId(),
                    nombre(nota.getEstudiante()),
                    nota.getEstudiante().getSemestre(),
                    nota.getCorte1(),
                    nota.getCorte2(),
                    nota.getCorte3(),
                    valor,
                    color(valor),
                    semaforo(valor));
        }).toList();

        return new Dtos.MateriaEstadisticasDto(
                materia.getId(),
                materia.getNombre(),
                materia.getCodigo(),
                nombre(materia.getProfesor()),
                filas.size(),
                promedio(valores),
                valores.isEmpty() ? 0.0 : redondear(valores.stream().mapToDouble(Double::doubleValue).max().orElse(0)),
                valores.isEmpty() ? 0.0 : redondear(valores.stream().mapToDouble(Double::doubleValue).min().orElse(0)),
                (int) aprobados,
                valores.size() - (int) aprobados,
                valores.isEmpty() ? 0.0 : redondear(aprobados * 100.0 / valores.size()),
                distribucion,
                estudiantes);
    }

    /* ------------------------------ Estudiantes ------------------------------ */
    public Dtos.PaginaEstudiantesDto paginaEstudiantes(Integer semestre, String buscar, Long profesorId,
                                                       int page, int perPage) {
        String filtro = buscar == null ? "" : buscar.trim().toLowerCase();
        Set<Long> idsProfesor = null;
        if (profesorId != null) {
            idsProfesor = new HashSet<>();
            for (Nota nota : notaRepository.notasDeProfesor(profesorId)) {
                idsProfesor.add(nota.getEstudiante().getId());
            }
        }
        final Set<Long> idsFiltro = idsProfesor;

        List<Estudiante> filtrados = estudianteRepository.findAllByOrderByApellidoAscNombreAsc().stream()
                .filter(e -> semestre == null || semestre.equals(e.getSemestre()))
                .filter(e -> filtro.isEmpty()
                        || (e.getNombre() + " " + e.getApellido() + " " + e.getPrograma() + " " + e.getEmail())
                        .toLowerCase().contains(filtro))
                .filter(e -> idsFiltro == null || idsFiltro.contains(e.getId()))
                .toList();

        int total = filtrados.size();
        int pages = total == 0 ? 0 : (int) Math.ceil((double) total / perPage);
        int pagina = Math.max(page, 1);
        int desde = Math.min((pagina - 1) * perPage, total);
        int hasta = Math.min(desde + perPage, total);
        List<Dtos.EstudianteResumenDto> items = filtrados.subList(desde, hasta).stream()
                .map(this::resumen).toList();

        return new Dtos.PaginaEstudiantesDto(total, pagina, perPage, pages, items);
    }

    public List<Dtos.EstudianteRiesgoDto> estudiantesEnRiesgo() {
        List<Dtos.EstudianteRiesgoDto> resultado = new ArrayList<>();
        for (Estudiante estudiante : estudianteRepository.findAllByOrderByApellidoAscNombreAsc()) {
            List<Nota> notas = notaRepository.notasDeEstudiante(estudiante.getId());
            if (notas.isEmpty()) {
                continue;
            }
            double general = promedio(notas.stream().map(this::promedio).toList());
            if (general >= 3) {
                continue;
            }
            List<String> materias = notas.stream()
                    .filter(nota -> promedio(nota) < 3)
                    .map(nota -> nota.getMateria().getNombre())
                    .toList();
            resultado.add(new Dtos.EstudianteRiesgoDto(
                    estudiante.getId(),
                    nombre(estudiante),
                    estudiante.getPrograma(),
                    estudiante.getSemestre(),
                    general,
                    semaforo(general),
                    materias,
                    insignias(notas)));
        }
        resultado.sort(Comparator.comparing(Dtos.EstudianteRiesgoDto::promedio));
        return resultado;
    }

    public List<Dtos.RankingDto> ranking() {
        List<Dtos.EstudianteResumenDto> resumenes = new ArrayList<>();
        for (Estudiante estudiante : estudianteRepository.findAll()) {
            Dtos.EstudianteResumenDto resumen = resumen(estudiante);
            if (resumen.promedioGeneral() != null) {
                resumenes.add(resumen);
            }
        }
        resumenes.sort(Comparator.comparing(Dtos.EstudianteResumenDto::promedioGeneral).reversed());

        List<Dtos.RankingDto> resultado = new ArrayList<>();
        int posicion = 1;
        for (Dtos.EstudianteResumenDto resumen : resumenes) {
            if (resultado.size() >= 10) {
                break;
            }
            resultado.add(new Dtos.RankingDto(
                    posicion++,
                    resumen.id(),
                    resumen.nombre() + " " + resumen.apellido(),
                    resumen.programa(),
                    resumen.promedioGeneral(),
                    resumen.color(),
                    resumen.semaforo(),
                    resumen.insignias()));
        }
        return resultado;
    }

    public Dtos.EstudianteDetalleDto obtenerEstudiante(Long id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado"));
        List<Nota> notas = notaRepository.notasDeEstudiante(id);
        Double general = notas.isEmpty() ? null : promedio(notas.stream().map(this::promedio).toList());
        return new Dtos.EstudianteDetalleDto(
                estudiante.getId(),
                estudiante.getNombre(),
                estudiante.getApellido(),
                estudiante.getEmail(),
                estudiante.getPrograma(),
                estudiante.getSemestre(),
                general,
                general == null ? null : color(general),
                general == null ? null : semaforo(general),
                notas.size(),
                insignias(notas),
                notas.stream().map(this::notaDto).toList());
    }

    @Transactional
    public Dtos.EstudianteDto crearEstudiante(Dtos.EstudianteCreateDto datos) {
        if (estudianteRepository.existsByEmail(datos.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un estudiante con ese correo");
        }
        Estudiante guardado = estudianteRepository.save(new Estudiante(
                datos.nombre(), datos.apellido(), datos.email(), datos.programa(), datos.semestre()));
        return new Dtos.EstudianteDto(guardado.getId(), guardado.getNombre(), guardado.getApellido(),
                guardado.getEmail(), guardado.getPrograma(), guardado.getSemestre());
    }

    public Dtos.SimuladorDto simulador(Long estudianteId, Long materiaId, double meta, Double corte3) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado"));
        Materia materia = materiaRepository.findById(materiaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Materia no encontrada"));
        Nota nota = notaRepository.findByEstudianteIdAndMateriaId(estudianteId, materiaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "El estudiante no tiene notas en esa materia"));

        double necesario = redondear(meta * 3 - nota.getCorte1() - nota.getCorte2());
        double notaNecesaria = Math.max(0, necesario);
        boolean alcanzable = necesario <= 5;
        double promedioActual = promedio(nota);
        double promedioMaximo = redondear((nota.getCorte1() + nota.getCorte2() + 5) / 3.0);

        String mensaje;
        if (necesario <= 0) {
            mensaje = "La meta ya está asegurada con los cortes anteriores.";
        } else if (alcanzable) {
            mensaje = String.format(
                    "Necesita al menos %.2f en el último corte para alcanzar un promedio de %.2f.",
                    notaNecesaria, meta);
        } else {
            mensaje = String.format(
                    "Con los cortes actuales no es posible alcanzar %.2f; el máximo promedio posible es %.2f.",
                    meta, promedioMaximo);
        }

        Double corteSimulado = null;
        Double promedioSimulado = null;
        String semaforoSimulado = null;
        if (corte3 != null) {
            corteSimulado = corte3;
            promedioSimulado = redondear((nota.getCorte1() + nota.getCorte2() + corte3) / 3.0);
            semaforoSimulado = semaforo(promedioSimulado);
        }

        return new Dtos.SimuladorDto(
                estudiante.getId(),
                nombre(estudiante),
                materia.getId(),
                materia.getNombre(),
                meta,
                nota.getCorte1(),
                nota.getCorte2(),
                nota.getCorte3(),
                promedioActual,
                redondear(notaNecesaria),
                promedioMaximo,
                alcanzable,
                corteSimulado,
                promedioSimulado,
                semaforoSimulado,
                mensaje);
    }

    /* -------------------------- Notas y promedios -------------------------- */
    public List<Dtos.NotaDto> listarPromedios(Long estudianteId, Long materiaId, String color, String semaforo) {
        List<Nota> filas;
        if (estudianteId != null) {
            filas = notaRepository.notasDeEstudiante(estudianteId);
        } else if (materiaId != null) {
            filas = notaRepository.notasDeMateria(materiaId);
        } else {
            filas = notaRepository.findAll();
        }

        List<Dtos.NotaDto> resultado = new ArrayList<>();
        for (Nota nota : filas) {
            if (estudianteId != null && materiaId != null
                    && !nota.getMateria().getId().equals(materiaId)) {
                continue;
            }
            Dtos.NotaDto dto = notaDto(nota);
            if (color != null && !color.equals(dto.color())) {
                continue;
            }
            if (semaforo != null && !semaforo.equals(dto.semaforo())) {
                continue;
            }
            resultado.add(dto);
        }
        resultado.sort(Comparator.comparing(Dtos.NotaDto::estudiante).thenComparing(Dtos.NotaDto::materia));
        return resultado;
    }

    @Transactional
    public Dtos.NotaEntityDto crearNota(Dtos.NotaCreateDto datos) {
        Estudiante estudiante = estudianteRepository.findById(datos.estudianteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado"));
        Materia materia = materiaRepository.findById(datos.materiaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Materia no encontrada"));
        if (notaRepository.existsByEstudianteIdAndMateriaId(datos.estudianteId(), datos.materiaId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El estudiante ya tiene notas en esa materia");
        }
        Nota guardada = notaRepository.save(new Nota(estudiante, materia,
                datos.corte1(), datos.corte2(), datos.corte3()));
        return new Dtos.NotaEntityDto(guardada.getId(), estudiante.getId(), materia.getId(),
                guardada.getCorte1(), guardada.getCorte2(), guardada.getCorte3());
    }

    /* ----------------------------- Estadísticas ----------------------------- */
    public Dtos.EstadisticasDto estadisticas() {
        List<Double> promedios = notaRepository.findAll().stream().map(this::promedio).toList();
        long aprobados = promedios.stream().filter(valor -> valor >= 3).count();
        return new Dtos.EstadisticasDto(
                (int) profesorRepository.count(),
                (int) estudianteRepository.count(),
                (int) materiaRepository.count(),
                promedios.size(),
                promedio(promedios),
                (int) aprobados,
                promedios.size() - (int) aprobados,
                promedios.isEmpty() ? 0.0 : redondear(aprobados * 100.0 / promedios.size()));
    }
}
