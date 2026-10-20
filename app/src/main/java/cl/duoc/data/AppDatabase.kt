package cl.duoc.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import cl.duoc.data.dao.BitacoraDao
import cl.duoc.data.dao.RegistroDao
import cl.duoc.data.dao.UsuarioDao
import cl.duoc.data.entity.BitacoraEntity
import cl.duoc.data.entity.RegistroEntity
import cl.duoc.data.entity.UsuarioEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Base de datos principal de la aplicación con Room.
 * Define las tablas (entidades) y expone los DAOs para consultar y guardar información.
 */
@Database(
    entities = [UsuarioEntity::class, BitacoraEntity::class, RegistroEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // DAOs para acceder a cada tabla
    abstract fun usuarioDao(): UsuarioDao
    abstract fun bitacoraDao(): BitacoraDao
    abstract fun registroDao(): RegistroDao

    companion object {
        // Instancia única (Patrón Singleton) para evitar abrir múltiples conexiones a la BD
        @Volatile
        private var INSTANCIA: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCIA ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bitacora_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCIA = instancia
                instancia
            }
        }

        // Callback para poblar usuarios iniciales de prueba al crear o abrir la base de datos
        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCIA?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        poblarUsuariosIniciales(database.usuarioDao())
                    }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCIA?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        poblarUsuariosIniciales(database.usuarioDao())
                    }
                }
            }
        }

        // Inserta usuarios iniciales (5 docentes y 5 estudiantes) si la base de datos está vacía
        suspend fun poblarUsuariosIniciales(usuarioDao: UsuarioDao) {
            if (usuarioDao.contarUsuarios() == 0) {
                val usuariosIniciales = listOf(
                    // --- 5 DOCENTES ---
                    UsuarioEntity(
                        id = "11.111.111-1",
                        rut = "11.111.111-1",
                        nombre = "María González",
                        correo = "profesora.maria@colegio.cl",
                        contrasena = "docente123",
                        rol = "DOCENTE",
                        cursoAsignado = "6.º básico A",
                        asignaturaAsignada = "Matemáticas"
                    ),
                    UsuarioEntity(
                        id = "22.222.222-2",
                        rut = "22.222.222-2",
                        nombre = "Carlos Soto",
                        correo = "profesor.carlos@colegio.cl",
                        contrasena = "docente123",
                        rol = "DOCENTE",
                        cursoAsignado = "7.º básico A",
                        asignaturaAsignada = "Ciencias Naturales"
                    ),
                    UsuarioEntity(
                        id = "55.555.555-5",
                        rut = "55.555.555-5",
                        nombre = "Ana Pérez",
                        correo = "profesora.ana@colegio.cl",
                        contrasena = "docente123",
                        rol = "DOCENTE",
                        cursoAsignado = "5.º básico A",
                        asignaturaAsignada = "Lenguaje y Comunicación"
                    ),
                    UsuarioEntity(
                        id = "66.666.666-6",
                        rut = "66.666.666-6",
                        nombre = "Jorge Ramírez",
                        correo = "profesor.jorge@colegio.cl",
                        contrasena = "docente123",
                        rol = "DOCENTE",
                        cursoAsignado = "8.º básico A",
                        asignaturaAsignada = "Historia y Geografía"
                    ),
                    UsuarioEntity(
                        id = "77.777.777-7",
                        rut = "77.777.777-7",
                        nombre = "Claudia Silva",
                        correo = "profesora.claudia@colegio.cl",
                        contrasena = "docente123",
                        rol = "DOCENTE",
                        cursoAsignado = "6.º básico B",
                        asignaturaAsignada = "Inglés"
                    ),

                    // --- 5 ESTUDIANTES ---
                    UsuarioEntity(
                        id = "33.333.333-3",
                        rut = "33.333.333-3",
                        nombre = "Matías Rojas",
                        correo = "matias.rojas@estudiante.cl",
                        contrasena = "estudiante123",
                        rol = "ESTUDIANTE",
                        cursoAsignado = "6.º básico A",
                        asignaturaAsignada = null
                    ),
                    UsuarioEntity(
                        id = "44.444.444-4",
                        rut = "44.444.444-4",
                        nombre = "Sofía Morales",
                        correo = "sofia.morales@estudiante.cl",
                        contrasena = "estudiante123",
                        rol = "ESTUDIANTE",
                        cursoAsignado = "6.º básico A",
                        asignaturaAsignada = null
                    ),
                    UsuarioEntity(
                        id = "88.888.888-8",
                        rut = "88.888.888-8",
                        nombre = "Benjamín Silva",
                        correo = "benjamin.silva@estudiante.cl",
                        contrasena = "estudiante123",
                        rol = "ESTUDIANTE",
                        cursoAsignado = "6.º básico A",
                        asignaturaAsignada = null
                    ),
                    UsuarioEntity(
                        id = "99.999.999-9",
                        rut = "99.999.999-9",
                        nombre = "Valentina Castro",
                        correo = "valentina.castro@estudiante.cl",
                        contrasena = "estudiante123",
                        rol = "ESTUDIANTE",
                        cursoAsignado = "7.º básico A",
                        asignaturaAsignada = null
                    ),
                    UsuarioEntity(
                        id = "10.101.101-1",
                        rut = "10.101.101-1",
                        nombre = "Martín Pérez",
                        correo = "martin.perez@estudiante.cl",
                        contrasena = "estudiante123",
                        rol = "ESTUDIANTE",
                        cursoAsignado = "5.º básico A",
                        asignaturaAsignada = null
                    )
                )
                usuarioDao.insertarUsuarios(usuariosIniciales)
            }
        }
    }
}
