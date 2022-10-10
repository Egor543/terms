package repository;

import model.Term;

import java.util.*;

public class TermRepository {
    private static List<Term> terms = new ArrayList<>();
    private static Map<Long, Term> map = new HashMap<>();
    private List<Term> foundTerms;

    public List<Term> findTermsByName(String name) {
        foundTerms = new ArrayList<>();
        findTermsByNameHelper(terms, name);

        return foundTerms;
    }

    private void findTermsByNameHelper(List<Term> terms, String name) {
        for (Term term : terms) {
            findTermsByNameHelper(term.getNestedTerms(), name);
            if (term.getName().equals(name)) {
                foundTerms.add(term);
            }
        }
    }

    public List<Term> findAll() {
        return terms;
    }

    public void sort(List<Term> terms, Comparator<Term> termComparator) {
        Collections.sort(terms, termComparator);
        for (int i = 0; i < terms.size(); i++) {
            sort(terms.get(i).getNestedTerms(), termComparator);
        }
    }

    public Term save(Term superTerm, Term termToSave) {
        superTerm.addSubTerm(termToSave);
        return termToSave;
    }

    public Term save(Term termToSave) {
        terms.add(termToSave);
        map.put(termToSave.getId(), termToSave);
        return termToSave;
    }

    public void removeTermById(Long id) {
        removeTermHelper(terms, id);
    }

    private void removeTermHelper(List<Term> terms, Long id) {
        Long foundId = 0l;
        Term t = null;
        for (Term term : terms) {
            if (term.getId() == id) {
                t = term;
                foundId = id;
                break;
            }
            removeTermHelper(term.getNestedTerms(), id);
        }
        if (foundId != 0)
            terms.remove(t);
            //terms = terms.stream().filter(t -> !t.getId().equals(id)).toList();
    }
}
