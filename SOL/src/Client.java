public class Client extends Order{
    private String name;
    private String surname;
    private boolean register;
    private Address address;
    private Payment payment;

    public Client(String name, String surname, Address address, Payment payment) {
        this.name = name;
        this.surname = surname;
        this.register = true;
        this.address = address;     
        this.payment = payment;
    }  
       
    public void ChangeRegister(){
        this.register = !this.register;
    }

    public String getName() {
            return name;
    }

    public String getSurname() {
        return surname;
    }

    public boolean getRegister() {
        return register;
    }  
    
    public Address getAddress() {
        return address;
    }

    public Payment getPayment() {
        return payment;
    }
}
