# Maze Solver



## Getting Started


### Prerequisites

* Latest Java JRE
* Latest Java JDK


### Running the program

Use a command line or terminal to "CD" to the directory you have the file downloaded in.
run the java command to run the file


```
cd "Some Directory"
```
<br/>
```
java –cp ./game.jar ie.gmit.sw.ai.GameRunner
``` 

Game should then start.

#### Multi-Threading Implementation

The first thread apart from the main thread that starts, creates the view and controls the players movement, it also paints the canvas. When the spiders are create each spider creates a new thread, it will then use an executor to keep the thread alive and repeat specific methods. This repeats every two seconds but will allow the previous execution to complete without interrupting it or creating another thread in its place.


#### Fuzzy Logic Implementation

Fuzzy logic is used to take the amount of strength a spider has when they attack the player.
The strength of the spider decreases as the spider travels. The spider also starts with 100 venom points and they decrease when the spider attacks the player.
Fuzzy logic is used to determine if the attack is stronger, average or weak based on the spiders strength. the venom is used to determine how potent the venom is and both values are used to determine the overall damage the player takes when attacked. 

#### Encogg Implementation

I have implement Encogg to determine the state that the spider should be in while playing. After the Neural network has been trained, the network will classify the state after it has been passed the values that will identify if the spider still has strength points, if the spider still has venom. It also passes the values to identify if the spider is standing next to the player, or if it has reached the hiding spot, where it can heal.
An enum is then used like a finite state machine to allow the spider to only perform one of the operations at a time.

#### Heuristic to find player

I used a Manhattan distance heuristic to find the player in the grid. It checks the array at the point where the spider is standing, then it checks the points neibours, it determmines which point will be closer to the player. Eg, if going left will be a closer point to the player, it will take that direction over the others. Due to the threading and the executors, this will be updated every two seconds and the spider will move to the next step, until the Encog network informs the spider to change state.


## Issues

* The biggest issue was that the encog network is giving incorrect values at times, this causes the player to be attacked when no spider is near by, this is due to the network predicting an incorrect value. 


## Authors

* **Michael Kidd** - *work* - [https://github.com/Michael-Kidd/AI-Project](https://github.com/Michael-Kidd/AI-Project)

## Acknowledgments

* Dr. John Healy for course work and initial code samples.