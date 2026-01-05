package es.unizar.eina.GM122_quads.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.unizar.eina.GM122_quads.database.ReservaQuadCascos;
import es.unizar.eina.GM122_quads.database.ReservaQuadCascosRepository;

/**
 * ViewModel para gestionar la relación Reserva–Quad.
 * Actúa como intermediario entre la UI y ReservaQuadRepository.
 *
 * @author GM122
 */
public class ReservaQuadCascosViewModel extends AndroidViewModel {

    private final ReservaQuadCascosRepository mRepository;

    public ReservaQuadCascosViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ReservaQuadCascosRepository(application);
    }

    /**
     * Inserta múltiples relaciones reserva–quad.
     */
    public void insertAll(int reservaId, @NonNull Map<String, Integer> datos) {
        mRepository.insertAll(reservaId, datos);
    }

    /**
     * Elimina todas las relaciones asociadas a una reserva.
     */
    public boolean deleteByReserva(int reservaId) {
        return mRepository.deleteByReserva(reservaId);
    }

    public boolean updateCascos(int reservaId, Map<String, Integer> nuevos) {
        return mRepository.updateCascos(reservaId, nuevos);
    }

    /**
     * Devuelve las matrículas de los quads asociados a una reserva.
     * Se usa en modo EDICIÓN para marcar checkboxes.
     *
     * @param reservaId id de la reserva
     */
    public LiveData<Map<String, Integer>> getByReserva(int reservaId) {
        return mRepository.getByReserva(reservaId);
    }


    public Map<String, Double> getPreciosParaReserva(int id, Map<String, Integer> cascosPorQuad) {
        return mRepository.getPreciosParaReserva(id, cascosPorQuad);
    }

    public void getPreciosParaReservaAsync(int reservaId,
                                           Map<String,Integer> seleccion,
                                           java.util.function.Consumer<Map<String,Double>> cb) {
        mRepository.getPreciosParaReservaAsync(reservaId, seleccion, cb);
    }

}

