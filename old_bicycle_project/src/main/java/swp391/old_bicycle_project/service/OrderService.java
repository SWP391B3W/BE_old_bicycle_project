package swp391.old_bicycle_project.service;

import swp391.old_bicycle_project.dto.request.OrderCreateRequestDTO;
import swp391.old_bicycle_project.dto.response.OrderResponseDTO;
import swp391.old_bicycle_project.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponseDTO createOrder(User currentUser, OrderCreateRequestDTO requestDTO);

    List<OrderResponseDTO> getMyOrders(User currentUser);

    OrderResponseDTO acceptOrder(UUID orderId, User currentUser);

    OrderResponseDTO confirmDeposit(UUID orderId, User currentUser);

    OrderResponseDTO completeOrder(UUID orderId, User currentUser, String note, List<MultipartFile> files);

    OrderResponseDTO confirmReceived(UUID orderId, User currentUser, String note, List<MultipartFile> files);

    OrderResponseDTO cancelOrder(UUID orderId, User currentUser);

    int autoCompleteOverdueBuyerConfirmations();
}
