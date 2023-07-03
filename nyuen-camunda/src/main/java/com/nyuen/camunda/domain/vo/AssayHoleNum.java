package com.nyuen.camunda.domain.vo;

public enum AssayHoleNum {
    A(20),
    B(20),
    C(29),
    D(26),
    E(30),
    F(25),
    G(23),
    CNV(1);


    private int holeNum;

    AssayHoleNum(int holeNum) {
        this.holeNum = holeNum;
    }

    public int getHoleNum(){
        return this.holeNum;
    }

    public static void main(String[] args) {
        System.out.println(AssayHoleNum.E.getHoleNum());
        System.out.println(AssayHoleNum.G.holeNum);
    }
}
