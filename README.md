# E-TRON
Armagetron-Advanced Like designed to be played on a movie room.


/*---------------------------------------------------------------------------------------------------------------------------------*\
  This project was carried out in partnership with a cinema, each player connecting via a web page and playing from a single screen
\*---------------------------------------------------------------------------------------------------------------------------------*/
  * [bonsoirlina](https://github.com/bonsoirlina)
  * [senisonn](https://github.com/senisonn) (Me : Lead Dev)
  * [TheAxeblack](https://github.com/TheAxeblack)
  * [asmaenoufoussi](https://github.com/asmae.nf)
  * [ryuma695](https://github.com/ryuma695)


/*Goal of the game*/
  -The goal is to be the last alive.
  -Player : 
            - You have 3 lives, a trail and a speed.
            - You loose a life by colliding to the window borders, by colliding on another player trail or an obstacle.
            - You gain a life point if someone hit your trail
  -Each 20 seconds a bonus spawn on the map :
            -Trail length bonus.
            -Speed bonus
            -+1 HP bonus
  -At the beginning of the game each player has 60 sec of invincibility

/*User Manual*/
  If you want to play on your own Computer : 
      -Replace the IP adress specified in App.java and App.js by your WIFI ip adress.
      -Compile the project by using gradle with : ./gradlew shadowJar Install Gradle (https://gradle.org/install/)
      -Execute it by using : java -jar build/libs/yourJar.jar
      -You will need Apache or LiveShare(VSCode) to run your website in your WIFI.
      -If someone want to play with you he has to be on the same WIFI and go to : http://yourIpAdress:yourPort/yourFilePath/index.html
    

    
