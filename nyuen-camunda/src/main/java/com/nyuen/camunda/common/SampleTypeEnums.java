package com.nyuen.camunda.common;

public enum SampleTypeEnums {
    B("外周血"),
    S("口腔拭子"),
    D("DNA"),
    F("干血片");

    private String description;
    SampleTypeEnums(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    public static String getDescByCode(String code){
        for(SampleTypeEnums ste : SampleTypeEnums.values()){
            if(code.equals(ste.toString())){
                return ste.getDescription();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(SampleTypeEnums.B.toString());

        System.out.println(getDescByCode("S"));
    }
}
