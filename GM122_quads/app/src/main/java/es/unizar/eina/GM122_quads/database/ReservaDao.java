package es.unizar.eina.GM122_quads.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object (DAO) para acceder a la tabla Reserva.
 * Define las operaciones básicas de inserción, actualización, borrado
 * y la consulta para obtener todos las reservas ordenadas.
 */
@Dao
public interface ReservaDao {

    /**
     * Inserta una reserva en la base de datos.
     * Si ya existe la misma matrícula se ignora.   ????????????????????????????????
     * @param reserva reserva a insertar
     * @return id de fila insertada o -1 si se ha ignorado por conflicto    ?????????????
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Reserva reserva);

    @Insert
    long insertAndReturnId(Reserva reserva);

    /**
     * Actualiza los datos de una reserva existente.
     * @param reserva reserva modificada
     * @return número de filas afectadas
     */
    @Update
    int update(Reserva reserva);

    /**
     * Elimina un reserva de la base de datos.
     * @param reserva reserva a eliminar
     * @return número de filas afectadas
     */
    @Delete
    int delete(Reserva reserva);

    /**
     * Elimina todos los reservas de la tabla.
     */
    @Query("DELETE FROM reserva")
    void deleteAll();

    @Query("UPDATE reserva SET precioTotal = :precio WHERE id = :reservaId ")
    void updatePrecio(int reservaId, double precio);


    /**
     * Recupera todos las reservas ordenadas por nombreCliente.
     * @return lista observable (LiveData) de reservas
     */

    @Query("SELECT COUNT(*) FROM reserva r INNER JOIN reserva_quad_cascos rq ON rq.reservaId = r.id WHERE rq.matriculaQuad = :matricula AND r.fechaRecogida <= :fechaFin AND r.fechaDevolucion >= :fechaInicio")
    int countOverlappingReservationsForQuad(String matricula, long fechaInicio, long fechaFin);

    /**
     * Recupera todos las reservas ordenadas por nombreCliente.
     * @return lista observable (LiveData) de reservas
     */
    @Query("SELECT * FROM reserva ORDER BY nombreCliente ASC")
    LiveData<List<Reserva>> getReservasOrderByNombre();

    /**
     * Recupera todos las reservas ordenadas por telefono.
     * @return lista observable (LiveData) de reservas
     */
    @Query("SELECT * FROM reserva ORDER BY movilCliente ASC")
    LiveData<List<Reserva>> getReservasOrderByTelefono();

    /**
     * Recupera todos las reservas ordenadas por fecha de recogida.
     * @return lista observable (LiveData) de reservas
     */
    @Query("SELECT * FROM reserva ORDER BY fechaRecogida ASC")
    LiveData<List<Reserva>> getReservasOrderByFechaRecogida();

    /**
     * Recupera todos las reservas ordenadas por fecha de devolucion.
     * @return lista observable (LiveData) de reservas
     */
    @Query("SELECT * FROM reserva ORDER BY fechaDevolucion ASC")
    LiveData<List<Reserva>> getReservasOrderByFechaDevolucion();

}

