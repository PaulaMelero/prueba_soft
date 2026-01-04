package es.unizar.eina.GM122_quads.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;
import es.unizar.eina.GM122_quads.R;

/**
 * Actividad principal de la aplicación.
 * Muestra el menú principal con acceso a la gestión de Quads y Reservas.
 *
 * Lanza las actividades correspondientes a cada módulo mediante botones.
 */

public class GM122_quads extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Carga el diseño de los botones
        setContentView(R.layout.activity_gm122_main);

        // Configura el botón 'QUADS' para lanzar la gestión de Quads
        MaterialButton quadButton = findViewById(R.id.quad);
        quadButton.setOnClickListener(view -> {
            Intent intent = new Intent(GM122_quads.this, GM122_QuadsList.class);
            startActivity(intent);
        });

        // Configura el botón 'RESERVA' para lanzar la gestión de Reservas
        MaterialButton reservaButton = findViewById(R.id.reserva);
        reservaButton.setOnClickListener(view -> {
            Intent intent = new Intent(GM122_quads.this, GM122_ReservasList.class);
            startActivity(intent);
        });

        Button test_reservaButton = findViewById(R.id.test_reservas);
        test_reservaButton.setOnClickListener(view -> {
            Intent intent = new Intent(GM122_quads.this, TestReservaActivity.class);
            startActivity(intent);
        });

        Button test_quadButton = findViewById(R.id.test_quads);
        test_quadButton.setOnClickListener(view -> {
            Intent intent = new Intent(GM122_quads.this, TestQuadActivity.class);
            startActivity(intent);
        });
    }
}
