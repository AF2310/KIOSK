Documentation:

Our Group (Clarke) consists of:
Achilleas Feratidis
Vincent Hawel
Jakob Hinze
Arseny Isaev
Vuk Vasovic
Ignas Vilimaitis
Michelle Weber

In this documentation we will document our thought process and our plans/doings to create the project as a team.
Our first meetup was on the 02.04.2025.
In this we discussed our inital toughts and ideas.
We also created the user/admin stories:
![alt text](references/KioskStories.png)

Then we split up into smaller groups to work on a design, database and class diagramm.
We met up again on the 08.04.2025 to check in on everybody discuss some more thoughts and talk about the use of Gitlab.
We are also using a Discord Server to always stay in touch.

For the design we used a Burger place as an example. The design was done with keynote.
We focused on the core mechanics and also a button with the ability to change the language.
[design draft](<references/Clarke Design v0.9.pdf>)

For the database we are using MySQL. In addition to storing the products we are also storing the list of ingredients
and a list of the ingredients which have been modified by the customer. With that feature we can allow customization of the ingredients.
We are also storing the orders in a table to make it possible to connect a kitchensystem in the future or to see the order history.
In addition to that we have an admin table with credentials which is linked to a role table to set permission in the form of Role Based access
controll.
We are also keeping logs to know who accessed the database and when to provide a bit of security.
![alt text](references/databasedesign.png)

Then we also created a UML class diagramm to have a bit of a plan of that classes we need, how they are connected and also which methods should be
in them. Because its such a big project the class diagramm will ofcourse change overtime especially the methods. But its good to have some kind of
a plan going into programming.
![alt text](references/classdiagram.png)


Sprint 1 (Week 17)

This week we worked on the welcome screen, main menu and admin login functionality, namely customer stories 1, 5 and admin story 5.

For the welcome screen Michelle and Arseny added the main elements and added button functionality to make them let the user travel to the other 2 screens.

For the admin screen Ignas and Jacob implemented the login functionality and set the scene for the admin menu. Ignas also worked on the populating the database and helping others with database connectivity.

The main menu screen was done by Vuk, Vince and Michelle.

Achilleas mainly worked on the database connectivity.

Michelle also worked on helping with database connectivity, merging and adjusting code according to checkstyle.

We had 3 meetings, 1 in person on Thursday, 1 on Friday and 1 on Monday on Discord.

We kept the sprint branch separately not to merge it without the scrum master.