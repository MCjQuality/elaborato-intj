package it.unibs.ing.elaborato;

import java.util.Objects;

public class Consumer extends User {
	
	private static final long serialVersionUID = 1L;
	private String email;
	private String district;

	public Consumer(String username, String psw, String district, String email) {
		super(username, psw);
		this.district = district;
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public String getDistrict() {
		return district;
	}

	@Override
	boolean isConsumer() {
		return true;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Consumer)) return false;
        Consumer consumer = (Consumer) o;
        return super.getUsername().equals(consumer.getUsername()) && super.getPsw().equals(consumer.getPsw()) && this.getEmail().equals(consumer.getEmail()) && this.getDistrict().equals(consumer.getDistrict());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getUsername(), super.getPsw(), email, district);
    }

}
