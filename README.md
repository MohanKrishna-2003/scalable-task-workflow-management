scalable-task-workflow-management
=================================

Problem Statement
-----------------

Modern teams require reliable backend systems to manage tasks, workflows, and ownership efficiently as the system scales.

The goal of this project is to design and implement a **scalable, extensible backend system** that supports task creation, assignment, workflow tracking, and prioritization while maintaining clean architecture and strong object-oriented design principles.

The system is designed to evolve incrementally, allowing new workflow rules, prioritization strategies, and scalability enhancements without major code changes. The focus is on backend engineering practices such as SOLID principles, proper data structure selection, and clean separation of responsibilities.

PHASE 1 – System Scope & Boundaries
===================================

Functional Requirements
-----------------------

### 1\. User Management

*   The system shall allow creation of users.
    
*   Each user shall have a unique identifier.
    
*   Users can be assigned tasks.
    
*   The system shall be able to retrieve user details by identifier.
    

### 2\. Task Creation & Management

*   The system shall allow creation of tasks.
    
*   Each task shall have:
    
    *   Unique task ID
        
    *   Title
        
    *   Description
        
    *   Priority
        
    *   Status
        
    *   Creation timestamp
        
*   The system shall allow updating task details.
    
*   The system shall allow deleting tasks.
    

### 3\. Task Assignment

*   The system shall allow assigning a task to a user.
    
*   A task can be reassigned from one user to another.
    
*   The system shall track the current owner of a task.
    

### 4\. Task Status Workflow

*   Each task shall follow a defined workflow.
    
*   Supported statuses:
    
    *   TODO
        
    *   IN\_PROGRESS
        
    *   COMPLETED
        
*   The system shall allow valid status transitions.
    
*   Invalid status transitions shall be rejected.
    

### 5\. Task Priority Handling

*   Each task shall have a priority level.
    
*   The system shall support ordering tasks by priority.
    
*   Higher-priority tasks shall be retrievable before lower-priority tasks.
    

### 6\. Task Retrieval & Querying

The system shall allow fetching:

*   All tasks
    
*   Tasks assigned to a specific user
    
*   Tasks filtered by status
    
*   Tasks sorted by priority
    

The system shall support efficient task lookup by task ID.

### 7\. Workflow Extensibility

*   The system shall allow introduction of new task types in the future.
    
*   The system shall allow modification of workflow rules without impacting existing logic.
    

_(To be designed in future phases.)_

### 8\. Error Handling

*   The system shall handle invalid inputs gracefully.
    
*   Meaningful errors shall be returned for:
    
    *   Invalid task ID
        
    *   Invalid user ID
        
    *   Invalid status transitions
        

Non-Functional Requirements
---------------------------

### 1\. Scalability

*   The system should support increasing numbers of users and tasks without major architectural changes.
    
*   The design should allow horizontal scaling in the future.
    

### 2\. Performance

*   Task retrieval operations should be efficient (near constant time for ID lookup).
    
*   Filtering and sorting operations should remain performant for large task volumes.
    
*   The system should avoid unnecessary full data scans where possible.
    

### 3\. Maintainability

*   The system shall follow SOLID principles.
    
*   The codebase should be modular and easily extensible.
    
*   New task types or workflow rules should be introduced with minimal modification to existing classes.
    

### 4\. Reliability

*   The system should handle invalid operations gracefully.
    
*   Data consistency should be maintained during task assignment and status transitions.
    

### 5\. Concurrency Awareness (Future-Ready)

*   The system design should consider concurrent task updates.
    
*   Race conditions should be preventable through proper design choices.
    

_(Not implemented in the current phase, but prepared for future enhancement.)_

### 6\. Extensibility

The system architecture should allow:

*   Introduction of new workflow states
    
*   New prioritization strategies
    
*   Additional task metadata
    

### 7\. Testability

*   Core business logic should be independent of external frameworks.
    
*   Services should be testable in isolation.
    

System Scope
============

In Scope
--------

The system will:

*   Provide backend services for managing tasks and workflows.
    
*   Support task creation, assignment, prioritization, and status transitions.
    
*   Allow retrieval and querying of tasks by multiple criteria.
    
*   Maintain clean separation between:
    
    *   Domain logic
        
    *   Service layer
        
    *   Data storage abstractions
        
*   Be designed with scalability and extensibility in mind.
    
*   Use in-memory data storage for the initial implementation.
    
*   Be implemented as a modular backend application.
    

Out of Scope
============

The system will not:

*   Provide a frontend UI in Phase 1.
    
*   Implement authentication or role-based access control.
    
*   Persist data using databases in the initial version.
    
*   Handle notifications (email, push, messaging).
    
*   Integrate with third-party services.
    
*   Support multi-tenancy.
    
*   Implement distributed or microservice architecture.
    

These capabilities may be added intentionally in later phases.

PHASE 2 – LLD Design
====================

Step 1: Core Entity Identification
----------------------------------

This step asks:

> What are the core business objects that must exist for this system to function?

At the business level, the core entities are:

1.  User
    
2.  Task
    
3.  Workflow
    
4.  TaskStatus
    
5.  Priority
    

User
----

### Why It Must Exist

*   Tasks are assigned to users.
    
*   Ownership must be tracked.
    

### Attributes

*   userId
    
*   name
    
*   email
    
*   assignedTasks (may be modeled as a relationship, not stored directly initially)
    

**Conclusion:**User is a strong domain entity.

Task
----

### Description

Task is the central entity of the system.

### Attributes

*   taskId
    
*   title
    
*   description
    
*   priority
    
*   status
    
*   createdAt
    
*   assignedUser
    

Task represents the core business object driving the system.

Workflow
--------

Workflow logic is considered part of the Task.

If a task does not exist, there is no workflow related to it.

We model:

*   Status transitions
    
*   Allowed state changes
    

Without introducing a separate Workflow entity.

### Decision

Workflow as a separate entity is not required at this stage.The design remains extensible for future enhancement.

TaskStatus
----------

TaskStatus is not a full entity.It represents a controlled state.

It will be modeled as an enum for now.

Supported values:

*   TODO
    
*   IN\_PROGRESS
    
*   COMPLETED
    

Priority
--------

Priority is not an entity.It is a classification mechanism.

It will also be modeled as an enum.

Supported values:

*   LOW
    
*   MEDIUM
    
*   HIGH
    

Final Entity List
=================

Core Domain Entities
--------------------

*   User
    
*   Task
    

Supporting Types
----------------

*   TaskStatus (enum)
    
*   Priority (enum)
    

This establishes the foundational domain model for Phase 2 and prepares the system for deeper LLD design steps such as relationships, service design, and data structures.


Step 2 – Entity Responsibilities & Relationships
================================================

1\. User – Responsibilities
---------------------------

### User is responsible for:

*   Holding user identity data
    
*   Representing task ownership
    
*   Optionally maintaining a list of assigned tasks (if needed later)
    

User represents a **pure domain identity object**.

### User is NOT responsible for:

*   Creating tasks
    
*   Assigning tasks
    
*   Changing task status
    
*   Enforcing business rules
    
*   Managing workflow transitions
    

These behaviors belong to the **service layer**, not the entity.

**Conclusion:**
User is a lightweight domain object focused strictly on identity and ownership representation.

2\. Task – Responsibilities
---------------------------

Task is the central domain entity.

### Task is responsible for:

*   Holding task data
    
*   Managing its own state
    
*   Validating status transitions
    
*   Updating its priority
    
*   Maintaining reference to the assigned user
    

Task encapsulates business invariants related to itself.

### Task is NOT responsible for:

*   Finding users
    
*   Persisting itself
    
*   Performing global task queries
    
*   Managing collections of tasks
    
*   Handling cross-entity coordination
    

Those responsibilities belong to repositories or service-layer abstractions.

**Conclusion:**
Task owns its internal consistency and state transitions but does not manage external concerns.

3\. Relationship Between User and Task
--------------------------------------

We must decide how the relationship is modeled.

Two options:

*   **Option A:** User → List
    
*   **Option B:** Task → assignedUser (reference only)
    

### Decision

We choose:

**Task contains a reference to assignedUser.**
**User does not maintain a task list initially.**

### Why Unidirectional?

*   Avoid bidirectional relationship complexity
    
*   Avoid synchronization issues between two aggregates
    
*   Prevent accidental inconsistency
    
*   Keep domain model simple
    
*   Query tasks through the service layer instead
    

This keeps the domain model clean and avoids premature optimization.

Unidirectional relationships reduce coupling and improve maintainability in early-stage system design.

4\. TaskStatus and Priority
---------------------------

TaskStatus and Priority are modeled as enums.

They:

*   Represent controlled value sets
    
*   Define allowed states and classifications
    
*   Do not have independent identity
    
*   Do not have lifecycle
    

Only the **Task** entity is allowed to modify:

*   Its status
    
*   Its priority
    

No external class should manipulate them directly.

### Why Enums?

*   Fixed, bounded set of values
    
*   No separate persistence needed
    
*   No independent business identity
    
*   Lightweight and sufficient for current requirements
    
*   Easy to extend later if needed
    

This aligns with minimalistic and clean LLD principles.

Here is your content in a clean, structured format:

Step 3 – Service Layer Identification
=====================================

Now we determine **which object performs which operations**.

Let’s think clearly:

Who should handle:

*   Creating a task?
    
*   Assigning a task to a user?
    
*   Changing task status?
    
*   Getting all tasks of a user?
    
*   Getting tasks by status?
    
*   Updating priority?
    
*   Deleting a task?
    

These operations should **NOT** be placed inside User or Task.

Why?

Because that would overload entities with too many responsibilities and violate clean design principles.

These operations belong to the **Service Layer**.

Correct approach:Business coordination logic should live inside a Service.

1.  Proposed Service
    

We introduce:

### TaskService – Responsibilities

*   createTask(...)
    
*   assignTask(taskId, userId)
    
*   changeStatus(taskId, status)
    
*   updatePriority(taskId, priority)
    
*   getTasksByUser(userId)
    
*   getTasksByStatus(status)
    
*   deleteTask(taskId)
    

This design ensures:

*   **Entities → Pure domain models**
    
*   **Service → Business logic coordination**
    
*   **Clear separation of concerns**
    

1.  Why Service Layer?
    

Because:

*   Entities should protect their **internal consistency**
    
*   Service layer coordinates **multiple entities**
    
*   Business rules do not belong inside simple data objects
    
*   Cross-entity operations must be centralized
    
*   Application workflow should not live inside domain identity objects
    

Entities manage their own invariants.Services orchestrate use cases.

Conclusion
----------

By introducing TaskService:

*   We avoid bloated entities
    
*   We keep domain objects clean
    
*   We centralize business operations
    
*   We follow clean architecture principles
    
*   We improve maintainability and scalability
    

This is proper Low-Level Design thinking with a clean separation between **data and behavior orchestration**.

Step 4 – Interface Design (Abstraction Layer)
=============================================

Why Interface is Needed
-----------------------

In a clean architecture design, business operations should depend on **abstractions rather than concrete implementations**.

Instead of directly implementing a service class, we first define an **interface** that declares the required operations.

Using an interface for the service layer provides several advantages:

*   **Loose Coupling**The rest of the system depends on a contract instead of a specific implementation.
    
*   **Better Testability**Interfaces allow easy mocking during unit testing.
    
*   **Flexibility to Change Implementation**The implementation can change without affecting other parts of the system.
    
*   **Support for Multiple Implementations**Example future implementations:
    
    *   InMemoryTaskService
        
    *   DatabaseTaskService
        
    *   RemoteTaskService (microservice / API)
        

This design follows the **Dependency Inversion Principle (DIP)**:

> High-level modules should not depend on low-level modules.Both should depend on abstractions.

By introducing a service interface, the system becomes **more maintainable, testable, and scalable**.

TaskService Interface
=====================

The **TaskService interface** defines the contract for task-related operations.

It specifies **what operations are available**, but does **not define how they are implemented**.

### Responsibilities of TaskService

TaskService will expose the following operations:

*   createTask(...)
    
*   assignTask(taskId, userId)
    
*   updateStatus(taskId, status)
    
*   updatePriority(taskId, priority)
    
*   getTasksByUser(userId)
    
*   getTasksByStatus(status)
    
*   deleteTask(taskId)
    

These operations represent the **core application use cases** for managing tasks.

The interface ensures that:

*   The service layer exposes **clear business capabilities**
    
*   Implementations remain **replaceable**
    
*   Higher layers remain **decoupled from concrete logic**
    

Implementation Class
====================

To implement the interface, we introduce a concrete class:

**TaskServiceImpl**

### Responsibilities of TaskServiceImpl

TaskServiceImpl will:

*   Implement the TaskService interface
    
*   Contain the **actual business logic**
    
*   Manage **task data storage (in-memory for now)**
    
*   Coordinate interactions between **User and Task entities**
    

For the current phase, the implementation will use **in-memory data structures**, such as:

*   HashMap for storing tasks
    
*   HashMap for storing users
    

This approach keeps the system simple while enabling easy expansion later.

Future Possibilities
====================

By designing with an interface, the system becomes extensible.

Possible future enhancements include:

### Database-backed Implementation

Replace the in-memory implementation with:
DatabaseTaskServiceImpl   `

This implementation could integrate with:

*   MySQL
    
*   PostgreSQL
    
*   MongoDB
    

### REST API Layer

A controller layer could expose endpoints such as:
 POST /tasksGET /tasks/user/{id}PUT /tasks/{id}/status   `

The controller would depend only on the **TaskService interface**, not on its implementation.

### Microservice Architecture

The task system could later evolve into a **separate microservice**, while still maintaining the same service interface.

Summary
=======

By introducing the **TaskService interface**, we achieve:

*   Clean separation of **interface vs implementation**
    
*   Reduced **coupling**
    
*   Improved **testability**
    
*   Support for **multiple implementations**
    
*   Better alignment with **clean architecture principles**
    

This abstraction layer prepares the system for the upcoming steps in Low-Level Design, including **data structures, repositories, and implementation details**.


Step 5: High-Level Class Design
===============================

In this step, we connect the previously identified entities, supporting types, and services into a clear **class structure**.

This stage focuses on identifying:

*   Core classes
    
*   Their responsibilities
    
*   How they relate to each other
    

The goal is to ensure a **clean separation between domain models, supporting types, and service-layer logic**, while keeping the architecture simple and extensible.

1\. Domain Classes
==================

User
----

The **User** class represents a system user who can own or be assigned tasks.

### Attributes

*   userId
    
*   name
    
*   email
    

### Responsibilities

The User entity is responsible for:

*   Holding user identity information
    
*   Representing task ownership
    
*   Acting as a reference when tasks are assigned
    

The User class intentionally remains lightweight and does not contain business logic related to task operations.

Task
----

The **Task** class represents the core business object of the system.

### Attributes

*   taskId
    
*   title
    
*   description
    
*   priority
    
*   status
    
*   createdAt
    
*   assignedUser
    

### Responsibilities

The Task entity is responsible for:

*   Storing task-related data
    
*   Maintaining its current workflow state
    
*   Validating allowed status transitions
    
*   Updating priority
    
*   Tracking the assigned user
    

The Task entity ensures **internal consistency of its own state**, but it does not perform cross-entity operations such as locating users or managing collections of tasks.

2\. Supporting Enums
====================

Supporting enums define controlled value sets used by the domain model.

They represent bounded classifications and do not have independent identity.

TaskStatus
----------

TaskStatus represents the **workflow state** of a task.

### Supported Values

*   TODO
    
*   IN\_PROGRESS
    
*   COMPLETED
    

### Purpose

*   Defines the allowed states in the task workflow
    
*   Helps enforce valid state transitions
    
*   Maintains a controlled workflow lifecycle
    

Priority
--------

Priority represents the **urgency level of a task**.

### Supported Values

*   LOW
    
*   MEDIUM
    
*   HIGH

Step 6: Data Structure Decisions
================================

At this stage of the design, we determine **how tasks and users will be stored and retrieved efficiently**.

Since the current implementation uses **in-memory storage**, choosing appropriate data structures is critical for maintaining good performance as the number of users and tasks grows.

The goal is to ensure that common operations such as **task lookup, assignment, and querying** remain efficient while keeping the design simple and extensible.

Design Considerations
=====================

The system must support the following operations:

*   Create a task
    
*   Find a task by ID
    
*   Assign a task to a user
    
*   Retrieve tasks assigned to a user
    
*   Retrieve tasks by status
    
*   Delete a task
    

If tasks were stored using a simple list, operations such as **finding a task by ID or deleting a task** would require scanning the entire list, resulting in **O(n)** time complexity.

To avoid this, we use **hash-based data structures** that provide constant-time lookups.

Task Storage
============

Tasks will be stored using a **HashMap**.

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   Map taskStore   `

### Key

taskId

### Value

Task object

### Reason

Using a HashMap allows:

*   **O(1) average time complexity** for task lookup
    
*   Efficient updates to task data
    
*   Fast deletion of tasks
    
*   Direct access to tasks without scanning the entire collection
    

This structure forms the **primary index** for all task-related operations.

User Storage
============

Users will also be stored using a **HashMap**.

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   Map userStore   `

### Key

userId

### Value

User object

### Reason

This allows:

*   Fast retrieval of users when assigning tasks
    
*   Efficient validation of user existence
    
*   Constant-time lookup during task assignment operations
    

Using a hash-based structure keeps **user-related operations lightweight and efficient**.

Task Queries
============

The system must support retrieving:

*   Tasks assigned to a specific user
    
*   Tasks filtered by status
    

For the initial implementation, these queries will be handled by **iterating over the taskStore**.

### Approach

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   for(Task task : taskStore.values())   `

### Reason

This approach is chosen because:

*   It keeps the initial implementation simple
    
*   The expected data volume is manageable in early phases
    
*   It avoids premature optimization
    
*   It keeps the system easier to maintain during early development
    

Although iteration is **O(n)**, it is acceptable for the current scope.

Future Optimization (Secondary Indexes)
=======================================

If the system grows and query performance becomes critical, additional **secondary indexes** can be introduced.

Possible optimizations include:

### Tasks by User

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   Map> tasksByUser   `

Key → userIdValue → List of tasks assigned to that user

This allows **O(1) access** to tasks belonging to a specific user.

### Tasks by Status

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   Map> tasksByStatus   `

Key → TaskStatusValue → List of tasks currently in that state

This allows fast retrieval of tasks based on workflow status.

Priority Handling
=================

Task priority will initially be represented using the **Priority enum** within the Task entity.

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   Priority priority   `

This allows tasks to be classified by urgency.

For the current implementation, priority-based sorting can be handled when retrieving tasks.

Future Enhancement for Priority-Based Retrieval
===============================================

If the system later requires **efficient retrieval of highest-priority tasks**, a **priority-based data structure** can be introduced.

Possible option:

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   PriorityQueue   `

This structure would allow:

*   Efficient retrieval of the **highest-priority task**
    
*   Automatic ordering based on priority
    
*   Support for scheduling or queue-based task processing

### Purpose

*   Classifies tasks based on urgency
    
*   Enables priority-based ordering and retrieval
    
*   Supports task prioritization logic
    

3\. Service Interface
=====================

TaskService (Interface)
-----------------------

The **TaskService interface** defines the contract for task-related operations in the system.

It describes **what operations are available**, without specifying how they are implemented.

### Declared Operations

*   createTask(...)
    
*   assignTask(taskId, userId)
    
*   updateStatus(taskId, status)
    
*   updatePriority(taskId, priority)
    
*   getTasksByUser(userId)
    
*   getTasksByStatus(status)
    
*   deleteTask(taskId)
    

### Purpose

The interface ensures that:

*   Higher-level components depend on **abstractions**
    
*   Implementations remain **replaceable**
    
*   The system follows the **Dependency Inversion Principle**
    

4\. Service Implementation
==========================

TaskServiceImpl
---------------

The **TaskServiceImpl** class provides the concrete implementation of the TaskService interface.

### Responsibilities

TaskServiceImpl is responsible for:

*   Implementing task-related business operations
    
*   Coordinating interactions between User and Task entities
    
*   Managing in-memory storage for tasks and users
    
*   Handling task assignment, updates, and queries
    

### Storage Strategy (Current Phase)

For the initial version, the implementation uses **in-memory data structures**, such as:

*   HashMap
    
*   HashMap
    

This provides efficient lookup and keeps the implementation simple while allowing future migration to database-backed storage.

5\. High-Level Class Relationships
==================================

The service layer coordinates operations between domain entities.

TaskServiceImpl --manages-> Task --assigned to--> User 

The Task entity also depends on supporting types:

Task = Priority, TaskStatus

### Relationship Explanation

*   **TaskServiceImpl → Task**The service manages the lifecycle and operations of tasks.
    
*   **Task → User**Each task maintains a reference to the user currently assigned to it.
    
*   **Task → Priority / TaskStatus**These enums represent controlled classifications used by the task.
    

This design keeps **entities simple, services responsible for orchestration, and supporting types lightweight**.

Step 6: Data Structure Decisions
================================

At this stage of the design, we determine **how tasks and users will be stored and retrieved efficiently**.

Since the current implementation uses **in-memory storage**, choosing appropriate data structures is critical for maintaining good performance as the number of users and tasks grows.

The goal is to ensure that common operations such as **task lookup, assignment, and querying** remain efficient while keeping the design simple and extensible.

Design Considerations
=====================

The system must support the following operations:

*   Create a task
    
*   Find a task by ID
    
*   Assign a task to a user
    
*   Retrieve tasks assigned to a user
    
*   Retrieve tasks by status
    
*   Delete a task
    

If tasks were stored using a simple list, operations such as **finding a task by ID or deleting a task** would require scanning the entire list, resulting in **O(n)** time complexity.

To avoid this, we use **hash-based data structures** that provide constant-time lookups.

Task Storage
============

Tasks will be stored using a **HashMap**.

`Map<String, Task> taskStore`

### Key

taskId

### Value

Task object

### Reason

Using a HashMap allows:

*   **O(1) average time complexity** for task lookup
    
*   Efficient updates to task data
    
*   Fast deletion of tasks
    
*   Direct access to tasks without scanning the entire collection
    

This structure forms the **primary index** for all task-related operations.

User Storage
============

Users will also be stored using a **HashMap**.
` Map<String, User> userStore  `

### Key

userId

### Value

User object

### Reason

This allows:

*   Fast retrieval of users when assigning tasks
    
*   Efficient validation of user existence
    
*   Constant-time lookup during task assignment operations
    

Using a hash-based structure keeps **user-related operations lightweight and efficient**.

Task Queries
============

The system must support retrieving:

*   Tasks assigned to a specific user
    
*   Tasks filtered by status
    

For the initial implementation, these queries will be handled by **iterating over the taskStore**.

### Approach

`   for(Task task : taskStore.values())   `

### Reason

This approach is chosen because:

*   It keeps the initial implementation simple
    
*   The expected data volume is manageable in early phases
    
*   It avoids premature optimization
    
*   It keeps the system easier to maintain during early development
    

Although iteration is **O(n)**, it is acceptable for the current scope.

Future Optimization (Secondary Indexes)
=======================================

If the system grows and query performance becomes critical, additional **secondary indexes** can be introduced.

Possible optimizations include:

### Tasks by User

`Map<String, List<Task>> tasksByUser`

Key → userIdValue → List of tasks assigned to that user

This allows **O(1) access** to tasks belonging to a specific user.

### Tasks by Status

Plain 
`Map<TaskStatus, List<Task>> tasksByStatus`

Key → TaskStatusValue → List of tasks currently in that state

This allows fast retrieval of tasks based on workflow status.

Priority Handling
=================

Task priority will initially be represented using the **Priority enum** within the Task entity.

`   Priority priority   `

This allows tasks to be classified by urgency.

For the current implementation, priority-based sorting can be handled when retrieving tasks.

Future Enhancement for Priority-Based Retrieval
===============================================

If the system later requires **efficient retrieval of highest-priority tasks**, a **priority-based data structure** can be introduced.

Possible option:
`   PriorityQueue   `

This structure would allow:

*   Efficient retrieval of the **highest-priority task**
    
*   Automatic ordering based on priority
    
*   Support for scheduling or queue-based task processing
