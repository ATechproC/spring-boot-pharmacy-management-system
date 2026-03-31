package com.atechproc.service.Order;

import com.atechproc.dto.MedicineDto;
import com.atechproc.dto.OrderDto;
import com.atechproc.dto.OrderItemDto;
import com.atechproc.enums.ACCOUNT_STATUS;
import com.atechproc.exception.ResourceNotFoundException;
import com.atechproc.mapper.OrderMapper;
import com.atechproc.model.*;
import com.atechproc.repository.MedicineRepository;
import com.atechproc.repository.OrderItemRepository;
import com.atechproc.repository.OrderRepository;
import com.atechproc.service.cart.ICartService;
import com.atechproc.service.medicine.IMedicineService;
import com.atechproc.service.pharmacy.IPharmacyService;
import com.atechproc.service.user.IUserService;
import com.atechproc.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final IPharmacyService pharmacyService;
    private final OrderItemRepository orderItemRepository;
    private final ICartService cartService;
    private final MedicineRepository medicineRepository;
    private final IMedicineService medicineService;
    private final Utils utils;
    private final IUserService userService;

    @Override
    public OrderDto createOrder(String jwt) throws Exception {
        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        User user = userService.getUserProfile(jwt);

        Cart cart = pharmacy.getCart();

        if (cart.getItems().isEmpty()) {
            // throw new Exception("You can not finalize the sale because the cart is
            // empty");
            return null;
        }

        Order order = new Order();
        order.setProfit(cart.getProfit());
        order.setTotal(cart.getTotal());
        order.setPharmacy(pharmacy);
         order.setPharmacist(user);
        order.setOrders(null);

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {

            if (cartItem.getQuantity() == 0)
                continue;

            OrderItem orderItem = new OrderItem();
            orderItem.setMedicine(cartItem.getMedicine());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setProfit(cartItem.getProfit());
            orderItem.setTotal(cartItem.getTotal());
            orderItem.setOrder(savedOrder);

            Medicine medicine = cartItem.getMedicine();
            medicine.setSoldQuantity(medicine.getSoldQuantity() + cartItem.getQuantity());
            medicineRepository.save(medicine);

            OrderItem savedOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(savedOrderItem);
        }

        order.setOrders(orderItems);

        cartService.clearCart(jwt);

        return OrderMapper.toOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto getOrder(Long orderId, String jwt) throws Exception {
        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Order order = getOrderById(orderId);
        if (!pharmacy.getOrders().contains(order)) {
            throw new Exception("This order does not belong to this pharmacy");
        }
        return OrderMapper.toOrderDto(order);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
    }

    @Override
    public OrderItem getOrderItem(Long id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found"));
    }

    @Override
    public List<Order> getTodayOrders(String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        return orderRepository.findByPharmacy_idAndDate(
                pharmacy.getId(),
                LocalDate.now());
    }

    @Override
    public List<Order> getThisMonthOrders(String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        return orderRepository.findByPharmacy_idAndYearMonth(pharmacy.getId(), YearMonth.now().toString());
    }

    @Override
    public List<Order> getThisYearOrders(String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        return orderRepository.findByPharmacy_idAndYear(pharmacy.getId(), LocalDate.now().getYear());
    }

    @Override
    public List<Order> getThisWeekOrders(String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        int currentWeekOfMonth = LocalDate.now().get(WeekFields.ISO.weekOfMonth());
        return orderRepository.findByPharmacy_idAndWeekOfMonthAndYearMonth(
                pharmacy.getId(),
                currentWeekOfMonth,
                YearMonth.now().toString());
    }

    @Override
    public List<Order> getDayOrders(String jwt, LocalDate date) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        return orderRepository.findByPharmacy_idAndDate(
                pharmacy.getId(),
                date);
    }

    @Override
    public List<Order> getMonthOrders(String jwt, YearMonth yearMonth) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        return orderRepository.findByPharmacy_idAndYearMonth(pharmacy.getId(), yearMonth.toString());
    }

    @Override
    public List<Order> getYearOrders(String jwt, int year) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        return orderRepository.findByPharmacy_idAndYear(pharmacy.getId(), year);
    }

    @Override
    public List<Order> getWeekOfMonthOrders(String jwt, int weekOfMonth, YearMonth yearMonth) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        return orderRepository.findByPharmacy_idAndWeekOfMonthAndYearMonth(pharmacy.getId(), weekOfMonth,
                yearMonth.toString());
    }

    @Override
    public List<Order> getAllOrders(String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        return orderRepository.findByPharmacy_id(pharmacy.getId());
    }

    @Override
    public OrderItem getOrderItemByMedicine(Long id) {
        Medicine medicine = medicineService.getMedicineById(id);
        OrderItem orderItem = orderItemRepository.findByMedicine_id(medicine.getId());
        if (orderItem == null) {
            throw new ResourceNotFoundException("Order not found with medicine id " + medicine.getId());
        }
        return orderItem;
    }

    @Override
    public List<OrderItemDto> getLastOrderItems(String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        List<OrderItem> items = orderItemRepository.searchLastOrderItems(pharmacy.getId(), LocalDate.now());
        return OrderMapper.toOrderItemDTOs(items);
    }
}
