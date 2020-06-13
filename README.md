# Android-Dev
This repository includes the sourcecode to the app that had to be developed for android during the Android Development class of VIA University College.

<hr>
<h2>Find the YouTube Video here: https://youtu.be/SGCrmQ1_Ia4</h2>
<hr>

<h3>For a better navigation throughout the app, and to understand each of the componants better, I have also created a Flowchart which you can find in this directory.</h3>


<strong>Please note:</strong>
Once you open the project, you are going to find a folder with deprecated classes. These were used to parse a JSON Object that was being received by the WIKIPEDIA API. Because it was not working as some of the objects had been nested up to 4 LEVELS!!!, I had decided to process the JSON Object instead, because it was more convenient and more than enough to implement my use case. (Subject: Networking)

#### Right at the moment, the amount of recipes & ingredients in the database is limited. Nevertheless it can always easily be expanded within the database.

<u><strong>More facts:</strong></u>
<ul>
  <li>The implemented Database holds the data for the ingredients and recipes</li>
  <li>I have mainly used activites for better user experience as the user can easily retrace his steps and perform changes.<br>This would be more difficult/annoying with fragments.</li>
  <li>I have also created a translation into German for this app.<br>You can find a screenshot in the root directory of this repository (or beneath).</li>
  <img src="https://github.com/danieldenk/Android-Dev/blob/master/German_Login.jpg" alt="Login in German" height="500px"/>
  <li>The app contains a login functionality for anonymous users next to a standard one.<br>For later development, I plan to include functionality to add own recipes that are linked to ones user.</li>
  <li>There is input validation which leads to: <br> <ul>
    <li>It is not possible to search for an empty list of ingredients.</li>
    <li>Only valid (known) Ingredients can be added to the list of ingredients.</li>
  </ul></li>
  <li>The background image that is used at the login, is a free to use background wallpaper for android.<br>Here I had to change the opacity.</li>
  <li>Some of the additional libraries I am using are: Guava and AndroidViewAnimations</li>
  <li>The source code includes a lot of comments about future development plans. Search for "TODO" to read more.</li>
  <li>You can also find the YouTube demonstration of the app beneath:</li>
  <a href="http://www.youtube.com/watch?feature=player_embedded&v=SGCrmQ1_Ia4" target="_blank"><img src="http://img.youtube.com/vi/SGCrmQ1_Ia4/0.jpg" 
alt="App Video Demo" width="280" height="200" border="10" /></a>
</ul>
