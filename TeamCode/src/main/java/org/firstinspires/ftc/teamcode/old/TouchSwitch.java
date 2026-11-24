package org.firstinspires.ftc.teamcode.old;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class TouchSwitch {

    private TouchSensor touchSw;

    public void init(HardwareMap hwMap, String device_name)
    {
        touchSw = hwMap.get(TouchSensor.class, device_name);
    }

    public boolean isLimitSwitchPressed(){
        return touchSw.isPressed();
    }

}
