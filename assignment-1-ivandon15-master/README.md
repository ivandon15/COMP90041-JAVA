# COMP90041 - Assignment 1

This repository contains the skeleton code and test files for the assignment 1 submission on GitHub.

Don't know about version control? Click [here](https://www.atlassian.com/git/tutorials/what-is-version-control).

Need a refresher on GitHub? Click [here](https://guides.github.com/activities/hello-world/).

This assignment is due on:
5pm (AEST), September 17, 2020 (late-submission penalties apply).

## NIM
This project is the first of two, with the objective of designing and implementing a simple variant of the ancient game known as Nim. The game will be written using Java and played as a text-based game through the standard output console. 

Here is an example run of the program:

    Welcome to Nim
    	
    Please enter a command to continue

    $ start

    Please enter Player 1's name : Luke
    Please enter Player 2's name : Han
    Enter upper bound : 3
    Enter initial number of stones : 12

    12 stones left : * * * * * * * * * * * *
    Luke's turn. Enter stones to remove : 3

    9 stones left : * * * * * * * * *
    Han's turn. Enter stones to remove : 1

    8 stones left : * * * * * * * *
    Luke's turn. Enter stones to remove : 1

    7 stones left : * * * * * * *
    Han's turn. Enter stones to remove : 2

    5 stones left : * * * * *
    Luke's turn. Enter stones to remove : 3

    2 stones left : * *
    Han's turn. Enter stones to remove : 1

    1 stones left : *
    Luke's turn. Enter stones to remove : 1

    Game Over
    Han wins!

    Do you want to play again (Y/N): Y
    Enter upper bound : 5
    Enter initial number of stones : 15

    15 stones left : * * * * * * * * * * * * * * *
    Luke's turn. Enter stones to remove : 1

    14 stones left : * * * * * * * * * * * * * *
    Han's turn. Enter stones to remove : 2

    12 stones left : * * * * * * * * * * * *
    Luke's turn. Enter stones to remove : 3

    9 stones left : * * * * * * * * *
    Han's turn. Enter stones to remove : 4

    5 stones left : * * * * *
    Luke's turn. Enter stones to remove : 5

    Game Over
    Han wins!

    Do you want to play again (Y/N): N
    Luke won 0 game out of 2 played
    Han won 2 games out of 2 played
    $ 
    $ exit
    
    Thank you for playing Nim