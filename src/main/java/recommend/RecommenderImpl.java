package recommend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import javafx.util.Pair;
import segmenter.ChineseSegmenterImpl;
import tf_idf.TF_IDFImpl;
import util.StockSorterImpl;
import vo.StockInfo;
import vo.UserInterest;

public class RecommenderImpl implements Recommender {

    /**
     * this function need to calculate stocks' content similarity,and return the similarity matrix
     *
     * @param stocks stock info
     * @return similarity matrix
     */
    @Override
    public double[][] calculateMatrix(StockInfo[] stocks) {
    	Set<String> expectedNature = new HashSet<String>() {{
            add("n");add("v");add("vd");add("vn");add("vf");
            add("vx");add("vi");add("vl");add("vg");
            add("nt");add("nz");add("nw");add("nl");
            add("ng");add("userDefine");add("wh");
        }};
        ChineseSegmenterImpl chinese=new ChineseSegmenterImpl();
		List<String> list=chinese.getWordsFromInput(stocks);
		List<String> list2=new ArrayList<>();
		TF_IDFImpl tf_idf=new TF_IDFImpl();
		Pair<String,Double>[] pair=tf_idf.getResult(list,stocks);
		StockSorterImpl stock=new StockSorterImpl();
		pair=stock.sort(pair);
		double[][]matrix2=new double[60][50];
		double[][]matrix=new double[60][60];
		for (int i=0;i<50;i++)
			list2.add(pair[i].getKey());
        for(int i=0;i<60;i++)
        {
        	String content = stocks[i+1].getContent();
        	Result result = ToAnalysis.parse(content);//分词
            List<Term> terms = result.getTerms();
            List<String> list3=new ArrayList<>();
            for(int n=0;n<terms.size();n++) {
         	   String word = terms.get(n).getName(); 
                String natureStr = terms.get(n).getNatureStr(); 
                if(expectedNature.contains(natureStr)) list3.add(word);
            }//分词完成
            for(int j=0;j<50;j++)
            {
            	int count=0;
            	for (int k=0;k<list3.size();k++)
            	{
					if (list3.get(k)==list2.get(j))
					{
						count++;
					}
            	}
            	matrix2[i][j]=count;
            }
        }//词频
        return matrix2;
    }

    /**
     * this function need to recommend the most possibility stock number
     *
     * @param matrix       similarity matrix
     * @param userInterest user interest
     * @return commend stock number
     */
    @Override
    public double[][] recommend(double[][] matrix, UserInterest[] userInterest) {
        //TODO: write your code here
    	double[][]judge=new double[500][60];
    	for(int i=0;i<500;i++)
    	{
    		for(int j=0;j<60;j++)
    		{
    			if (userInterest[i].array[j] == 1)
                    judge[i][j] = 0;
    			else
    			{
    				double m=0;
    				for(int n=0;n<60;n++)
    				{
    					if (userInterest[i].array[n] == 1) {
                            m += matrix[j][n];
                        }
    				}
    				judge[i][j]=m;
    			}
    		}
    	}
    	 double[][] recommend=new double[500][10];
    	 for(int i=0;i<500;i++)
    	 {
    		 for(int j=0;j<10;j++)
    		 {
    			 int m;
    		 for(m=0;m<60;m++)
    		 {
    			 recommend[i][j]=judge[i][0];
    			 if(judge[i][m]>recommend[i][j])
    			 {
    				 recommend[i][j]=judge[i][m];
    			 }
    		 }
    		 judge[i][m]=0;
    	 }
    }
    	 return recommend;
    }
    public List<String>  keys (StockInfo[] stocks){
        ChineseSegmenterImpl chinese=new ChineseSegmenterImpl();
		List<String> list=chinese.getWordsFromInput(stocks);
		List<String> list2=new ArrayList<>();
		TF_IDFImpl tf_idf=new TF_IDFImpl();
		Pair<String,Double>[] pair=tf_idf.getResult(list,stocks);
		StockSorterImpl stock=new StockSorterImpl();
		pair=stock.sort(pair);
		for (int i=0;i<50;i++)
			list2.add(pair[i].getKey());
		return list2;
    }
}

