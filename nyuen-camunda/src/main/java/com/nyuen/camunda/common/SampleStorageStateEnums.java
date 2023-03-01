package com.nyuen.camunda.common;

public enum SampleStorageStateEnums {
    IN(318,"入库"),
    OUT(319,"领出"),
    BACK(320,"归还"),
    RETURN(321,"返样"),
    DESTROY(322,"销毁"),
    CANCEL(323,"作废"),
    MOVE(324,"移动");

    private int code;
    private String name;
    SampleStorageStateEnums(int code,String name){
        this.code = code;
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public int getCode(){
        return code;
    }

    public static String getNameByCode(int code){
        for(SampleStorageStateEnums se : SampleStorageStateEnums.values()){
            if(code == se.getCode()){
                return se.getName();
            }
        }
        return null;
    }


    public static void main(String[] args) {
        System.out.println(getNameByCode(318));

    }
}
