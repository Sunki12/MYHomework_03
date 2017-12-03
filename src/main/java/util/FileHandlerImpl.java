package util;

import java.io.*;
import java.util.ArrayList;

import vo.StockInfo;
import vo.UserInterest;

public class FileHandlerImpl implements FileHandler {
    @Override
    public StockInfo[] getStockInfoFromFile(String filePath){
    	FileInputStream file;
    	InputStreamReader files=null;
		try
		{
			file=new FileInputStream(filePath);
			files=new InputStreamReader(file,"utf-8");
		}catch (IOException e) {
			e.printStackTrace();
		}
        BufferedReader br = new BufferedReader(files);
		ArrayList<StockInfo> list = new ArrayList<StockInfo>();
		String line;
		try {
			while((line=br.readLine())!=null) {
				String []node=line.split("\t");
				StockInfo record=new StockInfo();
				record.setId(node[0]);
				record.setTitle(node[1]);
				record.setAuthor(node[2]);
				record.setDate(node[3]);
				record.setLastUpdate(node[4]);
				record.setContent(node[5]);
				record.setAnswerAuthor(node[6]);
				record.setAnswer(node[7]);
				list.add(record);

			}
			br.close();
			files.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		StockInfo [] newdate=new StockInfo[list.size()];
		int i=0;
		while(i<list.size()) {
			newdate[i]=list.get(i);
			i++;
		}
		return newdate;
    }
    @Override
    public UserInterest[] getUserInterestFromFile(String filePath) {
    	UserInterest[] userInterests=new UserInterest[500];
        for(int i=0;i<500;i++){
            userInterests[i]=new UserInterest();
        }
        try {
            FileReader fileReader=new FileReader(filePath);
            int mark;
            int row=0;int column=0;
            while((mark=fileReader.read())!=1&&row<500){
                if(mark!=2){
                    userInterests[row].array[column]=mark;
                    column++;
                    if(column>=60){
                        row++;
                        column=0;
                    }
                }
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInterests;
    }

    /**
     * This function need write matrix to files
     *
     * @param matrix the matrix you calculate
     */
    @Override
    public void setCloseMatrix2File(double[][] matrix) {
    	StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<60;i++){
            for(int j=0;j<60;j++){
                stringBuilder.append(matrix[i][j]);
                if(j!=59)
                    stringBuilder.append("\t");
            }
            stringBuilder.append('\n');
        }
        BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(this.getClass().getClassLoader().getResource(".").getPath()+"CloseMatrix.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        //BufferedWriter writer=new BufferedWriter(new FileWriter("D:/CloseMatrix.txt"));
        try {
			writer.write(stringBuilder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }


    @Override
    public void setRecommend2File(double[][] recommend) {
    	
    	StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<500;i++){
            for(int j=0;j<10;j++){
                stringBuilder.append(recommend[i][j]);
                if(j!=9)
                    stringBuilder.append("\t");
            }
            stringBuilder.append('\n');
        }
        BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(this.getClass().getClassLoader().getResource(".").getPath()+"Recommend.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        //BufferedWriter writer=new BufferedWriter(new FileWriter("D:/Recommend.txt"));
        try {
			writer.write(stringBuilder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
