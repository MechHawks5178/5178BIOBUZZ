package org.firstinspires.ftc.teamcode.MH;

public class MHUtils {

    public float avg(float[] nums) {
        int numlen = nums.length;
        float tot = 0;
        for (float num : nums) {
            tot += num;
        }
        tot /= numlen;
        return tot;
    }
    public static int boolToInt(boolean bool) {
        return bool ? 1 : 0;
    }

    public float getAngleDistance(float angle,float target) {
        return (target - angle + 180) % 360 - 180;
    }
    public static float map(float min_out, float max_out, float min_in, float max_in, float val_in) {
        return min_out + ((max_out-min_out) / (max_in - min_in)) * (val_in - min_in);
    }

}
