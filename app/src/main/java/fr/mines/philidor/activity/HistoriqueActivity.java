package fr.mines.philidor.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.mines.philidor.R;
import fr.mines.philidor.adapter.PartieJoueeAdapter;
import fr.mines.philidor.databinding.ActivityHistoriqueBinding;
import fr.mines.philidor.model.PartieJouee;

public class HistoriqueActivity extends AppCompatActivity {

    private static final long UN_JOUR = 24L * 60 * 60 * 1000;

    private ActivityHistoriqueBinding binding;
    private PartieJoueeAdapter adaptateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHistoriqueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        List<PartieJouee> parties = partiesInventees();
        adaptateur = new PartieJoueeAdapter(this, parties);
        binding.liste.setAdapter(adaptateur);
        binding.liste.setEmptyView(binding.vide);

        binding.liste.setOnItemLongClickListener((parent, vue, position, id) -> {
            confirmerSuppression(adaptateur.getItem(position));
            return true;
        });
    }

    private void confirmerSuppression(PartieJouee partie) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.supprimer_titre)
                .setMessage(R.string.supprimer_message)
                .setNegativeButton(R.string.annuler, null)
                .setPositiveButton(R.string.supprimer,
                        (dialogue, quel) -> adaptateur.remove(partie))
                .show();
    }

    private List<PartieJouee> partiesInventees() {
        long maintenant = System.currentTimeMillis();
        Date aujourdhui = new Date(maintenant);
        Date hier = new Date(maintenant - UN_JOUR);
        Date avantHier = new Date(maintenant - 2 * UN_JOUR);

        List<PartieJouee> parties = new ArrayList<>();
        parties.add(new PartieJouee(aujourdhui, true, PartieJouee.Resultat.VICTOIRE, PartieJouee.Fin.MAT, 41));
        parties.add(new PartieJouee(aujourdhui, false, PartieJouee.Resultat.DEFAITE, PartieJouee.Fin.MAT, 28));
        parties.add(new PartieJouee(hier, true, PartieJouee.Resultat.NULLE, PartieJouee.Fin.PAT, 63));
        parties.add(new PartieJouee(hier, true, PartieJouee.Resultat.DEFAITE, PartieJouee.Fin.ABANDON, 19));
        parties.add(new PartieJouee(avantHier, false, PartieJouee.Resultat.VICTOIRE, PartieJouee.Fin.MAT, 52));
        parties.add(new PartieJouee(avantHier, false, PartieJouee.Resultat.DEFAITE, PartieJouee.Fin.MAT, 35));
        return parties;
    }
}
