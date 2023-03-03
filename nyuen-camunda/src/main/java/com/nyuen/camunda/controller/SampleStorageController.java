package com.nyuen.camunda.controller;

import com.alibaba.fastjson.JSON;
import com.nyuen.camunda.common.SampleStorageStateEnums;
import com.nyuen.camunda.common.SampleTypeEnums;
import com.nyuen.camunda.domain.po.LabFridgeLevel;
import com.nyuen.camunda.domain.po.SampleStorage;
import com.nyuen.camunda.domain.po.SampleStorageOperation;
import com.nyuen.camunda.domain.vo.SampleStorageBean;
import com.nyuen.camunda.domain.vo.SampleStorageVo;
import com.nyuen.camunda.domain.vo.SampleStoreOperateVo;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.domain.po.LabFridge;
import com.nyuen.camunda.service.LabFridgeLevelService;
import com.nyuen.camunda.service.LabFridgeService;
import com.nyuen.camunda.service.SampleStorageOperationService;
import com.nyuen.camunda.service.SampleStorageService;
import com.nyuen.camunda.utils.ObjectUtil;
import com.nyuen.camunda.utils.PageConvert;
import com.nyuen.camunda.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

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


    // 启用新的冰箱或选择已有冰箱
    @ApiOperation(value = "启用新的冰箱或选择已有冰箱", httpMethod = "GET")
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
    @ApiOperation(value = "启用新的冰箱层级或选择已有冰箱层级", httpMethod = "GET")
    @GetMapping("/chooseFridgeLevel")
    public Result chooseFridgeLevel(@RequestParam("flag") boolean flag,@RequestParam("fridgeNo") String fridgeNo,
                                    @RequestParam("levelNo") int levelNo,@RequestParam(value = "sampleType",required = false) String sampleType,
                                    HttpServletRequest request){
        //区分是已有层级(需提示层级样本类型)，还是新的层级(新层级样本类型必选)
        LabFridgeLevel lfl = new LabFridgeLevel();
        lfl.setFridgeNo(fridgeNo);
        lfl.setLevelNo(levelNo);
        List<LabFridgeLevel> labFridgeLevels = labFridgeLevelService.getSampleTypeByFridgeNoAndLevel(lfl);
        if(!flag) { // 已有层级
            if (labFridgeLevels == null || labFridgeLevels.size() == 0) {
                return ResultFactory.buildFailResult("该层级未开启，请核实编码！");
            }
            if(labFridgeLevels.size() > 1){
                return ResultFactory.buildFailResult("层级数据重复！");
            }
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
        StringBuilder lastSampleLocation = new StringBuilder(sampleStorage.getSampleLocation());

        // 2、判断空闲库位是否充足：样本数量 <--> 盒子数量，当前盒子剩余数量+（盒子数-1）*49 or 81
        int sampleCount = sampleStorageVo.getSampleNumList().size();
        int perBoxLocationCount = SampleTypeEnums.B.toString().equals(sampleStorageVo.getSampleType()) ? 49 : (SampleTypeEnums.F.toString().equals(sampleStorageVo.getSampleType()) ? sampleStorageVo.getFMax() : 81);
        int currentBoxRestLocationCount = getCurrentBoxRestLocationCount(sampleStorageVo.getSampleType(),lastSampleLocation.toString(),perBoxLocationCount);
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
        for(String sampleNum : sampleStorageVo.getSampleNumList()){
            // todo
            String nextLocation = getNextLocation(sampleStorageVo.getSampleType(),lastSampleLocation.toString(),perColumnCount);
            if(null == nextLocation){
                return ResultFactory.buildFailResult("计算下一个库位编号出错，请联系管理员！");
            }
            SampleStorage ss1 = new SampleStorage();
            ss1.setSampleNum(sampleNum);
            ss1.setFridgeNo(sampleStorageVo.getFridgeNo());
            ss1.setLevelNo(sampleStorageVo.getLevelNo());
            ss1.setBoxNo(nextLocation.split("-")[2]);// todo
            ss1.setHoleLocation(nextLocation);// todo
            ss1.setSampleType(sampleStorageVo.getSampleType());
            ss1.setSampleTypeName(SampleTypeEnums.getDescByCode(sampleStorageVo.getSampleType()));
            ss1.setCreateUserId(loginUserId);
            ss1.setCreateUserName(loginUserName);
            ss1.setCreateTime(new Date());
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
            lastSampleLocation = new StringBuilder(nextLocation);
        }
        return ResultFactory.buildResult(200,"样本存放冰箱完成",null);
    }

    //获取当前盒子剩余位置数量
    private static int getCurrentBoxRestLocationCount(String sampleType,String holeLocation, int perBoxLocationCount){
        // A1-6-F01-01
        // A1-6-B01-G7

        String[] strArray = holeLocation.split("-");
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

    //获取下一个样本位置编号(除干血片样本类型外)
    private static String getNextLocation(String sampleType,String holeLocation, int perColumnCount){
        // A1-2-B05-B6 A1-2-B05-F7 A1-2-B05-G7
        String[] strArray = holeLocation.split("-");
        String curLocationLetter = strArray[3].substring(0,1);
        int curLocationNumber = Integer.parseInt(strArray[3].substring(1));
        //干血片类型样本：A1-2-F01-01无孔位概念，perColumnCount等于fMax TODO
        if(SampleTypeEnums.F.toString().equals(sampleType)){
            curLocationNumber = Integer.parseInt(strArray[3]);
            int nextLocationNumber = curLocationNumber + 1;
            //情形一：当前盒子未满
            if(curLocationNumber < perColumnCount) {
                if (nextLocationNumber <= 9) {
                    return holeLocation.substring(0, holeLocation.length() - 1) + nextLocationNumber;
                } else {
                    return holeLocation.substring(0, holeLocation.length() - 2) + nextLocationNumber;
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
            return holeLocation.substring(0,holeLocation.length()-1)+nextLocationNumber;
        }
        //情形二：当前行已满，放下一行
        if(curLocationNumber == perColumnCount && getLetterIndex(curLocationLetter)+1 < perColumnCount){
            if(null == getNextLetter(curLocationLetter,perColumnCount)){
                return null;
            }
            return holeLocation.substring(0,holeLocation.length()-2)+getNextLetter(curLocationLetter,perColumnCount)+"1";
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

    public static void main(String[] args) {

        String lastSampleLocation = "A1-1-B01-F5";
        //System.out.println(lastSampleLocation.substring(lastSampleLocation.lastIndexOf("-")+1));


        String[] letterArray = {"A","B","C","D","E","F","G","H","J"};
        //System.out.println(getLetterIndex("W"));
        //System.out.println(getCurrentBoxRestLocationCount(lastSampleLocation,49));

        System.out.println(getNextLocation("F","A1-2-F09-06",50));
        //System.out.println(getNextLetter("W",9));
    }


}
