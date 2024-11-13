### Structure de Package "player"

```
player/                             // Package principal pour le joueur
├── animations/                     // Gestion des animations du joueur
│   ├── spritesheet/                // Traitement des spritesheets
│   │   ├── BodySpriteSheet.java    // Spritesheet pour le corps
│   │   ├── HairSpriteSheet.java    // Spritesheet pour les cheveux
│   │   ├── HatSpriteSheet.java     // Spritesheet pour le chapeau
│   │   ├── OutfitSpriteSheet.java  // Spritesheet pour la tenue
│   │   └── SpriteSheet.java        // Classe de base pour gérer les spritesheets
│   ├── AnimationController.java    // Gestion des animations du joueur
│   ├── AnimationState.java         // États d'animation (marche, attaque, etc.)
│   ├── CustomAnimation.java        // Classe pour les animations personnalisées
│   └── Direction.java              // Enum pour la direction du joueur
├── attacks/                        // Gestion des attaques du joueur
│   └── Attack.java                 // Classe pour les attaques
├── components/                     // Composants du joueur (cheveux, tenue, etc.)
│   ├── CollisionComponent          // Composant de collision
│   ├── Body.java                   // Composant pour le corps
│   ├── Outfit.java                 // Composant pour la tenue
│   ├── Hair.java                   // Composant pour les cheveux
│   ├── Hat.java                    // Composant pour le chapeau
│   ├── Tool1.java                  // Composant pour l'outil 1
│   └── Tool2.java                  // Composant pour l'outil 2
├── equipment/                      // Gestion de l'équipement
│   └── Tool.java                   // Classe de base pour les outils
├── rendering/                      // Rendu graphique
│   ├── Layer.java                  // (-)Classe pour définir un calque de rendu
│   └── Renderer.java               // Classe principale pour le rendu
├── states/                         // États du joueur (marche, attaque, etc.)
│   ├── StandingState.java          // État de repos
│   ├── WalkingState.jave           // État de marche
│   ├── RunningState                // État de course
│   ├── AttackingState.java         // État d'attaque
│   └── State.java                  // Interface ou classe abstraite de base pour les états
├── utils/                          // Utilitaires divers pour le joueur
│   ├── Constants.java              // Constantes pour faciliter la gestion
│   └── SpriteUtils.java            // (-)Méthodes utilitaires pour manipuler les sprites
└── Player.java                     // Classe principale du joueur
```

### Description détallée

1. **`animations/`**: Ce package contient les classes nécessaires pour gérer les animations du joueur. `AnimationController` est la classe principale qui gère les animations du joueur. `AnimationState` est une énumération des différents états d'animation possibles (marche, attaque, etc.). `CustomAnimation` est une classe pour les animations personnalisées. `Direction` est une énumération pour la direction du joueur.
2. **`attacks/`**: Ce package contient la classe `Attack` qui définit les attaques du joueur.
3. **`components/`**: Ce package contient les composants du joueur tels que le corps, les cheveux, la tenue, le chapeau, etc. Chaque composant est une classe Java qui définit les propriétés et les méthodes associées à ce composant.
4. **`equipment/`**: Ce package contient la classe `Tool` qui est la classe de base pour les outils que le joueur peut équiper.
5. **`rendering/`**: Ce package contient les classes nécessaires pour le rendu graphique du joueur. `Layer` est une classe pour définir un calque de rendu. `Renderer` est la classe principale pour le rendu du joueur.
6. **`states/`**: Ce package contient les différents états du joueur tels que l'état de repos, l'état de marche, l'état de course
7. **`utils/`**: Ce package contient des utilitaires divers pour le joueur. `Constants` contient des constantes pour faciliter la gestion. `SpriteUtils` contient des méthodes utilitaires pour manipuler les sprites.
8. **`Player`**: La classe principale du joueur qui contient les propriétés et les méthodes associées au joueur.
