#  Application de Chat Java avec Sockets – Cas 3

##  Description

Ce projet est une application complète de chat Client-Serveur développée en Java en utilisant des sockets TCP.

Elle permet à plusieurs clients de communiquer entre eux et avec le serveur en temps réel.

L’application dispose d’une interface graphique développée avec Java Swing.

---

##  Objectifs

- Implémenter une architecture client-serveur
- Utiliser la communication par sockets TCP
- Gérer plusieurs clients à l’aide des threads
- Permettre au serveur de participer activement à la discussion
- Développer une interface graphique conviviale

---

##  Technologies Utilisées

- Java  
- TCP Sockets  
- Java Swing  
- Multithreading  
- Eclipse IDE  

---

##  Fonctionnalités

- Messagerie en temps réel  
- Support multi-clients  
- Participation active du serveur  
- Gestion des noms d’utilisateur (pseudo)  
- Horodatage des messages  
- Différenciation des couleurs des messages  
- Défilement automatique (auto-scroll)  
- Bouton de déconnexion  
- Exportation de la discussion en fichier `.txt`  

---

##  Fonctionnement

1. Le serveur écoute sur le port **6000**.
2. Les clients se connectent au serveur.
3. Chaque client est géré par un thread dédié.
4. Les messages sont envoyés au serveur puis diffusés aux autres clients.
5. Le serveur peut également envoyer des messages à tous les clients connectés.

---

##  Comment Exécuter le Projet

1. Exécuter `ServeurChatActif.java`
2. Lancer plusieurs instances de `ClientChatGUI.java`
3. Vérifier que le port **6000** est disponible

---

##  Auteur

**Madina Aidara L3 Génie Logiciel**
