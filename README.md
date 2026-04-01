# LAB-16-Ma-triser-les-Services-dans-une-Application-Android

Ce projet Android démontre comment implémenter un chronomètre persistant capable de fonctionner même lorsque l'utilisateur quitte l'application ou verrouille son écran. 

L'accent a été mis sur la **séparation des tâches** (Service vs Activité) et sur une **interface utilisateur (UI) "humaine"** et animée.

---

##  Fonctionnalités Clés

- **Persistance Totale** : Utilisation d'un `Foreground Service` pour empêcher le système de tuer le processus.
- **Notification Interactive** : Affichage du temps réel directement dans la barre de notifications.
- **UI Dynamique & Vivante** :
    - **Effet de Pulsation** : Le chrono "respire" (zoom léger) à chaque seconde pour confirmer visuellement son fonctionnement.
    - **Transitions Fluides** : Les boutons de contrôle utilisent des animations d'opacité (`AlphaAnimation`).
    - **Feedback Utilisateur** : Utilisation de `Toasts` personnalisés avec des emojis pour une interaction plus chaleureuse.
- **Synchronisation Automatique** : Grâce au `Service Binding`, l'interface se reconnecte instantanément à la valeur actuelle du service lors du retour sur l'application.

---

##  Architecture Technique

### 1. ChronometreService (`Service`)
C'est le cerveau de l'application. Il tourne indépendamment de l'interface.
- **Gestion du temps** : Utilisation d'un `ScheduledExecutorService` pour une précision accrue.
- **Communication** : Expose la méthode `getTempsEcoule()` via un `Binder` local.
- **Notification Channel** : Configuration obligatoire pour Android 8.0+ (Oreo).

### 2. MainActivity (`Activity`)
C'est l'interface de contrôle.
- **Handler & Runnable** : Mise à jour de l'affichage toutes les 1000ms.
- **Service Binding** : Connexion via `ServiceConnection` pour un échange de données direct.
- **Animations Android View** : Utilisation de `.animate().scaleX().scaleY()` pour l'effet de vie.

---

##  Structure des fichiers principaux

| Fichier | Rôle |
| :--- | :--- |
| `ChronometreService.java` | Logique métier, compteur de secondes et notification. |
| `MainActivity.java` | Gestion des clics, liaison au service et animations UI. |
| `activity_main.xml` | Design moderne avec `MaterialComponents`. |
| `circle_background.xml` | Drawable personnalisé pour l'aspect visuel circulaire. |

---

##  Comment installer et tester ?

1. Cloner le projet dans **Android Studio**.
2. S'assurer que les dépendances `com.google.android.material:material` sont présentes dans le `build.gradle`.
3. Lancer l'application sur un émulateur ou un appareil physique (**API 26+ recommandé**).
4. Appuyer sur **Démarrer** : une notification apparaît.
5. Quitter l'application : le temps continue de défiler dans la notification.
6. Revenir dans l'application : le compteur se synchronise automatiquement.

---

##  Concepts Android approfondis
- **Android LifeCycle** : Gestion du cycle de vie des services.
- **Inter-Process Communication (IPC)** : Utilisation des Binders.
- **User Experience (UX)** : Humanisation d'un utilitaire technique par le design et le feedback visuel.


<img width="367" height="804" alt="Capture d&#39;écran 2026-04-01 123347" src="https://github.com/user-attachments/assets/ea2dc58a-bbe7-486e-8525-9f2081107819" />

<img width="370" height="804" alt="Capture d&#39;écran 2026-04-01 123411" src="https://github.com/user-attachments/assets/39b81c7c-244a-47fe-8f40-75808520f0c3" />

<img width="373" height="830" alt="Capture d&#39;écran 2026-04-01 123422" src="https://github.com/user-attachments/assets/3d3347b6-cf07-49c4-ad42-29eaa71ffda4" />

---
##  Informations sur l'Auteur
*   **Nom** : BELLAFRIKH ZAYNAB
*   **Établissement** : EMSI (École Marocaine des Sciences de l'Ingénieur)
*   **Groupe** : CI G2
*   **Module** : Développement Mobile Avancé
---
*Réalisé dans le cadre du cours de Développement Mobile - Lab 16.*
