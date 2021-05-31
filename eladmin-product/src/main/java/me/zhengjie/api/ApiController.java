package me.zhengjie.api;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.base.PageRequest;
import me.zhengjie.base.PageResult;
import me.zhengjie.domain.*;
import me.zhengjie.service.*;
import me.zhengjie.service.dto.*;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "报工api")
@RequestMapping("/api/produce")
@Slf4j
public class ApiController {

    private final AdmspeechheadService admspeechheadService;

    private final AdmspeechentryService admspeechentryService;

    private final AdminventoryService adminventoryService;

    private final SysUserService sysUserService;

    private final SysDeptService sysDeptService;

    private final SysLocalService sysLocalService;

    private static int httpStatus_OK = 200;

    private static int httpStatus_BAD_REQUEST = 400;

    private static int httpStatus_INTERNAL_SERVER_ERROR = 500;

    /**
     * 插入报工数据接口
     *
     * @param resources
     * @return
     */
    @PostMapping("/addReport")
    public Object addReport(@Validated @RequestBody Product resources) {
        if (resources.getProductList() != null) {
            try {
                ProductResult productResult = isSavedProduct(resources);

                AdmspeechheadDto admspeechhead = new AdmspeechheadDto();
                BeanUtils.copyProperties(resources, admspeechhead);
                admspeechhead.setFProduceDate(timeFormat(resources.getfProduceDate()));
                if (productResult.isSaveProduct()) {
                    //保存主表信息
                    admspeechhead.setFDate(new Timestamp(new Date().getTime()));
                    create(admspeechhead);
                } else {
                    admspeechhead.setFId(productResult.getAdmspeechheadList().get(0).getFId());
                    update(admspeechhead);
                    admspeechentryService.deleteAll(admspeechhead.getFId());
                }
                for (ProductInfo productInfo : resources.getProductList()) {
                    AdmspeechentryDto admspeechentry = new AdmspeechentryDto();
                    BeanUtils.copyProperties(productInfo, admspeechentry);
                    admspeechentry.setFHeadid(admspeechhead.getFId());
                    createInfo(admspeechentry);

                }

                return httpStatus_OK;

            } catch (Exception e) {

                log.error("create is catch", e);
                return httpStatus_INTERNAL_SERVER_ERROR;
            }

        } else {

            return httpStatus_BAD_REQUEST;
        }
    }

    /**
     * 获取地区电堆生产统计汇总
     *
     * @param criteria
     * @return
     * @throws ParseException
     */
    @GetMapping("/getProductByAddress")
    public ResponseEntity<Object> getProductByAddress(AdmspeechheadQueryCriteria criteria) throws ParseException {
        List<Map<String, Object>> resultList = getProductNum(criteria);
        return new ResponseEntity<>(PageUtil.toPage(resultList, resultList.size()), HttpStatus.OK);
    }

    /**
     * 下载地区电推生产数据汇总
     *
     * @param response
     * @param criteria
     * @throws ParseException
     * @throws IOException
     */
    @GetMapping("/getProductByAddress/download")
    public void downloadNum(HttpServletResponse response, AdmspeechheadQueryCriteria criteria) throws ParseException, IOException {
        List<Map<String, Object>> resultList = getProductNum(criteria);
        admspeechheadService.downloadProductNum(resultList, response);
    }

    /**
     * 用户获取填报工作列表
     *
     * @param pageable
     * @return
     */
    @GetMapping("/getProducByUser")
    public ResponseEntity<Object> getProductListByUser(String fProduceDate, PageRequest pageable) throws ParseException {
        AdmspeechheadQueryCriteria criteria = new AdmspeechheadQueryCriteria();
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        criteria.setFUseId(new JSONObject(new JSONObject(userDetails).get("user")).get("id", Integer.class));
        if (fProduceDate != null) {
            criteria.setFProduceDate(utc2Local(fProduceDate));
        }
        pageable.setPage(pageable.getPage() + 1);
        PageResult admspeechhead = admspeechheadService.findAdmspeechheadByPage(criteria, pageable);
        List<AdmspeechheadDto> admspeechheadList = admspeechhead.getContent();
        List<Product> list = new ArrayList<Product>();
        for (AdmspeechheadDto dto : admspeechheadList) {
            Product product = new Product();
            BeanUtils.copyProperties(dto, product);
            AdmspeechentryQueryCriteria queryCriteria = new AdmspeechentryQueryCriteria();
            queryCriteria.setFHeadid(dto.getFId());
            SysUserDto user = sysUserService.findById(product.getfUseId().longValue());
            product.setUserName(user.getUsername());
            SysDeptDto dept = sysDeptService.findById(user.getDeptId());
            product.setDeptName(dept.getName());
            List<AdmspeechentryDto> admspeechentry = admspeechentryService.queryAll(queryCriteria);
            List<ProductInfo> productInfoList = new ArrayList<>();
            for (AdmspeechentryDto speechentry : admspeechentry) {
                ProductInfo productInfo = new ProductInfo();
                BeanUtils.copyProperties(speechentry, productInfo);
                AdminventoryQueryCriteria query = new AdminventoryQueryCriteria();
                query.setFnumber(speechentry.getFNumber());
                List<AdminventoryDto> adminventoryDto = adminventoryService.queryAll(query);
                productInfo.setfName(adminventoryDto.get(0).getFname());
                productInfoList.add(productInfo);
            }
            product.setProductList(productInfoList);
            list.add(product);
        }
        admspeechhead.setContent(list);
        return new ResponseEntity<>(admspeechhead, HttpStatus.OK);
    }

    /**
     * 获取每日报工汇总
     *
     * @param criteria
     * @return
     * @throws ParseException
     */
    @GetMapping("/getProductList")
    public ResponseEntity<Object> getProduct(AdmspeechheadQueryCriteria criteria) throws ParseException {
        User user = getUser();
        criteria.setFUseId(user.getUserId());
        List<Product> list = getProductList(criteria);
        return new ResponseEntity<>(PageUtil.toPage(list, list.size()), HttpStatus.OK);
    }

    /**
     * 导出每日报工汇总
     *
     * @param response
     * @param criteria
     * @throws IOException
     * @throws ParseException
     */
    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/getProductList/download")
    public void download(HttpServletResponse response, AdmspeechheadQueryCriteria criteria) throws IOException, ParseException {
        List<Product> list = getProductList(criteria);
        admspeechheadService.downloadProduct(list, response);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("getUser")
    public User getUser() {
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        User userInfo = new User();
        userInfo.setUserId(new JSONObject(new JSONObject(userDetails).get("user")).get("id", Integer.class));
        userInfo.setUserName(userDetails.getUsername());
        JSONObject deptObject = (JSONObject) new JSONObject(new JSONObject(userDetails).get("user")).get("dept");
        userInfo.setDeptId(deptObject.get("id", Integer.class));
        userInfo.setDeptName(deptObject.get("name", String.class));
        userInfo.setUserAddress((String) new JSONObject(new JSONObject(userDetails).get("user")).get("userAddress"));
        return userInfo;
    }

    @GetMapping("/isSaveProduct")
    public ProductResult isSavedProduct(Product resources) throws ParseException {
        AdmspeechheadQueryCriteria criteria = new AdmspeechheadQueryCriteria();
        criteria.setFUseId(resources.getfUseId());
        criteria.setFProduceDate(timeFormat(resources.getfProduceDate()));
        List<AdmspeechheadDto> admspeechentry = admspeechheadService.queryByAdress(criteria);
        ProductResult productResult = new ProductResult();
        productResult.setSaveProduct(admspeechentry.isEmpty());
        productResult.setAdmspeechheadList(admspeechentry);
        return productResult;
    }

    @GetMapping("/getLocal")
    public ResponseEntity<Object> getLocal() {
        List<SysLocalDto> local = sysLocalService.getLocal();
        return new ResponseEntity<>(local, HttpStatus.OK);
    }

    //获取报工列表
    public List<Product> getProductList(AdmspeechheadQueryCriteria criteria) throws ParseException {
        String produceDate = utc2Local(criteria.getFProduceDate());
        criteria.setFProduceDate(produceDate);
        List<AdmspeechheadDto> admspeechhead = admspeechheadService.queryAll(criteria);
        List<Product> list = new ArrayList<>();
        for (AdmspeechheadDto dto : admspeechhead) {
            Product product = new Product();
            BeanUtils.copyProperties(dto, product);
            AdmspeechentryQueryCriteria queryCriteria = new AdmspeechentryQueryCriteria();
            queryCriteria.setFHeadid(dto.getFId());
            SysUserDto user = sysUserService.findById(product.getfUseId().longValue());
            product.setUserName(user.getUsername());
            SysDeptDto dept = sysDeptService.findById(user.getDeptId());
            product.setDeptName(dept.getName());
            product.setDeptId(dept.getDeptId());
            List<AdmspeechentryDto> admspeechentry = admspeechentryService.queryAll(queryCriteria);
            List<ProductInfo> productInfoList = new ArrayList<>();
            for (AdmspeechentryDto speechentry : admspeechentry) {
                ProductInfo productInfo = new ProductInfo();
                BeanUtils.copyProperties(speechentry, productInfo);
                AdminventoryQueryCriteria query = new AdminventoryQueryCriteria();
                query.setFnumber(speechentry.getFNumber());
                List<AdminventoryDto> adminventoryDto = adminventoryService.queryAll(query);
                productInfo.setfName(adminventoryDto.get(0).getFname());
                productInfoList.add(productInfo);
            }
            product.setProductList(productInfoList);
            list.add(product);
        }
        list.sort((x, y) -> Double.compare(x.getDeptId(), y.getDeptId()));
        return list;
    }

    //获取电堆生产数
    public List<Map<String, Object>> getProductNum(AdmspeechheadQueryCriteria criteria) throws ParseException {
        String produceDate = utc2Local(criteria.getFProduceDate());
        criteria.setFProduceDate(produceDate);
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<SysLocalDto> addressList = (List<SysLocalDto>) getLocal().getBody();

        Map<String, Object> product = new HashMap<>();
        product.put("address", "合计");
        product.put("fDate", criteria.getFProduceDate());

        int assemblyNumTotal260 = 0;
        int assemblyNumTotal340 = 0;
        int assemblyNumTotal370 = 0;

        int assemblyBalanceTotal260 = 0;
        int assemblyBalanceTotal340 = 0;
        int assemblyBalanceTotal370 = 0;

        int activationNumTotal260 = 0;
        int activationNumTotal340 = 0;
        int activationNumTotal370 = 0;

        int activationBalanceTotal260 = 0;
        int activationBalanceTotal340 = 0;
        int activationBalanceTotal370 = 0;

        List<ProductNum> productList = new ArrayList<>();
        ProductNum productNumTotal_260 = new ProductNum();
        ProductNum productNumTotal_340 = new ProductNum();
        ProductNum productNumTotal_370 = new ProductNum();
        ProductNum productNumTotal = new ProductNum();

        for (SysLocalDto address : addressList) {
            Map<String, Object> result = new HashMap<String, Object>();
            criteria.setFAddress(address.getLocalName());
            result.put("address", address.getLocalName());
            result.put("fDate", criteria.getFProduceDate());
            List<AdmspeechheadDto> admspeechheadList = admspeechheadService.queryByAdress(criteria);
            List<ProductNum> productInfoList = new ArrayList<>();
            ProductNum productNum_260 = new ProductNum();
            ProductNum productNum_340 = new ProductNum();
            ProductNum productNum_370 = new ProductNum();

            for (AdmspeechheadDto dto : admspeechheadList) {
                AdmspeechentryQueryCriteria queryCriteria = new AdmspeechentryQueryCriteria();
                queryCriteria.setFHeadid(dto.getFId());
                List<AdmspeechentryDto> admspeechentry = admspeechentryService.queryAll(queryCriteria);

                for (AdmspeechentryDto speechentry : admspeechentry) {
                    AdminventoryQueryCriteria query = new AdminventoryQueryCriteria();
                    query.setFnumber(speechentry.getFNumber());
                    List<AdminventoryDto> adminventoryDto = adminventoryService.queryAll(query);
                    String productName = adminventoryDto.get(0).getFname();

                    if (productName.contains("组装") && productName.contains("260")) {
                        productNum_260.setModelName("260节电堆");
                        productNum_260.setAssemblyNum(productNum_260.getAssemblyNum() + speechentry.getFOutputquantity().intValue());
                        productNum_260.setAssemblyBalance(productNum_260.getAssemblyBalance() + speechentry.getFBalancequantity().intValue());
                    } else if (productName.contains("组装") && productName.contains("340")) {
                        productNum_340.setModelName("340节电堆");
                        productNum_340.setAssemblyNum(productNum_340.getAssemblyNum() + speechentry.getFOutputquantity().intValue());
                        productNum_340.setAssemblyBalance(productNum_340.getAssemblyBalance() + speechentry.getFBalancequantity().intValue());

                    } else if (productName.contains("活化") && productName.contains("260")) {
                        productNum_260.setModelName("260节电堆");
                        productNum_260.setActivationNum(productNum_260.getActivationNum() + speechentry.getFOutputquantity().intValue());
                        productNum_260.setActivationBalance(productNum_260.getActivationBalance() + speechentry.getFBalancequantity().intValue());

                    } else if (productName.contains("活化") && productName.contains("340")) {
                        productNum_340.setModelName("340节电堆");
                        productNum_340.setActivationNum(productNum_340.getActivationNum() + speechentry.getFOutputquantity().intValue());
                        productNum_340.setActivationBalance(productNum_340.getActivationBalance() + speechentry.getFBalancequantity().intValue());
                    } else if (productName.contains("活化") && productName.contains("370")) {
                        productNum_370.setModelName("370节电堆");
                        productNum_370.setActivationNum(productNum_370.getActivationNum() + speechentry.getFOutputquantity().intValue());
                        productNum_370.setActivationBalance(productNum_370.getActivationBalance() + speechentry.getFBalancequantity().intValue());


                    } else if (productName.contains("组装") && productName.contains("370")) {
                        productNum_370.setModelName("370节电堆");
                        productNum_370.setAssemblyNum(productNum_370.getAssemblyNum() + speechentry.getFOutputquantity().intValue());
                        productNum_370.setAssemblyBalance(productNum_370.getAssemblyBalance() + speechentry.getFBalancequantity().intValue());
                    }
                }

            }


            productInfoList.add(productNum_260);
            productInfoList.add(productNum_340);
            productInfoList.add(productNum_370);
            result.put("productInfoList", productInfoList);
            resultList.add(result);

            assemblyNumTotal260 = assemblyNumTotal260 + productNum_260.getAssemblyNum();
            assemblyNumTotal340 = assemblyNumTotal340 + productNum_340.getAssemblyNum();
            assemblyNumTotal370 = assemblyNumTotal370 + productNum_370.getAssemblyNum();
            assemblyBalanceTotal260 = assemblyBalanceTotal260 + productNum_260.getAssemblyBalance();
            assemblyBalanceTotal340 = assemblyBalanceTotal340 + productNum_340.getAssemblyBalance();
            assemblyBalanceTotal370 = assemblyBalanceTotal370 + productNum_370.getAssemblyBalance();
            activationNumTotal260 = activationNumTotal260 + productNum_260.getActivationNum();
            activationNumTotal340 = activationNumTotal340 + productNum_340.getActivationNum();
            activationNumTotal370 = activationNumTotal370 + productNum_370.getActivationNum();
            activationBalanceTotal260 = activationBalanceTotal260 + productNum_260.getActivationBalance();
            activationBalanceTotal340 = activationBalanceTotal340 + productNum_340.getActivationBalance();
            activationBalanceTotal370 = activationBalanceTotal370 + productNum_370.getActivationBalance();

        }


        if (assemblyNumTotal260 > 0 || assemblyBalanceTotal260 > 0 || activationNumTotal260 > 0 || activationBalanceTotal260 > 0) {
            productNumTotal_260.setModelName("260节电堆总量");
            productNumTotal_260.setAssemblyNum(assemblyNumTotal260);
            productNumTotal_260.setAssemblyBalance(assemblyBalanceTotal260);
            productNumTotal_260.setActivationNum(activationNumTotal260);
            productNumTotal_260.setActivationBalance(activationBalanceTotal260);
        }

        if (assemblyNumTotal340 > 0 || assemblyBalanceTotal340 > 0 || activationNumTotal340 > 0 || activationBalanceTotal340 > 0) {
            productNumTotal_340.setModelName("340节电堆总量");
            productNumTotal_340.setAssemblyNum(assemblyNumTotal340);
            productNumTotal_340.setAssemblyBalance(assemblyBalanceTotal340);
            productNumTotal_340.setActivationNum(activationNumTotal340);
            productNumTotal_340.setActivationBalance(activationBalanceTotal340);
        }

        if (assemblyNumTotal370 > 0 || assemblyBalanceTotal370 > 0 || activationNumTotal370 > 0 || activationBalanceTotal370 > 0) {
            productNumTotal_370.setModelName("370节电堆总量");
            productNumTotal_370.setAssemblyNum(assemblyNumTotal370);
            productNumTotal_370.setAssemblyBalance(assemblyBalanceTotal370);
            productNumTotal_370.setActivationNum(activationNumTotal370);
            productNumTotal_370.setActivationBalance(activationBalanceTotal370);
        }

        if (productNumTotal_260.getModelName() != null || productNumTotal_340.getModelName() != null || productNumTotal_370.getModelName() != null) {
            productNumTotal.setModelName("电堆总量");
            productNumTotal.setAssemblyNum(assemblyNumTotal260 + assemblyNumTotal340 + assemblyNumTotal370);
            productNumTotal.setAssemblyBalance(assemblyBalanceTotal260 + assemblyBalanceTotal340 + assemblyBalanceTotal370);
            productNumTotal.setActivationNum(activationNumTotal260 + activationNumTotal340 + activationNumTotal370);
            productNumTotal.setActivationBalance(activationBalanceTotal260 + activationBalanceTotal340 + activationBalanceTotal370);
        }
        productList.add(productNumTotal_260);
        productList.add(productNumTotal_340);
        productList.add(productNumTotal_370);
        productList.add(productNumTotal);
        product.put("productInfoList", productList);
        resultList.add(product);

        return resultList;
    }

    public ResponseEntity<Object> create(AdmspeechheadDto resources) {
        return new ResponseEntity<>(admspeechheadService.create(resources), HttpStatus.CREATED);
    }

    public ResponseEntity<Object> createInfo(AdmspeechentryDto resources) {
        return new ResponseEntity<>(admspeechentryService.create(resources), HttpStatus.CREATED);
    }

    public void update(AdmspeechheadDto resources) {
        admspeechheadService.update(resources);
    }

    public String utc2Local(String utcTime) throws ParseException {
        Date date;
        Calendar calendar = Calendar.getInstance();
        if (utcTime == null) {
            date = new Date();
            calendar.setTime(date);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            date = sdf.parse(utcTime);
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
        }
        //calendar.getTime() 返回的是Date类型，也可以使用calendar.getTimeInMillis()获取时间戳
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date time = calendar.getTime();
        return sdf.format(time);

    }

    /**
     * 时间转换
     *
     * @param timeStr
     * @return
     * @throws ParseException
     */
    public String timeFormat(String timeStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(timeStr);
        return sdf.format(date);
    }

    /**
     * 时间转换
     *
     * @param timeStr
     * @return
     * @throws ParseException
     */
    public long formatTime(String timeStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(timeStr);
            return date.getTime();
        } catch (ParseException e) {

        }
        return 0;
    }
}
