package es.unizar.eina.test;

import android.content.Context;
import android.widget.Toast;

import androidx.test.platform.app.InstrumentationRegistry;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuadTestRunner {

    private final Context context;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public QuadTestRunner(Context context) {
        this.context = context;
    }

    /** Ejecuta las pruebas unitarias (insert, update, delete) */
    public void runUnitTests() {
        executor.execute(() -> {
            QuadTests tests = new QuadTests();
            tests.setUp();
            tests.testInsertQuad();
            tests.testUpdateQuad();
            tests.testDeleteQuad();
            Toast.makeText(context, "Pruebas unitarias completadas", Toast.LENGTH_SHORT).show();
        });
    }

    /** Ejecuta la prueba de volumen */
    public void runVolumeTest() {
        executor.execute(() -> {
            QuadTests tests = new QuadTests();
            tests.setUp();
            tests.testVolumenQuads();
            Toast.makeText(context, "Prueba de volumen completada", Toast.LENGTH_SHORT).show();
        });
    }

    /** Ejecuta la prueba de sobrecarga */
    public void runOverloadTest() {
        executor.execute(() -> {
            QuadTests tests = new QuadTests();
            tests.setUp();
            tests.testSobrecargaVolumenQuads();
            Toast.makeText(context, "Prueba de sobrecarga completada", Toast.LENGTH_SHORT).show();
        });
    }
}
