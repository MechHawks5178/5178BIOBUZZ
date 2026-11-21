package org.firstinspires.ftc.teamcode.MH;

public class MHUtils {

    public float avg(float[] nums) {
        int numlen = nums.length;
        float tot = 0;
        for (int i = 0; i < numlen; i++) {
            tot += nums[i];
        }
        tot /= numlen;
        return tot;
    }
    public static int boolToInt(boolean bool) {
        return bool ? 1 : 0;
    }
}
