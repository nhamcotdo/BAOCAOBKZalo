package Main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.Base64;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;

public class GUI extends Thread {

  private JFrame homeForm, form1, newform;
  private static String name = null;
  private JEditorPane tpnMessage = new JEditorPane();
  private static Image imgicon = null;
  private static BufferedImage img = null;
  private static int imgCount = 0;
  private static Image imgclose = null;
  private static boolean imgsend = false;

  @Override
  public void run() {
    //receiver
    while (true) {
      tpnMessage.setContentType("text/html");
      HTMLDocument doc = (HTMLDocument) tpnMessage.getDocument();
      String txt = MulticastReceiver.Receiver();

      if (txt.contains("---imgstart---:")) {
    	  File outputfile = null;
        System.out.println("start anh");
        txt = txt.replace("---imgstart---:", "");
        // decode base64
        byte[] imageByte = Base64.getDecoder().decode(txt);
        // convert byte array back to image
        try {
          img = ImageIO.read(new ByteArrayInputStream(imageByte));
          // save img to ipg 
		  imgCount++;
		  outputfile = new File(".//src//img" + imgCount + ".jpg");
		  ImageIO.write(img, "jpg", outputfile);  
        } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
        System.out.println(outputfile.getAbsolutePath());
        // add image to tpnMessage
        try {
          doc.insertAfterEnd(
            doc.getCharacterElement(doc.getLength()),
			"<img  width='100px' height='100px' src='file:///" + outputfile.getAbsolutePath()+ "'><br>"
          );
        } catch (BadLocationException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        System.out.println("end anh");
      } else {
        try {
          doc.insertAfterEnd(
            doc.getCharacterElement(doc.getLength()),
            "<b>" + txt + "<br></b>"
          );
        } catch (BadLocationException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      System.out.println(tpnMessage.getText());
    }
    
  }

  public static void main(String[] args) throws Exception {
    new GUI();
  }

  public GUI() throws Exception {
    Home();
  }

  private void Form1() {
    form1 = new JFrame("BK ZALO - " + name);
    form1.setSize(500, 400);
    form1.setDefaultCloseOperation(3);
    form1.setLayout(new BorderLayout());

    // buttondelimg click
    JButton btnDelImg = new JButton();
    try {
      imgclose = ImageIO.read(new File(".//src//closeimg.png"));
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    imgclose = imgclose.getScaledInstance(5, 10, java.awt.Image.SCALE_SMOOTH);
    btnDelImg.setIcon(new ImageIcon(imgclose));
    //	btnDelImg.setBounds(0, 0, 5, 5);
    btnDelImg.setVisible(false);

    JPanel pnHeader = new JPanel();
    pnHeader.setLayout(new BoxLayout(pnHeader, BoxLayout.X_AXIS));

    JLabel lb = new JLabel("Nhập tin nhắn: ");

    JButton btnChooseImg = new JButton();

    try {
      imgicon = ImageIO.read(new File(".//src//imgicon.png"));
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    imgicon = imgicon.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
    btnChooseImg.setIcon(new ImageIcon(imgicon));
    // buttondelimg click
    btnDelImg.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          btnChooseImg.setIcon(new ImageIcon(imgicon));
          btnDelImg.setVisible(false);
          imgsend = false;
        }
      }
    );

    // add btndel in left corner of btnChooseImg
    btnChooseImg.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          JFileChooser fileChooser = new JFileChooser();
          fileChooser.setCurrentDirectory(
            new File(System.getProperty("user.home") + "/Desktop")
          );
		  fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png"));
          int result = fileChooser.showOpenDialog(form1.getParent());
          if (result == JFileChooser.APPROVE_OPTION) {
            try {
              File file = fileChooser.getSelectedFile();
              img = ImageIO.read(file);
              btnDelImg.setVisible(true);
              Image imgtemp = img.getScaledInstance(
                150,
                150,
                java.awt.Image.SCALE_SMOOTH
                
              );
              imgsend = true;
              btnChooseImg.setIcon(new ImageIcon(imgtemp));
              img = (BufferedImage) imgtemp;
              btnDelImg.setVisible(true);
            } catch (Exception e1) {
              // TODO: handle exception
            }
          }
        }
      }
    );

    JTextField tfMessage = new JTextField();

    tfMessage.addKeyListener(
      new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            MulticastSender.Send(name + ": " + tfMessage.getText());
            tfMessage.setText("");
          }
        }
      }
    );
    // send message
    JButton btnSend = new JButton("Gửi");
    btnSend.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          if (tfMessage.getText() != null) {
            MulticastSender.Send(name + ": " + tfMessage.getText());
            tfMessage.setText(null);
          }
          if (imgsend) {
            // img to string
            String imgString = "";
            try {
              ByteArrayOutputStream baos = new ByteArrayOutputStream();
              ImageIO.write(img, "jpg", baos);
              baos.flush();
              imgString =
                Base64.getEncoder().encodeToString(baos.toByteArray());
              baos.close();
            } catch (IOException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            }
            MulticastSender.Send(name + ": ");
            System.out.println(imgString.length());
            try {
              sleep(100);
            } catch (InterruptedException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            }
            MulticastSender.Send("---imgstart---:");
            while (imgString.length() > 0) {
              if (imgString.length() > 1024) {
                MulticastSender.Send(imgString.substring(0, 1024));
                imgString = imgString.substring(1024);
              } else {
                MulticastSender.Send(imgString);
                imgString = "";
              }
            }
            MulticastSender.Send("---imgend---:");

            btnDelImg.setVisible(false);
            btnChooseImg.setIcon(new ImageIcon(imgicon));
            imgsend = false;
          }
        }
      }
    );

    pnHeader.add(lb);
    pnHeader.add(btnChooseImg);
    pnHeader.add(btnDelImg);
    pnHeader.add(tfMessage);
    pnHeader.add(btnSend);

    form1.add(pnHeader, BorderLayout.SOUTH);

    tpnMessage.setEditable(false);
    JScrollPane scrollta = new JScrollPane(tpnMessage);
    form1.add(scrollta, BorderLayout.CENTER);
    form1.setVisible(true);
    start();
  }

  private void Home() {
    JPanel pnMain = new JPanel();

    JLabel lb = new JLabel("Enter yourname: ");
    lb.setBounds(0, 0, 100, 30);

    JTextField tfName = new JTextField();
    tfName.setBounds(110, 0, 190, 30);
    tfName.addKeyListener(
      new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            homeForm.dispose();
            name = tfName.getText();
            Form1();
          }
        }
      }
    );

    JButton btnOK = new JButton("OK");
    btnOK.setBounds(310, 0, 70, 30);
    btnOK.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          homeForm.dispose();
          name = tfName.getText();
          Form1();
        }
      }
    );

    //	pnMain.setSize(300, 30);
    pnMain.setLayout(null);

    pnMain.add(lb);
    pnMain.add(tfName);
    pnMain.add(btnOK);

    homeForm = new JFrame("Home");
    homeForm.setSize(440, 120);
    homeForm.setMaximizedBounds(null);
    homeForm.getRootPane().setBorder(new EmptyBorder(20, 20, 20, 20));
    homeForm.add(pnMain);
    homeForm.setDefaultCloseOperation(3);
    homeForm.setVisible(true);
  }
}
