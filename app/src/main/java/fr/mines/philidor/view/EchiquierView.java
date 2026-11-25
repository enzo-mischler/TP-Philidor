package fr.mines.philidor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import fr.mines.philidor.R;
import fr.mines.philidor.moteur.Partie;

public class EchiquierView extends View {

    private static final String LETTRES = "KQRBNPkqrbnp";
    private static final String SYMBOLES =
            "\u265A\u265B\u265C\u265D\u265E\u265F\u265A\u265B\u265C\u265D\u265E\u265F";

    private final Paint pinceau = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int caseClaire;
    private int caseFoncee;
    private int pieceBlanche;
    private int pieceNoire;
    private int caseSelection;
    private int marque;
    private int dernierCoup;
    private float cote;

    private Partie partie;
    private boolean blancsEnBas = true;

    private int colonneChoisie = -1;
    private int ligneChoisie = -1;
    private List<String> coupsPossibles = new ArrayList<>();

    public EchiquierView(Context c) {
        super(c);
        lireCouleurs();
    }

    public EchiquierView(Context c, AttributeSet a) {
        super(c, a);
        lireCouleurs();
    }

    private void lireCouleurs() {
        caseClaire = ContextCompat.getColor(getContext(), R.color.case_claire);
        caseFoncee = ContextCompat.getColor(getContext(), R.color.case_foncée);
        pieceBlanche = ContextCompat.getColor(getContext(), R.color.white);
        pieceNoire = ContextCompat.getColor(getContext(), R.color.black);
        caseSelection = ContextCompat.getColor(getContext(), R.color.case_selection);
        marque = ContextCompat.getColor(getContext(), R.color.marque);
        dernierCoup = ContextCompat.getColor(getContext(), R.color.dernier_coup);
        pinceau.setTextAlign(Paint.Align.CENTER);
    }

    public void setPartie(Partie partie) {
        this.partie = partie;
        deselectionner();
    }

    public void setBlancsEnBas(boolean blancsEnBas) {
        this.blancsEnBas = blancsEnBas;
        invalidate();
    }

    public void deselectionner() {
        colonneChoisie = -1;
        ligneChoisie = -1;
        coupsPossibles = new ArrayList<>();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                int x = (int) (e.getX() / cote);
                int y = (int) (e.getY() / cote);
                if (partie != null && x >= 0 && x < 8 && y >= 0 && y < 8) {
                    toucher(colonne(x), ligne(y));
                }
                performClick();
                return true;
        }
        return super.onTouchEvent(e);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private void toucher(int colonne, int ligne) {
        String coup = coupVers(colonne, ligne);
        if (coup != null) {
            partie.jouer(coup);
            deselectionner();
        } else if (estAuTrait(partie.piece(colonne, ligne))) {
            colonneChoisie = colonne;
            ligneChoisie = ligne;
            coupsPossibles = partie.coupsDepuis(colonne, ligne);
            invalidate();
        } else {
            deselectionner();
        }
    }

    private String coupVers(int colonne, int ligne) {
        String arrivee = nomCase(colonne, ligne);
        for (String coup : coupsPossibles) {
            if (coup.substring(2, 4).equals(arrivee)) {
                if (coup.length() == 5) {
                    return coup.substring(0, 4) + "q";
                }
                return coup;
            }
        }
        return null;
    }

    private boolean estAuTrait(char piece) {
        if (piece == ' ') {
            return false;
        }
        return Character.isUpperCase(piece) == partie.traitAuxBlancs();
    }

    private String nomCase(int colonne, int ligne) {
        return "" + (char) ('a' + colonne) + (char) ('1' + ligne);
    }

    @Override
    protected void onSizeChanged(int w, int h, int ancienW, int ancienH) {
        super.onSizeChanged(w, h, ancienW, ancienH);
        cote = Math.min(w, h) / 8f;
        pinceau.setTextSize(cote * 0.8f);
        pinceau.setStrokeWidth(cote * 0.03f);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                int colonne = colonne(x);
                int ligne = ligne(y);
                dessinerCase(canvas, x, y, colonne == colonneChoisie && ligne == ligneChoisie);
                if (partie != null) {
                    if (estDansDernierCoup(colonne, ligne)) {
                        dessinerSurlignage(canvas, x, y);
                    }
                    dessinerPiece(canvas, x, y, partie.piece(colonne, ligne));
                    if (coupVers(colonne, ligne) != null) {
                        dessinerMarque(canvas, x, y, estUnePrise(colonne, ligne));
                    }
                }
            }
        }
    }

    private int colonne(int x) {
        if (blancsEnBas) {
            return x;
        }
        return 7 - x;
    }

    private int ligne(int y) {
        if (blancsEnBas) {
            return 7 - y;
        }
        return y;
    }

    private void dessinerCase(Canvas canvas, int x, int y, boolean choisie) {
        if (choisie) {
            pinceau.setColor(caseSelection);
        } else if ((x + y) % 2 == 0) {
            pinceau.setColor(caseClaire);
        } else {
            pinceau.setColor(caseFoncee);
        }
        pinceau.setStyle(Paint.Style.FILL);
        canvas.drawRect(x * cote, y * cote, (x + 1) * cote, (y + 1) * cote, pinceau);
    }

    private boolean estDansDernierCoup(int colonne, int ligne) {
        String coup = partie.dernierCoup();
        if (coup == null) {
            return false;
        }
        String nom = nomCase(colonne, ligne);
        return coup.substring(0, 2).equals(nom) || coup.substring(2, 4).equals(nom);
    }

    private void dessinerSurlignage(Canvas canvas, int x, int y) {
        pinceau.setStyle(Paint.Style.FILL);
        pinceau.setColor(dernierCoup);
        canvas.drawRect(x * cote, y * cote, (x + 1) * cote, (y + 1) * cote, pinceau);
    }

    private void dessinerPiece(Canvas canvas, int x, int y, char piece) {
        int rang = LETTRES.indexOf(piece);
        if (rang < 0) {
            return;
        }
        String symbole = String.valueOf(SYMBOLES.charAt(rang));
        float cx = (x + 0.5f) * cote;
        float base = (y + 0.8f) * cote;

        pinceau.setStyle(Paint.Style.FILL);
        if (Character.isUpperCase(piece)) {
            pinceau.setColor(pieceBlanche);
        } else {
            pinceau.setColor(pieceNoire);
        }
        canvas.drawText(symbole, cx, base, pinceau);

        pinceau.setStyle(Paint.Style.STROKE);
        pinceau.setColor(pieceNoire);
        canvas.drawText(symbole, cx, base, pinceau);
    }

    private boolean estUnePrise(int colonne, int ligne) {
        if (partie.piece(colonne, ligne) != ' ') {
            return true;
        }
        char piece = partie.piece(colonneChoisie, ligneChoisie);
        return (piece == 'P' || piece == 'p') && colonne != colonneChoisie;
    }

    private void dessinerMarque(Canvas canvas, int x, int y, boolean prise) {
        float cx = (x + 0.5f) * cote;
        float cy = (y + 0.5f) * cote;
        pinceau.setColor(marque);
        if (prise) {
            pinceau.setStyle(Paint.Style.STROKE);
            pinceau.setStrokeWidth(cote * 0.08f);
            canvas.drawCircle(cx, cy, cote * 0.42f, pinceau);
            pinceau.setStrokeWidth(cote * 0.03f);
        } else {
            pinceau.setStyle(Paint.Style.FILL);
            canvas.drawCircle(cx, cy, cote * 0.15f, pinceau);
        }
    }
}
