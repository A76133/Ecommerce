package in.main.util;

public enum OrderStatus {

    IN_PROGRESS(1, "in progress"),
    ORDER_RECIEVED(2, "order recieved"),
    PRODUCT_PACKED(3, "product packed"),
    OUT_FOR_DELIVERY(4, "out for delivery"),
    DELIVERED(5, "delivered"),
	CANCEL(6,"Cancelled"),
	SUCCESS(7,"Success");

    private Integer id;
    private String name;

    private OrderStatus(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String setName(String name) {
        return name;
    }
}