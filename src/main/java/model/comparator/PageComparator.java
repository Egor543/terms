package model.comparator;

import model.Term;

import java.util.Comparator;

public class PageComparator implements Comparator<Term> {
    @Override
    public int compare(Term term, Term t1) {
        Integer min1 = term.getPages().stream().mapToInt(Integer::parseInt).min().getAsInt();
        Integer min2 = t1.getPages().stream().mapToInt(Integer::parseInt).min().getAsInt();
        return  min1.compareTo(min2);
    }
}
