package co.edu.uco.panel.config;

import co.edu.uco.panel.model.Estudiante;
import co.edu.uco.panel.model.Materia;
import co.edu.uco.panel.model.Nota;
import co.edu.uco.panel.model.Profesor;
import co.edu.uco.panel.repository.EstudianteRepository;
import co.edu.uco.panel.repository.MateriaRepository;
import co.edu.uco.panel.repository.NotaRepository;
import co.edu.uco.panel.repository.ProfesorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner sembrarDatos(ProfesorRepository profesores,
                                   MateriaRepository materias,
                                   EstudianteRepository estudiantes,
                                   NotaRepository notas) {
        return args -> {
            if (profesores.count() > 0) {
                return;
            }

            Profesor ana = profesores.save(new Profesor(
                    "Ana María", "Gómez", "ana.gomez@uco.edu.co", "Ingeniería de Sistemas"));
            Profesor carlos = profesores.save(new Profesor(
                    "Carlos Andrés", "Pérez", "carlos.perez@uco.edu.co", "Ciencias Básicas"));
            Profesor luisa = profesores.save(new Profesor(
                    "Luisa Fernanda", "Rojas", "luisa.rojas@uco.edu.co", "Ingeniería Industrial"));
            Profesor jorge = profesores.save(new Profesor(
                    "Jorge Iván", "Ramírez", "jorge.ramirez@uco.edu.co", "Humanidades"));

            Materia programacion = materias.save(new Materia("Programación Web", "SIS-301", 4, ana));
            Materia basesDatos = materias.save(new Materia("Bases de Datos", "SIS-302", 4, ana));
            Materia calculo = materias.save(new Materia("Cálculo Diferencial", "MAT-101", 4, carlos));
            Materia estadistica = materias.save(new Materia("Estadística", "MAT-201", 3, carlos));
            Materia procesos = materias.save(new Materia("Procesos Industriales", "IND-210", 3, luisa));
            Materia etica = materias.save(new Materia("Ética Profesional", "HUM-150", 2, jorge));

            Estudiante santiago = estudiantes.save(new Estudiante(
                    "Santiago", "Álvarez", "santiago.alvarez@uco.edu.co", "Ingeniería de Sistemas", 5));
            Estudiante valentina = estudiantes.save(new Estudiante(
                    "Valentina", "Bedoya", "valentina.bedoya@uco.edu.co", "Ingeniería de Sistemas", 5));
            Estudiante mateo = estudiantes.save(new Estudiante(
                    "Mateo", "Cárdenas", "mateo.cardenas@uco.edu.co", "Ingeniería Industrial", 3));
            Estudiante isabella = estudiantes.save(new Estudiante(
                    "Isabella", "Duque", "isabella.duque@uco.edu.co", "Ingeniería de Sistemas", 7));
            Estudiante sebastian = estudiantes.save(new Estudiante(
                    "Sebastián", "Echeverri", "sebastian.echeverri@uco.edu.co", "Ingeniería Industrial", 4));
            Estudiante mariana = estudiantes.save(new Estudiante(
                    "Mariana", "Flórez", "mariana.florez@uco.edu.co", "Psicología", 2));
            Estudiante daniel = estudiantes.save(new Estudiante(
                    "Daniel", "Giraldo", "daniel.giraldo@uco.edu.co", "Ingeniería de Sistemas", 6));
            Estudiante camila = estudiantes.save(new Estudiante(
                    "Camila", "Hoyos", "camila.hoyos@uco.edu.co", "Contaduría Pública", 3));
            Estudiante andres = estudiantes.save(new Estudiante(
                    "Andrés", "Jaramillo", "andres.jaramillo@uco.edu.co", "Ingeniería Industrial", 8));
            Estudiante laura = estudiantes.save(new Estudiante(
                    "Laura", "Montoya", "laura.montoya@uco.edu.co", "Psicología", 4));

            notas.save(new Nota(santiago, programacion, 4.2, 3.8, 4.5));
            notas.save(new Nota(santiago, basesDatos, 3.5, 4.0, 3.9));
            notas.save(new Nota(santiago, calculo, 2.8, 2.5, 3.1));
            notas.save(new Nota(valentina, programacion, 4.8, 4.6, 4.9));
            notas.save(new Nota(valentina, basesDatos, 4.1, 4.3, 4.0));
            notas.save(new Nota(valentina, estadistica, 3.9, 4.2, 4.1));
            notas.save(new Nota(mateo, procesos, 3.0, 2.9, 3.2));
            notas.save(new Nota(mateo, estadistica, 2.4, 2.9, 2.7));
            notas.save(new Nota(isabella, programacion, 3.7, 3.9, 4.0));
            notas.save(new Nota(isabella, basesDatos, 4.5, 4.4, 4.7));
            notas.save(new Nota(isabella, procesos, 3.8, 4.0, 3.6));
            notas.save(new Nota(sebastian, procesos, 1.9, 2.3, 2.1));
            notas.save(new Nota(sebastian, calculo, 2.7, 3.0, 2.5));
            notas.save(new Nota(mariana, etica, 4.4, 4.1, 4.3));
            notas.save(new Nota(mariana, estadistica, 3.6, 3.5, 3.8));
            notas.save(new Nota(daniel, programacion, 3.2, 2.8, 3.4));
            notas.save(new Nota(daniel, basesDatos, 3.0, 3.3, 3.1));
            notas.save(new Nota(camila, etica, 4.6, 4.8, 4.5));
            notas.save(new Nota(camila, estadistica, 3.9, 4.0, 4.2));
            notas.save(new Nota(andres, procesos, 2.1, 1.8, 2.5));
            notas.save(new Nota(andres, calculo, 2.9, 2.6, 3.0));
            notas.save(new Nota(laura, etica, 4.0, 4.4, 4.1));
            notas.save(new Nota(laura, estadistica, 3.3, 3.7, 3.4));
        };
    }
}
