#Manual Testing Guidelines

Please ensure that you are testing this application in an IDE. In the same folder as this file, there should be a sample data set named `SampleData.xml`.
I will be referring to todos with a datetime interval as "events", with a deadline as "task with deadline", and without any datetimes as "floating task".

##Importing
To import the said data set for testing, first run the application.
Type `import .\src\test\data\ManualTesting\SampleData.xml` and submit. (Press enter)
You will see that there are 9 events and 16 tasks visible on the screen.
Note that there are 50 todos (events and tasks) present in the data set.

##Datetime Formats
Before you begin adding or editing todos with datetimes, have a quick brief on the formats of date and times accepted.

For dates, you must follow this format : `Day of the month, Month, Year`. I.e. `28/12/2018`, `3 feb 2019`

* You may choose to leave out the year if the date is in the current year.
* You may leave out the whole date if the date is today.
* You may also state relative dates such as `Today`, `Yesterday`, `Tomorrow`, `next/last/coming <weekday/month/year>`.

For times, you must follow either of these formats : `HH:MM`, `HHMMh`, `HHam/pm`. I.e. `23:59`, `2350h`, `9am`

* You may state relative times such as `Morning`, `Afternoon`, `Evening`, `Night`, `Midnight`

The date must come before the time.

##Adding
To add an event or a task, you must start your command with a `add` keyword.

###Events
The proper format for adding events would be `add <description> from <start_datetime> to <end_datetime>`.

* you must have the keywords `add`, `from` and `to` to tell the application to add an event.
* the description must not be empty.
* the datetimes must be valid. (See Datetime Formats)
* the start datetime must be after the end datetime.

An error will be thrown if any of the conditions are not met, along with detailed formats of the `add` command.

###Tasks
The proper format for adding tasks would be `add <description> by <end_datetime>`.

* the description must not be empty. (Like events)
* `by <end_datetime>` is optional. You can add a task without a deadline. It becomes a floating task.
* if the datetime is present, the datetime must be valid. (See Datetime Formats)

###Recurrence
Only events and tasks with deadlines can have recurrences. 

* the recurrence keywords, `daily/weekly/monthly/yearly` are parsed as recurrence only if there are datetime(s) in the command.
* the recurrence keywords must be added to the end of the command, right after the datetime.
* the length of the event must not exceed the recurrence interval, or an error will be thrown.

I.e. `add <description> from <start_datetime> to <end_datetime> <recurrence>`
I.e. `add <description> by <end_datetime> <recurrence>`

###Tagging
Any todo can have tags.
Tags will appear on the right side of the todo, to the left of the datetime if there is a datetime.

* to indicate that the word is a tag, put a `#` in front of it, together.
* to put multiple tags, separate them by spaces.
* the tags must appear after both datetimes and recurrence, if either or both fields exist.

I.e. `add <description> from <start_datetime> to <end_datetime> <recurrence> #tag1 #tag2...`
I.e. `add <description> from by <end_datetime> <recurrence> #tag1 #tag2...`

##Finding
To search for todos containing substring(s), keyword(s), tag(s) or any combination of them, you need a `find` keyword.
Note that this will only return todos that are not archived.

* the search conditions can be in any order, as long as they are separated by spaces.

I.e. `find <substring> <keyword> <tag>`

##Editing
To edit any todo, you would just need the index, which is a number attached to each visible todo.
The numbers will change with every modification to the visible lists.
The order would be the visible events first, followed by visible tasks with deadlines, and visible floating tasks.
Within this 3 groups, they are numbered in chronological order.

###Changing Description
To change the description of any todo, just provide a `edit` keyword at the start of your command.

* the new description must not be empty, it will throw an error

I.e. `edit <index> <new_description>`

###Changing Tags
The general editing format applies to this as well.

* all tags that are attached to the todo will be replaced by the new tags provided

I.e. `edit <index> <tags>`

###Changing Datetimes and Recurrences
The recurrence field cannot be edited directly; it must be edited along with the datetimes field.
Floating tasks can be converted to events or deadlines by editing in the respective fields.

* tasks with deadlines cannot be changed to an event directly, and vice versa.
* to remove datetimes, please refer to the delete command, which is after this command.

I.e. `edit <index> from <start_datetime> to <end_datetime> <recurrence>`
I.e. `edit <index> by <end_datetime> <recurrence>`

##Deleting
To delete a todo, the `delete` keyword must be provided.
Any editable fields, with the exception of description, can be deleted.

###Deleting Tags
Currently, all tags related to this particular todo will be deleted with this command.

I.e. `delete <index> tags`

###Deleting Datetimes
Recurrences are meaningless without datetime(s), and hence will be removed along with the datetime(s).

I.e. `delete <index> datetime`

###Deleting Recurrences

I.e. `delete <index> recurrence`

##Marking todos done
To mark a todo done, the `finish` keyword must be provided.
The todo will then be shifted to the bottom of the list and greyed out.

* events cannot be marked done.
* finished tasks cannot be marked finished again.

I.e. `finish <index>`

##Marking todos not done
To mark a todo not done, the `unfinish` keyword must be provided.

* events cannot be marked not done.
* unfinished tasks cannot be marked unfinished again.

I.e. `unfinish <index>`

##Undo
This command will rollback the latest change made to the todo list.
The history of edits extend back to the start of this session.

I.e. `undo`

##Redo
Redo the last undo.

I.e. `redo`

##Recall
This command lists all todos that are already done.
The format is similar to the search command.

I.e. `recall <substring> <keyword> <tag>`

##List
This command lists all todos, including todos that are already done. 

I.e. `list`
I.e. `list on <datetime>`
I.e. `list to <end_datetime>`
I.e. `list from <start_datetime>`
I.e. `list from <start_datetime> to <end_datetime>`

##Export
After making edits to the save file, you can export the data into another .xml file.
If no full filepath is specified, it is taken as a relative filepath to the commanDo.jar file.
Note that this does not change the default save location, as this command clones the save file in another location.

I.e. `export .\src\test\data\ManualTesting\newData.xml`

##Store
This command allows you to change the default save location.
If the folders specified in the new filepath does not exist, they will be created.
Note that the save file in the old save location remains.

I.e. `store .\src\test\data\ManualTesting\Test\newData.xml`

##Help
This command opens up a link to the user guide of this application.
If a keyword is provided after the command keyword, the application will attempt to navigate to the header in the user guide.
List of possible keywords include: ...

* If the keyword does not exist, an error will be returned, showing the list of possible keywords you can provide.

I.e. `help`
I.e. `help <keyword>`