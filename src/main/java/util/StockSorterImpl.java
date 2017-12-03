package util;

import javafx.util.Pair;
import vo.StockInfo;

public class StockSorterImpl implements StockSorter {
    public Pair<String, Double>[] sort(Pair<String, Double>[] pair) {
		for (int i = 0; i < pair.length - 1; i++) {
			for (int j = i + 1; j < pair.length; j++) {
				if (pair[i].getValue() < pair[j].getValue()) {
					Pair<String, Double> m = pair[i];
					pair[i] = pair[j];
					pair[j] = m;
				}
			}
		}
		return pair;
	}
}