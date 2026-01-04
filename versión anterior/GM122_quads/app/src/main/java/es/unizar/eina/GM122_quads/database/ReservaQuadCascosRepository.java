package es.unizar.eina.GM122_quads.database;

import static es.unizar.eina.GM122_quads.database.Quad_Reserva_RoomDataBase.databaseWriteExecutor;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Repositorio para gestionar la relación Reserva–Quad.
 * Encapsula el acceso a ReservaQuadDao.
 */
public class ReservaQuadCascosRepository {

    private final ReservaQuadCascosDao mReservaQuadCascosDao;
    private final QuadDao mQuadDao;

    public ReservaQuadCascosRepository(Application application) {
        Quad_Reserva_RoomDataBase db =
                Quad_Reserva_RoomDataBase.getDatabase(application);
        mReservaQuadCascosDao = db.reservaQuadCascosDao();
        mQuadDao = db.quadDao();
    }

    /* =========================================================
       INSERT + VALIDACIÓN
       ========================================================= */

    public void insertAll(int reservaId, Map<String, Integer> cascosPorQuad) {
        databaseWriteExecutor.execute(() -> {
            List<ReservaQuadCascos> rows = new ArrayList<>();

            for (Map.Entry<String, Integer> entry : cascosPorQuad.entrySet()) {
                rows.add(new ReservaQuadCascos(
                        reservaId,
                        entry.getKey(),
                        entry.getValue()
                ));
            }

            mReservaQuadCascosDao.insertAll(rows);
        });
    }


    /* =========================================================
       CONSULTAS
       ========================================================= */
    public LiveData<Map<String, Integer>> getByReserva(int reservaId) {
        return Transformations.map(
                mReservaQuadCascosDao.getByReserva(reservaId),
                lista -> {
                    Map<String, Integer> map = new HashMap<>();
                    for (ReservaQuadCascos rqc : lista) {
                        map.put(rqc.getMatriculaQuad(), rqc.getNumCascos());
                    }
                    return map;
                }
        );
    }


    /* =========================================================
       VALIDACIÓN CASCOS
       ========================================================= */
    private boolean numCascosValido(Quad quad, int numCascos) {

        if (numCascos < 0) return false;

        // true = biplaza
        if (quad.getTipo()) {
            return numCascos <= 2;
        } else {
            return numCascos <= 1;
        }
    }

    /* =========================================================
       DELETE
       ========================================================= */

    public boolean deleteByReserva(int reservaId) {
        Future<?> future = databaseWriteExecutor.submit(
                () -> mReservaQuadCascosDao.deleteByReserva(reservaId)
        );
        try {
            long TIMEOUT = 15000;
            future.get(TIMEOUT, TimeUnit.MILLISECONDS);
            return true;
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ReservaQuadCascosRepository",
                    ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return false;
        }
    }

}
