package segmenter;


import vo.StockInfo;

import java.util.List;
import java.util.ArrayList;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;


import java.util.*;


public class ChineseSegmenterImpl implements ChineseSegmenter {
    @Override
    public List<String> getWordsFromInput(StockInfo[] newdate) {
    	List<String> list=new ArrayList<String>();
		Set<String> expectedNature = new HashSet<String>() {{
            add("n");add("v");add("vd");add("vn");add("vf");
            add("vl");add("vi");add("vx");add("vg");
            add("nt");add("nz");add("nw");add("nl");
            add("ng");add("userDefine");add("wh");
        }};
        for(int m=1;m<newdate.length;m++)
        {
           String str=newdate[m].getAnswer();
           Result result = ToAnalysis.parse(str);
           List<Term> terms = result.getTerms();
            for (Term term : terms) {
                String word = term.getName();
                String natureStr = term.getNatureStr();
                if (expectedNature.contains(natureStr)) list.add(word);
            }
        }
        for(int m=1;m<newdate.length;m++)
        {
           String str=newdate[m].getContent();
           Result result = ToAnalysis.parse(str);
           List<Term> terms = result.getTerms();
            for (Term term : terms) {
                String word = term.getName();
                String natureStr = term.getNatureStr();
                if (expectedNature.contains(natureStr)) list.add(word);
            }
        }
        for(int m=1;m<newdate.length;m++)
        {
           String str=newdate[m].getTitle();
           Result result = ToAnalysis.parse(str);
           List<Term> terms = result.getTerms();
            for (Term term : terms) {
                String word = term.getName();
                String natureStr = term.getNatureStr();
                if (expectedNature.contains(natureStr)) list.add(word);
            }
        }
        return list;
    }
}
