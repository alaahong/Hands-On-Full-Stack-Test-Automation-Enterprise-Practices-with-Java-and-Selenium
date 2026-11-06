# Examples

[简体中文](README.md)

This repository contains a progressive set of Java automation testing
examples, from Selenium WebDriver fundamentals to advanced interactions
and test engineering. It also includes examples for Selenium 3, Appium,
Cucumber, and Playwright.

## Prerequisites

- JDK 25 or later
- Maven
- The relevant browser installed for browser tests (Chrome, Firefox, or Edge)
- For Android examples: an Android emulator or device and an Appium Server
  configured with the UiAutomator2 driver
- For Grid examples: an accessible Selenium Grid; you may need to adjust
  its URL and browser version for your environment

Check that Java and Maven are available:

```shell
java -version
mvn -version
```

## Quick start

Run tests for all Maven modules from the repository root:

```shell
mvn clean test
```

Run a module together with its required upstream modules:

```shell
mvn -pl 02-webdriver-basic -am test
```

Local Chrome is used by default. Select a browser with the `BROWSER`
environment variable or the same-named Java system property:

```shell
# Windows PowerShell
$env:BROWSER = "chrome-headless"
mvn -pl 02-webdriver-basic -am test

# macOS / Linux
BROWSER=firefox mvn -pl 02-webdriver-basic -am test
```

Supported browser options include `chrome`, `chrome-headless`, `firefox`,
`edge`, and `chrome-remote`. The remote Chrome example connects to
`http://localhost:4444/wd/hub` by default; set the `selenium.remote.url`
system property to use another Grid URL.

Tests in `09-selenium-4-39-0` are skipped by default. To run that module:

```shell
mvn -pl 09-selenium-4-39-0 -am test -DskipTests=false
```

## Modules

| Module | Topics |
| --- | --- |
| `00-common` | Shared WebDriver bases, browser setup, retry extension, and screenshot utilities |
| `first-selenium-project` | Starter Maven project using JUnit 4 |
| `01-basic-knowledge` | Selenium introduction and the first browser test |
| `02-webdriver-basic` | WebDriver fundamentals |
| `03-webdriver-advanced` | Advanced interactions, waits, exceptions, BiDi, and virtual authenticators |
| `04-engineering-automation` | Page Objects, data-driven tests, test conditions, and multiple data formats |
| `05-enterprise-automation` | Selenium Grid, Allure / Extent reports, and observability examples |
| `06-expansion` | Appium Android, Cucumber / Gherkin, and Playwright examples |
| `07-legacy-selenium-3` | Compatibility examples based on Selenium 3 |
| `09-selenium-4-39-0` | Selenium 4.39.0 and Chrome DevTools Protocol examples |

Except for `00-common`, modules generally keep tests under their own
`src/test/java` directories, with test data and configuration under
`src/test/resources`.

## Tests and reports

Tests use JUnit 5 (the starter project uses JUnit 4) and run through Maven
Surefire. Surefire reports are written to each module's
`target/surefire-reports`; modules configured for Allure also generate
`target/allure-report`. Some examples require access to external websites,
browsers, or local services; check the relevant test for URLs and service
requirements before running it.

The GitHub Actions workflow is `.github/workflows/maven.yml`. It runs Maven
tests on a Windows runner with Java 25, Chrome, and Firefox.

## License

No repository-wide license is declared at the root. Check the repository
license and the licenses of its dependencies before using or distributing
the code.

## Discussion

Welcome to leave your question on the issue or send email to hi@ianzhang.cn
Thanks