package model;

import java.util.ArrayList;
import java.util.List;

public class Term {
    private Long id;
    private String name;
    private int nestingLevel;


    private List<String> pages;
    private  List<Term> nestedTerms;

    private static Long idCounter = 0l;

    public Term getSuperTerm() {
        return superTerm;
    }

    private final Term superTerm;

    public Term(String name, Term superTerm) {
        id = ++idCounter;
        this.name = name;
        this.superTerm = superTerm;
        nestingLevel = 0;
        nestedTerms = new ArrayList<>();
        pages  = new ArrayList<>();
    }

    public void addSubTerm(Term term) {
        Term t = new Term(term.getName(), this);
        t.setNestingLevel(nestingLevel + 1);
        t.setPages(term.getPages());
        nestedTerms.add(t);
    }

    public List<Term> getNestedTerms() {
        return nestedTerms;
    }

    public void addPage(Integer page) {
        pages.add(page.toString());
    }

    public void addPage(String page) {
        pages.add(page);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setNestingLevel(int nestedLevel) {
        this.nestingLevel = nestedLevel;
    }

    public int getNestingLevel() {
        return nestingLevel;
    }

    public List<String> getPages() {
        return pages;
    }

    public void setPages(List<String> pages) {
        this.pages = List.copyOf(pages);
    }

    public String toString() {
        return "-".repeat(nestingLevel) + name + " " + String.join(", ", pages) + "\n" + String.join("", nestedTerms.stream().map(Term::toString).toArray(String[]::new));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
