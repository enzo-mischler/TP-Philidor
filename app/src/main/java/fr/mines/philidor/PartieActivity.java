package fr.mines.philidor;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

import fr.mines.philidor.databinding.ActivityPartieBinding;

public class PartieActivity extends AppCompatActivity {

    private ActivityPartieBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPartieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        boolean blancs = intent.getBooleanExtra(MainActivity.EXTRA_BLANCS, true);
        float creativite = intent.getFloatExtra(MainActivity.EXTRA_CREATIVITE, 0.6f);

        String couleur;
        if (blancs) {
            couleur = getString(R.string.blancs);
        } else {
            couleur = getString(R.string.noirs);
        }
        binding.couleurChoisie.setText(getString(R.string.couleur_choisie, couleur));
        binding.creativiteChoisie.setText(getString(R.string.creativite_choisie,
                String.format(Locale.getDefault(), "%.1f", creativite)));
    }
}
