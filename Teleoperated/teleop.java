package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="ManualControl")
public class ManualControl extends OpMode {
    // Define all motors
    DcMotor rightback;
    DcMotor leftback;
    DcMotor rightfront;
    DcMotor leftfront;
    DcMotor armpivotpos;
    //Servo gripper;
    CRServo duckmotor;
    double curarm;
    int pivotgoal;
    boolean gripperengaged;
    double duck;
    // defined constants
    protected static final double GOBILDA = 537.7; // tick/rev of gobilda and tetrix dc's
    protected static final double TETRIX = 1440;
    protected static final double GRIPOPEN = 0; // values for opening and closing servos
    protected static final double GRIPCLOSED = 1;

    public double[] mecanum(x, y, rotation){

        double lf = 0; // Initial variables for power set to each wheel
        double lb = 0;
        double rf = 0;
        double rb = 0;

        // Strafing
        if (y == 0 || Math.abs(x/y) > 0.7){
            lf = x;
            rb = x;
            lb = -x;
            rf = -x;
        }
        // Driving Foward and Backwards
        else if (x == 0 || Math.abs(y/x) > 0.7){
            lf = y;
            rb = y;
            lb = y;
            rf = y;
        }

        // Compensate for Rotation
        lf += rotation;
        lb += rotation;
        rf -= rotation;
        rb -= rotation;

        // Finally, assign power values to motors.
        return [lf, lb, rf, rb]
    }

    public void init(){
        // Map all motors
        rightback = hardwareMap.dcMotor.get("rightback");
        leftback = hardwareMap.dcMotor.get("leftback");
        rightfront = hardwareMap.dcMotor.get("rightfront");
        leftfront = hardwareMap.dcMotor.get("leftfront");
        armpivotpos = hardwareMap.dcMotor.get("armpivotpos");
        duckmotor = hardwareMap.get(CRServo.class, "duckmotor");
        //gripper = hardwareMap.get(Servo.class, "gripper1");
        // Negate direction for opposite drive motors
        leftback.setDirection(DcMotorSimple.Direction.REVERSE);
        leftfront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightback.setDirection(DcMotorSimple.Direction.FORWARD);
        rightfront.setDirection(DcMotorSimple.Direction.FORWARD);
        // Initialise arm motors
        //gripper.setDirection(Servo.Direction.FORWARD);
        // Define Motor Values
        curarm = 0;
        gripperengaged = false;
    }

    public void loop(){
        //---------------------------------//
        //   Initialisation of variables   //
        //---------------------------------//
        double y = -gamepad1.left_stick_y; // Variable for f/b motion
        double x = gamepad1.left_stick_x; // Variable for r/l motion
        double rotation = gamepad1.right_stick_x; // Variable for rotation around center
        double manualarm = -gamepad2.left_stick_y; // manual arm mvt
        double duck = gamepad2.right_stick_x/6; // Variable for carousel
        

        //---------------------------------//
        //     Drive Train Calculations    //
        //---------------------------------//

        double[] motorval = mecanum(x, y, rotation)

        leftfront.setPower(motorval[1]);
        leftback.setPower(lmotorval[2]);
        rightfront.setPower(motorval[3]);
        rightback.setPower(motorval[4]);

        //---------------------------------//
        //             Arm Pivot           //
        //---------------------------------//

        // override to manual control
        if(Math.abs(manualarm) > 0.3){
            armpivotpos.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            armpivotpos.setPower(manualarm);
        }
        // button hotkeys
        else if(gamepad2.x ||gamepad2.a || gamepad2.y || gamepad2.b){
            //hotkeys : x = down, a = lvl1, b = lvl2, y = lvl3
            // set hotkey positions
            if(gamepad2.x){
                pivotgoal = 0; //add value
            }
            if(gamepad2.a){
                pivotgoal = 0; //add value
            }
            if(gamepad2.b){
                pivotgoal = 0; //add value
            }
            if(gamepad2.y){
                pivotgoal = 0; //add value
            }
            // program motors
            armpivotpos.setTargetPosition(pivotgoal);
            armpivotpos.setPower(0.75);
            armpivotpos.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        //---------------------------------//
        //       Gripper and Carousel      //
        //---------------------------------//

        // Engagement of Gripper
        if(gamepad2.right_bumper && gripperengaged == true){
            // Engage Gripper
            gripperengaged = false;
            //gripper.setPosition(GRIPOPEN);
        }
        else if(gamepad2.left_bumper && gripperengaged == false){
            // Disengage Gripper
            gripperengaged = true;
            //gripper.setPosition(GRIPCLOSED);
        }
    
        duckmotor.setPower(duck); //set carousel power
    }

    public void stop(){

    }
}