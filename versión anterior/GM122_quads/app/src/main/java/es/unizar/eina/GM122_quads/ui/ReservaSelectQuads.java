package es.unizar.eina.GM122_quads.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.unizar.eina.GM122_quads.R;
import es.unizar.eina.GM122_quads.database.Quad;

public class ReservaSelectQuads extends AppCompatActivity {

    private QuadViewModel mQuadViewModel;
    private ReservaQuadCascosViewModel mReservaQuadViewModel;
    private ReservaSelectQuadsAdapter mAdapter;

    private long fechaInicio;
    private long fechaFin;
    private RecyclerView mRecyclerView;
    private Button mConfirmButton;
    private android.widget.TextView mEmptyText;
    private boolean isEditMode = false;
    private boolean fechasModificadas = false;
    private int reservaId = -1;
    private Map<String, Integer> cascosReserva = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_select_quads);

        fechaInicio = getIntent().getLongExtra(
                ReservaModify.RESERVA_FECHA_RECOGIDA, -1
        );
        fechaFin = getIntent().getLongExtra(
                ReservaModify.RESERVA_FECHA_DEVOLUCION, -1
        );

        if (fechaInicio == -1 || fechaFin == -1) {
            Toast.makeText(this, "Fechas inválidas", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        fechasModificadas =
                getIntent().getBooleanExtra("FECHAS_MODIFICADAS", false);

        isEditMode = getIntent().hasExtra(ReservaModify.RESERVA_ID);
        if (isEditMode) {
            reservaId = getIntent().getIntExtra(ReservaModify.RESERVA_ID, -1);
        }

        mReservaQuadViewModel =
                new ViewModelProvider(this).get(ReservaQuadCascosViewModel.class);

        setupRecyclerView();
        setupViewModel();

        findViewById(R.id.button_confirm).setOnClickListener(v -> confirmSelection());
    }
    private void setupRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerview_quads);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ReservaSelectQuadsAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mEmptyText = findViewById(R.id.text_no_quads);
    }

    private void setupViewModel() {

        mQuadViewModel = new ViewModelProvider(this).get(QuadViewModel.class);

        // Si editamos y NO han cambiado fechas -> cargar cascos
        if (isEditMode && !fechasModificadas) {
            mReservaQuadViewModel
                    .getByReserva(reservaId)
                    .observe(this, cascos -> {
                        Log.d("EDIT_RESERVA", "Cascos recibidos = " + cascos);
                        cascosReserva.clear();
                        if (cascos != null) {
                            cascosReserva.putAll(cascos);
                        }
                    });
        } else {
            // Si fechas cambiaron o no editamos -> empezar limpio
            cascosReserva.clear();
        }

        // Cargar quads disponibles
        mQuadViewModel
                .getAvailableQuads(fechaInicio, fechaFin)
                .observe(this, quadsDisponibles -> {

                    if (quadsDisponibles == null) {
                        quadsDisponibles = new ArrayList<>();
                    }

                    // Añadir quads antiguos si editamos y NO cambiaron fechas
                    if (isEditMode && !fechasModificadas && !cascosReserva.isEmpty()) {

                        for (String matricula : cascosReserva.keySet()) {
                            boolean yaEsta = false;

                            for (Quad q : quadsDisponibles) {
                                if (q.getMatricula().equals(matricula)) {
                                    yaEsta = true;
                                    break;
                                }
                            }

                            if (!yaEsta) {
                                Quad quad =
                                        mQuadViewModel.getQuadByMatriculaSync(matricula);
                                if (quad != null) {
                                    quadsDisponibles.add(quad);
                                }
                            }
                        }
                    }

                    // UI
                    if (quadsDisponibles.isEmpty()) {
                        mRecyclerView.setVisibility(View.GONE);
                        mEmptyText.setVisibility(View.VISIBLE);
                    } else {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mEmptyText.setVisibility(View.GONE);

                        mAdapter.submitList(quadsDisponibles);

                        if (!cascosReserva.isEmpty()) {
                            mAdapter.setInitialData(cascosReserva);
                        }
                    }
                });
    }


    private void confirmSelection() {
        Map<String, Integer> seleccion = mAdapter.getCascosPorQuad();

        if (seleccion == null || seleccion.isEmpty()) {
            Toast.makeText(this,
                    "Selecciona al menos un quad",
                    Toast.LENGTH_LONG).show();
            return;
        }

        Intent result = new Intent();
        result.putExtra(
                ReservaModify.RESERVA_QUADS_CASCOS,
                new HashMap<>(seleccion)
        );

        setResult(RESULT_OK, result);
        finish();

    }
}
