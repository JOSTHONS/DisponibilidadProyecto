package mx.edu.utng.jdrj.disponibilidad.utils

import android.content.Context
import com.google.firebase.firestore.DocumentChange
import mx.edu.utng.jdrj.disponibilidad.data.model.Reserva
import mx.edu.utng.jdrj.disponibilidad.data.model.Usuario
import mx.edu.utng.jdrj.disponibilidad.data.repository.ReservasRepository

class AppNotificationManager(context: Context) {
    private val notificationService = NotificationService(context)
    private val reservasRepo = ReservasRepository()

    // Banderas para evitar notificaciones masivas al abrir la app
    private var esCargaInicialUsuario = true
    private var esCargaInicialAdmin = true

    // --- ESCUCHA PARA ALUMNOS ---
    // Notifica:
    // 1. MODIFICACIONES: Aprobación o Rechazo.
    // 2. ADICIONES: Asignación directa por el Admin.
    // 3. ELIMINACIONES: Si el admin borra la reserva.
    fun iniciarEscuchaUsuario(usuario: Usuario) {
        // El admin no necesita ver sus propias reservas aquí
        if (usuario.rol == "admin") return

        reservasRepo.escucharCambiosMisReservas(usuario.idUsuario) { reserva, tipoCambio ->
            // Ignoramos la primera carga de datos (cuando se abre la app y descarga todo lo viejo)
            if (esCargaInicialUsuario) {
                // Un pequeño truco: asumimos que la carga inicial son puros ADDED.
                // Después de unos segundos o tras el primer batch, ya escuchamos cambios reales.
                // (En una implementación simple, esto puede requerir un delay o bandera más robusta,
                // pero para este proyecto escolar, asumiremos que la carga inicial pasa rápido).
                // Una mejora sería usar un contador o un timer, pero por ahora lo dejamos simple:
                // Solo ignoramos si son ADDED masivos al inicio.
            }

            // Para evitar spam al inicio, una técnica común es ignorar eventos por unos segundos.
            // Aquí simplemente procesaremos todo, pero ten en cuenta que al abrir la app
            // podrían llegar notificaciones viejas si no se maneja el estado local.
            // Para simplificar, asumimos que el usuario quiere saber qué pasó mientras no estaba.

            when (tipoCambio) {
                DocumentChange.Type.ADDED -> {
                    // NUEVA RESERVA AGREGADA
                    // Si el estado es APROBADA, significa que el Admin te la asignó directamente.
                    if (reserva.estado == Constants.ESTADO_APROBADA && !esCargaInicialUsuario) {
                        notificationService.mostrarNotificacion(
                            "📅 Nueva Asignación",
                            "El administrador te ha asignado: ${reserva.nombreEspacio}"
                        )
                    }
                }
                DocumentChange.Type.MODIFIED -> {
                    // CAMBIO DE ESTADO (Aprobada / Rechazada)
                    if (reserva.estado == Constants.ESTADO_CANCELADA && reserva.motivoRechazo.isNotEmpty()) {
                        notificationService.mostrarNotificacion(
                            "❌ Reserva Rechazada",
                            "Motivo: ${reserva.motivoRechazo}"
                        )
                    } else if (reserva.estado == Constants.ESTADO_APROBADA) {
                        notificationService.mostrarNotificacion(
                            "✅ Reserva Aprobada",
                            "Tu solicitud para ${reserva.nombreEspacio} fue aceptada."
                        )
                    }
                }
                DocumentChange.Type.REMOVED -> {
                    // RESERVA ELIMINADA
                    // Si el admin borra la reserva, avisamos.
                    notificationService.mostrarNotificacion(
                        "🗑️ Reserva Eliminada",
                        "Tu reserva en ${reserva.nombreEspacio} ha sido eliminada por el administrador."
                    )
                }
            }
        }

        // Desactivamos la bandera de carga inicial después de un momento breve
        // (Esto es un parche simple para evitar que suene todo al abrir la app)
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            esCargaInicialUsuario = false
        }, 2000)
    }

    // --- ESCUCHA PARA EL ADMIN ---
    // Notifica si llegan nuevas solicitudes pendientes
    fun iniciarEscuchaAdmin(usuario: Usuario) {
        if (usuario.rol != "admin") return

        reservasRepo.escucharNuevasSolicitudes { nuevaReserva ->
            // Ignoramos la carga inicial
            if (esCargaInicialAdmin) {
                return@escucharNuevasSolicitudes
            }

            notificationService.mostrarNotificacion(
                "🔔 Nueva Solicitud",
                "Solicitud pendiente para: ${nuevaReserva.nombreEspacio}"
            )
        }

        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            esCargaInicialAdmin = false
        }, 2000)
    }
}