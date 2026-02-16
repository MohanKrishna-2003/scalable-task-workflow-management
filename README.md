# scalable-task-workflow-management

## Problem Statement
Modern teams require reliable backend systems to manage tasks, workflows, and ownership efficiently as the system scales.

The goal of this project is to design and implement a <b>scalable, extensible backend system</b> that supports task creation, assignment, workflow tracking, and prioritization while maintaining clean architecture and strong object-oriented design principles.

The system is designed to evolve incrementally, allowing new workflow rules, prioritization strategies, and scalability enhancements without major code changes. The focus is on backend engineering practices such as SOLID principles, proper data structure selection, and clean separation of responsibilities.

  
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
    
    *   unique task ID
        
    *   title
        
    *   description
        
    *   priority
        
    *   status
        
    *   creation timestamp
        
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

*   The system shall allow fetching:
    
    *   all tasks
        
    *   tasks assigned to a specific user
        
    *   tasks filtered by status
        
    *   tasks sorted by priority
        
*   The system shall support efficient task lookup by task ID.
    

### 7\. Workflow Extensibility (Very Important)

*   The system shall allow introduction of new task types in the future.
    
*   The system shall allow modification of workflow rules without impacting existing logic.
    

(**we will design this in future**.)

### 8\. Error Handling

*   The system shall handle invalid inputs gracefully.
    
*   Meaningful errors shall be returned for:
    
    *   invalid task ID
        
    *   invalid user ID
        
    *   invalid status transitions
