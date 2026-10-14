# UML Diagrams

These diagrams describe the Game Backlog Tracker as submitted for CA1. They are written in [Mermaid](https://mermaid.js.org/), which GitHub renders directly when you view this file.

## Class diagram

The app is split into three layers: the **model** (`GameModel`, `GameStatus`), **persistence** behind the `GameStore` interface (currently `GameMemStore`, shared through the `AppData` singleton), and the **UI** (three activities plus `GameAdapter` for the RecyclerView). The adapter does not modify data itself; it reports Edit and Delete taps back to `GameListActivity` through callback functions.

```mermaid
classDiagram
    direction LR

    class GameStatus {
        <<enumeration>>
        WISHLIST
        BACKLOG
        PLAYING
        COMPLETED
        DROPPED
        +label: String
    }

    class GameModel {
        <<data class>>
        +id: Long
        +title: String
        +platform: String
        +status: GameStatus
        +rating: Int
        +playtimeHours: Int
        +notes: String
    }

    class GameStore {
        <<interface>>
        +findAll() List~GameModel~
        +findOne(id: Long) GameModel?
        +create(game: GameModel)
        +update(game: GameModel) Boolean
        +delete(id: Long) Boolean
    }

    class GameMemStore {
        -games: ArrayList~GameModel~
        -lastId: AtomicLong
        +findAll() List~GameModel~
        +findOne(id: Long) GameModel?
        +create(game: GameModel)
        +update(game: GameModel) Boolean
        +delete(id: Long) Boolean
    }

    class AppData {
        <<object>>
        +games: GameStore
    }

    class MainActivity {
        +onCreate(savedInstanceState: Bundle?)
        +onResume()
        -updateStats()
        -showComingSoon()
    }

    class GameListActivity {
        -adapter: GameAdapter
        -emptyText: TextView
        +onCreate(savedInstanceState: Bundle?)
        +onResume()
        -refreshList()
        -editGame(game: GameModel)
        -confirmDelete(game: GameModel)
        -deleteGame(game: GameModel)
        -showComingSoon()
    }

    class AddEditActivity {
        -editingId: Long?
        +onCreate(savedInstanceState: Bundle?)
        -setupStatusSpinner()
        -loadExistingGame(id: Long)
        -saveGame()
    }

    class GameAdapter {
        -games: List~GameModel~
        -onEdit: GameModel → Unit
        -onDelete: GameModel → Unit
        +onCreateViewHolder(parent: ViewGroup, viewType: Int) GameViewHolder
        +onBindViewHolder(holder: GameViewHolder, position: Int)
        +getItemCount() Int
        +updateGames(newGames: List~GameModel~)
    }

    class GameViewHolder {
        +titleText: TextView
        +statusText: TextView
        +platformText: TextView
        +ratingBar: RatingBar
        +playtimeText: TextView
        +notesText: TextView
        +editButton: Button
        +deleteButton: Button
    }

    GameModel --> GameStatus : status
    GameStore <|.. GameMemStore : implements
    GameMemStore "1" o-- "*" GameModel : stores
    AppData --> GameStore : games

    MainActivity ..> GameListActivity : starts
    MainActivity ..> AddEditActivity : starts
    MainActivity ..> AppData : reads stats
    GameListActivity ..> AddEditActivity : starts (id extra)
    GameListActivity *-- GameAdapter : adapter
    GameListActivity ..> AppData : uses
    AddEditActivity ..> AppData : uses
    GameAdapter ..> GameViewHolder : creates
    GameAdapter ..> GameModel : displays
```

## Use case diagram

One actor, the player, manages their own backlog. Adding and editing both include title validation; deleting always includes a confirmation step. The dashed use cases are visible in the UI as placeholders and are planned for CA2.

```mermaid
flowchart LR
    player(("🧑 Player"))

    subgraph app ["Game Backlog Tracker"]
        direction TB
        view(["View game list"])
        stats(["View backlog summary"])
        add(["Add game"])
        edit(["Edit game"])
        del(["Delete game"])
        validate(["Validate title"])
        confirm(["Confirm deletion"])

        search(["Search games"])
        filter(["Filter by status"])
        sort(["Sort games"])
        cover(["Add cover art"])
        link(["Link gaming account"])
    end

    player --- view
    player --- stats
    player --- add
    player --- edit
    player --- del
    player -.- search
    player -.- filter
    player -.- sort
    player -.- cover
    player -.- link

    add -. "«include»" .-> validate
    edit -. "«include»" .-> validate
    del -. "«include»" .-> confirm

    classDef planned stroke-dasharray: 5 5,opacity:0.6
    class search,filter,sort,cover,link planned
```

## Sequence diagram: adding a game

```mermaid
sequenceDiagram
    actor Player
    participant Main as MainActivity
    participant Form as AddEditActivity
    participant Store as AppData.games (GameMemStore)

    Player->>Main: tap "Add game"
    Main->>Form: startActivity(Intent)
    Form->>Form: setupStatusSpinner()
    Player->>Form: enter details, tap "Save"
    Form->>Form: saveGame() validates title and playtime
    alt title is empty
        Form-->>Player: show "Title is required" error
    else input valid
        Form->>Store: create(game)
        Store->>Store: game.id = lastId.incrementAndGet()
        Form-->>Player: Toast "Game added"
        Form->>Form: finish()
        Main->>Store: findAll() in onResume() → updateStats()
    end
```

## Sequence diagram: deleting a game

```mermaid
sequenceDiagram
    actor Player
    participant List as GameListActivity
    participant Adapter as GameAdapter
    participant Store as AppData.games (GameMemStore)

    Player->>Adapter: tap "Delete" on a card
    Adapter->>List: onDelete(game)
    List->>List: confirmDelete(game)
    List-->>Player: AlertDialog "Remove … from your backlog?"
    alt Player taps Cancel
        Player->>List: dismiss dialog
    else Player taps Delete
        List->>Store: delete(game.id)
        Store-->>List: true
        List->>Store: findAll()
        List->>Adapter: updateGames(games)
        List-->>Player: Toast "… deleted"
    end
```
