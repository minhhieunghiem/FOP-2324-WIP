# The Settlers of Catan – FOP WS23/24 Project

A Java-based implementation of the board game *Catan*, developed for the **Functional and Object-Oriented Programming Concepts** (FOP) course at TU Darmstadt, Winter Semester 2023/24.

## Project Overview

This project is part of the FOP course and aims to apply functional and object-oriented programming concepts through the development of a strategic board game. The game is based on **“The Settlers of Catan”** and is structured using the Model-View-Controller (MVC) design pattern.

## Current Progress

### H1: Model Implementation (20 Points)

The first part of the assignment focused on implementing the entire game model, including:

- `PlayerImpl`:
  - Resource management (`addResource`, `removeResources`, etc.)
  - Development cards (`addDevelopmentCard`, `getKnightsPlayed`, etc.)

- `HexGridImpl` & `EdgeImpl`:
  - Road-building functionality (`addRoad`, `getConnectedRoads`, etc.)

- `IntersectionImpl`:
  - Building and upgrading settlements (`placeVillage`, `upgradeSettlement`)

The implementation adheres to the provided interfaces and makes use of Java standard library features such as `Map`, `Set`, `Optional`, `Stream`, etc.

### Task H2: Controller Logic and Game Loop (26 Points)

Implemented the controller logic responsible for managing the game flow:

- Game start logic with firstRound(), allowing each player to place two settlements and roads
- regularTurn(), allowing players to take normal actions until they end their turn
- diceRollSeven(), handling logic for when a 7 is rolled (dropping cards, robber movement, and stealing)
- distributeResources(), distributing resources to players based on dice roll
- offerTrade(), managing trade logic between players with validation and responses

### Task H3: Graphical User Interface (24 Points)

Implemented GUI interaction and state updates:

- Enabled and disabled buttons for valid player actions
- Highlighted valid intersections and edges for building
- Connected GUI input to game actions using JavaFX properties
- Implemented player creation menu with color selection, bot toggle, and add/remove functionality
- Updated GUI based on PlayerObjective, including stealing, selecting robber tile, and playing development cards
