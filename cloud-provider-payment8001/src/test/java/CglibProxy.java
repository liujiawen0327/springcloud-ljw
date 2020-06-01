import com.ljw.springcloud.entity.IpfCcmOriginPage;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Description:
 * @Author: Ljw
 * @Date: 2020/5/28.
 */
public class CglibProxy implements MethodInterceptor {
    private Object target; //需要代理的目标对象

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("cgLib动态代理，监听开始");
        Object invoke = method.invoke(target,objects);//方法执行，参数：target 目标对象 arr参数数组
        System.out.println("cgLib动态代理，监听结束");
        return invoke;
    }

    //定义获取代理对象的方法
    public Object getCgLibProxy(Object objectTarget){
        //为目标对象target赋值
        this.target = objectTarget;
        Enhancer enhancer = new Enhancer();
        //设置父类，因为CgLib是针对指定的类生成一个子类，所以需要指定父类
        enhancer.setSuperclass(objectTarget.getClass());
        enhancer.setCallback(this);
        Object result = enhancer.create();
        return result;
    }

    public static void main(String[] args) {
        CglibProxy cglibProxy = new CglibProxy();
        IpfCcmOriginPage ipfCcmOriginPage = (IpfCcmOriginPage) cglibProxy.getCgLibProxy(new IpfCcmOriginPage());
        System.out.println("-----------------");
    }
}
