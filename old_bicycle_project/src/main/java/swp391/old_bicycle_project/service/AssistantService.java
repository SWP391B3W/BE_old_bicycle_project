package swp391.old_bicycle_project.service;

import swp391.old_bicycle_project.dto.assistant.AssistantChatRequestDTO;
import swp391.old_bicycle_project.dto.assistant.AssistantChatResponseDTO;
import swp391.old_bicycle_project.entity.User;

public interface AssistantService {
    AssistantChatResponseDTO chat(AssistantChatRequestDTO request, User currentUser);
}
