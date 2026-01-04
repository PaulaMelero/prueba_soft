package es.unizar.eina.GM122_quads.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.unizar.eina.GM122_quads.R;
import es.unizar.eina.GM122_quads.database.Reserva;

import static androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;

/**
 * Lista de reservas.
 * Permite crear, editar y eliminar reservas.
 */
public class GM122_ReservasList extends AppCompatActivity {

    private ReservaViewModel mReservaViewModel;
    private ReservaListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gm122_reserva);

        /* =========================
           RECYCLERVIEW
           ========================= */

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        mAdapter = new ReservaListAdapter(
                new ReservaListAdapter.ReservaDiff(),
                new ReservaListAdapter.OnReservaActionListener() {

                    public void onClick(Reserva reserva) {
                        editReserva(reserva);
                    }

                    @Override
                    public void onEdit(Reserva reserva) {
                        editReserva(reserva);
                    }

                    @Override
                    public void onDelete(Reserva reserva) {
                        showDeleteDialog(reserva);
                    }
                }
        );

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /* =========================
           VIEWMODEL
           ========================= */

        mReservaViewModel = new ViewModelProvider(this).get(ReservaViewModel.class);
        mReservaViewModel.getAllReservas().observe(this,
                reservas -> mAdapter.submitList(reservas)
        );

        /* =========================
           FAB – CREAR RESERVA
           ========================= */

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> createReserva());
    }

    /* =========================
       CREAR RESERVA
       ========================= */

    private void createReserva() {
        mStartCreateReserva.launch(new Intent(this, ReservaModify.class));
    }

    private final ActivityResultLauncher<Intent> mStartCreateReserva =
            registerForActivityResult(
                    new StartActivityForResult(),
                    result -> {
                        // No hacemos nada:
                        // ReservaModify ya guarda y LiveData refresca la lista
                    }
            );

    /* =========================
       EDITAR RESERVA
       ========================= */

    private void editReserva(Reserva reserva) {
        Intent intent = new Intent(this, ReservaModify.class);
        intent.putExtra(ReservaModify.RESERVA_ID, reserva.getId());
        intent.putExtra(ReservaModify.RESERVA_NOMBRE, reserva.getNombreCliente());
        intent.putExtra(ReservaModify.RESERVA_MOVIL, reserva.getMovilCliente());
        intent.putExtra(ReservaModify.RESERVA_FECHA_RECOGIDA, reserva.getFechaRecogida());
        intent.putExtra(ReservaModify.RESERVA_FECHA_DEVOLUCION, reserva.getFechaDevolucion());
        mStartUpdateReserva.launch(intent);
    }

    private final ActivityResultLauncher<Intent> mStartUpdateReserva =
            registerForActivityResult(
                    new StartActivityForResult(),
                    result -> {
                        // Tampoco hacemos nada aquí
                        // La actualización ya está hecha en ReservaModify
                    }
            );

    /* =========================
       ELIMINAR RESERVA
       ========================= */

    private void showDeleteDialog(Reserva reserva) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_reserva)
                .setMessage(R.string.confirm_delete_reserva)
                .setPositiveButton(
                        R.string.delete,
                        (dialog, which) -> mReservaViewModel.delete(reserva)
                )
                .setNegativeButton(R.string.button_cancel, null)
                .show();
    }
}
