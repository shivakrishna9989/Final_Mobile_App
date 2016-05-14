package com.shiva.firebaselogin;

public class Request {
    String name;
    String provider;
    String user;
    String completed;

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    String payment;
    String createdAt;

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "{service_requests{'payment='"+payment+"','name='"+name+"', 'user'="+user+"','provider'="+provider+"' , 'createdAt'=" +createdAt+"' , 'completed'="+completed+"'}}";
    }
}
