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
