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
import java.util.concurrent.atomic.AtomicBoolean;

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
                rows.add(
                        new ReservaQuadCascos(reservaId, entry.getKey(),
                                entry.getValue(), getPrecioActual(entry.getKey())
                ));
            }

            mReservaQuadCascosDao.insertAll(rows);
        });
    }


    /* =========================================================
       CONSULTAS
       =========================================================*/
    public LiveData<Map<String, Integer>> getByReserva(int reservaId) {
        return Transformations.map(
                mReservaQuadCascosDao.getByReservaLive(reservaId),
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

    public boolean updateCascos(int reservaId, Map<String, Integer> nuevos) {

        AtomicBoolean cambios = new AtomicBoolean(false);
        databaseWriteExecutor.execute(() -> {

            // obtengo actuales
            List<ReservaQuadCascos> actualesList =
                    mReservaQuadCascosDao.getByReservaSync(reservaId);

            Map<String, ReservaQuadCascos> actuales = new HashMap<>();
            for (ReservaQuadCascos rqc : actualesList) {
                actuales.put(rqc.getMatriculaQuad(), rqc);
            }

            // insertar o actualizar
            for (Map.Entry<String, Integer> entry : nuevos.entrySet()) {

                String matricula = entry.getKey();
                int nuevosCascos = entry.getValue();

                if (!actuales.containsKey(matricula)) {

                    // NO EXISTE -> insertar con precio actual
                    mReservaQuadCascosDao.insert(
                            new ReservaQuadCascos(reservaId, matricula,
                                    nuevosCascos, getPrecioActual(matricula)));
                    cambios.set(true);

                } else {

                    // EXISTE -> comprobar si cambian los cascos
                    ReservaQuadCascos actual = actuales.get(matricula);

                    if (actual.getNumCascos() != nuevosCascos) {
                        // actualizar SOLO el número de cascos
                        mReservaQuadCascosDao.updateNumCascos(reservaId, matricula, nuevosCascos);
                    }
                    // si son iguales -> NO SE TOCA NADA

                }

            }

            // eliminar los que ya no están
            for (String matricula : actuales.keySet()) {
                if (!nuevos.containsKey(matricula)) {
                    mReservaQuadCascosDao.delete(reservaId, matricula);
                    cambios.set(true);
                }
            }
        });

        return cambios.get();
    }

    /**
     * Devuelve el precio actual de un quad.
     * @param matricula
     * @return
     */
    private double getPrecioActual(String matricula) {
        Quad q = mQuadDao.getQuadByMatricula(matricula);
        if (q == null) {
            return -1;
        } else {
            return q.getPrecio();
        }
    }

    public Map<String, Double> getPreciosParaReserva(int reservaId, Map<String, Integer> seleccion) {

        Map<String, Double> precios = new HashMap<>();

        List<ReservaQuadCascos> existentes =
                mReservaQuadCascosDao.getByReservaSync(reservaId);

        Map<String, ReservaQuadCascos> congelados = new HashMap<>();
        for (ReservaQuadCascos rqc : existentes) {
            congelados.put(rqc.getMatriculaQuad(), rqc);
        }

        for (String matricula : seleccion.keySet()) {

            if (congelados.containsKey(matricula)) {
                // precio congelado
                precios.put(
                        matricula,
                        congelados.get(matricula).getPrecioOriginal()
                );
            } else {
                // quad nuevo -> precio actual
                precios.put(
                        matricula,
                        mQuadDao.getQuadByMatricula(matricula).getPrecio()
                );
            }
        }

        return precios;
    }

    public void getPreciosParaReservaAsync(int reservaId,
                                           Map<String,Integer> seleccion,
                                           java.util.function.Consumer<Map<String,Double>> cb) {

        databaseWriteExecutor.execute(() -> {
            Map<String, Double> precios = getPreciosParaReserva(reservaId, seleccion);
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> cb.accept(precios));
        });
    }


}
