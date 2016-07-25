# Flickstop
Flicks Android app

This is an Android application for displaying list of movies in different categories - 'Now playing', 'Popular', 'Upcoming' and 'Top Rated'.

Time spent: 20 hours spent in total

Completed:

- User can view a list of movies (title, poster image, and overview) currently playing in theaters from the Movie Database API.

- Lists should be fully optimized for performance with the ViewHolder pattern.

- Views should be responsive for both landscape/portrait mode.

  - In portrait mode, the poster image, title, and movie overview is shown.

  - In landscape mode, the rotated layout should use the backdrop image instead and show the title and movie overview to the right of it.
  
  
The following addditional stories have been implemented:

Add pull-to-refresh for popular stream with SwipeRefreshLayout (1 point)
Display a nice default placeholder graphic for each image during loading (read more about Picasso) (1 point)
Improve the user interface through styling and coloring (1 to 5 points depending on the difficulty of UI improvements)
For popular movies (i.e. a movie voted for more than 5 stars), the full backdrop image is displayed. Otherwise, a poster image, the movie title, and overview is listed. Use Heterogenous ListViews and use different ViewHolder layout files for popular movies and less popular ones. (2 points)
Stretch: Expose details of movie (ratings using RatingBar, popularity, and synopsis) in a separate activity. (3 points)
Stretch: Allow video posts to be played in full-screen using the YouTubePlayerView (2 points)
When clicking on a popular movie (i.e. a movie voted for more than 5 stars) the video should be played immediately.
Less popular videos rely on the detailed page should show an image preview that can initiate playing a YouTube video.
Stretch: Add a play icon overlay to popular movies to indicate that the movie can be played (1 point).
Stretch: Apply the popular ButterKnife annotation library to reduce view boilerplate. (1 point)
Stretch: Add a rounded corners for the images using the Picasso transformations. (1 point)

Extra:

Added Navigation drawer to display additional categories of movies.
Added Language field to the details screen
Displayed Vote average as text instead of rating bar as it is double valued.

Here's a walkthrough of implemented user stories:

<img src='https://github.com/mhareendra/Flickstop/blob/master/Flickstop_2.gif' width='' />

<img src='https://github.com/mhareendra/Flickstop/blob/master/Flickstop_3.gif' width='' />


