# Manual Testing Guidelines

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

Command to type | What should happen |
------- | :--------------
`>> help` | Our user guide will open up in a new window.
`>> help add` | Our user guide will open up in a new window, automatically jumping to the section of "Adding To-Dos" with the `add` command
`>> help cheatsheet` | Our user guide will open up in a new window, automatically jumping to the section of "Cheatsheet"

## Searching through Undone To-Dos: `find` command

In _CommanDo_, the default view is that every upcoming _Event_ is listed in the left panel and every unfinished _Task_ is listed in the right panel. To search through this list of undone to-dos, you use the `find` command:

Command to type | What should happen |
------- | :--------------
`>> find final` | Only upcoming _Events_ and unfinished _Tasks_ with description or tags containing "final" will be listed, in chronological order.
`>> find #2105` | Only upcoming _Events_ and unfinished _Tasks_ with a tag of "#2105" will be listed, in chronological order.
`>> find` | All upcoming _Events_ and finished _Tasks_ will be listed, in chronological order (default view).

## Browsing To-Dos that are Done: `recall` command

Any _Event_ that is over, or any _Task_ that has been marked done will be hidden and archived from the next day onwards. To browse through these to-dos that are considered done that are not visible on the default view of _CommanDo_, you use the `recall` command:

Command to type | What should happen |
------- | :--------------
`>> recall` | Only past _Events_ and finished _Tasks_ will be listed, in reverse chronological order.
`>> recall 2105` | Only past _Events_ and finished _Tasks_ with description or tags containing "2105" will be listed, in reverse chronological order.
`>> recall #homework` | Only past _Events_ and finished _Tasks_ with a tag "#2105" will be listed, in reverse chronological order.

Type `>> find` to return to the default view of all upcoming _Events_ and unfinished _Tasks_.

## Listing To-Dos Within Time Window: `list` command

To browse through both done and undone to-dos within a specific time interval, you use the `list` command:

Command to type | What should happen |
------- | :--------------
`>> list` | Shows all to-dos, done and undone.
`>> list from today to next week` | Shows all _Events_ and _Tasks_ that have time windows or due dates falling within the period between the current day, 0000h, to the Sunday of the week after, 2359h.
`>> list on dec` | Shows all  _Events_ and _Tasks_ that have time windows or due dates falling within the period between the current year's Dec 1, 0000h, to Dec 31, 2359h.

Type `>> find` to return to the default view of all upcoming _Events_ and unfinished _Tasks_.

## Adding Events: `add` command

An _Event_ is a to-do happening within a time window. When an _Event_ is over, it will drop down to the bottom of the list, greyed out. On the next day, _CommanDo_ will hide it for you. For some to-dos, you might need to scroll down the _Events_ panel, possible by pressing <kbd>Tab</kbd> to switch focus to that panel and pressing the Down arrow key. 

Command to type | What should happen |
------- | :--------------
`>> add wild party in hostel room from Dec 2016 to 1 Jan 2017` | A to-do will appear in the left panel, tinted yellow, which signifies that it is newly added. It will have the description "wild party in hostel room", accompanied by a time window of 31 Dec 2016, 0000h, to 1 Jan 2017, 2359h. If it doesn't appear, it could mean that it has past, and you'll need to type `>> recall` to view it.
`>> add physics revision from tues 9.30pm to 10pm weekly #physics` | A to-do will appear in the left panel.  It will have the description "physics revision", accompanied by a time window of the current day's coming Tuesday 2130h to 2200h, tagged with "#physics" and a "Weekly" recurrence.
`>> add tour with Jimmy on next Saturday` | A to-do will appear in the left panel. It will have the description "tour with Jimmy", accompanied by a time window of the current day's next Saturday 0000h to 2359h.

## Adding Tasks: `add` command

A _Task_, on the other hand, is an activity that can be done at your own time or by some due date. When you have completed a _Task_, you can mark it as done with the `finish` command.

Command to type | What should happen |
------- | :--------------
`>> add shop for groceries: banana, pineapple, watermelon #housework` | A to-do will appear in the right panel. It will have the description "add shop for groceries: banana, pineapple, watermelon" and has the tag "#housework". You might also need to scroll down the _Tasks_ panel to see the to-do.
`>> add finish FYP by tomorrow 2359h` | A to-do will appear in the right panel.  It will have the description "finish FYP", accompanied by a due date of the day after the current, 2359h.
`>> add submit math homework by wed 2359h weekly` | A to-do will appear in the right panel. It will have the description "submit math homework", accompanied by a due date of the current day's coming Wednesday, 2359h, and have the recurrence "Weekly".

## Deleting To-Dos and their Fields: `delete` command

Command to type | What should happen |
------- | :--------------
`>> delete 1` | The to-do with the index of "1" will be deleted.
`>> delete 1 4 5` | The to-dos with the indices of "1", "4" and "5" will be deleted.
`>> delete 1 - 5` | The to-dos with the indices of "1", "2", "3", "4" and "5" will be deleted.
`>> delete 1 time` | The to-do with the index of "1" will have its time window (if it's an _Event_) or due date (if it's a _Task_) removed. If the to-do is a _Task_ without a due date, there will be an error.
`>> delete 1 tag` | The to-do with the index of "1" will have its tags removed. If the to-do is a to-do without tags, there will be an error.
`>> delete 1 recurrence` | The to-do with the index of "1" will have its (only) recurrence removed, with its time window or due date preserved. If the to-do is without a recurrence, there will be an error.
`>> delete 1 - 5 time tag` | The to-do with the index of "1", "2", "3", "4" and "5" will have its time constraints and tags removed. If any of the target to-dos do not have a time constraint or tag, there will be an error and no action will be taken.

## Editing To-Dos: `edit` command

Command to type | What should happen |
------- | :--------------
`>> edit 1 #projectABC` | The to-do with the index of "1" will have its tags (if any) replaced with a single tag "#projectABC".
`>> edit 1 from today 14:00 to 16:00 #important #work` | The to-do with the index of "1" will have its time window changed to the current day 1400h to 1600h and replaces its tags (if any) by #important and #work. If the target to-do is a _Task_ with a due date, there will be an error.
`>> edit 2 by 6 feb 2017` | The to-do with the index of "2" will have its due date changed (or added) to 6 Feb 2017, 2359h. If the target to-do is an _Event_ with a time window, there will be an error.

## Marking Tasks as Done: `finish` command

A _Task_ can be marked as done with the `finish` command. The to-do will initially be moved to the bottom of its list, greyed out. It is subsequently archived on the next day. As abovementioned, _Events_ will automatically be marked done and greyed out when the current datetime is after the end of their time window. Therefore, you cannot use the `finish` command on an _Event_.

Command to type | What should happen |
------- | :--------------
`>> finish 1` | The to-do with the index of "1" will be marked as done, dropping down to the bottom of its list and greyed out. If the target to-do is an _Event_, or if the to-do is already a finished _Task_, there will be an error.
`>> finish 1 4 5` | The to-dos with the indices of "1", "4" and "5" will be marked as done. If any of the target to-dos is an _Event_, or if any of the to-dos is already a finished _Task_ there will be an error and no action will be taken.

## Marking Tasks as Not Done: `unfinish` command

Command to type | What should happen |
------- | :--------------
`>> unfinish 1` | The to-do with the index of "1" will be marked as undone. If the target to-do is an _Event_, or if the to-do is already a undone _Task_, there will be an error.

## Setting Save Location: `store` command

Command to type | What should happen |
------- | :--------------
`>> store ./data/new.xml` | The save location for storing the to-do list data will be changed to `./data/new.xml` relative to your `commando.jar`, and latest to-do list data will be saved to that file. From then on, all changes to the to-do list will be saved to that path. If the file already exists, there will be an error.
`>> store ./data/new.xml override` | The save location for storing the to-do list data will be changed to `./data/new.xml` relative to your `commando.jar`, and latest to-do list data will be saved to that file, overriding any existing file.

## Exporting: `export` command

Command to type | What should happen |
------- | :--------------
`>> export ./data/new.xml` | The latest to-do list data will be saved to `./data/new.xml` relative to your `commando.jar`. If the file already exists, there will be an error.
`>> export ./data/new.xml override` | The latest to-do list data will be saved to `./data/new.xml` relative to your `commando.jar`, overriding any existing file.

## Undoing: `undo` command

Multiple `>> undo` can be used in succcession, only limited in your current session, which ends when you close the application.

Command to type | What should happen |
------- | :--------------
`>> undo` | The most recent `add`, `edit`, `delete`, `clear` and `import` command would be reverted.

## Redoing: `redo` command

Multiple `>> redo` can be used in succcession, only limited in your current session, which ends when you close the application.

Command to type | What should happen |
------- | :--------------
`>> redo` | The most recent `undo` command would be reverted.

## Clearing To-Do List: `clear` command

Multiple `>> redo` can be used in succcession, only limited in your current session, which ends when you close the application.

Command to type | What should happen |
------- | :--------------
`>> clear` |  All to-dos, including those finished and archived, will be removed.

## Exiting: `exit` command

Command to type | What should happen |
------- | :--------------
`>> exit` |  _CommanDo_ closes.

## Datetime Formats

There is a quick brief on the formats of date and times accepted.

For dates, you must follow this format : `Day of the month, Month, Year`. * `28/12/2018`, `3 feb 2019`

* You may choose to leave out the year if the date is in the current year.
* You may leave out the whole date if the date is today.
* You may also state relative dates such as `Today`, `Yesterday`, `Tomorrow`, `next/last/coming <weekday/month/year>`.

For times, you must follow either of these formats : `HH:MM`, `HHMMh`, `HHam/pm`. * `23:59`, `2350h`, `9am`

* You may state relative times such as `Morning`, `Afternoon`, `Evening`, `Night`, `Midnight`

The date must come before the time.

Refer to the section on [supported datetime formats](https://cs2103aug2016-w13-c3.github.io/main/user#supported-date-time-formats) for more information.
