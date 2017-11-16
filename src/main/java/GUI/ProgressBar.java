/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

/**
 *
 * @author A.Y
 */
public class ProgressBar {

    Timer timer;

    public ProgressBar() {

    }

    public void progressBar(final JProgressBar bar, final JLabel label, int delay) {

        bar.setValue(0);

        label.setText("Connecting to database..");

        ActionListener listener = new ActionListener() {
            int counter = 0;

            @Override
            public void actionPerformed(ActionEvent ae) {

                bar.setForeground(Color.GRAY);
                counter++;
                bar.setValue(counter);

                if (counter == 20) {

                    label.setText("Receiving information..");

                } else if (counter == 40) {

                    label.setText("Sorting information..");

                } else if (counter == 60) {

                    label.setText("Creating Panels..");
                } else if (counter == 100) {

                    label.setText("");
                    counter = 0;
                    bar.setValue(0);
                    timer.stop();

                }
            }
        };

        timer = new Timer(delay, listener);
        timer.start();

    }

}
