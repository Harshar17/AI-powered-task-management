import React, { useState } from "react";
import api from "../../services/api";
import "./TaskCard.css";

const TaskCard = ({
    task,
    onDelete,
    onStatusChange,
    onTaskUpdated
}) => {

    const [editing, setEditing] = useState(false);

    const [title, setTitle] = useState(task.title);
    const [description, setDescription] = useState(task.description);
    const [priority, setPriority] = useState(task.priority);


    // ========================================
    // DELETE
    // ========================================

    const handleDelete = async () => {

        const confirmDelete = window.confirm(
            "Are you sure you want to delete this task?"
        );

        if (!confirmDelete) {
            return;
        }

        try {

            await api.delete(`/tasks/${task.id}`);

            onDelete(task.id);

        } catch (error) {

            console.error(error);

            alert("Failed to delete task");
        }
    };


    // ========================================
    // STATUS CHANGE
    // ========================================

    const handleStatusChange = async (e) => {

        const newStatus = e.target.value;

        try {

            const response = await api.patch(
                `/tasks/${task.id}/status?status=${newStatus}`
            );

            onStatusChange(response.data);

        } catch (error) {

            console.error(error);

            alert("Failed to update status");
        }
    };


    // ========================================
    // UPDATE
    // ========================================

    const handleUpdate = async (e) => {

        e.preventDefault();

        try {

            const updatedTask = {
                title: title,
                description: description,
                status: task.status,
                priority: priority
            };

            const response = await api.put(
                `/tasks/${task.id}`,
                updatedTask
            );

            onTaskUpdated(response.data);

            setEditing(false);

            alert("Task updated successfully!");

        } catch (error) {

            console.error(error);

            alert("Failed to update task");
        }
    };


    // ========================================
    // CANCEL EDIT
    // ========================================

    const handleCancel = () => {

        setTitle(task.title);
        setDescription(task.description);
        setPriority(task.priority);

        setEditing(false);
    };


    // ========================================
    // DATE
    // ========================================

    const formattedDate = task.createdAt
        ? new Date(task.createdAt).toLocaleDateString()
        : "N/A";


    // ========================================
    // EDIT MODE
    // ========================================

    if (editing) {

        return (

            <div className="task-card edit-card">

                <div className="edit-header">

                    <h3>Edit Task</h3>

                </div>


                <form onSubmit={handleUpdate}>

                    <label>
                        Task Title
                    </label>

                    <input
                        type="text"
                        value={title}
                        onChange={(e) =>
                            setTitle(e.target.value)
                        }
                        required
                    />


                    <label>
                        Description
                    </label>

                    <textarea
                        value={description}
                        onChange={(e) =>
                            setDescription(e.target.value)
                        }
                        required
                    />


                    <label>
                        Priority
                    </label>

                    <select
                        value={priority}
                        onChange={(e) =>
                            setPriority(e.target.value)
                        }
                    >

                        <option value="LOW">
                            LOW
                        </option>

                        <option value="MEDIUM">
                            MEDIUM
                        </option>

                        <option value="HIGH">
                            HIGH
                        </option>

                    </select>


                    <div className="edit-buttons">

                        <button
                            type="submit"
                            className="save-button"
                        >
                            Save Changes
                        </button>


                        <button
                            type="button"
                            className="cancel-button"
                            onClick={handleCancel}
                        >
                            Cancel
                        </button>

                    </div>

                </form>

            </div>
        );
    }


    // ========================================
    // NORMAL CARD
    // ========================================

    return (

        <div className="task-card">

            {/* HEADER */}

            <div className="task-card-header">

                <div>

                    <h3>
                        {task.title}
                    </h3>

                    <span className="task-date">
                        Created: {formattedDate}
                    </span>

                </div>


                <span
                    className={`priority ${task.priority?.toLowerCase()}`}
                >
                    {task.priority}
                </span>

            </div>


            {/* DESCRIPTION */}

            <p className="task-description">
                {task.description}
            </p>


            {/* BOTTOM */}

            <div className="task-card-bottom">

                {/* STATUS */}

                <div className="status-section">

                    <label>
                        Status
                    </label>

                    <select
                        value={task.status}
                        onChange={handleStatusChange}
                    >

                        <option value="TODO">
                            TODO
                        </option>

                        <option value="IN_PROGRESS">
                            IN PROGRESS
                        </option>

                        <option value="COMPLETED">
                            COMPLETED
                        </option>

                    </select>

                </div>


                {/* ACTIONS */}

                <div className="task-actions">

                    <button
                        className="edit-button"
                        onClick={() =>
                            setEditing(true)
                        }
                    >
                        Edit
                    </button>


                    <button
                        className="delete-button"
                        onClick={handleDelete}
                    >
                        Delete
                    </button>

                </div>

            </div>

        </div>
    );
};


export default TaskCard;