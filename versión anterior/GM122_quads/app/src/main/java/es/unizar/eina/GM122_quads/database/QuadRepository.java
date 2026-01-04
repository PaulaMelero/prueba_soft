package es.unizar.eina.GM122_quads.database;

import static es.unizar.eina.GM122_quads.database.Quad_Reserva_RoomDataBase.databaseWriteExecutor;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * Repositorio que gestiona el acceso a los datos de Quad.
 * Actúa como única puerta de entrada a Room desde la UI/ViewModel.
 */
public class QuadRepository {

    private final QuadDao mQuadDao;
    private final LiveData<List<Quad>> mAllQuads;

    /**
     * Constructor del repositorio.
     * Inicializa la base de datos y el DAO.
     */
    public QuadRepository(Application application) {
        Quad_Reserva_RoomDataBase db =
                Quad_Reserva_RoomDataBase.getDatabase(application);
        mQuadDao = db.quadDao();
        mAllQuads = mQuadDao.getOrderedQuads();
    }

    /**
     * Devuelve la lista observable de todos los quads.
     * Room se encarga de ejecutar la consulta en background.
     */
    public LiveData<List<Quad>> getAllQuads() {
        return mAllQuads;
    }

    /**
     * Inserta un nuevo quad en la base de datos.
     */
    public void insert(Quad quad) {
        databaseWriteExecutor.execute(() ->
                mQuadDao.insert(quad)
        );
    }

    /**
     * Actualiza un quad existente.
     */
    public void update(Quad quad) {
        databaseWriteExecutor.execute(() ->
                mQuadDao.update(quad)
        );
    }

    /**
     * Elimina un quad por su matrícula (clave primaria).
     */
    public void deleteByMatricula(String matricula) {
        databaseWriteExecutor.execute(() ->
                mQuadDao.deleteByMatricula(matricula)
        );
    }

    public LiveData<List<Quad>> getAvailableQuads(long inicio, long fin) {
        return mQuadDao.getAvailableQuads(inicio, fin);
    }

    public Quad getQuadByMatriculaSync(String matricula) {
        try {
            return databaseWriteExecutor.submit(
                    () -> mQuadDao.getQuadByMatricula(matricula)
            ).get();
        } catch (Exception e) {
            return null;
        }
    }


}
