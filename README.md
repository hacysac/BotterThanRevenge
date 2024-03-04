# Botter Than Revenge

2024 Team 1515 Robot Code

## Setup
1. Setup buttons in Control
2. Configure IDs in RobotMap
3. Set steering offsets in SwerveConstants (follow instructions below)

## Driver button lists 

### First driver button list:
| Action| Button |
| ------------- | ------------- |
| Drive | Left stick |
| Turn | Right stick |
| Drive robot oriented | LB |
| Sniper mode | LT |
| Reset gyro | Back |
| Flip drive direction | Y |
| Rotate angle target | B |
| Raise climbers | UP |
| Lower climbers | DOWN |
| Lower left climber | LEFT |
| Lower right climber | RIGHT |

### Second Driver Buttons
| Action| Button |
| ------------- | ------------- |
| Intake | RB |
| Outtake | LB |
| Index forward | RT |
| Index back | LT |
| Shoot speaker | X |
| Shoot amp | B |
| Shooter backward | A |
| Auto intake | Y |
| Intake up | UP |
| Intake down | DOWN | 

## Setting up module offsets

> Before setting up module offsets ensure each offset is set to `Rotation2d.fromDegrees(0.0)` and that code is deployed to the
> robot. This must be done each time offsets are determined.
1. Either turn the robot on its side or put it up on a cart with the wheels exposed.

> By default, module sensor information is displayed in the `Drivetrain` tab on ShuffleBoard.
2. Rotate each module so the bevel gear on the sides of each wheel are pointing to the robot's left (intake as front).
> When aligning the wheels they must be as straight as possible. It is recommended to use a long straight edge such as
> a piece of 2x1 in order to make the wheels straight.
3. Record the angles of each module using the reading displayed on the dashboard.

4. Set the values of each module's `angleOffset` in `SwerveConstants` to `Rotation2d.fromDegrees(<the angle you recorded>)`
5. Re-deploy and try to drive the robot forwards. All wheels should stay parallel to each other.
6. Make sure all the wheels are spinning in the correct direction. If not, add 180 degrees to the offset of each wheel 
that is spinning in the incorrect direction. (I.e. `Rotation2d.fromDegrees(<angle> + 180.0))`)
