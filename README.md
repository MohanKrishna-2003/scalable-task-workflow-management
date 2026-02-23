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
