# ITM13TermStatistics
This is a project for the Practical Software Engineering course of ITM13

## Contribute guidelines
- This repository represents the whole project, not a container for partially solutions. Your implementations should be included in `Java Resources/src`.
- Please do not create unnecessary packages, if there is an existing package which you could use - **USE IT!**
- Take code reviews, submitted code should be reusable, easy to understand and well documented.
- Try your best to produce "clean" code
- Always attach a description to your pull-request, so me or @MICSTI know what's the aim of your pull-request without the need to take a deep look inside your code or commit messages.

## Package Structure
- Root path to java files will be **src/at/fhj/itm/pswe**
- Package **PageCrawler**: Contains all files and the whole functionality of the Pagecrawler
  - Subpackages LinkCrawler, WordAnalyzer according to the different tasks of the algorithm
    - Within every Subpackes contained will be related  Packages such  as Model, Business, Helper, ... 
- Package **Database**: Contains everything with regard to Database Access
  - Examples Would be DAO's, or Connection Classes.
- Package **REST**: Like standard Wildflypackage, contains Endpoints
  - May contain Helper and Business Packages to format data
