# MoneyPilot
Personal finance assistant, Software Design Patterns final project, SE-2509.

Team: Manyakina Yuliya and Bazarbayev Bakdaulet.

## Stage 1: finance core
This first delivery contains the finance core and a working console interface. The graphical dashboard and remaining patterns will be added in stage 2.

Implemented patterns in this stage:
- Strategy: Balanced and Cautious budget warning thresholds.
- Command: Add, Edit, Delete, ImportBatch and a bounded undo history.
- Facade: one entry point for commands, persistence, monthly summaries and budget settings.

The immutable Transaction model uses BigDecimal and LocalDate. FileRepository saves local data through temporary-file replacement. Undo retains the latest 100 actions within one session. One global budget applies to every month. No bank connection or currency conversion.

## Build and run
JDK 17 or newer is required to build and test. On Windows run `build.bat`, then `run.bat`. On Linux/macOS run `sh build.sh`, then `sh run.sh`. The included MoneyPilot.jar runs the console version with Java 17+.

Test with `test.bat` or `sh test.sh`. CoreWorkflowTest checks precise totals, undo, budget policy changes, persistence and save failure. HistoryLimitTest checks the history limit and ordering.

Data is saved to `<user.home>/.moneypilot/data.properties`. For an isolated demo use `java -jar MoneyPilot.jar demo-data.properties`. Run only one instance per data file.

## Stage 2 plan
Add Builder for transaction creation, Observer for dashboard and budget updates, and Adapter for legacy CSV import. Add a Swing dashboard, charts, category filters, search and CSV export. Until that delivery, this repository deliberately launches the console version.
