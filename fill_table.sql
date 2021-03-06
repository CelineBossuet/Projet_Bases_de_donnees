INSERT INTO ID_UTILISATEUR VALUES(1);
INSERT INTO ID_UTILISATEUR VALUES(2);
INSERT INTO ID_UTILISATEUR VALUES(3);
INSERT INTO ID_UTILISATEUR VALUES(4);
INSERT INTO ID_UTILISATEUR VALUES(5);
--utilisateurs normaux
INSERT INTO UTILISATEUR VALUES('tim@gmail.com', 1, '1234', 'Desmoulins', 'Tim', '25 route de Bitche, 67340 INGWILLER');
INSERT INTO UTILISATEUR VALUES('tina@gmail.com', 2, 'password', 'Tasser', 'Tina', '32 grand rue, 38400 SMH');
--utilisateur avec des offres et remporte à oublier
INSERT INTO UTILISATEUR VALUES('john@gmail.com', 3, 'pswd', 'Doe', 'John', '16 rue Richelieu, 37000 TOURS');
--utilisateur sans offre à oublier
INSERT INTO UTILISATEUR VALUES('jane@gmail.com', 4, 'pwd', 'Doe', 'Jane', '16 rue Richelieu, 37000 TOURS');
--utilisateur avec offres sans remporter à oublier
INSERT INTO UTILISATEUR VALUES('socrate@gmail.com', 5, 'mdp', 'Petteng', 'Socrate', '15 avenue du General, 93300 AUBERVILLIERS');

--ajout de catégories
INSERT INTO CATEGORIE VALUES ('chaussures_de_ville');
INSERT INTO CATEGORIE VALUES ('chaussures_randonnee');
INSERT INTO CATEGORIE VALUES ('chaussures');
INSERT INTO CATEGORIE VALUES ('tennis');
INSERT INTO CATEGORIE VALUES ('vetements');
INSERT INTO CATEGORIE VALUES ('pantalons');
INSERT INTO CATEGORIE VALUES ('manteau');
INSERT INTO CATEGORIE VALUES ('CD');

--Ajout de produits
INSERT INTO PRODUIT VALUES(11, 'chaussures de rando', 18, 'blablablalbalbla', 'https://chaussures', 'chaussures_randonnee');
INSERT INTO PRODUIT VALUES(12, 'chaussures de ville', 21, 'blablablalbalbla', 'https://chaussure_de_ville', 'chaussures_de_ville');
INSERT INTO PRODUIT VALUES(13, 'jeans', 20, 'blabla', 'https://jeans', 'pantalons');
INSERT INTO PRODUIT VALUES(14, 'chaussures de rando', 18, 'blablablalbalbla', 'https://chaussures2', 'chaussures_randonnee');
INSERT INTO PRODUIT VALUES(15, 'chino', 25, 'blabla', 'https://chino', 'pantalons');
INSERT INTO PRODUIT VALUES(16, 'tn', 50, 'blablablalbalbla', 'https://tn', 'tennis');
INSERT INTO PRODUIT VALUES(17, 'tn', 50, 'blablablalbalbla', 'https://tn2', 'tennis');
INSERT INTO PRODUIT VALUES(18, 'doudoune the north face', 80, 'lorem ipsum', 'https://tnf', 'manteau');
INSERT INTO PRODUIT VALUES(19, 'Civilisation', 10, 'lorem ipsum', 'https://civilisation', 'CD');
INSERT INTO PRODUIT VALUES(20, 'Akimbo', 10, 'lorem ipsum', 'https://akimbo', 'CD');

--Caractéristiques
INSERT INTO CARACTERISTIQUE VALUES(11, 'pointure', '42');
INSERT INTO CARACTERISTIQUE VALUES(14, 'pointure', '41');
INSERT INTO CARACTERISTIQUE VALUES(16, 'pointure', '41');
INSERT INTO CARACTERISTIQUE VALUES(17, 'pointure', '38');
INSERT INTO CARACTERISTIQUE VALUES(12, 'pointure', '38');
INSERT INTO CARACTERISTIQUE VALUES(11, 'couleur', 'marron');
INSERT INTO CARACTERISTIQUE VALUES(14, 'couleur', 'vert');
INSERT INTO CARACTERISTIQUE VALUES(16, 'couleurs', 'gris et rouge');
INSERT INTO CARACTERISTIQUE VALUES(16, 'couleur', 'noir');
INSERT INTO CARACTERISTIQUE VALUES(12, 'couleur', 'noir');
INSERT INTO CARACTERISTIQUE VALUES(13, 'couleur', 'bleu');
INSERT INTO CARACTERISTIQUE VALUES(15, 'couleur', 'bordeau');
INSERT INTO CARACTERISTIQUE VALUES(13, 'taille', '40');
INSERT INTO CARACTERISTIQUE VALUES(15, 'taille', '38');
INSERT INTO CARACTERISTIQUE VALUES(18, 'taille', 'L');
INSERT INTO CARACTERISTIQUE VALUES(18, 'couleur', 'blanc');
INSERT INTO CARACTERISTIQUE VALUES(19, 'artiste', 'Orelsan');
INSERT INTO CARACTERISTIQUE VALUES(20, 'artiste', 'Ziak');

--Sous catégories
INSERT INTO EST_LA_SOUS_CATEGORIE_DE VALUES ('manteau', 'vetements');
INSERT INTO EST_LA_SOUS_CATEGORIE_DE VALUES ('tennis', 'chaussures');
INSERT INTO EST_LA_SOUS_CATEGORIE_DE VALUES ('chaussures_randonnee', 'chaussures');
INSERT INTO EST_LA_SOUS_CATEGORIE_DE VALUES ('chaussures_de_ville', 'chaussures');
INSERT INTO EST_LA_SOUS_CATEGORIE_DE VALUES ('chaussures', 'vetements');
INSERT INTO EST_LA_SOUS_CATEGORIE_DE VALUES ('pantalons', 'vetements');

--Offres
--non remporté
INSERT INTO OFFRE VALUES (11, (SELECT LOCALTIMESTAMP FROM dual), 19, 1);
INSERT INTO OFFRE VALUES (11, (SELECT LOCALTIMESTAMP FROM dual), 20, 2);
INSERT INTO OFFRE VALUES (11, (SELECT LOCALTIMESTAMP FROM dual), 21, 1);
INSERT INTO OFFRE VALUES (11, (SELECT LOCALTIMESTAMP FROM dual), 22, 2);

--non remporté
INSERT INTO OFFRE VALUES (12, (SELECT LOCALTIMESTAMP FROM dual), 20, 2);
INSERT INTO OFFRE VALUES (12, (SELECT LOCALTIMESTAMP FROM dual), 21, 2);
INSERT INTO OFFRE VALUES (12, (SELECT LOCALTIMESTAMP FROM dual), 22, 2);

--remporté par 2
INSERT INTO OFFRE VALUES (13, (SELECT LOCALTIMESTAMP FROM dual), 30, 5);
INSERT INTO OFFRE VALUES (13, (SELECT LOCALTIMESTAMP FROM dual), 35, 2);
INSERT INTO OFFRE VALUES (13, (SELECT LOCALTIMESTAMP FROM dual), 40, 1);
INSERT INTO OFFRE VALUES (13, (SELECT LOCALTIMESTAMP FROM dual), 45, 3);
INSERT INTO OFFRE VALUES (13, (SELECT LOCALTIMESTAMP FROM dual), 50, 2);

--non remporté
INSERT INTO OFFRE VALUES (14, (SELECT LOCALTIMESTAMP FROM dual), 22, 3);
INSERT INTO OFFRE VALUES (14, (SELECT LOCALTIMESTAMP FROM dual), 23, 1);
INSERT INTO OFFRE VALUES (14, (SELECT LOCALTIMESTAMP FROM dual), 30, 3);

--remporté par 3
INSERT INTO OFFRE VALUES (15, (SELECT LOCALTIMESTAMP FROM dual), 26, 3);
INSERT INTO OFFRE VALUES (15, (SELECT LOCALTIMESTAMP FROM dual), 27, 1);
INSERT INTO OFFRE VALUES (15, (SELECT LOCALTIMESTAMP FROM dual), 28, 3);
INSERT INTO OFFRE VALUES (15, (SELECT LOCALTIMESTAMP FROM dual), 29, 1);
INSERT INTO OFFRE VALUES (15, (SELECT LOCALTIMESTAMP FROM dual), 32, 3);

--remporté par 1
INSERT INTO OFFRE VALUES (16, (SELECT LOCALTIMESTAMP FROM dual), 55, 5);
INSERT INTO OFFRE VALUES (16, (SELECT LOCALTIMESTAMP FROM dual), 60, 1);
INSERT INTO OFFRE VALUES (16, (SELECT LOCALTIMESTAMP FROM dual), 61, 1);
INSERT INTO OFFRE VALUES (16, (SELECT LOCALTIMESTAMP FROM dual), 65, 5);
INSERT INTO OFFRE VALUES (16, (SELECT LOCALTIMESTAMP FROM dual), 70, 1);

--remporté par 2
INSERT INTO OFFRE VALUES (17, (SELECT LOCALTIMESTAMP FROM dual), 55, 5);
INSERT INTO OFFRE VALUES (17, (SELECT LOCALTIMESTAMP FROM dual), 60, 2);
INSERT INTO OFFRE VALUES (17, (SELECT LOCALTIMESTAMP FROM dual), 70, 1);
INSERT INTO OFFRE VALUES (17, (SELECT LOCALTIMESTAMP FROM dual), 80, 5);
INSERT INTO OFFRE VALUES (17, (SELECT LOCALTIMESTAMP FROM dual), 90, 2);

--remporté par 3
INSERT INTO OFFRE VALUES (18, (SELECT LOCALTIMESTAMP FROM dual), 90, 5);
INSERT INTO OFFRE VALUES (18, (SELECT LOCALTIMESTAMP FROM dual), 100, 2);
INSERT INTO OFFRE VALUES (18, (SELECT LOCALTIMESTAMP FROM dual), 110, 5);
INSERT INTO OFFRE VALUES (18, (SELECT LOCALTIMESTAMP FROM dual), 120, 1);
INSERT INTO OFFRE VALUES (18, (SELECT LOCALTIMESTAMP FROM dual), 125, 3);

--remporté par 3
INSERT INTO OFFRE VALUES (19, (SELECT LOCALTIMESTAMP FROM dual), 12, 3);
INSERT INTO OFFRE VALUES (19, (SELECT LOCALTIMESTAMP FROM dual), 15, 1);
INSERT INTO OFFRE VALUES (19, (SELECT LOCALTIMESTAMP FROM dual), 17, 5);
INSERT INTO OFFRE VALUES (19, (SELECT LOCALTIMESTAMP FROM dual), 18, 2);
INSERT INTO OFFRE VALUES (19, (SELECT LOCALTIMESTAMP FROM dual), 20, 3);

--remporté par 1
INSERT INTO OFFRE VALUES (20, (SELECT LOCALTIMESTAMP FROM dual), 15, 1);
INSERT INTO OFFRE VALUES (20, (SELECT LOCALTIMESTAMP FROM dual), 18, 5);
INSERT INTO OFFRE VALUES (20, (SELECT LOCALTIMESTAMP FROM dual), 19, 3);
INSERT INTO OFFRE VALUES (20, (SELECT LOCALTIMESTAMP FROM dual), 25, 2);
INSERT INTO OFFRE VALUES (20, (SELECT LOCALTIMESTAMP FROM dual), 30, 1);

-- Met à jour les prix des produits pour les offres proposées
UPDATE PRODUIT SET prix_produit = 22 WHERE id_prod = 11;
UPDATE PRODUIT SET prix_produit = 25 WHERE id_prod = 12;
UPDATE PRODUIT SET prix_produit = 50 WHERE id_prod = 13;
UPDATE PRODUIT SET prix_produit = 30 WHERE id_prod = 14;
UPDATE PRODUIT SET prix_produit = 32 WHERE id_prod = 15;
UPDATE PRODUIT SET prix_produit = 70 WHERE id_prod = 16;
UPDATE PRODUIT SET prix_produit = 90 WHERE id_prod = 17;
UPDATE PRODUIT SET prix_produit = 125 WHERE id_prod = 18;
UPDATE PRODUIT SET prix_produit = 20 WHERE id_prod = 19;
UPDATE PRODUIT SET prix_produit = 30 WHERE id_prod = 20;

--On ajoute dans les remportés
--13
INSERT INTO REMPORTE VALUES(13 , ((SELECT date_heure FROM OFFRE WHERE ID_prod = 13)
                                MINUS
                                (SELECT DISTINCT O1.date_heure FROM OFFRE O1 JOIN OFFRE O2
                                    ON O1.ID_prod = O2.ID_prod AND O1.ID_prod = 13
                                WHERE O1.date_heure < O2.date_heure)));
--15
INSERT INTO REMPORTE VALUES(15 , ((SELECT date_heure FROM OFFRE WHERE ID_prod = 15)
                                MINUS
                                (SELECT DISTINCT O1.date_heure FROM OFFRE O1 JOIN OFFRE O2
                                    ON O1.ID_prod = O2.ID_prod AND O1.ID_prod = 15
                                WHERE O1.date_heure < O2.date_heure)));
--16
INSERT INTO REMPORTE VALUES(16 , ((SELECT date_heure FROM OFFRE WHERE ID_prod = 16)
                                MINUS
                                (SELECT DISTINCT O1.date_heure FROM OFFRE O1 JOIN OFFRE O2
                                    ON O1.ID_prod = O2.ID_prod AND O1.ID_prod = 16
                                WHERE O1.date_heure < O2.date_heure)));
--17
INSERT INTO REMPORTE VALUES(17 , ((SELECT date_heure FROM OFFRE WHERE ID_prod = 17)
                                MINUS
                                (SELECT DISTINCT O1.date_heure FROM OFFRE O1 JOIN OFFRE O2
                                    ON O1.ID_prod = O2.ID_prod AND O1.ID_prod = 17
                                WHERE O1.date_heure < O2.date_heure)));
--18
INSERT INTO REMPORTE VALUES(18 , ((SELECT date_heure FROM OFFRE WHERE ID_prod = 18)
                                MINUS
                                (SELECT DISTINCT O1.date_heure FROM OFFRE O1 JOIN OFFRE O2
                                    ON O1.ID_prod = O2.ID_prod AND O1.ID_prod = 18
                                WHERE O1.date_heure < O2.date_heure)));
--19
INSERT INTO REMPORTE VALUES(19 , ((SELECT date_heure FROM OFFRE WHERE ID_prod = 19)
                                MINUS
                                (SELECT DISTINCT O1.date_heure FROM OFFRE O1 JOIN OFFRE O2
                                    ON O1.ID_prod = O2.ID_prod AND O1.ID_prod = 19
                                WHERE O1.date_heure < O2.date_heure)));
--20
INSERT INTO REMPORTE VALUES(20 , ((SELECT date_heure FROM OFFRE WHERE ID_prod = 20)
                                MINUS
                                (SELECT DISTINCT O1.date_heure FROM OFFRE O1 JOIN OFFRE O2
                                    ON O1.ID_prod = O2.ID_prod AND O1.ID_prod = 20
                                WHERE O1.date_heure < O2.date_heure)));
