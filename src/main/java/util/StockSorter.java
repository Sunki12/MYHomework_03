package util;

import javafx.util.Pair;
import vo.StockInfo;


public interface StockSorter {

	Pair<String, Double>[] sort(Pair<String, Double>[] pair);

}
