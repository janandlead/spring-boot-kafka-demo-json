package com.example.kafkademo.model;

public class Customer {

    private Long id;
    private String name;
    private String email;

    /**
     * Creates an empty customer for JSON deserialization.
     */
    public Customer() {
    }

    /**
     * Creates a customer with all fields initialized.
     *
     * @param id customer identifier
     * @param name customer name
     * @param email customer email address
     */
    public Customer(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Returns the customer identifier.
     *
     * @return customer identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * Updates the customer identifier.
     *
     * @param id customer identifier
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the customer name.
     *
     * @return customer name
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the customer name.
     *
     * @param name customer name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the customer email address.
     *
     * @return customer email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the customer email address.
     *
     * @param email customer email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns a readable representation of the customer.
     *
     * @return customer values formatted as text
     */
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
