# Description

## Functional user stories

1. User can login using Google (maybe Facebook)

    1. Stretch: add own account management (using phone number or email)

    2. Pull down profile image

2. User can browser list of volunteering opportunities nearby (GPS location required)

    3. User can see list when not logged

    4. In order to participate they need to login

3. User can see events on the map

    5. Stretch: App can navigate user to the event location

4. User can click on event to see details

    6. Mission statement

    7. Time and date

    8. Description 

    9. Type (virtual vs in-person)

    10. etc.

5. User can filter volunteering opportunities based on:

    11. Search radius

    12. Key words

    13. Type

    14. Stretch: Whether FB friends are volunteering

6. Stretch: Volunteer can subscribe to push notifications about various events such as upcoming dates, changes in descriptions, etc…

7. Volunteer can unsubscribe from events

8. Volunteer can see their events they are subscribed to

9. User can share event to a friend using a link (maybe also sharing to FB)

10. Organizer can create volunteering event

    15. Setting up date

    16. Location

    17. Image (banner)

    18. Description

    19. Type

    20. number of volunteers

    21. ...

11. Organizer can delete their own event

12. Organizer can edit event

13. App will cache subscribed event locally

14. App supports adding event to Android Calendar

15. Volunteer can email organizer through the app

16. Organizer can email all volunteers of the event

17. Organizer can see all attendees

Screens

Login Screen

Map View of nearby activities

List View of nearby activities - with action bar search 

Event detail page

Navigation view so that user can filter settings

Profile view of user to also display “My events”

Screen for event creation

![image](https://photos-5.dropbox.com/t/2/AAAgHLLmrH6eQsQirahx2vOvjFzMg1edwLIALb3v3NqjqQ/12/10477072/png/32x32/1/_/1/2/Screenshot%202017-10-07%2016.34.30.png/EKnd6AcY-sEDIAIoAg/wdMXUj19OrrpQiB9v5lV8MEP03lG-VIRXDy_DtLKt2w?size=800x600&size_mode=3)

Stretch: Basic settings screen (push notification etc)


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

