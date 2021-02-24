package com.jm.online_store.service.interf;

import com.jm.online_store.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    void deleteAllBlockedWithThirtyDaysPassed();

    List<Customer> findAll();

    boolean changePassword(Long id, String oldPassword, String newPassword);

    Optional<Customer> findById(Long id);

    void addCustomer(Customer customer);

    void cancelSubscription(Long id);

    List<Customer> findByDayOfWeekForStockSend(String dayOfWeek);

    List<Customer> findSubscriberByEmail(String email);

    void changeCustomerStatusToLocked(Long id);

    void changeCustomerProfileToDeletedProfileByID(long id);

    void updateCustomerDayOfWeekForStockSend(Customer customer, String dayOfWeekForStockSend);

    Customer getCurrentLoggedInUser();

    Customer getCurrentLoggedInUser(String sessionID);

    void updateCustomer(Customer customer);

    void restoreCustomer(String email);

    boolean isExist(String email);

    void deleteByID(Long id);

    Customer changeMail(String newMail);

    Customer getById(Long id);

    Customer findCustomerByEmail(String email);
}
