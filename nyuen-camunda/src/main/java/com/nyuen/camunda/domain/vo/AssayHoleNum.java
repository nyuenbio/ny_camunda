package com.nyuen.camunda.domain.vo;

public enum AssayHoleNum {
    A(29),
    B(22),
    C(29),
    D(27),
    E(30),
    F(25),
    G(25),
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
