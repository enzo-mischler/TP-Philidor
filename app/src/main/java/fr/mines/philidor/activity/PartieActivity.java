package fr.mines.philidor.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Locale;

import fr.mines.philidor.R;
import fr.mines.philidor.databinding.ActivityPartieBinding;
import fr.mines.philidor.moteur.Partie;

public class PartieActivity extends AppCompatActivity {

    private ActivityPartieBinding binding;
    private Partie partie;

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

        String campJoueur;
        String campAdversaire;
        if (blancs) {
            campJoueur = getString(R.string.blancs);
            campAdversaire = getString(R.string.noirs);
        } else {
            campJoueur = getString(R.string.noirs);
            campAdversaire = getString(R.string.blancs);
        }
        binding.campBas.setText(campJoueur);
        binding.campHaut.setText(getString(R.string.camp_creativite, campAdversaire,
                String.format(Locale.getDefault(), "%.1f", creativite)));

        partie = new Partie();
        binding.echiquier.setBlancsEnBas(blancs);
        binding.echiquier.setPartie(partie);

        binding.echiquier.setOnClickListener(v -> afficher());
        binding.annuler.setOnClickListener(v -> annuler());
        afficher();
    }

    private void annuler() {
        partie.annuler();
        binding.echiquier.deselectionner();
        afficher();
    }

    private void afficher() {
        binding.statut.setText(statut());
        binding.coups.setText(listeCoups());
        binding.annuler.setEnabled(!partie.coups().isEmpty());
    }

    private String statut() {
        switch (partie.etat()) {
            case MAT:
                return getString(R.string.statut_mat);
            case PAT:
                return getString(R.string.statut_pat);
            case EN_COURS:
                String trait;
                if (partie.traitAuxBlancs()) {
                    trait = getString(R.string.trait_blancs);
                } else {
                    trait = getString(R.string.trait_noirs);
                }
                if (partie.enEchec()) {
                    return getString(R.string.statut_echec, trait);
                }
                return trait;
            default:
                return getString(R.string.statut_nulle);
        }
    }

    private String listeCoups() {
        List<String> coups = partie.coupsSan();
        StringBuilder texte = new StringBuilder();
        for (int i = 0; i < coups.size(); i++) {
            if (i % 2 == 0) {
                texte.append(getString(R.string.numero_coup, i / 2 + 1)).append(' ');
            }
            texte.append(coups.get(i)).append(' ');
        }
        return texte.toString();
    }
}
