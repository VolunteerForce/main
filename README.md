# Description

VolunteerHero is an app designed to enable you to easily find ways to engage with your local community and
volunteer for different events in your area of interest. With VolunteerHero, subscribing and keeping track of 
these events is easier than ever before! Pick your favorite cause along with a convenient time and location
to make a positive social impact. Whether it is a food drive, a charity run or helping out at the local animal
shelter, a few fun hours and you will make the world a better place.

VolunteerHero also makes it easier to find friends who are interested so that volunteering can be a fun, social
activity. Powered in part by the VolunteerMatch API, VolunteerHero will definitely make your day, your week much
brighter as you take the time to improve the lives of people around you, one step at a time.

Be not the hero your city needs, be the hero it deserves!


## Functional user stories

1. User can login using Google (maybe Facebook)** (core)**

    1. **Stretch**: add own account management (using phone number or email)

    2. Pull down profile image ** (core)**

2. User can browser list of volunteering opportunities nearby (GPS location required)** (core)**

    3. User can see list when not logged** (stretch)**

    4. In order to participate they need to login

3. User can see events on the map **(core)**

    5. **Stretch**: App can navigate user to the event location

4. User can click on event to see details **(core)**

    6. Mission statement **(core)**

    7. Time and date **(core)**

    8. Description  **(core)**

    9. Type (virtual vs in-person) **(core)**

    10. etc.

5. User can filter volunteering opportunities based on **(core)**

    11. **Stretch: **Search radius

    12. Dates **(core)**

    13. Key words **(core)**

    14. Type **(core)**

    15. **Stretch** Whether FB friends are volunteering

6. **Stretch** Volunteer can subscribe to push notifications about various events such as upcoming dates, changes in descriptions, etc…

7. Volunteer can unsubscribe from events **(core)**

8. Volunteer can see their events they are subscribed to **(core)**

9. User can share event to a friend using a link (maybe also sharing to FB) **Stretch**

10. Organizer can create volunteering event **(core)**

    16. Setting up date **(core)**

    17. Location **(core)**

    18. Image (banner) **(stretch)**

    19. Description **(core)**

    20. Type **(core)**

    21. num of volunteers **(core)**

    22. …

11. Organizer can delete their own event **(core)**

12. Organizer can edit event** (stretch)**

13. App will cache subscribed event locally **(stretch)**

14. App supports adding event to Android Calendar **(stretch)**

15. Volunteer can email organizer through the app **(core)**

16. Organizer can email all volunteers of the event **(core)**

17. Organizer can see all attendees **(stretch)**

18. User gets points/badges based on how many hours they have volunteered **(stretch)**


## Progress

Running doc here: https://docs.google.com/document/d/1F_mepCT9BaTCb3PPIhYkbBqLBUz3xmzM814sY_sGzK4/edit#

# Week of Oct 15 2017

<img src='https://github.com/VolunteerForce/main/blob/master/codepath-volunteerhero-sub-1.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />


[Imgur link WalkThrough](https://imgur.com/a/QISf5)

# Week of Oct 8 2017

Screens

[Login Screen](https://imgur.com/V9zddc5)

[Map View of nearby activities](https://imgur.com/ziC9zj6)

[List View of nearby activities - with action bar search](https://imgur.com/1HBpTVe)

[Event detail page](https://www.dropbox.com/s/qtspa9rii50kdvh/Screenshot%202017-10-07%2016.34.30.png?dl=0)

[Profile view of user to also display “My events”](https://imgur.com/ykAYJbw)

[Screen for event creation](https://www.dropbox.com/s/pdy6f8popubkm3y/Screenshot%202017-10-07%2016.36.26.png?dl=0)

Stretch: Basic settings screen (push notification etc)
Navigation view so that user can filter settings

## Video Walkthrough

Wire frames for screens:

<img src='https://github.com/VolunteerForce/main/blob/master/ScreenDesigns/VolunteerHero_wireframes.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [EZGIF](https://ezgif.com/maker/ezgif-3-837bdecf-gif-equalized).

## Demo example

Start on Login screen

User logs in using FB/Google

User sees list of all events

User switches tabs from list to map view

User opens Nav View and updates filters -> views (list and map) updates based on the filter

Clicking on event will open the detail page

User subscribes from the details page (or list view)

App asks if user wants to add the event into their calendar

User can share the event URL (deeplink) with other friends (email/phone/FB)

User create a new event with details such as location, type, description.(for demo purposes we can store this locally if they don’t allow us to publish)

