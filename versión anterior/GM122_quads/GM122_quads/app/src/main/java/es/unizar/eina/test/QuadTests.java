package es.unizar.eina.test;

import android.util.Log;

import es.unizar.eina.GM122_quads.database.Quad;
import es.unizar.eina.GM122_quads.database.QuadDao;
import es.unizar.eina.GM122_quads.database.Quad_Reserva_RoomDataBase;


public class QuadTests {

    private Quad_Reserva_RoomDataBase db;
    private QuadDao quadDao;



    public void testInsertQuad() {
        // Insert válido Monoplaza
        try {
            Quad q = new Quad("1234ABC", true, 65.0, "Rojo");
            long result = quadDao.insert(q);
            Log.d("TEST_QUAD", "Insert válido Monoplaza -> " + result);
        } catch (Throwable t) {
            Log.d("TEST_QUAD", "(Insert Monoplaza debería funcionar: " + t.getMessage());
        }

        // Insert válido Biplaza
        try {
            Quad q = new Quad("1235ABC", false, 65.0, "Rojo");
            long result = quadDao.insert(q);
            Log.d("TEST_QUAD", "Insert válido Biplaza -> " + result);
        } catch (Throwable t) {
            Log.d("TEST_QUAD", ("Insert Biplaza debería funcionar: " + t.getMessage()));
        }

        // Precio inválido
        try {
            Quad q = new Quad("1238ABC", false, -5.0, "Rojo");
            long result = quadDao.insert(q);
            Log.d("TEST_QUAD", "Insert precio inválido -> " + result);
        } catch (Throwable t) {
            Log.d("TEST_QUAD", "Precio inválido correctamente rechazado");
        }

        // Descripción vacía
        try {
            Quad q = new Quad("1237ABC", false, 65.0, "");
            long result = quadDao.insert(q);
            Log.d("TEST_QUAD", "Insert descripción inválida -> " + result);
        } catch (Throwable t) {
            Log.d("TEST_QUAD", "Descripción inválida correctamente rechazada");
        }

        // Tipo nulo
        try {
            Quad q = new Quad("3333ABC", null, 20.0, "Verde");
            long result = quadDao.insert(q);
        } catch (Throwable t) {
            Log.d("TEST_QUAD", "Tipo inválido correctamente rechazado");
        }

        // Matrícula inválida
        try {
            Quad q = new Quad("A24B", true, 65.0, "Rojo");
            long result = quadDao.insert(q);
        } catch (Throwable t) {
            Log.d("TEST_QUAD", "Matrícula inválida correctamente rechazada");
        }

        // Matrícula repetida
        try {
            Quad q1 = new Quad("4444ABC", false, 20.0, "Verde");
            quadDao.insert(q1);

            Quad q2 = new Quad("4444ABC", true, 65.0, "Rojo");
            long result = quadDao.insert(q2);
        } catch (Throwable t) {
            Log.d("TEST_QUAD", "Matrícula repetida correctamente rechazada");
        }

        Log.d("TEST_QUAD", "Test completo de insert finalizado");
    }


    public void testUpdateQuad() {
        // Insertamos un quad inicial válido
        Quad q = new Quad("1234ABC", true, 65.0, "Rojo");
        long id = quadDao.insert(q);

        // Actualización Monoplaza -> Biplaza
        try {
            q.setTipo(false); // de monoplaza a biplaza
            int updatedRows = quadDao.update(q);

            q.setTipo(true); // de biplaza a monoplaza
            updatedRows = quadDao.update(q);

            Log.d("TEST_QUAD", "Actualización de tipo OK");
        } catch (Throwable t) {
            Log.d("TEST_QUAD", ("Fallo al actualizar tipo: " + t.getMessage()));
        }

        // Intento de actualizar un quad que no existe
        try {
            Quad qNoExiste = new Quad("3333ABC", true, 65.0, "No existe");
            int updatedRows = quadDao.update(qNoExiste);
            Log.d("TEST_QUAD", "Actualización de quad inexistente correctamente no realizada");
        } catch (Throwable t) {
            Log.d("TEST_QUAD", ("Excepción inesperada al actualizar quad inexistente: " + t.getMessage()));
        }

        // Poner null como tipo (solo si tipo es Boolean)
        try {
            q.setTipo(null);
            quadDao.update(q);
            Log.d("TEST_QUAD", "Actualizar tipo a null debería fallar");
        } catch (Throwable t) {
            Log.d("TEST_QUAD", "Tipo null correctamente rechazado");
        }

        // Poner null como descripción
        try {
            q.setTipo(true); // volvemos a tipo válido
            q.setDescripcion(null);
            quadDao.update(q);
        } catch (Throwable t) {
            Log.d("TEST_QUAD", "Descripción null correctamente rechazada");
        }

        // Poner precio negativo
        try {
            q.setDescripcion("Válida"); // volvemos a descripción válida
            q.setPrecio(-20.0);
            quadDao.update(q);
        } catch (Throwable t) {
            Log.d("TEST_QUAD", "Precio negativo correctamente rechazado");
        }

        Log.d("TEST_QUAD", "Test completo de update finalizado");
    }


    public void testDeleteQuad() {
        // Insertamos un quad válido que luego vamos a borrar
        Quad q = new Quad("1234ABC", true, 65.0, "Para borrar");
        try {
            long id = quadDao.insert(q);

        } catch (Throwable t) {
            Log.d("TEST_QUAD", ("Excepción al insertar quad para borrar: " + t.getMessage()));
        }

        // Borrar quad que existe
        try {
            int deletedRows = quadDao.deleteByMatricula("1234ABC");
            Log.d("TEST_QUAD", "Delete de quad existente OK");
        } catch (Throwable t) {
            Log.d("TEST_QUAD", "Fallo al borrar quad existente: " + t.getMessage());
        }

        // Intentar borrar quad que no existe
        try {
            int deletedRows = quadDao.deleteByMatricula("3333ABC");
            Log.d("TEST_QUAD", "Delete de quad inexistente correctamente no realizado");
        } catch (Throwable t) {
            Log.d("TEST_QUAD", "Excepción inesperada al borrar quad inexistente: " + t.getMessage());
        }

        Log.d("TEST_QUAD", "Test completo de delete finalizado");
    }

    public void testVolumenQuads() {
        int num = 0;
        try {
            String matricula;
            while ( num < 101 ){
                // Matrícula: 4 dígitos + "AAA"
                 matricula = String.format("%04dAAA", num);

                Quad q = new Quad(
                        matricula,
                        true,             //Monoplaza
                        10.00,
                        "Quad volumen " + num      // Descripción
                );

                long result = quadDao.insert(q);

                num ++;
            }
            Log.d("TEST_QUAD", "Prueba de volumen completada correctamente");
        } catch (Throwable t) {
            Log.d("TEST_QUAD", "Fallo en prueba de volumen con volumen ("+ num +")");
        }
    }


    public void testSobrecargaVolumenQuads() {
        String desc = "a";
        for (int i = 0; i < 100; i++){
            desc = desc + desc;
        }
        int longitud = desc.length();

        try {
            Quad q = new Quad("1111ABC",
                    true,
                    10.0,
                    desc
            );
            quadDao.insert(q);

            for (int i = 0; i < 6; i++) {
                desc = desc + desc;
                longitud = desc.length();
                q = new Quad("1111ABC",
                        true,
                        10.0,
                        desc
                );
               quadDao.update(q);

            }
            Log.d("TEST_QUAD", "Prueba de sobrecarga completada");
        } catch (Throwable t) {
            Log.d("TEST_QUAD", "Fallo en prueba de volumen, longitud = " + longitud);
        }
    }

}
