-- Insert tasks into the task table
INSERT INTO task (id, title, description, status, priority, due_date, created_at, updated_at)
VALUES
-- Regular Tasks
(1, 'Prepare Project Plan', 'Create a detailed project plan for Q4.', 'PENDING', 'HIGH', '2025-09-30', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Design Database Schema', 'Initial ER diagram and table structure.', 'IN_PROGRESS', 'MEDIUM', '2025-09-25', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Set up CI/CD Pipeline', 'Integrate Jenkins with AKS cluster.', 'PENDING', 'HIGH', '2025-10-05', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Develop REST APIs', 'Implement core APIs in Spring Boot.', 'IN_PROGRESS', 'HIGH', '2025-10-15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Write Unit Tests', 'Achieve at least 80% coverage.', 'PENDING', 'MEDIUM', '2025-09-28', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Update Documentation', 'Add new endpoints to API docs.', 'COMPLETED', 'LOW', '2025-09-10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'Deploy to Staging', 'Test deployment on staging environment.', 'PENDING', 'HIGH', '2025-10-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'Collect Feedback', 'Gather input from QA team.', 'IN_PROGRESS', 'MEDIUM', '2025-10-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'Finalize UI Design', 'Approve UI mockups.', 'COMPLETED', 'LOW', '2025-09-15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Overdue PENDING Tasks
(10, 'Research New Features', 'Explore new features for the upcoming release.', 'PENDING', 'HIGH', '2025-09-18', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 'Client Meeting Preparation', 'Prepare agenda and discussion points for client meeting.', 'PENDING', 'MEDIUM', '2025-09-20', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 'Write Release Notes', 'Document the changes for the next release.', 'PENDING', 'LOW', '2025-09-12', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Overdue IN_PROGRESS Tasks
(13, 'Design Frontend UI', 'Design the user interface for the dashboard.', 'IN_PROGRESS', 'HIGH', '2025-09-18', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(14, 'Configure Logging System', 'Set up centralized logging for all microservices.', 'IN_PROGRESS', 'MEDIUM', '2025-09-22', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(15, 'Optimize Database Queries', 'Refactor SQL queries to improve performance.', 'IN_PROGRESS', 'LOW', '2025-09-14', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
