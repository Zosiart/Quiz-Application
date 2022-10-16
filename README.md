# Quizzzz!

You are witnessing one of the greatest innovations of quiz-type games.

We here at quizzzz.ly believe in sustainability. Our mission is to promote smart and conservative
usage of resources so humankind can prosper for many centuries to come.

## Description of project

Quizzzz! is a quiz-type game which rewards good knowledge of the energy consumptions of everyday activities.

The game supports
- Playing alone for the highest score
- Competing against other people in real-time
- Private servers with custom activity-sets
- Easy to use admin tools to add more activities

## Group members

| Profile Picture | Name | Email |
|---|---|---|
| ![](https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/4517/avatar.png?width=400) | Stanisław Howard | S.M.Howard@student.tudelft.nl |
| ![](https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/4773/avatar.png?width=400) | Toma Volentir | T.Volentir@student.tudelft.nl |
| ![](https://cdn.discordapp.com/attachments/943585524654936145/943585743555661886/avatar.png) | Zofia Rogacka-Trojak | z.rogacka-trojak@student.tudelft.nl |
| ![](https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/4948/avatar.png?width=400) | Zhenya Yancheva | Z.Y.Yancheva-1@student.tudelft.nl |
| ![](https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/4862/avatar.png?width=400) | Timur Mukminov | T.I.Mukminov@student.tudelft.nl |
| ![](https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/4669/avatar.png?width=400) | Robert Kurvits | R.Kurvits@student.tudelft.nl |


## How to run it

Running the client:
- Download the client from Deployments -> Releases
- Read the readme.txt file
- Launch the game
- Connect to a server (by default connected to http://localhost:8080)
- Play!

Running a server:
- Download the server from Deployments -> Releases
- Read the readme.txt file
- Start the server
- Add activities via the client with a .json file of activities
  - Optional: Add images to the paths you specified when adding activities to "./server/src/main/resources/activity-bank-pictures/" starting from the root of the server
- Play!

Alternatively:
- Clone the branch
- Do ./gradlew run to launch the client
- Do ./gradlew bootRun to launch the server
- Add the activities via Postman by posting a list to "/api/activities/add"

We recommend using the activity bank for activities and images for the smoothest experience!

Note: "Connection failed" is the default error message when the server fails to provide the requested information.
This can be caused by the server not having or finding enough activities in the database.
The minimum requirement is 5 activities with distinct consumptions and 2 activities with equal activities

## How to contribute to it

- Fork to a private branch
- Add things, there's loads to improve!
  - Improve jokers in multiplayer
  - Improve question generation
  - And much more!
- Submit a merge request, our team will give you feedback in no time!

## Copyright / License (opt.)
