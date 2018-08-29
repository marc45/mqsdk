package cn.com.startai.mqttsdk.utils;

import android.os.Build;

/**
 * @author Robin
 * @time 2016年11月23日
 */
public class ProductConsts {

    /**
     * 设备制造商
     */


    public static final String PRODUCT_wing_mbox203 = "wing_mbox203";
    public static final String PRODUCT_octopus_uarmmid = "octopus_uarmmid";
    public static final String PRODUCT_fiber_a31st = "fiber_a31st";
    public static final String PRODUCT_octopus_102 = "octopus_102";
    public static final String PRODUCT_astar_sc3812r = "astar_sc3812r"; //音箱
    public static final String PRODUCT_sugar_ref001 = "sugar_ref001";
    public static final String PRODUCT_rk_3368 = "rk3368_box"; //新5.1主板
    public static final String PRODUCT_octopus_t106 = "octopus_t106";//展示平板 未兼容

    public static boolean isAdaptation() {

        if (is_wing_mbox203()
                || is_octopus_uarmmid()
                || is_fiber_a31st()
                || is_rk_3368()
                || is_octopus_102()
                || is_octopus_t106()
                || is_astar_sc3812r()
                || is_sugar_ref001()) {

            return true;
        } else {
            return false;
        }

    }

    public static boolean is_octopus_t106(){
        return PRODUCT_octopus_t106.equals(getProduct());
    }

    public static String getProduct() {
        return Build.PRODUCT;
    }

    public static boolean is_wing_mbox203() {

        return PRODUCT_wing_mbox203.equals(getProduct());

    }

    public static boolean is_octopus_uarmmid() {

        return PRODUCT_octopus_uarmmid.equals(getProduct());

    }

    public static boolean is_fiber_a31st() {

        return PRODUCT_fiber_a31st.equals(getProduct());

    }

    public static boolean is_rk_3368() {

        return PRODUCT_rk_3368.equals(getProduct());

    }


    public static boolean is_octopus_102() {

        return getProduct().equals(PRODUCT_octopus_102);
    }

    public static boolean is_astar_sc3812r() {

        return getProduct().equals(PRODUCT_astar_sc3812r);

    }

    public static boolean is_sugar_ref001() {
        return getProduct().equals(PRODUCT_sugar_ref001);

    }

}
