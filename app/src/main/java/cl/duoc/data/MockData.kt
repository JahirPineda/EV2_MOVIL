package cl.duoc.data

import cl.duoc.model.Bitacora
import cl.duoc.model.EstudianteSeguimiento
import cl.duoc.model.Recordatorio
import cl.duoc.model.Registro

/**
 * Datos simulados (MockData) para pruebas locales, historial demostrativo y recordatorios.
 */
object MockData {

    // Lista de bitácoras de ejemplo
    val bitacoras = mutableListOf(
        Bitacora(
            id = "1",
            titulo = "Aprendiendo fracciones",
            asignatura = "Matemáticas",
            curso = "6.º básico A",
            mes = "Octubre",
            anio = "2026",
            estado = "Publicada",
            docente = "María González",
            proposito = "Comprender el concepto de fracciones, equivalencias y suma con distinto denominador.",
            instrucciones = "Registra tus aprendizajes al término de cada clase. Adjunta foto de tu guía o cuaderno.",
            preguntasOrientadoras = listOf(
                "¿Qué parte representa el numerador y el denominador?",
                "¿Cómo sumamos fracciones con distinto denominador?",
                "¿Qué dificultad tuviste en los ejercicios?"
            )
        ),
        Bitacora(
            id = "2",
            titulo = "Ecosistemas y biodiversidad",
            asignatura = "Ciencias Naturales",
            curso = "6.º básico A",
            mes = "Octubre",
            anio = "2026",
            estado = "Publicada",
            docente = "María González",
            proposito = "Investigar las cadenas tróficas y el cuidado de los ecosistemas en San Bernardo.",
            instrucciones = "Completa tu bitácora luego de las actividades de laboratorio o trabajo de campo.",
            preguntasOrientadoras = listOf(
                "¿Cuáles son los seres vivos observados?",
                "¿Qué rol cumple cada organismo en la cadena trófica?"
            )
        ),
        Bitacora(
            id = "3",
            titulo = "Geometría y cuerpos 3D",
            asignatura = "Matemáticas",
            curso = "6.º básico A",
            mes = "Septiembre",
            anio = "2026",
            estado = "Cerrada",
            docente = "María González",
            proposito = "Calcular áreas y armar redes geométricas.",
            instrucciones = "Bitácora correspondiente al mes anterior ya finalizada.",
            preguntasOrientadoras = listOf("¿Qué diferencia hay entre prisma y pirámide?")
        ),
        Bitacora(
            id = "4",
            titulo = "Fuerzas y movimiento",
            asignatura = "Ciencias Naturales",
            curso = "7.º básico A",
            mes = "Noviembre",
            anio = "2026",
            estado = "Borrador",
            docente = "María González",
            proposito = "Planificación experimental de fuerzas de gravedad y roce.",
            instrucciones = "Bitácora en preparación para el próximo mes."
        )
    )

    // Lista de registros de ejemplo
    val registros = mutableListOf(
        Registro(
            id = "1",
            bitacoraId = "1",
            bitacoraTitulo = "Aprendiendo fracciones",
            estudiante = "Matías Rojas",
            curso = "6.º básico A",
            asignatura = "Matemáticas",
            fecha = "12/10/2026",
            contenidos = "Suma y resta de fracciones con distinto denominador.",
            actividades = "Resolvimos los ejercicios de la página 45 del texto escolar.",
            aprendizajes = "Aprendí a encontrar el mínimo común múltiplo para igualar los denominadores.",
            dificultades = "Me costó simplificar el resultado al final, pero ya lo entendí mejor.",
            evidencias = listOf("foto_cuaderno_pagina_45.jpg"),
            estado = "Revisado",
            comentarioDocente = "¡Excelente explicación Matías! Tu procedimiento fue muy claro."
        ),
        Registro(
            id = "2",
            bitacoraId = "1",
            bitacoraTitulo = "Aprendiendo fracciones",
            estudiante = "Matías Rojas",
            curso = "6.º básico A",
            asignatura = "Matemáticas",
            fecha = "14/10/2026",
            contenidos = "Fracciones equivalentes con tiras de papel.",
            actividades = "Doblamos papel lustre para comparar mitades, cuartos y octavos.",
            aprendizajes = "Pude ver que 2/4 es igual a 1/2 con el material recortable.",
            dificultades = "Ninguna dificultad, la actividad fue muy entretenida.",
            evidencias = listOf("foto_tiras_fracciones.jpg"),
            estado = "Pendiente",
            comentarioDocente = ""
        ),
        Registro(
            id = "3",
            bitacoraId = "1",
            bitacoraTitulo = "Aprendiendo fracciones",
            estudiante = "Sofía Morales",
            curso = "6.º básico A",
            asignatura = "Matemáticas",
            fecha = "14/10/2026",
            contenidos = "Fracciones equivalentes.",
            actividades = "Ejercicios en la pizarra en equipos de trabajo.",
            aprendizajes = "Multiplicar arriba y abajo por el mismo número para amplificar.",
            dificultades = "Recordar las tablas de multiplicar más rápido.",
            evidencias = listOf("foto_ejercicio_sofia.jpg"),
            estado = "Pendiente",
            comentarioDocente = ""
        ),
        Registro(
            id = "4",
            bitacoraId = "1",
            bitacoraTitulo = "Aprendiendo fracciones",
            estudiante = "Benjamín Silva",
            curso = "6.º básico A",
            asignatura = "Matemáticas",
            fecha = "11/10/2026",
            contenidos = "Representación gráfica de fracciones.",
            actividades = "Dibujos en el cuaderno usando círculos y rectángulos.",
            aprendizajes = "Dividir la figura en partes iguales.",
            dificultades = "Dibujar las partes exactamente del mismo tamaño.",
            evidencias = emptyList(),
            estado = "Revisado",
            comentarioDocente = "Buen trabajo con las figuras. Sigue practicando."
        )
    )

    // Lista de alumnos para seguimiento
    val estudiantesSeguimiento = listOf(
        EstudianteSeguimiento("Matías Rojas", "6.º básico A", 2, true),
        EstudianteSeguimiento("Sofía Morales", "6.º básico A", 1, true),
        EstudianteSeguimiento("Benjamín Silva", "6.º básico A", 1, true),
        EstudianteSeguimiento("Valentina Castro", "6.º básico A", 2, true),
        EstudianteSeguimiento("Martín Pérez", "6.º básico A", 1, true),
        EstudianteSeguimiento("Lucas Valenzuela", "6.º básico A", 0, false),
        EstudianteSeguimiento("Camila Soto", "6.º básico A", 2, true),
        EstudianteSeguimiento("Joaquín Araya", "6.º básico A", 0, false),
        EstudianteSeguimiento("Isidora Fuentes", "6.º básico A", 1, true),
        EstudianteSeguimiento("Agustín Muñoz", "6.º básico A", 0, false)
    )

    // Lista de recordatorios del docente
    val recordatorios = mutableListOf(
        Recordatorio("1", "Preparar bitácora de Noviembre", "28/10/2026", "Definir temas de Geometría para 6.º básico.", false),
        Recordatorio("2", "Cerrar bitácora de Matemáticas", "31/10/2026", "Revisar los últimos aportes pendientes del mes.", false),
        Recordatorio("3", "Revisión final de semestre", "15/11/2026", "Consolidar informe para UTP de la escuela.", true)
    )
}
