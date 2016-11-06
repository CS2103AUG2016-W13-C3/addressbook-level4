#Manual Testing Guidelines

You can find the sample data at `SampleData.xml` in the same folder as this document. Throughout this document, we will be referring to to-dos with a start and end datetime as _Events_. On the other hand, to-dos without any time constraints or with a single due date are referred to as _Tasks_. 

## Importing Sample Data

To start doing manual scripted testing, follow these steps:

1. Download `commando.jar` from our [releases](https://github.com/CS2103AUG2016-W13-C3/main/releases).
2. Copy `commando.jar` in a directory that would be the home directory for _CommanDo_
3. Double-click on `commando.jar`. You should have Java version `1.8.0_60` or later on your device - otherwise, download it [here](https://java.com/en/download/help/download_options.xml). You will notice that a `./data` folder would be created in the home directory for _CommanDo_ you chose.
4. Download `SampleData.xml` [here](https://raw.githubusercontent.com/CS2103AUG2016-W13-C3/main/master/src/test/data/ManualTesting/SampleData.xml) - this is our sample data file of more than 50 to-dos.
5. Copy `SampleData.xml` into the newly created `./data` folder in the home directory.
4. Switch back to _CommanDo_, type `>> import ./data/SampleData.xml` and hit <kbd>Enter</kbd>.
5. You have just used the `import` command to import our sample to-do list into _CommanDo_. You will see that the 2 panels will be populated with to-dos. The to-dos visible are only those which are undone - you can type ` >> recall` to see all to-dos that are done.

For more details on how _CommanDo_ works that is not described in this document, visit our [user guide](https://cs2103aug2016-w13-c3.github.io/main/user).

## Viewing Help: `help` command

Command | What should happen |
------- | :--------------
`>> help` | Our user guide will open up in a new window.
`>> help add` | Our user guide will open up in a new window, automatically jumping to the section of "Adding To-Dos" with the `add` command
`>> help cheatsheet` | Our user guide will open up in a new window, automatically jumping to the section of "Cheatsheet"

## Searching through Undone To-Dos: `find` command

In _CommanDo_, the default view is that every upcoming _Event_ is listed in the left panel and every unfinished _Task_ is listed in the right panel. To search through this list of undone to-dos, you use the `find` command:

Command | What should happen |
------- | :--------------
`>> find final` | Only upcoming _Events_ and unfinished _Tasks_ with description or tags containing "final" will be listed, in chronological order.
`>> find #2105` | Only upcoming _Events_ and unfinished _Tasks_ with a tag of "#2105" will be listed, in chronological order.
`>> find` | All upcoming _Events_ and finished _Tasks_ will be listed, in chronological order (default view).

## Browsing To-Dos that are Done: `recall` command

Any _Event_ that is over, or any _Task_ that has been marked done will be hidden and archived from the next day onwards. To browse through these to-dos that are considered done that are not visible on the default view of _CommanDo_, you use the `recall` command:

Command | What should happen |
------- | :--------------
`>> recall` | Only past _Events_ and finished _Tasks_ will be listed, in reverse chronological order.
`>> recall 2105` | Only past _Events_ and finished _Tasks_ with description or tags containing "2105" will be listed, in reverse chronological order.
`>> recall #homework` | Only past _Events_ and finished _Tasks_ with a tag "#2105" will be listed, in reverse chronological order.

Type `>> find` to return to the default view of all upcoming _Events_ and unfinished _Tasks_

## Listing To-Dos Within Time Window: `list` command

To browse through both done and undone to-dos within a specific time interval, you use the `list` command:

Command | What should happen |
------- | :--------------
`>> list` | Shows all to-dos, done and undone.
`>> list from today to next week` | Shows all _Events_ and _Tasks_ that have time windows or due dates falling within the period between the current day, 0000h, to the Sunday of the week after, 2359h.
`>> list on dec` | Shows all  _Events_ and _Tasks_ that have time windows or due dates falling within the period between the current year's Dec 1, 0000h, to Dec 31, 2359h.

Type `>> find` to return to the default view of all upcoming _Events_ and unfinished _Tasks_

## Adding Events: `add` command

An _Event_ is a to-do happening within a time window. When an _Event_ is over, it will drop down to the bottom of the list, greyed out. On the next day, _CommanDo_ will hide it for you.

Command | What should happen |
------- | :--------------
`>> add wild party in hostel room from Dec 2016 to 1 Jan 2017` | A to-do will appear in the left panel, tinted yellow, which signifies that it is newly added. You might need to scroll down the _Events_ panel, possible by pressing <kbd>Tab</kbd> to switch focus to that panel and pressing the Down arrow key. It will have the description "wild party in hostel room", accompanied by a time window of 31 Dec 2016, 0000h, to 1 Jan 2017, 2359h. If it doesn't appear, it could mean that it has past, and you'll need to type `>> recall` to view it.
`>> add physics revision from tues 9.30pm to 10pm weekly #physics` | A to-do will appear in the left panel.  It will have the description "physics revision", accompanied by a time window of the current day's coming Tuesday 2130h to 2200h, tagged with "#physics" and a "Weekly" recurrence.
`>> add tour with Jimmy on next Saturday` | A to-do will appear in the left panel. It will have the description "tour with Jimmy", accompanied by a time window of the current day's next Saturday 0000h to 2359h.

## Adding Tasks: `add` command

A _Task_, on the other hand, is an activity that can be done at your own time or by some due date. When you have completed a _Task_, you can mark it as done with the `finish` command.

Command | What should happen |
------- | :--------------
`>> add shop for groceries: banana, pineapple, watermelon #housework` | A to-do will appear in the right panel. It will have the description "add shop for groceries: banana, pineapple, watermelon" and has the tag "#housework". You might also need to scroll down the _Tasks_ panel to see the to-do.
`>> add finish FYP by tomorrow 2359h` | A to-do will appear in the right panel.  It will have the description "finish FYP", accompanied by a due date of the day after the current, 2359h.
`>> add submit math homework by wednesday 2359h weekly` | A to-do will appear in the right panel. It will have the description "submit math homework", accompanied by a due date of the



`>> help add` | Our user guide will open up in a new window, automatically jumping to the section of "Adding To-Dos" with the `add` command
`>> help cheatsheet` | Our user guide will open up in a new window, automatically jumping to the section of "Cheatsheet"


add task1 on today | A task with name `task1` and starting date `today` is added to the top of the list. <br> The default time is `00:00 hrs` <br> The task should appear at the Today panel on the right.
add task2 on today 1800 by tomorrow 1900 | A task with name `task2`, starting date `today` and ending date `tomorrow` is added to the top of the list. <br> The starting time is `18:00 hrs` and the ending time is `19:00 hrs`<br> `task2` should appear at the Today panel and the Next 7 Days panel on the right.
add task3 on 12/25/2017 1800 by 12/26/2017 1900 priority high every week| A task with name `task3`, starting date `12/25/2017` and ending date `12/26/2017` is added to the top of the list. <br> The starting time is `18:00 hrs` and the ending time is `19:00 hrs`<br> The priority circle should be red. <br> The recurrence label should display `Every: WEEK` <br>

## Using the Add Command

Command | Expected Results |
------- | :--------------
add floating task| A floating task with name `floating task` is added to top of the list.
add task1 on today | A task with name `task1` and starting date `today` is added to the top of the list. <br> The default time is `00:00 hrs` <br> The task should appear at the Today panel on the right.
add task2 on today 1800 by tomorrow 1900 | A task with name `task2`, starting date `today` and ending date `tomorrow` is added to the top of the list. <br> The starting time is `18:00 hrs` and the ending time is `19:00 hrs`<br> `task2` should appear at the Today panel and the Next 7 Days panel on the right.
add task3 on 12/25/2017 1800 by 12/26/2017 1900 priority high every week| A task with name `task3`, starting date `12/25/2017` and ending date `12/26/2017` is added to the top of the list. <br> The starting time is `18:00 hrs` and the ending time is `19:00 hrs`<br> The priority circle should be red. <br> The recurrence label should display `Every: WEEK` <br>



##Datetime Formats
Before you begin adding or editing todos with datetimes, have a quick brief on the formats of date and times accepted.

For dates, you must follow this format : `Day of the month, Month, Year`. * `28/12/2018`, `3 feb 2019`

* You may choose to leave out the year if the date is in the current year.
* You may leave out the whole date if the date is today.
* You may also state relative dates such as `Today`, `Yesterday`, `Tomorrow`, `next/last/coming <weekday/month/year>`.

For times, you must follow either of these formats : `HH:MM`, `HHMMh`, `HHam/pm`. * `23:59`, `2350h`, `9am`

* You may state relative times such as `Morning`, `Afternoon`, `Evening`, `Night`, `Midnight`

The date must come before the time.

##Adding
To add an event or a task, you must start your command with a `add` keyword.

###Events
When adding events, the `from` and `to` keywords, or `on` keyword must be provided, followed by a suitable datetime format.

```
> add <description> from <start_datetime> to <end_datetime>
> add <description> on <datetime>
```

* you must have the keywords `add`, `from` and `to` to tell the application to add an event.
* the description must not be empty.
* the datetimes must be valid. (See Datetime Formats)
* the start datetime must be after the end datetime.

An error will be thrown if any of the conditions are not met, along with detailed formats of the `add` command.

###Tasks
When adding tasks, the `by` keywords must be provided, followed by a suitable datetime format.

```
> add <description> by <end_datetime>
```

* the description must not be empty. (Like events)
* `by <end_datetime>` is optional. You can add a task without a deadline. It becomes a floating task.
* if the datetime is present, the datetime must be valid. (See Datetime Formats)

###Recurrence
Only events and tasks with deadlines can have recurrences. 

* the recurrence keywords, `daily/weekly/monthly/yearly` are parsed as recurrence only if there are datetime(s) in the command.
* the recurrence keywords must be added to the end of the command, right after the datetime.
* the length of the event must not exceed the recurrence interval, or an error will be thrown.

```
> add <description> from <start_datetime> to <end_datetime> <recurrence>
> add <description> by <end_datetime> <recurrence>
```

###Tagging
Any todo can have tags.
Tags will appear on the right side of the todo, to the left of the datetime if there is a datetime.

* to indicate that the word is a tag, put a `#` in front of it, together.
* to put multiple tags, separate them by spaces.
* the tags must appear after both datetimes and recurrence, if either or both fields exist.

```
> add <description> from <start_datetime> to <end_datetime> <recurrence> #tag1 #tag2...
> add <description> from by <end_datetime> <recurrence> #tag1 #tag2...
```

##Finding
To search for todos containing substring(s), keyword(s), tag(s) or any combination of them, you need a `find` keyword.
Note that this will only return todos that are not archived.

* the search conditions can be in any order, as long as they are separated by spaces.

```
> find <substring> <keyword> <tag>
```

##Editing
To edit any todo, you would just need the index, which is a number attached to each visible todo.
The numbers will change with every modification to the visible lists.
The order would be the visible events first, followed by visible tasks with deadlines, and visible floating tasks.
Within this 3 groups, they are numbered in chronological order.

###Changing Description
To change the description of any todo, just provide a `edit` keyword at the start of your command.

* the new description must not be empty, it will throw an error

```
> edit <index> <new_description>
```

###Changing Tags
The general editing format applies to this as well.

* all tags that are attached to the todo will be replaced by the new tags provided

```
> edit <index> <tags>
```

###Changing Datetimes and Recurrences
The recurrence field cannot be edited directly; it must be edited along with the datetimes field.
Floating tasks can be converted to events or deadlines by editing in the respective fields.

* tasks with deadlines cannot be changed to an event directly, and vice versa.
* to remove datetimes, please refer to the delete command, which is after this command.

```
> edit <index> from <start_datetime> to <end_datetime> <recurrence>
> edit <index> by <end_datetime> <recurrence>
```

##Deleting
To delete a todo, the `delete` keyword must be provided.
Any editable fields, with the exception of description, can be deleted.

###Deleting Tags
Currently, all tags related to this particular todo will be deleted with this command.

```
> delete <index> tags
```

###Deleting Datetimes
Recurrences are meaningless without datetime(s), and hence will be removed along with the datetime(s).

```
> delete <index> datetime
```

###Deleting Recurrences

```
> delete <index> recurrence
```

##Marking todos done
To mark a todo done, the `finish` keyword must be provided.
The todo will then be shifted to the bottom of the list and greyed out.

* events cannot be marked done.
* finished tasks cannot be marked finished again.

```
> finish <index>
```

##Marking todos not done
To mark a todo not done, the `unfinish` keyword must be provided.

* events cannot be marked not done.
* unfinished tasks cannot be marked unfinished again.

```
> unfinish <index>
```

##Undo
This command will rollback the latest change made to the todo list.
The history of edits extend back to the start of this session.

```
> undo
```

##Redo
Redo the last undo.

```
> redo
```

##Recall
This command lists all todos that are already done.
The format is similar to the search command.

```
> recall <substring> <keyword> <tag>
```

##List
This command lists all todos, including todos that are already done. 

```
> list
> list on <datetime>
> list to <end_datetime>
> list from <start_datetime>
> list from <start_datetime> to <end_datetime>
```

##Export
After making edits to the save file, you can export the data into another .xml file.
If no full filepath is specified, it is taken as a relative filepath to the commanDo.jar file.
Note that this does not change the default save location, as this command clones the save file in another location.

```
> export .\src\test\data\ManualTesting\newData.xml
```

##Store
This command allows you to change the default save location.
If the folders specified in the new filepath does not exist, they will be created.
Note that the save file in the old save location remains.

```
> store .\src\test\data\ManualTesting\Test\newData.xml
```

##Help
This command opens up a link to the user guide of this application.
If a keyword is provided after the command keyword, the application will attempt to navigate to the header in the user guide.
List of possible keywords include: ...

* If the keyword does not exist, an error will be returned, showing the list of possible keywords you can provide.

```
> help
> help <keyword>
```
