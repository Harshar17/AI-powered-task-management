import React, { useState } from "react";
import api from "../../services/api";
import "./TaskForm.css";

const TaskForm = ({ onTaskCreated }) => {

    const [title, setTitle] = useState("");

    const [aiTitle, setAiTitle] = useState("");
    const [description, setDescription] = useState("");
    const [priority, setPriority] = useState("MEDIUM");
    const [estimatedTime, setEstimatedTime] = useState("");

    const [aiGenerated, setAiGenerated] = useState(false);
    const [loadingAI, setLoadingAI] = useState(false);
    const [creating, setCreating] = useState(false);


    // ==============================
    // GENERATE TASK WITH AI
    // ==============================

    const handleGenerateAI = async () => {

        if (!title.trim()) {

            alert("Please enter a task title.");

            return;
        }

        try {

            setLoadingAI(true);

            const response = await api.post(
                "/ai/generate-task",
                null,
                {
                    params: {
                        title: title
                    }
                }
            );

            console.log("AI RESPONSE:", response.data);

            setAiTitle(response.data.title);
            setDescription(response.data.description);
            setPriority(response.data.priority);
            setEstimatedTime(response.data.estimatedTime);

            setAiGenerated(true);

        } catch (error) {

            console.error("AI ERROR:", error);

            if (error.response) {

                alert(
                    error.response.data?.message ||
                    "Failed to generate task with AI"
                );

            } else {

                alert("Server connection failed");
            }

        } finally {

            setLoadingAI(false);
        }
    };


    // ==============================
    // CREATE TASK
    // ==============================

    const handleCreateTask = async () => {

        try {

            setCreating(true);

            const task = {

                title: aiTitle,

                description: description,

                status: "TODO",

                priority: priority
            };

            const response = await api.post(
                "/tasks",
                task
            );

            console.log(
                "TASK CREATED:",
                response.data
            );

            onTaskCreated(response.data);

            // Clear form

            setTitle("");
            setAiTitle("");
            setDescription("");
            setPriority("MEDIUM");
            setEstimatedTime("");
            setAiGenerated(false);

            alert("Task created successfully!");

        } catch (error) {

            console.error("CREATE TASK ERROR:", error);

            if (error.response) {

                alert(
                    error.response.data?.message ||
                    "Failed to create task"
                );

            } else {

                alert("Server connection failed");
            }

        } finally {

            setCreating(false);
        }
    };


    return (

        <div className="task-form">

            {!aiGenerated ? (

                <>
                    <h2>
                        ✨ Create Task with AI
                    </h2>

                    <p className="task-form-subtitle">
                        Enter a task title and let AI generate
                        the description, priority and estimated time.
                    </p>


                    <label>
                        Task Title
                    </label>

                    <input
                        type="text"
                        placeholder="Example: Build login page"
                        value={title}
                        onChange={(e) =>
                            setTitle(e.target.value)
                        }
                    />


                    <button
                        type="button"
                        className="ai-button"
                        onClick={handleGenerateAI}
                        disabled={loadingAI}
                    >

                        {loadingAI
                            ? "✨ Generating..."
                            : "✨ Generate Task with AI"
                        }

                    </button>
                </>

            ) : (

                <div className="ai-result">

                    <h2>
                        ✨ AI Generated Task
                    </h2>


                    {/* TITLE */}

                    <label>
                        Task Title
                    </label>

                    <input
                        type="text"
                        value={aiTitle}
                        onChange={(e) =>
                            setAiTitle(e.target.value)
                        }
                    />


                    {/* DESCRIPTION */}

                    <label>
                        Description
                    </label>

                    <textarea
                        value={description}
                        onChange={(e) =>
                            setDescription(e.target.value)
                        }
                    />


                    {/* PRIORITY */}

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


                    {/* ESTIMATED TIME */}

                    <div className="estimated-time">

                        <strong>
                            Estimated Time:
                        </strong>

                        <span>
                            {estimatedTime}
                        </span>

                    </div>


                    {/* CREATE */}

                    <button
                        type="button"
                        className="create-button"
                        onClick={handleCreateTask}
                        disabled={creating}
                    >

                        {creating
                            ? "Creating..."
                            : "Create Task"
                        }

                    </button>

                </div>
            )}

        </div>
    );
};

export default TaskForm;