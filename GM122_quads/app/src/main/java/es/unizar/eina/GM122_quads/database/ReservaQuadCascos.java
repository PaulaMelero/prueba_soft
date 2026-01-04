package es.unizar.eina.GM122_quads.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        tableName = "reserva_quad_cascos",
        primaryKeys = {"reservaId", "matriculaQuad"},
        foreignKeys = {
                @ForeignKey(
                        entity = Reserva.class,
                        parentColumns = "id",
                        childColumns = "reservaId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Quad.class,
                        parentColumns = "matricula",
                        childColumns = "matriculaQuad",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("reservaId"),
                @Index("matriculaQuad")
        }
)
public class ReservaQuadCascos {

    @ColumnInfo(name = "reservaId")
    private int reservaId;

    @NonNull
    @ColumnInfo(name = "matriculaQuad")
    private String matriculaQuad;

    @ColumnInfo(name="numCascos")
    private int numCascos;

    @NonNull
    @ColumnInfo(name="precioOriginal")
    private double precioOriginal;

    public ReservaQuadCascos(int reservaId, @NonNull String matriculaQuad, int numCascos, @NonNull double precioOriginal) {
        this.reservaId = reservaId;
        this.matriculaQuad = matriculaQuad;
        this.numCascos = numCascos;
        this.precioOriginal = precioOriginal;
    }

    public int getReservaId() {
        return reservaId;
    }

    @NonNull
    public String getMatriculaQuad() {
        return matriculaQuad;
    }

    public int getNumCascos() { return numCascos; }

    @NonNull
    public double getPrecioOriginal() { return precioOriginal; }

}
