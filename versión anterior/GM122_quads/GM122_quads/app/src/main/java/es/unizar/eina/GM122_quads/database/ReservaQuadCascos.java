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

    public ReservaQuadCascos(int reservaId, @NonNull String matriculaQuad, int numCascos) {
        this.reservaId = reservaId;
        this.matriculaQuad = matriculaQuad;
        this.numCascos = numCascos;
    }

    public int getReservaId() {
        return reservaId;
    }

    @NonNull
    public String getMatriculaQuad() {
        return matriculaQuad;
    }

    public int getNumCascos() { return numCascos; }

}
