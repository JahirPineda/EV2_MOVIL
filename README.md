# Bitácora Escolar — Bitacora

Aplicación móvil desarrollada en **Jetpack Compose**, **Kotlin** y **Room** bajo la arquitectura **MVVM**, diseñada para facilitar el seguimiento pedagógico, la publicación de bitácoras docentes y el registro de aprendizajes y evidencias de los estudiantes en la **Escuela Básica de San Bernardo** (Proyecto correspondiente a la asignatura **DSY1105 - Desarrollo de Aplicaciones Móviles**, Duoc UC).

---

## 👥 Integrantes del Equipo
* **Ignacio Pérez** (Ingeniería en Informática - Duoc UC)
* **Camila Soto** (Ingeniería en Informática - Duoc UC)

---

## 🚀 Características y Funcionalidades Implementadas

1. **Base de Datos y Modelo Relacional (3FN):**
   * **`UsuarioEntity`**: Gestión de usuarios diferenciados por rol (`"DOCENTE"` o `"ESTUDIANTE"`), con campos de RUT, correo, contraseña, curso y asignatura asignada.
   * **`BitacoraEntity`**: Entidad normalizada con identificador autoincremental, título, descripción, fecha, estado (`"BORRADOR"`, `"PUBLICADA"`, `"CERRADA"`) y clave foránea (`docenteId`).
   * **`RegistroEntity`**: Entidad para los avances del estudiante vinculada a bitácoras y usuarios, con soporte para observaciones, fecha, estado de revisión y retroalimentación docente.
   * **Seeding Automático:** Carga inicial automática de 5 docentes y 5 estudiantes de prueba mediante un `RoomDatabase.Callback`.
   * **Persistencia Operativa:** Todas las operaciones de lectura, creación, modificación y eliminación se ejecutan de forma nativa sobre **Room Database**, asegurando persistencia offline completa.

2. **Autenticación, Registro y Gestión de Sesión:**
   * Validación de credenciales basadas en **RUT** y contraseña contra la base de datos local Room.
   * Funcionalidad de **"Recordar usuario"** persistida con `SharedPreferences`.
   * Pantalla de **Registro de Nuevos Usuarios** con selección de rol.
   * Gestión de sesión reactiva mediante `SessionManager` utilizando `StateFlow`.
   * Cierre de sesión (*Logout*) seguro que limpia la sesión actual y redirige al inicio de sesión.

3. **Navegación y Seguridad por Roles (RBAC):**
   * Redirección dinámica tras el inicio de sesión exitoso hacia el **Panel Docente** (`DocenteDashboardScreen`) o **Panel Estudiante** (`EstudianteDashboardScreen`).
   * **Restricción de Acceso:** Guardias de navegación implementadas para evitar que los estudiantes accedan a vistas o formularios exclusivos de docentes.

4. **Formularios, Validaciones de UI y Recursos Nativos:**
   * Formularios interactivos para la creación/edición de bitácoras y registros de aprendizaje.
   * **Validaciones Avanzadas (`ValidationUtils`):** Verificación de formato de RUT chileno (con dígito verificador), validación de fechas (`dd/MM/yyyy`) y longitud mínima de texto.
   * Feedback visual con `isError`, `trailingIcon` (ícono de error M3) y `supportingText` en componentes `OutlinedTextField`.
   * **Recursos Nativos:** Integración con **Photo Picker** (`ActivityResultContracts.PickVisualMedia`) y **Cámara** (`TakePicturePreview`) con guardado físico en almacenamiento interno (`cacheDir`) y vista previa mediante **Coil Compose**.

5. **Filtros, Búsqueda y Animaciones UX:**
   * Búsqueda en tiempo real por título/descripción, filtrado por estado y selector interactivo por fecha/mes.
   * **Animaciones Funcionales:** Uso de `AnimatedVisibility` para expandir/colapsar paneles de búsqueda/filtros, vista previa de fotos y transiciones de entrada basadas en corrutinas (`Animatable`).

---

## 🔑 Cuentas de Prueba (Seeding Inicial)

### 👩‍🏫 Docentes (5)
* **María González** | RUT: `11.111.111-1` | Pass: `docente123` | 6.º básico A • Matemáticas
* **Carlos Soto** | RUT: `22.222.222-2` | Pass: `docente123` | 7.º básico A • Ciencias Naturales
* **Ana Pérez** | RUT: `55.555.555-5` | Pass: `docente123` | 5.º básico A • Lenguaje y Comunicación
* **Jorge Ramírez** | RUT: `66.666.666-6` | Pass: `docente123` | 8.º básico A • Historia y Geografía
* **Claudia Silva** | RUT: `77.777.777-7` | Pass: `docente123` | 6.º básico B • Inglés

### 👨‍🎓 Estudiantes (5)
* **Matías Rojas** | RUT: `33.333.333-3` | Pass: `estudiante123` | 6.º básico A
* **Sofía Morales** | RUT: `44.444.444-4` | Pass: `estudiante123` | 6.º básico A
* **Benjamín Silva** | RUT: `88.888.888-8` | Pass: `estudiante123` | 6.º básico A
* **Valentina Castro** | RUT: `99.999.999-9` | Pass: `estudiante123` | 7.º básico A
* **Martín Pérez** | RUT: `10.101.101-1` | Pass: `estudiante123` | 5.º básico A

---

## 🛠️ Instrucciones de Compilación y Ejecución en Android Studio

1. **Requisitos previos:**
   * Tener instalado **Android Studio** (Koala / Ladybug o superior recomendado).
   * JDK 11 o superior configurado.
   * Dispositivo físico Android (con Depuración USB habilitada) o Emulador Android configurado (API 26 o superior).

2. **Pasos para compilar y ejecutar:**
   1. Clonar o descargar el repositorio del proyecto en su equipo.
   2. Abrir **Android Studio** y seleccionar **Open**, buscando la carpeta raíz del proyecto (`EV2_MOVIL-main`).
   3. Esperar a que Gradle sincronice todas las dependencias (`build.gradle.kts`, librerías Compose, Room con KSP, y Coil).
   4. Conectar un dispositivo o iniciar un emulador en la barra superior de Android Studio.
   5. Hacer clic en el botón **Run** (ícono de reproducción verde ▶) o presionar `Shift + F10` (`Ctrl + R` en macOS).
   6. ¡Listo! La app se compilará e instalará automáticamente en el dispositivo con el ícono adaptativo personalizado y la base de datos Room poblada con los usuarios de prueba.
