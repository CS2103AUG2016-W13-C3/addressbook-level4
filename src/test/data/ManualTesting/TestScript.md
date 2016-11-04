#Manual Testing Guidelines

Please ensure that you are testing this application in an IDE. In the same folder as this file, there should be a sample data set named `SampleData.xml`.

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
* If the datetime is present, the datetime must be valid. (See Datetime Formats)

###Tagging

###Recurrence

##Editing

###Changing Description

###Changing Tags

###Changing Datetimes and Recurrences

###Changing of Tasks to Events

##Deleting

###Deleting Tags

###Deleting Recurrences

###Deleting Datetimes

##Marking todos done

###Marking tasks done

###Why events cannot be marked done

##Marking todos not done

###Marking tasks not done

##Undo

##Redo

##List

##Recall

##Search
You can search by tags or by substring. The proper format for searching is `find <substring>` or `find #tag`.
* You may also specify multiple tags or substrings or a combination of both.
This filters out all events that are not over and tasks that are not done.

##Export

##Store

##Help
