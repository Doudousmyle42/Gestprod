package sn.examen.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "produit")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "quantite", nullable = false)
    private int quantite;

    @Column(name = "prix_unitaire", nullable = false)
    private double prixUnitaire;

    // Date d'ajout (pour le graphique par mois)
    @Column(name = "date_ajout")
    private LocalDateTime dateAjout;

    // Cle etrangere vers Categorie
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categorie")
    private Categorie categorie;

    public Produit() {}

    // Constructeur complet
    public Produit(String libelle, int quantite, double prixUnitaire,
                   Categorie categorie) {
        this.libelle = libelle;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.categorie = categorie;
        this.dateAjout = LocalDateTime.now(); // Date actuelle
    }

    // Getters et Setters (tous necessaires)
    public int getId()                    { return id; }
    public void setId(int id)             { this.id = id; }
    public String getLibelle()            { return libelle; }
    public void setLibelle(String l)      { this.libelle = l; }
    public int getQuantite()              { return quantite; }
    public void setQuantite(int q)        { this.quantite = q; }
    public double getPrixUnitaire()       { return prixUnitaire; }
    public void setPrixUnitaire(double p) { this.prixUnitaire = p; }
    public Categorie getCategorie()       { return categorie; }
    public void setCategorie(Categorie c) { this.categorie = c; }
    public LocalDateTime getDateAjout()   { return dateAjout; }
    public void setDateAjout(LocalDateTime d) { this.dateAjout = d; }
}

