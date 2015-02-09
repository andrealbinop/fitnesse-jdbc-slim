fitnesse-jdbc-slim [![Build Status](https://travis-ci.org/andreptb/fitnesse-jdbc-slim.svg)](https://travis-ci.org/andreptb/fitnesse-jdbc-slim) [![Coverage Status](https://coveralls.io/repos/andreptb/fitnesse-jdbc-slim/badge.svg)](https://coveralls.io/r/andreptb/fitnesse-jdbc-slim)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.andreptb/fitnesse-jdbc-slim/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.andreptb/fitnesse-jdbc-slim/)
==============

[FitNesse](https://github.com/unclebob/fitnesse) JDBC fixture in [slim format](http://www.fitnesse.org/FitNesse.UserGuide.WritingAcceptanceTests.SliM). Allows running SQL commands on multiple database connections. This project is licensed under [MIT](LICENSE).

#### Installation

* This module and spring dependencies must be in [FitNesse classpath](http://www.fitnesse.org/FitNesse.FullReferenceGuide.UserGuide.WritingAcceptanceTests.ClassPath). You can download all necessary jars from [here](https://github.com/andreptb/fitnesse-jdbc-slim/releases/download/0.0.1/fitness-jdbc-slim-all-jars.zip) or with [maven](https://github.com/lvonk/fitnesse-maven-classpath) (see below).
* The jdbc driver which the fixture will be used to connect also must be on [FitNesse](https://github.com/unclebob/fitnesse) classpath.

```xml
<dependency>
  <groupId>org.andreptb</groupId>
  <artifactId>fitnesse-jdbc-slim</artifactId>
  <version>0.0.1</version>
</dependency>
```

**Important**: As for now the project must be [locally installed](http://maven.apache.org/plugins/maven-install-plugin/usage.html) so it can be included in pom.xml, until get's available in [maven central repository](https://issues.sonatype.org/browse/OSSRH-13726).

####  Sample:
```
  |import|
  |org.andreptb.fitnesse|
  
  |library|
  |jdbc fixture|

  |script|
  | connect jdbc on | ${DATABASE_NAME} | with url | ${DATABASE_URL} | and driver | ${DRIVER_CLASS_NAME | and username | ${DATABASE_USERNAME} | and password | ${DATABASE_PASSWORD} |
  | execute in database | ${DB} | CREATE TABLE USER (ID BIGINT IDENTITY, NAME VARCHAR(255) NOT NULL, PASSWORD VARCHAR(255) NOT NULL) |
  | execute in database | ${DB} | INSERT INTO USER (NAME, PASSWORD) VALUES ('user1', 'password1') |
  | $usernameId= | execute in database | ${DB} | SELECT ID FROM USER WHERE NAME = 'user1' |
```






