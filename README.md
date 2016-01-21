# MoviesApp


# IMPORTANT NOTES:
In case you don't want to read what this project included please read the following notes:
- You will find it in 3 dots settings > Select from "Movies Sorted By:" SharedPreferences list.
- Favorite Movies is one of "Movies Sorted By:" SharePreferences list items, found in Settings.
- The ContentProvider used in this project was implemented step by step with its unit testing, without any use of external APIs.



You will find in this project the following Meeting Criteria:

# STAGE 1:

* User Interface - Layout:
---------------------------
- Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails
- UI contains an element (i.e a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated
Note: You will find it in 3 dots settings > Select from "Movies Sorted By:" SharedPreferences list.
- UI contains a screen for displaying the details for a selected movie
- Movie details layout contains title, release date, movie poster, vote average, and plot synopsis. 

* User Interface - Function:
---------------------------
- When a user changes the sort criteria (“most popular and highest rated”) the main view gets updated correctly.
- When a movie poster thumbnail is selected, the movie details screen is launched
- In a background thread, app queries the /discover/movies API with the query parameter for the sort criteria specified in the settings menu. (Note: Each sorting criteria is a different API call.)



# STAGE 2:

* Network API Implementation:
------------------------------
- In a background thread, app queries the /discover/movies API with the query parameter for the sort criteria specified in the settings menu. (Note: Each sorting criteria is a different API call.)
- App requests for related videos for a selected movie via the /movie/{id}/videos endpoint in a background thread and displays those details when the user selects a movie. 
- App requests for user reviews for a selected movie via the /movie/{id}/reviews endpoint in a background thread and displays those details when the user selects a movie.

* Data Persistence:
--------------------
- App saves a “Favorited” movie to SharedPreferences or a database using the movie’s id.
- When the “favorites” setting option is selected, the main view displays the entire favorites collection based on movie IDs stored in SharedPreferences or a database.
Note: Favorite Movies is one of "Movies Sorted By:" SharePreferences list items, found in Settings.


# To receive “exceeds specifications”:

* ContentProvider:
-------------------
- App persists favorite movie details using a database
- App displays favorite movie details even when offline
- App uses a ContentProvider* to populate favorite movie details
Note: This ContentProvider implemented step by step without any use of external Apis with its unit testing.


* Sharing Functionality:
-------------------------
- Movie Details View includes an Action Bar item that allows the user to share the first trailer video URL from the list of trailers
- App uses a share Intent to expose the external youtube URL for the trailer

