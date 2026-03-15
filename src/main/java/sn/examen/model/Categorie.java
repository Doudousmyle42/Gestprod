package sn.examen.model;

import jakarta.persistence.*;
import java.util.List;

@Entity                          // Cette classe est une table en BD
@Table(name = "categorie")       // Nom de la table
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
    private int id;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    // Relation inverse : une categorie a plusieurs produits
    @OneToMany(mappedBy = "categorie", fetch = FetchType.LAZY)
    private List<Produit> produits;

    // Constructeur vide OBLIGATOIRE pour JPA
    public Categorie() {}

    public Categorie(String libelle) {
        this.libelle = libelle;
    }

    // Getters et Setters
    public int getId()              { return id; }
    public void setId(int id)       { this.id = id; }
    public String getLibelle()      { return libelle; }
    public void setLibelle(String l){ this.libelle = l; }
    public List<Produit> getProduits() { return produits; }

    @Override
    public String toString() { return libelle; } // Affichage dans ComboBox
}

