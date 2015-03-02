package lv.javaguru.java2.domain;

public class ProductInCart extends Product {
    private Integer count;
    private boolean isOrdered;

    public ProductInCart(Long productID, String name, String description, float price,
                         String imageURL, Integer count, boolean isOrdered) {
        this.productId = productID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageURL = imageURL;
        this.count = count;
        this.isOrdered = isOrdered;
    }

    public ProductInCart(Product product, Integer count, boolean isOrdered) {
        this.productId = product.getProductId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.imageURL = product.getImage();
        this.count = count;
        this.isOrdered = isOrdered;
    }

    public Integer getCount() { return count; }
    public void setCount(int value) { count = value; }

    public boolean getIsOrdered() { return isOrdered; }
    public void setIsOrdered(boolean value) { isOrdered = value; }
}