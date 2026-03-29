package com.atechproc.service.cart;

import com.atechproc.dto.CartDto;
import com.atechproc.dto.CartItemDto;
import com.atechproc.enums.ACCOUNT_STATUS;
import com.atechproc.enums.MEDICINE_STATUS;
import com.atechproc.exception.ResourceNotFoundException;
import com.atechproc.mapper.CartMapper;
import com.atechproc.mapper.PharmacyMapper;
import com.atechproc.model.*;
import com.atechproc.repository.CartItemRepository;
import com.atechproc.repository.CartRepository;
import com.atechproc.repository.MedicineRepository;
import com.atechproc.service.medicine.IMedicineService;
import com.atechproc.service.pharmacy.IPharmacyService;
import com.atechproc.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final IMedicineService medicineService;
    private final IPharmacyService pharmacyService;
    private final MedicineRepository medicineRepository;
    private final IUserService userService;

    @Override
    public CartDto addItemToCart(Long medicineId, String jwt) throws Exception {

        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        if (!pharmacy.isOpen()) {
            throw new Exception("You can't achieve this actions because the pharmacy is closed for the moments");
        }
        
        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }

        Medicine medicine = medicineService.getMedicineById(medicineId);

        if (!medicine.getStatus().equals(MEDICINE_STATUS.AVAILABLE)) {
            throw new Exception("This medicine is expired");
        }

        if (!medicine.isInStock()) {
            throw new Exception("This medicine is outta stock");
        }

        if (!pharmacy.getMedicines().contains(medicine)) {
            throw new Exception("You can't add this medicine to the cart");
        }

        Cart cart = pharmacy.getCart();

        for (CartItem cartItem : cart.getItems()) {
            if (cartItem.getMedicine().getId().equals(medicine.getId())) {
                return CartMapper.toCartDto(cart);
            }
        }

        CartItem cartItem = new CartItem();
        cartItem.setMedicine(medicine);
        cartItem.setTotal(cartItem.calculateCartItemTotal());
        cartItem.setProfit(cartItem.calculateCartItemProfit());
        cartItem.setCart(cart);

        CartItem savedCartItem = cartItemRepository.save(cartItem);

        cart.getItems().add(savedCartItem);
        cart.setTotal(cart.calculateTotal());
        cart.setProfit(cart.calculateCartProfit());

        Cart savedCart = cartRepository.save(cart);

        medicine.setQuantity(medicine.getQuantity() - 1);
        medicine.setInStock(medicine.verifyIsInStock());
        medicine.setLow(medicine.updateLowVerification());
        medicineRepository.save(medicine);

        return CartMapper.toCartDto(savedCart);
    }

    @Override
    public CartItemDto updateCartItemQuantity(Long id, int quantity, String jwt)
            throws Exception {

        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        if (!pharmacy.isOpen()) {
            throw new Exception("You can't achieve this actions because the pharmacy is closed for the moments");
        }
        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }

        if (quantity == 0) {
            removeItemFromCart(id, jwt);
            return null;
        }

        CartItem cartItem = getCartItemById(id);

        Cart cart = pharmacy.getCart();

        if (!cart.getItems().contains(cartItem)) {
            throw new Exception("Your cant update this cart item quantity");
        }

        Medicine medicine = cartItem.getMedicine();

        if (medicine.getQuantity() == 0 && cartItem.getQuantity() < quantity) {
            throw new Exception("Insufficient stock available.");
        }

        if (quantity >= 0) {
            if (cartItem.getQuantity() < quantity) {
                medicine.setQuantity(medicine.getQuantity() - 1);
            } else {
                // throw new Exception("{{{{{{{{{{{{{{ "+quantity);
                medicine.setQuantity(medicine.getQuantity() + 1);
            }
            medicine.setInStock(medicine.verifyIsInStock());
        }

        medicineRepository.save(medicine);

        cartItem.setQuantity(quantity);
        cartItem.setTotal(cartItem.calculateCartItemTotal());
        cartItem.setProfit(cartItem.calculateCartItemProfit());
        cart.setProfit(cart.calculateCartProfit());

        CartItem savedCartItem = cartItemRepository.save(cartItem);

        cart.setTotal(cart.calculateTotal());
        cart.setProfit(cart.calculateCartProfit());
        cartRepository.save(cart);

        return CartMapper.toCartItemDto(savedCartItem);
    }

    @Override
    public CartItem getCartItemById(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id " + id));
    }

    @Override
    public Cart getPharmacyCart(String jwt) {
        return pharmacyService.getPharmacyByUser(jwt).getCart();
    }

    @Override
    public CartDto removeItemFromCart(Long id, String jwt) throws Exception {

        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        if (!pharmacy.isOpen()) {
            throw new Exception("You can't achieve this actions because the pharmacy is closed for the moments");
        }
        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }

        Cart cart = getPharmacyCart(jwt);
        CartItem item = getCartItemById(id);
        if (!cart.getItems().contains(item)) {
            throw new Exception("You are not allowed to remove this item from the cart");
        }

        // handle quantity in the medicine before deleting the item :

        Medicine medicine = item.getMedicine();
        medicine.setQuantity(medicine.getQuantity() + item.getQuantity());
        medicine.setInStock(medicine.verifyIsInStock());
        medicine.updateLowVerification();

        medicineRepository.save(medicine);

        cart.getItems().remove(item);
        cart.setTotal(cart.calculateTotal());
        cart.setProfit(cart.calculateCartProfit());

        Cart savedCart = cartRepository.save(cart);
        cartItemRepository.deleteById(id);

        return CartMapper.toCartDto(savedCart);
    }

    @Override
    public void cancelCart(String jwt) throws Exception {

        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        if (!pharmacy.isOpen()) {
            throw new Exception("You can't achieve this actions because the pharmacy is closed for the moments");
        }
        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }

        Cart cart = pharmacy.getCart();

        for (CartItem item : cart.getItems()) {
            Medicine medicine = item.getMedicine();
            medicine.setQuantity(medicine.getQuantity() + item.getQuantity());
            medicine.updateLowVerification();
            medicineRepository.save(medicine);
        }

        cart.getItems().clear();
        cart.setTotal(cart.calculateTotal());
        cart.setProfit(cart.calculateCartProfit());

        cartRepository.save(cart);
    }

    @Override
    public void clearCart(String jwt) throws Exception {

        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        if (!pharmacy.isOpen()) {
            throw new Exception("You can't achieve this actions because the pharmacy is closed for the moments");
        }
        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }

        Cart cart = pharmacy.getCart();

        cart.getItems().clear();
        cart.calculateCartProfit();
        cart.calculateTotal();
        cart.setProfit(BigDecimal.ZERO);
        cart.setTotal(BigDecimal.ZERO);
        cartRepository.save(cart);
    }
}
