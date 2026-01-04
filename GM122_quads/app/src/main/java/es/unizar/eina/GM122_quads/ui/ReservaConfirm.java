package es.unizar.eina.GM122_quads.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashMap;
import java.util.Map;

import es.unizar.eina.GM122_quads.R;
import es.unizar.eina.GM122_quads.database.Reserva;
import es.unizar.eina.GM122_quads.utils.DateUtils;

public class ReservaConfirm extends AppCompatActivity {

    private ReservaViewModel mReservaViewModel;
    private ReservaQuadCascosViewModel mReservaQuadCascosViewModel;

    private TextView tvCliente;
    private TextView tvFechas;
    private TextView tvPrecio;

    private Button btnConfirmar;
    private Button btnCancelar;

    private Reserva reserva;
    private Map<String, Integer> cascosPorQuad = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_confirm);

        bindViews();

        mReservaViewModel = new ViewModelProvider(this).get(ReservaViewModel.class);
        mReservaQuadCascosViewModel =
                new ViewModelProvider(this).get(ReservaQuadCascosViewModel.class);

        cargarDatos();
        mostrarDatos();
        calcularPrecio();

        btnConfirmar.setOnClickListener(v -> guardarReserva());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void bindViews() {
        tvCliente = findViewById(R.id.text_cliente);
        tvFechas = findViewById(R.id.text_fechas);
        tvPrecio = findViewById(R.id.text_precio_total);
        btnConfirmar = findViewById(R.id.button_confirm);
        btnCancelar = findViewById(R.id.button_cancel);
    }

    private void cargarDatos() {
        Intent intent = getIntent();

        String nombre = intent.getStringExtra(ReservaModify.RESERVA_NOMBRE);
        String movil = intent.getStringExtra(ReservaModify.RESERVA_MOVIL);
        long fechaInicio = intent.getLongExtra(
                ReservaModify.RESERVA_FECHA_RECOGIDA, -1);
        long fechaFin = intent.getLongExtra(
                ReservaModify.RESERVA_FECHA_DEVOLUCION, -1);

        reserva = new Reserva(nombre, movil, fechaInicio, fechaFin);

        if (intent.hasExtra(ReservaModify.RESERVA_ID)) {
            reserva.setId(
                    intent.getIntExtra(ReservaModify.RESERVA_ID, -1)
            );
        }

        cascosPorQuad =
                (HashMap<String, Integer>) intent
                        .getSerializableExtra(ReservaModify.RESERVA_QUADS_CASCOS);
    }


    private void mostrarDatos() {
        tvCliente.setText(
                "Cliente: " + reserva.getNombreCliente()
        );

        tvFechas.setText(
                "Del " + DateUtils.toHumanDate(reserva.getFechaRecogida()) +
                        " al " + DateUtils.toHumanDate(reserva.getFechaDevolucion())
        );
    }

    private void calcularPrecio() {
        /**

        mReservaQuadCascosViewModel.calcularPrecioPreview(
                reserva.getId(),
                cascosPorQuad,
                total -> tvPrecio.setText(
                        String.format("%.2f €", total)
                )
        );
         */
    }

    private void guardarReserva() {

        boolean esNueva = reserva.getId() == 0;

        if (esNueva) {
            long id = mReservaViewModel.insertAndReturnId(reserva);
            reserva.setId((int) id);
        } else {
            mReservaViewModel.update(reserva);
        }

        mReservaQuadCascosViewModel.updateCascos(
                reserva.getId(),
                cascosPorQuad
        );

        // aquí SÍ se guarda el precio definitivo
        mReservaViewModel.recalcularPrecio(
                reserva.getId(),
                reserva.getFechaRecogida(),
                reserva.getFechaDevolucion()
        );

        new AlertDialog.Builder(this)
                .setTitle("Reserva confirmada")
                .setMessage(
                        "Reserva guardada correctamente.\nID: #" + reserva.getId()
                )
                .setPositiveButton("Aceptar", (d, w) -> {
                    setResult(RESULT_OK);
                    finish();
                })
                .setCancelable(false)
                .show();
    }

}
