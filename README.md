# Java Swing Taxi Management System

An academic object-oriented programming project that models a small taxi-management workflow using Java Swing. It is coursework, not a production application or a claim of sole authorship.

## Capabilities

- Main-manager and regular-manager accounts.
- Subscriber, taxi, station, and order management.
- Taxi assignment and pricing data handled by the application models.
- Loading and saving of managers, subscribers, orders, and taxi data through local text files.

The gate and payment-related flows are coursework simulations. The project has no external dependencies or database server.

## Project layout

```text
src/
|- Model/    domain objects: managers, subscriptions, taxis, stations, and orders
|- View/     Java Swing screens and dialogs
`- Control/  shared in-memory data storage and validation support

SystemManagers.txt  manager accounts and demo login data
members.txt         subscriber demo data
orders.txt          order demo data
taxi.txt            taxi export/demo data
```

## Requirements and command-line use

- JDK 8 or newer.
- Run commands from the repository root so the application can load its relative data files.

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out src/Control/*.java src/Model/*.java src/View/*.java
java -cp out View.LoginFrame
```

The project also includes `.project`, `.classpath`, and `.settings/` files to make it easier to open as an existing Java project in Eclipse. In Eclipse, import the repository as an existing project, ensure a JDK 8-or-newer JRE is selected, and run `View.LoginFrame` as a Java application.

## Demo data and security note

`SystemManagers.txt`, `members.txt`, `orders.txt`, and `taxi.txt` are retained because they support the coursework demo. Their names, addresses, phone numbers, credentials, and other values are fictional demonstration data only.

Manager credentials are stored in plaintext to match the original coursework loading and login logic. They are not a real authentication or security mechanism and must not be reused outside this demo.

The **Download All Regular Managers** action preserves existing main-manager rows in `SystemManagers.txt`, including their login fields, while refreshing regular-manager rows.

## Design notes

The project demonstrates inheritance through the taxi and manager types, encapsulation through model fields and accessors, polymorphism through specialized taxi behavior, and a lightweight Model/View/Control separation between domain objects, Swing views, and shared control logic.

## Validation status

Source compilation is verified with a local JDK during repository preparation. Runtime UI workflows and file-based demo scenarios should still be exercised manually from the repository root before relying on them.
