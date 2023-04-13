package com.nyuen.camunda.controller;

import com.nyuen.camunda.common.SampleStorageStateEnums;
import com.nyuen.camunda.common.SampleTypeEnums;
import com.nyuen.camunda.domain.po.LabFridgeLevel;
import com.nyuen.camunda.domain.po.SampleStorage;
import com.nyuen.camunda.domain.po.SampleStorageOperation;
import com.nyuen.camunda.domain.vo.ImportSampleStorageVo;
import com.nyuen.camunda.domain.vo.SampleStorageBean;
import com.nyuen.camunda.domain.vo.SampleStorageVo;
import com.nyuen.camunda.domain.vo.SampleStoreOperateVo;
import com.nyuen.camunda.mapper.LabFridgeLevelMapper;
import com.nyuen.camunda.mapper.LabFridgeMapper;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.domain.po.LabFridge;
import com.nyuen.camunda.service.*;
import com.nyuen.camunda.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 样本回收控制类
 *
 * @author chengjl
 * @description 样本回收存储控制类
 * @date 2023/2/20
 */
@Api(tags = "样本回收存储控制类")
@RestController
@RequestMapping("/sampleStorage")
public class SampleStorageController {
    @Resource
    private SampleStorageService sampleStorageService;
    @Resource
    private LabFridgeService labFridgeService;
    @Resource
    private LabFridgeLevelService labFridgeLevelService;
    @Resource
    private SampleStorageOperationService sampleStorageOperationService;
    @Resource
    private SampleTypeSavePeriodService sampleTypeSavePeriodService;
    @Resource
    private LabFridgeMapper labFridgeMapper;
    @Resource
    private LabFridgeLevelMapper labFridgeLevelMapper;

    // 启用新的冰箱或选择已有冰箱
    @ApiOperation(value = "启用新的冰箱或选择已有冰箱(flag:true 启用新冰箱，false 选择已有冰箱)", httpMethod = "GET")
    @GetMapping("/chooseFridge")
    public Result chooseFridge(@RequestParam("flag") boolean flag,@RequestParam("fridgeNo") String fridgeNo, HttpServletRequest request){
        String loginUserIdStr = request.getHeader("loginUserId");
        String loginUserName = null==request.getHeader("loginUserName")?null: URLDecoder.decode(request.getHeader("loginUserName"));
        Long loginUserId;
        try {
            loginUserId = Long.parseLong(loginUserIdStr);
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
        List<LabFridge> labFridges = labFridgeService.getLabFridgeByNo(fridgeNo);
        //flag:true 启用新冰箱，false 选择已有冰箱
        if(!flag){
            //选择已有冰箱
            if(labFridges == null || labFridges.size() == 0){
                return ResultFactory.buildFailResult("该冰箱不存在，请核实冰箱编码！");
            }
        }else{
            if(labFridges != null && labFridges.size() >= 1){
               return ResultFactory.buildFailResult("冰箱编码不可重复！");
            }
            //启用新冰箱
            LabFridge labFridge = new LabFridge();
            labFridge.setFridgeNo(fridgeNo);
            labFridge.setFridgeState(1);//启用状态
            labFridge.setCreateUserId(loginUserId);
            labFridge.setCreateUserName(loginUserName);
            labFridge.setCreateTime(new Date());
            //添加新的冰箱
            labFridgeService.addLabFridge(labFridge);
        }
        return ResultFactory.buildSuccessResult(null);
    }

    // 启用新的层级或选择已有层级
    // 选择冰箱层级和样本类型
    @ApiOperation(value = "启用新的冰箱层级或选择已有冰箱层级(flag:true 启用新层级，false 选择已有层级)", httpMethod = "GET")
    @GetMapping("/chooseFridgeLevel")
    public Result chooseFridgeLevel(@RequestParam("flag") boolean flag,@RequestParam("fridgeNo") String fridgeNo,
                                    @RequestParam("levelNo") int levelNo,@RequestParam(value = "sampleType",required = false) String sampleType,
                                    HttpServletRequest request){
        //区分是已有层级(需提示层级样本类型)，还是新的层级(新层级样本类型必选)
        LabFridgeLevel lfl = new LabFridgeLevel();
        lfl.setFridgeNo(fridgeNo);
        lfl.setLevelNo(levelNo);
        List<LabFridgeLevel> labFridgeLevels = labFridgeLevelService.getInfoByFridgeNoAndLevel(lfl);
        if(!flag) { // 已有层级
            if (labFridgeLevels == null || labFridgeLevels.size() == 0) {
                return ResultFactory.buildFailResult("该层级未开启，请核实编码！");
            }
//            if(labFridgeLevels.size() > 1){
//                return ResultFactory.buildFailResult("层级数据重复！");
//            }
            sampleType = labFridgeLevels.get(0).getSampleType();//获取现有层级样本类型
            return ResultFactory.buildResult(200, "已有层级", sampleType);
        }else { // 新的层级
            if (labFridgeLevels != null && labFridgeLevels.size() > 0) {
                return ResultFactory.buildFailResult("该层级已开启，请核实编码！");
            }
            if(StringUtil.isEmpty(sampleType)){
                return ResultFactory.buildFailResult("选择新层级时，样本类型不能为空！");
            }
            String loginUserIdStr = request.getHeader("loginUserId");
            String loginUserName = null==request.getHeader("loginUserName")?null: URLDecoder.decode(request.getHeader("loginUserName"));
            Long loginUserId;
            try {
                loginUserId = Long.parseLong(loginUserIdStr);
            } catch (Exception e) {
                return ResultFactory.buildFailResult(e.getMessage());
            }
            lfl.setSampleType(sampleType);
            lfl.setCreateUserId(loginUserId);
            lfl.setCreateUserName(loginUserName);
            lfl.setCreateTime(new Date());
            labFridgeLevelService.addLabFridgeLevel(lfl);
            return ResultFactory.buildResult(200, "新的层级", sampleType);
        }
    }

    //启动盒子数量，自动生成保存位置：（冰箱+层级+盒子类型、数量 --> 孔位）
    // fMax：手动设置干血片最大数量，默认为50
    // 空闲库位不足时提示
    // 样本回收存储
    // 样本入库(根据样本类型设置保存周期 )
    @ApiOperation(value = "样本回收存储(需设置干血片最大数量，默认为50)", httpMethod = "POST")
    @PostMapping("/sampleStore")
    public Result sampleStore(@RequestBody SampleStorageVo sampleStorageVo, HttpServletRequest request){
        // 1、启用冰箱:填写新的冰箱编号或选择已有的冰箱
        // 2、启用层数:启用冰箱中新的层级或选择已启用的层级，若启用新的层级需选择保存样本和盒子类型，若选择已启动的层级需要提示目前层级保存的样本类型
        // 3、选择样本类型+启用盒子数量（外周血：7*7 A1-G7，口腔拭子、DNA：9*9 A1-J9，除字母I）
        // 4、选择位置
        // 5、入库
        if(null == sampleStorageVo || sampleStorageVo.getSampleNumList() == null || sampleStorageVo.getSampleNumList().size() == 0){
            return ResultFactory.buildFailResult("请选择要储存的样本编号！");
        }
        if(sampleStorageVo.getBoxNum() < 1){
            return ResultFactory.buildFailResult("盒子数量至少为1！");
        }
        if(!SampleTypeEnums.contains(sampleStorageVo.getSampleType())){
            return ResultFactory.buildFailResult("请选择正确的样本类型！");
        }
        // B外周血 7*7 A1-G7
        // S口腔拭子，DNA 9*9 A1-J9
        // F干血片 A1-6-F01-F01
        // A1-6-B01-G7
        // 1、获取最后一个样本的位置编号
        //      *冰箱编号和层级数必选：根据冰箱编号和层级数查询最后一个样本的位置编号
        //      String lastSampleLocation = "A1-6-B01-G7";
        SampleStorage ss = new SampleStorage();
        ss.setFridgeNo(sampleStorageVo.getFridgeNo());
        ss.setLevelNo(sampleStorageVo.getLevelNo());
        SampleStorage sampleStorage = sampleStorageService.getLastSampleLocation(ss);
        StringBuilder lastSampleLocation = null;

        if(null != sampleStorage && StringUtil.isEmpty(sampleStorage.getSampleLocation())){
            return ResultFactory.buildFailResult("查找最新样本存储位置出错，请联系管理员！");
        }
        if(null != sampleStorage && StringUtil.isNotEmpty(sampleStorage.getSampleLocation())){
            lastSampleLocation = new StringBuilder(sampleStorage.getSampleLocation());
        }
        // 2、判断空闲库位是否充足：样本数量 <--> 盒子数量，当前盒子剩余数量+（盒子数-1）*49 or 81
        int sampleCount = sampleStorageVo.getSampleNumList().size();
        int perBoxLocationCount = SampleTypeEnums.B.toString().equals(sampleStorageVo.getSampleType()) ? 49 : (SampleTypeEnums.F.toString().equals(sampleStorageVo.getSampleType()) ? sampleStorageVo.getFMax() : 81);
        int currentBoxRestLocationCount = getCurrentBoxRestLocationCount(sampleStorageVo.getSampleType(),lastSampleLocation,perBoxLocationCount);
        if(-1 == currentBoxRestLocationCount){
            return ResultFactory.buildFailResult("计算剩余库位编号出错，样本位置编号错误！");
        }
        int freeBoxCount = perBoxLocationCount*(sampleStorageVo.getBoxNum()-1)+currentBoxRestLocationCount;
        if(freeBoxCount < sampleCount){
            return ResultFactory.buildFailResult("空闲库位不足！所选样本数量"+sampleCount+"，空闲库位数量"+freeBoxCount);
        }
        String loginUserIdStr = request.getHeader("loginUserId");
        String loginUserName = null==request.getHeader("loginUserName")?null: URLDecoder.decode(request.getHeader("loginUserName"));
        Long loginUserId;
        try {
            loginUserId = Long.parseLong(loginUserIdStr);
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
        // 3、getNextLocation() && sample store
        // 干血片类型样本：A1-2-F01-01无孔位概念，perColumnCount等于fMax
        int perColumnCount =SampleTypeEnums.F.toString().equals(sampleStorageVo.getSampleType()) ? sampleStorageVo.getFMax ():(int)Math.sqrt(perBoxLocationCount);
        // 根据样本类型获取样本保存周期（单位：天）
        int savePeriod = sampleTypeSavePeriodService.getPeriodBySampleType(sampleStorageVo.getSampleType());
        Date today = new Date();
        // 计算样本过期时间
        Date overdueTime = savePeriod !=0 ? DateUtil.getOneDayAfterToday(today, savePeriod) : null;
        // 校验样本编号是否存在
        Set<String> sampleNumSet = new HashSet<>(sampleStorageVo.getSampleNumList());
        if(sampleNumSet.size() != sampleStorageVo.getSampleNumList().size()){
            return ResultFactory.buildFailResult("输入的样本编号存在重复，请核对！");
        }
        ImportSampleStorageVo issVo = new ImportSampleStorageVo();
        issVo.setSampleNumSet(sampleNumSet);
        issVo.setSampleLocationSet(null);
        List<SampleStorage> sampleStorageList = sampleStorageService.getBySampleNumOrLocation(issVo);
        if(null != sampleStorageList && sampleStorageList.size() >0 ){
            Set<String> sampleNumUsedSet = new HashSet<>();
            sampleStorageList.stream().map(m->{
                sampleNumUsedSet.add(m.getSampleNum());
                return m;
            }).collect(Collectors.toList());
            return ResultFactory.buildFailResult("样本编号"+sampleNumUsedSet.toString()+"已存在，请核对！");
        }
        for(String sampleNum : sampleStorageVo.getSampleNumList()){
            String nextLocation = getNextFreeLocation(sampleStorageVo.getFridgeNo(),sampleStorageVo.getLevelNo(),
                    sampleStorageVo.getSampleType(),lastSampleLocation,perColumnCount);
            if(null == nextLocation){
                return ResultFactory.buildFailResult("计算下一个库位编号出错，请联系管理员！");
            }
            lastSampleLocation = new StringBuilder(nextLocation);
            SampleStorage ss1 = new SampleStorage();
            ss1.setSampleNum(sampleNum);
            ss1.setFridgeNo(sampleStorageVo.getFridgeNo());
            ss1.setLevelNo(sampleStorageVo.getLevelNo());
            ss1.setBoxNo(nextLocation.split("-")[2]);
            ss1.setHoleLocation(nextLocation.split("-")[3]);
            ss1.setSampleLocation(nextLocation);
            ss1.setSampleType(sampleStorageVo.getSampleType());
            ss1.setSampleTypeName(SampleTypeEnums.getDescByCode(sampleStorageVo.getSampleType()));
            ss1.setOverdueTime(overdueTime);
            ss1.setSampleStorageState(318);
            ss1.setCreateUserId(loginUserId);
            ss1.setCreateUserName(loginUserName);
            ss1.setCreateTime(today);
            sampleStorageService.addSampleStorage(ss1);
            //添加入库记录
            SampleStorageOperation sampleStorageOperation = new SampleStorageOperation();
            sampleStorageOperation.setSampleNum(sampleNum);
            sampleStorageOperation.setOperateType(SampleStorageStateEnums.getNameByCode(318));
            sampleStorageOperation.setOperation(SampleStorageStateEnums.getNameByCode(318));
            Result result = addSampleStoreOperation(sampleStorageOperation, request);
            if (200 != result.getCode()) {
                return result;
            }
        }
        return ResultFactory.buildResult(200,"样本存放冰箱完成",null);
    }

    // 获取当前盒子剩余位置数量
    private static int getCurrentBoxRestLocationCount(String sampleType,StringBuilder holeLocation, int perBoxLocationCount){
        // A1-6-F01-01
        // A1-6-B01-G7
        if(null == holeLocation){ //新的冰箱层级
            if(SampleTypeEnums.F.toString().equals(sampleType)){
                return perBoxLocationCount;
            }else{
                return perBoxLocationCount*perBoxLocationCount;
            }
        }
        String[] strArray = holeLocation.toString().split("-");
        int perColumnCount = (int)Math.sqrt(perBoxLocationCount);
        String curLocationLetter = strArray[3].substring(0,1);
        int curLocationNumber = Integer.parseInt(strArray[3].substring(1));
        //干血片类型样本：A1-2-F01-01无孔位概念，perColumnCount等于fMax
        if(SampleTypeEnums.F.toString().equals(sampleType)){
            if(curLocationNumber > perBoxLocationCount){
                return -1;
            }else{
                return perBoxLocationCount-curLocationNumber;
            }
        }
        if(-1 == getLetterIndex(curLocationLetter)){
            return -1;
        }
        return (perColumnCount-curLocationNumber)+(perColumnCount-getLetterIndex(curLocationLetter)-1)*perColumnCount;
    }

    //获取下一个样本位置编号
    private String getNextLocation(String fridgeNo, int levelNo,String sampleType,StringBuilder sampleLocation, int perColumnCount){
        // A1-2-B05-B6 A1-2-B05-F7 A1-2-B05-G7
        // 1、新的冰箱层级
        if(null == sampleLocation){
            String newFridgeNextLocation = fridgeNo+ "-" + levelNo + "-"+sampleType+"01";
            if(SampleTypeEnums.F.toString().equals(sampleType)){
                newFridgeNextLocation = newFridgeNextLocation+"-01";
            }else {
                newFridgeNextLocation = newFridgeNextLocation+"-A1";
            }
            return newFridgeNextLocation;
        }
        // 1、已有冰箱层级
        String[] strArray = sampleLocation.toString().split("-");
        String curLocationLetter = strArray[3].substring(0,1);
        int curLocationNumber = Integer.parseInt(strArray[3].substring(1));
        //干血片类型样本：A1-2-F01-01无孔位概念，perColumnCount等于fMax TODO
        if(SampleTypeEnums.F.toString().equals(sampleType)){
            curLocationNumber = Integer.parseInt(strArray[3]);
            int nextLocationNumber = curLocationNumber + 1;
            //情形一：当前盒子未满
            if(curLocationNumber < perColumnCount) {
                if (nextLocationNumber <= 9) {
                    return sampleLocation.substring(0, sampleLocation.length() - 1) + nextLocationNumber;
                } else {
                    return sampleLocation.substring(0, sampleLocation.length() - 2) + nextLocationNumber;
                }
            }
            //情形二：当前盒子已满
            else if(curLocationNumber == perColumnCount){
                String curBoxNo = strArray[2];
                int curBoxCount = 0;
                if("0".equals(curBoxNo.substring(1,2))){
                    curBoxCount =Integer.parseInt(curBoxNo.substring(2));
                }else {
                    curBoxCount =Integer.parseInt(curBoxNo.substring(1));
                }
                if(curBoxCount<9) {
                    return strArray[0]+"-" + strArray[1]+"-" + curBoxNo.substring(0,1)+"0"+(curBoxCount+1)+"-"+"01";
                }else{
                    return strArray[0]+"-" + strArray[1]+"-" + curBoxNo.substring(0,1)+(curBoxCount+1)+"-"+"01";
                }
            }else {
                return null;
            }
        }
        //其他类型样本
        //情形一：当前行未满
        if(curLocationNumber < perColumnCount){
            int nextLocationNumber = curLocationNumber+1;
            return sampleLocation.substring(0,sampleLocation.length()-1)+nextLocationNumber;
        }
        //情形二：当前行已满，放下一行
        if(curLocationNumber == perColumnCount && getLetterIndex(curLocationLetter)+1 < perColumnCount){
            if(null == getNextLetter(curLocationLetter,perColumnCount)){
                return null;
            }
            return sampleLocation.substring(0,sampleLocation.length()-2)+getNextLetter(curLocationLetter,perColumnCount)+"1";
        }
        //情形三：当前盒子已满，放下一个盒子
        if(curLocationNumber == perColumnCount && getLetterIndex(curLocationLetter)+1 == perColumnCount){
            String curBoxNo = strArray[2];
            int curBoxCount = 0;
            if("0".equals(curBoxNo.substring(1,2))){
                curBoxCount =Integer.parseInt(curBoxNo.substring(2));
            }else {
                curBoxCount =Integer.parseInt(curBoxNo.substring(1));
            }
            if(curBoxCount<9) {
                return strArray[0]+"-" + strArray[1]+"-" + curBoxNo.substring(0,1)+"0"+(curBoxCount+1)+"-"+"A1";
            }else{
                return strArray[0]+"-" + strArray[1]+"-" + curBoxNo.substring(0,1)+(curBoxCount+1)+"-"+"A1";
            }
        }
        return null;
    }

    // 获取下一个空闲样本位置编号
    private String getNextFreeLocation(String fridgeNo, int levelNo,String sampleType,StringBuilder sampleLocation, int perColumnCount){
        // A1-2-B05-B6 A1-2-B05-F7 A1-2-B05-G7
        // 1、新的冰箱层级
        if(null == sampleLocation){
            String newFridgeNextLocation = fridgeNo+ "-" + levelNo + "-"+sampleType+"01";
            if(SampleTypeEnums.F.toString().equals(sampleType)){
                newFridgeNextLocation = newFridgeNextLocation+"-01";
            }else {
                newFridgeNextLocation = newFridgeNextLocation+"-A1";
            }
            if(isLocationFree(newFridgeNextLocation)) {
                return newFridgeNextLocation;
            }else {
                return getNextFreeLocation(fridgeNo,levelNo,sampleType,new StringBuilder(newFridgeNextLocation),perColumnCount);
            }
        }
        // 1、已有冰箱层级
        String[] strArray = sampleLocation.toString().split("-");
        String curLocationLetter = strArray[3].substring(0,1);
        int curLocationNumber = Integer.parseInt(strArray[3].substring(1));
        //干血片类型样本：A1-2-F01-1，无孔位概念，perColumnCount等于fMax TODO
        // 最后一个位置是纯数字,不能是A1-2-F01-01
        if(SampleTypeEnums.F.toString().equals(sampleType)){
            if(!NumberUtil.isWholeNumber(strArray[3])){
                return null;
            }
            curLocationNumber = Integer.parseInt(strArray[3]);
            int nextLocationNumber = curLocationNumber + 1;
            //情形一：当前盒子未满
            if(curLocationNumber < perColumnCount) {
//                if (nextLocationNumber <= 9) {
//                    String location11 = sampleLocation.substring(0, sampleLocation.lastIndexOf("-")) + nextLocationNumber;
//                    if(isLocationFree(location11)) {
//                        return location11;
//                    }else{
//                        return getNextFreeLocation(fridgeNo,levelNo,sampleType,new StringBuilder(location11),perColumnCount);
//                    }
//                } else {
                    String location12 = sampleLocation.substring(0, sampleLocation.lastIndexOf("-")) + nextLocationNumber;
                    if(isLocationFree(location12)) {
                        return location12;
                    }else{
                        return getNextFreeLocation(fridgeNo,levelNo,sampleType,new StringBuilder(location12),perColumnCount);
                    }
//                }
            }
            //情形二：当前盒子已满 A1-2-F01-50
            else if(curLocationNumber == perColumnCount){
                String curBoxNo = strArray[2];
                int curBoxCount = 0;
                if("0".equals(curBoxNo.substring(1,2))){
                    curBoxCount =Integer.parseInt(curBoxNo.substring(2));
                }else {
                    curBoxCount =Integer.parseInt(curBoxNo.substring(1));
                }
                if(curBoxCount<9) {
                    String location21 = strArray[0]+"-" + strArray[1]+"-" + curBoxNo.substring(0,1)+"0"+(curBoxCount+1)+"-"+"1";
                    if(isLocationFree(location21)) {
                        return location21;
                    }else{
                        return getNextFreeLocation(fridgeNo,levelNo,sampleType,new StringBuilder(location21),perColumnCount);
                    }
                }else{
                    String location22 = strArray[0]+"-" + strArray[1]+"-" + curBoxNo.substring(0,1)+(curBoxCount+1)+"-"+"1";
                    if(isLocationFree(location22)) {
                        return location22;
                    }else{
                        return getNextFreeLocation(fridgeNo,levelNo,sampleType,new StringBuilder(location22),perColumnCount);
                    }
                }
            }else {
                return null;
            }
        }
        //其他类型样本
        //情形一：当前行未满
        if(curLocationNumber < perColumnCount){
            int nextLocationNumber = curLocationNumber+1;
            String otherLocation11 = sampleLocation.substring(0,sampleLocation.length()-1)+nextLocationNumber;
            if(isLocationFree(otherLocation11)) {
                return otherLocation11;
            }else{
                return getNextFreeLocation(fridgeNo,levelNo,sampleType,new StringBuilder(otherLocation11),perColumnCount);
            }
        }
        //情形二：当前行已满，放下一行
        if(curLocationNumber == perColumnCount && getLetterIndex(curLocationLetter)+1 < perColumnCount){
            if(null == getNextLetter(curLocationLetter,perColumnCount)){
                return null;
            }
            String otherLocation21 = sampleLocation.substring(0,sampleLocation.length()-2)+getNextLetter(curLocationLetter,perColumnCount)+"1";
            if(isLocationFree(otherLocation21)) {
                return otherLocation21;
            }else{
                return getNextFreeLocation(fridgeNo,levelNo,sampleType,new StringBuilder(otherLocation21),perColumnCount);
            }
        }
        //情形三：当前盒子已满，放下一个盒子
        if(curLocationNumber == perColumnCount && getLetterIndex(curLocationLetter)+1 == perColumnCount){
            String curBoxNo = strArray[2];
            int curBoxCount = 0;
            if("0".equals(curBoxNo.substring(1,2))){
                curBoxCount =Integer.parseInt(curBoxNo.substring(2));
            }else {
                curBoxCount =Integer.parseInt(curBoxNo.substring(1));
            }
            if(curBoxCount<9) {
                String otherLocation31 = strArray[0]+"-" + strArray[1]+"-" + curBoxNo.substring(0,1)+"0"+(curBoxCount+1)+"-"+"A1";
                if(isLocationFree(otherLocation31)){
                    return otherLocation31;
                }else {
                    return getNextFreeLocation(fridgeNo,levelNo,sampleType,new StringBuilder(otherLocation31),perColumnCount);
                }
            }else{
                String otherLocation32 = strArray[0]+"-" + strArray[1]+"-" + curBoxNo.substring(0,1)+(curBoxCount+1)+"-"+"A1";
                if(isLocationFree(otherLocation32)) {
                    return otherLocation32;
                }else {
                    return getNextFreeLocation(fridgeNo,levelNo,sampleType,new StringBuilder(otherLocation32),perColumnCount);
                }
            }
        }
        return null;
    }

    // 判断样本位置是否被占用
    private boolean isLocationFree(String sampleLocation){
        return null == sampleStorageService.getInfoByLocation(sampleLocation) ||
                sampleStorageService.getInfoByLocation(sampleLocation).size() == 0;
    }

    //样本领出、归还、返样、销毁、作废、移动
    @ApiOperation(value = "样本存储操作(入库、领出、归还、返样、销毁、作废、移动)", httpMethod = "POST")
    @PostMapping("/sampleStoreOperate")
    public Result sampleStoreOperate(@RequestBody SampleStoreOperateVo sampleStoreOperateVo, HttpServletRequest request){
        if(null == sampleStoreOperateVo || null == sampleStoreOperateVo.getSampleStorageState()){
            return ResultFactory.buildFailResult("请选择样本操作类型！");
        }
        sampleStorageService.sampleStoreOperate(sampleStoreOperateVo);
        for(String sampleNum : sampleStoreOperateVo.getSampleNumList()) {
            SampleStorageOperation sampleStorageOperation = new SampleStorageOperation();
            sampleStorageOperation.setSampleNum(sampleNum);
            sampleStorageOperation.setOperateType(SampleStorageStateEnums.getNameByCode(sampleStoreOperateVo.getSampleStorageState()));
            sampleStorageOperation.setOperation(SampleStorageStateEnums.getNameByCode(sampleStoreOperateVo.getSampleStorageState()));
            Result result = addSampleStoreOperation(sampleStorageOperation, request);
            if (200 != result.getCode()) {
                return result;
            }
        }
        return ResultFactory.buildSuccessResult(null);
    }

    //添加样本存储出入库等操作记录
    private Result addSampleStoreOperation(SampleStorageOperation sampleStorageOperation, HttpServletRequest request){
        String loginUserIdStr = request.getHeader("loginUserId");
        String loginUserName = null==request.getHeader("loginUserName")?null: URLDecoder.decode(request.getHeader("loginUserName"));
        Long loginUserId;
        try {
            loginUserId = Long.parseLong(loginUserIdStr);
        }catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        sampleStorageOperation.setOperateUserId(loginUserId);
        sampleStorageOperation.setOperateUserName(loginUserName);
        sampleStorageOperation.setOperateTime(new Date());
        sampleStorageOperationService.addSampleStorageOperation(sampleStorageOperation);
        return ResultFactory.buildSuccessResult(null);
    }

    @ApiOperation(value = "获取样本存储信息列表", httpMethod = "POST")
    @PostMapping("/getSampleStorageList")
    public Result getSampleStorageList(@RequestBody SampleStorageBean sampleStorageBean) throws IllegalAccessException {
        Map<String,Object> map = ObjectUtil.objectToMap(sampleStorageBean);
        PageConvert.currentPageConvertStartIndex(map);
        return ResultFactory.buildSuccessResult(sampleStorageService.getSampleStorageList(map));
    }

    @ApiOperation(value = "根据样本编号获取样本操作记录", httpMethod = "GET")
    @GetMapping("/getSampleStorageOperation")
    public Result getSampleStorageOperation(@RequestParam(value = "sampleNum") String sampleNum){

        return ResultFactory.buildSuccessResult(sampleStorageOperationService.getSampleStorageOperation(sampleNum));
    }

    @ApiOperation(value = "导出样本库信息Excel", httpMethod = "POST")
    @PostMapping("/exportSampleStorageList")
    public void exportSampleStorageList(@RequestBody SampleStorageBean sampleStorageBean, HttpServletResponse response) throws Exception {
        Map<String,Object> map = ObjectUtil.objectToMap(sampleStorageBean);
        PageConvert.currentPageConvertStartIndex(map);
        List<SampleStorage> sampleStorageList = sampleStorageService.getSampleStorageListWithoutPage(map);
        // 构建导出excel表头（第一行）
        String[] excelHeader = {"样本编号", "样本位置", "样本类型", "样本状态", "创建人", "创建时间", "是否过期"};
        ExcelUtil.exportSampleStorageExcel(response,excelHeader,sampleStorageList,"样本库位信息"+DateUtil.DateToString(new Date(),"yyyyMMdd"),"样本库位信息");
    }

    private static int getLetterIndex(String letter){
        String[] letterArray = {"A","B","C","D","E","F","G","H","J"};
        for(int i=0; i<letterArray.length; i++){
            if(letterArray[i].equals(letter)){
                return i;
            }
        }
        return -1;
    }

    private static String getNextLetter(String letter,int perColumnCount){
        String[] letterArray = {"A","B","C","D","E","F","G","H","J"};
        if(perColumnCount > letterArray.length){
            return null;
        }
        if(getLetterIndex(letter)+1 == perColumnCount){
            return "A";
        }
        for(int i=0; i < perColumnCount-1; i++){
            if(letterArray[i].equals(letter)){
                return letterArray[i+1];
            }
        }
        return null;
    }

    //批量导入样本库，主要包括：样本编号，库位编号，样本类型信息
    @ApiOperation(value = "批量导入样本库信息Excel", httpMethod = "POST")
    @PostMapping("/importSampleStorageList")
    public Result importSampleStorageList(MultipartFile multipartFile, HttpServletRequest request) throws Exception {
        // 上传样本库位信息Excel表格
        Result excelResult = ExcelUtil.dealSampleStorageDataByExcel(multipartFile);
        if(200 != excelResult.getCode()){
            return excelResult;
        }
        // 得到Excel表格库位信息数据
        List<ImportSampleStorageVo> excelIssVoList = (List<ImportSampleStorageVo>) excelResult.getData();
        // 1、校验是否有重复的样本编号、库位信息
        int excelSize = excelIssVoList.size();
        Set<String> sampleNumSet = new HashSet<>();
        Set<String> sampleLocationSet = new HashSet<>();
        Set<String> fridgeNoSet = new HashSet<>();// 冰箱编号set
        Set<String> fridge_level_boxNoSet = new HashSet<>();// 冰箱层级编号_盒子编号set
        StringBuilder sampleNumRepeatErr = new StringBuilder();
        StringBuilder sampleLocationRepeatErr = new StringBuilder();
        for(ImportSampleStorageVo issVo : excelIssVoList){
            //校验库位编号是否合法
            if(!isSampleLocationValid(issVo.getSampleLocation(), issVo.getSampleType().charAt(0))){
                return ResultFactory.buildFailResult("库位编号"+issVo.getSampleLocation()+"不合法！");
            }
            if(sampleNumSet.contains(issVo.getSampleNum())){
                sampleNumRepeatErr.append(issVo.getSampleNum()).append(",");
            }
            if(sampleLocationSet.contains(issVo.getSampleLocation())){
                sampleLocationRepeatErr.append(issVo.getSampleLocation()).append(",");
            }
            String sampleLocation = issVo.getSampleLocation();
            String sampleTypeName = issVo.getSampleTypeName();
            String[] strs = sampleLocation.split("-");
            if(StringUtil.isEmpty(strs[2])){
                return ResultFactory.buildFailResult("样本编号"+issVo.getSampleNum()+"的库位编号"+sampleLocation+"不合法！");
            }
            String sampleType = strs[2].substring(0,1);
            // 4、校验库位和样本类型是否一致
            if(!SampleTypeEnums.contains(sampleType) || !SampleTypeEnums.getDescByCode(sampleType).equals(sampleTypeName.trim())){
                return ResultFactory.buildFailResult("样本编号"+issVo.getSampleNum()+"的库位编号"+sampleLocation+"与样本类型不匹配！");
            }
            fridgeNoSet.add(strs[0]);
            fridge_level_boxNoSet.add(sampleLocation.substring(0,sampleLocation.lastIndexOf("-")));
            sampleNumSet.add(issVo.getSampleNum());
            sampleLocationSet.add(issVo.getSampleLocation());
        }
        if(excelSize != sampleNumSet.size() || excelSize != sampleLocationSet.size()){
            return ResultFactory.buildFailResult((sampleNumRepeatErr.length() == 0?new StringBuilder():sampleNumRepeatErr.insert(0,"样本编号"))
                            .append((sampleLocationRepeatErr.length() == 0?"":sampleLocationRepeatErr.insert(0,"库位编号")))
                            .append("存在重复，请核对！").toString());
        }
        // 2、校验样本编号是否已存在
        // 3、校验库位是否已占用
        ImportSampleStorageVo issVo = new ImportSampleStorageVo();
        issVo.setSampleNumSet(sampleNumSet);
        issVo.setSampleLocationSet(sampleLocationSet);
        List<SampleStorage> sampleStorageList = sampleStorageService.getBySampleNumOrLocation(issVo);
        if(null != sampleStorageList && sampleStorageList.size() >0 ){
            Set<String> sampleNumUsedSet = new HashSet<>();
            Set<String> sampleLocationUsedSet = new HashSet<>();
            sampleStorageList.stream().map(m->{
                sampleNumUsedSet.add(m.getSampleNum());
                sampleLocationUsedSet.add(m.getSampleLocation());
                return m;
            }).collect(Collectors.toList());
            return ResultFactory.buildFailResult("样本编号"+sampleNumUsedSet.toString()+",库位编号"
                    +sampleLocationUsedSet.toString()+"已存在或已被占用，请核对！");
        }
        // 5、校验冰箱、层级、盒子是否已创建
        Set<String> allFridgeNoList = labFridgeMapper.getAllFridgeNo();
        if(null == allFridgeNoList || 0 == allFridgeNoList.size()){
            return ResultFactory.buildFailResult("未创建冰箱，请核对！");
        }
        if(!allFridgeNoList.containsAll(fridgeNoSet)){
            fridgeNoSet.removeAll(allFridgeNoList);
            return ResultFactory.buildFailResult(fridgeNoSet.toString()+"以上编号对应冰箱未创建，请核对！");
        }
        Set<String> allFridgeLevelBoxNoList = labFridgeLevelMapper.getAllFridgeLevelBoxNo();
        if(null == allFridgeLevelBoxNoList || allFridgeLevelBoxNoList.size() == 0 ){
            return ResultFactory.buildFailResult("未创建层级和盒子，请核对！");
        }
        if(!allFridgeLevelBoxNoList.containsAll(fridge_level_boxNoSet)){
            fridge_level_boxNoSet.removeAll(allFridgeLevelBoxNoList);
            return ResultFactory.buildFailResult(fridge_level_boxNoSet.toString()+"以上编号对应层级或盒子未创建，请核对！");
        }
        // 6、入库
        String loginUserIdStr = request.getHeader("loginUserId");
        String loginUserName = null==request.getHeader("loginUserName")?null: URLDecoder.decode(request.getHeader("loginUserName"));
        Long loginUserId;
        try {
            loginUserId = Long.parseLong(loginUserIdStr);
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
        for(ImportSampleStorageVo excelIssVo: excelIssVoList){
            SampleStorage ss = new SampleStorage();
            ss.setSampleNum(excelIssVo.getSampleNum());
            String[] locations = excelIssVo.getSampleLocation().split("-");
            ss.setFridgeNo(locations[0]);
            ss.setLevelNo(Integer.parseInt(locations[1]));
            ss.setBoxNo(locations[2]);
            ss.setHoleLocation(locations[3]);
            ss.setSampleLocation(excelIssVo.getSampleLocation());
            ss.setSampleType(locations[2].substring(0,1));
            ss.setSampleTypeName(excelIssVo.getSampleTypeName());
            ss.setCreateTime(new Date());
            ss.setCreateUserId(loginUserId);
            ss.setCreateUserName(loginUserName);
            // 根据样本类型获取样本保存周期（单位：天）
            int savePeriod = sampleTypeSavePeriodService.getPeriodBySampleType(ss.getSampleType());
            Date today = new Date();
            // 计算样本过期时间
            Date overdueTime = savePeriod !=0 ? DateUtil.getOneDayAfterToday(today, savePeriod) : null;
            ss.setOverdueTime(overdueTime);
            ss.setSampleStorageState(318);
            sampleStorageService.addSampleStorage(ss);
            //添加入库记录
            SampleStorageOperation sampleStorageOperation = new SampleStorageOperation();
            sampleStorageOperation.setSampleNum(excelIssVo.getSampleNum());
            sampleStorageOperation.setOperateType(SampleStorageStateEnums.getNameByCode(318));
            sampleStorageOperation.setOperation(SampleStorageStateEnums.getNameByCode(318));
            Result result = addSampleStoreOperation(sampleStorageOperation, request);
            if (200 != result.getCode()) {
                return result;
            }
        }
        return ResultFactory.buildResult(200,"样本存放冰箱完成",null);
    }

    //校验库位编号是否合法
    private boolean isSampleLocationValid(String sampleLocation, char sampleType){
        // B外周血： 7*7 , A1-G7
        // S口腔拭子，DNA： 9*9 , A1-J9(除去 I)
        // F干血片 A1-6-F01-1
        if(StringUtil.isEmpty(sampleLocation)){
            return false;
        }
        if(!SampleTypeEnums.contains(sampleType+"")){
            return false;
        }
        //A1-1-B01-F5
        String[] strs = sampleLocation.split("-");
        if(StringUtil.isEmpty(strs[2]) || strs[2].length() < 2){
            return false;
        }
        char locationSampleType = strs[2].charAt(0);
        char locationHoleCode = strs[3].charAt(0);
        //F干血片（A2-5-F01-01 与 A2-5-F01-1 被视为同一孔位，会被后者覆盖）
        //F干血片（A2-5-F01-08 与 A2-5-F01-8 被视为同一孔位，会被后者覆盖）
        if(sampleType == 70 &&  locationSampleType== 70){
           return NumberUtil.isWholeNumber(strs[3]);
        }
        if(strs[3].length() < 2 || strs[3].substring(1).startsWith("0")){
            return false;
        }
        int locationHoleNum = Integer.parseInt(strs[3].substring(1));
        //B外周血
        if(sampleType == 66 && locationSampleType == 66){
            return locationHoleCode >= 65 && locationHoleCode <= 71 && locationHoleNum >= 1 && locationHoleNum <= 9;
        }
        //D DNA,S口腔拭子
        if((sampleType == 68 && locationSampleType == 68) || (sampleType == 83 && locationSampleType == 83)){
            return locationHoleCode != 73 && locationHoleCode >= 65 && locationHoleCode <= 74 && locationHoleNum >= 1 && locationHoleNum <= 9;
        }
        return false;
    }

    public static void main(String[] args) {
        SampleStorageController ssc = new SampleStorageController();
        String lastSampleLocation = "A1-1-B01-F5";
        //System.out.println(lastSampleLocation.substring(lastSampleLocation.lastIndexOf("-")+1));

        String[] letterArray = {"A","B","C","D","E","F","G","H","J"};
        //System.out.println(getLetterIndex("W"));
        //System.out.println(getCurrentBoxRestLocationCount(lastSampleLocation,49));

//        System.out.println(ssc.getNextLocation("A1", 3,"F",new StringBuilder("A1-2-F09-06"),50));
//        System.out.println(ssc.getNextLocation("A1", 3,"F",null,50));
        System.out.println(ssc.isSampleLocationValid(lastSampleLocation, 'B'));
        System.out.println(ssc.isSampleLocationValid("A1-4-S01-B1", 'S'));
        System.out.println("A100".substring(1));
        System.out.println(lastSampleLocation.substring(0, lastSampleLocation.lastIndexOf("-")));

    }


}
