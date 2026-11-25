package fr.mines.philidor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.util.List;

import fr.mines.philidor.R;
import fr.mines.philidor.databinding.ItemPartieBinding;
import fr.mines.philidor.model.PartieJouee;

public class PartieJoueeAdapter extends ArrayAdapter<PartieJouee> {

    private final DateFormat formatDate = DateFormat.getDateInstance(DateFormat.MEDIUM);

    public PartieJoueeAdapter(Context context, List<PartieJouee> parties) {
        super(context, 0, parties);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ItemPartieBinding b;
        if (convertView == null) {
            b = ItemPartieBinding.inflate(LayoutInflater.from(getContext()), parent, false);
            b.getRoot().setTag(b);
        } else {
            b = (ItemPartieBinding) convertView.getTag();
        }

        PartieJouee p = getItem(position);
        Context c = getContext();

        int texteResultat;
        if (p.getResultat() == PartieJouee.Resultat.VICTOIRE) {
            texteResultat = R.string.victoire;
            b.pastille.setText(R.string.initiale_victoire);
            b.pastille.setBackground(ContextCompat.getDrawable(c, R.drawable.pastille_victoire));
        } else if (p.getResultat() == PartieJouee.Resultat.DEFAITE) {
            texteResultat = R.string.defaite;
            b.pastille.setText(R.string.initiale_defaite);
            b.pastille.setBackground(ContextCompat.getDrawable(c, R.drawable.pastille_defaite));
        } else {
            texteResultat = R.string.nulle;
            b.pastille.setText(R.string.initiale_nulle);
            b.pastille.setBackground(ContextCompat.getDrawable(c, R.drawable.pastille_nulle));
        }

        int texteCouleur;
        if (p.isBlancs()) {
            texteCouleur = R.string.blancs;
            b.roi.setText(R.string.roi_blanc);
        } else {
            texteCouleur = R.string.noirs;
            b.roi.setText(R.string.roi_noir);
        }
        b.resultat.setText(c.getString(R.string.resultat_couleur,
                c.getString(texteResultat), c.getString(texteCouleur)));

        int texteFin;
        if (p.getFin() == PartieJouee.Fin.MAT) {
            texteFin = R.string.fin_mat;
        } else if (p.getFin() == PartieJouee.Fin.PAT) {
            texteFin = R.string.fin_pat;
        } else if (p.getFin() == PartieJouee.Fin.ABANDON) {
            texteFin = R.string.fin_abandon;
        } else {
            texteFin = R.string.fin_nulle;
        }
        String coups = c.getResources().getQuantityString(
                R.plurals.nb_coups, p.getNbCoups(), p.getNbCoups());
        b.details.setText(c.getString(R.string.details_partie,
                formatDate.format(p.getDate()), coups, c.getString(texteFin)));

        return b.getRoot();
    }
}
