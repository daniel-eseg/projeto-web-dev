# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this project is

An individual academic project (ESEG, Engenharia de Computação, Programação Web, 2nd semester 2026). The task is to modernize a legacy Oil & Gas management system ("Petróleo e Gás") whose data lives in a spreadsheet (`10_petroleo_gas.xlsx`, 8 sheets, 14,799 records) and turn it into a full-stack web app. The project brief is `../contextualizacao-projeto.pdf`. The requirements spec is `02_Levantamento_de_Requisitos.pdf`. All domain names, identifiers and docs are in Portuguese, so keep that convention.

**Mandatory stack (the grade depends on it):** Java, Spring Boot (Spring MVC, Spring Data JPA, Spring Security), a relational DB, JPA/Hibernate, HTML, CSS, Bootstrap, Thymeleaf. Thymeleaf must render data and forms bound to Controllers, and it can't be swapped for another view technology (no SPA/React or REST-only frontend). Features that are simulated, not persisted, or only visual don't count.

## Current state

There is no build system yet: no Maven/Gradle, no Spring Boot app, no tests, no git repo. The folder holds Phase 3 (OO modeling) artifacts:

- `*.java`: field-only stubs generated from the Astah UML model (`petroleo_UML.asta`, also exported as `Class Diagram0.png`). They are in the default package, with no getters/setters, constructors or annotations. The two calculation methods return `0`.
- Documents referenced in the spec but missing here: `01_Relatorio_Entrega1`, `05_Scripts/schema_proposto.sql` and the source spreadsheet. Ask the user for them instead of guessing their contents.

Upcoming milestones: 2nd delivery **06/11/2026** (phases 5–8: runnable Spring Boot app, DB and scripts, JPA, services, controllers, persisted operations), 3rd **13/11/2026** (login/logout with two profiles, Thymeleaf + Bootstrap pages), 4th **20/11/2026** (integration, automated tests, docs, git history, run instructions).

When the Spring Boot project is created, add its build/run/test commands to this file (including how to run a single test).

## Domain model (see `Class Diagram0.png`)

- `Campo` 1—* `Poco` (composition). `Poco` 1—* `Producao` (composition).
- `Producao` → `Refinaria` (optional `refinariaDestino`). `SegurancaAmbiental` → `Poco` (optional).
- `MovimentacaoProduto` (abstract: produto, volume, data) is the superclass of `Venda` and `TransporteLogistica`. `TransporteLogistica` → `Refinaria` (`refinariaOrigem`) and aggregates into `Venda`.
- `Venda.calcularValorTotal` and `TransporteLogistica.calcularCustoTransporte` depend on `PrecoMercado` (at most one record per month).

Known stub issues to fix when turning these into JPA entities:
- The subclasses redeclare `produto`/`volume` that the superclass already has. This is the exact redundancy LA-06 calls out.
- `Poco.idCampo` and `Producao.idPoco` duplicate the object references.
- Legacy text IDs (`CP0001`, `PO00001`, `RF001`…) carry meaning and have fixed widths (LA-01/RNF-01). The spec calls for surrogate keys plus a unique business code.

## Business rules the implementation must enforce

Enforce these on the server (service layer plus DB constraints). Hiding them in the UI is not enough.

- **Poço status** ∈ {Ativo, Manutenção, Parado}. Allowed transitions: Ativo→Manutenção/Parado, Manutenção→Ativo/Parado, Parado→Manutenção only. Parado→Ativo must be blocked with a message (RN-01/02).
- **Produção**: the poço must be Ativo. The date must be ≥ the poço `dataInicio`. Only one produção per (poço, data). Volumes and costs must be ≥ 0, and efficiency must be 0–100 (RN-03/04/05).
- **Campo**: unique code, 2-letter UF, controlled `ambiente`/`status` domains. Deleting a campo cascades to its poços and produções, but is allowed only if none of its poços is Ativo (RN-06). Campo status goes Desenvolvimento→Produção→Declínio with no going back (RN-11, *to be validated*).
- **Produto** (venda and transporte) ∈ {Petróleo, Gás, GLP, Diesel, Gasolina}. Movement volume must be > 0 (RN-07/08).
- **Entrega status**: Em trânsito→Concluída/Atrasada, Atrasada→Concluída. Concluída is final (RN-09).
- **Months without a PrecoMercado quote**: the calculation returns a warning, not an error, and the venda is still saved (RN-10).
- **Profiles** (RN-12): `ADMIN` creates, edits and deletes campo/poço and deletes produção. `OPERADOR` can view and register produção. When OPERADOR opens the new-campo form or tries a delete, it must get HTTP 403.
- **Non-functional requirements**: BCrypt passwords, CSRF on, FK/UNIQUE/CHECK in the schema, indexes on FKs and on filter/sort columns, and layering into controller → service → repository. Business rules must be unit-testable without the UI, and tests must run with one command.
- **Legacy migration (RF-10)**: valid rows are migrated and invalid rows go to quarantine with a reason. Nothing is deleted or invented. For each sheet, migrated + rejected = source total. Expected for produção: 2,892 migrated and 3,108 rejected.

Requirement IDs (RL/LA/RF/RNF/RN/V) come from the requirements PDF and feed a traceability matrix that runs from the original requirement to the implementation and the test. Reference those IDs in code comments, tests and commits where it helps traceability. Points V-01..V-06 are open questions for the professor, so don't hard-code an answer to them without asking.

## Working constraints

- The student must defend every part of the code orally and make live changes during the defense (status-transition rules, JPA queries, access restrictions, explaining the owning side of an association, the Controller–Model–Thymeleaf flow). Prefer straightforward, conventional Spring code over clever abstractions, and explain non-obvious choices.
- Commits should show progressive history. No real passwords, tokens or personal data in the repo; test users are fictitious.
