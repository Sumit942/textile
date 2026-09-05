# Textile CRM — Project Flow Summary

## 1. Overview

This is a **Textile Manufacturing CRM** (company: "SHREERAM TEXTILE INDUSTRIES") built as a **Spring Boot (Java 17, WAR packaging) monolith** that serves both:
- Legacy **JSP + Thymeleaf** server-rendered pages, and
- A **React SPA** frontend (two generations: `frontend` legacy CRA-style build, and `frontend-v2` — a modern Vite + React 18 app), consumed as a REST client.

The app manages the textile order-to-invoice lifecycle: **Companies (suppliers/clients) → Yarn Orders → Yarn Builty (receipts) → Products/Fabric → Challans (delivery notes) → Invoices**, plus supporting master data (Yarn, Fabric Design, Employees, Units, States, etc.).

---

## 2. Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 2.7.2, Spring MVC, Spring Data JPA (Hibernate), Spring Security, Java 17 |
| DB | MySQL (`textile_crm_local_1`), Hibernate `ddl-auto: update` |
| View (legacy) | JSP (via Tomcat Jasper) + Thymeleaf (email templates only) |
| Mapping | ModelMapper 3.2.2 + custom manual DTO⇄Entity transformers |
| Reporting | Apache POI (Excel), PDFBox & Flying Saucer (PDF generation for invoices) |
| Caching | Spring Cache + Caffeine |
| Build | Maven (`frontend-maven-plugin` builds the legacy `frontend/` React app and copies output into `src/main/resources/static/react`) |
| New Frontend (`frontend-v2`) | React 18, Vite, React Router v7, MUI, Tailwind CSS, Axios, react-hook-form, yup, ag-grid |

---

## 3. Backend Package Structure (`com.example.textile`)

---

## 4. Core Architectural Pattern: Action/Executor (Command Pipeline)

Almost every "write" operation (Order, Invoice, Company Yarn Order, Product, Employee, Yarn, etc.) goes through a shared **template-method pipeline** defined in `ActionExecutor<T>`:

- Each controller registers its own `Map<ActionType, ActionExecutor>` at `@PostConstruct` via `ActionExecutorFactory` (reflection-based factory), then plugs in module-specific actions (e.g. `OrderSubmitAction`, `InvoiceSubmitAction`, `CompanyYarnOrderSubmitAction`).
- REST endpoints use a `RestActionExecutor<T>` variant returning an `ActionResponse<T>` (SUCCESS/FAILURE + DTO or error list), which controllers translate into `ResponseEntity` with proper HTTP status.
- Validation failures are converted into an `ErrorResponseDto` (field → localized message via `MessageSource` + `messages.properties`), so the frontend can show inline errors.

This gives the whole system a **consistent validate → pre-save → persist → respond** flow, whether triggered from a JSP form post or a React fetch call.

---

## 5. Entity / Persistence Layer

- Entities (e.g. `Orders`, `Company`, `CompanyYarnOrder`, `YarnBuilty`, `Invoice`, `ProductDetail`) extend a common base `Document` (holds shared fields like optimistic-locking `version`).
- Rich JPA relationships (`@OneToMany`, `@ManyToOne`, `@OneToOne`) are used to model the order → yarn order → yarn builty → invoice product chain.
- `@JsonManagedReference` / `@JsonBackReference` prevent infinite recursion in nested JSON serialization.
- Because DTOs arrive detached from the DB, **service-layer merge logic** (see `OrderServiceImpl.updatePersistedOrders`) manually reconciles incoming nested collections against the persisted graph:
    - Adds new child rows (no ID),
    - Updates changed rows (comparing DTO vs persisted via `DtoAndEntityComparator`, checking `version` for optimistic-lock conflicts → throws `OptimisticLockException` on mismatch),
    - Dissociates/removes rows no longer present.
- Complex nested object graphs are converted between DTO and Entity using **hand-written transformers** (`TransformationDTOToEntity` / `TransformationEntityToDTO`) rather than relying purely on ModelMapper, to control cascading and avoid infinite recursion (e.g., `Orders ⇄ CompanyYarnOrder ⇄ YarnOrderItem/YarnBuilty`).
- Read-only reporting/listing endpoints are backed by **database views** mapped to entities (`OrdersView` + `OrdersViewRepo`, `InvoiceView`) for flattened, query-friendly projections.
- Ad-hoc partial-field queries use the **JPA Criteria API** directly (e.g. `CompanyServiceImpl.getIdNameAndGstByName` selects only id/name/gst/code).

---

## 6. Security

- Spring Security is configured (`SecurityConfiguration`) with:
    - CSRF protection using a **cookie-based token repository** (`CookieCsrfTokenRepository`) — the SPA must fetch a CSRF token before issuing state-changing requests.
    - CORS enabled for local React dev servers (`localhost:3000`, etc.) with credentials allowed.
    - Currently **all requests are `permitAll()`** (role-based rules are present but commented out, ready to be re-enabled).
    - Form login for legacy JSP flows, using a custom `RoleBasedAuthenticationSuccessHandler` to redirect users based on role.
    - `UserDetailManagerImpl` provides user lookup for authentication.

---

## 7. REST API Surface (representative)

Each domain has its own controller under context-path `/textile` (per `application.yml`), e.g.:

| Controller | Base Path | Notes |
|---|---|---|
| `OrderController` | `/order` | CRUD + `/submit` (Action pipeline) + `/view` (DB view) + `/searchBy` |
| `CompanyController` | `/company` | Company master (supplier/client/transport) |
| `CompanyYarnOrderController` | `/companyYarnOrder` | Yarn purchase orders + Yarn Builty (receipts) |
| `InvoiceController` | `/invoice` | GST invoice generation, PDF/report support |
| `ProductController` / `ProductDetailController` | `/product*` | Product & challan line items |
| `YarnController` / `YarnOrderItemController` | `/yarn*` | Yarn master data |
| `FabricDesignController` / `YarnFabricDesignController` | `/fabricDesign*` | Fabric/yarn design mapping |
| `OrderProductMappingController` | `/orderProductMapping` | Maps orders to finished products + raw material allocation |
| `EmployeeController` | `/employee` | HR/employee master |
| `StateController`, `NavigationController` | `/state`, `/navigation` | Dropdown/master + dynamic nav menu |
| `CsrfController` | `/csrf` | Issues CSRF token for SPA bootstrap |
| `ReactController` | `/react/**` | Forwards all React SPA routes to `index.html` (client-side routing support) |

---

## 8. Frontend (`frontend-v2` — active React app)

> Note: the legacy `frontend/` app is excluded from this summary. `frontend-v2` (Vite + React 18 + MUI + Tailwind) appears to be the actively developed replacement UI.

**Startup flow (`App.jsx`):**
1. `setupCSRF()` — calls `GET /textile/csrf`, stores the CSRF token, and attaches it as a default Axios header for all subsequent requests (`service/api.js`, `baseURL: /textile`, `withCredentials: true`).
2. `getNavigation()` fetches the dynamic navigation menu (`NavigationController`) to build the `Navbar`.
3. `react-router-dom` (`BrowserRouter`) defines client-side routes for each domain screen: Home, Contact, Yarn (create/list), Company (create), Orders (create/list), CompanyYarnOrder (create/list), FabricDesign, YarnFabricDesign (create/list), OrderProductMapping.

**Typical component flow (illustrated by the Company form, representative of all forms):**
1. Component holds form state locally (`useState`), loads dropdown master data on mount (e.g. states via `getStates()`).
2. User input is captured with controlled inputs; nested/repeatable sub-sections (e.g. bank details) use array state with add/remove handlers.
3. Client-side `validate()` runs before submit (required fields, format checks like mobile number).
4. On submit, a `service/*.js` module (thin Axios wrapper) posts the payload to the matching Spring REST endpoint (e.g. `POST /textile/company`).
5. Response is inspected for `201` (success) vs `400` (validation errors) vs other (system error), and a success/failure banner is shown, auto-dismissing after a timeout.

---

## 9. End-to-End Example Flow (Order Creation)

1. **User** opens `/orders/save` in the React SPA.
2. App has already fetched CSRF token + navigation on load; the Order form fetches supporting master data (companies, yarn types) via Axios services.
3. **User** fills order details (company, remarks) and adds nested `CompanyYarnOrder` rows (with `YarnOrderItem` / `YarnBuilty` sub-rows).
4. **Submit** triggers `POST /textile/order/submit` with the full nested `OrdersDto`.
5. `OrderController.submitOrder()` resolves the `OrderSubmitAction` from its executor map and runs it through the shared `RestActionExecutor` pipeline: validate → pre-save checks → persist.
6. On success:
    - DTO → Entity via `TransformationDTOToEntity.transformOrdersDto()`.
    - If it's an update, `OrderServiceImpl.updatePersistedOrders()` reconciles the nested graph against the DB (adds/updates/dissociates child rows, enforcing optimistic locking via `version`).
    - Order number is auto-generated for new orders (`companyCode + sequence`).
    - Persisted via `OrdersRepository` (cascades to `CompanyYarnOrder`, `YarnOrderItem`, `YarnBuilty`).
    - Entity → DTO via `TransformationEntityToDTO` and returned as JSON (`201 Created`).
7. On validation failure: an `ErrorResponseDto` with localized field messages (`messages.properties`) is returned (`400 Bad Request`) and rendered inline in the form.
8. Listing/search screens (`/orders/view`) call `GET /textile/order/view`, which reads from a flattened **DB view** (`OrdersView`/`OrdersViewRepo`) for fast, denormalized display; `GET /textile/order/searchBy?orderNo=` supports quick lookup.

**This same pattern (Action/Executor → DTO↔Entity transform → JPA persistence with manual reconciliation → REST JSON response) is reused consistently across Company, Invoice, Product, Yarn, Employee, FabricDesign, and CompanyYarnOrder modules**, giving the whole application a uniform create/validate/submit/list lifecycle — while a few legacy screens (Challan, Invoice print, Ledger, Product, Employees, Login) still run through server-rendered **JSP views** for parts of the app not yet migrated to the React SPA.

**Invoice-specific extras:** duplicate GST/party detection, automatic CGST+SGST vs IGST selection based on matching state codes between buyer and seller, and PDF/Excel report generation (Apache POI, PDFBox, Flying Saucer) plus Thymeleaf email templates for sending invoices.

---

## 10. Build & Deployment

- `mvn package` builds a **WAR** (`textile-crm-spring-boot.war`).
- The `frontend-maven-plugin` installs Node/npm and builds the legacy `frontend/` React app during the Maven build, copying its output into `src/main/resources/static/react` (served statically and SPA-routed via `ReactController`).
- `frontend-v2` currently builds independently via Vite (`npm run build` → `dist/`) and is not yet wired into the Maven build pipeline — it's the newer UI, likely served separately during development (e.g. via Vite dev server proxying API calls to the Spring Boot backend on port 8082 under `/textile`).