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

    public static String getDescByCode(String str){
        for(SampleTypeEnums ste : SampleTypeEnums.values()){
            if(ste.toString().equals(str)){
                return ste.getDescription();
            }
        }
        return null;
    }

    public static boolean contains(String str){
        for(SampleTypeEnums ste : SampleTypeEnums.values()){
            if(ste.toString().equals(str)){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(SampleTypeEnums.B.toString());

        System.out.println(contains("W"));
    }
}
