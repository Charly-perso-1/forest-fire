# Forest fire simulation

This project implements a forest fire propagation simulation with a JavaFX MVC interface.

## Configuration

The application loads the parameters from a `config.properties` file. The accepted keys are:

- `app.grid-width`
- `app.grid-height`
- `app.begin-burning-cells` (for example: `1,1;3,3;5,5`)
- `app.spread-probability`
- `app.simulation-step-delay-ms` (for example: `600`)

A sample file is already present at the project root.

## Run

From the project root:

`mvn javafx:run`

## Tests

`mvn test`
