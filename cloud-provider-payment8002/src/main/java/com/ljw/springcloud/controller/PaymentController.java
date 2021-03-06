package com.ljw.springcloud.controller;

import com.ljw.springcloud.entity.CommonResult;
import com.ljw.springcloud.entity.Payment;
import com.ljw.springcloud.service.IpfCcmOriginPageService;
import com.ljw.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {

    /**
     * 服务对象
     */
    @Resource
    private PaymentService paymentService;

    @Resource
    private IpfCcmOriginPageService ipfCcmOriginPageService;

    @Value("${server.port}")
    private String serverPort;

    /**
     * 关于服务的信息
     */
    @Resource
    private DiscoveryClient discoveryClient;

    @PostMapping(value = "/payment/create")
    public CommonResult create(Payment payment){
        int result = paymentService.create(payment);
        log.info("******插入结果"+result);
        if (result>0){
            return new CommonResult(200,"插入数据库成功,serverPort:"+serverPort,result);
        }else {
            return new CommonResult(444,"插入数据库失败,serverPort:"+serverPort);
        }
    }

    /**
     * @Author liujiawen
     * @Description //TODO
     * @Date 2020/4/7 12:27 
     * @param
     * @param id
     * @return 
     **/
    @GetMapping(value = "/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
        Payment payment = paymentService.getPaymentById(id);
        log.info("******查询结果"+payment);
        if (payment!=null){
            return new CommonResult(200,"查询成功,serverPort:"+serverPort,payment);
        }else {
            return new CommonResult(444,"没有对应记录,查询ID："+id);
        }
    }
    @GetMapping(value = "/payment/discovery")
    public Object discovery(){
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            log.info("******element"+element);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
        }
        return discoveryClient;
    }

    /**
     * 自定义负载均衡算法测试接口
     * @return
     */
    @GetMapping(value ="/payment/lb")
    public String getPaymentLB(){
        return serverPort;
    }


    /**
     * Feign超时演示
     */
    @GetMapping("/payment/feign/timeout")
    public String paymentFeignTimeout(){
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return serverPort;
    }

//    @RequestMapping(value = "/ipfCcmOriginPage/get")
//    public IpfCcmOriginPage getPage(@RequestBody String id){
//        return ipfCcmOriginPageService.getPaymentById(id);
//    }



}
