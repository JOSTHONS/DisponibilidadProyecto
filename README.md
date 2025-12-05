# 📅 Disponibilidad - Gestión de Espacios TICS

**Disponibilidad** es una aplicación móvil nativa desarrollada para optimizar la gestión y reserva de espacios institucionales. El proyecto permite consultar disponibilidad en tiempo real y realizar reservas de manera eficiente.

🔗 **Sitio Web:** [https://JOSTHONS.github.io/disponibilidad-tics/](https://JOSTHONS.github.io/disponibilidad-tics/)

## 🚀 Características Principales
* **🔐 Autenticación Segura:** Login y registro con **Firebase Auth**.
* **📅 Reservas en Tiempo Real:** Visualización instantánea mediante **Cloud Firestore**.
* **🔔 Notificaciones:** Alertas de confirmación (Android 13+).
* **👤 Roles de Usuario:** Interfaz para Admin y Estudiante.
* **📊 Dashboard:** Métricas de ocupación.

## 🛠 Stack Tecnológico
| Categoría | Tecnología |
| :--- | :--- |
| **Lenguaje** | Kotlin |
| **UI** | Jetpack Compose (Material 3) |
| **Backend** | Firebase |
| **Patrón** | MVVM |

## 📂 Estructura del Proyecto
El código fuente está organizado siguiendo los principios de **Clean Architecture** y **MVVM** para asegurar la escalabilidad:

```text
mx.edu.utng.jdrj.disponibilidad
├── 📂 ui              # Capa de Presentación (Vistas)
│   ├── 📂 screens     # Pantallas Composable (Login, Home, Reserva)
│   └── 📂 theme       # Tema y Tipografía (Material Design 3)
├── 📂 viewmodel       # Capa de Lógica de Negocio (State Management)
├── 📂 data            # Capa de Datos (Modelos y Repositorios)
│   └── 📂 firebase    # Conexión con Firestore y Auth
└── 📂 utils           # Clases utilitarias (NotificationManager, Constantes)
```

## 📸 Capturas de Pantalla
| Login | Home | Reserva |
|:---:|:---:|:---:|
| ![Login](docs/screenshots/login.png) | ![Home](docs/screenshots/home.png) | ![Reserva](docs/screenshots/reserva.png) |

## 🔧 Instalación
1. Clonar repositorio.
2. Agregar `google-services.json`.
3. Compilar en Android Studio.

## 📄 Documentación KDoc (Ejemplos)

### 1. Navegación
```kotlin
/**
 * Gestiona el grafo de navegación y ViewModels.
 * @see LoginViewModel
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Implementación...
}

```

### 2. Verificación de Permisos

```kotlin
/**
 * Valida permiso POST_NOTIFICATIONS en Android 13+.
 * Si no se tiene, lanza el requestPermissionLauncher.
 */
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
}

```
## 🌟 Validación con Usuarios
El proyecto fue sometido a pruebas de usabilidad con **10 usuarios reales**, obteniendo los siguientes resultados:

| Métrica | Puntuación |
| :--- | :--- |
| **Satisfacción General** | ⭐⭐⭐⭐⭐ (4.8/5.0) |
| **Facilidad de Uso** | 95% |
| **Estabilidad** | 100% (Sin errores críticos) |

## 📺 Demostración
¡Mira la app en funcionamiento!
[**Ver Video en YouTube**](https://youtu.be/dQS_hPHYwmw)

## 👥 Equipo de Desarrollo

| Nombre | Rol |
| :--- | :--- |
| **Paola Moya Díaz** | Desarrollador Android |
| **Josthyn Daniel Rodríguez de Jesús** | Desarrollador Android |

## 📂 ANEXO: Código Fuente Completo
A continuación se adjunta el código fuente de los archivos principales de la aplicación para su revisión directa en este documento.

<details>
<summary><b>CLICK AQUÍ para ver: AndroidManifest.xml</b></summary>

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="[http://schemas.android.com/apk/res/android](http://schemas.android.com/apk/res/android)"
    xmlns:tools="[http://schemas.android.com/tools](http://schemas.android.com/tools)">
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Disponibilidad">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/Theme.Disponibilidad">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
</details>
```

### 🚀 Main Entry Point

Punto de entrada de la aplicación y configuración de navegación.

<details>
<summary><b>📄 MainActivity.kt</b></summary>

```kotlin
package mx.edu.utng.jdrj.disponibilidad

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mx.edu.utng.jdrj.disponibilidad.ui.theme.DisponibilidadTheme
import mx.edu.utng.jdrj.disponibilidad.ui.screens.*
import mx.edu.utng.jdrj.disponibilidad.utils.AppNotificationManager
import mx.edu.utng.jdrj.disponibilidad.viewmodel.AdminViewModel
import mx.edu.utng.jdrj.disponibilidad.viewmodel.HomeViewModel
import mx.edu.utng.jdrj.disponibilidad.viewmodel.LoginViewModel
import mx.edu.utng.jdrj.disponibilidad.viewmodel.ReservaViewModel
import mx.edu.utng.jdrj.disponibilidad.viewmodel.StatisticsViewModel

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Pedir permiso de notificaciones (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        setContent {
            DisponibilidadTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Instanciamos el Gestor de Notificaciones
    val notificationManager = remember { AppNotificationManager(context) }

    val loginViewModel: LoginViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val reservaViewModel: ReservaViewModel = viewModel()
    val adminViewModel: AdminViewModel = viewModel()
    val statisticsViewModel: StatisticsViewModel = viewModel()

    val usuario = loginViewModel.usuarioActual

    // Lógica de notificaciones en tiempo real
    LaunchedEffect(usuario) {
        if (usuario != null) {
            notificationManager.iniciarEscuchaPerfil(usuario.idUsuario)

            if (usuario.rol == "admin") {
                notificationManager.iniciarEscuchaAdmin(usuario)
            } else {
                notificationManager.iniciarEscuchaUsuario(usuario)
            }
        }
    }

    NavHost(navController = navController, startDestination = "login") {

        // 1. LOGIN
        composable("login") {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    val user = loginViewModel.usuarioActual
                    if (user?.rol == "admin") {
                        navController.navigate("admin_dashboard") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        // 2. REGISTRO
        composable("register") {
            RegisterScreen(
                viewModel = loginViewModel,
                onRegisterSuccess = {
                    notificationManager.notificarBienvenida()
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 3. HOME
        composable("home") {
            HomeScreen(
                viewModel = homeViewModel,
                loginViewModel = loginViewModel,
                onEspacioClick = { id -> navController.navigate("detail/$id") },
                onNavigateToMisReservas = { navController.navigate("mis_reservas") },
                onLogout = {
                    loginViewModel.logout()
                    navController.navigate("login") { popUpTo("home") { inclusive = true } }
                },
                onBack = { navController.popBackStack() },
                onIrAPerfil = { navController.navigate("profile") }
            )
        }

        // 4. ADMIN DASHBOARD
        composable("admin_dashboard") {
            AdminDashboardScreen(
                adminViewModel = adminViewModel,
                loginViewModel = loginViewModel,
                onLogout = {
                    loginViewModel.logout()
                    navController.navigate("login") { popUpTo("admin_dashboard") { inclusive = true } }
                },
                onIrAEspacios = { navController.navigate("home") },
                onIrAUsuarios = { navController.navigate("admin_users") },
                onIrAGestionEspacios = { navController.navigate("admin_spaces") },
                onIrAPerfil = { navController.navigate("profile") },
                onIrAEstadisticas = { navController.navigate("statistics") }
            )
        }

        // 5. DETALLE
        composable(
            route = "detail/{espacioId}",
            arguments = listOf(navArgument("espacioId") { type = NavType.StringType })
        ) { backStackEntry ->
            val espacioId = backStackEntry.arguments?.getString("espacioId") ?: ""
            DetailScreen(
                espacioId = espacioId,
                homeViewModel = homeViewModel,
                reservaViewModel = reservaViewModel,
                loginViewModel = loginViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 6. MIS RESERVAS
        composable("mis_reservas") {
            MyReservationsScreen(
                reservaViewModel = reservaViewModel,
                loginViewModel = loginViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 7. GESTIÓN DE USUARIOS
        composable("admin_users") {
            UserManagementScreen(
                adminViewModel = adminViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 8. PERFIL
        composable("profile") {
            ProfileScreen(
                loginViewModel = loginViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 9. GESTIÓN ESPACIOS
        composable("admin_spaces") {
            SpaceManagementScreen(
                adminViewModel = adminViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 10. ESTADÍSTICAS
        composable("statistics") {
            LaunchedEffect(Unit) { statisticsViewModel.calcularEstadisticas() }
            StatisticsScreen(
                viewModel = statisticsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
</details>
```
<details> <summary><b>🐘 build.gradle.kts (Module: app)</b></summary>

Kotlin

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "mx.edu.utng.jdrj.disponibilidad"
    compileSdk = 36
    
    defaultConfig {
        applicationId = "mx.edu.utng.jdrj.disponibilidad"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("androidx.compose.material3:material3:1.2.1")
    // 
}
</details>

```

### 📦 Models

Entidades de datos y objetos de dominio.

<details>
<summary><b>📄 Equipamiento.kt</b></summary>

```kotlin
package mx.edu.utng.jdrj.disponibilidad.data.model

data class Equipamiento(
    val idEquipo: String = "",
    val nombre: String = "",           // Ej: "Proyector", "PC Maestro"
    val cantidad: Int = 1
)

</details>
```

<details>
<summary><b>📄 EquipoUI.kt</b></summary>

```kotlin
package mx.edu.utng.jdrj.disponibilidad.data.model

// Modelo para mostrar equipos en la UI con su cantidad total disponible
data class EquipoUI(
    val idEquipo: String,
    val nombre: String,
    val cantidadTotal: Int
)

</details>
```

<details>
<summary><b>📄 Espacio.kt</b></summary>

```kotlin
package mx.edu.utng.jdrj.disponibilidad.data.model

data class Espacio(
    val idEspacio: String = "",
    val nombre: String = "",           // Ej: "Aula 1", "Laboratorio de Redes"
    val tipo: String = "",             // Ej: "Aula", "Laboratorio", "Auditorio"
    val capacidad: Int = 0,
    val planta: String = "",           // Ej: "Planta Baja", "Primer Piso"
    val descripcion: String = "",      // Detalles extra
    val imagenUrl: String = "",        // URL de la foto (opcional)
    val activo: Boolean = true         // Por si un aula entra en mantenimiento
)

</details>
```

<details>
<summary><b>📄 EspacioEquipamiento.kt</b></summary>

```kotlin
package mx.edu.utng.jdrj.disponibilidad.data.model

data class EspacioEquipamiento(
    val idRelacion: String = "",
    val idEspacio: String = "", // FK hacia Espacio
    val idEquipo: String = "",  // FK hacia Equipamiento
    val cantidad: Int = 1
)

</details>
```

<details>
<summary><b>📄 EspacioFavorito.kt</b></summary>

```kotlin
package mx.edu.utng.jdrj.disponibilidad.data.model

data class EspacioFavorito(
    val idFavorito: String = "",
    val idUsuario: String = "", // FK hacia Usuario
    val idEspacio: String = ""  // FK hacia Espacio
)
</details>```

<details>
<summary><b>📄 ItemReserva.kt</b></summary>

```kotlin
package mx.edu.utng.jdrj.disponibilidad.data.model

data class ItemReserva(
    val idEquipo: String = "",
    val nombreEquipo: String = "",
    val cantidad: Int = 1
)

</details>
```

<details>
<summary><b>📄 Reserva.kt</b></summary>

```kotlin
package mx.edu.utng.jdrj.disponibilidad.data.model

data class Reserva(
    val idReserva: String = "",
    val idUsuario: String = "",
    val idEspacio: String = "",
    val nombreEspacio: String = "",

    // --- CARRITO DE COMPRAS (NUEVO) ---
    // Lista de equipos reservados. Si está vacía = Reserva de Aula completa.
    val equiposReservados: List<ItemReserva> = emptyList(),

    // Campos de fecha y estado
    val fecha: String = "",
    val horaInicio: String = "",
    val horaFin: String = "",
    val estado: String = "pendiente",
    val proposito: String = "",
    val motivoRechazo: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

</details>
```

<details>
<summary><b>📄 Usuario.kt</b></summary>

```kotlin
package mx.edu.utng.jdrj.disponibilidad.data.model

data class Usuario(
    val idUsuario: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val email: String = "",
    val rol: String = "alumno",        // "alumno", "admin"
    val idInstitucional: String = "",

    // NUEVO: Control de acceso.
    // Por defecto es FALSE (nadie entra hasta que el admin lo apruebe, excepto el primer admin manual)
    val aprobado: Boolean = false
)
</details>
```

### 📂 Repositories

Lógica de negocio y acceso a datos (Firestore).

<details>
<summary><b>📄 AuthRepository.kt</b></summary>

```kotlin
package mx.edu.utng.jdrj.disponibilidad.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import mx.edu.utng.jdrj.disponibilidad.data.model.Usuario
import mx.edu.utng.jdrj.disponibilidad.utils.Constants

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // --- LOGIN ---
    suspend fun login(email: String, pass: String): Result<Boolean> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, pass).await()
            val userId = authResult.user?.uid ?: throw Exception("Error de autenticación")

            val snapshot = db.collection(Constants.COLLECTION_USUARIOS)
                .document(userId)
                .get()
                .await()

            val usuario = snapshot.toObject(Usuario::class.java)

            if (usuario == null) {
                auth.signOut()
                return Result.failure(Exception("Usuario no encontrado en la base de datos."))
            }

            if (!usuario.aprobado && usuario.rol != "admin") {
                auth.signOut()
                return Result.failure(Exception("Tu cuenta está pendiente de aprobación por el administrador."))
            }

            Result.success(true)
        } catch (e: Exception) {
            auth.signOut()
            Result.failure(e)
        }
    }

    // --- REGISTRO ---
    suspend fun registro(
        email: String,
        pass: String,
        nombre: String,
        apellido: String,
        idInstitucional: String
    ): Result<Boolean> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
            val userId = authResult.user?.uid ?: throw Exception("Error al obtener ID")

            val nuevoUsuario = Usuario(
                idUsuario = userId,
                nombre = nombre,
                apellido = apellido,
                email = email,
                rol = "alumno",
                idInstitucional = idInstitucional,
                aprobado = false
            )

            db.collection(Constants.COLLECTION_USUARIOS)
                .document(userId)
                .set(nuevoUsuario)
                .await()

            Result.success(true)
        } catch (e: Exception) {
            Log.e("AuthRepo", "Error en registro", e)
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    suspend fun obtenerUsuarioActual(): Usuario? {
        val userId = auth.currentUser?.uid ?: return null
        return try {
            val snapshot = db.collection(Constants.COLLECTION_USUARIOS)
                .document(userId)
                .get()
                .await()
            snapshot.toObject(Usuario::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun estaLogueado(): Boolean = auth.currentUser != null

    // --- GESTIÓN ADMIN ---

    suspend fun obtenerTodosLosUsuarios(): List<Usuario> {
        return try {
            val snapshot = db.collection(Constants.COLLECTION_USUARIOS).get().await()
            snapshot.toObjects(Usuario::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun actualizarEstadoUsuario(idUsuario: String, aprobado: Boolean): Result<Boolean> {
        return try {
            db.collection(Constants.COLLECTION_USUARIOS).document(idUsuario)
                .update("aprobado", aprobado).await()
            Result.success(true)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun cambiarRolUsuario(idUsuario: String, nuevoRol: String): Result<Boolean> {
        return try {
            db.collection(Constants.COLLECTION_USUARIOS).document(idUsuario)
                .update("rol", nuevoRol).await()
            Result.success(true)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun eliminarUsuario(idUsuario: String): Result<Boolean> {
        return try {
            db.collection(Constants.COLLECTION_USUARIOS).document(idUsuario).delete().await()
            Result.success(true)
        } catch (e: Exception) { Result.failure(e) }
    }

    // --- NUEVO: ACTUALIZAR PERFIL (Nombre/Apellido) ---
    suspend fun actualizarDatosPerfil(idUsuario: String, nuevosDatos: Map<String, Any>): Result<Boolean> {
        return try {
            db.collection(Constants.COLLECTION_USUARIOS).document(idUsuario)
                .update(nuevosDatos)
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

</details>
```

<details>
<summary><b>📄 EquiposRepository.kt</b></summary>

```kotlin
package mx.edu.utng.jdrj.disponibilidad.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import mx.edu.utng.jdrj.disponibilidad.data.model.Equipamiento
import mx.edu.utng.jdrj.disponibilidad.data.model.EspacioEquipamiento
import mx.edu.utng.jdrj.disponibilidad.data.model.EquipoUI
import mx.edu.utng.jdrj.disponibilidad.utils.Constants

class EquiposRepository {
    private val db = FirebaseFirestore.getInstance()

    // --- FUNCIÓN CLAVE ACTUALIZADA ---
    // Antes devolvía List<String>, ahora devuelve List<EquipoUI>
    suspend fun obtenerEquiposDeEspacio(idEspacio: String): List<EquipoUI> {
        val listaEquipos = mutableListOf<EquipoUI>()
        try {
            // 1. Buscamos en la tabla intermedia qué equipos tiene este espacio
            val relaciones = db.collection(Constants.COLLECTION_ESPACIO_EQUIPAMIENTO)
                .whereEqualTo("idEspacio", idEspacio)
                .get().await()
                .toObjects(EspacioEquipamiento::class.java)

            // 2. Por cada relación encontrada, buscamos el nombre del equipo en el catálogo
            for (rel in relaciones) {
                val equipoDoc = db.collection(Constants.COLLECTION_EQUIPAMIENTO)
                    .document(rel.idEquipo).get().await()
                val equipo = equipoDoc.toObject(Equipamiento::class.java)

                if (equipo != null) {
                    // AQUÍ ESTÁ LA MAGIA:
                    // Guardamos el objeto completo con su ID y la cantidad real
                    listaEquipos.add(
                        EquipoUI(
                            idEquipo = equipo.idEquipo,  // Necesario para la reserva
                            nombre = equipo.nombre,      // Para mostrar en pantalla
                            cantidadTotal = rel.cantidad // Para validar el stock (tope máximo)
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("EquiposRepo", "Error al traer equipos", e)
        }
        return listaEquipos
    }

    // --- SEEDER DE DATOS (Mantenemos la lógica de creación igual que antes) ---
    // Esta parte se encarga de llenar la base de datos si está vacía
    suspend fun inicializarEquiposSiVacio() {
        val colEquipos = db.collection(Constants.COLLECTION_EQUIPAMIENTO)
        val snapshot = colEquipos.limit(1).get().await()
        if (snapshot.isEmpty) {
            crearCatalogoYRelaciones()
        }
    }

    private fun crearCatalogoYRelaciones() {
        val batch = db.batch()

        // 1. CREAR CATÁLOGO (Tipos de equipos)
        val equipos = listOf(
            Equipamiento("eq_tv", "Smart TV 55\"", 1),
            Equipamiento("eq_pizarron", "Pizarrón Blanco", 1),
            Equipamiento("eq_proyector", "Proyector HD", 1),
            Equipamiento("eq_pc", "PC de Escritorio", 1),
            Equipamiento("eq_restirador", "Mesa de Dibujo", 1),
            Equipamiento("eq_camara", "Cámara de Video 4K", 1),
            Equipamiento("eq_green_screen", "Pantalla Verde (Chroma)", 1),
            Equipamiento("eq_luces", "Kit de Iluminación", 1),
            Equipamiento("eq_mic", "Micrófono Boom", 1),
            Equipamiento("eq_mesa_juntas", "Mesa Ejecutiva", 1),

            // NUEVOS EQUIPOS QUE FALTABAN
            Equipamiento("eq_birrete", "Birrete (Banco Alto)", 1),
            Equipamiento("eq_router", "Router Cisco", 1),
            Equipamiento("eq_switch", "Switch 24 Puertos", 1),
            Equipamiento("eq_cable_utp", "Cable UTP (Red)", 1),
            Equipamiento("eq_cable_consola", "Cable Consola (Azul)", 1),
            Equipamiento("eq_cable_serial", "Cable Serial (Rojo)", 1),
            Equipamiento("eq_cable_fibra", "Cable Fibra Óptica", 1),
            Equipamiento("eq_servidor", "Servidor Rack", 1)
        )

        for (eq in equipos) {
            batch.set(db.collection(Constants.COLLECTION_EQUIPAMIENTO).document(eq.idEquipo), eq)
        }

        // 2. RELACIONES (Asignar equipos a aulas)

        // A) Aulas 1-12
        for (i in 1..12) {
            agregarRelacion(batch, "aula_$i", "eq_tv", 1)
            agregarRelacion(batch, "aula_$i", "eq_pizarron", 1)
        }

        // B) Laboratorios 1-4
        for (i in 1..4) {
            agregarRelacion(batch, "lab_$i", "eq_pizarron", 1)
            agregarRelacion(batch, "lab_$i", "eq_proyector", 1)
            agregarRelacion(batch, "lab_$i", "eq_pc", 25) // 25 PCs disponibles
        }

        // C) Talleres
        for (i in 1..2) {
            agregarRelacion(batch, "taller_dibujo_$i", "eq_tv", 1)
            agregarRelacion(batch, "taller_dibujo_$i", "eq_pizarron", 1)
            agregarRelacion(batch, "taller_dibujo_$i", "eq_proyector", 1)
            agregarRelacion(batch, "taller_dibujo_$i", "eq_restirador", 30)
            agregarRelacion(batch, "taller_dibujo_$i", "eq_birrete", 30) // NUEVO
        }

        // D) Sala Audiovisual
        agregarRelacion(batch, "sala_audiovisual", "eq_proyector", 1)
        agregarRelacion(batch, "sala_audiovisual", "eq_pizarron", 1)

        // E) Sala de Rodajes (Cine)
        agregarRelacion(batch, "sala_rodajes", "eq_camara", 3)
        agregarRelacion(batch, "sala_rodajes", "eq_green_screen", 1)
        agregarRelacion(batch, "sala_rodajes", "eq_luces", 4) // Actualizado a 4
        agregarRelacion(batch, "sala_rodajes", "eq_mic", 2)

        // F) Sala de Juntas
        agregarRelacion(batch, "sala_juntas", "eq_mesa_juntas", 1)

        // G) Laboratorio WAN (NUEVO)
        agregarRelacion(batch, "lab_wan", "eq_pc", 10)
        agregarRelacion(batch, "lab_wan", "eq_router", 10)
        agregarRelacion(batch, "lab_wan", "eq_switch", 17)
        agregarRelacion(batch, "lab_wan", "eq_cable_utp", 4)
        agregarRelacion(batch, "lab_wan", "eq_cable_consola", 4)
        agregarRelacion(batch, "lab_wan", "eq_cable_serial", 4)
        agregarRelacion(batch, "lab_wan", "eq_cable_fibra", 4)

        // H) Laboratorio Seguridad (NUEVO)
        agregarRelacion(batch, "lab_seguridad", "eq_pc", 10)
        agregarRelacion(batch, "lab_seguridad", "eq_servidor", 1)

        batch.commit()
    }

    private fun agregarRelacion(batch: com.google.firebase.firestore.WriteBatch, idEspacio: String, idEquipo: String, cantidad: Int) {
        val docRef = db.collection(Constants.COLLECTION_ESPACIO_EQUIPAMIENTO).document()
        val relacion = EspacioEquipamiento(docRef.id, idEspacio, idEquipo, cantidad)
        batch.set(docRef, relacion)
    }
}

</details>
```

<details>
<summary><b>📄 EspaciosRepository.kt</b></summary>

```kotlin
package mx.edu.utng.jdrj.disponibilidad.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import mx.edu.utng.jdrj.disponibilidad.data.model.Espacio
import mx.edu.utng.jdrj.disponibilidad.utils.Constants

class EspaciosRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection(Constants.COLLECTION_ESPACIOS)

    // --- LEER (READ) ---
    suspend fun obtenerEspacios(): List<Espacio> {
        return try {
            val snapshot = collection.get().await()
            snapshot.toObjects(Espacio::class.java).sortedBy { it.nombre }
        } catch (e: Exception) {
            Log.e("EspaciosRepo", "Error al obtener espacios", e)
            emptyList()
        }
    }

    // --- CREAR / AGREGAR (CREATE) ---
    suspend fun agregarEspacio(espacio: Espacio): Result<Boolean> {
        return try {
            // Si no tiene ID, generamos uno automático
            val idFinal = if (espacio.idEspacio.isEmpty()) collection.document().id else espacio.idEspacio
            val espacioFinal = espacio.copy(idEspacio = idFinal)

            collection.document(idFinal).set(espacioFinal).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- ACTUALIZAR / EDITAR (UPDATE) ---
    suspend fun actualizarEspacio(espacio: Espacio): Result<Boolean> {
        return try {
            collection.document(espacio.idEspacio).set(espacio).await() // Sobrescribe con los nuevos datos
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- ELIMINAR (DELETE) ---
    suspend fun eliminarEspacio(idEspacio: String): Result<Boolean> {
        return try {
            collection.document(idEspacio).delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- INICIALIZAR DATOS (Solo si está vacío) ---
    suspend fun inicializarEspaciosSiEstaVacio() {
        try {
            val snapshot = collection.limit(1).get().await()
            if (snapshot.isEmpty) {
                Log.d("EspaciosRepo", "No hay espacios. Creando datos iniciales...")
                crearDatosIniciales()
            }
        } catch (e: Exception) {
            Log.e("EspaciosRepo", "Error al verificar espacios", e)
        }
    }

    private fun crearDatosIniciales() {
        val batch = db.batch()
        val listaEspacios = mutableListOf<Espacio>()

        // 1. AULAS
        for (i in 1..12) {
            val ubicacion = if (i == 12) "Edificio F - Planta Baja" else "Edificio F - Planta Alta"
            listaEspacios.add(Espacio("aula_$i", "Aula $i", "Aula", 30, ubicacion, "Aula académica estándar con Smart TV y Pizarrón."))
        }
        // 2. LABORATORIOS
        for (i in 1..4) {
            listaEspacios.add(Espacio("lab_$i", "Laboratorio $i", "Laboratorio", 25, "Edificio F - Planta Baja", "Laboratorio de cómputo general."))
        }
        // 3. TALLERES
        listaEspacios.add(Espacio("taller_dibujo_1", "Taller de Dibujo 1", "Taller", 30, "Edificio F - Planta Alta", "Espacio para dibujo técnico."))
        listaEspacios.add(Espacio("taller_dibujo_2", "Taller de Dibujo 2", "Taller", 30, "Edificio F - Planta Baja", "Espacio para dibujo técnico."))
        // 4. SALAS
        listaEspacios.add(Espacio("sala_audiovisual", "Sala Audiovisual", "Auditorio", 50, "Edificio F - Planta Baja", "Auditorio para conferencias."))
        listaEspacios.add(Espacio("sala_juntas", "Sala de Juntas", "Sala", 20, "Edificio F - Planta Alta", "Espacio ejecutivo."))
        listaEspacios.add(Espacio("sala_rodajes", "Estudio de Producción", "Estudio", 5, "Edificio F - Planta Baja", "Estudio profesional de grabación."))
        // 5. NUEVOS
        listaEspacios.add(Espacio("lab_wan", "Laboratorio WAN", "Laboratorio", 20, "Edificio F - Planta Baja", "Laboratorio especializado en redes."))
        listaEspacios.add(Espacio("lab_seguridad", "Laboratorio de Seguridad", "Laboratorio", 15, "Edificio F - Planta Alta", "Laboratorio de Ciberseguridad."))

        for (espacio in listaEspacios) {
            batch.set(collection.document(espacio.idEspacio), espacio)
        }
        batch.commit()
    }
}
</details>
```

<details>
<summary><b>📄 ReservasRepository.kt</b></summary>

```kotlin
package mx.edu.utng.jdrj.disponibilidad.data.repository

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import mx.edu.utng.jdrj.disponibilidad.data.model.Reserva
import mx.edu.utng.jdrj.disponibilidad.data.model.EspacioEquipamiento
import mx.edu.utng.jdrj.disponibilidad.utils.Constants

class ReservasRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection(Constants.COLLECTION_RESERVAS)

    // --- CRUD BÁSICO (Sin cambios) ---
    suspend fun crearReserva(reserva: Reserva): Result<Boolean> {
        return try {
            val errorConflicto = verificarDisponibilidadInteligente(reserva)
            if (errorConflicto != null) return Result.failure(Exception(errorConflicto))
            val docRef = collection.document()
            val nuevaReserva = reserva.copy(idReserva = docRef.id, timestamp = System.currentTimeMillis())
            docRef.set(nuevaReserva).await()
            Result.success(true)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun obtenerMisReservas(usuarioId: String): List<Reserva> {
        return try {
            val snapshot = collection.whereEqualTo("idUsuario", usuarioId).get().await()
            snapshot.toObjects(Reserva::class.java).sortedByDescending { it.timestamp }
        } catch (e: Exception) { emptyList() }
    }

    suspend fun obtenerTodasLasReservas(): List<Reserva> {
        return try {
            val snapshot = collection.get().await()
            snapshot.toObjects(Reserva::class.java)
        } catch (e: Exception) { emptyList() }
    }

    suspend fun eliminarReservaDefinitivamente(idReserva: String): Result<Boolean> {
        return try {
            collection.document(idReserva).delete().await()
            Result.success(true)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun cancelarReserva(idReserva: String): Result<Boolean> {
        return rechazarReserva(idReserva, "Cancelada por el usuario")
    }

    // --- VALIDACIONES (Sin cambios) ---
    private suspend fun verificarDisponibilidadInteligente(nueva: Reserva): String? {
        try {
            val snapshot = collection.whereEqualTo("idEspacio", nueva.idEspacio).whereEqualTo("fecha", nueva.fecha).get().await()
            val reservasDelDia = snapshot.toObjects(Reserva::class.java).filter { it.estado != Constants.ESTADO_CANCELADA }
            val inicioNuevo = convertirHoraAMinutos(nueva.horaInicio)
            val finNuevo = convertirHoraAMinutos(nueva.horaFin)
            val reservasEnConflicto = reservasDelDia.filter { r ->
                val inicioExistente = convertirHoraAMinutos(r.horaInicio)
                val finExistente = convertirHoraAMinutos(r.horaFin)
                (inicioNuevo < finExistente) && (inicioExistente < finNuevo)
            }

            if (nueva.equiposReservados.isEmpty()) {
                if (reservasEnConflicto.isNotEmpty()) return "El espacio o sus equipos ya están ocupados en este horario."
                return null
            } else {
                if (reservasEnConflicto.any { it.equiposReservados.isEmpty() }) return "El aula está reservada completa para un evento/clase."
                for (itemNuevo in nueva.equiposReservados) {
                    var cantidadOcupada = 0
                    for (reservaExistente in reservasEnConflicto) {
                        val itemEncontrado = reservaExistente.equiposReservados.find { it.idEquipo == itemNuevo.idEquipo }
                        if (itemEncontrado != null) cantidadOcupada += itemEncontrado.cantidad
                    }
                    val relacionQuery = db.collection(Constants.COLLECTION_ESPACIO_EQUIPAMIENTO).whereEqualTo("idEspacio", nueva.idEspacio).whereEqualTo("idEquipo", itemNuevo.idEquipo).get().await()
                    val relacion = relacionQuery.toObjects(EspacioEquipamiento::class.java).firstOrNull()
                    val capacidadTotal = relacion?.cantidad ?: 0
                    if ((cantidadOcupada + itemNuevo.cantidad) > capacidadTotal) {
                        val disponibles = capacidadTotal - cantidadOcupada
                        return "No hay suficientes '${itemNuevo.nombreEquipo}'. (Pides ${itemNuevo.cantidad}, quedan $disponibles)."
                    }
                }
                return null
            }
        } catch (e: Exception) { return "Error al verificar disponibilidad: ${e.message}" }
    }

    private fun convertirHoraAMinutos(horaTexto: String): Int {
        try {
            val partes = horaTexto.split(" ", ":")
            if (partes.size < 2) return 0
            var horas = partes[0].toInt()
            val minutos = partes[1].toInt()
            if (partes.size >= 3) {
                val periodo = partes[2].uppercase()
                if (periodo == "PM" && horas != 12) horas += 12
                if (periodo == "AM" && horas == 12) horas = 0
            }
            return (horas * 60) + minutos
        } catch (e: Exception) { return 0 }
    }

    suspend fun obtenerReservasPorEspacioYFecha(idEspacio: String, fecha: String): List<Reserva> {
        return try {
            val snapshot = collection.whereEqualTo("idEspacio", idEspacio).whereEqualTo("fecha", fecha).get().await()
            snapshot.toObjects(Reserva::class.java).filter { it.estado != Constants.ESTADO_CANCELADA }
        } catch (e: Exception) { emptyList() }
    }

    suspend fun obtenerReservasPendientes(): List<Reserva> {
        return try {
            val snapshot = collection.whereEqualTo("estado", Constants.ESTADO_PENDIENTE).get().await()
            snapshot.toObjects(Reserva::class.java).sortedBy { it.timestamp }
        } catch (e: Exception) { emptyList() }
    }

    suspend fun aprobarReserva(idReserva: String): Result<Boolean> {
        return try {
            collection.document(idReserva).update("estado", Constants.ESTADO_APROBADA).await()
            Result.success(true)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun rechazarReserva(idReserva: String, motivo: String): Result<Boolean> {
        return try {
            collection.document(idReserva).update(mapOf("estado" to Constants.ESTADO_CANCELADA, "motivoRechazo" to motivo)).await()
            Result.success(true)
        } catch (e: Exception) { Result.failure(e) }
    }

    // --- NOTIFICACIONES MEJORADAS (AHORA PASAMOS EL TIPO DE CAMBIO) ---

    // Escucha inteligente para ALUMNOS: Pasamos la Reserva y QUÉ LE PASÓ (Agregada, Modificada, Borrada)
    fun escucharCambiosMisReservas(idUsuario: String, onCambio: (Reserva, DocumentChange.Type) -> Unit) {
        collection.whereEqualTo("idUsuario", idUsuario)
            .addSnapshotListener { snapshot, _ ->
                snapshot?.documentChanges?.forEach { change ->
                    // Convertimos a objeto (Cuidado con REMOVED, a veces da null si no se maneja bien, pero Firestore suele guardar el oldDocument)
                    try {
                        val reserva = change.document.toObject(Reserva::class.java)
                        onCambio(reserva, change.type)
                    } catch (e: Exception) {
                        Log.e("ReservasRepo", "Error al procesar cambio", e)
                    }
                }
            }
    }

    // Escucha inteligente para ADMIN (Solo nuevas solicitudes)
    fun escucharNuevasSolicitudes(onNueva: (Reserva) -> Unit) {
        collection.whereEqualTo("estado", Constants.ESTADO_PENDIENTE)
            .addSnapshotListener { snapshot, _ ->
                snapshot?.documentChanges?.forEach { change ->
                    if (change.type == DocumentChange.Type.ADDED) {
                        val reserva = change.document.toObject(Reserva::class.java)
                        onNueva(reserva)
                    }
                }
            }
    }
}

</details>
```


### 🎨 UI Components

Componentes visuales reutilizables (Jetpack Compose).

<details>
<summary><b>📄 EspacioCard.kt</b></summary>

```kotlin
package mx.edu.utng.jdrj.disponibilidad.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.jdrj.disponibilidad.data.model.Espacio

@Composable
fun EspacioCard(
    espacio: Espacio,
    esFavorito: Boolean, // <--- NUEVO: Recibe si es favorito para pintar el corazón
    onClick: () -> Unit
) {
    val icono: ImageVector = when (espacio.tipo) {
        "Laboratorio" -> Icons.Default.Computer
        "Taller" -> Icons.Default.MeetingRoom
        "Auditorio", "Estudio" -> Icons.Default.Tv
        else -> Icons.Default.Class
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono del espacio
            Icon(
                imageVector = icono,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Información
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = espacio.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${espacio.tipo} • Cap: ${espacio.capacidad}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
                Text(
                    text = espacio.planta,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }

            // NUEVO: Icono de corazón si es favorito (Visualización en la lista)
            if (esFavorito) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Es favorito",
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

</details>
```


### 📱 Screens (UI)

Pantallas principales de la aplicación (Vistas).

<details>
<summary><b>📄 AdminDashboardScreen.kt</b></summary>

```kotlin
package mx.edu.utng.jdrj.disponibilidad.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle // <--- Perfil
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import mx.edu.utng.jdrj.disponibilidad.data.model.Reserva
import mx.edu.utng.jdrj.disponibilidad.viewmodel.AdminViewModel
import mx.edu.utng.jdrj.disponibilidad.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    adminViewModel: AdminViewModel,
    loginViewModel: LoginViewModel,
    onLogout: () -> Unit,
    onIrAEspacios: () -> Unit,
    onIrAUsuarios: () -> Unit,
    onIrAGestionEspacios: () -> Unit,
    onIrAPerfil: () -> Unit,
    onIrAEstadisticas: () -> Unit
) {
    val reservas = adminViewModel.reservasPendientes
    val isLoading = adminViewModel.isLoading
    val usuario = loginViewModel.usuarioActual

    var mostrarDialogoRechazo by remember { mutableStateOf(false) }
    var reservaSeleccionada by remember { mutableStateOf<Reserva?>(null) }
    var motivoRechazo by remember { mutableStateOf("") }
    var mostrarDialogoSalir by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        adminViewModel.cargarPendientes()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { }, // Título vacío para limpieza visual
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    // 1. PERFIL (Aquí a la derecha para fácil acceso)
                    IconButton(onClick = onIrAPerfil) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Mi Perfil")
                    }

                    // 2. Actualizar
                    IconButton(onClick = { adminViewModel.cargarPendientes() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualizar")
                    }

                    // 3. Estadísticas
                    IconButton(onClick = onIrAEstadisticas) {
                        Icon(Icons.Default.BarChart, contentDescription = "Reportes")
                    }

                    // 4. Espacios
                    IconButton(onClick = onIrAGestionEspacios) {
                        Icon(Icons.Default.Domain, contentDescription = "Espacios")
                    }

                    // 5. Usuarios
                    IconButton(onClick = onIrAUsuarios) {
                        Icon(Icons.Default.People, contentDescription = "Usuarios")
                    }

                    // 6. Salir
                    IconButton(onClick = { mostrarDialogoSalir = true }) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Salir")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onIrAEspacios,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Asignar Horario", fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // Gris Nube
        ) {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // --- CABECERA PRO ---
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.background
                                    )
                                )
                            )
                            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp, top = 10.dp)
                    ) {
                        Column {
                            Text(
                                text = "Panel de Administración",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = "Hola, ${usuario?.nombre ?: "Admin"}",
                                fontSize = 16.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Tarjeta Resumen
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(4.dp),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Dashboard, null, tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text("Solicitudes Pendientes", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                        Text("${reservas.size}", fontSize = 24.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    IconButton(onClick = { adminViewModel.cargarPendientes() }) {
                                        Icon(Icons.Default.Refresh, contentDescription = "Refrescar", tint = MaterialTheme.colorScheme.secondary)
                                    }
                                }
                            }
                        }
                    }
                }

                // --- LISTA ---
                if (reservas.isEmpty() && !isLoading) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Check, null, tint = Color.LightGray, modifier = Modifier.size(60.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Todo al día", color = Color.Gray, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { adminViewModel.cargarPendientes() }) {
                                Text("Actualizar Lista")
                            }
                        }
                    }
                } else {
                    items(reservas) { reserva ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            ReservaAdminItem(
                                reserva = reserva,
                                onAprobar = { adminViewModel.aprobarReserva(reserva.idReserva) },
                                onRechazarClick = {
                                    reservaSeleccionada = reserva
                                    motivoRechazo = ""
                                    mostrarDialogoRechazo = true
                                }
                            )
                        }
                    }
                }
            }

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        // Diálogos
        if (mostrarDialogoRechazo && reservaSeleccionada != null) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoRechazo = false },
                title = { Text("Rechazar Solicitud") },
                text = {
                    Column {
                        Text("Ingresa el motivo del rechazo:")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = motivoRechazo,
                            onValueChange = { motivoRechazo = it },
                            label = { Text("Motivo") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (motivoRechazo.isNotEmpty()) {
                                adminViewModel.rechazarReserva(reservaSeleccionada!!.idReserva, motivoRechazo)
                                mostrarDialogoRechazo = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) { Text("Confirmar") }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoRechazo = false }) { Text("Cancelar") }
                }
            )
        }

        if (mostrarDialogoSalir) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoSalir = false },
                title = { Text("Cerrar Sesión") },
                text = { Text("¿Estás seguro de que deseas salir?") },
                confirmButton = {
                    Button(onClick = { mostrarDialogoSalir = false; onLogout() }) { Text("Sí, salir") }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoSalir = false }) { Text("Cancelar") }
                }
            )
        }
    }
}

@Composable
fun ReservaAdminItem(
    reserva: Reserva,
    onAprobar: () -> Unit,
    onRechazarClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = reserva.nombreEspacio,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = reserva.fecha,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val detalleEquipo = if (reserva.equiposReservados.isEmpty()) {
                "(Aula Completa)"
            } else {
                "(${reserva.equiposReservados.joinToString(", ") { "${it.cantidad}x ${it.nombreEquipo}" }})"
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccessTime, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${reserva.horaInicio} - ${reserva.horaFin}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = detalleEquipo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(start = 20.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Motivo: ${reserva.proposito}", style = MaterialTheme.typography.bodyMedium, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
            Text(text = "Solicitante ID: ${reserva.idUsuario.take(8)}...", style = MaterialTheme.typography.labelSmall, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onRechazarClick) {
                    Text("Rechazar", color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onAprobar,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Aprobar")
                }
            }
        }
    }
}

</details>
```

<details> <summary><b>📄 DetailScreen.kt</b></summary>

```Kotlin

package mx.edu.utng.jdrj.disponibilidad.ui.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close // <--- IMPORTANTE
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.jdrj.disponibilidad.data.model.Reserva
import mx.edu.utng.jdrj.disponibilidad.data.model.EquipoUI
import mx.edu.utng.jdrj.disponibilidad.data.model.ItemReserva
import mx.edu.utng.jdrj.disponibilidad.viewmodel.HomeViewModel
import mx.edu.utng.jdrj.disponibilidad.viewmodel.LoginViewModel
import mx.edu.utng.jdrj.disponibilidad.viewmodel.ReservaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import mx.edu.utng.jdrj.disponibilidad.data.repository.EquiposRepository
import mx.edu.utng.jdrj.disponibilidad.data.repository.FavoritosRepository
import mx.edu.utng.jdrj.disponibilidad.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    espacioId: String,
    homeViewModel: HomeViewModel,
    reservaViewModel: ReservaViewModel,
    loginViewModel: LoginViewModel,
    onNavigateBack: () -> Unit
) {
    val espacio = homeViewModel.listaEspacios.find { it.idEspacio == espacioId }
    val usuario = loginViewModel.usuarioActual
    val esAdmin = usuario?.rol == "admin"
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val equiposRepo = remember { EquiposRepository() }
    val favoritosRepo = remember { FavoritosRepository() }

    // Estado de equipos
    var listaEquipos by remember { mutableStateOf<List<EquipoUI>>(emptyList()) }
    var esFavorito by remember { mutableStateOf(false) }

    // Agenda Visual
    val agendaOcupada = reservaViewModel.reservasEnFechaSeleccionada

    LaunchedEffect(espacioId) {
        listaEquipos = equiposRepo.obtenerEquiposDeEspacio(espacioId)
        if (usuario != null) esFavorito = favoritosRepo.esFavorito(usuario.idUsuario, espacioId)
        reservaViewModel.cargarAgenda(espacioId, "")
    }

    var fecha by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var horaFin by remember { mutableStateOf("") }
    var proposito by remember { mutableStateOf("") }
    var repetirSemanalmente by remember { mutableStateOf(false) }
    var fechaFinRepeticion by remember { mutableStateOf("") }

    var equipoSeleccionado by remember { mutableStateOf<EquipoUI?>(null) }
    var cantidadSolicitada by remember { mutableIntStateOf(1) }

    var expandirInicio by remember { mutableStateOf(false) }
    var expandirFin by remember { mutableStateOf(false) }

    val horasDisponibles = remember {
        (8..22).map { hora ->
            val periodo = if (hora < 12) "AM" else "PM"
            val hora12 = if (hora > 12) hora - 12 else hora
            "${hora12.toString().padStart(2, '0')}:00 $periodo"
        }
    }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val dia = dayOfMonth.toString().padStart(2, '0')
            val mes = (month + 1).toString().padStart(2, '0')
            val nuevaFecha = "$dia/$mes/$year"
            fecha = nuevaFecha
            reservaViewModel.cargarAgenda(espacioId, nuevaFecha)
        },
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.datePicker.minDate = calendar.timeInMillis

    val datePickerRepeticion = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val dia = dayOfMonth.toString().padStart(2, '0')
            val mes = (month + 1).toString().padStart(2, '0')
            fechaFinRepeticion = "$dia/$mes/$year"
        },
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerRepeticion.datePicker.minDate = calendar.timeInMillis + 86400000

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(espacio?.nombre ?: "Detalle", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (usuario != null) {
                            esFavorito = !esFavorito
                            scope.launch { favoritosRepo.toggleFavorito(usuario.idUsuario, espacioId) }
                        }
                    }) {
                        Icon(
                            imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (esFavorito) Color(0xFFFFEB3B) else Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        if (espacio != null) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // TARJETA DE INFO
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Ubicación:", fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
                        Text(text = espacio.planta, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Descripción:", fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
                        Text(text = espacio.descripcion, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        if (listaEquipos.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Equipamiento Disponible:", fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(8.dp))

                            listaEquipos.forEach { item ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Filled.CheckCircle, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "${item.cantidadTotal}x ${item.nombre}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            equipoSeleccionado = item
                                            cantidadSolicitada = 1
                                            proposito = "Reserva de equipo: ${item.nombre}"
                                        },
                                        modifier = Modifier.height(32.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                        colors = if (equipoSeleccionado == item)
                                            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                                        else
                                            ButtonDefaults.outlinedButtonColors()
                                    ) {
                                        Text(if (equipoSeleccionado == item) "Seleccionado" else "Reservar", fontSize = 12.sp)
                                    }
                                }
                            }

                            if (equipoSeleccionado != null) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "Seleccionaste: ${equipoSeleccionado!!.nombre}",
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Cantidad:", modifier = Modifier.padding(end = 16.dp))
                                            IconButton(
                                                onClick = { if (cantidadSolicitada > 1) cantidadSolicitada-- },
                                                enabled = cantidadSolicitada > 1
                                            ) {
                                                Icon(Icons.Filled.Remove, "Menos")
                                            }
                                            Text(
                                                text = cantidadSolicitada.toString(),
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 16.dp)
                                            )
                                            IconButton(
                                                onClick = { if (cantidadSolicitada < equipoSeleccionado!!.cantidadTotal) cantidadSolicitada++ },
                                                enabled = cantidadSolicitada < equipoSeleccionado!!.cantidadTotal
                                            ) {
                                                Icon(Icons.Filled.Add, "Más")
                                            }
                                        }
                                        Text("Máx: ${equipoSeleccionado!!.cantidadTotal}", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
                                    }
                                }

                                // --- AQUÍ ESTÁ EL BOTÓN ROJO QUE PEDISTE ---
                                OutlinedButton(
                                    onClick = {
                                        equipoSeleccionado = null
                                        proposito = ""
                                        cantidadSolicitada = 1
                                    },
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .padding(top = 8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    ),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                                ) {
                                    Icon(Icons.Filled.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Cancelar selección")
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // TÍTULO DE ACCIÓN
                val textoAccion = if (esAdmin) "Asignar Horario (Admin)" else "Reservar"
                val objetoReserva = if (equipoSeleccionado != null) "${cantidadSolicitada}x ${equipoSeleccionado!!.nombre}" else "Espacio Completo"

                Text(
                    text = "$textoAccion: $objetoReserva",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // CAMPO FECHA
                OutlinedTextField(
                    value = fecha, onValueChange = {}, label = { Text("Fecha") },
                    leadingIcon = { Icon(Icons.Filled.CalendarToday, null, tint = MaterialTheme.colorScheme.primary) },
                    modifier = Modifier.fillMaxWidth().clickable { datePickerDialog.show() }, enabled = false,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.Black,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                if (fecha.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    // AGENDA VISUAL
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Disponibilidad:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0D47A1))
                            Spacer(modifier = Modifier.height(8.dp))
                            if (agendaOcupada.isEmpty()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Todo el día libre ✅", fontSize = 13.sp, color = Color(0xFF2E7D32))
                                }
                            } else {
                                agendaOcupada.forEach { reserva ->
                                    val esBloqueoTotal = reserva.equiposReservados.isEmpty()
                                    val detalle = if (esBloqueoTotal) "(Aula Completa)" else "(${reserva.equiposReservados.joinToString { "${it.cantidad}x ${it.nombreEquipo}" }})"
                                    val colorEstado = if (esBloqueoTotal) MaterialTheme.colorScheme.error else Color(0xFFF57C00)
                                    val icono = if (esBloqueoTotal) Icons.Filled.Block else Icons.Filled.Info

                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 4.dp)) {
                                        Icon(icono, null, tint = colorEstado, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("${reserva.horaInicio} - ${reserva.horaFin} $detalle", fontSize = 13.sp, color = colorEstado)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // SELECTORES DE HORA
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = horaInicio, onValueChange = {}, label = { Text("Inicio") },
                            trailingIcon = { Icon(Icons.Filled.ArrowDropDown, null) },
                            leadingIcon = { Icon(Icons.Filled.AccessTime, null) },
                            modifier = Modifier.fillMaxWidth().clickable { expandirInicio = true }, enabled = false,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(disabledTextColor = Color.Black, disabledBorderColor = MaterialTheme.colorScheme.outline, disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        DropdownMenu(expanded = expandirInicio, onDismissRequest = { expandirInicio = false }, modifier = Modifier.heightIn(max = 200.dp)) {
                            horasDisponibles.forEach { hora ->
                                DropdownMenuItem(text = { Text(hora) }, onClick = { horaInicio = hora; expandirInicio = false; val indexInicio = horasDisponibles.indexOf(hora); val indexFinActual = horasDisponibles.indexOf(horaFin); if (horaFin.isEmpty() || indexFinActual <= indexInicio) if (indexInicio + 1 < horasDisponibles.size) horaFin = horasDisponibles[indexInicio + 1] else horaFin = "" })
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    val habilitarFin = horaInicio.isNotEmpty()
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = horaFin, onValueChange = {}, label = { Text("Fin") },
                            trailingIcon = { Icon(Icons.Filled.ArrowDropDown, null) },
                            leadingIcon = { Icon(Icons.Filled.AccessTime, null) },
                            modifier = Modifier.fillMaxWidth().clickable(enabled = habilitarFin) { expandirFin = true }, enabled = false,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(disabledTextColor = Color.Black, disabledBorderColor = MaterialTheme.colorScheme.outline, disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant, disabledContainerColor = if (habilitarFin) Color.Transparent else Color.LightGray.copy(alpha = 0.2f))
                        )
                        DropdownMenu(expanded = expandirFin, onDismissRequest = { expandirFin = false }, modifier = Modifier.heightIn(max = 200.dp)) {
                            val indexInicio = horasDisponibles.indexOf(horaInicio); horasDisponibles.forEachIndexed { index, hora -> if (horaInicio.isEmpty() || index > indexInicio) DropdownMenuItem(text = { Text(hora) }, onClick = { horaFin = hora; expandirFin = false }) }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(value = proposito, onValueChange = { proposito = it }, label = { Text("Propósito") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))

                if (esAdmin) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = repetirSemanalmente, onCheckedChange = { repetirSemanalmente = it }, colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary))
                                Text("Repetir todo el cuatrimestre")
                            }
                            if (repetirSemanalmente) {
                                OutlinedTextField(value = fechaFinRepeticion, onValueChange = {}, label = { Text("Fecha Fin") }, leadingIcon = { Icon(Icons.Filled.DateRange, null) }, modifier = Modifier.fillMaxWidth().clickable { datePickerRepeticion.show() }, enabled = false, shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(disabledTextColor = Color.Black))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (reservaViewModel.mensajeExito != null) Text(reservaViewModel.mensajeExito!!, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                if (reservaViewModel.mensajeError != null) Text(reservaViewModel.mensajeError!!, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val camposValidos = usuario != null && fecha.isNotEmpty() && horaInicio.isNotEmpty() && horaFin.isNotEmpty()
                        val repeticionValida = if (repetirSemanalmente) fechaFinRepeticion.isNotEmpty() else true

                        if (camposValidos && repeticionValida) {
                            val estadoInicial = if (esAdmin) Constants.ESTADO_APROBADA else Constants.ESTADO_PENDIENTE

                            // CREACIÓN DE LISTA DE EQUIPOS (CARRITO)
                            val listaItems = if (equipoSeleccionado != null) {
                                listOf(ItemReserva(equipoSeleccionado!!.idEquipo, equipoSeleccionado!!.nombre, cantidadSolicitada))
                            } else {
                                emptyList()
                            }

                            val nuevaReserva = Reserva(
                                idUsuario = usuario!!.idUsuario,
                                idEspacio = espacio.idEspacio,
                                nombreEspacio = espacio.nombre,
                                equiposReservados = listaItems, // <--- CARRITO
                                fecha = fecha,
                                horaInicio = horaInicio,
                                horaFin = horaFin,
                                proposito = proposito,
                                estado = estadoInicial
                            )

                            if (esAdmin && repetirSemanalmente) {
                                reservaViewModel.crearReservaRecurrente(nuevaReserva, fechaFinRepeticion) { }
                            } else {
                                reservaViewModel.crearReserva(nuevaReserva) { }
                            }
                        } else {
                            if (usuario == null) reservaViewModel.mensajeError = "Error crítico: Usuario no cargado."
                            else reservaViewModel.mensajeError = "Por favor completa todos los campos."
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !reservaViewModel.isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    if (reservaViewModel.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        val textoBoton = if (esAdmin && repetirSemanalmente) "ASIGNAR TODO EL CUATRI" else if (esAdmin) "ASIGNAR HORARIO" else "CONFIRMAR RESERVA"
                        Text(textoBoton, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Espacio no encontrado") }
        }
    }
} 
</details>
```

<details> <summary><b>📄 HomeScreen.kt</b></summary>

```Kotlin

package mx.edu.utng.jdrj.disponibilidad.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle // <--- ÍCONO DE PERFIL
import androidx.compose.material.icons.filled.Event // <--- ÍCONO DE RESERVAS (Calendario)
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import mx.edu.utng.jdrj.disponibilidad.ui.components.EspacioCard
import mx.edu.utng.jdrj.disponibilidad.viewmodel.HomeViewModel
import mx.edu.utng.jdrj.disponibilidad.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    loginViewModel: LoginViewModel? = null,
    onEspacioClick: (String) -> Unit,
    onNavigateToMisReservas: () -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit = {},
    onIrAPerfil: () -> Unit = {} // Parametro para ir al perfil
) {
    val todosLosEspacios = viewModel.listaEspacios
    val idsFavoritos = viewModel.idsFavoritos
    val mostrarSoloFavoritos = viewModel.mostrarSoloFavoritos
    val isLoading = viewModel.isLoading
    val usuario = loginViewModel?.usuarioActual

    val esAdmin = usuario?.rol == "admin"
    var mostrarDialogoSalir by remember { mutableStateOf(false) }

    // Carga inicial de datos
    LaunchedEffect(Unit) {
        if (usuario != null) viewModel.cargarFavoritos(usuario.idUsuario)
    }

    val espaciosA_Mostrar = if (mostrarSoloFavoritos) {
        todosLosEspacios.filter { it.idEspacio in idsFavoritos }
    } else {
        todosLosEspacios
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { }, // TÍTULO VACÍO (Para limpieza visual)
                navigationIcon = {
                    if (esAdmin) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                        }
                    }
                },
                actions = {
                    // 1. BOTÓN DE PERFIL (Ahora está aquí, a la derecha, junto a los demás)
                    IconButton(onClick = onIrAPerfil) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Mi Perfil", tint = Color.White)
                    }

                    // 2. Botón Actualizar
                    IconButton(onClick = {
                        viewModel.cargarEspacios()
                        if (usuario != null) viewModel.cargarFavoritos(usuario.idUsuario)
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualizar", tint = Color.White)
                    }

                    // 3. Botón Filtro Favoritos
                    IconButton(onClick = { viewModel.toggleFiltro() }) {
                        Icon(
                            imageVector = if (mostrarSoloFavoritos) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Filtrar Favoritos",
                            tint = if (mostrarSoloFavoritos) Color(0xFFFFEB3B) else Color.White
                        )
                    }

                    // 4. Botón Mis Reservas (SOLO ÍCONO DE CALENDARIO)
                    IconButton(onClick = onNavigateToMisReservas) {
                        Icon(Icons.Default.Event, contentDescription = "Mis Reservas", tint = Color.White)
                    }

                    // 5. Botón Salir
                    IconButton(onClick = { mostrarDialogoSalir = true }) {
                        Icon(Icons.Default.Logout, contentDescription = "Salir", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (espaciosA_Mostrar.isEmpty() && !isLoading) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (mostrarSoloFavoritos) "No tienes favoritos aún." else "No hay espacios disponibles.",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón de actualizar en pantalla vacía
                    Button(
                        onClick = { viewModel.cargarEspacios() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Icon(Icons.Default.Refresh, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Actualizar")
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 16.dp), // Quitamos padding superior para pegar el header
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // --- CABECERA PRO (HEADER) ---
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary, // Empieza azul
                                            MaterialTheme.colorScheme.background // Termina en gris fondo
                                        )
                                    )
                                )
                                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp, top = 8.dp)
                        ) {
                            Column {
                                Text(
                                    text = if (mostrarSoloFavoritos) "Tus Favoritos ❤️" else "Espacios UTNG",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Reserva aulas y laboratorios",
                                    fontSize = 16.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }

                    // --- LISTA DE TARJETAS ---
                    items(espaciosA_Mostrar) { espacio ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            EspacioCard(
                                espacio = espacio,
                                esFavorito = espacio.idEspacio in idsFavoritos,
                                onClick = { onEspacioClick(espacio.idEspacio) }
                            )
                        }
                    }
                }
            }

            // Spinner de carga
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        // DIÁLOGO DE SALIDA
        if (mostrarDialogoSalir) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoSalir = false },
                title = { Text("Cerrar Sesión") },
                text = { Text("¿Estás seguro de que deseas salir?") },
                confirmButton = {
                    Button(onClick = { mostrarDialogoSalir = false; onLogout() }) { Text("Sí, salir") }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoSalir = false }) { Text("Cancelar") }
                },
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
} 
</details>
```

<details> <summary><b>📄 LoginScreen.kt</b></summary>

```Kotlin

package mx.edu.utng.jdrj.disponibilidad.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.jdrj.disponibilidad.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Fondo con un degradado sutil para que se vea moderno (Azul muy oscuro a Negro si es dark, o Azul a Blanco)
    // Usaremos el color de fondo del tema pero le daremos estilo al contenido.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // --- LOGO / ICONO SUPERIOR ---
            // Usamos un contenedor circular con el color primario suave
            Surface(
                modifier = Modifier.size(100.dp),
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance, // Icono de Universidad
                        contentDescription = "Logo UTNG",
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- TÍTULO ---
            Text(
                text = "UTNG",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 2.sp
            )

            Text(
                text = "Gestión de Espacios",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- SUBTÍTULO (Ya no es verde feo) ---
            Text(
                text = "Edificio F - Tecnologías de la Información",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant, // Gris elegante
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // --- TARJETA DEL FORMULARIO ---
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Campo Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo Institucional") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp) // Bordes redondeados modernos
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (viewModel.errorMessage != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = viewModel.errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (viewModel.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Button(
                            onClick = { viewModel.login(email, password, onLoginSuccess) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(text = "INICIAR SESIÓN", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- FOOTER (Registro) ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "¿No tienes cuenta? ",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                TextButton(onClick = onNavigateToRegister) {
                    Text(
                        text = "Regístrate aquí",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
</details>
```

<details> <summary><b>📄 MyReservationsScreen.kt</b></summary>

```Kotlin

package mx.edu.utng.jdrj.disponibilidad.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh // Icono para actualizar
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.jdrj.disponibilidad.data.model.Reserva
import mx.edu.utng.jdrj.disponibilidad.utils.Constants
import mx.edu.utng.jdrj.disponibilidad.viewmodel.LoginViewModel
import mx.edu.utng.jdrj.disponibilidad.viewmodel.ReservaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyReservationsScreen(
    reservaViewModel: ReservaViewModel,
    loginViewModel: LoginViewModel,
    onNavigateBack: () -> Unit
) {
    val usuario = loginViewModel.usuarioActual
    val esAdmin = usuario?.rol == "admin"

    val reservas = reservaViewModel.reservasVisibles
    val isLoading = reservaViewModel.isLoading
    val mostrandoTodo = reservaViewModel.mostrarHistorialCompleto

    // Carga inicial
    LaunchedEffect(Unit) {
        if (usuario != null) {
            reservaViewModel.cargarMisReservas(usuario.idUsuario)
        }
    }

    val tituloPantalla = when {
        mostrandoTodo -> "Historial Completo"
        esAdmin -> "Horarios Asignados"
        else -> "Mis Reservas Activas"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tituloPantalla, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                actions = {
                    // --- BOTÓN ACTUALIZAR (Manual y Seguro) ---
                    IconButton(onClick = {
                        if (usuario != null) reservaViewModel.cargarMisReservas(usuario.idUsuario)
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualizar", tint = MaterialTheme.colorScheme.onPrimary)
                    }

                    // Botón Historial
                    TextButton(onClick = { reservaViewModel.toggleMostrarTodo() }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (mostrandoTodo) Icons.Default.Visibility else Icons.Default.History,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (mostrandoTodo) "Ver Recientes" else "Ver Historial", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            if (reservas.isEmpty() && !isLoading) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (mostrandoTodo) "Historial vacío." else "No hay registros activos.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón grande para actualizar si está vacío
                    Button(
                        onClick = { if (usuario != null) reservaViewModel.cargarMisReservas(usuario.idUsuario) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
                    ) {
                        Icon(Icons.Default.Refresh, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Actualizar")
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(reservas) { reserva ->
                        ReservaItem(
                            reserva = reserva,
                            esModoHistorial = mostrandoTodo,
                            onCancel = {
                                if (usuario != null) {
                                    reservaViewModel.cancelarReserva(reserva.idReserva, usuario.idUsuario)
                                }
                            },
                            onDelete = {
                                if (usuario != null) {
                                    reservaViewModel.eliminarReserva(reserva.idReserva, usuario.idUsuario)
                                }
                            }
                        )
                    }
                }
            }

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.secondary)
            }

            if (reservaViewModel.mensajeExito != null) {
                Text(
                    text = reservaViewModel.mensajeExito!!,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .padding(bottom = 32.dp)
                        .background(Color(0xFF4CAF50), shape = RoundedCornerShape(8.dp))
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun ReservaItem(
    reserva: Reserva,
    esModoHistorial: Boolean,
    onCancel: () -> Unit,
    onDelete: () -> Unit
) {
    val colorEstado = when (reserva.estado) {
        Constants.ESTADO_APROBADA -> Color(0xFF2E7D32) // Verde
        Constants.ESTADO_CANCELADA -> Color.Red
        else -> Color(0xFFF57C00) // Naranja
    }

    // --- LÓGICA CORREGIDA PARA EL CARRITO ---
    val descripcionReserva = if (reserva.equiposReservados.isEmpty()) {
        "(Aula Completa)"
    } else {
        // Muestra: (1x PC, 2x Cables)
        "(" + reserva.equiposReservados.joinToString(", ") { "${it.cantidad}x ${it.nombreEquipo}" } + ")"
    }

    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = reserva.nombreEspacio,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    // Aquí se muestra qué pidió
                    Text(
                        text = descripcionReserva,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Surface(
                    color = colorEstado.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = reserva.estado.uppercase(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = colorEstado,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Fecha: ${reserva.fecha}", color = Color.Gray)
            Text("Horario: ${reserva.horaInicio} - ${reserva.horaFin}", fontWeight = FontWeight.Medium)

            if (reserva.motivoRechazo.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Motivo: ${reserva.motivoRechazo}",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.align(Alignment.End)) {
                if (reserva.estado == Constants.ESTADO_CANCELADA && esModoHistorial) {
                    OutlinedButton(
                        onClick = onDelete,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.DarkGray),
                        shape = RoundedCornerShape(50)
                    ) {
                        Icon(Icons.Default.DeleteForever, null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Eliminar")
                    }
                } else if (reserva.estado != Constants.ESTADO_CANCELADA) {
                    OutlinedButton(
                        onClick = onCancel,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(50)
                    ) {
                        Icon(Icons.Default.Cancel, null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}
</details>
```

<details> <summary><b>📄 ProfileScreen.kt</b></summary>

```Kotlin

package mx.edu.utng.jdrj.disponibilidad.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.jdrj.disponibilidad.viewmodel.LoginViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    loginViewModel: LoginViewModel,
    onNavigateBack: () -> Unit
) {
    val usuario = loginViewModel.usuarioActual
    val isLoading = loginViewModel.isLoading
    val errorMsg = loginViewModel.errorMessage

    // Estados para edición
    var isEditing by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf(usuario?.nombre ?: "") }
    var apellido by remember { mutableStateOf(usuario?.apellido ?: "") }

    // Estado para mensaje de éxito
    var showSuccessMessage by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (usuario == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // --- AVATAR ---
                    Surface(
                        modifier = Modifier.size(100.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${usuario.nombre.firstOrNull() ?: ""}${usuario.apellido.firstOrNull() ?: ""}",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${usuario.nombre} ${usuario.apellido}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Surface(
                        color = if (usuario.rol == "admin") Color(0xFF1A237E) else MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = if (usuario.rol == "admin") "Administrador" else "Usuario Normal",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- TARJETA DE DATOS ---
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            if (!isEditing) {
                                // MODO VISUALIZACIÓN
                                ProfileItem("Nombre", usuario.nombre, Icons.Default.Person)
                                Spacer(modifier = Modifier.height(12.dp))
                                ProfileItem("Apellido", usuario.apellido, Icons.Default.Person)
                            } else {
                                // MODO EDICIÓN
                                OutlinedTextField(
                                    value = nombre,
                                    onValueChange = { nombre = it },
                                    label = { Text("Nombre") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedTextField(
                                    value = apellido,
                                    onValueChange = { apellido = it },
                                    label = { Text("Apellido") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(color = Color.LightGray.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(12.dp))

                            // DATOS NO EDITABLES
                            ProfileItem("Correo", usuario.email, Icons.Default.Email)
                            Spacer(modifier = Modifier.height(12.dp))
                            ProfileItem("ID / Matrícula", usuario.idInstitucional, Icons.Default.Badge)
                            Spacer(modifier = Modifier.height(12.dp))
                            ProfileItem(
                                "Estado",
                                if (usuario.aprobado) "Cuenta Verificada" else "Pendiente",
                                Icons.Default.VerifiedUser,
                                colorTexto = if (usuario.aprobado) Color(0xFF2E7D32) else Color.Red
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // MENSAJES
                    if (errorMsg != null) {
                        Text(errorMsg, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (showSuccessMessage) {
                        Text("¡Perfil actualizado correctamente!", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                        LaunchedEffect(Unit) {
                            delay(3000)
                            showSuccessMessage = false
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // BOTONES DE ACCIÓN
                    if (!isEditing) {
                        Button(
                            onClick = { isEditing = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Editar Perfil")
                        }
                    } else {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = {
                                    // Cancelar: Revertimos cambios
                                    isEditing = false
                                    nombre = usuario.nombre
                                    apellido = usuario.apellido
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancelar")
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(
                                onClick = {
                                    if (nombre.isNotEmpty() && apellido.isNotEmpty()) {
                                        loginViewModel.actualizarPerfil(nombre, apellido) {
                                            isEditing = false
                                            showSuccessMessage = true
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                enabled = !isLoading
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                                } else {
                                    Icon(Icons.Default.Save, null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Guardar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, colorTexto: Color = Color.Black) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
            Text(text = value, fontSize = 16.sp, color = colorTexto, fontWeight = FontWeight.Medium)
        }
    }
} 
</details>
```

<details> <summary><b>📄 RegisterScreen.kt</b></summary>

```Kotlin

package mx.edu.utng.jdrj.disponibilidad.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.jdrj.disponibilidad.viewmodel.LoginViewModel

@Composable
fun RegisterScreen(
    viewModel: LoginViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    // Estados locales para el formulario
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var idInstitucional by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Estados para mensajes de error de validación local
    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorApellido by remember { mutableStateOf<String?>(null) }
    var errorId by remember { mutableStateOf<String?>(null) }
    var errorEmail by remember { mutableStateOf<String?>(null) }
    var errorPassword by remember { mutableStateOf<String?>(null) }

    // Función auxiliar para validar email (Más flexible: solo requiere @)
    fun esEmailValido(email: String): Boolean {
        return email.contains("@")
    }

    // Scroll por si el teclado tapa los campos en pantallas pequeñas
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Crear Cuenta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- NOMBRE ---
        OutlinedTextField(
            value = nombre,
            onValueChange = { input ->
                // Validación en tiempo real: Solo letras y espacios
                if (input.all { it.isLetter() || it.isWhitespace() }) {
                    nombre = input
                    errorNombre = null // Limpiar error si corrige
                }
            },
            label = { Text("Nombre") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorNombre != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        if (errorNombre != null) {
            Text(text = errorNombre!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- APELLIDO ---
        OutlinedTextField(
            value = apellido,
            onValueChange = { input ->
                if (input.all { it.isLetter() || it.isWhitespace() }) {
                    apellido = input
                    errorApellido = null
                }
            },
            label = { Text("Apellido") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorApellido != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        if (errorApellido != null) {
            Text(text = errorApellido!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- ID INSTITUCIONAL / MATRÍCULA ---
        OutlinedTextField(
            value = idInstitucional,
            onValueChange = { input ->
                // Validación: Solo números
                if (input.all { it.isDigit() }) {
                    idInstitucional = input
                    errorId = null
                }
            },
            label = { Text("ID Institucional / Matrícula") },
            leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorId != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        if (errorId != null) {
            Text(text = errorId!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- EMAIL ---
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                errorEmail = null
            },
            label = { Text("Correo Electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            isError = errorEmail != null
        )
        if (errorEmail != null) {
            Text(text = errorEmail!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- PASSWORD ---
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorPassword = null
            },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            isError = errorPassword != null
        )
        if (errorPassword != null) {
            Text(text = errorPassword!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
        }

        // Mostrar error general de Firebase
        if (viewModel.errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = viewModel.errorMessage!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    // --- VALIDACIONES FINALES ---
                    var hayErrores = false

                    if (nombre.isBlank()) {
                        errorNombre = "El nombre es obligatorio"
                        hayErrores = true
                    }
                    if (apellido.isBlank()) {
                        errorApellido = "El apellido es obligatorio"
                        hayErrores = true
                    }
                    if (idInstitucional.isBlank()) {
                        errorId = "La matrícula es obligatoria"
                        hayErrores = true
                    }
                    if (email.isBlank()) {
                        errorEmail = "El correo es obligatorio"
                        hayErrores = true
                    } else if (!esEmailValido(email)) {
                        errorEmail = "El correo debe contener un @"
                        hayErrores = true
                    }
                    if (password.length < 6) {
                        errorPassword = "Mínimo 6 caracteres"
                        hayErrores = true
                    }

                    if (!hayErrores) {
                        viewModel.registro(
                            email, password, nombre, apellido, idInstitucional, onRegisterSuccess
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("REGISTRARME")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateBack) {
            Text("Volver al Login")
        }
    }
} 
</details>
```


<details> <summary><b>📄 SpaceManagementScreen.kt</b></summary>

```Kotlin

package mx.edu.utng.jdrj.disponibilidad.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.jdrj.disponibilidad.data.model.Espacio
import mx.edu.utng.jdrj.disponibilidad.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpaceManagementScreen(
    adminViewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    val espacios = adminViewModel.listaEspacios
    val isLoading = adminViewModel.isLoading

    // Estados para los diálogos
    var showAddEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Espacio seleccionado para editar o eliminar (null si es nuevo)
    var selectedEspacio by remember { mutableStateOf<Espacio?>(null) }

    // Campos del formulario
    var nombre by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var capacidad by remember { mutableStateOf("") }
    var planta by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    // Cargar espacios al entrar
    LaunchedEffect(Unit) {
        adminViewModel.cargarEspaciosAdmin()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Espacios", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A237E), // Azul Admin
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Preparar para AGREGAR nuevo
                    selectedEspacio = null
                    nombre = ""
                    tipo = ""
                    capacidad = ""
                    planta = ""
                    descripcion = ""
                    showAddEditDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Espacio")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(espacios) { espacio ->
                        EspacioAdminCard(
                            espacio = espacio,
                            onEdit = {
                                // Preparar para EDITAR
                                selectedEspacio = espacio
                                nombre = espacio.nombre
                                tipo = espacio.tipo
                                capacidad = espacio.capacidad.toString()
                                planta = espacio.planta
                                descripcion = espacio.descripcion
                                showAddEditDialog = true
                            },
                            onDelete = {
                                selectedEspacio = espacio
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }

        // --- DIÁLOGO AGREGAR / EDITAR ---
        if (showAddEditDialog) {
            AlertDialog(
                onDismissRequest = { showAddEditDialog = false },
                title = { Text(if (selectedEspacio == null) "Nuevo Espacio" else "Editar Espacio") },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre (Ej: Aula 1)") })
                        OutlinedTextField(value = tipo, onValueChange = { tipo = it }, label = { Text("Tipo (Ej: Aula, Laboratorio)") })
                        OutlinedTextField(
                            value = capacidad,
                            onValueChange = { if (it.all { char -> char.isDigit() }) capacidad = it },
                            label = { Text("Capacidad") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(value = planta, onValueChange = { planta = it }, label = { Text("Ubicación / Planta") })
                        OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, maxLines = 3)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (nombre.isNotEmpty() && capacidad.isNotEmpty()) {
                                val espacioFinal = Espacio(
                                    idEspacio = selectedEspacio?.idEspacio ?: "", // Si es nuevo, ID vacío (el repo lo genera)
                                    nombre = nombre,
                                    tipo = tipo,
                                    capacidad = capacidad.toIntOrNull() ?: 0,
                                    planta = planta,
                                    descripcion = descripcion
                                )

                                if (selectedEspacio == null) {
                                    adminViewModel.agregarEspacio(espacioFinal) { showAddEditDialog = false }
                                } else {
                                    adminViewModel.actualizarEspacio(espacioFinal) { showAddEditDialog = false }
                                }
                            }
                        }
                    ) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddEditDialog = false }) { Text("Cancelar") }
                }
            )
        }

        // --- DIÁLOGO ELIMINAR ---
        if (showDeleteDialog && selectedEspacio != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Eliminar Espacio") },
                text = { Text("¿Estás seguro de eliminar '${selectedEspacio!!.nombre}'? Esto no se puede deshacer.") },
                confirmButton = {
                    Button(
                        onClick = {
                            adminViewModel.eliminarEspacio(selectedEspacio!!.idEspacio)
                            showDeleteDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
                }
            )
        }
    }
}

@Composable
fun EspacioAdminCard(
    espacio: Espacio,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.MeetingRoom, null, tint = Color(0xFF1A237E), modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = espacio.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "${espacio.tipo} • Cap: ${espacio.capacidad}", fontSize = 12.sp, color = Color.Gray)
                    Text(text = espacio.planta, fontSize = 12.sp, color = Color.Gray)
                }
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, "Editar", tint = Color(0xFF1A237E))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
} 
</details>
```


<details> <summary><b>📄 StatisticsScreen.kt</b></summary>

```Kotlin

package mx.edu.utng.jdrj.disponibilidad.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.jdrj.disponibilidad.utils.Constants
import mx.edu.utng.jdrj.disponibilidad.viewmodel.StatisticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel,
    onNavigateBack: () -> Unit
) {
    val total = viewModel.totalReservas
    val topEspacio = viewModel.espacioMasSolicitado
    val horas = viewModel.horasTotalesReservadas
    val porEstado = viewModel.reservasPorEstado
    val isLoading = viewModel.isLoading

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reportes y Estadísticas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A237E), // Azul Admin
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // TARJETA PRINCIPAL: TOTALES
                    StatCard(
                        title = "Total de Solicitudes",
                        value = total.toString(),
                        icon = Icons.Default.BarChart,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(modifier = Modifier.fillMaxWidth()) {
                        // TARJETA: APROBADAS
                        val aprobadas = porEstado[Constants.ESTADO_APROBADA] ?: 0
                        StatSmallCard(
                            title = "Aprobadas",
                            value = aprobadas.toString(),
                            icon = Icons.Default.CheckCircle,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // TARJETA: RECHAZADAS
                        val rechazadas = porEstado[Constants.ESTADO_CANCELADA] ?: 0
                        StatSmallCard(
                            title = "Canceladas",
                            value = rechazadas.toString(),
                            icon = Icons.Default.Cancel,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // TARJETA DESTACADA: EL ESPACIO MÁS POPULAR
                    if (topEspacio != null) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Espacio Más Solicitado", fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(12.dp))
                                Icon(Icons.Default.EmojiEvents, null, tint = Color(0xFFFFC107), modifier = Modifier.size(48.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(topEspacio.first, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                                Text("${topEspacio.second} reservas", fontSize = 14.sp, color = Color.Gray)
                            }
                        }
                    }

                    // TARJETA: HORAS TOTALES
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Schedule, null, tint = Color(0xFF1565C0))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Uso Total del Edificio", fontSize = 14.sp, color = Color(0xFF1565C0), fontWeight = FontWeight.Bold)
                                Text("$horas Horas", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color(0xFF0D47A1))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, color: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(value, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }
            Icon(icon, null, tint = Color.White.copy(alpha = 0.9f), modifier = Modifier.size(48.dp))
        }
    }
}

@Composable
fun StatSmallCard(title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
            Text(title, fontSize = 12.sp, color = Color.Gray)
        }
    }
} 
</details>
```

<details> <summary><b>📄 UserManagementScreen.kt</b></summary>

```Kotlin

package mx.edu.utng.jdrj.disponibilidad.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.jdrj.disponibilidad.data.model.Usuario
import mx.edu.utng.jdrj.disponibilidad.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementScreen(
    adminViewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    val usuarios = adminViewModel.listaUsuarios
    val isLoading = adminViewModel.isLoading

    // Estado para el diálogo de borrar
    var mostrarDialogoBorrar by remember { mutableStateOf(false) }
    var usuarioABorrar by remember { mutableStateOf<Usuario?>(null) }

    // Cargar la lista de usuarios al entrar a la pantalla
    LaunchedEffect(Unit) {
        adminViewModel.cargarUsuarios()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Usuarios", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A237E), // Azul Oscuro Institucional (Admin)
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Ordenamos: Primero los Admins, luego por nombre alfabéticamente
                    val listaOrdenada = usuarios.sortedWith(
                        compareByDescending<Usuario> { it.rol == "admin" }.thenBy { it.nombre }
                    )

                    items(listaOrdenada) { usuario ->
                        UserItem(
                            usuario = usuario,
                            onToggleAprobado = { adminViewModel.alternarAprobacion(usuario) },
                            onHacerAdmin = { adminViewModel.hacerAdmin(usuario) },
                            onQuitarAdmin = { adminViewModel.quitarAdmin(usuario) },
                            onEliminar = {
                                usuarioABorrar = usuario
                                mostrarDialogoBorrar = true
                            }
                        )
                    }
                }
            }
        }

        // --- DIÁLOGO DE CONFIRMACIÓN DE ELIMINACIÓN ---
        if (mostrarDialogoBorrar && usuarioABorrar != null) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoBorrar = false },
                title = { Text("Eliminar Usuario") },
                text = { Text("¿Estás seguro de que quieres eliminar a ${usuarioABorrar!!.nombre}? Esta acción no se puede deshacer.") },
                confirmButton = {
                    Button(
                        onClick = {
                            adminViewModel.eliminarUsuario(usuarioABorrar!!)
                            mostrarDialogoBorrar = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoBorrar = false }) { Text("Cancelar") }
                }
            )
        }
    }
}

@Composable
fun UserItem(
    usuario: Usuario,
    onToggleAprobado: () -> Unit,
    onHacerAdmin: () -> Unit,
    onQuitarAdmin: () -> Unit,
    onEliminar: () -> Unit
) {
    val esAdmin = usuario.rol == "admin"

    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // INFO DEL USUARIO
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = if (esAdmin) Icons.Default.Security else Icons.Default.Person,
                        contentDescription = null,
                        tint = if (esAdmin) Color(0xFF1A237E) else Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "${usuario.nombre} ${usuario.apellido}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = if (esAdmin) Color(0xFF1A237E) else Color.Black
                        )
                        val tipoUsuario = if (esAdmin) "Administrador" else "Usuario Normal"
                        Text(
                            text = tipoUsuario,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "ID: ${usuario.idInstitucional}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                // SWITCH DE APROBACIÓN (ADMITIDO / PENDIENTE)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Switch(
                        checked = usuario.aprobado,
                        onCheckedChange = { onToggleAprobado() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF4CAF50),
                            checkedTrackColor = Color(0xFFE8F5E9)
                        )
                    )
                    Text(
                        text = if (usuario.aprobado) "ADMITIDO" else "PENDIENTE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (usuario.aprobado) Color(0xFF4CAF50) else Color.Red
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // BOTONES DE ROL Y ELIMINAR
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                }

                if (!esAdmin) {
                    TextButton(onClick = onHacerAdmin) {
                        Text("Hacer Admin", color = Color(0xFF1A237E), fontWeight = FontWeight.Bold)
                    }
                } else {
                    TextButton(onClick = onQuitarAdmin) {
                        Text("Quitar Admin", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
</details>
```

### 🎨 Theme (Estilos y Colores)

Definición de la paleta de colores institucional y el tema de la aplicación.

<details>
<summary><b>📄 Color.kt</b></summary>

```kotlin
package mx.edu.utng.jdrj.disponibilidad.ui.theme

import androidx.compose.ui.graphics.Color

// --- PALETA INSTITUCIONAL UTNG ---

val UTNGBluePrimary = Color(0xFF0057B8)
val UTNGGreenAccent = Color(0xFF009B4D)
val UTNGBlueLight = Color(0xFF00C2FF)

val FondoGrisNube = Color(0xFFF0F4F8)
val FondoLavandaNiebla = Color(0xFFF3F4F9)

val CardBackground = Color(0xFFFFFFFF) // Blanco puro para que resalte sobre la nube

// Textos
val TextPrimary = Color(0xFF37474F)
val TextSecondary = Color(0xFF78909C)

// Estados
val StatusRed = Color(0xFFD32F2F)
val StatusGreenLogo = UTNGGreenAccent

// Modo Oscuro (Adaptado)
val DarkBackground = Color(0xFF121212)
val DarkSurface = Color(0xFF1E1E1E)
val BluePrimaryDark = UTNGBlueLight
</details>
```

<details> <summary><b>📄 Theme.kt</b></summary>

```Kotlin

package mx.edu.utng.jdrj.disponibilidad.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimaryDark,
    secondary = UTNGGreenAccent,
    tertiary = UTNGBluePrimary,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurface,
    onPrimary = DarkBackground,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = UTNGBluePrimary,
    secondary = UTNGGreenAccent,
    tertiary = UTNGBlueLight,
    background = FondoGrisNube,

    // Tarjetas y Superficies
    surface = CardBackground,
    surfaceVariant = CardBackground,

    // Textos y contenido sobre colores
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextPrimary
)

@Composable
fun DisponibilidadTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color está disponible en Android 12+
    dynamicColor: Boolean = false, // Lo ponemos en false para forzar los colores UTNG
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Pintar la barra de estado del color primario (Azul UTNG)
            window.statusBarColor = colorScheme.primary.toArgb()
            // Controlar si los iconos de la barra son blancos o negros
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Usa la tipografía por defecto de Type.kt
        content = content
    )
} 
</details>
```

<details> <summary><b>📄 Type.kt</b></summary>

```Kotlin

package mx.edu.utng.jdrj.disponibilidad.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
</details>
```
### 🛠️ Utils

Clases de utilidad, constantes y servicios de notificación.

<details>
<summary><b>📄 AppNotificationManager.kt</b></summary>

```kotlin
package mx.edu.utng.jdrj.disponibilidad.utils

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.utng.jdrj.disponibilidad.data.model.Reserva
import mx.edu.utng.jdrj.disponibilidad.data.model.Usuario
import mx.edu.utng.jdrj.disponibilidad.data.repository.ReservasRepository

class AppNotificationManager(private val context: Context) {
    private val notificationService = NotificationService(context)
    private val reservasRepo = ReservasRepository()
    private val db = FirebaseFirestore.getInstance()

    private var esPrimeraCargaUsuario = true
    private var esPrimeraCargaAdmin = true

    // Variable para recordar el estado anterior y detectar cambios reales
    private var estadoAprobacionAnterior: Boolean? = null

    // --- 1. ESCUCHA DE RESERVAS (ALUMNO) ---
    fun iniciarEscuchaUsuario(usuario: Usuario) {
        if (usuario.rol == "admin") return

        reservasRepo.escucharCambiosMisReservas(usuario.idUsuario) { reserva, tipo ->
            if (esPrimeraCargaUsuario && tipo == DocumentChange.Type.ADDED) return@escucharCambiosMisReservas

            when (tipo) {
                DocumentChange.Type.MODIFIED -> {
                    if (reserva.estado == Constants.ESTADO_CANCELADA && reserva.motivoRechazo.isNotEmpty()) {
                        notificationService.mostrarNotificacion("❌ Reserva Rechazada", "Motivo: ${reserva.motivoRechazo}")
                    } else if (reserva.estado == Constants.ESTADO_APROBADA) {
                        notificationService.mostrarNotificacion("✅ Reserva Aprobada", "Tu solicitud para ${reserva.nombreEspacio} fue aceptada.")
                    }
                }
                DocumentChange.Type.ADDED -> {
                    if (reserva.estado == Constants.ESTADO_APROBADA) {
                        notificationService.mostrarNotificacion("📅 Nueva Asignación", "El administrador te asignó: ${reserva.nombreEspacio}")
                    }
                }
                DocumentChange.Type.REMOVED -> {
                    notificationService.mostrarNotificacion("🗑️ Reserva Eliminada", "Tu reserva en ${reserva.nombreEspacio} fue eliminada.")
                }
            }
        }

        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({ esPrimeraCargaUsuario = false }, 3000)
    }

    // --- 2. ESCUCHA DE SOLICITUDES (ADMIN) ---
    fun iniciarEscuchaAdmin(usuario: Usuario) {
        if (usuario.rol != "admin") return

        reservasRepo.escucharNuevasSolicitudes { nuevaReserva ->
            if (!esPrimeraCargaAdmin) {
                notificationService.mostrarNotificacion("🔔 Nueva Solicitud", "Pendiente: ${nuevaReserva.nombreEspacio}")
            }
        }
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({ esPrimeraCargaAdmin = false }, 3000)
    }

    // --- 3. ESCUCHA DE PERFIL (CORREGIDA) ---
    fun iniciarEscuchaPerfil(idUsuario: String) {
        Log.d("Notificaciones", "Iniciando escucha de perfil para: $idUsuario")

        db.collection(Constants.COLLECTION_USUARIOS).document(idUsuario)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("Notificaciones", "Error escuchando perfil", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val usuarioActualizado = snapshot.toObject(Usuario::class.java) ?: return@addSnapshotListener
                    val aprobadoActual = usuarioActualizado.aprobado

                    // Si es la primera vez que leemos, solo guardamos el estado inicial
                    if (estadoAprobacionAnterior == null) {
                        estadoAprobacionAnterior = aprobadoActual
                        return@addSnapshotListener
                    }

                    // Detectar CAMBIO DE ESTADO: De Falso a Verdadero
                    if (estadoAprobacionAnterior == false && aprobadoActual == true) {
                        Log.d("Notificaciones", "¡Usuario Aprobado! Lanzando notificación.")
                        notificationService.mostrarNotificacion(
                            "🎉 Cuenta Aprobada",
                            "¡Felicidades! El administrador ha aprobado tu acceso."
                        )
                    }
                    // Detectar CAMBIO DE ESTADO: De Verdadero a Falso (Bloqueo)
                    else if (estadoAprobacionAnterior == true && aprobadoActual == false) {
                        notificationService.mostrarNotificacion(
                            "⛔ Cuenta Suspendida",
                            "El administrador ha revocado tu acceso."
                        )
                    }

                    // Actualizamos el estado guardado
                    estadoAprobacionAnterior = aprobadoActual
                }
            }
    }

    fun notificarBienvenida() {
        notificationService.mostrarNotificacion("👋 ¡Bienvenido!", "Registro exitoso. Espera aprobación del administrador.")
    }
}
</details>
```

<details> <summary><b>📄 Constants.kt</b></summary>

Kotlin

package mx.edu.utng.jdrj.disponibilidad.utils

object Constants {
    const val COLLECTION_USUARIOS = "usuarios"
    const val COLLECTION_ESPACIOS = "espacios"
    const val COLLECTION_RESERVAS = "reservas"

    // --- NUEVAS TABLAS PARA CAMINO B ---
    const val COLLECTION_EQUIPAMIENTO = "equipamiento"                 // Catálogo de equipos
    const val COLLECTION_ESPACIO_EQUIPAMIENTO = "espacio_equipamiento" // Tabla intermedia
    const val COLLECTION_FAVORITOS = "espacio_favorito"                // Tabla de likes

    const val ESTADO_PENDIENTE = "pendiente"
    const val ESTADO_APROBADA = "aprobada"
    const val ESTADO_CANCELADA = "cancelada"
}
</details>

<details> <summary><b>📄 DateUtils.kt</b></summary>

Kotlin

package mx.edu.utng.jdrj.disponibilidad.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    private const val FORMATO_FECHA = "dd/MM/yyyy"
    private const val FORMATO_HORA = "HH:mm"

    // Obtener la fecha de hoy como String (ej: "24/11/2023")
    fun obtenerFechaActual(): String {
        val sdf = SimpleDateFormat(FORMATO_FECHA, Locale.getDefault())
        return sdf.format(Date())
    }

    // Validar si el texto tiene formato de fecha correcto
    fun esFechaValida(fecha: String): Boolean {
        return try {
            val sdf = SimpleDateFormat(FORMATO_FECHA, Locale.getDefault())
            sdf.isLenient = false // No permitir fechas como 32/01/2023
            sdf.parse(fecha)
            true
        } catch (e: Exception) {
            false
        }
    }

    // Validar formato de hora (ej: "14:30")
    fun esHoraValida(hora: String): Boolean {
        return try {
            val sdf = SimpleDateFormat(FORMATO_HORA, Locale.getDefault())
            sdf.isLenient = false
            sdf.parse(hora)
            true
        } catch (e: Exception) {
            false
        }
    }

    // Convertir una fecha String a objeto Calendar (útil para comparaciones)
    fun stringACalendar(fecha: String): Calendar? {
        return try {
            val sdf = SimpleDateFormat(FORMATO_FECHA, Locale.getDefault())
            val date = sdf.parse(fecha)
            Calendar.getInstance().apply {
                if (date != null) {
                    time = date
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}
</details>
```

<details> <summary><b>📄 NotificationService.kt</b></summary>

Kotlin

package mx.edu.utng.jdrj.disponibilidad.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import mx.edu.utng.jdrj.disponibilidad.MainActivity

class NotificationService(private val context: Context) {

    private val CHANNEL_ID = "reservas_channel"
    private val CHANNEL_NAME = "Notificaciones de Reservas"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Avisos sobre el estado de las reservas"
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun mostrarNotificacion(titulo: String, mensaje: String) {
        // Verificar permisos (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }

        // --- NUEVO: INTENT PARA ABRIR LA APP ---
        val intent = Intent(context, MainActivity::class.java).apply {
            // Esto asegura que si la app ya está abierta, no cree una copia, sino que la traiga al frente
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE // Requerido en versiones nuevas de Android
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) // <--- Aquí conectamos el click
            .setAutoCancel(true) // La notificación desaparece al tocarla

        val notificationId = System.currentTimeMillis().toInt()
        NotificationManagerCompat.from(context).notify(notificationId, builder.build())
    }
}
</details>
```


