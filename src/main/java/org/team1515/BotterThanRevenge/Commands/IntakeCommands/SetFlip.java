// package org.team1515.BotterThanRevenge.Commands.IntakeCommands;

// import edu.wpi.first.wpilibj2.command.Command;

// import org.team1515.BotterThanRevenge.Subsystems.Flip;
// import org.team1515.BotterThanRevenge.Subsystems.Intake;

// public class SetFlip extends Command {
//     private final Flip flip; 

//     public SetFlip(Flip flip) {
//         this.flip = flip;
//         addRequirements(flip);
//     }

//     @Override
//     public void execute() {
//         if (flip.canCoderDown()){
//             flip.setFlipUp();
//         }
//         else{
//             flip.setFlipDown();
//         }
//     }

//     @Override
//     public void end(boolean interrupted) {
//         flip.end();
//     }
// }
