package com.tech.Education.Cognitive.App_Bean;

/**
 * Created by admin on 1/18/2018.
 */

public class QuizBean {

    int questionid;
    int question_quizid;
    String question_question;
    String question_ans;
    String question_option_a;
    String question_option_b;
    String question_option_c;
    String question_option_d;
    String question_image;

    public QuizBean() {
    }

    public QuizBean(int question_quizid, String question_question, String question_ans, String question_option_a, String question_option_b, String question_option_c, String question_option_d, String question_image) {
       // this.questionid = questionid;
        this.question_quizid = question_quizid;
        this.question_question = question_question;
        this.question_ans = question_ans;
        this.question_option_a = question_option_a;
        this.question_option_b = question_option_b;
        this.question_option_c = question_option_c;
        this.question_option_d = question_option_d;
        this.question_image = question_image;
    }

    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    public int getQuestion_quizid() {
        return question_quizid;
    }

    public void setQuestion_quizid(int question_quizid) {
        this.question_quizid = question_quizid;
    }

    public String getQuestion_question() {
        return question_question;
    }

    public void setQuestion_question(String question_question) {
        this.question_question = question_question;
    }

    public String getQuestion_option_a() {
        return question_option_a;
    }

    public void setQuestion_option_a(String question_option_a) {
        this.question_option_a = question_option_a;
    }

    public String getQuestion_option_b() {
        return question_option_b;
    }

    public void setQuestion_option_b(String question_option_b) {
        this.question_option_b = question_option_b;
    }

    public String getQuestion_option_c() {
        return question_option_c;
    }

    public void setQuestion_option_c(String question_option_c) {
        this.question_option_c = question_option_c;
    }

    public String getQuestion_option_d() {
        return question_option_d;
    }

    public void setQuestion_option_d(String question_option_d) {
        this.question_option_d = question_option_d;
    }

    public String getQuestion_ans() {
        return question_ans;
    }

    public void setQuestion_ans(String question_ans) {
        this.question_ans = question_ans;
    }

    public String getQuestion_image() {
        return question_image;
    }

    public void setQuestion_image(String question_image) {
        this.question_image = question_image;
    }
}
