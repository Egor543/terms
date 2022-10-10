package view;

import model.Term;
import model.comparator.NameComparator;
import model.comparator.PageComparator;
import repository.TermRepository;

import java.util.List;
import java.util.Scanner;

public class TermPresenter {

    private final TermRepository termRepository;
    private static final String ask;

    private Scanner in;

    static {
        ask = """
                1. Добавить термин
                2. Отредактировать термин
                3. Удалить термин
                4. Вывести термины
                5. Поиск термина(ов)
                6. Отсортировать термины по алфавиту
                7. Отсортировать термины по номеру страниц
                8. Завершить работу
                """;
    }

    public TermPresenter(TermRepository termRepository, Scanner in) {
        this.termRepository = termRepository;
        this.in = in;
    }

    public void askForActivities() {
        System.out.println(ask);

        int choiceIndex = in.nextInt() - 1;
        if (choiceIndex > ActionType.values().length - 1) {
            System.out.println("Такого варианта нет");
            askForActivities();
        }
        ActionType choice = ActionType.values()[choiceIndex];
        in.nextLine();

        switch (choice) {
            case SORT_ALPHABETICALLY -> {
                termRepository.sort(termRepository.findAll(), new NameComparator());
            }
            case SORT_BY_PAGES -> {
                termRepository.sort(termRepository.findAll(), new PageComparator());
            }
            case EDIT_TERM -> {
                editTerm();
            }
            case FIND_TERM -> {
                findTerm();
            }
            case PRINT_TERMS -> {
                printTerms(termRepository.findAll());
            }
            case ADD_TERM -> {
                addTerm();
            }
            case REMOVE_TERM -> {
                removeTerm();
            }
            case EXIT -> {
                System.out.println("bye.");
                return;
            }
            default -> {
                System.out.println("некорректный ввод");
            }
        }
        askForActivities();
    }

    private void addTerm() {
        System.out.println("Введите имя родительского термина. Нажмите enter, если термин не будет подтермином");
        String name = in.nextLine();
        if (!name.isBlank()) {
            List<Term> terms = termRepository.findTermsByName(name);
            Term term = checkUserTerms(terms);
            if (term != null)
                term.addSubTerm(constructTerm(term));
        } else {
            termRepository.save(constructTerm(null));
        }

    }

    public void printTerms(List<Term> terms) {
        for (Term term : terms) {
            System.out.println(term);
        }
    }

    public void printInnerTerms(List<Term> terms) {
        for (Term term : terms) {
            if (term.getSuperTerm() != null) {
                System.out.println("Найден термин \"" + term.getName() + "\"" + ", находящийся в термине:");
                System.out.println(term.getSuperTerm());
            } else {
                System.out.println("Найден термин \"" + term.getName() + "\":");
                System.out.println(term);
            }
        }
    }

    public void printTerm(Term term) {
        System.out.println(term);
    }

    private void findTerm() {
        List<Term> terms = askForTerms();
        if (terms.size() > 0) {
            printInnerTerms(terms);
        } else {
            System.out.println("Терминов не найдено");
        }
    }

    private void editTerm() {
        editTerm(getTermsFromUser());
    }


    private Term getTermsFromUser() {
        List<Term> terms = askForTerms();
        return checkUserTerms(terms);
    }

    private void removeTerm() {
        Term term;
        if ((term = getTermsFromUser()) != null)
            termRepository.removeTermById(term.getId());
    }


    private Term constructTerm(Term superTerm) {
        System.out.println("Введите имя термина");
        String termName = in.nextLine();
        System.out.println("Введите страницы (через пробел)");
        List<String> pages = List.of(in.nextLine().split(" "));
        Term term = new Term(termName, superTerm);
        term.setPages(pages);
        return term;
    }


    private List<Term> askForTerms() {
        System.out.println("Введите имя термина");
        String name = in.nextLine();
        return termRepository.findTermsByName(name);
    }

    private void editTerm(Term term) {
        if (term == null) {
            return;
        }
        System.out.println("Что необходимо изменить?");
        String ask = """
                1. Название
                2. Страницы
                """;
        System.out.println(ask);
        int choice = in.nextInt();
        in.nextLine();
        if (choice == 1) {
            System.out.println("Введите новое имя");
            String name = in.nextLine();
            term.setName(name);
        } else if (choice == 2) {
            System.out.println("Введите новые страницы (через пробел)");
            List<String> pages = List.of(in.nextLine().split(", "));
            term.setPages(pages);
        } else {
            System.out.println("чел ти кринжжж");
        }
    }

    private Term checkUserTerms(List<Term> terms) {
        if (terms.size() == 0) {
            System.out.println("Терминов не найдено");
        } else if (terms.size() == 1) {
            System.out.println("Термин найден: ");
            printTerms(terms);
            return terms.get(0);
        } else {
            System.out.println("Найдено несколько терминов. В каком из терминов находится нужный подтермин?");
            for (int i = 0; i < terms.size(); i++) {
                System.out.println((i + 1) + ". ");
                if (terms.get(i).getSuperTerm() == null)
                    printTerm(terms.get(i));
                else
                    printTerm(terms.get(i).getSuperTerm());
            }
            int termNumber = in.nextInt() - 1;
            in.nextLine();
            if (termNumber >= 0 && termNumber < terms.size()) {
                return terms.get(termNumber);
            } else {
                System.out.println("Некорректный ввод");
                return null;
            }
        }
        return null;
    }
}
