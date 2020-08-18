package com.lagou.bean;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("lagou_resume_datas")
public class ResumeData {

    private String name;
    private  double  salary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "ResumeData{" +
                "name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }
}
