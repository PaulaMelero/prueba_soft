package es.unizar.eina.GM122_quads.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import es.unizar.eina.GM122_quads.R;
import es.unizar.eina.GM122_quads.database.Reserva;

/**
 * ViewHolder que representa una cajita del RecyclerView para reservas.
 * Cada instancia corresponde a una fila del listado de reservas
 * (recyclerview_item.xml).
 *
 * Se encarga de:
 *  - Mostrar información resumida de la reserva
 *  - Exponer los botones de editar y eliminar
 */
public class ReservaViewHolder extends RecyclerView.ViewHolder {

    /* ========= VISTAS DEL ITEM ========= */

    private final TextView txtMain;
    public final ImageView btnEdit;
    public final ImageView btnDelete;

    /**
     * Constructor privado.
     * Se invoca desde el método create().
     */
    private ReservaViewHolder(@NonNull View itemView) {
        super(itemView);

        txtMain   = itemView.findViewById(R.id.textView);
        btnEdit   = itemView.findViewById(R.id.btn_edit);
        btnDelete = itemView.findViewById(R.id.btn_delete);
    }

    /**
     * Asocia el id de una Reserva con la vista.
     * @param reserva objeto Reserva a representar
     */
    public void bind(@NonNull Reserva reserva) {
        String resumen = "#" + String.valueOf(reserva.getId());

        txtMain.setText(resumen);
    }

    /**
     * Método de factoría para crear el ViewHolder.
     */
    @NonNull
    public static ReservaViewHolder create(@NonNull ViewGroup parent) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);

        return new ReservaViewHolder(view);
    }

    /* =========================================================
       UTILIDAD
       ========================================================= */

    private String formatDate(long millis) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTimeInMillis(millis);
        return c.get(java.util.Calendar.DAY_OF_MONTH) + "/"
                + (c.get(java.util.Calendar.MONTH) + 1) + "/"
                + c.get(java.util.Calendar.YEAR);
    }
}
