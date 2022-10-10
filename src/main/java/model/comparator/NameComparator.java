package model.comparator;

import model.Term;
import java.util.Comparator;

public class NameComparator implements Comparator<Term> {

    @Override
    public int compare(Term term, Term t1) {
        return term.getName().compareTo(t1.getName());
    }
}
