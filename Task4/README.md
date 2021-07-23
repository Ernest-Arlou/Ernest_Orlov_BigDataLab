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
* 
#### 6. Stop and Search correlation with crimes 

**Description:**

Comparison between Stop and Search data with "Arrest" outcome and Crime data on the street level by the following _Object of Search_/_Crime category_ pairs:
* "drugs" _to_ "Controlled drugs"
* "possession-of-weapons" _to_ "Offensive weapons" and "Firearms"
* "theft-from-the-person" and "shoplifting" _to_ "Stolen goods"

**Input:**

* Start month.
* End month.

**Output:**

* Street ID.
* Street Name.
* Month.
* Drugs crimes count.
* Drugs stop-and-search count.
* Weapons crimes count.
* Weapons stop-and-search count.
* Theft crimes count.
* Theft stop-and-search count.
