package com.zt.googleplay.fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: ZT on 2016/12/27
 *
 * 注：由java语法可知，创建一个子类对象必然创建一个父类对象,其子类的构造方法里会默认调用super()
 *
 */
public class   FragmentFactory {

    //程序退出后,若不手动清除程序，下次进入时程序时。静态变量map里的fragmet任然存在于内存【注：static才有，非static则否】
    // 故：可以直接从map里获取fragment从而避免了再次new对象。
    // 优点：节约了内存，性能的优化，节约了用户流量，加快了数据加载速度良好的用户体验
    private static Map<Integer, BaseFragment> map = new HashMap<>();

    public static BaseFragment getFragment(int position){

        BaseFragment fragment = map.get(position);

        if(fragment==null){
            switch (position){
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new AppFragment();
                    break;
                case 2:
                    fragment = new GameFragment();
                    break;
                case 3:
                    fragment = new ZhuantiFragment();
                    break;
                case 4:
                    fragment = new TueijianFragment();
                    break;
                case 5:
                    fragment = new CategoryFragment();
                    break;
                case 6:
                    fragment = new PaiHanFragment();
                    break;
            }
            map.put(position, fragment);
        }
        return fragment;
    }
}
