#Manual Testing Guidelines

You can find the sample data at `SampleData.xml` in the same folder as this document. Throughout this document, I will be referring to to-dos with a datetime interval as _Events_, to-dos with a due date as _Tasks With Deadlines_, and to-dos without any time constraint as _Floating Tasks_.

##Importing Sample Data

To start doing manual scripted testing, follow these steps:

1. Download `commando.jar` from our [releases](https://github.com/CS2103AUG2016-W13-C3/main/releases).
2. Copy `commando.jar` in a directory that would be the home directory for _CommanDo_
3. Double-click on `commando.jar`. You should have Java version `1.8.0_60` or later on your device - otherwise, download it [here](https://java.com/en/download/help/download_options.xml). You will notice that a `./data` folder would be created in the home directory for _CommanDo_ you chose.
4. Download `SampleData.xml` [here](https://raw.githubusercontent.com/CS2103AUG2016-W13-C3/main/master/src/test/data/ManualTesting/SampleData.xml) - this is our sample data file of more than 50 to-dos.
5. Copy `SampleData.xml` into the newly created `./data` folder in the home directory.
4. Switch back to _CommanDo_, type `>> import ./data/SampleData.xml` and hit <kbd>Enter</kbd>.
5. You have just used the `import` command to import our sample to-do list into _CommanDo_. You will see that the 2 panels will be populated with to-dos. The to-dos visible are only those which are undone - you can type ` >> recall` to see all to-dos that are done.

For more details on how _CommanDo_ works that is not described in this document, visit our [user guide](https://cs2103aug2016-w13-c3.github.io/main/user).

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
