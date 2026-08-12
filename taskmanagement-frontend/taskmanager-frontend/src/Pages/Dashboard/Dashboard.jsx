import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import api from "../../services/api";

import TaskForm from "../../components/TaskForm/TaskForm";
import TaskCard from "../../components/TaskCard/TaskCard";
import Navbar from "../../components/Navbar/Navbar";

import "./Dashboard.css";


const Dashboard = () => {

    const [tasks, setTasks] = useState([]);
    const [loading, setLoading] = useState(true);

    const [search, setSearch] = useState("");
    const [statusFilter, setStatusFilter] = useState("ALL");
    const [priorityFilter, setPriorityFilter] = useState("ALL");
    const [sortBy, setSortBy] = useState("NEWEST");

    const navigate = useNavigate();


    useEffect(() => {
        fetchTasks();
    }, []);


    const fetchTasks = async () => {

        try {

            const response = await api.get("/tasks");

            setTasks(response.data);

        } catch (error) {

            console.error(error);

            if (error.response?.status === 401) {

                localStorage.removeItem("token");

                alert("Session expired. Please login again.");

                navigate("/login");

            } else {

                alert("Failed to load tasks");
            }

        } finally {

            setLoading(false);
        }
    };


    const handleTaskCreated = (newTask) => {

        setTasks((previousTasks) => [
            ...previousTasks,
            newTask
        ]);
    };


    const handleDelete = (id) => {

        setTasks((previousTasks) =>
            previousTasks.filter(
                (task) => task.id !== id
            )
        );
    };


    const handleStatusChange = (updatedTask) => {

        setTasks((previousTasks) =>
            previousTasks.map((task) =>
                task.id === updatedTask.id
                    ? updatedTask
                    : task
            )
        );
    };


    const handleTaskUpdated = (updatedTask) => {

        setTasks((previousTasks) =>
            previousTasks.map((task) =>
                task.id === updatedTask.id
                    ? updatedTask
                    : task
            )
        );
    };


    // ================================
    // STATISTICS
    // ================================

    const totalTasks = tasks.length;

    const todoTasks = tasks.filter(
        (task) => task.status === "TODO"
    ).length;

    const inProgressTasks = tasks.filter(
        (task) => task.status === "IN_PROGRESS"
    ).length;

    const completedTasks = tasks.filter(
        (task) => task.status === "COMPLETED"
    ).length;

    const highPriorityTasks = tasks.filter(
        (task) => task.priority === "HIGH"
    ).length;


    // ================================
    // FILTER + SEARCH + SORT
    // ================================

    const filteredTasks = tasks
        .filter((task) => {

            const matchesSearch =
                task.title
                    ?.toLowerCase()
                    .includes(search.toLowerCase());

            const matchesStatus =
                statusFilter === "ALL" ||
                task.status === statusFilter;

            const matchesPriority =
                priorityFilter === "ALL" ||
                task.priority === priorityFilter;

            return (
                matchesSearch &&
                matchesStatus &&
                matchesPriority
            );
        })
        .sort((a, b) => {

            if (sortBy === "NEWEST") {
                return b.id - a.id;
            }

            if (sortBy === "OLDEST") {
                return a.id - b.id;
            }

            if (sortBy === "PRIORITY") {

                const priorityOrder = {
                    HIGH: 3,
                    MEDIUM: 2,
                    LOW: 1
                };

                return (
                    priorityOrder[b.priority] -
                    priorityOrder[a.priority]
                );
            }

            return 0;
        });


    return (

        <>

            <Navbar
                userName={tasks[0]?.userName}
            />


            <div className="dashboard">

                {/* TITLE */}

                <div className="dashboard-title">

                    <h1>Dashboard</h1>

                    <p>
                        Manage your tasks efficiently with AI
                    </p>

                </div>


                {/* AI TASK FORM */}

                <TaskForm
                    onTaskCreated={handleTaskCreated}
                />


                {/* STATISTICS */}

                <div className="stats-container">

                    <div className="stat-card">
                        <span className="stat-title">
                            Total Tasks
                        </span>

                        <span className="stat-number">
                            {totalTasks}
                        </span>
                    </div>


                    <div className="stat-card">
                        <span className="stat-title">
                            TODO
                        </span>

                        <span className="stat-number">
                            {todoTasks}
                        </span>
                    </div>


                    <div className="stat-card">
                        <span className="stat-title">
                            In Progress
                        </span>

                        <span className="stat-number">
                            {inProgressTasks}
                        </span>
                    </div>


                    <div className="stat-card">
                        <span className="stat-title">
                            Completed
                        </span>

                        <span className="stat-number">
                            {completedTasks}
                        </span>
                    </div>


                    <div className="stat-card">
                        <span className="stat-title">
                            High Priority
                        </span>

                        <span className="stat-number">
                            {highPriorityTasks}
                        </span>
                    </div>

                </div>


                {/* TASK HEADER */}

                <div className="tasks-header">

                    <div>
                        <h2>My Tasks</h2>

                        <span className="task-count">
                            {tasks.length} task
                            {tasks.length !== 1 ? "s" : ""}
                        </span>
                    </div>

                </div>


                {/* SEARCH / FILTER */}

                <div className="task-controls">

                    <div className="search-box">

                        <input
                            type="text"
                            placeholder="🔍 Search tasks by title..."
                            value={search}
                            onChange={(e) =>
                                setSearch(e.target.value)
                            }
                        />

                    </div>


                    <div className="filter-group">

                        <label>Status</label>

                        <select
                            value={statusFilter}
                            onChange={(e) =>
                                setStatusFilter(e.target.value)
                            }
                        >

                            <option value="ALL">All</option>
                            <option value="TODO">TODO</option>
                            <option value="IN_PROGRESS">
                                In Progress
                            </option>
                            <option value="COMPLETED">
                                Completed
                            </option>

                        </select>

                    </div>


                    <div className="filter-group">

                        <label>Priority</label>

                        <select
                            value={priorityFilter}
                            onChange={(e) =>
                                setPriorityFilter(e.target.value)
                            }
                        >

                            <option value="ALL">All</option>
                            <option value="HIGH">High</option>
                            <option value="MEDIUM">Medium</option>
                            <option value="LOW">Low</option>

                        </select>

                    </div>


                    <div className="filter-group">

                        <label>Sort</label>

                        <select
                            value={sortBy}
                            onChange={(e) =>
                                setSortBy(e.target.value)
                            }
                        >

                            <option value="NEWEST">Newest</option>
                            <option value="OLDEST">Oldest</option>
                            <option value="PRIORITY">Priority</option>

                        </select>

                    </div>

                </div>


                {/* TASK CONTENT */}

                {loading ? (

                    <div className="state-box">

                        <div className="spinner"></div>

                        <p>Loading your tasks...</p>

                    </div>

                ) : tasks.length === 0 ? (

                    <div className="state-box empty-state">

                        <div className="empty-icon">
                            📋
                        </div>

                        <h3>No tasks yet</h3>

                        <p>
                            Create your first task using the
                            AI task generator above.
                        </p>

                    </div>

                ) : filteredTasks.length === 0 ? (

                    <div className="state-box empty-state">

                        <div className="empty-icon">
                            🔍
                        </div>

                        <h3>No matching tasks</h3>

                        <p>
                            Try changing your search or filters.
                        </p>

                        <button
                            className="clear-filter-button"
                            onClick={() => {

                                setSearch("");
                                setStatusFilter("ALL");
                                setPriorityFilter("ALL");

                            }}
                        >
                            Clear Filters
                        </button>

                    </div>

                ) : (

                    <div className="task-list">

                        {filteredTasks.map((task) => (

                            <TaskCard
                                key={task.id}
                                task={task}
                                onDelete={handleDelete}
                                onStatusChange={handleStatusChange}
                                onTaskUpdated={handleTaskUpdated}
                            />

                        ))}

                    </div>
                )}

            </div>

        </>
    );
};


export default Dashboard;