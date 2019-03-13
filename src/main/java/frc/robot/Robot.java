/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
/*
          ______    __    __  .___________.   .______        ___       __       __
         /  __  \  |  |  |  | |           |   |   _  \      /   \     |  |     |  |
        |  |  |  | |  |  |  | `---|  |----`   |  |_)  |    /  ^  \    |  |     |  |
        |  |  |  | |  |  |  |     |  |        |   _  <    /  /_\  \   |  |     |  |
        |  `--'  | |  `--'  |     |  |        |  |_)  |  /  _____  \  |  `----.|  `----.
         \______/   \______/      |__|        |______/  /__/     \__\ |_______||_______|

    Credit: "2 hours" lmao
*/
package frc.robot;

import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.utils.Utils;
import com.explodingbacon.bcnlib.vision.Vision;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.vision.VisionThread;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    public static DriveSubsystem driveSubsystem;
    public static PanelSubsystem panelSubsystem;
    public static LiftSubsystem liftSubsystem;
    public static IntakeSubsystem intakeSubsystem;
    public static OutBallSubsystem outBallSubsystem;
    public static VisionThread vision;
    UsbCamera camera;
    MjpegServer server;
    public static boolean OutBall = false;
    public static Robot self;

    //Solenoid s;


    @Override
    public void robotInit() {
        /*camera = new UsbCamera("usb camera", 0);
        camera.setResolution(640,480);
        camera.setFPS(15);
        //CameraServer.getInstance().addCamera(camera);
        server = CameraServer.getInstance().addServer("serve_USB Camera 0",1182);

        server.setSource(camera);
        camera.setFPS(15);

        for(VideoProperty v : camera.enumerateProperties()){
            System.out.println("Property: " + v.getName());
        }
        System.out.println("Default compression: " + server.getProperty("default_compression").get());
        System.out.println("compression min: " + server.getProperty("default_compression").getMin());
        System.out.println("compression max: " + server.getProperty("default_compression").getMax());
        System.out.println("Server fps: " + server.getProperty("fps").get());
        server.getProperty("compression").set(50);
        */


        driveSubsystem = new DriveSubsystem();
        panelSubsystem = new PanelSubsystem();
        intakeSubsystem = new IntakeSubsystem();
        //liftSubsystem = new LiftSubsystem();
        outBallSubsystem = new OutBallSubsystem();


        /*Vision.init();
        vision = new VisionThread();

        vision.start();*/

        self = this;
    }

 /**
     * This function is called every robot packet, no matter the mode. Use
     * this for items like diagnostics that you want ran during disabled,
     * autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
     //System.out.println("Server fps: " + server.getProperty("fps").get());
        //System.out.println("Compression: " + server.getProperty("compression").get());

        //System.out.println("FPS: " + usbCamera.getActualFPS());
        //System.out.println("Data Rate: " + usbCamera.getActualDataRate());
    }

    @Override
    public void disabledPeriodic() {
        Log.d("Intake Encoder: " + intakeSubsystem.intakeEncoder.getCurrentPosition());
    }

    @Override
    public void autonomousInit() {
        OI.runCommand(new PanelCommand());
        OI.runCommand(new DriveCommand(this,vision));
        //OI.runCommand(new LiftCommand(this));
        OI.runCommand(new IntakeCommand(this));
        //OI.runCommand(new OutBallCommand(this));
    }

    @Override
    public void autonomousPeriodic() {
        double x = OI.driveController.getX2();
        double y = OI.driveController.getY();

        y = -y;

        x = Math.pow(Utils.deadzone(x, 0.1), 2) * Utils.sign(x);
        y = Math.pow(Utils.deadzone(y, 0.1), 2) * Utils.sign(y);

        driveSubsystem.left.set(y+x);
        driveSubsystem.right.set(y-x);
    }

    @Override
    public void teleopPeriodic() {
        double x = OI.driveController.getX2();
        double y = OI.driveController.getY();

        y = -y;

        x = Math.pow(Utils.deadzone(x, 0.1), 2) * Utils.sign(x);
        y = Math.pow(Utils.deadzone(y, 0.1), 2) * Utils.sign(y);

        driveSubsystem.left.set(y+x);
        driveSubsystem.right.set(y-x);
    }

    @Override
    public void testInit() {
     driveSubsystem.shift(false);
     driveSubsystem.left.testEachWait(0.5, 0.5);
     driveSubsystem.right.testEachWait(0.5, 0.5);
    }

    @Override
    public void testPeriodic() {
    }

    @Override
    public void teleopInit() {
        OI.deleteAllTriggers();
     OI.runCommand(new PanelCommand());
     OI.runCommand(new DriveCommand(this));
        //OI.runCommand(new LiftCommand(this));
        OI.runCommand(new IntakeCommand(this));
        //OI.runCommand(new OutBallCommand(this));
    }
}
