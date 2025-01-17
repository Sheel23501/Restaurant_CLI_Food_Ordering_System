package utils;

import models.Order;
import java.util.Comparator;

public class OrderComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {

        if ("VIP".equals(o1.getCustomerType()) && !"VIP".equals(o2.getCustomerType())) {
            return -1;
        } else if (!"VIP".equals(o1.getCustomerType()) && "VIP".equals(o2.getCustomerType())) {
            return 1;
        } else {
            return 0;
        }
    }
}