package swp391.old_bicycle_project.service;

import org.springframework.web.multipart.MultipartFile;
import swp391.old_bicycle_project.dto.order.OrderCreateRequest;
import swp391.old_bicycle_project.dto.order.OrderResponse;
import swp391.old_bicycle_project.entity.User;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponse createOrder(User currentUser, OrderCreateRequest request);

    List<OrderResponse> getMyOrders(User currentUser);

    OrderResponse acceptOrder(UUID orderId, User currentUser);

    OrderResponse confirmDeposit(UUID orderId, User currentUser);

    OrderResponse completeOrder(UUID orderId, User currentUser, String note, List<MultipartFile> files);

    OrderResponse confirmReceived(UUID orderId, User currentUser, String note, List<MultipartFile> files);

    OrderResponse cancelOrder(UUID orderId, User currentUser);
}
