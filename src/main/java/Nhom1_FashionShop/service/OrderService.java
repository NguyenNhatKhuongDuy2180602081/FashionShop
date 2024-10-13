package Nhom1_FashionShop.service;

import Nhom1_FashionShop.model.CartItem;
import Nhom1_FashionShop.model.Order;
import Nhom1_FashionShop.model.OrderDetail;
import Nhom1_FashionShop.repository.OrderDetailRepository;
import Nhom1_FashionShop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartService cartService;

    public Order createOrder(String customerName, String address, String phoneNumber, String eMail,
                             String note, String payment, List<CartItem> cartItems) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        System.out.println("Khởi tạo đơn hàng cho: " + username);

        Order order = new Order();
        order.setCustomerName(customerName);
        order.setUsername(username);
        order.setAddress(address);
        order.setPhoneNumber(phoneNumber);
        order.setEMail(eMail);
        order.setNote(note);
        order.setPayment(payment);
        order.setDate(LocalDateTime.now());

        double totalPrice = 0;

        order = orderRepository.save(order);

        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            orderDetailRepository.save(detail);

            totalPrice += item.getProduct().getPrice() * item.getQuantity();
        }


        cartService.clearCart();

        return order;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUsername(String username) {
        return orderRepository.findByUsername(username);
    }

    public Order updateOrderStatus(Long orderId, String newStatus) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(newStatus); // Cập nhật trạng thái mới
            return orderRepository.save(order); // Lưu vào cơ sở dữ liệu
        } else {
            throw new RuntimeException("Không tìm thấy đơn hàng với id: " + orderId);
        }
    }
}