--Connexion
    --remplacer le 1er ? par le mail entré 
    --remplacer le 2ème ? par le mot de passe rentré
    SELECT * FROM UTILISATEUR WHERE mail LIKE ? AND mdp LIKE ?;

--Suppression d'un utilisateur

    --nouvel ID (fantôme)
    Savepoint save1;
    SELECT max(id_uti) FROM ID_UTILISATEUR;
    --on insère le nouvel id_uti
    --En remplaçant ? par le résultat précédant + 1 
    INSERT INTO ID_UTILISATEUR VALUES (?);

    --On récupère l'id de utilisateur à supprimer
    --remplacer ? par le mail de l'utilisateur qui va être supprimé
    SELECT id_uti FROM UTILISATEUR WHERE mail LIKE ?;

    --si il n'y a aucun résultat:
    Rollback to save1;

    --sinon:
    --on regarde les offres à modifier
    --remplacer ? par le mail de l'utilisateur qui va être supprimé
    SELECT ID_prod, date_heure, ID_uti FROM OFFRE WHERE ID_uti = ?;
    Commit;

    --avec chaque ligne de résultats on modifie:
    --remplacer le 1er ? par le nouvel ID
    --remplacer le 2ème ? par l'id de l'utilisateur qui va être supprimé
    --remplacer le 3ème ? par date_heure de la ligne actuelle du résultat 
    --remplacer le 4ème ? par ID_prod de la ligne actuelle du résultat 
    UPDATE OFFRE SET id_uti = ? WHERE id_uti = ? AND date_heure = ? AND ID_prod = ?;

    --on supprime l'utilisateur
    --remplacer ? par le mail de l'utilisateur qui va être supprimé
    DELETE FROM UTILISATEUR WHERE mail LIKE ?;
    --remplacer ? par l'id de l'utilisateur qui va être supprimé
    DELETE FROM ID_UTILISATEUR WHERE id_uti = ?;

--afficher la fiche d'un produit
    --On récupère toutes les informations sur le produit sauf lecaractéristique
    --remplacer ? par l'id du produit entré
    SELECT * FROM PRODUIT WHERE ID_prod=?;
    --On récupère les caractéristiques du le produit
    --remplacer ? par l'id du produit entré
    SELECT * FROM CARACTERISTIQUE WHERE ID_prod=?;

--faire une enchère
    --on récupère l'id de l'utilisateur
    --remplacer ? par le mail de l'utilisateur
    SELECT ID_uti FROM UTILISATEUR WHERE mail=?;
    --Récupérer l'ID du produit correspondant et vérifier qu'il ne soit ni déjà acheté, ni trop cher par rapport au prix proposé
    --Remplacer le 1er ? par l'id entré par l'utilisateur
    --Remplacer le 2ème ? par le prix proposé par l'utilisateur
    SELECT ID_prod FROM PRODUIT WHERE ID_prod=? AND prix_produit < ? AND ID_prod NOT IN (SELECT ID_prod FROM REMPORTE);
    --Si il y a au moins un résultat:
        --On ajoute la nouvelle offre
        --Remplacer le 1er ? par l'id du produit
        --Remplacer le 2ème ? par le prix proposé
        --Remplacer le 3ème ? par l'id de lutilisateur
        INSERT INTO OFFRE VALUES(?, (SELECT LOCALTIMESTAMP FROM dual), ?, ?);
        --on met à jour le prix du produit dans le table PRODUIT
        --Remplacer le 1er ? par le nouveau prix
        --Remplacer le 2ème ? par l'id du produit
        --Remplacer le 2ème ? par l'id du produit
        UPDATE PRODUIT SET prix_produit=? WHERE ID_prod=?;
        --On compte le nombre d'offres
        --Remplacer ? par l'id du produit
        SELECT COUNT(*) FROM OFFRE WHERE ID_prod = ?;
        --Si le nombre d'offres est à 5 le produit est remporté:
            --Remplacer les trois ? par l'id du produit
            INSERT INTO REMPORTE VALUES(?, ((SELECT date_heure FROM OFFRE WHERE ID_prod = ?)
                    MINUS
                    (SELECT DISTINCT O1.date_heure FROM OFFRE O1 JOIN OFFRE O2
                        ON O1.ID_prod = O2.ID_prod AND O1.ID_prod = ?
                        WHERE O1.date_heure < O2.date_heure)));
        --on commit les changements
        Commit;
    --Sinon soit le produit est vendu soit le prix proposé n'est pas assez élevé


--Afficher les noms des catégories
    --On récupère les catégories qui n'ont pas de parents
    SELECT * FROM CATEGORIE WHERE nom_cat NOT IN (SELECT nom_cat FROM EST_LA_SOUS_CATEGORIE_DE);
    --Pour chaque ligne du résultat:
        --On récupère les sous-catégories
        --Remplacer ? par le nom_cat du résultat actuel
        SELECT * FROM EST_LA_SOUS_CATEGORIE_DE WHERE nom_pere LIKE ?;


--Recommendations personnelles 
    --On récupère le nombre d'offres (sans remporter) par catégorie pour utilisateur
    --Remplacer ? par le mail de l'utilisateur
    SELECT nom_cat, COUNT(*) AS nb_offres FROM OFFRE
    JOIN PRODUIT 
    ON PRODUIT.ID_prod = OFFRE.ID_prod
    JOIN UTILISATEUR
    ON UTILISATEUR.ID_uti = OFFRE.ID_uti
    WHERE UTILISATEUR.mail = ? AND NOT EXISTS (SELECT * FROM REMPORTE JOIN OFFRE OFR 
                                ON REMPORTE.ID_uti = OFFRE.ID_uti AND REMPORTE.date_heure = OFR.date_heure
                                WHERE OFR.ID_uti = OFFRE.ID_uti
                                AND OFFRE.ID_prod = REMPORTE.ID_prod)
    GROUP BY nom_cat
    ORDER BY nb_offres DESC, nom_cat;

--Recommendations Générales
    --On récupère le nombre moyen d'offres par catégorie pour lesquellesutilisateur n'a pas fait d'offres sans remporter
    --Remplacer ? par le mail de l'utilisateur
    SELECT nom_cat, AVG(nb_offres) AS moyenne_offre FROM (SELECT prod.ID_prod, prod.nom_cat, COUNT(*) AS nb_offres FROM PRODUIT
        JOIN OFFRE
        ON OFFRE.ID_prod = prod.ID_prod
        GROUP BY prod.ID_prod, pord.nom_cat
        UNION
        SELECT prod1.ID_prod, pord1.nom_cat,0 as nb_offres from PRODUIT prod1
        WHERE prod1.ID_prod NOT IN (SELECT OFFRE.ID_prod from OFFRE)
        GROUP BY prod1.ID_prod, prod1.nom_cat)
    WHERE nom_cat not in (SELECT DISTINCT PRODUIT.nom_cat FROM OFFRE
        JOIN PRODUIT ON PRODUIT.ID_prod = OFFRE.ID_prod
        JOIN UTILISATEUR ON UTILISATEUR.ID_uti = OFFRE.ID_uti
        WHERE UTILISATEUR.mail = ? AND NOT EXISTS (SELECT * FROM REMPORTE JOIN OFFRE OFR
            ON REMPORTE.ID_prod = OFR.ID_prod AND REMPORTE.date_heure = OFR.date_heure
            WHERE OFR.ID_uti = OFFRE.ID_uti AND OFFRE.ID_prod = REMPORTE.ID_prod))
    GROUP BY nom_cat
    ORDER BY moyenne_offre DESC, nom_cat;

--Afficher les produits d'une catégorie
    --On récupère  les id des produits non remportés ayant au moins une offre dans l'ordre décroissant du nombre d'offres
    --Remplacer ? par le nom de la catégorie entrée
    SELECT OFFRE.ID_prod, count(*) AS nb_offres, PRODUIT.intitule FROM OFFRE
    JOIN PRODUIT ON PRODUIT.ID_prod = OFFRE.ID_prod
    WHERE PRODUIT.nom_cat LIKE ?
    AND NOT EXISTS (SELECT * FROM REMPORTE
        JOIN OFFRE OFR
        ON REMPORTE.ID_prod = OFR.ID_prod AND REMPORTE.date_heure = OFR.date_heure
        WHERE OFFRE.ID_prod = REMPORTE.ID_prod)
    GROUP BY OFFRE.ID_prod, PRODUIT.intitule
    ORDER BY nb_offres DESC, PRODUIT.intitule;
    --Pour chaque ligne du résultat:
        --On récupère les informations sur les produits
        SELECT * FROM PRODUIT WHERE ID_prod=?;
        SELECT * FROM PRODUIT
        WHERE nom_cat LIKE ?
        AND NOT EXISTS (SELECT ID_prod FROM OFFRE 
            WHERE PRODUIT.ID_prod = OFFRE.ID_prod)
        ORDER BY PRODUIT.intitule;
    --On récupère les produits n'ayant pas d'offres
    --Remplacer ? par le nom de la catégorie entrée
    SELECT * FROM PRODUIT
    WHERE nom_cat LIKE ?
    AND NOT EXISTS (SELECT ID_prod FROM OFFRE 
        WHERE PRODUIT.ID_prod = OFFRE.ID_prod)
    ORDER BY PRODUIT.intitule;
