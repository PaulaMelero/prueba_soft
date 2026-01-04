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

    @Query("SELECT * FROM reserva_quad_cascos WHERE reservaId = :reservaId")
    LiveData<List<ReservaQuadCascos>> getByReserva(int reservaId);

}

