package es.unizar.eina.GM122_quads.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
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

    public void updateReservaCascos(int reservaId, @NonNull Map<String, Integer> datos) {

    }
    /**
     * Elimina todas las relaciones asociadas a una reserva.
     */
    public boolean deleteByReserva(int reservaId) {
        return mRepository.deleteByReserva(reservaId);
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

}

