package com.flipkart.bean;

public class Payment {
	int courseId;
    String courseName;
    int fee;

    public Payment(int courseId, String courseName, int fee) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.fee = fee;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }
}
