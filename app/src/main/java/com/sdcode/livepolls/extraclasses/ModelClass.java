package com.sdcode.livepolls.extraclasses;

public class ModelClass {
    private final String questionCurrent;
    private final String choice1;
    private final String choice2;
    private final String choice3;
    private final String choice4;
    private final String liveStatus;
    private final String publicStatus;
    private final int qid;


    public ModelClass(String questionCurrent, String choice1, String choice2, String choice3, String choice4, int qid, int liveStatus, int publicStatus) {
        this.questionCurrent = questionCurrent;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.qid = qid;
        this.liveStatus = String.valueOf(liveStatus);
        this.publicStatus = String.valueOf(publicStatus);
    }

    String getQuestionCurrent() {
        return questionCurrent;
    }

    String getOption1() {
        return choice1;
    }

    String getOption2() {
        return choice2;
    }

    String getOption3() {
        return choice3;
    }

    String getOption4() {
        return choice4;
    }

    public String getLiveStatus() {
        return liveStatus;
    }

    public String getPublicStatus() {
        return publicStatus;
    }

    public int getQid() {
        return qid;
    }
}
