package es.unizar.eina.GM122_quads.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import es.unizar.eina.GM122_quads.R;
import es.unizar.eina.GM122_quads.database.Reserva;

/**
 * Activity única para CREAR o EDITAR una reserva.
 */
public class ReservaModify extends AppCompatActivity {

    /* =========================
       EXTRAS
       ========================= */

    public static final String RESERVA_ID = "reserva_id";
    public static final String RESERVA_NOMBRE = "nombre_cliente";
    public static final String RESERVA_MOVIL = "movil_cliente";
    public static final String RESERVA_FECHA_RECOGIDA = "fecha_recogida";
    public static final String RESERVA_FECHA_DEVOLUCION = "fecha_devolucion";
    public static final String RESERVA_QUADS_CASCOS = "reserva_quads_cascos";

    /* =========================
       VISTAS
       ========================= */

    private EditText mNombre;
    private EditText mMovil;
    private TextView mFechaRecogida;
    private TextView mFechaDevolucion;
    private Button mContinueButton;
    private Button mCancelButton;
    private TextView mTitle;
    private TextView mReservaId;
    private View mLayoutReservaId;

    /* =========================
       ESTADO
       ========================= */

    private boolean isEditMode = false;
    private int reservaId = -1;

    private long fechaInicioMillis = -1;
    private long fechaFinMillis = -1;

    private long fechaInicioOriginal;
    private long fechaFinOriginal;

    private ReservaViewModel mReservaViewModel;
    private ReservaQuadCascosViewModel mReservaQuadCascosViewModel;


    /* =========================
       CICLO DE VIDA
       ========================= */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_form);

        bindViews();

        mReservaViewModel = new ViewModelProvider(this).get(ReservaViewModel.class);
        mReservaQuadCascosViewModel =
                new ViewModelProvider(this).get(ReservaQuadCascosViewModel.class);

        determineMode();
        configureUiForMode();
        preloadDataIfEditing();

        mFechaRecogida.setOnClickListener(v -> pickDate(true));
        mFechaDevolucion.setOnClickListener(v -> pickDate(false));

        mContinueButton.setOnClickListener(v -> continueReserva());
        mCancelButton.setOnClickListener(v -> cancel());
    }

    /* =========================
       INIT
       ========================= */

    private void bindViews() {
        mNombre = findViewById(R.id.nombre_cliente);
        mMovil = findViewById(R.id.movil_cliente);
        mFechaRecogida = findViewById(R.id.reserva_fecha_recogida);
        mFechaDevolucion = findViewById(R.id.reserva_fecha_devolucion);
        mContinueButton = findViewById(R.id.button_continue);
        mCancelButton = findViewById(R.id.button_cancel);
        mTitle = findViewById(R.id.title_create_reserva);
        mLayoutReservaId = findViewById(R.id.layout_reserva_id);
        mReservaId = findViewById(R.id.text_reserva_id);
    }

    private void determineMode() {
        isEditMode = getIntent().hasExtra(RESERVA_ID);
        if (isEditMode) {
            reservaId = getIntent().getIntExtra(RESERVA_ID, -1);
        }
    }

    private void configureUiForMode() {
        if (isEditMode) {
            mTitle.setText(R.string.edit_reserva);
            mLayoutReservaId.setVisibility(View.VISIBLE);
            mReservaId.setText("#" + reservaId);
        } else {
            mTitle.setText(R.string.create_reserva);
            mLayoutReservaId.setVisibility(View.GONE);
        }
    }

    private void preloadDataIfEditing() {
        if (!isEditMode) return;

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        mNombre.setText(extras.getString(RESERVA_NOMBRE));
        mMovil.setText(extras.getString(RESERVA_MOVIL));

        fechaInicioMillis = extras.getLong(RESERVA_FECHA_RECOGIDA);
        fechaFinMillis = extras.getLong(RESERVA_FECHA_DEVOLUCION);

        fechaInicioOriginal = fechaInicioMillis;
        fechaFinOriginal = fechaFinMillis;

        mFechaRecogida.setText(formatDate(fechaInicioMillis));
        mFechaDevolucion.setText(formatDate(fechaFinMillis));
    }

    /* =========================
       FECHAS
       ========================= */

    private void pickDate(boolean inicio) {
        Calendar cal = Calendar.getInstance();

        new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    Calendar c = Calendar.getInstance();
                    c.set(year, month, day, 0, 0, 0);
                    c.set(Calendar.MILLISECOND, 0);

                    if (inicio) {
                        fechaInicioMillis = c.getTimeInMillis();
                        mFechaRecogida.setText(formatDate(fechaInicioMillis));
                    } else {
                        fechaFinMillis = c.getTimeInMillis();
                        mFechaDevolucion.setText(formatDate(fechaFinMillis));
                    }
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private String formatDate(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return c.get(Calendar.DAY_OF_MONTH) + "/"
                + (c.get(Calendar.MONTH) + 1) + "/"
                + c.get(Calendar.YEAR);
    }

    /* =========================
       FLUJO
       ========================= */

    private void continueReserva() {
        if (!isFormValid()) {
            Toast.makeText(this, R.string.empty_not_saved, Toast.LENGTH_LONG).show();
            return;
        }

        boolean fechasModificadas =
                fechaInicioMillis != fechaInicioOriginal ||
                        fechaFinMillis != fechaFinOriginal;

        Intent intent = new Intent(this, ReservaSelectQuads.class);
        intent.putExtra("FECHAS_MODIFICADAS", fechasModificadas);
        intent.putExtra(RESERVA_FECHA_RECOGIDA, fechaInicioMillis);
        intent.putExtra(RESERVA_FECHA_DEVOLUCION, fechaFinMillis);

        if (isEditMode) {
            intent.putExtra(RESERVA_ID, reservaId);
        }

        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != 100 || resultCode != RESULT_OK || data == null) return;

        HashMap<String, Integer> cascosPorQuad =
                (HashMap<String, Integer>) data.getSerializableExtra(RESERVA_QUADS_CASCOS);

        if (cascosPorQuad == null || cascosPorQuad.isEmpty()) {
            Toast.makeText(this,
                    "Debes seleccionar al menos un quad",
                    Toast.LENGTH_LONG).show();
            return;
        }

        Reserva reserva = new Reserva(
                mNombre.getText().toString().trim(),
                mMovil.getText().toString().trim(),
                fechaInicioMillis,
                fechaFinMillis
        );

        if (isEditMode) {

            // actualizo reserva
            reserva.setId(reservaId);
            mReservaViewModel.update(reserva);

            // actualizo cascos (borrar + insertar)
            mReservaQuadCascosViewModel.deleteByReserva(reservaId);
            mReservaQuadCascosViewModel.insertAll(reservaId, cascosPorQuad);

            setResult(RESULT_OK);
            finish();

        } else {

            // inserto reserva
            long id = mReservaViewModel.insertAndReturnId(reserva);
            if (id == -1) {
                Toast.makeText(this,
                        "Error al guardar la reserva",
                        Toast.LENGTH_LONG).show();
                return;
            }

            reservaId = (int) id;

            // inserto cascos
            mReservaQuadCascosViewModel.insertAll(reservaId, cascosPorQuad);

            // diálogo de confirmación
            new AlertDialog.Builder(this)
                    .setTitle("Reserva creada")
                    .setMessage("La reserva se ha creado correctamente.\nID: #" + reservaId)
                    .setPositiveButton("Aceptar", (d, w) -> {
                        setResult(RESULT_OK);
                        finish();
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    private void cancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private boolean isFormValid() {
        return !TextUtils.isEmpty(mNombre.getText())
                && !TextUtils.isEmpty(mMovil.getText())
                && fechaInicioMillis != -1
                && fechaFinMillis != -1
                && fechaFinMillis >= fechaInicioMillis;
    }
}
