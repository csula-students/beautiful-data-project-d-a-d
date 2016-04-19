# Data Acquisition

To collect data ... in any format your question may require from any source.

But ... most of time, data comes in with some variety.

How do we handle with this messy data? After cleaning up data, how do we store this data?

## Objectives

* Data variety
* Common data acquisition problems
* Data quality
* Java data collector
* Data cleaning
* Git tutorial
* Set up team repository
* [Homework 2](homeworks/homework2.md)

## Metrics

* Java data collector
* Team repo
* Initial storage concern
* Test test test

### Data variety (problems)

When acquiring data, they usually come in with some variety (including good and bad!).

Therefore, it is important for data scientists to have ability to work with this diversity. In other word, data scientists need to be able to:
* Test about assumptions about data
  * Value
  * Data type
  * Shape
* Identify errors or outliers
* Finding missing values

### How do we acquire data?

To start with, we will talk about some common ways to get data. This includes:

* Http request
> The most common and low level approach to collect data. In example, you may be making http request to a url to download data (this could be in various format such as JSON, binary file or HTML).

* Web scraping
> You are scraping HTML content and use it to download further data. In example, you may use HTML parser to get meta information like image url in a HTML page and use this meta information to download the source (image itself).

* Api calls
> You may be working with a data provider like Twitter. Then, you will need to use their API client to collect data (like using Twitter client to get streaming tweets).

### Common data format

We are now knowing ways to download data. However, what kind of data format we may be dealing with?

Here are a few common data format but not limited:

* Tabular data
> Think about excel spreadsheet or sql table.

* CSV(Comma Separate Values)
> Very easy to read by software

* JSON
> Probably by far most common data format because it is readable by both human and machine

* Binary files
> Sometimes, you may store the binary file like videos, audios or images. Then you may extract meta information out of this binary file.

### Extract data

Upon download data, you will usually need to extra data out of its raw format so your program can start to use this information to do processing.

In example, you might need to deserialize from JSON to your Java object. You might need to extract HTML and read its text or even to download more content from this extracted content.

In nutshell, your program need to be able to load this data from its source to your memory so you can start doing some data cleaning.

### Data cleaning

The process of cleaning up acquired data is often referred to as `data munging`.  On the surface, data munging is simply trying to make sense and organize acquired data into structured data.

After initial extraction, you will find out not all data are ideal sooner or later. For instance, there may be missing records. Some data might have inconsistency issues and so on.

This issue is what I called dirty data.

How do we deal with dirty data?

We need perform data cleaning (e.g. removing duplicated entity).

Keep it in mind that data cleaning is an **iterative** process. In other word, you might need to perform multiple iterations in order to get to your ideal data format. Sometimes, this may involve changing the *shape* of the data.

### Data quality

Before we start to talk about the process, we need a way to measure data.

Here are 5 qualities of data:

* Validity
> Conforms a schema

* Accuracy
> Conforms to gold standard

* Completeness
> Contains **all** records

* Consistency
> Same data may show up twice with different values

* Uniformity
> Same unit

### Blue print of cleaning

Once we can measure data, we want to set up a plan to clean data.

Here is a blue print of data cleaning:

1. Audit your data
2. Create data cleaning plans
  * Identify causes
  * Define operations
  * Test your theory
3. Execute the plan
4. Manually correct

### Data storage concern

Follow up with discussion, data comes in with variety. This implies traditional relational database may have hard time to store the data due to unknown shape. Therefore, it is common to store the data in **plain file** or to store in **NoSQL database**.

Since we will be learning *MongoDB* in next class. I'd suggest you to store all data in plain file format without worry too much on MongoDB storage. In specific, you can be just store your data in CSV or JSON format as long as you can read the file later.

But it's just a plain file we are writing. What about performance?

Plain file format is not that bad. Think about HDFS(Hadoop Distributed File System), it is just plain file from your program point of view! But by underlying implementation, HDFS partitions files and do parallel processing on top. In short, plain file may not be the worst in performance.

## Programming time

* Cli
* Git
* Collector Interface

### Why use terminal?

I will be strongly encourage students to use terminal rather than relying on the Eclipse right click menu to run any task. Reason being in terminal you know it will work regardless of the environment.

Think of this case, if you are working with me in a team. If you say your code works only with Eclipse, this implies an enforcement for me to use Eclipse.

Secondly, you may not be able to use this knowledge of Eclipse outside of Java language. Consider we will be working with Python later, please learn how to use terminal to run bash commands.

### Git cheatsheet

To follow up the strong suggestion on using terminal, the following session on git will be only for terminal. This way, I ensure everyone who has git bash can run the script without problem.

In git, there are few commands you absolutely have to know:

* `git add {files}`
> Before you are going to commit the file, you will have to *add* file to index so git know which file to commit.

* `git commit`
> This will bring up a editor for you to add commit message. In short, commit is to save changes in git.

* `git fetch`
> Fetch is like to get updates from *remote*. In a sense, this tells git what changes are on the remote.

* `git push`
> This is how you *upload* your *changes* from your computer to *remote* Github server.

* `git pull`
> This is how you *download changes* from *remote* Github server.

A common workflow may be like:

```bash
# This add all files under current directory
$ git add .

# This commit the changes
$ git commit -m "Commit message"

# This push changes
$ git push
```
