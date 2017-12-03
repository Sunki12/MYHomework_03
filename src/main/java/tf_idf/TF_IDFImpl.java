package tf_idf;

import javafx.util.Pair;
import util.StockSorter;
import vo.StockInfo;

import java.util.HashMap;
import java.util.List;

public class TF_IDFImpl implements TF_IDF {
    @Override
    public Pair<String, Double>[] getResult(List<String> words, StockInfo[] stockInfos)
    {
    	HashMap<String,Double> tf=TF(words);
    	HashMap<String,Double> idf=IDF(words,stockInfos);
    	Pair<String, Double>[] pair=new Pair[tf.size()];
    	for (int i=0;i<words.size()-1;i++)
		{
			for(int j=i+1;j<words.size();j++)
			{
				if(words.get(i).equals(words.get(j)))
				{
					words.set(j," ");
				}
			}
		}
    	for(int i=0;i<tf.size();i++) {
         if(!(words.get(i).equals(" "))) {
	         double A = tf.get(words.get(i));
	         double B = idf.get(words.get(i));
	         double result = A * B;
	         pair[i] = new Pair(words.get(i), result);
         }
         else
	     {
	     	pair[i]=new Pair(" ",0.0);
	     }
    	}
    	return pair;
    }

    private HashMap<String,Double> TF(List<String> words)
    {
    	HashMap<String,Double> tf=new HashMap<String,Double>();
    	double count=1;

    	for(int i=0;i<words.size();i++)
    	{
    		count=1;
    		for(int j=0;j<words.size();j++)
    		{
    			if(words.get(i)==words.get(j)&&i!=j)
    			{
    				count++;
    			}
    		}
    		double Frequency=count/words.size();
    		tf.put(words.get(i),Frequency );
    	}
    	return tf;
    }

    private HashMap<String,Double> IDF(List<String> words, StockInfo[] stockInfos)
    {
    	HashMap<String,Double> idf=new HashMap<String,Double>();
		for (String word : words) {
			double count = 0;
			for (int j = 1; j < stockInfos.length; j++) {
				if (stockInfos[j].getId().contains(word)
						|| stockInfos[j].getTitle().contains(word)
						|| stockInfos[j].getAuthor().contains(word)
						|| stockInfos[j].getDate().contains(word)
						|| stockInfos[j].getLastUpdate().contains(word)
						|| stockInfos[j].getContent().contains(word)
						|| stockInfos[j].getAnswerAuthor().contains(word)
						|| stockInfos[j].getAnswer().contains(word)) {
					count++;
				}
			}
			double frequency = Math.log((double) (stockInfos.length - 1) / (count + 1));
			idf.put(word, frequency);
		}
        return idf;
    }
}


    
    
    
    
    
    
    
    
    
    
    