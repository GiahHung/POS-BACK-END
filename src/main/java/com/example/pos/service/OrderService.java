package com.example.pos.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pos.component.OrderCreatedEvent;
import com.example.pos.dto.ResponseData;
import com.example.pos.dto.orders.AddProductToOrderDTO;
import com.example.pos.dto.orders.CreateOrderDTO;
import com.example.pos.dto.orders.OrderOutputDTO;
import com.example.pos.dto.orders.RemoveProductFromOrderDTO;
import com.example.pos.dto.orders.UpdateOrderDTO;
import com.example.pos.model.Customer;
import com.example.pos.model.EmployeeDiscount;
import com.example.pos.model.OrderDetail;
import com.example.pos.model.Orders;
import com.example.pos.model.Product;
import com.example.pos.model.Voucher;
import com.example.pos.repository.CustomerRepository;
import com.example.pos.repository.EmployeeDiscountRepository;
import com.example.pos.repository.OrderDetailRepository;
import com.example.pos.repository.OrderRepository;
import com.example.pos.repository.ProductRepository;
import com.example.pos.repository.VoucherRepository;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private EmployeeDiscountRepository employeeDiscountRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private VoucherRepository voucherRepository;

    public ResponseData<Orders> createOrderService(CreateOrderDTO dto) {
        try {
            Orders orders = new Orders(null, dto.getEmployeeId(), null, 0, null, 0, "S1");
            Orders saveOrder = orderRepository.save(orders);
            return new ResponseData<Orders>(0, "Create success", saveOrder);
        } catch (Exception e) {
            return new ResponseData<Orders>(0, "error from server" + e.getMessage(), (Orders) null);
        }
    }

    public ResponseData<Orders> completeOrderService(UpdateOrderDTO dto) {
        try {
            if (dto.getId() == null) {
                return new ResponseData<Orders>(3, "Missing required field", (Orders) null);
            }

            Orders order = orderRepository.findById(dto.getId()).orElse(null);
            if (order == null) {
                return new ResponseData<Orders>(1, "Order not found", (Orders) null);
            }

            // Cập nhật trạng thái đơn hàng
            order.setStatusId("S2");
            order.setPaymentId(dto.getPaymentId());

            // Kiểm tra xem đơn hàng có khách hàng không
            if (order.getCustomerId() != null) {
                Customer customer = customerRepository.findById(order.getCustomerId()).orElse(null);
                if (customer != null) {
                    // Cộng thêm 5 điểm loyalty
                    customer.setLoyaltyPoint(customer.getLoyaltyPoint() + 5);
                    customerRepository.save(customer); // Lưu thông tin khách hàng
                }
            }

            orderRepository.save(order);

            eventPublisher.publishEvent(new OrderCreatedEvent(this, dto.getPaymentId(), dto.getTotalAmount()));

            return new ResponseData<Orders>(0, "Order completed successfully", (Orders) null);
        } catch (Exception e) {
            return new ResponseData<Orders>(1, "Error from server: " + e.getMessage(), (Orders) null);
        }
    }

    @Transactional
    public ResponseData<OrderOutputDTO> addProductToOrder(AddProductToOrderDTO dto) {

        Orders order = orderRepository.findById(dto.getOrderId()).orElse(null);
        if (order == null) {
            return new ResponseData<OrderOutputDTO>(1, "Order not found", (OrderOutputDTO) null);
        }

        Product product = productRepository.findById(dto.getProductId()).orElse(null);
        if (product == null) {
            return new ResponseData<OrderOutputDTO>(2, "Product not found", (OrderOutputDTO) null);
        }

        // Kiểm tra sản phẩm đã có trong đơn hàng chưa
        OrderDetail orderDetail = orderDetailRepository.findByOrderIdAndProductId(dto.getOrderId(),
                dto.getProductId());

        if (orderDetail != null) {
            // Nếu đã có, tăng số lượng
            int newQuantity = orderDetail.getQuantity() + 1;
            orderDetail.setQuantity(newQuantity);
            // Cập nhật totalPrice dựa trên số lượng mới và giá của sản phẩm
            orderDetail.setTotalPrice(newQuantity * product.getPrice());
        } else {
            // Nếu chưa có, thêm mới với số lượng = 1
            orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(1);
            orderDetail.setTotalPrice(product.getPrice());
        }

        // Cập nhật tổng tiền đơn hàng
        order.setTotalAmount(order.getTotalAmount() + product.getPrice());

        // Lưu thay đổi
        orderDetailRepository.save(orderDetail);
        orderRepository.save(order);

        Optional<Orders> optionalOrder = orderRepository.findById(order.getId());
        if (optionalOrder.isPresent()) {
            Orders orders = optionalOrder.get();
            List<OrderDetail> listDetail = orders.getOrderDetails();

            // Khởi tạo giá trị mặc định cho các biến
            String employeeName = "";
            String payment = "";
            String customerName = "";

            if (orders.getEmployee() != null) {
                employeeName = orders.getEmployee().getName();
            }
            if (orders.getPayment() != null) {
                payment = orders.getPayment().getValue();
            }
            if (orders.getCustomer() != null) { // Cần đảm bảo entity Orders có mối quan hệ với Customer
                customerName = orders.getCustomer().getName();
            }

            OrderOutputDTO outputDTO = new OrderOutputDTO(
                    orders.getId(),
                    employeeName,
                    customerName,
                    orders.getTotalAmount(),
                    payment,
                    orders.getDiscount(),
                    customerName, // Nếu đây là thông tin khác, hãy điều chỉnh lại
                    listDetail,
                    orders.getCreatedAt());
            outputDTO.setDescription(orders.getDescription());
            return new ResponseData<OrderOutputDTO>(0, "Get order after add product success", outputDTO);
        } else {
            return new ResponseData<OrderOutputDTO>(1, "Order not found", (OrderOutputDTO) null);
        }

    }

    public ResponseData<OrderOutputDTO> discountStaffService(String employeeCode, Long orderId) {
        try {
            if (employeeCode == null || employeeCode.isEmpty()) {
                return new ResponseData<OrderOutputDTO>(2, "Missing required field", (OrderOutputDTO) null);
            }

            EmployeeDiscount employeeDiscount = employeeDiscountRepository.findByEmployeeCode(employeeCode);
            if (employeeDiscount != null) {
                Orders orders = orderRepository.findById(orderId).orElse(null);
                if (orders == null) {
                    return new ResponseData<OrderOutputDTO>(5, "Order not found", (OrderOutputDTO) null);
                }
                double discountValue = employeeDiscount.getDiscountValue();
                double totalAmount = orders.getTotalAmount();
                double totalAfterDiscount = orders.getTotalAmount() * discountValue;
                orders.setTotalAmount(totalAfterDiscount);
                orders.setDiscount(totalAmount - totalAfterDiscount);
                orders.setDescription("Staff discount");
                orderRepository.save(orders);
                Optional<Orders> optionalOrder = orderRepository.findById(orders.getId());
                if (optionalOrder.isPresent()) {
                    Orders order = optionalOrder.get();
                    List<OrderDetail> listDetail = orders.getOrderDetails();

                    // Khởi tạo giá trị mặc định cho các biến
                    String employeeName = "";
                    String payment = "";
                    String customerName = "";
                    OrderOutputDTO outputDTO = new OrderOutputDTO(
                            order.getId(),
                            employeeName,
                            customerName,
                            order.getTotalAmount(),
                            payment,
                            order.getDiscount(),
                            customerName,
                            listDetail,
                            order.getCreatedAt());
                    outputDTO.setDescription(order.getDescription());
                    return new ResponseData<OrderOutputDTO>(0, "Get last order success", outputDTO);
                } else {
                    return new ResponseData<OrderOutputDTO>(6, "Order not found", (OrderOutputDTO) null);

                }
            } else {
                return new ResponseData<OrderOutputDTO>(4, "Invalid code", (OrderOutputDTO) null);
            }
        } catch (Exception e) {
            return new ResponseData<OrderOutputDTO>(1, "error from server" + e.getMessage(), (OrderOutputDTO) null);
        }

    }

    public ResponseData<OrderOutputDTO> discountVoucherService(String voucherCode, Long orderId) {
        try {
            if (voucherCode == null || voucherCode.isEmpty()) {
                return new ResponseData<>(2, "Missing required voucher code", null);
            }
            Voucher voucher = voucherRepository.findByVoucherCode(voucherCode);
            if (voucher == null || !voucher.getIsActive()) {
                return new ResponseData<>(3, "Voucher not exist", null);
            }
            Orders orders = orderRepository.findById(orderId).orElse(null);
            if (orders == null) {
                return new ResponseData<>(4, "Order not found", null);
            }

            double discountValue = voucher.getDiscount();
            double totalAmount = orders.getTotalAmount();
            double totalAfterDiscount = totalAmount - discountValue;

            if (totalAfterDiscount < 0) {
                totalAfterDiscount = 0;
            }
            orders.setTotalAmount(totalAfterDiscount);
            orders.setDiscount(discountValue);
            orders.setDescription("Voucher applied: ");
            orderRepository.save(orders);

            Optional<Orders> optionalOrder = orderRepository.findById(orders.getId());
            if (optionalOrder.isPresent()) {
                Orders order = optionalOrder.get();
                List<OrderDetail> listDetail = orders.getOrderDetails();

                // Khởi tạo giá trị mặc định cho các biến
                String employeeName = "";
                String payment = "";
                String customerName = "";
                OrderOutputDTO outputDTO = new OrderOutputDTO(
                        order.getId(),
                        employeeName,
                        customerName,
                        order.getTotalAmount(),
                        payment,
                        order.getDiscount(),
                        customerName,
                        listDetail,
                        order.getCreatedAt());
                outputDTO.setDescription(order.getDescription());
                return new ResponseData<OrderOutputDTO>(0, "Add voucher success", outputDTO);
            } else {
                return new ResponseData<OrderOutputDTO>(6, "Order not found", (OrderOutputDTO) null);

            }

        } catch (Exception e) {
            return new ResponseData<>(1, "Error from server: " + e.getMessage(), null);
        }
    }

    public ResponseData<OrderOutputDTO> selectCustomerService(Long customerId, Long orderId) {
        try {
            if (customerId == null || orderId == null) {
                return new ResponseData<OrderOutputDTO>(2, "Missing required field", (OrderOutputDTO) null);
            }
            Customer customer = customerRepository.findById(customerId).orElse(null);
            if (customer == null) {
                return new ResponseData<OrderOutputDTO>(3, "Customer not found", (OrderOutputDTO) null);
            }
            Orders orders = orderRepository.findById(orderId).orElse(null);
            if (orders == null) {
                return new ResponseData<OrderOutputDTO>(5, "Order not found", (OrderOutputDTO) null);
            }
            orders.setCustomerId(customer.getId());
            orderRepository.save(orders);
            Optional<Orders> optionalOrder = orderRepository.findById(orders.getId());
            if (optionalOrder.isPresent()) {
                Orders order = optionalOrder.get();
                List<OrderDetail> listDetail = orders.getOrderDetails();

                // Khởi tạo giá trị mặc định cho các biến
                String employeeName = "";
                String payment = "";
                String customerName = "";
                if (orders.getCustomer() != null) { // Cần đảm bảo entity Orders có mối quan hệ với Customer
                    customerName = orders.getCustomer().getName();
                }
                OrderOutputDTO outputDTO = new OrderOutputDTO(
                        order.getId(),
                        employeeName,
                        customerName,
                        order.getTotalAmount(),
                        payment,
                        order.getDiscount(),
                        customerName,
                        listDetail,
                        order.getCreatedAt());
                outputDTO.setDescription(order.getDescription());
                return new ResponseData<OrderOutputDTO>(0, "Get last order success", outputDTO);
            } else {
                return new ResponseData<OrderOutputDTO>(6, "Order not found", (OrderOutputDTO) null);

            }

        } catch (Exception e) {
            return new ResponseData<OrderOutputDTO>(1, "error from server" + e.getMessage(), (OrderOutputDTO) null);
        }
    }

    public ResponseData<OrderOutputDTO> getOrderService(Long orderId) {
        try {
            Optional<Orders> optionalOrder = orderRepository.findById(orderId);
            if (optionalOrder.isPresent()) {
                Orders order = optionalOrder.get();
                List<OrderDetail> listDetail = order.getOrderDetails();

                // Khởi tạo giá trị mặc định cho các biến
                String employeeName = "";
                String payment = "";
                String customerName = "";

                if (order.getEmployee() != null) {
                    employeeName = order.getEmployee().getName();
                }
                if (order.getPayment() != null) {
                    payment = order.getPayment().getValue();
                }
                if (order.getCustomer() != null) { // Cần đảm bảo entity Orders có mối quan hệ với Customer
                    customerName = order.getCustomer().getName();
                }

                OrderOutputDTO outputDTO = new OrderOutputDTO(
                        order.getId(),
                        employeeName,
                        customerName,
                        order.getTotalAmount(),
                        payment,
                        order.getDiscount(),
                        customerName,
                        listDetail,
                        order.getCreatedAt());
                outputDTO.setDescription(order.getDescription());
                return new ResponseData<OrderOutputDTO>(0, "Get order success", outputDTO);
            } else {
                return new ResponseData<OrderOutputDTO>(1, "Order not found", (OrderOutputDTO) null);
            }
        } catch (Exception e) {
            return new ResponseData<OrderOutputDTO>(-1, "Error: " + e.getMessage(), (OrderOutputDTO) null);
        }
    }

    public ResponseData<List<OrderOutputDTO>> getAllOrderService(LocalDateTime createdAt) {
        try {
            List<Orders> orders;
            if (createdAt != null) {
                orders = orderRepository.findByCreatedAtAndStatusId(createdAt, "S2");
            } else {
                orders = orderRepository.findByStatusId("S2");
            }

            // Tạo danh sách để chứa các OrderOutputDTO
            List<OrderOutputDTO> orderDTOs = new ArrayList<>();

            for (Orders order : orders) {
                List<OrderDetail> listDetail = order.getOrderDetails();
                String employeeName = "";
                String payment = "";
                String customerName = "";

                if (order.getEmployee() != null) {
                    employeeName = order.getEmployee().getName();
                }
                if (order.getPayment() != null) {
                    payment = order.getPayment().getValue();
                }
                if (order.getCustomer() != null) { // Đảm bảo rằng entity Orders có mối quan hệ với Customer
                    customerName = order.getCustomer().getName();
                }

                OrderOutputDTO outputDTO = new OrderOutputDTO(
                        order.getId(),
                        employeeName,
                        customerName,
                        order.getTotalAmount(),
                        payment,
                        order.getDiscount(),
                        customerName, // Nếu đây không phải là customerName thì hãy điều chỉnh lại theo mục đích
                        listDetail,
                        order.getCreatedAt());
                outputDTO.setDescription(order.getDescription());

                // Thêm DTO vừa tạo vào danh sách
                orderDTOs.add(outputDTO);
            }
            return new ResponseData<List<OrderOutputDTO>>(0, "Get all orders success", orderDTOs);
        } catch (Exception e) {
            return new ResponseData<List<OrderOutputDTO>>(-1, "Error: " + e.getMessage(), (List<OrderOutputDTO>) null);
        }
    }

    public ResponseData<OrderOutputDTO> getLastOrderService() {
        try {
            Optional<Orders> optionalOrder = orderRepository.findTopByOrderByIdDesc();
            if (optionalOrder.isPresent()) {
                Orders order = optionalOrder.get();
                // Nếu quan hệ orderDetails được cấu hình theo LAZY, có thể gọi size() để kích
                // hoạt load dữ liệu nếu cần.
                List<OrderDetail> listDetail = order.getOrderDetails();

                // Khởi tạo giá trị mặc định cho các biến
                String employeeName = "";
                String payment = "";
                String customerName = "";

                if (order.getEmployee() != null) {
                    employeeName = order.getEmployee().getName();
                }
                if (order.getPayment() != null) {
                    payment = order.getPayment().getValue();
                }
                if (order.getCustomer() != null) { // Cần đảm bảo entity Orders có mối quan hệ với Customer
                    customerName = order.getCustomer().getName();
                }

                OrderOutputDTO outputDTO = new OrderOutputDTO(
                        order.getId(),
                        employeeName,
                        customerName,
                        order.getTotalAmount(),
                        payment,
                        order.getDiscount(),
                        customerName, // Nếu đây là thông tin khác, hãy điều chỉnh lại
                        listDetail,
                        order.getCreatedAt());
                outputDTO.setDescription(order.getDescription());
                return new ResponseData<OrderOutputDTO>(0, "Get last order success", outputDTO);
            } else {
                return new ResponseData<OrderOutputDTO>(1, "No orders found", (OrderOutputDTO) null);
            }
        } catch (Exception e) {
            return new ResponseData<OrderOutputDTO>(-1, "Error: " + e.getMessage(), (OrderOutputDTO) null);
        }
    }

    @Transactional
    public ResponseData<OrderOutputDTO> removeProductFromOrder(RemoveProductFromOrderDTO dto) {
        // Tìm đơn hàng theo orderId
        Orders order = orderRepository.findById(dto.getOrderId()).orElse(null);
        if (order == null) {
            return new ResponseData<OrderOutputDTO>(1, "Order not found", (OrderOutputDTO) null);
        }

        // Tìm sản phẩm theo productId
        Product product = productRepository.findById(dto.getProductId()).orElse(null);
        if (product == null) {
            return new ResponseData<OrderOutputDTO>(2, "Product not found", (OrderOutputDTO) null);
        }

        // Kiểm tra sản phẩm có tồn tại trong đơn hàng hay không
        OrderDetail orderDetail = orderDetailRepository.findByOrderIdAndProductId(dto.getOrderId(), dto.getProductId());
        if (orderDetail == null) {
            return new ResponseData<OrderOutputDTO>(3, "Product not found in order", (OrderOutputDTO) null);
        }

        // Nếu số lượng của sản phẩm lớn hơn 1, giảm số lượng và cập nhật totalPrice
        if (orderDetail.getQuantity() > 1) {
            int newQuantity = orderDetail.getQuantity() - 1;
            orderDetail.setQuantity(newQuantity);
            orderDetail.setTotalPrice(newQuantity * product.getPrice());
            orderDetailRepository.save(orderDetail);
        } else {
            // Nếu số lượng bằng 1, xóa bản ghi chi tiết đơn hàng
            orderDetailRepository.delete(orderDetail);
        }

        // Cập nhật lại tổng tiền của đơn hàng: trừ đi giá của sản phẩm
        order.setTotalAmount(order.getTotalAmount() - product.getPrice());
        orderRepository.save(order);

        // Lấy lại đơn hàng sau khi cập nhật để trả về thông tin mới nhất
        Optional<Orders> optionalOrder = orderRepository.findById(order.getId());
        if (optionalOrder.isPresent()) {
            Orders orders = optionalOrder.get();
            List<OrderDetail> listDetail = orders.getOrderDetails();

            // Khởi tạo các thông tin mặc định
            String employeeName = "";
            String payment = "";
            String customerName = "";

            if (orders.getEmployee() != null) {
                employeeName = orders.getEmployee().getName();
            }
            if (orders.getPayment() != null) {
                payment = orders.getPayment().getValue();
            }
            if (orders.getCustomer() != null) {
                customerName = orders.getCustomer().getName();
            }

            OrderOutputDTO outputDTO = new OrderOutputDTO(
                    orders.getId(),
                    employeeName,
                    customerName,
                    orders.getTotalAmount(),
                    payment,
                    orders.getDiscount(),
                    customerName, // Nếu đây là thông tin khác, điều chỉnh lại cho phù hợp
                    listDetail,
                    orders.getCreatedAt());
            outputDTO.setDescription(orders.getDescription());
            return new ResponseData<OrderOutputDTO>(0, "Remove product from order success", outputDTO);
        } else {
            return new ResponseData<OrderOutputDTO>(1, "Order not found", (OrderOutputDTO) null);
        }
    }

    @Transactional
    public ResponseData<OrderOutputDTO> deleteCustomerFromOrder(Long orderId) {
        // Tìm đơn hàng theo orderId
        Orders order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return new ResponseData<OrderOutputDTO>(1, "Order not found", (OrderOutputDTO) null);
        }

        // Kiểm tra xem đơn hàng có khách hàng hay không
        if (order.getCustomerId() == null) {
            return new ResponseData<OrderOutputDTO>(2, "Order does not have a customer", (OrderOutputDTO) null);
        }

        // Xóa khách hàng khỏi đơn hàng
        order.setCustomerId(null);
        orderRepository.save(order);

        // Lấy lại đơn hàng sau khi cập nhật để trả về thông tin mới nhất
        Optional<Orders> optionalOrder = orderRepository.findById(order.getId());
        if (optionalOrder.isPresent()) {
            Orders updatedOrder = optionalOrder.get();
            List<OrderDetail> listDetail = updatedOrder.getOrderDetails();

            // Khởi tạo thông tin mặc định
            String employeeName = "";
            String payment = "";
            String customerName = ""; // Vì đã xóa khách hàng nên để trống

            if (updatedOrder.getEmployee() != null) {
                employeeName = updatedOrder.getEmployee().getName();
            }
            if (updatedOrder.getPayment() != null) {
                payment = updatedOrder.getPayment().getValue();
            }

            OrderOutputDTO outputDTO = new OrderOutputDTO(
                    updatedOrder.getId(),
                    employeeName,
                    customerName,
                    updatedOrder.getTotalAmount(),
                    payment,
                    updatedOrder.getDiscount(),
                    customerName, // Giá trị này có thể điều chỉnh nếu cần
                    listDetail,
                    updatedOrder.getCreatedAt());
            outputDTO.setDescription(updatedOrder.getDescription());

            return new ResponseData<OrderOutputDTO>(0, "Remove customer from order success", outputDTO);
        } else {
            return new ResponseData<OrderOutputDTO>(1, "Order not found after update", (OrderOutputDTO) null);
        }
    }

    public ResponseData<Orders> deleteOrderService(Long id) {
        try {
            if (id == null) {
                return new ResponseData<>(2, "Missing required field", null);
            }

            Orders order = orderRepository.findById(id).orElse(null);
            if (order == null) {
                return new ResponseData<>(1, "Order not found", null);
            }

            orderRepository.deleteById(id);
            return new ResponseData<>(0, "Order deleted successfully", null);
        } catch (Exception e) {
            return new ResponseData<>(3, "Error from server: " + e.getMessage(), null);
        }
    }

}
