package fr.mines.philidor;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;
import java.util.Random;

import fr.mines.philidor.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_BLANCS = "blancs";
    public static final String EXTRA_CREATIVITE = "creativite";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        afficherCreativite(binding.creativite.getValue());
        binding.creativite.addOnChangeListener(
                (slider, valeur, parUtilisateur) -> afficherCreativite(valeur));

        binding.nouvellePartie.setOnClickListener(v -> nouvellePartie());
        binding.historique.setOnClickListener(v -> ouvrirHistorique());
    }

    private void nouvellePartie() {
        int id = binding.couleur.getCheckedButtonId();
        boolean blancs;
        if (id == R.id.hasard) {
            blancs = new Random().nextBoolean();
        } else {
            blancs = id == R.id.blancs;
        }

        Intent intent = new Intent(this, PartieActivity.class);
        intent.putExtra(EXTRA_BLANCS, blancs);
        intent.putExtra(EXTRA_CREATIVITE, binding.creativite.getValue());
        startActivity(intent);
    }

    private void ouvrirHistorique() {
        Intent intent = new Intent(this, HistoriqueActivity.class);
        startActivity(intent);
    }

    private void afficherCreativite(float valeur) {
        binding.valeurCreativite.setText(String.format(Locale.getDefault(), "%.1f", valeur));
    }
}
