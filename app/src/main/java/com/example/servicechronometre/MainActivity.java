package com.example.servicechronometre;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvTemps;
    private Button btnStart, btnStop;

    private ChronometreService chronometreService;
    private boolean isBound = false;

    // Handler pour mettre à jour l'interface utilisateur toutes les secondes
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            if (isBound && chronometreService != null) {
                // On récupère le temps depuis le service et on l'affiche
                int secondes = chronometreService.getTempsEcoule();
                tvTemps.setText(formaterTemps(secondes));
            }
            // On redemande une mise à jour dans 1 seconde
            handler.postDelayed(this, 1000);
        }
    };

    // Connexion au service
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ChronometreService.LocalBinder binder = (ChronometreService.LocalBinder) service;
            chronometreService = binder.getService();
            isBound = true;

            // Si le service tournait déjà en arrière-plan, on relance la mise à jour visuelle
            handler.post(updateTimeRunnable);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des vues
        tvTemps = findViewById(R.id.tvTemps);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);

        // Action du bouton Démarrer
        btnStart.setOnClickListener(v -> demarrerChrono());

        // Action du bouton Arrêter
        btnStop.setOnClickListener(v -> arreterChrono());
    }

    private void demarrerChrono() {
        Intent intent = new Intent(this, ChronometreService.class);

        // Démarrage du service (Foreground pour Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

        // Liaison au service pour communiquer avec lui
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        // Feedback visuel "humain"
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        Toast.makeText(this, "Chronomètre lancé !", Toast.LENGTH_SHORT).show();

        // Lancer la boucle de mise à jour du texte
        handler.post(updateTimeRunnable);
    }

    private void arreterChrono() {
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }

        // Arrêt effectif du service
        Intent intent = new Intent(this, ChronometreService.class);
        stopService(intent);

        // Réinitialisation de l'interface
        handler.removeCallbacks(updateTimeRunnable);
        tvTemps.setText("00:00");
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);

        Toast.makeText(this, "Chronomètre arrêté", Toast.LENGTH_SHORT).show();
    }

    // Petite fonction utilitaire pour un affichage propre
    private String formaterTemps(int totalSecondes) {
        int minutes = totalSecondes / 60;
        int secondes = totalSecondes % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, secondes);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // On nettoie pour éviter les fuites de mémoire
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
        handler.removeCallbacks(updateTimeRunnable);
    }
}
