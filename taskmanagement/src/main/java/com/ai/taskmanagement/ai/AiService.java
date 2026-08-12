package com.ai.taskmanagement.ai;

import com.ai.taskmanagement.dto.AiResponse;

public interface AiService {

    AiResponse generateTask(String title);

}