# Java - Practical task

### Used technologies:
fastjson, HikariCP, postgresql, fluentjdbc, commons-cli, junit, slf4j, log4j

__Deadline:__ 9 working days.  

__Disclaimer:__ In case of any questions related to the task please immediately contact any of the mentors to avoid misunderstanding.

### Legend

UK Police department would like to analyze existing crime information in London streets and figure out the main trends and relations between the crimes.
First of all you need to download the data from [existing API](https://data.police.uk/docs/) and store it in the database for further analysis.

### Requirements

#### Database

* Investigate the following [Street Level Crimes API method](https://data.police.uk/docs/method/crime-street/).
* Map API method response object to the database schema, each DB table should represent a single JSON object (same for nested objects) with required relations and constraints (API objects IDs, if present, should be primary keys in the target schema as well).
* Schema creation SQL script should be committed to the git repository as well.

#### Application

Implement __extensible__ command line java application which should be able to download data from the mentioned API and save it in various targets (DB, file).

* "Extensible" in this context means that it's possible to support other API methods (e.g. [Outcomes](https://data.police.uk/docs/method/outcomes-at-location/) or [Stop and search](https://data.police.uk/docs/method/stops-street/)) and targets just by adding corresponding implementations with a few code changes.
* Consider using java-style arguments (`-Dproperty.name=value`) in case of implementation-specific options ([Apache Commons CLI](https://www.tutorialspoint.com/commons_cli/commons_cli_properties_option.htm) can be used for this). 
* Proper error handling and user-friendly output during script execution should be implemented.
* All available options and command line arguments should be listed in help message (as well as implementation-specific options).
* JDBC wrapper library ([FluentJDBC](https://github.com/zsoltherpai/fluent-jdbc), [Spring JDBC Template](https://spring.io/guides/gs/relational-data-access/), [Apache Commons DbUtils](https://commons.apache.org/proper/commons-dbutils/), etc.) can be used in order to simplify DB access.
* __Feel free to implement other unmentioned options and features that you think will improve your application__

#### Street Level Crimes Implementation

Write an implementation for the application above which should be able to download street level crimes from the list of existing coordinates, parse the result and save it into the database.

* Target API method is `https://data.police.uk/api/crimes-street/all-crime` ([Documentation](https://data.police.uk/docs/method/crime-street/))
* List of existing coordinates should be provided from a file (path to the file passed as an option). Kindly use [LondonStations.csv](https://gist.github.com/Meosit/bd4d42dcf7e863fef4283c7b3d28f663) as the input file for this task.
* Date range to fetch should be provided as option as well.
* All database constraints should be preserved and duplicates eliminated.
* Implementation should properly handle [API call limits](https://data.police.uk/docs/api-call-limits/).
* `5xx` and `404` API errors should be properly handled and do not stop application execution.

### Deployment Script

Write deployment Bash script, which should be able to:
* Execute in __batch__ mode.
* Ensure that PostgreSQL is running and check that required tables already present (if not, schema creation script should be executed).
* Provide an option to clear database data.
* Build the project with maven (option to disable/enable this stage should be present as well).
* Run your application for a sample month range.
* Perform all necessary input and path validations, check that required services running, perform some error handling.
* Print user-friendly output during script execution (consider providing ability to enable/disable non-script output (i.e. preserve only script-specific echo output) at all through an option `-v`/`--verbose`).
* Show Unix-like help message which describes all features and usage of the script.
* Refer [Google Shell Style Guide](https://google.github.io/styleguide/shell.xml) to make your code more readable and [Shell Check](https://www.shellcheck.net/) to avoid common mistakes.

### Technology stack

* Build tool: Maven (Gradle can also be used).
* Logging framework: SLF4J.
* Database: PostgreSQL 9.4 or higher.
* Database connection pooling: HikariCP.

### Acceptance

* The most recent available at `master` branch setup/deployment scripts are used every time.
* Reviewer has OS version, configured by the script from Task 1.
* Reviewer can be able to execute the script correctly after referring built-in help message.


# SQL - Practical task
__Deadline:__ 7 working days.  

__Disclaimer:__ In case of any questions related to the task please immediately contact any of the mentors to avoid misunderstanding.

## Legend

Customer would like to extend analysis with another feed - [Stop and Search](https://www.lincs.police.uk/reporting-advice/stop-and-search/) data.
Then downloaded data needs to be analyzed in order to provide various statistics for future reports.

## Requirements

### Stop and Search Implementation

Extend the application from the previous task with the new implementation - [Stop and searches by force](https://data.police.uk/docs/method/stops-force/) data. Implementation should align the similar requirements:

* Map API method response object to the __existing__ database schema (i.e. already existing tables shall not be duplicated). Schema creation SQL script should be committed to the git repository as well.
* List of forces should be fetched from the another API method for the specific month - [List of forces](https://data.police.uk/docs/method/forces/).
* Date range to fetch should be provided as an option as well.
* All database constraints should be preserved and duplicates eliminated.
* Implementation should properly handle [API call limits](https://data.police.uk/docs/api-call-limits/).
* `5xx` and `404` API errors should be properly handled and do not stop application execution.

### Database

Download at least 5 months of data for each station and for each implementation (i.e. [Stop and searches by force](https://data.police.uk/docs/method/stops-force/) and [Street Level Crimes](https://data.police.uk/docs/method/crime-street/)) using the application from the previous task.

### Queries

Write several SQL queries by the given specification (below). Queries should align with the following constraints:
* Subqueries in `SELECT` clause are not allowed.
* [Correlated subqueries](https://en.wikipedia.org/wiki/Correlated_subquery) are not allowed.
* Each query should be stored in a separate file. Name of the file should start with the index of the query and can contain short query description.
* Each query should be properly formatted using common SQL code style (there is no single code style, but [Kickstarter SQL Style Guide](https://gist.github.com/fredbenenson/7bb92718e19138c20591) can be used as good example).

__Hint:__ for some queries [Window Functions](https://www.postgresql.org/docs/9.5/tutorial-window.html) might be extremely helpful.


#### 1. Most dangerous streets

**Description:**

Top streets by crime number within specified period.

**Input:**

* Start month.
* End month.

**Output:**

* Street ID.
* Street name.
* Period (format: `from <start> till <end>`, e.g. `from 2016-01 till 2018-01`).
* Crime count.

#### 2. Month to month crime volume comparison

**Description:**

Changes between each month in crime counts within each category and specified period.

**Input:**

* Start month.
* End month.

**Output:**

* Crime category.
* Month.
* Previous month count.
* Current month count.
* Delta count.
* [Basic growth rate](https://www.wikihow.com/Calculate-Growth-Rate), e.g. `5.55%`, `-15.7%`.


#### 3. Crimes with specified outcome status

**Description:**

Count and percentage of crimes per each location (street), within the given period, where outcome category equals to the given value. Taken into account only crimes where outcome status is present.

**Input:**

* Outcome category.
* Start month.
* End month.

**Output:**

* Street ID.
* Street name.
* Outcome category value.
* Count of crimes with the specified outcome category.
* Percentage of the total crimes (where outcome category is present).

#### 4. Stop and search statistics by ethnicity

**Description:**

Various Stop and Search statistics by ethnicity (officer_defined_ethnicity) within specified period:
* Total number of occurrences.
* Rate of Stop and Search with 'Arrest' outcome.
* Rate of Stop and Search with 'A no further action disposal' outcome.
* Rate of Stop and Search with other outcomes.
* Most popular object of search.

**Input:**

* Start date.
* End date.

**Output:**

* Officer-defined ethnicity.
* Total number of stop and search for ethnicity.
* Arrest rate `%`.
* No action rate `%`.
* Other outcome rate `%`.
* Most popular object of search.

#### 5. Most probable Stop and Search snapshot on street level

**Description:**

Within specified date range, find the most probable Stop and Search characteristics for each street:
* Most popular age range.
* Most popular gender.
* Most popular ethnicity.
* Most popular object_of_search.
* Most popular outcome.

__Note:__ each person parameter do not depend on the others.

**Input:**

* Start date.
* End date.

**Output:**

* Street ID.
* Street name.
* Age Range.
* Gender.
* Ethnicity.
* Object of search.
* Outcome.
