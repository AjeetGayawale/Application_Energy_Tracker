# Appliance Energy Optimizer

A software-only Java web application for entering household appliances and typical daily use, estimating electricity consumption and cost, and finding practical ways to reduce usage. Estimates are calculated from user-entered rated power and hours; the application has no sensor or appliance-control integration.

## Problem and solution

Households often lack a simple way to understand which appliances drive their electricity use. This project provides a small manual energy tracker with a dashboard, appliance management, configurable tariff and thresholds, usage analytics, optimization suggestions, and preferred usage schedules.

## Features

- Dashboard with energy and cost estimates, top consumers, alerts, and charts.
- Create, edit, activate/deactivate, and delete appliances.
- Per-appliance daily, weekly, monthly, and yearly energy and cost estimates.
- Analytics with appliance shares and highest/lowest consumers.
- Configurable electricity tariff and daily/monthly/appliance-hour limits.
- Recommendations based on stored appliance power and daily hours.
- Preferred time windows for appliances. These are planning suggestions only; nothing is switched on or off.
- Starts with an empty appliance list; household estimates remain zero until appliances are added.
- Responsive web interface served by Spring Boot.

## Technology

Java 17+, Spring Boot 3, Spring Web, Spring Data JPA, Jakarta Validation, Maven, H2 for a no-setup local run, and MySQL Connector/J for MySQL deployments. The chart uses Chart.js from its public CDN.

## Architecture

`controller` exposes REST endpoints, `service` centralizes energy estimates, `repository` provides Spring Data JPA persistence, `entity` defines database models, and `config` initializes settings and removes legacy sample rows. The HTML, CSS, and JavaScript client is served from Spring Boot static resources.

## Database structure

- `appliance`: name, category, rated power, daily usage hours, active status, priority, sample-data flag, creation time. New installs start with an empty appliance table.
- `usage_records`: appliance relationship, date, actual entered hours, and consumed energy (schema ready for dated usage tracking).
- `app_settings`: electricity rate, daily and monthly energy thresholds, and maximum daily appliance use.
- `schedules`: appliance relationship, preferred start/end times, and enabled state.

JPA creates/updates tables on startup. Any previously seeded sample appliances are removed at startup, along with their related usage records and schedules. Appliance deletion also removes its related usage records and schedules.

## Calculation assumptions

- Daily kWh = rated power (W) × average daily hours ÷ 1,000.
- Weekly estimate = daily kWh × 7.
- Monthly estimate = daily kWh × 30.
- Yearly estimate = daily kWh × 365.
- Cost = estimated kWh × configured rupees per kWh.

These are estimates, not meter readings. Only active appliances contribute to household totals. Change the tariff and alert limits in Settings.

## Configure and run

Requirements: Java 17 or later and Maven 3.6+. In the project folder:

```bash
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080). The default database is persistent, file-backed H2 at `./data/appliance-energy`; its console is at `/h2-console` (JDBC URL `jdbc:h2:file:./data/appliance-energy`, user `sa`, blank password).

To use MySQL, create a database and provide connection values as environment variables before starting:

```text
DB_URL=jdbc:mysql://localhost:3306/appliance_energy?createDatabaseIfNotExist=true
DB_USERNAME=your_mysql_user
DB_PASSWORD=your_mysql_password
```

The MySQL JDBC driver is included. Hibernate manages the schema with `ddl-auto=update`. No login credentials are required by this student demo.

## API overview

- `GET /api/dashboard`, `GET /api/appliances`
- `POST /api/appliances`, `PUT /api/appliances/{id}`, `DELETE /api/appliances/{id}`
- `PATCH /api/appliances/{id}/status`
- `GET /api/settings`, `PUT /api/settings`
- `GET /api/schedules`, `POST /api/schedules`, `DELETE /api/schedules/{id}`

Invalid data returns a helpful HTTP 400 response; missing records return HTTP 404.

## Tests

Run `mvn test` for unit tests covering energy and cost arithmetic, monthly/yearly estimates, threshold detection, and recommendation calculations.

## Screenshots

_Add application screenshots here._

## Future scope

Possible future extensions include ESP32/Arduino sensor integration, real-time power measurement, a mobile application, cloud synchronization, machine-learning-based energy prediction, voice assistant integration, and automated appliance control. None of these extensions are implemented in this software-only version.
