import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import recommend.RecommenderImpl;
import util.FileHandlerImpl;
import vo.StockInfo;

public class Main {
    private static StockInfo[] stockInfos=new StockInfo[100];
    private static List<String> keys;
    private static double[][] similar;//全局变量


    public static void main(String[] args) throws IOException {
        JFrame frame=new JFrame("关键词搜索");
        frame.setBounds(400,200,1000,900);
        frame.setLayout(null);
        JButton select=new JButton("选择文件");//按钮select
        frame.add(select);
        select.setBounds(160,30,100,30);
        //监听事件
        select.addActionListener(e -> {
            //写入文件选择器
            JFileChooser chooser=new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.showDialog(new JLabel(), "选择");
            File fileselect=chooser.getSelectedFile();
            String path = fileselect.getAbsolutePath();
            if(fileselect.isDirectory()){
                System.out.println("文件夹:"+fileselect.getAbsolutePath());
            }else if(fileselect.isFile()){
                System.out.println("文件:"+fileselect.getAbsolutePath());
            }
            //文件处理
            FileHandlerImpl fileHandler=new FileHandlerImpl();
            stockInfos =fileHandler.getStockInfoFromFile(path);
            //读取的文件的关键词矩阵
            RecommenderImpl recommend=new RecommenderImpl();
            similar=recommend.calculateMatrix(stockInfos);
            keys=recommend.keys(stockInfos);
            for(int i=0;i<keys.size();i++)
            {
                System.out.println(keys.get(i));
            }
        });

        final JTextField ss=new JTextField();
        frame.add(ss);
        ss.setBounds(40,30,100,30);
        final JTextArea show=new JTextArea();
        frame.add(show);
        JScrollPane scroll = new JScrollPane(show);
        scroll.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        frame.add(scroll);
        scroll.setBounds(40,80,800,500);
        JButton search=new JButton("搜索");
        frame.add(search);
        search.setBounds(280,30,100,30);
        search.addActionListener(e -> {
            Set<String> expectedNature = new HashSet<String>() {{
                add("n");add("v");add("vd");add("vn");add("vf");
                add("vx");add("vi");add("vl");add("vg");
                add("nl");add("nz");add("nw");add("nt");
                add("ng");add("userDefine");add("wh");
            }};
            List<String> listss=new ArrayList<String>();
            String str =ss.getText().trim() ;
            Result result = ToAnalysis.parse(str); //分词结果的一个封装，主要是一个List<Term>的terms

            List<Term> terms = result.getTerms(); //拿到terms
            for(int i=0; i<terms.size(); i++) {
                String word = terms.get(i).getName(); //拿到词
                String natureStr = terms.get(i).getNatureStr(); //拿到词性
                if(expectedNature.contains(natureStr)) {
                    listss.add(word);
                }
            }
            double []input=new double[100];
            for(int k=0;k<keys.size();k++) {
                double count=0;
                for(int l=0;l<listss.size();l++) {
                    if(keys.get(k).equals(listss.get(l)))
                    {
                        count++;
                    }
                }
                input[k]=count;
            }
            //以下计算输入文本和已有文件相似度
            double [] sim=new double [100];
            for(int i=0;i<60;i++)
            {
                double molecular=0;
                double denominator1=0;
                double denominator2=0;
                for(int k=0;k<50;k++)
                {
                    molecular=molecular+similar[i][k]*input[k];
                    denominator1=denominator1+similar[i][k]*similar[i][k];
                    denominator2=denominator2+input[k]*input[k];
                }
                double denominator=Math.sqrt(denominator2)*Math.sqrt(denominator1);
                sim[i]=molecular/denominator;
            }
            //输出最大的十个记录
            int count[] =new int[10] ;
            for(int i=0;i<10;i++)
            {
                int m;
                for(m=0;m<60;m++)
                {
                    double max=sim[0];
                    if(sim[m]>max)
                    {
                        max=sim[m];
                        count[i]=m;
                    }
                }
                sim[count[i]]=0;
            }
            show.setText("");
            for(int i=0;i<10;i++)
            {

                show.append(stockInfos[count[i]].getId()+" "+stockInfos[count[i]].getTitle()+" "+
                        stockInfos[count[i]].getAuthor()+" "+stockInfos[count[i]].getDate()+" "+stockInfos[count[i]].getLastUpdate()
                        +" "+stockInfos[count[i]].getContent()+" "+stockInfos[count[i]].getAnswerAuthor()+" "
                        +stockInfos[count[i]].getAnswer()+"\n");
            }
            Color[] colors = new Color[10];
            for (int i = 0; i < colors.length; i++) {
                colors[i] = new Color(
                        (new Double(Math.random() * 128)).intValue() + 128,
                        (new Double(Math.random() * 128)).intValue() + 128,
                        (new Double(Math.random() * 128)).intValue() + 128);
            }

            WordCloudBuilder.buildWordCouldByWords(500,500,4,20,10,keys,new Color(-1),"data.png",colors);
        });

        JButton pngshow=new JButton("生成图片");
        pngshow.setBounds(600,700,200,60);
        frame.add(pngshow);
        JFrame jframe2=new JFrame();
        jframe2.setBounds(100,100,500,500);
        JPanel panel=new JPanel();
        jframe2.add(panel);
        pngshow.addActionListener(e -> {
            JLabel l = new JLabel("");
            l.setIcon(new ImageIcon("data.png"));
            jframe2.getContentPane().add(l, BorderLayout.NORTH);
            jframe2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            jframe2.setVisible(true);
        });
        JButton save =new JButton("另存为");
        jframe2.add(save);
        select.setBounds(160,30,100,30);
        final ImageIcon icon = new ImageIcon("data.png");
        icon.setImage(icon.getImage().getScaledInstance(icon.getIconWidth(),
                icon.getIconHeight(), Image.SCALE_DEFAULT));
        save.addActionListener(e -> {
            JFileChooser fileChooser=new JFileChooser();
            //后缀名过滤器
            FileNameExtensionFilter filter=new FileNameExtensionFilter("png图片","png");
            fileChooser.setFileFilter(filter);
            //保存
            int option=fileChooser.showSaveDialog(null);
            if(option==JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String fileName = fileChooser.getName(file);

                if (!fileName.contains(".png")) {
                    file = new File(fileChooser.getCurrentDirectory(), fileName + ".png");
                } else file = new File(fileChooser.getCurrentDirectory(), fileName);
                Image image = icon.getImage();
                BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = bi.createGraphics();
                g2d.drawImage(image, 0, 0, null);
                g2d.dispose();
                try {
                    ImageIO.write(bi, "png", file);
                } catch (IOException ee) {
                    // TODO Auto-generated catch block
                    ee.printStackTrace();
                }
            }
        });


        frame.setVisible(true);//设置窗体可见
    }

}

