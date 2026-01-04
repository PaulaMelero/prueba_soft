package es.unizar.eina.GM122_quads.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Entidad Room que representa una reserva.
 * Una reserva tiene un identificador único (clave primaria), un nombre de cliente, un móvil
 * de cliente, una fecha de recogida, una fecha de devolución, el número de quads reservados
 * para la reserva y un movil total.
 *
 * @author GM122
 */@Entity(tableName = "reserva")
public class Reserva {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "nombreCliente")
    private String nombreCliente;

    @NonNull
    @ColumnInfo(name = "movilCliente")
    private String movilCliente;

    @ColumnInfo(name = "fechaRecogida")
    private long fechaRecogida;

    @ColumnInfo(name = "fechaDevolucion")
    private long fechaDevolucion;

    //@NonNull
    //@ColumnInfo(name = "precio")
    //private Double precio;

    /**
     * Crea una reserva con todos sus campos.
     * @param nombreCliente Nombre del cliente que ha realizado la reserva.
     * @param movilCliente Número móvil del cliente que ha realizado la reserva.
     * @param fechaRecogida Fecha de recogida de los quads alquilados.
     * @param fechaDevolucion Fecha de devolución de los quads alquilados.
     */
    public Reserva(@NonNull String nombreCliente, @NonNull String movilCliente, long fechaRecogida,
                   long fechaDevolucion) {

        this.nombreCliente = nombreCliente;
        this.movilCliente = movilCliente;
        this.fechaRecogida = fechaRecogida;
        this.fechaDevolucion = fechaDevolucion;
    }

    /** Devuelve el id de la reserva */
    public int getId(){
        return this.id;
    }

    /** Permite actualizar el id de la reserva */
    public void setId(int id){
        this.id = id;
    }

    /** Devuelve el nombre del cliente de la reserva */
    public String getNombreCliente(){
        return this.nombreCliente;
    }

    /** Permite actualizar el nombre del cliente de la reserva */
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    /** Devuelve el móvil del cliente de la reserva */
    public String getMovilCliente(){
        return this.movilCliente;
    }

    /** Permite actualizar el móvil del cliente de la reserva */
    public void setMovilCliente(String movilCliente) { this.movilCliente = movilCliente; }

    /** Devuelve la fecha de recogida de los quads reservados */
    public long getFechaRecogida(){
        return this.fechaRecogida;
    }

    /** Permite actualizar la fecha de recogida de los quads reservados */
    public void setFechaRecogida(long fechaRecogida) { this.fechaRecogida = fechaRecogida; }

    /** Devuelve la fecha de devolución de los quads reservados */
    public long getFechaDevolucion() { return this.fechaDevolucion; }

    /** Permite actualizar la fecha de devolución de los quads reservados */
    public void setFechaDevolucion(long fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    /** Devuelve el precio de la reserva */
    //public Double getPrecio() { return this.precio; }

    /** Permite actualizar el precio de la reserva */
    //public void setPrecio(Double precio) { this.precio = precio; }

}
