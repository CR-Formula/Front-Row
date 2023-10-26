package org.main;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import java.awt.*;
//import java.awt.Dimension;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MainTwo {

    private static JComboBox listA;
    public static void main(String[] args) throws InterruptedException {


        // TODO: Optimize the main and adding graphs
        listA = new JComboBox<>(new String[]{"ListONe", "ListTwo"});
//        listA.setBounds(10, 10, 50, 10);
//        listA.setPreferredSize(new Dimension(1, 25));
        listA.addActionListener(event -> {
            System.out.println(listA.getSelectedItem());
        });



        final JFrame frame = new JFrame("Drawing");

//        listA.setPrototypeDisplayValue("My text");

        listA.setMaximumSize(listA.getPreferredSize());

        frame.add(listA, BorderLayout.SOUTH);


        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);



    }
}
