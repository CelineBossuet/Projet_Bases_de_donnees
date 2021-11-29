INSERT INTO ID_UTILISATEUR VALUES(1);
INSERT INTO ID_UTILISATEUR VALUES(2);
INSERT INTO ID_UTILISATEUR VALUES(3);
INSERT INTO ID_UTILISATEUR VALUES(4);
INSERT INTO ID_UTILISATEUR VALUES(5);
INSERT INTO UTILISATEUR VALUES('tim@gmail.com', 1, '1234', 'Desmoulins', 'Tim', '25 route de Bitche, 67340 INGWILLER');
INSERT INTO UTILISATEUR VALUES('tina@gmail.com', 2, 'password', 'Tasser', 'Tina', '32 grand rue, 38400 SMH');
--utilisateur avec des offres et remporte à oublier
INSERT INTO UTILISATEUR VALUES('john@gmail.com', 3, 'pswd', 'Doe', 'John', '16 rue Richelieu, 37000 TOURS');
--utilisateur sans offre à oublier
INSERT INTO UTILISATEUR VALUES('jane@gmail.com', 4, 'pwd', 'Doe', 'Jane', '16 rue Richelieu, 37000 TOURS');
--utilisateur avec offres sans remporter à oublier
INSERT INTO UTILISATEUR VALUES('socrate@gmail.com', 5, 'mdp', 'Petteng', 'Socrate', '15 rue du General, 93300 Aubervilliers');

--ajout de catégories
INSERT INTO CATEGORIE VALUES ('chaussures_de_ville');
INSERT INTO CATEGORIE VALUES ('chaussures_randonnee');
INSERT INTO CATEGORIE VALUES ('chaussures');
INSERT INTO CATEGORIE VALUES ('tennis');
INSERT INTO CATEGORIE VALUES ('vetements');
INSERT INTO CATEGORIE VALUES ('pantalons');

--Ajout de produits
INSERT INTO PRODUIT VALUES(11, 'chaussures de rando', 18, 'blablablalbalbla', 'https://chaussures', 'chaussures_randonnee');
INSERT INTO PRODUIT VALUES(12, 'chaussures de ville', 21, 'blablablalbalbla', 'https://chaussure_de_ville', 'chaussures_de_ville');
INSERT INTO PRODUIT VALUES(13, 'jeans', 20, 'blabla', 'https://jeans', 'pantalons');
INSERT INTO PRODUIT VALUES(14, 'chaussures de rando', 18, 'blablablalbalbla', 'https://chaussures', 'chaussures_randonnee');
INSERT INTO PRODUIT VALUES(15, 'chino', 25, 'blabla', 'https://chino', 'pantalons');
INSERT INTO PRODUIT VALUES(16, 'tn', 50, 'blablablalbalbla', 'https://tn', 'tennis');

--Caractéristiques
INSERT INTO CARACTERISTIQUE VALUES(11, 'pointure', '42');
INSERT INTO CARACTERISTIQUE VALUES(14, 'pointure', '41');
INSERT INTO CARACTERISTIQUE VALUES(16, 'pointure', '41');
INSERT INTO CARACTERISTIQUE VALUES(12, 'pointure', '38');
INSERT INTO CARACTERISTIQUE VALUES(11, 'couleur', 'marron');
INSERT INTO CARACTERISTIQUE VALUES(14, 'couleur', 'vert');
INSERT INTO CARACTERISTIQUE VALUES(16, 'couleur', 'gris');
INSERT INTO CARACTERISTIQUE VALUES(12, 'couleur', 'noir');
INSERT INTO CARACTERISTIQUE VALUES(13, 'couleur', 'bleu');
INSERT INTO CARACTERISTIQUE VALUES(13, 'taille', '40');
INSERT INTO CARACTERISTIQUE VALUES(15, 'taille', '38');

--Sous catégories
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

--On ajoute dans les remportés
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
