/**
 * Where Is My Mouse?
 * Author: Royal Nason <http://inic.io>
 * 2013
 */

package io.inic;

import it.sauronsoftware.ftp4j.*;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Main extends JFrame
{
    static String FTP_URL = "URL";
    static String FTP_USER = "USERNAME";
    static String FTP_PWRD = "PASSWORD";
    static String JSON_DIR = "DIRECTORY";

    FTPClient client;
    boolean tracking = true;

    public Main()
    {
        super("Where Is My Mouse?");
        connectToFTP();

        // Set up view
        JPanel p = new JPanel(new BorderLayout());
        final JButton exit = new JButton("Tracking");
        p.add(exit);

        exit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent)
            {
                tracking = !tracking;
                if (tracking)
                {
                    exit.setText("Tracking");
                } else
                {
                    exit.setText("Not Tracking");
                }
            }
        });

        Container content = getContentPane();
        content.setBackground(Color.lightGray);
        content.add(p);
        setVisible(true);

        pack();

        new Mouse().start();

    }

    public static void main(String[] args)
    {
        Main main = new Main();
    }

    public void connectToFTP()
    {
        client = new FTPClient();
        try
        {
            client.connect(FTP_URL);
            client.login(FTP_USER, FTP_PWRD);
            client.changeDirectory(JSON_DIR);
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (FTPIllegalReplyException e)
        {
            e.printStackTrace();
        } catch (FTPException e)
        {
            e.printStackTrace();
        }
    }

    public void createJSON(int state)
    {
        Point mouse = MouseInfo.getPointerInfo().getLocation();

        JsonGenerator jg;
        JsonFactory jsonFactory = new JsonFactory();

        File file = new File("mouse.json");

        try
        {
            jg = jsonFactory.createJsonGenerator(file, JsonEncoding.UTF8);
            jg.writeStartObject();
            jg.writeNumberField("active", state);
            jg.writeNumberField("x", Math.round(mouse.getX()));
            jg.writeNumberField("y", Math.round(mouse.getY()));
            jg.writeEndObject();
            jg.close();
            client.upload(file);
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (FTPAbortedException e)
        {
            e.printStackTrace();
        } catch (FTPDataTransferException e)
        {
            e.printStackTrace();
        } catch (FTPException e)
        {
            e.printStackTrace();
        } catch (FTPIllegalReplyException e)
        {
            e.printStackTrace();
        }
    }

    class Mouse extends Thread
    {
        Point mouse;

        public Mouse()
        {
            super();
            mouse = MouseInfo.getPointerInfo().getLocation();
        }

        public void run()
        {
            if (tracking)
                createJSON(1); // active
            else
                createJSON(0);
            try
            {
                sleep(100);
                new Mouse().start();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
                new Mouse().start();
            }
        }
    }
}
