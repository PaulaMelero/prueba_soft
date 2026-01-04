package es.unizar.eina.GM122_quads.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReservaQuadCascosDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(ReservaQuadCascos reservaQuadCascos);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertAll(List<ReservaQuadCascos> reservaQuadCascos);

    @Query("DELETE FROM reserva_quad_cascos WHERE reservaId = :reservaId")
    void deleteByReserva(int reservaId);

    @Query("DELETE FROM reserva_quad_cascos WHERE reservaId = :reservaId AND matriculaQuad = :matricula")
    void delete(int reservaId, String matricula);

    // PARA OBSERVAR (UI)
    @Query("SELECT * FROM reserva_quad_cascos WHERE reservaId = :reservaId")
    LiveData<List<ReservaQuadCascos>> getByReservaLive(int reservaId);

    // PARA LÓGICA INTERNA (background)
    @Query("SELECT * FROM reserva_quad_cascos WHERE reservaId = :reservaId")
    List<ReservaQuadCascos> getByReservaSync(int reservaId);


    @Query("UPDATE reserva_quad_cascos SET numCascos = :numCascos WHERE reservaId = :reservaId AND matriculaQuad = :matricula")
    void updateNumCascos(int reservaId, String matricula, int numCascos);

    @Query("SELECT SUM(precioOriginal) FROM reserva_quad_cascos WHERE reservaId = :reservaId")
    double getPrecioDiarioReserva(int reservaId);

}

