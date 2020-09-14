package me.niotgg.output;

import sun.awt.image.ToolkitImage;

import javax.swing.*;
import java.awt.*;

public class OutputFrame extends JFrame {

    public OutputFrame(String text) {

        Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/check.png"));
        setIconImage(img);
        setSize(532, 278);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Tradução");


        JScrollPane jScrollPane = new JScrollPane();
        JTextArea jTextArea = new JTextArea();
        jTextArea.setEditable(true);
        jTextArea.setColumns(20);
        jTextArea.setRows(5);
        jTextArea.setText(text);

        jScrollPane.setViewportView(jTextArea);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                                .addContainerGap())
        );



        setVisible(true);



    }

}
